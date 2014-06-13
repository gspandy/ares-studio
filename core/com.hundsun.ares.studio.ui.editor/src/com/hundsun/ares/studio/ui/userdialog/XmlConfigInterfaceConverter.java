/**
 * <p>Copyright: Copyright   2010</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
/**
 * 
 */
package com.hundsun.ares.studio.ui.userdialog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.extendable.ExtendFieldsHeader;
import com.hundsun.ares.studio.core.util.PersistentUtil;
import com.hundsun.ares.studio.core.util.StringUtil;

/**
 * �û�xml���öԻ������л�����
 * 
 * @author maxh
 */
public class XmlConfigInterfaceConverter implements IResourceChangeListener {

	public static final String CONFIG_FILE_NAME = "userExtendConfig.xml";

	/**
	 * 
	 */
	XmlConfigInterfaceConverter() {
	}

	static XmlConfigInterfaceConverter converter;

	static public XmlConfigInterfaceConverter getConverter() {
		if (converter == null) {
			converter = new XmlConfigInterfaceConverter();
		}
		ResourcesPlugin.getWorkspace().addResourceChangeListener(converter);
		return converter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(converter);
		super.finalize();
	}

	Map<IARESProject, XmlConfigInterface> configs = new HashMap<IARESProject, XmlConfigInterface>();

	public XmlConfigInterface getConfig(IARESProject project) {
		if (configs.get(project) == null) {
			IFile file = project.getProject().getFile(CONFIG_FILE_NAME);
			if (file.exists()) {
				configs.put(project, readXmlConfigInterface(file));
			}
		}
		return configs.get(project);
	}

	/**
	 * @param file
	 * @return
	 */

	/** ���ڵ� */

	private static String DIALOG = "publicResource";
	private static String TITLE = "title";

	/** �����ID */
	private static String ID = "id";
	private static String FILE_ID = "fileId";
	private static String GROUP = "group";
	private static String GROUPNAME = "groupName";
	private static String ISUSE = "isUse";
	/** �ؼ���Ŀ */
	private static String ITEM = "item";
	/** �ؼ������� */
	private static String NAME = "name";

	/** �ؼ����� */
	private static String TYPE = "type";

	/** ��Ӧjava�����еı��� */
	private static String ITEM_ID = "id";

	/** �ؼ�ֵ */
	private static String VALUE = "value";

	private static String EXTEND_COLUMN = "extendColumn";
	private static String WIDTH = "width";
	private static String CNAME = "cname";
	private static String ENAME = "ename";
	private static String COLUMN = "column";

	public static XmlConfigInterface readXmlConfigInterface(IFile file) {
		XmlConfigInterface xmlConfigInterface = new XmlConfigInterface();
		try {
			xmlConfigInterface = read(file.getContents());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlConfigInterface;
	}

	public static XmlConfigInterface read(InputStream is) {
		XmlConfigInterface xmlConfigInterface = new XmlConfigInterface();
		try {
			Element root = PersistentUtil.readRoot(is);
			// ��ȡ�û���չҳ��
			for (Element typeelement : (List<Element>) root.elements(DIALOG)) {
				xmlConfigInterface.addMenuInterfaceXml(typeelement.attributeValue(ID),
						readDialogInterfaceXml(typeelement));
			}
			// ��ȡ�û���չ��
			for (Element element : (List<Element>) root.elements(EXTEND_COLUMN)) {
				if (element.attribute(ID) == null) {
					continue;
				}
				String id = element.attributeValue(ID).trim();
				List<ExtendFieldsHeader> result = new ArrayList<ExtendFieldsHeader>();
				for (Object o : element.elements(COLUMN)) {
					Element field = (Element) o;
					String ename = StringUtil.getStringSafely(field.attributeValue(ENAME));
					String cname = StringUtil.getStringSafely(field.attributeValue(CNAME));
					String width = StringUtil.getStringSafely(field.attributeValue(WIDTH));
					String type = StringUtil.getStringSafely(field.attributeValue(TYPE));
					String value = StringUtil.getStringSafely(field.attributeValue(VALUE));
					ExtendFieldsHeader header = new ExtendFieldsHeader(ename, cname, type, width, value);
					result.add(header);
				}
				xmlConfigInterface.getExtendColumns().put(element.attributeValue(ID), result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlConfigInterface;
	}

	public static DialogInterfaceXml readDialogInterfaceXml(Element typeelement) {
		List<DialogInterfaceGroup> lstMenuInterfaceGroup = new ArrayList<DialogInterfaceGroup>();
		DialogInterfaceXml menuInterfaceXml = new DialogInterfaceXml();
		menuInterfaceXml.setTitle(StringUtil.getStringSafely(typeelement.attributeValue(TITLE)));
		menuInterfaceXml.setFileId(StringUtil.getStringSafely(typeelement.attributeValue(FILE_ID)));
		for (Element menuElement : (List<Element>) typeelement.elements(GROUP)) {
			DialogInterfaceGroup group = new DialogInterfaceGroup();
			group.setGroupName(StringUtil.getStringSafely(menuElement.attributeValue(GROUPNAME)));
			group.setUse(!StringUtil.getStringSafely(menuElement.attributeValue(ISUSE)).equalsIgnoreCase("false"));
			List<DialogInterfaceItem> lstMenuInterfaceItem = new ArrayList<DialogInterfaceItem>();
			for (Element itemElement : (List<Element>) menuElement.elements(ITEM)) {
				DialogInterfaceItem item = new DialogInterfaceItem();
				item.setLableName(StringUtil.getStringSafely(itemElement.attributeValue(NAME)));
				item.setSwtType(StringUtil.getStringSafely(itemElement.attributeValue(TYPE)));
				item.setValue(StringUtil.getStringSafely(itemElement.attributeValue(VALUE)));
				item.setId(StringUtil.getStringSafely(itemElement.attributeValue(ITEM_ID)));
				lstMenuInterfaceItem.add(item);
			}
			group.setLstMenuInterfaceItem(lstMenuInterfaceItem);
			lstMenuInterfaceGroup.add(group);
		}
		menuInterfaceXml.setLstMenuInterfaceGroup(lstMenuInterfaceGroup);
		return menuInterfaceXml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org
	 * .eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			if (event.getDelta() != null) {
				for (IResourceDelta delta1 : event.getDelta().getAffectedChildren()) {
					for (IResourceDelta delta2 : delta1.getAffectedChildren()) {
						if (delta2.getResource() != null && delta2.getResource().getName().equals(CONFIG_FILE_NAME)) {
							ResourcesPlugin.getWorkspace().removeResourceChangeListener(converter);
							converter = null;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
