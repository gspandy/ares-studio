/**
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.metadata.service;

import java.util.List;

import com.hundsun.ares.studio.core.service.IDataService;

/**
 * @author gongyf
 *
 */
public interface IMetadataService extends IDataService {
	
	/**
	 * ��ȡ���п���Ĭ��ֵ�����������ù��̺����ð�
	 * @return
	 */
	List<ITypeDefaultValue> getTypeDefaultValueList();
	
	/**
	 * ��ȡָ�����Ƶ�Ĭ��ֵ
	 * @param name
	 * @return
	 */
	ITypeDefaultValue getTypeDefaultValue(String name);
	
	/**
	 * ��ȡ���п��õı�׼�������ͣ����������ù��̺����ð�
	 * @return
	 */
	List<IStandardDataType> getStandardDataTypeList();
	
	/**
	 * ��ȡָ�����Ƶı�׼��������
	 * @param name
	 * @return
	 */
	IStandardDataType getStandardDataType(String name);
	
	/**
	 * ��ȡ���п��õ�ҵ���������ͣ����������ù��̺����ð�
	 * @return
	 */
	List<IBusinessDataType> getBusinessDataTypeList();
	
	/**
	 * ��ȡָ�����Ƶ�ҵ����������
	 * @param name
	 * @return
	 */
	IBusinessDataType getBusinessDataType(String name);
	
	/**
	 * ��ȡ���п��õı�׼�ֶΣ����������ù��̺����ð�
	 * @return
	 */
	List<IStandardField> getStandardFieldList();
	
	/**
	 * ��ȡָ�����Ƶı�׼�ֶ�
	 * @param name
	 * @return
	 */
	IStandardField getStandardField(String name);
	
	/**
	 * ��ȡ���п��õĴ���ţ����������ù��̺����ð�
	 * @return
	 */
	List<IErrorNoItem> getErrorNoList();
	
	/**
	 * ��ȡָ�����ƵĴ����
	 * @param name
	 * @return
	 */
	IErrorNoItem getErrorNo(String name);
	
	/**
	 * ��ȡ���п��õ��û����������������ù��̺����ð�
	 * @return
	 */
	List<IUserConstantItem> getUserConstantList();
	
	/**
	 * ��ȡָ�����Ƶ��û�����
	 * @param name
	 * @return
	 */
	IUserConstantItem getUserConstant(String name);
	
	/**
	 * ��ȡ���п��õ��ֵ���Ŀ�����������ù��̺����ð�
	 * @return
	 */
	List<IDictionaryType> getDictionaryTypeList();
	
	/**
	 * ��ȡָ�����Ƶ��ֵ���Ŀ
	 * @param name
	 * @return
	 */
	IDictionaryType getDictionary(String name);
}
