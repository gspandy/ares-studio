/**
 * Դ�������ƣ�PutCommand.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.core.model.util;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.ChangeCommand;

/**
 * �����޸�map��ֵ
 * @author gongyf
 *
 */
public class PutCommand extends ChangeCommand {

	private EObject owner;
	private EReference reference;
	private Object key;
	private Object value;
	
	
	
	/**
	 * @param owner
	 * @param reference
	 * @param key
	 * @param value
	 */
	public PutCommand(EObject owner,
			EReference reference, Object key, Object value) {
		super(owner);
		this.owner = owner;
		this.reference = reference;
		this.key = key;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.command.ChangeCommand#doExecute()
	 */
	@Override
	protected void doExecute() {
		EMap<Object, Object> map = (EMap<Object, Object>) owner.eGet(reference);
		map.put(key, value);
	}

	@Override
	public Collection<?> getAffectedObjects() {
		Collection<Object> result = new ArrayList<Object>();
		result.add(owner);
		return result;
	}

}
