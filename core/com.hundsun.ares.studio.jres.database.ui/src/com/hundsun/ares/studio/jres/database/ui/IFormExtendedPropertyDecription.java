/**
 * Դ�������ƣ�IFormExtendedPropertyDecription.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hundsun.ares.studio.jres.model.database.DatabaseResourceData;
import com.hundsun.ares.studio.ui.editor.editable.IEditableControl;

/**
 * @author gongyf
 *
 */
public interface IFormExtendedPropertyDecription {
	
	
	/**
	 * �����༭�ؼ�
	 * @param parent
	 * @return
	 */
	Control createControl(Composite parent);
	
	/**
	 * ���ܱ���ε���
	 * @param info
	 */
	void setInfo(DatabaseResourceData info);
	
	/**
	 * �������ݰ�������
	 * @param context
	 */
	void setDataBindingContext(DataBindingContext context);
	
	/**
	 * ����ֻ��������
	 * @param editableControl
	 */
	void setEditableControl(IEditableControl editableControl);
}
