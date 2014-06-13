package com.hundsun.ares.studio.atom.ui.editor;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.constants.IAtomRefType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.cres.extend.ui.text.CRESTextHover;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.model.database.DatabaseResourceData;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.constants.IProcedureRefType;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class AtomTextHover extends CRESTextHover{

	public AtomTextHover(IARESProject project) {
		super(project);
	}

	@Override
	protected String getResHoverInfo(String name) {
		//ԭ�Ӻ���
		ReferenceInfo afRef = ReferenceManager.getInstance().getFirstReferenceInfo(project, IAtomRefType.ATOM_FUNCTION_CNAME, name, true);
		if(null != afRef && afRef.getObject() instanceof AtomFunction) {
			AtomFunction function = (AtomFunction)afRef.getObject();
			StringBuilder info = new StringBuilder();
			info.append(" <b>�����: </b>" +function.getObjectId() + LINE_SEPERATOR);
			info.append(" <b>������: </b>" + function.getChineseName() + LINE_SEPERATOR);
			info.append(" <b>˵    ��: </b>" + function.getDescription()+ LINE_SEPERATOR);
			info.append(" <b>�������: </b>" + LINE_SEPERATOR);
			for(Parameter  inparam: function.getInputParameters()){
				String flag = "";
				if(!StringUtils.isBlank(inparam.getFlags())){
					flag = inparam.getFlags();
				}
				info.append(String.format("%s<dd><dd>%s", flag,inparam.getId() + LINE_SEPERATOR));
			}
			info.append(" <b>�������: </b>" + LINE_SEPERATOR);
			for(Parameter  outparam: function.getOutputParameters()){
				String flag = "";
				if(!StringUtils.isBlank(outparam.getFlags())){
					flag = outparam.getFlags();
				}
				info.append(String.format("%s<dd><dd>%s", flag,outparam.getId() + LINE_SEPERATOR));
			}
			return info.toString();
		}
		//����
		ReferenceInfo procRef = ReferenceManager.getInstance().getFirstReferenceInfo(project, IProcedureRefType.PROCEDURE_CNAME, name, true);
		if(null != procRef && procRef.getObject() instanceof Procedure) {
			Procedure function = (Procedure)procRef.getObject();
			StringBuilder info = new StringBuilder();
			info.append(" <b>�����: </b>" +function.getObjectId() + LINE_SEPERATOR);
			info.append(" <b>������: </b>" + function.getChineseName() + LINE_SEPERATOR);
			info.append(" <b>˵    ��: </b>" + function.getDescription()+ LINE_SEPERATOR);
			info.append(" <b>�������: </b>" + LINE_SEPERATOR);
			for(Parameter  inparam: function.getInputParameters()){
				String flag = "";
				if(!StringUtils.isBlank(inparam.getFlags())){
					flag = inparam.getFlags();
				}
				info.append(String.format("%s<dd><dd>%s", flag,inparam.getId() + LINE_SEPERATOR));
			}
			info.append(" <b>�������: </b>" + LINE_SEPERATOR);
			for(Parameter  outparam: function.getOutputParameters()){
				String flag = "";
				if(!StringUtils.isBlank(outparam.getFlags())){
					flag = outparam.getFlags();
				}
				info.append(String.format("%s<dd><dd>%s", flag,outparam.getId() + LINE_SEPERATOR));
			}
			return info.toString();
			
		}
		//���ݿ��
		ReferenceInfo dbRef = ReferenceManager.getInstance().getFirstReferenceInfo(project, IDatabaseRefType.Table, name, true);
		if(null != dbRef && dbRef.getObject() instanceof DatabaseResourceData){
			DatabaseResourceData db = (DatabaseResourceData)dbRef.getObject();
			StringBuilder info = new StringBuilder();
			info.append(" <b>���ݿ��: </b>" +db.getName() + LINE_SEPERATOR);
			info.append(" <b>�����: </b>" +db.getObjectId() + LINE_SEPERATOR);
			info.append(" <b>������: </b>" + db.getChineseName() + LINE_SEPERATOR);
			info.append(" <b>˵    ��: </b>" + db.getDescription()+ LINE_SEPERATOR);
			return info.toString();
		}
		return StringUtils.EMPTY;
	}

}
