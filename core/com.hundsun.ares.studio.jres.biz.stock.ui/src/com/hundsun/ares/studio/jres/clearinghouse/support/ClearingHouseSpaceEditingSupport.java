/**
 * 
 */
package com.hundsun.ares.studio.jres.clearinghouse.support;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.jres.clearinghouse.constant.IClearingHouseConstant;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleRefType;
import com.hundsun.ares.studio.jres.database.oracle.internal.ui.providers.OracleEMPropertyDescriptor;
import com.hundsun.ares.studio.jres.database.oracle.ui.OracleExtensibleModelEditingSupport;
import com.hundsun.ares.studio.jres.database.oracle.ui.viewer.OracleSpaceColumnProposalProvider;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.TextEMPropertyDescriptor;

/**
 * @author yanwj06282
 *
 */
public class ClearingHouseSpaceEditingSupport extends OracleExtensibleModelEditingSupport {

	@Override
	public String getName() {
		return "�������չ";
	}

	@Override
	public String getKey() {
		return IClearingHouseConstant.TABLE_SPACE_KEY;
	}

	@Override
	public EObject createMapValueObject() {
		return ChouseFactory.eINSTANCE.createTableSpaceProperty();
	}

	@Override
	public IExtensibleModelPropertyDescriptor[] getPropertyDescriptors(
			IARESElement aresElement, EClass eClass) {
		List<IExtensibleModelPropertyDescriptor> descriptors = new ArrayList<IExtensibleModelPropertyDescriptor>();
		
		OracleSpaceColumnProposalProvider proposalProvider = new OracleSpaceColumnProposalProvider(IOracleRefType.Space);
		TextEMPropertyDescriptor hisSpace = new OracleEMPropertyDescriptor(proposalProvider,ChousePackage.Literals.TABLE_SPACE_PROPERTY__REDU_TABLE);
		hisSpace.setDisplayName("�����ռ�");
		descriptors.add(hisSpace);
		
		TextEMPropertyDescriptor clearTableSpace = new OracleEMPropertyDescriptor(proposalProvider,ChousePackage.Literals.TABLE_SPACE_PROPERTY__CHEAR_TABLE);
		clearTableSpace.setDisplayName("�����ռ�");
		descriptors.add(clearTableSpace);
		
		TextEMPropertyDescriptor clearTableIndexSpace = new OracleEMPropertyDescriptor(proposalProvider,ChousePackage.Literals.TABLE_SPACE_PROPERTY__CHEAR_TABLE_INDEX);
		clearTableIndexSpace.setDisplayName("����������ռ�");
		descriptors.add(clearTableIndexSpace);
		
		return descriptors.toArray(new IExtensibleModelPropertyDescriptor[descriptors.size()]) ;
	}

}
