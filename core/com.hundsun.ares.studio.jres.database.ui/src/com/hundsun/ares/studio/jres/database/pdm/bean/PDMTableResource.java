/**
 * Դ�������ƣ�PDMTableResource.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm.bean;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author liaogc
 *
 */
public class PDMTableResource {
	private IARESResource resource;//����Դ
	private TableResourceData tableInfo;//����Դ�еı�ģ��
	private String subSystem;//��������ϵͳ
	
	public PDMTableResource(IARESResource resource,TableResourceData tableInfo,String subSystem){
		this.resource = resource;
		this.tableInfo = tableInfo;
		this.subSystem = subSystem;
		
	}
	/**
	 * @return the resource
	 */
	public IARESResource getResource() {
		return resource;
	}
	
	/**
	 * @return the tableInfo
	 */
	public TableResourceData getTableInfo() {
		return tableInfo;
	}
	
	/**
	 * @return the suSystem
	 */
	public String getSubSystem() {
		return subSystem;
	}
	
	
}
