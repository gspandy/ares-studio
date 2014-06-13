/**
 * Դ�������ƣ�IRefExtendPropertyProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import java.util.Map;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.ExtensibleModel;

/**
 * 
 * @author sundl
 */
public interface IRefExtendPropertyProvider {
	void config(Map<String, String> config, IARESProject project);

	String getValue(ExtensibleModel model);
	void setValue(ExtensibleModel model, String value);
}
