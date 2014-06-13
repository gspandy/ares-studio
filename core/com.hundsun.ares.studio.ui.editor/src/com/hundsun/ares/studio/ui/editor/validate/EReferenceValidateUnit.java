/**
 * Դ�������ƣ�EReferenceValidateUnit.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.validate;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.IValidateUnit;
import com.hundsun.ares.studio.ui.validate.KeyParameter;

public class EReferenceValidateUnit implements IValidateUnit {

	private EObject owner;
	private EReference reference;
	
	
	/**
	 * @param owner
	 * @param reference
	 */
	public EReferenceValidateUnit(EObject owner, EReference reference) {
		super();
		this.owner = owner;
		this.reference = reference;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(EObject owner) {
		this.owner = owner;
	}
	
	/**
	 * @param reference the reference to set
	 */
	public void setReference(EReference reference) {
		this.reference = reference;
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IValidateUnit#updateProblemPool(com.hundsun.ares.studio.jres.ui.validate.IProblemPool, java.util.Map)
	 */
	@Override
	public void updateProblemPool(IProblemPool pool,
			Map<Object, Object> context) throws Exception {
		
		BasicDiagnostic basicDiagnostic =
			new BasicDiagnostic
				(Diagnostic.ERROR,
				 ARESEditorPlugin.PLUGIN_ID,
				 0,
				 "�������",
				 new Object [] { owner });
		
		List<EObject> list;
		if (reference.isMany()) {
			list = (List<EObject>)owner.eGet(reference);
		} else {
			list = Collections.singletonList((EObject)owner.eGet(reference));
		}
		
		// �����������
		for (EObject content : list) {
			Diagnostician.INSTANCE.validate(content, basicDiagnostic, context);
		}
		
		pool.beginChange();
		for (EObject content : list) {
			for (Iterator<EObject> iterator = EcoreUtil.getAllContents(content, true); iterator.hasNext(); ) {
				EObject eObject = iterator.next();
				pool.clear(new KeyParameter(eObject));
			}
			
			pool.clear(new KeyParameter(content));
		}
		// ��������ռ�
		for (Diagnostic child : basicDiagnostic.getChildren()) {
			pool.addProblem(child);
		}
		pool.endChange();
	}
	
}