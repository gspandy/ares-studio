package com.hundsun.ares.studio.biz.ui.block;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.CellEditor;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.ui.editor.blocks.DisplayItem;
import com.hundsun.ares.studio.ui.editor.editingsupport.IEditingSupportDecorator;

/**
 * ֻ�в�ѡ���׼�ֶε�ʱ����ܱ༭������
 * @author gongyf
 *
 */
public class ParameterDefineEditingSupportDecorator implements IEditingSupportDecorator {

	private IARESProject project;
	private EAttribute attribute;
	
	/**
	 * @param attribute
	 */
	public ParameterDefineEditingSupportDecorator(IARESProject project, EAttribute attribute) {
		super();
		this.project = project;
		this.attribute = attribute;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.IEditingSupportDecorator#decorateGetCellEditor(org.eclipse.jface.viewers.CellEditor, java.lang.Object)
	 */
	@Override
	public CellEditor decorateGetCellEditor(CellEditor cellEditor,	Object element) {
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editingsupports.IEditingSupportDecorator#decorateCanEdit(boolean, java.lang.Object)
	 */
	@Override
	public boolean decorateCanEdit(boolean canEdit, Object element) {
		if (element instanceof DisplayItem) {
			return false;
		}
		
		Parameter p = (Parameter) element;
		
		if (p.eContainer() == null) {
			return false;
		}

		switch (p.getParamType()) {
		case STD_FIELD:
			if (BizPackage.Literals.PARAMETER__NAME.equals(attribute) 
					|| BizPackage.Literals.PARAMETER__REAL_TYPE.equals(attribute)
					|| BizPackage.Literals.PARAMETER__TYPE.equals(attribute)
					|| BizPackage.Literals.PARAMETER__DESCRIPTION.equals(attribute)) {
				return false;
			}
			break;
		case NON_STD_FIELD:
			if (BizPackage.Literals.PARAMETER__REAL_TYPE.equals(attribute)) {
				return false;
			}
			break;
		case OBJECT:
			// ����ж����׼�ֶΣ���ֻ�༭���������ö����׼�ֶΣ� ���û�У������ֺ����Ͷ����Ա༭
			boolean hasStdObjectList = BizUtil.hasStdObjList(project);
			if (hasStdObjectList && BizPackage.Literals.PARAMETER__TYPE.equals(attribute)) {
				return false;
			}
			if (BizPackage.Literals.PARAMETER__NAME.equals(attribute) 
					|| BizPackage.Literals.PARAMETER__REAL_TYPE.equals(attribute)
					|| BizPackage.Literals.PARAMETER__DESCRIPTION.equals(attribute)
					|| BizPackage.Literals.PARAMETER__DEFAULT_VALUE.equals(attribute)) {
				return false;
			}
			break;
		case PARAM_GROUP:
			// �����飬ֻ�༭�����������������������ɱ༭
			if (BizPackage.Literals.PARAMETER__NAME.equals(attribute)
					|| BizPackage.Literals.PARAMETER__ID.equals(attribute)
					|| BizPackage.Literals.PARAMETER__REAL_TYPE.equals(attribute)
					|| BizPackage.Literals.PARAMETER__DESCRIPTION.equals(attribute)
					|| BizPackage.Literals.PARAMETER__DEFAULT_VALUE.equals(attribute)) {
				return false;
			}
			break;
		case COMPONENT:
			// ���
			if (BizPackage.Literals.PARAMETER__NAME.equals(attribute) 
					|| BizPackage.Literals.PARAMETER__REAL_TYPE.equals(attribute)
					|| BizPackage.Literals.PARAMETER__DESCRIPTION.equals(attribute)
					|| BizPackage.Literals.PARAMETER__DEFAULT_VALUE.equals(attribute)) {
				return false;
			}
			break;
		}
		return canEdit;
	}

}
