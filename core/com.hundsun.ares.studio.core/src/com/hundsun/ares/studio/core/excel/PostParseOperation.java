/**
 * Դ�������ƣ�PostParseOperation.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.util.ArrayList;
import java.util.List;

import com.hundsun.ares.studio.core.util.log.Log;


/**
 * ������ɺ���еĲ����� 
 * ��Щ����������ȫ��������ɺ���ܽ��У����������������������ƵĹ�����
 * @author sundl
 *
 */
public class PostParseOperation {
	
	protected Log log;
	
	public PostParseOperation(Log log) {
		this.log = log;
	}
	
	public class Command {
		public void excute() {
		}
	}
	
	private List<Command> commands = new ArrayList<Command>();
	
	public void addCommand(Command command) {
		this.commands.add(command);
	}
	
	public void run() {
		for (Command cmd : commands) {
			cmd.excute();
		}
	}
	
}
