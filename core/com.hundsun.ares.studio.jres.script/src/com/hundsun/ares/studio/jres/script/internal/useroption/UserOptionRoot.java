/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.internal.useroption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lvgao
 *
 */
public class UserOptionRoot  implements IControlContainer,IUserOption{
	
	Map<String, Object> option = new HashMap<String, Object>();
	
	List<IControl> cList = new ArrayList<IControl>();
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.internal.useroption.IControlContainer#addChildren(com.hundsun.ares.studio.jres.script.internal.useroption.IControl)
	 */
	@Override
	public void addChildren(IControl control) {
		cList.add(control);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.internal.useroption.IControlContainer#getChildren()
	 */
	@Override
	public IControl[] getChildren() {
		return cList.toArray(new IControl[0]);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.internal.useroption.IUserOption#setOptionValue(java.util.Map)
	 */
	@Override
	public void setOptionValue(Map<String, Object> option) {
		for(IControl item:cList){
			item.setOptionValue(option);
		}
	}
	
	
	/**
	 * ����û�ѡ��
	 */
	public void setOptionValue(){
		setOptionValue(option);
	}
	
	/**
	 * ����Ĭ��ֵ(�޽���ʱʹ��)
	 */
	public void setDefaultValue(){
		for(IControl item:cList){
			setChildDefaultValue(item);
		}
	}
	
	private void setChildDefaultValue(IControl item){
		option.put(item.getID(), item.getDefaultValue());
		if(item instanceof IControlContainer ){
			IControl[] children = ((IControlContainer)item).getChildren();
			for(IControl child:children){
				option.put(child.getID(), child.getDefaultValue());
			}
		}
		
	}
	
	/**
	 * ��ȡ�û�ѡ��
	 * @param openDlg
	 * @return
	 */
	public Map<String, Object> getOptionValue(){
		return option;
	}

}
