/**
 * Դ�������ƣ�UserExtendedPropertyUtils.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extend;

import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.UserExtensibleProperty;

/**
 * �û�������صĹ�����
 * @author sundl
 */
public class UserExtendedPropertyUtils {

	public static String getUserExtendedProperty(ExtensibleModel model, String key) {
		UserExtensibleProperty userProperties = (UserExtensibleProperty) model.getData2().get(Constants.USER_DATA2_KEY);
		return userProperties == null ? null : userProperties.getMap().get(key);
	}
	
}
