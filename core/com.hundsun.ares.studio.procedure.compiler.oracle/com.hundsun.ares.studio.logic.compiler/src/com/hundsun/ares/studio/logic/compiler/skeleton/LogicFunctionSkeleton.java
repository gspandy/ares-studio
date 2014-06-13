/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.compiler.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroHandlerFactory;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenFilter;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonInput;
import com.hundsun.ares.studio.engin.token.CodeTokenFactory;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.logic.LogicFunction;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicEngineContextConstant;
import com.hundsun.ares.studio.logic.compiler.macro.service.IAtomServiceMacroTonkenService;
import com.hundsun.ares.studio.logic.compiler.macro.service.ILogicFunctionMacroTokenService;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicFunctionIsTransFunctionBeginToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicFunctionIsTransFunctionEndToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicFunctionPackerReleseToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicFunctionSvrEndToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicFunctionVariableDefineToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicServiceBeginToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicServiceEndToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicServiceObjectReleseToken;
import com.hundsun.ares.studio.logic.compiler.tokens.LogicSubCallWithMFlagToken;
import com.hundsun.ares.studio.usermacro.compiler.handlers.IUserMacroTokenService;

/**
 * @author qinyuan
 *
 */
public class LogicFunctionSkeleton extends LogicBusinessSkeleton {

	/**
	 * @param resource
	 * @param context
	 */
	public LogicFunctionSkeleton(IARESResource resource,
			Map<Object, Object> context) {
		super(resource, context);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#getSkeletonToken()
	 */
	@Override
	public Iterator<ICodeToken> getSkeletonToken() throws Exception {
		
		List<ICodeToken> tList = new ArrayList<ICodeToken>();
		
		LogicFunction logicFuc = (LogicFunction)getRuntimeContext().get(ILogicEngineContextConstant.ResourceModel);
		
		//���token����
		tList.add(new LogicServiceBeginToken());//��������
		// ��������
		tList.add(new LogicFunctionVariableDefineToken());
		//�Ӻ��������������<M>��־�������⴦��
		tList.add(new LogicSubCallWithMFlagToken());
		//LS�������֤���
//		tList.add(new LogicServiceCheckLicenseToken());
		
		//LFΪ����������ʼ
		tList.add(new LogicFunctionIsTransFunctionBeginToken());
		//α����
		tList.add(CodeTokenFactory.getInstance().createPseudoCodeToken(logicFuc.getPseudoCode()));
		//LFΪ�������������
		tList.add(new LogicFunctionIsTransFunctionEndToken());
		//Svr_end����
		tList.add(new LogicFunctionSvrEndToken());
		//������ͷŴ���
		tList.add(new LogicFunctionPackerReleseToken());
		//�ͷ�ҵ�����
		tList.add(new LogicServiceObjectReleseToken());
		//������������returnCode
		tList.add(new LogicServiceEndToken());
		
		return tList.iterator();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#getMacroTokenHandler(com.hundsun.ares.studio.engin.token.macro.IMacroToken, java.util.Map)
	 */
	@Override
	public IMacroTokenHandler getMacroTokenHandler(IMacroToken macro,
			Map<Object, Object> context) throws Exception {
		IMacroTokenHandler  handler = null;//MacroHandlerFactory.getInstance().create(macro.getKeyword());
		
		int macroType = -1;//Ĭ��Ϊδƥ��ĺ�
		//�����괦��
		if(context.containsKey(ILogicEngineContextConstant.Logic_Function_Macro_Service)){
			ILogicFunctionMacroTokenService fmservice = (ILogicFunctionMacroTokenService)context.get(ILogicEngineContextConstant.Logic_Function_Macro_Service);
			if(fmservice.isLogicFunction(macro.getKeyword())){
				macroType = 1;//ԭ�Ӻ�������
				handler = fmservice.getHandler(macro.getKeyword());
			}
		}
		
		//ԭ�ӷ���괦��
		if(context.containsKey(ILogicEngineContextConstant.Atom_Service_Macro_Service)){
			IAtomServiceMacroTonkenService prcservice = (IAtomServiceMacroTonkenService)context.get(ILogicEngineContextConstant.Atom_Service_Macro_Service);
			if(prcservice.isAtomService(macro.getKeyword())){
				macroType = 2;//ԭ�ӷ������
				handler = prcservice.getHandler(macro.getKeyword());
			}
		}
		
		//�û��Զ���괦��
		if(macroType < 0 && context.containsKey(ILogicEngineContextConstant.UserMacro_Service)){
			IUserMacroTokenService fmservice = (IUserMacroTokenService)context.get(ILogicEngineContextConstant.UserMacro_Service);
			if(fmservice.isUserMacro(macro.getKeyword())){
				macroType = 3;
				List<String> params = new ArrayList<String>();
				{
					IARESProject project = (IARESProject)context.get(IAtomEngineContextConstant.Aresproject);
					LogicFunction lf = (LogicFunction) context.get(ILogicEngineContextConstant.ResourceModel);
					List<Parameter> inputParameters = new ArrayList<Parameter>();
					ParamGroupUtil.parserParameters(lf.getInputParameters(), inputParameters, project);
					for(Parameter input : inputParameters){
						params.add(input.getId());
					}
					List<Parameter> outputParameters = new ArrayList<Parameter>();
					ParamGroupUtil.parserParameters(lf.getOutputParameters(), outputParameters, project);
					for (Parameter ouput : outputParameters) {
						params.add(ouput.getId());
					}
				}
				ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(ILogicEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
				Set<String> rsIdSet = helper.getAttribute(ILogicEngineContextConstant.ATTR_GETLAST_RESULTSET);
				String lastId = "";
				if(rsIdSet.size() > 0){
					lastId = (String)rsIdSet.toArray()[rsIdSet.size() - 1];
				}
				
				Map<String,Object> paramsMap= new HashMap<String,Object>();
				paramsMap.put("dataRealType", new HashMap<String, String>());
				paramsMap.put("lastCur", "");
				paramsMap.put("lastResId", lastId);
				paramsMap.put("inoutParams", params);
				handler = fmservice.getUserMacroHandler(macro.getKeyword(),context ,paramsMap);
			}
		}
		
		//�ڲ��괦�� 
		if(macroType < 0 ){
			IMacroTokenHandler tempHandler = MacroHandlerFactory.getInstance().create(macro.getKeyword());//MacroTokenHandlerManager.getInstance().getHandler(macro.getKeyword());
			if(null != tempHandler){
				macroType = 4;
				handler = tempHandler;
			}
		}
		/*����������ȡ��ISkeletonInput�����������String generate(ISkeletonInput input,Queue<IARESProblem> msgQueue,
		  int level,Map<Object, Object> context)ʱ����*/
		ISkeletonInput input = (ISkeletonInput)context.get(IEngineContextConstant.SKELETON_INPUT);
		IMacroTokenFilter filter = (IMacroTokenFilter)context.get(ILogicEngineContextConstant.MACRO_FILTER);
		
		switch (macroType) {
		case -1:
			throw new Exception(String.format("û���ҵ���[%s]��صĴ����ࡣ��ȷ�Ϻ��ı���д��ȷ��", macro.getKeyword()));
		case 1:   //����
			break;
		case 2:   //�洢����
			break;
		case 3:   //�Զ����
			if(!filter.filte(input, macro)){
				throw new Exception(String.format("���������ú�[%s]��صĴ����ࡣ��ȷ���û��Զ���귶Χ�Ƿ���ȷ��", macro.getKeyword()));
			}
			break;
		case 4:  //ϵͳ��
			if(!filter.filte(input, macro)){
				throw new Exception(String.format("���������ú�[%s]��صĴ����ࡣ��ȷ��ϵͳ���ú귶Χ�Ƿ���ȷ��", macro.getKeyword()));
			}
			break;
		default:
			break;
		}
		
		if(null != handler){
			return handler;
		}else{
			throw new Exception(String.format("û���ҵ���[%s]��صĴ����ࡣ��ȷ�Ϻ��ı���д��ȷ��", macro.getKeyword()));
		}
	}
}
