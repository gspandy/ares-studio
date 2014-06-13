/**
 * Դ�������ƣ�ICellLinkProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.viewers.link;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ViewerCell;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.util.Pair;


/**
 * @author gongyf
 *
 */
public interface ICellLinkProvider {
	Pair<EObject, IARESResource> getLinkedObject(ViewerCell cell);
}
