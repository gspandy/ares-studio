/**
 * Դ�������ƣ�IBusinessDataType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.metadata.service;

/**
 * @author gongyf
 *
 */
public interface IBusinessDataType extends IMetadataItem {
	ITypeDefaultValue getDefaultValue();
	IStandardDataType getStdType();
	
	String getDefaultValueId();
	String getLength();
	String getPrecision();
	String getRealDefaultValue(String typeId);
	String getRealType(String typeId);
}
