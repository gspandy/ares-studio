/**
 * Դ�������ƣ�ExtensibleModelTrigger.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;

/**
 * ��Դ����������ExtensibleModel���г�ʼ��
 * @author gongyf
 *
 */
public class ExtensibleModelTrigger extends TriggerListener {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.TriggerListener#trigger(org.eclipse.emf.transaction.TransactionalEditingDomain, org.eclipse.emf.common.notify.Notification)
	 */
	@Override
	protected Command trigger(TransactionalEditingDomain domain,
			Notification notification) {
		
//		if (notification.getEventType() == Notification.) {
//			
//		}
		return null;
	}

}
