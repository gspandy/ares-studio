/**
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.jres.model.metadata.provider;


import com.hundsun.ares.studio.core.model.provider.JRESResourceInfoItemProvider;

import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;

import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MetadataResourceDataItemProvider
	extends JRESResourceInfoItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetadataResourceDataItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS);
			childrenFeatures.add(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ROOT);
			childrenFeatures.add(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((MetadataResourceData<?>)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_MetadataResourceData_type") :
			getString("_UI_MetadataResourceData_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(MetadataResourceData.class)) {
			case MetadataPackage.METADATA_RESOURCE_DATA__OPERATIONS:
			case MetadataPackage.METADATA_RESOURCE_DATA__ROOT:
			case MetadataPackage.METADATA_RESOURCE_DATA__ITEMS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__OPERATIONS,
				 MetadataFactory.eINSTANCE.createOperation()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ROOT,
				 MetadataFactory.eINSTANCE.createMetadataCategory()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createMetadataItem()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createTypeDefaultValue()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createStandardDataType()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createBusinessDataType()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createStandardField()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createDictionaryType()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createConstantItem()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createErrorNoItem()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createMenuItem()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createIDRangeItem()));

		newChildDescriptors.add
			(createChildParameter
				(MetadataPackage.Literals.METADATA_RESOURCE_DATA__ITEMS,
				 MetadataFactory.eINSTANCE.createBizPropertyConfig()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return MetadataUI.INSTANCE;
	}

}
