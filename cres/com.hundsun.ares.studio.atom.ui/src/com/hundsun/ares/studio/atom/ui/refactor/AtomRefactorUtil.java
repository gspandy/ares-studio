package com.hundsun.ares.studio.atom.ui.refactor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ltk.core.refactoring.Change;

import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.internal.ui.refactoring.changes.ReferenceProviderRefChange;
import com.hundsun.ares.studio.model.reference.RelationInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class AtomRefactorUtil {

	/**
	 * ����һ�����������������ƶ��Ķ�����Դ(��˼����������ȫ�����ı�)������Ӧ�ý��е���صĸı䣬����һ��CHange�б�
	 * @param objRes ��������Object��Դ
	 * @param newName
	 * @param resTypes ��Դ����
	 * @return
	 */
	public static List<Change> createChanges(IARESResource objRes, String newRefName,List<String> resTypes) {
		List<Change> changes = new ArrayList<Change>();
		// ����ȫ����Ҫ�ı�Ķ����ҵ���������������Դ
		IARESProject project = objRes.getARESProject();
		List<RelationInfo> relList = ReferenceManager.getInstance().getRelationInfoByTarget(IBizRefType.Object, objRes.getFullyQualifiedName(), project);
		for (RelationInfo rel : relList) {
			// ������������Դ�Ƿ��񣬲����������Ǹ���������Ҫ��������
			IARESResource hostRes = rel.getHostResource();
			if (resTypes.contains(hostRes.getType())) {
				changes.add(new ReferenceProviderRefChange(hostRes, IBizRefType.Object, objRes.getFullyQualifiedName(), newRefName));
			}
			
		}
		return changes;
	}
	
}
