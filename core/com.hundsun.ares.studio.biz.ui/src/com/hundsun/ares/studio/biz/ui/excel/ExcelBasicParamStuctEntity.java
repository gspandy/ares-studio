/**
 * Դ�������ƣ�ExcelCellStuctEntity.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�yanwj06282
 */
package com.hundsun.ares.studio.biz.ui.excel;

/**
 * @author yanwj06282
 *
 */
public class ExcelBasicParamStuctEntity {

	/**
	 * cell��ֵ
	 */
	private String value;
	/**
	 * cell���ȣ�1�����ȶ�Ӧexcel��һ��cell,����1������excel�кϲ���Ԫ����ʾ
	 */
	private int length = 1;
	/**
	 * cell�ĸ߶ȣ�1�����ȶ�Ӧexcel��һ��cell,����1������excel�кϲ���Ԫ����ʾ
	 * ���key��value��Ԫ��ĸ߶Ȳ�һ�£���key�ĸ߶�Ϊ׼
	 */
	private int width = 1;
	
	public ExcelBasicParamStuctEntity(String value ,int length , int width) {
		this.value = value;
		this.length = length;
		this.width = width;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
}
