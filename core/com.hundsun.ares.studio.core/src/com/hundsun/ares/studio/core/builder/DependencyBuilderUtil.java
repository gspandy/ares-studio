/**
 * Դ�������ƣ�DependencyBuilderUtil.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESProjectProperty;
import com.hundsun.ares.studio.core.IDependencyUnit;
import com.hundsun.ares.studio.core.IReferencedLibrary;
import com.hundsun.ares.studio.core.util.Util;
import com.hundsun.ares.studio.core.validate.ReferenceLibValidator;
import com.hundsun.ares.studio.internal.core.ARESProject;

/**
 * ��������Build�߼�
 * @author sundl
 */
public class DependencyBuilderUtil {
	
	/**
	 * �����Ŀ������
	 * @param project
	 * @param monitor
	 */
	public static void validateDependencies(IARESProject project, IProgressMonitor monitor) {
		List<IDependencyUnit> dependencies = project.getDependencies();
		
		monitor = Util.monitorFor(monitor);
		monitor.beginTask("", dependencies.size() + 1);
		
		// ��ʼ������������Ϣת��Map����ʽ������ʹ�ã�Ч��Ҳ����
		Multimap<String , IDependencyUnit> units = ArrayListMultimap.create();
		for (IDependencyUnit unit : dependencies) {
			units.put(unit.getId(), unit);
		}
		
		monitor.subTask("�����Ŀ����ID");
		IARESProblem problem = ReferenceLibValidator.validateProjectNamespace(project);
		if (problem != null) {
			// �����ռ��ͻ�Ĵ�����ӵ�����������
			AresProjectBuilderUtil.markProblems(project.getProperty().getResource(), Arrays.asList(problem));
		}
		
		monitor.worked(1);
		
		for (IDependencyUnit unit : dependencies) {
			if (monitor.isCanceled())
				break;
			
			monitor.subTask("���ڼ��" + unit.getDescriptionStr());
			List<IARESProblem> problems = new ArrayList<IARESProblem>();
			
			// 1. �����ռ��Ƿ�������������ͻ			
			List<IARESProblem> conflicts = ReferenceLibValidator.validateIdConfilicts(unit, project, units);
			problems.addAll(conflicts);
			
			// 2. �Լ��������Ƿ�����
			List<IARESProblem> dependencyProblems = ReferenceLibValidator.validateDependencies(unit, units);
			problems.addAll(dependencyProblems);
			
			IResource resToMark = null;
			if (unit instanceof IReferencedLibrary) {
				resToMark = ((IReferencedLibrary) unit).getResource();
			} 
			
			// ����Ҳ������ʵĵط����Marker������ӵ���Ŀ��
			if (resToMark == null) {
				resToMark = project.getProject();
			}
			
			AresProjectBuilderUtil.clearMarkers(resToMark,IAresMarkers.REFERLIB_MARKER_ID,false);
			AresProjectBuilderUtil.markProblems(resToMark, problems);
			
			monitor.worked(1);
		}
		monitor.done();
	}

	/**
	 * @param project
	 * @param delta
	 * @return
	 */
	public static boolean needBuild(IARESProject project, IResourceDelta delta) {
		try {
			if (delta.findMember(new Path(ARESProject.RES_PATH_FILE)) != null
					|| delta.findMember(new Path(IARESProjectProperty.PRO_FILE)) != null) {
				return true;
			} else {
				List<IReferencedLibrary> libs = Arrays.asList(project.getReferencedLibs());
				for (IReferencedLibrary lib : libs) {
					IResourceDelta subDelta = delta.findMember(lib.getPath().makeRelativeTo(project.getProject().getFullPath()));
					if (subDelta != null) {
						// Ŀǰ���߼��������һ�����ð������˱仯���ͱ������¼�����еİ���Ϊ�˼�������ռ�
						return true;
					}
				}
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
