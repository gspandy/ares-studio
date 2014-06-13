/**
 * Դ�������ƣ�PDMHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����pdm���빫�ô���
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.jres.database.pdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableResource;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.util.DatabaseUtil;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;

/**
 * pdm�����İ�����
 * @author liaogc
 *
 */
public class PDMHelper {
	/**
	 * ����ģ������
	 * @param root ģ���
	 * @param moduleName ģ����
	 * @param moduleChineseName ģ��������
	 * @param path //·��
	 * @throws CoreException
	 */
	public static void createModuleProperty(IARESModuleRoot root,String moduleName,String moduleChineseName,String path) throws CoreException{

		IARESModule createdModule = root.findModule(path);
		if (createdModule == null) {
			createdModule = root.createModule(path);
		}
		IARESResource resource = createdModule
				.findResource(IARESModule.MODULE_PROPERTY_FILE);
		if (resource == null) {
			ModuleProperty property = new ModuleProperty();
			property.setValue(ICommonModel.CNAME, moduleChineseName);
			property.setValue(ICommonModel.NAME, moduleName);
			createdModule.createResource(IARESModule.MODULE_PROPERTY_FILE, property);
		}
		
	}
	
	/**
	 * ����һ��Ψһ���ֶ���
	 * @param preffix ǰ׺
	 * @param exclusionNames �����name
	 * @return
	 */
	public static String getUniqueName(String preffix,List<String> exclusionNames){
		for(int i=0;i<Integer.MAX_VALUE;i++){
			String name = preffix+i;
			if(!exclusionNames.contains(name)){
				return name;
			}
		}
		return "";
	}
	/**
	 * �����µ�ҵ�����������Ƿ��ھɵ�ҵ���������͵�λ��-1:����ʷ,��׼��������,����,������ͬ��������ͬ
	 * @param businessDataTypeList ҵ�����������б�
	 * @param businessDataType ҵ����������
	 * @return ����ҵ������������ҵ�����������б��е�λ��,�����������ҵ�������б����򷵻�-1
	 */
	public static int indexOfBusinessDataType(List<BusinessDataType> businessDataTypeList,BusinessDataType businessDataType){
		if(businessDataTypeList==null || businessDataTypeList.size()==0){
			return -1;
		}
		for(int i =0;i<businessDataTypeList.size();i++){
			BusinessDataType oldBusinessDataType = businessDataTypeList.get(i);
			if(StringUtils.equals(businessDataType.getName(), oldBusinessDataType.getName())
					//&& StringUtils.equals(businessDataType.getChineseName(), oldBusinessDataType.getChineseName())
					&& StringUtils.equals(businessDataType.getStdType(), oldBusinessDataType.getStdType())
					&& StringUtils.equals(businessDataType.getLength(), oldBusinessDataType.getLength())
					&& StringUtils.equals(businessDataType.getPrecision(), oldBusinessDataType.getPrecision())){
				return i;
			}
		}
		return -1;
		
	}
	
	/**
	 * �����Ƿ�����ͬ�ı�׼�ֶ�:�ֶ���,������,��������ͬ����ͬ
	 * @param standardFieldList ��׼�ֶ��б�
	 * @param srcStandardField �ֶ�
	 * @return ���ر�׼�ֶ��ڱ�׼�ֶ����б��е�λ��,����������ڱ�׼�ֶ����б����򷵻�-1
	 */
	public static int indexOfStandardField(List<StandardField> standardFieldList,StandardField srcStandardField){
		if(standardFieldList==null || standardFieldList.size()==0)return -1;
		for(int i=0;i<standardFieldList.size();i++){
			StandardField destStandardField = standardFieldList.get(i);
			if(StringUtils.equalsIgnoreCase(srcStandardField.getName(), destStandardField.getName()) && StringUtils.equalsIgnoreCase(srcStandardField.getDataType(), destStandardField.getDataType()) &&StringUtils.equalsIgnoreCase(srcStandardField.getChineseName(), destStandardField.getChineseName() )){
				return i;
			}
		}
		return -1;
	}
	/**
	 * ��ѯ��ǰ�����Դ��ģ���
	 * @param tableResource ����Դ
	 * @return  subSystem ��ϵͳ����
	 */
	 private static String getSubSystem(IARESResource tableResource){
		if(tableResource==null){
			return "";
		}
		IARESModule module =tableResource.getModule();
		IARESModule parentModule = module;
		
		while(parentModule!=null){
			module = parentModule;
			parentModule =parentModule.getParentModule();
			
		}
		 return module.getElementName();	
			
	}
	 
	 /**
		 * ��ѯģ���
		 * @param IARESModule ģ��
		 * @return  subSystem ��ϵͳ����
		 */
		 public static String getSubSystem(IARESModule module){
			if(module==null){
				return "";
			}
			IARESModule parentModule = module;
			
			while(parentModule!=null){
				module = parentModule;
				parentModule =parentModule.getParentModule();
				
			}
			 return module.getElementName();	
				
		}

	/**
	 * ��table��Դ��������,��ͬ��ϵͳ���ܴ���ͬ�����
	 * 
	 * @param tabInfoList
	 * @return List<PDMTableResource>һ��map,�Ա���Ϊkey
	 * @throws ARESModelException
	 */
		public static Map<String ,List<PDMTableResource>> getTableResourceMap(List<ReferenceInfo> tabInfoList) throws ARESModelException{
			Map<String,List<PDMTableResource>>tableResourceMap = new HashMap<String,List<PDMTableResource>>();
			for(ReferenceInfo referenceInfo:tabInfoList){
				IARESResource tableResource =referenceInfo.getResource();
				TableResourceData tableInfo = tableResource.getInfo(TableResourceData.class);
				String subSystem = getSubSystem(tableResource);//��ȡ����Դ��Ӧ����ϵͳ
				String tableName =tableInfo.getName();
				PDMTableResource pdmTableResource = new PDMTableResource(tableResource,tableInfo,subSystem);
					if(tableResourceMap.get(tableName)!=null){
						tableResourceMap.get(tableName).add(pdmTableResource);
					}else{
						List<PDMTableResource> standardFieldCategory = new ArrayList<PDMTableResource>();
						standardFieldCategory.add(pdmTableResource);
						tableResourceMap.put(tableName, standardFieldCategory);
					}
				
			}
			return tableResourceMap;
		}
		
		/**
		 * ���������������͹���Ĭ��ֵ
		 * @param businessDataTypeList //Ĭ��ֵ�����б�
		 * @param typeDefaultValueList //Ĭ��ֵ�б�
		 */
		public static void associateDefaultValueByStdType(List<BusinessDataType>  businessDataTypeList,List<TypeDefaultValue> typeDefaultValueList){
			for(BusinessDataType businessDataType:businessDataTypeList){
				String bStdType = businessDataType.getStdType();
				for(TypeDefaultValue typeDefaultValue:typeDefaultValueList){
					if(StringUtils.isBlank(businessDataType.getDefaultValue())){
						String typeDefaultValueName  = typeDefaultValue.getName();//ȡ��Ĭ��ֵ��
						if(StringUtils.startsWithIgnoreCase(bStdType, "std")){//���ҵ�����������Ǳ�׼��
							String prefix = StringUtils.substring(bStdType,0 ,3);
							String suffix = StringUtils.substring(bStdType, 3) ;//ȡ��ҵ���������͵�3λ�Ժ����Ϣ
							if(StringUtils.startsWithIgnoreCase(typeDefaultValueName, suffix)){//���Ĭ��ֵ��suffixΪǰǰ׺
								businessDataType.setDefaultValue(typeDefaultValueName);//����ҵ���������͵�Ĭ��ֵ
								break;
							}
						}
					}
				}
			}
		}
		
		/**
		 * ���� ��׼�ֶ��������ʵ����
		 * @param name
		 * @return ��׼�ֶζ�Ӧ����ʵ����
		 */
		public static String getRealDataType(IARESProject project ,String name,String databaseType){
				String dataType =StringUtils.EMPTY;
				StandardDataType stdType = null;
				if(StringUtils.isBlank(databaseType)){
					databaseType = "oracle";
				}
				try {
					stdType = MetadataServiceProvider.getStandardDataTypeOfStdFieldByName(project, name);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			
				BusinessDataType busType = null;
				try {
					busType = MetadataServiceProvider.getBusinessDataTypeOfStdFieldByName(project, name);
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
				if((stdType != null)  && ( busType!= null))//��׼�ֶ�
				{
					dataType = stdType.getValue(databaseType);
					String  length = StringUtils.defaultIfBlank(busType.getLength(), "0");
					String precision =StringUtils.defaultIfBlank(busType.getPrecision(),"0");
					
					if(StringUtils.indexOf(dataType, "$L")>-1){
						dataType = dataType.replace("$L", length);
					}
					if(StringUtils.indexOf(dataType, "$P")>-1){
						dataType = dataType.replace("$P", precision);
					}
					
				}
			
			
			return dataType;
		}
		
		
		/**
		 * ���ص���ģʽ(����Ŀ�����л��)
		 * @return
		 */
		public static boolean getImportMode(IARESProject project){
			return (new DatabaseUtil()).isNonStdFiledAllowed(project);
		}
		
		
}
