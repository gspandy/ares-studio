package com.hundsun.ares.studio.engin.parser;

import java.util.Iterator;
import java.util.Map;

import com.hundsun.ares.studio.engin.token.ICodeToken;

public interface IPseudocodeParser {

	/**
	 * 
	 * @param pseudocode  α����
	 * @param i 
	 * @param context     ������
	 * @return
	 */
	public Iterator<ICodeToken> parse(String pseudocode,int commentType, Map<Object, Object> context)throws Exception;
	
}
