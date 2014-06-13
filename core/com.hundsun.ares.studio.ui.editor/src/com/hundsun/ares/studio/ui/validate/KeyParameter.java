/**
 * Դ�������ƣ�KeyParameter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.validate;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * �����ڴ�����е�key�������һ��map��key�������key�Ƕ��ص�<BR>
 * key(a)�����ڴ������ȡ��key(a,*) key(a,*,*) key(a,*,*,*)...��Ӧ�Ĵ����б�
 * 
 * @author lvgao
 *
 */
public class KeyParameter {
	
	final private Object[] key;
	private KeyParameter[] resolved;
	
	public KeyParameter(Object... key) {
		this.key = key;
	}
	
	/**
	 * ���ؼ�ֵ�����б���
	 * @return
	 */
	public KeyParameter[] resolve() {
		if (resolved == null) {
			resolved = new KeyParameter[key.length];
			for (int i = 0; i < key.length; i++) {
				resolved[i] = new KeyParameter(ArrayUtils.subarray(key, 0, i + 1));
			}
		}
		return resolved;
	}
	
	public boolean contains(KeyParameter key) {
		return ArrayUtils.contains(resolve(), key);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof KeyParameter)){
			return false;
		}
		
		return Arrays.equals(key, ((KeyParameter)obj).key);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(key).toHashCode();
	}

}
