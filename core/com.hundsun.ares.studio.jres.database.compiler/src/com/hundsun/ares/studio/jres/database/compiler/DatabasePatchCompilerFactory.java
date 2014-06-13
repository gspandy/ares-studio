/**
 * Դ�������ƣ�DatabaseResourceCompilerFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.compiler
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.compiler;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.compiler.IResourceCompiler;
import com.hundsun.ares.studio.jres.compiler.IResourceCompilerFactory;
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;

/**
 * @author gongyf
 *
 */
public class DatabasePatchCompilerFactory implements
		IResourceCompilerFactory {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.compiler.IResourceCompilerFactory#create(java.lang.Object)
	 */
	@Override
	public IResourceCompiler create(Object resource) {
		return new DatabaseInfoCompiler(IDBConstant.JS_GEN_INFO_PATCH_FUNCTION);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.compiler.IResourceCompilerFactory#isSupport(com.hundsun.ares.studio.jres.core.compiler.IARESProject)
	 */
	@Override
	public boolean isSupport(IARESProject project) {
		return true;
	}

}
