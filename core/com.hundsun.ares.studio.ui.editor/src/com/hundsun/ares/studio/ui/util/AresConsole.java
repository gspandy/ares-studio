/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.util;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;

/**
 * 
 * ��������ʱ�Ŀ���̨���
 * @author maxh
 *
 */
public class AresConsole {
	static AresConsole console;
	MessageConsole messageConsole;
	MessageConsoleStream messageOut;
	MessageConsoleStream errorOut;
	
	private AresConsole() {
	   messageConsole = new MessageConsole("ARES����̨",null);
	   ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{ messageConsole });
	   messageOut = messageConsole.newMessageStream();
	   errorOut = messageConsole.newMessageStream();
	   errorOut.setColor(ARESEditorPlugin.getDefault().getColorManager().getColor(HSColorManager.RED));
	}
	
	static public AresConsole getInstance(){
		if(console == null){
			console = new AresConsole();
		}
		return console;
	}
	
	/**
	 * ����̨���һ����Ϣ
	 * @param outString
	 */
	public void consoleMessage(String outString){
		try {
			messageOut.print(outString);
			errorOut.print("\n");
			messageOut.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����̨���������Ϣ
	 * @param outString
	 */
	public void consoleError(String outString){
		try {		
			errorOut.print(outString);
			errorOut.print("\n");
			errorOut.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
