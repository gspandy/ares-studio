/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
/**
 */
package com.hundsun.ares.studio.cres.extend.ui.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataServiceProvider;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.usermacro.UserMacro;
import com.hundsun.ares.studio.usermacro.UserMacroItem;
import com.hundsun.ares.studio.usermacro.constants.IUserMacroResType;

/**
 * <p>
 * CreatedDate: 2008-2-21
 * </p>
 * ���ദ��༭����������ʾ
 * �������ݻ����������ʾ
 * 
 * ��׼�ֶ�
 * ҵ����
 * ����
 *
 * @author sundl
 */
public abstract class CRESTextHover implements ITextHover {

	protected static String LINE_SEPERATOR = "<br>";
	
	/**
	 * ȫ��д�ַ������м������_��,����CNST_FUTURESDIRECTION_ALL
	 */
	private static Pattern CONSTANT_PATTERN = Pattern.compile("[[A-Z]+_*]+");
	
	protected IARESProject project;

	public CRESTextHover(IARESProject project) {
		this.project = project;
	}
	
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return CRESTextUtil.findWord(textViewer.getDocument(), offset);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer,
	 *      org.eclipse.jface.text.IRegion)
	 */
 	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
	    //IMetadataService service = DataServiceManager.getInstance().getService(project, IMetadataService.class);

		if (hoverRegion != null && hoverRegion.getLength() >= 0) {
			IDocument document = textViewer.getDocument();
			try {
				String text = document.get(hoverRegion.getOffset(), hoverRegion.getLength());
				
				//���ȫ��д����Ϊ����ƥ��
				Matcher m = CONSTANT_PATTERN.matcher(text);
				if(m.matches()){
					return getConstantHoverInfo(text);
				}
				
				char preChar = (char) 0;
				if (hoverRegion.getOffset() > 0) {
					preChar = document.getChar(hoverRegion.getOffset() - 1);
				}
				
				//�����@����ͷ��˵���Ǳ�׼�ֶ� 
				if(preChar == '@'){
					ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(project, IMetadataRefType.StdField, text, true);
					if(null != ref && ref.getObject() instanceof StandardField){
						StandardField field = (StandardField)ref.getObject();
						//  ��׼�ֶ�
						StringBuffer result = new StringBuffer();
						result.append(" <b>��׼�ֶ�:</b> " + field.getName() + LINE_SEPERATOR);
						result.append(" <b>������  :</b> " + field.getChineseName() + LINE_SEPERATOR);
						result.append(" <b>��������:</b> " + field.getDataType() + LINE_SEPERATOR);
						TypeDefaultValue dftValue = MetadataServiceProvider.getTypeDefaultValueOfStdFieldByName(project, text);
						if(null != dftValue) {
							result.append(" <b>C����Ĭ��ֵ:</b> " + dftValue.getValue(MetadataServiceProvider.C_TYPE));
						}
						return result.toString();
					}
				} else if(preChar == '<'){
					return getConstantHoverInfo(text);
					
				}else {
					// ϵͳ��
					IARESResource[]  sysMacroRes = project.getResources(IUserMacroResType.SYSTEM_MACRO);
					if(null != sysMacroRes && sysMacroRes.length > 0){
						UserMacro sysMacro = sysMacroRes[0].getInfo(UserMacro.class);
						if (sysMacro != null) {
							UserMacroItem macroItem = findMacro(text, sysMacro);
							if (macroItem != null) {
								StringBuilder info = new StringBuilder();
								info.append(" <b>ϵͳ�� </b>" + LINE_SEPERATOR);
								info.append(" <b>�����б�: </b>" + LINE_SEPERATOR);
								info.append(macroItem.getSequence() + LINE_SEPERATOR);
								info.append(LINE_SEPERATOR);
								info.append(" <b>��˵��: </b>" + LINE_SEPERATOR);
								info.append(" " + preProcess(macroItem.getDescription()));
								return info.toString();
							}
						}
					}
					
					// �û��Զ����
					IARESResource[] userMacroRes = project.getResources(IUserMacroResType.USER_MACRO);
					if (userMacroRes != null && userMacroRes.length > 0) {
						UserMacro userMacro = userMacroRes[0].getInfo(UserMacro.class);
						if (userMacro != null) {
							UserMacroItem macroItem = findMacro(text, userMacro);
							if (macroItem != null) {
								StringBuilder info = new StringBuilder();
								info.append(" <b>�Զ���� </b>" + LINE_SEPERATOR);
								info.append(" <b>�����б�: </b>" + LINE_SEPERATOR);
								info.append(macroItem.getSequence() + LINE_SEPERATOR);
								info.append(LINE_SEPERATOR);
								info.append(" <b>��˵��: </b>" + LINE_SEPERATOR);
								info.append(" " + preProcess(macroItem.getDescription()));
								return info.toString();	
							}
						}
					}
				}
				//��������
				return getResHoverInfo(text);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ""; //$NON-NLS-1$
	}

	private String getConstantHoverInfo(String text) {
		text = text.replaceFirst("<", "").replaceFirst(">", "");
		ReferenceInfo ref = ReferenceManager.getInstance().getFirstReferenceInfo(project, IMetadataRefType.ErrNo, text, true);
		if(null != ref && ref.getObject() instanceof ErrorNoItem){
			// �����
			ErrorNoItem errorNO = (ErrorNoItem)ref.getObject();
				if (errorNO != null) {
					StringBuilder info = new StringBuilder();
					info.append(" <b>�����: </b>" + errorNO.getNo() + LINE_SEPERATOR);
					info.append(" <b>������Ϣ: </b>" + errorNO.getMessage() + LINE_SEPERATOR);
					info.append(" <b>˵      ��: </b>" + errorNO.getDescription());
					return info.toString();
				}
		}
		
		ReferenceInfo ref1 = ReferenceManager.getInstance().getFirstReferenceInfo(project, IMetadataRefType.Const, text, true);
		if(null != ref1 && ref1.getObject() instanceof ConstantItem){
			//�û�����
			ConstantItem constantObj = (ConstantItem)ref1.getObject();
			if(null != constantObj){
				StringBuilder info = new StringBuilder();
				info.append(" <b>������: </b>" + constantObj.getName() + LINE_SEPERATOR);
				info.append(" <b>����ֵ: </b>" + constantObj.getValue() + LINE_SEPERATOR);
				info.append(" <b>˵      ��: </b>" + constantObj.getDescription());
				return info.toString();
			}
		}
		ReferenceInfo ref2 = ReferenceManager.getInstance().getFirstReferenceInfo(project, IMetadataRefType.Dictionary_Const, text, true);
		if(null != ref2 && ref2.getObject() instanceof DictionaryItem){
			//�����ֵ�
			DictionaryItem dicItem = (DictionaryItem)ref2.getObject();
			if(null != dicItem){
				StringBuilder info = new StringBuilder();
				info.append(" <b>��Ŀ������: </b>" + dicItem.getParent().getChineseName() + LINE_SEPERATOR);
				info.append(" <b>��Ŀ��: </b>" + dicItem.getParent().getName() + LINE_SEPERATOR);
				info.append(" <b>�ֵ���: </b>" + dicItem.getChineseName() + LINE_SEPERATOR);
				info.append(" <b>ֵ: </b>" + dicItem.getValue() + LINE_SEPERATOR);
				info.append(" <b>˵      ��: </b>" + dicItem.getDescription());
				return info.toString();
			}
		}
		return text;
	}

 	/**
 	 * ��ȡ��Դ������ʾ��Ϣ
 	 * @param name
 	 * @return
 	 */
 	protected abstract String getResHoverInfo(String name);

	/**
 	 * �������ֲ��Һ꣬�����������ֲ��������ǲ���[ ]��
 	 * @param name
 	 * @param macroList
 	 * @return
 	 */
 	private UserMacroItem findMacro(String name, UserMacro macroList) {
 		for (UserMacroItem item : macroList.getMacroItems()) {
 			String itemName = item.getName();
 			if (StringUtils.startsWith(itemName, "[")) {
 				itemName = StringUtils.substringAfter(itemName, "[");
 			} 
 			if (StringUtils.endsWith(itemName, "]")) {
 				itemName = StringUtils.substringBeforeLast(itemName, "]");
 			}
 			if (StringUtils.equals(itemName, name))
 				return item;
 		}
 		return null;
 	}
 	
 	/**
 	 * �Ը������ַ�������Ԥ�����ѻ���ת����<br>���Ա�����Ϣ�ؼ��л���
 	 */
 	private String preProcess(String text) {
 		if (!StringUtils.isEmpty(text)) {
 			return text.replace("\n", "<br>");
 		}
 		return text;
 	}

}
