/**
 * Դ�������ƣ�PDMImportExecel.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵�������������ı�׼�ֶ���ҵ����������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.hundsun.ares.studio.jres.database.utils.DatabaseUtils;
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
 * pdm�ڶ��ε���
 *
 */
public class PDMImportExecel {
	
	private IARESProject project;
	private PDMImportExecelHelper helper= new PDMImportExecelHelper();
	static final Logger console = ConsoleHelper.getLogger();
	/**
	 * �����׼�ֶκϲ������¼
	 * @param file
	 * @param monitor
	 * @param iaresModule
	 */
	public void importPDMExcel(IARESProject pro ,String file,IProgressMonitor monitor){
		project = pro;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(file));
			PDMExcelReaderWriter pmdrw = new PDMExcelReaderWriter();
			HSSFWorkbook wb = new HSSFWorkbook(inputStream);
			List<PDMStandardField> newStandardFieldList = pmdrw.standardFieldReader(wb);
			List<PDMBusinessDataType>  newBusinessDataTypeList =pmdrw.BusTypeReader(wb);
			StringBuffer errorMsg = new StringBuffer();
		   if(check(newBusinessDataTypeList,newStandardFieldList,errorMsg)){
			  importBusinessDataType(newBusinessDataTypeList,errorMsg,monitor);//ҵ����������
			  List<PDMStandardField> uniquePDMStandardField= helper.mergeStandardField(newStandardFieldList);
			  importStandardField(uniquePDMStandardField,errorMsg,monitor);//�����׼�ֶ�
			  updateTables(newStandardFieldList,errorMsg,monitor);//���±��ֶ�
			 
			}
			//������־�ļ�
			if(StringUtils.isNotBlank(errorMsg.toString())){
				DatabaseUtils.writeErrorMsgToFile(project, errorMsg.toString(), "�����¼�����쳣����"+ System.currentTimeMillis() + ".txt", monitor);
			}
		} catch (Exception e) {
			console.info(e.getMessage());
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					console.info(e.getMessage());
				}
			}
		}
		
		
		
		
	}
	

	/**
	 * �������ı�׼�ֶ����򵥵ļ���
	 * @param newBusinessDataTypeList
	 * @param newStandardFieldList
	 * @param errorMsg
	 * @return
	 * @throws ARESModelException 
	 */
	private boolean check(List<PDMBusinessDataType> newBusinessDataTypeList,List<PDMStandardField> newStandardFieldList,StringBuffer errorMsg) throws ARESModelException{
			boolean checkFieldState = helper.checkField(newBusinessDataTypeList, newStandardFieldList, errorMsg);//�����ֶ�
			boolean checkStateTable = helper.checkTable(project,newStandardFieldList,errorMsg);//�����ֶ�������
			boolean checkBusinessDataType = helper.checkBusinessDataType(newBusinessDataTypeList, errorMsg);//����ҵ����������
			return checkFieldState && checkStateTable && checkBusinessDataType;
	}
	
	
	/**
	 * ���������ı�׼�ֶ���ҵ������
	 * @param newStandardFieldList
	 * @throws ARESModelException 
	 */
	private void importStandardField(List<PDMStandardField>newStandardFieldList,StringBuffer errorMsg,IProgressMonitor monitor) throws ARESModelException{
		IARESResource resStdField = project.findResource(IMetadataResType.StdField, IMetadataResType.StdField);
		List<StandardField>  newAllStandardFieldList  =new ArrayList<StandardField>();
		if (resStdField != null) {
			StandardFieldList allStandardFieldList = resStdField.getInfo(StandardFieldList.class);
			
			
			for(PDMStandardField pdmStandardField:newStandardFieldList){
				StandardField standardField = helper.createStandardField(pdmStandardField);
				newAllStandardFieldList.add(standardField);
			}
			//�����µ�ҵ����������
			 allStandardFieldList.getItems().clear();
			 Collections.sort(newAllStandardFieldList,new NameSorter());
			 allStandardFieldList.getItems().addAll(newAllStandardFieldList);
			resStdField.save(allStandardFieldList, true, monitor);
		
		}
		
	}
	
	/**
	 * ���������ı�׼�ֶ���ҵ������
	 * @param newBusinessDataTypeList
	 * @throws ARESModelException 
	 */
	private void importBusinessDataType(List<PDMBusinessDataType> newPDMBusinessDataTypeList,StringBuffer errorMsg,IProgressMonitor monitor) throws ARESModelException{
		IARESResource resBizType = project.findResource(IMetadataResType.BizType, IMetadataResType.BizType);
		IARESResource resTDVType = project.findResource(IMetadataResType.DefValue, IMetadataResType.DefValue);
		List<TypeDefaultValue> typeDefaultValueList = new ArrayList<TypeDefaultValue>();
		if(resTDVType!=null){
			typeDefaultValueList = resTDVType.getInfo(TypeDefaultValueList.class).getItems();
		}
		List<BusinessDataType>  newAllBusinessDataTypeList  =new ArrayList<BusinessDataType>();
		if (resBizType != null) {
			BusinessDataTypeList allBusinessDataTypeList = resBizType.getInfo(BusinessDataTypeList.class);
			
			//����ɵ�ҵ����������
			for(BusinessDataType businessDataType:allBusinessDataTypeList.getItems()){
				int index = helper.indexOfBusinessDataType(newPDMBusinessDataTypeList, businessDataType);
				if(index!=-1){
					//BusinessDataType newBusinessDataType = helper.createBusinessDataType(newPDMBusinessDataTypeList.get(index),businessDataType);
					newPDMBusinessDataTypeList.remove(index);//��ͬ�ĵ�ҵ���������Ͳ���Ҫ�ٴ���
					//newAllBusinessDataTypeList.add(newBusinessDataType);
				}
			}
			
			for(PDMBusinessDataType pdmBusinessDataType:newPDMBusinessDataTypeList){
				BusinessDataType newBusinessDataType = helper.createBusinessDataType(pdmBusinessDataType);
				newAllBusinessDataTypeList.add(newBusinessDataType);
			}
			newAllBusinessDataTypeList.addAll(new ArrayList<BusinessDataType>(allBusinessDataTypeList.getItems()));//��ԭ����ҵ���������ͼ���
			allBusinessDataTypeList.getItems().clear();//ɾ���ɵ�ҵ����������
			//�����µ�ҵ����������
			 Collections.sort(newAllBusinessDataTypeList,new NameSorter());
			 PDMHelper.associateDefaultValueByStdType(newAllBusinessDataTypeList,typeDefaultValueList);
			allBusinessDataTypeList.getItems().addAll(newAllBusinessDataTypeList);
			
			resBizType.save(allBusinessDataTypeList, true, monitor);
		
		}
	
		
	}
	
	/**
	 * ���±���ֶ�
	 * @param newStandardFieldList
	 * @param errorMsg
	 * @throws ARESModelException 
	 */
	private void updateTables(List<PDMStandardField>newStandardFieldList,StringBuffer errorMsg,IProgressMonitor monitor) throws ARESModelException{
		List<ReferenceInfo> tabInfoList = ReferenceManager.getInstance().getReferenceInfos( project,IDatabaseRefType.Table, true) ;
		Map<String ,List<PDMTableResource>> tableResourceMap  = PDMHelper.getTableResourceMap(tabInfoList);//����Ϊ��λ���з���
		Map<String,List<PDMStandardField>> pdmStaneardFieldMap = helper.getStandardFieldCategoryByTable(newStandardFieldList);//����Ϊ��λ���б�׼�ֶη���
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
										
										if(StringUtils.equals(pdmStandardField.getGenName(), tableColumn.getName())){
											
											String name = tableColumn.getName();
											if(!StringUtils.equals(name, pdmStandardField.getNewName()) && StringUtils.isNotBlank(pdmStandardField.getNewName())){
												tableColumn.setName(pdmStandardField.getNewName());
												isChanged = true;
											}
											String changeStr = helper.getModifyInfo(pdmStandardField);
											if(StringUtils.isNotBlank(changeStr)){
												StringBuffer newComments = new StringBuffer();
												newComments.append("�����¼����--");
												newComments.append(changeStr);
												if(StringUtils.isNotBlank(tableColumn.getComments())){
													newComments.append("\r\n");
													newComments.append(tableColumn.getComments());
												}
												
												tableColumn.setComments(newComments.toString());
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
		
		tableResourceMap.clear();
		
		
	}
	
	
	
	
	
}
