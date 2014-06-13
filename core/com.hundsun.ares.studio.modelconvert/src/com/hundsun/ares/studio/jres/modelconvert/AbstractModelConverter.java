/**
 * Դ�������ƣ�AbstractModelConverter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.modelconvert
 * ����˵�����ļ���ȡ�ͷ������е�������չʵ��
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.modelconvert;

import java.io.InputStream;
import java.io.OutputStream;

import com.hundsun.ares.studio.core.model.converter.IModelConverter2;

/**
 * ����JRES��Դ���л��ͷ����л�ͳһ����
 * @author gongyf
 *
 */
public abstract class AbstractModelConverter implements IModelConverter2 {
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter2#read(java.io.InputStream)
	 */
	@Override
	final public Object read(InputStream in) throws Exception {
		throw new UnsupportedOperationException("����ʹ��read(IARESResource)");
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter2#read(java.io.InputStream, java.lang.Object)
	 */
	@Override
	final public void read(InputStream in, Object info) throws Exception {
		throw new UnsupportedOperationException("����ʹ��read(IARESResource)");
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter#write(java.io.OutputStream, java.lang.Object)
	 */
	@Override
	final public void write(OutputStream out, Object info) throws Exception {
		throw new UnsupportedOperationException("����ʹ��write(IARESResource, Object)");
	}
}
