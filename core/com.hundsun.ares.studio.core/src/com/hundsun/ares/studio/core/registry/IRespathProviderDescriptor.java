/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import com.hundsun.ares.studio.core.IRespathProvider;

/**
 * ����Res-path Provider.
 * @author sundl
 */
public interface IRespathProviderDescriptor extends ICommonDescriptor {

	IRespathProvider getProvider();
}
