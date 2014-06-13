/**
 * Դ�������ƣ�NameSorter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;

/**
 * @author liaogc
 *
 */
public class NameSorter implements Comparator<Object>{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object o1, Object o2) {
		char[] array1 =new char[0];
		char[] array2 = new char[0];
		if(o1 instanceof String && o1 instanceof String){
			 array1 = StringUtils.defaultIfBlank((String)o1, "").trim().toLowerCase().toCharArray();
			 array2 = StringUtils.defaultIfBlank((String)o2, "").trim().toLowerCase().toCharArray();
		}
		else if(o1 instanceof BusinessDataType && o1 instanceof BusinessDataType){

			 array1 = StringUtils.defaultIfBlank(((BusinessDataType)o1).getName(), "").trim().toLowerCase().toCharArray();
			 array2 = StringUtils.defaultIfBlank(((BusinessDataType)o2).getName(), "").trim().toLowerCase().toCharArray();
		
		}else if(o1 instanceof StandardField && o1 instanceof StandardField){

			array1 = StringUtils.defaultIfBlank(((StandardField)o1).getName(), "").trim().toLowerCase().toCharArray();
			array2 = StringUtils.defaultIfBlank(((StandardField)o2).getName(), "").trim().toLowerCase().toCharArray();
		}
		
		int length = array1.length<array2.length?array1.length:array2.length;
		for(int i=0;i<length;i++){
			char a1 = array1[i];
			char a2 = array2[i];
			if(((int)a1)!=((int)a2)){
				return (int)a1-(int)a2;
			}
		}
		
		return array1.length-array2.length ;
		
	}

	

	

}
