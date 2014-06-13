/**
 * 
 */
package com.hundsun.ares.studio.usermacro.compiler.handlers;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * SP��ǵĴ���
 * ��SP��Ǵ������Ϣ��ƴ���ϲ�����ƴ�ӵĽṹ��ȡ�����Ƿ������PROC����
 * 
 * @author yanwj06282
 *
 */
public class FlagSPHelper implements IUserMacroFlagHelper {

	private String prefix = "@";
	
	@Override
	public String genFlag(UserMacroToken token ,Map<Object, Object> context ,String content) {
		//ȡ�î��������±�λ�����磺<SP>%1$s������Ҫȡ������ġ�1������ʾ��ǰ�ı����ǵ�һλ
		int index = Integer.parseInt(StringUtils.substring(content, StringUtils.indexOfIgnoreCase(content, "<SP>")+5, StringUtils.indexOfIgnoreCase(content, "<SP>")+6));
		//α�����еĲ���
		int paramsSize = token.getUmParams().length;
		//�û����ж���Ĳ���
		int varSize = token.getVars().length;
		
		if (paramsSize > 1) {
			String errorCons = token.getUmParams()[paramsSize - 2];
			ReferenceInfo info = ReferenceManager.getInstance().getFirstReferenceInfo(token.getProject(), IMetadataRefType.ErrNo, errorCons, true);
			if (info != null && info.getObject() != null) {
				ErrorNoItem noItem = (ErrorNoItem) info.getObject();
				if (noItem != null) {
					token.getUmParams()[paramsSize - 1] = noItem.getMessage();
				}
			}
		}
		
		//�������proc������
		if (token.getParent().inProc) {
			prefix = "p_";
			//��α����������������û��궨��Ĳ�����������һһ��Ӧ
			if(paramsSize > varSize){
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				for(String param : token.getUmParams()[varSize].split(",")){
					param = StringUtils.replace(param.trim(), "@", "");
					if (token.getInoutParams().contains(param)) {
						sb.append(":p_");
						prefix = ":p_";
					}else {
						sb.append(":v_");
						prefix = ":v_";
					}
					sb.append(param);
					sb.append("='||");
					sb.append(prefix + param);
					sb.append("||'");
				}
				//'��ѯ�û���������ʧ��[p_operator_no='||p_operator_no||',v_limit_type='||v_limit_type||']', 1, 500);
				String param1 = sb.substring(0, sb.length() -1);
				param1 = param1 + "']";
				if (index<=token.getUmParams().length && StringUtils.indexOf(token.getUmParams()[index-1], param1) > -1) {
					content = content.substring(0, StringUtils.indexOf(content, "<SP>")) + content.substring(StringUtils.indexOf(content, "<SP>")+4, content.length());
					return content;
				}
				if(index<=token.getUmParams().length){
					token.getUmParams()[index-1] = "'" + token.getUmParams()[index-1]+param1 + "'";
				}else{
					throw new RuntimeException("�Զ���꣺["+token.getItem().getName()+"] ,������Ϣ��ƥ��!");
				}
				
			}else {//���α��������������û��������ƥ��
				token.getUmParams()[index-1] = "'" + token.getUmParams()[index-1] + "'";
			}
			content = content.substring(0, StringUtils.indexOf(content, "<SP>")) + content.substring(StringUtils.indexOf(content, "<SP>")+4, content.length());
		}else {//����PROC������
			if(paramsSize > varSize){
				StringBuffer sb = new StringBuffer();
				StringBuffer sb1 = new StringBuffer();
				sb.append("[");
				for(String param : token.getUmParams()[varSize].split(",")){
					param = StringUtils.replace(param.trim(), "@", "");
					sb1.append(prefix + param);
					sb.append(param);
					sb.append("=");
					if (token.getBusinessType() != null) {
						sb.append(UserMacroUtil.getDataType(token.getProject() , token.getBusinessType(), param));
					}else {
						sb.append("%s");
					}
					sb.append(",");
					sb1.append(",");
				}
				String param1 = sb.substring(0, sb.length() -1);
				String param2 = sb1.substring(0, sb1.length() -1);
				param1 = param1 + "]";
				if (index<=token.getUmParams().length && StringUtils.indexOf(token.getUmParams()[index-1], param1) > -1) {
					content = content.substring(0, StringUtils.indexOf(content, "<SP>")) + content.substring(StringUtils.indexOf(content, "<SP>")+4, content.length());
					return content;
				}

			token.getUmParams()[index-1] = "\"" + token.getUmParams()[index-1]+param1 + "\","+param2;
			content = content.substring(0, StringUtils.indexOf(content, "<SP>")) + content.substring(StringUtils.indexOf(content, "<SP>")+4, content.length());
		}else{
			if(index<=token.getUmParams().length){
				if (!StringUtils.startsWith(token.getUmParams()[index-1], "\"%s\",\"")) {
					token.getUmParams()[index-1] = "\"%s\",\"" + token.getUmParams()[index-1] + "\"";
				}
			}else{
				throw new RuntimeException("�Զ���꣺["+token.getItem().getName()+"] ,������Ϣ��ƥ��!");
			}
			
			content = content.substring(0, StringUtils.indexOf(content, "<SP>")) + content.substring(StringUtils.indexOf(content, "<SP>")+4, content.length());
			}
		}
		
		return content;
	}

}
