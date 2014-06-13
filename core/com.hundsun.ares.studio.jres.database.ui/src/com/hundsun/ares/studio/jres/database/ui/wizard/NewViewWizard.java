/**
 * Դ�������ƣ�NewViewWizard.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.wizard;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleConstant;
import com.hundsun.ares.studio.jres.database.ui.DatabaseUI;
import com.hundsun.ares.studio.jres.database.ui.extend.ModuleDatabasePropertyPage;
import com.hundsun.ares.studio.jres.model.database.ViewResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.DatabaseModuleExtensibleProperty;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleFactory;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleTableProperty;
import com.hundsun.ares.studio.jres.model.database.oracle.table_type;
import com.hundsun.ares.studio.ui.newwizard.ARESResourceNewWizardPage;
import com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard;


/**
 * @author qinyuan
 *
 */
public class NewViewWizard   extends ModuleARESResourceNewWizard {

	@Override
	protected String getResType() {
		return "view";
	}
	
	@Override
	protected void initNewResourceInfo(Object info) {
		super.initNewResourceInfo(info);
		if (info instanceof ViewResourceData) {
			Map<Object, Object> context = getContext();
			String resname = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_NAME).toString();
			String resCName = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_CNAME).toString();
			ViewResourceData view = (ViewResourceData) info;
			if(StringUtils.isNotBlank(resname)){
				view.setName(resname);
			}
			if(StringUtils.isNotBlank(resCName)){
				view.setChineseName(resCName);
			}
			fillViewInfo((ViewResourceData) info);
		}
	}
	@Override
	public void addPages() {
		super.addPages();
		getShell().setImage(AbstractUIPlugin.imageDescriptorFromPlugin(DatabaseUI.PLUGIN_ID, "icons/view.gif").createImage());
	}
	
	/**
	 * �½�����Դʱ�����ģ�͵����Ĭ����Ϣ
	 * 
	 * @param view
	 */
	private void fillViewInfo(ViewResourceData view){
		if(selectedElement instanceof IARESModule){
			IARESModule module = (IARESModule)selectedElement;
			while(module != null){
				IARESResource moduleRes = module.getARESResource("module.xml");
				if (moduleRes != null) {
					try {
						ModuleProperty modulePro = moduleRes.getInfo(ModuleProperty.class);
						DatabaseModuleExtensibleProperty mem = (DatabaseModuleExtensibleProperty) modulePro.getMap().get(ModuleDatabasePropertyPage.KEY);
						if (mem == null) {
							module = module.getParentModule();
							continue;
						}
						OracleTableProperty tableOp = OracleFactory.eINSTANCE.createOracleTableProperty();
						view.getData2().put(IOracleConstant.TABLE_DATA2_KEY, tableOp);
						String moduleSpace = mem.getSpace();
						table_type moduleTableType = mem.getTableType();
						if (StringUtils.isBlank(moduleSpace) && moduleTableType == null) {
							module = module.getParentModule();
							continue;
						}
						tableOp.setSpace(moduleSpace);
						break;
					} catch (ARESModelException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}