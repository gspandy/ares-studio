/**
 * Դ�������ƣ�DomainDataType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm.bean;


/**
 * @author liaogc
 *
 */
public class PDMDomainDataType {
	private String typeName = "";//������
	private String typeDesc = "";//��������
	private String typeReal = "";//��ʵ����
	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * @return the typeDesc
	 */
	public String getTypeDesc() {
		return typeDesc;
	}
	/**
	 * @param typeDesc the typeDesc to set
	 */
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	/**
	 * @return the typeReal
	 */
	public String getTypeReal() {
		return typeReal;
	}
	/**
	 * @param typeReal the typeReal to set
	 */
	public void setTypeReal(String typeReal) {
		this.typeReal = typeReal;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PDMDomainDataType) {
			return typeName.equals(((PDMDomainDataType) obj).typeName);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return typeName.hashCode();
	}
	
}
