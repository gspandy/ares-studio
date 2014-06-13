/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hundsun.ares.studio.jres.model.database.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IValidateConstant;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.util.ResourcesUtil;
import com.hundsun.ares.studio.jres.database.DatabaseCore;
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseResType;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.database.ColumnType;
import com.hundsun.ares.studio.jres.model.database.DBGenContext;
import com.hundsun.ares.studio.jres.model.database.DBModuleCommonProperty;
import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.jres.model.database.DatabaseResourceData;
import com.hundsun.ares.studio.jres.model.database.ForeignKey;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.TableKey;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.ViewResourceData;
import com.hundsun.ares.studio.jres.model.database.key_type;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleTableProperty;
import com.hundsun.ares.studio.jres.model.metadata.util.IDRangeUtil;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.validate.ValidateUtil;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see com.hundsun.ares.studio.jres.model.database.DatabasePackage
 * @generated
 */
public class DatabaseValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final DatabaseValidator INSTANCE = new DatabaseValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "com.hundsun.ares.studio.jres.model.database";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatabaseValidator() {
		super();
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return DatabasePackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case DatabasePackage.DB_MODULE_COMMON_PROPERTY:
				return validateDBModuleCommonProperty((DBModuleCommonProperty)value, diagnostics, context);
			case DatabasePackage.DATABASE_RESOURCE_DATA:
				return validateDatabaseResourceData((DatabaseResourceData)value, diagnostics, context);
			case DatabasePackage.TABLE_RESOURCE_DATA:
				return validateTableResourceData((TableResourceData)value, diagnostics, context);
			case DatabasePackage.TABLE_COLUMN:
				return validateTableColumn((TableColumn)value, diagnostics, context);
			case DatabasePackage.TABLE_INDEX_COLUMN:
				return validateTableIndexColumn((TableIndexColumn)value, diagnostics, context);
			case DatabasePackage.TABLE_INDEX:
				return validateTableIndex((TableIndex)value, diagnostics, context);
			case DatabasePackage.VIEW_RESOURCE_DATA:
				return validateViewResourceData((ViewResourceData)value, diagnostics, context);
			case DatabasePackage.DB_GEN_CONTEXT:
				return validateDBGenContext((DBGenContext)value, diagnostics, context);
			case DatabasePackage.FOREIGN_KEY:
				return validateForeignKey((ForeignKey)value, diagnostics, context);
			case DatabasePackage.TABLE_KEY:
				return validateTableKey((TableKey)value, diagnostics, context);
			case DatabasePackage.KEY_TYPE:
				return validatekey_type((key_type)value, diagnostics, context);
			case DatabasePackage.COLUMN_TYPE:
				return validateColumnType((ColumnType)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDBModuleCommonProperty(DBModuleCommonProperty dbModuleCommonProperty, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(dbModuleCommonProperty, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validateDBModuleCommonProperty_database(dbModuleCommonProperty, diagnostics, context);
		if (result || diagnostics != null) result &= validateDBModuleCommonProperty_supportDatabases(dbModuleCommonProperty, diagnostics, context);
		return result;
	}

	/**
	 * Validates the database constraint of '<em>DB Module Common Property</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDBModuleCommonProperty_database(DBModuleCommonProperty dbModuleCommonProperty, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO implement the constraint
		// -> specify the condition that violates the constraint
		// -> verify the diagnostic details, including severity, code, and message
		// Ensure that you remove @generated or mark it @generated NOT
		if (false) {
			if (diagnostics != null) {
				diagnostics.add
					(createDiagnostic
						(Diagnostic.ERROR,
						 DIAGNOSTIC_SOURCE,
						 0,
						 "_UI_GenericConstraint_diagnostic",
						 new Object[] { "database", getObjectLabel(dbModuleCommonProperty, context) },
						 new Object[] { dbModuleCommonProperty },
						 context));
			}
			return false;
		}
		return true;
	}

	/**
	 * Validates the supportDatabases constraint of '<em>DB Module Common Property</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDBModuleCommonProperty_supportDatabases(DBModuleCommonProperty dbModuleCommonProperty, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO implement the constraint
		// -> specify the condition that violates the constraint
		// -> verify the diagnostic details, including severity, code, and message
		// Ensure that you remove @generated or mark it @generated NOT
		if (false) {
			if (diagnostics != null) {
				diagnostics.add
					(createDiagnostic
						(Diagnostic.ERROR,
						 DIAGNOSTIC_SOURCE,
						 0,
						 "_UI_GenericConstraint_diagnostic",
						 new Object[] { "supportDatabases", getObjectLabel(dbModuleCommonProperty, context) },
						 new Object[] { dbModuleCommonProperty },
						 context));
			}
			return false;
		}
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDatabaseResourceData(DatabaseResourceData databaseResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(databaseResourceData, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableResourceData(TableResourceData tableResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(tableResourceData, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableResourceData_name(tableResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableResourceData_objectId(tableResourceData, diagnostics, context);
		return result;
	}
	
	/**
	 * ��ȡ��ռ�
	 * @param table
	 * @return
	 */
	public static String getTableSpace(DatabaseResourceData table) {
		EObject exOracle = table.getData2().get("Oracle");
		if(exOracle != null && exOracle instanceof OracleTableProperty){
			OracleTableProperty otp = (OracleTableProperty)exOracle;
			return otp.getSpace();
		}
		return "";
	}

	/**
	 * Validates the name constraint of '<em>Table Resource Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableResourceData_name(TableResourceData tableResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO ����Ϊ�գ�A-Za-z_0-9������Ҫ�󣬳�������30�ַ��������ظ�
		String name = tableResourceData.getName();
		//2013��5��17��11:13:55 ��ͬ��ռ䣬��ͬ�������ж�Ϊ������
		String tableSpace = getTableSpace(tableResourceData);
//		IJSONUtil.instance.getStringFromJSON(tableResourceData.toJSON(), "Oracle_space");
		
		Pattern p1 = Pattern.compile("[A-Za-z_0-9]*");
		Matcher m1 = p1.matcher(name);
		if(StringUtils.isBlank(name)){
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						"����Ϊ��", 
						new Object[]{tableResourceData, CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
				return false;
			}
		}
		
		if(!m1.matches()){
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						"����������A-Za-z_0-9������Ҫ��", 
						new Object[]{tableResourceData, CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
				return false;
			}
		}
		if(StringUtils.isNotBlank(name)){
			//Ĭ��Ϊ26
			String strLength = DatabasePreferencesUtil.getDatabsePprfernce(IDBConstant.TABLE_NAME_LENGTH, "26");
			int length = NumberUtils.toInt(strLength,26);
			if (length <= 0) {
				length = 26;
			}
			if(name.length() > length){
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							"�������ȴ���"+ length+":["+name+"]", 
							new Object[]{tableResourceData, CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
					return false;
				}
			}
		}
		
		List<String> tableNames = new ArrayList<String>();
		IARESProject project =(IARESProject) context.get(IValidateConstant.KEY_RESOUCE_PROJECT);
		List<ReferenceInfo> infoList = ReferenceManager.getInstance().getReferenceInfos(project, IDatabaseRefType.Table, true);
		for(ReferenceInfo referenceInfo : infoList){
			IARESResource res = referenceInfo.getResource();
		//	TableResourceData table = (TableResourceData)((Map<String, Object>)obj).get(IResourceTable.TARGET_OWNER);
			TableResourceData table;
			try {
				table = (TableResourceData)res.getInfo();
				if (table == null) {
					continue;
				}
				//��ռ�
				String space = getTableSpace(table);
					//IJSONUtil.instance.getStringFromJSON(table.toJSON(), "Oracle_space");
				tableNames.add(table.getName() + space);
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		
		if(tableNames.indexOf(name+tableSpace) != tableNames.lastIndexOf(name+tableSpace)){
			IARESResource res = (IARESResource) context.get(IValidateConstant.KEY_RESOUCE);
			if (res != null) {
				try {
					IARESResource[] reses = res.getARESProject().getResources(name+"."+IDatabaseResType.Table);
					StringBuffer sb = new StringBuffer();
					sb.append("�����ظ���");
					for(IARESResource resTemp : reses){
						TableResourceData trd = resTemp.getInfo(TableResourceData.class);
						sb.append(ResourcesUtil.getChineseFileName("/", resTemp.getModule())+"/"+trd.getChineseName()+"["+name+"] ,");
					}
					sb.substring(0, sb.length()-1);
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								sb.toString(), 
								new Object[]{tableResourceData, CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
						return false;
					}
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}

	/**
	 * Validates the objectId constraint of '<em>Table Resource Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableResourceData_objectId(TableResourceData tableResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validateObject(tableResourceData, diagnostics, context, IDatabaseRefType.Table);
	}

	private boolean validateObject(DatabaseResourceData databaseResourceData, DiagnosticChain diagnostics, Map<Object, Object> context ,String type){
		String objID = databaseResourceData.getObjectId();
		if (StringUtils.isNotBlank(objID)) {
			try {
				int value = Integer.parseInt(objID);
				IARESResource resource = (IARESResource)context.get(IValidateConstant.KEY_RESOUCE);
				String key = StringUtils.equals(type, IDatabaseRefType.Table) ? IDBConstant.TABLE_ID_RANGE_KEY : IDBConstant.VIEW_ID_RANGE_KEY;
				String range = IDRangeUtil.getIDRange(resource,key);
				if(!IDRangeUtil.isInRange(value, range)){
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
								DIAGNOSTIC_SOURCE,
								0,
								String.format("����Ų���%s��Χ��",range), 
								new Object[]{databaseResourceData,
								CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID}));
					}
					return false;
				}
				
			} catch (Exception e) {
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
							DIAGNOSTIC_SOURCE,
							0,
							"����Ų�������", 
							new Object[]{databaseResourceData,
							CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID}));
				}
				return false;
			}
			
			IARESProject project =(IARESProject) context.get(IValidateConstant.KEY_RESOUCE_PROJECT);
			List<ReferenceInfo> referInfos = ReferenceManager.getInstance().getReferenceInfos(project, type, false);
			for(ReferenceInfo info : referInfos){
				Object obj = info.getObject();
				if (obj instanceof DatabaseResourceData) {
					if (!StringUtils.equals(databaseResourceData.getFullyQualifiedName(), ((DatabaseResourceData) obj).getFullyQualifiedName()) &&
							StringUtils.equals(((DatabaseResourceData) obj).getObjectId(), databaseResourceData.getObjectId())) {
						if(diagnostics != null){
							StringBuffer sb = new StringBuffer();
							sb.append("������ظ���");
							sb.append(ResourcesUtil.getChineseFileName("/", info.getResource().getModule())+"/"+((DatabaseResourceData) obj).getChineseName()+"["+databaseResourceData.getName()+"]");
							diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
									DIAGNOSTIC_SOURCE,
									0,
									sb.toString(), 
									new Object[]{databaseResourceData,
									CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID}));
						}
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableColumn(TableColumn tableColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(tableColumn, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableColumn_name(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableColumn_columnName(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableColumn_fieldName(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableColumn_defaultValue(tableColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableColumn_dataType(tableColumn, diagnostics, context);
		return result;
	}

	/**
	 * Validates the name constraint of '<em>Table Column</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableColumn_name(TableColumn tableColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (StringUtils.isNotBlank(tableColumn.getName())) {
			if(ValidateUtil.isDuplication(tableColumn, DatabasePackage.Literals.TABLE_COLUMN__NAME, UpperCaseTransform.INSTANCE, context)){
				if(diagnostics != null){
					EAttribute errAttribute = DatabasePackage.Literals.TABLE_COLUMN__FIELD_NAME;
					
					// ����û�������������,��Ӧ��������������ֶκ������ĳ�ͻ
					if (StringUtils.isNotBlank(tableColumn.getColumnName())) {
						errAttribute = DatabasePackage.Literals.TABLE_COLUMN__COLUMN_NAME;
					}
					
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
							DIAGNOSTIC_SOURCE,
							0,
							"���ֶ��ظ�:["+tableColumn.getName()+"]",
							new Object[]{tableColumn,
							errAttribute}));
				}
				return false;
			}
		}

		return true;
	}
	/**
	 * Validates the columnName constraint of '<em>Table Column</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableColumn_columnName(TableColumn tableColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO implement the constraint
		// -> specify the condition that violates the constraint
		// -> verify the diagnostic details, including severity, code, and message
		// Ensure that you remove @generated or mark it @generated NOT
		if (false) {
			if (diagnostics != null) {
				diagnostics.add
					(createDiagnostic
						(Diagnostic.ERROR,
						 DIAGNOSTIC_SOURCE,
						 0,
						 "_UI_GenericConstraint_diagnostic",
						 new Object[] { "columnName", getObjectLabel(tableColumn, context) },
						 new Object[] { tableColumn },
						 context));
			}
			return false;
		}
		return true;
	}

	/**
	 * Validates the fieldName constraint of '<em>Table Column</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableColumn_fieldName(TableColumn tableColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO ���ֶ�������Ϊ�գ���Ӧ��׼�ֶβ����ڣ�����
		IARESResource resource = (IARESResource)context.get(IValidateConstant.KEY_RESOUCE);
		IARESProject project = resource.getARESProject();
		
		String message = "";
		if (!(tableColumn.eContainer() instanceof TableResourceData)) {
			message = "�޶���¼��";
		}
		
		String colName = tableColumn.getFieldName();
		if(StringUtils.isBlank(colName)){
			if (diagnostics != null) {
				diagnostics.add(new BasicDiagnostic(
								Diagnostic.ERROR,
								DIAGNOSTIC_SOURCE,
								0,
								message+"���ֶβ���Ϊ��",
								new Object[] {
										tableColumn,
										DatabasePackage.Literals.TABLE_COLUMN__FIELD_NAME }));
			}
			return false;
		}
		
		if (StringUtils.isNotBlank(colName)){
			String strcolNameLength = DatabasePreferencesUtil.getDatabsePprfernce(IDBConstant.TABLE_COLUMN_LENGTH, "30");
			int colNameLength = NumberUtils.toInt(strcolNameLength, 30);
			if(colName.length()>colNameLength){
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							message + "�ֶ������ȴ���"+colNameLength+":["+colName+"]", 
							new Object[]{tableColumn, DatabasePackage.Literals.TABLE_COLUMN__FIELD_NAME}));
					return false;
				}
			}
		}
		
		// ��׼�ֶ����͵ļ���׼�ֶ��Ƿ���ڣ��Ƿ��ظ�
		if (tableColumn.getColumnType() == ColumnType.STD_FIELD) {
			switch (ValidateUtil.checkReferenceId(project, colName, IMetadataRefType.StdField, context)) {
			case ERROR_NOT_EXIST:
				if (diagnostics != null) {
					diagnostics.add(new BasicDiagnostic(
									Diagnostic.ERROR,
									DIAGNOSTIC_SOURCE,
									0,
									message+"���ֶζ�Ӧ�ı�׼�ֶβ�����:["+colName+"]",
									new Object[] {
											tableColumn,
											DatabasePackage.Literals.TABLE_COLUMN__FIELD_NAME }));
				}
				return false;
			case ERROR_DUPLICATION:
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							message+"���õı�׼�ֶ���Ŀ���ظ�����������:["+colName+"]", 
							new Object[]{tableColumn,
							DatabasePackage.Literals.TABLE_COLUMN__FIELD_NAME}));
				}
				return false;
			}
		}

		return true;
	}

	/**
	 * Validates the defaultValue constraint of '<em>Table Column</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableColumn_defaultValue(TableColumn tableColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO implement the constraint
		// -> specify the condition that violates the constraint
		// -> verify the diagnostic details, including severity, code, and message
		// Ensure that you remove @generated or mark it @generated NOT
		if (false) {
			if (diagnostics != null) {
				diagnostics.add
					(createDiagnostic
						(Diagnostic.ERROR,
						 DIAGNOSTIC_SOURCE,
						 0,
						 "_UI_GenericConstraint_diagnostic",
						 new Object[] { "defaultValue", getObjectLabel(tableColumn, context) },
						 new Object[] { tableColumn },
						 context));
			}
			return false;
		}
		return true;
	}

	/**
	 * Validates the dataType constraint of '<em>Table Column</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableColumn_dataType(TableColumn tableColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// ��׼�ֶβ���Ҫ�������
		if (tableColumn.getColumnType() == ColumnType.STD_FIELD)
			return true;
		
		if (StringUtils.isEmpty(tableColumn.getDataType())) {
			if (diagnostics != null) {
				diagnostics.add
					(new BasicDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, 
							String.format("�ֶΡ�%s������������Ϊ�գ�", tableColumn.getName()), 
							new Object[] {tableColumn, DatabasePackage.Literals.TABLE_COLUMN__DATA_TYPE}));
			}
			return false;
		}
		
		IARESResource resource = (IARESResource)context.get(IValidateConstant.KEY_RESOUCE);
		IARESProject project = resource.getARESProject();
		ReferenceManager manager = ReferenceManager.getInstance();
		ReferenceInfo ref = manager.getFirstReferenceInfo(project, IMetadataRefType.BizType, tableColumn.getDataType(), true);
		if (ref == null) {
			if (diagnostics != null) {
				diagnostics.add
					(new BasicDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, 
							String.format("�ֶΡ�%s����ҵ���������͡�%s�������ڣ�", tableColumn.getName(), tableColumn.getDataType()), 
							new Object[] {tableColumn, DatabasePackage.Literals.TABLE_COLUMN__DATA_TYPE}));
			}
		}
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableIndexColumn(TableIndexColumn tableIndexColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(tableIndexColumn, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(tableIndexColumn, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableIndexColumn_columnName(tableIndexColumn, diagnostics, context);
		return result;
	}

	/**
	 * Validates the columnName constraint of '<em>Table Index Column</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableIndexColumn_columnName(TableIndexColumn tableIndexColumn, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO implement the constraint
		// -> specify the condition that violates the constraint
		// -> verify the diagnostic details, including severity, code, and message
		// Ensure that you remove @generated or mark it @generated NOT
		if (false) {
			if (diagnostics != null) {
				diagnostics.add
					(createDiagnostic
						(Diagnostic.ERROR,
						 DIAGNOSTIC_SOURCE,
						 0,
						 "_UI_GenericConstraint_diagnostic",
						 new Object[] { "columnName", getObjectLabel(tableIndexColumn, context) },
						 new Object[] { tableIndexColumn },
						 context));
			}
			return false;
		}
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableIndex(TableIndex tableIndex, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(tableIndex, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableIndex_name(tableIndex, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableIndex_columns(tableIndex, diagnostics, context);
		return result;
	}

	/**
	 * Validates the name constraint of '<em>Table Index</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableIndex_name(TableIndex tableIndex, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO ��������Ϊ�գ�a-z_0-9����Ҫ�󣬲�������
		String name = tableIndex.getName();
		String message = "";
		if (!(tableIndex.eContainer() instanceof TableResourceData)) {
			message = "�޶���¼��";
		}
		if(StringUtils.isBlank(name)){
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						message + "������Ϊ��", 
						new Object[]{tableIndex, DatabasePackage.Literals.TABLE_INDEX__NAME}));
				return false;
			}
		}
		
		Pattern p1 = Pattern.compile("[a-z_0-9]*");
		Matcher m1 = p1.matcher(name);
		if(!m1.matches()){
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, 
						DIAGNOSTIC_SOURCE, 
						0, 
						message + "������������a-z_0-9������Ҫ��:["+name+"]", 
						new Object[]{tableIndex, DatabasePackage.Literals.TABLE_INDEX__NAME}));
				return false;
			}
		}
		if(StringUtils.isNotBlank(name)){
			String strIndexLength = DatabasePreferencesUtil.getDatabsePprfernce(IDBConstant.INDEX_LENGTH, "26");
			int indexLength = NumberUtils.toInt(strIndexLength, 26);
			if(name.length()>indexLength){
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							message + "���������ȴ���"+indexLength+":["+name+"]", 
							new Object[]{tableIndex, DatabasePackage.Literals.TABLE_INDEX__NAME}));
					return false;
				}
			}
		}
		
		if(isDuplication(tableIndex)){
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						message + "�����������ظ�:["+name+"]", 
						new Object[]{tableIndex, DatabasePackage.Literals.TABLE_INDEX__NAME}));
				return false;
			}
		}
		if(isDuplicationIndex(tableIndex)){
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						"����:["+name+"] ,���Ʋ��ܺ���������ΨһԼ��ͬ����", 
						new Object[]{tableIndex, DatabasePackage.Literals.TABLE_INDEX__NAME}));
				return false;
			}
		}
		return true;
	}

	private boolean isDuplication(TableIndex tableIndex){
		if (tableIndex.eContainer() instanceof TableResourceData) {
			for (TableIndex index : ((TableResourceData)tableIndex.eContainer()).getIndexes()) {
				if (index != tableIndex && StringUtils.equals(index.getMark(), tableIndex.getMark())&& StringUtils.equals(index.getName(), tableIndex.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isDuplicationIndex(TableIndex tableIndex){
		if (tableIndex.eContainer() instanceof TableResourceData) {
			for (TableKey key : ((TableResourceData)tableIndex.eContainer()).getKeys()) {
				if ((key.getType() == key_type.PRIMARY || key.getType() == key_type.UNIQUE) && (StringUtils.equals(StringUtils.defaultString(key.getMark()), StringUtils.defaultString(tableIndex.getMark()))) && StringUtils.equals(key.getName(), tableIndex.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Validates the columns constraint of '<em>Table Index</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableIndex_columns(TableIndex tableIndex, DiagnosticChain diagnostics, Map<Object, Object> context) {
		String message = "";
		if (tableIndex.eContainer() instanceof TableResourceData) {
			message = "�����ֶβ���Ϊ��";
		}else {
			message = "�޶���¼�����������ֶβ���Ϊ��";
		}
		if (tableIndex.getColumns().size() <= 0 ) {
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						message, 
						new Object[]{tableIndex, DatabasePackage.Literals.TABLE_INDEX__COLUMNS}));
				return false;
			}
		}
		if(tableIndex.eContainer() instanceof TableResourceData){
			TableResourceData tableResourceData = (TableResourceData) tableIndex.eContainer();
			List<TableColumn> tableColumns = tableResourceData.getColumns();
			List<TableIndexColumn> tableIndexColumns = tableIndex.getColumns();
			List<String> fields = new ArrayList<String>();
			for(TableIndexColumn tableIndexColumn:tableIndexColumns){
				boolean find = false;
				for(TableColumn tableColumn:tableColumns){
					if(StringUtils.equals(tableColumn.getFieldName(), tableIndexColumn.getColumnName())){
						find = true;
						break;
					}
				}
				if(!find){
					fields.add(tableIndexColumn.getColumnName());
				}
			}
			if(fields.size()>0){//˵���������в��ڱ����
				StringBuffer messages = new StringBuffer();
				for(int i = 0;i < fields.size();i++){
					if(i!=fields.size()-1){
						messages.append(fields.get(i)).append(",");
					}else{
						messages.append(fields.get(i));
					}
				}
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							messages.toString()+"�����ֶ��в������ڱ���", 
							new Object[]{tableIndex, DatabasePackage.Literals.TABLE_INDEX__COLUMNS}));
					return false;
				}
				
			}
			
		}
		
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateViewResourceData(ViewResourceData viewResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(viewResourceData, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validateViewResourceData_name(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validateViewResourceData_sql(viewResourceData, diagnostics, context);
		if (result || diagnostics != null) result &= validateViewResourceData_objectId(viewResourceData, diagnostics, context);
		return result;
	}

	/**
	 * Validates the name constraint of '<em>View Resource Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateViewResourceData_name(ViewResourceData viewResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO ��ͼ������Ϊ�գ�A-Za-z_0-9����Ҫ�󣬲�������
		
		String enName = viewResourceData.getName();
		
		String tableSpace = getTableSpace(viewResourceData);
		
		if(StringUtils.isBlank(enName)){
			if (diagnostics != null) {
				diagnostics.add(new BasicDiagnostic(
								Diagnostic.ERROR,
								DIAGNOSTIC_SOURCE,
								0,
								"��ͼ������Ϊ��",
								new Object[] {
										viewResourceData,
										CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
			}
			return false;
		}
		
		Pattern p1 = Pattern.compile("[A-Za-z_0-9]*");
		Matcher m1 = p1.matcher(enName);
		if(!m1.matches()){
			if (diagnostics != null) {
				diagnostics.add(new BasicDiagnostic(
								Diagnostic.ERROR,
								DIAGNOSTIC_SOURCE,
								0,
								"��ͼ��������A-Za-z_0-9��Ҫ��:["+enName+"]",
								new Object[] {
										viewResourceData,
										CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
			}
			return false;
		}

		if(enName.length()>50){
			if (diagnostics != null) {
				diagnostics.add(new BasicDiagnostic(
								Diagnostic.ERROR,
								DIAGNOSTIC_SOURCE,
								0,
								"��ͼ�����ȳ���50�ַ�:["+enName+"]",
								new Object[] {
										viewResourceData,
										CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
			}
			return false;
		}
		List<String> viewNames = new ArrayList<String>();
		IARESProject project =(IARESProject) context.get(IValidateConstant.KEY_RESOUCE_PROJECT);
		List<ReferenceInfo> infoList = ReferenceManager.getInstance().getReferenceInfos(project, IDatabaseRefType.View, true);
		for(ReferenceInfo referenceInfo : infoList){
			IARESResource res = referenceInfo.getResource();
			ViewResourceData view;
			try {
				view = res.getInfo(ViewResourceData.class);
				if (view == null) {
					continue;
				}
				//��ռ�
				String space = getTableSpace(view);
				viewNames.add(view.getName() + space);
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		
		if(viewNames.indexOf(enName+tableSpace) != viewNames.lastIndexOf(enName+tableSpace)){
			IARESResource res = (IARESResource) context.get(IValidateConstant.KEY_RESOUCE);
			if (res != null) {
				try {
					IARESResource[] reses = res.getARESProject().getResources(enName+"."+IDatabaseResType.View);
					StringBuffer sb = new StringBuffer();
					sb.append("��ͼ���ظ���");
					for(IARESResource resTemp : reses){
						DatabaseResourceData trd = resTemp.getInfo(DatabaseResourceData.class);
						sb.append(ResourcesUtil.getChineseFileName("/", resTemp.getModule())+"/"+trd.getChineseName()+"["+enName+"] ,");
					}
					sb.substring(0, sb.length()-1);
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								sb.toString(), 
								new Object[]{viewResourceData, CorePackage.Literals.BASIC_RESOURCE_INFO__NAME}));
						return false;
					}
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * Validates the sql constraint of '<em>View Resource Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateViewResourceData_sql(ViewResourceData viewResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		// TODO ���ݿ���ͼSQL�����
		String viewSQL = viewResourceData.getSql();
		if(StringUtils.isBlank(viewSQL)){
			if (diagnostics != null) {
				diagnostics.add(new BasicDiagnostic(
								Diagnostic.ERROR,
								DIAGNOSTIC_SOURCE,
								0,
								"SQL��䲻��Ϊ��",
								new Object[] {
										viewResourceData,
										DatabasePackage.Literals.VIEW_RESOURCE_DATA__SQL }));
			}
			return false;
		}
		return true;
	}

	/**
	 * Validates the objectId constraint of '<em>View Resource Data</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateViewResourceData_objectId(ViewResourceData viewResourceData, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validateObject(viewResourceData, diagnostics, context, IDatabaseRefType.View);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDBGenContext(DBGenContext dbGenContext, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(dbGenContext, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateForeignKey(ForeignKey foreignKey, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if(foreignKey!=null){
			String foreignKeyFullTablePath = foreignKey.getTableName();
			String foreignKeyFieldName = foreignKey.getFieldName();
			if(StringUtils.isNotBlank(foreignKeyFullTablePath) && StringUtils.isNotBlank(foreignKeyFieldName)){
				IARESResource resource = (IARESResource) context.get(IValidateConstant.KEY_RESOUCE);
				IARESResource tableResource;
				try {
					tableResource = resource.getARESProject().findResource(foreignKeyFullTablePath, IDatabaseResType.Table);
					if(tableResource!=null){
						TableResourceData tableResourceData = tableResource.getInfo(TableResourceData.class);
						for(TableColumn tableColumn :tableResourceData.getColumns()){
							String fieldName =tableColumn.getFieldName();
							if(StringUtils.equals(foreignKeyFieldName, fieldName)){
								return true;
							}
						}
					}
					
					String message = "�����Ӧ���в�����";
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								message, 
								new Object[]{foreignKey, DatabasePackage.Literals.TABLE_COLUMN__FOREIGNKEY}));
						return false;
					}
				
					return false;
				} catch (ARESModelException e) {
				}
				
			}
			
				
		}
	
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTableKey(TableKey tableKey, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(tableKey, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableKey_name(tableKey, diagnostics, context);
		if (result || diagnostics != null) result &= validateTableKey_columns(tableKey, diagnostics, context);
		return result;
	}

	/**
	 * Validates the name constraint of '<em>Table Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableKey_name(TableKey tableKey, DiagnosticChain diagnostics, Map<Object, Object> context) {
		//���Ʋ���Ϊ��
		if (StringUtils.isBlank(tableKey.getName())) {
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						"��Լ������Ϊ��!", 
						new Object[]{tableKey, DatabasePackage.Literals.TABLE_KEY__NAME}));
				return false;
			}
		}
		
		//����
		if (StringUtils.isNotBlank(tableKey.getName())) {
			//Ĭ��Ϊ26
			String strLength = DatabasePreferencesUtil.getDatabsePprfernce(IDBConstant.CONSTRAINT_LENGTH, "26");
			int length = NumberUtils.toInt(strLength,26);
			if (length <= 0) {
				length = 26;
			}
			if(tableKey.getName().length() > length){
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							"��Լ�������ȴ���"+ length+":["+tableKey.getName()+"]", 
							new Object[]{tableKey,  DatabasePackage.Literals.TABLE_KEY__NAME}));
					return false;
				}
			}
		
		}
		//Լ�������ܺ����������ظ�,
		EObject eobj = tableKey.eContainer();
		if (eobj instanceof TableResourceData) {
			EList<TableKey> tableKeys = ((TableResourceData) eobj).getKeys();
			for(TableKey ti : tableKeys){
				if (tableKey != ti && StringUtils.equals(ti.getMark(), tableKey.getMark()) && StringUtils.equals(ti.getName(), tableKey.getName())) {
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								"��Լ����["+ti.getName()+"] �����ظ�!", 
								new Object[]{tableKey, DatabasePackage.Literals.TABLE_KEY__NAME}));
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Validates the columns constraint of '<em>Table Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateTableKey_columns(TableKey tableKey, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (tableKey.getColumns().size() == 0) {
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						"��Լ����" + tableKey.getName() + "���ֶ��б�Ϊ��!", 
						new Object[]{tableKey, DatabasePackage.Literals.TABLE_KEY__COLUMNS}));
				return false;
			}
		}
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatekey_type(key_type key_type, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateColumnType(ColumnType columnType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} //DatabaseValidator