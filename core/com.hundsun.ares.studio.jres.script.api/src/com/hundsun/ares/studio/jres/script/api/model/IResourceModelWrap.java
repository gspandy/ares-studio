/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.model;

import com.hundsun.ares.studio.jres.script.api.wrap.IARESModuleWrap;



/**
 * @author lvgao
 *
 */
public interface IResourceModelWrap extends IScriptModelWrap{

	/**
	 * ��Դ�����Ķ���ģ�飬Ҳ����ϵͳ
	 * @return
	 */
	IARESModuleWrap getTopModule();
	
	/**
	 * ��ȡ��Դȫ����������ģ����ǰ׺�ģ�����
	 * com.hundsun.ares.User
	 * @return
	 */
	public String getFullyQualifiedName();

	/**
	 * ת���ɸ����ɱ༭ģʽ������д����Ϣ,�޸�����֮ǰ�������
	 * 
	 */
	public void becomeWorkingCopy();
	
	/**
	 * ������Դ
	 * ��Ҫ���ڣ�д����Դ��Ϣ�󣬱������
	 * 
	 */
	public void save();
	
}
