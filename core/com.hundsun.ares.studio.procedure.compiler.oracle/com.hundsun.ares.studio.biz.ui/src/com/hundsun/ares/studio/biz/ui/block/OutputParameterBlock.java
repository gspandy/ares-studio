/**
 * Դ�������ƣ�OutputParameterBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.block;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.TreeViewer;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.ui.ColumnViewerHoverToolTip;
import com.hundsun.ares.studio.ui.validate.IProblemPool;

/**
 * �༭��������б��Block
 * @author sundl
 *
 */
public class OutputParameterBlock extends ParameterViewerBlock {

	/**
	 * @param reference
	 * @param editingDomain
	 * @param resource
	 * @param problemPool
	 */
	public OutputParameterBlock(EditingDomain editingDomain, IARESResource resource, IProblemPool problemPool) {
		super(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_PARAMETERS, editingDomain, resource, problemPool);
	}
	

	@Override
	protected void addToolTipSupport(TreeViewer viewer) {
		ColumnViewerHoverToolTip.enableFor(viewer);
	}


}
