/**
 * Դ�������ƣ�WriterFactory.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.excel.export.writer;

import org.apache.poi.ss.usermodel.Sheet;

import com.hundsun.ares.studio.biz.excel.export.Area;
import com.hundsun.ares.studio.biz.excel.export.Block;
import com.hundsun.ares.studio.biz.excel.export.Group;

/**
 * @author sundl
 *
 */
public interface WriterFactory {

	public Writer newGroupWriter(Group group, Sheet sheet);
	
	public Writer newAreaWriter(Area area, Sheet sheet);

	public Writer newBlockWriter(Block block, Sheet sheet);
	
}
