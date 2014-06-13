/**
 * Դ�������ƣ�ExtensibleModelUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.model.extendable;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.UserExtensibleProperty;

/**
 * @author sundl
 *
 */
public class ExtensibleModelUtil {
	
	public static Object convert(Object value, Class<?> toClassType) {
		if (value == null) {
			if (String.class.isAssignableFrom(toClassType)) {
				return StringUtils.EMPTY;
			} else if (Boolean.class.isAssignableFrom(toClassType)) {
				return Boolean.FALSE;
			} else if (Integer.class.isAssignableFrom(toClassType)) {
				return 0;
			}
		} else {
			if (String.class.isAssignableFrom(toClassType)) {
				return value.toString();
			} else if (Boolean.class.isAssignableFrom(toClassType) || toClassType.getName().equals("boolean")) {
				return BooleanUtils.toBoolean(value.toString());
			} else if (Integer.class.isAssignableFrom(toClassType)) {
				return NumberUtils.toInt(value.toString());
			}
		}
		return value;
	}
	
	/**
	 * ��ȡ�û���չ����
	 * @param model
	 * @param key
	 * @return
	 */
	public static String getUserExtendedProperty(ExtensibleModel model, String key) {
		UserExtensibleProperty userProperty = (UserExtensibleProperty) model.getData2().get(Constants.USER_DATA2_KEY);
		if (userProperty != null)
			return userProperty.getMap().get(key);
		return null;
	}
	
	public static void setUserExtendedProperty(ExtensibleModel model, String key, String value) {
		UserExtensibleProperty userProperty = (UserExtensibleProperty) model.getData2().get(Constants.USER_DATA2_KEY);
		if (userProperty != null)
			userProperty.getMap().put(key, value);
	}
	
}
