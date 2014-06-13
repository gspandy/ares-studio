/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.util;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.BizInterfaceParameterUtil;
import com.hundsun.ares.studio.core.IARESProject;

/**
 * ҵ���߼����������
 * @author liaogc
 *
 */
public class LogicCompilerUtil {
	/**
	 * �жϸ����Ĳ������Ƿ���ҵ���߼������������
	 * @param logicResource ҵ���߼�
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINInputParameterByName(AtomFunction logicResource, String parameterName,IARESProject project) {
		return BizInterfaceParameterUtil.isInputParameter(logicResource, parameterName,project);
	}
	
	/**
	 * �жϸ����Ĳ������Ƿ���ҵ���߼������������
	 * @param logicResource ҵ���߼�
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINOutputParameterByName(AtomFunction logicResource, String parameterName,IARESProject project) {
		return BizInterfaceParameterUtil.isOutputParameter(logicResource, parameterName,project);
	}
	
	/**
	 * �жϸ����Ĳ������Ƿ���ҵ���߼����������������
	 * @param procedure ҵ���߼�
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINInputAndOutputParameterByName(AtomFunction logicResource, String parameterName,IARESProject project) {
		return isParameterINInputParameterByName(logicResource, parameterName,project) ||
				isParameterINOutputParameterByName(logicResource, parameterName,project);
	}

	/**
	 * �жϸ����Ĳ������Ƿ���ҵ���߼����ڲ�������
	 * @param procedure ҵ���߼�
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINInternalVariablesByName(AtomFunction logicResource, String parameterName) {
		List<String> names = new ArrayList<String>();
		for (Parameter para : logicResource.getInternalVariables()) {
			names.add(para.getId());
		}
		return names.contains(parameterName);
	}
	
	/**
	 * �жϸ����Ĳ������Ƿ���ҵ���߼�����������Լ��ڲ�������
	 * @param procedure ҵ���߼�
	 * @param parameterName ������
	 * @return
	 */
	public static boolean isParameterINLogicParameterByName(AtomFunction logicResource, String parameterName,IARESProject project) {
		return isParameterINInputParameterByName(logicResource, parameterName,project) ||
				isParameterINOutputParameterByName(logicResource, parameterName,project) ||
				isParameterINInternalVariablesByName(logicResource, parameterName);
	}
}
