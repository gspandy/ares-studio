/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.hundsun.ares.studio.biz.util;

import com.hundsun.ares.studio.biz.*;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.BizInterface;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.Multiplicity;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.StandardObjField;
import com.hundsun.ares.studio.biz.StandardObjFieldList;
import com.hundsun.ares.studio.biz.constants.IBizConstants;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IValidateConstant;
import com.hundsun.ares.studio.core.ResourceTypeMapping;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.util.ResourcesUtil;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.util.IDRangeUtil;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see com.hundsun.ares.studio.biz.BizPackage
 * @generated
 */
public class BizValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final BizValidator INSTANCE = new BizValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "com.hundsun.ares.studio.biz";

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
	public BizValidator() {
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
	  return BizPackage.eINSTANCE;
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
			case BizPackage.PARAMETER:
				return validateParameter((Parameter)value, diagnostics, context);
			case BizPackage.BIZ_INTERFACE:
				return validateBizInterface((BizInterface)value, diagnostics, context);
			case BizPackage.ARES_OBJECT:
				return validateARESObject((ARESObject)value, diagnostics, context);
			case BizPackage.STANDARD_OBJ_FIELD_LIST:
				return validateStandardObjFieldList((StandardObjFieldList)value, diagnostics, context);
			case BizPackage.STANDARD_OBJ_FIELD:
				return validateStandardObjField((StandardObjField)value, diagnostics, context);
			case BizPackage.ERROR_INFO:
				return validateErrorInfo((ErrorInfo)value, diagnostics, context);
			case BizPackage.PARAM_TYPE:
				return validateParamType((ParamType)value, diagnostics, context);
			case BizPackage.MULTIPLICITY:
				return validateMultiplicity((Multiplicity)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateParameter(Parameter parameter, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(parameter, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validateParameter_id(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validateParameter_type(parameter, diagnostics, context);
		return result;
	}

	/**
	 * Validates the id constraint of '<em>Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateParameter_id(Parameter parameter, DiagnosticChain diagnostics, Map<Object, Object> context) {
		String id = parameter.getId();
		boolean isOK = true;
		// ������������Ϊ�յģ�����������Ϊ��
		if(StringUtils.isBlank(id) && (parameter.getParamType() != ParamType.PARAM_GROUP)){
			if(diagnostics != null){
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
						DIAGNOSTIC_SOURCE, 
						0, 
						String.format("����������Ϊ��",id), 
						new Object[]{parameter,
						BizPackage.Literals.PARAMETER__ID
				}));
			}
			isOK = false;
		}
		IARESResource resource = (IARESResource)context.get(IValidateConstant.KEY_RESOUCE);
		ParamType paramType = parameter.getParamType();
		//ƴװ������Ϣ
		StringBuffer sb = new StringBuffer();
		sb.append(getParameterType(parameter));
		sb.append("["+StringUtils.defaultString(parameter.getId())+"],");
		if(paramType.equals(ParamType.STD_FIELD)){
			ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(resource.getARESProject(), IMetadataRefType.StdField, id, true);
			if(info == null){
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							sb.toString() + "�����ڸñ�׼�ֶ�", 
							new Object[]{parameter,
							BizPackage.Literals.PARAMETER__ID
					}));
				}
				isOK = false;
			}
		}
		//�����������ظ�
		if(parameter.eContainer() instanceof BizInterface) {
			BizInterface biz = (BizInterface)parameter.eContainer();
			EList<Parameter> inputs = biz.getInputParameters();
			int inputIndex = 0;
			if(inputs.contains(parameter)){
				for (Parameter param : inputs) {
					// ��id�����������飬����Ҫ�ٱ��ظ���
					if (StringUtils.isEmpty(param.getId()))
						continue;
					
					if(StringUtils.equals(id, param.getId())&& parameter!=param){
						inputIndex++;
					}
				}
				if(inputIndex > 0) {
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								sb.toString() + "�ֶ��ظ�", 
								new Object[]{parameter,
								BizPackage.Literals.PARAMETER__ID
						}));
					}
					isOK = false;
				}
			}
		
			int outIndex = 0;
			EList<Parameter> outputs = biz.getOutputParameters();
			if( outputs.contains(parameter)){
				for (Parameter param : outputs) {
					if (StringUtils.isEmpty(param.getId()))
						continue;
					
					if(StringUtils.equals(id, param.getId())&& parameter!=param){
						outIndex++;
					}
				}
				if(outIndex > 0) {
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								sb.toString() + "�ֶ��ظ�", 
								new Object[]{parameter,
								BizPackage.Literals.PARAMETER__ID
						}));
					}
					isOK = false;
				}
			}
		
		}
		
		return isOK;
	}

	/**
	 * Validates the type constraint of '<em>Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateParameter_type(Parameter parameter, DiagnosticChain diagnostics, Map<Object, Object> context) {
		String type = parameter.getType();
		IARESResource resource = (IARESResource)context.get(IValidateConstant.KEY_RESOUCE);
		ParamType paramType = parameter.getParamType();
		//ƴװ������Ϣ
		StringBuffer sb = new StringBuffer();
		sb.append(getParameterType(parameter));
		sb.append("["+StringUtils.defaultString(parameter.getId())+"],");
		if(paramType.equals(ParamType.NON_STD_FIELD)){
			if(StringUtils.isBlank(type)){
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							sb.toString() + "ҵ�����Ͳ���Ϊ��", 
							new Object[]{parameter,
							BizPackage.Literals.PARAMETER__TYPE
					}));
				}
				return false;
			}
			ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(resource.getARESProject(), IMetadataRefType.BizType, type, true);
			if(info == null){
				if(diagnostics != null){
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
							DIAGNOSTIC_SOURCE, 
							0, 
							sb.toString() + String.format("ҵ������%s������",type), 
							new Object[]{parameter,
							BizPackage.Literals.PARAMETER__TYPE
					}));
				}
				return false;
			}
		}else if(paramType.equals(ParamType.OBJECT)){
			// ����ж����׼�ֶΣ��Ͳ�Ӧ����У�������ˣ�������ϢӦ�ø���idȥ�����׼�ֶ��б��л�ȡ
			if (resource != null && BizUtil.hasStdObjList(resource.getARESProject())) {
				// do nothing
			} else {
				if(StringUtils.isBlank(type)){
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								sb.toString() + "ҵ�������Ϊ��", 
								new Object[]{parameter,
								BizPackage.Literals.PARAMETER__TYPE
						}));
					}
					return false;
				}
				//�ж�ҵ������Ƿ���ڣ�Ӧ����BizUtil.getObject������BizUtil.getObject���ܰ���UFT�еĶ���
				ARESObject info = BizUtil.getObject(type, resource.getARESProject());//ReferenceManager.getInstance().getFirstReferenceInfo(resource.getARESProject(), IBizRefType.Object, type, true);
				if(info == null){
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, 
								DIAGNOSTIC_SOURCE, 
								0, 
								sb.toString() +String.format("ҵ�����%s������",type), 
								new Object[]{parameter,
								BizPackage.Literals.PARAMETER__TYPE
						}));
					}
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * �жϲ����Ƿ�������,�������
	 * 
	 * @return
	 */
	private String getParameterType(Parameter parameter){
		if (parameter.eContainmentFeature() == BizPackage.Literals.BIZ_INTERFACE__INPUT_PARAMETERS) {
			return "�������";
		}else if (parameter.eContainmentFeature() == BizPackage.Literals.BIZ_INTERFACE__OUTPUT_PARAMETERS) {
			return "�������";
		}else {
			return "����";
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateBizInterface(BizInterface bizInterface, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(bizInterface, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateARESObject(ARESObject aresObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(aresObject, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(aresObject, diagnostics, context);
		if (result || diagnostics != null) result &= validateARESObject_objectId(aresObject, diagnostics, context);
		return result;
	}

	/**
	 * Validates the objectId constraint of '<em>ARES Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean validateARESObject_objectId(ARESObject aresObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		String objID = aresObject.getObjectId();
		IARESResource resource = (IARESResource)context.get(IValidateConstant.KEY_RESOUCE);
		if (StringUtils.isNotBlank(objID)) {
			try {
				int value = Integer.parseInt(objID);
				String range = IDRangeUtil.getIDRange(resource,IBizConstants.OBJ_ID_RANGE_KEY);
				if(!IDRangeUtil.isInRange(value, range)){
					if(diagnostics != null){
						diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
								DIAGNOSTIC_SOURCE,
								0,
								String.format("����Ų���%s��Χ��",range), 
								new Object[]{aresObject,
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
							new Object[]{aresObject,
							CorePackage.Literals.BASIC_RESOURCE_INFO__OBJECT_ID}));
				}
				return false;
			}
			
			IARESProject project =(IARESProject) context.get(IValidateConstant.KEY_RESOUCE_PROJECT);
			String type = ResourceTypeMapping.getInstance().getReferenceType(resource.getType());
			List<ReferenceInfo> referInfos = ReferenceManager.getInstance().getReferenceInfos(project, type, false);
			for(ReferenceInfo info : referInfos){
				Object obj = info.getObject();
				if (obj instanceof ARESObject) {
					if (!StringUtils.equals(aresObject.getFullyQualifiedName(), ((ARESObject) obj).getFullyQualifiedName()) &&
							StringUtils.equals(((ARESObject) obj).getObjectId(), aresObject.getObjectId())) {
						if(diagnostics != null){
							StringBuffer sb = new StringBuffer();
							sb.append("������ظ���");
							sb.append(ResourcesUtil.getChineseFileName("/", info.getResource().getModule())+"/"+((ARESObject) obj).getChineseName()+"["+aresObject.getName()+"]");
							diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR,
									DIAGNOSTIC_SOURCE,
									0,
									sb.toString(), 
									new Object[]{aresObject,
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
	public boolean validateStandardObjFieldList(StandardObjFieldList standardObjFieldList, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(standardObjFieldList, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateStandardObjField(StandardObjField standardObjField, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(standardObjField, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateErrorInfo(ErrorInfo errorInfo, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(errorInfo, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateParamType(ParamType paramType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMultiplicity(Multiplicity multiplicity, DiagnosticChain diagnostics, Map<Object, Object> context) {
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

} //BizValidator
