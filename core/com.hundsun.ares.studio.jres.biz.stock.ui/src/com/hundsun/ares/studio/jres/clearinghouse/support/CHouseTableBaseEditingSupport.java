/**
 * 
 */
package com.hundsun.ares.studio.jres.clearinghouse.support;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.clearinghouse.constant.IClearingHouseConstant;
import com.hundsun.ares.studio.jres.clearinghouse.provider.ExtendsLabelProvider;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.database.oracle.ui.OracleExtensibleModelEditingSupport;
import com.hundsun.ares.studio.jres.database.ui.support.TableFieldContentProposalHelper;
import com.hundsun.ares.studio.jres.database.ui.support.TableFieldContentProposalProvider;
import com.hundsun.ares.studio.jres.database.ui.support.TableFieldEMPropertyDescriptor;
import com.hundsun.ares.studio.jres.model.chouse.ChouseFactory;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.ui.editor.extend.BooleanEMPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.DerivedEMPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.TextEMPropertyDescriptor;

/**
 * @author yanwj06282
 *
 */
public class CHouseTableBaseEditingSupport extends OracleExtensibleModelEditingSupport {

	@Override
	public String getName() {
		return "����Ϣ��չ";
	}

	@Override
	public String getKey() {
		return IClearingHouseConstant.TABLE_BASE_KEY;
	}

	@Override
	public EObject createMapValueObject() {
		return ChouseFactory.eINSTANCE.createTableBaseProperty();
	}

	@Override
	public IExtensibleModelPropertyDescriptor[] getPropertyDescriptors(
			IARESElement aresElement, EClass eClass) {
		List<IExtensibleModelPropertyDescriptor> descriptors = new ArrayList<IExtensibleModelPropertyDescriptor>();
		{
			TextEMPropertyDescriptor objIDDesc = new TextEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__OBJECT_ID);
			objIDDesc.setCategory(IClearingHouseConstant.CAT_STOCK_EXTEND);
			objIDDesc.setDisplayName("�����");
			descriptors.add(objIDDesc);
		}
		
		{
			TableFieldContentProposalHelper helper = new TableFieldContentProposalHelper();
			TableFieldContentProposalProvider provider = new TableFieldContentProposalProvider(helper, IDatabaseRefType.TableField, aresElement.getARESProject());
			TextEMPropertyDescriptor splitFieldDesc = new TableFieldEMPropertyDescriptor(provider, ChousePackage.Literals.TABLE_BASE_PROPERTY__SPLIT_FIELD);
			splitFieldDesc.setCategory(IClearingHouseConstant.CAT_SPLIT_TABLE);
			splitFieldDesc.setDisplayName("������ֶ�");
			descriptors.add(splitFieldDesc);
			
			TextEMPropertyDescriptor splitNumDesc = new TextEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__SPLIT_NUM);
			splitNumDesc.setCategory(IClearingHouseConstant.CAT_SPLIT_TABLE);
			splitNumDesc.setDisplayName("��������");
			descriptors.add(splitNumDesc);
			TextEMPropertyDescriptor startDataDesc = new TextEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__START_DATA);
			startDataDesc.setCategory(IClearingHouseConstant.CAT_SPLIT_TABLE);
			startDataDesc.setDisplayName("������ʼ����");
			descriptors.add(startDataDesc);
			BooleanEMPropertyDescriptor user = new BooleanEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__USER_SPLIT);
			user.setCategory(IClearingHouseConstant.CAT_SPLIT_TABLE);
			user.setDisplayName("�Ƿ��Զ�������");
			descriptors.add(user);
		}
		
		{
			BooleanEMPropertyDescriptor history = new BooleanEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__HISTORY);
			history.setCategory(IClearingHouseConstant.CAT_HISTORY_TABLE);
			history.setDisplayName("������ʷ��");
			descriptors.add(history);
			
			ExtendsLabelProvider extendProvider = new ExtendsLabelProvider(ARESElementUtil.getARESBundle(aresElement),IClearingHouseConstant.TABLE_SPACE_RELATION_KEY,ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__HIS_SPACE);
			if (aresElement instanceof IARESResource) {
				try {
					extendProvider.setExtensibleModel(((IARESResource) aresElement).getInfo(ExtensibleModel.class));
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			DerivedEMPropertyDescriptor hisSpace = 
				new DerivedEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__HISTORY_SPACE, extendProvider);
			hisSpace.setCategory(IClearingHouseConstant.CAT_HISTORY_TABLE);
			hisSpace.setDisplayName("��ʷ��ռ�");
			descriptors.add(hisSpace);
			
			
			extendProvider = new ExtendsLabelProvider(ARESElementUtil.getARESBundle(aresElement),IClearingHouseConstant.TABLE_SPACE_RELATION_KEY,ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__HIS_INDEX_SPACE);
			if (aresElement instanceof IARESResource) {
				try {
					extendProvider.setExtensibleModel(((IARESResource) aresElement).getInfo(ExtensibleModel.class));
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			DerivedEMPropertyDescriptor hisIndexSpace = 
				new DerivedEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__HISTORY_INDEX_SPACE, extendProvider);
			hisIndexSpace.setCategory(IClearingHouseConstant.CAT_HISTORY_TABLE);
			hisIndexSpace.setDisplayName("��ʷ������ռ�");
			descriptors.add(hisIndexSpace);
			
			extendProvider = new ExtendsLabelProvider(ARESElementUtil.getARESBundle(aresElement),IClearingHouseConstant.TABLE_SPACE_RELATION_KEY,ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__FILE_SPACE);
			if (aresElement instanceof IARESResource) {
				try {
					extendProvider.setExtensibleModel(((IARESResource) aresElement).getInfo(ExtensibleModel.class));
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			DerivedEMPropertyDescriptor fileSpace = 
				new DerivedEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__FILE_SPACE, extendProvider);
			fileSpace.setCategory(IClearingHouseConstant.CAT_HISTORY_TABLE);
			fileSpace.setDisplayName("�鵵��ռ�");
			descriptors.add(fileSpace);
			
			extendProvider = new ExtendsLabelProvider(ARESElementUtil.getARESBundle(aresElement),IClearingHouseConstant.TABLE_SPACE_RELATION_KEY,ChousePackage.Literals.TABLE_SPACE_RELATION_PROPERTY__FILE_INDEX_SPACE);
			if (aresElement instanceof IARESResource) {
				try {
					extendProvider.setExtensibleModel(((IARESResource) aresElement).getInfo(ExtensibleModel.class));
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			DerivedEMPropertyDescriptor fileIndexSpace = 
				new DerivedEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__FILE_INDEX_SPACE, extendProvider);
			fileIndexSpace.setCategory(IClearingHouseConstant.CAT_HISTORY_TABLE);
			fileIndexSpace.setDisplayName("�鵵������ռ�");
			descriptors.add(fileIndexSpace);
		}
		
		{
			BooleanEMPropertyDescriptor isRedu = new BooleanEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__IS_REDU);
			isRedu.setCategory(IClearingHouseConstant.CAT_REDU_TABLE);
			isRedu.setDisplayName("���������");
			descriptors.add(isRedu);
			
			ExtendsLabelProvider extendProvider = new ExtendsLabelProvider(ARESElementUtil.getARESBundle(aresElement),IClearingHouseConstant.TABLE_SPACE_KEY,ChousePackage.Literals.TABLE_SPACE_PROPERTY__REDU_TABLE);
			if (aresElement instanceof IARESResource) {
				try {
					extendProvider.setExtensibleModel(((IARESResource) aresElement).getInfo(ExtensibleModel.class));
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			DerivedEMPropertyDescriptor redu = 
				new DerivedEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__REDU, extendProvider);
			redu.setCategory(IClearingHouseConstant.CAT_REDU_TABLE);
			redu.setDisplayName("�����ռ�");
			descriptors.add(redu);
		}
		
		{
			BooleanEMPropertyDescriptor isClear = new BooleanEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__IS_CLEAR);
			isClear.setCategory(IClearingHouseConstant.CAT_CLEAR_TABLE);
			isClear.setDisplayName("���������");
			descriptors.add(isClear);
			
			ExtendsLabelProvider extendProvider = new ExtendsLabelProvider(ARESElementUtil.getARESBundle(aresElement),IClearingHouseConstant.TABLE_SPACE_KEY,ChousePackage.Literals.TABLE_SPACE_PROPERTY__CHEAR_TABLE);
			if (aresElement instanceof IARESResource) {
				try {
					extendProvider.setExtensibleModel(((IARESResource) aresElement).getInfo(ExtensibleModel.class));
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			DerivedEMPropertyDescriptor clear = 
				new DerivedEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__CHEAR, extendProvider);
			clear.setCategory(IClearingHouseConstant.CAT_CLEAR_TABLE);
			clear.setDisplayName("�����ռ�");
			descriptors.add(clear);
			
			extendProvider = new ExtendsLabelProvider(ARESElementUtil.getARESBundle(aresElement),IClearingHouseConstant.TABLE_SPACE_KEY,ChousePackage.Literals.TABLE_SPACE_PROPERTY__CHEAR_TABLE_INDEX);
			if (aresElement instanceof IARESResource) {
				try {
					extendProvider.setExtensibleModel(((IARESResource) aresElement).getInfo(ExtensibleModel.class));
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			DerivedEMPropertyDescriptor clearIndex = 
				new DerivedEMPropertyDescriptor(ChousePackage.Literals.TABLE_BASE_PROPERTY__CLEAR_INDEX_SPACE, extendProvider);
			clearIndex.setCategory(IClearingHouseConstant.CAT_CLEAR_TABLE);
			clearIndex.setDisplayName("����������ռ�");
			descriptors.add(clearIndex);
		}
		return descriptors.toArray(new IExtensibleModelPropertyDescriptor[descriptors.size()]) ;
	}

}
