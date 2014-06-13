package com.hundsun.ares.studio.logic.excel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.excel.Resource;
import com.hundsun.ares.studio.logic.LogicFactory;
import com.hundsun.ares.studio.logic.LogicService;
import com.hundsun.ares.studio.logic.constants.ILogicResType;

/**
 * ԭ�ӷ���Sheetҳ������
 * @author sundl
 */
public class LogicServiceSheetHandler extends LogicSheetHandler{
	
	private LogicService service;
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#startArea(java.lang.String)
	 */
	@Override
	public void startArea(String startTag) {
		super.startArea(startTag);
		if (StringUtils.equals(startTag, "�����"))
			service = LogicFactory.eINSTANCE.createLogicService();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endArea()
	 */
	@Override
	public void endArea() {
		super.endArea();
		if (service != null) {
			Resource resource = new Resource();
			resource.info = service;
			resource.name = service.getName();
			resource.type = ILogicResType.LOGIC_SERVICE;
			
			resourceFound(resource);
		}
	}
	
	@Override
	protected EObject getOwner() {
		return service;
	}
}
