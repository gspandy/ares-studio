/**
 * Դ�������ƣ�DictionaryColumnViewerProblemView.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ���ݱ༭�����
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.ui.editor.viewers.RefreshViewerJob;
import com.hundsun.ares.studio.ui.validate.ProblemPoolChangeEvent;

/**
 * @author wangxh
 *
 */
public class DictionaryColumnViewerProblemView extends MetadataColumnViewerProblemView {

	ColumnViewer parentViewer;
	
	public DictionaryColumnViewerProblemView(ColumnViewer viewer,ColumnViewer parentViewer) {
		super(viewer);
		this.parentViewer=parentViewer;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.viewer.MetadataColumnViewerProblemView#doRefresh(com.hundsun.ares.studio.jres.ui.validate.ProblemPoolChangeEvent)
	 */
	@Override
	protected void doRefresh(ProblemPoolChangeEvent event) {
		// �������ӻ�ɾ��������BasicDiagnostic
		Object[] problems = ArrayUtils.addAll(event.getAddProblems(), event.getRemoveedProblems());
		Set<Object> objects = new HashSet<Object>();
		for (Object problem : problems) {
			EObject eObj = (EObject) ((Diagnostic)problem).getData().get(0);
			objects.add( eObj );
			
		}
		
		RefreshViewerJob.refresh(getViewer(), objects.toArray(), true);
		
		// ��ȡ���ڵ�
		objects = getAllCategoriesAndSelf(objects, parentViewer);
		RefreshViewerJob.refresh(parentViewer, objects.toArray(), true);
	}

}
