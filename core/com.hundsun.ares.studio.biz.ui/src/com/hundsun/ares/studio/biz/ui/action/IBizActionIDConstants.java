/**
 * Դ�������ƣ�IBizActionIDConstants.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.action;

import com.hundsun.ares.studio.ui.editor.actions.IActionIDConstant;

/**
 * ҵ���߼�����Action ID��������
 * @author sundl
 *
 */
public interface IBizActionIDConstants extends IActionIDConstant{
	
	/** ��ӱ�׼�ֶβ��� Action */
	String ADD_PARAM_STD_FIELD = CV_ADD + "std";
	/** ��Ӳ������Action */
	String ADD_PARAM_GROUP = CV_ADD + ".paramGroup";
	/** ��Ӷ������Ͳ��� */
	String ADD_OBJECT_PARAM = CV_ADD + ".objparm";
	/** ��ӷǱ�׼�ֶβ��� */
	String ADD_NON_STD_FIELD_PARME = CV_ADD + ".non-stdfield-parma";
	/** ��ӵ���׼�ֶ� */
	String ADD_TO_STD_FIELD = CV_ADD + ".add-to-std-field";
	/** ������ */
	String ADD_PARAM_COMPONENT = CV_ADD + ".component";
}
