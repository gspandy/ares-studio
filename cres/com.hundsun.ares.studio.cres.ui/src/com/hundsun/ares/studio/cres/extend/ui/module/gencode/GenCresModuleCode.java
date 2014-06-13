/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.extend.ui.module.gencode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.engin.logic.ResourceEngin;

/**
 * @author qinyuan
 *
 */
public abstract class GenCresModuleCode implements IGenCresModuleCode{

	private static final Logger logger = Logger.getLogger(GenCresModuleCode.class);
	
	/**
	 * �Ƿ�����ǰ�ô���
	 */
	protected boolean isHeadCode = false;
	/**
	 * �Ƿ����ɺ��ô���
	 */
	protected boolean isEndCode = false;
	
	public void setIsHeadCode(boolean isHeadCode){
		this.isHeadCode = isHeadCode;
	}
	
	public void setIsEndCode(boolean isEndCode){
		this.isEndCode = isEndCode;
	}
	
	protected boolean isHeadCode(){
		return isHeadCode;
	}
	
	protected boolean isEndCode(){
		return isEndCode;
	}
	
	//��¼������Ϣ
	protected StringBuffer errLog;
	
	protected Map<String, StringBuffer> resCodeCache = new HashMap<String, StringBuffer>();//������̴���,keyΪ�����̶����&&����ȫ����
	
	/**
	 * ��ջ���
	 */
	public void clearCache() {
		resCodeCache.clear();
	}
	
	/**
	 * ��ȡģ����
	 * ���ģ���Ӣ����Ϊ�Զ����ɵģ��硰m+[z������hash��]�����򷵻�ģ����������
	 * ���򷵻�ģ��Ӣ����
	 * @param module
	 * @return
	 */
	protected String getModuleNameFromAutoCreateModuleName(IARESModule module) {
		String name = module.getShortName();
		Pattern p = Pattern.compile("m\\d+");
		Matcher m = p.matcher(name);
		if(m.matches()) {
			try {
				return ModuleGeneratorHelper.getModuleProperty(module).getString(ICommonModel.CNAME);
			} catch (Exception e) {
				e.printStackTrace();
				return name;
			}
		}else {
			return name;
		}
		
	}

	/**
	 * @param errLog the errLog to set
	 */
	public void setErrLog(StringBuffer errLog) {
		this.errLog = errLog;
	}
	
	/**
	 * ��ȡ�������ɵ�������
	 * @param project
	 * @return
	 * 	Ĭ�Ϸ���һ���յ�������
	 */
	public Map<Object, Object> getContext(IARESProject project) {
		return new HashMap<Object, Object>();
	}

	
	/**
	 * ���������ļ�
	 * @param gccName �����ļ���
	 * @param path �ļ�����·��
	 * @param isWithPath ���ɴ����Ƿ��Ŀ¼
	 */
	protected void createMakeOrderFile(String gccName, String path,boolean isWithPath,String charset) {
		if(isWithPath){//��Ŀ¼����Ҫ����
			String fileName = path + "makeall";//makefile��Ϊmakeall
			StringBuffer content = new StringBuffer();
			content.append("make -f " + gccName + " ORA_VER=10");//-f��Ҫ���ո񣬷�����Ҳ���Ŀ������GCC�ļ�
			
			//д���ļ�
			writeToFile(fileName, content.toString(),charset);
			
			String fileName2 = path + "makeclean";//makefile��Ϊmakeall
			StringBuffer content2 = new StringBuffer();
			content2.append("make -f " + gccName + " ORA_VER=10 clean");//-f
			//д���ļ�
			writeToFile(fileName2, content2.toString(),charset);
			
		}
	}
	
	/**
	 * �����ļ�Ŀ¼
	 * @param path 
	 */
	protected void createFilePath(String path) {
		File codeFolder = new File(path);
		if (!codeFolder.exists()) {
			codeFolder.mkdirs();
		}
	}
	
	/**
	 * ���ַ�����д��ָ���ļ�
	 * @param fileName
	 * @param contant
	 */
	protected void writeToFile(String fileName,String contant,String charset) {
		charset = StringUtils.defaultString(charset, "GB2312");
		try {
			FileUtils.writeStringToFile(new File(fileName), contant, charset);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/**
	 * @param msgQueue
	 * @param errorLogFileName
	 * @param path 
	 * @param charset 
	 */
	protected void createErrLog(Queue<IARESProblem> msgQueue,
			String errorLogFileName, String path,String charset) {
		StringBuffer errBuffer = new StringBuffer();
		if(msgQueue.size() > 0){
			
			errBuffer.append("/***" + "\r\n");
			errBuffer.append("������ʾ��Ϣ��\r\n");
			for(IARESProblem pItem: msgQueue){
				errBuffer.append(ResourceEngin.instance.getCodeErrorMessage(pItem));
				errBuffer.append("\r\n");
			}
			errBuffer.append("***/" + "\r\n");
			writeToFile(path + errorLogFileName, errBuffer.toString(),charset);
			
			if(errLog != null){
				errLog.append(errorLogFileName + "�ļ�:\n");
				errLog.append(errBuffer);
				errLog.append("\n\n");
			}
		}else{
			//û�д�����Ϣ��ɾ����־�ļ�
			File errorLogFile = new File(path + errorLogFileName);
			if(errorLogFile.exists()){
				errorLogFile.delete();
			}
		}
	}
}
