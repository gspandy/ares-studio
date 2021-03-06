/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: 恒生电子股份有限公司</p>
*/
package com.hundsun.ares.studio.logic.ui.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.logic.ResourceEngin;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicEngineContextConstant;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;

/**
 * @author qinyuan
 *
 */
public class PreviewUpdater implements IPageChangedListener {
	
	private List<String> skeletonList = new ArrayList<String>();
	private LogicGenCodeThread logicGenCodeThread = null;
	public PreviewUpdater(String skeleton){
		skeletonList.add(skeleton);
	}
	
	public PreviewUpdater(String[] skeletons) {
		skeletonList.addAll(Arrays.asList(skeletons));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IPageChangedListener#pageChanged(org.eclipse.jface.dialogs.PageChangedEvent)
	 */
	@Override
	public void pageChanged(PageChangedEvent event) {
		if (event.getSelectedPage() instanceof LogicPreViewPage) {
			LogicPreViewPage page = (LogicPreViewPage) event.getSelectedPage();
			ResourceEngin engin = new ResourceEngin();
			Map<Object, Object> context = new HashMap<Object, Object>();
			IARESResource resource = page.getEditor().getARESResource();
			context.put(ILogicEngineContextConstant.ResourceModel, page.getEditor().getInfo());
//			IARESProject project = resource.getARESProject();
//			context.put(IEngineContextConstant.Aresproject, project);
//			IFunctionMacroTokenService funcService = DataServiceManager.getInstance().getService(project, IFunctionMacroTokenService.class);
//			context.put(IEngineContextConstant.Function_Macro_Service, funcService);
//			context.put(IEngineContextConstant.UserMacro_Service, DataServiceManager.getInstance().getService(project,IUserMacroTokenService.class));

			String text = "正在生成代码......";
			page.setInput(new TextEditorInput(text));
			if(logicGenCodeThread!=null){
				logicGenCodeThread.genCode(true);
			}
			logicGenCodeThread = new LogicGenCodeThread(page,skeletonList,engin,resource,context);
			logicGenCodeThread.genCode(false);
			 
		}
	}


}
