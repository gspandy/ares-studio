/**
 * Դ�������ƣ�DatabaseScriptPoxyFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.core.script.impl;

import java.util.Map;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.script.api.factory.IScriptPoxyFactory;

/**
 * @author lvgao
 *
 */
public class DatabaseScriptPoxyFactory  implements IScriptPoxyFactory{

	@Override
	public Object createPoxy(Object input, Map<Object, Object> context) {
		if(input instanceof IARESProject){
			return new DatabaseScriptWrap((IARESProject)input);
		}
		return null;
	}

	@Override
	public Object createModuleProxy(Object input) {
		return null;
	}

}
