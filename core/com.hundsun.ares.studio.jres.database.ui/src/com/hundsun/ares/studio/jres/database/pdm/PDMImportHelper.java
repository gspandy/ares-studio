/**
 * Դ�������ƣ�PDMHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.service.DataServiceManager;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMStandardField;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTable;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableColumn;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableField;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableIndex;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMTableIndexColumn;
import com.hundsun.ares.studio.jres.database.pdm.bean.PDMView;
import com.hundsun.ares.studio.jres.metadata.service.IMetadataService;
import com.hundsun.ares.studio.jres.metadata.service.IStandardDataType;
import com.hundsun.ares.studio.jres.model.database.ColumnType;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.TableKey;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.ViewResourceData;
import com.hundsun.ares.studio.jres.model.database.key_type;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;

/**
 * @author liaogc
 * pdm��һ�ε��������
 */
public class PDMImportHelper {
	
	public static final String NUMBER_TYPE="NUMBER";
	public static final String NUMERIC_Type = "NUMERIC";
	
	
	/**
	 * �ϲ���ͬ���ֶβ�Ϊÿһ���ֶβ���һ���µ�Ψһ�ı�׼�ֶ���
	 * @param tablefieldMap
	 * @return
	 */
	public List<PDMTableField> mergeTableField(Map<String,List<PDMTableField>> tablefieldMap){
		 List<PDMTableField> mergeList = new  ArrayList<PDMTableField>();//ȫ�����ֶ��б�
		 List<String> exclusionNames = new ArrayList<String>();//�ֶ�����ͻ�б�
		 List<String> fieleNames = new ArrayList<String>(tablefieldMap.keySet());//�ֶ����б�
		 Collections.sort(fieleNames,new NameSorter());//�������ֶ��б�
		 for(String fieldName:fieleNames){
			 List<PDMTableField> fields = tablefieldMap.get(fieldName);
			 if(fields.size()==1){//���û����ͬ����
				 if(!exclusionNames.contains(fieldName)){//�����������ͻ
					 mergeList.add(fields.get(0));
					 exclusionNames.add(fieldName);
					 fields.get(0).setChanged(true);
				 }else{//��ͻȡ�л�ȡһ��Ψһ������
					 String newName = PDMHelper.getUniqueName(fieldName,exclusionNames);
					 fields.get(0).setNewName(newName);
					 mergeList.add(fields.get(0));
					 exclusionNames.add(newName);
					 fields.get(0).setChanged(true);
					 
				 }
				
				
			 }else if(fields.size()>1){//�ϲ���ͬ���ֶ�
				 List<PDMTableField> subUniqueFeilds = new ArrayList<PDMTableField>();//�ֶ�����ͬ,��������������ԭʼ���Ͳ�ͬ���ֶ��б�
				 for(int i = 0;i < fields.size();i++){
					 PDMTableField field1 = fields.get(i);
					 if(!subUniqueFeilds.contains(field1)){
						 boolean found = false;
						 for(int j=0;j<subUniqueFeilds.size();j++){
							 PDMTableField field2 = subUniqueFeilds.get(j);
							 if(isTheSameTableField(field1,field2)){
								 found = true;
								 if(!StringUtils.equalsIgnoreCase(field1.getTable(), field2.getTable())){
									 if(!field2.getBeLongTable().contains(field1.getTable())){
										 field2.getBeLongTable().add(field1.getTable());//���������
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
				 //�Ϻ���ͬ�ֶ����������ͻ�������������ͬ���ֶ��б�����ֶ�������
				 for(PDMTableField field:subUniqueFeilds){
					 String name = field.getName();
					 if(!exclusionNames.contains(name)){//�����������ͻ
						 mergeList.add(field);
						 exclusionNames.add(name);
						 field.setChanged(true);
					 }else{
						 String newName = PDMHelper.getUniqueName(name,exclusionNames);//���������ͻ,���ȡһ��Ψһ������
						 field.setNewName(newName);
						 mergeList.add(field);
						 exclusionNames.add(newName);
						 field.setChanged(true);
						 
						 for(String tableName:field.getBeLongTable()){//�������ͻ��ͬ�ı����(�ؼ�)
							 for(PDMTableField asociateTableField:fields){
								 if(StringUtils.equals(tableName, asociateTableField.getTable())){
									 //��Ӧ���ٱȽ������ֶ��Ƿ�һ���,�������ڲ�������һ��pdm���ֶ�����ͬ�������ֶ�ͬʱ��һ�ű���,����Ϊ�˼�ֻ�Ƚϱ���������
									 if(!asociateTableField.isChanged()){//���û�иı��
										 asociateTableField.setNewName(newName);
										 asociateTableField.setChanged(true);
									 }
								 }
							 }
						 }
						 
					 }
				 }
			 }
			 
		 }
		
		return mergeList;
		
	}
	/**
	 * ��һ�׶κϲ�����ֶ�����ԭ����׼�ֶν��кϲ�
	 * @param mergeList ��һ�׶κϲ���ı��ֶ��б�
	 * @param standardFields ԭ�б�׼�ֶ��б�
	 * @return
	 */
	public List<PDMTableField> mergeTableField(List<PDMTableField> mergeList ,List<StandardField> standardFields,IARESProject project){
		List<PDMTableField> newMergeList = new  ArrayList<PDMTableField>();
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
		
		 List<String> exclusionNames = new ArrayList<String>();
		 for(StandardField standardField:standardFields){
			 exclusionNames.add(standardField.getName());
		 }
		 for(PDMTableField pdmTableField:mergeList){
			 String name = StringUtils.isNotBlank(pdmTableField.getNewName())?pdmTableField.getNewName():pdmTableField.getName();
			 for(StandardField standardField:standardFields){
				 if(isTheSameStandardField(pdmTableField,standardField,project,databaseType)){//��ͬ
					 break;
				 }else if(StringUtils.equals(name, standardField.getName())){//�г�ͻ��
					
					 String newName = PDMHelper.getUniqueName(name,exclusionNames);
					 pdmTableField.setNewName(newName);//�����µ��ֶ���
					 exclusionNames.add(newName);
					 break;
				 }
			 }
				 newMergeList.add(pdmTableField);
		 }
		 
		 return newMergeList;
		
	}
	
/**
 * ����TableField,ȡ�ø�TableField��Ӧ�ı�׼�ֶ���Ϣ��ҵ��׼�ֶ���Ϣ
 * @param tableField
 * @param project
 * @return
 */
	public Map<String,String> analyseTableFeild(PDMTableField tableField,IARESProject project,String databaseType){
		Map<String,String> fieldInfo = new HashMap<String,String>();
		IMetadataService metadataService = DataServiceManager.getInstance().getService(project,IMetadataService.class);
		String type ="";//ҵ������
		String length ="";//����
		String precision = "";//����
		String stdName = "";//��׼������
		
		boolean find = false;
		//���Ų��ұ�׼����(��һ����)
		IStandardDataType standardDataType = findStandardDataTypeByRealType(tableField.getType(),metadataService.getStandardDataTypeList(),databaseType);
		if(standardDataType!=null){
			find = true;
			type = tableField.getType();
			stdName = standardDataType.getName();
		}
		//�����һ��û���ҵ�,������Ӧ�Ŀմ����NUMBER(X)ת����NUMBER(X,0)����NUMERIC(X)ת����NUMERIC(X,0)
		if(StringUtils.isBlank(type)){
			if(StringUtils.indexOf(StringUtils.defaultIfBlank(tableField.getType(), "").toLowerCase(), NUMBER_TYPE.toLowerCase())>-1
					||StringUtils.indexOf(StringUtils.defaultIfBlank(tableField.getType(), "").toLowerCase(), NUMERIC_Type.toLowerCase())>-1){
				String lp = StringUtils.substringBetween(StringUtils.defaultIfBlank(tableField.getType(), ""), "(", ")");
				if(lp!=null){
					if(StringUtils.indexOf(lp,",")== -1){
						int index1 = tableField.getType().indexOf("(");
						String prefix = tableField.getType().substring(0,index1);
						//��NUMBER(X)ת����NUMBER(X,0)����NUMERIC(X)ת����NUMERIC(X,0)
						String fullType = prefix+"("+lp+",0)";
						standardDataType = findStandardDataTypeByRealType(fullType,metadataService.getStandardDataTypeList(),databaseType);
						if(standardDataType!=null){
							type = fullType;
							find = true;
							//tableField.setType(fullType);
							stdName = standardDataType.getName();
						}
						
					}
				}
			}
		}
		//ֻ�г��ȵ����
		String lp = StringUtils.substringBetween(StringUtils.defaultIfBlank(tableField.getType(), ""), "(", ")");
		String lp2 = StringUtils.substringBetween(StringUtils.defaultIfBlank(tableField.getType(), ""), "��", "��");
		if(StringUtils.isNotBlank(lp)||StringUtils.isNotBlank(lp2)){
			
			if(StringUtils.isNotBlank(lp) && StringUtils.split(lp, ",").length == 1 || StringUtils.isNotBlank(lp2) && StringUtils.split(lp2, "��").length == 1){//��()��ֻ��һ������ʱ
				length = lp ;
				if(!find){//���ŵ�һ�α�׼���Ͳ���
					int index1 = tableField.getType().indexOf("(");
					if(index1==-1){
						index1 = tableField.getType().indexOf("��");
					}
					String prefix = tableField.getType().substring(0,index1);
					String fullType = prefix+"($L)";
					standardDataType = findStandardDataTypeByRealType(fullType,metadataService.getStandardDataTypeList(),databaseType);
					if(standardDataType!=null){
						type = fullType;
						find = true;
						stdName = standardDataType.getName();
					}
					if(!find){//���Ͼ������ŵڶ��α�׼���Ͳ���
						fullType = prefix+"($L,$P)";
						standardDataType = findStandardDataTypeByRealType(fullType,metadataService.getStandardDataTypeList(),databaseType);
						if(standardDataType!=null){
							type = fullType;
							find = true;
							stdName = standardDataType.getName();
						}
					}
				}
				
			}else if(StringUtils.isNotBlank(lp) && StringUtils.split(lp, ",").length == 2 || StringUtils.isNotBlank(lp2) && StringUtils.split(lp2, "��").length == 2){//��()������������ʱ���Ų�ѯ��׼��������
				String []lplitArray ={""};
				if(StringUtils.isNotBlank(lp)){
					lplitArray = StringUtils.split(lp, ",");
				}
				if(lplitArray.length==0 || lplitArray.length==1){
					lplitArray = StringUtils.split(lp, "��");
				}
				length = lplitArray[0] ;
				precision = lplitArray[1];
				if(!find){
					int index1 = tableField.getType().indexOf("(");
					if(index1==-1){
						index1 = tableField.getType().indexOf("��");
					}
					String prefix = tableField.getType().substring(0,index1);
					String fullType = prefix+"($L,$P)";
					standardDataType = findStandardDataTypeByRealType(fullType,metadataService.getStandardDataTypeList(),databaseType);
					if(standardDataType!=null){
						type = fullType;
						find = true;
						stdName = standardDataType.getName();
					}
				}
				
			}
		}
		
		
		//ҵ������������Ϣ
		fieldInfo.put("BusinessDataType_name", "Hs"+tableField.getNewName());
		fieldInfo.put("BusinessDataType_chinesName", tableField.getChineseName());
		fieldInfo.put("BusinessDataType_stdType", stdName);
		fieldInfo.put("BusinessDataType_length", length);
		fieldInfo.put("BusinessDataType_precision", precision);
		fieldInfo.put("BusinessDataType_comment", tableField.getChineseName());
		fieldInfo.put("BusinessDataType_no_std_name", convertOriginalTypeToBusinessTypeOfNoStd(tableField.getType()));//�Ǳ�׼�ֶ�ʱҵ���������͵�����
		
		//��׼�ֶ���Ϣ
		fieldInfo.put("StandardFeild_name", tableField.getNewName());
		fieldInfo.put("StandardFeild_chinesName", tableField.getChineseName());
		fieldInfo.put("StandardFeild_type","Hs"+tableField.getNewName());
		fieldInfo.put("StandardFeilde_comment", tableField.getChineseName());
		
		return fieldInfo;
	}
	

	
	/**
	 * ��PDM�еı���Ϣ��Ƶ�ares�ж�Ӧ�����ݿ��ģ����ȥ
	 * @param pdmTable
	 * @return
	 */
	public TableResourceData createTableResourceData(PDMTable pdmTable,Map<String,List<PDMTableField>> allTableFieldMap,boolean importMode ){
		TableResourceData table = DatabaseFactory.eINSTANCE.createTableResourceData();//�½�����Դ
		//���ݽ����ı���Ϣ���ñ���Դ�и�����Ϣ
		table.setName(pdmTable.getName());//����
		table.setChineseName(pdmTable.getChineseName());//��������
		table.setDescription(pdmTable.getDesc());//��˵����Ϣ
		//��Լ�����Ӻ󣬵����޸�
		TableKey tableKey = null;
		for( PDMTableColumn pdmColumn: pdmTable.getColumns()){//�������Ϣ
			TableColumn column = DatabaseFactory.eINSTANCE.createTableColumn();//�½�����
			List<PDMTableField> pdmTableFieldList = allTableFieldMap.get(pdmColumn.getFieldName());//��ȡ��ͬ���ĸ���������е���
			int index =indexTableFieldByColumnName(pdmTableFieldList,pdmTable.getName(),pdmColumn.getFieldName());//���ݱ���,�ֶ���(ԭʼ��)��������������ֶ��б��е�λ��
			if(index>-1){
				PDMTableField pdmTableField = pdmTableFieldList.get(index);//�µı��ֶ���Ϣ
				String fieldName = StringUtils.isNotBlank(pdmTableField.getNewName())?pdmTableField.getNewName():pdmTableField.getName();//�ֶ���
				column.setFieldName(fieldName);
				if(!importMode){
					column.setColumnType(ColumnType.STD_FIELD);
					
				}else{
					column.setDataType(pdmTableField.getBusType());
					column.setColumnType(ColumnType.NON_STD_FIELD);
					column.setDefaultValue(pdmColumn.getDefaultValue());
					column.setChineseName(pdmTableField.getChineseName());
				}
				StringBuffer modifyStr = new StringBuffer();//����޸����ֶ��������һ���ֶ��޸ļ�¼
				if(StringUtils.isNotBlank(pdmTableField.getNewName())&& !StringUtils.equals(pdmTableField.getNewName(), pdmTableField.getName())){
					modifyStr.append("PDM����--").append("�ֶ���:").append("\""+pdmTableField.getName()+"\"").append("�޸�Ϊ:").append("\""+pdmTableField.getNewName()+"\"");
				}
				if(StringUtils.isNotBlank(modifyStr.toString())){
					StringBuffer newComments = new StringBuffer();
					newComments.append(modifyStr.toString());//�µı�ע����ǰ
					if(StringUtils.isNotBlank(pdmColumn.getComment())){//���ԭ���б�ע
						newComments.append("\r\n");//�¾�������
						newComments.append(pdmColumn.getComment());//�ɵ��ں�
					}
					column.setComments(newComments.toString());
				}else{
					column.setComments(pdmColumn.getComment());
				}
				
			}else{
				column.setFieldName(pdmColumn.getFieldName());
			}
			//����������Ϣ
			//column.setPrimaryKey(pdmColumn.isPrimaryKey());//����
			column.setNullable(pdmColumn.isNullable());//������NULL
			column.setDefaultValue(pdmColumn.getDefaultValue());//Ĭ��ֵ
			
			
			if(pdmColumn.isPrimaryKey()){
				if(tableKey == null){
					tableKey = DatabaseFactory.eINSTANCE.createTableKey();
					tableKey.setName(pdmTable.getName() + "_pk");
					tableKey.setType(key_type.PRIMARY);
				}
				tableKey.getColumns().add(column);
			}
			
			table.getColumns().add(column);
	
		}
		if(tableKey != null){
			table.getKeys().add(tableKey);
		}
		for(PDMTableIndex pdmTableIndex :pdmTable.getIndexes()){//���ñ�������Ϣ
			TableIndex index = DatabaseFactory.eINSTANCE.createTableIndex();
			index.setName(pdmTableIndex.getName());
			index.setUnique(pdmTableIndex.isUnique());
			index.setCluster(pdmTableIndex.isCluster());
			
			for(PDMTableIndexColumn pdmTableIndexColumn :pdmTableIndex.getColumns()){//������������Ϣ
				TableIndexColumn indexCol = DatabaseFactory.eINSTANCE.createTableIndexColumn();
				indexCol.setColumnName(pdmTableIndexColumn.getColumnName());
				index.getColumns().add(indexCol);
			}
			table.getIndexes().add(index);
		}
		
		return table;
	}
	/**
	 * ����pdmTableField����PDMStandardField
	 * @param pdmTableField
	 * @param parameters
	 * @return PDMStandardField
	 */
	public PDMStandardField createPDMStandardField(PDMTableField pdmTableField,Map<String,String> parameters){
		 PDMStandardField pdmStandardField = new PDMStandardField();
		 
		 pdmStandardField.setOldName(pdmTableField.getName());
		 pdmStandardField.setOldChineseName(pdmTableField.getChineseName());
		 pdmStandardField.setOldBusType(pdmTableField.getType());
		 pdmStandardField.getBolongSubSystemList().add((pdmTableField.getSubSystem()));
		 pdmStandardField.setGenName(pdmTableField.getNewName());
		 pdmStandardField.setGenBusType(pdmTableField.getBusType());
		 pdmStandardField.setDictId("");
		 pdmStandardField.setOldComment(pdmTableField.getDesc());
		 pdmStandardField.setModefyDesc("");
		 pdmStandardField.setImportPath(StringUtils.defaultIfBlank(parameters.get("subSystem"), ""));//����·��
		 pdmStandardField.getBelongTableList().addAll(pdmTableField.getBeLongTable());//�ֶ�������
		 
		 return pdmStandardField;
	}
	
	/**
	 * ��PDM�е���ͼ��Ϣ��Ƶ�ares�ж�Ӧ�����ݿ���ͼģ����
	 * @param pdmView
	 * @return
	 */
	public ViewResourceData createVrewResourceData(PDMView pdmView){
		ViewResourceData view = DatabaseFactory.eINSTANCE.createViewResourceData();
		view.setName(pdmView.getName());
		view.setChineseName(pdmView.getChineseName());
		view.setSql(pdmView.getSql());
		return view;
		
	}
	
	/**
	 * ������ʵ�������Ͷ�Ӧ�ı�׼����
	 * @param realType
	 * @param standardDataTypeList
	 * @return
	 */
	private IStandardDataType findStandardDataTypeByRealType(String realType,List<IStandardDataType> standardDataTypeList,String databaseType){
		if(StringUtils.isBlank(databaseType)){
			databaseType = "oracle";
		}
		for(IStandardDataType sdt:standardDataTypeList){
			if(StringUtils.equalsIgnoreCase(realType, sdt.getValue(databaseType))){
				 return sdt;
			}
		}
		return null;
	}
	
	
	/**
	 * �ж������ֶ��Ƿ���ͬ:�ֶ���,������,����(ԭʼ����)��ͬ����ͬ
	 * @param field1
	 * @param field2
	 * @return
	 */
	private boolean isTheSameTableField(PDMTableField field1,PDMTableField field2){
		String name = StringUtils.isNotBlank(field1.getNewName())?field1.getNewName():field1.getName();
		return StringUtils.equalsIgnoreCase(name, field2.getName()) && StringUtils.equalsIgnoreCase(field1.getType(), field2.getType()) &&StringUtils.equalsIgnoreCase(field1.getChineseName(), field2.getChineseName()) ;
	}
	
	/**
	 * �жϱ�����׼�ֶ��Ƿ���ͬ:���ڱ�׼�ֶ�����Hs+ԭʼ����,���ԱȽ�ʱҪע��
	 * @param field1
	 * @param field2
	 * @return
	 */
	private boolean isTheSameStandardField(PDMTableField field1,StandardField field2,IARESProject project,String databaseType){
		 String name = StringUtils.isNotBlank(field1.getNewName())?field1.getNewName():field1.getName();
		 boolean nameSame = StringUtils.equalsIgnoreCase(field1.getName(), field2.getName())&& StringUtils.equalsIgnoreCase(field1.getChineseName(), field2.getChineseName());
		 boolean stdTypeSame = StringUtils.equalsIgnoreCase("Hs"+name, field2.getDataType())  ;
		 if(!stdTypeSame){//���Hs+name����ͬ˵���϶�����
			 return nameSame && stdTypeSame;
		 }else if(nameSame && stdTypeSame ){
			 String realDataType = PDMHelper.getRealDataType(project, field2.getName(),databaseType);
			 if(StringUtils.isNotBlank(realDataType) && StringUtils.isNotBlank(field1.getType())){
				 stdTypeSame = compareRealDataType(field1.getType(),realDataType);//�Ƚ���ʵ����
			 }
			 
			 
		 }
		return  nameSame && stdTypeSame;
	}
	
	/**
	 * �Ƚ�����ԭʼ�����Ƿ���ͬ
	 * @param realDataType1
	 * @param realDataType2
	 * @return
	 */
	private boolean compareRealDataType(String realDataType1, String realDataType2){
		  boolean isSame = StringUtils.equalsIgnoreCase(realDataType1, realDataType2);
		  if(isSame){
			  return true;
		  }

			if(StringUtils.indexOf(StringUtils.defaultIfBlank(realDataType1, "").toLowerCase(), NUMBER_TYPE.toLowerCase())>-1
					||StringUtils.indexOf(StringUtils.defaultIfBlank(realDataType1, "").toLowerCase(), NUMERIC_Type.toLowerCase())>-1){
				String lp = StringUtils.substringBetween(StringUtils.defaultIfBlank(realDataType1, ""), "(", ")");
				if(lp!=null){
					if(StringUtils.indexOf(lp,",")== -1){
						int index1 = realDataType1.indexOf("(");
						String prefix = realDataType1.substring(0,index1);
						//��NUMBER(X)ת����NUMBER(X,0)����NUMERIC(X)ת����NUMERIC(X,0)
						String fullType = prefix+"("+lp+",0)";
						isSame =  StringUtils.equalsIgnoreCase(fullType, realDataType2);
						
					}
				}
			}
		
		  
		return isSame;
	}
	
	/**
	 * ����ͬ���ֶ��б����ҵ���Ӧ���Ӧ����Ӧ��,�����������Ӧ����
	 * �򷵻�-1,���򷵻ظñ����������ͬ���ֶ��б��е�λ��
	 * @param pdmTableFieldList
	 * @param columnName
	 * @return
	 */
	private int indexTableFieldByColumnName(List<PDMTableField> pdmTableFieldList,String tableName,String columnName){
		if(pdmTableFieldList==null || pdmTableFieldList.size()==0){
			return -1;
		}
		int i=0;
		for(PDMTableField pdmTableField:pdmTableFieldList){
			//�ֶ�����ͬ,����ͬ���ҵ�
			if(StringUtils.equals(pdmTableField.getName(), columnName) && StringUtils.equals(pdmTableField.getTable(), tableName)){
				return i;
			}
			i++;
		}
		return -1;
	}
	
	/**
	 * ����ҵ�����ݻ�����Ϣ����һ��ҵ����������
	 * @param fieldInfo
	 * @return
	 */
	public BusinessDataType createBusinessDataType(Map<String,String> fieldInfo,boolean importMode){
		BusinessDataType businessDataType = MetadataFactory.eINSTANCE.createBusinessDataType();
		//ҵ����������
		if(!importMode){
			businessDataType.setChineseName(fieldInfo.get("BusinessDataType_chinesName"));//�Ǳ�׼�ֶβ���Ҫ��������
			businessDataType.setName(fieldInfo.get("BusinessDataType_name"));
		}else{
			businessDataType.setName(fieldInfo.get("BusinessDataType_no_std_name"));
		}
		businessDataType.setStdType(fieldInfo.get("BusinessDataType_stdType"));
		if(StringUtils.isNotBlank(fieldInfo.get("BusinessDataType_length"))){
			businessDataType.setLength(fieldInfo.get("BusinessDataType_length"));	
		}
		if(StringUtils.isNotBlank(fieldInfo.get("BusinessDataType_precision"))){
			businessDataType.setPrecision(fieldInfo.get("BusinessDataType_precision"));
		}
		businessDataType.setDescription(fieldInfo.get("BusinessDataType_comment"));
       return businessDataType;
	}

	/**
	 * ���ݱ�׼�ֶλ���������׼�ֶ�
	 * @param fieldInfo
	 * @return
	 */
	public StandardField createcreateStandardField(Map<String,String> fieldInfo){
	StandardField standardField = MetadataFactory.eINSTANCE.createStandardField();
	
	//��׼�ֶ���Ϣ
	standardField.setName(fieldInfo.get("StandardFeild_name"));
	standardField.setChineseName(fieldInfo.get("StandardFeild_chinesName"));
	standardField.setDataType(fieldInfo.get("StandardFeild_type"));
	standardField.setDescription(fieldInfo.get("StandardFeilde_comment"));
	return standardField;
	}
	
	
	/**
	 * ����ԭʼ����������
	 * @param originalType
	 * @return
	 */
	private  String convertOriginalTypeToBusinessTypeOfNoStd(String originalType){
		String businessType = StringUtils.defaultIfBlank(originalType, "").trim();
		if(StringUtils.indexOf(businessType, "(")>-1){//��(ת����_
			businessType = StringUtils.replace(businessType,"(","_");
		}else if(StringUtils.indexOf(businessType, "��")>-1){//����(
			businessType = StringUtils.replace(businessType,"��","_");
		}
		if(StringUtils.indexOf(businessType, ",")>-1){//��,ת����_
			businessType = StringUtils.replace(businessType,",", "_");
		}else if(StringUtils.indexOf(businessType, "��")>-1){//���ģ�
			businessType = StringUtils.replace(businessType,"��","_");
		}
		if(StringUtils.indexOf(businessType, ")")>-1){//��)ת����""��
			businessType = StringUtils.replace(businessType,")", "");
		}else if(StringUtils.indexOf(businessType, "��")>-1){//���ģ�
			businessType = StringUtils.replace(businessType,"��","_");
		}
	
		return businessType;
	}
	
	public static void  main(String[] args){
		/*System.out.println(convertOriginalTypeToBusinessTypeOfNoStd("number(10,0)"));
		System.out.println(convertOriginalTypeToBusinessTypeOfNoStd("number(10)"));
		System.out.println(convertOriginalTypeToBusinessTypeOfNoStd("varchar2(10)"));*/
	}
	

	
}
