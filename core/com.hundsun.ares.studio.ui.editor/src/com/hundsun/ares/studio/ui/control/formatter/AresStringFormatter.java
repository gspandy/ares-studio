/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.control.formatter;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.nebula.widgets.formattedtext.StringFormatter;
import org.eclipse.swt.events.VerifyEvent;

import com.hundsun.ares.studio.core.model.ICreateInstance;

public class AresStringFormatter extends StringFormatter implements ICreateInstance<AresStringFormatter>, Cloneable{
	
	/**
	 * �ַ�����󳤶�
	 */
	int maxLen = Integer.MAX_VALUE;
	/**
	 * �����ַ���СASC��
	 */
	int minAsc = 0;
	/**
	 * �����ַ����ASC��
	 */
	int maxAsc = Integer.MAX_VALUE;
//	/**
//	 * �����ַ�������������ʽ
//	 */
//	String expression = null;
	/**
	 * �ų��ַ�
	 */
	Set<Character> exclude = new HashSet<Character>();

	
	
	
	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public int getMinAsc() {
		return minAsc;
	}

	public void setMinAsc(int minAsc) {
		this.minAsc = minAsc;
	}

	public int getMaxAsc() {
		return maxAsc;
	}

	public void setMaxAsc(int maxAsc) {
		this.maxAsc = maxAsc;
	}

//	public String getExpression() {
//		return expression;
//	}
//
//	public void setExpression(String expression) {
//		this.expression = expression;
//	}

	public void addExclude(char[] ch) {
		for(char c:ch){
			exclude.add(c);
		}
	}

	@Override
	public void verifyText(VerifyEvent e) {
		String newString = text.getText();
		newString = newString.substring(0,e.start)+ e.text+ newString.substring(e.end,newString.length());
		//�����󳤶�
		if(newString.length() > maxLen){
			e.doit = false;
			beep();
			return;
		}
		//����Ƿ���ASC�뷶Χ��        ����ų��ַ�
		for(char c:e.text.toCharArray()){
			if(c < minAsc || c > maxAsc || exclude.contains(c)){
				e.doit = false;
				beep();
				return;
			}
		}
//		//���������ʽ
//		if(!newString.matches(expression)){
//			e.doit = false;
//			beep();
//			return;
//		}
	}

	public AresStringFormatter getNewInstance() {
		try {
			return (AresStringFormatter) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new AresStringFormatter();
	}
}
