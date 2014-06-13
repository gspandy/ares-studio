/**
 * Դ�������ƣ�ParserContext.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sundl
 *
 */
public class ParserContext {

	private Map<String, Object> context = new HashMap<String, Object>();
	
	public Object get(String key) {
		return context.get(key);
	}
	
	public void put(String key, Object value) {
		this.context.put(key, value);
	}
	
}
