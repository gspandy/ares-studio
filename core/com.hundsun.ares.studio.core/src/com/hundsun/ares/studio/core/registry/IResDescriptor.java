/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.registry;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.converter.IModelConverter;

/**
 * 
 * @author sundl
 */
public interface IResDescriptor extends ICommonDescriptor, IOrderable {
	String getFileName();
	String getId();
	String getCategory();
	IARESResource createCommonResource();
	Object createInfo();
	IModelConverter getConverter();
	boolean isCopyable();
	boolean isMoveable();
	boolean isDeleteable();
	boolean isRenameable();
}
