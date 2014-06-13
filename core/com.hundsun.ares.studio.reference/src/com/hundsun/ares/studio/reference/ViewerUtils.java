/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModel;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ProjectReferenceCollection;
import com.hundsun.ares.studio.model.reference.ProjectRelationCollection;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.model.reference.ReferenceTable;
import com.hundsun.ares.studio.model.reference.RelationInfo;
import com.hundsun.ares.studio.model.reference.RelationTable;

/**
 * @author gongyf
 *
 */
public class ViewerUtils {
	
	private static ListMultimap<String, IReferenceInfoProvider> refInfoProviderMap;
	
	
	/**
	 * ������Դ���͵õ���Դ������Ϣ�ṩ���б�
	 * 
	 * @return
	 */
	private static List<IReferenceInfoProvider> getRefInfoProviders(String resType) {
		
		if (refInfoProviderMap == null) {
			
			refInfoProviderMap = ArrayListMultimap.create();
			
			// ��ȡ��չ��
			IExtensionRegistry reg = Platform.getExtensionRegistry();
			IConfigurationElement[] elements = reg.getConfigurationElementsFor(ReferencePlugin.PLUGIN_ID, "ReferenceInfoProviders");
			for (IConfigurationElement element : elements) {
				try {
					IReferenceInfoProvider provider = (IReferenceInfoProvider) element.createExecutableExtension("class");					
					for (String refType : StringUtils.split(element.getAttribute("type"), ',')) {
						refInfoProviderMap.put(refType, provider);
					}
				} catch (Exception e) {
				}
			}
		}
		return refInfoProviderMap.get(resType);
	}
	
	private static ListMultimap<String, IRelationInfoProvider> relInfoProviderMap;
	
	/**
	 * ������Դ���͵õ���Դ�ⲿ������Ϣ�ṩ���б�
	 * 
	 * @param resType
	 * @return
	 */
	private static List<IRelationInfoProvider> getRelInfoProviders(String resType) {
		if (relInfoProviderMap == null) {
			relInfoProviderMap = ArrayListMultimap.create();
			
			// ��ȡ��չ��
			IExtensionRegistry reg = Platform.getExtensionRegistry();
			IConfigurationElement[] elements = reg.getConfigurationElementsFor(ReferencePlugin.PLUGIN_ID, "RelationInfoProviders");
			for (IConfigurationElement element : elements) {
				try {
					IRelationInfoProvider provider = (IRelationInfoProvider) element.createExecutableExtension("class");					
					for (String refType : StringUtils.split(element.getAttribute("type"), ',')) {
						relInfoProviderMap.put(refType, provider);
					}
				} catch (Exception e) {
				}
			}
		}
		
		return relInfoProviderMap.get(resType);
	}
	
	private static SetMultimap<String, String> refTypes2ResTypes;
	
	/**
	 * ����ָ�����������Ϳ�������Щ��Դ�����ṩ(һ�������һ����������ֻ����һ����Դ�ṩ)
	 * 
	 * @param refType
	 * @return
	 */
	public static String[] getResTypesCanSupport(String refType) {
		if (refTypes2ResTypes == null) {
			refTypes2ResTypes = HashMultimap.create();
			
			// ��ȡ��չ��
			IExtensionRegistry reg = Platform.getExtensionRegistry();
			IConfigurationElement[] elements = reg.getConfigurationElementsFor(ReferencePlugin.PLUGIN_ID, "ReferenceTypes");
			for (IConfigurationElement element : elements) {
				String id = element.getAttribute("id");
				String[] types = StringUtils.split( element.getAttribute("resType"), ',');
				
				refTypes2ResTypes.putAll(id, Arrays.asList(types));
			}
		}
		return refTypes2ResTypes.get(refType).toArray(new String[0]);
	}
	
	
	private static SetMultimap<String, String> resTypes2RefTypes;//һ����Դ�ṩ��Щ��������
	
	/**
	 * ����ָ����Դ�����ṩ��Щ��������
	 * 
	 * @param resType
	 * @return
	 */
	public static String[] getRefTypesCanSupport(String resType) {
		if (resTypes2RefTypes == null) {
			resTypes2RefTypes = HashMultimap.create();
			
			// ��ȡ��չ��
			IExtensionRegistry reg = Platform.getExtensionRegistry();
			IConfigurationElement[] elements = reg.getConfigurationElementsFor(ReferencePlugin.PLUGIN_ID, "ReferenceTypes");
			for (IConfigurationElement element : elements) {
				String id = element.getAttribute("id");
				String[] types = StringUtils.split( element.getAttribute("resType"), ',');
				for(String type:types){
					if(resTypes2RefTypes.get(type)!=null){
						if(!resTypes2RefTypes.get(type).contains(id)){
							resTypes2RefTypes.get(type).add(id);
						}
					}else{
						List<String> resTypes = new ArrayList<String>();
						resTypes.add(id);
						resTypes2RefTypes.putAll(type, resTypes);
					}
				}
				
			}
		}
		return resTypes2RefTypes.get(resType).toArray(new String[0]);
	}
	
	private static <T> Collection<T> getSafeCollection(Collection<T> c) {
		if (c == null) {
			return Collections.emptyList();
		}
		return c;
	}
	
	private static List<String> onlyResourceOnlyRefType = null;
	/**
	 * �Ƿ�Ϊһ����Դֻ�ṩһ����������
	 * @param refType
	 * @return
	 */
	public static  boolean isOnlyResourceOnlyRefType(String refType){
		if(onlyResourceOnlyRefType==null){
			onlyResourceOnlyRefType = new ArrayList<String>(10);
			onlyResourceOnlyRefType.add("jres.md.defaultvalue");
			onlyResourceOnlyRefType.add("jres.md.stdfield");
			onlyResourceOnlyRefType.add("jres.md.defaulttype");
			onlyResourceOnlyRefType.add("jres.md.datatype");
			onlyResourceOnlyRefType.add("jres.md.errorno");
			onlyResourceOnlyRefType.add("jres.md.constant");
		}
		
		String[] resTypes = ViewerUtils.getResTypesCanSupport(refType);
	
		if(resTypes!=null && resTypes.length==1) {
			String[] refTypes = ViewerUtils.getRefTypesCanSupport(refType);
			if(refTypes!=null && refTypes.length==1){
				return onlyResourceOnlyRefType.contains(refType);
			}else{
				return false;
			}
			
		}else{
			return false;
		}
		
	}
	
	/**
	 * ��ȡһ����Դ�ṩ������������Ϣ�����뱣֤�����Դû�б�ɾ��
	 * @param resource
	 * @param context
	 * @return
	 */
	public static List<ReferenceInfo> getReferenceInfos(IARESResource resource, Map<Object, Object> context) {
		List<ReferenceInfo> infoList = new ArrayList<ReferenceInfo>();
		
		for (IReferenceInfoProvider provider : getRefInfoProviders(resource.getType())) {
			infoList.addAll(getSafeCollection(provider.getReferenceInfos(resource, context)));
		}
		
		return infoList;
	}
	
	/**
	 * �����ñ���ȡ��ָ����Դ�ṩ������������Ϣ
	 * @param table
	 * @param resource
	 * @return
	 */
	public static List<ReferenceInfo> getReferenceInfos(ReferenceTable table, IARESResource resource) {
		List<ReferenceInfo> infoList = new ArrayList<ReferenceInfo>();
		
		ProjectReferenceCollection pi = table.getProjects().get(resource.getARESProject());
		if (pi != null) {
			EList<ReferenceInfo> referenceInfoList =  pi.getReferences().get(resource.getType());
			if(referenceInfoList!=null){
				for (ReferenceInfo ref : referenceInfoList) {
					if (ObjectUtils.equals(ref.getResource(), resource)) {
						infoList.add(ref);
					}
				}
			}
			
		}
		
		
		return infoList;
	}
	
	/**
	 * �õ�������������Ŀ��Ҳ�����Լ�
	 * 
	 * @param project
	 * @return
	 */
	public static IARESProject[] getAllRequiredProject(IARESProject project) {
		Set<IARESProject> result = new HashSet<IARESProject>();
		
		// ͨ��ջ�������й���
		Stack<IARESProject> stack = new Stack<IARESProject>();
		stack.add(project);
		
		while (!stack.isEmpty()) {
			IARESProject p = stack.pop();
			// ��������Ѿ��������б���û�б�Ҫ�����������Ĺ��̣���Ϊ�Ѿ���������
			if (!result.contains(p)) {
				result.add(p);
				stack.addAll(Arrays.asList(p.getRequiredProjects()));
			}
		}
		
		return result.toArray(new IARESProject[result.size()]);
		
	}
	
	/**
	 * �ҵ�����ֱ�ӻ������������̵Ĺ��̣���ȻҲ��������
	 * 
	 * @param project
	 * @return
	 */
	public static IARESProject[] getAllRelatedProjects(IARESProject project) {
		IARESModel model = project.getARESModel();
		Set<IARESProject> result = new HashSet<IARESProject>();
		result.add(project);
		try {
			for (IARESProject p : model.getARESProjects()) {
				if (ArrayUtils.contains(getAllRequiredProject(p), project) ) {
					// ˵���������ֱ�ӻ�������������
					result.add(p);
				}
			}
		} catch (ARESModelException e) {
		}
		return result.toArray(new IARESProject[result.size()]);
	}
	
	/**
	 * ��ȡһ����Դ���ⲿ�����б�
	 * @param resource
	 * @param context
	 * @return
	 */
	public static List<RelationInfo> getRelationInfos(IARESResource resource, Map<Object, Object> context) {
		List<RelationInfo> infoList = new ArrayList<RelationInfo>();
		
		for (IRelationInfoProvider provider : getRelInfoProviders(resource.getType())) {
			infoList.addAll(getSafeCollection(provider.getRelationInfos(resource, context)));
		}
		
		return infoList;
	}
	
	/**
	 * �����ù�ϵ�����ҵ�ָ����Դ���ⲿ���õ���Ϣ
	 * 
	 * @param table
	 * @param resource
	 * @return
	 */
	public static List<RelationInfo> getRelationInfos(RelationTable table, IARESResource resource) {
		List<RelationInfo> infoList = new ArrayList<RelationInfo>();
		ProjectRelationCollection pi = table.getProjects().get(resource.getARESProject());
		if (pi != null) {
			List<RelationInfo> relationInfoInfoList = pi.getRelations().getRelationOperator().getAllRelation();
			if (relationInfoInfoList != null) {
				for (RelationInfo rel : relationInfoInfoList) {
					if (ObjectUtils.equals(rel.getHostResource(), resource)) {
						infoList.add(rel);
					}
				}
			}
		}

		return infoList;
	}

}
