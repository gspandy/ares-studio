/**
 * Դ�������ƣ�DefaultContextUpdateSource.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

/**
 * @author lvgao
 *
 */
public class DefaultContextUpdateSource implements IContextUpdateSource{
	String type;
	Object[] objs;
	
	public DefaultContextUpdateSource(String type,Object[] objs){
		this.type = type;
		this.objs = objs;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IContextUpdateSource#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IContextUpdateSource#getContent()
	 */
	@Override
	public Object[] getContent() {
		return objs;
	}

}
