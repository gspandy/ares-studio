/**
 * Դ�������ƣ�JresActionEditableUnit.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editable;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;

/**
 * @author lvgao
 *
 */
public class JresActionEditableUnit implements IEditableUnit{

	IAction action;
	public JresActionEditableUnit(IAction action){
		this.action = action;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editors.editable.IEditableUnit#setReadonlyStatus(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setReadonlyStatus(String key, Object status) {
		if(StringUtils.equals(KEY_SYSTEM, key)){
			if(EDITABLE_TRUE.equals(status)){
				this.action.setEnabled(true);
			}else{
				this.action.setEnabled(false);
			}
		}
	}

}
