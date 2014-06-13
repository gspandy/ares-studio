/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import org.eclipse.core.runtime.IPath;

/**
 * ��Դ·���ϵ�һ����Ŀ����¼����
 * ÿ��res-path entry�����Լ���Content-Kind(source, or binary)�����Kindȡ������������Module-Root���������͡�
 * <p>
 * һ��res-path entry����ָ��<ul>
 * <li>����Ŀ�µ�Դ���롣 ��������£�res-path entryָ����һ��ģ�����</li>
 * <li>һ��zip����(����Ϊ��binary���͵�)��չ������������ʽ������zip��ʽ�ģ��Ҳ�֧�ַ��ڹ���������ġ�</li>
 * <li>һ��������Ŀ����������£����entry��ʶ��һ�����������µ���Ŀ��</li>
 * </p><p>
 * ����ͨ��{@link ARESCommons}�ﶨ��ķ�������������ӿڵ�ʵ����
 * @author sundl
 */
public interface IResPathEntry {
	
	/**
	 * ������һ��Res-Path Entry��һ��ָ��Libray����zip��ʽ��ѹ����.
	 */
	int RPE_LIBRAY = 1;
	
	/**
	 * ��ʶ������Ŀ��entry����
	 */
	int RPE_PROJECT = 2;
	
	/**
	 * ��ʶSource��entry����
	 */
	int RPE_SOURCE = 3;

	/**
	 * �����Ŀ����ѹ������Ŀ¼�����Ŀ¼
	 * @return entry��Ӧ��·��
	 */
	IPath getPath();
	
	/**
	 * ���ͣ���ָ�����Դ���뻹��Libary.��zip��
	 * @return
	 */
	int getEntryKind();
	
	/**
	 * �������ͣ���Ӧ��ָ���ModuleRoot�����͡�
	 * ����Source���͵ģ�����KIND_SOURCE��������Libary���͵ģ�����KIND_BINARY
	 * @return
	 */
	int getContentKind();
	
	/**
	 * ��getKind()ΪԴ�����ʱ�򣬷���ָ���ģ���������ID.
	 * ����kindΪ���ù��̻�������Դ����ʱ�򣬱�ʾ���ð������͡�
	 * @return ��ָ��Դ�������͵�ʱ�򣬷�����ָ���ģ���������ID
	 */
	String getType();
}
