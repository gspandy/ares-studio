/**
 * Դ�������ƣ�ProjectSettingUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;

/**
 * @author zhuyf
 *
 */
public class ProjectSettingUtil {
	
	static Logger logger = Logger.getLogger(ProjectSettingUtil.class);
	
	public static final String MYSQL = "mysql";
	
	public static final String ORACLE = "oracle";
	
	/**
	 * 
	 * @param project
	 * @return
	 */
	public static String getDatabaseType(IARESProject project){
		String databaseType = "oracle";
		try {
			databaseType =  project.getProjectProperty().getString("tabledir");
			int _index = -1 ;
			int dotIndex = -1;
			if((_index=StringUtils.lastIndexOf(databaseType,"_" ))>-1  && (dotIndex=StringUtils.lastIndexOf(databaseType,"."))>-1 ){
				databaseType = StringUtils.substring(databaseType,_index+1, dotIndex).toLowerCase();
			}else{
				databaseType = "oracle";
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("��Ŀ����/ϵͳ����/���ݱ�ű�������Ϣ������Ҫ���޷�����������ԭ��" + e.getMessage());
		}
		return databaseType;
	}

}
