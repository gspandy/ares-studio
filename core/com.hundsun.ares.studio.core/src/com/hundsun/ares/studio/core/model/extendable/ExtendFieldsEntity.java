package com.hundsun.ares.studio.core.model.extendable;

import java.util.ArrayList;
import java.util.List;


/**
 * һ����չ�ֶεļ���
 * һ���Ӧһ��ҳ���������չ�ֶ�
 * @author maxh
 *
 */
public class ExtendFieldsEntity {
	List<ExtendFieldsHeader> extendFields;
	
	public ExtendFieldsEntity() {
		extendFields = new ArrayList<ExtendFieldsHeader>();
	}
	
	public List<ExtendFieldsHeader> getExtendFields() {
		return extendFields;
	}
	
	public void setExtendFields(List<ExtendFieldsHeader> extendFields) {
		this.extendFields = extendFields;
	}
}
