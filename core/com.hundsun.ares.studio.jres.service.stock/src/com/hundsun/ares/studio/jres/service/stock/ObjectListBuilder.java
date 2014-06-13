/**
 * Դ�������ƣ�ObjectListBuilder.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.service.stock
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.service.stock;

import java.util.List;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.excel.export.AbstractBuilder;
import com.hundsun.ares.studio.biz.excel.export.Area;
import com.hundsun.ares.studio.biz.excel.export.Group;
import com.hundsun.ares.studio.biz.excel.export.TableBlock;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandlerFactory;

/**
 * @author sundl
 *
 */
public class ObjectListBuilder extends AbstractBuilder{

	public static final String[] PROPERTIES = new String[] {
		"������", "����������", "����״̬"
	};
	
	private List<ARESObject> objects;
	
	/**
	 * @param project
	 */
	public ObjectListBuilder(IARESProject project, List<ARESObject> objects) {
		super(project);
		this.objects = objects;
	}
	
	public void build() {
		Group group = new Group();
		group.name = "�����б�";
		group.columnWidth = new int [] {15, 25, 15,};
		groups.add(group);
		
		Area area = new Area();
		group.areas.add(area);
		
		IPropertyHandlerFactory handlerFactory = getPropertyHandlerFactory(BizPackage.Literals.ARES_OBJECT);
		TableBlock tableBlock = buildTableBlock(PROPERTIES, null, objects, handlerFactory);
		tableBlock.linkColumn = 0;
		tableBlock.linkedGroup = "����-ȫ��";
		
		area.blocks.add(tableBlock);
	}

}
