package com.hundsun.ares.studio.logic.ui.pages;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.constants.IAtomRefType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.cres.extend.ui.text.CRESTextHover;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.model.database.DatabaseResourceData;
import com.hundsun.ares.studio.logic.LogicFunction;
import com.hundsun.ares.studio.logic.constants.ILogicRefType;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class LogicTextHover extends CRESTextHover {

	public LogicTextHover(IARESProject project) {
		super(project);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResHoverInfo(String name) {
		//�߼�����
		ReferenceInfo lfRef = ReferenceManager.getInstance().getFirstReferenceInfo(project, ILogicRefType.LOGIC_FUNCTION_CNAME, name, true);
		if(null != lfRef && lfRef.getObject() instanceof LogicFunction) {
			LogicFunction function = (LogicFunction)lfRef.getObject();
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
		//ԭ�ӷ���
		ReferenceInfo asRef = ReferenceManager.getInstance().getFirstReferenceInfo(project, IAtomRefType.ATOM_SERVICE_CNAME, name, true);
		if(null != asRef && asRef.getObject() instanceof AtomService) {
			AtomService function = (AtomService)asRef.getObject();
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
