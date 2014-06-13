package com.hundsun.ares.studio.engin.logic;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.ARESProblem;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.IPseudocodeParser;
import com.hundsun.ares.studio.engin.skeleton.ISkeleton;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonFactory;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonInput;
import com.hundsun.ares.studio.engin.skeleton.SkeletonFactoryManager;
import com.hundsun.ares.studio.engin.token.DefaultTokenLisetenerManager;
import com.hundsun.ares.studio.engin.token.IBranchNode;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.ITokenListenerManager;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;

public class ResourceEngin {

	private static final int VALIDATE_LEVEL = 0;  
	
	private static final int GENERATE_LEVEL = 1;
	
	private static final int FORMATE_LEVEL = 2;
	
	public static ResourceEngin instance = new ResourceEngin();
	
	/**
	 * ���ɴ���
	 * @param input
	 * 			����ģ��
	 * @param context
	 * 			������
	 * @param msgQueue
	 *     		 ��Ϣ���У����ڷ��ش�����Ϣ����ģ���������ʱ��Ҫ
	 * @return
	 * @throws Exception
	 */
	public String generate(ISkeletonInput input,Map<Object, Object> context,Queue<IARESProblem> msgQueue) throws Exception{
		//��Ϣ�б� 
		return generate(input,msgQueue,FORMATE_LEVEL, context);
	}
	
	/**
	 * ���ɴ���
	 * @param input
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public String generate(ISkeletonInput input,Map<Object, Object> context) throws Exception{
		//��Ϣ�б� 
		Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
		return generate(input,msgQueue,FORMATE_LEVEL, context);
	}
	
	/**
	 * ������
	 * @param input
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public IARESProblem[] validate(ISkeletonInput input,Map<Object, Object> context) throws Exception{
		//��Ϣ�б� 
		Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
		generate(input,msgQueue,VALIDATE_LEVEL, context);
		return msgQueue.toArray(new IARESProblem[0]);
	}
	
	/**
	 * ��ȡtoken
	 * @param input
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public String generateWithoutFromat(ISkeletonInput input,Map<Object, Object> context) throws Exception{
		//��Ϣ�б� 
		Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
		return generate(input,msgQueue,GENERATE_LEVEL, context);
	}
	
	
	
	/**
	 * ���ɴ���
	 * @param input
	 * @param msgQueue
	 * @param checkOnly
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private String generate(ISkeletonInput input,
			Queue<IARESProblem> msgQueue,
			int level,
			Map<Object, Object> context) throws Exception{
		if(!context.containsKey(IEngineContextConstant.ENGINE_EXCEPTION)){
			context.put(IEngineContextConstant.ENGINE_EXCEPTION,new HashMap<ISkeletonInput,  Queue<IARESProblem>>());
		}
		//������׼��
		List<ICodeToken> queue = new ArrayList<ICodeToken>();
		context.put(IEngineContextConstant.TOKEN_LIST, queue);
		
		//���ʱ���ַ���
		if(!context.containsKey(IEngineContextConstant.DATE_STR)){
			Format f = new SimpleDateFormat("yyyyMMDD HH:mm:ss");
			String value = f.format(new Date());
			context.put(IEngineContextConstant.DATE_STR, value);
		}
		if(!context.containsKey(IEngineContextConstant.USER_NAME)){
			context.put(IEngineContextConstant.USER_NAME, " ");
		}
		
		//��ͼ��token����
		ISkeletonFactory SkeletonFactory = SkeletonFactoryManager.getInstance().getSkeletonFactory(input.getType());
		ISkeleton skeleton = SkeletonFactory.createSkeleton(input.getInput(), context);
		if(null == skeleton){
			return "";
		}
		
		//����ʱ������
		Map<Object, Object> runtimeConext = skeleton.getRuntimeContext();
		//��ȫ������������Ϣ�����뵽����ʱ����ͳһ��BussinessSkeleton�Ĺ��캯���г�ʼ��
		//runtimeConext.putAll(context);
		
		//��ӹǼ������ĵ�����
		runtimeConext.put(IEngineContextConstant.SKELETON_INPUT, input);
		
		//��Ӽ���������
		if(!runtimeConext.containsKey(IEngineContextConstant.TOKEN_LISTENER_MANAGER)){
			runtimeConext.put(IEngineContextConstant.TOKEN_LISTENER_MANAGER, new DefaultTokenLisetenerManager());
		}
		ITokenListenerManager  manager = (ITokenListenerManager)runtimeConext.get(IEngineContextConstant.TOKEN_LISTENER_MANAGER);

		//���������Ϣ������
		EnginMessageListener listener = new EnginMessageListener();
		manager.addListener(listener);
		
		Iterator<ICodeToken> tokenIterator = skeleton.getSkeletonToken();
		//����token�б�
		handleToken(skeleton,input,tokenIterator, queue, runtimeConext,msgQueue);
		
		//��ͼ����,���ڴ�������token�������һЩ���
		Object[] message = skeleton.postValidate();
		if(message.length > 0){
			IARESProblem problem = getTokenError(input,null,message[0].toString());
			msgQueue.add(problem);
			((Map)(context.get(IEngineContextConstant.ENGINE_EXCEPTION))).put(input, msgQueue);
			return "//" + message[0].toString();
		}
		
		//��������Ϣ:Ҫ��������ͼ��չʾ�ľ���
		for(IARESProblem item: listener.getARESProblem()){
			addMessageAttr(item, input, null);
			msgQueue.add(item);
		}
		listener.clear();
		
		if( level  <= VALIDATE_LEVEL){  //�������
			return "";
		}
		
//////////////////////���ɴ���/////////////////////////////////////////////////////
		StringBuffer buffer = new StringBuffer();
		runtimeConext.put(IEngineContextConstant.BUFFER, buffer);
		ICodeToken token1 =null;
		boolean isAddResourceInfo = false;
		for(ICodeToken ttoken:queue){
			try{
				token1 = ttoken;
				buffer.append(ttoken.genCode(runtimeConext));
			}catch(Exception e){
				if(!isAddResourceInfo){
					if(runtimeConext.get(IEngineContextConstant.CURR_RESOURCE)!=null){
						IARESResource resource = (IARESResource) runtimeConext.get(IEngineContextConstant.CURR_RESOURCE);

						IARESProblem problem = ARESProblem.createError();
						problem.setMessage("������Դ:"+resource.getFullyQualifiedName()+"\r\n");
						msgQueue.add(problem);
						isAddResourceInfo= true;
					}
				}
				msgQueue.add(getTokenError(input,ttoken, e.getMessage()));
			}
		}
		
		/*���Ӻ����߼���ͳһ������@����ת��Ϊp,v������<>�������ȵ�ֵ����*/
		try{
			skeleton.onFinish(runtimeConext);
		}catch(Exception e){
			if(!isAddResourceInfo){
				if(runtimeConext.get(IEngineContextConstant.CURR_RESOURCE)!=null){
					IARESResource resource = (IARESResource) runtimeConext.get(IEngineContextConstant.CURR_RESOURCE);

					IARESProblem problem = ARESProblem.createError();
					problem.setMessage("������Դ:"+resource.getFullyQualifiedName()+"\r\n");
					msgQueue.add(problem);
					isAddResourceInfo= true;
				}
			}
		}
		
		
		((Map)(context.get(IEngineContextConstant.ENGINE_EXCEPTION))).put(input, msgQueue);
		
		//��������Ϣ:����ľ��治����������ͼ�г���
		for(IARESProblem item: listener.getARESProblem()){
			addMessageAttr(item, input, null);
			msgQueue.add(item);
		}
		
		if( level  <= GENERATE_LEVEL){  //��������
			return buffer.toString();
		}
		
		StringBuffer realBuffer = new StringBuffer();
		//�ڴ���ͷע�ʹ�����Ϣ
		if(msgQueue.size() > 0){
			realBuffer.append("/***" + "\r\n");
			if(!isAddResourceInfo){
				if(runtimeConext.get(IEngineContextConstant.CURR_RESOURCE)!=null){
					IARESResource resource = (IARESResource) runtimeConext.get(IEngineContextConstant.CURR_RESOURCE);
					realBuffer.append("������Դ:"+resource.getFullyQualifiedName()+"\r\n");
					isAddResourceInfo= true;
				}
			}
			for(IARESProblem pItem: msgQueue){
				realBuffer.append(getCodeErrorMessage(pItem));
				realBuffer.append("\r\n");
			}
			realBuffer.append("***/" + "\r\n");
		}
		
		realBuffer.append(buffer);
		
		//�Ƿ�����Դ�������ɺ���Ҫ���ظ�ȫ�������ĵ�����
		if(runtimeConext.containsKey(IEngineContextConstant.RETURN_CONTANT_TO_GLOBAL_CONTEXT)){
			context.put(IEngineContextConstant.RETURN_CONTANT_TO_GLOBAL_CONTEXT, runtimeConext.get(IEngineContextConstant.RETURN_CONTANT_TO_GLOBAL_CONTEXT));
		}
		
		return realBuffer.toString();
//		//�����ʽ��
//		if(UFTEngineTraceFlagHelper.getDebugTraceFlag()){
//			return realBuffer.toString();
//		}else{
//			if(skeleton.IsNeedFormat()){
//				return CodeFormater.formatC(realBuffer).toString();
//			}
//			return realBuffer.toString();
//		}
	}
	

	
	/**
	 * ������
	 * @param tokenIterator
	 * @param buffer
	 * @param context
	 * @param map 
	 * @throws Exception
	 */
	private void handleToken(
			ISkeleton skeleton,
			ISkeletonInput input,
			Iterator<ICodeToken> tokenIterator,
			List<ICodeToken> queue,
			Map<Object, Object> context, 
			Queue<IARESProblem> msgQueue){
		if(tokenIterator == null)
		{
			return ;
		}
		boolean isAddResourceInfo = false;
		while (tokenIterator.hasNext()) {
			ICodeToken ttoken = (ICodeToken) tokenIterator.next();
			try{
				switch (ttoken.getType()) {
				case ICodeToken.CODE_TEXT:
					ICodeToken rtoken = skeleton.handleTextToken(ttoken, context);
					queue.add(rtoken);
					continue;
				case ICodeToken.COMMENT:
					queue.add(ttoken);
					continue;
				case ICodeToken.STRING:
					queue.add(ttoken);
					continue;
				case ICodeToken.PseudoCode:
					IPseudocodeParser parser = skeleton.getPseudocodeParser(ttoken, context);
					Iterator<ICodeToken> pIterator = parser.parse(StringUtils.defaultIfBlank(ttoken.getContent(), StringUtils.EMPTY), skeleton.getCommentType(),context);
					handleToken(skeleton,input,pIterator, queue, context,msgQueue);
					continue;
				case ICodeToken.MACRO:
					IMacroToken macro = (IMacroToken)ttoken;
					IMacroTokenHandler handler = skeleton.getMacroTokenHandler(macro, context);
					//���������
					Iterator<ICodeToken> tIterator = handler.handle(macro, context);
					handleToken(skeleton,input,tIterator, queue, context,msgQueue);
					continue;
				default:
					break;
				}
			}catch (Exception e) {
				e.printStackTrace();
				
				//System.out.println(token1.getContent());
				if(!isAddResourceInfo){
					if(context.get(IEngineContextConstant.CURR_RESOURCE)!=null){
						IARESResource resource = (IARESResource) context.get(IEngineContextConstant.CURR_RESOURCE);

						IARESProblem problem = ARESProblem.createError();
						problem.setMessage("������Դ:"+resource.getFullyQualifiedName());
						msgQueue.add(problem);
						isAddResourceInfo= true;
					}
				}
				msgQueue.add(getTokenError(input,ttoken,e.getMessage()));
			}
		}
	}
	
//	/**
//	 * ���˺�token
//	 * @param input
//	 * @param token
//	 * @param context
//	 * @throws Exception
//	 */
//	private void filteMacro(ISkeletonInput input,IMacroToken token,Map<Object, Object> context) throws Exception{
//		if(context.containsKey(IEngineContextConstant.MACRO_FILTER)){
//			IMacroTokenFilter filter = (IMacroTokenFilter)context.get(IEngineContextConstant.MACRO_FILTER);
//			if(!filter.filte(input, token)){
//				throw new Exception(String.format("��[%s]���������ڵ�ǰ��Χ����ȷ��ϵͳ���ú��ϵͳ�û���������Ƿ���ȷ��", token.getKeyword()));
//			}
//		}
//	}
	
	
	/**
	 * ����token�Ĵ������
	 * @param input
	 * @param token
	 * @param message
	 * @return
	 */
	private IARESProblem getTokenError(ISkeletonInput input,ICodeToken token,String message){
		IARESProblem problem = ARESProblem.createError();
		problem.setMessage(message);
		addMessageAttr(problem,input,token);
		return problem;
	}
	
	
	/**
	 * ������Ե����������
	 * @param problem
	 * @param input
	 * @param token
	 */
	private void addMessageAttr(IARESProblem problem,ISkeletonInput input,ICodeToken token){
		problem.setAttribute(IEngineContextConstant.MSG_ATTR_OWNER, token);
		if(token instanceof IMacroToken){
			problem.setAttribute("location", ((IMacroToken)token).getLineNo());
		}else{
			problem.setAttribute("location", 1);
		}
		problem.setAttribute(IEngineContextConstant.MSG_ATTR_INPUT, input);
	}
	
	/**
	 * ��ȡ������Ϣ
	 * @param problem
	 * @return
	 */
	public String getCodeErrorMessage(IARESProblem problem){
		String msgType = "����";
		if(!problem.isError()){
			msgType = "����";
		}
		
		ICodeToken token = (ICodeToken)problem.getAttribute(IEngineContextConstant.MSG_ATTR_OWNER);
		IMacroToken macroToken = null;
		if(token instanceof IMacroToken){
			macroToken = (IMacroToken)token;
		}
		
		if(token instanceof IBranchNode ){
			if(((IBranchNode)token).getOwner() instanceof IMacroToken){
				macroToken = (IMacroToken)((IBranchNode)token).getOwner();
			}
		}
		if(null != macroToken){
			return String.format("%s��%d�� [%s] ��ϸ��Ϣ:%s ",msgType, macroToken.getLineNo(),macroToken.getKeyword(),problem.getMessage());
		}
		if(token!=null){
			return String.format("%s������[%s] ��ϸ��Ϣ:%s \r\n",msgType,token.getClass().getSimpleName(),problem.getMessage());
		}
		return String.format("%s�� ��ϸ��Ϣ:%s \r\n",msgType,problem.getMessage());
	}
}
