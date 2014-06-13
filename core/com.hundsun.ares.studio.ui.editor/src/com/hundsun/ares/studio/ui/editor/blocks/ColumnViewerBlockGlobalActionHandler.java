/**
 * Դ�������ƣ�ColumnViewerBlockGlobalActionHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.ui.editor.blocks;


import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;

import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;

/**
 * Global Action
 * @author sundl
 *
 */
public class ColumnViewerBlockGlobalActionHandler {

	private static Logger logger = Logger.getLogger(ColumnViewerBlockGlobalActionHandler.class);
	
	private IActionBars actionBars;
	private ColumnViewerBlock block;
	
	// ��һ����������� ����cell editor, ����������ı���Ȼ�󽹵�ֱ���л������˿�
	// ����������£����յ�control��deactived�¼���Ȼ�󣬻��ܵ�editor deactived�¼�
	// ������Ҫ��¼control��active״̬����editor deactive�¼��Ĵ����ʱ������ؼ�����actived���Ͳ�set global action
	private boolean controlActive = false;
	
	public ColumnViewerBlockGlobalActionHandler(ColumnViewerBlock block, IActionBars actionBars) {
		this.block = block;
		this.actionBars = actionBars;
		hook();
	}
	
	private void hook() {
		// active��ʱ�򣬼����Ӧ��GlobalAction
		block.getColumnViewer().getControl().addListener(SWT.Activate, new Listener() {
			@Override
			public void handleEvent(Event event) {
				logger.debug("tree/table activated");
				controlActive = true;
				setupGlobalActions();
			}
		});
		
		// deactive��ʱ�����GlobalAction
		block.getColumnViewer().getControl().addListener(SWT.Deactivate, new Listener() {
			@Override
			public void handleEvent(Event event) {
				logger.debug("tree/table Deactivate");
				controlActive = false;
				clearGlobalActions();
			}
		});
		
		// ����cell editor�ļ���״̬�������ʱ�����GLobalAction
		block.getColumnViewer().getColumnViewerEditor().addEditorActivationListener(new ColumnViewerEditorActivationListener() {
			@Override
			public void beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
			}
			@Override
			public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
			}
			@Override
			public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
				logger.debug("Editor deactived...");
				boolean focus = block.getColumnViewer().getControl().isFocusControl();
				logger.debug("control active : " + focus);
				if (controlActive)
					setupGlobalActions();
			}
			@Override
			public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
				logger.debug("Editor actived...");
				if (controlActive)
					clearGlobalActions();
			}
		});
	}
	
	// ���������д������������������Action
	protected void setupGlobalActions() {
		IAction copyAction = block.getActionRegistry().getAction(ActionFactory.COPY.getId());
		IAction pasteAction = block.getActionRegistry().getAction(ActionFactory.PASTE.getId());
		IAction deleteAction = block.getActionRegistry().getAction(IActionIDConstant.CV_DELETE);
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		logger.debug("Global action set to copy/paste");
	}
	
	protected void clearGlobalActions() {
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), null);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), null);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), null);
		logger.debug("Global action set to null");
	}
	
}
