/**
 * Դ�������ƣ�LanguageRegister.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

/**
 * @author gongyf
 *
 */
public class LanguageRegister {
	
	private Language[] languages;
	
	private static LanguageRegister instance;
	
	private LanguageRegister() {
		
	}
	
	public static LanguageRegister getInstance() {
		if (instance == null) {
			instance = new LanguageRegister();
		}
		return instance;
	}
	
	
	public Language[] getRegisteredLanguages() {
		/*
		 * DESIGN#Ԫ������չ#��Ҷ��#��ͨ#��Ԫ#��ȡ������չ����Ϣ
		 *
		 * ����չ���ȡĿǰע����������ͣ�����Ϣ��Ҫ���л���
		 * ��չ�����ݲ��ᶯ̬�仯
		 */
		if (languages == null) {
			IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(ExtensionPointConst.Languages.NAME);
			if (extension != null) {
				List<Language> languageList = new ArrayList<Language>();
				
				// ��ȡ�����ӽڵ�
				IConfigurationElement[] elements = extension.getConfigurationElements();
				if (elements != null) {
					for (IConfigurationElement element : elements) {
						if (ExtensionPointConst.Languages.Language.NAME.equals(element.getName())) {
							Language l = new Language(
									element.getAttribute(ExtensionPointConst.Languages.Language.ATTR_ID), 
									element.getAttribute(ExtensionPointConst.Languages.Language.ATTR_NAME));
							
							languageList.add(l);
							
						}
					}
				}
				languages = languageList.toArray(new Language[languageList.size()]);
			}
			
			
		}
		return languages;
	}
	
	public Language getLanguageByName(String name) {
		for (Language lang : getRegisteredLanguages()) {
			if (StringUtils.equals(lang.getName(), name))
				return lang;
		}
		return null;
	}
}
