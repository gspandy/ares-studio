/**
 * Դ�������ƣ�MetadataListPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.actions.ButtonGroupManager;
import com.hundsun.ares.studio.ui.editor.blocks.ColumnViewerListPage;

/**
 * ���Ԫ���ݵı༭������<br>
 * �ڱ��ť���������˺ͽű������İ�ť���ܹ����ݽű���̬����Ӻ�ɾ����ť<br>
 * �ṩ��{@link #createScriptContext()} �����ű�������
 * 
 * @author gongyf
 * @author sundl mark Deprecated 
 */
@Deprecated 
public abstract class MetadataListPage extends ColumnViewerListPage {

	private OperationButtonGroupControl obgc;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public MetadataListPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	protected IARESResource getARESResource() {
		return getEditor().getARESResource();
	}
	
	/**
	 * @return the obgc
	 */
	protected OperationButtonGroupControl getOperationControl() {
		return obgc;
	}

	//����һ�㰴ť�͸��ݽű�������̬���ɵİ�ť
	protected Control createViewerButtons(Composite parent, FormToolkit toolkit) {
		Composite client = toolkit.createComposite(parent);
		
		ButtonGroupManager btnGroupManager = new ButtonGroupManager();
		createButtons(btnGroupManager);
		btnGroupManager.createControl(client);
		
		ButtonGroupManager operationGroupManager = new ButtonGroupManager();
		//obgc = new OperationButtonGroupControl(operationGroupManager);
		operationGroupManager.createControl(client);
		
		toolkit.adapt(btnGroupManager.getControl());
		toolkit.adapt(operationGroupManager.getControl());
		
		GridLayoutFactory.fillDefaults().applyTo(client);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(btnGroupManager.getControl());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(operationGroupManager.getControl());
		
		return client;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#isNeedValidate(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	protected boolean isNeedValidate(ResourceSetChangeEvent event) {
		for (Notification notification : event.getNotifications()) {
			if (notification.getFeature() == MetadataPackage.Literals.METADATA_CATEGORY__CHILDREN
					|| notification.getFeature() == MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS
					|| notification.getNotifier() instanceof MetadataItem
					|| notification.getNotifier() instanceof MetadataCategory) {
				return true;
			}
		}
		return false;
	}

	//��ȡԪ�����б�
	protected MetadataResourceData<?> getInfo() {
		return (MetadataResourceData<?>) getEditor().getInfo();
	}
	

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.ColumnViewerListPage#infoChange()
	 */
	@Override
	public void infoChange() {
		getOperationControl().setData(getInfo());
		super.infoChange();
		//getOperationControl().setContext(createScriptContext());
	}
	
	//�����ű�������
	protected Map<String, Object> createScriptContext() {
		return ScriptUtils.createDefaultScriptContext(ScriptUtils.MODE_EDITOR_BUTTON,getARESResource(), getARESResource(), getInfo(), getClass().getClassLoader());
	}
}
