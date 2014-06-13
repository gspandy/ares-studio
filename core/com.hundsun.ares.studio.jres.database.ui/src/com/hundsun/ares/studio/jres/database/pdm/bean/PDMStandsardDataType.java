/**
 * Դ�������ƣ�PDMStandsardDataType.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm.bean;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liaogc
 *
 */
public class PDMStandsardDataType {
	private String typeName;//������
	private String typeChineseName;//����������
	private String comment;//˵��
	private Map<String,String> languageTypeValue = new LinkedHashMap<String,String>();
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
	
	public Set<String> getLanguages(){
		return languageTypeValue.keySet();
		
	}
	
	public String getTypeValueByLanguage(String language) {
		return languageTypeValue.get(language);
	}

}
