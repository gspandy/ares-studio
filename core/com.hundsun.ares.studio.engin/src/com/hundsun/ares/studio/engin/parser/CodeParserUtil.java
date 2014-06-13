/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.engin.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qinyuan
 *
 */
public class CodeParserUtil {

	/**ƥ�䳣��1**/
	private static Pattern CONST_PATTERN = Pattern.compile("(([^<]|^)<[\\w\\d_]+>([^\\[]|$))", Pattern.MULTILINE);
	/**ƥ�䳣��2**/
	private static Pattern CONST_PATTERN2 = Pattern.compile("<[\\w\\d_]+>");
	/**
	 * �������г������
	 * 
	 * @param strText
	 * @return
	 */
	public static List<String> findConstAll(String strText) {
		
		List<String> ret = new ArrayList<String>();
		Matcher m = CONST_PATTERN.matcher(strText);
		int index = 0;
		while ( m.find(index) ) {
			String group = m.group();
			Matcher m2 = CONST_PATTERN2.matcher(group);
			if (m2.find()) {
				ret.add(m2.group());
			}
			index = m.start() + m2.end() - 1;
		}
		
		return ret;
	}
	
	/**
	 * �����ַ��������еı�׼�ֶ�(��@��ͷ���ֶ�)
	 * @param str
	 * @return
	 */
	public static List<String> findStandardField(String str) {
		List<String> stdFlds = new ArrayList<String>();
		Pattern p = Pattern.compile("@[\\w\\d_\\.]+");
		Matcher m = p.matcher(str);
		Pattern p1 = Pattern.compile("[\\w\\d_'\"]");//���������жϣ�������ֱ����Ϊ�ַ�������
		while (m.find()) {
			//2011��11��29��19:47:11 ����Ҫ��  ���@ǰΪ��ĸ���ֻ��»����򲻽��б�׼�ֶν���
			// �˴����֤ȯ��������
			int mStart = m.start();
			
			if(mStart == 0 || (mStart > 0 && !p1.matcher(str.substring(mStart-1, mStart)).matches())) {
				stdFlds.add(m.group().substring(1));
			}
		}
		return stdFlds;
	}
	
	/**
	 * �����ַ��������еı�׼�ֶ�(��@��ͷ���ֶ�)
	 * @param str
	 * @return
	 */
	public static boolean findResultSetStr(String str) {
		Pattern p = Pattern.compile("lpResultSet->");
		Matcher m = p.matcher(str);
		while (m.find()) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args){
		boolean b = findResultSetStr(" while (!lpResultSet->IsEOF())");
		System.out.println(b);
	}
}
