/**
 * Դ�������ƣ�IReferenceParser.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.model.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.model.Reference;

/**
 * ���ڽ���һ��ģ����һ���ֶλ����õ�������Ϣ�ķ�����<BR>
 * ������ֻ��ʵ����һ�Σ��ᱻ���ã����Բ�Ӧ�ñ����κη����й���Ϣ
 * @author gongyf
 *
 */
public interface IReferenceParser {
	
	/**
	 * ��Ҫ����null�����û�����ݣ��뷵��{@link Collections#emptyList()}
	 * @param object
	 * @param feature
	 * @param parameters
	 * @return
	 */
	List<Reference> analyse(EObject object, EStructuralFeature feature, String[] parameters);
}
