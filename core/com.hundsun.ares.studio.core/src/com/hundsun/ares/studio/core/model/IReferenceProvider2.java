/**
 * Դ�������ƣ�IReferenceProvider2.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model;

import java.util.List;

import com.hundsun.ares.studio.core.IARESProject;


/**
 * ��������£�Reference�ķ���ֵ����Project�й�ϵ�ģ���������IARESProject����Ⱦģ�ͣ�
 * ���Դ�������ӿڣ�����adapterģʽ����һ��
 * @author sundl
 */
public interface IReferenceProvider2 {

	/**
	 * ��������Ķ����Reference
	 * @param obj
	 * @param aresProject   ������һ�������ģ�˵����ǰ���Ǹ���Ŀ�н���
	 * @return
	 */
	public List<Reference> getReferences(Object obj, IARESProject aresProject);
}
