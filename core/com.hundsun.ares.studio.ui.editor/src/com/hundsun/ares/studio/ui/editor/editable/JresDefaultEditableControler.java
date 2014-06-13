/**
 * Դ�������ƣ�JresDefaultEditableControler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.editable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.ui.part.FileEditorInput;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.internal.core.ArchiveARESResource;
import com.hundsun.ares.studio.ui.ARESResourceEditorInput;
import com.hundsun.ares.studio.ui.editor.EMFFormEditor;

/**
 * @author lvgao
 *
 */
public class JresDefaultEditableControler implements IEditableControl{

	EMFFormEditor editor;
	boolean is_readonly = false;
	//�û�ֻ��״̬
	Map<String, Object> userStatusMap = new HashMap<String, Object>();
	//ֻ�����Ƶ�Ԫ
	List<IEditableUnit> unitList = new ArrayList<IEditableUnit>();
	
	public JresDefaultEditableControler(EMFFormEditor editor){
		this.editor = editor;
	}
	
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editors.editable.IEditableControl#getResourceReadonlyStatus()
	 */
	@Override
	public boolean getResourceReadonlyStatus() {
		return is_readonly;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editors.editable.IEditableControl#putUserStatus(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putUserStatus(String key, Object status) {
		userStatusMap.put(key, status);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editors.editable.IEditableControl#notifyUserStatus(java.lang.String)
	 */
	@Override
	public void notifyUserStatus(String key) {
		if(!userStatusMap.containsKey(key)){
			return;
		}
		for(IEditableUnit unit:unitList){
			unit.setReadonlyStatus(key,userStatusMap.get(key));
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editors.editable.IEditableControl#addEditableUnit(com.hundsun.ares.studio.jres.ui.editors.editable.IEditableUnit)
	 */
	@Override
	public void addEditableUnit(IEditableUnit unit) {
		if(null != unit){
			unitList.add(unit);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editors.editable.IEditableControl#refreshAllUnit()
	 */
	@Override
	public void refreshAllUnit(Map<Object, Object> context) {
		if(is_readonly){
			//ֻ��״̬�¿���
			for(IEditableUnit item:unitList){
				item.setReadonlyStatus(IEditableUnit.KEY_SYSTEM,IEditableUnit.EDITABLE_FALSE);
			}
		}else{
			//��ֻ��״̬�¿���
			for(IEditableUnit item:unitList){
				item.setReadonlyStatus(IEditableUnit.KEY_SYSTEM,IEditableUnit.EDITABLE_TRUE);
				for(Entry<String, Object> entry:userStatusMap.entrySet()){
					item.setReadonlyStatus(entry.getKey(), entry.getValue());
				}
			}
		}
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.editors.editable.IEditableControl#refreshResourceReadonlyStatus()
	 */
	@Override
	public void refreshResourceReadonlyStatus() {
		/**TODO#ֻ������#����#�е�#��д��#����״̬#������(�������հ��к�ע����)#��ʱ(��ȷ������)#�༭��input��ֻ���ж�
		*1���ļ�ֻ�����ж���ֻ��
		*2��������Դ����Դֻ��
		*3��δע��ֻ��???
		*/
		Object input = editor.getEditorInput();
		File file = new File(((FileEditorInput)input).getURI());
//		RegisterUtil instance = RegisterUtil.instance;
		if(file.exists()){
//			if(instance ==null || !instance.isRegisted()){
//				is_readonly = true;
//			}
			if( !file.canWrite()){
				is_readonly = true;
			}
			else if(input instanceof ARESResourceEditorInput){
				IARESResource resource = ((ARESResourceEditorInput)input).getARESResource();
				if(resource instanceof ArchiveARESResource){
					is_readonly = true;
				}
			}
			else{
				is_readonly = false;
			}
		}
//		is_readonly = false;//editor.isReadOnly();
	}

}
