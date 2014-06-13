/**
 * Դ�������ƣ�EMFBasedSheetHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel.handler;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.excel.AbstractSheetHandler;
import com.hundsun.ares.studio.core.excel.BlockTypes;

/**
 * ��װһЩ����EMF
 * @author sundl
 *
 */
public abstract class EMFSheetHandler extends AbstractSheetHandler{
	
	private static final Logger logger = Logger.getLogger(EMFSheetHandler.class);
	
	protected EObject owner;
	protected String[] header;

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#keyValue(java.lang.String, java.lang.String)
	 */
	@Override
	public void keyValue(String key, String value) {
		EObject model = getOwner();
		if (model == null) {
			super.keyValue(key, value);
			return;
		}
		
		IPropertyHandler handler = getHandler(model.eClass(), key);
		if (handler != null)
			handler.setValue(getOwner(), value);
		else 
			super.keyValue(key, value);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#header(java.lang.String[])
	 */
	@Override
	public void header(String[] header) {
		this.header = header;
	} 
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#row(java.lang.String[])
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void row(String[] row) {
		if (header == null)
			return;
		
		// ��ʱ���������
		if (isAllEmpty(row)) 
			return;
		
		EClass eClass = getEClass();
		if (eClass == null)
			return;
		
		EStructuralFeature feature = getTableFeature();
		if (feature == null)
			return;
		
		EObject owner = getOwner();
		EObject newObject = eClass.getEPackage().getEFactoryInstance().create(eClass);
		List list = (List) owner.eGet(feature);
		list.add(newObject);
		
		int cellCount = row.length;
		int headerCount = header.length;
		
		// ����бȱ�ͷ�༸�У�����������ļ����ǿ��У�Ҳ����ν�� ����ǿգ�˵�����ܳ������ˡ�
		if (cellCount > headerCount) {
			for (int i = headerCount; i < cellCount; i++) {
				if (!StringUtils.isEmpty(row[i])) {
					log.warn(String.format("��ͷ���е�������һ�£�����ֵ�����޷����룬\n��ͷ��%s,\n�У�%s", StringUtils.join(header, ','), StringUtils.join(row, ',')), generateLocation());
					break;
				}
			}
		} // else if ...  ��֮������бȱ�ͷ�ټ��У���Ϊ�ǿ�ֵ���ѣ�����������
		
		for (int i = 0; i < headerCount; i++) {
			IPropertyHandler handler = getHandler(eClass, header[i].toUpperCase());
			// �����������п��ܻ�ȱ�ͷ�ټ���
			String value = null;
			if (i >= cellCount) {
				value = StringUtils.EMPTY;
			} else {
				value = row[i];
			}
			
			if (handler != null) 
				handler.setValue(newObject, value);
			else
				log.error("�޷���������ԣ�" + header[i], generateLocation());
			
			if (StringUtils.isEmpty(value) && StringUtils.equals(header[i], "������")) {
				log.error("������������Ĳ�����Ϊ��", generateLocation());
			}
		}
	}
	
	/**
	 * һ�н�����������ã���������ڴ˴�����һЩ����
	 * @param object
	 */
	protected void newRowObjectFound(EObject object) {}
	
	private boolean isAllEmpty(String[] strings) {
		for (String str : strings) {
			if (!StringUtils.isEmpty(str))
				return false;
		}
		return true;
	}
	
	protected IPropertyHandler getHandler(EClass eClass, String property) {
		IPropertyHandlerFactory factory = PropertyHandlerFactoryManager.getPropertyHandlerFactory(eClass);
		if (factory == null) 
			logger.error(eClass.getName() + "��PropertyHandlerFactoryΪnull");
		IPropertyHandler handler =  factory.getPropertyHandler(property, getProject());
		if (handler == null)
			logger.error(String.format("%s -- %s��PropertyHandlerΪnull", eClass.getName(), property));
		return handler;
	}
	
	/**
	 * ��ǰ��owner���� ���ڲ�ͬ��area��block�е�ʱ�����ֵ�������ǲ�ͬ�ġ�
	 * @return
	 */
	protected abstract EObject getOwner();
	
	/**
	 * ��������Ӧ��owner���ĸ��б�����
	 * @param tableTag
	 * @return
	 */
	protected abstract EStructuralFeature getTableFeature();
	
	/**
	 * ��ǰ�������ȶ�Ӧ��EClass
	 * @return
	 */
	protected abstract EClass getEClass();


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endBlock()
	 */
	@Override
	public void endBlock() {
		if (parser.getCurrentBlockType() == BlockTypes.TABLE) {
			header = null;
		} else if (parser.getCurrentBlockType() == BlockTypes.TEXT) {
			IPropertyHandler handler = getHandler(getEClass(), parser.getCurrentBlockTag());
			if (handler != null) {
				handler.setValue(getOwner(), parser.getText());
			} else {
				log.error("�����޷�����" + parser.getCurrentBlockTag(), generateLocation());
			}
		}
	}
	
}
