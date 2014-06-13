/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhuyf
 *
 */
public class KeyWordEntity {
	
	private String name;//ԭ�ֶ���
	private String type;//���ݿ�����
	private String escape;//ת���ֶ���
	
	public KeyWordEntity(String name,String type,String escape){
		this.name = name;
		this.type = type;
		this.escape = escape;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the escape
	 */
	public String getEscape() {
		return escape;
	}
	
	

}
