/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.aresaction;

import com.hundsun.ares.studio.core.registry.ICommonDescriptor;

/**
 * ARES������ע��������Ϣ�ӿ�
 * @author sundl
 */
public interface IARESActionDescriptor extends ICommonDescriptor {
	
	String getResType();
	
	IARESAction createAction();
}
