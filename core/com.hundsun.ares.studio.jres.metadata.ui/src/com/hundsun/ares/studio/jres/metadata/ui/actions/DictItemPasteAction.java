/**
 * Դ�������ƣ�DictItemPasteAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.ui.editor.actions.JresPasteAction;

/**
 * 
 * @author sundl
 */
public class DictItemPasteAction extends JresPasteAction {

	@Override
	protected Object getFeature() {
		return MetadataPackage.Literals.DICTIONARY_TYPE__ITEMS;
	}
	
}
