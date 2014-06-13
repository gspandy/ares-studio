package com.hundsun.ares.studio.jres.basicdata.logic.epackage;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

import com.hundsun.ares.studio.core.IARESBundle;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataEpacakgeConstant;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataRestypes;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.EpacakgeDefineList;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.PackageDefine;
import com.hundsun.ares.studio.jres.basicdata.logic.epackage.extensionpoint.EpackageFactoryManager;

public class BasicDataEpackageFactory {
	
	
	private final static Map<IARESResource, EPackage> epackageMap = new HashMap<IARESResource, EPackage>(); 
	
	private final static Map<IARESBundle, IARESResource> defineMap = new HashMap<IARESBundle, IARESResource>(); 
	
	
	
	public static BasicDataEpackageFactory eINSTANCE = new BasicDataEpackageFactory();
	
	/*
	* Instantiate EcoreFactory
	*/
	EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE;
	
	/*
	* Instantiate EcorePackage
	*/
	EcorePackage theCorePackage = EcorePackage.eINSTANCE;

	public EPackage createEPackage(IARESResource resource)throws Exception{
		if( null != resource.getLib()){
			//TODO ������Դ���е���Դ�Ĵ���
			return null;
		}	
		
		
		if(!epackageMap.containsKey(resource)){
			PackageDefine tableDefine = getDefine(resource);
			epackageMap.put(resource, createEPackage(resource.getARESProject(), tableDefine));
		}
		return epackageMap.get(resource);
	}
	
	
	/**
	 * ��ȡ����
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public PackageDefine getDefine(IARESResource resource)throws Exception{
		
		String resourctUrl = resource.getResource().getProjectRelativePath().toOSString();
		
		//��ȡ������Դ
		IARESProject project = resource.getARESProject();
		if(!defineMap.containsKey(project)){
			IARESResource[] resources = project.getResources(IBasicDataRestypes.PackageDefineResorceName);
			if(0 == resources.length){
				throw new Exception("û���ҵ��������Ϣ��");
			}
			defineMap.put(project, resources[0]);
		}
		IARESResource defineResource = defineMap.get(project);
		
		EpacakgeDefineList defineList = defineResource.getInfo(EpacakgeDefineList.class);
		if(null == defineList){
			throw new Exception("�������Ϣģ�ͼ���ʧ�ܡ�");
		}
		
		PackageDefine tableDefine = null;
		for(PackageDefine defineItem:defineList.getItems()){
			if(StringUtils.equals(defineItem.getUrl(), resourctUrl)){
				tableDefine = defineItem;
			}
		}
		
		if(null == tableDefine){
			throw new Exception("û���ڱ������Ϣ���ҵ�����Դ�Ķ��塣");
		}
		
		return tableDefine;
	}
	
	
	/**
	 * ���EPackage����
	 * @param resource
	 */
	public void clearEPackage(IARESResource resource){
		epackageMap.remove(resource);
		try {
			resource.close();
		} catch (Exception e) {
		}
	}
	
	/**
	 * ����Epackage
	 * @param project
	 * @param tableDefine
	 * @return
	 * @throws Exception
	 */
	public EPackage createEPackage(IARESProject project,PackageDefine tableDefine)throws Exception{
		IBaiscDataEpackageFactory factory = EpackageFactoryManager.getInstance().getFactory(tableDefine.getType());
		return factory.createEPackage(project, tableDefine);
	
	}
	
	/**
	 * ��������ʵ��
	 * @param project
	 * @param tableDefine
	 * @return
	 * @throws Exception
	 */
	public EObject createInstance(IARESProject project,PackageDefine tableDefine)throws Exception{
		
		EPackage tempEPackage = createEPackage(project,tableDefine);

//		EFactory tempFactoryInstance = tempEPackage.getEFactoryInstance();
//		return tempFactoryInstance.create((EClass)tempEPackage
//				.getEClassifier(IBasicDataEpacakgeConstant.ResourceRoot));
		
		return createInstance(tempEPackage);
	}
	
	
	/**
	 * ��������ʵ��
	 * @param tempEPackage
	 * @return
	 * @throws Exception
	 */
public EObject createInstance(EPackage tempEPackage)throws Exception{
		EFactory tempFactoryInstance = tempEPackage.getEFactoryInstance();
		return tempFactoryInstance.create((EClass)tempEPackage
				.getEClassifier(IBasicDataEpacakgeConstant.ResourceRoot));
		
	}
	
}
