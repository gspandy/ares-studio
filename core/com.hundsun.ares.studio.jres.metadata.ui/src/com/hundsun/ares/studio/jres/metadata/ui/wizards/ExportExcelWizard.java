/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.metadata.ui.Language;
import com.hundsun.ares.studio.jres.metadata.ui.LanguageRegister;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IHeaderSorter;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.StandardFieldList;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValueList;
import com.hundsun.ares.studio.ui.util.DialogHelper;

/**
 * @author gongyf
 *
 */
public class ExportExcelWizard extends Wizard implements IExportWizard {

	private IWorkbench workbench;
	private IStructuredSelection selection;
	private ExportExcelWizardPage onePage;
	
	/**
	 * 
	 */
	public ExportExcelWizard() {
		setWindowTitle("����Ԫ����");
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public void createPageControls(Composite pageContainer) {
		// TODO Auto-generated method stub
		super.createPageControls(pageContainer);
		onePage.getShell().setImage(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/metadataFolder.gif").createImage());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(onePage = new ExportExcelWizardPage("one", selection));
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				monitor.beginTask("��ʼ����������", 4);
				
				OutputStream excelStream = null;
				
				try {
					excelStream = new FileOutputStream(onePage.getExcelFile());
					
					Map< String, List< List<String> > > contents = new HashMap<String, List<List<String>>>();
					
					Language[] languages = LanguageRegister.getInstance().getRegisteredLanguages();
					String[] languageIds = new String[languages.length];
					String[] languageTitles = new String[languages.length];
					for (int i = 0; i < languageIds.length; i++) {
						languageIds[i] = languages[i].getId();
						languageTitles[i] = languages[i].getName();
					}
					
					IARESProject project = ARESCore.create(onePage.getSelectedProject());
					
					IHeaderSorter descriptionLast = new IHeaderSorter() {
						
						@Override
						public void sort(List<String> header) {
							int index = header.indexOf("˵��");
							if (index >= 0) {
								header.remove(index);
								header.add("˵��");
							}
						}
					};
					List<List<String>> revHisTotles = new ArrayList<List<String>>();
					// ��׼�ֶ�
					{
						
						monitor.subTask("��׼�ֶΡ�����");
						IARESResource resource = project.findResource(IMetadataResType.StdField, IMetadataResType.StdField);
						StandardFieldList list = resource.getInfo(StandardFieldList.class);
						
						List<List<String>> stdHises = getRevHises(resource, list);
						stdHises.remove(0);
						revHisTotles.addAll(stdHises);
						
						List< List<String> > table = POIUtils.exportExcelStringTable(list, MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,MetadataPackage.Literals.STANDARD_FIELD,
								new String[]{"�ֶ���","�ֶ�����","�ֶ�����","�ֵ���Ŀ","˵��"}, 
								new EStructuralFeature[]{
									MetadataPackage.Literals.NAMED_ELEMENT__NAME, 
									MetadataPackage.Literals.STANDARD_FIELD__DATA_TYPE,
									MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
									MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE,
									MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
								true, 
								ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY, resource, null);
						
						contents.put("��׼�ֶ�", table);
						
						monitor.worked(1);
					}
					
					// Ĭ��ֵ
					{
						monitor.subTask("Ĭ��ֵ������");
						IARESResource resource = project.findResource(IMetadataResType.DefValue, IMetadataResType.DefValue);
						TypeDefaultValueList list = resource.getInfo(TypeDefaultValueList.class);
						
						List<List<String>> devHises = getRevHises(resource, list);
						devHises.remove(0);
						revHisTotles.addAll(devHises);
						
						List< List<String> > table = POIUtils.exportExcelStringTable(list, MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS, MetadataPackage.Literals.TYPE_DEFAULT_VALUE,
								new String[]{"Ĭ��ֵ��", "����", "˵��"}, 
								new EStructuralFeature[]{MetadataPackage.Literals.NAMED_ELEMENT__NAME, MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME, MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
								true, 
								languageTitles, languageIds, resource, descriptionLast);
						
						contents.put("Ĭ��ֵ", table);
						
						monitor.worked(1);
					}
					
					
					// ��׼��������
					{
						monitor.subTask("��׼�������͡�����");
						IARESResource resource = project.findResource(IMetadataResType.StdType, IMetadataResType.StdType);
						StandardDataTypeList list = resource.getInfo(StandardDataTypeList.class);
						
						List<List<String>> styHises = getRevHises(resource, list);
						styHises.remove(0);
						revHisTotles.addAll(styHises);
						
						List< List<String> > table = POIUtils.exportExcelStringTable(list, MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,MetadataPackage.Literals.STANDARD_DATA_TYPE,
								new String[]{"������", "����", "˵��"}, 
								new EStructuralFeature[]{MetadataPackage.Literals.NAMED_ELEMENT__NAME, MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME, MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
								true, 
								languageTitles, languageIds, resource, descriptionLast);
						
						contents.put("��׼��������", table);
						
						monitor.worked(1);
					}
					
					// ҵ����������
					{
						monitor.subTask("ҵ���������͡�����");
						IARESResource resource = project.findResource(IMetadataResType.BizType, IMetadataResType.BizType);
						BusinessDataTypeList list = resource.getInfo(BusinessDataTypeList.class);
						
						List<List<String>> bizHises = getRevHises(resource, list);
						bizHises.remove(0);
						revHisTotles.addAll(bizHises);
						
						List< List<String> > table = POIUtils.exportExcelStringTable(list, MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,MetadataPackage.Literals.BUSINESS_DATA_TYPE,
								new String[]{"������","��������","��׼����","����","����","Ĭ��ֵ","˵��"}, 
								new EStructuralFeature[]{
									MetadataPackage.Literals.NAMED_ELEMENT__NAME, 
									MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
									MetadataPackage.Literals.BUSINESS_DATA_TYPE__STD_TYPE,
									MetadataPackage.Literals.BUSINESS_DATA_TYPE__LENGTH,
									MetadataPackage.Literals.BUSINESS_DATA_TYPE__PRECISION,
									MetadataPackage.Literals.BUSINESS_DATA_TYPE__DEFAULT_VALUE,
									MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION}, 
								true, 
								ArrayUtils.EMPTY_STRING_ARRAY, ArrayUtils.EMPTY_STRING_ARRAY, resource, null);
						
						contents.put("ҵ����������", table);
						
						monitor.worked(1);
					}
					
					POIUtils.putExcelString(excelStream, "Ԫ���ݹ淶�ĵ�" ,contents,revHisTotles,
							new String[]{"��׼�ֶ�", "ҵ����������","��׼��������","Ĭ��ֵ"}, 
							new int[]{1,1,1,1}, 
							new int[]{1,1,1,1});
					
					monitor.done();
				} catch (Exception e) {
					e.printStackTrace();
					throw new InvocationTargetException(e);
				} finally {
					IOUtils.closeQuietly(excelStream);
				}
				
			}
		};
		
		
		try {
//			getContainer().run(true, false, runnable);
			MessageDialog msgdialog = null;
			try {
				IProgressService progress = MetadataUI.getDefault().getWorkbench()
						.getProgressService();
				progress.run(true, false, runnable);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				msgdialog = new MessageDialog(null, "����ʧ��", null,
						"�����ļ��ѱ��򿪻���д��Ȩ�ޣ�������������رպ��ٵ�����", MessageDialog.WARNING,
						new String[] { "ȷ��" }, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (null != msgdialog){
				msgdialog.open();
			}else {
				String path = onePage.getExcelFile().getPath();
				msgdialog = new MessageDialog(null, "�����ɹ�", null,
						String.format("����·��Ϊ%s,�Ƿ���ļ���", path), MessageDialog.INFORMATION, new String[] {
								"ȷ��","ȡ��"}, 0);
				if(Window.OK == msgdialog.open()){
					openFile(path);
				}
				return true;
			}
		} catch (Exception e) {
			DialogHelper.showErrorMessage(e.getMessage());
		} 
		return false;
	}
	
	/**
	 * 
	 * ��ȡ�޶���Ϣ
	 * 
	 * @param resource
	 * @param info
	 * @return
	 */
	private List<List<String>> getRevHises(IARESResource resource , MetadataResourceData info){
		List<List<String>> revHises = POIUtils
		.exportExcelStringTable(
				info,
				CorePackage.Literals.JRES_RESOURCE_INFO__HISTORIES,
				CorePackage.Literals.REVISION_HISTORY,
				new String[] {"�޸İ汾", "�޶�����","�޸�����","�޸�ԭ��", "�޸ĵ�","������","������","��ע"},
				new EStructuralFeature[] {
						CorePackage.Literals.REVISION_HISTORY__VERSION,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED_DATE,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED_REASON,
						CorePackage.Literals.REVISION_HISTORY__ORDER_NUMBER,
						CorePackage.Literals.REVISION_HISTORY__CHARGER,
						CorePackage.Literals.REVISION_HISTORY__MODIFIED_BY,
						CorePackage.Literals.REVISION_HISTORY__COMMENT},
				false, ArrayUtils.EMPTY_STRING_ARRAY,
				ArrayUtils.EMPTY_STRING_ARRAY,
				resource, null);
		
		return revHises;
	}
	
	private void openFile(final String path) {    
	    Runtime rn = Runtime.getRuntime(); 
	    String cmd="cmd.exe /c start \"\" \"" + path + "\"";  
	    try {    
	        rn.exec(cmd);  
	    } catch (Exception e) { 
	    	e.printStackTrace();
	    }    
	}

}
