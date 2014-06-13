/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.text.assistant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.text.IDocument;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.editor.text.TextUtil;

/**
 * @author wangxh
 * �������ͼ���������ʾ��
 */
public class ResourceAssistantLoader extends AbstractAssistantLoader {
	private String PREFIX = "[";
	private String SUFFIX = "]";
	
	private IARESResource resource;
	private String type = "";
	
	public ResourceAssistantLoader(IARESResource resource, String type){
		this.resource = resource;
		this.type = type;
	}
	
	@Override
	public List<String> loadAssitant(String text,IDocument doc,int offset) {
		List<String> allproposals = new ArrayList<String>();
		if(StringUtils.startsWith(text, PREFIX) && !TextUtil.isAfterMacro(doc, offset)){
			List<ReferenceInfo> references = ReferenceManager.getInstance().getReferenceInfos(resource.getARESProject(), type, true);
			if(references != null){
				if(filter != null){
					//����ǰ�ĳ�ʼ���������������ģ���������
					filter.init();
				}
				for(ReferenceInfo info : references){
					if(info != null){
						if(filter == null){
							allproposals.add(PREFIX + info.getRefName() + SUFFIX);
						}else if(filter.filter(info.getResource())){
							//��Ҫ���й��ˣ���Ҫ�������¼��֣�
							//LS����ʾLF��ֻ��ʾ����ģ�飩
							//LF��ʾLF��ֻ��ʾ����ģ�飩
							//AS��ʾAF,AP��ֻ��ʾ����ģ�飩
							//AF��ʾAF,AP��ֻ��ʾ����ģ�飩
							//AP��ʾAP��ֻ��ʾ����ģ�飩
							allproposals.add(PREFIX + info.getRefName() + SUFFIX);
						}
					}
				}
			}
		}
		return allproposals;
	}
}
