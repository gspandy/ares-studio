package com.hundsun.ares.studio.logic.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.atom.AtomPackage;
import com.hundsun.ares.studio.biz.excel.handlers.InterfaceSheetHandler;
import com.hundsun.ares.studio.core.excel.Module;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler;

public abstract class LogicSheetHandler  extends InterfaceSheetHandler{

	/** �������Դ����Ӧ�б� */
	public static Map<String, IPropertyHandler> VAR_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	public static Map<String, IPropertyHandler> LOGIC_CODE_PROPERTY_HANDLERS = 	new HashMap<String, IPropertyHandler>();
	
	/** �ƶ�����Ӧ��PropertyHandlerFactory�� --> LogicPropertyHandlerFactory, VarPropertyHandlerFactory
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
	public void startSheet(Sheet sheet) {
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
	
}
