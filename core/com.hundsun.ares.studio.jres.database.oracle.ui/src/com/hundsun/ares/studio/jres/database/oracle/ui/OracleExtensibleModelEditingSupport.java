/**
 * 
 */
package com.hundsun.ares.studio.jres.database.oracle.ui;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.ui.editor.extend.AbstractExtensibleModelEditingSupport;

/**
 * @author gongyf
 *
 */
public abstract class OracleExtensibleModelEditingSupport extends
		AbstractExtensibleModelEditingSupport {
	
	@Override
	public boolean isEnable(IARESElement aresElement, EClass eClass) {
		// FIXME �жϵ�ǰ�����ݿ�֧��
		return super.isEnable(aresElement, eClass);
	}
}
