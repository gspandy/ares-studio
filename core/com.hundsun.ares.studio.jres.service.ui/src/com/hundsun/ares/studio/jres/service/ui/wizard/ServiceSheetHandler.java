package com.hundsun.ares.studio.jres.service.ui.wizard;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.biz.excel.handlers.InterfaceSheetHandler;
import com.hundsun.ares.studio.core.excel.Module;
import com.hundsun.ares.studio.core.excel.Resource;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.service.Service;
import com.hundsun.ares.studio.jres.service.ServiceFactory;
import com.hundsun.ares.studio.jres.service.ServicePackage;

public class ServiceSheetHandler extends InterfaceSheetHandler{

	/** ת�Ƶ���Ӧ��PropertyHandlerFactory�� ---> ServiceHandlerFactory
	static{
		SERVICE_PROPERTY_HANDLERS.putAll(INTERFACE_PROPERTY_HANDLERS);
		SERVICE_PROPERTY_HANDLERS.put("���ܺ�", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID));
		SERVICE_PROPERTY_HANDLERS.put("������", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		//2014-01-28 modified by zhuyf ���Ӣ�������������ӿڣ�ʱ�ĵ����д˸�ʽ��Ϣ��
		SERVICE_PROPERTY_HANDLERS.put("Ӣ����", EMFPropertyHandler.NAME_PROPERTY_HANDLER);
		SERVICE_PROPERTY_HANDLERS.put("����������", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		SERVICE_PROPERTY_HANDLERS.put("��������", new EMFPropertyHandler(CorePackage.Literals.BASIC_RESOURCE_INFO__CHINESE_NAME));
		//�����ڷ���ӿ� ����Ϊ������������
		SERVICE_PROPERTY_HANDLERS.put("��������", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__INPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("��������", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("���������", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION));

		//�����Կ���  ��ɾ��ԭ��������
		SERVICE_PROPERTY_HANDLERS.put("�����Ƿ�����", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__INPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("����Ƿ�����", new InOutCollectionPropertyHandler(BizPackage.Literals.BIZ_INTERFACE__OUTPUT_COLLECTION));
		SERVICE_PROPERTY_HANDLERS.put("�Ƿ񹫿�", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("�Ƿ񸴺�", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("��������", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("���˼���", NullPropertyHandler.INSTANCE);
		SERVICE_PROPERTY_HANDLERS.put("ҵ��������", NullPropertyHandler.INSTANCE);
		
		SERVICE_PARAM_PROPERTY_HANDLERS.putAll(PARAM_PROPERTY_HANDLERS);
		SERVICE_PARAM_PROPERTY_HANDLERS.put("xml��ǩ", NullPropertyHandler.INSTANCE);
		SERVICE_PARAM_PROPERTY_HANDLERS.put("XML��ǩ", NullPropertyHandler.INSTANCE);
	}*/
	
	private Service service;
	
	@Override
	public void startSheet(Sheet sheet) {
		super.startSheet(sheet);
		module = new Module();
		String cName = null;
		//���ķָ�����Ҳ֧��
		String sheetName = StringUtils.replace(sheet.getSheetName(), "��", "-");
		if (StringUtils.startsWith(sheetName, "����-")) {
			cName = StringUtils.removeStart(sheetName, "����-");
		} else if (StringUtils.startsWith(sheetName, "���ܽӿڶ���-")) {
			cName = StringUtils.removeStart(sheetName, "���ܽӿڶ���-");
		} else {
			cName = sheetName;
		}
		module.cName = cName;
	}
	
	@Override
	public void startArea(String startTag) {
		super.startArea(startTag);
		if (StringUtils.equals(startTag, "�����") || StringUtils.equals(startTag, "���ܺ�"))
			service = ServiceFactory.eINSTANCE.createService();
	}
	
	@Override
	protected EObject getOwner() {
		if (isInterfacePropertyes() || isErrorInfoBlock()) {
			return service.getInterface();
		}
		return service;
	}
	
	protected EClass getEClass() {
		if (isInterfacePropertyes()) {
			return BizPackage.Literals.PARAMETER;
		} else if (isErrorInfoBlock()) {
			return BizPackage.Literals.ERROR_INFO;
		}
		return ServicePackage.Literals.SERVICE;
	}
	
	protected boolean isInterfacePropertyes() {
		boolean isInputOuputCollection = StringUtils.equals(parser.getCurrentBlockTag(), "��������")
											|| StringUtils.equals(parser.getCurrentBlockTag(), "��������")
											|| StringUtils.equals(parser.getCurrentBlockTag(), "�����Ƿ�����")
											|| StringUtils.equals(parser.getCurrentBlockTag(), "����Ƿ�����");
		return isInputOuputCollection || isInputParmaBlock() || isOutputParmaBlock();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endArea()
	 */
	public void endArea() {
		super.endArea();
		if (service != null) {
			Resource resource = new Resource();
			resource.info = service;
			resource.name = service.getName();
			resource.type = IBizResType.Service;
			
			resourceFound(resource);
		}
	}
	
}
