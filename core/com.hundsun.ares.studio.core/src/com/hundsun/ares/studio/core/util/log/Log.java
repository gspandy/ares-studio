/**
 * Դ�������ƣ�Log.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.util.log;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * ��������Excel�����е���־������
 * @author sundl
 *
 */
public class Log {
	
	private static Logger logger = Logger.getLogger(Log.class);
	
	/**
	 * ��¼������־��ʱ���һ����λ��Ϣ��
	 * @author sundl
	 */
	public static class Location {
		public String file;
		public String sheet;
		public int row;
		public int column;
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			if (file != null) {
				sb.append("File:\t" + file + ",\n");
			}
			if (sheet != null) {
				sb.append("Sheetҳ:\t");
				sb.append(sheet);
				sb.append(",\n");
			}
			sb.append("�к�:\t" + (row + 1));
			return sb.toString();
		}
	}
	
	public static Location createLocation() {
		return new Location();
	}
	
	public static Location createLocation(String sheet) {
		return createLocation(sheet, -1, -1);
	}
	
	public static Location createLocation(String sheet, int row, int column) {
		Location loc = new Location();
		loc.sheet = sheet;
		loc.row = row;
		loc.column = column;
		return loc;
	}
	
	public static class LogEntry {
		int level;
		String msg;
		Location location;
		
		/**
		 * @param msg
		 * @param location
		 */
		public LogEntry(String msg, Location location) {
			super();
			this.msg = msg;
			this.location = location;
		}
		
	}
	
	public static class LogGroup {
		private List<LogEntry> errors = new ArrayList<Log.LogEntry>();
		private List<LogEntry> warns = new ArrayList<Log.LogEntry>();
		private List<LogEntry> infos = new ArrayList<Log.LogEntry>();
		
		public void info(String msg, Location location) {
			infos.add(new LogEntry(msg, location));
			logger.info(msg);
		}
		
		public void warn(String msg, Location location) {
			warns.add(new LogEntry(msg, location));
			logger.warn(msg);
		}
		
		public void error(String msg, Location location) {
			errors.add(new LogEntry(msg, location));
			logger.error(msg);
		}
	}
	
	// ���󼶱�
	public static final int LEVEL_INFO = 0;
	public static final int LEVEL_WARN = 1;
	public static final int LEVEL_ERROR = 2;
	
	
	protected List<LogEntry> errors = new ArrayList<Log.LogEntry>();
	protected List<LogEntry> warns = new ArrayList<Log.LogEntry>();
	protected List<LogEntry> infos = new ArrayList<Log.LogEntry>();
	
	public void info(String msg, Location location) {
		infos.add(new LogEntry(msg, location));
		logger.info(msg);
	}
	
	public void warn(String msg, Location location) {
		warns.add(new LogEntry(msg, location));
		logger.warn(msg);
	}
	
	public void error(String msg, Location location) {
		errors.add(new LogEntry(msg, location));
		logger.error(msg);
	}
	
	
	protected String genErrorInfo(List<LogEntry> errors, List<LogEntry> warns) {
		StringBuffer sb = new StringBuffer();
		sb.append("<tbody>");
		int num = 1;
		
		for (LogEntry entry : errors) {
			// ��Ŀ̫�ֻ࣬��ʾǰ2000��
			if (num > 2000)
				break;
			
			sb.append("<tr class=\"danger\">");
			if (entry.location == null) {
				sb.append(String.format("<td>%s</td>", num));
				sb.append(String.format("<td>%s</td>", "����"));
				sb.append(String.format("<td>%s</td>", StringUtils.replace(entry.msg, "\n", "<br>")));
				sb.append(String.format("<td colspan=\"2\">%s</td>", StringUtils.EMPTY));
				sb.append("</tr>");
			} else {
				sb.append(String.format("<td rowspan=\"4\">%s</td>", num));
				sb.append(String.format("<td rowspan=\"4\">%s</td>", "����"));
				sb.append(String.format("<td rowspan=\"4\">%s</td>", StringUtils.replace(entry.msg, "\n", "<br>")));
				sb.append("<tr class=\"danger\">");
				sb.append("<th>�ļ�</th>");
				sb.append(String.format("<td>%s</td>", StringUtils.defaultString(entry.location.file)));
				sb.append("</tr>");
				sb.append("<tr class=\"danger\">");
				sb.append("<th>Sheetҳ</th>");
				sb.append(String.format("<td>%s</td>", StringUtils.defaultString(entry.location.sheet)));
				sb.append("</tr>");
				sb.append("<tr class=\"danger\">");
				sb.append("<th>�к�</th>");
				sb.append(String.format("<td>%s</td>", StringUtils.defaultString(String.valueOf(entry.location.row))));
				sb.append("</tr>");
				sb.append("</tr>");
			}
			num++;
		} 
		
		for (LogEntry entry : warns) {
			if (num > 2000)
				break;
			
			sb.append("<tr class=\"warning\">");
			if (entry.location == null) {
				sb.append(String.format("<td>%s</td>", num));
				sb.append(String.format("<td>%s</td>", "����"));
				sb.append(String.format("<td>%s</td>", StringUtils.replace(entry.msg, "\n", "<br>")));
				sb.append(String.format("<td colspan=\"2\">%s</td>", StringUtils.EMPTY));
				sb.append("</tr>");
			} else {
				sb.append(String.format("<td rowspan=\"4\">%s</td>", num));
				sb.append(String.format("<td rowspan=\"4\">%s</td>", "����"));
				sb.append(String.format("<td rowspan=\"4\">%s</td>", StringUtils.replace(entry.msg, "\n", "<br>")));
				sb.append("<tr class=\"warning\">");
				sb.append("<th>�ļ�</th>");
				sb.append(String.format("<td>%s</td>", StringUtils.defaultString(entry.location.file)));
				sb.append("</tr>");
				sb.append("<tr class=\"warning\">");
				sb.append("<th>Sheetҳ</th>");
				sb.append(String.format("<td>%s</td>", StringUtils.defaultString(entry.location.sheet)));
				sb.append("</tr>");
				sb.append("<tr class=\"warning\">");
				sb.append("<th>�к�</th>");
				sb.append(String.format("<td>%s</td>", StringUtils.defaultString(String.valueOf(entry.location.row))));
				sb.append("</tr>");
				sb.append("</tr>");
			}
			num++;
		} 

		sb.append("</tbody>");
		return sb.toString();
	}

}
