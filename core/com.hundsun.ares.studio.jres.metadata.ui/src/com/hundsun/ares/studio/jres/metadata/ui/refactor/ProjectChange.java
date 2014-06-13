package com.hundsun.ares.studio.jres.metadata.ui.refactor;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.Operation;

/**
 * @author wangxh
 *
 */
public class ProjectChange extends ResourceChange {
	//�������������Ĺ��̽ű���Ԫ������Դ
	IARESResource res;
	//������ǰ�Ĺ������ȫ·��
	String oldPath;
	//��������Ĺ������ȫ·��
	String newPath;
	//Ԫ������Դ���ȫ·��
	String filePath;
	
	public ProjectChange(IARESResource res,String oldPath,
			String newPath) {
		this.res = res;
		this.oldPath = oldPath;
		this.newPath = newPath;
		this.filePath = res.getResource().getFullPath().toString();
	}

	@Override
	protected IResource getModifiedResource() {
		return res.getResource();
	}

	@Override
	public String getName() {
		String name = ARESResRegistry.getInstance().getResDescriptor(res).getName();
		return String.format("������Ŀ%1$s�µ�%2$s�Ľű�·��",res.getARESProject().getElementName(),name);
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		if(res == null || !res.exists()){
			//��Ŀ��������ǰ����ʱ��Ŀ�Ѿ��������꣬���Ըù��������Դ��Ҫ���»�ȡ·��
			IPath path = Path.fromPortableString(filePath.replaceFirst(oldPath, newPath));
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if(file.exists()){
				res = (IARESResource) ARESCore.create(file);
			}
		}
		if(res != null && res.exists()){
			MetadataResourceData info = res.getInfo(MetadataResourceData.class);
			EList<Operation> ops = info.getOperations();
			for(Operation op : ops){
				//�����������б����õĽű�·��
				String file = op.getFile();
				if(StringUtils.isNotBlank(file) && Path.fromPortableString(oldPath).isPrefixOf(Path.fromPortableString(file))){
					op.setFile(op.getFile().replaceFirst(oldPath, newPath));
				}
			}
			res.save(info, true, null);
			return new ProjectChange(res,oldPath, newPath);
		}
		return new NullChange();
	}

}
