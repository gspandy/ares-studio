/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.cellEditor.ARESContentProposal;

/**
 * @author qinyuan
 * <p>���ݿ� ҵ���߼� ������ʾ</p>
 * <p>�����ظ��ı�׼�ֶβ�Ӧ�ý�����ʾ </p>
 */
public class MetadataContentProposalHelperWipeOffRepeatStd extends MetadataContentProposalHelper{

	/**
	 * @param project
	 */
	public MetadataContentProposalHelperWipeOffRepeatStd(IARESProject project) {
		super(project);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.metadata.ui.editors.editingsupport.MetadataContentProposalHelper#getContent(com.hundsun.ares.studio.jres.model.metadata.MetadataItem, com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	protected String getContent(MetadataItem item, IARESResource resource) {
		//���ݿⲻӦ��֧�� �����ռ䷽ʽ�����ã������ظ��ı�׼�ֶβ�Ӧ�ý�����ʾ 
		if(duplicateList.contains(item.getName())){
			return "";
		}
		return item.getName();
	}
	
	@Override
	protected IContentProposal getProposal(String contents, int position,
			EObject item, IARESResource resource) {
		if (item instanceof MetadataItem) {
			MetadataItem mdItem = (MetadataItem) item;
			String content = getContent(mdItem, resource);
			// 2012-04-27 sundl ���Ϊ�ղ�������ʾ��Ŀ
			if (StringUtils.isEmpty(content))
				return null;
			if (!content.toUpperCase().contains(contents.toUpperCase())) {
				return null;
			}
			
			String shortDesc = mdItem.getChineseName();
			return new ARESContentProposal(content, shortDesc,getDescription(mdItem));
		}
		return null;
	}
	
	private String getDescription(MetadataItem mdItem){
		//��׼�ֶ���ʾ�����˵����Ϣ  TASK #8602 ��������б�׼�ֶβ���������ʾ�����˵����Ϣ
		if(mdItem instanceof StandardField){
			StandardField field = (StandardField)mdItem;
			StringBuffer text = new StringBuffer();
			String dictTypeStr = field.getDictionaryType();
			if(StringUtils.isNotBlank(dictTypeStr)){
				ReferenceInfo  dictReferenceInfo = ReferenceManager.getInstance().getFirstReferenceInfo(project,IMetadataRefType.Dict,dictTypeStr,true);
				if(dictReferenceInfo != null){
					DictionaryType objDictionaryType = (DictionaryType) dictReferenceInfo.getObject();
					if(objDictionaryType!=null){
						for(DictionaryItem item : objDictionaryType.getItems()){
							String value = StringUtils.defaultString(item.getValue());
							String chineseName = StringUtils.defaultString(item.getChineseName());
							text.append(objDictionaryType.getName());
							text.append(":");
							text.append(objDictionaryType.getChineseName());
							text.append("-");
							text.append(value);
							text.append(":");
							text.append(chineseName);
							text.append("\r\n");
						}
					}
				}
			}
			if(StringUtils.isNotBlank(text.toString()) && StringUtils.isNotBlank(field.getDescription())){
				text.append("\r\n");
				text.append(field.getDescription());
			}
			String desc = StringUtils.defaultString(StringUtils.defaultIfBlank(text.toString(), field.getDescription())) ;
			if(StringUtils.isNotBlank(desc)){
				return desc;
			}
		}
		return null;
	}
}
