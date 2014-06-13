/**
 * Դ�������ƣ�ExtensibleModelUtils.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.extend;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.ui.editor.IDiagnosticProvider;
import com.hundsun.ares.studio.ui.editor.extend.user.IUserExtendedPropertyDescriptor;
import com.hundsun.ares.studio.ui.editor.extend.user.UserExtendedPropertyColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.extend.user.UserExtendedPropertyEditingSupport;

/**
 * @author gongyf
 * 
 */
public class ExtensibleModelUtils {

	/**
	 * ��ȡ��ǰ��Ҫ�ṩ�ı༭֧��
	 * 
	 * @param resource
	 * @param eClass
	 * @return
	 */
	public static IExtensibleModelEditingSupport[] getEndabledEditingSupports(
			IARESElement resource, EClass eClass) {
		IExtensibleModelEditingSupport[] editingSupports = ExtensibleModelManager
				.getInstance().getExtensibleModelEditingSupports(eClass);
		List<IExtensibleModelEditingSupport> elementList = new ArrayList<IExtensibleModelEditingSupport>();
		for (IExtensibleModelEditingSupport es : editingSupports) {
			if (es.isEnable(resource, eClass)) {
				elementList.add(es);
			}
		}
		return elementList
				.toArray(new IExtensibleModelEditingSupport[elementList.size()]);
	}

	/**
	 * ����ָ������չģ�ͽ��г�ʼ��������װ�ر��������չ����
	 * 
	 * @param resource
	 * @param model
	 * @param force
	 *            ǿ�Ƹ��Ǵ��ڵ���չ����
	 */
	public static void extend(IARESElement element, ExtensibleModel model, boolean force) {
		IExtensibleModelEditingSupport[] supports = getEndabledEditingSupports(
				element, model.eClass());
		for (IExtensibleModelEditingSupport support : supports) {
			if (force || !model.getData2().containsKey(support.getKey())) {
				model.getData2().put(support.getKey(),
						support.createMapValueObject());
			}
		}
	}

	/**
	 * ��ģ���е���չģ�ͽ��г�ʼ��������װ�ر��������չ����
	 * 
	 * @param resource
	 * @param res
	 * @param force
	 *            ǿ�Ƹ��Ǵ��ڵ���չ����
	 */
	public static void extendResource(IARESElement element, Resource res,
			boolean force) {
		List<ExtensibleModel> models = new ArrayList<ExtensibleModel>();
		for (Iterator<Object> iterator = EcoreUtil.getAllContents(res, true); iterator
				.hasNext();) {
			Object obj = iterator.next();
			// ���ﲻ��ֱ�Ӳ���ģ�ͣ���Ҫ�ȱ�����
			if (obj instanceof ExtensibleModel) {
				models.add((ExtensibleModel) obj);
			}
		}

		for (ExtensibleModel model : models) {
			extend(element, model, force);
		}
	}

	/**
	 * ������չ��
	 * 
	 * @param viewer
	 * @param resource
	 * @param eClass
	 * @param diagnosticProvider
	 */
	public static void createExtensibleModelTableViewerColumns(
			TableViewer viewer, IARESResource resource, EClass eClass,
			IDiagnosticProvider diagnosticProvider) {
		createExtensibleModelTableViewerColumns(viewer, resource, eClass, diagnosticProvider, true);
	}

	/**
	 * ������չ��
	 * 
	 * @param viewer
	 * @param resource
	 * @param eClass
	 * @param diagnosticProvider
	 */
	public static void createExtensibleModelTableViewerColumns(
			TableViewer viewer, IARESResource resource, EClass eClass,
			IDiagnosticProvider diagnosticProvider, boolean isEditingSupport) {
		IExtensibleModelEditingSupport[] editingSupports = ExtensibleModelUtils
				.getEndabledEditingSupports(resource, eClass);
		GC gc = new GC(viewer.getControl());

		try {
			for (IExtensibleModelEditingSupport support : editingSupports) {
				for (IExtensibleModelPropertyDescriptor descriptor : support
						.getPropertyDescriptors(resource, eClass)) {
					TableViewerColumn tvColumn = new TableViewerColumn(viewer,	SWT.LEFT);

					// ���ȸ�����ʵ�����������
					String displayName = descriptor.getDisplayName();

					Point p = gc.stringExtent(displayName);

					tvColumn.getColumn().setWidth(p.x + 20);
					tvColumn.getColumn().setText(displayName);
					
					// �½ӿڱ�־�����µĴ���ʽ
					if (descriptor instanceof IUserExtendedPropertyDescriptor) {
						tvColumn.setLabelProvider(new UserExtendedPropertyColumnLabelProvider(descriptor));
						tvColumn.setEditingSupport(new UserExtendedPropertyEditingSupport(viewer, descriptor));
					} else {
						// ���������ϵĴ���ʽ
						ExtensibleModelColumnLabelProvider provider = new ExtensibleModelColumnLabelProvider(
								support, descriptor , resource);
						provider.setDiagnosticProvider(diagnosticProvider);
						tvColumn.setLabelProvider(provider);
						if (isEditingSupport) {
							tvColumn.setEditingSupport(new ExtensibleModelEditingSupport(
									viewer, support, descriptor));
						}
					}
					tvColumn.getColumn().setMoveable(true);
				}

			}
		} finally {
			gc.dispose();
		}
	}

	/**
	 * ������չ��
	 * 
	 * @param viewer
	 * @param resource
	 * @param eClass
	 * @param diagnosticProvider
	 */
	public static void createExtensibleModelTreeViewerColumns(
			TreeViewer viewer, IARESResource resource, EClass eClass,
			IDiagnosticProvider diagnosticProvider) {
		// 2012-10-29 sundl ���ȡ����resource������ִ�� 
		if (resource == null) {
			return;
		}
		IExtensibleModelEditingSupport[] editingSupports = ExtensibleModelUtils
				.getEndabledEditingSupports(resource, eClass);
		GC gc = new GC(viewer.getControl());

		try {
			for (IExtensibleModelEditingSupport support : editingSupports) {
				for (IExtensibleModelPropertyDescriptor descriptor : support.getPropertyDescriptors(resource, eClass)) {
					TreeViewerColumn tvColumn = new TreeViewerColumn(viewer, SWT.LEFT);

					// ���ȸ�����ʵ�����������
					String displayName = descriptor.getDisplayName();

					Point p = gc.stringExtent(displayName);

					tvColumn.getColumn().setWidth(p.x + 20);
					tvColumn.getColumn().setText(displayName);

					if (descriptor instanceof IUserExtendedPropertyDescriptor) {
						tvColumn.setLabelProvider(new UserExtendedPropertyColumnLabelProvider(descriptor));
						tvColumn.setEditingSupport(new UserExtendedPropertyEditingSupport(viewer, descriptor));
					} else {
						ExtensibleModelColumnLabelProvider provider = new ExtensibleModelColumnLabelProvider(support, descriptor, resource);
						provider.setDiagnosticProvider(diagnosticProvider);
						tvColumn.setLabelProvider(provider);

						tvColumn.setEditingSupport(new ExtensibleModelEditingSupport(viewer, support, descriptor));
					}

					tvColumn.getColumn().setMoveable(true);
				}

			}
		} finally {
			gc.dispose();
		}
	}

	public static ExtensibleModel getHostExtensibleModel(EObject obj) {
		EObject parent = obj;
		while (parent != null && !(parent instanceof ExtensibleModel)) {
			parent = parent.eContainer();
		}
		return (ExtensibleModel) parent;
	}
	
}
