/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.script.engin.ScriptUtils;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionConfigReader;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionDialog;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionRoot;


/**
 * @author lvgao
 *���봦��������
 */
public class ScriptInUtilImpl{
	private static Logger logger = Logger.getLogger(ScriptInUtilImpl.class);
	
	static final Logger console = ConsoleHelper.getLogger();

	int model;
	IARESResource jsResource;
	Map<String, Object> context;
	public ScriptInUtilImpl(int model,IARESResource jsResource,Map<String, Object> context){
		this.model = model;
		this.jsResource = jsResource;
		this.context = context;
	}

	public int getInput() {
		
		//2013��3��15��9:49:37 ���û������ļ���ű��ļ�����һ��
		//2013/5/6 zhuyf ͨ����ȡ������Դģ�����չ�����õ��ļ��У���̬��ȡxml�����ļ���
//		IFolder rootFolder = ARESElementUtil.getModuleRootFolder(jsResource.getARESProject(),"com.hundsun.ares.studio.jres.moduleroot.tools");
		
		String filepath = String.format("/%s.xml", jsResource.getName());
		
		//2013��5��13��9:27:22 �ű���ģ�飬xml�ļ����ܲ���Ĭ��ģ�����棬��Ҫȡ�ű�ֱ�����ڵ�ģ��Ŀ¼
		IFolder rootFolder = (IFolder) jsResource.getParent().getResource();
		
		if(rootFolder == null){
			console.error(String.format(".respath�в�������չ��[%s]��Ӧ��ģ������á�", "com.hundsun.ares.studio.jres.moduleroot.tools"));
			return Window.CANCEL;
		}
		
		IFile file =  rootFolder.getFile(filepath);
		
		if(!file.exists()){
			//console.error(String.format("�ļ�[%s]�����ڡ�", file.getFullPath().toOSString()));
			return Window.OK;
		}
		
		UserOptionConfigReader instance = new UserOptionConfigReader();
		try {
			final UserOptionRoot root = instance.read(file.getContents());
			if(ScriptUtils.MODE_BUILDER == model || ScriptUtils.MODE_CMD_BUILDER == model){
				//root.setOptionValue();
				root.setDefaultValue();
				context.putAll(root.getOptionValue()) ;
			}else{
				UserOptionDialog dlg = new UserOptionDialog(new Shell(),root , jsResource.getARESProject());
				int inState = dlg.open();
				context.putAll(root.getOptionValue()) ;
				return inState;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			console.error(String.format("��ȡ�û�����ʧ�ܣ���ϸ��Ϣ:%s", e.getMessage()));
		}
		return Window.CANCEL;
	}
	
	
	
}
