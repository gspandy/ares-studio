/**
 * Դ�������ƣ�OracleSpaceColumnProposalProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.resources
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.viewer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.IContentProposal;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleRefType;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleSpaceResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpaceRelation;
import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;

/**
 * @author yanwj06282
 *
 */
public class OracleSpaceColumnProposalProvider extends
		AresContentProposalProvider {
	private String type ;
	
	public OracleSpaceColumnProposalProvider(String type) {
		super();
		this.type = type;
	}

	@Override
	public void updateContent(Object element) {
		List<ExtensibleModel>input = new ArrayList<ExtensibleModel>();
		if(element != null){
			if (element instanceof TableSpaceRelation) {
				OracleSpaceResourceData oracleSpaceRD = (OracleSpaceResourceData) ((TableSpaceRelation)element).eContainer();
				if(StringUtils.equals(type, IOracleRefType.Space)){
					input.addAll(oracleSpaceRD.getSpaces());
				}
			}
		}
		setInput(input.toArray(new ExtensibleModel[0]));
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider#getProposals(java.lang.String, int)
	 */
	@Override
	public IContentProposal[] getProposals(String contents, int position) {
//		updateContent(null);
		List<IContentProposal> proposals = new ArrayList<IContentProposal>();
		if (input != null) {
			for (Object obj : input) {
				if(obj instanceof TableSpace){
					TableSpace item = (TableSpace)obj;
					if (StringUtils.startsWithIgnoreCase(item.getName(), contents)) {
						IContentProposal proposal = new columnProposal(item.getName(), item.getChineseName());
						if (proposal != null) {
							proposals.add(proposal);
						}
					}
				}
			}
		}
		return proposals.toArray(new IContentProposal[0]);
	}
	
	//��ǰ���е��ֶκ�������ContentProposal
	protected class columnProposal implements IContentProposal{
		String name = "";
		String label = "";
		
		
		public columnProposal(String name, String description) {
			super();
			this.name = name;
			this.label = name + "--->" + description;
		}

		@Override
		public String getContent() {
			return name;
		}

		@Override
		public int getCursorPosition() {
			return name.length();
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public String getDescription() {
			return null;
		}
		
	}
}
