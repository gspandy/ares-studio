/**
 * Դ�������ƣ�DBTableGenCodeUtils.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.compiler.CompilationResult;
import com.hundsun.ares.studio.jres.compiler.CompileManager;
import com.hundsun.ares.studio.jres.compiler.CompileUtils;
import com.hundsun.ares.studio.jres.compiler.IResourceCompilerFactory;
import com.hundsun.ares.studio.jres.database.compiler.DBCompilationResult;
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.model.database.DBModuleCommonProperty;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * @author qinyuan
 *
 */
public class DBTableGenCodeUtils {
	
	private static final String SEPARATOR = "-------------------------------------------------\r\n";
	private static final String NEWLINE = "\r\n";
	
	/**
	 * �������ݿ�ȫ���ű�
	 * @param resource
	 * @param eObj
	 * @return
	 */
	public static StringBuffer genTableFullCode( IARESResource resource, EObject eObj) {
		StringBuffer buffer = new StringBuffer();
		if (eObj instanceof TableResourceData) {
			buffer.append(SEPARATOR);
			buffer.append("--ȫ���ű�");
			buffer.append(NEWLINE);
			buffer.append(SEPARATOR);
		}
		
		// ����������Ҫ����IARESResource����
		Map<Object, Object> context = getContext(resource);
		
		context.put("type", IDBConstant.COMPILE_DATABASE_FULL);
		appendTextCompilationResult(buffer, IDBConstant.COMPILE_DATABASE_FULL, eObj, context);
		
		return buffer;
	}
	
	public static StringBuffer genTablePatchCode( IARESResource resource, EObject eObj) {
		StringBuffer buffer = new StringBuffer();
		
		if (eObj instanceof TableResourceData) {
			buffer.append(NEWLINE);
			buffer.append(SEPARATOR);
			buffer.append("--�����ű�");
			buffer.append(NEWLINE);
			buffer.append(SEPARATOR);
		}
		
		// ����������Ҫ����IARESResource����
		Map<Object, Object> context = getContext(resource);
		
		context.put("type", IDBConstant.COMPILE_DATABASE_PATCH);
		appendTextCompilationResult(buffer, IDBConstant.COMPILE_DATABASE_PATCH, eObj, context);
		
		return buffer;
	}
	
	/**
	 * ��ȡ������
	 * @param resource
	 * @return
	 */
	private static Map<Object, Object> getContext(IARESResource resource){
		// ����������Ҫ����IARESResource����
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put(CompileUtils.ARES_RESOURCE, resource);
		
		IARESProject project = resource.getARESProject();
		DBModuleCommonProperty property;
		try {
			property = (DBModuleCommonProperty) project.getProjectProperty().getMap().get(IDBConstant.MODULE_DATABASE_EXTEND_PROPERTY_KEY);
			property = (DBModuleCommonProperty) ObjectUtils.defaultIfNull(
					property, 
					DatabaseFactory.eINSTANCE.createDBModuleCommonProperty());
			context.put(IDBConstant.DATABASE_TYPE, property.getDatabase());
		} catch (ARESModelException e) {
			e.printStackTrace();
		} 
		return context;
	}
 	
	private static void appendTextCompilationResult(StringBuffer buffer, String type, EObject eObj, Map<Object, Object> context) {
		IResourceCompilerFactory factory = CompileManager.getInstance().getFactoryByType(type);
		if (factory != null) {
			CompilationResult result = factory.create(eObj).compile(eObj, context);
			if (result instanceof DBCompilationResult) {
				Map<String, StringBuffer> sqlMap = ((DBCompilationResult) result).getSqlByUser();
				StringBuffer sbs = new StringBuffer();
				for (StringBuffer sb : sqlMap.values()){
					sbs.append(sb);
				}
				buffer.append(sbs.toString());
			} else {
				buffer.append("--�޷���ʾ�������������������ı�");
			}
		} else {
			buffer.append("--�޷���ʾ��������û�ж�Ӧ�ı�����֧��");
		}
	}


}
