/**
 * Դ�������ƣ�NewTableWizard.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.ui.wizard;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CoreFactory;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.jres.database.oracle.constant.IOracleConstant;
import com.hundsun.ares.studio.jres.database.ui.DatabaseUI;
import com.hundsun.ares.studio.jres.database.ui.extend.ModuleDatabasePropertyPage;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
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
public class NewTableWizard extends ModuleARESResourceNewWizard {

	@Override
	protected void initNewResourceInfo(Object info) {
		super.initNewResourceInfo(info);
		
		if(info instanceof TableResourceData) {
			TableResourceData table = (TableResourceData)info;
			Map<Object, Object> context = getContext();
			String resname = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_NAME).toString();
			String resCName = context.get(ARESResourceNewWizardPage.CONTEXT_KEY_CNAME).toString();
			if(StringUtils.isNotBlank(resname)){
				table.setName(resname);
			}
			if(StringUtils.isNotBlank(resCName)){
				table.setChineseName(resCName);
			}
			//RevisionHistory history = CoreFactory.eINSTANCE.createRevisionHistory();
			//history.setModifiedDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
			//history.setVersion(getVersion());
			//table.getHistories().add(history);
			fillTableInfo(table);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.newwizard.ModuleARESResourceNewWizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		getShell().setImage(AbstractUIPlugin.imageDescriptorFromPlugin(DatabaseUI.PLUGIN_ID, "icons/table.gif").createImage());
	}
	
	/**
	 * �½�����Դʱ�����ģ�͵����Ĭ����Ϣ
	 * 
	 * @param table
	 */
	private void fillTableInfo(TableResourceData table){
		if(selectedElement instanceof IARESModule){
			IARESModule module = (IARESModule)selectedElement;
			while(module != null){
				IARESResource moduleRes = module.getARESResource("module.xml");
				if (moduleRes != null && moduleRes.exists()) {
					try {
						ModuleProperty modulePro = moduleRes.getInfo(ModuleProperty.class);
						DatabaseModuleExtensibleProperty mem = (DatabaseModuleExtensibleProperty) modulePro.getMap().get(ModuleDatabasePropertyPage.KEY);
						if (mem == null) {
							module = module.getParentModule();
							continue;
						}
						OracleTableProperty tableOp = OracleFactory.eINSTANCE.createOracleTableProperty();
						table.getData2().put(IOracleConstant.TABLE_DATA2_KEY, tableOp);
						String moduleSpace = mem.getSpace();
						table_type moduleTableType = mem.getTableType();
						if (StringUtils.isBlank(moduleSpace) && moduleTableType == null) {
							module = module.getParentModule();
							continue;
						}
						tableOp.setSpace(moduleSpace);
						tableOp.setTabletype(moduleTableType);
						break;
					} catch (ARESModelException e) {
						e.printStackTrace();
						module = module.getParentModule();
					}
				}else {
					module = module.getParentModule();
				}
			}
		}
	}
	
	/**
	 * �ο�com.hundsun.ares.studio.ui.editor.actions.AddRevisionHistoryRecordAction.getVersion()
	 * @return
	 */
	private String getVersion() {
		// 2012-09-28 sundl �������汾��ʱ��ֻȡ��Դ���ڵĵ�ǰ���ģ��
		// 2012-11-21 sundl ���ݿ��µ���Դ������ģ����㣻 �����ط�����Դ��Ȼȡ��ǰģ��
		// 2012-12-28 sundl Ԫ�����µ� ��Դ������ģ�飬ֻ�ڱ���Դ����Ŀ������ȡ���ֵ
		IARESModule topModule = null; 
		
		if (selectedElement == null) {
			topModule = null; 
		} else {
			if(selectedElement instanceof IARESModule){
				IARESModule module = (IARESModule)selectedElement;
				if( module.getElementName().indexOf('.') > 0){
					IARESModuleRoot root = module.getRoot();
					String moduleName = StringUtils.substringBefore(module.getElementName(), ".");
					topModule =  root.getModule(moduleName);
				}else {
					topModule = module;
				}
			}
		}
		
		// ��ǰ�Ѿ��������Դ�е����汾
		String currentVersion = RevisionHistoryUtil.getMaxVersion(topModule);
		// ��ǰ�༭���е����汾
		//
//		String maxInEditor = RevisionHistoryUtil.getMaxVersion((List<RevisionHistory>)info.eGet(eReference));

		// ��Ŀ����
		String projectVersion = RevisionHistoryUtil.getProjectPropertyVersion(selectedElement.getARESProject());
		
		// ������3�����ֵ
		String versionStr = RevisionHistoryUtil.max(Arrays.asList(currentVersion, projectVersion));
		// ��һ���Ҳ����κμ�¼��ʱ��
		if (StringUtils.isEmpty(versionStr)) {
			versionStr = "1.0.0.0";
		} 
		return versionStr;
	}
	
	@Override
	protected String getResType() {
		return "table";
	}
	
}
