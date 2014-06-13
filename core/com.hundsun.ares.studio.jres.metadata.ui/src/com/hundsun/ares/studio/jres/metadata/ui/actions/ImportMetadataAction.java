/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.metadata.ui.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.metadata.ui.dialog.ImportDialog;
import com.hundsun.ares.studio.jres.metadata.ui.utils.DictoniaryUtils;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.ImportMetaDataHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.MenuAndFunctionExporter;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryList;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.MenuList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;
import com.hundsun.ares.studio.jres.model.metadata.impl.ErrorNoListImpl;
import com.hundsun.ares.studio.ui.editor.actions.ColumnViewerAction;

/**
 * @author qinyuan
 *
 */
public class ImportMetadataAction extends ColumnViewerAction{

	IARESResource resource;
	
	private String sheetName;
	
	private String path = "";
	
	private int importType;
	
	private static final String ERROR_SHEET_NAME = "ϵͳ�������";
	private static final String BIZ_TYPE = "��������";
	
	private static final String USER_CONST_SHEET_NAME = "�궨��";
	private static final String CATE = "���";
	private String dialogTitle = "";
	private Image dialogImage = null;

	
	/**
	 * @param viewer
	 * @param editingDomain
	 */
	public ImportMetadataAction(IARESResource resource,TreeViewer viewer,
			EditingDomain editingDomain) {
		super(viewer, editingDomain);
		
		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/import_wiz.gif"));
		setId(IMetadataActionIDConstant.CV_IMPORT_METADATA);
		this.resource =resource;
		setText("����");
		
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.actions.ColumnViewerAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		MetadataResourceData<MetadataItem> owner = (MetadataResourceData<MetadataItem>) getViewer().getInput();
		if(null == owner)
			return false;
		else 
			return !((TransactionalEditingDomain)getEditingDomain()).isReadOnly(owner.eResource());
	}
	
	@Override
	public void run() {
		final List<String> types = new ArrayList<String>();
		 String dialogTitle = "";
		 String dialogMessage = "";
		 Image dialogImage = null;
		if(resource.getType().equals(IMetadataResType.DefValue)){
			types.add("Ĭ��ֵ");
			dialogTitle	= "����Ĭ��ֵ";
			dialogMessage = "���Ѵ���Ĭ��ֵ��Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/defaultValueFile.png").createImage();
		}else
		if(resource.getType().equals(IMetadataResType.StdType)){
			types.add("��׼��������");
			dialogTitle	= "�����׼��������";
			dialogMessage = "���Ѵ��ڱ�׼�������͵�Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/stdTypeFile.png").createImage();
		}else
		if(resource.getType().equals(IMetadataResType.BizType)){
			types.add("ҵ����������");
			types.add(BIZ_TYPE);
			dialogTitle	= "����ҵ����������";
			dialogMessage = "���Ѵ���ҵ���������͵�Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/bizTypeFile.png").createImage();
		}else
		if(resource.getType().equals(IMetadataResType.StdField)){
			types.add("��׼�ֶ�");
			types.add("��׼�ֶ�Ŀ¼");
			types.add("��׼�ֶζ���");
			dialogTitle	= "�����׼�ֶ�";
			dialogMessage = "���Ѵ��ڱ�׼�ֶε�Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/stdFieldFile.png").createImage();
		}else
		if(resource.getType().equals(IMetadataResType.ErrNo)){
			types.add("�����");
			types.add(ERROR_SHEET_NAME);
			dialogTitle	= "�����׼�����";
			dialogMessage = "���Ѵ��ڱ�׼����ŵ�Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/errornoFile.png").createImage();
		}else
		if(resource.getType().equals(IMetadataResType.Const)){
			types.add("�û�����");
			types.add(USER_CONST_SHEET_NAME);
			dialogTitle	= "�����û�����";
			dialogMessage = "���Ѵ����û�������Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/cnstFile.png").createImage();
		}else
		if(resource.getType().equals(IMetadataResType.Dict)){
			types.add("�����ֵ����");
			dialogTitle	= "���������ֵ�";
			dialogMessage = "���Ѵ��������ֵ��Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/dictFile.png").createImage();
		} else if (resource.getType().equals(IMetadataResType.Menu)) {
			types.add("�˵�Ŀ¼");
			dialogTitle	= "����˵��빦��";
			dialogMessage = "���Ѵ��ڲ˵��빦�ܵ�Excel�ļ����뵽��Ŀ��.";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/menu.gif").createImage();
		}else if(resource.getType().equals(IMetadataResType.BizPropertyConfig)){
			types.add("ҵ�������");
			dialogTitle	= "����ҵ�������";
			dialogImage = AbstractUIPlugin.imageDescriptorFromPlugin(MetadataUI.PLUGIN_ID, "icons/full/obj16/bizconfig.png").createImage();
			dialogMessage = "���Ѵ���ҵ������õ�Excel�ļ����뵽��Ŀ��.";
		}
		else {
			types.add(getResorceType(resource));
		}
		ImportDialog dialog = new ImportDialog(((TreeViewer)getViewer()).getTree().getShell(), true,dialogTitle,dialogImage,dialogMessage){
			@Override
			public boolean validate(String fileText) {
				File file = new File(fileText);
				if (!file.exists()) {
					setErrorMessage("ָ���ļ�������!");
					importButton.setEnabled(false);
					return false;
				}
				
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
		
		Job job = new Job("����Ԫ������Դ") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("����Ԫ������Դ������", IProgressMonitor.UNKNOWN);
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
				List<EObject> items = null;
				MetadataResourceData metadata = null;
				List<MetadataCategory> mcs = new ArrayList<MetadataCategory>();
				Map< String, List< List<String> > > contents = null;
				ImportMetaDataHelper helper = ImportMetaDataHelper.getInstance();
				{
					try {
						excelStream = new FileInputStream(path);
						MetadataResourceData<MetadataItem> owner = (MetadataResourceData<MetadataItem>) getViewer().getInput();
						if(null == owner)
							throw new OperationCanceledException();
						
						//ɾ��ԭ����
						if (importType == ImportDialog.IMPORT_TYPE_COVER) {
							owner.getItems().clear();
							owner.getRoot().getChildren().clear();
							owner.getHistories().clear();
						}
						HSSFWorkbook workBook = new HSSFWorkbook(excelStream);
						List<List<String>>  revHisContents = POIUtils.getAresContents(workBook, "�汾ҳ", 11, 1);
						List<EObject> revHisInfos = helper.getRevHisesInfos(resource, revHisContents);
						//�����Ϊ����Ԫ����
						boolean isImportOtherInfo = false;
						
						if(resource.getType().equals(IMetadataResType.DefValue)){
							contents = POIUtils.getExcelString(workBook, 
									new String[]{"Ĭ��ֵ"}, new int[]{1}, new int[]{1});
							table = contents.get("Ĭ��ֵ");
							items = helper.getDefValueItems(resource, table);
						}else if(resource.getType().equals(IMetadataResType.StdType)){
							contents = POIUtils.getExcelString(workBook, 
									new String[]{"��׼��������"}, new int[]{1}, new int[]{1});
							table = contents.get("��׼��������");
							items = helper.getStdTypeItems(resource, table);
						}else if(resource.getType().equals(IMetadataResType.BizType)){
							
							if (StringUtils.equals(sheetName, BIZ_TYPE)) {
								sheetName = BIZ_TYPE;
							}else {
								sheetName = "ҵ����������";
							}
							contents = POIUtils.getExcelString(workBook, 
									new String[]{sheetName}, new int[]{1}, new int[]{1});
							table = contents.get(sheetName);
							items = helper.getBizTypeItems(resource, table);
						}else if(resource.getType().equals(IMetadataResType.StdField)){
							contents = POIUtils.getExcelStringForCate(workBook, 
									new String[]{sheetName}, new int[]{1}, new int[]{1});
							table = contents.get(sheetName);
							List<String> title = table.get(0);
							table.remove(0);
							Map<String, List<List<String>>> cateLineMap = clearMetadataItems(table);
							List<List<String>> total = new ArrayList<List<String>>();
							for(List<List<String>> lls : cateLineMap.values()){
								total.addAll(lls);
							}
							total.add(0, title);
							items = helper.getStdFldItems(resource, total);
							getMetadataItemCategory1(owner , items , cateLineMap);
						}else if(resource.getType().equals(IMetadataResType.ErrNo)){
							contents = POIUtils.getExcelStringForCate(workBook, 
									new String[]{sheetName}, new int[]{1}, new int[]{1});
							table = contents.get(sheetName);
							if (table.size() > 0 && table.get(0).size() > 0) {
								String title = table.get(0).get(0);
								if (StringUtils.equals(title, CATE)) {
									items = helper.getErrorNoItems(resource, table);
									getMetadataItemCategoryForSHClear(owner , items , table);
								}else {
									helper.getErrorNo(owner, resource, table);
									if (getViewer().getInput() instanceof ErrorNoListImpl) {
										ErrorNoListImpl error = (ErrorNoListImpl) getViewer().getInput();
										error.getHistories().addAll((Collection<? extends RevisionHistory>) revHisInfos);
									}
								}
							}
						}else if(resource.getType().equals(IMetadataResType.Const)){
							//���������������ߵ��û���������
							if (StringUtils.equals(sheetName, USER_CONST_SHEET_NAME)) {
								contents = POIUtils.getExcelStringForUtilCate(workBook, 
										new String[]{USER_CONST_SHEET_NAME}, new int[]{1}, new int[]{1});
								
								table = contents.get(USER_CONST_SHEET_NAME);
								//�滻���⣬�����Ե���JRES�û�����
								if (table.size() > 0) {
									List<String> titles = table.get(0);
									if (titles.indexOf("�궨��") > -1) {
										titles.set(titles.indexOf("�궨��"), "����");
									}
									if (titles.indexOf("���Ӧֵ") > -1) {
										titles.set(titles.indexOf("���Ӧֵ"), "����ֵ");
									}
									if (titles.indexOf("����ϸ˵��") > -1) {
										titles.set(titles.indexOf("����ϸ˵��"), "˵��");
									}
									
									List<String> title = table.get(0);
									table.remove(0);
									Map<String, List<List<String>>> cateLineMap = clearMetadataItemsForUtil(table);
									List<List<String>> total = new ArrayList<List<String>>();
									for(List<List<String>> lls : cateLineMap.values()){
										total.addAll(lls);
									}
									total.add(0, title);
									items = helper.getConsItem(resource, total);
									getMetadataItemCategory1(owner , items , cateLineMap);
								}
							}else {
								contents = POIUtils.getExcelStringForCate(workBook, 
										new String[]{sheetName}, new int[]{1}, new int[]{1});
								table = contents.get(sheetName);
								List<String> title = table.get(0);
								table.remove(0);
								Map<String, List<List<String>>> cateLineMap = clearMetadataItems(table);
								List<List<String>> total = new ArrayList<List<String>>();
								for(List<List<String>> lls : cateLineMap.values()){
									total.addAll(lls);
								}
								total.add(0, title);
								items = helper.getConsItem(resource, total);
								getMetadataItemCategory1(owner , items , cateLineMap);
							}
						}else if(resource.getType().equals(IMetadataResType.Dict)){
							if (getViewer().getInput() instanceof DictionaryList) {
								DictionaryList dict = (DictionaryList) getViewer().getInput();
								DictoniaryUtils.importDict(resource, dict, new File(path), new String[]{"�����ֵ����","ϵͳ�����ֵ����"}, new int[]{1,1}, new int[]{1,1});
								dict.getHistories().addAll((Collection<? extends RevisionHistory>) revHisInfos);
							}
						} else if (resource.getType().equals(IMetadataResType.Menu)) {
							metadata = MenuAndFunctionExporter.importMenusAndFunctions(workBook, resource);
							items = new ArrayList<EObject>();
							items.addAll(metadata.getItems());
						}else if(resource.getType().equals(IMetadataResType.BizPropertyConfig)){
							//ҵ�������
							contents = POIUtils.getExcelString(workBook, 
									new String[]{"ҵ�������"}, new int[]{1}, new int[]{1});
							table = contents.get("ҵ�������");
							items = helper.getBizPropertyConfigs(resource, table);
						}else {
							//��������Ԫ������Ϣ�����絼�����������Ϣ
							isImportOtherInfo = true;
							importOtherMetadataInfo(excelStream,contents,table,items,owner,resource);
						}
						
						
						// ��ӵ���Դ
						if(null != items && !isImportOtherInfo) {
							
							if (metadata instanceof MenuList && importType == 0) {
								MenuList menuList = (MenuList) getViewer().getInput();
								menuList.getFunctions().clear();
							}
							
							//��ӷ�����Ϣ
							if (metadata != null) {
								owner.getRoot().getChildren().addAll(metadata.getRoot().getChildren());
							}
							
							owner.getItems().addAll((Collection<? extends MetadataItem>) items);
							owner.getHistories().addAll((Collection<? extends RevisionHistory>) revHisInfos);
							if (metadata instanceof MenuList) {
								MenuList menuList = (MenuList) getViewer().getInput();
								menuList.getFunctions().addAll(((MenuList) metadata).getFunctions());
							}
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		return cc;
	}
	
	private Map<String , List<List<String>>> clearMetadataItems(List<List<String>> itemLines){
		Map<String , List<List<String>>> itemcCateStrs = new LinkedHashMap<String, List<List<String>>>();
		String cate = null;
		for(List<String> line : itemLines){
			
			boolean isCate = false;
			for (int i = 0; i < line.size(); i++) {
				if (i == 0) {
					if (StringUtils.isNotBlank(line.get(i))) {
						isCate = true;
					}
				}else {
					//ֻ�е�һ����ֵ���У�ͳһ��Ϊ����
					if (line.get(i) != null) {
						isCate = false;
					}
				}
			}
			
			if (isCate) {
				cate = line.get(0);
				continue;
			}
			if (itemcCateStrs.get(cate) == null) {
				itemcCateStrs.put(cate, new ArrayList<List<String>>());
			}
			itemcCateStrs.get(cate).add(line);
		}
		return itemcCateStrs;
	}
	
	private Map<String , List<List<String>>> clearMetadataItemsForUtil(List<List<String>> itemLines){
		Map<String , List<List<String>>> itemcCateStrs = new LinkedHashMap<String, List<List<String>>>();
		String cate = null;
		for(List<String> line : itemLines){
			if (StringUtils.isNotBlank(line.get(0)) && StringUtils.isBlank(line.get(1))) {
				cate = line.get(0);
				continue;
			}
			if (itemcCateStrs.get(cate) == null) {
				itemcCateStrs.put(cate, new ArrayList<List<String>>());
			}
			itemcCateStrs.get(cate).add(line);
		}
		return itemcCateStrs;
	}
	
	/**
	 * ��ȡ��Դ����
	 * @return
	 */
	protected String getResorceType(IARESResource resource) {
		return "";
	}

	/**
	 * ��������Ԫ������Ϣ
	 * ���ش˷���һ��Ҫ�����ط���#getResorceType
	 * @param excelStream 
	 * @param contents
	 * @param table
	 * @param items
	 * @param owner
	 * @param resource2
	 */
	protected void importOtherMetadataInfo(
			InputStream excelStream, Map<String, List<List<String>>> contents, List<List<String>> table,
			List<EObject> items, MetadataResourceData<MetadataItem> owner,
			IARESResource resource) throws Exception{
		
	}

	protected void getMetadataItemCategory1(MetadataResourceData ower ,
			List<EObject> items, Map<String, List<List<String>>> cateLineMap) {
		for(Iterator<String> it = cateLineMap.keySet().iterator();it.hasNext();){
			String key = it.next();
			List<List<String>> values = cateLineMap.get(key);
			String[] cates = StringUtils.split(key, "/");
			MetadataCategory mc = ower.getRoot();
			if (StringUtils.isBlank(key)) {
				for(List<String> value : values){
					String id = value.get(0);
					for(EObject obj : items){
						String stdId = ((MetadataItem)obj).getName();
						if (StringUtils.equals(id, stdId)) {
							ower.getItems().add((MetadataItem) obj);
							break;
						}
					}
				}
				continue;
			}
			for (String cate : cates) {
				mc = createCate(cate, mc);
			}
			for(List<String> value : values){
				String id = value.get(0);
				for(EObject obj : items){
					String stdId = ((MetadataItem)obj).getName();
					if (StringUtils.equals(id, stdId)) {
						mc.getItems().add((MetadataItem) obj);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * ���ӷ�����Ϣ
	 * 
	 * @param ower
	 * @param cates
	 * @param parient ������
	 * @return �ӷ���
	 */
	private MetadataCategory createCate(String cate ,MetadataCategory parient){
		MetadataCategory even = null;
		for (MetadataCategory mc : parient.getChildren()) {
			if (StringUtils.equals(mc.getName(), cate)) {
				even = mc;
				break;
			}
		}
		if (even == null) {
			even = MetadataFactory.eINSTANCE.createMetadataCategory();
			even.setName(cate);
			parient.getChildren().add(even);
		}
		return even;
	}
	
	protected void getMetadataItemCategory(MetadataResourceData ower ,
			List<EObject> items, List<List<String>> tables) {
		Map<String , MetadataItem> itemMap = new HashMap<String, MetadataItem>();
		for (EObject obj : items){
			MetadataItem item = (MetadataItem) obj;
			itemMap.put(item.getName(), item);
		}
		MetadataCategory mc = null;
		MetadataCategory submc = null;
		for (int i=1 ; i<tables.size();i++){
			List<String> table = tables.get(i);
			String cate = table.get(0);
			String subCate = table.get(1);
			if (StringUtils.isNotBlank(cate)) {
				mc = DictoniaryUtils.isDupMc(ower.getRoot(), cate);
				if (mc == null) {
					mc = MetadataFactory.eINSTANCE.createMetadataCategory();
					mc.setName(cate);
				}
				submc = null;
				ower.getRoot().getChildren().add(mc);
			}
			if (StringUtils.isNotBlank(subCate)) {
				submc = MetadataFactory.eINSTANCE.createMetadataCategory();
				submc.setName(subCate);
				mc.getChildren().add(submc);
			}
			if (submc != null) {
				submc.getItems().add(itemMap.get(table.get(2)));
			}else {
				if (mc != null) {
					mc.getItems().add(itemMap.get(table.get(2)));
				}
			}
		}
	}
	
	protected void getMetadataItemCategoryForSHClear(MetadataResourceData ower ,
			List<EObject> items, List<List<String>> tables) {
		Map<String , MetadataItem> itemMap = new HashMap<String, MetadataItem>();
		for (EObject obj : items){
			MetadataItem item = (MetadataItem) obj;
			itemMap.put(((ErrorNoItem)item).getNo(), item);
		}
		MetadataCategory mc = null;
		MetadataCategory submc = null;
		for (int i=1 ; i<tables.size();i++){
			List<String> table = tables.get(i);
			String cate = table.get(0);
			String subCate = table.get(1);
			if (StringUtils.isNotBlank(cate)) {
				mc = DictoniaryUtils.isDupMc(ower.getRoot(), cate);
				if (mc == null) {
					mc = MetadataFactory.eINSTANCE.createMetadataCategory();
					mc.setName(cate);
				}
				submc = null;
				ower.getRoot().getChildren().add(mc);
			}
			if (StringUtils.isNotBlank(subCate)) {
				submc = MetadataFactory.eINSTANCE.createMetadataCategory();
				submc.setName(subCate);
				mc.getChildren().add(submc);
			}
			if (submc != null) {
				submc.getItems().add(itemMap.get(table.get(2)));
			}else {
				if (mc != null) {
					mc.getItems().add(itemMap.get(table.get(2)));
				}
			}
		}
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
