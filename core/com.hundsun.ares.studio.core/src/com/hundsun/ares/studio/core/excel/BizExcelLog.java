/**
 * Դ�������ƣ�BizExcelLog.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.util.log.Log;

/**
 * @author sundl
 *
 */
public class BizExcelLog extends Log{

	private static Logger logger = Logger.getLogger(BizExcelLog.class);
	
	public int totle;
	
	List<Resource> createdResources = new ArrayList<Resource>();
	List<Resource> overwrittenResoruces = new ArrayList<Resource>();
	List<Resource> skippedResources = new ArrayList<Resource>();
	List<Resource> failedResources = new ArrayList<Resource>();

	public void logResCreted(Resource res) {
		createdResources.add(res);
		if (logger.isDebugEnabled())
			logger.debug(String.format("��Դ�����ɹ�: %s-%s", res.name, res.type));
	}
	
	public void logResOverwritten(Resource res) {
		overwrittenResoruces.add(res);
		if (logger.isDebugEnabled())
			logger.debug(String.format("������Դ��%s-%s", res.name, res.type));
	}
	
	public void logResSkipped(Resource res) {
		skippedResources.add(res);
		if (logger.isDebugEnabled())
			logger.debug(String.format("������Դ��%s-%s", res.name, res.type));
	}
	
	public void logResFailed(Resource res, String msg) {
		failedResources.add(res);
		error(msg + ":" + res.name, res.startLoc);
		logger.error(String.format("��Դ����ʧ�ܣ�%s-%s, ����ԭ��%s", res.name, res.type, msg));
	}
	
	private String genResList() {
		StringBuffer sb = new StringBuffer();
		sb.append("<tbody>");
		
		int num = 1;
		for (Resource res : failedResources) {
			sb.append(genResRow(num, "error", res, "����ʧ��"));
			sb.append("</tr>");
			num++;
		} 
		
		for (Resource res : skippedResources) {
			sb.append(genResRow(num, "warning", res, "�û�����"));
			sb.append("</tr>");
			num++;
		} 
		
		for (Resource res : overwrittenResoruces) {
			sb.append(genResRow(num, "info", res, "����"));
			sb.append("</tr>");
			num++;
		} 
		
//		for (Resource res : createdResources) {
//			sb.append(genResRow(num, "success", res, "������Դ"));
//			sb.append("</tr>");
//			num++;
//		} 
		sb.append("</tbody>");
		return sb.toString();
	}
	
	private String genResRow(int num, String clazz, Resource res, String handled) {
		String id = null;
		String cName = null;
		if (res.info instanceof BasicResourceInfo) {
			id = ((BasicResourceInfo) res.info).getObjectId();
			cName = ((BasicResourceInfo) res.info).getChineseName();
		}
		if (id == null)
			id = StringUtils.EMPTY;
		if (cName == null)
			cName = StringUtils.EMPTY;
		
		String name = res.name != null  ? res.name : StringUtils.EMPTY;
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("<tr class=\"%s\">", clazz));
		sb.append(String.format("<td>%s</td>", num));
		sb.append(String.format("<td>%s</td>", id));
		sb.append(String.format("<td>%s</td>", name));
		sb.append(String.format("<td>%s</td>", cName));
		sb.append(String.format("<td>%s</td>", handled));
		sb.append("</tr>");
		return sb.toString();
	}
	

	public void generateReport(OutputStream stream) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("<div id=\"overview\">��������%s����Դ, %s�������ɹ��� %s������, %s�����û�����", 
								totle, createdResources.size(), overwrittenResoruces.size(), skippedResources.size()));
		if (!failedResources.isEmpty()) {
			sb.append(String.format("<div class=\"error\"�� %s��ʧ��</div>", failedResources.size()));
		}
		sb.append("</div>");
		String overview = sb.toString();
		
		sb.setLength(0);
		
		URL url = ARESCore.getDefault().getBundle().getEntry("import_report.html");
		try {
			url = FileLocator.resolve(url);
			String template = IOUtils.toString(url.openStream(), "utf-8");
			String result = StringUtils.replace(template, "{{overview}}", overview);
			String errInfoOverview = String.format("����%s��������%s����", String.valueOf(errors.size()), String.valueOf(warns.size()));
			if (errors.size() > 1000 || warns.size() > 1000) {
				errInfoOverview += "����Ϣ����̫�࣬����ʾǰ2000����";
			}
			result = StringUtils.replace(result, "{{err_info_overview}}", errInfoOverview);
			result = StringUtils.replace(result, "{{err_info_body}}", genErrorInfo(errors, warns));
			result = StringUtils.replace(result, "{{reslist}}", genResList());
			stream.write(result.getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("��ȡģ���ļ�����", e);
		}
	}
}
