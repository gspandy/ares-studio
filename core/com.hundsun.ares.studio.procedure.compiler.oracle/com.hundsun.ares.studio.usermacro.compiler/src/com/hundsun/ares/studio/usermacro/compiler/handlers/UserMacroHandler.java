package com.hundsun.ares.studio.usermacro.compiler.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.constant.ITokenConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.CodeParserUtil;
import com.hundsun.ares.studio.engin.parser.PseudoCodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.token.DefaultTokenEvent;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.IDomainHandler;
import com.hundsun.ares.studio.engin.token.ITokenDomain;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.usermacro.UserMacroItem;
import com.hundsun.ares.studio.usermacro.compiler.contants.IUserMacroEnginConstant;

/**
 * �û��괦��ͬ���û����в�ͬ�ĺ����Լ���ͬ�Ĳ���,�ڵ��õĵط�ͨ��������setParamsMap�������ò�����Ϣ
 * @author liaogc
 *
 */
public class UserMacroHandler implements IMacroTokenHandler{
	
	private static final String SKELETON_ATTRIBUTE_HELPER = "��ͼȫ�����԰�����";
	private static final String ATTR_PROC_VARIABLE_LIST = "PRO*C�����б�";
	private static final String RESOURCE_MODEL = "��Դģ��";
	private Map<String,Object> paramsMap= new HashMap<String,Object>();//���еĲ���map
	boolean inProc = false;  //�Ƿ���proc���鵱��
	
	String key;
	UserMacroItem item;
	public UserMacroHandler(UserMacroItem item){
		this.key = item.getName();
		this.item = item;
	}
	
	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}
	
	public Map<String, Object> getParamsMap(){
		return paramsMap;
	}
	
	@Override
	public String getKey() {
		return key;
	}

	@Override
	public Iterator<ICodeToken> handle(IMacroToken token,
			Map<Object, Object> context) throws Exception {
		
		IDomainHandler handler = (IDomainHandler)context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain domain = handler.getDomain("PRO*C���鿪ʼ");
		if(null == domain){//�ж��Ƿ���pro*c������
			//���Ϊ������Դ��Ҳ˵����porc����
			Object resource = context.get(IEngineContextConstant.CURR_RESOURCE);
			if(null != resource && resource instanceof IARESResource){
				IARESResource res = (IARESResource)resource;
				if(StringUtils.equalsIgnoreCase(res.getType(), "procedure")) {
					inProc = true;
				}
			}
		}else{
			inProc = true;
		}
		if(token.getKeyword().equals(IUserMacroEnginConstant.TRAN_BLOCK_BEGIN_MACRONAME)){//�������ʼ��
			addDomain(context, IUserMacroEnginConstant.TRAN_BLOCK_BEGIN_MACRONAME);
		}
		if(token.getKeyword().equals(IUserMacroEnginConstant.TRAN_BLOCK_END_MACRONAME)){
			IDomainHandler handler2 = (IDomainHandler)context.get(IEngineContextConstant.DOMAIN_HANDLER);
			ITokenDomain domain2 = handler.getDomain(IUserMacroEnginConstant.TRAN_BLOCK_BEGIN_MACRONAME);
			if(domain2==null){
				fireEvent(context);
			}else{
				removeDomain(context, IUserMacroEnginConstant.TRAN_BLOCK_BEGIN_MACRONAME);//ɾ������ʼ��
			}
			
		}
		//�����proc*c���м��뵽pro*c�����б���
		if(inProc){
			ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(SKELETON_ATTRIBUTE_HELPER);
			for (String param : token.getParameters()) {
				if (StringUtils.startsWith(param, "@") && StringUtils.indexOf(param, ",")<0) {//��ֹ������[@operator_no,@trans_name]�����
					param = StringUtils.replaceOnce(param, "@", "");
					helper.addAttribute("PRO*C�����б�", param);
				}else if(StringUtils.startsWith(param, "@") && StringUtils.indexOf(param, ",")>-1){//����[@operator_no,@trans_name]
					String[] procParams = StringUtils.split(param,",");
					for(String procParam:procParams){
						procParam = StringUtils.trim(procParam);
						if(StringUtils.startsWith(procParam, "@")){
							procParam = StringUtils.replaceOnce(procParam, "@", "");
						}
						helper.addAttribute("PRO*C�����б�", procParam);
					}
				}
			}
		}
		
		//���û����еĴ���@���ı������뵽PRO*C�����б�
		IUserMacroTokenService service = (IUserMacroTokenService)context.get(IUserMacroEnginConstant.UserMacro_Service);
		UserMacroItem userMacroItem = service.getUserMacro(token.getKeyword());
		Pattern p = Pattern.compile("@[\\w\\d_]+");
		//sql����ڵ�һ������()
		Matcher m = p.matcher(userMacroItem.getContent());
		ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(SKELETON_ATTRIBUTE_HELPER);
		while (m.find()) {
			String varName = m.group().substring(1);
			helper.addAttribute(ATTR_PROC_VARIABLE_LIST, varName);
			//�ھ����ʹ�õط����ж��Ƿ�Ϊ������Դ
			helper.addAttribute(IUserMacroEnginConstant.USER_MACRO_OBJ, varName);
		}
		
		List<ICodeToken> tList = new ArrayList<ICodeToken>();
		
		Map<String,String> realDataType = (Map<String, String>) paramsMap.get("dataRealType");//��������
		String curId = "";
		
		if (userMacroItem.getContent().indexOf("<T>") > -1) {
			Set<String> lastCurs = (Set<String>)getParamsMap().get("lastCur");
			Set<String> lastCurIds = helper.getAttribute(IEngineContextConstant.LASTEST_CUR_ID);
			//ȡ
			if (lastCurIds.size() == 0 && lastCurs.size() > 0) {
				curId = lastCurs.toArray(new String[0])[lastCurs.size()-1];
			}else if (lastCurIds.size() > 0){
				curId = lastCurIds.toArray(new String[0])[lastCurIds.size()-1];
			}
			//��
			List<String> curs = new ArrayList<String>();
			curs.addAll(lastCurs);
			int index = curs.indexOf(curId);
			if (index > 0 && curs.size() > index-1) {
				helper.getAttribute(IEngineContextConstant.LASTEST_CUR_ID).clear();
				helper.addAttribute(IEngineContextConstant.LASTEST_CUR_ID, curs.get(index-1));
			}
		}
		String lastResId = (String) paramsMap.get("lastResId");
		List<String> inoutParams = (List<String>) paramsMap.get("inoutParams");//�������������
		
		//2013��11��28��17:17:34 �û������ȥ��@���ţ�
		//����UFTģʽ��UFT��Щ�û���Ĳ�����@���ţ�����ʱҲ��@���ţ����³�ͻ 
		//����UFT�С���ȡ��¼��
		List<String> paraList = new ArrayList<String>();
		for(String item:token.getParameters()){
			//2014��3��28��09:51:57 �˴����滻���ŵ��û���������������滻com.hundsun.ares.studio.usermacro.compiler.handlers.UserMacroUtil.genCode(String, List<String>, String[])
			//�û����в��������ʱ��û�д�@���ţ��û���ʹ�õ�ʱ���ִ���@���ţ������������ɴ���ǰ׺��ʧ
//			paraList.add(StringUtils.replaceOnce(item, "@", ""));
			paraList.add(item);
		}
		
		tList.add(new UserMacroToken(this, userMacroItem, realDataType, context, curId,lastResId ,paraList.toArray(new String[0]) ,inoutParams));
		
		return tList.iterator();
	}
	
	public boolean isInProc(){
		return inProc;
	}
	
	/**
	 * �������
	 * @param context
	 * @param key
	 */
	private void addDomain(Map<Object, Object> context,final String key){
		IDomainHandler handler = (IDomainHandler)context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain domain = handler.getDomain(key);
		if(domain == null){
			handler.addDomain(new ITokenDomain() {
				
				@Override
				public String getType() {
					return null;
				}
				@Override
				public String getKey() {
					return key;
				}
				@Override
				public Object[] getArgs() {
					return null;
				}
			});
		}
	}
	
	/**
	 * ɾ����
	 * @param context
	 * @param key
	 */
	private void removeDomain(Map<Object, Object> context,final String key){
		IDomainHandler handler = (IDomainHandler)context.get(IEngineContextConstant.DOMAIN_HANDLER);
		ITokenDomain domain = handler.getDomain(key);
		if(domain != null){
			handler.removeDomain(key);
		}
	}
	/**
	 * ��������������ȱ��������ʼ���¼�
	 */
	private void fireEvent(Map<Object, Object> context){
		ITokenListenerManager  manager = (ITokenListenerManager)context.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);
		String message = String.format("��[%1s]ȱ�ٺ�[%2s]", IUserMacroEnginConstant.TRAN_BLOCK_END_MACRONAME,IUserMacroEnginConstant.TRAN_BLOCK_BEGIN_MACRONAME);
		manager.fireEvent(new DefaultTokenEvent(ITokenConstant.EVENT_ENGINE_WARNNING,message));
	}
}



