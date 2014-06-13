/**
 * Դ�������ƣ�DBModuleDatabaseSelectionDialog.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.hundsun.ares.studio.jres.database.ui.editors.dialog.SelectDialog;
import com.hundsun.ares.studio.jres.model.database.DBModuleCommonProperty;

/**
 * @author wangbin
 *
 */
public class DBModuleDatabaseSelectionDialog extends SelectDialog{
	
	/**���ݿ�����*/
	static List<String> databaseTypeList = Arrays.asList("Oracle","DB2","MySQL","SQL Sever","Informix","Sybase");

	/**
	 * @param parentShell
	 * @param Title
	 */
	protected DBModuleDatabaseSelectionDialog(Shell parentShell, 
			String Title, 
			DBModuleCommonProperty model) {
		super(parentShell, Title, getChoiceInput(model), getResultInput(model), getLabelProvider());
	}
	
	protected static List<String> getResultInput(DBModuleCommonProperty model) {
		return getData(model);
	}
	
	/**
	 * ����ȡ����database�б�װ����List
	 */
	protected static List<String> getData(DBModuleCommonProperty model){
		List<String> dbType = new ArrayList<String>();
		String database = model.getSupportDatabases();
		if(StringUtils.isNotBlank(database)){
			if(StringUtils.contains(database, ",")){
				dbType.addAll(Arrays.asList(StringUtils.split(database, ",")));
			}else{
				dbType.add(database);	
			}
		}
		return dbType;
	}
	
	protected static List<String> getChoiceInput(DBModuleCommonProperty model) {
		
		List<String> choiceInput = new ArrayList<String>();
		
		List<String> dataType = getData(model);
		
		for(int i = 0; i<databaseTypeList.size(); i++ ){
			
			String databaseType = databaseTypeList.get(i);
			
			if(!dataType.contains(databaseType)){
				choiceInput.add(databaseType);
			}
		}
		return choiceInput;
	}

	protected static ILabelProvider getLabelProvider() {
		return new ILabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			
			@Override
			public void dispose() {
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				
			}
			
			@Override
			public String getText(Object element) {
				return (String) element;
			}
			
			@Override
			public Image getImage(Object element) {
				return null;
			}
		};
	}
	
}
