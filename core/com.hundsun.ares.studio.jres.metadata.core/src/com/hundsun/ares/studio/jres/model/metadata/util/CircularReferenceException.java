/**
 * Դ�������ƣ�CircularReferenceException.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.core
 * ����˵����Ԫ����ģ�Ͷ��塢���������
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.model.metadata.util;

/**
 * @author gongyf
 *
 */
public class CircularReferenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4538139916507993100L;

	/**
	 * 
	 */
	public CircularReferenceException() {
		super();
	}

	/**
	 * @param message
	 */
	public CircularReferenceException(String message) {
		super(message);
	}

	
}
