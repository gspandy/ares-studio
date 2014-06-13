/**
 * Դ�������ƣ�ProblemPoolChangeEvent.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.validate;

import java.util.Map;

/**
 * @author gongyf
 *
 */
public class ProblemPoolChangeEvent {
	
	
	private IProblemPool source;
	private Map<Object, Object> context;
	private Object[] removeedProblems;
	private Object[] addProblems;
	
	/**
	 * @param source
	 * @param removeedProblems
	 * @param addProblems
	 * @param context
	 */
	public ProblemPoolChangeEvent(IProblemPool source,
			Object[] removeedProblems, Object[] addProblems,
			Map<Object, Object> context) {
		super();
		this.source = source;
		this.removeedProblems = removeedProblems;
		this.addProblems = addProblems;
		this.context = context;
	}
	/**
	 * @return the source
	 */
	public IProblemPool getSource() {
		return source;
	}
	/**
	 * @return the context
	 */
	public Map<Object, Object> getContext() {
		return context;
	}
	
	/**
	 * @return the removeedProblems
	 */
	public Object[] getRemoveedProblems() {
		return removeedProblems;
	}
	
	/**
	 * @return the addProblems
	 */
	public Object[] getAddProblems() {
		return addProblems;
	}
}
