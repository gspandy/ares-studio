package com.hundsun.ares.studio.jres.basicdata.ui.proposal;

import org.eclipse.emf.ecore.EAttribute;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

public class BasicDataContentProposalProvider extends
		AresContentProposalProvider {
	private EAttribute attribute;
	private IARESResource resource;
	public BasicDataContentProposalProvider(IContentProposalProviderHelper helper,EAttribute attribute,IARESResource resource) {
		super(helper);
		this.attribute = attribute;
		this.resource = resource;
	}

	@Override
	public void updateContent(Object element) {
		if(element instanceof MetadataCategory){//����Ƿ�����������ʾ
			setInput(new Object[0]);
		}else if(attribute != null && resource != null){
			//�����ֵ���Ŀ���öԻ���ѡ��
			/*
			if(BasicDataEpackageUtil.isStandardField(attribute)){
				StandardField stdField = JRESResProviderUtil.getMetadataModel(resource.getARESProject(), attribute.getName(),IMetadataRefType.StdField , StandardField.class);
				if(null != stdField && StringUtils.isNotBlank(stdField.getDictionaryType())){
					DeDictionaryType dicType = MetadataUtil.decrypt(stdField, resource).getDictionaryType();
					setInput(dicType.getItems().toArray());
				}
			}
			
		*/}
		
	}
	
	public void setInput(Object[] input) {
		this.input = input;
	}
	
	public Object[] getInput() {
		return input;
	}
	/**
	 * @return the attribute
	 */
	public EAttribute getAttribute() {
		return attribute;
	}

	/**
	 * @return the resource
	 */
	public IARESResource getResource() {
		return resource;
	}


}
