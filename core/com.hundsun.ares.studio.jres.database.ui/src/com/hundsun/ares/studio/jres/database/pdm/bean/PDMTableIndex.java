/**
 * Դ�������ƣ�PDMTableIndex.java
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
public class PDMTableIndex {
	private String name;//������
	private boolean unique;//�Ƿ�Ψһ
	private boolean cluster;//�Ƿ��������
	private List<PDMTableIndexColumn> columns = new ArrayList<PDMTableIndexColumn>();//������
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
	 * @return the unique
	 */
	public boolean isUnique() {
		return unique;
	}
	/**
	 * @param unique the unique to set
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	/**
	 * @return the cluster
	 */
	public boolean isCluster() {
		return cluster;
	}
	/**
	 * @param cluster the cluster to set
	 */
	public void setCluster(boolean cluster) {
		this.cluster = cluster;
	}
	/**
	 * @return the columns
	 */
	public List<PDMTableIndexColumn> getColumns() {
		return columns;
	}
	
	
}
