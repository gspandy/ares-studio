/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.modelconvert;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * @author gongyf
 *
 */
public abstract class ModelConverterHandle {
	
	private ModelConverterHandle nextHandle;
	
	/**
	 * @param nextHandle the nextHandle to set
	 */
	public void setNextHandle(ModelConverterHandle nextHandle) {
		this.nextHandle = nextHandle;
	}
	
	/**
	 * @return the nextHandle
	 */
	public ModelConverterHandle getNextHandle() {
		return nextHandle;
	}
	
	/**
	 * ��ȡ��Դ�ļ���ģ�Ͷ���
	 * 
	 * @param resource ��Ҫ��ȡ����Դ�����ֽ����������Ѿ���ȡ������Ҫ�ٽ��ж�ȡ
	 * @param contents ��Դ�ļ�������
	 * @param clazz ָ����ȡ����ģ�����ͣ�Ҳ���Ƿ��ص�����
	 * @return
	 * @throws Exception 
	 */
	public abstract Object handleRead(IARESResource resource, byte[] contents, Class<?> clazz) throws Exception;
	
	/**
	 * ������Ҫд�����Դ���ֽ�����
	 * 
	 * @param resource ��Ҫд�����Դ���󣬲���Ҫ����������д�����
	 * @param info ��Ҫд���ģ��
	 * @return
	 * @throws Exception 
	 */
	public abstract byte[] handleWrite(IARESResource resource, Object info) throws Exception;
}
