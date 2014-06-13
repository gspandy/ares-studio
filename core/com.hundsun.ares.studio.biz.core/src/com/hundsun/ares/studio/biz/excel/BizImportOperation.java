/**
 * Դ�������ƣ�ImportOperation.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�dollyn
 */
package com.hundsun.ares.studio.biz.excel;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.ui.dialogs.IOverwriteQuery;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.core.excel.AbstractSheetHandler;
import com.hundsun.ares.studio.core.excel.ExcelHandlerException;
import com.hundsun.ares.studio.core.excel.ExcelParser;
import com.hundsun.ares.studio.core.excel.ImportOperation;
import com.hundsun.ares.studio.core.excel.Module;
import com.hundsun.ares.studio.core.excel.Resource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataModifyOperation;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author sundl
 *
 */
public abstract class BizImportOperation extends ImportOperation{
	
	/**
	 * @param files
	 * @param overwriteQuery
	 */
	public BizImportOperation(File[] files, IOverwriteQuery overwriteQuery) {
		super(files, overwriteQuery);
	}

	/**
	 * �޸ı�׼�ֶεĲ���
	 */
	private MetadataModifyOperation<StandardField> stdModifyOperation = null;
	
	@Override
	public void run(IProgressMonitor monitor) throws CoreException{
		monitor.beginTask("���룺", 5000);
		logger.debug("start");
		
		stdModifyOperation = new MetadataModifyOperation<StandardField>(project, IMetadataResType.StdField);
		
		// ��һ���� �����ļ����������е���Դ
		IProgressMonitor subMonitor1 = new SubProgressMonitor(monitor, 1000);
		subMonitor1.beginTask("", files.length);
		for (File file : files) {
			if (subMonitor1.isCanceled())
				return;
			
			subMonitor1.subTask("�����ļ���" + file.getName());
			exlPaser = createParser(file, log);
			exlPaser.postParseOperation = postParseOperation;
			exlPaser.context.put("std_filed_modify_operation", stdModifyOperation);
			exlPaser.parse();
			
			for (Module m : exlPaser.resources.keySet()) {
				m.processName(exlPaser.moduleNameMap, log);
			}
			
			resources.putAll(exlPaser.resources);
			subMonitor1.worked(1);
		}
		subMonitor1.done();
		log.totle = resources.size();
		
		// �ڶ����� ������Դ������ɺ󣬽���һ���������
		IProgressMonitor spmCheck = new SubProgressMonitor(monitor, 1000);
		checkRes(spmCheck);
		
		// �������� ��������Դ�Ͷ���
		IProgressMonitor spmProcessParma = new SubProgressMonitor(monitor, 1000);
		setParameterTypes(spmProcessParma);
		
		// ������PostOperation���������ȷ���˲��������Ժ����ִ��
		postParseOperation.run();
		stdModifyOperation.run(log, new SubProgressMonitor(monitor, 1000));
		
		// ���Ĳ�: ������Դ
		IProgressMonitor spmCreate = new SubProgressMonitor(monitor, 1000);
		try {
			createResources(spmCreate);
		} catch (ExcelHandlerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		monitor.done();
	}
	
	/**
	 * ���ڷ���ӿ���Դ�����еĲ�������,��ȷ���Ǳ�׼�ֶΣ��Ǳ�׼�ֶλ��Ƕ������͵Ĳ���
	 */
	protected void setParameterTypes(IProgressMonitor monitor) {
		monitor.beginTask("������������...", resources.size());
		for (Resource res : resources.values()) {
			if (res.info instanceof BizInterface) {
				BizInterface iface  = (BizInterface) res.info;
				for (Parameter p : iface.getInputParameters()) {
					setParameterType(p);
				}
				
				for (Parameter p : iface.getOutputParameters()) {
					setParameterType(p);
				}
			} else if (res.info instanceof ARESObject) {
				ARESObject obj = (ARESObject) res.info;
				for (Parameter p : obj.getProperties()) {
					setParameterType(p);
				}
			}
			monitor.worked(1);
		}
		monitor.done();
	}
	
	/**
	 * ������������
	 */
	protected void setParameterType(Parameter param) {
		String type = param.getType();
		// ���ҵ�������к��е㣬����Ϊ�Ƕ������͵Ĳ���������ǳ�����Ҳ�����Ƕ�������������룩
		if (StringUtils.contains(type, '.')) {
			param.setParamType(ParamType.OBJECT);
			
			// ������Դ��ģ���ڵ���Ĺ����У����Ӣ�������в����������淶�������ַ����ᱻ�滻��
			// ���Դ˴����ö�����ԴҲ��Ҫ�滻
			String correctType = AbstractSheetHandler.correctResName(type);
			if (correctType != null) 
				param.setType(correctType);
			return;
		}
		
		String obj = getObjectFullName(type);
		if (obj != null) {
			// �����ĵ��ﶼ�Ƕ�����ȷ�����ͺ���Ҫ�滻�ɳ���
			param.setParamType(ParamType.OBJECT);
			param.setType(obj);
		} else if (isStdField(param)) {
			param.setParamType(ParamType.STD_FIELD);
		} else {
			param.setParamType(ParamType.NON_STD_FIELD);
		}
	}
	
	//���������׼�ֶ�ͬ����������������Ҳ��ͬ����׼�ֶε���ͬ����Ϊ��׼�ֶ�
	protected boolean isStdField(Parameter p) {
		ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(project, IMetadataRefType.StdField, p.getId(), false);
		if(null != ref && ref.getObject() instanceof StandardField) {
			// 2013-09-17 sundl �������Ϊ�գ���ֻ�����ֶ���ƥ���׼�ֶμ���
			if (StringUtils.isEmpty(p.getType())) {
				return true;
			}
			
			// ���ָ�������ͣ�������ֶ��������͡��������ͱ�׼�ֶ���ȫ���
			StandardField field = (StandardField)ref.getObject();
			if(field != null && StringUtils.equals(p.getType(), field.getDataType())
								&& StringUtils.equals(p.getName(), field.getChineseName())){
				return true;
			}
		}
		return false;
	}
	
	protected String getObjectFullName(String id) {
		if (StringUtils.isEmpty(id))
			return null;
		
		// ������Դ�ڵ���Ĺ����У����Ӣ�������в����������淶�������ַ����ᱻ�滻��
		// ���Դ˴����ö�����ԴҲ��Ҫ�滻
		String correctType = AbstractSheetHandler.correctResName(id);
		if (correctType != null) 
			id = correctType;
		
		// ��������û��
		List<ReferenceInfo> references = ReferenceManager.getInstance().getReferenceInfos(project, IBizRefType.Object, true);
		for (ReferenceInfo ref : references) {
			ARESObject obj = (ARESObject) ref.getObject();
			if (obj != null && StringUtils.equals(id, obj.getName()))
				return ref.getResource().getFullyQualifiedName();
		}
		// ����������Ҳ������ڱ��ν�����������Դ����
		for (Module module : resources.keySet()) {
			for (Resource res : resources.get(module)) {
				if (StringUtils.equals(res.type, "object")) {
					if (StringUtils.equals(id, res.name))
						return module.getFullName() + "." + res.name;
				}
			}
		}
		return null;
	}

}
