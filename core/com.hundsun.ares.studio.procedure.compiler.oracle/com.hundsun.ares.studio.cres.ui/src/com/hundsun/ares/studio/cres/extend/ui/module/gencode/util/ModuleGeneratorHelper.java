/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.cres.extend.ui.module.gencode.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.model.ModuleRevisionHistoryList;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.cres.extend.core.constants.ICresExtendConstants;
import com.hundsun.ares.studio.cres.extend.cresextend.CresMoudleExtendProperty;
import com.hundsun.ares.studio.cres.extend.cresextend.CresProjectExtendProperty;
import com.hundsun.ares.studio.cres.extend.cresextend.CresextendFactory;
import com.hundsun.ares.studio.cres.extend.cresextend.MoudleDepend;

/**
 * ������������
 * @author 
 * @version 1.0
 * @history
 */
public class ModuleGeneratorHelper {
	
	public static final String FILE_OUTPUT_LOCATION = "com.hundsun.ares.studio.preference.fileoutputlocation";
	
	private static Pattern pCppMethodSignature = Pattern.compile("(//[^\\r\\n]*\\r?\\n\\s*)?int\\s+FUNCTION_CALL_MODE\\s+\\w+\\(IAS2Context \\* lpContext,IF2UnPacker \\* lpInUnPacker,IF2Packer \\* lpOutPacker\\)");
		//Pattern.compile("(//[^\\r\\n]*\\r?\\n\\s*)?int\\s+\\w+\\s+F\\d+\\(.*?\\)");
	private static String FORMAT_ALL = 
		"#ifndef %1$s \n" +
		"#define %1$s \n" +
		"%2$s \n" +
		"%3$s\n" +
		"#endif /* %1$s */\r\n";
	
	private static String FORMAT_INCLUDE =
		"#include \"%1$s\" \n";
	
	/**
	 * ��Դͳ�Ƹ�ʽ��
	 */
	private static String RES_HEADER_FORMAT = "*%-15s%-50s%-50s%-100s*";
	
	/**
	 * ģ�����������Դͳ����Ϣ
	 * @param infos
	 * @return
	 */
	public static StringBuffer getResourceStatisticsInfo(List<ModuleResourceStatisticsInfo> infos) {
		StringBuffer ret = new StringBuffer();
		//�����ܺ�����
		Collections.sort(infos,new Comparator<ModuleResourceStatisticsInfo>() {
			@Override
			public int compare(ModuleResourceStatisticsInfo o1,
					ModuleResourceStatisticsInfo o2) {
				return o1.getObjectID().compareTo(o2.getObjectID());
			}
		});
		
		ret.append("/");
		ret.append(String.format(RES_HEADER_FORMAT, "***************","*****************************************","*****************************************","********************************************************************************"));
		ret.append("\r\n");
		ret.append(String.format(RES_HEADER_FORMAT, " �����","Ӣ����","������","˵����Ϣ"));
		ret.append("\r\n");
		ret.append(String.format(RES_HEADER_FORMAT, "---------------","-----------------------------------------","-----------------------------------------","---------------------------------------------------------------------------------"));
		ret.append("\r\n");

		for (ModuleResourceStatisticsInfo info : infos) {
			ret.append(String.format(RES_HEADER_FORMAT, " "+info.getObjectID(),
					info.getName(),info.getcName(),info.getDesc().replaceAll("\n", "").replaceAll("\r", "")));
			ret.append("\r\n");
		}
		ret.append(String.format(RES_HEADER_FORMAT, "*******************","*****************************************","*****************************************","********************************************************************************"));
		ret.append("/");
		ret.append("\r\n");
		
		return ret;
	}

	/**
	 * ��ȡ��ѡ��������ļ�·��
	 * 
	 */
	public static String getFileOutPutPath() {
		String id = "com.hundsun.ares.studio.ui.jres_gen_path";

		int index = StringUtils.lastIndexOf(id, '.');
		if(index <= 0 || index == id.length()){
			return null;
		}
		
		String qualifer = StringUtils.substring(id,0, index);
		String key = StringUtils.substring(id,index + 1, id.length());
		String text = Platform.getPreferencesService().getString(qualifer, key, "", null); 
		return text;
	}
	
	/**
	 * д�ļ��İ�����Ϣ
	 * 
	 * @param sb
	 * @param includes
	 */
	public static void writeIncludeSection(StringBuffer sb, List<String> includes) {

		HashSet<String> includeSet = new HashSet<String>();
		includeSet.addAll(includes);
		
		sb.append("\n");
		for (String file : includeSet) {
			sb.append(String.format(FORMAT_INCLUDE, file));
		}

	}
	
	/**
	 * ��ȡģ��ĸ�ģ��·�� ��Ӣ����·����
	 * <br>���磺
	 * ģ���ϵ A->B->C,����Cģ�飺
	 * ���أ�A\B
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static String getParentModulePath(IARESModule module) throws Exception{
		List<IARESModule> parents = new ArrayList<IARESModule>();
		getParentModules(module, parents);
		int parentSize = parents.size();
		if(parentSize > 0){//���ڸ�ģ��
			StringBuffer path = new StringBuffer();
			for (int i = parentSize - 1; i >= 0; i--) {
				path.append(parents.get(i).getShortName());
				path.append("\\");
			}
			return path.toString();
		}
		return "";
	}
	
	/**
	 * ��ȡģ��ĸ�ģ��·�� (������·����ָ���ָ���)
	 * <br>���磺
	 * ģ���ϵ A->B->C,����Cģ�飺
	 * @param module
	 * @segment �ָ��� 
	 * @return �ָ�����-������A-B��
	 * @throws Exception
	 */
	public static String getParentModuleCnamePath(IARESModule module,String segment) throws Exception{
		List<IARESModule> parents = new ArrayList<IARESModule>();
		getParentModules(module, parents);
		int parentSize = parents.size();
		if(parentSize > 0){//���ڸ�ģ��
			StringBuffer path = new StringBuffer();
			for (int i = parentSize - 1; i >= 0; i--) {
				path.append(getModuleProperty(parents.get(i)).getString(ICommonModel.CNAME));
				path.append(segment);
			}
			return path.toString();
		}
		return "";
	}
	
	/**
	 * ��ȡģ��ĸ�ģ��·�� (������·��)
	 * <br>���磺
	 * ģ���ϵ A->B->C,����Cģ�飺
	 * ���أ�A\B
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static String getParentModuleCnamePath(IARESModule module) throws Exception{
		List<IARESModule> parents = new ArrayList<IARESModule>();
		getParentModules(module, parents);
		int parentSize = parents.size();
		if(parentSize > 0){//���ڸ�ģ��
			StringBuffer path = new StringBuffer();
			for (int i = parentSize - 1; i >= 0; i--) {
				path.append(getModuleProperty(parents.get(i)).getString(ICommonModel.CNAME));
				path.append("\\");
			}
			return path.toString();
		}
		return "";
	}
	
	/**
	 * ��ȡģ��ĸ�ģ��·�� (������·��)
	 * ��ָ��ǰ׺�����߼�_��ԭ��_��
	 * <br>���磺
	 * ģ���ϵ A->B->C,����Cģ�飺
	 * ���أ�A\B
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static String getParentModuleCnamePathWithPrefix(IARESModule module,String prefix) throws Exception{
		List<IARESModule> parents = new ArrayList<IARESModule>();
		getParentModules(module, parents);
		int parentSize = parents.size();
		if(parentSize > 0){//���ڸ�ģ��
			StringBuffer path = new StringBuffer();
			for (int i = parentSize - 1; i >= 0; i--) {
				path.append(prefix + getModuleProperty(parents.get(i)).getString(ICommonModel.CNAME));
				path.append("\\");
			}
			return path.toString();
		}
		return "";
	}
	
	/**
	 * ��ȡģ������и�ģ��
	 * @param module
	 * @param parents
	 */
	public static void getParentModules(IARESModule module,List<IARESModule> parents){
		IARESModule parent = module.getParentModule();
		if(null != parent) {
			parents.add(parent);
			getParentModules(parent, parents);
		}
	}
	
	/**
	 * ��ȡ��������·��
	 * @param module
	 * @return
	 */
	public static String getModuleGenCodePath(IARESProject project) {
		String path = getFileOutPutPath();
		if (path == null || path.isEmpty()) {
			path = "c:\\generate\\";
		} else {
			if (!(path.endsWith("\\") || path.endsWith("/"))) {
				path += "\\";
			}
		}
		// 2014-3-27 sundl TASK #9687 ��ѡ������Cres��UFT��������Ŀ¼   CRESҵ���߼����ɴ���Ŀ¼�Զ�������CRES����Ŀ¼
		// 2014-4-14 sundl TASK #9885 �洢���̹����У�ģ���������ʱ������Ҫ��CRES
		return path = path + project.getElementName() + File.separator;
	}
	
	/**
	 * ��ȡ����������
	 * 
	 * @param module
	 * @return
	 */
	public static List<MoudleDepend> getAllDepends(IARESModule module) throws Exception{
		List<MoudleDepend> depends = new ArrayList<MoudleDepend>();
		getAllDepends(module,depends);
		return depends;
	}

	private static void getAllDepends(IARESModule module, List<MoudleDepend> depends) throws Exception{
		EList<MoudleDepend> mds = getCresMoudleExtendProperty(module).getDepends();
		depends.addAll(mds);
		for (MoudleDepend md : mds) {
			//����moudleDepend�ҵ���Ӧ��ģ��
			for (IARESModule m : module.getARESProject().getModules()) {
				if(StringUtils.equals(m.getElementName(), md.getModulePath())){
					getAllDepends(m, depends);
					break;
				}
			}
		}
	}

	
	/**
	 * ��ȡcresģ����չ����
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static CresMoudleExtendProperty getCresMoudleExtendProperty(IARESModule module) throws Exception{
		ModuleProperty mp = getModuleProperty(module);
		Object mProperty = mp.getMap().get(ICresExtendConstants.CRES_EXTEND_MOUDLE_PROPERTY);
		if(mProperty instanceof CresMoudleExtendProperty) {
			return (CresMoudleExtendProperty)mProperty;
		}
		return CresextendFactory.eINSTANCE.createCresMoudleExtendProperty();
	}
	
	/**
	 * ��ȡcres������չ����
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static CresProjectExtendProperty getCresProjectExtendProperty(IARESModule module) throws Exception{
		return getCresProjectExtendProperty(module.getARESProject());
	}
	
	/**
	 * ��ȡcres������չ����
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static CresProjectExtendProperty getCresProjectExtendProperty(IARESProject project) throws Exception{
		Object pProperty = project.getProjectProperty().getMap().get(ICresExtendConstants.CRES_EXTEND_PROJECT_PROPERTY);
		if(pProperty instanceof CresProjectExtendProperty) {
			return (CresProjectExtendProperty)pProperty;
		}
			return CresextendFactory.eINSTANCE.createCresProjectExtendProperty();
	}
	
	/**
	 * ��ȡģ������
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static ModuleProperty getModuleProperty(IARESModule module) throws Exception{
		IARESResource res = module.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
		return res.getInfo(ModuleProperty.class);
	}
	
	public static void writeStartupMethods(StringBuffer sb, String ProjectVersion) {
		sb.append("const char *  ASFC_CALL_MODE GetLibVersion()\n");
		sb.append("{\n");
		sb.append("return \"");
		sb.append(ProjectVersion);
		sb.append("\";\n");
		sb.append("}\n");
		sb.append("void  ASFC_CALL_MODE OnLoad(IAppContext * pContext,char * arg)\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����Ӧ��������¼�����\n");
		sb.append("}\n");
		sb.append("void  ASFC_CALL_MODE OnUnload(IAppContext * pContext)\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����Ӧ���ж���¼�����\n");
		sb.append("}\n");
		sb.append("void  ASFC_CALL_MODE OnStart(IAppContext * pContext,char * arg)\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����ӦAS�����¼�����\n");
		sb.append("}\n");
		sb.append("void  ASFC_CALL_MODE OnStop(IAppContext * pContext)\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����ӦASֹͣ�¼�����\n");
		sb.append("}\n");
		sb.append("\n");
		sb.append("\n");
	}
	
	public static void writeStartupMethods2(StringBuffer sb, String ProjectVersion) {
		sb.append("char *  FUNCTION_CALL_MODE GetLibVersion()\n");
		sb.append("{\n");
		sb.append("return \"");
		sb.append(ProjectVersion);
		sb.append("\";\n");
		sb.append("}\n");
		sb.append("void  FUNCTION_CALL_MODE OnLoad(char * arg)\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����Ӧ��������¼�����\n");
		sb.append("}\n");
		sb.append("void  FUNCTION_CALL_MODE OnUnload()\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����Ӧ���ж���¼�����\n");
		sb.append("}\n");
		sb.append("void  FUNCTION_CALL_MODE OnStart()\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����ӦAS�����¼�����\n");
		sb.append("}\n");
		sb.append("void  FUNCTION_CALL_MODE OnStop()\n");
		sb.append("{\n");
		sb.append("//@todo �ڴ˲�����ӦASֹͣ�¼�����\n");
		sb.append("}\n");
		sb.append("\n");
		sb.append("\n");
	}
	
	/**
	 * д���м��ö�ٷ���1.0
	 * 
	 * @param sb
	 * @param businesses
	 */
	public static void writeMiddlewareEnumerateMethod(StringBuffer sb, List<BasicResourceInfo> businesses) {
		sb.append("int ASFC_CALL_MODE GetCompsInfo(int Index, LPFUNC_INFO  ppv )\n");
		writeMiddlewareEnumerateMethodbody(sb, businesses);
	}
	
	/**
	 * д���м��ö�ٷ���2.0
	 * 
	 * @param sb
	 * @param businesses
	 */
	public static void writeMiddlewareEnumerateMethod2(StringBuffer sb, List<BasicResourceInfo> businesses) {
		sb.append("int FUNCTION_CALL_MODE GetBizFunctionsInfo(int Index, LPBIZ_FUNC_INFO  ppv )\n");
		writeMiddlewareEnumerateMethodbody(sb, businesses);
	}
	
	private static void writeMiddlewareEnumerateMethodbody(StringBuffer sb, List<BasicResourceInfo> businesses) {

		sb.append("{\n");
		sb.append("int iReturnCode = ASFC_EXISTS;\n");
		sb.append("switch (Index)\n");
		sb.append("{\n");
		for (int i = 0; i < businesses.size(); i++) {
			sb.append("case ");
			sb.append(i);
			sb.append(":\n");
			sb.append("if  (ppv!=NULL)\n");
			sb.append("{\n");
			// ppv->dwFunctionNo = 433001;
			sb.append("ppv->dwFunctionNo = ");
			sb.append(businesses.get(i).getObjectId());
			sb.append(";\n");
			// ppv->iVersion = 20070821;
			sb.append("ppv->iVersion = ");
			sb.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));

			// String ver = services.get(i).getVersion();
			// if (ver == null || ver.isEmpty()) {
			// sb.append(0);
			// } else {
			// if (ver.indexOf(".") != -1) {
			// String vv[] = ver.split("\\.");
			// sb.append(vv[vv.length - 1]);
			// } else {
			// sb.append(ver);
			// }
			// }

			sb.append(";\n");
			// ppv->ReqProc = F433001;
			sb.append("ppv->ReqProc = ");
			sb.append("F" + businesses.get(i).getObjectId());
			sb.append(";\n");
			// ppv->AnsProc = NULL;
			sb.append("ppv->AnsProc = NULL;\n");
			// ppv->Caption = "����_�ͻ�������Ϣ��";
			sb.append("ppv->Caption = \"");
			sb.append(businesses.get(i).getChineseName());
			sb.append("\";\n");
			sb.append("}\n");
			sb.append("break;\n");
		}
		// default:iReturnCode = ASFC_NONE;
		sb.append("default:iReturnCode = ASFC_NONE;\n");
		sb.append("}\n");
		sb.append("return iReturnCode;\n");
		sb.append("}\n");
	}

	/**
	 * ����cpp�ļ�����ͷ�ļ�
	 * 
	 * @param Cppbody
	 * @param headerFileName
	 * @param includeFiles
	 * @param defaults
	 * @return
	 */
	public static StringBuffer generateHeaderFile(StringBuffer Cppbody, String headerFileName, String includeFiles[], String defaults[]) {

		String macroName = headerFileName.toUpperCase().replaceAll("\\.", "_");
		macroName = "_" + macroName + "_";
		
		StringBuffer include = new StringBuffer();
		if (includeFiles != null) {
			for (String file : includeFiles) {
				include.append( String.format(FORMAT_INCLUDE, file) );
			}
		}
		
		StringBuffer body = new StringBuffer();
		Matcher m = pCppMethodSignature.matcher(Cppbody);
		int iFindPostion = 0;
		while (m.find(iFindPostion)) {
			MatchResult result = m.toMatchResult();
			body.append("#ifdef __cplusplus \nextern \"C\" { \n#endif \n\n");
			if (defaults != null) {
				String function = result.group();
				int beginIndex = function.indexOf('(') + 1;
				int endIndex = function.lastIndexOf(')');
				
				String s1 = function.substring(0, beginIndex);
				String s2 = function.substring(beginIndex, endIndex);
				String s3 = function.substring(endIndex);
				
				String splitString[] = s2.split(",");
				for (int i = 0; i < defaults.length; i++) {
					splitString[splitString.length - 1 - i ] += " = " + defaults[i];
				}
				body.append(s1);
				body.append(splitString[0]);
				for (int i = 1; i < splitString.length; i++) {
					body.append(", ");
					body.append(splitString[i]);
				}

				body.append(s3);
			} else {
				body.append(result.group());
			}
			body.append(";\n");
			body.append("#ifdef __cplusplus \n} \n#endif\n\n");
			iFindPostion = result.end();
		}
		
		return new StringBuffer(String.format(FORMAT_ALL, macroName, include, body));
	}
	
	/**
	 * д���ļ�ע��ͷ��Ϣ
	 * 
	 * @param commentHeader
	 * @param fileName
	 * @param date
	 */
	public static void writeCommentHeader(StringBuffer sb, String commentHeader, String fileName, String moduleName, Date date) {
		if(commentHeader == null) return;
		// ֧�ֺ��滻 $(FILE) $(DATE) $(MODULE)
		commentHeader = commentHeader.replaceAll("\\$\\(FILE\\)", fileName);
		commentHeader = commentHeader.replaceAll("\\$\\(MODULE\\)", moduleName);
		String theDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
		commentHeader = commentHeader.replaceAll("\\$\\(DATE\\)", theDate);
		
		sb.append(commentHeader);
		sb.append("\n");
	}
	
	/**
	 * ��ȡģ���޸ļ�¼
	 * @param module
	 * @return
	 * @throws Exception
	 */
	protected static List<RevisionHistory> getModuleHistorys(IARESModule module) throws Exception{
		ModuleProperty property = ModuleGeneratorHelper.getModuleProperty(module);//ģ������
		//ģ���޸ļ�¼
		ModuleRevisionHistoryList modRev = (ModuleRevisionHistoryList) property.getMap().get(Constants.RevisionHistory.MODULE_REVISION_EXT_KEY);
		
		if(null != modRev)
		{
			return modRev.getHistories();
		}else {
			return new ArrayList<RevisionHistory>();
		}
	}
	
	/**
	 * ���޸ļ�¼���汾������
	 * @param hiss
	 */
	protected static void sortHistoryByVersion(List<RevisionHistory> hiss) {
		Collections.sort(hiss, new Comparator<RevisionHistory>() {

			@Override
			public int compare(RevisionHistory log1, RevisionHistory log2) {
				if(log1.getVersion().compareToIgnoreCase(log2.getVersion()) != 0){
					if(log1.getVersion().trim().isEmpty()){
						return 1;
					}
					if(log2.getVersion().trim().isEmpty()){
						return -1;
					}
					String[] v1 = log1.getVersion().split("\\.");
					String[] v2 = log2.getVersion().split("\\.");
					int i = 0;
					while(i < v1.length && i < v2.length){
						if(v1[i].compareToIgnoreCase(v2[i]) != 0){
							try{
								return Integer.valueOf(removeFirst(v2[i])) - Integer.valueOf(removeFirst(v1[i]));
							}catch(Exception e){
								return removeFirst(v1[i]).compareToIgnoreCase(removeFirst(v2[i]));
							}
						}else{
							i++;
						}
					}
					return 1;
				}else{
						return -1;
				}
			}
		});
	}

	protected static String removeFirst(String str){
		for(char c:str.toCharArray()){
			if(c<'0' || c > '9'){
				str = str.substring(1);
			}else{
				break;
			}
		}
		return str;
	}
}
