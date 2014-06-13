/**
 * Դ�������ƣ�SubSystemMergeHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMBusinessDataType;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMStandardField;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableResource;
import com.hundsun.ares.studio.jres.database.ui.model.MergePojo;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author liaogc
 * ��ϵͳ�ϲ�������
 */
public class PDMSubSystemMergeHelper {

	private IARESProject project;
	public PDMSubSystemMergeHelper(IARESProject project){
		this.project = project;
		
	}
	/**
	 * �ϲ���ͬ��ҵ����������
	 * @param pdmBusinessDataTypeList
	 * @return
	 */
	public List<PDMBusinessDataType> mergeBusinessDataType(List<PDMBusinessDataType> pdmBusinessDataTypeList){
		Map<String,List<PDMBusinessDataType>>pdmSystemanalyseStaneardFieldMap = getBusinessDataTypeCategory(pdmBusinessDataTypeList);

		 List<PDMBusinessDataType> mergeList = new  ArrayList<PDMBusinessDataType>();
		 List<String> exclusionNames = new ArrayList<String>();
		 List<String> businessDataTypeNames = new ArrayList<String>(pdmSystemanalyseStaneardFieldMap.keySet());
		 Collections.sort(businessDataTypeNames,new NameSorter());
		 for(String businessDataTypeName:businessDataTypeNames){
			 List<PDMBusinessDataType> subBusinessDataTypeList = pdmSystemanalyseStaneardFieldMap.get(businessDataTypeName);
			 if(subBusinessDataTypeList.size()==1){//���û����ͬ��ҵ����������
				 if(!exclusionNames.contains(businessDataTypeName)){//���ҵ���������Ͳ���ͻ
					 mergeList.add(subBusinessDataTypeList.get(0));
					 exclusionNames.add(businessDataTypeName);
				 }else{//��ͻ��ȡһ��Ψһ��ҵ������������
					 String newName = PDMHelper.getUniqueName(businessDataTypeName,exclusionNames);
					 subBusinessDataTypeList.get(0).setTypeName(newName);
					 mergeList.add(subBusinessDataTypeList.get(0));
					 exclusionNames.add(newName);
				 }
				
				
			 }else if(subBusinessDataTypeList.size()>1){
				 List<PDMBusinessDataType> subUniqueBusinessDataTypes = new ArrayList<PDMBusinessDataType>();
				 for(int i = 0;i < subBusinessDataTypeList.size();i++){
					 PDMBusinessDataType businessDataType1 = subBusinessDataTypeList.get(i);
					 if(!subUniqueBusinessDataTypes.contains(businessDataType1)){
						 boolean found = false;
						 for(int j=0;j<subUniqueBusinessDataTypes.size();j++){
							 PDMBusinessDataType businessDataType2 = subUniqueBusinessDataTypes.get(j);
							 if(isTheBusinessDataType(businessDataType1,businessDataType2)){//�ϲ���ͬ��ҵ����������
								 found = true;
								 if(businessDataType1.getBelongStandardFieldList().size()>0){//���ӵ�и�ҵ���������ͱ�׼�ֶ�
									 for(String standardFieldName:businessDataType1.getBelongStandardFieldList()){
										 if(!businessDataType2.getBelongStandardFieldList().contains(standardFieldName)){ 
											 businessDataType2.getBelongStandardFieldList().add(standardFieldName);
										} 
									}

								}
								 break;
								
							 }
							 
						 }
						 if(!found){
							 subUniqueBusinessDataTypes.add(businessDataType1);
						 }
						
					 }
					
				 }
				 
				 for(PDMBusinessDataType field:subUniqueBusinessDataTypes){
					 String name = field.getOldTypeName();
					 if(!exclusionNames.contains(name)){//���ҵ���������Ͳ���ͻ
						 mergeList.add(field);
						 exclusionNames.add(name);
					 }else{
						 String newName = PDMHelper.getUniqueName(name,exclusionNames);//�����׼�ֶγ�ͻ,���ȡһ��Ψһ��ҵ������������
						 field.setTypeName(newName);
						 mergeList.add(field);
						 exclusionNames.add(newName);
					 }
				 }
			 }
			 
		 }
		return mergeList;
		
	
		
	}
	
	/**
	 * �ϲ���ͬ�ı�׼�ֶ�
	 * @param pmdStandardFieldList
	 * @return
	 */
	public List<PDMStandardField> mergeStandardField(List<PDMStandardField> pmdStandardFieldList){
		Map<String,List<PDMStandardField>>pdmSystemanalyseStaneardFieldMap = getStandardFieldCategory(pmdStandardFieldList);

		 List<PDMStandardField> mergeList = new  ArrayList<PDMStandardField>();
		 List<String> exclusionNames = new ArrayList<String>();
		 List<String> fieleNames = new ArrayList<String>(pdmSystemanalyseStaneardFieldMap.keySet());
		 Collections.sort(fieleNames,new NameSorter());
		 for(String fieldName:fieleNames){
			 List<PDMStandardField> fields = pdmSystemanalyseStaneardFieldMap.get(fieldName);
			 if(fields.size()==1){//���û����ͬ�ı�׼�ֶ�
				 if(!exclusionNames.contains(fieldName)){//�����׼�ֶβ���ͻ
					 fields.get(0).setGenName(fieldName);
					 mergeList.add(fields.get(0));
					 exclusionNames.add(fieldName);
					 fields.get(0).setChanged(true);
				 }else{//��ͻ��ȡһ��Ψһ�ı�׼�ֶ���
					 String genName = PDMHelper.getUniqueName(fieldName,exclusionNames);
					 fields.get(0).setGenName(genName);
					 mergeList.add(fields.get(0));
					 exclusionNames.add(genName);
					 fields.get(0).setChanged(true);
				 }
				
				
			 }else if(fields.size()>1){//�ϲ���ͬ�ı�׼�ֶ�
				 List<PDMStandardField> subUniqueFeilds = new ArrayList<PDMStandardField>();
				 for(int i = 0;i < fields.size();i++){
					 PDMStandardField field1 = fields.get(i);
					 if(!subUniqueFeilds.contains(field1)){
						 boolean found = false;
						 for(int j=0;j<subUniqueFeilds.size();j++){
							 PDMStandardField field2 = subUniqueFeilds.get(j);
							 if(isTheSameStandardField(field1,field2)){
								 found = true;
								 if(field1.getBolongSubSystemList().size()>0){//��Ӵ˱�׼�ֶ������ĸ���ϵͳ
									String subSystem =  field1.getBolongSubSystemList().get(0);
									if(!field2.getBolongSubSystemList().contains(subSystem)){ 
										field2.getBolongSubSystemList().add(subSystem);
									}
								 }
								 if(field1.getBelongTableList().size()>0){
									 for(String tableName:field1.getBelongTableList()){//��ӱ�׼�ֶ����Ĺ�ϵ
										 if(!field2.getBelongTableList().contains(tableName)){ 
												field2.getBelongTableList().add(tableName);
											} 
									}

								}
								 break;
								
							 }
							 
						 }
						 if(!found){
							 subUniqueFeilds.add(field1);
						 }
						
					 }
					
				 }
				 
				 for(PDMStandardField field:subUniqueFeilds){
					 String name = field.getOldName();
					 if(!exclusionNames.contains(name)){//�����׼�ֶβ���ͻ
						 mergeList.add(field);
						 field.setGenName(name);
						 exclusionNames.add(name);
						 field.setChanged(true);
					 }else{
						 String genName = PDMHelper.getUniqueName(name,exclusionNames);//�����׼�ֶγ�ͻ,���ȡһ��Ψһ�ı�׼�ֶ���
						 for(PDMStandardField aField:fields){
							 if(field!=aField){
								 if(isTheSameStandardField(field,aField)){
									 if(!aField.isChanged()){
										 aField.setChanged(true);
										 aField.setGenName(genName); 
									 }
								 }
							 }
						 }
						 
						 field.setGenName(genName);
						 mergeList.add(field);
						 exclusionNames.add(genName);
						 field.setChanged(true);
					 }
				 }
			 }
			 
		 }
		return mergeList;
		
	}
	
	/**
	 * ��ҵ������������Ϊkey��PDMBusinessDataType����
	 * @param pmdBusinessDataTypeList
	 * @return
	 */
	private Map<String,List<PDMBusinessDataType>>getBusinessDataTypeCategory(List<PDMBusinessDataType> pmdBusinessDataTypeList){
		Map<String,List<PDMBusinessDataType>>pdmSystemanalyseBusinessDataTypeMap = new HashMap<String,List<PDMBusinessDataType>>();
		for(PDMBusinessDataType pmdBusinessDataType:pmdBusinessDataTypeList){
			if(pdmSystemanalyseBusinessDataTypeMap.get(pmdBusinessDataType.getOldTypeName())!=null){
				pdmSystemanalyseBusinessDataTypeMap.get(pmdBusinessDataType.getOldTypeName()).add(pmdBusinessDataType);
			}else{
				List<PDMBusinessDataType> businessDataTypeCategory = new ArrayList<PDMBusinessDataType>();
				businessDataTypeCategory.add(pmdBusinessDataType);
				pdmSystemanalyseBusinessDataTypeMap.put(pmdBusinessDataType.getOldTypeName(), businessDataTypeCategory);
			}
		}
		return pdmSystemanalyseBusinessDataTypeMap;
	}
	
	/**
	 * ���ֶ���Ϊkey��PDMStandardField���з���
	 * @param pmdStandardFieldList
	 * @return
	 */
	private Map<String,List<PDMStandardField>>getStandardFieldCategory(List<PDMStandardField> pmdStandardFieldList){
		Map<String,List<PDMStandardField>>pdmSystemanalyseStaneardFieldMap = new HashMap<String,List<PDMStandardField>>();
		
		for(PDMStandardField pmdStandardField:pmdStandardFieldList){
			if(pdmSystemanalyseStaneardFieldMap.get(pmdStandardField.getOldName())!=null){
				pdmSystemanalyseStaneardFieldMap.get(pmdStandardField.getOldName()).add(pmdStandardField);
			}else{
				List<PDMStandardField> standardFieldCategory = new ArrayList<PDMStandardField>();
				standardFieldCategory.add(pmdStandardField);
				pdmSystemanalyseStaneardFieldMap.put(pmdStandardField.getOldName(), standardFieldCategory);
			}
		}
		return pdmSystemanalyseStaneardFieldMap;
	}
	
	
	
	/**
	 * ����ҵ�������������׼�ֶεĹ�ϵ,������һ��������List<PDMBusinessDataType>
	 * @param businessDataTypeList
	 * @return
	 */
	public List<PDMBusinessDataType>  analyseBusinessDataType(List<BusinessDataType> businessDataTypeList,List<StandardField> standardFieldList,List<String> subSyses){
		List<PDMBusinessDataType>pdmSystemBusinessDataTypeList = new ArrayList<PDMBusinessDataType>();
		for(BusinessDataType businessDataType:businessDataTypeList){
			PDMBusinessDataType pdmSystemBusinessDataType = createPDMBusinessDataType(businessDataType);
			for(StandardField standardField:standardFieldList){
				if(StringUtils.equals(standardField.getDataType(), businessDataType.getName())){
					if(!pdmSystemBusinessDataType.getBelongStandardFieldList().contains(standardField.getName())){
						pdmSystemBusinessDataType.getBelongStandardFieldList().add(standardField.getName());
					}
					
				}
			}
			for(String subSyse:subSyses){
				if(!pdmSystemBusinessDataType.getSubSyses().contains(subSyse)){
					pdmSystemBusinessDataType.getSubSyses().add(subSyse);
				}
			}
			pdmSystemBusinessDataTypeList.add(pdmSystemBusinessDataType);
			
			
		}
		return pdmSystemBusinessDataTypeList;
	}
	
	
	
	/**
	 * ������׼�ֶεĹ�ϵ�����ݿ��Ĺ�ϵ,������һ��������List<PDMStandardField>
	 * @param standardFieldList
	 * @param moduleList
	 * @return
	 * @throws ARESModelException 
	 */
	public List<PDMStandardField>  analyseStandardField(List<StandardField> standardFieldList,List<String> moduleList) throws ARESModelException{
		List<PDMStandardField>pdmSubSystemStaneardFieldList = new ArrayList<PDMStandardField>();
		List<ReferenceInfo> tabInfoList = ReferenceManager.getInstance().getReferenceInfos( project,IDatabaseRefType.Table, true) ;
		Map<IARESResource ,TableResourceData> tableResourceMap = getTableResourceMap(tabInfoList);
		for(StandardField standardField:standardFieldList){
			PDMStandardField pdmStandardField = createPDMStandardField(standardField);
			for(String module :moduleList){
				
				for(IARESResource tableResource:tableResourceMap.keySet()){
					if(isOfSubSystem(tableResource, module)){
						boolean find = false;
						TableResourceData tableInfo = tableResourceMap.get(tableResource);
						for(TableColumn tableColumn:tableInfo.getColumns()){
							if(StringUtils.equals(pdmStandardField.getOldName(), tableColumn.getName())){
								if(!pdmStandardField.getBelongTableList().contains(tableInfo.getName())){
									pdmStandardField.getBelongTableList().add(tableInfo.getName());
									find = true;
									//break;
								}
								
							}
						}
						if(find){
							if(!pdmStandardField.getBolongSubSystemList().contains(module)){
								pdmStandardField.getBolongSubSystemList().add(module);
							}
						}
					}
				}
				
			}
			
			pdmSubSystemStaneardFieldList.add(pdmStandardField);
		}
		tableResourceMap.clear();
		return pdmSubSystemStaneardFieldList;
	}
	
	
	public Map<IARESResource ,TableResourceData> getTableResourceMap(List<ReferenceInfo> tabInfoList) throws ARESModelException{
		Map<IARESResource ,TableResourceData> tableResourceMap = new HashMap<IARESResource ,TableResourceData>();
		for(ReferenceInfo referenceInfo:tabInfoList){
			IARESResource tableResource =referenceInfo.getResource();
			TableResourceData tableInfo = tableResource.getInfo(TableResourceData.class);
			tableResourceMap.put(tableResource, tableInfo);
		}
		return tableResourceMap;
	}
	
	
	/**
	 * ��BusinessDataTypeת����PDMBusinessDataType
	 * @param businessDataType
	 * @return
	 */
	private  PDMBusinessDataType createPDMBusinessDataType(BusinessDataType businessDataType){
		PDMBusinessDataType pdmBusinessDataType = new PDMBusinessDataType();
		pdmBusinessDataType.setComment(businessDataType.getDescription());
		pdmBusinessDataType.setDefaultValue(businessDataType.getDefaultValue());
		pdmBusinessDataType.setLength(businessDataType.getLength());
		pdmBusinessDataType.setPrecision(businessDataType.getPrecision());
		pdmBusinessDataType.setStandardTypeName(businessDataType.getStdType());
		pdmBusinessDataType.setTypeChineseName(businessDataType.getChineseName());
		//pdmBusinessDataType.setTypeName(businessDataType.getName());
		pdmBusinessDataType.setOldTypeName(businessDataType.getName());
	    return pdmBusinessDataType;
	}

	/**
	 * ��StandardFieldת����PDMStandardField
	 * @param standardField
	 * @return
	 */
	private  PDMStandardField createPDMStandardField(StandardField standardField){
	    PDMStandardField pdmStandardField = new PDMStandardField();
	    pdmStandardField.setDictId(standardField.getDictionaryType());
	    pdmStandardField.setGenBusType(standardField.getDataType());
	    pdmStandardField.setGenName(standardField.getName());
	    pdmStandardField.setOldBusType(standardField.getDataType());
	    pdmStandardField.setOldChineseName(standardField.getChineseName());
	    pdmStandardField.setOldComment(standardField.getDescription());
	    pdmStandardField.setOldName(standardField.getName());
	    return pdmStandardField;
	}
	
	
	
	/**
	 * ��PDMBusinessDataTypeת����BusinessDataType
	 * @param pdmBusinessDataType
	 * @return
	 */
	public BusinessDataType createBusinessDataType(PDMBusinessDataType pdmBusinessDataType){
		BusinessDataType businessDataType  =MetadataFactory.eINSTANCE.createBusinessDataType();
		String name = StringUtils.isNotBlank(pdmBusinessDataType.getTypeName())?pdmBusinessDataType.getTypeName():pdmBusinessDataType.getOldTypeName();
		businessDataType.setName(name);
		businessDataType.setChineseName(pdmBusinessDataType.getTypeChineseName());
		businessDataType.setDefaultValue(pdmBusinessDataType.getDefaultValue());
		businessDataType.setDescription(pdmBusinessDataType.getComment());
		businessDataType.setLength(pdmBusinessDataType.getLength());
		businessDataType.setPrecision(pdmBusinessDataType.getPrecision());
		businessDataType.setStdType(pdmBusinessDataType.getStandardTypeName());
	    return businessDataType;
	}

	/**
	 * ��PDMStandardFieldת����StandardField
	 * @param pdmStandardField
	 * @return
	 */
	public  StandardField createStandardField(PDMStandardField pdmStandardField){
		StandardField standardField =MetadataFactory.eINSTANCE.createStandardField();
		standardField.setChineseName(StringUtils.isBlank(pdmStandardField.getNewChineseName())?pdmStandardField.getOldChineseName():pdmStandardField.getNewChineseName());
		standardField.setDataType(StringUtils.isBlank(pdmStandardField.getNewBusType())?pdmStandardField.getGenBusType():pdmStandardField.getGenBusType());
		standardField.setDescription(StringUtils.isBlank(pdmStandardField.getNewComment())?pdmStandardField.getOldComment():pdmStandardField.getNewComment());
		standardField.setDictionaryType(pdmStandardField.getDictId());
		standardField.setName(StringUtils.isBlank(pdmStandardField.getGenName())?pdmStandardField.getOldName():pdmStandardField.getGenName());
		return standardField;
	}
	/**
	 * �������еĵ���Ϊ�ܵı�׼�ֶκϲ������¼�еı�׼�ֶ�,�Ժϲ��ı�׼�ֶν��в��:��ֱ����Բ�ͬ��ϵͳ������µĺϲ����ı�׼�ֶβŽ��в��)
	 * @param allPDMStaneardFieldMap
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	
	public  List<PDMStandardField> getAllPDMStandardFieldList(List<PDMStandardField> allPDMStaneardFieldList,Map<String ,List<PDMTableResource>> tableResourceMap) throws IllegalAccessException, InvocationTargetException{
		String databaseType = "oracle";
		try {
			databaseType =  project.getProjectProperty().getString("tabledir");
			int _index = -1 ;
			int dotIndex = -1;
			if((_index=StringUtils.lastIndexOf(databaseType,"_" ))>-1  && (dotIndex=StringUtils.lastIndexOf(databaseType,"."))>-1 ){
				databaseType = StringUtils.substring(databaseType,_index+1, dotIndex).toLowerCase();
			}else{
				databaseType = "oracle";
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		 List<PDMStandardField> allPDMStandardFieldList = new ArrayList<PDMStandardField>();
				 for(PDMStandardField pdmStandardField:allPDMStaneardFieldList){
					String realDataType = PDMHelper.getRealDataType(this.project,pdmStandardField.getGenName(),databaseType);
					 if(pdmStandardField.getBolongSubSystemList().size()>1){
						 for(String subSystem: pdmStandardField.getBolongSubSystemList()){
							 PDMStandardField pdmStandardField1 = new PDMStandardField();
									BeanUtilsBean.getInstance().copyProperties(pdmStandardField1,pdmStandardField);//�Ժϲ��ı�׼�ֶν��в��
									pdmStandardField1.getBolongSubSystemList().clear();
									pdmStandardField1.getBolongSubSystemList().add(subSystem);//���ֶ�ͳ��������ϵͳ
									pdmStandardField1.getBelongTableList().clear();
									List<String> belongTableList = new ArrayList<String>();
									for(String tableName:pdmStandardField.getBelongTableList()){
										List<PDMTableResource> tableResourceList = tableResourceMap.get(tableName);
										if(tableResourceList!=null){
											for(PDMTableResource tableResource:tableResourceList){
												if(StringUtils.equals(tableResource.getSubSystem(), subSystem)){
													belongTableList.add(tableName);//���ֶ�ͳ��������
												}
											}
										}
									}
									pdmStandardField1.getBelongTableList().addAll(belongTableList);
									pdmStandardField1.setOldBusType(realDataType);
									allPDMStandardFieldList.add(pdmStandardField1);
							 
						 }
						
					 }else{
						 pdmStandardField.setOldBusType(realDataType);
						 allPDMStandardFieldList.add(pdmStandardField);
					 }
		 }
		 return allPDMStandardFieldList;
	}
	
	/**
	 * ��ҵ�������������ºϲ���ͬ��ʱ,��ʱҵ�����ݵ������Ѿ��޸�.
	 * @param allUniqueStaneardFieldList
	 * @param allUniquePDMBusinessDataTypeList
	 */
	public void updatePDMSStandardFieldBusinessType(List<PDMStandardField> allUniqueStaneardFieldList,List<PDMBusinessDataType> allUniquePDMBusinessDataTypeList){
		Map<String,List<PDMStandardField>>pdmStaneardFieldMap =getStandardFieldCategory(allUniqueStaneardFieldList);//map��list�ٶȿ�,����������ĸ���
		for(PDMBusinessDataType pdmBusinessDataType :allUniquePDMBusinessDataTypeList){
			if(!StringUtils.equals(pdmBusinessDataType.getOldTypeName(), pdmBusinessDataType.getTypeName())
					&& StringUtils.isNotBlank(pdmBusinessDataType.getTypeName())){
				for(String standardFieldName:pdmBusinessDataType.getBelongStandardFieldList()){
					if(pdmStaneardFieldMap.get(standardFieldName)!=null){
						List<PDMStandardField> pdmStandardFields = pdmStaneardFieldMap.get(standardFieldName);
						for(PDMStandardField pdmStandardField:pdmStandardFields){
							pdmStandardField.setGenBusType(pdmBusinessDataType.getTypeName());//ֻ��excelʱ����setNewBusType();
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * ����ҵ���������͵��޸�,���±�׼�ֶε���ҵ�����������޸�
	 * @param listInfo
	 * @param allUniquePDMBusinessDataTypeList
	 */
	public void updateStandardFieldBusinessType(Collection<MergePojo> listInfo,List<PDMBusinessDataType> allUniquePDMBusinessDataTypeList){
		for(MergePojo mergePojo:listInfo){
			List<StandardField> stdFields =mergePojo.getStdFields();
			for(StandardField standardField:stdFields){
				String fieldName = standardField.getName();
				boolean isChange = false;
				for(PDMBusinessDataType pdmBusinessDataType :allUniquePDMBusinessDataTypeList){//���ҵ���������ͽ������޸�
					if(!StringUtils.equals(pdmBusinessDataType.getOldTypeName(), pdmBusinessDataType.getTypeName())
							&& StringUtils.isNotBlank(pdmBusinessDataType.getTypeName())){
						
						for(String standardFieldName:pdmBusinessDataType.getBelongStandardFieldList()){//�����׼�ֶζ�Ӧ��ҵ�������������ҵ������������ͬ
							if(StringUtils.equals(fieldName, standardFieldName)){
								
								for(String subSys:pdmBusinessDataType.getSubSyses()){
									if(mergePojo.getSubSyses().contains(subSys)){//���ڶ�Ӧ����ϵͳ��
										isChange = true;
										break;
									}
								}
								
							}
						}
					}
					if(isChange){//���ȷʵҵ���������ͽ������޸����޸ı�׼�ֶζ�Ӧ��ҵ����������
						standardField.setDataType(pdmBusinessDataType.getTypeName());
						break;
						
					}
				}
				
			}
			
		}
		
		
	}
	
	/**
	 * �ж�ָ���ı����Դ�Ƿ���ָ�������ģ����(����ģ��)
	 * @param tableResource
	 * @param subSystem
	 * @return
	 */
	public boolean isOfSubSystem(IARESResource tableResource,String subSystem){
		if(tableResource==null){
			return false;
		}
		//StringUtils.isNotBlank(moduleName) && StringUtils.indexOf(moduleName, ".") == -1;
		IARESModule module =tableResource.getModule();
		IARESModule parentModule = module;
		
		while(parentModule!=null){
			module = parentModule;
			parentModule =parentModule.getParentModule();
			
		}
		if(StringUtils.equals(module.getElementName(), subSystem)){
			return true;
		}
			
		return false;
	}
	/**
	 * �ж�ָ���ı����Դ�Ƿ���ָ�������ģ���б���(����ģ��)
	 * @param tableResource
	 * @param subSystemList
	 * @return
	 */
	public boolean isInSubSystems(IARESResource tableResource,List<String> subSystemList){
		if(tableResource==null){
			return false;
		}
		IARESModule module =tableResource.getModule();
		for (String subSystem : subSystemList) {
			IARESModule parentModule = tableResource.getModule();
			while (parentModule != null) {
				module = parentModule;
				parentModule = parentModule.getParentModule();

			}
			if (StringUtils.equals(module.getElementName(), subSystem)) {
				return true;
			}
		}
		
		return false;
	}
	/**
	 * ��tableName��PDMStandardField���з���
	 * @param pmdStandardFieldList
	 * @return
	 */
	public  Map<String,List<PDMStandardField>>getStandardFieldCategoryByTable(List<PDMStandardField> pmdStandardFieldList){
		Map<String,List<PDMStandardField>>pdmStaneardFieldMap = new HashMap<String,List<PDMStandardField>>();
		
		for(PDMStandardField pmdStandardField:pmdStandardFieldList){
			for(String tableName:pmdStandardField.getBelongTableList()){
				if(pdmStaneardFieldMap.get(tableName)!=null){
					pdmStaneardFieldMap.get(tableName).add(pmdStandardField);
				}else{
					List<PDMStandardField> standardFieldCategory = new ArrayList<PDMStandardField>();
					standardFieldCategory.add(pmdStandardField);
					pdmStaneardFieldMap.put(tableName, standardFieldCategory);
				}
			}
			
		}
		return pdmStaneardFieldMap;
	}
	
	/**
	 * �ж�ҵ�����������Ƿ���ͬ:ҵ��ҵ������������,��׼����,����,������ͬ��ͬ
	 * @param field1
	 * @param field2
	 * @return
	 */
	private boolean isTheBusinessDataType(PDMBusinessDataType pdmBusinessDataType1,PDMBusinessDataType pdmBusinessDataType2){
		return StringUtils.equals(pdmBusinessDataType1.getOldTypeName(), pdmBusinessDataType2.getOldTypeName())
				//&& StringUtils.equals(businessDataType.getChineseName(), pdmBusinessDataType.getTypeChineseName())
				&& StringUtils.equals(pdmBusinessDataType1.getStandardTypeName(), pdmBusinessDataType2.getStandardTypeName())
				&& StringUtils.equals(pdmBusinessDataType1.getLength(), pdmBusinessDataType2.getLength())
				&& StringUtils.equals(pdmBusinessDataType1.getPrecision(), pdmBusinessDataType2.getPrecision());
		
	}
	
	/**
	 * �ж�������׼�ֶ��Ƿ���ͬ:��׼�ֶ���,������,ҵ��������������ͬ����ͬ
	 * @param field1
	 * @param field2
	 * @return
	 */
	private boolean isTheSameStandardField(PDMStandardField field1,PDMStandardField field2){
		return StringUtils.equalsIgnoreCase(field1.getOldName(), field2.getOldName()) && StringUtils.equalsIgnoreCase(field1.getOldBusType(), field2.getOldBusType()) &&StringUtils.equalsIgnoreCase(field1.getOldChineseName(), field2.getOldChineseName()) ;
	}
	/**
	 * 
	 * @param pdmFeild
	 * @return
	 */
	public String getModifyInfo(PDMStandardField pdmFeild){
		StringBuffer modifyStr = new StringBuffer();
		if(StringUtils.isNotBlank(pdmFeild.getGenName()) && !StringUtils.equals(pdmFeild.getGenName(), pdmFeild.getOldName())){
			modifyStr.append("�ֶ���:").append(pdmFeild.getOldName()).append("�޸�Ϊ:").append(pdmFeild.getGenName());
		}
		
		if(StringUtils.isNotBlank(pdmFeild.getGenBusType())&& !StringUtils.equals(pdmFeild.getOldBusType(), pdmFeild.getGenBusType())){
			modifyStr.append("����:").append(pdmFeild.getOldBusType()).append("�޸�Ϊ:").append(pdmFeild.getGenBusType());
		}
		
		return modifyStr.toString();
	}

}
