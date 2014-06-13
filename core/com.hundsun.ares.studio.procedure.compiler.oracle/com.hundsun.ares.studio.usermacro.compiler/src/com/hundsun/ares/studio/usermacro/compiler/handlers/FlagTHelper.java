/**
 * 
 */
package com.hundsun.ares.studio.usermacro.compiler.handlers;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * T��ǵĴ���
 * 
 * @author yanwj06282
 *
 */
public class FlagTHelper implements IUserMacroFlagHelper {

	
	@Override
	public String genFlag(UserMacroToken token ,Map<Object, Object> context ,String content) {
		//ȡ�î��������±�λ�����磺<T>%1$s������Ҫȡ������ġ�1������ʾ��ǰ�ı����ǵ�һλ
		int index = Integer.parseInt(StringUtils.substring(content, StringUtils.indexOfIgnoreCase(content, "<T>")+4, StringUtils.indexOfIgnoreCase(content, "<T>")+5));
		String cur = token.getCur();
		if (!StringUtils.startsWith(cur, CUR_REFIX)) {
			cur = CUR_REFIX + cur;
		}
		String[] temps = new String[token.getUmParams().length + 1];
			System.arraycopy(token.getUmParams(), 0, temps, 0, index-1);
			temps[index-1] = cur;
			System.arraycopy(token.getUmParams(), index-1, temps, index, token.getUmParams().length-index+1);
			token.setUmParams(temps);
		
		content = content.substring(0, StringUtils.indexOfIgnoreCase(content, "<T>")) + content.substring(StringUtils.indexOfIgnoreCase(content, "<T>")+3, content.length());
		return content;
	}

}
