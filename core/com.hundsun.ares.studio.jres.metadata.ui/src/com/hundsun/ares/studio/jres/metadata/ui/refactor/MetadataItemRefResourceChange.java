/**
 * Դ�������ƣ�MetadataItemRefResourceChange.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.refactor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.ResourceTypeMapping;
import com.hundsun.ares.studio.core.model.IReferenceProvider;
import com.hundsun.ares.studio.core.model.Reference;
import com.hundsun.ares.studio.core.model.impl.ConditionReference;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.core.registry.IResDescriptor;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;

/**
 * @author qinyuan
 *
 */
public class MetadataItemRefResourceChange  extends ResourceChange {
	
	private String oldName;
	private String newName;
	private MetadataItem item;
	private IARESResource selfResource; 	//Ԫ���ݵ���Դ���������ķ�����
	private IARESResource resource; 	//��������item���ڵ���Դ
	
	/**
	 * @param oldName
	 * @param newName
	 * @param item
	 */
	public MetadataItemRefResourceChange(String oldName, String newName,IARESResource selfRes ,IARESResource tarRes) {
		this.oldName = oldName;
		this.newName = newName;
		this.selfResource = selfRes;
		this.resource = tarRes;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.resource.ResourceChange#getModifiedResource()
	 */
	@Override
	protected IResource getModifiedResource() {
		return resource.getResource();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#getName()
	 */
	@Override
	public String getName() {
		return oldName + " ������Ϊ: " + this.newName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		
		Class<?> clazz = null;
		IReferenceProvider provider = (IReferenceProvider)resource.getAdapter(IReferenceProvider.class);
		String type = ResourceTypeMapping.getInstance().getReferenceType(selfResource.getType());
		boolean isChanged = false;
		for ( Reference  reference : provider.getReferences()) {
			if (reference instanceof ConditionReference){//��������������reference
				Map<Object,Object> parameters = new HashMap<Object,Object>();
				String version = selfResource.getARESProject().getProjectProperty().getVersion();
				parameters.put("oldValue", oldName);
				parameters.put("newValue", newName);
				parameters.put("version", version);
				if(StringUtils.equals(type, reference.getType())&& ((ConditionReference)reference).canDo(parameters)){//�Ƿ��ܹ�Ҫ��������������ȷ��
					reference.setValue(newName);
					isChanged = true;
				}
			}else if(StringUtils.equals(type, reference.getType()) && StringUtils.equals(oldName, reference.getValue())){//һ�����ع�
				reference.setValue(newName);
				isChanged = true;
			}
		}
		//����޸ļ�¼
		ARESResRegistry reg = ARESResRegistry.getInstance();
		IResDescriptor desc = reg.getResDescriptor(resource.getElementName());
		if (desc == null){
			desc = reg.getResDescriptor(resource.getType());
		}
		clazz = desc.createInfo().getClass();	
		
		
		if(isChanged){
			//����޸ļ�¼
			resource.save(resource.getInfo(clazz), true, null);//ÿ����Դֻ����һ��
		}
		
		
		return new MetadataItemRefResourceChange(newName , oldName ,selfResource , resource);
	}



}
