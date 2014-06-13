/*
 * ϵͳ����: ARES Ӧ�ÿ��ٿ�����ҵ�׼�
 * ģ������:
 * �� �� ��: ARESConsole.java
 * �����Ȩ: ���ݺ������ӹɷ����޹�˾
 * ����ĵ�:
 * �޸ļ�¼:
 * �޸�����      �޸���Ա                     �޸�˵��<BR>
 * ========     ======  ============================================
 * 20110224     mawb	��Ӧ�޸ĵ��ţ�20110128022
 * ========     ======  ============================================
 * �����¼��
 * 
 * ������Ա��
 * �������ڣ�
 * �������⣺
 */
package com.hundsun.ares.studio.ui.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.hundsun.ares.studio.core.ConsoleHelper;


/**
 * ARES����̨��
 * 
 * @author mawb
 */
public class ARESConsole extends MessageConsole {
	
	protected MessageConsoleStream infoStream;
	protected MessageConsoleStream warningStream;
	protected MessageConsoleStream errorStream;
	
	public ARESConsole() {
		super(ConsoleHelper.CONSOLE_ID, null);
		
		this.setTabWidth(4);
		
		this.infoStream = this.newMessageStream();
		this.warningStream = this.newMessageStream();
		this.errorStream = this.newMessageStream();
		
		this.loadPreferences();
	}
	
	protected void loadPreferences() {
		// 2011-09-13 sundl �޸�Ϊʹ��ϵͳ��ɫ
		// ������Ϣ����ɫ
		
		// UI����������UI�߳�ִ�У������׳� org.eclipse.swt.SWTException: Invalid thread access
		// by xuzhen at 2011-09-19
		// TODO ��Ҫ���Դ���
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				infoStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				
				// ���þ�������ɫ
				warningStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW));
				
				// ���ô�������ɫ
				errorStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			}
		});

	}
	
	/**
	 * @return the infoStream
	 */
	public MessageConsoleStream getInfoStream() {
		return infoStream;
	}

	/**
	 * @return the warningStream
	 */
	public MessageConsoleStream getWarningStream() {
		return warningStream;
	}

	/**
	 * @return the errorStream
	 */
	public MessageConsoleStream getErrorStream() {
		return errorStream;
	}

	public void shutdown() {
		super.dispose();
		
		ConsolePlugin.getDefault().getConsoleManager().removeConsoles(new IConsole[] {this});
		
		// unsupported in Eclipse IDE 3.0
//		try {this.infoStream.close();} catch (Exception ex) {}
//		try {this.warningStream.close();} catch (Exception ex) {}
//		try {this.errorStream.close();} catch (Exception ex) {}
		
		// UI����������UI�߳�ִ�У������׳� org.eclipse.swt.SWTException: Invalid thread access
		// by xuzhen at 2011-09-19
		// TODO ��Ҫ���Դ���
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {infoStream.close();} catch (Exception ex) {}
				try {warningStream.close();} catch (Exception ex) {}
				try {errorStream.close();} catch (Exception ex) {}
			}
		});
	
	}

}
