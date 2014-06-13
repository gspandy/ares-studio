/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.compiler.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.compiler.constant.IAtomEngineContextConstant;
import com.hundsun.ares.studio.atom.compiler.macro.MacroHandlerFactory;
import com.hundsun.ares.studio.atom.compiler.macro.func.IFunctionMacroTokenService;
import com.hundsun.ares.studio.atom.compiler.macro.procedure.IProcedureMacroTokenService;
import com.hundsun.ares.studio.atom.compiler.skeleton.util.AtomFunctionCompilerUtil;
import com.hundsun.ares.studio.atom.compiler.token.FunctionPackServiceDefineToken;
import com.hundsun.ares.studio.atom.compiler.token.FunctionParamDefineToken;
import com.hundsun.ares.studio.atom.compiler.token.FunctionResultSetDefineToken;
import com.hundsun.ares.studio.atom.compiler.token.LpSPDefineToken;
import com.hundsun.ares.studio.atom.compiler.token.ResultSetDefineToken;
import com.hundsun.ares.studio.atom.compiler.token.function.FunctionBeginToken;
import com.hundsun.ares.studio.atom.compiler.token.function.FunctionDatabaseConnectionBeginToken;
import com.hundsun.ares.studio.atom.compiler.token.function.FunctionDatabaseConnectionEndToken;
import com.hundsun.ares.studio.atom.compiler.token.function.FunctionEndToken;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.engin.constant.IEngineContextConstant;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenFilter;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.parser.IPseudocodeParser;
import com.hundsun.ares.studio.engin.parser.PseudocodeParserFactory;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonAttributeHelper;
import com.hundsun.ares.studio.engin.skeleton.ISkeletonInput;
import com.hundsun.ares.studio.engin.token.CodeTokenFactory;
import com.hundsun.ares.studio.engin.token.ICodeToken;
import com.hundsun.ares.studio.engin.token.macro.IMacroToken;
import com.hundsun.ares.studio.usermacro.compiler.handlers.IUserMacroTokenService;


/**
 * @author lvgao
 *
 */
public class FunctionSkeleton extends BussinessSkeleton{

	/**
	 * @param resource
	 * @param context
	 */
	public FunctionSkeleton(IARESResource resource, Map<Object, Object> context) {
		super(resource, context);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#getSkeletonToken()
	 */
	@Override
	public Iterator<ICodeToken> getSkeletonToken() throws Exception {
		List<ICodeToken> tList = new ArrayList<ICodeToken>();
		
		AtomFunction func = (AtomFunction)getRuntimeContext().get(IAtomEngineContextConstant.ResourceModel);
		tList.add(new FunctionBeginToken());
		tList.add(new FunctionParamDefineToken());
		tList.add(new LpSPDefineToken());
		tList.add(new FunctionPackServiceDefineToken());
		tList.add(new FunctionResultSetDefineToken());
		tList.add(new ResultSetDefineToken());
		tList.add(new FunctionDatabaseConnectionBeginToken());
		tList.add(CodeTokenFactory.getInstance().createPseudoCodeToken(func.getPseudoCode()));
		tList.add(new FunctionDatabaseConnectionEndToken());
		tList.add(new FunctionEndToken());
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
		if(context.containsKey(IAtomEngineContextConstant.Function_Macro_Service)){
			IFunctionMacroTokenService fmservice = (IFunctionMacroTokenService)context.get(IAtomEngineContextConstant.Function_Macro_Service);
			if(fmservice.isAtomFunction(macro.getKeyword())){
				macroType = 1;//ԭ�Ӻ�������
				handler = fmservice.getHandler(macro.getKeyword());
			}
		}
		
		//�洢���̺괦��
		if(context.containsKey(IAtomEngineContextConstant.Procedure_Macro_Service)){
			IProcedureMacroTokenService prcservice = (IProcedureMacroTokenService)context.get(IAtomEngineContextConstant.Procedure_Macro_Service);
			if(prcservice.isProcedure(macro.getKeyword())){
				macroType = 2;//�洢���̵���
				handler = prcservice.getHandler(macro.getKeyword());
			}
		}
		
		//�û��Զ���괦��
		if(macroType < 0 && context.containsKey(IAtomEngineContextConstant.UserMacro_Service)){
			IUserMacroTokenService fmservice = (IUserMacroTokenService)context.get(IAtomEngineContextConstant.UserMacro_Service);
			if(fmservice.isUserMacro(macro.getKeyword())){
				macroType = 3;
				Map<String,String> businessType = new HashMap<String, String>();
				AtomFunction func = (AtomFunction)context.get(IAtomEngineContextConstant.ResourceModel);
				IARESProject project = (IARESProject)context.get(IAtomEngineContextConstant.Aresproject);
				businessType.putAll(AtomFunctionCompilerUtil.getParamterBusinessType2Map(func.getInputParameters(), project));
				businessType.putAll(AtomFunctionCompilerUtil.getParamterBusinessType2Map(func.getOutputParameters(), project));
				List<Parameter> internalVars = new ArrayList<Parameter>();
				internalVars.addAll(func.getInternalVariables());
				businessType.putAll(AtomFunctionCompilerUtil.getParamterBusinessType2Map(internalVars, project));
				//��handlerע���α�
				ISkeletonAttributeHelper helper = (ISkeletonAttributeHelper)context.get(IAtomEngineContextConstant.SKELETON_ATTRIBUTE_HELPER);
				Set<String> cursorList = helper.getAttribute(IAtomEngineContextConstant.ATTR_CURSOR_LIST);
				Set<String> rsIdSet = helper.getAttribute(IAtomEngineContextConstant.ATTR_GETLAST_RESULTSET);
				String lastId = "";
				if(rsIdSet.size() > 0){
					lastId = (String)rsIdSet.toArray()[rsIdSet.size() - 1];
				}
				List<String> params = new ArrayList<String>();
				{
					List<Parameter> inputParameters = new ArrayList<Parameter>();
					ParamGroupUtil.parserParameters(func.getInputParameters(), inputParameters, project);
					for(Parameter input : inputParameters){
						params.add(input.getId());
					}
					List<Parameter> outputParameters = new ArrayList<Parameter>();
					ParamGroupUtil.parserParameters(func.getOutputParameters(), outputParameters, project);
					for (Parameter ouput : outputParameters) {
						params.add(ouput.getId());
					}
				}
				Map<String,Object> paramsMap= new HashMap<String,Object>();
				paramsMap.put("dataRealType", businessType);
				paramsMap.put("lastCur", cursorList);
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
		IMacroTokenFilter filter = (IMacroTokenFilter)context.get(IAtomEngineContextConstant.MACRO_FILTER);
		
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

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.engin.skeleton.ISkeleton#getPseudocodeParser(com.hundsun.ares.studio.engin.token.ICodeToken, java.util.Map)
	 * α�����������
	 */
	@Override
	public IPseudocodeParser getPseudocodeParser(ICodeToken token,
			Map<Object, Object> context) throws Exception {
		return PseudocodeParserFactory.instance.createParser();
	}

	
}
