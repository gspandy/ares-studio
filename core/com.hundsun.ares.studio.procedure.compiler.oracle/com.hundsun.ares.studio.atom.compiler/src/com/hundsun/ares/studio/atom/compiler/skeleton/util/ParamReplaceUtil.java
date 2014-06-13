/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.skeleton.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.token.ICodeToken;

/**
 * @author lvgao
 *
 */
public class ParamReplaceUtil {

	/**
	 * ��������滻
	 * @param prefix        ǰ׺��proc������:,���������""
	 * @param content
	 * @param params
	 * @param context
	 * @return
	 */
	public static String handleParams(String prefix,String content,String[] params,Set<String> inoutParamList){
		
		StringBuffer ret = new StringBuffer();
		//��proc����һ��
		Iterator<ICodeToken> codes = PseudoCodeParser.parseEx(content, PseudoCodeParser.CPP_COMMENT_PATTERN, PseudoCodeParser.CPP_STRING_PATTERN, new HashMap<Object, Object>() );
		while(codes.hasNext()) {
			ICodeToken code = codes.next();
			String c = code.getContent();
			if(code.getType() != ICodeToken.COMMENT /*&&
					code.getType() != ICodeToken.STRING*/) {//��ע�Ͳ���Ҫ�滻
				for(String para:params){
					if(inoutParamList.contains(para)){
						String rStr = String.format("%s%s%s", prefix, "p_",para);
						c = replaceVariable(c,para, rStr);
					}else{
						String rStr = String.format("%s%s%s", prefix, "v_",para);
						c = replaceVariable(c,para, rStr);
					}
				}
			}
			ret.append(c);
		}
		return ret.toString();
	}
	
	/**
	 * �����������
	 * @param prefix
	 * @param content
	 * @param objects
	 * @return
	 */
	public static String handleObjectParams(String prefix,String content,List<String> objects){
		StringBuffer ret = new StringBuffer();
	Iterator<ICodeToken> codes = PseudoCodeParser.parseEx(content, PseudoCodeParser.CPP_COMMENT_PATTERN, PseudoCodeParser.CPP_STRING_PATTERN, new HashMap<Object, Object>() );
	while(codes.hasNext()) {
		ICodeToken code = codes.next();
		String c = code.getContent();
		if(code.getType() != ICodeToken.COMMENT /*&&
				code.getType() != ICodeToken.STRING*/) {//��ע�Ͳ���Ҫ�滻
			//��ԭ�����������ݴ���
			for(String para:objects){
				String rStr = String.format("%s%s%s", prefix, "v_",para+"ResultSet");
				c = replaceVariable(c,para+"ResultSet", rStr);
			}
			for(String para:objects){
				String rStr = String.format("%s%s%s", prefix, "v_",para+"ResultSet");
				c = replaceVariable(c,para, rStr);
			}
		}
		ret.append(c);
	}
	return ret.toString();
	}
	
	
	/**
	 * �������������
	 * @param prefix
	 * @param content
	 * @param specialsParams
	 * @return
	 */
	public static String handleSpecialParams(String prefix,String content,List<String>specialsParams){
		StringBuffer ret = new StringBuffer();
	Iterator<ICodeToken> codes = PseudoCodeParser.parseEx(content, PseudoCodeParser.CPP_COMMENT_PATTERN, PseudoCodeParser.CPP_STRING_PATTERN, new HashMap<Object, Object>() );
	while(codes.hasNext()) {
		ICodeToken code = codes.next();
		String c = code.getContent();
		if(code.getType() != ICodeToken.COMMENT /*&&
				code.getType() != ICodeToken.STRING*/) {//��ע�Ͳ���Ҫ�滻
			//��ԭ�����������ݴ���
			for(String para:specialsParams){
				String rStr = String.format("%s%s%s", prefix, "",para);
				c = replaceVariable(c,para, rStr);
			}
			
		}
		ret.append(c);
	}
	return ret.toString();
	}
	/**
	 * �����滻
	 * @param content
	 * @param params
	 * @param context
	 * @return
	 */
	public static String handleParams(String content,String[] params,Set<String> inoutParamList){
		return handleParams("", content, params, inoutParamList);
	}

	/**
	 * �滻��׼�ֶ�
	 * @param code
	 * @param varName
	 * @param replaceName
	 * @return
	 */
	public static String replaceVariable(String code, String varName, String replaceName) {
		Pattern p = Pattern.compile("@" + varName+"[^a-zA-Z\\d_@=(){};,\\[\\]\\+\\-\\>\\<]*");//������Ҫȫ��ƥ�䣬���滻@user_idʱ��ֻ�滻@user_id���������滻@user_id1
		//@result_num=@result_num + 1;�������Ҳ��֧�֣�����@�����󣬿�����=(,+-�ȷ��ţ���Щ�ַ�Ҳ��ȥ��
		StringBuffer sbRet = new StringBuffer();
		Matcher m = p.matcher(code);
		int lastPos = 0;
		while (m.find()) {
				if (m.start() > lastPos) {
					sbRet.append(code.substring(lastPos, m.start()));
				}
				sbRet.append(m.group().replaceAll("@" + varName, replaceName));
				lastPos = m.end();
			
		}
		if (lastPos < code.length()) {
			sbRet.append(code.substring(lastPos));
		}
		
		return sbRet.toString();
	}
	
	/**
	 * �滻����
	 * @param code
	 * @param varName
	 * @param replaceName
	 * @return
	 */
	public static String replaceConstant(String code, String varName, String replaceName) {
		Pattern p = Pattern.compile("@" + varName + "[^\\w\\d_]");
		StringBuffer sbRet = new StringBuffer();
		Matcher m = p.matcher(code);
		int lastPos = 0;
		while (m.find()) {
			if (m.start() > lastPos) {
				sbRet.append(code.substring(lastPos, m.start()));
			}
			sbRet.append(m.group().replaceAll("@" + varName, replaceName));
			lastPos = m.end();
		}
		if (lastPos < code.length()) {
			sbRet.append(code.substring(lastPos));
		}
		
		return sbRet.toString();
	}
	
	/**
	 * ��ȥ��ע�ͺ�Ĵ�����һ���ַ����滻������һ���ַ���
	 * @param content ����
	 * @param targetStr Ŀ���ַ���
	 * @param replaceStr �滻�ַ���
	 * @return
	 */
	public static String replaceContentWithNotString(String content, String targetStr, String replaceStr){
		StringBuffer ret = new StringBuffer();
		Iterator<ICodeToken> codes = PseudoCodeParser.parseEx(content, PseudoCodeParser.CPP_COMMENT_PATTERN, PseudoCodeParser.CPP_STRING_PATTERN, new HashMap<Object, Object>() );
		while(codes.hasNext()) {
			ICodeToken code = codes.next();
			String c = code.getContent();
			if(code.getType() != ICodeToken.COMMENT/* &&
					code.getType() != ICodeToken.STRING*/) {//��ע�Ͳ���Ҫ�滻
				c = c.replaceAll(targetStr, replaceStr);
			}
			ret.append(c);
		}
		return ret.toString();
	}
	
	
	private static Pattern CONST_PATTERN = Pattern.compile("(([^<]|^)<[\\w\\d_]+>([^\\[]|$))", Pattern.MULTILINE);
	private static Pattern CONST_PATTERN2 = Pattern.compile("<[\\w\\d_]+>");
	/**
	 * �������г������
	 * 
	 * @param strText
	 * @return
	 */
	public static List<String> findConstAll(String strText) {
		
	List<String> ret = new ArrayList<String>();
		
		Iterator<ICodeToken> codes = PseudoCodeParser.parseEx(strText, PseudoCodeParser.CPP_COMMENT_PATTERN, PseudoCodeParser.CPP_STRING_PATTERN, new HashMap<Object, Object>() );
		while(codes.hasNext()) {
			ICodeToken code = codes.next();
			String c = code.getContent();
			if(code.getType() != ICodeToken.COMMENT /*&&
					code.getType() != ICodeToken.STRING*/) {//��ע����Ҫ���
				Matcher m = CONST_PATTERN.matcher(c);
				int index = 0;
				while ( m.find(index) ) {
					String group = m.group();
					Matcher m2 = CONST_PATTERN2.matcher(group);
					if (m2.find()) {
						ret.add(m2.group());
					}
					index = m.start() + m2.end() - 1;
				}
			}
		}
		return ret;
	}
	
	
	public static StringBuffer formatInsert(String str){
		int haveAt = 0;
		if(str.indexOf("@") != -1)
			haveAt = 1;
		StringBuffer buffer = new StringBuffer(str);
		for(int i = 0 ;20-buffer.length()-haveAt>0;i++){
			buffer.append(" ");
		}
		return buffer;
	 }
	
	public static StringBuffer formatInsert(String str,int maxLength){
		int haveAt = 0;
		if(str.indexOf("@") != -1)
			haveAt = 1;
		StringBuffer buffer = new StringBuffer(str);
		int length = buffer.length();
		if(maxLength>length){
			for(int i = 0 ;i<(maxLength-length-haveAt);i++){
				buffer.append(" ");
			}
		}
		
		return buffer;
	 }
	
}
