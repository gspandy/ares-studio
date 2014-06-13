package com.hundsun.ares.studio.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.ui.editor.blocks.OpenResourcePage;
import com.hundsun.ares.studio.ui.editor.sync.IFileSyncnizeUnit;
import com.hundsun.ares.studio.ui.editor.sync.JRESDefaultSyncnizeUnit;
import com.hundsun.ares.studio.ui.editor.sync.JRESEditorSyncManager;

/**
 * 2013��3��7��16:41:47 mod ��Ԫ ģ��༭��������ʾ��������Ӣ���� 
 * @author qinyuan
 */
public class ModulePropertyEditor extends AbstractHSFormEditor<ModuleProperty> {
	//�ļ�ͬ����Ԫ
	private IFileSyncnizeUnit fileSyncUnit;
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.AbstractHSFormEditor#getModelType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Class getModelType() {
		return ModuleProperty.class;
	}
	
	@Override
	protected void addPages() {
		ModulePropertyBasicPage basic = new ModulePropertyBasicPage(this);
		try {
			addPage(basic);
			IARESElement element = getARESElement();
			if (element instanceof IARESResource) {
				element = ((IARESResource) element).getModule();
			}
			addPage(new OpenResourcePage(this, element, "openresource", "��Դ�б�"));
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		super.addPages();
		fileSyncUnit = new JRESDefaultSyncnizeUnit(this);
		JRESEditorSyncManager.getInstance().addSyncUnit(fileSyncUnit);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.AbstractHSFormEditor#getPartTitleName()
	 * 
	 * 2013��3��7��16:41:47 mod ��Ԫ ģ��༭��������ʾ��������Ӣ���� 
	 */
	@Override
	protected String getPartTitleName() {
		String eName = getResource().getModule().getShortName();
		
		return String.format("%s(%s)", eName,info.getValue(info.CNAME));
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.BasicAresFormEditor#dispose()
	 */
	@Override
	public void dispose() {
		JRESEditorSyncManager.getInstance().removeSyncUnit(fileSyncUnit);
		super.dispose();
	}
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.editor.AbstractHSFormEditor#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		 fileSyncUnit.beforeSave();
		if (isReadOnly()) {
			MessageDialog.openInformation(getSite().getShell(), "�޷�����",
					"��ǰ��Դ��ֻ��״̬���޷����б���");
		} else {
			super.doSave(monitor);
		}
	}
}
