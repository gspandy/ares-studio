package com.hundsun.ares.studio.jres.model.metadata.util;

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.jres.model.metadata.Operation;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;

public class MetadataScriptUtil {

	public static IARESResource getJSResource(String path) throws Exception {
		if(StringUtils.isBlank(path)){
			throw new Exception("�ű�·��Ϊ�գ�");
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile file = root.getFile(new Path(path));
		return (IARESResource) ARESCore.create(file);
	}
	
	/**
	 * ִ��һ��Metadata�е��û�����
	 * @param operation �û������������
	 * @param mode		�ű���ִ��ģʽ���ο�ScriptUtils�еĳ�������
	 * @param res		
	 * @param info
	 * @param loader
	 * @param extContext 	�����context����
	 * @throws NoSuchMethodException 
	 * @throws ScriptException 
	 * @throws CoreException 
	 * @throws IOException 
	 */
	public static void runMetadataOperation(Operation operation, 
												int mode, 
												IARESResource res, 
												JRESResourceInfo info, 
												ClassLoader loader,
												Map<String, Object> extContext) throws IOException, CoreException, ScriptException, NoSuchMethodException,Exception {
		IARESResource jsResource = getJSResource(operation.getFile());
		ScriptUtils.excuteJS(mode, jsResource, res, info, loader, extContext);
	}
	
}
