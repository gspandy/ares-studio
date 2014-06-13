/**
 * Դ�������ƣ�AddSampleAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.Operation;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAction;

/**
 * �����������ʾ������
 * @author wangxh
 *
 */
public class AddSampleAction extends ColumnViewerAction {

	private EObject owner;
	private EClass eClass;
	private String text;
	private ColumnViewer viewer;
	private URL url;
	private String scriptLocation;
	/**
	 * @param viewer
	 * @param editingDomain
	 * @param text  ����
	 * @param owner 
	 * @param path   ·��
	 * @param project
	 */
	public AddSampleAction(ColumnViewer viewer, EditingDomain editingDomain,String text,EObject owner, URL url,String scriptLocation) {
		super(viewer, editingDomain);
		setText(text);
		setEnabled(true);
		this.owner = owner;
		this.eClass=MetadataPackage.Literals.OPERATION;
		this.text=text;
		this.viewer=viewer;
		this.url=url;
		this.scriptLocation =  scriptLocation;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#createCommand()
	 */
	@Override
	protected Command createCommand() {
		//�����ṩ�Ľű��ļ�ʵ�ֲ���
		EObject newObj = eClass.getEPackage().getEFactoryInstance().create(eClass);
		Command command = AddCommand.create(getEditingDomain(), owner, 
				MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS, newObj);
		if(newObj instanceof Operation){
			((Operation)newObj).setTitle(text);
			((Operation)newObj).setFile(this.scriptLocation);
			((Operation)newObj).setFunctionName("main");
			InputStream is;
			try {
				is = FileLocator.toFileURL(url).openStream();
				((Operation)newObj).setCode(convertStreamToString(is));
			} catch (Exception e) {
				MessageDialog.openError(viewer.getControl().getShell(),"�ļ�������", "��ʾ���Ľű��ļ������ڣ�");
				e.printStackTrace();
				return null;
			}
		}
		return command;
	}

	/**
	 * @param is
	 * @return
	 */
	private String convertStreamToString(InputStream is) {
		 BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(is));
		        StringBuilder sb = new StringBuilder();    
		     
		        String line = null;    
		        try {    
		            while ((line = reader.readLine()) != null) {    
		                sb.append(line + "\n");    
		            }    
		        } catch (IOException e) {    
		            e.printStackTrace();    
		        } finally {    
		            try {    
		            	is.close();    
		            } catch (IOException e) {    
		                e.printStackTrace();    
		            }    
		        }    
		     
		        return sb.toString();    
			} catch (Exception e1) {
				MessageDialog.openError(viewer.getControl().getShell(),"�ļ����쳣", "��ʾ���Ľű��ļ����쳣��");
				e1.printStackTrace();
			}
			return null;
	}

}
