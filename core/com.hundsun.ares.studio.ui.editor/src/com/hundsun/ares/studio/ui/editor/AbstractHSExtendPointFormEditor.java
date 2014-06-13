/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.editor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;

import com.hundsun.ares.studio.ui.extendpoint.manager.ExtendPageInfo;
import com.hundsun.ares.studio.ui.page.ExtendPageWithMyDirtySystem;
import com.hundsun.ares.studio.ui.page.UserConfigPage;
import com.hundsun.ares.studio.ui.userdialog.DialogInterfaceXml;
import com.hundsun.ares.studio.ui.userdialog.XmlConfigInterface;
import com.hundsun.ares.studio.ui.userdialog.XmlConfigInterfaceConverter;

/**
 * ����ͨ����չ������ҳ��ı༭��
 * 
 * @author maxh
 * 
 */
public abstract class AbstractHSExtendPointFormEditor<T> extends BasicAresFormEditor<T> {
	
	private static Logger logger = Logger.getLogger(AbstractHSExtendPointFormEditor.class);
	/**
	 * �������չ�㴴����ҳ�� KEYΪ��չ�������õ�ҳ���ID
	 */
	protected Map<String, IFormPage> extendsPointPages = new HashMap<String, IFormPage>();

	public Map<String, IFormPage> getExtendsPointPages() {
		return extendsPointPages;
	}

	@Override
	protected void addPages() {
		createExtendPage();
		createUserConfigPage();
	}

	/**
	 * ��ȡ��չҳ��
	 */
	private void createExtendPage() {
		for(ExtendPageInfo info:ARESEditorPlugin.getExtendPageManager().getPageInfo(getSite().getId())){
			try {
				Class cls = info.getPageClass();
				Constructor cst = cls.getConstructor(new Class[] { FormEditor.class, String.class, String.class });
				ExtendPageWithMyDirtySystem page = (ExtendPageWithMyDirtySystem) cst.newInstance(new Object[] { this, info.getPageId(), info.getPageName() });
				if (page.shouldLoad()) {
					addPageContext(page);
					addPage(page);
					extendsPointPages.put(info.getPageId(), page);
				}

			} catch (Exception e) {
				logger.error("��ȡ��չҳ���쳣", e);
			}
		}
	}

	private void createUserConfigPage() {
		if(getARESProject()!=null){
			try {
				XmlConfigInterface config = XmlConfigInterfaceConverter.getConverter().getConfig(getARESProject());
				if(config != null){
					DialogInterfaceXml dialogInterfaceXml = config.getMenuInterfaceXml(getSite().getId());
					if(dialogInterfaceXml != null){
						UserConfigPage page = new UserConfigPage(this,dialogInterfaceXml.getTitle(),dialogInterfaceXml.getTitle(),dialogInterfaceXml);
						addPageContext(page);
						addPage(page);
						extendsPointPages.put(dialogInterfaceXml.getTitle(), page);
					}
				}
			} catch (Exception e) {
				logger.error("��ȡ�û�����ҳ���쳣",e);
			}
		}
	}
}
