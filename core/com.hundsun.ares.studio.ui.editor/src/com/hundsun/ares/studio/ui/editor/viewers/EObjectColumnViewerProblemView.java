/**
 * Դ�������ƣ�EObjectColumnViewerProblemView.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.viewers;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ColumnViewer;

import com.hundsun.ares.studio.ui.editor.IDiagnosticProvider;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.IProblemView;
import com.hundsun.ares.studio.ui.validate.KeyParameter;
import com.hundsun.ares.studio.ui.validate.ProblemPoolChangeEvent;

/**
 * @author gongyf
 *
 */
public class EObjectColumnViewerProblemView implements IProblemView, IDiagnosticProvider {

	ColumnViewer viewer;
	IProblemPool pool;
	/**
	 * @param viewer
	 */
	public EObjectColumnViewerProblemView(ColumnViewer viewer) {
		super();
		this.viewer = viewer;
	}

	/**
	 * @return the viewer
	 */
	protected ColumnViewer getViewer() {
		return viewer;
	}
	
	/**
	 * @return the pool
	 */
	protected IProblemPool getPool() {
		return pool;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemView#refresh(com.hundsun.ares.studio.jres.ui.validate.IProblemPool, java.util.Map)
	 */
	@Override
	public final void refresh(ProblemPoolChangeEvent event) {
		this.pool = event.getSource();
		doRefresh(event);
	}
	
	protected void doRefresh(ProblemPoolChangeEvent event) {
		// �������ӻ�ɾ��������BasicDiagnostic
		Object[] problems = ArrayUtils.addAll(event.getAddProblems(), event.getRemoveedProblems());
		Set<Object> objects = new HashSet<Object>();
		for (Object problem : problems) {
			objects.add( ((Diagnostic)problem).getData().get(0) );
		}
		RefreshViewerJob.refresh(getViewer(), objects.toArray(), true);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.IDiagnosticProvider#getDiagnostic(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public Diagnostic getDiagnostic(EObject object, EStructuralFeature feature, Object... objects ) {
		if (getPool() != null) {
			Object[] problems = getPool().getProblem(new KeyParameter(ArrayUtils.addAll(new Object[]{object, feature}, objects)));
			if (problems != null && problems.length > 0) {
				return (Diagnostic) problems[0];
			}
		}

		return null;
	}
	
	
}
