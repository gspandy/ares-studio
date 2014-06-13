/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.script.api.biz.IBizScriptWrap;
import com.hundsun.ares.studio.jres.script.api.biz.IObjScriptWrap;


/**
 * ARES���ܶ���,��ֱ�ӻ��߼�ӵĻ�ȡ�Ķ����µ�������Ϣ
 * 
 * @author lvgao
 *
 */
public interface IARESProjectScriptWrap extends IMetadataScriptWrap,IBasicdataScriptWrap,IDatabaseScriptWrap,
												IBusinessScriptWrap, IObjScriptWrap, IBizScriptWrap,IProcedureScriptWrap,
												ILogicScriptWrap,IGenCodeScriptWrap,IScriptCallScriptWarp,IGenCode4UFTScriptWrap,IScriptCmdBuilderWarp{
	
	/**
	 * ��ȡ���̵ľ���·��
	 * <p>���ظ�ʽ�磺D:\workspace\helloworld\</p>
	 * @return 
	 */
	public String getProjectPath();
	
	/**
	 * ��ȡARESProject����
	 * 
	 * @return
	 */
	public IARESProject getARESProject();
	
	/**
	 * ��ȡ���е���ϵͳ
	 * 
	 * @return
	 */
	public String[] getAllSubsys();
	
	/**
	 * ��ȡָ����ģ�����������е���ϵͳ��������ģ��; ����rootTypeΪģ���������Id������ֵ���Բο���ĿĿ¼�µ�
	 * .respath�ļ�;<br>
	 * Ĭ��ģ�飨������Ϊ���ַ��������ģ�������Ϊ��ϵͳ����
	 * @param root
	 * @return
	 */
	public IARESModuleWrap[] getSubSystems(String rootType);
	
	/**
	 * �������ͻ�ȡģ����� �����ҵ��ĵ�һ��ָ�����͵�ģ���; �Ҳ���������null
	 * @param type <!-- �������Կ������getRootByName()��������ȡ���� -->
	 * @return
	 */
	IARESModuleRootWrap getRoot(String type);
}
