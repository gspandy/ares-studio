/**
 * Դ�������ƣ�PseudoCodeReference.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.core.model.impl;

import java.util.Map;

import com.hundsun.ares.studio.core.model.Reference;

/**
 * ������������������,���ع�ʱ�������������ع�
 * @author liaogc
 */
public interface ConditionReference extends Reference{
	boolean canDo(Map<Object,Object> parameters);

}
