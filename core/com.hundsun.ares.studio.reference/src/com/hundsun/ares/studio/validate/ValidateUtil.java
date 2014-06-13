/**
 * Դ�������ƣ�ValidateUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESProblem;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IValidateConstant;
import com.hundsun.ares.studio.core.context.DefaultNamespaceHelper;
import com.hundsun.ares.studio.core.context.INamespaceHelper;
import com.hundsun.ares.studio.core.context.IResStatisticProvider;
import com.hundsun.ares.studio.core.context.JRESContextManager;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.core.model.util.Pair;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class ValidateUtil {
	
	/**
	 * ��������Դ�����Լ����ǰ׺
	 */
	private static final String KEY_RESOURCE_EXIST_CHECK_RESULT_PREFIX = "KEY_RESOURCE_EXIST_CHECK_RESULT_PREFIX$";
	
	/**
	 * ��Դ���Ľ��״̬
	 * @author gongyf
	 *
	 */
	public static enum RESULT_REFID_CHECK {
		/**
		 * ��Դ����
		 */
		OK,
		
		/**
		 * ��Դ������
		 */
		ERROR_NOT_EXIST,
		
		/**
		 * ��Դ�ظ�������ֱ������
		 */
		ERROR_DUPLICATION
	}
	
	/**
	 * ��Ϣ�Ľ������ȼ�
	 */
	public static final String VALIDATE_KEY_UI_SEVERITY = "��Ϣ�Ľ������ȼ�";
	
	/**
	 * ��Ϣ��д��Ķ���
	 */
	public static final String  VALIDATE_KEY_OBJECT = String.format("%s%s", IARESProblem.UNPERSISTENT_PROPERTY_PREFIX,".pkgide.object");
	
	/**
	 * ��emf�Ĵ���ģ��ת��Ϊares����ģ��
	 * 
	 * @param chain
	 * @return
	 */
	public static List<IARESProblem> diagnosticChainToARESProblem(
			BasicDiagnostic chain) {
		List<IARESProblem> tmplist = new ArrayList<IARESProblem>();
		for (Diagnostic element : chain.getChildren()) {
			IARESProblem tmpitem = null;
			if (Diagnostic.ERROR == element.getSeverity()) {
				tmpitem = ARESProblem.createError();
			} else {
				tmpitem = ARESProblem.createWaring();
			}
			setProblemAttribute(element,tmpitem);
			tmplist.add(tmpitem);
		}
		return tmplist;
	}
	
	
	/**
	 * ��chain�л�ȡ���⣬����д������ȷ��ش���
	 * ���û����Ϣ����null
	 * @param chain
	 * @return
	 */
	public static IARESProblem getARESProblem(BasicDiagnostic chain) {
		for (Diagnostic element : chain.getChildren()) {
			IARESProblem tmpitem = null;
			if (Diagnostic.ERROR == element.getSeverity()) {
				tmpitem = ARESProblem.createError();
				setProblemAttribute(element,tmpitem);
				return tmpitem;
			}
		}
		for (Diagnostic element : chain.getChildren()) {
			IARESProblem tmpitem = null;
			if (Diagnostic.WARNING == element.getSeverity()) {
				tmpitem = ARESProblem.createWaring();
				setProblemAttribute(element,tmpitem);
				return tmpitem;
			}
		}
		return null;
	}
	
	/**
	 * ������Ϣ�еĸ����ֶ�
	 * @param element
	 * @param tmpitem
	 */
	private static void setProblemAttribute(Diagnostic element,IARESProblem tmpitem){
		tmpitem.setAttribute(VALIDATE_KEY_UI_SEVERITY, element.getSeverity());
		tmpitem.setAttribute(VALIDATE_KEY_OBJECT, element.getData());
		tmpitem.setAttribute(IMarker.MESSAGE, element.getMessage());
		if (element.getData().size() > 0) {
			Object object =  element.getData().get(0);
			if (object instanceof EObject) {
				Resource res = ((EObject) object).eResource();
				tmpitem.setAttribute(IMarker.LOCATION, res.getURIFragment((EObject) object));
			}
			
		}
		tmpitem.setAttribute(IMarker.PRIORITY, element.getCode());
	}
	
//	/**
//	 * ��ȡ�������������
//	 * @param resource     ��Դ
//	 * @param calculator   ��Դ������������
//	 * @param provider     ��Ŀ��Դ��Ϣͳ����
//	 * @return
//	 */
//	public static Map<Object, Object> getValidateContext(IARESResource resource
//			,INamespaceHelper calculator
//			,IResStatisticProvider provider){
//		// ����������
//		Map<Object, Object> validateContext = new HashMap<Object, Object>();
//		// ������Դ
//		validateContext.put(IValidateConstant.KEY_RESOUCE, resource);
//		//����������㹤��
//		validateContext.put(IValidateConstant.KEY_REFERENCE_CALCULATOR, calculator);
//
//		// ����ͳ����Ϣ�ṩ��
//		validateContext.put(IValidateConstant.KEY_STATIC_PROVIDER, provider);
//		return validateContext;
//	}
	
	/**
	 * ��ȡ��Դ�Ĵ�����������
	 * @param resource
	 * @return
	 */
	public static Map<Object, Object> getValidateContext(IARESResource resource){
		
		Assert.isNotNull(resource);
		
		// ����������
		Map<Object, Object> validateContext = new HashMap<Object, Object>();
		
		// ������Դ
		validateContext.put(IValidateConstant.KEY_RESOUCE, resource);
		
		//����������㹤��
		validateContext.put(IValidateConstant.KEY_REFERENCE_CALCULATOR, new DefaultNamespaceHelper());
		
		// ����ͳ����Ϣ�ṩ��
		validateContext.put(IValidateConstant.KEY_STATIC_PROVIDER, ReferenceManager.getInstance());
		
		// ���뱾���̵���ͳ����Ϣ�ṩ��
		validateContext.put(IValidateConstant.KEY_CURRENT_STATISTIC_PROVIDER, ReferenceManager.getInstance());
		
		// ����������Ϣ�ṩ��
		validateContext.put(IValidateConstant.KEY_REFERENCE_PROVIDER, ReferenceManager.getInstance());
		
		validateContext.put(IValidateConstant.KEY_RESOUCE_PROJECT, resource.getARESProject());
		
		return validateContext;
	}

	/**
	 * �ж���ָ���������Ƿ�������ֵ
	 * 
	 * @param value
	 * @param listOwner
	 * @param listFeature
	 * @param idFeature
	 * @param context
	 * @return
	 */
	public static boolean isContainValue(Object value, EObject listOwner, EReference listFeature, EStructuralFeature idFeature , Map<Object, Object> context) {
		Object key = new Pair<Object, Object>("���ָ�������ڲ��Ƿ�������ֵ", 
				new Pair<Object, Object>(listOwner, new Pair<Object, Object>(listFeature, idFeature)));
		
		Set<Object> valueSet = (Set<Object>) context.get(key);
		if (valueSet == null) {
			valueSet = new HashSet<Object>();
			if (listFeature.isMany()) {
				List<EObject> list = (List<EObject>) listOwner.eGet(listFeature);
				for (EObject eObject : list) {
					valueSet.add(eObject.eGet(idFeature));
				}
			} else {
				EObject eObj = (EObject) listOwner.eGet(listFeature);
				valueSet.add(eObj.eGet(idFeature));
			}
			
			context.put(key, valueSet);
		}
		
		return valueSet.contains(value);
	}
	
	/**
	 * ���ָ���������ָ�����ֶ��Ƿ�����������������
	 * <BR>
	 * �ṩ��ת����
	 * 
	 * @param object
	 * @param idFeature
	 * @param transform
	 * @param context
	 * @return
	 */
	public static  boolean isDuplication(EObject object, EStructuralFeature idFeature, IValueTransform transform, Map<Object, Object> context) {
		Assert.isNotNull(object);
		Assert.isNotNull(object.eContainer());
		
		EObject owner = object.eContainer();
		EReference reference = object.eContainmentFeature();
		if (!reference.isMany()) {
			// ֻ��һ�����󲻿�������
			return false;
		}
		Object key = new Pair<EObject, Pair<EReference, EStructuralFeature>>(owner, new Pair<EReference, EStructuralFeature>(reference, idFeature)) ;
		
		// ����������Ǽ����д������Ŀ
		Set<EObject> errors = (Set<EObject>) context.get(key);
		if (errors == null) {
			// û�н��й�������飬��Ҫ���м��
			errors = new HashSet<EObject>();
			
			Map<Object, EObject> map = new HashMap<Object, EObject>();
			
			for (EObject child : (List<EObject>)owner.eGet(reference)) {
				Object id = child.eGet(idFeature);
				
				// 2012��3��29��15:13:56 gongyf
				// �����ת���������ת��
				if (transform != null) {
					id = transform.transform(id);
				}
				if (id == null) {
					continue;
				}
				
				EObject find = map.get(id);
				if (find != null) {
					// �ܹ��ҵ���˵����������
					errors.add(child);
					errors.add(find);
				} else {
					// ���û���ҵ������ģ�������Լ����б��У����ڼ��ʣ�µ��Ƿ��������
					map.put(id, child);
				}
			}
			
			context.put(key, errors);
		}
		
		return errors.contains(object);
	}
	
	/**
	 * ���ָ���������ָ�����ֶ��Ƿ�����������������
	 * 
	 * @param object ������������
	 * @param idFeature ���ڼ���������ֶ�
	 * @param context
	 * @return
	 */
	public static  boolean isDuplication(EObject object, EStructuralFeature idFeature, Map<Object, Object> context) {
		return isDuplication(object, idFeature, null, context);
	}
	
	/**
	 * �����Դ�Ƿ���ڣ���鷶Χ�����ñ�
	 * 
	 * @param project
	 * @param refId
	 * @param refType
	 * @param context
	 * @return
	 */
	public static  RESULT_REFID_CHECK checkReferenceId(IARESProject project, String refId, String refType, Map<Object, Object> context) {

		Object key_ns = new Pair<IARESProject, String>(project, "�����Լ�飨�������ռ䣩" + refType);
		Object key_nonens = new Pair<IARESProject, String>(project, "�����Լ�飨�������ռ䣩" + refType);
		
		Map<String, Integer> noneNsMap = (Map<String, Integer>) context.get(key_nonens);
		Map<String, Integer> nsMap = (Map<String, Integer>) context.get(key_ns);
		
		if (noneNsMap == null || nsMap == null) {
			
			noneNsMap = new HashMap<String, Integer>();
			nsMap = new HashMap<String, Integer>();
			
			context.put(key_nonens, noneNsMap);
			context.put(key_ns, nsMap);
			
			List<ReferenceInfo> referenceInfoList = ReferenceManager.getInstance().getReferenceInfos(project, refType, true);
			
			// һ�����ҳ��������������ݣ���׼��2��hash��һ���������ռ䣬һ��û�������ռ�
			for (ReferenceInfo referenceInfo : referenceInfoList) {
				
				String resName = referenceInfo.getRefName();
				String resNs = referenceInfo.getRefNamespace();
				//EObject owner = (EObject) referenceInfo.getObject();
				//IARESResource res = referenceInfo.getResource();
				
				{
					String fullName = String.format("%s.%s", resNs, resName);
					Integer count = nsMap.get(fullName);
					if (count == null) {
						count = 1;
					} else {
						count++;
					}
					nsMap.put(fullName, count);
				}

				{
					Integer count = nsMap.get(resName);
					if (count == null) {
						count = 1;
					} else {
						count++;
					}
					noneNsMap.put(resName, count);
				}
			}
		}
		

		int count = 0;
		INamespaceHelper helper = (INamespaceHelper)context.get(IValidateConstant.KEY_REFERENCE_CALCULATOR);
		String name = helper.removeNamespace(refId);
		if (name.equals(refId)) {
			// û�������ռ�����
			Integer c = noneNsMap.get(refId);
			if (c != null) {
				count = c;
			}
		} else {
			Integer c = nsMap.get(refId);
			if (c != null) {
				count = c;
			}
		}

		if (count == 0) {
			return RESULT_REFID_CHECK.ERROR_NOT_EXIST;
		} else if (count == 1) {
			return RESULT_REFID_CHECK.OK;
		} else {
			return RESULT_REFID_CHECK.ERROR_DUPLICATION;
		}
	}
	
	/**
	 * ���ָ���Ķ����ָ�������Ƿ����������ظ�
	 * ����Ǹ���ͳ����Ϣ��ģ����Բ��ܶ�û�н��б������Դ���м��
	 * 
	 * @param object ���Ķ���
	 * @param idFeature Ҫ��������
	 * @param refType �����Թ�������������
	 * @param refIdFeature ���ö�����ʹ�õ�����ֵ
	 * @param excludeSet �ų��б�����Ϊnull
	 * @param context ������
	 * @return ������ظ��򷵻��ظ��Ķ������Դ��û���򷵻�null
	 */
	public static  Pair<EObject, IARESResource> checkDuplicationInResStatisticProvider(EObject object, EStructuralFeature idFeature, 
			String refType, EStructuralFeature refIdFeature, Set<IARESResource> excludeSet, Map<Object, Object> context) {
		
		Object key = new Pair<Object, Object>("checkDuplicationInResStatisticProvider", new Pair<Object, Object>(refType, refIdFeature)) ;
		
		// �������������Ϣ�е�idӳ����Դ�������map
		Multimap<Object, Pair<EObject, IARESResource>> idMap = (Multimap<Object, Pair<EObject, IARESResource>>) context.get(key);
		if (idMap == null) {
			context.put(key, idMap = HashMultimap.create());
			
			//����������Դ�е��ظ����
			IARESProject project = (IARESProject) context.get(IValidateConstant.KEY_RESOUCE_PROJECT);
			List<ReferenceInfo> referenceInfoList = ReferenceManager.getInstance().getReferenceInfos(project, refType, true);
			
			// ��Ҫ�ų������̵���Ϣ
			for (ReferenceInfo referenceInfo : referenceInfoList) {
				
				EObject owner = (EObject) referenceInfo.getObject();
				IARESResource res = referenceInfo.getResource();
				
				Object id = owner.eGet(refIdFeature);
				if (id != null) {
					idMap.put(id, new Pair<EObject, IARESResource>(owner, res));
				}
			}
		}
		
		Object id = object.eGet(idFeature);
		Collection<Pair<EObject, IARESResource>> result = idMap.get(id);
		if (result != null && !result.isEmpty()) {
			if (excludeSet == null) {
				excludeSet = Collections.emptySet();
			}
			// ֻ����������Դ�����ų��б��в����Ǵ���
			for (Pair<EObject, IARESResource> pair : result) {
				if (!excludeSet.contains(pair.second)) {
					return pair;
				}
			}
		}
		
		return null;

	}
}
