/**
 * Դ�������ƣ�CompileUtils.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.compiler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESResource;


/**
 * @author gongyf
 *
 */
public class CompileUtils {
	
	/**
	 * ����һЩֱ�ӱ���ģ�͵ı�������ȡ��Ӧ����Դ����<BR>
	 * ���������п������²�����<BR>
	 * <code>
	 * IARESResource resource = 
	 * 	(IARESResource)context.get(CompileUtils.ARES_RESOURCE);
	 * </code>
	 */
	public static final String ARES_RESOURCE = "#IARESResource#";
	
	/**
	 * �ڱ������л�ȡ��Դ����
	 * @param resource
	 * @param context
	 * @return
	 */
	public static IARESResource getARESResource(Object resource,
			Map<Object, Object> context) {
		if (resource instanceof IARESResource) {
			return (IARESResource) resource;
		}
		return (IARESResource) context.get(ARES_RESOURCE);
	}
	
	/**
	 * ����EClass��ȡ��������
	 * @param eClass
	 * @return
	 */
	public static String getEClassCompileType(EClass eClass) {
		return getEClassCompileType(eClass.getEPackage().getNsURI(), eClass.getName());
	}
	
	public static String getEClassCompileType(String uri, String name) {
		return String.format(CompilerConstants.Compile_EObject_Format, uri, name);
	}
	
	/**
	 * ����EMF����ʵ����ȡ��������
	 * @param eObj
	 * @return
	 */
	public static String getEObjectCompileType(EObject eObj) {
		return getEClassCompileType(eObj.eClass());
	}
	
	/**
	 * ��ӡ������־
	 * 
	 * @param logger logger����
	 * @param compilationResult ����������
	 * @param message ��־����Ϣ
	 */
	public static void writeCompilationResultLog (Logger logger, CompilationResult compilationResult ,String message){
		IStatus status = compilationResult.getStatus();
		IStatus[] statuses = status.getChildren();
		
		StringBuilder sb = new StringBuilder();
		sb.append(status.getMessage());
		
		for (int i = 0; i<statuses.length ; i++) {
			IStatus stat = statuses[i];
			if (i == 0) {
				sb.append("(");
			}
			if (i > 0) {
				sb.append("��");
			}
			sb.append(stat.getMessage());
			if (i == statuses.length - 1) {
				sb.append(")");
			}
		}
		message = message + sb;
		if (statuses.length == 0 || status.getSeverity() == IStatus.INFO || status.getSeverity() == IStatus.OK || status.getSeverity() == IStatus.CANCEL) {
			logger.info(message);
		}
		if (status.getSeverity() == IStatus.ERROR) {
			logger.error(message);
		}
		if (status.getSeverity() == IStatus.WARNING) {
			logger.warn(message);
		}
	}
	
}
