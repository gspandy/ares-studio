/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import com.hundsun.ares.studio.core.IARESModuleRoot;

/**
 * ģ���ע����Ϣ��������
 * @author sundl
 */
public interface IModuleRootDescriptor extends ICommonDescriptor, IOrderable{

	public static final int MODULE_LEVEL_NO_CONSTRAINT = -1;
	
	/**
	 * �Ƿ���ʾĬ��ģ��
	 * @return
	 */
	boolean useDefaultModule();
	
	/**
	 * �����ģ��㼶��Ĭ�ϲ��޲㼶��
	 * @return
	 */
	int getMaxModuleLevel();
	
	/**
	 * �Ƿ����ɾ����
	 * @return
	 */
	boolean isDeletable();
	
	/**
	 * �ɷ�������
	 * @return
	 */
	boolean isRenameable();
	
	IARESModuleRoot createRoot();

	public boolean useProperty();
	
}
