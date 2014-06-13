package com.hundsun.ares.studio.usermacro.compiler.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.util.TypeRule;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;


public class UserMacroUtil {
	
//	private static final String param_regx = "\\[(('[^']*')|(\"((\\\\\")|([^\"]))*\")|([^\\]\\[]*))\\]";
	
	/**
	 * ����ƥ�人�ֻ���Ӣ����ĸ(��Сд)
	 * 
	 */
	private static final String PARAM_REGX = "\\[([\u4e00-\u9fa5|A-Z|a-z|0-9]?)+([\u4e00-\u9fa5|A-Z|a-z])+([\u4e00-\u9fa5|A-Z|a-z|0-9]?)+\\]";
	
	private static final String FLAG_REGX = "<[A-Z|a-z]+>";
	
//	private static final String param_regx = "\\[[\u4e00-\u9fa5]+\\]";
	
	public static List<String> getParamList(String content){
		ArrayList<String> marcoIndex = new ArrayList<String>();
		if(!StringUtils.isBlank(content) && content.indexOf("[") != -1 ){
			Pattern pattern = Pattern.compile(PARAM_REGX);
			Matcher m = pattern.matcher(content);
			while (m.find()) {
				marcoIndex.add(m.group());
			}
		}
		return marcoIndex;
	}
	
	public static Set<String> getParamSet(String content){
		Set<String> marcoIndex = new LinkedHashSet<String>();
		if(!StringUtils.isBlank(content) && content.indexOf("[") != -1 ){
			Pattern pattern = Pattern.compile(PARAM_REGX);
			Matcher m = pattern.matcher(content);
			while (m.find()) {
				marcoIndex.add(m.group());
			}
		}
		return marcoIndex;
	}

	public static String getFormatStr(String text,List<String> marcoIndex) {
		if(StringUtils.isBlank(text)){
			return "";
		}
		text = text.replaceAll("%", "%%");
		StringBuffer buffer = new StringBuffer();
		Pattern pattern = Pattern.compile(PARAM_REGX);
		Matcher m = pattern.matcher(text);
		while (m.find()) {
			String key = m.group();
			if(!marcoIndex.contains(key)){
				marcoIndex.add(key);
			}
			m.appendReplacement(buffer, "%"+(marcoIndex.indexOf(key) + 1 ) +"\\$s");
		}
		m.appendTail(buffer);
		return buffer.toString();
	}
	
	public static String[] getFormatFlag(String text) {
		List<String> flagStrs = new ArrayList<String>();
		if(StringUtils.isBlank(text)){
			return flagStrs.toArray(new String[0]);
		}
		Pattern pattern = Pattern.compile(FLAG_REGX);
		Matcher m = pattern.matcher(text);
		while (m.find()) {
			String key = m.group();
			if (StringUtils.isNotBlank(key)) {
				if (flagStrs.contains("<T>")) {
					continue;
				}
				//T��Ƿ�����ͷ
				if (StringUtils.equalsIgnoreCase("<T>", key)) {
					flagStrs.add(0, key);
				}else {
					flagStrs.add(key);
				}
			}
		}
		return flagStrs.toArray(new String[0]);
	}
	
	/**
	 * ��ȡ��������
	 * 
	 * @param context
	 * @param fieldName
	 * @return
	 */
	public static String getDataType(IARESProject project , Map<String, String> context , String fieldName){
		String dataType = context.get(fieldName);
		if(StringUtils.isBlank(dataType)){
			dataType = getRealDataType(fieldName, project, null); 
		}
		if (TypeRule.typeRuleChar(dataType)) {
			return "%c";
		}else if (TypeRule.typeRuleCharArray(dataType)) {
			return "%s";
		} else if (TypeRule.typeRuleFloat(dataType) || TypeRule.typeRuleDouble(dataType)) {
			String[] temp = StringUtils.substringsBetween(dataType, ",", ")");
			if (temp == null || temp.length == 0) {
				temp = StringUtils.substringsBetween(dataType, ",", "]");
				if (temp != null && temp.length > 0) {
					return "%."+temp[0]+"f";
				}
			}else {
				return "%."+temp[0]+"f";
			}
			return "%f";
		}else {
			return "%d";
		}
	}
	
	/**
	 * ���ݱ�׼�ֶ����ƻ����ʵ����
	 * 
	 * @param stdName
	 * @param project
	 * @return
	 */
	public static String getRealDataType(String stdName, IARESProject project,
			String type) {
		if (StringUtils.isBlank(type)) {
			type = MetadataServiceProvider.C_TYPE;
		}
		StandardDataType stdType = null;
		try {
			stdType = MetadataServiceProvider
					.getStandardDataTypeOfStdFieldByName(project, stdName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BusinessDataType busType = null;
		try {
			busType = MetadataServiceProvider
					.getBusinessDataTypeOfStdFieldByName(project, stdName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ((stdType != null) && (busType != null))// ��׼�ֶ�
		{
			String dataType = StringUtils.defaultIfBlank(
					stdType.getValue(type), "");
			int length = 0;
			if (StringUtils.isNotBlank(busType.getLength())) {
				try {
					length = NumberUtils.toInt(busType.getLength(), 0) + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return dataType = dataType.replace("$L", length + "");
			}
			return dataType;
			/*
			 * if(busType.getPrecision() != null){ int precision = 0; try {
			 * precision = NumberUtils.toInt(busType.getPrecision(), 0) ; }
			 * catch (Exception e) { e.printStackTrace(); } dataType =
			 * dataType.replace("$P", precision + ""); return dataType;
			 * 
			 * }
			 */

		}
		return StringUtils.EMPTY;
	}
	
	public static String genCode(String paraDefine,String txt,String[] params){
		List<String> index = UserMacroUtil.getParamList(paraDefine);
		String str = UserMacroUtil.getFormatStr(txt, index);
		return genCode(str, index, params);
	}
	
	/**
	 * ���ɴ���
	 * @param str
	 * @param index
	 * @param params
	 * @return
	 */
	public static String genCode(String str,List<String> index,String[] params){
		List<String> inputList = new ArrayList<String>();
		for(int i = 0;i < index.size() ; i ++){
			if(i < params.length){
				inputList.add(params[i]);
			}else{
				inputList.add("{" + (i+1) +"}");
			}
		}
		//�п����û����в��������ʱ��ʹ����@���ţ�ʹ���û����ʱ���ִ�@���ţ�����@�ظ�������
		return String.format(str, inputList.toArray()).replaceAll("@@", "@");
	}
	
	
	public static void main(String[] args) {
		ArrayList<String> marcoIndex = new ArrayList<String>();
		String s = "a[][sa][��][1222aaa2][��a][��ʲ��a1][1][1212][a1231]";
		Pattern p = Pattern.compile(PARAM_REGX);
		Matcher m = p.matcher(s);
		while (m.find()) {
			marcoIndex.add(m.group());
		}
		for(String idx : marcoIndex){
			System.out.println(idx);
		}
	}
	/**
	 * ��������滻
	 * @param prefix        ǰ׺��proc������:,���������""
	 * @param content
	 * @param params
	 * @param context
	 * @return
	 */
	public static String handleParams(String prefix,String content,String[] params,List<String> inoutParamList){
		
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
	
}
