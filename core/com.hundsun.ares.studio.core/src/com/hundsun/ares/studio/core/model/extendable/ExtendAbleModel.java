package com.hundsun.ares.studio.core.model.extendable;

import java.util.HashMap;
import java.util.Map;

/**
 * ����չ��ģ�ͻ���
 * ��һ��map��¼��չ��Ϣ
 * @author maxh
 *
 */
public abstract class ExtendAbleModel implements IExtendAbleModel {
	private Map<String,Object> extendObject;
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.extendable.IExtendAbleModel#getMap()
	 */
	public Map<String,Object> getMap() {
		if(extendObject == null){
			extendObject = new HashMap<String,Object>();
		}
		return extendObject;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ExtendAbleModel)) {
			return false;
		}
		ExtendAbleModel other = (ExtendAbleModel) obj;
		return getMap().equals(other.getMap());
	}
	
}
