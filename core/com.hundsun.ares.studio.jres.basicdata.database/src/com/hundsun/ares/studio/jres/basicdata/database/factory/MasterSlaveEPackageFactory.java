package com.hundsun.ares.studio.jres.basicdata.database.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataEpacakgeConstant;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.BasicDataField;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.BasicdataFactory;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.BasicdataPackage;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.MasterSlaveTable;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.PackageDefine;
import com.hundsun.ares.studio.jres.basicdata.logic.epackage.BaseEPackageFactory;
import com.hundsun.ares.studio.jres.basicdata.logic.util.BasicDataEpackageUtil;
import com.hundsun.ares.studio.jres.basicdata.logic.util.JRESResProviderUtil;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

public class MasterSlaveEPackageFactory extends BaseEPackageFactory{
	
	public static MasterSlaveEPackageFactory eInstance = new MasterSlaveEPackageFactory();

	public EPackage create(IARESProject project,PackageDefine define)throws Exception{
		if(define instanceof MasterSlaveTable){
			MasterSlaveTable defineData = (MasterSlaveTable)define;
			
			//������Դ
			String masterTableName = defineData.getMaster();
			TableResourceData masterTable = JRESResProviderUtil.getResourceModel(project, 
					masterTableName,
					IDatabaseRefType.Table,
					TableResourceData.class);
			if(null == masterTable){
				throw new Exception(String.format("����Դ[%s]�����ڡ�", masterTableName));
			}
			
			//�����ֶ�
			Map<String,BasicDataField> masterBasicDataFields = new LinkedHashMap<String,BasicDataField>();
			for(TableColumn item:masterTable.getColumns()){
				BasicDataField basicDataField = BasicdataFactory.eINSTANCE.createBasicDataField();
				basicDataField.setName(item.getFieldName());
				basicDataField.setDataType(item.getDataType());
				basicDataField.setColumnType(item.getColumnType().getValue());
				basicDataField.setChineseName(item.getChineseName());
				masterBasicDataFields.put(item.getFieldName(),basicDataField);
			}
			
			//�ӱ���Դ
			String slvaeTableName = defineData.getSlave();
			TableResourceData slaveTable = JRESResProviderUtil.getResourceModel(project, 
					slvaeTableName,
					IDatabaseRefType.Table,
					TableResourceData.class);
			if(null == slaveTable){
				throw new Exception(String.format("����Դ[%s]�����ڡ�", slvaeTableName));
			}
			
			//�ӱ���Դ
			Map<String,BasicDataField> slaveBasicDataFields = new LinkedHashMap<String,BasicDataField>();
			for(TableColumn item:slaveTable.getColumns()){
				BasicDataField basicDataField = BasicdataFactory.eINSTANCE.createBasicDataField();
				basicDataField.setName(item.getFieldName());
				basicDataField.setDataType(item.getDataType());
				basicDataField.setColumnType(item.getColumnType().getValue());
				slaveBasicDataFields.put(item.getFieldName(),basicDataField);
				basicDataField.setChineseName(item.getChineseName());
			}
			
			//�����ֶ�
			String[] linkFields = DataBaseEPackageUtil.getLinkFields(masterTable,
					slaveTable ,"����[%s]�е������ֶ�[%s]�ڹ�����[%s]�в����ڡ�");
			
			//�ӱ���ȥ�������ֶ�
			for(String item:linkFields){
				slaveBasicDataFields.remove(item);
			}
			
			if(slaveBasicDataFields.isEmpty()){
				throw new Exception(String.format("�ӱ�[%s]�ֶ���Ϊ0��", slvaeTableName));
			}
			
			
			
			EPackage ePackage = theCoreFactory.createEPackage();
			ePackage.setName("MasterSlaveTablePackage");
			ePackage.setNsPrefix("masterSlaveTable");
			ePackage.setNsURI("http://www.hundsun.com/masterslavetable");
			
			EClass rootClass = addResourceRoot(ePackage);
			
			EClass masterClass = addMasterItem(ePackage,
					IBasicDataEpacakgeConstant.MasterItem,
					masterBasicDataFields);
			
			EClass slaveClass = addEclass(ePackage,IBasicDataEpacakgeConstant.SlaveItem, slaveBasicDataFields);
			slaveClass.getESuperTypes().add(BasicdataPackage.Literals.BASICDATA_ITEM);//�̳�BasicdataItem
			//���ע��
			BasicDataEpackageUtil.addMasterKeyAnnotation(masterClass, linkFields);
			
			addReference( masterClass, slaveClass, IBasicDataEpacakgeConstant.Attr_Slave_Items);
			
			return ePackage;
		}
		return null;
	}
}
