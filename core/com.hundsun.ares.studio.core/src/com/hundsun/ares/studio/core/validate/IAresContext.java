/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.validate;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;

/**
 * �����Ķ���һ������������ǰ��Ŀ����ǰ��Ŀ�е���Դ��<br>
 * ���������ڴ����飬�ű��ȴ�����ִ�е�ǰ�����������Ļ����������ȡ��Ŀ����Դ��Ϣ������
 * ͬʱ���������Դ���ҵ�Ч�ʡ� <br>
 * ����ṩ��Ĭ�ϵ������ģ�idΪ"default"������ʵ����ΪDefaultContextProvider. �û�����
 * �ṩ�Լ���������ʵ�֣��ڿ����ע�ᡣ
 * @author sundl
 */
public interface IAresContext {

	/** Ĭ�������ĵ�id�� ֵΪ"default" */
	String DEFAULT_CONTEXT = "default";
		
	/**
	 * ��ʼ��������
	 * @param project ��ǰ��Ŀ, ��ֹnull
	 * @throws ARESModelException �����ʼ�����̷�������Ļ�
	 */
	public void init(IARESProject project) throws ARESModelException;
	
}
