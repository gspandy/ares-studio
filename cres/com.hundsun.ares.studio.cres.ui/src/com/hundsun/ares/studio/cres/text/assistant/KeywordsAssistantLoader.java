/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.text.assistant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * @author wangxh
 * ����һЩ�ؼ��ֵ���ʾ
 */
public class KeywordsAssistantLoader extends AbstractAssistantLoader {

	//Ŀǰֻ��lpResultSet,�Ժ�����Ҫ��������
	private final static String[] keywords = new String[]{"lpResultSet"};
	
	@Override
	public List<String> loadAssitant(String text, IDocument doc, int offset) {
		List<String> allproposals = new ArrayList<String>();
		try {
			int preOffset = offset-text.length()-1;
			if(preOffset < 0){
				//α������ʼλ��
				allproposals.addAll(Arrays.asList(keywords));
			}else{
				char c = doc.getChar(preOffset);
				if (c == '\r' || c == '\t' || c == ' ' || c == '\n'){
					allproposals.addAll(Arrays.asList(keywords));
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return allproposals;
	}

}
