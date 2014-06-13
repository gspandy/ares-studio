/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.model.database.oracle.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleRefType;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author qinyuan
 *
 */
public class OracleDataBaseUtil {
	
	/**
	 * ��ȡ���ݿ������
	 * @param project ����
	 * @param unique �Ƿ�ȥ���ظ���
	 * @return
	 */
	public static String[] getDataBaseName(IARESProject project,boolean unique) {
		if(null == project) {
			return new String[0];
		}
		List<String> list = new ArrayList<String>();
		List<ReferenceInfo> referenceInfos = ReferenceManager.getInstance().getReferenceInfos(project, IOracleRefType.Space, false);
		for(ReferenceInfo info : referenceInfos){
			Object obj = info.getObject();
			if (obj instanceof TableSpace && StringUtils.isNotBlank(((TableSpace) obj).getLogicName())) {
				String name = ((TableSpace) obj).getLogicName();
				if(unique){
					if(!list.contains(name)){
						list.add(name);
					}
				}else {
					list.add(name);
				}
			}
		}
		//����
		Collections.sort(list);
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * �����߼�����ȡ��ռ�
	 * @param project ����
	 * @param name ��ռ��߼���
	 * @return
	 */
	public static TableSpace getTableSpaceByLogicName(IARESProject project,String name) {
		List<ReferenceInfo> referenceInfos = ReferenceManager.getInstance().getReferenceInfos(project, IOracleRefType.Space, false);
		for(ReferenceInfo info : referenceInfos){
			Object obj = info.getObject();
			if (obj instanceof TableSpace && StringUtils.equalsIgnoreCase(name,((TableSpace) obj).getLogicName())) {
				return (TableSpace) obj;
			}
		}
		return null;
	}
	

	/**
	 * �����߼�����ȡ��ռ�
	 * @param project ����
	 * @param name ��ռ���
	 * @return
	 */
	public static TableSpace getTableSpaceByName(IARESProject project,String name) {
		List<ReferenceInfo> referenceInfos = ReferenceManager.getInstance().getReferenceInfos(project, IOracleRefType.Space, false);
		for(ReferenceInfo info : referenceInfos){
			Object obj = info.getObject();
			if (obj instanceof TableSpace && StringUtils.equalsIgnoreCase(name,((TableSpace) obj).getName())) {
				return (TableSpace) obj;
			}
		}
		return null;
	}

}
