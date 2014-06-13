package com.hundsun.ares.studio.engin.skeleton;

import java.util.Iterator;
import java.util.Map;

import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.IPseudocodeParser;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

public interface ISkeleton {

	/**
	 * ��ȡ��ͼtoken
	 * @param resource
	 * @param context
	 * @return
	 */
	public Iterator<ICodeToken> getSkeletonToken()throws Exception;
	
	/**
	 * ��������ʱ������
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> getRuntimeContext()throws Exception;
	
	
	
	/**
	 * ���к�ļ��
	 * @return
	 */
	public Object[] postValidate();
	
	
	
	/**
	 * ������ͨ�ı�token
	 * @param token
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public ICodeToken handleTextToken(ICodeToken token,Map<Object, Object> context)throws Exception;
	
	/**
	 * ��ȡ��token�Ĵ�����
	 * @param macro      ��
	 * @param context   ������
	 * @return
	 * @throws Exception
	 */
	public IMacroTokenHandler getMacroTokenHandler(IMacroToken macro,Map<Object, Object> context)throws Exception;

	
	/**
	 * ��ȡα���������
	 * @param token
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public IPseudocodeParser getPseudocodeParser(ICodeToken token,Map<Object, Object> context)throws Exception;
	
	//	public boolean IsNeedFormat();
	
	/**
	 * ͳһ������@����ת��Ϊp,v������<>�������ȵ�ֵ����
	 */
	public void onFinish(Map<Object, Object> context)throws Exception;
	
	
	
	/**
	 * ��ȡע������
	 */
	public int getCommentType();
}
