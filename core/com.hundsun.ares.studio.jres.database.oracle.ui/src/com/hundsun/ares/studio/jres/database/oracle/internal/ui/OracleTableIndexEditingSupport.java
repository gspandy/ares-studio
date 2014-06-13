/**
 * Դ�������ƣ�OracleTableIndexEditingSupport.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleConstant;
import com.hundsun.ares.studio.jres.database.oracle.ui.OracleExtensibleModelEditingSupport;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleFactory;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePackage;
import com.hundsun.ares.studio.ui.editor.extend.BooleanEMPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;

/**
 * @author gongyf
 *
 */
public class OracleTableIndexEditingSupport extends OracleExtensibleModelEditingSupport  {

	/**
	 * 
	 */
	public OracleTableIndexEditingSupport() {
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelEditingSupport#getName()
	 */
	@Override
	public String getName() {
		return "Oracle";
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelEditingSupport#getKey()
	 */
	@Override
	public String getKey() {
		return IOracleConstant.INDEX_DATA2_KEY;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelEditingSupport#createMapValueObject()
	 */
	@Override
	public EObject createMapValueObject() {
		return OracleFactory.eINSTANCE.createOracleIndexProperty();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.pages.extend.IExtensibleModelEditingSupport#getPropertyDescriptors()
	 */
	@Override
	public IExtensibleModelPropertyDescriptor[] getPropertyDescriptors(IARESElement aresElement, EClass eClass) {
		List<IExtensibleModelPropertyDescriptor> descriptors = new ArrayList<IExtensibleModelPropertyDescriptor>();
		BooleanEMPropertyDescriptor db = new BooleanEMPropertyDescriptor(OraclePackage.Literals.ORACLE_INDEX_PROPERTY__REVERSE);
		db.setDisplayName("��ת");
		descriptors.add(db);
		
		return descriptors.toArray(new IExtensibleModelPropertyDescriptor[descriptors.size()]) ;
	}

}
