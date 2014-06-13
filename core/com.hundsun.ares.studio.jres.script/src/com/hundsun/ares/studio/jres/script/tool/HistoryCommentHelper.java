/**
 * 
 */
package com.hundsun.ares.studio.jres.script.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.script.internal.util.IJSONUtil;
import com.hundsun.ares.studio.jres.script.util.IScriptStringUtil;

/**
 * @author yanwj06282
 *
 */
public class HistoryCommentHelper {

	public static final String DATABASE_TYPE = "database";
	public static final String METADATA_TYPE = "metadata";
	
	public static String getHistoryCommentForDatabase(JRESResourceInfo info , String commentMark) {
		return createHistoryComment(info, DATABASE_TYPE ,commentMark);
	}
	
	public static String getHistoryCommentForMetadata(JRESResourceInfo info , String commentMark) {
		return createHistoryComment(info, METADATA_TYPE , commentMark);
	}
	
	/**
	 * 
	 * 
	 * @param type ���ݲ�ͬģ������ͣ���ȡ��ͬģ����޶���¼
	 * @return
	 */
	private static String createHistoryComment(JRESResourceInfo info ,String type , String commentMark){
		List<List<String>> list = new ArrayList<List<String>>();

		{
			List<String> content = new ArrayList<String>();
			content.add(commentMark);
			content.add("�޸İ汾"+"   ");
			content.add("�޸�����"+"   ");
			content.add("�޸ĵ�"+"        ");
			content.add("������"+"   ");
			content.add("������"+"   ");
			content.add("�޸����� "+"   ");
			content.add("�޸�ԭ��"+"   ");
			content.add("��ע");
			list.add(content);
		}

		List<RevisionHistory> histories = (List<RevisionHistory>) EcoreUtil.copyAll(info.getHistories());
		//����,��ǰ����
		Collections.sort(histories, new HistoryCommentCompator());
		for (RevisionHistory his : histories) {
			List<String> content = new ArrayList<String>();
			content.add(commentMark);
			content.add("V"+his.getVersion()+"   ");
			String modifyDate = his.getModifiedDate();
			String newDate = StringUtils.substring(modifyDate, 0, 10).replaceAll("-", "");
			content.add(newDate+"   ");
			content.add(his.getOrderNumber()+"        ");
			content.add(his.getModifiedBy()+"   ");
			content.add(his.getCharger()+"   ");
			
			if (StringUtils.equals(type, DATABASE_TYPE)) {
				content.add(IJSONUtil.instance.getStringFromJSON(his.toJSON(),
				"Stock3_actionDescription")+"   ");
			}else {
				content.add(his.getModified()+"   ");
			}
			content.add(his.getModifiedReason()+"   ");
			content.add(his.getComment());

			list.add(content);
		}

		return IScriptStringUtil.instance.genStringTable(list)+"\r\n";
	}
}
