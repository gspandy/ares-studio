/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.extend.ui.module.gencode;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;

/**
 * @author qinyuan
 *
 */
public interface IGenCresModuleCode {
	
	/**
	 * �����Ƿ����ǰ�ô���
	 * 
	 * @param isHeadCode
	 */
	public void setIsHeadCode(boolean isHeadCode);
	
	/**
	 * �����Ƿ����ǰ�ô���
	 * 
	 * @param isHeadCode
	 */
	public void setIsEndCode(boolean isEndCode);
	
	/**
	 * ��ջ���
	 */
	public void clearCache();
	
	/**
	 * ��ȡ�������ɵ�������
	 * @param project
	 * @return
	 * 	Ĭ�Ϸ���һ���յ�������
	 */
	public Map<Object, Object> getContext(IARESProject project);
	
	/**
	 * ����ģ�����
	 * @param module
	 * 		��Ҫ���������ģ��
	 * @param context
	 * 		������
	 * @param isWithPath
	 * 		�Ƿ��Ŀ¼
	 * @param isPathWithCname
	 * 		Ŀ¼�Ƿ�ʹ��������
	 * @param monitor
	 * 		������
	 */
	void genModuleCode(IARESModule module, Map<Object, Object> context, boolean isWithPath,boolean isPathWithCname,IProgressMonitor monitor);
	
	
	/**
	 * �ܷ�����
	 * @param module
	 * 		��Ҫ���������ģ��
	 * @return
	 */
	boolean canGenCode(IARESModule module);
	
	void setErrLog(StringBuffer errLog);
}