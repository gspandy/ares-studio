/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.ui.editor.IDiagnosticProvider;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.ExtensibleModelUtils;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelEditingSupport;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.IMapExtensibleModelPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.UserExtensibleModelEditingSupport;

/**
 * 
 * @author yanwj06282
 */
public class OpenResourceHelper {

	public static Map<String,String> createExtensibleModelTableViewerColumns(
			TableViewer viewer, IARESResource resource, IDiagnosticProvider diagnosticProvider ,Set<String> exDupTitle) {
		EObject info = null;
		Map<String,String> exTitle = new LinkedHashMap<String,String>();
		try {
			info = resource.getInfo(EObject.class);
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		if (info == null) {
			return exTitle;
		}
		//������Ҫ�ҳ�����Դ��Ӧ��ģ��EClass
		EClass eClass = null;
		for (Object obj : EPackage.Registry.INSTANCE.values()) {
			if (obj instanceof EPackage) {
				Class[] cla = info.getClass().getInterfaces();
				if (cla.length > 0) {
					eClass = (EClass) ((EPackage) obj).getEClassifier(cla[0].getSimpleName());
					if (eClass != null) {
						break;
					}
				}
			}
		}
		if (eClass == null) {
			return exTitle;
		}
		IExtensibleModelEditingSupport[] editingSupports = ExtensibleModelUtils
				.getEndabledEditingSupports(resource, eClass);
		GC gc = new GC(viewer.getControl());
		try {
			for (IExtensibleModelEditingSupport support : editingSupports) {
				//ֻ�����û���չ����������ȫ������
				if (!(support instanceof UserExtensibleModelEditingSupport)) {
					continue;
				}
				for (IExtensibleModelPropertyDescriptor descriptor : support
						.getPropertyDescriptors(resource, eClass)) {
					String displayName = descriptor.getDisplayName();
					if (!exDupTitle.contains(displayName)) {
						exDupTitle.add(displayName);
					}else {
						continue;
					}
					if (descriptor instanceof IMapExtensibleModelPropertyDescriptor) {
						Object obj = ((IMapExtensibleModelPropertyDescriptor)descriptor).getKey();
						if (obj != null) {
							exTitle.put(displayName , obj.toString());
						}
					}
					TableViewerColumn tvColumn = new TableViewerColumn(viewer,
							SWT.LEFT);

					// ���ȸ�����ʵ�����������
					Point p = gc.stringExtent(displayName);

					tvColumn.getColumn().setWidth(p.x + 20);
					tvColumn.getColumn().setText(displayName);

					ExtensibleModelColumnLabelProvider provider = new ExtensibleModelColumnLabelProvider(
							support, descriptor , resource){
						@Override
						protected EObject getOwner(Object element) {
							if (element instanceof OpenResourceInfo) {
								return ((OpenResourceInfo) element).getObj();
							}if (element instanceof EObject) {
								return (EObject) element;
							}
							return null;
						}
						
						@Override
						public String getText(Object element) {
							if (element instanceof OpenResourceInfo) {
								return super.getText(((OpenResourceInfo) element).getObj());
							}
							return StringUtils.EMPTY;
						}
						
						@Override
						protected Image doGetImage(Object element) {
							if (element instanceof OpenResourceInfo) {
								return super.doGetImage(((OpenResourceInfo) element).getObj());
							}
							return null;
						}
						
					};
					provider.setDiagnosticProvider(diagnosticProvider);
					tvColumn.setLabelProvider(provider);

					tvColumn.setEditingSupport(new ExtensibleModelEditingSupport(
							viewer, support, descriptor));
					tvColumn.getColumn().setMoveable(true);
				}

			}
		} finally {
			gc.dispose();
		}
		
		return exTitle;
	}
	
	public static String getType(Object element){
		if (element instanceof OpenResourceInfo) {
			String type = ((OpenResourceInfo) element).getResource().getType();
			if (StringUtils.equals(type, "table")) {
				return "��";
			}else if (StringUtils.equals(type, "view")) {
				return "��ͼ";
			}else if (StringUtils.equals(type, "jres_osequence")) {
				return "����";
			}else if (StringUtils.equals(type, "dbobject")) {
				return "Oracle��ռ�";
			}else if (StringUtils.equals(type, "dbuser")) {
				return "Oracle�û�Ȩ��";
			}else if (StringUtils.equals(type, "service")) {
				return "����";
			}else if (StringUtils.equals(type, "object")) {
				return "����";
			}else if (StringUtils.equals(type, "logicservice")) {
				return "�߼�����";
			}else if (StringUtils.equals(type, "logicfunction")) {
				return "�߼�����";
			}else if (StringUtils.equals(type, "atomservice")) {
				return "ԭ�ӷ���";
			}else if (StringUtils.equals(type, "atomfunction")) {
				return "ԭ�Ӻ���";
			}else if (StringUtils.equals(type, "procedure")) {
				return "����";
			}else if (StringUtils.equals(type, "singletabledata")) {
				return "��������";
			}
		}
		return StringUtils.EMPTY;
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
	
}
