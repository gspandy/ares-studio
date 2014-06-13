/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.dialog.ImportDialog;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataTypeList;
import com.hundsun.ares.studio.jres.model.metadata.StandardFieldList;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValueList;
import com.hundsun.ares.studio.ui.util.DialogHelper;

/**
 * @author gongyf
 *
 */
public class ImportExcelWizard extends Wizard implements IImportWizard {

	private IWorkbench workbench;
	private IStructuredSelection selection;
	private ImportExcelWizardPage onePage;
	private String[] sheetNames = new String[]{"��׼�ֶ�", "ҵ����������","��׼��������","Ĭ��ֵ"};
	
	/**
	 * 
	 */
	public ImportExcelWizard() {
		setWindowTitle("�����׼�ֶ�");
		setNeedsProgressMonitor(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle("�����׼�ֶ�");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		getShell().setImage(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/stdFieldFile.png").createImage());
		onePage.setErrorMessage("ѡ����Ҫ�����׼�ֶ�.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(onePage = new ImportExcelWizardPage("one", selection){
			protected void validate() {
				super.validate();
				if (StringUtils.isBlank(getErrorMessage())) {
					boolean status = true;
					try {
						HSSFWorkbook workBook = new HSSFWorkbook(new FileInputStream(getExcelFile()));
						for (String sheetName : sheetNames) {
							HSSFSheet sheet = workBook.getSheet(sheetName);
							if (sheet == null) {
								status = false;
								break;
							}
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (!status) {
						setErrorMessage("�ļ���ʽ����! ��ο�  \"Ԫ���ݶ���\"�����ĵ������ĵ��������: [��׼�ֶ�],[ҵ����������],[��׼��������],[Ĭ��ֵ]");
						setPageComplete(false);
					}
				}
			};
		});
		
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
				monitor.beginTask("����", 4);
				
				InputStream excelStream = null;
				
				try {
					excelStream = new FileInputStream(onePage.getExcelFile());
					
					HSSFWorkbook workBook = new HSSFWorkbook(excelStream);
					
					Map< String, List< List<String> > > contents = POIUtils.getExcelString(workBook, 
							sheetNames, new int[]{1,1,1,1}, new int[]{1,1,1,1});
					
					List< List<String> > table = null;
					ImportMetaDataHelper helper = ImportMetaDataHelper.getInstance();
					IARESProject project = ARESCore.create(onePage.getSelectedProject());
					int importType = onePage.getImportMode();
					
					IARESResource defRes = project.findResource(IMetadataResType.DefValue, IMetadataResType.DefValue);
					IARESResource stdtRes = project.findResource(IMetadataResType.StdType, IMetadataResType.StdType);
					IARESResource btRes = project.findResource(IMetadataResType.BizType, IMetadataResType.BizType);
					IARESResource stdRes = project.findResource(IMetadataResType.StdField, IMetadataResType.StdField);
					StringBuffer sb = new StringBuffer();
					if (defRes.isReadOnly()) {
						sb.append("Ĭ��ֵ ��");
					}
					if (stdtRes.isReadOnly()) {
						sb.append("��׼�������� ��");
					}
					if (btRes.isReadOnly()) {
						sb.append("ҵ���������� ��");
					}
					if (stdRes.isReadOnly()) {
						sb.append("��׼�ֶ� ��");
					}
					if (StringUtils.isNotBlank(sb.toString())) {
						throw new InterruptedException("Ԫ���ݵ������ ��������Դ����ֻ��״̬��[" +StringUtils.substring(sb.toString(), 0, sb.toString().length()-1) + "]");
					}
					// Ĭ��ֵ����
					{
						monitor.subTask("Ĭ��ֵ������");
						table = contents.get("Ĭ��ֵ");
						if (table != null) {
							TypeDefaultValueList list = null;
							if (importType == ImportDialog.IMPORT_TYPE_COMB) {
								list = defRes.getInfo(TypeDefaultValueList.class);
							}else {
								list = MetadataFactory.eINSTANCE.createTypeDefaultValueList();
							}
							helper.importDefValue(defRes, table,list, monitor);
						}
						monitor.worked(1);
					}
					
					// ��׼��������
					{
						monitor.subTask("��׼�������͡�����");
						table = contents.get("��׼��������");
						if (table != null) {
							StandardDataTypeList list = null;
							if (importType == ImportDialog.IMPORT_TYPE_COMB) {
								list = stdtRes.getInfo(StandardDataTypeList.class);
							}else {
								list = MetadataFactory.eINSTANCE.createStandardDataTypeList();
							}
							helper.importStdType(stdtRes, table,list, monitor);
						}
						monitor.worked(1);
					}
					
					// ҵ����������
					{
						monitor.subTask("ҵ���������͡�����");
						table = contents.get("ҵ����������");
						if (table != null) {
							BusinessDataTypeList list = null;
							if (importType == ImportDialog.IMPORT_TYPE_COMB) {
								list = btRes.getInfo(BusinessDataTypeList.class);
							}else {
								list = MetadataFactory.eINSTANCE.createBusinessDataTypeList();
							}
							helper.importBizType(btRes, table,list, monitor);
						}
						monitor.worked(1);
					}
					
					// ��׼�ֶ�
					{
						monitor.subTask("��׼�ֶΡ�����");
						table = contents.get("��׼�ֶ�");
						if (table != null) {
							StandardFieldList list = null;
							if (importType == ImportDialog.IMPORT_TYPE_COMB) {
								list = stdRes.getInfo(StandardFieldList.class);
							}else {
								list = MetadataFactory.eINSTANCE.createStandardFieldList();
							}
							helper.importStdFld(stdRes, table,list, monitor);
						}
						monitor.worked(1);
					}
					monitor.done();
				} catch (Exception e) {
					e.printStackTrace();
					throw new InvocationTargetException(e , e.getMessage());
				} finally {
					IOUtils.closeQuietly(excelStream);
				}
				
			}
		};
		
		try {
			MessageDialog msgdialog = null;
			try {
				IProgressService progress = MetadataUI.getDefault().getWorkbench()
						.getProgressService();
				progress.run(true, false, runnable);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				msgdialog = new MessageDialog(null, "����ʧ��", null,
						e.getMessage(), MessageDialog.ERROR,
						new String[] { "ȷ��" }, 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (null != msgdialog){
				msgdialog.open();
			}else {
				return true;
			}
		} catch (Exception e) {
			DialogHelper.showErrorMessage(e.getMessage());
		} 
		return false;
	}

}
