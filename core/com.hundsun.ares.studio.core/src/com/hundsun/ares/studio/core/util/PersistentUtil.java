/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * dom4j������
 * @author maxh
 */
public class PersistentUtil {

	// longger
	private static final Logger persistentLogger = Logger.getLogger("PersistentUtil");
	static {
		// ��������logger����Ϣ���ڴ˴�ȥ����������ע��
		persistentLogger.setLevel(Level.OFF);
	}

	// Ԫ��������
	public static final String HS_DOC = "hsdoc"; // ��Ԫ�ص�Ԫ����

	// ����������
	/**
	 * HS�ĵ��汾��Ϊ���ǰ汾����������裬��ʱ���ã�Ĭ��Ϊ1.0.0 20081117 sundl ��Ĭ��ֵΪ1.1.0
	 */
	public static final String HS_DOC_VERSION = "version";

	/**
	 * ��ȡxml�и�Ԫ�ء�
	 * 
	 * @param is
	 *            ��������
	 * @return ��Ԫ�ء�
	 */
	public static Element readRoot(InputStream is) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
			// e.printStackTrace();
			persistentLogger.log(Level.SEVERE, "��ȡxml�ĵ�����", e);
			return document == null ? null : document.getRootElement();
		}
		return document == null ? null : document.getRootElement();
	}
	
	/**
	 * ��ȡxml�и�Ԫ�ء�
	 * 
	 * @param is
	 *            ��������
	 * @return ��Ԫ�ء�
	 */
	public static Element readRoot(InputStream is,StringBuffer errorBuffer) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
			errorBuffer.append(e.getMessage());
			errorBuffer.append("\n");
			persistentLogger.log(Level.SEVERE, "��ȡxml�ĵ�����", e);
			return document == null ? null : document.getRootElement();
		}
		return document == null ? null : document.getRootElement();
	}
	
	/**
	 * ��ȡxml�и�Ԫ�ء�
	 * 
	 * @param is
	 *            ��������
	 * @return ��Ԫ�ء�
	 */
	public static Element readRoot(Reader is,StringBuffer errorBuffer) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
			errorBuffer.append(e.getMessage());
			errorBuffer.append("\n");
			persistentLogger.log(Level.SEVERE, "��ȡxml�ĵ�����", e);
			return document == null ? null : document.getRootElement();
		}
		return document == null ? null : document.getRootElement();
	}
	
	/**
	 * ��ȡxml�и�Ԫ�ء�
	 * 
	 * @param is
	 *            ��������
	 * @return ��Ԫ�ء�
	 */
	public static Element readRoot(InputStream is,StringBuffer errorBuffer,String enCoding) {
		SAXReader reader = new SAXReader();
		reader.setEncoding(enCoding);
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
			errorBuffer.append(e.getMessage());
			errorBuffer.append("\n");
			persistentLogger.log(Level.SEVERE, "��ȡxml�ĵ�����", e);
			return document == null ? null : document.getRootElement();
		}
		return document == null ? null : document.getRootElement();
	}

	public static Document readDocument(IFile file) {
		try {
			return readDocument(file.getContents());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return createHSDocument();
	}

	public static Document readDocument(InputStream is) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(is);
			return document;
		} catch (DocumentException e) {
			 e.printStackTrace();
			persistentLogger.log(Level.SEVERE, "��ȡxml�ĵ�����", e);
		}
		return createHSDocument();
	}
	
	public static Document readDocument(Reader is) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(is);
			return document;
		} catch (DocumentException e) {
			// e.printStackTrace();
			persistentLogger.log(Level.SEVERE, "��ȡxml�ĵ�����", e);
		}
		return createHSDocument();
	}

	/**
	 * ���ļ��ж���xml��Ԫ�أ� ��������򷵻�null��
	 * 
	 * @param file
	 * @return
	 */
	public static Element readRoot(IFile file) {
		try {
			return readRoot(file.getContents());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * ������õ�Documentд���ļ�
	 */
	public static void writeDocumentToFile(IFile file, Document doc) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		writeDocument(bos, doc);
		try {
			file.setContents(new ByteArrayInputStream(bos.toByteArray()), true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ���attribuleֵ����
	 */
	public static String[] getAttributeValue(Element element, String[] keys) {
		String[] attributeValues = new String[keys.length];
		if (null != element) {

			for (int i = 0; i < keys.length; i++) {
				if (null != element.attribute(keys[i])){
//					attributeValues[i] = transFromXmlValue(element.attributeValue(keys[i]));
					attributeValues[i] = element.attributeValue(keys[i]);
				}
				else
					attributeValues[i] = "";
			}
		}
		return attributeValues;
	}

	/**
	 * ����һ��Document���󣬲�Ϊ�����Ĭ�ϵ�hs�ĵ��ĸ�Ԫ�ء�
	 * 
	 * @return
	 */
	public static Document createHSDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement(HS_DOC);

		// 20081117 sundl ��Ĭ��ֵΪ1.1.0
		root.addAttribute(HS_DOC_VERSION, "1.1.0");
		return document;
	}

	/**
	 * ����һ��Document���󣬲�Ϊ�����Ĭ�ϵ�hs�ĵ��ĸ�Ԫ�أ���������ע����Ϣ
	 * 
	 * @return
	 */
	public static Document createHSDocumentWithComment(String comment) {
		Document document = DocumentHelper.createDocument();
		if (!StringUtils.isBlank(comment)) {
			document.addComment("\r\n" + comment);
		}
		Element root = document.addElement(HS_DOC);
		
		root.addAttribute(HS_DOC_VERSION, "1.1.0");
		return document;
	}
	
	/**
	 * ����һ��Document���󣬲�Ϊ�����Ĭ�ϵ�hs�ĵ��ĸ�Ԫ�أ���ָ���汾
	 * 
	 * @return
	 */
	public static Document createHSDocument(String ver) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement(HS_DOC);
		root.addAttribute(HS_DOC_VERSION, ver);
		return document;
	}

	/**
	 * ��ȡָ���ĵ��İ汾
	 * 
	 * @param doc
	 * @return
	 */
	public static String getHSDocumentVersion(Document doc) {
		return doc.getRootElement().attributeValue(HS_DOC_VERSION);
	}

	/**
	 * �ڸ����ĸ�Ԫ���д���һ���µ�XMLԪ�أ����������������ֵ�����鳤�ȱ���һһ��Ӧ��
	 * 
	 * @param parent
	 *            ��Ԫ��
	 * @param name
	 *            �½�Ԫ�ص�����
	 * @param keys
	 *            �½�Ԫ�ص�����������
	 * @param values
	 *            �½�Ԫ�ص�ֵ����
	 * @return �´�����Ԫ�� ����<Field Name="op_branch_no" Type="HsBranchNo" Width=""
	 *         Scale=""/>��ʽ��xml
	 */
	public static Element createElement(Element parent, String name, String[] keys, String[] values) {
		if (keys.length != values.length) {
			persistentLogger.warning("key�����value����ĳ��Ȳ����!");
			return null;
		}
		
		String[] checkedValues = new String[values.length];
		for(int i = 0; i < values.length; i++) {
			checkedValues[i] = values[i];
		}
		
		Element newElement = parent.addElement(name);
		for (int i = 0; i < keys.length; i++) {
			// ��֤value��Ϊnull
			newElement.addAttribute(keys[i], StringUtil.getStringSafely(values[i]));
		}
		return newElement;
	}

	public static Element createElementWithText(Element parent, String name, String[] keys, String[] values) {
		if (keys.length != values.length) {
			persistentLogger.warning("key�����value����ĳ��Ȳ����!");
			return null;
		}

		Element newElement = parent.addElement(name);
		for (int i = 0; i < keys.length; i++) {
			// ��֤value��Ϊnull
			Element childElement = newElement.addElement(keys[i]);
			childElement.setText(values[i]);
		}
		return newElement;
	}

	public static Element createSingleElement(Element parent, String name, String key, String value) {

		Element newElement = parent.addElement(name);
		newElement.setText(StringUtil.getStringSafely(value));
		return newElement;
	}

	/**
	 * �ڸ����ĸ�Ԫ�������һ���µ���Ԫ�أ��������Ԫ�����������keys�����Ӧ����Ԫ�أ���Ԫ�ص�TextֵΪ��Ӧ��values����Ԫ��ֵ��
	 * keys��values�������һһ��Ӧ��
	 * 
	 * @param parent
	 *            ��Ԫ��
	 * @param name
	 *            ��Ԫ����
	 * @param keys
	 *            javaBean�������б�
	 * @param values
	 *            javaBean���Ե��ַ���ֵ�б�
	 * @return
	 */
	public static Element createElementWithTextSupport(Element parent, String name, String[] keys, String[] values) {
		if (keys.length != values.length) {
			persistentLogger.warning("key�����value����ĳ��Ȳ����!");
			return null;
		}

		Element newElement = parent.addElement(name);
		for (int i = 0; i < keys.length; i++) {
			if (isEmpty(keys[i])) {
				continue;
			} else {
				Element child = newElement.addElement(keys[i]);
				child.setText(StringUtil.getStringSafely(values[i]));
			}
		}
		return newElement;
	}

	/**
	 * ��ָ��Ԫ���ж�ȡ��keys�����Ӧ��Textֵ��
	 * 
	 * @param parent
	 *            ��Ԫ��
	 * @param name
	 *            ��Ԫ�أ���ָ��Ԫ�ء�
	 * @param keys
	 *            javaBean���������顣
	 * @return ���������ַ���ֵ���顣
	 */
	public static String[] readElementWithTextSupport(Element parent, String name, String[] keys) {
		Element elm = parent.element(name);
		String[] values = new String[keys.length];
		if (null != elm) {
			for (int i = 0; i < keys.length; i++) {
				Element child = elm.element(keys[i]);
				if (null != child) {
					values[i] = child.getText();
				}
			}
		}
		return values;
	}

	/**
	 * ���������Document����
	 * 
	 * @param os
	 *            �������
	 * @param document
	 *            xml�ĵ�����
	 */
	public static void writeDocument(OutputStream os, Document document) {
		// �޸�Ϊ��Ҫת��
		writeDocument(os, document, true);
	}
	
	/**
	 * ���������Document����
	 * 
	 * @param os
	 *            �������
	 * @param document
	 *            xml�ĵ�����
	 * @param isEscapeText
	 *            �Ƿ�ת��ڵ��ı�
	 */
	public static void writeDocument(OutputStream os, Document document, boolean isEscapeText) {
		OutputFormat format2 = OutputFormat.createPrettyPrint();
		format2.setTrimText(false);
		// OutputFormat format = new OutputFormat();
		format2.setEncoding("UTF-8");
		// format.setEncoding("GB2312");
		try {
			XMLWriter writer = new XMLWriter(os, format2);
			writer.setEscapeText(isEscapeText);
			writer.write(document);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * ���������Document����
	 * <br>ȥ������
	 * @param os
	 *            �������
	 * @param document
	 *            xml�ĵ�����
	 */
	public static void writeDocumentWithoutNewLine(OutputStream os, Document document) {
		OutputFormat format2 = OutputFormat.createPrettyPrint();
		format2.setTrimText(false);
		format2.setNewlines(false);
		format2.setEncoding("UTF-8");
		try {
			XMLWriter writer = new XMLWriter(os, format2) {

				/* (non-Javadoc)
				 * @see org.dom4j.io.XMLWriter#escapeAttributeEntities(java.lang.String)
				 */
				@Override
				protected void writeEscapeAttributeEntities(String txt) throws IOException  {
					if (txt != null) {
//						String escapedText = transToXmlValue(txt);
//			            writer.write(escapedText);
						writer.write(txt);
			        }
				}
				
			};
			writer.setEscapeText(false);
			writer.write(document);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * �������ĵ�д���ļ���
	 * 
	 * @param file
	 *            IFile���ͣ��ļ�
	 * @param document
	 *            xml�ĵ�����
	 */
	public static void writeDocument(IFile file, Document document) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		writeDocument(bos, document);
		try {
			file.setContents(new ByteArrayInputStream(bos.toByteArray()), true, false, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
//
//	/**
//	 * ����������ַ���Ϊnull����nullֵת��Ϊ����Ϊ0���ַ���;
//	 * 
//	 * @param s
//	 *            ��Ҫת�����ַ���
//	 * @return �����Ϊnull�������ַ����������򷵻س���Ϊ0���ַ�����
//	 */
//	public static String convertString(String s) {
//		if (s == null) {
//			return "";
//		}
//		return s;
//	}
//
	/**
	 * ����ַ����Ƿ�Ϊ�ա�
	 * 
	 * @param target
	 * @return ���ַ�������Ϊnull��������Ϊ�գ���ֻ�пհ��ַ����򷵻�true��
	 */
	public static boolean isEmpty(String target) {
		if (null == target || target.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}
//
//	public static String switchNull(String s) {
//		return s == null ? "" : s;
//	}
//
//	//Ч��̫��
//	private static String transToXmlValue2(String str) {
//		if(str == null || str.equals("")) {
//			return "";
//		}
//		str= str.replace("<", "&lt");
//		str=str.replace(">", "&gt");
//		str=str.replace("&", "&amp");
//		str=str.replace("\"", "&quot");
//		String str=str.replace("\'", "&apos");
//		return str;
//	}
//	/**
//	 * �������ַ�ת��
//	 * <br>Ч�ʺܹؼ������Ч�ʲ����ߣ��ٴ��Ż�������
//	 * <br>10w��  50���ȣ�ת�����10�����ң���ʱ 400ms����
//	 */
//	public static String transToXmlValue(String str) {
//		if(str == null || str.equals("")) {
//			return "";
//		}
//		StringBuilder sb = new StringBuilder();
//		int length = str.length();
//		for(int i = 0; i < length; i ++) {
//			char ch = str.charAt(i);
//			if(ch == '<') {
//				sb.append("&lt;");
//			}else if (ch == '>') {
//				sb.append("&gt;");
//			}else if(ch == '&') {
//				sb.append("&amp;");
//			}else if(ch =='\"') {
//				sb.append("&quot;");
//			}else if(ch =='\'') {
//				sb.append("&apos;");
//			}else {
//				sb.append(ch);
//			}
//		}
//		return sb.toString();
//	}
//	
//	/**
//	 * �ָ��������ַ�
//	 * <br>Ч�ʺܹؼ������Ч�ʲ����ߣ��ٴ��Ż�������
//	 * <br>10w��  50���ȣ�ת�����10�����ң���ʱ 400ms����
//	 * @param str
//	 * @return
//	 */
//	private static String transFromXmlValue(String str) {
//		if(str == null || str.equals("")) {
//			return "";
//		}
//		StringBuilder sb = new StringBuilder();
//		
//		int length = str.length();
//		for(int i = 0; i < length; i ++) {
//			char ch = str.charAt(i);
//			if(ch == '&') {
//				if(i + 5 < length) {//quot;�ĳ�����5
//					if(checkTransformed(str, "&quot;", i )) {
//						sb.append('\"');
//						i = i +5;
//						continue;
//					}else if(checkTransformed(str, "&apos;", i )) {
//						sb.append('\'');
//						i = i +5;
//						continue;
//					}
//				}
//				if(i + 4 < length){//amp;�ĳ�����4
//					if(checkTransformed(str, "&amp;", i )) {
//						sb.append('&');
//						i = i +4;
//						continue;
//					}
//				}
//				if(i + 3 < length){//lt;�ĳ�����3
//					if(checkTransformed(str, "&lt;", i )) {
//						sb.append('<');
//						i = i +3;
//						continue;
//					}else if(checkTransformed(str, "&gt;", i )) {
//						sb.append('>');
//						i = i +3;
//						continue;
//					}
//				}else {
//					sb.append(ch);
//				}
//			}else {
//				sb.append(ch);
//			}
//
//		}
//		return sb.toString();
//	}
//	
//	/**
//	 * ����Ƿ���ת������ַ���
//	 * @return
//	 */
//	private static boolean checkTransformed(String allString, String transString, int index ) {
//		int length = transString.length();
//		if(index + length < allString.length()) {
//			for(int i = 0 ; i < length; i++) {
//				if(allString.charAt(index + i) != transString.charAt(i)) {
//					return false;
//				}
//			}
//			return true;
//		}
//		return false;
//	}
//	
//	
//	
//	
//	public static void main(String[] a) {
//		long startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��   
//		
//		doSomeThing();  //���ԵĴ����   
//		long endTime=System.currentTimeMillis(); //��ȡ����ʱ��   
//		System.out.println("��������ʱ�䣺 "+(endTime-startTime)+"ms");   
//		
//		
//		
//		
//		 startTime=System.currentTimeMillis();   //��ȡ��ʼʱ��   
//		doSomeThing2();  //���ԵĴ����   
//		 endTime=System.currentTimeMillis(); //��ȡ����ʱ��   
//		System.out.println("��������ʱ��2�� "+(endTime-startTime)+"ms");   
//		
//		String text = "sdfjein<sudhf>sdfgwereurhk&sdhf/'/'sadfsd\"aabd&%#><Ksdf>sdf";
//		String t1 = transToXmlValue(text);
//		String t2 = transFromXmlValue(t1);
//		System.out.println("\naaaa "+t1);  
//		System.out.println("\naaaa "+t2);  
//		System.out.println("\naaaa "+t2.equals(text));  
//		
//		
//	}
//
//	/**
//	 * 
//	 */
//	private static void doSomeThing2() {
//		// TODO Auto-generated method stub
//		for(int i = 0; i < 100000; i++) {
//			 transFromXmlValue("sdfjein&ltsudhf&gtsdfgwereurhk&ampsdhf/&apos/&apossadfsd&quotaabd&amp%#&gt&ltKsdf&gtsdf");
//		}
//	}
//
//	/**
//	 * 
//	 */
//	private static void doSomeThing() {
//		for(int i = 0; i < 100000; i++) {
//			transToXmlValue("sdfjein<sudhf>sdfgwereurhk&sdhf/'/'sadfsd\"aabd&%#><Ksdf>sdf ");
//		}
//		
//	}
	
}
