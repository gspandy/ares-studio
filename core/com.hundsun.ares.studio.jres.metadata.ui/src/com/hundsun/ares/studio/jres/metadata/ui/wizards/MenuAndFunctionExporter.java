package com.hundsun.ares.studio.jres.metadata.ui.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.ExtensibleData2AttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.ExtensibleData2MapAttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.ExtensibleDataAttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IAttributeHelper;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.IHeaderSorter;
import com.hundsun.ares.studio.jres.metadata.ui.wizards.POIUtils.NormalAttributeHelper;
import com.hundsun.ares.studio.jres.model.metadata.Function;
import com.hundsun.ares.studio.jres.model.metadata.FunctionProxy;
import com.hundsun.ares.studio.jres.model.metadata.MenuItem;
import com.hundsun.ares.studio.jres.model.metadata.MenuList;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.util.MenuUtils;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.IMapExtensibleModelPropertyDescriptor;

public class MenuAndFunctionExporter {
	
	private static Logger log = Logger.getLogger(MenuAndFunctionExporter.class);
	
	public static final String MENU_SHEET = "�˵�Ŀ¼";
	public static final String FUN_SHEET = "����Ŀ¼";
	public static final String MENU_TO_FUN = "�˵����ܶ���Ŀ¼";

	public static MenuList importMenusAndFunctions(HSSFWorkbook workBook, IARESElement element) throws Exception {
		 Map< String, List< List<String> > > excelStrings = 
		 POIUtils.getExcelStringForMenu(workBook, new String[] {MENU_SHEET, FUN_SHEET, MENU_TO_FUN}, new int[] {1, 1, 1}, new int[] {0,0,0});
		 
		 MenuList menuList = MetadataFactory.eINSTANCE.createMenuList();
		 List<List<String>> menuStrings = excelStrings.get(MENU_SHEET);
		 importMenus(menuList, menuStrings, element);
		 
		 List<List<String>> funStrings = excelStrings.get(FUN_SHEET);
		 importFunctions(menuList, funStrings, element);
		 
		 List<List<String>> funProxyStrings = excelStrings.get(MENU_TO_FUN);
		 importFunctionProxies(menuList, funProxyStrings, element);
		 
		 return menuList;
	}
	
	public static void importMenus(MenuList menuList, List<List<String>> menuStrings, IARESElement element) {
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		
		String[] titles = getMenuTitles();
		EStructuralFeature[] features = getMenuFeatures();
		
		for (int i = 0; i < titles.length; i++) {
			helperMap.put(titles[i], new NormalAttributeHelper(features[i]));
		}
			
		IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(element, MetadataPackage.Literals.MENU_ITEM);
		for (IExtensibleModelEditingSupport support : supports) {
			for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(element, MetadataPackage.Literals.MENU_ITEM)) {
				if (!desc.isDerived()) {
					if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
						helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
					} else {
						helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
					}
				}
			}
		}
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		int menuProsite = -1;
		for (int i = 0; i < menuStrings.get(0).size();i++) {
			String title = menuStrings.get(0).get(i);
			if (StringUtils.equals(title, "�˵�λ��")) {
				menuProsite = i;
			}
			helperList.add(helperMap.get(title));
		}
		
		Map<String , MenuItem> itemMap = new LinkedHashMap<String, MenuItem>();
		
		int lastDepth = 0;
		MenuItem lastMenu = null;
		LableA: for (int i = 1; i < menuStrings.size(); i++) {
			MenuItem item = (MenuItem) POIUtils.readObject(menuStrings.get(i), MetadataPackage.Literals.MENU_ITEM, helperList, true, element);
			
			{
				if (menuProsite > -1) {
					String mp = menuStrings.get(i).get(menuProsite);
					if (StringUtils.isNotBlank(mp)) {
						mp = mp.trim();
						itemMap.put(mp, item);
						if (mp.length() > 1) {
							{
								String subMp = mp.substring(0, mp.length()-1);
								while (StringUtils.isNotBlank(subMp)) {
									if (itemMap.get(subMp) != null) {
										itemMap.get(subMp).getSubItems().add(item);
										continue LableA;
									}else {
										subMp = subMp.substring(0, subMp.length()-1);
									}
								}
							}
						}
					}
					menuList.getItems().add(item);
				}else {
					String name = item.getChineseName();
					int depth = getDepth(name);
					
					if (depth == 0) {
						menuList.getItems().add(item);
					} else if (depth > lastDepth) {
						// ���ڵ������ֻ���ܴ���1
						lastMenu.getSubItems().add(item);
					} else if (depth <= lastDepth) {
						int delta = lastDepth - depth;
						EObject parent = getParentMenu(lastMenu, delta/3 + 1);
						addToParent(parent, item);
					}
					
					item.setChineseName(StringUtils.trim(item.getChineseName()));
					lastMenu = item;
					lastDepth = depth;
				}
			}
		}
	}
	
	// ������ deltaDepth �㸸�˵�
	private static EObject getParentMenu(MenuItem item, int deltaDepth) {
		EObject parent = item;
		for (int i = 0; i < deltaDepth; i++) {
			parent = parent.eContainer();
		}
		return parent;
	}
	
	private static void addToParent(EObject parent, MenuItem item) {
		if (parent instanceof MenuList) {
			((MenuList) parent).getItems().add(item);
		} else if (parent instanceof MenuItem) {
			((MenuItem) parent).getSubItems().add(item);
		}
	}

	private static void importFunctions(MenuList menuList, List<List<String>> funStrings, IARESElement element) {
		String[] titles = getFunctionTitles();
		EStructuralFeature[] features = getFunctionFeatures();
		
		List<EObject> functions = POIUtils.importExcelStringTable(funStrings, MetadataPackage.Literals.FUNCTION, titles, features, true, new String[0], new String[0], element);
		for (EObject fun : functions) {
			menuList.getFunctions().add((Function) fun);
		}
	}
	
	private static void importFunctionProxies(MenuList menuList, List<List<String>> funProxyStrings, IARESElement element) {
		int rowCount = funProxyStrings.size();
		
		Map<String , Integer> titleMap = new HashMap<String, Integer>();
		
		if (rowCount > 0) {
			for (int i = 0; i < funProxyStrings.get(0).size(); i++) {
				String title = funProxyStrings.get(0).get(i);
				titleMap.put(title, i);
			}
		}
		
		// ��һ���Ǳ��⣬����
		MenuItem menu = null;
		for (int i = 1; i < rowCount; i++) {
			List<String> row = funProxyStrings.get(i);
			
			int menuNo = 0;
			if(!MenuUtils.isStockDepartment()){
				menuNo = titleMap.get("������");
			}else {
				menuNo = titleMap.get("�˵���");
			}
			if (StringUtils.isNotBlank(row.get(menuNo))) {
				menu = findMenu(menuList.getItems(), row.get(menuNo).trim() ,menu);
			}
			int funNo = 0;
			if(!MenuUtils.isStockDepartment()){
				funNo = titleMap.get("�ӽ�����");
			}else {
				funNo = titleMap.get("���ܱ��");
			}
				
			if (menu == null) {
				log.error("����˵����ܶ���ʱ���Ҳ����˵�, ������" + rowCount);
			} else {
				FunctionProxy proxy = MetadataFactory.eINSTANCE.createFunctionProxy();
				if (!StringUtils.isEmpty(row.get(funNo)) && !StringUtils.equals(row.get(funNo), "-1")) {
					proxy.setFunCode(row.get(funNo));
					proxy.setDescription(row.get(1));
					menu.getFunctionProxys().add(proxy);	
				}
			}
		}
	}
	
	private static MenuItem findMenu(EList<MenuItem> menuItems, String menuId , MenuItem returnItem) {
		
		for (MenuItem item : menuItems) {
			if (StringUtils.equals(item.getName(), menuId)) {
				return item;
			}
			MenuItem item1 = findMenu(item.getSubItems(), menuId ,returnItem);
			if (item1 != null && StringUtils.equals(item1.getName(), menuId)) {
				return item1;
			}
		}
		return null;
	}
	
	public static void export(IARESResource resource, Map<String, List<List<String>>> contents) throws ARESModelException {
		MenuList menuList = resource.getInfo(MenuList.class);
		if (menuList == null)
			return;
		
		List<List<String>> menuListContents = exportExcelStringTable(resource, menuList);
		contents.put("�˵�Ŀ¼", menuListContents);
		
		List<List<String>> functionListContents = exportFunctionListTable(resource, menuList);
		contents.put("����Ŀ¼", functionListContents);
		
		List<List<String>> functionProxyListContents = exportFunctionProxyListTable(resource, menuList);
		contents.put("�˵����ܶ���Ŀ¼", functionProxyListContents);
	}
	
	private static List< List<String> > exportExcelStringTable(IARESResource element, MenuList menuList) {
		List<List<String>> result = new ArrayList<List<String>>();
		// ���ȹ���������
		List<String> header = new ArrayList<String>();
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		String[] titles = new String[]{"�˵�����", "�˵���", "ҳ��", "˵��"};
		header.addAll(Arrays.asList(titles));
		EStructuralFeature[] features = new EStructuralFeature[] {
											MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
											MetadataPackage.Literals.NAMED_ELEMENT__NAME,
											MetadataPackage.Literals.MENU_ITEM__URL,
											MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION
										};
		
		for (int i = 0; i < titles.length; i++) {
			helperMap.put(titles[i], new NormalAttributeHelper(features[i]));
		}
		
		IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(element, MetadataPackage.Literals.MENU_ITEM);
		for (IExtensibleModelEditingSupport support : supports) {
			for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(element, MetadataPackage.Literals.MENU_ITEM)) {
				if (!desc.isDerived()) {
					header.add(desc.getDisplayName());
					if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
						helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
					} else {
						helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
					}
				}
				
			}
		}
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		for (String title : header) {
			helperList.add(helperMap.get(title));
		}
		
		result.add(header);
		
		// ��ι�������
		List<MenuItem> items = menuList.getItems();
		result.addAll(generateContents(items, 0, helperList));
		
		return result;
	}
	
	private static String[] getMenuTitles() {
		return new String[]{"�˵�����", "�˵���", "ҳ��", "˵��"};
	}
	
	private static EStructuralFeature[] getMenuFeatures() {
		EStructuralFeature[] features = new EStructuralFeature[] {
				MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME,
				MetadataPackage.Literals.NAMED_ELEMENT__NAME,
				MetadataPackage.Literals.MENU_ITEM__URL,
				MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION
			};
		return features;
	}
	
	private static List<List<String>> generateContents(List<MenuItem> items, int depth, List<IAttributeHelper> helperList) {
		List<List<String>> result = new ArrayList<List<String>>();
		
		for (MenuItem item : items) {
			// �����������Ŀ��
			List<String> content = new ArrayList<String>();
			for (IAttributeHelper helper : helperList) {
				String value = helper.getValue(item);
				// ����ǰ�渽�ӿո��ʾ�㼶
				if (helper instanceof NormalAttributeHelper) {
					NormalAttributeHelper nHelper = (NormalAttributeHelper) helper;
					if (nHelper.feature == MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME) {
						value = getPrefix(depth) + value;
					}
				}
				content.add(value);
			}
			result.add(content);
			
			// ��Ŀ���ܻ�������:
			result.addAll(generateContents(item.getSubItems(), depth + 1, helperList));
		}
		
		return result;
	}
	
	// ���ݲ㼶�ӿո�ǰ׺��ÿ���2���ո�
	private static String getPrefix(int depth) {
		String prefix = StringUtils.EMPTY;
		for (int i = 0; i < depth; i++) {
			prefix += "  ";
		}
		return prefix;
	}
	
	private static int getDepth(String str) {
		String realStr = StringUtils.trim(str);
		int index = StringUtils.indexOf(str, realStr);
		if (index == -1) {
			return 0;
		}
		return index / 2;
	}
	
	/**
	 * �����������б�
	 * 
	 */
	private static List<List<String>> exportFunctionListTable(IARESResource element, MenuList menuList) {
		return exportFunctionExcelStringTable(menuList,
											MetadataPackage.Literals.MENU_LIST__FUNCTIONS, 
											MetadataPackage.Literals.FUNCTION, 
											element, 
											null);
	}
	
	
	/** �������˵����ܶ����б� */
	private static List<List<String>> exportFunctionProxyListTable(IARESResource element, MenuList menuList) {
		List<List<String>> result = new ArrayList<List<String>>();
		// ���ȹ���������
		List<String> header = new ArrayList<String>();
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		
		String[] titles = getFunctionProxyTitles();
		header.addAll(Arrays.asList(titles));
		
		addExtensionColumnsForFunction(element, new String[0], new String[0], header, helperMap);
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		for (String title : header) {
			helperList.add(helperMap.get(title));
		}
		
		result.add(header);
		generateFunctionProxyStringTable(result, 0, menuList.getItems(), element, header, helperMap);
		return result;
	}
	
	private static void generateFunctionProxyStringTable(List<List<String>> result, int depth, List<MenuItem> menus, IARESResource resource, List<String> header, Map<String, IAttributeHelper> helperMap) {
		for (MenuItem menu : menus) {
			List<FunctionProxy> contentObjectList = menu.getFunctionProxys();
			boolean firstTime = true; // ��ÿ���˵���	���ڵ�һ����Ӳ˵���Ϣ������ֻ������ܺ�
			if (contentObjectList == null || contentObjectList.isEmpty()) {
				List<String> content = new ArrayList<String>();
				content.addAll(Arrays.asList(getPrefix(depth) + menu.getName(), "", "", ""));
				if (header.size() > 4) {
					for (int i = 4; i < header.size(); i++) {
						content.add(StringUtils.EMPTY);
					}
				}
				result.add(content);
			} else {
				for (FunctionProxy proxy : contentObjectList) {
					List<String> content = new ArrayList<String>();
					// ǰ�漸�й̶�д��
					if (firstTime) {
						content.add(getPrefix(depth) + menu.getName());
						firstTime = false;
					} else {
						content.add("");
					}
					
					content.add(proxy.getFunCode());
					content.add(MenuUtils.getFunctionByName(resource, proxy.getFunCode()).getChineseName());
					content.add(proxy.getDescription());
					
					// ���洦����չ�У�����еĻ�...
					if (header.size() > 4) {
						for (int i = 4; i < header.size(); i++) {
							// ǰ�������Helperֻ�ܴ���Function
							IAttributeHelper helper = helperMap.get(header.get(i));
							Function function = MenuUtils.getFunctionByName(resource, proxy.getFunCode());
							content.add(helper.getValue(function));
						}
					}
					
					result.add(content);
				}
			}
			generateFunctionProxyStringTable(result, depth + 1, menu.getSubItems(), resource, header, helperMap);
		}
	}
	
	private static String[] getFunctionProxyTitles() {
		List<String> titles = new ArrayList<String>();
		if(MenuUtils.isStockDepartment()){
			titles.add("�˵���");
			titles.add("���ܱ��");
		} else {
			titles.add("������");
			titles.add("�ӽ�����");
		}
		titles.addAll(Arrays.asList("��������", "��ע"));
		return titles.toArray(new String[0]);
	}
	
	private static EStructuralFeature[] getFunctionProxyFeatures() {
		// ����ǰ�����к͹���������������һ�����ԣ������������ȷ������
		return new EStructuralFeature[] {
				MetadataPackage.Literals.FUNCTION_PROXY__FUN_CODE,
				MetadataPackage.Literals.FUNCTION_PROXY__FUN_CODE,
				MetadataPackage.Literals.FUNCTION_PROXY__FUN_CODE,
				MetadataPackage.Literals.FUNCTION_PROXY__DESCRIPTION
		};
	}
	
	public static List< List<String> > exportFunctionExcelStringTable(EObject owner, EReference reference, EClass itemClass, IARESElement element, IHeaderSorter sorter) {
		List<List<String>> result = new ArrayList<List<String>>();

		// ���ȹ���������
		List<String> header = new ArrayList<String>();
		// ���������������ֵ�ӳ��
		Map<String, IAttributeHelper> helperMap = new HashMap<String, POIUtils.IAttributeHelper>();
		
		String[] titles = getFunctionTitles();
		EStructuralFeature[] features = getFunctionFeatures();
		
		header.addAll(Arrays.asList(titles));
		for (int i = 0; i < titles.length; i++) {
			helperMap.put(titles[i], new NormalAttributeHelper(features[i]));
		}
		
		addExtensionColumnsForFunction(element, new String[0], new String[0], header, helperMap);
		
		if (sorter != null) {
			sorter.sort(header);
		}
		
		// ����ʵ�ʱ������������������֣��п��ܴ��ڿ���
		List<IAttributeHelper> helperList = new ArrayList<IAttributeHelper>();
		for (String title : header) {
			helperList.add(helperMap.get(title));
		}
		
		result.add(header);
		
		// ��ι�������
		List<EObject> contentObjectList = (List<EObject>) owner.eGet(reference);
		for (EObject eObject : contentObjectList) {
			List<String> content = new ArrayList<String>();
			for (IAttributeHelper helper : helperList) {
				content.add(helper.getValue(eObject));
			}
			
			result.add(content);
		}
		
		return result;
	}
	
	private static String[] getFunctionTitles() {
		List<String> titles = new ArrayList<String>();
		if(!MenuUtils.isStockDepartment()){
			titles.add("�ӽ�����");
			titles.add("����");
		}
		titles.addAll(Arrays.asList("���ܱ��", "��������", "��ע"));
		return titles.toArray(new String[0]);
	}
	
	private static EStructuralFeature[] getFunctionFeatures() {
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		if(!MenuUtils.isStockDepartment()){
			features.add(MetadataPackage.Literals.FUNCTION__SUB_TRANS_CODE);
			features.add(MetadataPackage.Literals.FUNCTION__SERV_ID);
		}
		features.addAll(Arrays.asList(MetadataPackage.Literals.NAMED_ELEMENT__NAME, 
										MetadataPackage.Literals.NAMED_ELEMENT__CHINESE_NAME, 
										MetadataPackage.Literals.NAMED_ELEMENT__DESCRIPTION));
		return features.toArray(new EStructuralFeature[0]);
	}
	

	
	private static void addExtensionColumnsForFunction(IARESElement element, String[] titles2, String[] dataKeys, List<String> header, Map<String, IAttributeHelper> helperMap) {
		for (int i = 0; i < titles2.length; i++) {
			header.add(titles2[i]); 
			helperMap.put(titles2[i], new ExtensibleDataAttributeHelper(dataKeys[i]));
		}
		
		IExtensibleModelEditingSupport[] supports = ExtensibleModelUtils.getEndabledEditingSupports(element, MetadataPackage.Literals.FUNCTION);
		for (IExtensibleModelEditingSupport support : supports) {
			for (IExtensibleModelPropertyDescriptor desc : support.getPropertyDescriptors(element, MetadataPackage.Literals.FUNCTION)) {
				if (!desc.isDerived()) {
					header.add(desc.getDisplayName());
					if (desc instanceof IMapExtensibleModelPropertyDescriptor) {
						helperMap.put(desc.getDisplayName(), new ExtensibleData2MapAttributeHelper(support.getKey(), desc.getStructuralFeature(), ((IMapExtensibleModelPropertyDescriptor) desc).getKey()));
					} else {
						helperMap.put(desc.getDisplayName(), new ExtensibleData2AttributeHelper(support.getKey(), desc.getStructuralFeature()));
					}
				}
				
			}
		}
	}
	
}
