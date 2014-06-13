/**
 * Դ�������ƣ�TableBlock.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.export;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * ���ʽ��Block
 * @author sundl
 *
 */
public class TableBlock extends Block{
	
	private static Logger logger =  Logger.getLogger(TableBlock.class);
	
	public static class Column{
		public static int LABEL_STYLE = 1;
		public static int TEXT_STYLE = 2;
		public static int LINK_STYLE = 3;
		
		public int style;
		public String header;
		public List<String> valueList = new ArrayList<String>();
	}
	
	public int numOfRows;
	public List<Column> columns = new ArrayList<TableBlock.Column>();
	
	/** �ڼ������ӣ� ��ʱֻ֧��1�����ӵ�����һ��group��area�б�;
	 *  -1����û��������
	 */
	public int linkColumn = -1;
	public String linkedGroup;
	
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		for (Column column : columns) {
			headers.add(column.header);
		}
		return headers;
	}
	
	public Column addColumn(String header) {
		Column c = new Column();
		c.header = header;
		columns.add(c);
		return c;
	}
	
	public void addRow(List<String> row) {
		if (row.size() != columns.size()) {
			logger.error("��ͷ���е�������һ��");
			return;
		}
		
		for (int i = 0; i < columns.size(); i++) {
			columns.get(i).valueList.add(row.get(i));
		}
		numOfRows++;
	}
	
}
