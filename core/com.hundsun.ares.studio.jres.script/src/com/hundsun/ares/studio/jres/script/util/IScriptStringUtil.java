/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util;

import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.jres.script.util.impl.ScriptStringUtilImpl;


/**
 * @author lvgao
 *
 */
public interface IScriptStringUtil {

	public static IScriptStringUtil instance = new ScriptStringUtilImpl();
	
	/**
	 * �����������ڸ�ʽ��ȡ�����ַ���
	 * @param pattern
	 * @return
	 */
	public  String getCurrentDate(String pattern) ;
	

	/**
     * ���ַ�������� Unicode��
     * 
     * @param theString ��ת����Unicode������ַ�����
     * @param escapeSpace �Ƿ���Կո�
     * 
     * @return ����ת����Unicode������ַ�����
     */
    public  String toUnicode(String theString, boolean escapeSpace);
    
//    public  char toHex(int nibble) ;
    
    /**
     * ???????
     * �� Unicode ��ת���ɱ���ǰ�������ַ�����
     * 
     * @param in Unicode������ַ����顣
     * @param off ת������ʼƫ������
     * @param len ת�����ַ����ȡ�
     * @param convtBuf ת���Ļ����ַ����顣
     * @return ���ת�������ر���ǰ�������ַ�����
     */
    public  String fromUnicode(char[] input, int off, int len, char[] convtBuf) ;
    
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
    public  boolean arrayEquals(String[] a, String[] a2) ;
    
	/**
	 * ����һ���ַ���������ַ���������������
	 * <BR>�������е��ַ�����Ӧ�ù���
	 * <BR>���������õĳ��Ȼ�׼���ֽ�����һ��ȫ�ǵ���2����ǵĳ��ȣ�ֻ���ڵȿ�����ı༭���в鿴���ܵõ��ǳ�Ч��
	 * @param contents
	 * @return
	 */
    public  String genStringTable(List< List<String> > contents);

    /**
     * 
     * ��ȡSQL�ļ�ͷ
     * 
     * @param fileName
     * @param userName
     * @param date
     * @param notes
     * @return
     */
    public String getSQLFileHeader(String fileName,String userName, String date, String notes);
    
    /**
     * 
     * ��ȡC�ļ�ͷ
     * 
     * @param fileName
     * @param userName
     * @param date
     * @param notes
     * @return
     */
    public String getCHeadFileHeader(String fileName,String userName, String date, String notes);
    
    /**
     * 
     * ��ȡ�����ļ�ͷ
     * 
     * @param fileName
     * @param userName
     * @param date
     * @param notes
     * @return
     */
    public String getPropertyFileHeader(String fileName,String userName, String date, String notes);
    
    
    /**
     * ��ȡ�ı��ļ�ͷ
     * 
     * @param fileName
     * @param userName
     * @param date
     * @param notes
     * @return
     */
    public String getTxtFileHeader(String fileName, String userName,
			String date, String notes);
    
    /**
     * ��ȡStringBuffer����
     * 
     * @return
     */
    public StringBuffer getStringBuffer();
    
    /**
     * ��ȡ����
     * 
     * @return
     */
    public List<Object> getList();
    
    /**
     * ��ȡMap����
     * 
     * @return
     */
    public Map getMap();
    
    /**
     * ���ɹ̶����ȵ��ַ��������Ȳ���ʱ��fill���뵽����
     * @param str
     * @param len
     * @param fill
     * @return
     */
    public String fixLength(String str,int len,char fill);
    
    /**
     * �Ƿ���ʲô��ͷ
     * @param str
     * @param prefix  ǰ׺
     * @return
     */
    public boolean startWith(String str,String prefix);
  
    /**
    * �ַ�����ʽ��
    * @param format
    * @param args
    * @return
    */
   public  String format(String format, List<String> args);
   
   /**
    * <p>Converts all the delimiter separated words in a String into capitalized words, 
    * that is each word is made up of a titlecase character and then a series of 
    * lowercase characters. </p>
    *
    * <p>The delimiters represent a set of characters understood to separate words.
    * The first string character and the first non-delimiter character after a
    * delimiter will be capitalized. </p>
    *
    * <p>A <code>null</code> input String returns <code>null</code>.
    * Capitalization uses the unicode title case, normally equivalent to
    * upper case.</p>
    *
    * <pre>
    * WordUtils.capitalizeFully(null, *)            = null
    * WordUtils.capitalizeFully("", *)              = ""
    * WordUtils.capitalizeFully(*, null)            = *
    * WordUtils.capitalizeFully(*, new char[0])     = *
    * WordUtils.capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
    * </pre>
    * 
    * @param str  the String to capitalize, may be null
    * @param delimiters  set of characters to determine capitalization, null means whitespace
    * @return capitalized String, <code>null</code> if null String input
    * @since 2.1
    */
   public String capitalizeFully(String str, char[] delimiters);
   
   public String uncapitalize(String str, char[] delimiters);
   
   /**
    * ��ʽ��SQLԴ�ı�
    * 
    * @param sql
    * @param language ȡֵΪ"Any SQL","SQL Server","DB2/UDB","MSAccess","Sybase","Informix","MYSQL","PostgreSQL","Oracle"����Сд�����У�
    * @return
    */
   public String formatSql(String sql , String language);
   
}
