/**
 * Դ�������ƣ�IUserOptionControlProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.script
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.script.internal.useroption.control;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.script.internal.useroption.IControl;

/**
 * @author sundl
 *
 */
public interface IUserOptionControlProvider {

	IControl createControl();
	
	Control createUIControl(Composite parent, IControl control, IARESProject project);
}
