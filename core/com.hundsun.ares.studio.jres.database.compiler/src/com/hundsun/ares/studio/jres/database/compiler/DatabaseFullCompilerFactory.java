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
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.ViewResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.SequenceResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.TriggerResourceData;

/**
 * @author gongyf
 *
 */
public class DatabaseFullCompilerFactory implements
		IResourceCompilerFactory {

	@Override
	public IResourceCompiler create(Object resource) {
		if (resource instanceof ViewResourceData) {
			return new DatabaseInfoCompiler(IDBConstant.JS_GEN_VIEW_INFO_FUNCTION);
		}else if (resource instanceof TableResourceData){
			return new DatabaseInfoCompiler(IDBConstant.JS_GEN_TABLE_INFO_FUNCTION);
		}else if (resource instanceof SequenceResourceData) {
			return new DatabaseInfoCompiler(IDBConstant.JS_GEN_OSEQUENCE_INFO_FUNCTION);
		}else if (resource instanceof TriggerResourceData) {
			return new DatabaseInfoCompiler(IDBConstant.JS_GEN_OTRIGGER_INFO_FUNCTION);
		}
		return null;
	}

	@Override
	public boolean isSupport(IARESProject project) {
		return true;
	}

}
