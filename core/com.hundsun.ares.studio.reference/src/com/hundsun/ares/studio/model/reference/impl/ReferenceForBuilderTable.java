/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.model.reference.impl;

import java.util.HashMap;
import java.util.Map;

import com.hundsun.ares.studio.core.IARESProject;

/**
 * @author liaogc
 *
 */
public class ReferenceForBuilderTable {
	private Map<IARESProject, ReferencesForBuilderCollection> projects = new HashMap<IARESProject, ReferencesForBuilderCollection>();
	
	public Map<IARESProject, ReferencesForBuilderCollection> getProjects(){
		return projects;
	}
}
