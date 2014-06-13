package com.hundsun.ares.studio.cres.text.assistant;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class ConstantAssistantLoader extends AbstractAssistantLoader {
	
	IARESResource resource;
	
	public ConstantAssistantLoader (IARESResource resource){
		this.resource = resource;
	}
	
	@Override
	public List<String> loadAssitant(String text,IDocument doc,int offset) {
		try {
			int preOffset = offset-text.length()-1;
			if(preOffset < 0){
				//α���뿪ʼ��
				return getAllConstants("");
			}else{
				char c = doc.getChar(preOffset);
				//���ǰһ���ֶ��ǻ��пո�
				if (c == '\r' || c == '\t' || c == ' ' || c == '\n'){
					return getAllConstants("");
				}else if(doc.getChar(preOffset+1) == '['){
					//������ʾ������ʽ��[����������][ERR_630001][����ϵͳ������־ʧ��]
					return getAllConstants("[");
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * ��ȡ���г���
	 * @param prefix ǰ׺����[���Ժ���ܻ�����=֮��ģ�
	 * @return
	 */
	private List<String> getAllConstants(String prefix){
		List<String> contants = new ArrayList<String>();
		//�û�����
		List<ReferenceInfo> infos = ReferenceManager.getInstance().getReferenceInfos(resource.getARESProject(), IMetadataRefType.Const, true);
		//����ų���
		infos.addAll(ReferenceManager.getInstance().getReferenceInfos(resource.getARESProject(), IMetadataRefType.ErrNo, true));
		//�����ֵ䳣��
		infos.addAll(ReferenceManager.getInstance().getReferenceInfos(resource.getARESProject(), IMetadataRefType.Dictionary_Const, true));
		for(ReferenceInfo info : infos){
			contants.add(prefix + info.getRefName());
		}
		return contants;
	}

}
