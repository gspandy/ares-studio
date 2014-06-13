/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.constants;

import java.util.HashMap;

/**
 * @author zhuyf
 *
 */
public class AtomResourceMapping {
	
private static HashMap<String, String> typeMap = new HashMap<String, String>();
	
	static{
		typeMap.put(IAtomResType.ATOM_FUNCTION, IAtomRefType.ATOM_FUNCTION_CNAME);
		typeMap.put(IAtomResType.ATOM_SERVICE, IAtomRefType.ATOM_SERVICE_CNAME);
	}
	
	/**
	 * ��ȡ����������
	 * @param type
	 * @return
	 */
	public static String getCNameType(String type){
		return typeMap.get(type);
	}

}
