/**
 * Դ�������ƣ�ButtonGroupManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * ��Ҫ�Ա����������Control���в���
 * @author gongyf
 *
 */
public class ButtonGroupManager {
	
	
	private final static String KEY_ACTION = "action";
	
	private ToolBar composite;
	private List<IAction> actions = new ArrayList<IAction>();
	
	/**
	 * ��Action�Ŀ���״̬ͬ������ť��ȥ
	 */
	private IPropertyChangeListener listener = new IPropertyChangeListener(){

		public void propertyChange(PropertyChangeEvent event) {
			if (IAction.ENABLED.equals(event.getProperty()) ) {
				updateEnableStatus();
			} else if (IAction.TEXT.equals(event.getProperty())){
				updateTexts();
			}
			
		}};
	
	public void add(IAction action) {
		actions.add(action);
		action.addPropertyChangeListener(listener);
		update();
	}
	
	public void remove(IAction action) {
		actions.remove(action);
		action.removePropertyChangeListener(listener);
		update();
	}
	
	public void replaceActions(Collection<IAction> actions) {
		for (IAction action : this.actions) {
			action.removePropertyChangeListener(listener);
		}
		this.actions.clear();
		this.actions.addAll(actions);
		for (IAction action : this.actions) {
			action.addPropertyChangeListener(listener);
		}
		update();
	}
	
	public Composite getControl() {
		return composite;
	}
	
	protected ToolItem createButton(IAction action) {
		ToolItem button = new ToolItem(composite, SWT.PUSH );
		
		button.setData(KEY_ACTION, action);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				IAction action = (IAction) e.widget.getData(KEY_ACTION);
				action.run();
			}
		});
		//GridDataFactory.fillDefaults().grab(true, false).applyTo(button);
		return button;
	}
	
	public void createControl(Composite parent) {
		composite = new ToolBar(parent, SWT.VERTICAL);
		update();
		GridLayoutFactory.swtDefaults().applyTo(composite);
		
		
		composite.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				ButtonGroupManager.this.dispose();
			}
		});
	}
	
	protected void updateEnableStatus() {
		if (composite != null) {
			ToolItem[] children = composite.getItems();
			for (ToolItem control : children) {
				IAction action = (IAction) control.getData(KEY_ACTION);
				control.setEnabled(action.isEnabled());
			}
		}
	}
	
	protected void updateTexts() {
		if (composite != null) {
			ToolItem[] children = composite.getItems();
			for (ToolItem control : children) {
				IAction action = (IAction) control.getData(KEY_ACTION);
				Assert.isNotNull(action.getText(), "Action����������Text");
				((ToolItem)control).setText(action.getText());
			}
		}
	}
	
	protected void updateImages() {
		if (composite != null) {
			ToolItem[] children = composite.getItems();
			for (ToolItem control : children) {
				IAction action = (IAction) control.getData(KEY_ACTION);
				action.getImageDescriptor();
				((ToolItem)control).setText(action.getText());
			}
		}
	}
	
	public void update() {
		if (composite != null) {
			ToolBar toolbar = new ToolBar(composite, SWT.VERTICAL);
			ToolItem[] children = composite.getItems();
			// ����ӳ��
			Map<IAction, ToolItem> actionToButtonMap = new HashMap<IAction, ToolItem>();
			for (ToolItem button : children) {
				actionToButtonMap.put((IAction) button.getData(KEY_ACTION),  button);
			}
			
			List<ToolItem> newButtons = new ArrayList<ToolItem>();
			Set<ToolItem> removedButtons = new HashSet<ToolItem>(Arrays.asList(children));
			for (int i = 0; i < actions.size(); i++) {
				IAction action = actions.get(i);
				
				ToolItem button = actionToButtonMap.get(action);
				if (button == null) {
					// ��Ҫ����
					button = createButton(action);
				} else {
					removedButtons.remove(button);
				}
				
				newButtons.add(button);
			}
			
			for (ToolItem button : removedButtons) {
				button.dispose();
			}
			removedButtons.clear();

			actionToButtonMap.clear();
			
			updateEnableStatus();
			updateTexts();
			
			composite.layout();
		}
		

	}
	
	public void dispose() {
		composite = null;
		actions.clear();
		actions = null;
	}
}
