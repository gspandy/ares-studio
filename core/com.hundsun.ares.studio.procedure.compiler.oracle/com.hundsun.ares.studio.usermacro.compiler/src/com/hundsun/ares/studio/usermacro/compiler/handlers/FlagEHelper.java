/**
 * 
 */
package com.hundsun.ares.studio.usermacro.compiler.handlers;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * E��Ǵ���E��ǵ���ϢΪ����ŵĳ������壩����E����滻�ɴ����
 * 
 * @author yanwj06282
 *
 */
public class FlagEHelper implements IUserMacroFlagHelper {

	
	@Override
	public String genFlag(UserMacroToken token ,Map<Object, Object> context ,String content) {
		//ȡ�î��������±�λ�����磺<E>%1$s������Ҫȡ������ġ�1������ʾ��ǰ�ı����ǵ�һλ
		
		int index = Integer.parseInt(StringUtils.substring(content, StringUtils.indexOfIgnoreCase(content, "<E>")+4, StringUtils.indexOfIgnoreCase(content, "<E>")+5));
		if(index<=token.getUmParams().length){
			String errorName = token.getUmParams()[index-1];
			ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(token.getProject(), IMetadataRefType.ErrNo, errorName, true);
			if (info != null && info.getObject() != null) {
				ErrorNoItem noItem = (ErrorNoItem) info.getObject();
				if (noItem != null) {
					errorName = noItem.getNo();
				}
			}
			if (!StringUtils.equals(token.getUmParams()[index-1], errorName)) {
				token.getUmParams()[index-1] = errorName;
			}
		}else{
			throw new RuntimeException("�Զ���꣺["+token.getItem().getName()+"] ,������Ϣ��ƥ��!");
		}
		
		content = content.substring(0, StringUtils.indexOfIgnoreCase(content, "<E>")) + content.substring(StringUtils.indexOfIgnoreCase(content, "<E>")+3, content.length());
		return content;
	}

}
