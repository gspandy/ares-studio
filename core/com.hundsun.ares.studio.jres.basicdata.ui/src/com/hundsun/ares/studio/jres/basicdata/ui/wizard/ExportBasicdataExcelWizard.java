package com.hundsun.ares.studio.jres.basicdata.ui.wizard;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataEpacakgeConstant;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataRestypes;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.PackageDefine;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.SingleTable;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.impl.BasicDataBaseModelImpl;
import com.hundsun.ares.studio.jres.basicdata.logic.epackage.BasicDataEpackageFactory;
import com.hundsun.ares.studio.jres.basicdata.ui.BasicDataUI;

public class ExportBasicdataExcelWizard extends ExportExcelWizard{
	private static Logger logger = Logger.getLogger(ExportBasicdataExcelWizard.class);
	private Multimap<IARESModule ,IARESResource> basicDataMap = LinkedHashMultimap.<IARESModule, IARESResource>create();

	public ExportBasicdataExcelWizard() {
		super("");
		setWindowTitle("������������");

	}
	
	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		page.getShell().setImage(AbstractUIPlugin.imageDescriptorFromPlugin(BasicDataUI.PLUGIN_ID, "icons/full/obj16/BasicDataBaseModel.gif").createImage());
	}

	@Override
	public void addPages() {
		IFolder rootFolder = ARESElementUtil.getModuleRootFolder(this.project,"com.hundsun.ares.studio.jres.moduleroot.commondata");
		if( rootFolder != null){
			setModuleRootName(rootFolder.getName());//�������ͨ����չ��õ��ļ�������δ����business
			addPage(page = new ExportWizardPage("������������", selection ,moduleRootName){
				protected void addDirButtonListener(org.eclipse.swt.widgets.Button dirButton, final org.eclipse.swt.widgets.Group pathGroup) {
					dirButton.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(final SelectionEvent e) {
							
							DirectoryDialog dialog = new DirectoryDialog(pathGroup.getShell(), SWT.OPEN);
							String filePath = dialog.open();
							txtPath.setText(filePath);
						}
						
					});
				};
			});//����ʾ�����򵼽���ʱ��������ָ��һ��ģ���Ŀ¼
			return;
		}
		MessageDialog.openError(getShell(), "��������", "��ǰ���̲����ڻ�������ģ�����");
	}
	
	@Override
	public IWorkspaceRunnable createExportOperation(IARESProject project,
			final List<IARESResource> results, String file) {
			IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				monitor.beginTask("��Դ��������", 4);
				for(IARESResource res : results){
					//��ģ�����
					iteratorModuleRes(res);
				}
				monitor.worked(1);
				monitor.subTask("��ʼ����...");
				exportExcel(monitor);
				monitor.done();
			}
		};
		return runnable;
	}
	
	public void exportExcel(IProgressMonitor monitor) {
		monitor.subTask("�����ĵ�...");
		OutputStream output = null;
		
		String path = page.getPath();
		//ÿһ��ģ�鴴��һ���ļ�
		for (IARESModule module : basicDataMap.keySet()) {
			try {
				Map<String, List<String>> headInfo = new LinkedHashMap<String, List<String>>();
				Map<String, List<List<String>>> contents = new LinkedHashMap<String, List<List<String>>>();
				String moduleCname = getMoudleCname("." ,module);
				String fileName = StringUtils.isBlank(moduleCname)? module.getShortName():moduleCname;
				if(StringUtils.isNotBlank(moduleCname)){
					fileName+="("+module.getShortName()+")";
				}
				output = new FileOutputStream(path + "\\ͨ������-" + StringUtils.replace(fileName, ".", "-") + ".xls");
				
				//��װ������Ϣ
				
				constractContent(moduleCname,headInfo,contents,module);
				
				// ����contents�е����ݾ�����Ҫ�ж��ٸ�sheetҳ
				int[] starts = new int[contents.keySet().size()];
				for (int i = 0; i < starts.length; i++) {
					starts[i] = 1;// sheetҳ����ʼλ��
				}
				
				ExportBasicdataUtil.exportBasicData(headInfo, output, contents,
						contents.keySet().toArray(new String[0]), starts,
						starts);
//				POIUtils.putExcelString(output, null, contents,null,
//						contents.keySet().toArray(new String[0]), starts,
//						starts);

			} catch (FileNotFoundException e) {
				throw new RuntimeException("�����ļ������ڣ����鵼��·����"+e.getMessage() ,e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}finally{
				if (output != null) {
					IOUtils.closeQuietly(output);
				}
			}
		}
		monitor.worked(1);
	}
	
	/**
	 * ��ȡģ��������
	 * @param module
	 * @return
	 */
//	private String getMoudleCname(IARESModule module) {
//		IARESResource res = module.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
//		ModuleProperty property = null;
//		try {
//			property = res.getInfo(ModuleProperty.class);
//		} catch (ARESModelException e) {
//			e.printStackTrace();
//			return StringUtils.EMPTY;
//		}
//		String cName = property.getString(ICommonModel.CNAME);
//		return StringUtils.isBlank(cName)?StringUtils.EMPTY:cName;
//	}

	public String getMoudleCname (String separator ,IARESModule module){
		StringBuffer sb = new StringBuffer();
		getModuleChineseName(module, sb ,separator);
		String[] ps = StringUtils.split(sb.toString(), separator);
		StringBuffer sbf = new StringBuffer();
		for (int i = ps.length-1; i > -1 && ps.length > 0; i--) {
			if (StringUtils.isNotBlank(sbf.toString())) {
				sbf.append(separator);
			}
			sbf.append(ps[i]);
		}
		return sbf.toString();
	}
	
	public void getModuleChineseName (IARESModule module , StringBuffer sb , String separator){
		if (module != null) {
			IARESResource property = module.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
			if (property != null && property.exists()) {
				try {
					ModuleProperty info = property.getInfo(ModuleProperty.class);
					if (info != null) {
						Object obj = info.getValue(ICommonModel.CNAME);
						if (obj != null) {
							if (StringUtils.isNotBlank(sb.toString())) {
								sb.append(separator);
							}
							sb.append(obj.toString());
						}
					}
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			getModuleChineseName(module.getParentModule(), sb ,separator);
		}
	}
	
	private List<String> getDirTitle() {
		List<String> dirTitle = new ArrayList<String>();
		dirTitle.add("ģ��Ӣ����");
		dirTitle.add("ģ��������");
		dirTitle.add("�����");
		dirTitle.add("��������������");
		dirTitle.add("��������Ӣ����");
		dirTitle.add("������Ӣ����");
//		dirTitle.add("������������");
		dirTitle.add("��������ģʽ");
		dirTitle.add("��Դsheet��");
		dirTitle.add("���ɽű��ļ���");
		return dirTitle;
	} 
	
	private String valideSheetName(String name){
		if(name.length() > 31){
			return String.format("����[%s]���ܴ���31���ַ�",name);
		}else if(name.startsWith("'") || name.endsWith("'")){
			return String.format("����[%s]������\"'\"��ʼ�����",name);
		}else if(StringUtils.contains(name, ":")){
			return String.format("����[%s]���ܰ��������ַ�\":\"",name);
		}else if(StringUtils.contains(name, "\\")){
			return String.format("����[%s]���ܰ��������ַ�\"\\\"",name);
		}else if(StringUtils.contains(name, "*")){
			return String.format("����[%s]���ܰ��������ַ�\"*\"",name);
		}else if(StringUtils.contains(name, "?")){
			return String.format("����[%s]���ܰ��������ַ�\"?\"",name);
		}else if(StringUtils.contains(name, "/")){
			return String.format("����[%s]���ܰ��������ַ�\"/\"",name);
		}else if(StringUtils.contains(name, "[")){
			return String.format("����[%s]���ܰ��������ַ�\"[\"",name);
		}else if(StringUtils.contains(name, "]")){
			return String.format("����[%s]���ܰ��������ַ�\"]\"",name);
		}else if(StringUtils.isBlank(name)){
			return String.format("����[%s]����Ϊ��",name);
		}
		
		return StringUtils.EMPTY;
	}
	
	private void constractContent(String moduleCname,Map<String, List<String>> headInfo, Map<String, List<List<String>>> contents,
			IARESModule module) throws Exception{
		
		//Ŀ¼����
		List<List<String>> dirTable = new ArrayList<List<String>>();
		dirTable.add(getDirTitle());//Ŀ¼ҳ����
		contents.put(ExportBasicdataUtil.dirSheetName, dirTable);
		Collection<IARESResource> ress = basicDataMap.get(module);
		for (IARESResource resource : ress) {
			if(StringUtils.equals(resource.getType(),IBasicDataRestypes.singleTable)){
				BasicDataBaseModelImpl info = null;
				try {
					info = resource.getInfo(BasicDataBaseModelImpl.class);
				} catch (ARESModelException e1) {
					logger.error("������������ʱ����ȡģ��ʧ��", e1);
					continue;
				}//ģ��
				EPackage ePackage = null;
				try {
					ePackage = BasicDataEpackageFactory.eINSTANCE.createEPackage(resource);
				} catch (Exception e) {
					logger.error("������������ʱ����ȡEPackageʧ��", e);
					continue;
				}
				EClass masterEclass = (EClass)ePackage.getEClassifier(IBasicDataEpacakgeConstant.MasterItem);
				List<List<String>> table = ExportBasicdataUtil.getMasterTableInfo(resource, info, masterEclass,true);

				String sheetName = "";
				//Ӣ����
				String name = resource.getName();//��ֱ��ȡģ�͵�����,��Ϊ�п���������,��ʱ��ģ���е�Ӣ�������������µ�
				//�����ģ�����п���û��Ӣ��������ʱҪ����Դ����ΪӢ����
				//if(StringUtils.isBlank(name)) {
				//	name = resource.getName();
				//}
				if (info != null) {
					if (StringUtils.isNotBlank(info.getChineseName())) {
						sheetName = "ͨ������-" + info.getChineseName();
					}
				}
				if(StringUtils.isNotBlank(valideSheetName(sheetName)) || contents.containsKey(sheetName)) {
					sheetName = "ͨ������-" + name;
				}
				if(StringUtils.isNotBlank(valideSheetName(sheetName)) || contents.containsKey(sheetName)) {
//					throw new Exception(valideSheetName(sheetName));
					logger.error(valideSheetName(sheetName));
					sheetName = "ͨ������-N" + sheetName.hashCode();
				}
				
				contents.put(sheetName, table);//����������Ϣ
				
				//������
				PackageDefine define = null;
				try {
					define = BasicDataEpackageFactory.eINSTANCE.getDefine(resource);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String linkedTableName = "";
				if(define != null){
					linkedTableName = ((SingleTable) define).getMaster();
				}
				
				//Ŀ¼��Ϣ
				List<String> dirInfo = new ArrayList<String>();
				dirInfo.add(module.getElementName());
				dirInfo.add(moduleCname);
				dirInfo.add(info.getObjectId());
				dirInfo.add(info.getChineseName());
				dirInfo.add(name);
				dirInfo.add(linkedTableName);
//				dirInfo.add("");
				dirInfo.add("��ά��");
				dirInfo.add(sheetName);
				dirInfo.add(info.getFile());
				dirTable.add(dirInfo);
				
				

				//ͷ��Ϣ
				List<String> headInfoList = new ArrayList<String>();
				headInfoList.add("��������Ӣ����");
				headInfoList.add(name);
				headInfoList.add("��������������");
				headInfoList.add(info.getChineseName());
				headInfoList.add("������");
				headInfoList.add(linkedTableName);
				headInfo.put(sheetName, headInfoList);
				
				
			}else if(StringUtils.equals(resource.getType(),IBasicDataRestypes.MasterSlaveTable)){
			}else if(StringUtils.equals(resource.getType(),IBasicDataRestypes.MasterSlaveLinkTable)){
			}else if(StringUtils.equals(resource.getType(), "module.xml")){
			}else {
			}
		}
	}

	/**
	 * ģ�����Դ�����ݴ�������Դ����������
	 * 
	 * @param res
	 */
	private void iteratorModuleRes(IARESResource res){
		basicDataMap.put(getSubsys(res.getModule()), res);
	}
	
	private IARESModule getSubsys(IARESModule mod){
		if (mod != null) {
			if (StringUtils.indexOf(mod.getElementName(), ".") > -1) {
				return getSubsys(mod.getParentModule());
			}else {
				return mod;
			}
		}else {
			return null;
		}
	}

}
