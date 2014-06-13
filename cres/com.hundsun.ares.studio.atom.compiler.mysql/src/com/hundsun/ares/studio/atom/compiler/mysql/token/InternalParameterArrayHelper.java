/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.mysql.token;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.InternalParam;
import com.hundsun.ares.studio.biz.Parameter;

/**�ڲ�����(����)������
 * @author liaogc
 *
 */
public class InternalParameterArrayHelper {
	
	/**
	 * �ж��ڲ��������������Ƿ�Ϊ����
	 * @param var �ڲ�����
	 * @return boolean
	 */
	public static boolean  isArrayParameter(InternalParam var){
			String type = var.getType();
			int index1 = StringUtils.indexOf(type, "[");
			int index2 = StringUtils.indexOf(type, "]");
			if(index2>index1 && StringUtils.endsWith(type, "]")){
				return true;
			}
		return false;
	}
	
	/**
	 * ����ڲ�����ҵ�����ͣ�ȥ�����鳤�ȣ�
	 * @param var �ڲ�����
	 * @return String ҵ������
	 */
	public static String getArrayBusType(InternalParam var){
		String type = var.getType();
		int index1 = StringUtils.indexOf(type, "[");
		int index2 = StringUtils.indexOf(type, "]");
		if(index2>index1 && StringUtils.endsWith(type, "]")){
			return StringUtils.substring(type, 0, index1);
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ����ڲ�����ҵ�����͵����鳤��
	 * @param var
	 * @return
	 */
	public static String getArrayLength(InternalParam var){
		String type = var.getType();
		int index1 = StringUtils.indexOf(type, "[");
		int index2 = StringUtils.indexOf(type, "]");
		if(index2>index1 && StringUtils.endsWith(type, "]")){
			return StringUtils.substring(type, index1+1, index2);
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ���ҵ�����ͣ�ȥ�����鳤�ȣ�
	 * @param bizType ҵ�����ͣ������鳤�ȣ�
	 * @return String ҵ������
	 */
	public static String getArrayDataType(String bizType){
		int index1 = StringUtils.indexOf(bizType, "[");
		int index2 = StringUtils.indexOf(bizType, "]");
		if(index2>index1 && StringUtils.endsWith(bizType, "]")){
			return StringUtils.substring(bizType, 0, index1);
		}
		return bizType;
	}
}
