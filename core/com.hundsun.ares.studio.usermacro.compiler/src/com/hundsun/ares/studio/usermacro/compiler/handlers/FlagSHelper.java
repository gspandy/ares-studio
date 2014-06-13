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
 * S��ǵĴ���
 * ��S��Ǵ������Ϣ��ƴ���ϲ���
 * 
 * @author yanwj06282
 *
 */
public class FlagSHelper implements IUserMacroFlagHelper {

	
	@Override
	public String genFlag(UserMacroToken token ,Map<Object, Object> context ,String content) {
		//ȡ�î��������±�λ�����磺<S>%1$s������Ҫȡ������ġ�1������ʾ��ǰ�ı����ǵ�һλ
		int index = Integer.parseInt(StringUtils.substring(content, StringUtils.indexOfIgnoreCase(content, "<S>")+4, StringUtils.indexOfIgnoreCase(content, "<S>")+5));
		int paramsSize = token.getUmParams().length;
		if (paramsSize > 1) {
			String errorCons = token.getUmParams()[paramsSize - 2];
			ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(token.getProject(), IMetadataRefType.ErrNo, errorCons, true);
			if (info != null && info.getObject() != null) {
				ErrorNoItem noItem = (ErrorNoItem) info.getObject();
				if (noItem != null) {
					token.getUmParams()[paramsSize - 1] = noItem.getMessage();
				}
			}
		}
		if(index<=token.getUmParams().length){
			String errorName = token.getUmParams()[index-1];
			if (!StringUtils.startsWith(errorName, "\"")) {
				errorName = "\"" + errorName;
			}
			if (!StringUtils.endsWith(errorName, "\"")) {
				errorName = errorName + "\"";
			}
			token.getUmParams()[index-1] = errorName;
		}else{
			throw new RuntimeException("�Զ���꣺["+token.getItem().getName()+"] ,������Ϣ��ƥ��!");
		}
		
		content = content.substring(0, StringUtils.indexOfIgnoreCase(content, "<S>")) + content.substring(StringUtils.indexOfIgnoreCase(content, "<S>")+3, content.length());
		return content;
	}

}
