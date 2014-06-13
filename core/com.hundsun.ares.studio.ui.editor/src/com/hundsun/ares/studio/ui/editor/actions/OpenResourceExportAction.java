/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor.actions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.model.UserExtensibleProperty;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.editor.action.ExportDialog;
import com.hundsun.ares.studio.ui.editor.blocks.OpenResourceBlock;
import com.hundsun.ares.studio.ui.editor.blocks.OpenResourceHelper;
import com.hundsun.ares.studio.ui.editor.blocks.OpenResourceInfo;

/**
 * 
 * @author yanwj06282
 */
public class OpenResourceExportAction extends Action implements IUpdateAction{

	private final Object input;
	private String[] titles;
	private Map<String,String> exTitles;
	private EStructuralFeature[] features;
	
	public OpenResourceExportAction(Object input ,OpenResourceBlock block) {
		this.input = input;
		this.titles = block.titiles;
		this.features = block.features;
		this.exTitles = block.exTitles;
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				ARESEditorPlugin.PLUGIN_ID, "icons/export_wiz.gif"));
	}
	
	@Override
	public void update() {
	}
	
	@Override
	public void run() {

		ExportDialog dialog = new ExportDialog("Export", Display.getDefault().getActiveShell());
		dialog.open();

		if (dialog.getReturnCode() != Window.OK)
			return;

		final String path = dialog.getFilePath();

		IRunnableWithProgress operation = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				monitor.beginTask("����Ԫ������Դ������", IProgressMonitor.UNKNOWN);

				OutputStream excelStream = null;
				List<List<String>> table = new ArrayList<List<String>>();
				//��ӱ���
				List<String> subTils = new ArrayList<String>();
				table.add(subTils);
				subTils.add("����");
				subTils.add("·��");
				subTils.addAll(Arrays.asList(titles));
				subTils.addAll(exTitles.keySet());
				try {
					if (input instanceof List) {
						for (Object item :(List)input) {
							if (item instanceof OpenResourceInfo) {
								List<String> itemList = new ArrayList<String>();
								itemList.add(OpenResourceHelper.getType(item));
								IARESModule path = null;
								if (item instanceof OpenResourceInfo) {
									path = ((OpenResourceInfo) item).getResource().getModule();
									itemList.add(OpenResourceHelper.getChineseFileName("/" ,path));
								}else {
									itemList.add("");
								}
								table.add(itemList);
								for (EStructuralFeature fea : features) {
									Object value = ((OpenResourceInfo)item).getObj().eGet(fea);
									if (value != null) {
										itemList.add(value.toString());
									}else {
										itemList.add("");
									}
								}
								//������չ��Ϣ
								EObject object = ((OpenResourceInfo) item).getObj();
								if (object instanceof JRESResourceInfo) {
									if (((JRESResourceInfo) object).getData2().values().size() > 0) {
										for (EObject exPro : ((JRESResourceInfo) object).getData2().values()) {
											if (exPro instanceof UserExtensibleProperty) {
												for (String key : exTitles.values()) {
													itemList.add(StringUtils.defaultString(((UserExtensibleProperty) exPro).getMap().get(key)));
												}
											}
										}
									}
								}
								if (itemList.size() < subTils.size()) {
									for (int i = 0; i < subTils.size() - itemList.size(); i++) {
										itemList.add(StringUtils.EMPTY);
									}
								}
							}
						}
					}

					excelStream = new FileOutputStream(path);
					
					
					export(table, excelStream, 1, 1);

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
			IProgressService progress = ARESEditorPlugin.getDefault().getWorkbench()
					.getProgressService();
			progress.run(true, false, operation);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			String message = "�����ļ��ѱ��򿪻���д��Ȩ�ޣ�������������رպ��ٵ�����";
			Throwable exception = e.getTargetException();
			msgdialog = new MessageDialog(Display.getDefault().getActiveShell(), "����ʧ��", null,
					message, MessageDialog.WARNING, new String[] { "ȷ��" }, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (null != msgdialog)
			msgdialog.open();
		else {
			msgdialog = new MessageDialog(null, "�����ɹ�", null, String.format(
					"����·��Ϊ%s,�Ƿ���ļ���", path), MessageDialog.INFORMATION,
					new String[] { "ȷ��", "ȡ��" }, 0);
			if (Window.OK == msgdialog.open()) {
				openFile(path);
			}
		}
	}
	
	// �����д��ļ�
	private void openFile(final String path) {
		Runtime rn = Runtime.getRuntime();
		String cmd = "cmd.exe /c start \"\" \"" + path + "\"";
		try {
			rn.exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void export(List<List<String>> contents , OutputStream excelStream , int startRow , int startCell) throws IOException{
		labelStyle = null;
		textStyle = null;
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("��Դ�б�");
		setDefaultCellStyle(wb, sheet);
		for (int i = 0; i < contents.size(); i++) {
			HSSFRow row = sheet.createRow(startRow+i);
			List<String> item = contents.get(i);
			for (int j = 0; j < item.size(); j++) {
				String cellVlaue = item.get(j);
				HSSFCell cell = row.createCell(startCell+j);
				cell.setCellValue(cellVlaue);
				if (i == 0) {
					cell.setCellStyle(getLabelStyle(wb));
				}else {
					cell.setCellStyle(getTextStyle(wb));
				}
			}
		}
		sheet.setColumnWidth(0, 500);
		sheet.setColumnWidth(1, 2000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 8000);
		wb.write(excelStream);
	}
	
	private HSSFCellStyle labelStyle;
	private HSSFCellStyle textStyle;
	
	private  void setDefaultCellStyle(HSSFWorkbook wb, HSSFSheet sheet) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		for (int i = 0; i < 20; i++) {
			sheet.setDefaultColumnStyle((short) i, style);
		}
	}
	
	/**
	 * �ı������ʽ
	 * 
	 * @param wb
	 * @return
	 */
	private HSSFCellStyle getTextStyle(HSSFWorkbook wb) {
		if (textStyle == null) {
			textStyle = wb.createCellStyle();
			textStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			textStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			textStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		}
		return textStyle;
	}
	
	/**
	 * �ı���ǰlabel����ʽ
	 * 
	 * @param wb
	 * @return
	 */
	private HSSFCellStyle getLabelStyle(HSSFWorkbook wb) {
		if (labelStyle == null) {
			labelStyle = wb.createCellStyle();
			labelStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			labelStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			labelStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			labelStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			labelStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			labelStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			labelStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		}
		return labelStyle;
	}
	
}
