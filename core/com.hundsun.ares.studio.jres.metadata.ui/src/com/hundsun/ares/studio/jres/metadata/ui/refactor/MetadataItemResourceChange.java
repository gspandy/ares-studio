/**
 * Դ�������ƣ�MetadataItemResourceChange.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.refactor;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.ResourceTypeMapping;
import com.hundsun.ares.studio.core.model.Reference;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;

/**
 * @author qinyuan
 *
 */
public class MetadataItemResourceChange extends ResourceChange {
	
	private String oldName;
	private String newName;
	private MetadataItem item;
	private IARESResource resource;
	private EditingDomain editingDomain;
	
	/**
	 * @param oldName
	 * @param newName
	 * @param item
	 * @param editingDomain 
	 */
	public MetadataItemResourceChange(String oldName, String newName,
			MetadataItem item,IARESResource resource, EditingDomain editingDomain) {
		this.oldName = oldName;
		this.newName = newName;
		this.item = item;
		this.resource = resource;
		this.editingDomain = editingDomain;
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
		return item.getName() + " ������Ϊ: " + this.newName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.Change#perform(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		
		//������Դ���� 
		Command command = new ChangeCommand(item.eResource()) {
			
			@Override
			protected void doExecute() {
				EObject container = item.eContainer();
				@SuppressWarnings("unchecked")
				String type = ResourceTypeMapping.getInstance().getReferenceType(resource.getType());
				if(container instanceof MetadataResourceData){
					MetadataResourceData<? extends MetadataItem> resourceData = (MetadataResourceData<? extends MetadataItem>) container;
					for (Reference ref : resourceData.getReferences()) {
						// BUG #3155::ҵ���������������õı�׼�������ͼ�Ĭ��ֵ��ID��ҵ����������ID��ͬ��
						// ��Ҫ�ж���������
						if(ref.getType().equals(type) &&  ref.getValue().equals(oldName)) {
							ref.setValue(newName);
						}
					}
				}
				
			}
		};
		
		//��������
		command = command.chain(SetCommand.create(editingDomain, item, MetadataPackage.Literals.NAMED_ELEMENT__NAME, newName)) ;
		editingDomain.getCommandStack().execute(command);
		
		return new MetadataItemResourceChange(oldName,newName,item,resource,editingDomain);
	}

}
