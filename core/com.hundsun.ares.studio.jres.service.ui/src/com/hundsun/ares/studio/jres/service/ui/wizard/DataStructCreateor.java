/**
 * Դ�������ƣ�DataStructCreateor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�yanwj06282
 */
package com.hundsun.ares.studio.jres.service.ui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.biz.ui.excel.BizResExcelStructEntity;
import com.hundsun.ares.studio.biz.ui.excel.ExcelBasicParamStuctEntity;
import com.hundsun.ares.studio.biz.ui.excel.ExcelMenuItemEntity;
import com.hundsun.ares.studio.biz.ui.excel.ExcelMenuSheetStructEntity;
import com.hundsun.ares.studio.biz.ui.excel.ExcelSheetStructEntity;
import com.hundsun.ares.studio.biz.ui.excel.ExportExcelEntity;
import com.hundsun.ares.studio.biz.ui.excel.ParameterItemStructEntity;
import com.hundsun.ares.studio.biz.ui.excel.ParameterStructEntity;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.service.IMetadataService;
import com.hundsun.ares.studio.jres.metadata.service.IStandardField;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.ExtensibleData2AttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.ExtensibleData2MapAttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.ExtensibleDataAttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IAttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IHeaderSorter;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.NormalAttributeHelper;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.service.Service;
import com.hundsun.ares.studio.jres.service.ServicePackage;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.IMapExtensibleModelPropertyDescriptor;

/**
 * @author yanwj06282
 *
 */
public class DataStructCreateor {
	
	private static final Logger log = Logger.getLogger(DataStructCreateor.class);
	public static IMetadataService metadataService;
	public static IARESProject project;
	public static boolean devValueStatus;
	public static boolean multStatus;
	public static final String OBJECT_ID = "���ܺ�";
	public static final String VERSION_ID = "�汾��";
	public static final String UPDATE_DATE = "��������";
	public static final String SERVICE_NAME = "������";
	public static final String SERVICE_CHINESE_NAME = "����������";
	public static final String SERVICE_DESCRIPTION = "˵��";
	public static final String INPUT_PARAMETER = "�������";
	public static final String OUTPUT_PARAMETER = "�������";
	public static final String MODIFICATION = "�޸ļ�¼";
	public static final String INPUT_COLLECTION = "��������";
	public static final String OUTPUT_COLLECTION = "��������";
	
	public static final String OBJ_ID = "�����";
	public static final String OBJECT_NAME = "������";
	public static final String OBJECT_CHINESE_NAME = "����������";
	public static final String OBJECT_PARAMETER = "��������";
	
	public static ExportExcelEntity createServiceExcelEntity(Multimap<IARESModule ,IARESResource> resourceMap){
		ExportExcelEntity entity = new ExportExcelEntity();
		List<IARESResource> menuResList = new ArrayList<IARESResource>();
		{
			for (Iterator<IARESModule> iterator = resourceMap.keySet().iterator();iterator.hasNext();) {
				IARESModule key = iterator.next();
				Collection<IARESResource> resed = resourceMap.get(key);
				ExcelSheetStructEntity sheetEntity = new ExcelSheetStructEntity();
				String moduleCName = getChineseFileName("-", key);
				sheetEntity.setCnamePrefix(ExcelStructConstantDefine.SERVICE_PREFIX);
				sheetEntity.setSheetCName(moduleCName);
				sheetEntity.setSheetEName(StringUtils.replace(key.getElementName(), ".", "-"));
				for (IARESResource res : resed) {
					try {
						Service service = res.getInfo(Service.class);
						if (service == null) {
							continue;
						}
						menuResList.add( res);
						String[] str = getModuleMaxVersion(key);
						BizResExcelStructEntity resEntity = new BizResExcelStructEntity();
						resEntity.setHyperlinkKey(res.getFullyQualifiedName()+"."+res.getType());
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(OBJECT_ID,1,1) , new ExcelBasicParamStuctEntity(service.getObjectId(),1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(VERSION_ID,1,1) , new ExcelBasicParamStuctEntity(str[0],1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(UPDATE_DATE,1,1) , new ExcelBasicParamStuctEntity(str[1],1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(SERVICE_NAME,1,1) , new ExcelBasicParamStuctEntity(service.getName(),1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(SERVICE_CHINESE_NAME,1,1) , new ExcelBasicParamStuctEntity(service.getChineseName(),1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity("",1,1) , new ExcelBasicParamStuctEntity("",1,1));
						//����һ�У���Ϊһ��block�Ŀ�ʼ���Ա㵼��ʱ���жϣ������������������� TASK #9511 by wangxh
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(INPUT_COLLECTION,1,1) , new ExcelBasicParamStuctEntity(service.getInterface().isInputCollection()?"Y":"N",1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(OUTPUT_COLLECTION,1,1) , new ExcelBasicParamStuctEntity(service.getInterface().isOutputCollection()?"Y":"N",1,1));
//						resEntity.putBasicParams(new ExcelBasicParamStuctEntity("",1,1) , new ExcelBasicParamStuctEntity("",1,1));
						
						Map<ExcelBasicParamStuctEntity, ExcelBasicParamStuctEntity> extendsMap = getExtendsValueMap(res, service, ServicePackage.Literals.SERVICE);
						resEntity.getBasicParams().putAll(extendsMap);
						if (extendsMap.size()%resEntity.getBasicParmasMAXCellLength() != 0) {
							for (int i = 0; i < resEntity.getBasicParmasMAXCellLength() - extendsMap.size()%resEntity.getBasicParmasMAXCellLength(); i++) {
								resEntity.putBasicParams(new ExcelBasicParamStuctEntity("",1,1) , new ExcelBasicParamStuctEntity("",1,1));
							}
						}
						
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(SERVICE_DESCRIPTION,1,2) , new ExcelBasicParamStuctEntity(service.getDescription(),5,2));
						
						List<ParameterItemStructEntity> inputParams = exportExcelStringTable(service.getInterface(), BizPackage.Literals.BIZ_INTERFACE__INPUT_PARAMETERS,BizPackage.Literals.PARAMETER,
								ExcelStructConstantDefine.SERVICE_INPUT_PARAMETER_TITLES, 
								new EStructuralFeature[]{BizPackage.Literals.PARAMETER__FLAGS,BizPackage.Literals.PARAMETER__ID,
								BizPackage.Literals.PARAMETER__NAME ,
								BizPackage.Literals.PARAMETER__TYPE,
								BizPackage.Literals.PARAMETER__MULTIPLICITY,BizPackage.Literals.PARAMETER__DEFAULT_VALUE,BizPackage.Literals.PARAMETER__DESCRIPTION}, 
								true, 
								new String[]{}, new String[]{}, res, null
						);
						
						for (ParameterItemStructEntity item : inputParams) {
							if (StringUtils.contains(item.getItem().get(3), ".")) {
								item.setHyprelinkKey(item.getItem().get(3)+"."+IBizResType.Object);
							}
						}
						
						ParameterStructEntity inputStruct = new ParameterStructEntity(inputParams, new ArrayList<String>());
						List<String> inputaramTtile = new ArrayList<String>();
						if (!devValueStatus) {
							inputaramTtile.add("Ĭ��ֵ");//Ĭ�ϵ���Ĭ��ֵ���ڽ��õ���Ĭ��ֵѡ��ʱ��������Ĭ��ֵ
						}
						if (!multStatus) {
							inputaramTtile.add("��ϵ����");//Ĭ�ϵ�����ϵ���ԣ��ڽ��õ�����ϵ����ѡ��ʱ����������ϵ����
						}
						inputStruct.setFilterTitles(inputaramTtile);
						resEntity.getParameterMaps().put(INPUT_PARAMETER, inputStruct);
						
						List<ParameterItemStructEntity> outParams = exportExcelStringTable(service.getInterface(), BizPackage.Literals.BIZ_INTERFACE__OUTPUT_PARAMETERS,BizPackage.Literals.PARAMETER,
								ExcelStructConstantDefine.SERVICE_OUTPUT_PARAMETER_TITLES, 
								new EStructuralFeature[]{BizPackage.Literals.PARAMETER__FLAGS,BizPackage.Literals.PARAMETER__ID,
								BizPackage.Literals.PARAMETER__NAME ,
								BizPackage.Literals.PARAMETER__TYPE, 
								BizPackage.Literals.PARAMETER__MULTIPLICITY,BizPackage.Literals.PARAMETER__DEFAULT_VALUE ,BizPackage.Literals.PARAMETER__DESCRIPTION}, 
								true, 
								new String[]{}, new String[]{}, res, null
						);
						
						for (ParameterItemStructEntity item : outParams) {
							if (StringUtils.contains(item.getItem().get(3), ".")) {
								item.setHyprelinkKey(item.getItem().get(3)+"."+IBizResType.Object);
							}
						}
						
						ParameterStructEntity outputStruct = new ParameterStructEntity(outParams, new ArrayList<String>());
						List<String> outputparamTtile = new ArrayList<String>();
						if (!devValueStatus) {
							outputparamTtile.add("Ĭ��ֵ");//Ĭ�ϵ���Ĭ��ֵ���ڽ��õ���Ĭ��ֵѡ��ʱ��������Ĭ��ֵ
						}
						if (!multStatus) {
							outputparamTtile.add("��ϵ����");//Ĭ�ϵ�����ϵ���ԣ��ڽ��õ�����ϵ����ѡ��ʱ����������ϵ����
						}
						outputStruct.setFilterTitles(outputparamTtile);
						resEntity.getParameterMaps().put(OUTPUT_PARAMETER, outputStruct);
						
						resEntity.putEndAres(new ExcelBasicParamStuctEntity(MODIFICATION,1,2) , new ExcelBasicParamStuctEntity(buildReviceHistory(service.getHistories()),5,2));
						
						sheetEntity.getEntityList().add(resEntity);
					} catch (ARESModelException e) {
						log.error("ģ�ͽ����쳣��["+res.getElementName()+"]", e);
					}
				}
				entity.getSheetList().add(sheetEntity);
			}
			ExcelMenuSheetStructEntity menuEntity = new ExcelMenuSheetStructEntity();
			menuEntity.setSheetName(ExcelStructConstantDefine.SERIVCE_SHEET_NAME);
			menuEntity.getMenuItems().add(new ExcelMenuItemEntity("", Arrays.asList(ExcelStructConstantDefine.SERVICE_MENU_TITLES)));
			for (IARESResource res : menuResList) {
				ExcelMenuItemEntity itemEntity = new ExcelMenuItemEntity();
				List<String> item = new ArrayList<String>();
				Service service;
				try {
					service = res.getInfo(Service.class);
					if (service == null) {
						continue;
					}
					item.add(StringUtils.replace(res.getModule().getElementName(), ".", "-"));
					item.add(getChineseFileName("-", res.getModule()));
					item.add(service.getObjectId());
					item.add(service.getName());
					item.add(service.getChineseName());
					item.add(service.getDescription());
					itemEntity.setHyprelinkKey(res.getFullyQualifiedName()+"."+res.getType());
					itemEntity.setItem(item);
					menuEntity.getMenuItems().add(itemEntity);
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			entity.getMenuList().add(menuEntity);
		}
		
		return entity;
	}
	
	public static ExportExcelEntity createObjectExcelEntity(Multimap<IARESModule ,IARESResource> resourceMap){
		
		ExportExcelEntity entity = new ExportExcelEntity();
		List<IARESResource> menuResList = new ArrayList<IARESResource>();
		{
			List<IARESModule> modules = new ArrayList<IARESModule>();
			modules.addAll(resourceMap.keySet());
			Collections.sort(modules, new ModuleNameComp());
			for (IARESModule key : modules) {
				Collection<IARESResource> resed = resourceMap.get(key);
				ExcelSheetStructEntity sheetEntity = new ExcelSheetStructEntity();
				String moduleCName = getChineseFileName("-", key);
				sheetEntity.setCnamePrefix(ExcelStructConstantDefine.OBJECT_PREFIX);
				sheetEntity.setSheetCName(moduleCName);
				sheetEntity.setSheetEName(StringUtils.replace(key.getElementName(), ".", "-"));
				//������Դ�����ն��������
				List<IARESResource> reses = sortObjectMap(resed);
				for (IARESResource res : reses) {
					try {
						ARESObject object = res.getInfo(ARESObject.class);
						if (object == null) {
							continue;
						}
						menuResList.add(res);
						String[] str = getModuleMaxVersion(key);
						BizResExcelStructEntity resEntity = new BizResExcelStructEntity();
						resEntity.setHyperlinkKey(res.getFullyQualifiedName()+"."+res.getType());
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(OBJECT_NAME,1,1) , new ExcelBasicParamStuctEntity(object.getName(),1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(OBJECT_CHINESE_NAME,1,1) , new ExcelBasicParamStuctEntity(object.getChineseName(),1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity("",1,1) , new ExcelBasicParamStuctEntity("",1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(OBJ_ID,1,1) , new ExcelBasicParamStuctEntity(object.getObjectId(),1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(VERSION_ID,1,1) , new ExcelBasicParamStuctEntity(str[0],1,1));
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(UPDATE_DATE,1,1) , new ExcelBasicParamStuctEntity(str[1],1,1));
						Map<ExcelBasicParamStuctEntity, ExcelBasicParamStuctEntity> extendsMap = getExtendsValueMap(res, object, BizPackage.Literals.ARES_OBJECT);
						resEntity.getBasicParams().putAll(extendsMap);
						if (extendsMap.size()%resEntity.getBasicParmasMAXCellLength() != 0) {
							for (int i = 0; i < resEntity.getBasicParmasMAXCellLength() - extendsMap.size()%resEntity.getBasicParmasMAXCellLength(); i++) {
								resEntity.putBasicParams(new ExcelBasicParamStuctEntity("",1,1) , new ExcelBasicParamStuctEntity("",1,1));
							}
						}
						resEntity.putBasicParams(new ExcelBasicParamStuctEntity(SERVICE_DESCRIPTION,1,1) , new ExcelBasicParamStuctEntity(object.getDescription(),5,1));
						
						List<ParameterItemStructEntity> objectParams = exportExcelStringTable(object, BizPackage.Literals.ARES_OBJECT__PROPERTIES,BizPackage.Literals.PARAMETER,
								ExcelStructConstantDefine.OBJECT_PARAMETER_Titles, 
								new EStructuralFeature[]{BizPackage.Literals.PARAMETER__FLAGS,BizPackage.Literals.PARAMETER__ID,
								BizPackage.Literals.PARAMETER__NAME ,BizPackage.Literals.PARAMETER__TYPE, 
								BizPackage.Literals.PARAMETER__MULTIPLICITY,BizPackage.Literals.PARAMETER__DEFAULT_VALUE,BizPackage.Literals.PARAMETER__DESCRIPTION}, 
								true, 
								new String[]{}, new String[]{}, res, null
						);
						
						for (ParameterItemStructEntity item : objectParams) {
							if (StringUtils.contains(item.getItem().get(3), ".")) {
								item.setHyprelinkKey(item.getItem().get(3)+"."+IBizResType.Object);
							}
						}
						ParameterStructEntity paramStruct = new ParameterStructEntity(objectParams, new ArrayList<String>());
						List<String> paramTtile = new ArrayList<String>();
						if (!devValueStatus) {//Ĭ�ϵ���Ĭ��ֵ������ѡ��ʱ����ʾ������
							paramTtile.add("Ĭ��ֵ");
						}
						if (!multStatus) {//Ĭ�ϵ�����ϵ���ԣ�����ѡ��ʱ����ʾ������
							paramTtile.add("��ϵ����");
						}
						paramStruct.setFilterTitles(paramTtile);
						resEntity.getParameterMaps().put(OUTPUT_PARAMETER, paramStruct);
						
						
						resEntity.putEndAres(new ExcelBasicParamStuctEntity(MODIFICATION,1,2) , new ExcelBasicParamStuctEntity(buildReviceHistory(object.getHistories()),5,2));
						
						sheetEntity.getEntityList().add(resEntity);
					} catch (ARESModelException e) {
						log.error("ģ�ͽ����쳣��["+res.getElementName()+"]", e);
					}
				}
				entity.getSheetList().add(sheetEntity);
			}
			ExcelMenuSheetStructEntity menuEntity = new ExcelMenuSheetStructEntity();
			menuEntity.setSheetName(ExcelStructConstantDefine.OBJECT_SHEET_NAME);
			menuEntity.setHyperlinkIndex(3);
			menuEntity.getMenuItems().add(new ExcelMenuItemEntity("", Arrays.asList(ExcelStructConstantDefine.OBJECT_MENU_TITLES)));
			for (IARESResource res : menuResList) {
				ExcelMenuItemEntity itemEntity = new ExcelMenuItemEntity();
				List<String> item = new ArrayList<String>();
				ARESObject object;
				try {
					object = res.getInfo(ARESObject.class);
					if (object == null) {
						continue;
					}
					item.add(StringUtils.replace(res.getModule().getElementName(), ".", "-"));
					item.add(getChineseFileName("-", res.getModule()));
					item.add(StringUtils.defaultString(object.getObjectId()));
					item.add(object.getName());
					item.add(object.getChineseName());
					item.add(object.getDescription());
					itemEntity.setHyprelinkKey(res.getFullyQualifiedName()+"."+res.getType());
					itemEntity.setItem(item);
					menuEntity.getMenuItems().add(itemEntity);
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			entity.getMenuList().add(menuEntity);
		}
		
		return entity;
	}
	
	/**
	 * �����б����ն���Ŵ�С�����˳������
	 * 
	 */
	private static List<IARESResource> sortObjectMap(Collection<IARESResource> resed){
		List<IARESResource> reses = new ArrayList<IARESResource>();
		reses.addAll(resed);
		Collections.sort(reses, new ObjectIdComp());
		return reses;
	}
	
	private static Map<ExcelBasicParamStuctEntity ,ExcelBasicParamStuctEntity> getExtendsValueMap(IARESResource res ,EObject info , EClass eclass){
		Map<String, Object> helperMap = new HashMap<String, Object>();
		IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(res, eclass);
		for (IExtensibleModelEditingSupport support : supports) {
			for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(res, eclass)) {
				if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
					helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
				} else {
					helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
				}
			}
		}
		
		Map<ExcelBasicParamStuctEntity, ExcelBasicParamStuctEntity> result = new HashMap<ExcelBasicParamStuctEntity,ExcelBasicParamStuctEntity>();
		
		for (Entry<String, Object> entry : helperMap.entrySet()) {
			if (entry.getValue() instanceof IAttributeHelper) {
				result.put(new ExcelBasicParamStuctEntity(entry.getKey(), 1, 1), new ExcelBasicParamStuctEntity(((IAttributeHelper)entry.getValue()).getValue(info), 1, 1));
			}else {
				result.put(new ExcelBasicParamStuctEntity(entry.getKey(), 1, 1), new ExcelBasicParamStuctEntity(entry.getValue().toString(), 1, 1));
			}
		}
		
		return result;
	}
	
	public static String getChineseFileName (String separator ,IARESModule module){
		StringBuffer sb = new StringBuffer();
		getModuleChineseName(module, sb ,separator);
		String[] ps = StringUtils.split(sb.toString(), separator);
		StringBuffer sbf = new StringBuffer();
		for (int i = ps.length-1; i > -1 && ps.length > 0; i--) {
			if (StringUtils.isNotBlank(sbf.toString())) {
				sbf.append(separator);
			}
			sbf.append(ps[i]);
		}
		return sbf.toString();
	}
	
	/**
	 * ��ȡģ��������
	 * 
	 * @param module ѡ��ģ��
	 * @param sb ����ֵ��������
	 * @param separator ģ��㼶��ķָ���
	 */
	private static void getModuleChineseName (IARESModule module , StringBuffer sb , String separator){
		if (module != null) {
			IARESResource property = module.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
			if (property != null && property.exists()) {
				try {
					ModuleProperty info = property.getInfo(ModuleProperty.class);
					if (info != null) {
						Object obj = info.getValue(ICommonModel.CNAME);
						if (obj != null) {
							if (StringUtils.isNotBlank(sb.toString())) {
								sb.append(separator);
							}
							sb.append(obj.toString());
						}
					}
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			getModuleChineseName(module.getParentModule(), sb ,separator);
		}
	}
	
	private static String[] getModuleMaxVersion(IARESModule module){
		String[] str = new String[2];
		RevisionHistory his = RevisionHistoryUtil.getMaxVersionHisInfo(module);
		if (his != null) {
			str[0] = his.getVersion();
			str[1] = his.getModifiedDate();
		}else {
			String projectVersion = RevisionHistoryUtil.getProjectPropertyVersion(module.getARESProject());
			if (StringUtils.isBlank(projectVersion)) {
				projectVersion = "1.0.0.1";
			}
			str[0] = projectVersion;
			str[1] = "";
		}
		return str;
	}
	
	/**
	 * 
	 * ��ȡ������excel�����ݱ�
	 * 
	 * @param owner
	 * @param reference
	 * @param titles
	 * @param features
	 * @param includeExtend �Ƿ񵼳���չ��
	 * @param element
	 * @return
	 */
	public static List<ParameterItemStructEntity> exportExcelStringTable(EObject owner, EReference reference, EClass itemClass, String[] titles, EStructuralFeature[] features,
			boolean includeExtend, String[] titles2, String[] dataKeys, IARESElement element, IHeaderSorter sorter) {
		List<ParameterItemStructEntity> result = new ArrayList<ParameterItemStructEntity>();

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
		
		result.add(new ParameterItemStructEntity("", header));
		
		// ��ι�������
		List<EObject> contentObjectList = (List<EObject>) owner.eGet(reference);
		for (EObject eObject : contentObjectList) {
			List<String> content = new ArrayList<String>();
			if (eObject.eGet(BizPackage.Literals.PARAMETER__PARAM_TYPE) == ParamType.STD_FIELD) {
				Object o = eObject.eGet(BizPackage.Literals.PARAMETER__ID);
				if (o instanceof String && StringUtils.isNotBlank(o.toString())) {
					//�����Ƿ��б�׼�ֶΣ���ǡ�Id
					content.add(((Parameter)eObject).getFlags());
					content.add(((Parameter)eObject).getId());
					String comment = ((Parameter)eObject).getComments();
					IStandardField std = metadataService.getStandardField(o.toString());
					if (std != null) {
						content.add(std.getChineseName());//������׼�ֶζ�Ӧ��������
						content.add(std.getDataTypeId());//������׼�ֶζ�Ӧ��ҵ������
						if (StringUtils.isBlank(comment)) {
							StringBuffer text = new StringBuffer();
							ReferenceInfo  dictReferenceInfo = ReferenceManager.getInstance().getFirstReferenceInfo(project,IMetadataRefType.Dict,std.getDictionaryTypeId(),true);
							if(dictReferenceInfo != null){
								DictionaryType objDictionaryType = (DictionaryType) dictReferenceInfo.getObject();
								if(objDictionaryType!=null){
									for(DictionaryItem item : objDictionaryType.getItems()){
										String value = StringUtils.defaultString(item.getValue());
										String chineseName = StringUtils.defaultString(item.getChineseName());
										text.append(value);
										text.append(":");
										text.append(chineseName);
										text.append(" ");
									}
								}
							}
							comment = text.toString() + "\r\n" + std.getDescription();//��עΪ��ʱ��ȡ��׼�ֶι����ֵ��˵����Ϣ��
						}
					}else{
						content.add("");//�����׼�ֶβ����ڣ����������л���Ҫ���ϡ����������л�Բ���
						content.add("");//�����׼�ֶβ����ڣ�ҵ�����͵��л���Ҫ���ϡ����������л�Բ���
					}
					//�����Ƿ��б�׼�ֶΣ���ϵ���ԡ�Ĭ��ֵ����ע���ᵼ��
					content.add(((Parameter)eObject).getMultiplicity().getLiteral());
					content.add(((Parameter)eObject).getDefaultValue());
					content.add(comment);
					for (int i = titles.length; i < helperList.size(); i++) {
						content.add(helperList.get(i).getValue(eObject));
					}
					result.add(new ParameterItemStructEntity("", content));
				}
				continue;
			}else if (eObject.eGet(BizPackage.Literals.PARAMETER__PARAM_TYPE) == ParamType.OBJECT) {
				content.add(((Parameter)eObject).getFlags());
				content.add(((Parameter)eObject).getId());
				String chineseName = "";
				String desc = ((Parameter)eObject).getComments();
				ReferenceInfo objectInfo = ReferenceManager.getInstance().getFirstReferenceInfo(project,IBizRefType.Object,((Parameter)eObject).getType(),true);
				if (objectInfo != null) {
					ARESObject obj = (ARESObject) objectInfo.getObject();
					chineseName = obj.getChineseName();
					if (StringUtils.isBlank(desc)) {
						desc = obj.getDescription();
					}
				}
				
				content.add(chineseName);
				content.add(((Parameter)eObject).getType());
				content.add(((Parameter)eObject).getMultiplicity().getLiteral());
				content.add(((Parameter)eObject).getDefaultValue());
				content.add(desc);
				for (int i = titles.length; i < helperList.size(); i++) {
					content.add(helperList.get(i).getValue(eObject));
				}
				result.add(new ParameterItemStructEntity("", content));
				
				continue;
			}else if(eObject.eGet(BizPackage.Literals.PARAMETER__PARAM_TYPE) == ParamType.NON_STD_FIELD) {
				content.add(((Parameter)eObject).getFlags());
				content.add(((Parameter)eObject).getId());
				content.add(((Parameter)eObject).getName());
				content.add(((Parameter)eObject).getType());
				content.add(((Parameter)eObject).getMultiplicity().getLiteral());
				content.add(((Parameter)eObject).getDefaultValue());
				String comment = ((Parameter)eObject).getComments();
				if (StringUtils.isBlank(comment)) {
					comment = ((Parameter)eObject).getDescription();
				}
				content.add(comment);
				for (int i = titles.length; i < helperList.size(); i++) {
						content.add(helperList.get(i).getValue(eObject));
				}
				result.add(new ParameterItemStructEntity("", content));
				continue;
			}
		}
		
		return result;
	}
	
	/**
	 * ��ȡ���ݿ����޶���¼
	 * 
	 * @param table
	 * @return
	 */
	private static String buildReviceHistory(List<RevisionHistory> histories) {

		List<List<String>> list = new ArrayList<List<String>>();

		{
			if (histories.size() > 0) {
				List<String> content = new ArrayList<String>();
				content.add("�޸İ汾"+"   ");
				content.add("�޸�����"+"   ");
				content.add("�޸ĵ�"+"        ");
				content.add("������"+"   ");
				content.add("������"+"   ");
				content.add("�޸�����"+"        ");
				content.add("�޸�ԭ��"+"        ");
				content.add("��ע"+"        ");
				list.add(content);
			}
		}

		for (RevisionHistory his : histories) {
			String version = his.getVersion();
			List<String> content = new ArrayList<String>();
			content.add("V" + version+"   ");
			String modifyDate = his.getModifiedDate();
			String newDate = StringUtils.substring(modifyDate, 0, 10).replaceAll("-", "");
			content.add(newDate+"   ");
			content.add(his.getOrderNumber()+"        ");
			content.add(his.getModifiedBy()+"   ");
			content.add(his.getCharger()+"   ");
			content.add(his.getModified()+"        ");
			content.add(his.getModifiedReason()+"        ");
			content.add(StringUtils.defaultString(his.getComment())+"        ");

			list.add(content);
		}

		return genStringTable(list);
	}
	
	private static String genStringTable(List< List<String> > contents) {
		// �����ҳ�����ַ������鳤��
		int maxLength = 0;
		for (List<String> content : contents) {
			maxLength = Math.max(maxLength, content.size());
		}
		
		if (maxLength == 0) {
			return StringUtils.EMPTY;
		}
		
		// ���Ƚ������ַ�������ͳһ���ȣ�����ĳ����ÿհ��ַ�������
		// ͬʱ�ҳ�ÿһ�б���ĳ���
		List<List<String>> contents_normalization = new ArrayList<List<String>>();
		int[] widths = new int[maxLength];
		
		for (int i = 0; i < contents.size(); i++) {
			List<String> content = contents.get(i);
			// ������Ȳ���Ҫ�仯��ֱ��ʹ��ԭʼ���󣬼���ʱ��ռ�ɱ�
			if (maxLength != content.size()) {
				List<String> newContent = new ArrayList<String>();
				newContent.addAll(content);
				
				for (int j = content.size(); j < maxLength; j++) {
					newContent.add(StringUtils.EMPTY);
				}
				content = newContent;
			}
			
			contents_normalization.add(content);
			
			// ���ҳ���
			for (int j = 0; j < content.size(); j++) {
				int len = StringUtils.defaultString(content.get(j)).getBytes().length;
				widths[j] = Math.max(len, widths[j]);
			}
		}
		
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < contents_normalization.size(); i++) {
			List<String> content = contents_normalization.get(i);
			for (int j = 0; j < content.size(); j++) {
				// ������Ҫ����Ŀո���������������ȥ�Ѿ�ռ�����������к���ռ2���ո񳤶�
				int len = StringUtils.defaultString(content.get(j)).getBytes().length;
				int spaceWidth = widths[j] - len + 1; // ���һ���ո���Էָ�����ռ����2����Ԫ��
				result.append(StringUtils.defaultString(content.get(j)));
				result.append(StringUtils.repeat(" ", spaceWidth));
				
			}
			result.append("\r\n");
		}
		return result.toString();
	}
	
	static class ModuleNameComp implements Comparator<IARESModule>{

		@Override
		public int compare(IARESModule m1, IARESModule m2) {
			String mn1 = m1.getElementName();
			String mn2 = m2.getElementName();
			
			return StringUtils.split(mn1, ".").length - StringUtils.split(mn2, ".").length;
		}
		
	}
	
	static class ObjectIdComp implements Comparator<IARESResource>{

		@Override
		public int compare(IARESResource o1, IARESResource o2) {
			try {
				ARESObject ao1 = o1.getInfo(ARESObject.class);
				ARESObject ao2 = o2.getInfo(ARESObject.class);
				
				if (ao1 != null && ao2 != null) {
					String obj1 = ao1.getObjectId();
					String obj2 = ao2.getObjectId();
					if (StringUtils.isBlank(obj1) || StringUtils.isBlank(obj2)) {
						return 0;
					}
					return Integer.parseInt(obj1) - Integer.parseInt(obj2);
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}catch (NumberFormatException e) {
				e.printStackTrace();
			}
			return 0;
		}
		
	}
	
}
