/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.basicdata.manage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.converter.IModelConverter;
import com.hundsun.ares.studio.core.model.converter.IModelConverter2;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.core.registry.IResDescriptor;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataRestypes;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.BasicdataFactory;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.EpacakgeDefineList;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.PackageDefine;

/**
 * @author lvgao
 *
 */
public class BasicdataDefineManageUtil {

	//private static String BasicdataModueRoot =  getBasicdataModuleRoot;
	//private static String EpacakgeFilePath = BasicdataModueRoot + "/" +IBasicDataRestypes.PackageDefineResorceName;
	
	private static String getEpacakgeFilePath(IARESProject project){
		return getBascicdataModuleRoot(project) + "/" +IBasicDataRestypes.PackageDefineResorceName;
	}
	
	private static String getBascicdataModuleRoot(IARESProject project){
		IFolder root = ARESElementUtil.getModuleRootFolder(project,"com.hundsun.ares.studio.jres.moduleroot.commondata");
		return root.getName();
	}
	
	/**
	 * ����PackageDefine
	 * @param project
	 * @param define
	 * @throws Exception
	 */
	public static void savePackageDefine(IARESProject project,PackageDefine define)  throws Exception{
		try {
			IARESResource resource = getDefineResource(project,true);
			EpacakgeDefineList defineList = resource.getInfo(EpacakgeDefineList.class);

			//����Ѵ��ڴ�·����Ӧ��ɾ��
			for(int i = defineList.getItems().size() - 1; i >= 0; i--){
				if(StringUtils.equals(defineList.getItems().get(i).getUrl(), define.getUrl())){
					defineList.getItems().remove(i);
				}
			}
	
			defineList.getItems().add(define);
			resource.save(defineList, true, new NullProgressMonitor());
		} catch (Exception e) {
			String mesage = String.format("������������������Ϣʧ��,��ϸ��Ϣ:%s", e.getMessage());
			throw new Exception(mesage);
		}
	}
	
	/**
	 * ��ȡ������������
	 * @param project
	 * @param create    �Ƿ�ǿ�ƴ���
	 * @return
	 * @throws Exception
	 */
	public static IARESResource getDefineResource(IARESProject project,boolean create)throws Exception{
		//�������������ļ�
		IFile file = project.getProject().getFile(getEpacakgeFilePath(project));
		if (!file.exists() && create) {
			//��������ھʹ���
			createEpacakgeDefineResource(project);
		}
		
		return (IARESResource) ARESCore.create(file);
	}
	
	
	/**
	 * �����������ݶ����ļ�
	 * @return
	 */
	private static IResource createEpacakgeDefineResource(IARESProject project) {
		IResDescriptor resDescriptor = ARESResRegistry.getInstance()
				.getResDescriptor(IBasicDataRestypes.PackageDefine);
		IModelConverter converter = resDescriptor.getConverter();
		Object info = BasicdataFactory.eINSTANCE.createEpacakgeDefineList();
		IFolder folder = project.getProject().getFolder(getBascicdataModuleRoot(project) + "/");
		String name = IBasicDataRestypes.PackageDefineResorceName;
		IFile file = folder.getFile(name);
		if (!file.exists()) {
			if (converter instanceof IModelConverter2) {
				try {

					IARESResource resource = (IARESResource) ARESCore
							.create(file);
					file.create(
							new ByteArrayInputStream(
									((IModelConverter2) converter).write(
											resource, info)), true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					converter.write(bos, info);
					file.create(new ByteArrayInputStream(bos.toByteArray()),
							true, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	
}
