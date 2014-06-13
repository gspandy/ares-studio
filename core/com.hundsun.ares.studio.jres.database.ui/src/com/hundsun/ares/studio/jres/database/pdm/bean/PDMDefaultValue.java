/**
 * Դ�������ƣ�PDMDefaultValue.java
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
public class PDMDefaultValue {

	private String name;//������
	private String chineseName;//����������
	private String comment;//˵��
	private Map<String,String> languageDefaultValue = new LinkedHashMap<String,String>();
	/**
	
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
		return languageDefaultValue.keySet();
		
	}
	
	public String getDefaultValueByLanguage(String language) {
		return languageDefaultValue.get(language);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the chineseName
	 */
	public String getChineseName() {
		return chineseName;
	}
	/**
	 * @param chineseName the chineseName to set
	 */
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}


}
