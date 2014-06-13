/**
 * Դ�������ƣ�UncategorizedItemsCategoryImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.impl.MetadataCategoryImpl;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;

/**
 * @author gongyf
 *
 */
public class UncategorizedItemsCategoryImpl extends MetadataCategoryImpl {
	
	private MetadataResourceData<?> data;
	
	/**
	 * @param data
	 */
	public UncategorizedItemsCategoryImpl(MetadataResourceData<?> data) {
		super();
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.metadata.impl.MetadataCategoryImpl#getName()
	 */
	@Override
	public String getName() {
		return "δ����";
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.metadata.impl.MetadataCategoryImpl#getDescription()
	 */
	@Override
	public String getDescription() {
		return "��ǰ��Դ��û�з������Ŀ";
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.metadata.impl.MetadataCategoryImpl#setName(java.lang.String)
	 */
	@Override
	public void setName(String newName) {
		// dothing
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.metadata.impl.MetadataCategoryImpl#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String newDescription) {
		// dothing
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.model.metadata.impl.MetadataCategoryImpl#getItems()
	 */
	@Override
	public EList<MetadataItem> getItems() {
		return new BasicEList<MetadataItem>(MetadataUtil.getUncategorizedItems(data));
	}
}
