/**
 * Դ�������ƣ�JRESContextManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESBundle;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.core.model.util.Pair;
import com.hundsun.ares.studio.core.util.ResourcesUtil;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author sundl
 * @Deprecated ʹ��ReferenceManager����صķ�������
 */
@Deprecated 
public class JRESContextManager{
//	private static Map<IARESProject, StatisticProviderProxy> statisticMap = new HashMap<IARESProject, StatisticProviderProxy>();
	
	
	/**
	 * ��ȡͳ����Ϣ�ṩ��
	 * �������ù���
	 * @param project
	 * @return
	 * @Deprecated ʹ��ReferenceManager
	 */
	@Deprecated
	public static IResStatisticProvider getStatisticProvider(IARESProject project){
		return new JRESStatisticProvider(project, true);
	}
	
//	/**
//	 * ���ָ��project�Ĵ���proxy,��ִ�д���Ļ��������������Ҫ��ɾ���䴴����View��
//	 * @param aresProject
//	 */
//	public static void removeStatisticProvider(IARESProject aresProject) {
//		StatisticProviderProxy proxy = statisticMap.get(aresProject);
//		if (proxy != null) {
//			proxy.dispose();
//		}
//		statisticMap.remove(aresProject);
//	}
	
	/**
	 * ��ȡͳ����Ϣ�ṩ��
	 * @param project
	 * @return
	 */
	public static IResStatisticProvider getCurrentStatisticProvider(IARESProject project){
		return new JRESStatisticProvider(project, false);
	}
	
	/**
	 * ��ȡ����������
	 * @param project
	 * @return
	 */
	public static IResReferenceProvider getReferencProvider(IARESProject project){
		return new JRESReferencProvider(project);
	}
	
	/**
	 * ����һ������
	 * @param bundle
	 * @param name
	 * @param type
	 * @return
	 * @Deprecated ʹ��ReferenceManager��صĽӿڴ���
	 */
	@Deprecated
	public static <T extends EObject> Pair<T , IARESResource> findResource(IARESBundle bundle, String name, String type, boolean useNameSpace) {
		IARESProject project = ResourcesUtil.getARESProject(bundle);
		
		String nameSpace = useNameSpace ? bundle.getInfo().getId() : IResourceTable.Scope_IGNORE_NAMESPACE;
		// FIXME JRESĿǰ��ʹ�������ռ�
		List<ReferenceInfo> infoListList =  ReferenceManager.getInstance().getReferenceInfos(project, type, name, true);
		if (infoListList!=null && infoListList.size()>0) {
			return new Pair<T, IARESResource>((T)infoListList.get(0).getObject(), infoListList.get(0).getResource());
		}
		return null;
	}
}
