package com.hundsun.ares.studio.logic.excel;

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

public abstract class LogicSheetHandler  extends InterfaceSheetHandler{

	public static Map<String, IPropertyHandler> LOGIC_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	/** �������Դ����Ӧ�б� */
	public static Map<String, IPropertyHandler> VAR_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	public static Map<String, IPropertyHandler> LOGIC_CODE_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	static {
		LOGIC_PROPERTY_HANDLERS.putAll(INTERFACE_PROPERTY_HANDLERS);
		LOGIC_PROPERTY_HANDLERS.put("�������ݿ�", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("������", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("�Ƿ񸴺�", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("��������", NullPropertyHandler.INSTANCE);
		LOGIC_PROPERTY_HANDLERS.put("���˼���", NullPropertyHandler.INSTANCE);

		// �ڲ�����
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__FLAGS));
		VAR_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		VAR_PROPERTY_HANDLERS.put("˵��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__NAME));
		VAR_PROPERTY_HANDLERS.put("ȱʡֵ", NullPropertyHandler.INSTANCE);
		VAR_PROPERTY_HANDLERS.put("����", NullPropertyHandler.INSTANCE);
		
		LOGIC_CODE_PROPERTY_HANDLERS.put("ҵ��������", new EMFPropertyHandler(AtomPackage.Literals.ATOM_FUNCTION__PSEUDO_CODE));
		LOGIC_CODE_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
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
		String cName = StringUtils.removeStart(sheetName, "�߼�����-");
		cName = StringUtils.removeStart(cName, "�߼�����-");
		module.cName = cName;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.AbstractSheetHandler#endSheet()
	 */
	@Override
	public void endSheet() {
		super.endSheet();
	}

	@Override
	protected Map<String, IPropertyHandler> getPropertyHandlers() {
		if (isVarBlock()) {
			return VAR_PROPERTY_HANDLERS;
		} else if (isTextBlock()) {
			return LOGIC_CODE_PROPERTY_HANDLERS;
		}
		return super.getPropertyHandlers();
	}
	
	protected Map<String, IPropertyHandler> getDefaultPropertyHandlers() {
		return LOGIC_PROPERTY_HANDLERS;
	}

	@Override
	protected EStructuralFeature getTableFeature() {
		if (isVarBlock()) {
			return AtomPackage.Literals.ATOM_FUNCTION__INTERNAL_VARIABLES;
		}
		return super.getTableFeature();
	}
	
	protected boolean isVarBlock() {
		return StringUtils.equals(parser.getCurrentBlockTag(), "����");
	}
	
	protected boolean isTextBlock() {
		return StringUtils.equals(parser.getCurrentBlockTag(), "ҵ��������");
	}
	
	protected EClass getEClass() {
		if (isVarBlock()) {
			return AtomPackage.Literals.INTERNAL_PARAM;
		}
		return super.getEClass();
	}

}
