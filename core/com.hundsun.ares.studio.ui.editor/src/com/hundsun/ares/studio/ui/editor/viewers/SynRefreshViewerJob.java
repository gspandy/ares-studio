/**
 * Դ�������ƣ�RefreshViewerJob.java
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
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.progress.UIJob;

/**
 * @author yanwj06282
 * 
 */
public class SynRefreshViewerJob extends UIJob {

	private static Set<SynRefreshViewerJob> jobSet = new HashSet<SynRefreshViewerJob>();

	private ColumnViewer viewer;
	private Object[] elements;
	private boolean updateLables;
	private Command cmd;
	private EStructuralFeature feature;

	/**
	 * ָ��ˢ��һ����ͼ��ˢ�¹��̲�һ���Ǽ�ʱ�ģ�����ˢ�±�֤��Ӱ���û�����ͼ�ϵı༭��Ϊ TODO ��ͬ��ˢ�²������������ú�Ӧ��ֻ����һ��
	 * 
	 * @param viewer
	 */
	public static void refresh(ColumnViewer viewer, Object[] elements,
			boolean updateLables, Command cmd, EStructuralFeature feature) {
		SynRefreshViewerJob job = new SynRefreshViewerJob(viewer, elements,
				updateLables, cmd, feature);
		if (!jobSet.contains(job)) {
			jobSet.add(job);
			job.schedule();
			job.addJobChangeListener(new JobChangeAdapter() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.core.runtime.jobs.JobChangeAdapter#done(org.eclipse
				 * .core.runtime.jobs.IJobChangeEvent)
				 */
				@Override
				public void done(IJobChangeEvent event) {
					jobSet.remove(event.getJob());
				}
			});
		}

	}

	public static void refresh(ColumnViewer viewer, Object[] elements,
			Command cmd, EStructuralFeature feature) {
		refresh(viewer, elements, true, cmd, feature);
	}

	public static void refresh(ColumnViewer viewer, Command cmd,
			EStructuralFeature feature) {
		refresh(viewer, null, true, cmd, feature);
	}

	/**
	 * @param name
	 */
	private SynRefreshViewerJob(ColumnViewer viewer, Object[] elements,
			boolean updateLables, Command cmd, EStructuralFeature feature) {
		super("ˢ����ͼ");
		this.viewer = viewer;

		setSystem(true);
		setPriority(SHORT);

		this.cmd = cmd;
		this.feature = feature;
		this.elements = elements;
		this.updateLables = updateLables;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#shouldRun()
	 */
	@Override
	public boolean shouldRun() {
		return !viewer.getControl().isDisposed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#shouldSchedule()
	 */
	@Override
	public boolean shouldSchedule() {
		return !viewer.getControl().isDisposed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		if (viewer != null && !viewer.getControl().isDisposed()) {
			if (viewer.isCellEditorActive() || viewer.isBusy()) {
				this.schedule(getJobDelay());
			} else {
				if (elements == null) {
					viewer.refresh(updateLables);
				} else {
					if (elements.length > 10) {
						viewer.refresh();
					} else {
						viewer.update(elements, null);
						// �����������߲������,��Ҫ�����㶨λ������
						if (cmd instanceof AddCommand && cmd.canExecute()) {
							if (viewer instanceof TableViewer) {
								Table table = ((TableViewer) viewer).getTable();
								boolean status = false;
								for (TableItem item : table.getItems()) {
									if (item.getData() == cmd.getAffectedObjects().toArray()[0]) {
										status = true;
									}
								}
								if (status) {
									editElement(table.getColumnCount(), cmd.getAffectedObjects().toArray()[0]);
								}
							}else if (viewer instanceof TreeViewer) {
								Tree tree = ((TreeViewer) viewer).getTree();
								boolean status = false;
								for (TreeItem item : tree.getItems()) {
									if (item.getData() == cmd.getAffectedObjects().toArray()[0]) {
										status = true;
									}
								}
								if (status) {
									editElement(tree.getColumnCount(),cmd.getAffectedObjects().toArray()[0]);
								}
							}
						}
					}
				}
			}
		}
		return Status.OK_STATUS;
	}

	private void editElement(int count, Object obj) {
		if (feature == null) {
			viewer.editElement(obj, 0);
			return;
		}
		for (int i = 0; i < count; i++) {
			CellLabelProvider pro = viewer.getLabelProvider(i);
			if (pro instanceof EObjectColumnLabelProvider) {
				EStructuralFeature f = ((EObjectColumnLabelProvider) pro).getEStructuralFeature(null);
				if (feature.equals(f)) {
					viewer.editElement(obj, i);
					break;
				}
			}
			if (i == count -1) {
				viewer.editElement(obj, 0);
			}
		}
	}

	protected long getJobDelay() {
		return 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof SynRefreshViewerJob) {
			SynRefreshViewerJob other = (SynRefreshViewerJob) obj;
			if (this.updateLables = other.updateLables
					&& this.viewer == other.viewer
					&& ArrayUtils.isEquals(this.elements, other.elements)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(updateLables).append(viewer)
				.append(elements).toHashCode();
		// return (updateLables ? 1 : -1) * (viewer.hashCode() +
		// ArrayUtils.hashCode(elements));
	}
}
