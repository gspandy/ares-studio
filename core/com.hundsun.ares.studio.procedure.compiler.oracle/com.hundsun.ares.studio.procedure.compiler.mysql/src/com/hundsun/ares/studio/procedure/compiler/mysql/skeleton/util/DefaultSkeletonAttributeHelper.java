/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.mysql.skeleton.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;

/**
 * @author qinyuan
 *
 */
public class DefaultSkeletonAttributeHelper implements ISkeletonAttributeHelper{

	/***ע�⣬����ʹ��LinkedHashSet��Ϊ�˱�֤������˳�򣬲�����ʱ��Ҫ˳��֤�ȶ����ʹ��**/
	Map<String, Set<String>> context = new HashMap<String, Set<String>>();
	
	@Override
	public void addAttribute(String key, String value) {
		if(!context.containsKey(key)){
			context.put(key, new LinkedHashSet<String>());
		}
		context.get(key).add(value);
	}

	@Override
	public Set<String> getAttribute(String key) {
		if(context.containsKey(key)){
			return context.get(key);
		}
		LinkedHashSet<String> hashSet = new LinkedHashSet<String>();
		context.put(key,hashSet);
		return hashSet;
	}

	@Override
	public void addAllAttribute(String key, Set<String> tset) {
		if(!context.containsKey(key)){
			context.put(key, new LinkedHashSet<String>());
		}
		context.get(key).addAll(tset);
	}

}
