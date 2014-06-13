/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model.converter;

import com.hundsun.ares.studio.core.IARESResource;

/**
 * 
 * @author gongyf
 */
public interface IModelConverter2 extends IModelConverter {
	
	/**
	 * ��ȡ��Դ���ݣ�Ӧ�÷���ע���info-class���͵Ķ���
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public Object read(IARESResource resource) throws Exception;

	
	/**
	 * д����Դ���ݣ������info��һ����info-class���͵Ķ���
	 * @param resource
	 * @param info
	 * @throws Exception
	 */
	public byte[] write(IARESResource resource, Object info) throws Exception;
}
