package com.hundsun.ares.studio.jres.basicdata.ui.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.compiler.CompilationResult;
import com.hundsun.ares.studio.jres.compiler.CompileManager;
import com.hundsun.ares.studio.jres.compiler.CompileUtils;
import com.hundsun.ares.studio.jres.compiler.ICompilationResultExtension;
import com.hundsun.ares.studio.jres.compiler.IResourceCompilerFactory;

public class BasicDataGenCodeUtils {
	
	//�������ݰ�װģʽ����������չ��types
	public static final String BASICDATA_SQL_INSTALL= "#basicdata.gensql.install#";
	//�������ݰ�װ��������������չ��types
	public static final String BASICDATA_SQL_UPDATA= "#basicdata.gensql.update#";
	private static final String SEPARATOR = "-------------------------------------------------\n";
	private static final String NEWLINE = "\n";
	
	/**
	 * ��ȡ��������ȫ��ģ��
	 * @param resource ����������Դ
	 * @param eObj ��������ģ��
	 * @return
	 */
	public static StringBuffer genBasicDataFullCode(IARESResource resource, EObject eObj) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(SEPARATOR);
		buffer.append("--��װģʽ");
		buffer.append(NEWLINE);
		buffer.append(SEPARATOR);
		
		// ����������Ҫ����IARESResource����
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put(CompileUtils.ARES_RESOURCE, resource);
		
		appendTextCompilationResult(buffer, BASICDATA_SQL_INSTALL, eObj, context);
		
		return buffer;
	}
	
	/**
	 * ��ȡ�������������ű�
	 * @param resource ����������Դ
	 * @param eObj ��������ģ��
	 * @return
	 */
	public static StringBuffer genBasicDataPatchCode(IARESResource resource, EObject eObj) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(NEWLINE);
		buffer.append(SEPARATOR);
		buffer.append("--����ģʽ");
		buffer.append(NEWLINE);
		buffer.append(SEPARATOR);
		
		// ����������Ҫ����IARESResource����
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put(CompileUtils.ARES_RESOURCE, resource);
		
		appendTextCompilationResult(buffer, BASICDATA_SQL_UPDATA, eObj, context);
		return buffer;
	}
	
	private static void appendTextCompilationResult(StringBuffer buffer, String type, EObject eObj, Map<Object, Object> context) {
		IResourceCompilerFactory factory = CompileManager.getInstance().getFactoryByType(type);
		if (factory != null) {

			CompilationResult result = factory.create(eObj).compile(eObj, context);
			if (result instanceof ICompilationResultExtension) {
				buffer.append(((ICompilationResultExtension) result).getTextResult());
			} else {
				buffer.append("--�޷���ʾ�������������������ı�");
			}
		} else {
			buffer.append("--�޷���ʾ��������û�ж�Ӧ�ı�����֧��");
		}
	}


}
