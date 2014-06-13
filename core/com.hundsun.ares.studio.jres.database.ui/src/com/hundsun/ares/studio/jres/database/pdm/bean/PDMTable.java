/**
 * Դ�������ƣ�PDMTable.java
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
public class PDMTable {
	private String packageName;//����(PDM�еİ���)
	private String name;//����
	private String chineseName;//��������
	private String desc;//��˵��
	private List<PDMTableColumn>columns = new ArrayList<PDMTableColumn>();//����е���
	private List<PDMTableIndex> indexes = new ArrayList<PDMTableIndex>();//�������
	/**
	 * @return the indexes
	 */
	public List<PDMTableIndex> getIndexes() {
		return indexes;
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
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the columns
	 */
	public List<PDMTableColumn> getColumns() {
		return columns;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
