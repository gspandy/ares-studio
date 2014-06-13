/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.api.factory;

import java.util.Map;

/**
 * ���ڽű���ģ�ͷ�װ
 * 
 * @author lvgao
 */
public interface IScriptPoxyFactory {

	/**
	 * ��װ����
	 * 
	 * @param input
	 * @param context
	 * @return
	 */
	public Object createPoxy(Object input,Map<Object, Object> context);
	
	/**
	 * ��ȡģ���װ�Ĵ���ʵ��
	 * @param input һ���Ƕ���
	 * @return
	 */
	public Object createModuleProxy(Object input);
}
