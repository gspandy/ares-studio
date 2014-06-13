/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.hundsun.ares.studio.core.util.SqlFormater;
import com.hundsun.ares.studio.jres.script.util.IScriptStringUtil;

/**
 * @author lvgao
 *
 */
public class ScriptStringUtilImpl  extends StringUtils implements IScriptStringUtil{

	public  String getCurrentDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/** 7λASCII�ַ���Ҳ����ISO646-US��Unicode�ַ����Ļ���������*/    
    public static final String US_ASCII = "US-ASCII";   
	
	private static final char[] hexDigit = {
        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    };
	
	/**
     * ���ַ�������� Unicode��
     * 
     * @param theString ��ת����Unicode������ַ�����
     * @param escapeSpace �Ƿ���Կո�
     * 
     * @return ����ת����Unicode������ַ�����
     */
    public  String toUnicode(String theString, boolean escapeSpace) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);
        
        for(int x=0; x<len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\'); outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch(aChar) {
                case ' ':
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':outBuffer.append('\\'); outBuffer.append('t');
                break;
                case '\n':outBuffer.append('\\'); outBuffer.append('n');
                break;
                case '\r':outBuffer.append('\\'); outBuffer.append('r');
                break;
                case '\f':outBuffer.append('\\'); outBuffer.append('f');
                break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    outBuffer.append('\\'); outBuffer.append(aChar);
                    break;
                default:
                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >>  8) & 0xF));
                        outBuffer.append(toHex((aChar >>  4) & 0xF));
                        outBuffer.append(toHex( aChar        & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }
    
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }
    
    /**
     * �� Unicode ��ת���ɱ���ǰ�������ַ�����
     * 
     * @param input Unicode������ַ����顣
     * @param off ת������ʼƫ������
     * @param len ת�����ַ����ȡ�
     * @param convtBuf ת���Ļ����ַ����顣
     * 
     * @return ���ת�������ر���ǰ�������ַ�����
     */
    public  String fromUnicode(char[] input, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;
        
        while (off < end) {
            aChar = input[off++];
            if (aChar == '\\') {
                aChar = input[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = input[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = ' ';
                    } else if (aChar == 'r') {
                        aChar = ' ';
                    } else if (aChar == 'n') {
                        aChar = ' ';
                    } else if (aChar == 'f') {
                        aChar = ' ';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = (char) aChar;
            }
        }
        return new String(out, 0, outLen);
    }
    
    /**
     * ���ڱȽ������ַ����������ֶ��Ƿ���ȫ��ͬ��
     * 
     * StringUtils.arrayEquals(null, null)   = true
     * StringUtils.equals(null, String[]{"abc"})  = false
     * StringUtils.equals(String[]{"abc"}, null)  = false
     * StringUtils.equals(String[]{"abc","qwe"}, String[]{"qwe","abc"}) = true
     * StringUtils.equals(String[]{"abc","qwe"}, String[]{"abc","qwe"}) = true
     * </pre>
     * 
     * @param a
     * @param a2
     * @return
     */
    public  boolean arrayEquals(String[] a, String[] a2) {
    	if (a == a2) {
            return true;
    	}
    	
        if (a == null || a2 == null) {
            return false;
        }

        if (a2.length != a.length) {
        	return false;
        }
            
        for (int i = 0; i < a.length; i++) {
            int j = 0;
            for (; j< a2.length; j++) {
            	if (org.apache.commons.lang.StringUtils.equals(a[i], a2[j])) {
            		break;
            	}
            }
            if (j == a2.length) {
        		return false;
        	}
        }

        return true;
    }
    
    
    
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
	public static String replaceLineSeparator(String original ) {
		String temp = StringUtils.replace(original, "\r\n", "\n") ;
		temp = StringUtils.replace( temp , "\r","\n" );
		temp = StringUtils.replace( temp , "\n", LINE_SEPARATOR);
		return temp;
	}
	
	/**
	 * ����һ���ַ���������ַ���������������
	 * <BR>�������е��ַ�����Ӧ�ù���
	 * <BR>���������õĳ��Ȼ�׼���ֽ�����һ��ȫ�ǵ���2����ǵĳ��ȣ�ֻ���ڵȿ�����ı༭���в鿴���ܵõ��ǳ�Ч��
	 * @param contents
	 * @return
	 */
	public  String genStringTable(List< List<String> > contents) {
		// �����ҳ�����ַ������鳤��
		int maxLength = 0;
		for (List<String> content : contents) {
			maxLength = Math.max(maxLength, content.size());
		}
		
		if (maxLength == 0) {
			return StringUtils.EMPTY;
		}
		
		// ���Ƚ������ַ�������ͳһ���ȣ�����ĳ����ÿհ��ַ�������
		// ͬʱ�ҳ�ÿһ�б���ĳ���
		List<List<String>> contents_normalization = new ArrayList<List<String>>();
		int[] widths = new int[maxLength];
		
		for (int i = 0; i < contents.size(); i++) {
			List<String> content = contents.get(i);
			// ������Ȳ���Ҫ�仯��ֱ��ʹ��ԭʼ���󣬼���ʱ��ռ�ɱ�
			if (maxLength != content.size()) {
				List<String> newContent = new ArrayList<String>();
				newContent.addAll(content);
				
				for (int j = content.size(); j < maxLength; j++) {
					newContent.add(StringUtils.EMPTY);
				}
				content = newContent;
			}
			
			contents_normalization.add(content);
			
			// ���ҳ���
			for (int j = 0; j < content.size(); j++) {
				int len = StringUtils.defaultString(content.get(j)).getBytes().length;
				widths[j] = Math.max(len, widths[j]);
			}
		}
		
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < contents_normalization.size(); i++) {
			List<String> content = contents_normalization.get(i);
			for (int j = 0; j < content.size(); j++) {
				// ������Ҫ����Ŀո���������������ȥ�Ѿ�ռ�����������к���ռ2���ո񳤶�
				int len = StringUtils.defaultString(content.get(j)).getBytes().length;
				int spaceWidth = widths[j] - len + 1; // ���һ���ո���Էָ�����ռ����2����Ԫ��
				result.append(StringUtils.defaultString(content.get(j)));
				result.append(StringUtils.repeat(" ", spaceWidth));
				
			}
			result.append("\r\n");
		}
		return result.toString();
	}

	@Override
	public String getSQLFileHeader(String fileName, String userName,
			String date, String notes) {

		StringBuffer sb = new StringBuffer();
		sb.append("-- -------------------------------------------------------------\r\n");
		sb.append("-- " + "SQLfile" + "   : "+ fileName+ "\r\n");
		sb.append("-- Author   : "+ userName+ "\r\n");
		sb.append("-- Date     : "+ date + "\r\n");
		sb.append("-- Notes    : "+ notes + "\r\n");
		sb.append("-- -------------------------------------------------------------\r\n");
		return sb.toString();
		
	}

	

	@Override
	public StringBuffer getStringBuffer() {
		return new StringBuffer();
	}
    
	public List<Object> getList(){
		return new ArrayList<Object>();
	}
	
	public Map getMap(){
		return new LinkedHashMap();
	}

	@Override
	public String getCHeadFileHeader(String fileName, String userName, String date,
			String notes) {
		return getFileHeader("Headfile",fileName,userName,date,notes);
	}
	
	private String getFileHeader(String headerName,String fileName, String userName, String date,
			String notes){
		StringBuffer sb = new StringBuffer();
		sb.append("/************************************************************\r\n");
		sb.append(" *** " + headerName + "   : "+ fileName+ "\r\n");
		sb.append(" *** Author   : "+ userName+ "\r\n");
		sb.append(" *** Date     : "+ date + "\r\n");
		sb.append(" *** Notes    : "+ notes + "\r\n");
		sb.append(" ************************************************************/\r\n");
		return sb.toString();
	}

	@Override
	public String getPropertyFileHeader(String fileName, String userName,
			String date, String notes) {
		StringBuffer sb = new StringBuffer();
		sb.append("#************************************************************\r\n");
		sb.append("#*** Propertyfile   : "+ fileName+ "\r\n");
		sb.append("#*** Author         : "+ userName+ "\r\n");
		sb.append("#*** Date           : "+ date + "\r\n");
		sb.append("#*** Notes          : "+ notes + "\r\n");
		sb.append("#************************************************************\r\n");
		return sb.toString();
	}

	@Override
	public String getTxtFileHeader(String fileName, String userName,
			String date, String notes) {
		return getFileHeader("Txtfile",fileName,userName,date,notes);
	}

	@Override
	public String fixLength(String str, int len, char fill) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(str);
		
		for(int i = str.length();  i < len; i++){
			buffer.append(fill);
		}
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.util.IScriptStringUtil#starWith(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean startWith(String str, String prefix) {
		return startsWith(str, prefix);
	}

	
	@Override
	public String format(String format, List<String> args) {
		return String.format(format, args.toArray());
	}

	@Override
	public String capitalizeFully(String str, char[] delimiters) {
		return WordUtils.capitalizeFully(str, delimiters);
	}
	
	@Override
	public String uncapitalize(String str, char[] delimiters) {
		return WordUtils.uncapitalize(str, delimiters);
	}
	
	public String formatSql(String sql , String language){
		return SqlFormater.formatSqlOfCreateStatement(sql, language);
	}
	
}
