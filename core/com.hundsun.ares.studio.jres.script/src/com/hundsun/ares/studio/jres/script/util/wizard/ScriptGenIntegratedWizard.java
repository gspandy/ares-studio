/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.util.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionConfigReader;
import com.hundsun.ares.studio.jres.script.internal.useroption.UserOptionRoot;

/**
 *  ͳһ������:��һ���򵼽����г��ű��嵥,�ڶ������г���ѡ��ű�����Ӧ��������
 * @author liaogc
 *
 */
public class ScriptGenIntegratedWizard extends DynamicPageWizard{
	static final Logger console = ConsoleHelper.getLogger();//��־
	private ScriptGenIntegratedPage integratedPage;//���еĵ�һҳ(�г����еĽű��嵥�Ա㹩�û�ѡ��)
	private ScriptGenIntegratedDetailPage detailPage;//���е���ϸ����(�г��ű��嵥�ж�Ӧ����xml���õĽ���)
	private IARESProject project;//�ű�������Ŀ
	private Map<String, Object>context = new HashMap<String, Object>();//����������
	private Map<IARESResource,Map<String, Object>>contexts =  new HashMap<IARESResource,Map<String, Object>>();// �ܵ�������
	private List<ScriptGenInteWizardModel>  inputScriptList = new ArrayList<ScriptGenInteWizardModel>();//��Ӧѡ�����ϵĽڵ�
	private boolean isOK = false;//�û��Ƿ�����ɰ�Ŧ


	

	public ScriptGenIntegratedWizard() {
		setWindowTitle("ͳһ������");
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public boolean performFinish() {
		/*�����û�����Ӧ�ű�������,��װ�ɽű��������Ļ���*/
		for(ScriptGenInteWizardModel model :detailPage.getSelectedHasXmlConfig()){

			Map<String, Object>jsContext = new HashMap<String, Object>();
			if(!contexts.containsKey(model.getJsResource())){
				
				if(context!=null){
					jsContext.putAll(context);
					model.getOptionRoot().setOptionValue();
					jsContext.putAll(model.getOptionRoot().getOptionValue());
					contexts.put(model.getJsResource(), jsContext);
				}
			}else{
			
				if(model.getOptionRoot()!=null){
					 model.getOptionRoot().setOptionValue();
					jsContext.putAll(model.getOptionRoot().getOptionValue());
				}
				contexts.put(model.getJsResource(), jsContext);
			}
		
		}
		isOK = true;//�û�������
		return true;
		
	}
	
	/**
	 * @return the detailPage
	 */
	public ScriptGenIntegratedDetailPage getDetailPage() {
		return detailPage;
	}

	/**
	 * @param detailPage the detailPage to set
	 */
	public void setDetailPage(ScriptGenIntegratedDetailPage detailPage) {
		this.detailPage = detailPage;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public boolean performCancel() {
		return super.performCancel();
	}
	@Override
	public void addPages() {
		loadJSXML();
		integratedPage = new ScriptGenIntegratedPage("ͳһ������",inputScriptList);
		addPage(integratedPage);
		detailPage = new ScriptGenIntegratedDetailPage("������ϸ��Ϣ",project);
		addPage(detailPage);
	}
	
	

	

	@Override
	public boolean canFinish() {
		return getContainer().getCurrentPage().equals(detailPage) && detailPage.isPageComplete();
	}
	
	/**
	 * ���ؽű��ű���Ӧ��������Ϣ
	 */
	private void loadJSXML(){
		if(inputScriptList==null || inputScriptList.size()==0){
			return ;
		}
		for(ScriptGenInteWizardModel model :inputScriptList ){
			Map<String, Object>context = new HashMap<String, Object>();
			String filepath = String.format("/%s.xml", model.getJsResource().getName());
			
			//2013��5��13��9:27:22 �ű���ģ�飬xml�ļ����ܲ���Ĭ��ģ�����棬��Ҫȡ�ű�ֱ�����ڵ�ģ��Ŀ¼
			IFolder rootFolder = (IFolder) model.getJsResource().getParent().getResource();
			
			if(rootFolder == null){
				console.error(String.format(".respath�в�������չ��[%s]��Ӧ��ģ������á�", "com.hundsun.ares.studio.jres.moduleroot.tools"));
				return ;
			}
			IFile file =  rootFolder.getFile(filepath);
			
			if(!file.exists()){
				
				Map<String, Object>jsContext = new HashMap<String, Object>();
				if(context!=null){
					jsContext.putAll(context);
					contexts.put(model.getJsResource(), jsContext);
				}
				continue;
			}
			UserOptionConfigReader instance = new UserOptionConfigReader();
			try {
				final UserOptionRoot root = instance.read(file.getContents());
				model.setOptionRoot(root);
				
				
			} catch (Exception e) {
				console.error(String.format("��ȡ�û�����ʧ�ܣ���ϸ��Ϣ:%s", e.getMessage()));
			}
				
		}
	}
	
	public void setProject(IARESProject project) {
		this.project = project;
	}
	
	/**
	 * @return the context
	 */
	public Map<String, Object> getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	/**
	 * @param inputScriptList the inputScriptList to set
	 */
	public void setInputScriptList(List<ScriptGenInteWizardModel> inputScriptList) {
		this.inputScriptList = inputScriptList;
	}
	
	/**
	 * ��ȡ�û�ѡ��Ľű�
	 * @return
	 */
	public List<ScriptGenInteWizardModel> getSelectedElements() {
		return integratedPage.getSelectedElements();
	}

	/**
	 * @return the isOK
	 */
	public boolean isOK() {
		return isOK;
	}
	/**
	 * ���ݽű���ȡ��Ӧ�Ľű������Ļ���
	 * @param jsResource
	 * @return
	 */
	public Map<String, Object> getContextByJsResource(IARESResource jsResource){
		return  contexts.get(jsResource);
	}
	/**
	 * @return the project
	 */
	public IARESProject getProject() {
		return project;
	}

	
}
