/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

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
	public static String handleParams(String prefix,String content,String[] params,Set<String> inoutParamList){StringBuffer ret = new StringBuffer();
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
		Pattern p = Pattern.compile("@" + varName+"[^\\@w\\d_]+");
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
	
	/**
	 * ���Դ��и�����־λ���ֶ�.
	 * ���table����his_��fil��r_��rl_��ͷ�ģ����ǻ᷵�������ֶ�
	 * @param flag ��־λ
	 * @return ���Ժ��
	 */
	public static TableColumn[] getFieldsWithoutFlag(TableResourceData table , String flag,String tableName) {
		if(tableName.indexOf(".") >= 0){
			tableName = tableName.substring(tableName.lastIndexOf(".")+1);
		}
		if(!tableName.isEmpty()){
			tableName = tableName.trim();
			if(tableName.startsWith("his_")||
					tableName.startsWith("fil_")||
					tableName.startsWith("r_")||
					tableName.startsWith("rl_")){
				return table.getColumns().toArray(new TableColumn[0]);
			}
		}
		List<TableColumn> filtered = new ArrayList<TableColumn>();
		char[] flags = flag.toCharArray();
		;
		for (TableColumn field : table.getColumns()) {
			boolean contains = false;
			for (int ch : flags) {
				if (field.getMark() != null) {
					if (field.getMark().indexOf(ch) != -1) {
						contains = true;
						break;
					}
				}
			}
			if (!contains) {
				filtered.add(field);
			}
		}
		return filtered.toArray(new TableColumn[filtered.size()]);
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
	
}
