/**
 * Դ�������ƣ�Sheet.java
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

/**
 * @author sundl
 *
 */
public class Group {
	
	/** ��ӦSheetҳ������ */
	public String name;
	/** ��Ӧsheetҳ��index,��Ҫע����ǣ�����group����ӣ����index�������ղ�����Ϊ���ֵ������Ҫ���group��˳����ʹ�� */
	public int index = -1;
	
	/** �п� int��ֵΪ�ַ��� */
	public int[] columnWidth;
	
	public List<Area> areas = new ArrayList<Area>();
	
}
