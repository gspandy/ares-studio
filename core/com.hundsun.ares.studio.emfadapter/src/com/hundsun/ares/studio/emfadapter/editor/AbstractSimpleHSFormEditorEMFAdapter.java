package com.hundsun.ares.studio.emfadapter.editor;

import org.eclipse.emf.ecore.EObject;

/**
 * ����sourceҳ��EMF�༭����
 * 
 * @author mawb
 *
 * @param <T>
 */
public abstract class AbstractSimpleHSFormEditorEMFAdapter<T extends EObject> extends AbstractHSFormEditorEMFAdapter<T> {

	@Override
	protected void createSourcePage() {
		// do nothing
	}
	
}
