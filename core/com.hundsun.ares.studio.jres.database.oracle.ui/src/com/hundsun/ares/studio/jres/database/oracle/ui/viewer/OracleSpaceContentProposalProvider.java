/**
 * Դ�������ƣ�MetadataContentProposalProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.viewer;

import java.util.Map;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.ui.assist.JresResourceRefConentProposalPovider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * ��ʾԪ�������õ�ʱ��ʹ�õ�ProposalContentProvider
 * @author sundl
 */
public class OracleSpaceContentProposalProvider extends JresResourceRefConentProposalPovider {

	public OracleSpaceContentProposalProvider(IContentProposalProviderHelper helper, String resType, IARESProject project) {
		super(helper, resType, project);
	}

	@Override
	protected boolean filter(Object inputItem, Object element) {
		if (inputItem instanceof Map && element instanceof TableSpace) {
			@SuppressWarnings("rawtypes")
			Map map = (Map) inputItem;
			IARESResource res = (IARESResource) map.get(IResourceTable.TARGET_RESOURCE);
			TableSpace item = (TableSpace) map.get(IResourceTable.TARGET_OWNER);
			TableSpace elementItem = (TableSpace) element;
			if (res.getARESProject().equals(project) && item.getName().equals(elementItem.getName())) { //ͬ��Դ����Ҫ����
				return false;
			}
		}
		return true;
	}
	
}
