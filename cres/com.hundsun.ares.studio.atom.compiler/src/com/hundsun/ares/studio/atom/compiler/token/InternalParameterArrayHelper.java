/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.token;

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
	 * �жϲ����Ƿ����ڲ������е��������
	 * @param parameter
	 * @param internalParameter
	 * @return
	 */
	public static boolean  isArrayParameter(Parameter parameter,List<InternalParam> internalParameter){
		if(internalParameter.contains(parameter)){
			String type = parameter.getType();
			int index1 = StringUtils.indexOf(type, "[");
			int index2 = StringUtils.indexOf(type, "]");
			if(index2>index1 && StringUtils.endsWith(type, "]")){
				return true;
			}
		}
		return false;
	}
	
	public static Parameter  getInternalParameter(String paramName,List<InternalParam> internalParameter){
		for(Parameter parameter:internalParameter){
			if(StringUtils.equals(parameter.getId(), paramName)){
				return parameter;
			}
		}
		return null;
	}
	/**
	 * ����ڲ�����ҵ����������
	 * @param parameter
	 * @return
	 */
	public static String getArrayBusType(Parameter parameter){
		String type = parameter.getType();
		int index1 = StringUtils.indexOf(type, "[");
		int index2 = StringUtils.indexOf(type, "]");
		if(index2>index1 && StringUtils.endsWith(type, "]")){
			return StringUtils.substring(type, 0, index1);
		}
		return StringUtils.EMPTY;
	}
	/**
	 * ���ҵ����������һά����
	 * @param parameter
	 * @return
	 */
	public static String getArrayLength(Parameter parameter){
		String type = parameter.getType();
		int index1 = StringUtils.indexOf(type, "[");
		int index2 = StringUtils.indexOf(type, "]");
		if(index2>index1 && StringUtils.endsWith(type, "]")){
			return StringUtils.substring(type, index1+1, index2);
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * ������ݵ���ʵ����
	 * @param type
	 * @return
	 */
	public static String getArrayDataType(String type){
		int index1 = StringUtils.indexOf(type, "[");
		int index2 = StringUtils.indexOf(type, "]");
		if(index2>index1 && StringUtils.endsWith(type, "]")){
			return StringUtils.substring(type, 0, index1);
		}
		return type;
	}
}
