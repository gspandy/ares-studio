/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.impl;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;

import com.hundsun.ares.studio.jres.script.util.IScriptSysUtil;

/**
 * @author lvgao
 *
 */
public class ScriptSysUtilImpl  implements IScriptSysUtil{

	public static ScriptSysUtilImpl instance = new ScriptSysUtilImpl();
	
	/**
	 * ��ȡ��ѡ��������ļ�·��
	 * 
	 */
	public static final String FILE_OUTPUT_LOCATION = "com.hundsun.ares.studio.preference.fileoutputlocation";
	
	public static final String FILE_OUTPUT_CHARSET = "com.hundsun.ares.studio.preference.charset";
	
	/**
	 * ��ȡ��ѡ���Ƿ���֤ȯ����
	 * 
	 */
	public static final String IS_STOCK = "jres_application_department";
	

	@Override
	public Object getConfig(String id) {
		if (StringUtils.equals(id, FILE_OUTPUT_LOCATION)) {
			id = "com.hundsun.ares.studio.ui.jres_gen_path";
		}else if(StringUtils.equals(id, FILE_OUTPUT_CHARSET)){
			id = "com.hundsun.ares.studio.ui.jres_gen_charset";
		}

		int index = StringUtils.lastIndexOf(id, '.');
		if(index <= 0 || index == id.length()){
			return null;
		}
		
		String qualifer = StringUtils.substring(id,0, index);
		String key = StringUtils.substring(id,index + 1, id.length());
		String text = Platform.getPreferencesService().getString(qualifer, key, "", null);
		return text;
	}

	@Override
	public Object get(String id) {
		if(KEY_USER_NAME.equals(id)){
			return System.getProperty("user.name");
		}
		return getConfig(id);
	}

	/**
	 * ����ʹ�ò����Ƿ�Ϊ֤������
	 */
	public static boolean isStockDepartment() {
		String id = "com.hundsun.ares.studio.jres.ui.jres_application_department";
		return StringUtils.equals("stock" ,ScriptSysUtilImpl.instance.getConfig(id).toString());
	}
	
}
