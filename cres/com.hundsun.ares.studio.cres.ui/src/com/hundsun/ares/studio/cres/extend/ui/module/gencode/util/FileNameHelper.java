/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.extend.ui.module.gencode.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author liaogc
 *
 */
public class FileNameHelper {
	/**
	 * ȥ��Ŀ¼�����ļ��в��Ϸ����ַ�
	 * @param name
	 * @return
	 */
	public static String legalFileOrDirName(String name) {
		String illegalStr = "\\/:*?\"<>|";
		char []illegalArray = illegalStr.toCharArray();
		String newName = name;
		for(int i=0;i<illegalArray.length;i++){
			newName = StringUtils.replace(newName, illegalArray[i]+"", "");
		}
		return newName;
	}
}
