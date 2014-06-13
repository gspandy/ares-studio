/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import java.util.Arrays;
import java.util.List;

import com.hundsun.ares.studio.internal.core.ARESBundleInfo;

/**
 * �½ӿ�IARESBundleInfo, �������ʵ�ֺ��Ͻӿڱ��ּ��ݡ�
 * @author sundl
 */
public class BasicReferencedLibInfo extends ARESBundleInfo implements IBasicReferencedLibInfo {

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.IBasicReferencedLibInfo#getDependencyDescriptors()
	 */
	@Override
	public List<IDependenceDescriptor> getDependencyDescriptors() {
		return Arrays.asList(getDependencies());
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.IBasicReferencedLibInfo#getPublishTime()
	 */
	@Override
	public String getPublishTime() {
		return getPubTime();
	}
	
	public void setPublishTime(String time) {
		setPubTime(time);
	}
	
}
