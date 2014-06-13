/**
 * Դ�������ƣ�MetadataOverviewContentProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.model.MetadataOverviewElement;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author wangxh
 *
 */
public class MetadataOverviewContentProvider implements ITreeContentProvider {

	private IARESResource resource;
	private String type;

	/**
	 * 
	 */
	public MetadataOverviewContentProvider(IARESResource resource, String type) {
		this.resource = resource;
		this.type = type;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	

	@Override
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		
		IARESProject curPrj = resource.getARESProject();
		
		List<ReferenceInfo> infoList = ReferenceManager.getInstance().getReferenceInfos(curPrj, type, true);
		List< MetadataOverviewElement > elementList = new ArrayList<MetadataOverviewElement>();
		
		for (ReferenceInfo referenceInfo : infoList) {
			IARESResource res = referenceInfo.getResource();
			MetadataItem owner = (MetadataItem) referenceInfo.getObject();
			
			// FIXME: ��Ӳ��Ǳ����̵���Ϣ����������Ϣʹ�õ�ǰ�༭ģ�͵�����
			elementList.add(new MetadataOverviewElement(res, owner));
		}
		
		
		// ����ͬ����ͻ����Ŀ
		{
			Map<String, MetadataOverviewElement> nameSet = new HashMap<String, MetadataOverviewElement>();
			Set<String> errNameSet =  new HashSet<String>();
			
			for (MetadataOverviewElement element : elementList) {
				String name = element.getItem().getName();
				if (errNameSet.contains(name)) {
					// ���Ѿ��ظ�������
					element.setConflict(true);
				} else if (nameSet.containsKey(name)) {
					// ͬ���������Ѿ����Ǽǣ���������������������ƶ������������б���
					element.setConflict(true);
					
					MetadataOverviewElement firstElement = nameSet.get(name);
					firstElement.setConflict(true);
					nameSet.remove(name);
					errNameSet.add(name);
				} else {
					element.setConflict(false);
					nameSet.put(name, element);
				}
			}
		}
		
		return elementList.toArray();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

}
