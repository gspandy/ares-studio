package com.hundsun.ares.studio.engin.macrohandler;

import com.hundsun.ares.studio.engin.skeleton.ISkeletonInput;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

public interface IMacroTokenFilter{

	/**
	 * ������ͼ���˺�
	 * @param input
	 * @param token
	 * @return
	 */
	public boolean filte(ISkeletonInput input,IMacroToken token);
	
	/**
	 * ������ͼ��ȡ�궨��
	 * @param input
	 * @return
	 */
	public String[] getMacroDefine(ISkeletonInput input);
}
