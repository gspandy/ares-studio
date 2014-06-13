/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.page;

import java.util.List;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.hundsun.ares.studio.ui.grid.EditorComponent;
import com.hundsun.ares.studio.ui.util.FormLayoutFactory;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;

/**
 * section���� ����״̬����չҳ��
 * @author maxh
 *
 */
public abstract class ExtendSectionScrolledFormPage<T> extends
ExtendPageWithMyDirtySystem<T> {

	public ExtendSectionScrolledFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	protected ScrolledForm scrolledForm;
	protected FormToolkit formToolkit;
	protected Composite formComposite;
	protected IManagedForm managedForm;
	
	protected Section createSectionWithTitle(Composite parent, FormToolkit toolkit, String title, boolean expanded) {
		return createSectionWithTitle(parent, toolkit, title, expanded,new TableWrapData(TableWrapData.FILL_GRAB));
	}
	
	protected Section createSectionWithTitle(Composite parent, FormToolkit toolkit, String title, boolean expanded,Object sectionLayoutData) {
		int style = /* Section.DESCRIPTION | */ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE;
		if (expanded)
			style = style | ExpandableComposite.EXPANDED;

		Section section = toolkit.createSection(parent, style);
		section.setText(title);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				scrolledForm.reflow(true);
			}
		});
		section.setLayoutData(sectionLayoutData);
		return section;
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		// �������崹ֱ��ҳ��
		this.managedForm = managedForm;
		scrolledForm = managedForm.getForm();
		formToolkit = managedForm.getToolkit();
		formComposite = scrolledForm.getBody();

		scrolledForm.setText(getTitle());
		formComposite.setLayout(getFormLayout());
		
		//����ʵ�ִ���sections����
		createSections(managedForm);
		
		updateEditableState();
	}
	
	protected abstract void createSections(IManagedForm managedForm);
	
	public ImporveControlWithDitryStateContext createImporveControlWithDitryStateContext(Composite parent, final IManagedForm managedForm){
		FormToolkit toolkit = managedForm.getToolkit();
		return new ImporveControlWithDitryStateContext(parent,dirty,toolkit,getUndoContext(),managedForm.getMessageManager(),info);
	}
	/**
	 * �������д�÷����޸�ҳ��Ĳ���
	 * @return
	 */
	protected Layout getFormLayout(){
		return FormLayoutFactory.createClearTableWrapLayout(false, 1);
	}
	
	/**
	 * �����ؼ�����Component
	 * ����ΪGridLayout(4, false)
	 * @param title
	 * @param expanded
	 * @return
	 */
	public Section createCompositeSection(String title,boolean expanded){
		return createCompositeSection(title,expanded,new GridLayout(4, false));
	}
	
	/**
	 * �����ؼ�����Component�����û��Լ�ָ������
	 * @param title
	 * @param expanded
	 * @return
	 */
	public Section createCompositeSection(String title,boolean expanded,Layout layout){
		Section section = createSectionWithTitle(formComposite, formToolkit, title, expanded);
		Composite composite = formToolkit.createComposite(section);
		section.setClient(composite);
		composite.setLayout(layout);
		return section;
	}
	
	/**
	 * ����Table��Tree�͵�Component
	 * @param title
	 * @param expanded
	 * @param component
	 * @param input
	 * @return
	 */
	public Section  createEditorComponentSection(String title,boolean expanded,EditorComponent component,List input){
		Section section = createSectionWithTitle(formComposite, formToolkit, title, expanded);
		Composite client = createEditorComponentComposite(component,input,section,managedForm);
		section.setClient(client);
		return section;
	}
}
