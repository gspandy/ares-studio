/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.core;

import java.util.HashMap;
import java.util.Map;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ARESModelStatus;
import com.hundsun.ares.studio.core.IARESElement;

/**
 * ���ARESģ�Ͳ�����Operation
 * @author sundl
 */
public abstract class MultiOperation extends ARESModelOperation {
	
	/** �����ƶ�������ʱ���Ŀ��Ԫ�� */
	protected Map<IARESElement, IARESElement> newParents;
	/** ���������б� */
	protected String[] renamingList = null;	
	// elements ---> newName
	protected Map<IARESElement, String> renamings;
	
	public MultiOperation(IARESElement[] elementsToProcess, IARESElement[] newParent, boolean force) {
		super(elementsToProcess, newParent, force);
		newParents = new HashMap<IARESElement, IARESElement>();
		if (newParent == null)
			return;
		if (elementsToProcess.length == newParent.length) {
			for (int i = 0; i < elementsToProcess.length; i++) {
				newParents.put(elementsToProcess[i], newParent[i]);
			}
		} else {
			for (int i = 0; i < elementsToProcess.length; i++) {
				newParents.put(elementsToProcess[i], newParent[0]);
			}
		}
	}
	
	private void initializeRenamings() {
		if (renamingList != null && renamingList.length == elementsToProcess.length) {
			this.renamings = new HashMap<IARESElement, String>(renamingList.length);
			for (int i = 0; i < renamingList.length; i++) {
				this.renamings.put(elementsToProcess[i], renamingList[i]);
			}
		}
	}
	
	protected void excuteOperation() throws ARESModelException {
		processElements();		
	}
	
	protected String getNewNameFor(IARESElement element) {
		String name = null;
		if (this.renamings != null) {
			name = this.renamings.get(element);
		}
		return name;
	}
	
	/** ����������֣�����ʵ�� */
	protected abstract String getMainTaskName();
	
	protected void processElements() throws ARESModelException {
		try {
			beginTask(getMainTaskName(), this.elementsToProcess.length);
			for (IARESElement element : elementsToProcess) {
				try {
					vertify(element);
					processElement(element);
				} catch (ARESModelException e) {
					e.printStackTrace();
				} finally {
					worked(1);
				}
			}
		} catch (Exception e) {
		} finally {
			done();
		}
	}
	
	public void setRenamingList(String[] renamingList) {
		this.renamingList = renamingList;
		initializeRenamings();
	}  
	
	protected IARESElement getDestinationParent(IARESElement child) {
		return this.newParents.get(child);
	}
	
	/**
	 * ÿ��ִ��processElement֮ǰ�������element���ã���������Ƿ����ִ��
	 */
	protected abstract void vertify(IARESElement element) throws ARESModelException;
	
	/**
	 * �Ը�����Ԫ��ִ�����operation,������ʵ�����������
	 */
	protected abstract void processElement(IARESElement element) throws ARESModelException;
	
	/**
	 * �׳�һ��ARESModelException
	 */
	protected void error(int code, IARESElement element) throws ARESModelException{
		throw new ARESModelException(new ARESModelStatus(code, element));
	}
}
