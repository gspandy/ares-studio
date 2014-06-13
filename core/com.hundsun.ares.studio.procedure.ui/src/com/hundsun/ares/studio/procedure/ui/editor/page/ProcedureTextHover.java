package com.hundsun.ares.studio.procedure.ui.editor.page;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.cres.extend.ui.text.CRESTextHover;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.model.database.DatabaseResourceData;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.constants.IProcedureRefType;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class ProcedureTextHover extends CRESTextHover{

	public ProcedureTextHover(IARESProject project) {
		super(project);
	}

	@Override
	protected String getResHoverInfo(String name) {
		ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(project, IProcedureRefType.PROCEDURE_CNAME, name, true);
		if(null != ref && ref.getObject() instanceof Procedure) {
			Procedure function = (Procedure)ref.getObject();
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
