/**
 * Դ�������ƣ�BizParameterUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�qinyuan
 */
package com.hundsun.ares.studio.biz.util;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.Parameter;

/**
 * ����ӿ������������������
 * 
 * ��Ҫ�ж�һ���ֶ��Ƿ����������������
 * @author qinyuan
 */
public class BizParameterUtil {
	
	public static final String FLAG_IO = "IO";
	
	/**
	 * �Ƿ�Ϊ�������
	 * @param biz ����ӿ���Դ
	 * @param field �ֶ���
	 * @return
	 */
	public static boolean isInputParameter(BizInterface biz, String field){
		for (Parameter p : biz.getInputParameters()) {
			if(StringUtils.equals(p.getId(), field)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �Ƿ�Ϊ�������
	 * @param biz ����ӿ���Դ
	 * @param field �ֶ���
	 * @return
	 */
	public static boolean isOutputParameter(BizInterface biz, String field){
		for (Parameter p : biz.getOutputParameters()) {
			if(StringUtils.equals(p.getId(), field)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �Ƿ�Ϊ��'IO'��־���������
	 * @param biz ����ӿ���Դ
	 * @param field �ֶ���
	 * @return
	 */
	public static boolean isOutputParameterWithIO(BizInterface biz, String field){
		for (Parameter p : biz.getOutputParameters()) {
			if(StringUtils.equals(p.getId(), field) && 
					StringUtils.equalsIgnoreCase(p.getFlags(), FLAG_IO)){
				return true;
			}
		}
		return false;
	}

}
