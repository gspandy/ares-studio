/**
 * Դ�������ƣ�ActionRegistry.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.edit.provider.Disposable;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.services.IDisposable;

/**
 * A container for editor actions. You must register the actions before they
 * will be available to the editor.
 */
public class ActionRegistry {

	/*
	 * A hashmap that contains the actions.
	 */
	private Map<String, IAction> map = new HashMap<String, IAction>(15);

	/**
	 * Calls dispose on all actions which implement the {@link Disposable}
	 * interface so they can perform their own clean-up.
	 */
	public void dispose() {
		Iterator<IAction> actions = getActions();
		while (actions.hasNext()) {
			IAction action = actions.next();
			if (action instanceof IDisposable)
				((IDisposable) action).dispose();
		}
	}

	public IAction getAction(String id) {
		IAction action = map.get(id);
		if (action == null) {
			System.out.println("action is null for id : " + id);
		}
		return action;
	}

	public Iterator<IAction> getActions() {
		return map.values().iterator();
	}


	public void registerAction(IAction action) {
		Assert.isNotNull(action.getId(), "action must have an ID in " + //$NON-NLS-1$
				getClass().getName() + " :registerAction(IAction)");//$NON-NLS-1$
		registerAction(action.getId(), action);
	}

	private void registerAction(String id, IAction action) {
		map.put(id, action);
	}

	public void removeAction(IAction action) {
		map.remove(action.getId());
	}

}
