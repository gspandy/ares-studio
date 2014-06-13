/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IParent;

/**
 * ֻ����ѡ��Ԫ�ص�������ҳ��
 * @author sundl
 */
public class CommonElementSelectionPage extends WizardPage {
	
	public static final int CATEGORY = 8888888;
	public static final int UNKNOWN = Integer.MAX_VALUE;
	
	protected Object selection;
	protected IWorkbench workbench;
	protected TreeViewer viewer;

	public CommonElementSelectionPage(String pageName, IWorkbench workbench, IARESElement selection) {
		super(pageName);
		if (selection != null && selection.exists())
			this.selection = selection;
		this.workbench = workbench;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		//GridLayoutFactory.fillDefaults().applyTo(parent);
		
		GridLayoutFactory.fillDefaults().applyTo(composite);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		viewer = new AresModelViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		viewer.getTree().setLayoutData(gd);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelectionProvider().getSelection();
					if (selection.getFirstElement() == null) {
						selection = null;
						return;
					}
					if (selection.getFirstElement() instanceof IARESElement) {
						CommonElementSelectionPage.this.selection = (IARESElement) selection.getFirstElement();
					} else if (selection.getFirstElement() instanceof IResource){
						CommonElementSelectionPage.this.selection = ARESCore.create(((IResource) selection.getFirstElement()).getProject());
					} else {
						CommonElementSelectionPage.this.selection = selection.getFirstElement();
					}
				}
				updateComplete();
			}
		});

		viewer.setInput(ARESCore.getModel());
//		viewer.addFilter(new AresParentViewerFilter());
		addFilter();
		viewer.setComparator(new AresElementComparater());

		if (selection != null) {
			viewer.setSelection(new StructuredSelection(selection), true);
		}

		setControl(composite);
		setPageComplete(false);
		updateComplete();
	}
	
	/**
	 * ������ѡ�ж���
	 * @param target
	 */
	public void select(Object target) {
		viewer.setSelection(new StructuredSelection(target), true);
	}
	
	protected void addFilter(){
		if(viewer != null){
			viewer.addFilter(new ElementTypeFilter());
		}
	}

	protected void updateComplete() {
		if (!validate()) {
			setPageComplete(false);
		} else {
			setPageComplete(true);
		}
	}
	
	/**
	 * ����Ҫ��ʾ������
	 */
	protected int[] getDisplayedElementTypes() {
		return null;
	}
	
	/**
	 * ����Ҫ����ѡ�����Դ����; ��λ���飬��һ�����͵ڶ������֡�<br>
	 * ������ص���null������Ϊ�κ�ѡ�����Ч/�Ϸ�
	 */
	protected String[][] getSelctingElementTypes() {
		return null;
	}

	protected boolean inModule() {
		if (selection instanceof IARESModule) {
			return true;
		}
		return false;
	}

	public IARESElement getSelectedElement() {
		if (this.selection instanceof IARESElement) 
			return ((IARESElement)selection);
		else 
			return null;
	}

	public boolean validate() {		
		boolean selection = validateSelection();
		if (!selection) {
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	// ��鵱ǰ��ѡ��ڵ�
	protected boolean validateSelection() {
		if (selection == null) {
			setErrorMessage("ѡ����Ϊ��");
			return false;
		} else {
			
			String[][] selectingTypes = getSelctingElementTypes();
			if (selectingTypes == null) {
				setErrorMessage(null);
				return true;
			}
			
			int curType = getCurrentSelectionType();
			for (String[] type : selectingTypes) {
				if (type[0].equals(String.valueOf(curType))) {
					setErrorMessage(null);
					return true;
				}
 			}
			
			StringBuffer errorMsg = new StringBuffer();
			errorMsg.append("����ѡ��һ���������͵Ľڵ㣺 ");
			for (String[] type : selectingTypes) {
				errorMsg.append(type[1]);
				errorMsg.append("��");
			}
			errorMsg.deleteCharAt(errorMsg.length() - 1);
			
			setErrorMessage(errorMsg.toString());
			return false;
		}
	}
	
	protected int getCurrentSelectionType() {
		if (selection instanceof IARESElement) {
			return ((IARESElement)selection).getElementType();
		} else if (selection instanceof ARESResourceCategory) {
			return CATEGORY;
		} else 
			return UNKNOWN;
	}

	// ��Դ���͹�����
	class ElementTypeFilter extends ViewerFilter {
		
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return filte(viewer, parentElement, element);
		}
		
	}
	
	
	/**
	 * ����Ԫ��
	 * @param viewer
	 * @param parentElement
	 * @param element
	 * @return
	 */
	protected boolean filte(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IARESElement) {
			IARESElement ares = (IARESElement)element;
			int type = ares.getElementType();
			int[] allowedTypes = getDisplayedElementTypes();
			if (allowedTypes != null) {
				for (int allowedType : allowedTypes) {
					if (allowedType == type) {
						return true;
					}
				}
				return false;
			} else 
				return true;
		}
		return true;
	}
	
}

class AresParentViewerFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (!(element instanceof IParent)) {
			return false;
		}
		return true;
	}

}
