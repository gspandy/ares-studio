/**
 * Դ�������ƣ�EnumToStringConventor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import org.eclipse.core.databinding.conversion.IConverter;

public class EnumToStringConventor implements IConverter{

	@Override
	public Object getFromType() {
		return null;
	}

	@Override
	public Object getToType() {
		return null;
	}

	@Override
	public Object convert(Object fromObject) {
		return fromObject.toString();
	}

}
