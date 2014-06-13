/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataTypeList;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.IMapExtensibleModelPropertyDescriptor;

/**
 * @author gongyf
 *
 */
public class POIUtils {
	
	private static Logger logger = ConsoleHelper.getLogger();
	
	private static HSSFFont titleFont;
	private static HSSFCellStyle titleStyle;
	private static HSSFCellStyle textStyle;
	private static HSSFCellStyle cateStyle;
	
	/**
	 * ������������
	 * @author gongyf
	 *
	 */
	public interface IHeaderSorter {
		void sort(List<String> header);
	}
	
	public interface IAttributeHelper {
		String getValue(EObject model);
		void setValue(EObject model, String value);
	}
	
	public static class NormalAttributeHelper implements IAttributeHelper {
		public EStructuralFeature feature;

		/**
		 * @param feature
		 */
		public NormalAttributeHelper(EStructuralFeature feature) {
			super();
			this.feature = feature;
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IAttributeHelper#getValue(org.eclipse.emf.ecore.EObject)
		 */
		@Override
		public String getValue(EObject model) {
			return (String) convert(model.eGet(feature), String.class);
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IAttributeHelper#setValue(org.eclipse.emf.ecore.EObject, java.lang.String)
		 */
		@Override
		public void setValue(EObject model, String value) {
			model.eSet(feature, convert(value, feature.getEType().getInstanceClass()));
		}
		
	}
	
	public static class ExtensibleDataAttributeHelper implements IAttributeHelper {

		private String mapKey;
		
		
		/**
		 * @param mapKey
		 */
		public ExtensibleDataAttributeHelper(String mapKey) {
			super();
			this.mapKey = mapKey;
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IAttributeHelper#getValue(org.eclipse.emf.ecore.EObject)
		 */
		@Override
		public String getValue(EObject model) {
			return ((ExtensibleModel)model).getData().get(mapKey);
		}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IAttributeHelper#setValue(org.eclipse.emf.ecore.EObject, java.lang.String)
		 */
		@Override
		public void setValue(EObject model, String value) {
			((ExtensibleModel)model).getData().put(mapKey, value);
		}
	
		
	}
	
	public static class ExtensibleData2AttributeHelper implements IAttributeHelper {
		private String map2Key;
		private EStructuralFeature feature;
		
		/**
		 * @param map2Key
		 * @param feature
		 */
		public ExtensibleData2AttributeHelper(String map2Key,
				EStructuralFeature feature) {
			super();
			this.map2Key = map2Key;
			this.feature = feature;
		}

		@Override
		public String getValue(EObject model) {
			Object obj = ((ExtensibleModel)model).getData2().get(map2Key);
			if (obj instanceof EObject) {
				Object value = ((EObject) obj).eGet(feature);
				if (value instanceof String) {
					return (String) value;
				} else {
					return (String)convert(value, String.class);
				}
			}
			
			return StringUtils.EMPTY;
		}
		
		@Override
		public void setValue(EObject model, String value) {
			Object obj = ((ExtensibleModel)model).getData2().get(map2Key);
			if (obj instanceof EObject) {
				((EObject) obj).eSet(feature, convert(value, feature.getEType().getInstanceClass()));
			}
		}
	}
	
	public static class ExtensibleData2MapAttributeHelper implements IAttributeHelper {
		private String map2Key;
		private Object key;
		private EStructuralFeature feature;
		
		
		/**
		 * @param map2Key
		 * @param feature
		 */
		public ExtensibleData2MapAttributeHelper(String map2Key,
				EStructuralFeature feature, Object key) {
			super();
			this.map2Key = map2Key;
			this.feature = feature;
			this.key = key;
		}

		@Override
		public String getValue(EObject model) {
			Object obj = ((ExtensibleModel)model).getData2().get(map2Key);
			if (obj instanceof EObject) {
				EMap<Object, Object> map = (EMap<Object, Object>) ((EObject) obj).eGet(feature);
				return (String)convert(map.get(key), String.class);
			}
			
			return StringUtils.EMPTY;
		}
		
		@Override
		public void setValue(EObject model, String value) {
			Object obj = ((ExtensibleModel)model).getData2().get(map2Key);
			if (obj instanceof EObject) {
				EMap<Object, Object> map = (EMap<Object, Object>) ((EObject) obj).eGet(feature);
				map.put(key, convert(value, ((EClass)feature.getEType()).getEStructuralFeature("value").getEType().getInstanceClass()));
			}
		}
	}
	
	
	public static Object convert(Object value, Class<?> toClassType) {
		if (value == null) {
			if (String.class.isAssignableFrom(toClassType)) {
				return StringUtils.EMPTY;
			} else if (Boolean.class.isAssignableFrom(toClassType)) {
				return Boolean.FALSE;
			} else if (Integer.class.isAssignableFrom(toClassType)) {
				return 0;
			}
		} else {
			if (String.class.isAssignableFrom(toClassType)) {
				return value.toString();
			} else if (Boolean.class.isAssignableFrom(toClassType) || toClassType.getName().equals("boolean")) {
				return BooleanUtils.toBoolean(value.toString());
			} else if (Integer.class.isAssignableFrom(toClassType)) {
				return NumberUtils.toInt(value.toString());
			}
		}
		return value;
	}
	
	/**
	 * 
	 * ��ȡ������excel�����ݱ�
	 * 
	 * @param owner ����ģ��
	 * @param reference EMFģ��reference����
	 * @param itemClass EMFģ��eclass����
	 * @param titles excel����
	 * @param features item���Ե�reference����
	 * @param includeExtend �Ƿ񵼳���չ��
	 * @param titles2 ��չ�б���
	 * @param dataKeys ��չ��key
	 * @param element ��Դ
	 * @param sorter ����
	 * @return
	 */
	public static List< List<String> > exportExcelStringTable(EObject owner, EReference reference, EClass itemClass, String[] titles, EStructuralFeature[] features,
			boolean includeExtend, String[] titles2, String[] dataKeys, IARESElement element, IHeaderSorter sorter) {
		List<List<String>> result = new ArrayList<List<String>>();

		// ���ȹ���������
		List<String> header = new ArrayList<String>();
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		
		
		header.addAll(Arrays.asList(titles));
		for (int i = 0; i < titles.length; i++) {
			helperMap.put(titles[i], new NormalAttributeHelper(features[i]));
		}
		
		if (includeExtend) {
			
			for (int i = 0; i < titles2.length; i++) {
				header.add(titles2[i]);
				helperMap.put(titles2[i], new ExtensibleDataAttributeHelper(dataKeys[i]));
			}
			
			IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(element, itemClass);
			for (IExtensibleModelEditingSupport support : supports) {
				for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(element, itemClass)) {
					if (!desc.isDerived()) {
						header.add(desc.getDisplayName());
						if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
						} else {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
						}
					}
				}
			}
		}
		
		if (sorter != null) {
			sorter.sort(header);
		}
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		for (String title : header) {
			helperList.add(helperMap.get(title));
		}
		
		result.add(header);
		
		// ��ι�������
		List<EObject> contentObjectList = (List<EObject>) owner.eGet(reference);
		for (EObject eObject : contentObjectList) {
			List<String> content = new ArrayList<String>();
			for (IAttributeHelper helper : helperList) {
				content.add(helper.getValue(eObject));
			}
			
			result.add(content);
		}
		
		return result;
	}
	
	/**
	 * 
	 * ���ַ�������ת���ɶ�����������
	 * 
	 * @param table �ַ�������
	 * @param itemClass EMF����ĿEClass����
	 * @param titles ����
	 * @param features MF����Ŀfeatures����
	 * @param includeExtend �Ƿ������չ��Ϣ
	 * @param titles2 ��չ��Ϣ����
	 * @param dataKeys ��չ��Ϣ��key
	 * @param element ������Դ
	 * @return
	 */
	public static List<EObject> importExcelStringTable(List< List<String> > table, EClass itemClass, String[] titles, EStructuralFeature[] features, 
			boolean includeExtend, String[] titles2, String[] dataKeys, IARESElement element ) {
		
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		
		for (int i = 0; i < titles.length; i++) {
			helperMap.put(titles[i], new NormalAttributeHelper(features[i]));
		}
		
		if (includeExtend) {
			
			for (int i = 0; i < titles2.length; i++) {
				helperMap.put(titles2[i], new ExtensibleDataAttributeHelper(dataKeys[i]));
			}
			
			IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(element, itemClass);
			for (IExtensibleModelEditingSupport support : supports) {
				for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(element, itemClass)) {
					if (!desc.isDerived()) {
						if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
						} else {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
						}
					}
				}
			}
		}
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		List<EObject> result = new ArrayList<EObject>();
		if (table.size() == 0) {
			return result;
		}
		for (String title : table.get(0)) {
			helperList.add(helperMap.get(title));
		}
		
		for (int i = 1; i < table.size(); i++) {
			result.add(readObject(table.get(i), itemClass, helperList, includeExtend, element));
		}
		
		return result;
	}
	
	/**
	 * ����ģʽ�������������Ĵ���ŵ����ʽ
	 * 
	 * @param table
	 * @param itemClass
	 * @param titles
	 * @param features
	 * @param includeExtend
	 * @param titles2
	 * @param dataKeys
	 * @param element
	 * @return
	 */
	public static void importExcelStringTableForError(List< List<String> > table, EClass itemClass, String[] titles, EStructuralFeature[] features, 
			boolean includeExtend, String[] titles2, String[] dataKeys,MetadataResourceData ower, IARESElement element ) {
		
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		int cateIndex = -1;
		for (int i = 0; i < titles.length; i++) {
			if (MetadataPackage.Literals.NAMED_ELEMENT__NAME == features[i]) {
				cateIndex = i;
			}
			helperMap.put(titles[i], new NormalAttributeHelper(features[i]));
		}
		
		if (includeExtend) {
			
			for (int i = 0; i < titles2.length; i++) {
				helperMap.put(titles2[i], new ExtensibleDataAttributeHelper(dataKeys[i]));
			}
			
			IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(element, itemClass);
			for (IExtensibleModelEditingSupport support : supports) {
				for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(element, itemClass)) {
					if (!desc.isDerived()) {
						if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
						} else {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
						}
					}
				}
			}
		}
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		for (String title : table.get(0)) {
			helperList.add(helperMap.get(title));
		}
		
		MetadataCategory mc = null;
		for (int i = 1; i < table.size(); i++) {
			boolean con = true;
			for(String str : table.get(i)){
				if (StringUtils.isNotBlank(str)) {
					con = false;
				}
			}
			if (con) {
				continue;
			}
			if (isErrorNoCate(table.get(i))) {
				String[] cates = StringUtils.split(table.get(i).get(0), "/");
				mc = ower.getRoot();
				for (String cate : cates) {
					mc = createCate(cate, mc);
				}
				continue;
			}
			EObject obj = readObject(table.get(i), itemClass, helperList, includeExtend, element);
			ower.getItems().add(obj);
			if (mc != null) {
				mc.getItems().add((MetadataItem) obj);
			}
		}
	}
	
	/**
	 * ����ģʽ�������������Ĵ���ŵ����ʽ
	 * 
	 * @param table
	 * @param itemClass
	 * @param titles
	 * @param features
	 * @param includeExtend
	 * @param titles2
	 * @param dataKeys
	 * @param element
	 * @return
	 */
	public static void importExcelStringTableForSHClear(List< List<String> > table, EClass itemClass, String[] titles, EStructuralFeature[] features, 
			boolean includeExtend, String[] titles2, String[] dataKeys,MetadataResourceData ower, IARESElement element ) {
		
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		
		for (int i = 0; i < titles.length; i++) {
			helperMap.put(titles[i], new NormalAttributeHelper(features[i]));
		}
		
		if (includeExtend) {
			
			for (int i = 0; i < titles2.length; i++) {
				helperMap.put(titles2[i], new ExtensibleDataAttributeHelper(dataKeys[i]));
			}
			
			IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(element, itemClass);
			for (IExtensibleModelEditingSupport support : supports) {
				for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(element, itemClass)) {
					if (!desc.isDerived()) {
						if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
						} else {
							helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
						}
					}
				}
			}
		}
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		for (String title : table.get(0)) {
			helperList.add(helperMap.get(title));
		}
		
		MetadataCategory mc = null;
		for (int i = 1; i < table.size(); i++) {
			boolean con = true;
			for(String str : table.get(i)){
				if (StringUtils.isNotBlank(str)) {
					con = false;
				}
			}
			if (con) {
				continue;
			}
			if (isCate(table.get(i))) {
				String[] cates = StringUtils.split(table.get(i).get(0), "/");
				mc = ower.getRoot();
				for (String cate : cates) {
					mc = createCate(cate, mc);
				}
				continue;
			}
			EObject obj = readObject(table.get(i), itemClass, helperList, includeExtend, element);
			ower.getItems().add(obj);
			if (mc != null) {
				mc.getItems().add((MetadataItem) obj);
			}
		}
	}
	
	protected void getMetadataItemCategory1(MetadataResourceData ower ,
			List<EObject> items, Map<String, List<List<String>>> cateLineMap) {
		for(Iterator<String> it = cateLineMap.keySet().iterator();it.hasNext();){
			String key = it.next();
			List<List<String>> values = cateLineMap.get(key);
			String[] cates = StringUtils.split(key, "/");
			MetadataCategory mc = ower.getRoot();
			for (String cate : cates) {
				mc = createCate(cate, mc);
			}
			ower.setRoot(mc);
		}
	}
	
	private static MetadataCategory createCate(String cate ,MetadataCategory parient){
		MetadataCategory even = null;
		for (MetadataCategory mc : parient.getChildren()) {
			if (StringUtils.equals(mc.getName(), cate)) {
				even = mc;
				break;
			}
		}
		if (even == null) {
			even = MetadataFactory.eINSTANCE.createMetadataCategory();
			even.setName(cate);
			parient.getChildren().add(even);
		}
		return even;
	}
	
	/**
	 * �ж��ǲ��Ƿ��飬�����һ���Ƿǿգ�����Ϊ�գ�����Ϊ�Ƿ���
	 * 
	 * @param strs
	 * @return
	 */
	private static boolean isCate (List<String> strs){
		for (int i = 0; i < strs.size(); i++) {
			if (i == 0) {
				if (StringUtils.isBlank(strs.get(i))) {
					return false;
				}
			}else if (strs.get(i) != null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * �жϴ��׼������Ƿ��Ƿ���
	 * @param strs
	 * @return
	 */
	private static boolean isErrorNoCate (List<String> strs){
		for (int i = 0; i < strs.size(); i++) {
			if (i == 0) {
				if (StringUtils.isBlank(strs.get(i))) {
					return false;
				}
			}else if (StringUtils.isNotBlank(strs.get(i))){
				return false;
			}
		}
		return true;
	}
	public static EObject readObject(List<String> tableRow, EClass itemClass, List<IAttributeHelper> helperList, boolean includeExtend, IARESElement element) {
		EObject eObj = itemClass.getEPackage().getEFactoryInstance().create(itemClass);
		if (includeExtend && eObj instanceof ExtensibleModel) {
			ExtensibleModelUtils.extend(element, (ExtensibleModel) eObj, false);
		}
		for (int j = 0; j < helperList.size(); j++) {
			IAttributeHelper helper = helperList.get(j);
			if (helper != null) {
				if (j < tableRow.size())
					helper.setValue(eObj, tableRow.get(j));
			}
			
		}
		return eObj;
	}
	
	/**
	 * ��������Ķ�ά��Excel�ļ��У�map�� key->��ά��
	 * @param excelStream Excel�ļ������
	 * @param docTitle       �ĵ����⣨��һҳ�Ĵ���⣬���确��׼�ֶι淶�ĵ����������û�У�����Ϊnull
	 * @param tableMap map�� key->��ά��
	 * @param sheetNames
	 * @param startCols ��ʼ�У����飬��Ӧÿ��sheet��Ҫ��һ��
	 * @param startRows ��ʼ�У����飬��Ӧÿ��sheet��Ҫ��һ��
	 */
	public static void putExcelString(OutputStream excelStream, String docTitle, Map<String, List<List<String>>> tableMap,List<List<String>> revHises , String[] sheetNames, int[] startCols, int[] startRows) {
		try {
			init();
			InputStream is = MetadataUI.getDefault().getBundle().getEntry("template/metadataExportTemplate.xls").openStream();
			HSSFWorkbook wb = new HSSFWorkbook(is);
			
			if (docTitle != null) {
				// 2012-09-25 sundl ����docTitle�������޸ĵ�һҳ����ҳ�����
				HSSFSheet coverSheet = wb.getSheet("����");
				if (coverSheet != null) {
					HSSFRow titleRow = coverSheet.getRow(5);
					HSSFCell titleCell = titleRow.getCell(0);
					titleCell.setCellValue(docTitle);
				}
			}
			//�ڰ汾ҳ�У�������Դ���޶���Ϣ
			if (revHises != null && revHises.size() > 1) {
				HSSFSheet versionSheet = wb.getSheet("�汾ҳ");
				for (int i = 1; i < revHises.size(); i++) {
					List<String> items = revHises.get(i);
					HSSFRow row = versionSheet.createRow(11+i);
					for (int j = 0; j < items.size(); j++) {
						String cv = items.get(j);
						HSSFCell cell = row.getCell(j+1);
						if (cell == null) {
							cell = row.createCell(j+1);
							cell.setCellStyle(getTextStyle(wb));
						}
						cell.setCellValue(cv);
					}
				}
			}
			
			//��ʼ��sheet
			for (int i = 0; i < sheetNames.length; i++) {
				HSSFSheet sheet = wb.cloneSheet(2);
				wb.setSheetName(wb.getSheetIndex(sheet), sheetNames[i]);
			}
			wb.removeSheetAt(2);
			for (int i = 0; i < sheetNames.length; i++) {
				init();
				HSSFSheet sheet = wb.getSheet(sheetNames[i]);
				List<List<String>> sheetData = tableMap.get(sheetNames[i]);
				//˵����
				int descColumnIndex = -1;
				sheet.createFreezePane(0, startRows[i]+1);
				for (int j = 0; j < sheetData.size(); j++) {
					HSSFRow row = sheet.createRow(startRows[i]+j);
					List<String> data = sheetData.get(j);
					//����
					if (j == 0) {
						for (int k = 0; k < data.size(); k++) {
							HSSFCell title = row.createCell(k+startCols[i]);
							title.setCellValue(data.get(k));
							title.setCellStyle(getTitleStyle(wb));
							if ("˵��".equals(data.get(k))) {
								descColumnIndex = k+startCols[i];
							}
						}
					}else {
						//������
						boolean cateStatus = false;
						for (int k = 0; k < data.size(); k++) {
							String d = StringUtils.defaultString(data.get(k));
							if (k == 0) {
								if (StringUtils.isNotBlank(d)) {
									cateStatus = true;
								}
							}else {
								if (d != null) {
									cateStatus = false;
								}
							}
							HSSFCell cell = row.createCell(k+startCols[i]);
							if (d.length() > 32767) {
								d = StringUtils.substring(d, 0, 32767);
								logger.warn("sheet:[" +sheetNames[i] + "] ,λ�� ��[" + row.getRowNum()+1 +"��,"+ cell.getColumnIndex() +"��]�����ݳ�����Ԫ�����ָ�����ȣ�����ȡ!");
							}
							cell.setCellValue(d);
							cell.setCellStyle(getTextStyle(wb));
						}
						if (cateStatus) {
							for (int k = 0; k < data.size()-1; k++) {
								row.getCell(k + startCols[i]).setCellStyle(getCateStyle(wb));
							}
							sheet.addMergedRegion(new CellRangeAddress(startRows[i]+j, startRows[i]+j ,startCols[i], startCols[i] + data.size()-1));
						}
					}
				}
				setSheetWidth(sheet, startCols[i], sheetData.get(0).size());
				if (descColumnIndex > -1) {
					sheet.setColumnWidth(descColumnIndex, 10000);
				}
			}
			wb.write(excelStream);
			excelStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void init (){
		if (titleFont != null) {
			titleFont = null;
		}
		if (titleStyle != null) {
			titleStyle = null;
		}
		if (textStyle != null) {
			textStyle = null;
		}
		if (cateStyle != null) {
			cateStyle = null;
		}
	}
	
	/**
	 *�����п� 
	 *
	 * @param sheet
	 * @param startCol
	 * @param size
	 */
	private static void setSheetWidth (HSSFSheet sheet , int startCol , int size){
		for (int i = startCol; i < startCol+size; i++) {
			sheet.autoSizeColumn(i);
			if (sheet.getColumnWidth(i) > 10000) {
				sheet.setColumnWidth(i, 10000);
			}
		}
	}
	
	private static HSSFCellStyle getTitleStyle(HSSFWorkbook wb) {
		if (titleStyle == null) {
			titleStyle = wb.createCellStyle();
			titleStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			if (titleFont == null) {
				titleFont = wb.createFont();
				titleFont.setFontName("����");
				titleFont.setFontHeightInPoints((short) 10);
				titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			}
			titleStyle.setFont(titleFont);
		}
		return titleStyle;
	}
	
	/**
	 * �ı������ʽ
	 * 
	 * @param wb
	 * @return
	 */
	private static HSSFCellStyle getTextStyle(HSSFWorkbook wb) {
		if (textStyle == null) {
			textStyle = wb.createCellStyle();
			textStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			textStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			textStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			// 2012-09-11 sundl �ڿ�ͷ�пո������£������п�AutoWidth�ֲ��������������ݱ�������һ�У�Ĭ��״̬������.
			//textStyle.setWrapText(true);
		}
		return textStyle;
	}
	
	private static HSSFCellStyle getCateStyle(HSSFWorkbook wb) {
		if (cateStyle == null) {
			cateStyle = wb.createCellStyle();
			cateStyle.setFillForegroundColor(HSSFColor.TAN.index);
			cateStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cateStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cateStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cateStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cateStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cateStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			// 2012-09-11 sundl �ڿ�ͷ�пո������£������п�AutoWidth�ֲ��������������ݱ�������һ�У�Ĭ��״̬������.
			//textStyle.setWrapText(true);
		}
		return cateStyle;
	}
	
	/**
	 * ��Excel�ļ���ָ����
	 * 
	 * @param excelStream �ļ���
	 * @param sheetNames ��Ҫ�����sheet��������
	 * @param startCols sheetҳ�Ŀ�ʼ��
	 * @param startRows sheetҳ�Ŀ�ʼ��
	 * @return
	 * @throws Exception 
	 */
	public static Map< String, List< List<String> > > getExcelString(HSSFWorkbook workBook , String[] sheetNames, int[] startCols, int[] startRows) throws Exception {
		Map< String, List< List<String> > > sheetFieldMap = new HashMap<String, List<List<String>>>();
		
		if (sheetNames.length == startCols.length && startCols.length == startRows.length && startRows.length != 0) {
			try {
				
				HSSFFormulaEvaluator evaluator =  workBook.getCreationHelper().createFormulaEvaluator();
				
				for (int i=0;i<sheetNames.length;i++) {
					List<List<String>> fieldLists = new ArrayList<List<String>>();
					HSSFSheet sheet = workBook.getSheet(sheetNames[i]);
					if (sheet ==null) {
						continue;
					}
					List<String> titleField = getColumns(sheet, startCols[i],startRows[i], evaluator);
					fieldLists.add(titleField);
					for (int j = startRows[i]+1; j<sheet.getLastRowNum()+1;j++){
						HSSFRow row = sheet.getRow(j);
						if (row == null) {
							break;
						}
						int cellNum = startCols[i];
						List<String> fields = new ArrayList<String>();
						for (int k = 0; k < titleField.size(); k++) {
							HSSFCell cell = row.getCell(k + cellNum);
							fields.add(getCellStringValue(cell, evaluator).trim());
						}
						boolean isLast = true;
						for (String field : fields) {
							if (StringUtils.isNotBlank(field)) {
								isLast = false;
								break;
							}
						}
						if (!isLast) {
							fieldLists.add(fields);
						}
					}
					sheetFieldMap.put(sheetNames[i], fieldLists);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("�ļ������ڻ��ļ���ʽ����ȷ");
			}
		}
		return sheetFieldMap;
	}
	
	/**
	 * ��ȡָ�������ڵĶ�ά����Ϣ
	 * 
	 * @param input
	 * @param sheetName
	 * @param startRow
	 * @param startCell
	 * @return
	 */
	public static List<List<String>> getAresContents (HSSFWorkbook wb , String sheetName , int startRow , int startCell){
		List<List<String>> contents = new ArrayList<List<String>>();
		try {
			HSSFFormulaEvaluator evaluator =  wb.getCreationHelper().createFormulaEvaluator();
			HSSFSheet sheet = wb.getSheet(sheetName);
			if (sheet != null) {
				HSSFRow row = sheet.getRow(startRow);
				while (row != null) {
					int tempCell = startCell;
					List<String> content = new ArrayList<String>();
					contents.add(content);
					HSSFCell cell = row.getCell(startCell);
					while (cell != null) {
						content.add(getCellStringValue(cell, evaluator).trim());
						tempCell ++;
						cell = row.getCell(tempCell);
					}
					startRow++;
					row = sheet.getRow(startRow);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contents;
	}
	
	public static List<EObject> genBizTypeFromHSBizType(HSSFWorkbook workBook, IARESProject project ,String sheetName, int startCol, int startRow){
		List<EObject> typeList = new ArrayList<EObject>();
		
		if (StringUtils.isNotBlank(sheetName)) {
			try {
				HSSFFormulaEvaluator evaluator =  workBook.getCreationHelper().createFormulaEvaluator();
				HSSFSheet sheet = workBook.getSheet(sheetName);
				IARESResource stdRes = project.findResource(IMetadataResType.StdType, IMetadataResType.StdType);
				StandardDataTypeList stdtypeList = stdRes.getInfo(StandardDataTypeList.class);
				for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
					HSSFRow row = sheet.getRow(i);
					if (row != null) {
						//������
						String tn = getCellStringValue(row.getCell(1), evaluator).trim();
						//C����
						String c = getCellStringValue(row.getCell(2), evaluator).trim();
						//oracle����
						String o = getCellStringValue(row.getCell(3), evaluator).trim();
						//Ĭ��ֵ
						String d = getCellStringValue(row.getCell(4), evaluator).trim();
						//������
						String ch = getCellStringValue(row.getCell(5), evaluator).trim();
						
						if (StringUtils.isNotBlank(tn)) {
							BusinessDataType bizType = MetadataFactory.eINSTANCE.createBusinessDataType();
								
							typeList.add(bizType);
							bizType.setName(tn);
							bizType.setChineseName(ch);
							bizType.setDefaultValue(d);
							String typeName = findBizType(stdtypeList, c, o);
							String[] ct = getLP(c);
							String[] ot = getLP(o);
							bizType.setStdType(typeName);
							if (StringUtils.isNotBlank(typeName)) {
								if (ct.length == 3) {
									bizType.setLength(ct[1]);
									bizType.setPrecision(ct[2]);
								}else if (ct.length == 2) {
									bizType.setLength(ct[1]);
								}
								if (ot.length == 3) {
									bizType.setLength(ot[1]);
									bizType.setPrecision(ot[2]);
								}else if (ot.length == 2) {
									bizType.setLength(ot[1]);
								}
							}
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return typeList;
	}
	
	private static String findBizType(StandardDataTypeList stdtypeList ,String cc , String oo){
		String[] typeC = getLP(cc);
		String[] typeO = getLP(oo);
		for(StandardDataType bizType : stdtypeList.getItems()){
			String c = bizType.getValue("c");
			String o = bizType.getValue("oracle");
			String ct = "";
			String ot = "";
			if (typeC.length == 3) {
				c = StringUtils.replace(c, "$L", typeC[1]);
				c = StringUtils.replace(c, "$P", typeC[2]);
			}else if (typeC.length == 2) {
				c = StringUtils.replace(c, "$L", typeC[1]);
			}
			if (typeO.length == 3) {
				o = StringUtils.replace(o, "$L", typeO[1]);
				o = StringUtils.replace(o, "$P", typeO[2]);
			}else if (typeO.length == 2) {
				o = StringUtils.replace(o, "$L", typeO[1]);
			}
			if (StringUtils.equals(c, cc) && StringUtils.equals(o, oo) ) {
				return bizType.getName();
			}
		}
		
		return StringUtils.EMPTY;
	}
	
	private static String[] getLP(String type){
		List<String> retype = new ArrayList<String>();
		try {
			Pattern p = Pattern.compile("(^\\w+)(\\[.*\\])?+(\\(.*\\))?$"); 
			Matcher m = p.matcher(type);
			if (m.find()) {
				retype.add(m.group(1));
				if (StringUtils.isNotBlank(m.group(2))) {
					String t = StringUtils.substring(m.group(2), 1, m.group(2).length()-1);
					retype.addAll(Arrays.asList(StringUtils.split(t, ",")));
				}else if (StringUtils.isNotBlank(m.group(3))){
					String t = StringUtils.substring(m.group(3), 1, m.group(3).length()-1);
					retype.addAll(Arrays.asList(StringUtils.split(t, ",")));
				}
			}
		} catch (Exception e) {
		}
		return retype.toArray(new String[0]);
	}
	
	/**
	 * ��Excel�ļ���ָ����
	 * 
	 * @param excelStream
	 * @param sheetNames
	 * @param startCols
	 * @param startRows
	 * @return
	 * @throws Exception 
	 */
	public static Map< String, List< List<String> > > getExcelStringForCate(HSSFWorkbook workBook, String[] sheetNames, int[] startCols, int[] startRows) throws Exception {
		Map< String, List< List<String> > > sheetFieldMap = new HashMap<String, List<List<String>>>();
		
		if (sheetNames.length == startCols.length && startCols.length == startRows.length && startRows.length != 0) {
			try {
				
				HSSFFormulaEvaluator evaluator =  workBook.getCreationHelper().createFormulaEvaluator();
				
				for (int i=0;i<sheetNames.length;i++) {
					List<List<String>> fieldLists = new ArrayList<List<String>>();
					HSSFSheet sheet = workBook.getSheet(sheetNames[i]);
					if (sheet == null) {
						continue;
					}
					List<String> titleField = POIUtils.getColumns(sheet, startCols[i],startRows[i], evaluator);
					fieldLists.add(titleField);
					for (int j = startRows[i]+1; j<sheet.getLastRowNum()+1;j++){
						HSSFRow row = sheet.getRow(j);
						if (row == null) {
							break;
						}
						int cellNum = startCols[i];
						List<String> fields = new ArrayList<String>();
						int isCate = 0;
						for (int k = 0; k < titleField.size(); k++) {
							HSSFCell cell = row.getCell(k + cellNum);//addMergedRegion
							fields.add(POIUtils.getCellStringValue(cell, evaluator).trim());						
						}
						String titile = "";
						for (int k = 0; k < fields.size(); k++) {
							if (StringUtils.isNotBlank(fields.get(k))) {
								titile = fields.get(k);
								isCate ++;
							}
						}
						
						if (isCate == 1) {
							fields.set(0, titile);
							for (int k = 1; k < fields.size(); k++) {
								fields.set(k, null);
							}
						}
						
						boolean isLast = true;
						for (String field : fields) {
							if (StringUtils.isNotBlank(field)) {
								isLast = false;
								break;
							}
						}
						if (!isLast) {
							fieldLists.add(fields);
						}
					}
					sheetFieldMap.put(sheetNames[i], fieldLists);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("�ļ������ڻ��ļ���ʽ����ȷ");
			}
		}
		return sheetFieldMap;
	}
	
	/**
	 * ���ݺ����������ߵĸ�ʽ
	 * 
	 * @param excelStream
	 * @param sheetNames
	 * @param startCols
	 * @param startRows
	 * @return
	 * @throws Exception 
	 */
	public static Map< String, List< List<String> > > getExcelStringForUtilCate(HSSFWorkbook workBook, String[] sheetNames, int[] startCols, int[] startRows) throws Exception {
		Map< String, List< List<String> > > sheetFieldMap = new HashMap<String, List<List<String>>>();
		
		if (sheetNames.length == startCols.length && startCols.length == startRows.length && startRows.length != 0) {
			try {
				
				HSSFFormulaEvaluator evaluator =  workBook.getCreationHelper().createFormulaEvaluator();
				
				for (int i=0;i<sheetNames.length;i++) {
					List<List<String>> fieldLists = new ArrayList<List<String>>();
					HSSFSheet sheet = workBook.getSheet(sheetNames[i]);
					List<String> titleField = POIUtils.getColumns(sheet, startCols[i],startRows[i], evaluator);
					fieldLists.add(titleField);
					for (int j = startRows[i]+1; j<sheet.getLastRowNum()+1;j++){
						HSSFRow row = sheet.getRow(j);
						if (row == null) {
							break;
						}
						int cellNum = startCols[i];
						List<String> fields = new ArrayList<String>();
						for (int k = 0; k < titleField.size(); k++) {
							HSSFCell cell = row.getCell(k + cellNum);//addMergedRegion
							String value = POIUtils.getCellStringValue(cell, evaluator);
							fields.add(value);						
						}
						
						boolean isLast = true;
						for (String field : fields) {
							if (StringUtils.isNotBlank(field)) {
								isLast = false;
								break;
							}
						}
						if (!isLast) {
							fieldLists.add(fields);
						}
					}
					sheetFieldMap.put(sheetNames[i], fieldLists);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("�ļ������ڻ��ļ���ʽ����ȷ");
			}
		}
		return sheetFieldMap;
	}
	
	/**
	 * ��Excel�ļ���ָ����
	 * 
	 * @param excelStream
	 * @param sheetNames
	 * @param startCols
	 * @param startRows
	 * @return
	 * @throws Exception 
	 */
	public static Map< String, List< List<String> > > getExcelStringForMenu(HSSFWorkbook workBook, String[] sheetNames, int[] startCols, int[] startRows) throws Exception {
		Map< String, List< List<String> > > sheetFieldMap = new HashMap<String, List<List<String>>>();
		
		if (sheetNames.length == startCols.length && startCols.length == startRows.length && startRows.length != 0) {
			try {
				HSSFFormulaEvaluator evaluator =  workBook.getCreationHelper().createFormulaEvaluator();
				
				for (int i=0;i<sheetNames.length;i++) {
					List<List<String>> fieldLists = new ArrayList<List<String>>();
					HSSFSheet sheet = workBook.getSheet(sheetNames[i]);
					if (sheet == null) {
						continue;
					}
					int startR = startRows[i];
					List<String> titleField = POIUtils.getColumns(sheet, startCols[i],startRows[i], evaluator);
					int total = 0;
					while (titleField.size() <= 1 && total < 10) {
						startR ++;
						total++;
						titleField = POIUtils.getColumns(sheet, startCols[i],startR, evaluator);
					}
					//�Ա����еġ���������Ϣ���д���
					//����س�����
					{
						for (int j = 0; j < titleField.size(); j++) {
							String title = titleField.get(j);
							if (StringUtils.indexOf(title, "(") > -1) {
								title = StringUtils.substringBefore(title, "(");
							}else if (StringUtils.indexOf(title, "��") > -1) {
								title = StringUtils.substringBefore(title, "��");
							}
							if (StringUtils.indexOf(title, "\r\n") > -1) {
								title = StringUtils.replace(title, "\r\n", "");
							}else if (StringUtils.indexOf(title, "\n") > -1) {
								title = StringUtils.replace(title, "\n", "");
							}
							titleField.set(j, title);
						}
					}
					fieldLists.add(titleField);
					for (int j = startR+1; j<sheet.getLastRowNum()+1;j++){
						HSSFRow row = sheet.getRow(j);
						if (row == null) {
							break;
						}
						int cellNum = startCols[i];
						List<String> fields = new ArrayList<String>();
						boolean isCateColor = false;
						int isCate = 0;
						for (int k = 0; k < titleField.size(); k++) {
							HSSFCell cell = row.getCell(k + cellNum);//addMergedRegion
							if (k == 0) {
								if (POIUtils.readMerge(sheet, j)) {
									isCateColor = true;
								}
							}
							fields.add(POIUtils.getCellStringValue(cell, evaluator));
						}
						if (isCateColor) {
							for (int k = 0; k < fields.size(); k++) {
								if (StringUtils.isNotBlank(fields.get(k))) {
									isCate ++;
								}
							}
						}
						
						if (isCateColor && isCate == 1) {
							continue;
						}
						
						boolean isLast = true;
						for (String field : fields) {
							if (StringUtils.isNotBlank(field)) {
								isLast = false;
								break;
							}
						}
						if (!isLast) {
							fieldLists.add(fields);
						}
					}
					sheetFieldMap.put(sheetNames[i], fieldLists);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("�ļ������ڻ��ļ���ʽ����ȷ");
			}
		}
		return sheetFieldMap;
	}
	
	/**
	 * ��Excel�ļ���ָ����
	 * 
	 * @param excelStream
	 * @param sheetNames
	 * @param startCols
	 * @param startRows
	 * @return
	 * @throws Exception 
	 */
	public static Map< String, List< List<String> > > getExcelStringForDict(InputStream excelStream, String[] sheetNames, int[] startCols, int[] startRows) throws Exception {
		Map< String, List< List<String> > > sheetFieldMap = new HashMap<String, List<List<String>>>();
		
		if (sheetNames.length == startCols.length && startCols.length == startRows.length && startRows.length != 0) {
			try {
				HSSFWorkbook workBook = new HSSFWorkbook(excelStream);
				
				HSSFFormulaEvaluator evaluator =  workBook.getCreationHelper().createFormulaEvaluator();
				
				for (int i=0;i<sheetNames.length;i++) {
					List<List<String>> fieldLists = new ArrayList<List<String>>();
					HSSFSheet sheet = workBook.getSheet(sheetNames[i]);
					List<String> titleField = POIUtils.getColumns(sheet, startCols[i],startRows[i], evaluator);
					int startR = startRows[i];
					while (titleField.size() == 0) {
						startR ++;
						titleField = POIUtils.getColumns(sheet, startCols[i],startR, evaluator);
					}
					fieldLists.add(titleField);
					for (int j = startR+1; j<sheet.getLastRowNum()+1;j++){
						HSSFRow row = sheet.getRow(j);
						if (row == null) {
							break;
						}
						int cellNum = startCols[i];
						List<String> fields = new ArrayList<String>();
						for (int k = 0; k < titleField.size(); k++) {
							HSSFCell cell = row.getCell(k + cellNum);//addMergedRegion
							fields.add(POIUtils.getCellStringValue(cell, evaluator).trim());
						}
						
						boolean isLast = true;
						for (String field : fields) {
							if (StringUtils.isNotBlank(field)) {
								isLast = false;
								break;
							}
						}
						if (!isLast) {
							fieldLists.add(fields);
						}
					}
					sheetFieldMap.put(sheetNames[i], fieldLists);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception("�ļ������ڻ��ļ���ʽ����ȷ");
			}
		}
		return sheetFieldMap;
	}
	
	/**
	 * ��֤�ϲ���Ԫ��,�з���
	 * 
	 * @param hs
	 * @param i
	 * @return
	 */
	public static boolean readMerge(HSSFSheet hs ,int i){
		int sheetmergerCount = hs.getNumMergedRegions();
		int firstrow = 0;
		int lastrow = 0;
		int firstcolumn = 0;
		int lastcolumn = 0;
		HSSFCell hc = null;
		CellRangeAddress ca = null;
		for (int sheetmergerIndex = 0; sheetmergerIndex < sheetmergerCount; sheetmergerIndex++) {
			ca = hs.getMergedRegion(sheetmergerIndex);
			hc = hs.getRow(ca.getFirstRow()).getCell(ca.getFirstColumn());
			firstrow = ca.getFirstRow();
			lastrow = ca.getLastRow();
			firstcolumn = ca.getFirstColumn();
			lastcolumn = ca.getLastColumn();
		  if(firstrow<=i&&i<=lastrow){
		  	return true;
		  }
		}
		return false;
	}
	
	public static String getCellStringValue(HSSFCell cell, HSSFFormulaEvaluator evaluator) {
		
		if (cell != null) {
		    switch (evaluator.evaluateInCell(cell).getCellType()) {
		        case Cell.CELL_TYPE_BOOLEAN:
		        	return BooleanUtils.toStringTrueFalse(cell.getBooleanCellValue());
		        case Cell.CELL_TYPE_NUMERIC:
		        	// FIXME ������ʵ�ų��� �������͵�����
		        	return String.valueOf((int)cell.getNumericCellValue());
		        case Cell.CELL_TYPE_STRING:
		        	return cell.getStringCellValue();
		        case Cell.CELL_TYPE_BLANK:
		            break;
		        case Cell.CELL_TYPE_ERROR:

		        // CELL_TYPE_FORMULA will never occur
		        case Cell.CELL_TYPE_FORMULA:
		            break;
		    }
		}
		
		return StringUtils.EMPTY;
	}
	
	public static List<String> getColumns (HSSFSheet sheet , int cellNum , int rowNum, HSSFFormulaEvaluator evaluator){
		List<String> fields = new ArrayList<String>();
		boolean nextCell = true;
		while(nextCell){
			HSSFRow row = sheet.getRow(rowNum);
			if (row != null) {
				HSSFCell cell = row.getCell(cellNum);
				if (cell != null) {
					String text = getCellStringValue(cell, evaluator);
					
					if (StringUtils.isNotBlank(text)) {
						fields.add(text);
						cellNum ++;
					}else {
						nextCell = false;
					}
				}else {
					nextCell = false;
				}
			}else {
				nextCell = false;
			}
		}
		return fields;
	}
	
}
