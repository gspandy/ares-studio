package com.hundsun.ares.studio.procedure.compiler.oracle.macro;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandlerFactory;
import com.hundsun.ares.studio.procedure.compiler.oracle.macro.handlers.InsertTableInProcBlockMacroHandler;
import com.hundsun.ares.studio.procedure.compiler.oracle.macro.handlers.SelectInsertTableInProcBlockMacroHandler;

public class MacroHandlerFactory implements IMacroTokenHandlerFactory{

	private  Map<String, IMacroTokenHandler> macroMap = new ConcurrentHashMap<String, IMacroTokenHandler>();
	private  Set<IMacroTokenHandler> handlerSet = new HashSet<IMacroTokenHandler>();
	
	/**
	 * �Ƿ�Ϊԭ�ӵ���
	 */
	private boolean isAtomCalled = true;

	/**
	 * @param isAtomCalled the isAtomCalled to set
	 */
	public void setAtomCalled(boolean isAtomCalled) {
		this.isAtomCalled = isAtomCalled;
	}



	private static MacroHandlerFactory instance;
	
	public static MacroHandlerFactory getInstance(){
		if(null == instance){
			instance = new MacroHandlerFactory();
		}
		return instance;
	}
	
	
	
	private MacroHandlerFactory(){
		init();
	}
	
	private void init(){
		
		
		//Ĭ��ֻ����ֹ����
//		handlerSet.add(new PackAddFieldByHandworkMacroHandler());//�ֹ����ͷ
//		handlerSet.add(new PackAddValueByHandworkMacroHandler());//�ֹ������
		
		//ԭ�Ӳ���ã�����Ҫ�������ϵͳ��
		if(isAtomCalled) {
//			handlerSet.add(new ErrorMacroHandler());
//			handlerSet.add(new PROCStatementMacroHandler());//Proc���
			handlerSet.add(new InsertTableInProcBlockMacroHandler());//������¼
			handlerSet.add(new SelectInsertTableInProcBlockMacroHandler());//select������¼
//			handlerSet.add(new CommonSelectStatementMacroHandler());//ͨ��select
//			handlerSet.add(new ProcBlockBeginMacroHandler());//proc���鿪ʼ
//			handlerSet.add(new PROCBlockEndMacroHandler());//proc�������
//			handlerSet.add(new PROCResultSetStatementMacroHandler());//PRO*C��������
//			handlerSet.add(new PROCResultSetReturnMacroHandler());//PRO*C���������
//			handlerSet.add(new PROCGetRecordBeginMacroHandler());//PRO*C��¼��ȡ��ʼ
//			handlerSet.add(new PROCGetRecordEndMacroHandler());//PRO*C��¼��ȡ����
//			handlerSet.add(new PROCInserTableMacroHandler());//PRO*C������¼
		}
		
		for(IMacroTokenHandler handler:handlerSet){
			macroMap.put(handler.getKey(), handler);
		}
	}
	
	@Override
	public boolean canHandle(String key) {
		return macroMap.containsKey(key.toUpperCase());
	}

	@Override
	public IMacroTokenHandler create(String key) {
		if(macroMap.containsKey(key.toUpperCase())){
			return macroMap.get(key.toUpperCase());
		}
		return null;
	}
	
	

	@Override
	public Set<IMacroTokenHandler> getHandledMacros() {
		return handlerSet;
	}
}
