/**
 * Դ�������ƣ�IProblemPool.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.validate;

import java.util.Set;

/**
 * @author gongyf
 *
 */
public interface IProblemPool {
	void addView(IProblemView view);
	void removeView(IProblemView view);
	
	void beginChange();
	void endChange();
	
	void addProblem(KeyParameter key, Object problem);
	void addProblem(Object problem);
	void addProblems(Object[] problem);
	public void setKeyConstructor(IKeyConstructor handler);
	
	public Object[] getProblem(KeyParameter key);
	public boolean hasProblem(KeyParameter key);
	
	public void clear();
	public void clear(KeyParameter key);
	
	public Set<KeyParameter> getKeys();
}
