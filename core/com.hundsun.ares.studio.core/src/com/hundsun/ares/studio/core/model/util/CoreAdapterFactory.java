/**
 */
package com.hundsun.ares.studio.core.model.util;

import com.hundsun.ares.studio.core.model.*;

import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.hundsun.ares.studio.core.model.CorePackage
 * @generated
 */
public class CoreAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static CorePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CoreAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = CorePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CoreSwitch<Adapter> modelSwitch =
		new CoreSwitch<Adapter>() {
			@Override
			public Adapter caseJRESResourceInfo(JRESResourceInfo object) {
				return createJRESResourceInfoAdapter();
			}
			@Override
			public Adapter caseBasicResourceInfo(BasicResourceInfo object) {
				return createBasicResourceInfoAdapter();
			}
			@Override
			public Adapter caseRevisionHistory(RevisionHistory object) {
				return createRevisionHistoryAdapter();
			}
			@Override
			public Adapter caseExtensibleModel(ExtensibleModel object) {
				return createExtensibleModelAdapter();
			}
			@Override
			public Adapter caseEStringToEObjectMapEntry(Map.Entry<String, EObject> object) {
				return createEStringToEObjectMapEntryAdapter();
			}
			@Override
			public Adapter caseReference(Reference object) {
				return createReferenceAdapter();
			}
			@Override
			public Adapter caseReferenceWithNamespace(ReferenceWithNamespace object) {
				return createReferenceWithNamespaceAdapter();
			}
			@Override
			public Adapter caseIReferenceProvider(IReferenceProvider object) {
				return createIReferenceProviderAdapter();
			}
			@Override
			public Adapter caseIJSONData(IJSONData object) {
				return createIJSONDataAdapter();
			}
			@Override
			public Adapter caseExtensibleModelConfigProperty(ExtensibleModelConfigProperty object) {
				return createExtensibleModelConfigPropertyAdapter();
			}
			@Override
			public Adapter caseExtensibleModelAttribute(ExtensibleModelAttribute object) {
				return createExtensibleModelAttributeAdapter();
			}
			@Override
			public Adapter caseUserExtensibleProperty(UserExtensibleProperty object) {
				return createUserExtensiblePropertyAdapter();
			}
			@Override
			public Adapter caseModuleExtensibleModel(ModuleExtensibleModel object) {
				return createModuleExtensibleModelAdapter();
			}
			@Override
			public Adapter caseProjectExtensibleModel(ProjectExtensibleModel object) {
				return createProjectExtensibleModelAdapter();
			}
			@Override
			public Adapter caseProjectRevisionHistoryProperty(ProjectRevisionHistoryProperty object) {
				return createProjectRevisionHistoryPropertyAdapter();
			}
			@Override
			public Adapter caseModuleRevisionHistoryList(ModuleRevisionHistoryList object) {
				return createModuleRevisionHistoryListAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.JRESResourceInfo <em>JRES Resource Info</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.JRESResourceInfo
	 * @generated
	 */
	public Adapter createJRESResourceInfoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.BasicResourceInfo <em>Basic Resource Info</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.BasicResourceInfo
	 * @generated
	 */
	public Adapter createBasicResourceInfoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.RevisionHistory <em>Revision History</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.RevisionHistory
	 * @generated
	 */
	public Adapter createRevisionHistoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ExtensibleModel <em>Extensible Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ExtensibleModel
	 * @generated
	 */
	public Adapter createExtensibleModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To EObject Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToEObjectMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.Reference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.Reference
	 * @generated
	 */
	public Adapter createReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ReferenceWithNamespace <em>Reference With Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ReferenceWithNamespace
	 * @generated
	 */
	public Adapter createReferenceWithNamespaceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.IReferenceProvider <em>IReference Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.IReferenceProvider
	 * @generated
	 */
	public Adapter createIReferenceProviderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.IJSONData <em>IJSON Data</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.IJSONData
	 * @generated
	 */
	public Adapter createIJSONDataAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ExtensibleModelConfigProperty <em>Extensible Model Config Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ExtensibleModelConfigProperty
	 * @generated
	 */
	public Adapter createExtensibleModelConfigPropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ExtensibleModelAttribute <em>Extensible Model Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ExtensibleModelAttribute
	 * @generated
	 */
	public Adapter createExtensibleModelAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.UserExtensibleProperty <em>User Extensible Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.UserExtensibleProperty
	 * @generated
	 */
	public Adapter createUserExtensiblePropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ModuleExtensibleModel <em>Module Extensible Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ModuleExtensibleModel
	 * @generated
	 */
	public Adapter createModuleExtensibleModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ProjectExtensibleModel <em>Project Extensible Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ProjectExtensibleModel
	 * @generated
	 */
	public Adapter createProjectExtensibleModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ProjectRevisionHistoryProperty <em>Project Revision History Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ProjectRevisionHistoryProperty
	 * @generated
	 */
	public Adapter createProjectRevisionHistoryPropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.hundsun.ares.studio.core.model.ModuleRevisionHistoryList <em>Module Revision History List</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hundsun.ares.studio.core.model.ModuleRevisionHistoryList
	 * @generated
	 */
	public Adapter createModuleRevisionHistoryListAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //CoreAdapterFactory
