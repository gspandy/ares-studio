/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ARESProblem;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESProjectProperty;
import com.hundsun.ares.studio.core.IDependenceDescriptor;
import com.hundsun.ares.studio.core.IDependencyUnit;
import com.hundsun.ares.studio.core.IReferencedLibrary;
import com.hundsun.ares.studio.core.builder.DependencyBuilderUtil;
import com.hundsun.ares.studio.core.builder.IAresMarkers;
import com.hundsun.ares.studio.core.util.Util;

/**
 * ���ã�������صļ�顣
 */
// * ��ʼ��ʱ��ֻ�����ð����������������ù��̣�����������ֲ�����ʣ����������漰�������ع�����ʱû�ġ�
public class ReferenceLibValidator {

	/**
	 * ���һ����Ŀ����������Ĵ��󣬱��������ռ��ͻ����������ڵ�...
	 * �������������ҪΪ�ڵ������ð��ĵط���֤������Ŀ�Ƿ���ڴ��� ��������builder�У��õ����߼�������������ƣ�����Ϊ�˷������
	 * Marker���õ�������һ������{@link DependencyBuilderUtil#validateDependencies()}}
	 * @see DependencyBuilderUtil
	 * @param project
	 * @param monitor
	 * @return
	 */
	// 2012-2-22 sundl ���������������Ϊ����������Դ���Ĺ��ܻ����ڵ����Ϸ��������󣬸�Ϊ������·��������߼���
	public static List<IARESProblem> validateProjectDependencies(IARESProject project, IProgressMonitor monitor) {
		List<IDependencyUnit> dependencies = project.getDependencies();
		List<IARESProblem> result = new ArrayList<IARESProblem>();
		
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
			result.add(problem);
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
			
			result.addAll(problems);
			monitor.worked(1);
		}
		
		monitor.done();
		return result;
	}

	/**
	 * ���������������ռ�ĳ�ͻ��
	 * @param units
	 * @param unitToValidate
	 * @return
	 * @author sundl
	 */
	public static IARESProblem validateNameSpace(Collection<IDependencyUnit> units, IDependencyUnit unitToValidate) {
		for (IDependencyUnit unit : units) {
			if (unit == unitToValidate)
				continue;
			
			if (StringUtils.equals(unit.getId(), unitToValidate.getId())) {
				IARESProblem problem = ARESProblem.createError(IAresMarkers.REFERLIB_MARKER_ID);
				problem.setMessage(String.format("%s��%s�������ռ��ͻ��", unitToValidate.getDescriptionStr(), unit.getDescriptionStr()));
			}
		}
		return null;
	}
	
	/**
	 * ���������Դ���������ռ�
	 * 
	 * @param liblist
	 *            ������Դ���б�
	 * @param slib
	 *            �����������Դ��
	 * @return
	 */
	public static IARESProblem validateNameSpace(List<IReferencedLibrary> liblist, IReferencedLibrary slib) {
		String sid = slib.getId();
		for (IReferencedLibrary tlib : liblist) {
			if (!slib.equals(tlib)) { // ��ͬ��������Դ��
				String tid = tlib.getId();
				if (StringUtils.equals(tid, sid) && StringUtils.equals(slib.getType(), tlib.getType())) {
					// �����ռ��ͻ
					IARESProblem problem = ARESProblem.createError(IAresMarkers.REFERLIB_MARKER_ID);
					problem.setMessage(String.format("������Դ��:%s��������Դ��:%s�����ռ��ͻ��", slib.getElementName(), tlib.getElementName()));
					return problem;
				}
			}
		}
		return null;

	}
	
	

	/**
	 * ������������Ŀ�������ռ������Ƿ��������Դ��(�����ù���)��ͻ
	 * @param aresProject ��Ҫ������Ŀ
	 * @return
	 */
	public static IARESProblem validateProjectNamespace(IARESProject aresProject) {
		try {
			IARESProjectProperty property = aresProject.getProjectProperty();
			String pid = property.getId();
			if (!StringUtils.isEmpty(pid)) {
				for (IDependencyUnit dependency : aresProject.getDependencies()) {
					if (StringUtils.equals(pid, dependency.getId())
							// 2011-08-25 sundl ����������֤��ͨ��nature���жϣ�
							&& aresProject.getProject().hasNature(String.valueOf(dependency.getType()))) {
						// �����ռ��ͻ
						IARESProblem problem = ARESProblem.createError(IAresMarkers.REFERLIB_MARKER_ID);
						problem.setMessage(String.format("��Ŀ��:%s�����ռ��ͻ��", dependency.getDescriptionStr()));
						return problem;
					}
				}
			}
		} catch (ARESModelException e1) {
			e1.printStackTrace();
		} catch (CoreException e2) {
			e2.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ���ָ�����������������ͻ
	 * @param unitToValidate
	 * @param units
	 * @return
	 */
	public static List<IARESProblem> validateIdConfilicts(IDependencyUnit unitToValidate, IARESProject project, Multimap<String, IDependencyUnit> units) {
		List<IARESProblem> problems = new ArrayList<IARESProblem>();
		
		// 2012-2-17 sundl BUG #2500 �����ռ�Ϊ��Ϊ�Ϸ������ټ�顣
		if (StringUtils.isEmpty(unitToValidate.getId())) {
			return problems;
		}
		
		// ���ȼ���Ƿ�͵�ǰ��Ŀ��ͻ
		try {
			if (StringUtils.equals(project.getInfo().getId(), unitToValidate.getId())
					&& project.getProject().hasNature(unitToValidate.getType())) {
				IARESProblem problem = ARESProblem.createError(IAresMarkers.REFERLIB_MARKER_ID);
				problem.setMessage(String.format("%s�͵�ǰ������Ŀ�������ռ��ͻ��", unitToValidate.getDescriptionStr()));
				problems.add(problem);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		// ��������������ĳ�ͻ����ȡ����ID��ͬ��������������Ҳ��ͬ����Ϊ��ͻ
		Collection<IDependencyUnit> unitsWithSameId = units.get(unitToValidate.getId());
		//unitsWithSameId.remove(unitToValidate);
		for (IDependencyUnit unit : unitsWithSameId) {
			if (StringUtils.equals(unitToValidate.getType(), unit.getType())
					&& !(unit.equals(unitToValidate))) {
				IARESProblem problem = ARESProblem.createError(IAresMarkers.REFERLIB_MARKER_ID);
				problem.setMessage(String.format("%s��%s�������ռ��ͻ��", unitToValidate.getDescriptionStr(), unit.getDescriptionStr()));
				problems.add(problem);
			}
		}
		return problems;
	}
	
	/**
	 * ���ָ���ĵ�Ԫ�������Ƿ�����
	 * @param unitToValidate
	 * @param units �����ģ���ǰ�Ѿ���ӵ����е������
	 * @return
	 */
	public static List<IARESProblem> validateDependencies(IDependencyUnit unitToValidate, Multimap<String, IDependencyUnit> units) {
		List<IARESProblem> problems = new ArrayList<IARESProblem>();
		
		List<IDependenceDescriptor> dependencyDescriptors = unitToValidate.getDependencyDescriptors();
		for (IDependenceDescriptor desc : dependencyDescriptors) {
			// ������������ָ�����������Ҳ�������Ӵ���
			Collection<IDependencyUnit> unitsWithSameId = units.get(desc.getId());
			boolean dependencyFound = false;
			for (IDependencyUnit unit : unitsWithSameId) {
				if (StringUtils.equals(desc.getType(), unit.getType())
						&& StringUtils.equals(desc.getVersionConstraint(), unit.getVersion())) {
					dependencyFound = true;
					break;
				}
			}
			if (!dependencyFound) {
				IARESProblem problem = ARESProblem.createError(IAresMarkers.REFERLIB_MARKER_ID);
				problem.setMessage(String.format("%s��������(ID:%s, Version:%s, Type:%s)û���ҵ�!", 
											unitToValidate.getDescriptionStr(), 
											desc.getId(),
											desc.getVersionConstraint(),
											desc.getType()));
				problems.add(problem);
			}
		}
		return problems;
	}
}
