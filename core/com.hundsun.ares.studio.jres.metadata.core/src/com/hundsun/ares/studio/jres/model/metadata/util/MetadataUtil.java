/**
 * Դ�������ƣ�MetadataUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.context.INamespaceHelper;
import com.hundsun.ares.studio.core.model.util.Pair;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.model.metadata.BizPropertyConfig;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeBusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeStandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeStandardField;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeTypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DeBusinessDataTypeImpl;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DeConstantItemImpl;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DeDictionaryTypeImpl;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DeErrorNoItemImpl;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DeStandardDataTypeImpl;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DeStandardFieldImpl;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.impl.DeTypeDefaultValueImpl;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * Ԫ���ݵĹ�����
 * @author gongyf
 *
 */
public class MetadataUtil {
	
	private static Logger logger = Logger.getLogger(MetadataUtil.class);
	
	/**
	 * ��ָ����Ԫ������Ŀ�����ɸ�����ʹ�õĶ���
	 * @param item
	 * @param resource
	 * @return
	 */
	public static DeTypeDefaultValue decrypt(TypeDefaultValue item, IARESResource resource) {
		DeTypeDefaultValue result = new DeTypeDefaultValueImpl(item, resource);
		return result;
	}
	
	/**
	 * ��ָ����Ԫ������Ŀ�����ɸ�����ʹ�õĶ���
	 * @param item
	 * @param resource
	 * @return
	 */
	public static DeStandardDataType decrypt(StandardDataType item, IARESResource resource) {
		DeStandardDataType result = new DeStandardDataTypeImpl(item, resource);
		return result;
	}
	
	/**
	 * ��ָ����Ԫ������Ŀ�����ɸ�����ʹ�õĶ���
	 * @param item
	 * @param resource
	 * @return
	 */
	public static DeBusinessDataType decrypt(BusinessDataType item, IARESResource resource) {
		DeBusinessDataType result = new DeBusinessDataTypeImpl(item, resource);
		return result;
	}
	
	/**
	 * ��ָ����Ԫ������Ŀ�����ɸ�����ʹ�õĶ���
	 * @param item
	 * @param resource
	 * @return
	 */
	public static DeStandardField decrypt(StandardField item, IARESResource resource) {
		DeStandardField result = new DeStandardFieldImpl(item, resource);
		return result;
	}
	
	/**
	 * ��ָ����Ԫ������Ŀ�����ɸ�����ʹ�õĶ���
	 * @param item
	 * @param resource
	 * @return
	 */
	public static DeErrorNoItem decrypt(ErrorNoItem item, IARESResource resource) {
		DeErrorNoItem result = new DeErrorNoItemImpl(item, resource);
		return result;
	}
	
	/**
	 * ��ָ����Ԫ������Ŀ�����ɸ�����ʹ�õĶ���
	 * @param item
	 * @param resource
	 * @return
	 */
	public static DeConstantItem decrypt(ConstantItem item, IARESResource resource) {
		DeConstantItem result = new DeConstantItemImpl(item, resource);
		return result;
	}
	
	/**
	 * ��ָ����Ԫ������Ŀ�����ɸ�����ʹ�õĶ���
	 * @param item
	 * @param resource
	 * @return
	 */
	public static DeDictionaryType decrypt(DictionaryType item, IARESResource resource) {
		DeDictionaryType result = new DeDictionaryTypeImpl(item, resource);
		return result;
	}

	public static Pair<MetadataItem, IARESResource> findMetadataItem(String refId, String resType, IARESProject project) {
		if (StringUtils.isBlank(refId)) {
			return null;
		}
		
		String name = INamespaceHelper.INSTANCE.removeNamespace(refId);
		ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(project, resType, name, true);
		if (ref != null) {
			return new Pair<MetadataItem, IARESResource>((MetadataItem)ref.getObject(), ref.getResource());
		}
		return null;
	}
	
	/**
	 * ��ȡδ�������
	 * @param data
	 * @return
	 */
	public static List<MetadataItem> getUncategorizedItems(MetadataResourceData<?> data) {
		Set<MetadataItem> items = new LinkedHashSet<MetadataItem>(data.getItems()) ;
		Map<EObject, Collection<Setting>> settingMaps = EcoreUtil.UsageCrossReferencer.findAll(items, data.getRoot());
		
		for (Entry<EObject, Collection<Setting>> entry : settingMaps.entrySet()) {
			if (!entry.getValue().isEmpty()) {
				items.remove(entry.getKey());
			}
			
		}
		
		return new ArrayList<MetadataItem>(items);
	}
	
	/**
	 * ��ȡ��Դ����
	 * @param project
	 * @return
	 */
	public static List<BizPropertyConfig> getBizProperties(IARESProject project) {
		ReferenceManager manager = ReferenceManager.getInstance();
		List<ReferenceInfo> infos = manager.getReferenceInfos(project, IMetadataRefType.BizPropertyConfig, false);
		List<BizPropertyConfig> bizPropertyConfigs = new ArrayList<BizPropertyConfig>();
		for (ReferenceInfo info : infos) {
			Object obj = info.getObject();
			if (obj instanceof BizPropertyConfig) {
				bizPropertyConfigs.add((BizPropertyConfig) obj);
			}
		}
		return bizPropertyConfigs;
	}
	
	public static BizPropertyConfig getBizPropertyConfig(String id, IARESProject project) {
		ReferenceManager manager = ReferenceManager.getInstance();
		ReferenceInfo ref = manager.getFirstReferenceInfo(project, IMetadataRefType.BizPropertyConfig, id, false);
		if (ref != null) {
			return (BizPropertyConfig) ref.getObject();
		}
		return null;
	}
	
	/**
	 * ��ȡָ��Ԫ������Ŀ�ķ��飬���޷���ʱ���ؿ��б�
	 * @param item
	 * @return
	 */
	public static EList<MetadataCategory> getCategories(MetadataItem item) {
		EList<MetadataCategory> categories = new BasicEList<MetadataCategory>();
		if(item.getParent() != null){
			for (TreeIterator<EObject> iterator = EcoreUtil.getAllContents(item.getParent().getRoot(), true); iterator.hasNext(); ) {
				EObject obj = iterator.next();
				if (obj instanceof MetadataCategory) {
					// �������ڷ���
					if (((MetadataCategory) obj).getItems().contains(item)) {
						categories.add((MetadataCategory) obj);
					}
				}
			}
		}
		return categories;
	}
	
	/**
	 * �ж��Ƿ���һ������ʹ������ֵ������<BR>
	 * ����ʹ������ֵ��������Ԫ������Ŀ�У���Ԫ���ݴ�����������Ԫ����ʱ��չ�ֵ�ֵӦ�������õ����ݣ������Ǳ�������ݡ�
	 * ����ͨ������ʱ����������ǲ��ܱ༭�ġ�
	 * 
	 * @param item
	 * @param feature
	 * @return
	 */
	public static boolean isReferencableFeature(MetadataItem item, EStructuralFeature feature) {
		if (feature == MetadataPackage.Literals.NAMED_ELEMENT__NAME 
				|| feature == MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME 
				|| feature == MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION
				|| feature == MetadataPackage.Literals.METADATA_ITEM__REF_ID) {
			return false;
		}
		
		return true;
	}
	
	public static String getReferenceTypeByEClass(EClass clazz) {
		if (MetadataPackage.Literals.TYPE_DEFAULT_VALUE == clazz) {
			return IMetadataRefType.DefValue;
		} else if (MetadataPackage.Literals.STANDARD_DATA_TYPE == clazz) {
			return IMetadataRefType.StdType;
		} else if (MetadataPackage.Literals.BUSINESS_DATA_TYPE == clazz) {
			return IMetadataRefType.BizType;
		} else if (MetadataPackage.Literals.STANDARD_FIELD == clazz) {
			return IMetadataRefType.StdField;
		} else if (MetadataPackage.Literals.DICTIONARY_TYPE == clazz) {
			return IMetadataRefType.Dict;
		} else if (MetadataPackage.Literals.ERROR_NO_ITEM == clazz) {
			return IMetadataRefType.ErrNo;
		} else if (MetadataPackage.Literals.CONSTANT_ITEM == clazz) {
			return IMetadataRefType.Const;
		}else if (MetadataPackage.Literals.MENU_ITEM == clazz) {
			return IMetadataRefType.Menu;
		}
		return "";
	}
	
	/**
	 * �ж϶����Ƿ�����������������
	 * @param item
	 * @return
	 */
	public static boolean isReferencingItem(MetadataItem item) {
		return !StringUtils.isBlank(item.getRefId());
	}
	
	/**
	 * �ж�ָ�������Ƿ�ʹ��Ԫ������������
	 * @param resource
	 * @return
	 */
	public static boolean isUseRefFeature(IARESResource resource) {
//		try {
//			MDModuleCommonProperty property = (MDModuleCommonProperty) ObjectUtils.defaultIfNull(
//					bundle.getInfo().getMap().get(IMDConstant.MODULE_METADATA_EXTEND_PROPERTY_KEY), 
//					MetadataFactory.eINSTANCE.createMDModuleCommonProperty());
//			return property.isUseRefFeature();
//		} catch (Exception e) {
//			logger.error("��ȡ��Ŀ���Դ���", e);
//		}
		// FIXME ĿǰԪ�����������Ի�������
		if(resource.getType().equals(IMetadataResType.Menu)){
			return true;
		}
		return false;
	}
	
	/**
	 * ��ȡ�������ö��󣬵�����û�н�������ʱ���������������ò�����ʱ������null,
	 * ���������ж���Ŀ���ã������Ƿ������������Զ��������ò���
	 * @param <T>
	 * @param item
	 * @param master
	 * @return
	 * @throws CircularReferenceException
	 */
	public static <T extends MetadataItem> Pair<T, IARESResource> resolve(T item, IARESResource master) throws CircularReferenceException {
		return resolve(item, master, null);
	}
	
	/**
	 * ���޷��ҵ����ö���ʱ��������null�������������
	 * @param <T>
	 * @param item
	 * @param master
	 * @return
	 * @throws CircularReferenceException
	 */
	public static <T extends MetadataItem> Pair<T, IARESResource> defaultResolve(T item, IARESResource master) throws CircularReferenceException {
		Pair<T, IARESResource> result = resolve(item, master, null);
		if (result == null) {
			result = new Pair<T, IARESResource>(item, master);
		}
		return result;
	}
	
	/**
	 * ��һ�����ϵĶ���ת��Ϊ���ú�Ķ����б�
	 * @param <T>
	 * @param items
	 * @param master
	 * @return
	 * @throws CircularReferenceException
	 */
	public static <T extends MetadataItem> List<T> resolveList(Collection<T> items, IARESResource master) throws CircularReferenceException {
		List<T> list = new ArrayList<T>();
		for (T item : items) {
			list.add(defaultResolve(item, master).first);
		}
		return list;
	}
	
	private static <T extends MetadataItem> Pair<T, IARESResource> resolve(T item, IARESResource master, Stack<MetadataItem> itemPath) throws CircularReferenceException {
		if (item == null) {
			return null;
		}
		
		if (itemPath == null) {
			itemPath = new Stack<MetadataItem>();
		}
		
		if (itemPath.contains(item)) {
			MetadataItem lastItem = itemPath.peek();
			throw new CircularReferenceException(String.format("ѭ�����ã�[%s]->[%s]", lastItem.getName(), item.getName()) );
		}
		
		itemPath.push(item);
		
		try {
			if (isReferencingItem(item)) {
				// �������õĶ���
				
				String ns = INamespaceHelper.INSTANCE.getSlaveNamespace(master, item.getRefId());
				String name = INamespaceHelper.INSTANCE.removeNamespace(item.getRefId());
				if (StringUtils.equals(INamespaceHelper.INSTANCE.getResourceNamespace(master), ns) ) {
					// ���õ��ǵ�ǰ��Դ�е�
					EObject listData = item.eContainer();
					if (listData != null && listData instanceof MetadataResourceData<?>) {
						EObject obj = ((MetadataResourceData<?>)listData).find(name);
						if (obj != null) {
							 return resolve((T)obj, master, itemPath);
						}
					}
					
					// �����ǰģ����δ�ҵ�������Ϊ�Ǽ̳е���Ŀ�������ñ��л�ȡ
					ReferenceInfo referenceInfo = ReferenceManager.getInstance().getFirstReferenceInfo(master.getARESProject(), getReferenceTypeByEClass(item.eClass()), name, true);
					if(referenceInfo!=null){
						IARESResource res = referenceInfo.getResource();
						MetadataItem owner = (MetadataItem) referenceInfo.getObject();
						
						return resolve((T)owner, res, itemPath);
					}
					
					// �����ò����ڵĴ���
					return null;
				} else {
					ReferenceInfo  referenceInfo = ReferenceManager.getInstance().getFirstReferenceInfo(master.getARESProject(), getReferenceTypeByEClass(item.eClass()), name, true);
					if(referenceInfo!=null){
						IARESResource res = referenceInfo.getResource();
						MetadataItem owner = (MetadataItem) referenceInfo.getObject();
						
						return resolve((T)owner, res, itemPath);
					}
					return null;
					
				}
				
			} else {
				return new Pair<T, IARESResource>(item, master);
			}
		} finally {
			itemPath.pop();
		}
	}


}
