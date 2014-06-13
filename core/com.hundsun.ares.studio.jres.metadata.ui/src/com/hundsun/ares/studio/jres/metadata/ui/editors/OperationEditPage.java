/**
 * Դ�������ƣ�OperationEditPage.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.editors;

import java.util.EventObject;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.ui.forms.IManagedForm;

import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.Operation;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.EMFFormPage;
import com.hundsun.ares.studio.ui.editor.viewers.RefreshViewerJob;

/**
 * ���ڿɱ����û�������ģ�ͽ��в�����Ϣ�ı༭
 * @author gongyf
 *
 */
public class OperationEditPage extends EMFFormPage {
	
	private static final Logger logger = Logger.getLogger(OperationEditPage.class); 
	
	 
	private OperationBlock block;

	public OperationEditPage(EMFFormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	@Override
	protected void doCreateFormContent(IManagedForm managedForm) {
		// TODO#�����߼�#��Ҷ��#����#��Ԫ#����״̬ #���ʱ�� #������(�������հ��к�ע����) #��ʱ(��ȷ������) #��һ��master/Detailҳ�棬master���ǲ������б�detail���ǽ���xml�ʹ���
	
		block = new OperationBlock(this,getEditableControl());
		 
		block.createContent(managedForm);
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#commandStackChanged(java.util.EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		RefreshViewerJob.refresh(block.getViewer());
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormPage#isNeedValidate(org.eclipse.emf.transaction.ResourceSetChangeEvent)
	 */
	@Override
	protected boolean isNeedValidate(ResourceSetChangeEvent event) {
		for (Notification notification : event.getNotifications()) {
			if (notification.getFeature() == MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS
					|| notification.getNotifier() instanceof Operation) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the block
	 */
	public OperationBlock getBlock() {
		return block;
	}
	
	@Override
	public void infoChange() {
		block.setInput(getInfo());
		super.infoChange();
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			ColumnViewer viewer = block.getViewer();
			if (viewer != null) {
				getSite().setSelectionProvider(viewer);
				logger.debug("Selection provider: " + viewer.hashCode() + " Site: " + getSite().hashCode());
			} else {
				logger.debug("Page actived, but no viewer!");
			}
		} 
	}
	
}
