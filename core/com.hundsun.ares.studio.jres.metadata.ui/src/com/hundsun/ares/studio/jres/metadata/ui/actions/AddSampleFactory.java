/**
 * Դ�������ƣ�AddSampleFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.ConstantList;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryList;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoList;
import com.hundsun.ares.studio.jres.model.metadata.MenuList;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.StandardFieldList;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValueList;

/**
 * ��ͬԪ���ݸ�������ͬ�ű�ʵ������ʾ������
 * 
 * @author wangxh
 * 
 */
public class AddSampleFactory {

	public static List<IAction> getSampleActions(ColumnViewer viewer,
			EditingDomain editingDomain, EObject info,IARESResource res) {
		IARESProject project =  res.getARESProject();
		if (info instanceof ConstantList) {
			return getConstantSampleAction(viewer, editingDomain,
					(ConstantList) info, project);
		}
		if (info instanceof BusinessDataTypeList) {
			return getBizSampleAction(viewer, editingDomain,
					(BusinessDataTypeList) info, project);
		}
		if (info instanceof DictionaryList) {
			return getDicSampleAction(viewer, editingDomain,
					(DictionaryList) info, project);
		}
		if (info instanceof ErrorNoList) {
			return getErrorSampleAction(viewer, editingDomain,
					(ErrorNoList) info, project);
		}
		if (info instanceof StandardDataTypeList) {
			return getStdTypeSampleAction(viewer, editingDomain,
					(StandardDataTypeList) info, project);
		}
		if (info instanceof StandardFieldList) {
			return getStdfldSampleAction(viewer, editingDomain,
					(StandardFieldList) info, project);
		}
		if (info instanceof TypeDefaultValueList) {
			return getDefvalueSampleAction(viewer, editingDomain,
					(TypeDefaultValueList) info,  project);
		}
		if(info instanceof MenuList){
			return getMenuSampleAction(viewer, editingDomain,
					(MenuList) info,  project);
		}
		return new ArrayList<IAction>();

	}

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info
	 * @param project
	 * @return
	 */
	private static List<IAction> getMenuSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, MenuList info, IARESProject project) {
		List<IAction> actions = new ArrayList<IAction>();
		String text = "���¹����б�";
		String scriptDir = "tools";
		if(isOldProject(project)){
			scriptDir = "others";
		}
		URL url=null;
		try {
			url = new URL(project.getProject().getLocationURI()+"/"+scriptDir+"/"+"logical_menuFunction_infoUpdate.js");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IAction action = new AddSampleAction(viewer, editingDomain, text, info,
				url,"/"+project.getProject().getName()+"/"+scriptDir+"/"+"logical_menuFunction_infoUpdate.js");
		actions.add(action);
		
		
		text = "����SQL";
		try {
			url = new URL(project.getProject().getLocationURI()+"/"+scriptDir+"/"+"logical_menu_generate_sql.js");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		action = new AddSampleAction(viewer, editingDomain, text, info,
				url,"/"+project.getProject().getName()+"/"+scriptDir+"/"+"logical_menu_generate_sql.js");
		actions.add(action);
		return actions;
	}

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info
	 * @param bundle
	 * @return
	 */
	private static List<IAction> getDefvalueSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, TypeDefaultValueList info,
			IARESProject project) {
		// TODO Auto-generated method stub
		return new ArrayList<IAction>();
	}

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info
	 * @param bundle
	 * @return
	 */
	private static List<IAction> getStdfldSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, StandardFieldList info, IARESProject project) {
		// TODO Auto-generated method stub
		return new ArrayList<IAction>();
	}

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info
	 * @param bundle
	 * @return
	 */
	private static List<IAction> getStdTypeSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, StandardDataTypeList info,
			IARESProject project) {
		// TODO Auto-generated method stub
		return new ArrayList<IAction>();
	}

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info
	 * @param bundle
	 * @return
	 */
	private static List<IAction> getErrorSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, ErrorNoList info, IARESProject project) {
		List<IAction> actions = new ArrayList<IAction>();
		String text = "��������Ŷ����ļ�";
		String scriptDir = "tools";
		if(isOldProject(project)){
			scriptDir = "others";
		}
		URL url=null;
		try {
			url = new URL(project.getProject().getLocationURI()+"/"+scriptDir+"/"+"logical_stderrorno_generate_java.js");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IAction action = new AddSampleAction(viewer, editingDomain, text, info,
				url,"/"+project.getProject().getName()+"/"+scriptDir+"/"+"logical_stderrorno_generate_java.js");
		actions.add(action);
		text = "���ɴ���������ļ�";
		try {
			url  =  new URL(project.getProject().getLocationURI()+"/"+scriptDir+"/"+"logical_stderrorno_generate_properties.js");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		action = new AddSampleAction(viewer, editingDomain, text, info, url,"/"+project.getProject().getName()+"/"+scriptDir+"/"+"logical_stderrorno_generate_properties.js");
		actions.add(action);
		return actions;
	}

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info
	 * @param bundle
	 * @return
	 */
	private static List<IAction> getDicSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, DictionaryList info, IARESProject project) {
		List<IAction> actions = new ArrayList<IAction>();
		String text = "�����ֵ䳣��";
		String scriptDir = "tools";
		if(isOldProject(project)){
			scriptDir = "others";
		}
		URL url=null;
		try {
			url = new URL(project.getProject().getLocationURI()+"/"+scriptDir+"/"+"logical_dictionary_generate_java.js");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IAction action = new AddSampleAction(viewer, editingDomain, text, info,
				url,"/"+project.getProject().getName()+"/"+scriptDir+"/"+"logical_dictionary_generate_java.js");
		actions.add(action);
		text = "���ɱ��ʼ����";
		try {
			url =  new URL(project.getProject().getLocationURI()+"/"+scriptDir+"/"+"database_dictionary_generate_oracle.js");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		action = new AddSampleAction(viewer, editingDomain, text, info, url,"/"+project.getProject().getName()+"/"+scriptDir+"/"+"database_dictionary_generate_oracle.js");
		actions.add(action);
		return actions;
	}

	

	/**
	 * @param viewer
	 * @param editingDomain
	 * @param info
	 * @param bundle
	 * @return
	 */
	private static List<IAction> getBizSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, BusinessDataTypeList info,
			IARESProject project) {
		// TODO Auto-generated method stub
		return new ArrayList<IAction>();
	}

	/**
	 * @param editingDomain
	 * @param viewer
	 * @param info
	 * @param bundle
	 * @return
	 */
	private static List<IAction> getConstantSampleAction(ColumnViewer viewer,
			EditingDomain editingDomain, ConstantList info, IARESProject project) {
		List<IAction> actions = new ArrayList<IAction>();
		String text = "����JAVA����������";
		String scriptDir = "tools";
		if(isOldProject(project)){
			scriptDir = "others";
		}
		URL url=null;
		try {
			url = new URL(project.getProject().getLocationURI()+"/"+scriptDir+"/"+"logical_constant_generate_java.js");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IAction action = new AddSampleAction(viewer, editingDomain, text, info,
				url,"/"+project.getProject().getName()+"/"+scriptDir+"/"+"logical_constant_generate_java.js");
		actions.add(action);
		return actions;
	}
	private static boolean isOldProject(IARESProject project) {
		try {
			return !project.getProject().hasNature(
					"com.hundsun.ares.studio.jres.core.modulenature");
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;

	}

}
