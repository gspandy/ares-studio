/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.text.assistant;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.usermacro.UserMacroItem;

/**
 * @author wangxh
 *
 */
public class UserMacroAssistantFilter implements IAssistantFilter {
	
	/**���ͣ�ֻ�ܴ����¼���ѡȡ*/
	private String type = "";
	
	public static final String ATOM_TYPE = "ԭ��";
	public static final String LOGIC_TYPE = "�߼�";
	public static final String PROCEDURE_TYPE = "����";
	
	public UserMacroAssistantFilter(String type){
		this.type = type;
	}
	
	@Override
	public boolean filter(Object obj) {
		if(obj instanceof UserMacroItem){
			UserMacroItem item = (UserMacroItem)obj;
			if(StringUtils.contains(item.getType(),type)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void init() {
	}

}
