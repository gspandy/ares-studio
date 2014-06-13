/**
 * Դ�������ƣ�JumpAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.hundsun.ares.studio.core.builder.IAresMarkers;
import com.hundsun.ares.studio.jres.metadata.ui.model.MetadataOverviewElement;
import com.hundsun.ares.studio.ui.editor.actions.IUpdateAction;

/**
 * ����ҳ�����Ŀģ����<code> MetadataOverviewElement</code>
 * @author wangxh
 * 
 *
 */
public class JumpAction extends Action implements IUpdateAction{
	private Logger logger = Logger.getLogger(getClass());
	
	private ColumnViewer viewer;
	
	public JumpAction(ColumnViewer viewer) {
		super("��ת������");
		this.viewer = viewer;
		setId(IMetadataActionIDConstant.CV_JUMP);
		
	}
	
	/**
	 * @return the viewer
	 */
	public ColumnViewer getViewer() {
		return viewer;
	}
	
	/**
	 * ���᷵��null
	 * @return
	 */
	protected List<Object> getSelectedObjects() {
		ISelection selection = getViewer().getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).toList();
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public void run() {
		List<Object> objects = getSelectedObjects();
		if (objects.size() > 0 ) {
			MetadataOverviewElement element =  (MetadataOverviewElement) objects.get(0);
			
			if (element!=null && element.getItem() != null && element.getResource() != null) {
				try {
					IMarker marker = element.getResource().getResource().createMarker(IAresMarkers.BOOK_MARKER_ID);
					marker.setAttribute(IMarker.LOCATION, element.getItem().eResource().getURIFragment(element.getItem()));
					String editorId = IDE.getEditorDescriptor(element.getResource().getElementName()).getId();
					marker.setAttribute(IDE.EDITOR_ID_ATTR, editorId);
					
					IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), marker);
					marker.delete();
				} catch (PartInitException e) {
					logger.error(e.getMessage(), e);
				} catch (CoreException e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				logger.info("Ԫ������ĿΪ�ջ�����ԴΪ��");
			}
		}
	}
	@Override
	public void update() {
		setEnabled(getSelectedObjects().size()==1);
	}
}
