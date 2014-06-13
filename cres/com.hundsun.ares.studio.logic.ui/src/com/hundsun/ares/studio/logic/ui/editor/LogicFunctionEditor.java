package com.hundsun.ares.studio.logic.ui.editor;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.ui.PartInitException;

import com.hundsun.ares.studio.atom.AtomPackage;
import com.hundsun.ares.studio.biz.ui.editor.page.InterfacePage;
import com.hundsun.ares.studio.logic.LogicFunction;
import com.hundsun.ares.studio.logic.LogicPackage;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicSkeletonFactoryConstant;
import com.hundsun.ares.studio.logic.ui.pages.LogicFunctionInterfacePage;
import com.hundsun.ares.studio.logic.ui.pages.LogicPreViewPage;
import com.hundsun.ares.studio.logic.ui.pages.LogicTextEditor;
import com.hundsun.ares.studio.logic.ui.pages.PreviewUpdater;
import com.hundsun.ares.studio.ui.CommonElementContentProvider;
import com.hundsun.ares.studio.ui.CommonElementLabelProvider;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;
import com.hundsun.ares.studio.ui.editor.blocks.RevisionHistoryListPage;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;
import com.hundsun.ares.studio.ui.editor.textbase.ITextBased;
import com.hundsun.ares.studio.ui.editor.textbase.TextBasedEditorInput;

public class LogicFunctionEditor  extends EMFFormEditor {
	
	private InterfacePage interfacePage;
	private LogicTextEditor sourcePage;
	private LogicPreViewPage preViewPage;
	private int index;

	static CommonElementContentProvider cp = new CommonElementContentProvider();
	static CommonElementLabelProvider provider = new CommonElementLabelProvider(cp);
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.form.EMFFormEditor#getInfoClass()
	 */
	@Override
	protected EClass getInfoClass() {
		return LogicPackage.Literals.LOGIC_FUNCTION;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		interfacePage = new LogicFunctionInterfacePage(null, this, "interface", "�ӿ�");
		try {
//			addPage(new LogicFunctionBaseInfoPage(this, "baseinfo", "������Ϣ"));
			addPage(interfacePage);
//			addPage(new LogicServiceIntenalVarPage(this, "internalvar", "�ڲ�����"));
			
			// α����ҳ��
			final LogicFunction func = (LogicFunction) getInfo();
			sourcePage = new LogicTextEditor(getARESResource());
			TextBasedEditorInput input = new TextBasedEditorInput(new ITextBased() {
				@Override
				public void setText(String text) {
					Command command =  SetCommand.create(getEditingDomain(), func, AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE, text);
					getEditingDomain().getCommandStack().execute(command);
				}
				@Override
				public String getText() {
					return func.getPseudoCode();
				}
			}, getARESResource());
			index = addPage(sourcePage, input);
			sourcePage.setEditor(this);
			setPageText(index, "ʵ��-α����");
			
			//Ԥ��ҳ��
			addPage(preViewPage=new LogicPreViewPage(this, "preview", "Ԥ��"), new TextEditorInput());
			addPage(new RevisionHistoryListPage(this, "revisionHistory", "�޶���Ϣ"));
			
			addPageChangedListener(new PreviewUpdater(ILogicSkeletonFactoryConstant.SKELETONTYPE_LOGIC_FUNCTION));
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void handleBeforeSave() {
		sourcePage.doSave(new NullProgressMonitor());
		setPartName(getEditorTitle());
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
		if(newPageIndex!=2){//����Ԥ���༭�༭��λ����Ϣ
			preViewPage.saveLocation();
		}
		super.pageChange(newPageIndex);
		if (newPageIndex==2){//�ָ�Ԥ���༭�༭��λ��
			preViewPage.restoreLocation();
		}
	}
}
