/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.database.oracle.ui.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.database.oracle.ui.OracleUI;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.dialog.ImportDialog;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.ImportMetaDataHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePackage;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleSpaceResourceData;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpace;
import com.hundsun.ares.studio.jres.model.database.oracle.TableSpaceRelation;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAction;

/**
 * @author qinyuan
 *
 */
public class ImportTableSpaceAction extends ColumnViewerAction{

	public static final String CV_IMPORT_TABLE_SPACE = "cv.import.tablespace";
	
	IARESResource resource;
	
	private String sheetName;
	
	private String path = "";
	
	private int importType;
	
	private static final String sheetName1 = "��ռ�";
	private static final String sheetName2 = "���ݿ����";
	
	public ImportTableSpaceAction(IARESResource resource,TreeViewer viewer,
			EditingDomain editingDomain) {
		super(viewer, editingDomain);
		
		setText("����");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/import_wiz.gif"));
		setId(CV_IMPORT_TABLE_SPACE);
		this.resource =resource;
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		OracleSpaceResourceData owner = (OracleSpaceResourceData) getViewer().getInput();
		if(null == owner)
			return false;
		else 
			return !((TransactionalEditingDomain)getEditingDomain()).isReadOnly(owner.eResource());
	}
	
	@Override
	public void run() {
		String dialogTitle = "�����ռ�";
		String dialogMessage = "���Ѵ��ڱ�ռ��Excel�ļ����뵽��Ŀ��.";
		Image dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(OracleUI.PLUGIN_ID, "icons/oracle_tablespace.png").createImage();
		ImportDialog dialog = new ImportDialog(((TreeViewer)getViewer()).getTree().getShell(), true,dialogTitle,dialogImage,dialogMessage){
			@Override
			public boolean validate(String fileText) {
				File file = new File(fileText);
				if (!file.exists()) {
					setErrorMessage("ָ���ļ�������!");
					importButton.setEnabled(false);
					return false;
				}
				List<String> types = new ArrayList<String>();
				types.add(sheetName1);
				types.add(sheetName2);
				boolean status = false;
				try {
					status = checkExcel(types, new FileInputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if (!status) {
					setErrorMessage("�ļ���ʽ����! ��ο�  \"����\"�����ĵ�");
					importButton.setEnabled(false);
					return false;
				}
				filePath = fileText;
				setErrorMessage(null);
				importButton.setEnabled(true);
				return true;
			}
		};
		dialog.open();
		
		if(dialog.getReturnCode() != Window.OK){
			throw new OperationCanceledException();
		}
		
		path = dialog.getFilePath();
		
		importType = dialog.getImportType();
		
		if (importType == ImportDialog.IMPORT_TYPE_COVER) {
			MessageDialog msg = new MessageDialog(((TreeViewer)getViewer()).getTree().getShell(), 
					"��ʾ", null, "���뽫ɾ��ԭ�����ݣ��Ƿ������", MessageDialog.QUESTION, new String[]{"��","��"}, 0);
			msg.open();
			if(msg.getReturnCode() != Window.OK){
				throw new OperationCanceledException();
			}
		}
		
		Job job = new Job("�����ռ���Դ") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("�����ռ���Դ������", IProgressMonitor.UNKNOWN);
				Command command = createCommand();
				if (command != null) {
					if ( getViewer().isCellEditorActive()) {
						 getViewer().cancelEditing();
					}
					
					getEditingDomain().getCommandStack().execute(command);
					clearCommand();

					// �ñ��ѡ��Ӱ������Ķ���
					Command mostRecentCommand = getEditingDomain().getCommandStack()
							.getMostRecentCommand();
					if (mostRecentCommand != null) {
						setSelectionToViewer(mostRecentCommand.getAffectedObjects());
					}
				}
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		
		
	}
	
	@Override
	protected Command createCommand() {
		
		RecordingCommand cc = new RecordingCommand((TransactionalEditingDomain)getEditingDomain()) {
			
			@SuppressWarnings("unchecked")
			@Override
			protected void doExecute() {
				
				InputStream excelStream = null;
				List<List<String>> table = null;
				List<EObject> tableSpaces = null;
				List<EObject> tsrs = null;
				Map< String, List< List<String> > > contents = null;
				ImportMetaDataHelper helper = ImportMetaDataHelper.getInstance();
				{
					try {
						excelStream = new FileInputStream(path);
						OracleSpaceResourceData space = (OracleSpaceResourceData) getViewer().getInput();
						if(null == space)
							throw new OperationCanceledException();
						
						//ɾ��ԭ����
						if (importType == ImportDialog.IMPORT_TYPE_COVER) {
							space.getRelations().clear();
							space.getSpaces().clear();
							space.getHistories().clear();
						}
						
						HSSFWorkbook workBook = new HSSFWorkbook(excelStream);
						
						List<List<String>>  revHisContents = POIUtils.getAresContents(workBook, "�汾ҳ", 11, 1);
						List<EObject> revHisInfos = helper.getRevHisesInfos(resource, revHisContents);
						
						if (StringUtils.equals(sheetName, sheetName1)) {
							contents = POIUtils.getExcelStringForCate(workBook, 
									new String[]{sheetName,"������ռ�"}, new int[]{1 ,1}, new int[]{1 ,1});
							table = contents.get(sheetName);
							tableSpaces = POIUtils.importExcelStringTable(table, OraclePackage.Literals.TABLE_SPACE,
									new String[]{"��ռ���", "�߼���", "������","���ݿ��û�" ,"�ļ���" ,"��С" ,"��ע"}, 
									new EStructuralFeature[]{
									OraclePackage.Literals.TABLE_SPACE__NAME,
									OraclePackage.Literals.TABLE_SPACE__LOGIC_NAME,
									OraclePackage.Literals.TABLE_SPACE__CHINESE_NAME,
									OraclePackage.Literals.TABLE_SPACE__USER,
									OraclePackage.Literals.TABLE_SPACE__FILE,
									OraclePackage.Literals.TABLE_SPACE__SIZE,
									OraclePackage.Literals.TABLE_SPACE__DESCRIPTION}, 
									true, 
									new String[0], new String[0], resource);
							
							table = contents.get("������ռ�");
							tsrs = POIUtils.importExcelStringTable(table, OraclePackage.Literals.TABLE_SPACE_RELATION,
									new String[]{ "����ռ�", "������ռ�"}, 
									new EStructuralFeature[]{
									OraclePackage.Literals.TABLE_SPACE_RELATION__MAIN_SPACE,
									OraclePackage.Literals.TABLE_SPACE_RELATION__INDEX_SPACE}, 
									true, 
									new String[0], new String[0], resource);
							
						}else if (StringUtils.equals(sheetName, sheetName2)) {
							contents = POIUtils.getExcelStringForCate(workBook, 
									new String[]{sheetName}, new int[]{1}, new int[]{1});
							table = contents.get(sheetName);

							tableSpaces = POIUtils.importExcelStringTable(table, OraclePackage.Literals.TABLE_SPACE,
									new String[]{"���ݿ���ռ�", "���ݿ��߼���", "���ݿ�������","���ݿ�ֲ�" ,"FILENAME" ,"SIZE" ,"���ݿⱸע"}, 
									new EStructuralFeature[]{
									OraclePackage.Literals.TABLE_SPACE__NAME,
									OraclePackage.Literals.TABLE_SPACE__LOGIC_NAME,
									OraclePackage.Literals.TABLE_SPACE__CHINESE_NAME,
									OraclePackage.Literals.TABLE_SPACE__USER,
									OraclePackage.Literals.TABLE_SPACE__FILE,
									OraclePackage.Literals.TABLE_SPACE__SIZE,
									OraclePackage.Literals.TABLE_SPACE__DESCRIPTION}, 
									true, 
									new String[0], new String[0], resource);
							
							try {
								table.get(0).set(10, "��ʷ��ռ�");
								table.get(0).set(11, "��ʷ������ռ�");
								table.get(0).set(12, "�鵵��ռ�");
								table.get(0).set(13, "�鵵������ռ�");
								table.get(0).set(14, "�����ռ�");
								table.get(0).set(15, "�����ռ�");
								table.get(0).set(16, "����������ռ�");
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							for (int i = 1; i < table.size(); i++) {
								if (StringUtils.isBlank(table.get(i).get(10))
										&& StringUtils.isBlank(table.get(i).get(11))
										&& StringUtils.isBlank(table.get(i).get(12))
										&& StringUtils.isBlank(table.get(i).get(13))
										&& StringUtils.isBlank(table.get(i).get(14))
										&& StringUtils.isBlank(table.get(i).get(15))
										&& StringUtils.isBlank(table.get(i).get(16))) {
									table.remove(i);
									i--;
								}
							}
							
							tsrs = POIUtils.importExcelStringTable(table, OraclePackage.Literals.TABLE_SPACE_RELATION,
									new String[]{ "���ݿ���ռ�", "�����������ݿ�"}, 
									new EStructuralFeature[]{
									OraclePackage.Literals.TABLE_SPACE_RELATION__MAIN_SPACE,
									OraclePackage.Literals.TABLE_SPACE_RELATION__INDEX_SPACE}, 
									true, 
									new String[0], new String[0], resource);
						}
						
						// ��ӵ���Դ
						space.getSpaces().addAll((Collection<? extends TableSpace>) tableSpaces);
						space.getRelations().addAll((Collection<? extends TableSpaceRelation>) tsrs);
						space.getHistories().addAll((Collection<? extends RevisionHistory>) revHisInfos);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						IOUtils.closeQuietly(excelStream);
					}
				}
			}
		};
		return cc;
	}
	
	private boolean checkExcel (List<String> sheetNames , InputStream excelStream){
		try {
			HSSFWorkbook workBook = new HSSFWorkbook(excelStream);
			for (String sheetName : sheetNames) {
				HSSFSheet sheet = workBook.getSheet(sheetName);
				this.sheetName = sheetName;
				if (sheet != null) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public IARESResource getResource() {
		return resource;
	}
}
