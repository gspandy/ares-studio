/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util;


/**
 * @author lvgao
 *
 */
public interface IScriptFileUtil {

	
	/**
	 * д���ļ�
	 * @param filepath
	 * 		�ļ�·�������������·�������·��,��������ھʹ���
	 * @param content
	 * 		�ļ�����
	 * @param charsetName
	 * 		�����ʽ
	 * @return
	 */
	public  boolean write(String filepath, String content, String charsetName );
	/**
	 * ���ص�ǰ��ǰ���ļ�����·��
	 * @return
	 */
	public String getAbsolutePath();
	
}
