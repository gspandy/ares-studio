/**
 * Դ�������ƣ�DatabasePrefernceInitializer.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.pages;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.database.ui.DatabaseUI;

/**
 * @author liaogc
 *
 */
public class DatabasePrefernceInitializer extends AbstractPreferenceInitializer {

	public DatabasePrefernceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences preferences = new DefaultScope().getNode(DatabaseUI.PLUGIN_ID);
		preferences.put(IDBConstant.TABLE_NAME_LENGTH, "26");
		preferences.put(IDBConstant.TABLE_COLUMN_LENGTH, "30");
		preferences.put(IDBConstant.INDEX_LENGTH, "26");
		preferences.put(IDBConstant.CONSTRAINT_LENGTH, "26");
		
	}
}
