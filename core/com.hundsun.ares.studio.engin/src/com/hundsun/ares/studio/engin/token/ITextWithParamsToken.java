package com.hundsun.ares.studio.engin.token;

import java.util.List;

/**
 * �����������ı�token
 * @author lvgao
 *
 */
public interface ITextWithParamsToken extends ICodeToken{

	/**
	 * ��ȡʹ�ù��Ĳ���
	 * @return
	 */
	public List<String> getUsedParams();
}
