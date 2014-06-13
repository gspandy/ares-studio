/**
 * Դ�������ƣ�IFileSyncnizeUnit.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.sync;

import org.eclipse.core.resources.IResourceDelta;

/**
 * �ļ�ͬ����Ԫ
 * @author lvgao
 *
 */
public interface IFileSyncnizeUnit {
	
	/**
	 * ����Ǳ��涯��,�ⲿ�޸ġ����ɾ����
	 * @param context
	 */
	public void handleAction(IResourceDelta delta);
	
	/**
	 * ����ǰ����
	 * ��ѡ�ڱ���ǰ����
	 */
	public void beforeSave();
}
