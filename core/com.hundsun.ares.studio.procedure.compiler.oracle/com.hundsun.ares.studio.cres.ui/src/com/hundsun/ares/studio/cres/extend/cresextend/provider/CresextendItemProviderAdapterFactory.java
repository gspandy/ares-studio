/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hundsun.ares.studio.cres.extend.cresextend.provider;

import com.hundsun.ares.studio.cres.extend.cresextend.util.CresextendAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CresextendItemProviderAdapterFactory extends CresextendAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CresextendItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.hundsun.ares.studio.cres.extend.cresextend.MoudleDepend} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MoudleDependItemProvider moudleDependItemProvider;

	/**
	 * This creates an adapter for a {@link com.hundsun.ares.studio.cres.extend.cresextend.MoudleDepend}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMoudleDependAdapter() {
		if (moudleDependItemProvider == null) {
			moudleDependItemProvider = new MoudleDependItemProvider(this);
		}

		return moudleDependItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.hundsun.ares.studio.cres.extend.cresextend.CresMoudleExtendProperty} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CresMoudleExtendPropertyItemProvider cresMoudleExtendPropertyItemProvider;

	/**
	 * This creates an adapter for a {@link com.hundsun.ares.studio.cres.extend.cresextend.CresMoudleExtendProperty}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCresMoudleExtendPropertyAdapter() {
		if (cresMoudleExtendPropertyItemProvider == null) {
			cresMoudleExtendPropertyItemProvider = new CresMoudleExtendPropertyItemProvider(this);
		}

		return cresMoudleExtendPropertyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.hundsun.ares.studio.cres.extend.cresextend.CresProjectExtendProperty} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CresProjectExtendPropertyItemProvider cresProjectExtendPropertyItemProvider;

	/**
	 * This creates an adapter for a {@link com.hundsun.ares.studio.cres.extend.cresextend.CresProjectExtendProperty}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCresProjectExtendPropertyAdapter() {
		if (cresProjectExtendPropertyItemProvider == null) {
			cresProjectExtendPropertyItemProvider = new CresProjectExtendPropertyItemProvider(this);
		}

		return cresProjectExtendPropertyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.hundsun.ares.studio.cres.extend.cresextend.ProcDefine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProcDefineItemProvider procDefineItemProvider;

	/**
	 * This creates an adapter for a {@link com.hundsun.ares.studio.cres.extend.cresextend.ProcDefine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createProcDefineAdapter() {
		if (procDefineItemProvider == null) {
			procDefineItemProvider = new ProcDefineItemProvider(this);
		}

		return procDefineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.hundsun.ares.studio.cres.extend.cresextend.GccDefine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GccDefineItemProvider gccDefineItemProvider;

	/**
	 * This creates an adapter for a {@link com.hundsun.ares.studio.cres.extend.cresextend.GccDefine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createGccDefineAdapter() {
		if (gccDefineItemProvider == null) {
			gccDefineItemProvider = new GccDefineItemProvider(this);
		}

		return gccDefineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.hundsun.ares.studio.cres.extend.cresextend.MvcDefine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MvcDefineItemProvider mvcDefineItemProvider;

	/**
	 * This creates an adapter for a {@link com.hundsun.ares.studio.cres.extend.cresextend.MvcDefine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMvcDefineAdapter() {
		if (mvcDefineItemProvider == null) {
			mvcDefineItemProvider = new MvcDefineItemProvider(this);
		}

		return mvcDefineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.hundsun.ares.studio.cres.extend.cresextend.FileDefine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FileDefineItemProvider fileDefineItemProvider;

	/**
	 * This creates an adapter for a {@link com.hundsun.ares.studio.cres.extend.cresextend.FileDefine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFileDefineAdapter() {
		if (fileDefineItemProvider == null) {
			fileDefineItemProvider = new FileDefineItemProvider(this);
		}

		return fileDefineItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (moudleDependItemProvider != null) moudleDependItemProvider.dispose();
		if (cresMoudleExtendPropertyItemProvider != null) cresMoudleExtendPropertyItemProvider.dispose();
		if (cresProjectExtendPropertyItemProvider != null) cresProjectExtendPropertyItemProvider.dispose();
		if (procDefineItemProvider != null) procDefineItemProvider.dispose();
		if (gccDefineItemProvider != null) gccDefineItemProvider.dispose();
		if (mvcDefineItemProvider != null) mvcDefineItemProvider.dispose();
		if (fileDefineItemProvider != null) fileDefineItemProvider.dispose();
	}

}