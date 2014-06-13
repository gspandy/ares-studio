package com.hundsun.ares.studio.jres.obj.ui.refactor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ltk.core.refactoring.Change;

import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.internal.ui.refactoring.changes.ReferenceProviderRefChange;
import com.hundsun.ares.studio.model.reference.RelationInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class ObjectRefactorUtil {

	public static List<Change> createChanges(IARESResource objRes, String newRefName ) {
		List<Change> changes = new ArrayList<Change>();
		changes.addAll(createChanges(objRes, newRefName, IBizResType.Object));
		changes.addAll(createChanges(objRes, newRefName, IBizResType.Std_Obj));
		return changes;
	}
	
	/**
	 * ����һ�����������������ƶ��Ķ�����Դ(��˼����������ȫ�����ı�)������Ӧ�ý��е���صĸı䣬����һ��CHange�б�
	 * @param objRes ��������Object��Դ
	 * @param newName
	 * @param targetResType ��Ҫ�ع�����Դ����
	 * @return
	 */
	public static List<Change> createChanges(IARESResource objRes, String newRefName, String targetResType ) {
		List<Change> changes = new ArrayList<Change>();
		// ����ȫ����Ҫ�ı�Ķ����ҵ���������������Դ
		IARESProject project = objRes.getARESProject();
		List<RelationInfo> relList = ReferenceManager.getInstance().getRelationInfoByTarget(IBizRefType.Object, objRes.getFullyQualifiedName(), project);
		for (RelationInfo rel : relList) {
			// ������������Դ�Ƿ��񣬲����������Ǹ���������Ҫ��������
			IARESResource hostRes = rel.getHostResource();
			if (StringUtils.equals(hostRes.getType(), targetResType)) {
				changes.add(new ReferenceProviderRefChange(hostRes, IBizRefType.Object, objRes.getFullyQualifiedName(), newRefName));
			}
		}
		return changes;
	}
	
}
