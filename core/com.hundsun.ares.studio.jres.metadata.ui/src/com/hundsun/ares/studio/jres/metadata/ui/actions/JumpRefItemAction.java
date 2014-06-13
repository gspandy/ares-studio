/**
 * Դ�������ƣ�JumpRefItemAction.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.builder.IAresMarkers;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;

/**
 * @author wangxh
 *
 */
public class JumpRefItemAction extends Action {
	IARESResource res;
	MetadataItem item;
	private Logger logger = Logger.getLogger(getClass());
	public JumpRefItemAction(IARESResource res, MetadataItem item) {
		super();
		this.item = item;
		this.res = res;
	}
	@Override
	public void run() {
		try {
			try {
				IMarker marker = res.getResource().createMarker(IAresMarkers.BOOK_MARKER_ID);
				marker.setAttribute(IMarker.LOCATION, item.eResource().getURIFragment(item));
				IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), marker);
				marker.delete();
			} catch (PartInitException e) {
				logger.error(e.getMessage(), e);
			} catch (CoreException e) {
				logger.error(e.getMessage(), e);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
