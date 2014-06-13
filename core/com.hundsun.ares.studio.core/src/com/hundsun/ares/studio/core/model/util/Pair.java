/**
 * Դ�������ƣ�Pair.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.model.util;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * ������Ϊ�ں���2��ֵ�Ķ���
 * @author gongyf
 *
 */
public class Pair<F, S> {
	public final F first;
	public final S second;
	/**
	 * @param first
	 * @param second
	 */
	public Pair(F first, S second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Pair) {
			return ObjectUtils.equals(first, ((Pair) obj).first) 
				&& ObjectUtils.equals(second, ((Pair) obj).second);
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(first).append(second).toHashCode();
	}
}
