/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.jres.metadata.ui.Language;
import com.hundsun.ares.studio.jres.metadata.ui.LanguageRegister;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.ConstantList;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.StandardFieldList;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValueList;

/**
 * 
 * Ԫ���ݵ��������
 * @author qinyuan
 *
 */
public class ImportMetaDataHelper {
	
	private static ImportMetaDataHelper instance;
	/**
	 * @return the instance
	 */
	public static ImportMetaDataHelper getInstance() {
		if(null == instance) {
			instance = new ImportMetaDataHelper();
			initMetadataLanguages();
		}
		return instance;
	}
	
	static Language[] languages;
	static String[] languageIds;
	static String[] languageTitles;
	private static void initMetadataLanguages() {
		languages = LanguageRegister.getInstance().getRegisteredLanguages();
		languageIds = new String[languages.length];
		languageTitles = new String[languages.length];
		for (int i = 0; i < languageIds.length; i++) {
			languageIds[i] = languages[i].getId();
			languageTitles[i] = languages[i].getName();
		}
	}
	
	/**
	 * �����׼�ֶ�
	 * @param resource
	 * @param table
	 * @param monitor
	 * @throws ARESModelException
	 */
	public void importStdFld(IARESResource resource, List<List<String>> table,StandardFieldList list, IProgressMonitor monitor) throws ARESModelException {
		List<EObject> items = getStdFldItems(resource, table);
		
		list.getItems().addAll( (Collection<? extends StandardField>) items);
		if(null != monitor) {
			resource.save(list, true, new SubProgressMonitor(monitor, 25));
		}else {
			resource.save(list, true, monitor);
		}
	}

	/**
	 * ��׼�ֶ���Ŀ
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getStdFldItems(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.STANDARD_FIELD,
				new String[]{"�ֶ���","�ֶ�����","�ֶ�����","�ֵ���Ŀ","˵��","�ֶ�˵��"}, 
				new EStructuralFeature[]{
					MetadataPackage.Literals.NAMED_ELEMENT__NAME, 
					MetadataPackage.Literals.STANDARD_FIELD__DATA_TYPE,
					MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
					MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE,
					MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION,
					MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
				true, 
				ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY, resource);
		return items;
	}
	
	/**
	 * ҵ����������
	 * @param resource
	 * @param table
	 * @param monitor
	 * @throws ARESModelException
	 */
	public void importBizType(IARESResource resource, List<List<String>> table,BusinessDataTypeList list, IProgressMonitor monitor) throws ARESModelException {
		List<EObject> items = getBizTypeItems(resource, table);
		
		list.getItems().addAll((Collection<? extends BusinessDataType>) items);
		if(null != monitor) {
			resource.save(list, true, new SubProgressMonitor(monitor, 25));
		}else {
			resource.save(list, true, monitor);
		}
	}

	/**
	 * �����
	 * @param resource
	 * @param table
	 * @param list
	 * @param monitor
	 * @throws ARESModelException
	 */
	public void importErrorNo(IARESResource resource, List<List<String>> table,ErrorNoList list, IProgressMonitor monitor) throws ARESModelException {
		List<EObject> items = getErrorNoItems(resource, table);
		
		list.getItems().addAll((Collection<? extends ErrorNoItem>) items);
		if(null != monitor) {
			resource.save(list, true, new SubProgressMonitor(monitor, 25));
		}else {
			resource.save(list, true, monitor);
		}
	}
	
	/**
	 * ��ȡ�������Ŀ
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getErrorNoItems(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.ERROR_NO_ITEM,
				new String[] { "�����","������ʾ��Ϣ", "��������", "���󼶱�","˵��" },
				new EStructuralFeature[] {
					MetadataPackage.Literals.ERROR_NO_ITEM__NO,
					MetadataPackage.Literals.ERROR_NO_ITEM__MESSAGE,
					MetadataPackage.Literals.NAMED_ELEMENT__NAME,
					MetadataPackage.Literals.ERROR_NO_ITEM__LEVEL,
					MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION},
				true, 
				ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY, resource);
		return items;
	}
	
	/**
	 * ��ȡ�������Ŀ(����ģʽ)
	 * @param resource
	 * @param table
	 * @return
	 */
	public MetadataResourceData<ErrorNoItem> getErrorNo(MetadataResourceData ower , IARESResource resource,
			List<List<String>> table) {
		POIUtils.importExcelStringTableForError(table, MetadataPackage.Literals.ERROR_NO_ITEM,
				new String[] { "�����","������ʾ��Ϣ", "��������", "���󼶱�","˵��" },
				new EStructuralFeature[] {
					MetadataPackage.Literals.ERROR_NO_ITEM__NO,
					MetadataPackage.Literals.ERROR_NO_ITEM__MESSAGE,
					MetadataPackage.Literals.NAMED_ELEMENT__NAME,
					MetadataPackage.Literals.ERROR_NO_ITEM__LEVEL,
					MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION},
				true, 
				ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY,ower , resource);
		return ower;
	}
	
	/**
	 * �û�����
	 * @param resource
	 * @param table
	 * @param monitor
	 * @throws ARESModelException
	 */
	public void importConstant(IARESResource resource, List<List<String>> table,ConstantList list, IProgressMonitor monitor) throws ARESModelException {
		List<EObject> items = getConsItem(resource, table);
		
		list.getItems().addAll((Collection<? extends ConstantItem>) items);
		if(null != monitor) {
			resource.save(list, true, new SubProgressMonitor(monitor, 25));
		}else {
			resource.save(list, true, monitor);
		}
	}
	
	/**
	 * ��ȡ�û�������Ŀ
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getConsItem(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.CONSTANT_ITEM,
				new String[] {"����","����ֵ","˵��" },
				new EStructuralFeature[] {
					MetadataPackage.Literals.NAMED_ELEMENT__NAME,
					MetadataPackage.Literals.CONSTANT_ITEM__VALUE,
					MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION},
				true, 
				ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY, resource);
		return items;
	}
	
	/**
	 * ��ȡҵ������������Ŀ
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getBizTypeItems(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.BUSINESS_DATA_TYPE,
				new String[]{"������","��������","��׼����","����","����","Ĭ��ֵ","˵��"}, 
				new EStructuralFeature[]{
					MetadataPackage.Literals.NAMED_ELEMENT__NAME, 
					MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
					MetadataPackage.Literals.BUSINESS_DATA_TYPE__STD_TYPE,
					MetadataPackage.Literals.BUSINESS_DATA_TYPE__LENGTH,
					MetadataPackage.Literals.BUSINESS_DATA_TYPE__PRECISION,
					MetadataPackage.Literals.BUSINESS_DATA_TYPE__DEFAULT_VALUE,
					MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
				true, 
				ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY, resource);
		return items;
	}
	
	/**
	 * �����׼��������
	 * @param resource
	 * @param table
	 * @param monitor
	 * @throws ARESModelException 
	 */
	public void importStdType(IARESResource resource, List<List<String>> table,StandardDataTypeList list, IProgressMonitor monitor) throws ARESModelException {
		List<EObject> items = getStdTypeItems(resource, table);
		
		list.getItems().addAll( (Collection<? extends StandardDataType>) items);
		if(null != monitor) {
			resource.save(list, true, new SubProgressMonitor(monitor, 25));
		}else {
			resource.save(list, true, monitor);
		}
	}

	/**
	 * ��ȡ��׼����������Ŀ
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getStdTypeItems(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.STANDARD_DATA_TYPE,
				new String[]{"������", "����","˵��"}, 
				new EStructuralFeature[]{MetadataPackage.Literals.NAMED_ELEMENT__NAME, MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME, MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
				true, 
				languageTitles, languageIds, resource);
		return items;
	}
	
	/**
	 * ����Ĭ��ֵ
	 * @param resource Ĭ��ֵ
	 * @param table	Ĭ��ֵ�б�
	 * @param monitor
	 * @throws ARESModelException
	 */
	public void importDefValue(IARESResource resource, List<List<String>> table,TypeDefaultValueList list, IProgressMonitor monitor) throws ARESModelException {
		
		List<EObject> items = getDefValueItems(resource, table);
		
		list.getItems().addAll((Collection<? extends TypeDefaultValue>) items);
		if(null != monitor) {
			resource.save(list, true, new SubProgressMonitor(monitor, 25));
		}else {
			resource.save(list, true, monitor);
		}
	}

	/**
	 * ��ȡĬ��ֵ��Ŀ
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getDefValueItems(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.TYPE_DEFAULT_VALUE,
				new String[]{"Ĭ��ֵ��", "����", "˵��"}, 
				new EStructuralFeature[]{MetadataPackage.Literals.NAMED_ELEMENT__NAME, MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME, MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
				true, 
				languageTitles, languageIds, resource);
		return items;
	}
	
	/**
	 * ��ȡҵ�������
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getBizPropertyConfigs(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.BIZ_PROPERTY_CONFIG,
				new String[]{"���","����","������","˵��"}, 
				new EStructuralFeature[]{MetadataPackage.Literals.NAMED_ELEMENT__NAME,MetadataPackage.Literals.BIZ_PROPERTY_CONFIG__ENAME, MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME, MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
				true, 
				languageTitles, languageIds, resource);
		return items;
	}

	/**
	 * ��ȡ����ŷ�Χ
	 * @param resource
	 * @param table
	 * @return
	 */
	public List<EObject> getIdRangeItems(IARESResource resource,
			List<List<String>> table) {
		List<EObject> items = POIUtils.importExcelStringTable(table, MetadataPackage.Literals.ID_RANGE_ITEM,
				new String[]{"ģ��·��"}, 
				new EStructuralFeature[]{MetadataPackage.Literals.NAMED_ELEMENT__NAME}, 
				true, 
				new String[]{}, new String[]{}, resource);
		return items;
	}
	
	/**
	 * ��װ����ʷ�޶���¼
	 * 
	 * @param resource
	 * @param table
	 * @return
	 */
	public static List<EObject> getRevHisesInfos(IARESResource resource,
			List<List<String>> table){
		List<EObject> items = POIUtils.importExcelStringTable(table, CorePackage.Literals.REVISION_HISTORY,
				new String[] {"�޸İ汾", "�޸�����","�޸�����","�޸�ԭ��", "�޸ĵ�","������","������","��ע"},
				new EStructuralFeature[] {
						CorePackage.Literals.REVISION_HISTORY__VERSION,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED_DATE,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED_REASON,
						CorePackage.Literals.REVISION_HISTORY__ORDER_NUMBER,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED_BY,
						CorePackage.Literals.REVISION_HISTORY__CHARGER,
						CorePackage.Literals.REVISION_HISTORY__COMMENT}, 
				false, 
				languageTitles, languageIds, resource);
		return items;
	}
	
	
}
