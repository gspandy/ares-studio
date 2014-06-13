/**
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.actions;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.jres.database.oracle.ui.OracleUI;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.dialog.ExportDialog;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IHeaderSorter;
import com.hundsun.ares.studio.jres.model.database.oracle.OraclePackage;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleSpaceResourceData;
import com.hundsun.ares.studio.ui.editor.actions.IUpdateAction;

/**
 * @author yanwj06282
 * 
 */
public class ExportTableSpaceAction extends Action implements IUpdateAction {
	public static final String CV_EXPORT_TABLE_SPACE = "cv.export.tablespace";
	IARESResource resource;
	IWorkbenchPartSite site;

	public ExportTableSpaceAction(IARESResource resource, IWorkbenchPartSite site) {
		this.resource = resource;
		this.site = site;

		setText("����");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/export_wiz.gif"));
		setId(CV_EXPORT_TABLE_SPACE);

		setEnabled(true);
	}

	@Override
	public void run() {
		String dialogTitle = "������ռ�";
		String dialogMessage = "����Ŀ�еı�ռ�ĵ���(Excel�ļ�).";
		Image dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(OracleUI.PLUGIN_ID, "icons/oracle_tablespace.png").createImage();
		ExportDialog dialog = new ExportDialog(site.getShell(),dialogTitle,dialogImage,dialogMessage);
		dialog.open();

		if (dialog.getReturnCode() != Window.OK)
			return;

		final String path = dialog.getFilePath();

		IRunnableWithProgress operation = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException{
				monitor.beginTask("������ռ䡣����", IProgressMonitor.UNKNOWN);

				OutputStream excelStream = null;
				List<List<String>> table = null;
				Map<String, List<List<String>>> contents = new LinkedHashMap<String, List<List<String>>>();
				try {
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
					
					// �ĵ���һҳ�����
					String title = null;
					
					OracleSpaceResourceData tableSpace = resource.getInfo(OracleSpaceResourceData.class);
					if (tableSpace == null) {
						return;
					}
					
					//�����޶���¼
					List<List<String>> revHises = POIUtils
					.exportExcelStringTable(
							tableSpace,
							CorePackage.Literals.JRES_RESOURCE_INFO__HISTORIES,
							CorePackage.Literals.REVISION_HISTORY,
							new String[] {"�޸İ汾", "�޸�����","�޸�����","�޸�ԭ��", "�޸ĵ�","������","������","��ע"},
							new EStructuralFeature[] {
									CorePackage.Literals.REVISION_HISTORY__VERSION,
									CorePackage.Literals.REVISION_HISTORY__MODIFIED_DATE,
									CorePackage.Literals.REVISION_HISTORY__MODIFIED,
									CorePackage.Literals.REVISION_HISTORY__MODIFIED_REASON,
									CorePackage.Literals.REVISION_HISTORY__ORDER_NUMBER,
									CorePackage.Literals.REVISION_HISTORY__MODIFIED_BY,
									CorePackage.Literals.REVISION_HISTORY__CHARGER,
									CorePackage.Literals.REVISION_HISTORY__COMMENT},
							false, ArrayUtils.EMPTY_STRING_ARRAY,
							ArrayUtils.EMPTY_STRING_ARRAY,
							resource, null);
					
					table = POIUtils
							.exportExcelStringTable(
									tableSpace,
									OraclePackage.Literals.ORACLE_SPACE_RESOURCE_DATA__SPACES,
									OraclePackage.Literals.TABLE_SPACE,
									new String[] { "��ռ���", "�߼���", "������","���ݿ��û�","�ļ���","��С","��ע" },
									new EStructuralFeature[] {
											OraclePackage.Literals.TABLE_SPACE__NAME,
											OraclePackage.Literals.TABLE_SPACE__LOGIC_NAME,
											OraclePackage.Literals.TABLE_SPACE__CHINESE_NAME,
											OraclePackage.Literals.TABLE_SPACE__USER,
											OraclePackage.Literals.TABLE_SPACE__FILE,
											OraclePackage.Literals.TABLE_SPACE__SIZE,
											OraclePackage.Literals.TABLE_SPACE__DESCRIPTION},
									true, new String[0], new String[0],
									resource, descriptionLast);
					contents.put("��ռ�", table);
					
					table = POIUtils.exportExcelStringTable(
							tableSpace,
							OraclePackage.Literals.ORACLE_SPACE_RESOURCE_DATA__RELATIONS,
							OraclePackage.Literals.TABLE_SPACE_RELATION,
							new String[] { "����ռ�", "������ռ�"},
							new EStructuralFeature[] {
									OraclePackage.Literals.TABLE_SPACE_RELATION__MAIN_SPACE,
									OraclePackage.Literals.TABLE_SPACE_RELATION__INDEX_SPACE},
							true, new String[0], new String[0],
							resource, descriptionLast);
					contents.put("������ռ�", table);
					
					title = "���ݿ��ռ�";
					
					//����contents�е����ݾ�����Ҫ�ж��ٸ�sheetҳ
					int[] starts = new int[contents.keySet().size()];
					for (int i = 0; i < starts.length ; i++) {
						starts[i] = 1;//sheetҳ����ʼλ��
					}
					excelStream = new FileOutputStream(path);
					POIUtils.putExcelString(excelStream, title, contents,revHises, contents
							.keySet().toArray(new String[0]), starts, starts);
					
				} catch (Exception e) {
					e.printStackTrace();
					throw new InvocationTargetException(e);
				} finally {
					IOUtils.closeQuietly(excelStream);
				}
				monitor.done();
			}
		};
		MessageDialog msgdialog = null;
		try {
			IProgressService progress = MetadataUI.getDefault().getWorkbench()
					.getProgressService();
			progress.run(true, false, operation);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			String message = "�����ļ��ѱ��򿪻���д��Ȩ�ޣ�������������رպ��ٵ�����";
			Throwable exception = e.getTargetException();
			if(exception instanceof ExportExcelException){
				message = ((ExportExcelException)exception).getMessage();
			}
			msgdialog = new MessageDialog(site.getShell(), "����ʧ��", null,
					message, MessageDialog.WARNING,
					new String[] { "ȷ��" }, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (null != msgdialog)
			msgdialog.open();
		else {
			msgdialog = new MessageDialog(null, "�����ɹ�", null,
					String.format("����·��Ϊ%s,�Ƿ���ļ���", path), MessageDialog.INFORMATION, new String[] {
							"ȷ��","ȡ��"}, 0);
			if(Window.OK == msgdialog.open()){
				openFile(path);
			}
		}
	}
	//�����д��ļ�
	private void openFile(final String path) {    
	    Runtime rn = Runtime.getRuntime();    
	    String cmd="cmd.exe /c start \"\" \"" + path + "\"";  
	    try {    
	        rn.exec(cmd);  
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }    
	}
	
	/***
	 *�Զ����쳣������󵼳�ʧ�ܵ���������ʾ�Զ�����Ϣ��
	 */
	protected class ExportExcelException extends Exception{

		private static final long serialVersionUID = 988537005828794522L;
		
		public ExportExcelException(String message) {
			super(message);
		}
		
	}

	@Override
	public void update() {
		
	}
}
