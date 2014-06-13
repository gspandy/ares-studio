/**
 * Դ�������ƣ�OracleSpaceContentProposalHelper
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
// 2012-2-22 sundl �޸����µ�ContentProposal���󣬿���ʵ���ò�ͬ��ɫ��ʾ������
// ����ɾ�����ô�����䡣
package com.hundsun.ares.studio.jres.database.ui.support;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.service.DataServiceManager;
import com.hundsun.ares.studio.jres.metadata.service.IMetadataService;
import com.hundsun.ares.studio.jres.metadata.service.IStandardField;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.ui.assist.JRESContentPorposalHelper;
import com.hundsun.ares.studio.ui.cellEditor.ARESContentProposal;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * ��ռ���ʾ
 * @author yanwj06282
 */
public class TableFieldContentProposalHelper extends JRESContentPorposalHelper implements IContentProposalProviderHelper{

	@Override
	protected IContentProposal getProposal(String contents, int position, EObject item, IARESResource resource) {
		if (item instanceof TableColumn && resource != null) {
			TableColumn column = (TableColumn) item;
			String content = column.getName();
			String chineseName = StringUtils.EMPTY;
			if (content == null || !content.toUpperCase().contains(contents.toUpperCase())) {
				return null;
			}
			
			IMetadataService metadataService = DataServiceManager.getInstance().getService(resource.getARESProject(), IMetadataService.class);
			IStandardField stdField = metadataService.getStandardField(column.getFieldName());
			if (stdField != null) {
				chineseName = stdField.getChineseName();
			}
			return new ARESContentProposal(content, chineseName);
		}
		return null;
	}

}
