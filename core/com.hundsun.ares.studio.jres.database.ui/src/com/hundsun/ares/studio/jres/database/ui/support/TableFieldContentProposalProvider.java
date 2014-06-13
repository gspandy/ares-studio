/**
 * Դ�������ƣ�MetadataContentProposalProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.support;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.ui.assist.JresResourceRefConentProposalPovider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * ��ʾԪ�������õ�ʱ��ʹ�õ�ProposalContentProvider
 * @author yanwj06282
 */
public class TableFieldContentProposalProvider extends JresResourceRefConentProposalPovider {

	public TableFieldContentProposalProvider(IContentProposalProviderHelper helper, String resType, IARESProject project) {
		super(helper, resType, project);
	}

	@Override
	protected boolean filter(Object inputItem, Object element) {
		if (inputItem instanceof Map && element instanceof TableResourceData) {
			@SuppressWarnings("rawtypes")
			Map map = (Map) inputItem;
			IARESResource res = (IARESResource) map.get(IResourceTable.TARGET_RESOURCE);
			TableResourceData tdb;
			try {
				tdb = res.getInfo(TableResourceData.class);
				if (res.getARESProject().equals(project) && StringUtils.equals(((TableResourceData)element).getFullyQualifiedName(), tdb.getFullyQualifiedName())) { //ͬ��Դ����Ҫ����
					return true;
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
