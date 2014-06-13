/**
 * Դ�������ƣ�ExtensibleModelColumnViewerProblemView.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnViewerProblemView;
import com.hundsun.ares.studio.ui.editor.viewers.RefreshViewerJob;
import com.hundsun.ares.studio.ui.validate.ProblemPoolChangeEvent;

/**
 * @author gongyf
 *
 */
public class ExtensibleModelColumnViewerProblemView extends
		EObjectColumnViewerProblemView {

	/**
	 * @param viewer
	 */
	public ExtensibleModelColumnViewerProblemView(ColumnViewer viewer) {
		super(viewer);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.EObjectColumnViewerProblemView#doRefresh(com.hundsun.ares.studio.jres.ui.validate.ProblemPoolChangeEvent)
	 */
	@Override
	protected void doRefresh(ProblemPoolChangeEvent event) {
		// �������ӻ�ɾ��������BasicDiagnostic
		Object[] problems = ArrayUtils.addAll(event.getAddProblems(), event.getRemoveedProblems());
		Set<Object> objects = new HashSet<Object>();
		for (Object problem : problems) {
			EObject eObj = (EObject) ((Diagnostic)problem).getData().get(0);
			objects.add(eObj);
			// �ٶ���չֻ����һ�㣬Ҳ����˵�����ж������ExtensibleModel
			// ��Ҫע����ǣ�����Ķ�����ܱ�ɾ�������������κ�ExtensibleModel����
			while (!(eObj instanceof ExtensibleModel) && eObj != null) {
				eObj = eObj.eContainer();
			}
			if (eObj != null) {
				objects.add(eObj);
			}
			
		}
		RefreshViewerJob.refresh(getViewer(), objects.toArray(), true);
	}
	
}
