/**
 * Դ�������ƣ�SubSystemMerge.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.core.runtime.IProgressMonitor;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMBusinessDataType;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMStandardField;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableResource;
import com.hundsun.ares.studio.jres.database.ui.DatabaseUI;
import com.hundsun.ares.studio.jres.database.ui.model.MergePojo;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.StandardFieldList;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValueList;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author liaogc 
 * ��ɸ�����ϵͳ�ϲ�,��Ҫ�漰ҵ���������ͺϲ�,��׼�ֶκϲ�,���ݿ���ֶθ���,�Լ�����һ�ݴ�����ı�׼�ֶα���
 */
public class PDMSubSystemMerge {
	private IARESProject project;
	private PDMSubSystemMergeHelper helper =null;
	private String outFile = "";
	static final Logger console = ConsoleHelper.getLogger();
	public void subSystemMerge(Map<String ,MergePojo> pojoMap,IARESProject project,String targetDir,IProgressMonitor monitor, StringBuffer errorMsg) throws Exception {
		this.project = project;
		helper  = new PDMSubSystemMergeHelper(this.project);
		this.outFile = targetDir;
		Collection<MergePojo> listInfo =pojoMap.values();
		List<PDMBusinessDataType> allUniquePDMBusinessDataTypeList = mergeBusinessDataType(listInfo,monitor,errorMsg);//�ϲ�ҵ����������
		helper.updateStandardFieldBusinessType(listInfo,allUniquePDMBusinessDataTypeList);
		List<PDMStandardField> allUniqueStaneardFieldList = mergeStandardField(listInfo,monitor,errorMsg);//�ϲ���׼�ֶ�
		//helper.updatePDMSStandardFieldBusinessType(allUniqueStaneardFieldList,allUniquePDMBusinessDataTypeList);//����ҵ�����������޸�����Ҫ�޸���Ӧ�ı�׼�ֶζ�Ӧ��ҵ����������
		Map<String ,List<PDMTableResource>> tableResourceMap =updateTable(allUniqueStaneardFieldList,monitor,errorMsg);//�ϲ����ݿ��
		report(allUniqueStaneardFieldList,tableResourceMap,monitor,errorMsg);//����������ĺϲ��ı�׼�ֶα���
	}

	/**
	 * �ϲ�ҵ����������
	 * @param listInfo
	 * @param monitor
	 * @param errorMsg
	 * @return
	 * @throws ARESModelException
	 */
	private List<PDMBusinessDataType>  mergeBusinessDataType(Collection<MergePojo> listInfo,IProgressMonitor monitor,StringBuffer errorMsg) throws ARESModelException {
		 
		List<PDMBusinessDataType>  allPDMBusinessDataTypeList =  new ArrayList<PDMBusinessDataType>();
		IARESResource businessDataTypeResource = project.findResource(IMetadataResType.BizType, IMetadataResType.BizType);
		IARESResource resTDVType = project.findResource(IMetadataResType.DefValue, IMetadataResType.DefValue);
		
		List<BusinessDataType> oldBusinessDataTypeList = new ArrayList<BusinessDataType>();
		BusinessDataTypeList allBusinessDataTypeList=null;
		if (businessDataTypeResource != null) {
			 allBusinessDataTypeList = businessDataTypeResource.getInfo(BusinessDataTypeList.class);
			oldBusinessDataTypeList.addAll(allBusinessDataTypeList.getItems());
		
		}
		List<TypeDefaultValue> typeDefaultValueList = new ArrayList<TypeDefaultValue>();
		if(resTDVType!=null){
			typeDefaultValueList = resTDVType.getInfo(TypeDefaultValueList.class).getItems();
		}
		for(MergePojo mergePojo:listInfo){
			//�Ѹ����̴�������BusinessDataTypeת����PDMBusinessDataType
			List<PDMBusinessDataType> susSystemPDMBusinessDataTypeList=helper.analyseBusinessDataType(mergePojo.getBusTypes(),mergePojo.getStdFields(),mergePojo.getSubSyses());
			allPDMBusinessDataTypeList.addAll(susSystemPDMBusinessDataTypeList);
			
		}
		List<PDMBusinessDataType> allUniquePDMBusinessDataTypeList = helper.mergeBusinessDataType(allPDMBusinessDataTypeList);
		List<BusinessDataType> allUniqueBusinessDataTypeList = new ArrayList<BusinessDataType>();
		for(PDMBusinessDataType pmdBusinessDataType:allUniquePDMBusinessDataTypeList){
			BusinessDataType newBusinessDataType =helper.createBusinessDataType(pmdBusinessDataType);//�Ѻϲ��õ�PDMBusinessDataTypeת����BusinessDataType
			int index = PDMHelper.indexOfBusinessDataType(oldBusinessDataTypeList, newBusinessDataType);
			if(index==-1){
				allUniqueBusinessDataTypeList.add(newBusinessDataType);
			}
		}
		
		if (businessDataTypeResource != null) {
			List<BusinessDataType> totalBusinessDataTypeList = new ArrayList<BusinessDataType>();
			totalBusinessDataTypeList.addAll(allBusinessDataTypeList.getItems());
			totalBusinessDataTypeList.addAll(allUniqueBusinessDataTypeList);
			allBusinessDataTypeList.getItems().clear();
			PDMHelper.associateDefaultValueByStdType(totalBusinessDataTypeList,typeDefaultValueList);
			 Collections.sort(totalBusinessDataTypeList,new NameSorter());
			allBusinessDataTypeList.getItems().addAll(totalBusinessDataTypeList);
			businessDataTypeResource.save(allBusinessDataTypeList, true, monitor);//����ϲ��õ�ҵ����������
		
		}
		return allUniquePDMBusinessDataTypeList;


	}
/**
 * ���غϲ��õ��ܵı�׼�ֶ�
 * @param listInfo
 * @param monitor
 * @param errorMsg
 * @return
 * @throws ARESModelException 
 */
	private List<PDMStandardField> mergeStandardField(Collection<MergePojo> listInfo,IProgressMonitor monitor,StringBuffer errorMsg) throws ARESModelException { 
		List<PDMStandardField>  allPDMStaneardFieldList =  new ArrayList<PDMStandardField>();
		for(MergePojo mergePojo:listInfo){
			//��ÿ����Ŀ�������ı�׼�ֶ��б�ת����PDMStandard�б�
			List<PDMStandardField> susSystemPDMStandardFieldList=helper.analyseStandardField(mergePojo.getStdFields(), mergePojo.getSubSyses());
			allPDMStaneardFieldList.addAll(susSystemPDMStandardFieldList);
			
		}
		//�ϲ���׼�ֶ�
		List<PDMStandardField> allUniquePDMStandardFieldList =helper.mergeStandardField(allPDMStaneardFieldList);
		List<StandardField> allUniqueStandardFieldList = new ArrayList<StandardField>();
		
		for(PDMStandardField pmdStandardField:allUniquePDMStandardFieldList){
			allUniqueStandardFieldList.add(helper.createStandardField(pmdStandardField));//��pmdStandardFieldת������Ӧ��StandardField
		}
		IARESResource standardFieldResource = project.findResource(IMetadataResType.StdField, IMetadataResType.StdField);
		if (standardFieldResource != null) {
			StandardFieldList allStandardFieldList = standardFieldResource.getInfo(StandardFieldList.class);
			allStandardFieldList.getItems().clear();
			Collections.sort(allUniqueStandardFieldList,new NameSorter());
			allStandardFieldList.getItems().addAll(allUniqueStandardFieldList);
			standardFieldResource.save(allStandardFieldList, true, monitor);//�����µĴ�����ı�׼�ֶ�
		}
		return allUniquePDMStandardFieldList;

	}
	
	/**
	 * ���±�׼�ֶ���ر�������
	 * @param allUniquePDMStandardFieldList
	 * @param monitor
	 * @param errorMsg
	 * @throws ARESModelException
	 */
	private Map<String ,List<PDMTableResource>> updateTable(List<PDMStandardField> allUniquePDMStandardFieldList ,IProgressMonitor monitor,StringBuffer errorMsg) throws ARESModelException {

		List<ReferenceInfo> tabInfoList = ReferenceManager.getInstance().getReferenceInfos( project,IDatabaseRefType.Table, true) ;
		Map<String,List<PDMStandardField>> pdmStaneardFieldMap = helper.getStandardFieldCategoryByTable(allUniquePDMStandardFieldList);//����Ϊ��λ���б�׼�ֶη���
		Map<String ,List<PDMTableResource>> tableResourceMap  = PDMHelper.getTableResourceMap(tabInfoList);//����Ϊ��λ���з���
		Set<String> tableNames = pdmStaneardFieldMap.keySet();
		for(String tableName:tableNames){
			if(StringUtils.isNotBlank(tableName)){
				List<PDMStandardField> pdmtandardFieldList = pdmStaneardFieldMap.get(tableName);
				List<PDMTableResource> pdmTableResources= tableResourceMap.get(tableName);//һ��ͬ������Դ���Դ��ڲ�ͬ����ϵͳ��
				if(pdmTableResources!=null){
					for(PDMTableResource pdmTableResource:pdmTableResources){//�Ա���ԴΪ��λ���и���
						TableResourceData tableInfo = pdmTableResource.getTableInfo();
						boolean isChanged = false;
						for(PDMStandardField pdmStandardField:pdmtandardFieldList){
							
							if(pdmTableResource!=null 
									&& pdmStandardField.getBolongSubSystemList().size()>0 
									&& StringUtils.isNotBlank(pdmTableResource.getSubSystem())
									&& pdmStandardField.getBolongSubSystemList().contains(pdmTableResource.getSubSystem())){
							    
								if(StringUtils.equals(tableName, tableInfo.getName())){
									
									for(TableColumn tableColumn:tableInfo.getColumns()){
										
										if(StringUtils.equals(pdmStandardField.getOldName(), tableColumn.getName())){
											String name = tableColumn.getName();
											if(!StringUtils.equals(name, pdmStandardField.getGenName()) && StringUtils.isNotBlank(pdmStandardField.getGenName())){
												tableColumn.setName(pdmStandardField.getGenName());
												isChanged = true;
											}
											String changeStr = helper.getModifyInfo(pdmStandardField);
											if(StringUtils.isNotBlank(changeStr)){
												tableColumn.setComments(changeStr+"|"+StringUtils.defaultIfBlank(tableColumn.getComments(), ""));
												isChanged = true;
											}
										    break;	
											
										}
									}
									
								}
								
							}
							
					}
					if(isChanged){
							pdmTableResource.getResource().save(pdmTableResource.getTableInfo(), true, monitor);
						}
					}
						
				}
				
			}
		}
		
		return tableResourceMap;
		
	
	}
	/**
	 * ������׼�ֶκϲ������¼
	 * @param pmdStandardFieldList
	 * @param monitor
	 * @param errorMsg
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void report(List<PDMStandardField> pmdStandardFieldList,Map<String ,List<PDMTableResource>> tableResourceMap,IProgressMonitor monitor,StringBuffer errorMsg) throws IllegalAccessException, InvocationTargetException  {
		
		List<PDMStandardField> allPDMStandardFieldList = helper.getAllPDMStandardFieldList(pmdStandardFieldList,tableResourceMap);
		InputStream input = null;
		OutputStream output = null;
		try {
			input = DatabaseUI.getDefault().getBundle().getEntry("template/��׼�ֶκϲ������¼.xls").openStream();
			HSSFWorkbook wb = new HSSFWorkbook(input);
			PDMExcelReaderWriter pdmWriter = new PDMExcelReaderWriter();
			pdmWriter.standardFieldWriter(wb, allPDMStandardFieldList);
			output = new FileOutputStream(outFile);
			wb.write(output);
		} catch (IOException e) {
			console.info(e.getMessage());
		}finally{
			org.apache.commons.io.IOUtils.closeQuietly(input);
			org.apache.commons.io.IOUtils.closeQuietly(output);
		}
		
	}
	
	
	

}
