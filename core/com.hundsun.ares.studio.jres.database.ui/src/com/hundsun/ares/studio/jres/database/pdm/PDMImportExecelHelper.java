/**
 * Դ�������ƣ�PDMImportExecelHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMBusinessDataType;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMStandardField;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableResource;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author liaogc
 * ��һ�ε���excel������
 */
public class PDMImportExecelHelper {

	/**
	 * ����ҵ��������������ҵ�������б��е�λ��,������ڷ���-1.���������,��׼������,����,������ͬ����ͬ
	 * @param pdmBusinessDataTypeList
	 * @param businessDataType
	 * @return
	 */
	public int indexOfBusinessDataType(List<PDMBusinessDataType> pdmBusinessDataTypeList,BusinessDataType businessDataType){
		if(pdmBusinessDataTypeList==null || pdmBusinessDataTypeList.size()==0){
			return -1;
		}
		for(int i =0;i<pdmBusinessDataTypeList.size();i++){
			PDMBusinessDataType pdmBusinessDataType = pdmBusinessDataTypeList.get(i);
			if(StringUtils.equals(businessDataType.getName(), pdmBusinessDataType.getTypeName())
					//&& StringUtils.equals(businessDataType.getChineseName(), pdmBusinessDataType.getTypeChineseName())
					&& StringUtils.equals(businessDataType.getStdType(), pdmBusinessDataType.getStandardTypeName())
					&& StringUtils.equals(businessDataType.getLength(), pdmBusinessDataType.getLength())
					&& StringUtils.equals(businessDataType.getPrecision(), pdmBusinessDataType.getPrecision())){
				return i;
			}
		}
		return -1;
		
	}
	/**
	 * ���ر�׼�ֶ��������ı�׼�ֶ��б��е�λ��,������������ı�׼�ֶ��б�,����-1.����ֶ���,������,ҵ��������������ͬ����ͬ
	 * @param pdmStandardFieldList
	 * @param standardField
	 * @return
	 */
	public int indexOfStandardField(List<PDMStandardField> pdmStandardFieldList,StandardField standardField){
		if(pdmStandardFieldList==null || pdmStandardFieldList.size()==0){
			return -1;
		}
		for(int i=0;i<pdmStandardFieldList.size();i++){
			PDMStandardField pdmstandardField = pdmStandardFieldList.get(i);
			String fieldName = StringUtils.isBlank(pdmstandardField.getNewName())?pdmstandardField.getGenName():pdmstandardField.getNewName();
			if(StringUtils.equals(standardField.getName(), fieldName)){
				return i;
			}
		}
		return -1;
		
	}
	/**
	 * ����PDMStandardField�Լ�standardField����һ���µ�standardField
	 * @param pdmStandardField
	 * @param standardField
	 * @return
	 */
	public StandardField createStandardField(PDMStandardField pdmStandardField,StandardField standardField){
		StandardField newStandardField = MetadataFactory.eINSTANCE.createStandardField();
		String fieldName = StringUtils.isBlank(pdmStandardField.getNewName())?pdmStandardField.getGenName():pdmStandardField.getNewName();
		newStandardField.setName(fieldName);
		String chineseName =  StringUtils.isBlank(pdmStandardField.getNewChineseName())?pdmStandardField.getOldChineseName():pdmStandardField.getNewChineseName();
		newStandardField.setChineseName(chineseName);
		String busType = StringUtils.isBlank(pdmStandardField.getNewBusType())?pdmStandardField.getGenBusType():pdmStandardField.getNewBusType();
		
		newStandardField.setDataType(busType);
		String comment =StringUtils.isBlank(pdmStandardField.getNewComment())?pdmStandardField.getNewComment():pdmStandardField.getOldName();
		newStandardField.setDescription(comment);
		if(StringUtils.isNotBlank(pdmStandardField.getDictId())){
			newStandardField.setRefId(pdmStandardField.getDictId());	
		}
		return newStandardField;
		
	}
	/**
	 * ����PDMStandardField����һ���µ�standardField
	 * @param pdmStandardField
	 * @return
	 */
	public StandardField createStandardField(PDMStandardField pdmStandardField){
		StandardField standardField = MetadataFactory.eINSTANCE.createStandardField();
		String fieldName = StringUtils.isBlank(pdmStandardField.getNewName())?pdmStandardField.getGenName():pdmStandardField.getNewName();
		standardField.setName(fieldName);
		String chineseName =  StringUtils.isBlank(pdmStandardField.getNewChineseName())?pdmStandardField.getOldChineseName():pdmStandardField.getNewChineseName();
		standardField.setChineseName(chineseName);
		String busType = StringUtils.isBlank(pdmStandardField.getNewBusType())?pdmStandardField.getGenBusType():pdmStandardField.getNewBusType();
		standardField.setDataType(busType);
		String comment =StringUtils.isBlank(pdmStandardField.getNewComment())?pdmStandardField.getNewComment():pdmStandardField.getOldName();
		standardField.setDescription(comment);
		if(StringUtils.isNotBlank(pdmStandardField.getDictId())){
			standardField.setRefId(pdmStandardField.getDictId());	
		}
		return standardField;
		
	}
	
	/**
	 * ����PDMBusinessDataType����һ���µ�BusinessDataType
	 * @param pdmBusinessDataType
	 * @return
	 */
	public BusinessDataType createBusinessDataType(PDMBusinessDataType pdmBusinessDataType){
		BusinessDataType newBusinessDataType = MetadataFactory.eINSTANCE.createBusinessDataType();
		newBusinessDataType.setName(pdmBusinessDataType.getTypeName());
		newBusinessDataType.setChineseName(pdmBusinessDataType.getTypeChineseName());
		newBusinessDataType.setStdType(pdmBusinessDataType.getStandardTypeName());
		if(StringUtils.isNotBlank(pdmBusinessDataType.getDefaultValue())){
			newBusinessDataType.setDefaultValue(pdmBusinessDataType.getDefaultValue());
		}
		if(StringUtils.isNotBlank(pdmBusinessDataType.getLength())){
			newBusinessDataType.setLength(pdmBusinessDataType.getLength());
		}
		if(StringUtils.isNotBlank(pdmBusinessDataType.getPrecision())){
			newBusinessDataType.setPrecision(pdmBusinessDataType.getPrecision());
		}
		newBusinessDataType.setDescription(pdmBusinessDataType.getComment());
		return newBusinessDataType;
		
	}
	
	/**
	 * ����PDMBusinessDataType�Լ��ɵ�businessDataType����һ���µ�BusinessDataType
	 * @param pdmBusinessDataType
	 * @return
	 */
	public BusinessDataType createBusinessDataType(PDMBusinessDataType pdmBusinessDataType,BusinessDataType businessDataType){
		BusinessDataType newBusinessDataType = MetadataFactory.eINSTANCE.createBusinessDataType();
		newBusinessDataType.setName(pdmBusinessDataType.getTypeName());
		newBusinessDataType.setChineseName(pdmBusinessDataType.getTypeChineseName());
		newBusinessDataType.setStdType(pdmBusinessDataType.getStandardTypeName());
		if(StringUtils.isNotBlank(pdmBusinessDataType.getDefaultValue())){
			newBusinessDataType.setDefaultValue(pdmBusinessDataType.getDefaultValue());
		}
		if(StringUtils.isNotBlank(pdmBusinessDataType.getLength())){
			newBusinessDataType.setLength(pdmBusinessDataType.getLength());
		}
		if(StringUtils.isNotBlank(pdmBusinessDataType.getPrecision())){
			newBusinessDataType.setPrecision(pdmBusinessDataType.getPrecision());
		}
		newBusinessDataType.setDescription(pdmBusinessDataType.getComment());
		return newBusinessDataType;
		
	}
	/**
	 * �ϲ���ͬ�ı�׼�ֶ�
	 * @param pmdStandardFieldList
	 * @return
	 */
	public List<PDMStandardField> mergeStandardField(List<PDMStandardField> pmdStandardFieldList){
		Map<String,PDMStandardField>pdmStaneardFieldMap = getStandardFieldCategory(pmdStandardFieldList);//��ʱvalueֵ�Ǹ��ֶζ�����ͬ(��ͬ�ı�������)
		 List<PDMStandardField> mergeList = new  ArrayList<PDMStandardField>();
		 mergeList.addAll(pdmStaneardFieldMap.values());
		return mergeList;
		
	
		
	}
	
	
	
	
	
	/**
	 * �����޸���Ϣ
	 * @param pdmFeild
	 * @return
	 */
	 public String getModifyInfo(PDMStandardField pdmFeild){
		StringBuffer modifyStr = new StringBuffer();
		if(StringUtils.isNotBlank(pdmFeild.getNewName()) && !StringUtils.equals(pdmFeild.getGenName(), pdmFeild.getNewName())){
			modifyStr.append("�ֶ���:").append("\""+pdmFeild.getGenName()+"\"").append("�޸�Ϊ:").append("\""+pdmFeild.getNewName()+"\"");
		}
		if(StringUtils.isNotBlank(pdmFeild.getNewChineseName())){
			if(StringUtils.isNotBlank(modifyStr.toString())){
				modifyStr.append("||");
			}
			modifyStr.append("������:").append("\""+pdmFeild.getOldChineseName()+"\"").append("�޸�Ϊ:").append("\""+pdmFeild.getNewChineseName()+"\"");
		}
		if(StringUtils.isNotBlank(pdmFeild.getNewBusType())){
			if(StringUtils.isNotBlank(modifyStr.toString())){
				modifyStr.append("||");
			}
			modifyStr.append("����:").append("\""+pdmFeild.getGenBusType()+"\"").append("�޸�Ϊ:").append("\""+pdmFeild.getNewBusType()+"\"");
		}
		if(StringUtils.isNotBlank(pdmFeild.getNewComment())){
			if(StringUtils.isNotBlank(modifyStr.toString())){
				modifyStr.append("||");
			}
			modifyStr.append("��ע:").append("\""+pdmFeild.getOldComment()+"\"").append("�޸�Ϊ:").append("\""+pdmFeild.getNewComment()+"\"");
		}
		return modifyStr.toString();
	}
	
	/**
	 * ���ֶ���Ϊkey��PDMStandardField���з���
	 * @param pmdStandardFieldList
	 * @return
	 */
	private Map<String,PDMStandardField>getStandardFieldCategory(List<PDMStandardField> pmdStandardFieldList){
		Map<String,PDMStandardField>pdmStaneardFieldMap = new HashMap<String,PDMStandardField>();
		
		for(PDMStandardField pmdStandardField:pmdStandardFieldList){
			String name = StringUtils.isNotBlank(pmdStandardField.getNewName())?pmdStandardField.getNewName():pmdStandardField.getGenName();
			pdmStaneardFieldMap.put(name, pmdStandardField);
		}
		return pdmStaneardFieldMap;
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
				if(StringUtils.isNotBlank(tableName)){
					if(pdmStaneardFieldMap.get(tableName)!=null){
						pdmStaneardFieldMap.get(tableName).add(pmdStandardField);
					}else{
						List<PDMStandardField> standardFieldCategory = new ArrayList<PDMStandardField>();
						standardFieldCategory.add(pmdStandardField);
						pdmStaneardFieldMap.put(tableName, standardFieldCategory);
					}
				}
			}
			
		}
		return pdmStaneardFieldMap;
	}
	/**
	 * �����Ƿ����
	 * @param project
	 * @param newStandardFieldList
	 * @param errorMsg
	 * @return
	 * @throws ARESModelException
	 */
	public boolean checkTable(IARESProject project,List<PDMStandardField> newStandardFieldList,StringBuffer errorMsg) throws ARESModelException{
		StringBuffer tableError = new StringBuffer();
		List<ReferenceInfo> tabInfoList = ReferenceManager.getInstance().getReferenceInfos( project,IDatabaseRefType.Table, true) ;
		Map<String ,List<PDMTableResource>> tableResourceMap  = PDMHelper.getTableResourceMap(tabInfoList);//������е����ݿ��,���Ա���Ϊkey
			for(int i =0;i< newStandardFieldList.size();i++ ){
				PDMStandardField std = newStandardFieldList.get(i);
				String name = StringUtils.isNotBlank(std.getNewName())?std.getNewName():std.getGenName();
				String subSystem = std.getBolongSubSystemList().size()==0?"":std.getBolongSubSystemList().get(0);
				if( StringUtils.isBlank(subSystem)){//���������ϵͳ������,�������ֶε����������
					break;
				}
				for(String tableName:std.getBelongTableList()){
					List<PDMTableResource> tableList = tableResourceMap.get(tableName);
					
					if(tableList==null  || tableList.size()==0){//������������в����ڱ�
						tableError.append("�ֶ�:").append(name).append("������:").append(tableName).append("������").append("(��"+(i+3)+"��)").append("\r\n");
					}else{//��������д����ֶ��������ı�����˱��ǲ�������������ϵͳ��
						boolean find = false;
						for(PDMTableResource pdmTableResource:tableList){
							//���������ͬ,��ϵͳҲ��ͬ�����
							if(StringUtils.equals(pdmTableResource.getSubSystem(),subSystem) && StringUtils.isNotBlank(subSystem)){
								find = true;
								break;
							}
						}
						if(!find){//�����������������
							tableError.append("�ֶ�:").append(name).append("������:").append(tableName).append("������").append("(��"+(i+3)+"��)").append("\r\n");
						}
					}
				}
				
			}
			if (StringUtils.isNotBlank(tableError.toString())) {
				errorMsg.append(tableError.toString());
				return false;
			}
			return true;
		}

	/**
	 * ����׼�ֶδ��ڴ�������:�ֶ�����ͬ,�����಻��ͬ���������
	 * @param newBusinessDataTypeList
	 * @param newStandardFieldList
	 * @param errorMsg
	 * @return
	 */
	public boolean checkField(List<PDMBusinessDataType> newBusinessDataTypeList,List<PDMStandardField> newStandardFieldList,StringBuffer errorMsg){
		for(int i =0;i< newStandardFieldList.size();i++ ){
			PDMStandardField std = newStandardFieldList.get(i);
			String stdName = StringUtils.isNotBlank(std.getNewName())?std.getNewName():std.getGenName();
			String stdType = StringUtils.isNotBlank(std.getNewBusType())?std.getNewBusType():std.getGenBusType();
			for(int j =0;j< newStandardFieldList.size();j++ ){
				if(j>i){
					PDMStandardField subStd = newStandardFieldList.get(j);
					String subStdName = StringUtils.isNotBlank(subStd.getNewName())?subStd.getNewName():subStd.getGenName();
					String subBusType = StringUtils.isNotBlank(subStd.getNewBusType())?subStd.getNewBusType():subStd.getGenBusType();
					if (StringUtils.equals(stdName, subStdName) && !StringUtils.equals(stdType, subBusType)) {
						errorMsg.append("�ֶ�����"+stdName+" ����Դ���ʹ�������: "+"��"+(i+3)+"��:"+stdType +","+"��"+(j+3)+"��:"+subBusType+"\r\n");
					}
				
				}
			
			}
		}
		if (StringUtils.isNotBlank(errorMsg.toString())) {
			return false;
		}
		return true;
	}
	/**
	 * ����ҵ�����������Ƿ������ͬ��������
	 * @param newBusinessDataTypeList
	 * @param errorMsg
	 * @return
	 */
	public boolean checkBusinessDataType(List<PDMBusinessDataType> newBusinessDataTypeList,StringBuffer errorMsg){
		
		 class LinePDMBusinessDataType {
			public LinePDMBusinessDataType(PDMBusinessDataType pdmBusinessDataType,int line ) {
				 this.line = line;
				 this.pdmBusinessDataType = pdmBusinessDataType;
			 }
			 private PDMBusinessDataType pdmBusinessDataType;
			 /**
			 * @return the pdmBusinessDataType
			 */
			public PDMBusinessDataType getPdmBusinessDataType() {
				return pdmBusinessDataType;
			}
			
			public int getLine() {
				return line;
			}
		
			private int line;
			
		}
		
		Map<String,List<LinePDMBusinessDataType>> sameNameBusinessDataTypeMap= new HashMap<String,List<LinePDMBusinessDataType>>();
		StringBuffer businessDataTypeError = new StringBuffer();
		//������Ϊkey��ҵ���������ͷ���
		for(int i =0;i< newBusinessDataTypeList.size();i++ ){
			PDMBusinessDataType pdmBusinessDataType = newBusinessDataTypeList.get(i);
			String name = StringUtils.isNotBlank(pdmBusinessDataType.getTypeName())?pdmBusinessDataType.getTypeName():pdmBusinessDataType.getOldTypeName();
			if(sameNameBusinessDataTypeMap.get(name)!=null){
				sameNameBusinessDataTypeMap.get(name).add(new LinePDMBusinessDataType(pdmBusinessDataType,(i+3)));
			}else{
				List<LinePDMBusinessDataType> LinePDMBusinessDataTypeList = new ArrayList<LinePDMBusinessDataType>();
				LinePDMBusinessDataTypeList.add(new LinePDMBusinessDataType(pdmBusinessDataType,(i+3)));
				sameNameBusinessDataTypeMap.put(name, LinePDMBusinessDataTypeList);
			}
			
		}
		for(String name:sameNameBusinessDataTypeMap.keySet()){
			List<LinePDMBusinessDataType> linePDMBusinessDataTypeList = sameNameBusinessDataTypeMap.get(name);
			if(linePDMBusinessDataTypeList !=null &&linePDMBusinessDataTypeList.size()>1){
				for(LinePDMBusinessDataType linePDMBusinessDataType :linePDMBusinessDataTypeList){
					businessDataTypeError.append("ҵ����������:").append(name).append("���ڳ�ͻ(����ͬ����ҵ����������)").append("��").append(linePDMBusinessDataType.getLine()).append("��").append("\r\n");
				}
			}
		}
		if (StringUtils.isNotBlank(businessDataTypeError.toString())) {
			errorMsg.append(businessDataTypeError.toString());
			return false;
		}
		return true;
	}

}
