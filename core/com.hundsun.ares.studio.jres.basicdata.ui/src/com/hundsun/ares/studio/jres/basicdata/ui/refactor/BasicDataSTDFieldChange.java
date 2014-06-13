/**
 * Դ�������ƣ�MetadataItemResourceChange.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.refactor;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * @deprecated �������˷���������ʱ��ʵ�ֱ���
 * @author Administrator
 *
 */

public class BasicDataSTDFieldChange extends ResourceChange {
	
	private static Logger logger = Logger.getLogger(BasicDataSTDFieldChange.class);
	
	private String oldName;
	private String newName;
	private IARESResource resource;
	
	/**
	 * @param oldName
	 * @param newName
	 * @param item
	 * @param editingDomain 
	 */
	public BasicDataSTDFieldChange(IARESResource resource,String oldName, String newName) {
		this.oldName = oldName;
		this.newName = newName;
		this.resource = resource;
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
		SAXReader reader = new SAXReader();
		IFile file = (IFile)resource.getResource();
		try {
			Document doc =  reader.read(file.getContents());
			StringBuffer buffer = new StringBuffer();
//			buffer.append("/" + IBasicDataEpacakgeConstant.Attr_Master_Items);
			buffer.append("//@" + oldName);
			changeAttrName(doc.selectNodes(buffer.toString()),newName);
			file.setContents(new ByteArrayInputStream(doc.asXML().getBytes("UTF-8")), IResource.FORCE, pm);
		
		} catch (Exception e) {
			logger.error(String.format("��ȡ�ļ�%sʧ��,��ϸ��Ϣ:%s", file.getFullPath().toOSString(),e.getMessage()));
		}
		
		
		return new BasicDataSTDFieldChange(resource,oldName,newName);
	}
	
	private void changeAttrName(List objs,String value){
		for(Object obj:objs){
			if(obj instanceof Attribute){
				Attribute attr = ((Attribute)obj);
				Element element =  attr.getParent();
				element.addAttribute(value, attr.getValue());
				attr.detach();
			}
		}
	}

}
