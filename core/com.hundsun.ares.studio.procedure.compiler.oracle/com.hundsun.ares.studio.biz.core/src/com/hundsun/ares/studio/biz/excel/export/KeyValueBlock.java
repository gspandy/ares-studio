/**
 * Դ�������ƣ�KeyValueBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.export;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sundl
 *
 */
public class KeyValueBlock extends Block {

	public class KeyValue {
		public String key;
		public String value;
		/** ָ���key-value��ռ��������λ��Ĭ��Ϊ1; �����2��Ч������keyռ��һ�����ӣ�valueռ����3�����ϲ���Ԫ�� */
		public int span = 1;
		
		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public KeyValue(String key, String value, int span) {
			this.key = key;
			this.value = value;
			this.span = span;
		}
	}
	
	public List<KeyValue> kvList = new ArrayList<KeyValueBlock.KeyValue>();
	
	public void addKeyValue(String key, String value) {
		this.kvList.add(new KeyValue(key, value));
	}
	
	public void addKeyValue(String key, String value, int span) {
		this.kvList.add(new KeyValue(key, value, span));
	}
	
}
