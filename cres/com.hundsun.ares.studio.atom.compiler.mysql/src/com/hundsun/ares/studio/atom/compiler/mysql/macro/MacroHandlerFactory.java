package com.hundsun.ares.studio.atom.compiler.mysql.macro;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.CommonSelectStatementMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.ErrorMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.FuncResultObjectReturnMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.FunctionResultSetGetValueHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.InsertTableInProcBlockMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.NestObjectResultsetMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.NestPackAddFieldMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.NestPackAddValueMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCBlockEndMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCCommitHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCGetRecordBeginMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCGetRecordEndMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCInserTableMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCResultSetReturnMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCResultSetStatementMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PROCStatementMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PackAddFieldByHandworkMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PackAddValueByHandworkMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.PackerDefineMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.ProcBlockBeginMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.ResultSetObjectReturnMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.ResultSetObjectSetValueMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.SelectInsertTableInProcBlockMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.UnPackerDefineMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.UnpackAddValueByHandworkMacroHandler;
import com.hundsun.ares.studio.atom.compiler.mysql.macro.handlers.UnpackerInitHandler;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandler;
import com.hundsun.ares.studio.engin.macrohandler.IMacroTokenHandlerFactory;

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
		handlerSet.add(new UnpackAddValueByHandworkMacroHandler());//�ֹ������
		handlerSet.add(new PackAddFieldByHandworkMacroHandler());//�ֹ����ͷ
		handlerSet.add(new PackAddValueByHandworkMacroHandler());//�ֹ������
		handlerSet.add(new NestObjectResultsetMacroHandler());//��ȡǶ�׶�������
		handlerSet.add(new NestPackAddFieldMacroHandler());//Ƕ�׽�����ֹ����ͷ
		handlerSet.add(new NestPackAddValueMacroHandler());//Ƕ�׽�����ֹ������
		handlerSet.add(new FuncResultObjectReturnMacroHandler());//������������󷵻�
		handlerSet.add(new FunctionResultSetGetValueHandler());//������������󷵻�
		handlerSet.add(new PackerDefineMacroHandler());//���������
		handlerSet.add(new UnPackerDefineMacroHandler());//���������
		
		//ԭ�Ӳ���ã�����Ҫ�������ϵͳ��
		if(isAtomCalled) {
			handlerSet.add(new ErrorMacroHandler());
			handlerSet.add(new PROCStatementMacroHandler());//Proc���
			handlerSet.add(new InsertTableInProcBlockMacroHandler());//������¼
			handlerSet.add(new SelectInsertTableInProcBlockMacroHandler());//select������¼
			handlerSet.add(new CommonSelectStatementMacroHandler());//ͨ��select
			handlerSet.add(new ProcBlockBeginMacroHandler());//proc���鿪ʼ
			handlerSet.add(new PROCBlockEndMacroHandler());//proc�������
			handlerSet.add(new PROCResultSetStatementMacroHandler());//PRO*C��������
			handlerSet.add(new PROCResultSetReturnMacroHandler());//PRO*C���������
			handlerSet.add(new PROCGetRecordBeginMacroHandler());//PRO*C��¼��ȡ��ʼ
			handlerSet.add(new PROCGetRecordEndMacroHandler());//PRO*C��¼��ȡ����
			handlerSet.add(new PROCInserTableMacroHandler());//PRO*C������¼
			handlerSet.add(new PROCCommitHandler());//PRO*C�����ύ
			handlerSet.add(new ResultSetObjectReturnMacroHandler());//��������󷵻�
			handlerSet.add(new ResultSetObjectSetValueMacroHandler());//���������ֵ
			handlerSet.add(new UnpackerInitHandler());//����������ʼ��
			
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
