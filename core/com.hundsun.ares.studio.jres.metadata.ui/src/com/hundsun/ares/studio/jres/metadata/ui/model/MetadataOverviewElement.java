/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.metadata.ui.model;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;

/**
 * ����Ԫ���������б�ı���ж���
 * 
 * @author gongyf
 *
 */
public class MetadataOverviewElement {
	private IARESResource resource;
	private MetadataItem item;
	private boolean conflict;
	
	/**
	 * @param resource
	 * @param item
	 * @param conflict
	 */
	public MetadataOverviewElement(IARESResource resource, MetadataItem item) {
		super();
		this.resource = resource;
		this.item = item;
	}
	
	
	/**
	 * @param conflict the conflict to set
	 */
	public void setConflict(boolean conflict) {
		this.conflict = conflict;
	}


	/**
	 * @return the resource
	 */
	public IARESResource getResource() {
		return resource;
	}
	/**
	 * @return the item
	 */
	public MetadataItem getItem() {
		return item;
	}
	/**
	 * @return the conflict
	 */
	public boolean isConflict() {
		return conflict;
	}
	
	
	
}
