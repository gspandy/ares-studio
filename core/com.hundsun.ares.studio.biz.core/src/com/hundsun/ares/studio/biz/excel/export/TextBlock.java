/**
 * Դ�������ƣ�TextBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.export;

/**
 * @author sundl
 *
 */
public class TextBlock extends Block {
	public String label;
	public String text;
	
	/** text���������з��Ƿ���excel�������� */
	public boolean newRow;
	/** textռ�е����� */
	public int textColumns = 5;
}
