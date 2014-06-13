package com.hundsun.ares.studio.atom.ui.editor;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.ui.PartInitException;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomPackage;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomSkeletonFactoryConstant;
import com.hundsun.ares.studio.atom.ui.editor.page.AtomPreViewPage;
import com.hundsun.ares.studio.atom.ui.editor.page.AtomServiceInterfacePage;
import com.hundsun.ares.studio.atom.ui.editor.page.PreviewUpdater;
import com.hundsun.ares.studio.biz.ui.editor.page.InterfacePage;
import com.hundsun.ares.studio.ui.CommonElementContentProvider;
import com.hundsun.ares.studio.ui.CommonElementLabelProvider;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;
import com.hundsun.ares.studio.ui.editor.textbase.ITextBased;
import com.hundsun.ares.studio.ui.editor.textbase.TextBasedEditorInput;

public class AtomServiceEditor  extends EMFFormEditor {

	private InterfacePage interfacePage;
	private AtomTextEditor sourcePage;
	private AtomPreViewPage atomPreViewPage;//Ԥ��Page
	private int index;
	
	static CommonElementContentProvider cp = new CommonElementContentProvider();
	static CommonElementLabelProvider provider = new CommonElementLabelProvider(cp);
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#getInfoClass()
	 */
	@Override
	protected EClass getInfoClass() {
		return AtomPackage.Literals.ATOM_SERVICE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		interfacePage = new AtomServiceInterfacePage(null, this, "interface", "�ӿ�");
		try {
//			addPage(new AtomServiceBaseInfoPage(this, "baseinfo", "������Ϣ"));
			addPage(interfacePage);
//			addPage(new AtomFunctionIntenalVarPage(this, "internalvar", "�ڲ�����"));
			
			// α����ҳ��
			final AtomService atomservice = (AtomService) getInfo();
			sourcePage = new AtomTextEditor(getARESResource());
			TextBasedEditorInput input = new TextBasedEditorInput(new ITextBased() {
				@Override
				public void setText(String text) {
					Command command =  SetCommand.create(getEditingDomain(), atomservice, AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE, text);
					getEditingDomain().getCommandStack().execute(command);
				}
				@Override
				public String getText() {
					return atomservice.getPseudoCode();
				}
			}, getARESResource());
			index = addPage(sourcePage, input);
			sourcePage.setEditor(this);
			setPageText(index, "ʵ��-α����");
			
			//Ԥ��ҳ��
			addPage(atomPreViewPage=new AtomPreViewPage(this, "preview", "Ԥ��"), new TextEditorInput());
			addPage(new RevisionHistoryListPage(this, "revisionHistory", "�޶���Ϣ"));
			
			addPageChangedListener(new PreviewUpdater(IAtomSkeletonFactoryConstant.SKELETONTYPE_ATOM_SERVICE));
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void handleBeforeSave() {
		sourcePage.doSave(new NullProgressMonitor());
	}

	@Override
	protected void handleAfterSave() {
		setPartName(getEditorTitle());
	}
	
	
	
	@Override
	protected String getEditorTitle() {
		String partName = provider.getText(getARESResource());
		if (isReadOnly()) {
			partName += "(ֻ��)";
		}
		return partName;
	}
	
	@Override
	public boolean isDirty() {
		if (sourcePage.isDirty()) {
			return true;
		}
		return super.isDirty();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#pageChange(int)
	 */
	@Override
	protected void pageChange(int newPageIndex) {
		if(3 == getCurrentPage()){
			String txt = sourcePage.getMySourceViewer().getTextWidget().getText();
			
			AtomFunction function = (AtomFunction)getInfo();
			if (sourcePage.isDirty() && !StringUtils.equals(function.getPseudoCode(),txt)){
				Command command =  SetCommand.create(getEditingDomain(), function, AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE, txt);
				getEditingDomain().getCommandStack().execute(command);
			}
		}
		if(newPageIndex!=2){//����Ԥ���༭�༭��λ����Ϣ
			atomPreViewPage.saveLocation();
		}
		super.pageChange(newPageIndex);
		if (newPageIndex==2){//�ָ�Ԥ���༭�༭��λ��
			atomPreViewPage.restoreLocation();
		}
	}

}
