/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model.converter;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * һ����Դ�Ķ�д��
 * @author maxh
 */
public interface IModelConverter {
	/**
	 * ��in�ж�ȡ��Ϣ�����info
	 * @param in
	 * @param info
	 */
	void read(InputStream in,Object info) throws Exception; 	// �쳣�д���ȶ���������ȷ�ļ����쳣
	/**
	 * ��һ�������in�ж�����
	 * @param in
	 * @return
	 */
	Object read(InputStream in) throws Exception;
	/**
	 * ��һ������д��out��
	 * @param out
	 * @param info
	 */
	void write(OutputStream out,Object info) throws Exception;
}
