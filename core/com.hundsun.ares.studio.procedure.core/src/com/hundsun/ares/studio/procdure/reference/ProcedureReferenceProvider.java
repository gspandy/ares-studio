/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procdure.reference;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.BasicEList;

import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.ParamType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.model.IReferenceProvider2;
import com.hundsun.ares.studio.core.model.Reference;
import com.hundsun.ares.studio.core.model.impl.TextAttributeReferenceImpl;
import com.hundsun.ares.studio.core.model.impl.TextAttributeReferenceWithNamespaceImpl;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.procdure.ProcdurePackage;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.impl.PseudoCodeTextAttributeReferenceImpl;

/**
 * @author liaogc
 *
 */
public class ProcedureReferenceProvider implements IReferenceProvider2 {
		
		public static ProcedureReferenceProvider INSTANCE = new ProcedureReferenceProvider();
		
		private ProcedureReferenceProvider() {}

		/* (non-Javadoc)
		 * @see com.hundsun.ares.studio.core.model.IReferenceProvider2#getReferences(java.lang.Object, com.hundsun.ares.studio.core.IARESProject)
		 */
		@Override
		public List<Reference> getReferences(Object obj, IARESProject aresProject) {
			List<Reference> references = new ArrayList<Reference>();
			if (obj instanceof Procedure) {
				Procedure procedure = (Procedure) obj;
				BasicEList<Parameter> parameters = new BasicEList<Parameter>();
				parameters.addAll(procedure.getInputParameters());//����������
				parameters.addAll(procedure.getOutputParameters());//����������
				parameters.addAll(procedure.getInternalVariables());//����ڲ�����
				for(Parameter parameter:parameters){
					if(StringUtils.isNotBlank(parameter.getId())&& parameter.getParamType()==ParamType.STD_FIELD){
						Reference ref = new TextAttributeReferenceWithNamespaceImpl(IMetadataRefType.StdField, 
								parameter, 
								BizPackage.Literals.PARAMETER__ID);
						references.add(ref);
					}else if(/*parameter.getParamType()==ParamType.OBJECT ||*/ parameter.getParamType()==ParamType.PARAM_GROUP){
						if (BizUtil.hasStdObjList(aresProject)) {
							if (!StringUtils.isEmpty(parameter.getId())) {
								Reference ref = new TextAttributeReferenceImpl(IBizRefType.Std_Obj, parameter, BizPackage.Literals.PARAMETER__ID);
								references.add(ref);
							}
						} else 	if (!StringUtils.isEmpty(parameter.getType())) {
							// ��ʹ�ö����׼�ֶε�����£��������ö�����Դ
							Reference ref = new TextAttributeReferenceImpl(IBizRefType.Object, parameter, BizPackage.Literals.PARAMETER__TYPE);
							references.add(ref);
						}
					}
				}
				//α����Reference
				String pseudoCode = procedure.getPseudoCode();
				if(StringUtils.isNotBlank(pseudoCode)){
					Reference ref1 = new PseudoCodeTextAttributeReferenceImpl(IMetadataRefType.StdField, procedure,ProcdurePackage.Literals.PROCEDURE__PSEUDO_CODE);
					references.add(ref1);
					//�����׼�ֶ�Ҳ֧��α�����ع�
					Reference ref2 = new PseudoCodeTextAttributeReferenceImpl(IBizRefType.Std_Obj, procedure, ProcdurePackage.Literals.PROCEDURE__PSEUDO_CODE);
					references.add(ref2);
				}
			}
			return references;
		}


}
