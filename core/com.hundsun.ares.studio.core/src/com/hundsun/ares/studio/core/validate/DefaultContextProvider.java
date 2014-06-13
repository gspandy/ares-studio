package com.hundsun.ares.studio.core.validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModel;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.internal.core.ARESModelManager;

/**
 * Ĭ�ϵ�Context��
 * ��һ��MultiMap������Դ��Ϣ��fullyQualfiedName--->IAresResource;
 * һ��key���ܶ�Ӧ���Value.
 * �����ǻ��棬��ʹ�ù����в�Ӧ����ͼ�޸Ļ������ֵ����Ȼ����ֱ���׳��쳣�����ǿ��ܻ��������Ԥ�����߼�����
 * @author sundl
 */
public class DefaultContextProvider extends AbstractAresContext {

	private IARESModel model;
	private IARESProject project;
	private List<IARESModule> modules = new ArrayList<IARESModule>();
	private Multimap<String, IARESResource> resources = ArrayListMultimap.create();
	
	public void init(IARESProject project) throws ARESModelException {
		this.model = ARESModelManager.getManager().getModel();
		this.project = project;
		if (this.project != null) {
			this.modules.clear();
			this.modules.addAll(Arrays.asList(project.getModules()));
			this.resources.clear();
			for (IARESResource res : project.getResources()) {
				resources.put(res.getFullyQualifiedName(), res);
			}
		}
	}

	/**
	 * @return the model
	 */
	public IARESModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(IARESModel model) {
		this.model = model;
	}

	/**
	 * @return the project
	 */
	public IARESProject getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(IARESProject project) {
		this.project = project;
	}

	/**
	 * @return the modules
	 */
	public List<IARESModule> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<IARESModule> modules) {
		this.modules = modules;
	}

	/**
	 * @return the resources
	 */
	public Multimap<String, IARESResource> getResources() {
		return resources;
	}

}
