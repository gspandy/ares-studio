/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.basicdata.core.basicdata.validate.util;

import org.eclipse.emf.ecore.EObject;

/**
 * @author lvgao
 *
 */
public interface IKeyTableLocator {

	/**
	 * ��ȡ��������
	 * @param obj
	 * @return
	 */
	public EObject getLinkObject(EObject obj) throws Exception;
	
	
	/**
	 * ��ȡ�����������
	 * @param obj
	 * @return
	 */
	public int getLinkObjectCount(EObject obj)throws Exception;
	
	/**
	 * ����
	 */
	public void reset();
	
	/**
	 * �����Ƿ�׼����
	 * @return
	 */
	public boolean isReady();
}
