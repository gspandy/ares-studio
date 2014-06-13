/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.model.metadata.util;

import java.util.Map;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.context.JRESContextManager;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.MenuItem;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * Ԫ���ݷ����ṩ������Ҫ����һ����Ϣ��
 * <li>ͨ���ֶ�����ȡԪ������Ϣ;
 * <li>ͨ��Ԫ������֮��Ĺ�ϵ�����Ҷ�ӦԪ������Ϣ��
 * 
 * @author qinyuan
 *
 */
public class MetadataServiceProvider {
	
	/**
	 * c���Ͷ���
	 */
	public static final String C_TYPE = "c";
	
	/**
	 * oracle���Ͷ���
	 */
	public static final String ORACLE_TYPE = "oracle";
	
	//mysql���ݿ�
	public static final String MYSQL_TYPE = "mysql";
	
	/**
	 * ���ݱ�׼�ֶ����ƻ�ȡ��׼�ֶ�
	 * @param project ����
	 * @param name �ֶ���
	 * @return StandardField ��׼�ֶ�
	 * @throws Exception
	 */
	public static StandardField getStandardFieldByName(IARESProject project,String name) throws Exception{
		
		StandardField  stdField = getMetadataModelByName(project, name, IMetadataRefType.StdField, StandardField.class);
		if(null == stdField){
			throw new Exception(String.format("��׼�ֶ�[%s]�����ڡ�", name));
		}
		return stdField;
	}
	
	/**
	 * ���ݱ�׼�ֶ����ƻ�ȡ��׼�ֶΣ�Ϊ��ʱ��ֱ�ӷ���
	 * @param project ����
	 * @param name �ֶ���
	 * @return StandardField ��׼�ֶ�
	 */
	public static StandardField getStandardFieldByNameNoExp(IARESProject project,String name){
		StandardField  stdField = getMetadataModelByName(project, name, IMetadataRefType.StdField, StandardField.class);
		return stdField;
	}
	
	/**
	 * ����ҵ���������ƻ�ȡҵ����������
	 * @param project ����
	 * @param name ҵ������������
	 * @return BusinessDataType ҵ����������
	 * @throws Exception
	 */
	public static BusinessDataType getBusinessDataTypeByName(IARESProject project,String name) throws Exception{
		BusinessDataType bizType = getMetadataModelByName(project, name, IMetadataRefType.BizType, BusinessDataType.class);
		if(null == bizType) {
			throw new Exception(String.format("ҵ����������[%s]�����ڡ�", name));
		}
		return bizType;
	}
	
	/**
	 * ����ҵ���������ƻ�ȡҵ���������ͣ�Ϊ��ʱ��ֱ�ӷ���
	 * @param project ����
	 * @param name ҵ������������
	 * @return BusinessDataType ҵ����������
	 */
	public static BusinessDataType getBusinessDataTypeByNameNoExp(IARESProject project,String name){
		BusinessDataType bizType = getMetadataModelByName(project, name, IMetadataRefType.BizType, BusinessDataType.class);
		return bizType;
	}
	
	/**
	 * ���ݱ�׼�ֶ����ƻ�ȡ��׼�ֶζ�Ӧ��ҵ����������
	 * @param project ����
	 * @param name �ֶ���
	 * @return BusinessDataType ҵ����������
	 * @throws Exception
	 */
	public static BusinessDataType getBusinessDataTypeOfStdFieldByName(IARESProject project,String name) throws Exception{
		
		StandardField stdField = getStandardFieldByName(project, name);
		BusinessDataType bizType =  getMetadataModelByName(project, stdField.getDataType(), IMetadataRefType.BizType, BusinessDataType.class);
		
		if(null == bizType){
			throw new Exception(String.format("��׼�ֶ�[%s]������ҵ����������[%s]�����ڡ�", name,stdField.getDataType()));
		}
		return bizType;
	}
	
	/**
	 * ���ݱ�׼�������ƻ�ȡ��׼����
	 * @param project ����
	 * @param name ��׼��������
	 * @return StandardDataType ��׼����
	 * @throws Exception
	 */
	public static StandardDataType getStandardDataTypeByName(IARESProject project,String name) throws Exception{
		StandardDataType standType = getMetadataModelByName(project, name, IMetadataRefType.StdType, StandardDataType.class);
		if(null == standType){
			throw new Exception(String.format("��׼��������[%s]�����ڡ�", name));
		}
		return standType;
	}
	
	/**
	 * ���ݱ�׼�������ƻ�ȡ��׼����
	 * @param project ����
	 * @param name ��׼��������
	 * @return StandardDataType ��׼����
	 */
	public static StandardDataType getStandardDataTypeByNameNoExp(IARESProject project,String name){
		StandardDataType standType = getMetadataModelByName(project, name, IMetadataRefType.StdType, StandardDataType.class);
		return standType;
	}
	
	/**
	 * ����ҵ�����������ֶ����ƻ�ȡ��׼�ֶζ�Ӧ�ı�׼��������
	 * @param project ����
	 * @param name �ֶ���
	 * @return StandardDataType ��׼��������
	 * @throws Exception
	 */
	public static StandardDataType getStandardDataTypeOfBizTypeByName(IARESProject project,String name) throws Exception{
		BusinessDataType bizType = getBusinessDataTypeByName(project, name);
		StandardDataType standType = getMetadataModelByName(project, bizType.getStdType(), IMetadataRefType.StdType, StandardDataType.class);
		if(null == standType){
			throw new Exception(String.format("ҵ����������[%s]�������ı�׼��������[%s]�����ڡ�", bizType.getName(),bizType.getStdType()));
		}
		return standType;
	}
	
	/**
	 * ���ݱ�׼�ֶ����ƻ�ȡ��׼�ֶζ�Ӧ�ı�׼��������
	 * @param project ����
	 * @param name �ֶ���
	 * @return StandardDataType ��׼��������
	 * @throws Exception
	 */
	public static StandardDataType getStandardDataTypeOfStdFieldByName(IARESProject project,String name) throws Exception{
		BusinessDataType bizType = getBusinessDataTypeOfStdFieldByName(project, name);
		StandardDataType standType = getMetadataModelByName(project, bizType.getStdType(), IMetadataRefType.StdType, StandardDataType.class);
		if(null == standType){
			throw new Exception(String.format("��׼�ֶ�[%s]����Ӧ�ı�׼��������[%s]�����ڡ�", bizType.getName(),bizType.getStdType()));
		}
		
		return standType;
	}
	
	/**
	 * ����Ĭ��ֵ���ƻ�ȡĬ��ֵ
	 * @param project ����
	 * @param name Ĭ��ֵ�ֶ���
	 * @return TypeDefaultValue Ĭ��ֵ
	 * @throws Exception
	 */
	public static TypeDefaultValue getTypeDefaultValueByName(IARESProject project,String name) throws Exception{
		TypeDefaultValue typeDefValue = getMetadataModelByName(project, name, IMetadataRefType.DefValue, TypeDefaultValue.class);
		if(null == typeDefValue) {
			throw new Exception(String.format("Ĭ��ֵ[%s]�����ڡ�", name));
		}
		return typeDefValue;
	}
	
	/**
	 * ����Ĭ��ֵ���ƻ�ȡĬ��ֵ��Ϊ��ʱҲֱ�ӷ���
	 * @param project ����
	 * @param name Ĭ��ֵ�ֶ���
	 * @return TypeDefaultValue Ĭ��ֵ
	 */
	public static TypeDefaultValue getTypeDefaultValueByNameNoExp(IARESProject project,String name){
		TypeDefaultValue typeDefValue = getMetadataModelByName(project, name, IMetadataRefType.DefValue, TypeDefaultValue.class);
		return typeDefValue;
	}
	
	/**
	 * ����ҵ�������������ƻ�ȡĬ��ֵ
	 * @param project ����
	 * @param name Ĭ��ֵ�ֶ���
	 * @return TypeDefaultValue Ĭ��ֵ
	 * @throws Exception
	 */
	public static TypeDefaultValue getTypeDefaultValueOfBizTypeByName(IARESProject project,String name) throws Exception{
		BusinessDataType bizType = getBusinessDataTypeByName(project, name);
		TypeDefaultValue typeDefValue = getMetadataModelByName(project, bizType.getDefaultValue(), IMetadataRefType.DefValue, TypeDefaultValue.class);
		if(null == typeDefValue) {
			throw new Exception(String.format("ҵ����������[%s]������Ĭ��ֵ[%s]�����ڡ�", name, bizType.getDefaultValue()));
		}
		return typeDefValue;
	}
	
	/**
	 * ���ݱ�׼�ֶ����ƻ�ȡĬ��ֵ
	 * @param project ����
	 * @param name Ĭ��ֵ�ֶ���
	 * @return TypeDefaultValue Ĭ��ֵ
	 * @throws Exception
	 */
	public static TypeDefaultValue getTypeDefaultValueOfStdFieldByName(IARESProject project,String name) throws Exception{
		BusinessDataType bizType = getBusinessDataTypeOfStdFieldByName(project, name);
		TypeDefaultValue typeDefValue = getMetadataModelByName(project, bizType.getDefaultValue(), IMetadataRefType.DefValue, TypeDefaultValue.class);
		if(null == typeDefValue) {
			throw new Exception(String.format("��׼�ֶ�[%s]������Ĭ��ֵ[%s]�����ڡ�", name, bizType.getDefaultValue()));
		}
		return typeDefValue;
	}
	
	/**
	 * �����ֶ�����ȡ�����
	 * @param project ����
	 * @param name �����
	 * @return ErrorNoItem �����ֵ
	 * @throws Exception
	 */
	public static ErrorNoItem getErrorNoItemByName(IARESProject project,String name) throws Exception{
		ErrorNoItem errorNo = getMetadataModelByName(project, name, IMetadataRefType.ErrNo, ErrorNoItem.class);
		if(null == errorNo) {
			throw new Exception(String.format("�����[%s]�����ڡ�", name));
		}
		return errorNo;
	}
	
	/**
	 * �����ֶ�����ȡ����
	 * @param project ����
	 * @param name ��������
	 * @return ConstantItem ����ֵ
	 * @throws Exception
	 */
	public static ConstantItem getConstantItemByName(IARESProject project,String name) throws Exception{
		ConstantItem constant = getMetadataModelByName(project, name, IMetadataRefType.Const, ConstantItem.class);
		if(null == constant) {
			throw new Exception(String.format("����[%s]�����ڡ�", name));
		}
		return constant;
	}
	
	/**
	 * �����ֶ�����ȡ�����ֵ�
	 * @param project ����
	 * @param name �ֵ���Ŀ
	 * @return DictionaryType �����ֵ�
	 * @throws Exception
	 */
	public static DictionaryType getDictionaryTypeByName(IARESProject project,String name) throws Exception{
		DictionaryType dict = getMetadataModelByName(project, name, IMetadataRefType.Dict, DictionaryType.class);
		if(null == dict) {
			throw new Exception(String.format("�����ֵ�[%s]�����ڡ�", name));
		}
		return dict;
	}
	
	/**
	 * �����ֶ�����ȡ�˵�
	 * @param project ����
	 * @param name �˵���
	 * @return MenuItem �˵�
	 * @throws Exception
	 */
	public static MenuItem getMenuItemByName(IARESProject project,String name) throws Exception{
		MenuItem menu = getMetadataModelByName(project, name, IMetadataRefType.Menu, MenuItem.class);
		if(null == menu) {
			throw new Exception(String.format("�˵�[%s]�����ڡ�", name));
		}
		return menu;
	}
	
	/**
	 * ����Ԫ�������ƻ�ȡԪ�����ֶ�
	 * @param <T>
	 * @param project ����
	 * @param name �ֶ���
	 * @param restype Ԫ�������� 
	 * 			<li>�������{@link com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType}
	 * @param clazz Ԫ������
	 * @return MetadataItem Ԫ�����ֶ�
	 */
	public static <T> T  getMetadataModelByName(IARESProject project,String name,String restype,Class<T> clazz){
		ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(project, restype, name, true);
		if(ref != null){
			return (T)ref.getObject();
		}
		return null;
	}

}
