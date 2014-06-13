/**
 * Դ�������ƣ�PDMBusinessDataType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm.bean;

import java.util.ArrayList;
import java.util.List;


/**
 * @author liaogc
 *
 */
public class PDMBusinessDataType {

	
	private String typeName ;//������
	private String oldTypeName ;//���;���
	private String typeChineseName;//����������
	private String standardTypeName;//��׼����
	private String length;//����
	private String precision ;//����
	private String defaultValue ;//Ĭ��ֵ
	private String comment;//˵��
	private List<String> belongStandardFieldList = new ArrayList<String>();//������׼�ֶ�
	private List<String> subSyses = new ArrayList<String>();//������ϵͳ

	/**
	 * @return the subSyses
	 */
	public List<String> getSubSyses() {
		return subSyses;
	}
	/**
	 * @return the oldTypeName
	 */
	public String getOldTypeName() {
		return oldTypeName;
	}
	/**
	 * @param oldTypeName the oldTypeName to set
	 */
	public void setOldTypeName(String oldTypeName) {
		this.oldTypeName = oldTypeName;
	}
	/**
	 * @return the belongStandardFieldList
	 */
	public List<String> getBelongStandardFieldList() {
		return belongStandardFieldList;
	}
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
	 * @return the typeChineseName
	 */
	public String getTypeChineseName() {
		return typeChineseName;
	}
	/**
	 * @param typeChineseName the typeChineseName to set
	 */
	public void setTypeChineseName(String typeChineseName) {
		this.typeChineseName = typeChineseName;
	}
	/**
	 * @return the standardTypeName
	 */
	public String getStandardTypeName() {
		return standardTypeName;
	}
	/**
	 * @param standardTypeName the standardTypeName to set
	 */
	public void setStandardTypeName(String standardTypeName) {
		this.standardTypeName = standardTypeName;
	}
	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}
	/**
	 * @return the precision
	 */
	public String getPrecision() {
		return precision;
	}
	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
