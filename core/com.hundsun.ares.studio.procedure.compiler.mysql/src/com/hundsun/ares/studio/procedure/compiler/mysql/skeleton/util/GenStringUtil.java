/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.mysql.skeleton.util;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liaogc
 *
 */
public class GenStringUtil {
	
	static private Pattern P_SELECT_OR_FROM = Pattern.compile("\\W(select|from)\\W", Pattern.CASE_INSENSITIVE);
		
		/**
		 * ��ȡһ��select sql�е�select����
		 * @param sqlStr
		 * @return
		 */
		public static String getMainSelectContent(String sqlStr)  {
			try {
				sqlStr = " " + sqlStr+ " "; // ��ʹ��ͷ����selectҲ�ܱ�����ƥ�䵽
				int begin = 0, end = 0;
				int level = 0; // ��ʾĿǰ�ڵڼ����select...from
				Matcher m = P_SELECT_OR_FROM.matcher(sqlStr);
				int start = 0;
				while (m.find(start)) {
					if (m.group().toLowerCase().indexOf("select") != -1) { // ƥ����select
						level++;
						if (level == 1) {
							begin = m.end() - 1;
						}
					} else { // ƥ����from
						level--;
						if (level == 0) {
							end = m.start() + 1;
							break;
						}
					}
					
					start = m.end();
				}
				
				if (end > begin) {
					return sqlStr.substring(begin, end);
				}
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "";
		}
		
		
		// ȡ��select��from�м�ı�������
		public static String getStrBetweenSelectAndFrom(String str) throws Exception{
			String returnStr = "";
			Stack<String> stack = new Stack<String>();
			String selectStr = "select";
			String tempStr = "";
			boolean isGetStr = false;
			
			Pattern p = Pattern.compile("(\\s+from\\s+)");
			Matcher m = p.matcher(str);
			if (m.find()) {
				int selectIndex = str.indexOf("(select ");
				int fromIndex = str.indexOf(" from ");
				
				if(selectIndex < 0 || selectIndex > fromIndex) {//select��䲻���ڣ�����select����ڵ�һ��from֮��
					returnStr = str.substring(0, fromIndex);
				} else {
					stack.push(selectStr);
					
					tempStr = str.substring(selectIndex+ 8, str.length());//��ȡ��һ���Ӳ�ѯselect֮����ַ�
					selectIndex = tempStr.indexOf("(select ");
					fromIndex = tempStr.indexOf(" from ");
					
					while(selectIndex >= 0 || !stack.isEmpty()) {
						if(0 <= selectIndex && selectIndex < fromIndex) {//�����ַ��������Ӳ�ѯ
							stack.push(selectStr);
							
							tempStr = tempStr.substring(selectIndex+ 8, tempStr.length());
						} else {
							if(!stack.isEmpty()) {
								stack.pop();
								
								tempStr = tempStr.substring(fromIndex+ 6, tempStr.length());
							} else {
								tempStr = tempStr.substring(fromIndex+6, tempStr.length());
								returnStr = str.substring(0, str.indexOf(tempStr));
								isGetStr = true;
							}
						}
						selectIndex = tempStr.indexOf("(select ");
						fromIndex = tempStr.indexOf(" from ");
					}
					
					if(selectIndex < 0) {
//						tempStr = tempStr.substring(fromIndex+ 6, tempStr.length());
						fromIndex = tempStr.indexOf(" from ");
						if(!isGetStr) {
							tempStr = tempStr.substring(fromIndex, tempStr.length());
							returnStr = str.substring(0, str.indexOf(tempStr));
						}
					}
				}
			}
			return returnStr;
		}

		// ȡ��select��from�м�ı�������
		public static String getStrBetSelectAndFrom(String str) {
			Pattern p = Pattern.compile("(\\s+from\\s+)");
			Matcher m = p.matcher(str);
			if (m.find()) {
				int lastFromIndex = str.lastIndexOf(" from ");//Ӧ�ý�ȡ�Ӳ�ѯ
				if(lastFromIndex != -1) {
					int lastWhereIndex = str.lastIndexOf(" where ");
					String backtemp = str.substring(lastFromIndex,lastWhereIndex);//���һ��from�����һ��where֮����ַ���
					String beforetemp = str.substring(0, lastFromIndex);
					while(backtemp.indexOf(" where ") != -1) {//���һ��from�����Ӳ�ѯ���ֶΣ�Ӧ�޳�
						lastFromIndex = beforetemp.lastIndexOf(" from ");//�޳��Ӳ�ѯ���˻ص������ڶ���from
		//				lastWhereIndex = backtemp.lastIndexOf(" where ");
						backtemp = beforetemp.substring(lastFromIndex, beforetemp.length());
						beforetemp = beforetemp.substring(0, lastFromIndex);
					}
					lastFromIndex = str.indexOf(backtemp);
					str = str.substring(0, lastFromIndex).trim();
				} else {
					str = str.substring(0, m.start()).trim();
				}
			}
			return str;
		}
}
