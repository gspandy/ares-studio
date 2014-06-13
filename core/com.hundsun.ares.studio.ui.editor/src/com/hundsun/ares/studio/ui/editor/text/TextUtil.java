package com.hundsun.ares.studio.ui.editor.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * <p>
 * CreatedDate: 2008-2-19
 * </p>
 * 
 * @author sundl
 */
public class TextUtil {

	/**
	 * ȡ��ͨ���ʣ���ĸ�����»��߿�ͷ(������Java������ͬ)
	 * @param document
	 * @param offset
	 * @return
	 */
	public static IRegion findCommonWord(IDocument document, int offset) {
		
		char c;
		int pre_offset = offset;
		int suf_offset = offset;

		try {
			c = document.getChar(offset);
			if (Character.isWhitespace(c)) {
				return null;
			}

			int length = document.getLength();

			while (pre_offset > 0) {
				pre_offset--;
				char ch = document.getChar(pre_offset);
			    if (Character.isJavaIdentifierStart(ch)) {
			    	continue;
			    } else {
			    	break;
			    }
			}
			pre_offset++;

			while (suf_offset < length - 1) {
				suf_offset++;
				char ch = document.getChar(suf_offset);
				if (Character.isJavaIdentifierPart(ch)) {
					continue;
				} else {
					break;
				}
				
			}
			return new Region(pre_offset, suf_offset - pre_offset);
		} catch (BadLocationException e) {
			e.printStackTrace();
			System.out.println(pre_offset);
			System.out.println(suf_offset);
		}

		return null;

	}
	
	public static IRegion findWord(IDocument document, int offset) {

		char c;
		int pre_offset = offset;
		int suf_offset = offset;

		try {
			c = document.getChar(offset);
			if (Character.isWhitespace(c)) {
				return null;
			}

			int length = document.getLength();

			while (pre_offset > 0) {
				pre_offset--;
				char ch = document.getChar(pre_offset);
				// if (Character.isJavaIdentifierPart(ch))
				if (ch != '[' && ch != '\n' && ch != '\r')
					continue;
				else
					break;
			}
			pre_offset++;

			while (suf_offset < length - 1) {
				suf_offset++;
				char ch = document.getChar(suf_offset);
				// if (Character.isJavaIdentifierPart(ch))
				if (ch != ']' && ch != '\n' && ch != '\r')
					continue;
				else
					break;
			}
			return new Region(pre_offset, suf_offset - pre_offset);
		} catch (BadLocationException e) {
			e.printStackTrace();
			System.out.println(pre_offset);
			System.out.println(suf_offset);
		}

		return null;

	}

	public static IRegion findHyperlink(IDocument document, int offset) {
		IRegion region = findWord(document, offset);
		if (region == null)
			return null;

		try {
			char pre = document.getChar(region.getOffset() - 1);
			char suf = document.getChar(region.getOffset() + region.getLength());

			if (pre == '[' && suf == ']') {
				return new Region(region.getOffset() - 1, region.getLength() + 2);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ���㵱ǰ�����ǰ׺֮�������,���ڱ�׼�ֶ���ʾ
	 * 
	 * @param document
	 *            ��ǰ�ı�
	 * @param offset
	 *            ���ƫ����
	 * @return IRegion �������
	 */
	public static IRegion getStdFieldPrefixRegion(IDocument document, int offset) {
		try {
			
			if (offset <= 1) // �ı���ͷ�������ı��а��ȼ��������ı������롰[����
				return new Region(0, offset);
			char c;
			int pre_pointer = offset;
			c = document.getChar(--pre_pointer);// ��ĸ���д�0��ʼ,��ƫ����-1Ϊ���ǰһ��ĸ�����
			while (c != '@' ) {
				pre_pointer--;
				if (pre_pointer <= 0)
					return new Region(0, 0);// �ı���ͷ����
				if ((c == ' ') || (c == '\n'))// �ڡ�[��֮ǰ�����ָ��
				{
					pre_pointer = offset;
					break;
				}
				c = document.getChar(pre_pointer);
			}
			return new Region(pre_pointer, offset - pre_pointer);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static class IntegerString {
		public IntegerString(int num, String val) {
			this.num = num;
			this.value = val;
		};
		public int num;
		public String value;
	}
	
	/**
	 * ��ò����Ƴ̶ȣ�����ط���-1,����Խ��Խ������
	 * 
	 * @param str1
	 * @param input
	 * @return
	 */
	private static int getSimilarityLevel(String str1, String input) {
		
		int ret = 0;
		char data[] = input.toCharArray();
		int order = -1;
		for (int i = 0; i < data.length; i++) {
			int nowOrder = str1.indexOf(data[i],order + 1);
			if (nowOrder == -1) {
				return -1;
			} else {
				if (nowOrder == order + 1) {
					// ֱ�����������ƣ������ƶȲ���������
				} else {
					ret += nowOrder;
				}
				order = nowOrder;


			}
		}
		
		return ret;
	}
	
	public static List<String> filter(List<String> allStrings, String prefix) {

		if (prefix == null || prefix.length() == 0) {
			return allStrings;
		}
				
		prefix = prefix.toLowerCase();
		
		List<IntegerString> temp = new ArrayList<IntegerString>();
		
		for (String m : allStrings) {
			String cm = m.toLowerCase();
			
			// ������ƶ�
			int x = getSimilarityLevel(cm, prefix);
			int y = getSimilarityLevel( ChineseCharToEn.getAllFirstLetter(cm) , prefix);
			
			if (x == -1) {
				if (y == -1) {
					continue;
				} else {
					temp.add(new IntegerString(y, m));
				}
			} else {
				if (y == -1) {
					temp.add(new IntegerString(x, m));
				} else {
					temp.add(new IntegerString(y > x ? x : y, m));
				}
			}
		}
		
		Collections.sort(temp, new Comparator<IntegerString>(){

			@Override
			public int compare(IntegerString o1, IntegerString o2) {
				return o1.num - o2.num;
			}});
		
		List<String> ret = new ArrayList<String>();
		for (IntegerString integerString : temp) {
			ret.add(integerString.value);
		}
		
		return ret;
	}

	public static IRegion getGeneralPrefixRegion(IDocument document, int offset) {
		if (offset == 0)
			return new Region(0, offset);;
		int preOffset = offset - 1;// ��ĸ���д�0��ʼ,��ƫ����-1Ϊ���ǰһ��ĸ�����
		try {
			
			while (preOffset > 0) {
				char c = document.getChar(preOffset);
				if (c == '@' || c == '[') break;
				if(c == '>' && document.getChar(preOffset-1) == '-') 
					return new Region(preOffset + 1, offset - preOffset - 1);
				if (c == '\r' || c == '\t' || c == ' ' || c == '\n') 
					//���ڳ���@��[�⣬��������������ʾ������������ʱ��IRegion��ȡ��ʽ  by wangxh
					return new Region(preOffset+1, offset - preOffset -1);
				--preOffset;
			}
			if(preOffset == 0){
				char c = document.getChar(0);
				if (c == '\r' || c == '\t' || c == ' ' || c == '\n') 
					//��0���ַ������ϼ����ַ�ʱ���ӵ�1���ַ���ʼ��ȡ
					return new Region(1, offset-1);
			}
			return new Region(preOffset, offset - preOffset);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Region(offset, 0);
	}
	
	public static String getGeneralPrefix(IDocument document, int offset) {

		if (offset == 0)
			return "";
		int preOffset = offset - 1;// ��ĸ���д�0��ʼ,��ƫ����-1Ϊ���ǰһ��ĸ�����
		try {
			
			while (preOffset > 0) {
				char c = document.getChar(preOffset);
				if (c == '@' || c == '[') break;
				if(c == '>' && document.getChar(preOffset-1) == '-') 
					return document.get(preOffset + 1, offset - preOffset - 1);;
				if (c == '\r' || c == '\t' || c == ' ' || c == '\n'){
					//���ڳ���@��[�⣬��������������ʾ������������ʱҲ��Ҫ��ȡprefix��  by wangxh
					return document.get(preOffset + 1, offset - preOffset - 1);
				}
				--preOffset;
			}
			if(preOffset == 0){
				char c = document.getChar(0);
				if (c == '\r' || c == '\t' || c == ' ' || c == '\n')
					//��0���ַ������ϼ����ַ�ʱ���ӵ�1���ַ���ʼ��ȡ
					return document.get(1, offset - 1);
			}
			return document.get(preOffset, offset - preOffset);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * ��������׼�ֶε�ǰ׺�ı�
	 * 
	 * @param document
	 * @param offset
	 * @return
	 */
	public static String getStdFieldPrefix(IDocument document, int offset) {
		try {
			if (offset == 0)// �ı���ͷ�������ı��а��ȼ���
				return "";
			if (offset == 1)// �ı���ͷ�������ı������롰@����
				return document.get(0, 1);
			offset--;// ��ĸ���д�0��ʼ,��ƫ����-1Ϊ���ǰһ��ĸ�����
			char c;
			int pre_pointer = offset;
			c = document.getChar(pre_pointer);
			while (c != '@' ) {
				pre_pointer--;
				if (pre_pointer < 0)
					return "";// �ı���ͷ����
				if (c == ' ' || c == '\n')// �ڡ�[��֮ǰ�����ָ��
					return "";
				c = document.getChar(pre_pointer);
			}

			String prefix = document.get(pre_pointer, offset - pre_pointer + 1);
			return prefix;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ���ǰ׺�ַ���
	 * 
	 * @param document
	 *            ��ǰ�ı�
	 * @param offset
	 *            ���ƫ����
	 * @return ǰ׺�ַ���(����"[")
	 */
	public static String getFunctionPrefix(IDocument document, int offset) {
		try {
			if (offset == 0)// �ı���ͷ�������ı��а��ȼ���
				return "";
			if (offset == 1)// �ı���ͷ�������ı������롰[����
				return document.get(0, 1);
			offset--;// ��ĸ���д�0��ʼ,��ƫ����-1Ϊ���ǰһ��ĸ�����
			char c;
			int pre_pointer = offset;
			c = document.getChar(pre_pointer);
			while (c != '[' ) {
				pre_pointer--;
				if (pre_pointer < 0)
					return "";// �ı���ͷ����
				if (c == ' ' || c == '\n')// �ڡ�[��֮ǰ�����ָ��
					return "";
				c = document.getChar(pre_pointer);
			}

			String prefix = document.get(pre_pointer, offset - pre_pointer + 1);
			return prefix;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ú�׺�ַ���
	 * 
	 * @param document
	 *            ��ǰ�ı�
	 * @param offset
	 *            ���ƫ����
	 * @return ��׺�ַ���(����"]")
	 */
	public static String getFunctionSuffix(IDocument document, int offset) {
		try {
			if (document.getLength() == offset)// �ı�ĩ����
				return "";
			char c;
			int pre_pointer = offset;
			c = document.getChar(pre_pointer);
			while (c != ']') {
				pre_pointer++;
				if (pre_pointer >= document.getLength())
					return "";// �ı�ĩ����
				if (c == ' ' || c == '\n')// �ڡ�]��֮ǰ�����ָ��
					return "";
				c = document.getChar(pre_pointer);
			}

			String suffix = document.get(offset, pre_pointer - offset + 1);
			return suffix;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���㵱ǰ�����ǰ׺֮�������
	 * 
	 * @param document
	 *            ��ǰ�ı�
	 * @param offset
	 *            ���ƫ����
	 * @return IRegion �������
	 */
	public static IRegion getFunctionPrefixRegion(IDocument document, int offset) {
		try {
			// System.out.println("document.getChar("+0+"):"+document.getChar(0));
			// System.out.println("offset:" + offset);
			// System.out.println("document length:" + document.getLength());
			if (offset <= 1) // �ı���ͷ�������ı��а��ȼ��������ı������롰[����
				return new Region(0, offset);
			char c;
			int pre_pointer = offset;
			c = document.getChar(--pre_pointer);// ��ĸ���д�0��ʼ,��ƫ����-1Ϊ���ǰһ��ĸ�����
			while (c != '[' ) {
				pre_pointer--;
				if (pre_pointer <= 0)
					return new Region(0, 0);// �ı���ͷ����
				if ((c == ' ') || (c == '\n'))// �ڡ�[��֮ǰ�����ָ��
				{
					pre_pointer = offset;
					break;
				}
				c = document.getChar(pre_pointer);
			}
			return new Region(pre_pointer, offset - pre_pointer);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �жϵ�ǰ�Ƿ�����һ�����
	 * 
	 * @param document
	 *            ��ǰ�ı�
	 * @param offset
	 *            ���ƫ����
	 * @return boolean(�Ǻ귵��true,��Ϊfalse)
	 */
	public static boolean isAfterMacro(IDocument document, int offset) {
		try {
			if (offset < 1)
				return false;
			offset--;
			char c;
			int pre_pointer = offset;
			c = document.getChar(pre_pointer);
			while (c != '\n') {
				pre_pointer--;
				if (pre_pointer < 0)
					return false;
				if (c == ']')
				{
					return true;
				}
				
				c = document.getChar(pre_pointer);
			}
			return false;
		} catch (BadLocationException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * �жϵ�ǰ������Ӧ�ĺ���
	 * 
	 * @param document
	 *            ��ǰ�ı�
	 * @param offset
	 *            ���ƫ����
	 * @return ����
	 */
	public static String getMacroName(IDocument document, int offset) {
		try {
			offset--;// ��ĸ���д�0��ʼ,��ƫ����-1Ϊ���ǰһ��ĸ�����
			char c;
			int pre_pointer = offset;
			c = document.getChar(pre_pointer);
			while (c != '[') {// �ҵ���һ����[��
				pre_pointer--;
				if (pre_pointer < 0)
					return "";
				if (c == ' ' || c == '\n')// �ڡ�[��֮ǰ�����ָ��
				{
					return "";
				}
				c = document.getChar(pre_pointer);
			}
			if (pre_pointer <= 1)// �ı���ͷ����
				return "";
			c = document.getChar(--pre_pointer);
			int end = pre_pointer;
			if (c != ']')
				return "";
			else {// �ҵ���[��֮ǰ�ġ�]��
				c = document.getChar(--pre_pointer);
				while (c != '[') {// �ҵ��ڶ�����[��
					pre_pointer--;
					if (pre_pointer < 0)
						return "";
					if (c == ' ' || c == '\n')// �ڡ�[��֮ǰ�����ָ��
					{
						return "";
					}
					c = document.getChar(pre_pointer);
				}
				return document.get(pre_pointer + 1, end - pre_pointer - 1);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "";
		}
	}

}
