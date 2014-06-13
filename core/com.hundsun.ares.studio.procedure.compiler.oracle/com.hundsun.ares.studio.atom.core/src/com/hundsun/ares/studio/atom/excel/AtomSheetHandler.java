package com.hundsun.ares.studio.atom.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.atom.AtomPackage;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.excel.handlers.InterfaceSheetHandler;
import com.hundsun.ares.studio.core.excel.Module;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.NullPropertyHandler;

public abstract class AtomSheetHandler  extends InterfaceSheetHandler{

	public static Map<String, IPropertyHandler> ATOM_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();

	/** �������Դ����Ӧ�б� */
	public static Map<String, IPropertyHandler> VAR_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	public static Map<String, IPropertyHandler> TEXT_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	static {
		ATOM_PROPERTY_HANDLERS.putAll(INTERFACE_PROPERTY_HANDLERS);
		ATOM_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__DATABASE));
		ATOM_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__DATABASE));//���ڲ�Ʒ����ϵͳ06��۰��и�ʽ��
		
		// �ڲ�����
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__FLAGS));
		VAR_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		VAR_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		VAR_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		VAR_PROPERTY_HANDLERS.put("˵��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__NAME));
		VAR_PROPERTY_HANDLERS.put("ȱʡֵ", NullPropertyHandler.INSTANCE);
		VAR_PROPERTY_HANDLERS.put("����", NullPropertyHandler.INSTANCE);
		
		TEXT_PROPERTY_HANDLERS.put("ҵ��������", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE));
		TEXT_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
		TEXT_PROPERTY_HANDLERS.put("������ʾ", NullPropertyHandler.INSTANCE);//���ڲ�Ʒ����ϵͳ06��۰����д���Ϣ����ȥ����
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.AbstractSheetHandler#startSheet(org.apache.poi.hssf.usermodel.HSSFSheet)
	 */
	@Override
	public void startSheet(HSSFSheet sheet) {
		super.startSheet(sheet);
		module = new Module();
		//���ķָ�����Ҳ֧��
		String sheetName = StringUtils.replace(sheet.getSheetName(), "��", "-");
		String cName = StringUtils.removeStart(sheetName, "ԭ�Ӻ���-");
		cName = StringUtils.removeStart(cName, "ԭ�ӷ���-");
		module.cName = cName;
	}

	@Override
	protected Map<String, IPropertyHandler> getPropertyHandlers() {
		if (isVarBlock()) {
			return VAR_PROPERTY_HANDLERS;
		} else if (isTextBlock()) {
			return TEXT_PROPERTY_HANDLERS;
		}
		return super.getPropertyHandlers();
	}
	
	@Override
	protected Map<String, IPropertyHandler> getDefaultPropertyHandlers() {
		return ATOM_PROPERTY_HANDLERS;
	}

	@Override
	protected EStructuralFeature getTableFeature() {
		if (isVarBlock()) {
			return AtomPackage.Literals.ATOM_FUNCTION__INTERNAL_VARIABLES;
		}
		return super.getTableFeature();
	}
	
	@Override
	protected EClass getEClass() {
		if (isVarBlock()) {
			return BizPackage.Literals.PARAMETER;
		}
		return super.getEClass();
	}
	
	protected boolean isVarBlock() {
		return StringUtils.equals(parser.getCurrentBlockTag(), "����");
	}
	
	protected boolean isTextBlock() {
		return StringUtils.equals(parser.getCurrentBlockTag(), "ҵ��������");
	}

}
