/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.jres.script.ScriptPlugin;
import com.hundsun.ares.studio.jres.script.util.IScriptFileUtil;

/**
 * @author lvgao
 *
 */
public class ScriptFileUtilImpl  implements IScriptFileUtil{
	
	public static ScriptFileUtilImpl instance = new ScriptFileUtilImpl();
	public String absolutePath = StringUtils.EMPTY;
	

	static final Logger console = ConsoleHelper.getLogger();
	
	/**
	 * �����ļ���
	 * 
	 * @param fileName ��ȫ·�����ļ�����
	 * @param content �ļ����ݡ�
	 * @param charsetName �����ʽ��
	 * @param isisRecursive �Ƿ�ݹ鴴����Ŀ¼��
	 * 
	 * @return
	 */
	public static IStatus genFile(String fileName, String content, String errLogInfo, String charsetName, boolean isRecursive) {
		File file = createFile(fileName, isRecursive);
		if (file == null) {
			String message = "�ļ�����ʧ�ܣ�����·����" + fileName;
			return new Status(IStatus.ERROR, ScriptPlugin.PLUGIN_ID, message);
		}
		
		boolean result = writeToFile(file, content, charsetName);
		if (!result) {
			String message = "д�ļ�ʧ�ܣ��ļ�·����" + fileName;
			return new Status(IStatus.ERROR, ScriptPlugin.PLUGIN_ID, message);
		}
		return Status.OK_STATUS;
	}
	
	
	/**
	 * ��ָ���ı�д���ļ���
	 * 
	 * @param file ��д���ļ���
	 * @param content �ļ����ݡ�
	 * @param charsetName �����ʽ��
	 * 
	 * @return �Ƿ�д�ļ��ɹ���
	 */
	public static boolean writeToFile(File file, String content, String charsetName) {
		try {
			PrintWriter print = 
				new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName)));
			print.print(content);
			print.flush();
			print.close();
			console.warn(String.format("д�ļ��ɹ�:[%s]",file.getPath().toString()));
			return true;
		} catch (Exception e) {
			console.error(String.format("д�ļ���[%s]����ԭ��:[%s]",file.getPath().toString(),e.getMessage()), e);
		} 
		return false;
	}
	
	/**
	 * �����ļ�������ʧ�ܺ󷵻�null��
	 * 
	 * @param fileName ��ȫ·�����ļ�����
	 * @param isRecursive �Ƿ����ϵݹ鴴����
	 * @return
	 */
	public static File createFile(String fileName, boolean isRecursive) {
		File file = new File(fileName);
		if(!file.exists()) {
			try {
				if (isRecursive) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
				console.debug(String.format("�����ļ��ɹ�:[%s]",file.getPath().toString()));
			} catch (IOException e) {
				console.error(String.format("�����ļ���[%s]����:ԭ��[%s]",file.getPath().toString(),e.getMessage()));
				file = null;
			}
		}
		return file;
	}

	@Override
	public boolean write(String filepath, String content, String charsetName) {
		File file = createFile(filepath,true);
		if ( file != null ) {
			this.absolutePath = file.getAbsolutePath();
			return writeToFile(file,content,charsetName);
		}
		
		return false;
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.util.IScriptFileUtil#getAbsolutePath()
	 */
	@Override
	public String getAbsolutePath() {
		return this.absolutePath;
	}
	
}
