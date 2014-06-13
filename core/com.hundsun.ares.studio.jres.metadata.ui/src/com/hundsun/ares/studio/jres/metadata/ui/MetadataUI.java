/**
 * Դ�������ƣ�MetadataUI.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui;

import com.hundsun.ares.studio.jres.metadata.MetadataCore;
import com.hundsun.ares.studio.ui.ARESUI;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MetadataUI extends EMFPlugin {

	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final MetadataUI INSTANCE = new MetadataUI();

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hundsun.ares.studio.jres.metadata.ui"; //$NON-NLS-1$

	// The shared instance
	private static Implementation plugin;

	/**
	 * ��ȡ����ʹ�ò��ţ����ڣ�finance����֤ȯ��stock������
	 * 
	 * @return
	 */
	public static String getApplicationDepartment() {
		IPreferenceStore pStore = getPlugin().getPreferenceStore();
		return pStore.getString(MetadataCore.PRE_APPLICATION_DEPARTMENT);
	}

	/**
	 * Create the instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetadataUI() {
		super
		  (new ResourceLocator [] {
		     ARESUI.INSTANCE,
		   });
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public static class Implementation extends EclipseUIPlugin {
		/**
		 * Creates an instance.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public Implementation() {
			super();

			// Remember the static instance.
			//
			plugin = this;
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
		 */
		public void stop(BundleContext context) throws Exception {
			plugin = null;
			super.stop(context);
		}
		
		public Image getImage(String path) {
			ImageRegistry reg = getPlugin().getImageRegistry();
			Image image = reg.get(path);
			if (image == null) {
				ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
				image = desc.createImage();
				reg.put(path, image);
			}
			return image;
		}
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Implementation getDefault() {
		return getPlugin();
	}

	public static Image getImage2(String path) {
		return getPlugin().getImage(path);
	}
	
	public Object getImage(String path) {
		return super.getImage(path);
	}
}
