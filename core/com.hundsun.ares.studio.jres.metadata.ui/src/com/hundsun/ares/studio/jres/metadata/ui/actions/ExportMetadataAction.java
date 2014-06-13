/**
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.metadata.ui.Language;
import com.hundsun.ares.studio.jres.metadata.ui.LanguageRegister;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.dialog.ExportDialog;
import com.hundsun.ares.studio.jres.metadata.ui.utils.DictoniaryUtils;
import com.hundsun.ares.studio.jres.metadata.ui.viewer.IDRangeContentProvider;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.MenuAndFunctionExporter;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IHeaderSorter;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryList;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.IDRange;
import com.hundsun.ares.studio.jres.model.metadata.IDRangeItem;
import com.hundsun.ares.studio.jres.model.metadata.IDRangeList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;
import com.hundsun.ares.studio.ui.editor.actions.IUpdateAction;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;

/**
 * @author yanwj06282
 * 
 */
public class ExportMetadataAction extends Action implements IUpdateAction {
	
	private static BiMap<String, String> moduleCH = HashBiMap.create();
	
	static {
		moduleCH.put("database", "���ݿ�");
		moduleCH.put("business", "ҵ���߼�");
		moduleCH.put("basicdata", "��������");
	}
	
	protected IARESResource resource;
	IWorkbenchPartSite site;
	protected Image dialogImage;
	protected  String dialogMessage = "";
	protected  String dialogTitle = "";

	/**
	 * 
	 */
	public ExportMetadataAction(IARESResource resource, IWorkbenchPartSite site,String dialogTitle,Image dialogImage,String dialogMessage) {
		this.resource = resource;
		this.site = site;

		setText("����");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				MetadataUI.PLUGIN_ID, "icons/full/obj16/export_wiz.gif"));
		setId(IMetadataActionIDConstant.CV_EXPORT_METADATA);

		setEnabled(true);
		
		this.dialogTitle = dialogTitle;
		this.dialogImage = dialogImage;
		this.dialogMessage = dialogMessage;
	}

	/**
	 * ��ȡ����Դģ��
	 * 
	 * @return
	 * @throws ARESModelException
	 */
	protected MetadataResourceData getResourceInfo() throws ARESModelException{
		MetadataResourceData list = resource.getInfo(MetadataResourceData.class);
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		
		ExportDialog dialog = new ExportDialog(site.getShell(),this.dialogTitle,this.dialogImage,this.dialogMessage);
		dialog.open();

		if (dialog.getReturnCode() != Window.OK)
			return;

		final String path = dialog.getFilePath();

		IRunnableWithProgress operation = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				monitor.beginTask("����Ԫ������Դ������", IProgressMonitor.UNKNOWN);

				OutputStream excelStream = null;
				List<List<String>> table = null;
				Map<String, List<List<String>>> contents = new LinkedHashMap<String, List<List<String>>>();
				try {
					Language[] languages = LanguageRegister.getInstance()
							.getRegisteredLanguages();
					String[] languageIds = new String[languages.length];
					String[] languageTitles = new String[languages.length];
					for (int i = 0; i < languageIds.length; i++) {
						languageIds[i] = languages[i].getId();
						languageTitles[i] = languages[i].getName();
					}
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
					MetadataResourceData list = getResourceInfo();
					if (list == null) {
						throw new RuntimeException("��Դ��������:["+resource.getElementName()+"]");
					}
					
					//�����޶���¼
					List<List<String>> revHises = POIUtils
					.exportExcelStringTable(
							list,
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
					
					if (resource.getType().equals(IMetadataResType.DefValue)) {
						table = POIUtils
								.exportExcelStringTable(
										list,
										MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
										MetadataPackage.Literals.TYPE_DEFAULT_VALUE,
										new String[] { "Ĭ��ֵ��", "����", "˵��" },
										new EStructuralFeature[] {
												MetadataPackage.Literals.NAMED_ELEMENT__NAME,
												MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
												MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
										true, languageTitles, languageIds,
										resource, descriptionLast);
						contents.put("Ĭ��ֵ", table);
						title = "Ĭ��ֵ�淶�ĵ�";
					} else if (resource.getType().equals(
							IMetadataResType.StdType)) {

						table = POIUtils
								.exportExcelStringTable(
										list,
										MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
										MetadataPackage.Literals.STANDARD_DATA_TYPE,
										new String[] { "������", "����", "˵��" },
										new EStructuralFeature[] {
												MetadataPackage.Literals.NAMED_ELEMENT__NAME,
												MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
												MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
										true, languageTitles, languageIds,
										resource, descriptionLast);
						contents.put("��׼��������", table);
						title = "��׼�������͹淶�ĵ�";
					} else if (resource.getType().equals(
							IMetadataResType.IDRange)) {
						
						table = getIDRangeList(resource ,(IDRangeList)list);
						contents.put("����ŷ�Χ", table);
						title = "����ŷ�Χ�淶�ĵ�";
					} else if (resource.getType().equals(
							IMetadataResType.BizType)) {

						table = POIUtils
								.exportExcelStringTable(
										list,
										MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
										MetadataPackage.Literals.BUSINESS_DATA_TYPE,
										new String[] { "������", "��������", "��׼����",
												"����", "����", "Ĭ��ֵ", "˵��" },
										new EStructuralFeature[] {
												MetadataPackage.Literals.NAMED_ELEMENT__NAME,
												MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
												MetadataPackage.Literals.BUSINESS_DATA_TYPE__STD_TYPE,
												MetadataPackage.Literals.BUSINESS_DATA_TYPE__LENGTH,
												MetadataPackage.Literals.BUSINESS_DATA_TYPE__PRECISION,
												MetadataPackage.Literals.BUSINESS_DATA_TYPE__DEFAULT_VALUE,
												MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
										true, ArrayUtils.EMPTY_STRING_ARRAY,
										ArrayUtils.EMPTY_STRING_ARRAY,
										resource, null);
						contents.put("ҵ����������", table);
						title = "ҵ���������͹淶�ĵ�";
					} else if (resource.getType().equals(
							IMetadataResType.StdField)) {

						table = POIUtils
								.exportExcelStringTable(
										list,
										MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
										MetadataPackage.Literals.STANDARD_FIELD,
										new String[] { "�ֶ���", "�ֶ�����", "�ֶ�����",
												"�ֵ���Ŀ", "˵��" },
										new EStructuralFeature[] {
												MetadataPackage.Literals.NAMED_ELEMENT__NAME,
												MetadataPackage.Literals.STANDARD_FIELD__DATA_TYPE,
												MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
												MetadataPackage.Literals.STANDARD_FIELD__DICTIONARY_TYPE,
												MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
										true, ArrayUtils.EMPTY_STRING_ARRAY,
										ArrayUtils.EMPTY_STRING_ARRAY,
										resource, null);
						// addCategories(table, list,new String[]{"�ֶ�" ,"���"});
						addCategories(table, list);
						contents.put("��׼�ֶ�", table);
						title = "��׼�ֶι淶�ĵ�";
					} else if (resource.getType().equals(IMetadataResType.Dict)) {
						{
							List<List<String>> main = POIUtils
									.exportExcelStringTable(
											list,
											MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
											MetadataPackage.Literals.DICTIONARY_TYPE,
											new String[] { "�ֵ���Ŀ", "��Ŀ����", "˵��" },
											new EStructuralFeature[] {
													MetadataPackage.Literals.NAMED_ELEMENT__NAME,
													MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
													MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
											true, new String[] {},
											new String[] {}, resource,
											descriptionLast);
							addCategoriesForDict(main, (DictionaryList)list);
							contents.put("�����ֵ����", main);
						}
						{
							List<List<String>> sub = new ArrayList<List<String>>();
							for (int i = 0; i < list.getItems().size(); i++) {
								DictionaryType dictType = ((DictionaryList)list).getItems()
										.get(i);
								dictType.eGet(MetadataPackage.Literals.DICTIONARY_ITEM__VALUE);
								List<List<String>> table1 = POIUtils
										.exportExcelStringTable(
												dictType,
												MetadataPackage.Literals.DICTIONARY_TYPE__ITEMS,
												MetadataPackage.Literals.DICTIONARY_ITEM,
												new String[] { "�ֵ���Ŀ", "�ֵ�����",
														"�ֵ�����", "��������", "�ֵ䳣��",
														"˵��" },
												new EStructuralFeature[] {
														MetadataPackage.Literals.NAMED_ELEMENT__NAME,
														MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
														MetadataPackage.Literals.DICTIONARY_ITEM__VALUE,
														MetadataPackage.Literals.DICTIONARY_ITEM__CHINESE_NAME,
														MetadataPackage.Literals.DICTIONARY_ITEM__CONSTANT_NAME,
														MetadataPackage.Literals.DICTIONARY_ITEM__DESCRIPTION },
												true, new String[] {},
												new String[] {}, resource,
												descriptionLast);
								for (int j = 1; j < table1.size(); j++) {
									List<String> titles = table1.get(j);
									titles.set(0, dictType.getName());
									titles.set(1, dictType.getChineseName());
								}
								if (table1.size() > 0 && i > 0) {
									table1.remove(0);
								}
								sub.addAll(table1);
							}

							contents.put("ϵͳ�����ֵ����", sub);
						}
						excelStream = new FileOutputStream(path);
						DictoniaryUtils.exportDict(excelStream, contents,revHises,
								new int[] { 2, 1 }, new int[] { 1, 1 });
						return;
					} else if (StringUtils.equals(resource.getType(),
							IMetadataResType.Menu)) {
						title = "�˵��빦�ܹ淶�ĵ�";
						MenuAndFunctionExporter.export(resource, contents);
					} else if (resource.getType()
							.equals(IMetadataResType.ErrNo)) {

						table = POIUtils
								.exportExcelStringTable(
										list,
										MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
										MetadataPackage.Literals.ERROR_NO_ITEM,
										new String[] { "�����", "������ʾ��Ϣ", "��������",
												"���󼶱�", "˵��" },
										new EStructuralFeature[] {
												MetadataPackage.Literals.ERROR_NO_ITEM__NO,
												MetadataPackage.Literals.ERROR_NO_ITEM__MESSAGE,
												MetadataPackage.Literals.NAMED_ELEMENT__NAME,
												MetadataPackage.Literals.ERROR_NO_ITEM__LEVEL,
												MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
										true, ArrayUtils.EMPTY_STRING_ARRAY,
										ArrayUtils.EMPTY_STRING_ARRAY,
										resource, null);
						addCategories(table, list);
						contents.put("ϵͳ�������", table);
						title = "����Ź淶�ĵ�";
					} else if (resource.getType()
							.equals(IMetadataResType.Const)) {

						table = POIUtils
								.exportExcelStringTable(
										list,
										MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
										MetadataPackage.Literals.CONSTANT_ITEM,
										new String[] { "����", "����ֵ", "˵��" },
										new EStructuralFeature[] {
												MetadataPackage.Literals.NAMED_ELEMENT__NAME,
												MetadataPackage.Literals.CONSTANT_ITEM__VALUE,
												MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
										true, ArrayUtils.EMPTY_STRING_ARRAY,
										ArrayUtils.EMPTY_STRING_ARRAY,
										resource, null);
						addCategories(table, list);
						contents.put("�û�����", table);
						title = "�û������淶�ĵ�";
					}else if(resource.getType().equals(IMetadataResType.BizPropertyConfig)){
						//ҵ�������
						table = POIUtils.exportExcelStringTable(
										list,
										MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
										MetadataPackage.Literals.BIZ_PROPERTY_CONFIG,
										new String[] { "���", "����", "������","˵��" },
										new EStructuralFeature[] {
												MetadataPackage.Literals.NAMED_ELEMENT__NAME,
												MetadataPackage.Literals.BIZ_PROPERTY_CONFIG__ENAME,
												MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
												MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION },
										true, ArrayUtils.EMPTY_STRING_ARRAY,
										ArrayUtils.EMPTY_STRING_ARRAY,
										resource, null);
						contents.put("ҵ�������", table);
						title = "ҵ������ù淶�ĵ�";
					} 
					else {
						// ������չԪ���ݵ�����,�����������
						exportOtherMetaDataInfoToExcel(contents, table,
								resource);
						title = getDocTitle();
					}

					// ����contents�е����ݾ�����Ҫ�ж��ٸ�sheetҳ
					int[] starts = new int[contents.keySet().size()];
					for (int i = 0; i < starts.length; i++) {
						starts[i] = 1;// sheetҳ����ʼλ��
					}
					excelStream = new FileOutputStream(path);
					POIUtils.putExcelString(excelStream, title, contents,revHises,
							contents.keySet().toArray(new String[0]), starts,
							starts);

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
			if (exception instanceof ExportExcelException) {
				message = ((ExportExcelException) exception).getMessage();
			}
			msgdialog = new MessageDialog(site.getShell(), "����ʧ��", null,
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

	private List<List<String>> getIDRangeList(IARESResource resource , IDRangeList idList){
		List<List<String>> contents = new ArrayList<List<String>>();
		Map<String,IDRangeItem> itemStayMap = new LinkedHashMap<String, IDRangeItem>();
		if (resource != null && idList != null) {
			IARESProject project = resource.getARESProject();
			for (IDRangeItem item : idList.getItems()) {
				String key = tranEN2CH(project, item.getName());
				if (StringUtils.isBlank(key)) {
					continue;
				}
				itemStayMap.put(key, item);
			}
			IDRangeContentProvider cp = new IDRangeContentProvider(resource,MetadataPackage.Literals.ID_RANGE_ITEM){
				@Override
				protected Object[] getModuleRootChildren(
						IARESModuleRoot root) throws ARESModelException {
					return root.getModules();
				}
			};
			IARESModule[] objs = (IARESModule[]) cp.getProjectChildren(resource.getARESProject());
			contents = fillContents(objs, itemStayMap, MetadataPackage.Literals.ID_RANGE_ITEM);
		}
		return contents;
	}
	
	private String tranEN2CH(IARESProject project ,String url){
		String[] strs = StringUtils.split(url, "/");
		if (strs.length <= 1) {
			return StringUtils.EMPTY;
		}
		StringBuffer sb = new StringBuffer();
		try {
			IARESModuleRoot root = project.getModuleRoot(strs[0]);
			if (root == null) {
				return sb.toString();
			}
			sb.append(root.getElementName());
			String moduleDir = "";
			for (int i = 1; i < strs.length; i++) {
				if (StringUtils.isBlank(moduleDir)) {
					moduleDir += strs[i];
				}else {
					moduleDir +="."+ strs[i];
				}
				IARESModule module = root.getModule(moduleDir);
				sb.append("/");
				if (module.exists()) {
					IARESResource resource = module.getARESResource("module.xml");
					ModuleProperty property = resource.getInfo(ModuleProperty.class);
					Object obj = property.getValue(ICommonModel.CNAME);
					if (StringUtils.isNotBlank(obj.toString())) {
						sb.append(obj.toString());
					}
				}else {
					return StringUtils.EMPTY;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private List<List<String>> fillContents(IARESModule[] allObjs ,Map<String,IDRangeItem> itemStayMap ,EClass eClass){
		List<List<String>> contents = new ArrayList<List<String>>();
		IExtensibleModelEditingSupport[] editingSupports = ExtensibleModelUtils.getEndabledEditingSupports(resource, eClass);
		List<String> exTitleList = new ArrayList<String>();
		List<String> titles = new ArrayList<String>();
		Map<String,String> title = new LinkedHashMap<String, String>();
		for (IExtensibleModelEditingSupport support : editingSupports) {
			for (IExtensibleModelPropertyDescriptor descriptor : support
					.getPropertyDescriptors(resource, eClass)) {
				title.put(descriptor.getDisplayName(), support.getKey());
				exTitleList.add(descriptor.getDisplayName());
			}
		}
		titles.add("ģ��·��");
		titles.addAll(exTitleList);
		contents.add(titles);
		for (Iterator<String> iterator = itemStayMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			IDRangeItem value = itemStayMap.get(key);
			List<String> cont = new ArrayList<String>();
			cont.add(StringUtils.replace(getModuleCH(key), "\\", "/"));
			for (String ex : exTitleList) {
				String exKey = title.get(ex);
				Object obj = value.getData2().get(exKey);
				if (obj instanceof IDRange) {
					cont.add(((IDRange) obj).getRange());
				}else {
					cont.add("0-0");
				}
			}
			contents.add(cont);
		}
		for (IARESModule module : allObjs) {
			List<String> cont = new ArrayList<String>();
			String mn = getChineseFileName("/", module);
			if (StringUtils.isNotBlank(mn)) {
				String key = module.getRoot().getElementName() + "/" + mn;
				if (itemStayMap.get(key) == null) {
					cont.add(getModuleCH(key));
					for (int i = 0; i < exTitleList.size(); i++) {
						cont.add("0-0");
					}
					contents.add(cont);
				}
			}
		}
		return contents;
	}
	
	public static String getChineseFileName (String separator ,IARESModule module){
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
	
	public static void getModuleChineseName (IARESModule module , StringBuffer sb , String separator){
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
							if (StringUtils.isNotBlank(obj.toString())) {
								sb.append(obj.toString());
							}
						}
					}
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
			getModuleChineseName(module.getParentModule(), sb ,separator);
		}
	}
	
	private String getModuleCH(String key){
		if (StringUtils.startsWith(key,"database/")) {
			return StringUtils.replaceOnce(key, "database/", "���ݿ�/");
		}else if (StringUtils.startsWith(key ,"database\\")) {
			return StringUtils.replaceOnce(key, "database\\", "���ݿ�\\");
		}else if (StringUtils.startsWith(key ,"business/")) {
			return StringUtils.replaceOnce(key, "business/", "ҵ���߼�/");
		}else if (StringUtils.startsWith(key ,"business\\")) {
			return StringUtils.replaceOnce(key, "business\\", "ҵ���߼�\\");
		}else if (StringUtils.startsWith(key ,"objects/")) {
			return StringUtils.replaceOnce(key, "objects/", "����/");
		}else if (StringUtils.startsWith(key ,"objects\\")) {
			return StringUtils.replaceOnce(key, "objects\\", "����\\");
		}else if (StringUtils.startsWith(key ,"atom/")) {
			return StringUtils.replaceOnce(key, "atom/", "ԭ��/");
		}else if (StringUtils.startsWith(key ,"atom\\")) {
			return StringUtils.replaceOnce(key, "atom\\", "ԭ��\\");
		}else if (StringUtils.startsWith(key ,"logic/")) {
			return StringUtils.replaceOnce(key, "logic/", "�߼�/");
		}else if (StringUtils.startsWith(key ,"logic\\")) {
			return StringUtils.replaceOnce(key, "logic\\", "�߼�\\");
		}else if (StringUtils.startsWith(key ,"procedure/")) {
			return StringUtils.replaceOnce(key, "procedure/", "����/");
		}else if (StringUtils.startsWith(key ,"procedure\\")) {
			return StringUtils.replaceOnce(key, "procedure\\", "����\\");
		}else if (StringUtils.startsWith(key ,"service/")) {
			return StringUtils.replaceOnce(key, "service/", "ҵ���߼�/");
		}else if (StringUtils.startsWith(key ,"service\\")) {
			return StringUtils.replaceOnce(key, "service\\", "ҵ���߼�\\");
		}
		return key;
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

	/**
	 * @return
	 */
	protected String getDocTitle() {
		return null;
	}

	/**
	 * @param excelStream
	 * @param contents
	 * @param table
	 * @param resource2
	 * @throws Exception
	 */
	protected void exportOtherMetaDataInfoToExcel(
			Map<String, List<List<String>>> contents, List<List<String>> table,
			IARESResource resource) throws ARESModelException, Exception {

	}

	public void addCategories(List<List<String>> table,
			MetadataResourceData metadata) {
		int colLength = 0;
		if (table.size() > 0) {
			colLength = table.get(0).size();
		} else {
			return;
		}
		List<List<String>> tableCopy = new ArrayList<List<String>>();
		tableCopy.add(table.get(0));
		// ������δ�������Ŀ
		for (Object obj : metadata.getItems()) {
			MetadataItem item = (MetadataItem) obj;
			if (item.getCategories().size() == 0) {
				for (List<String> cols : table) {
					if (item instanceof ErrorNoItem) {
						if (StringUtils.equals(((ErrorNoItem) item).getNo(),
								cols.get(0))) {
							tableCopy.add(cols);
							table.remove(cols);
							break;
						}
					} else {
						if (StringUtils.equals(item.getName(), cols.get(0))) {
							tableCopy.add(cols);
							table.remove(cols);
							break;
						}
					}
				}
			}
		}
		// �����ӷ�����Ŀ
		for (MetadataCategory cate : metadata.getRoot().getChildren()) {
			// List<String> cats = new ArrayList<String>();
			addCateItems(cate, table, tableCopy, colLength);
		}
		table.clear();
		table.addAll(tableCopy);
	}

	private void addCateItemsForDict(MetadataCategory cate,
			List<List<String>> table, List<List<String>> tableCopy,
			int colLength, List<String> cats) {
		String space = "";
		for (int i = 0; i < cats.size(); i++) {
			space += cats.get(i) + "/";
		}
		if (cate.getItems().size() > 0
				|| (cate.getItems().size() == 0 && cate.getChildren().size() > 0)) {// ��һ��Ŀ¼Ҳ��Ҫ����
			List<String> title = new ArrayList<String>();
			title.add(space + cate.getName());
			for (int i = 0; i < colLength - 1; i++) {
				title.add(null);
			}
			tableCopy.add(title);
			if (!cats.contains(cate.getName())) {
				cats.add(cate.getName());
			}
		}
		for (MetadataItem item : cate.getItems()) {
			for (List<String> cols : table) {
				if (StringUtils.equals(item.getName(), cols.get(0))) {
					table.remove(cols);
					cols.add(0, "");
					cols.add(1, "");
					tableCopy.add(cols);
					break;
				}
			}
		}
		for (MetadataCategory subcate : cate.getChildren()) {
			List<String> cats_temp = new ArrayList<String>();
			cats_temp.addAll(cats);
			addCateItemsForDict(subcate, table, tableCopy, colLength, cats_temp);
		}
	}

	private void addCateItems(MetadataCategory cate, List<List<String>> table,
			List<List<String>> tableCopy, int colLength) {
		List<String> cateStr = new ArrayList<String>();
		getCateStr(cate, cateStr);
		String space = packageCate(cateStr);
		List<String> title = new ArrayList<String>();
		title.add(space);
		for (int i = 0; i < colLength - 1; i++) {
			title.add(null);
		}
		tableCopy.add(title);
		for (MetadataItem item : cate.getItems()) {
			for (List<String> cols : table) {
				if (item instanceof ErrorNoItem) {
					if (StringUtils.equals(((ErrorNoItem) item).getNo(),
							cols.get(0))) {
						tableCopy.add(cols);
						table.remove(cols);
						break;
					}
				} else {
					if (StringUtils.equals(item.getName(), cols.get(0))) {
						tableCopy.add(cols);
						table.remove(cols);
						break;
					}
				}
			}
		}
		for (MetadataCategory subcate : cate.getChildren()) {
			addCateItems(subcate, table, tableCopy, colLength);
		}
	}

	private String packageCate(List<String> cats) {
		StringBuffer sb = new StringBuffer();
		for (String cate : cats) {
			sb.append(cate + "/");
		}
		return StringUtils.substring(sb.toString(), 0,
				sb.toString().length() - 1);
	}

	private void getCateStr(MetadataCategory cate, List<String> sb) {
		sb.add(0, cate.getName());
		if (cate.eContainer() instanceof MetadataCategory
				&& cate.eContainer().eContainer() instanceof MetadataCategory) {
			getCateStr((MetadataCategory) cate.eContainer(), sb);
		}
	}

	public void addCategoriesForDict(List<List<String>> table,
			DictionaryList dict) {
		int colLength = 0;
		if (table.size() > 0) {
			table.get(0).add(0, "�ֵ����");
			table.get(0).add(1, "�ֵ�ֶ�");
			colLength = table.get(0).size();
		} else {
			return;
		}
		List<List<String>> tableCopy = new ArrayList<List<String>>();
		tableCopy.add(table.get(0));
		// ������δ�������Ŀ
		for (Object obj : dict.getItems()) {
			MetadataItem item = (MetadataItem) obj;
			if (item.getCategories().size() == 0) {
				for (List<String> cols : table) {
					if (StringUtils.equals(item.getName(), cols.get(0))) {
						table.remove(cols);
						cols.add(0, "");
						cols.add(1, "");
						tableCopy.add(cols);
						break;
					}
				}
			}
		}
		// �����ӷ�����Ŀ
		for (MetadataCategory cate : dict.getRoot().getChildren()) {
			List<String> cats = new ArrayList<String>();
			addCateItemsForDict(cate, table, tableCopy, colLength, cats);
		}
		table.clear();
		table.addAll(tableCopy);
	}

	protected void addCategories(List<List<String>> table,
			MetadataResourceData metadata, String[] strs) {
		Map<String, Cate> cateMap = new HashMap<String, Cate>();
		for (MetadataCategory dictCate : metadata.getRoot().getChildren()) {
			getSubCate(dictCate, cateMap);
		}
		String cateStr = null;
		String subCateStr = null;
		// ����δ����ķ�����ͷ
		List<List<String>> table1 = new ArrayList<List<String>>(table.size());
		// ��֤�����в���
		List<String> tt = table.get(0);
		Collections.reverse(table);// ����
		table.remove(table.size() - 1);// ɾ��������
		Collections.reverse(table);// 2012��10��25��14:32:27 ��Ԫ ˳����������
		table.add(0, tt);
		table1.addAll(table);
		for (MetadataItem item : MetadataUtil.getUncategorizedItems(metadata)) {

			for (int i = 0; i < table1.size(); i++) {
				List<String> datas = table1.get(i);
				if (StringUtils.equals(item.getName(), datas.get(0))) {
					table.remove(datas);
					table.add(1, datas);
				}
			}
		}

		Map<Cate, List<List<String>>> resultMap = new LinkedHashMap<Cate, List<List<String>>>();
		// ���÷�����Ϣ
		List<String> start = null;
		for (int i = 0; i < table.size(); i++) {
			List<String> datas = table.get(i);
			if (i == 0) {
				datas.add(0, strs[0]);
				datas.add(0, strs[1]);
				start = datas;
				continue;
			}
			Cate cate = cateMap.get(datas.get(0));
			if (cate != null) {
				if (StringUtils.equals(cateStr, cate.getCate())
						|| resultMap.get(cate) != null) {
					datas.add(0, StringUtils.EMPTY);
				} else {
					datas.add(0, cate.getCate());
				}
				if (StringUtils.equals(subCateStr, cate.getSubCate())
						|| resultMap.get(cate) != null) {
					datas.add(1, StringUtils.EMPTY);
				} else {
					datas.set(0, cate.getCate());
					datas.add(1, cate.getSubCate());
				}
				cateStr = cate.getCate();
				subCateStr = cate.getSubCate();
			} else {
				datas.add(0, StringUtils.EMPTY);
				datas.add(1, StringUtils.EMPTY);
			}
			if (resultMap.get(cate) == null) {
				List<List<String>> ssm = new ArrayList<List<String>>();
				ssm.add(datas);
				resultMap.put(cate, ssm);
			} else {
				resultMap.get(cate).add(datas);
			}

		}
		table.clear();
		table.add(start);
		for (List<List<String>> data : resultMap.values()) {
			table.addAll(data);
		}
	}

	protected void addCategoriesForSHClear(List<List<String>> table,
			MetadataResourceData metadata, String[] strs) {
		Map<String, Cate> cateMap = new HashMap<String, Cate>();
		for (MetadataCategory dictCate : metadata.getRoot().getChildren()) {
			getSubCateErrorForSHClear(dictCate, cateMap);
		}
		String cateStr = null;
		String subCateStr = null;
		// ����δ����ķ�����ͷ
		List<List<String>> table1 = new ArrayList<List<String>>(table.size());
		// ��֤�����в���
		List<String> tt = table.get(0);
		Collections.reverse(table);// ����
		table.remove(table.size() - 1);// ɾ��������
		Collections.reverse(table);// 2012��10��25��14:32:27 ��Ԫ ˳����������
		table.add(0, tt);
		table1.addAll(table);
		for (MetadataItem item : MetadataUtil.getUncategorizedItems(metadata)) {

			for (int i = 0; i < table1.size(); i++) {
				List<String> datas = table1.get(i);
				if (StringUtils.equals(item.getName(), datas.get(0))) {
					table.remove(datas);
					table.add(1, datas);
				}
			}
		}

		Map<Cate, List<List<String>>> resultMap = new LinkedHashMap<Cate, List<List<String>>>();
		// ���÷�����Ϣ
		List<String> start = null;
		for (int i = 0; i < table.size(); i++) {
			List<String> datas = table.get(i);
			if (i == 0) {
				datas.add(0, strs[0]);
				datas.add(0, strs[1]);
				start = datas;
				continue;
			}
			Cate cate = cateMap.get(datas.get(0));
			if (cate != null) {
				if (StringUtils.equals(cateStr, cate.getCate())
						|| resultMap.get(cate) != null) {
					datas.add(0, StringUtils.EMPTY);
				} else {
					datas.add(0, cate.getCate());
				}
				if (StringUtils.equals(subCateStr, cate.getSubCate())
						|| resultMap.get(cate) != null) {
					datas.add(1, StringUtils.EMPTY);
				} else {
					datas.set(0, cate.getCate());
					datas.add(1, cate.getSubCate());
				}
				cateStr = cate.getCate();
				subCateStr = cate.getSubCate();
			} else {
				datas.add(0, StringUtils.EMPTY);
				datas.add(1, StringUtils.EMPTY);
			}
			if (resultMap.get(cate) == null) {
				List<List<String>> ssm = new ArrayList<List<String>>();
				ssm.add(datas);
				resultMap.put(cate, ssm);
			} else {
				resultMap.get(cate).add(datas);
			}

		}
		table.clear();
		table.add(start);
		for (List<List<String>> data : resultMap.values()) {
			table.addAll(data);
		}
	}

	private void getSubCate(MetadataCategory dictCate, Map<String, Cate> cateMap) {
		for (MetadataItem item : dictCate.getItems()) {
			cateMap.put(item.getName(), new Cate(dictCate.getName(),
					getCateName(dictCate)));
		}
		if (dictCate.getChildren().size() > 0) {
			for (MetadataCategory mc : dictCate.getChildren()) {
				getSubCate(mc, cateMap);
			}
		}
	}

	private void getSubCateErrorForSHClear(MetadataCategory dictCate,
			Map<String, Cate> cateMap) {
		for (MetadataItem item : dictCate.getItems()) {
			cateMap.put(((ErrorNoItem) item).getNo(),
					new Cate(dictCate.getName(), getCateName(dictCate)));
		}
		if (dictCate.getChildren().size() > 0) {
			for (MetadataCategory mc : dictCate.getChildren()) {
				getSubCate(mc, cateMap);
			}
		}
	}

	private String getCateName(MetadataCategory dictCate) {
		if (dictCate.eContainer().eContainer() instanceof MetadataResourceData) {
			return dictCate.getName();
		} else if (dictCate.eContainer() instanceof MetadataCategory) {
			return getCateName((MetadataCategory) dictCate.eContainer());
		} else {
			return StringUtils.EMPTY;
		}
	}

	class Cate {
		private String subCate = StringUtils.EMPTY;
		private String cate = StringUtils.EMPTY;

		public Cate(String subCate, String cate) {
			this.subCate = subCate;
			this.cate = cate;
		}

		public String getSubCate() {
			if (StringUtils.equals(cate, subCate)) {
				return StringUtils.EMPTY;
			}
			return subCate;
		}

		public String getCate() {
			return cate;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Cate) {
				if (StringUtils.equals(((Cate) obj).getCate(), this.getCate())
						&& StringUtils.equals(((Cate) obj).getSubCate(),
								this.getSubCate())) {
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			return cate.hashCode() + subCate.hashCode();
		}

	}

	public IARESResource getResource() {
		return resource;
	}

	public IWorkbenchPartSite getSite() {
		return site;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hundsun.ares.studio.jres.ui.actions.IUpdateAction#update()
	 */
	@Override
	public void update() {

	}

	/***
	 * �Զ����쳣������󵼳�ʧ�ܵ���������ʾ�Զ�����Ϣ��
	 */
	protected class ExportExcelException extends Exception {

		private static final long serialVersionUID = 988537005828794522L;

		public ExportExcelException(String message) {
			super(message);
		}

	}
}
