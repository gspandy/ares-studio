/**
 * Դ�������ƣ�ModifyActionComposite.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author wangxh
 *
 */
public abstract class ModifyActionComposite extends Composite {

	//�޸���ϸ��Ϣ
	Modification action ;
	protected IARESResource resource;
	
	protected TableResourceData tableData;

	/**
	 * �����޶����ݵ�һ�㵯������ʽ
	 * @param parent
	 * @param resource
	 * @param modification
	 */
	public ModifyActionComposite(Composite parent, TableResourceData tableData, IARESResource resource,Modification modification) {
		super(parent, SWT.None);
		this.resource = resource;
		this.tableData = tableData;
		initAction(modification);
		setLayout(new GridLayout(2,false));
		creatDetailComposite(this,resource);
	}
	
	/**
	 * ��ʼ���޸���Ϣ
	 * @param action
	 */
	protected abstract void initAction(Modification action);
	
	/**
	 * ������ϸ����
	 * @param parent
	 * @param resource
	 */
	protected abstract void creatDetailComposite(Composite parent, IARESResource resource);
	
	/**
	 * ��ȡ�����޸���Ϣ
	 * @return the action
	 */
	public abstract Modification getAction();
	
	public boolean canFinish() {
		return false;
	}
}
