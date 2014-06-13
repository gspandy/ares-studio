package com.hundsun.ares.studio.procdure.excel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.excel.handlers.InterfaceSheetHandler;
import com.hundsun.ares.studio.core.excel.Module;
import com.hundsun.ares.studio.core.excel.Resource;
import com.hundsun.ares.studio.procdure.ProcdureFactory;
import com.hundsun.ares.studio.procdure.ProcdurePackage;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.constants.IProcedureResType;

public class ProcedureSheetHandler  extends InterfaceSheetHandler{
	
	/** ת�Ƶ���Ӧ��PropertyHandlerFactory: ProcedurePropertyHandlerFactory,ParameterPropertyHandlerFactory, VarPropertyHandlerFactory
	static {
		PROCEDURE_PROPERTY_HANDLERS.putAll(INTERFACE_PROPERTY_HANDLERS);
		PROCEDURE_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DATABASE));
		PROCEDURE_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DATABASE));
		PROCEDURE_PROPERTY_HANDLERS.put("�������ݿ�", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DATABASE));
		PROCEDURE_PROPERTY_HANDLERS.put("�ӿڱ�־", new EMFPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__INTERFACE_FLAG));
		//--����֤ȯ����
		PROCEDURE_PROPERTY_HANDLERS.put("ҵ������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__BIZ_TYPE));
		PROCEDURE_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__DEFINE_TYPE));
		PROCEDURE_PROPERTY_HANDLERS.put("�汾���", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__CREATE_DATE));
		PROCEDURE_PROPERTY_HANDLERS.put("���̷�������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__RETURN_TYPE));
		PROCEDURE_PROPERTY_HANDLERS.put("������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		PROCEDURE_PROPERTY_HANDLERS.put("��������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		PROCEDURE_PROPERTY_HANDLERS.put("����˵��", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		PROCEDURE_PROPERTY_HANDLERS.put("����˵��", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		//�����������
		IO_PROPERTY_HANDLERS.put("�������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__FLAGS));
		IO_PROPERTY_HANDLERS.put("�������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__FLAGS));
		IO_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		IO_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		IO_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		IO_PROPERTY_HANDLERS.put("˵��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__NAME));
		IO_PROPERTY_HANDLERS.put("ȱʡֵ", NullPropertyHandler.INSTANCE);
		IO_PROPERTY_HANDLERS.put("����", NullPropertyHandler.INSTANCE);
		//--����֤ȯ����
		IO_PROPERTY_HANDLERS.put("Ĭ��ֵ", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__DEFAULT_VALUE));
		IO_PROPERTY_HANDLERS.put("ע��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__COMMENTS));
		
		// �ڲ�����
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__FLAGS));
		VAR_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		VAR_PROPERTY_HANDLERS.put("������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__ID));
		VAR_PROPERTY_HANDLERS.put("����", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		VAR_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__TYPE));
		VAR_PROPERTY_HANDLERS.put("˵��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__NAME));
		VAR_PROPERTY_HANDLERS.put("ȱʡֵ", NullPropertyHandler.INSTANCE);
		VAR_PROPERTY_HANDLERS.put("����", NullPropertyHandler.INSTANCE);
		//--����֤ȯ����
		VAR_PROPERTY_HANDLERS.put("Ĭ��ֵ", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__DEFAULT_VALUE));
		VAR_PROPERTY_HANDLERS.put("ע��", new EMFPropertyHandler(BizPackage.Literals.PARAMETER__COMMENTS));
		
		TEXT_PROPERTY_HANDLERS.put("ҵ��������", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__PSEUDO_CODE));
		TEXT_PROPERTY_HANDLERS.put("����˵��", NullPropertyHandler.INSTANCE);
		//--����֤ȯ����
		TEXT_PROPERTY_HANDLERS.put("ǰ�ô���", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__BEGIN_CODE));
		TEXT_PROPERTY_HANDLERS.put("���ô���", new EMFPropertyHandler(ProcdurePackage.Literals.PROCEDURE__END_CODE));
	} */

	private Procedure procedure;
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.handlers.AbstractSheetHandler#startSheet(org.apache.poi.hssf.usermodel.HSSFSheet)
	 */
	@Override
	public void startSheet(Sheet sheet) {
		super.startSheet(sheet);
		module = new Module();
		//���ķָ�����Ҳ֧��
		String sheetName = StringUtils.replace(sheet.getSheetName(), "��", "-");
		String cName = StringUtils.removeStart(sheetName, "ԭ�ӹ���-");
		module.cName = cName;
	}
	
//	@Override
//	protected Map<String, IPropertyHandler> getPropertyHandlers() {
//		if (isInputParmaBlock() || isOutputParmaBlock()) {
//			return IO_PROPERTY_HANDLERS;
//		}
//		
//		if (isVarBlock()) {
//			return VAR_PROPERTY_HANDLERS;
//		} else if (isTextBlock()) {
//			return TEXT_PROPERTY_HANDLERS;
//		}
//		
//		return PROCEDURE_PROPERTY_HANDLERS;
//	}

	@Override
	protected EStructuralFeature getTableFeature() {
		if (isVarBlock()) {
			return ProcdurePackage.Literals.PROCEDURE__INTERNAL_VARIABLES;
		}
		return super.getTableFeature();
	}
	
	@Override
	protected EClass getEClass() {
		if (isVarBlock()) {
			return BizPackage.Literals.PARAMETER;
		}
		if (isInputParmaBlock() || isOutputParmaBlock()) {
			return BizPackage.Literals.PARAMETER;
		}
	
		if (isVarBlock()) {
			return ProcdurePackage.Literals.INTERNAL_PARAM;
		} else if (isTextBlock()) {
			return ProcdurePackage.Literals.PROCEDURE;
		}
		
		return ProcdurePackage.Literals.PROCEDURE;
	}
	
	protected boolean isVarBlock() {
		return StringUtils.equals(parser.getCurrentBlockTag(), "����");
	}
	
	protected boolean isTextBlock() {
		return StringUtils.equals(parser.getCurrentBlockTag(), "ҵ��������") 
				|| StringUtils.equals(parser.getCurrentBlockTag(), "ǰ�ô���")
				|| StringUtils.equals(parser.getCurrentBlockTag(), "���ô���");
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#startArea(java.lang.String)
	 */
	@Override
	public void startArea(String startTag) {
		super.startArea(startTag);
		if(StringUtils.equals(startTag, "�����"))
			procedure = ProcdureFactory.eINSTANCE.createProcedure();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endArea()
	 */
	public void endArea() {
		super.endArea();
		if (procedure != null) {
			Resource resource = new Resource();
			resource.info = procedure;
			resource.name = procedure.getName();
			resource.type = IProcedureResType.PROCEDURE;
			
			for (Parameter param : procedure.getInternalVariables()) {
				param.setParamType(ParamType.NON_STD_FIELD);
			}
			
			resourceFound(resource);
		}
	}
	
	@Override
	protected EObject getOwner() {
		return procedure;
	}


}
