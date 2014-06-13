/**
 * Դ�������ƣ�EMFKeyConstructor.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;

import com.hundsun.ares.studio.ui.validate.IKeyConstructor;
import com.hundsun.ares.studio.ui.validate.KeyParameter;

/**
 * @author gongyf
 *
 */
public class EMFKeyConstructor implements IKeyConstructor {

	private static Object NULL = new Object();
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IKeyConstructor#constructKey(java.lang.Object)
	 */
	@Override
	public KeyParameter constructKey(Object problem) {
		if (problem instanceof Diagnostic) {
			List<?> dataList = ((Diagnostic) problem).getData();
			if (dataList.size() >= 2) {
				return new KeyParameter(new Object[]{dataList.get(0), dataList.get(1)});
			}
			
		}
		return new KeyParameter(NULL);
	}

}
