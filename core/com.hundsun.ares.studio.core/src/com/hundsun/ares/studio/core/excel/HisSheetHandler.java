/**
 * Դ�������ƣ�HisSheetHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.excel.handler.EMFSheetHandler;
import com.hundsun.ares.studio.core.model.CoreFactory;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.extendable.ExtensibleModelUtil;

/**
 * ר�����ڴ����汾ҳ�����޸ļ�¼
 * @author sundl
 */
public class HisSheetHandler extends EMFSheetHandler {
	
	// ������ʽ��ͨ��ƥ�䡰�޸Ķ�����һ�е�ֵ���жϾ�����޸Ķ����
	private static final Pattern PATTERN = Pattern.compile("\\w*\\d+");
	
	JRESResourceInfo info;
	
	@Override
	public void startSheet(Sheet sheet) {
		super.startSheet(sheet);
		info = CoreFactory.eINSTANCE.createJRESResourceInfo();
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.EMFSheetHandler#getOwner()
	 */
	@Override
	protected EObject getOwner() {
		return info;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.EMFSheetHandler#getTableFeature()
	 */
	@Override
	protected EStructuralFeature getTableFeature() {
		return CorePackage.Literals.JRES_RESOURCE_INFO__HISTORIES;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.excel.handler.EMFSheetHandler#getEClass()
	 */
	@Override
	protected EClass getEClass() {
		if (parser.getCurrentBlockTag().equals("�޸İ汾")) {
			return CorePackage.Literals.REVISION_HISTORY;
		}
		return null;
	}
	
	@Override
	public void endBlock() {
		super.endBlock();
		for (RevisionHistory his : info.getHistories()) {
			String objects = ExtensibleModelUtil.getUserExtendedProperty(his, "objects");
			if (StringUtils.isEmpty(objects)) {
				log.error(String.format("�޸ļ�¼%s�޸Ķ���Ϊ��!", his.getVersion()), generateLocation());
				continue;
			}
			
			Matcher matcher = PATTERN.matcher(objects);
			List<String> objlList = new ArrayList<String>();
			while (matcher.find()) {
				objlList.add(matcher.group());
			}
			
			if (objects.contains("-") || objects.contains("~") || objlList.isEmpty()) {
				parser.exlParser.histories.put("ȫ��", his);
			} else {
				for (String obj : objlList) {
					parser.exlParser.histories.put(obj, his);
				}
			}
		}
	}

}
