/**
 * Դ�������ƣ�RefEMFPropertyHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.reference;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.excel.handler.EMFPropertyHandler;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandler2;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;

/**
 * ���Handler��getValue�������ݸ�����Object�����ԣ�refType����Ϣ���ҵ���Ӧ�����ö��󣬷���Ŀ������ĳ�����ԡ�
 * ���������������ʵ������Ӧ�����ñ�׼�ֶε����������ԣ����Handler����ʵ������������.
 * @author sundl
 */
public class RefEMFPropertyHandler extends EMFPropertyHandler implements IPropertyHandler2{

	private IARESProject project;
	private EStructuralFeature refIdFeature;
	private EStructuralFeature targetFeature;
	private String refType;
	
	/**
	 * @param refIdFeature ȷ������id�����ԣ����������id������ȷ�������ĸ���׼�ֶ�
	 * @param refType ��������ID���������ñ�׼�ֶΣ����Ǳ�׼�ֶε�refType
	 * @param targetFeature Ŀ������feature���������ñ�׼�ֶε����������ԣ�����CName���Feature.
	 * @param feature ͬEMFPropertyHandler��feature���������������������͵�������������setValue, ������������getValue
	 */
	public RefEMFPropertyHandler(EStructuralFeature refIdFeature, String refType, EStructuralFeature targetFeature, EStructuralFeature feature) {
		super(feature);
		this.refIdFeature = refIdFeature;
		this.targetFeature = targetFeature;
		this.refType = refType;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.IPropertyHandler2#setProject(com.hundsun.ares.studio.core.IARESProject)
	 */
	@Override
	public void setProject(IARESProject project) {
		this.project = project;
	}
	
	@Override
	public String getValue(Object obj) {
		EObject owner = getOwner(obj);
		if (owner != null) {
			Object value = ((EObject) owner).eGet(getTargetFeature(owner));
			if (value != null) {
				return String.valueOf(value);
			} else {
				return StringUtils.EMPTY;
			}
		}
		return super.getValue(obj);
	}
	
	protected EStructuralFeature getTargetFeature(EObject object) {
		return targetFeature;
	}
	
	protected EObject getOwner(Object object) {
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			EStructuralFeature refIdFeature = getRefIdFeature(eObject);
			if (refIdFeature == null)
				return null;
			
			String refName = String.valueOf(((EObject) object).eGet(refIdFeature));
			ReferenceManager manager = ReferenceManager.getInstance();
			String refType = getRefType(eObject);
			if (refType == null) {
				return null;
			}
			
			ReferenceInfo refInfo = manager.getFirstReferenceInfo(project, refType, refName, false);
			if (refInfo != null) {
				Object owner = refInfo.getObject();
				if (owner instanceof EObject) {
					return (EObject) owner;
				}
			}
		}
		return null;
	}
	
	protected EStructuralFeature getRefIdFeature(EObject object) {
		return this.refIdFeature;
	}
	
	protected String getRefType(EObject object) {
		return this.refType;
	}

}
