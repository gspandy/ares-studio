/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model.converter;

import java.io.InputStream;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.Element;

import com.hundsun.ares.studio.core.util.PersistentUtil;

/**
 * ���汾�������л���
 * @author maxh
 */
public abstract class AresBasicModelVersionCheckConverter implements IModelConverter{

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter#read(java.io.InputStream, java.lang.Object)
	 */
	public void read(InputStream in, Object info) throws Exception {
		Element root = readRoot(in);
		if(root != null){
			read(root, info);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter#read(java.io.InputStream)
	 */
	public Object read(InputStream in) throws Exception {
		Element root = readRoot(in);
		if(root != null){
			return read(root);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter#write(java.io.OutputStream, java.lang.Object)
	 */
	public void write(OutputStream out, Object info) throws Exception {
		Document doc = PersistentUtil.createHSDocument(getRealVersion());
		write(doc.getRootElement(), info);
		PersistentUtil.writeDocument(out, doc);
	}
	
	/**
	 * ��ȡroot�ڵ� ��ȡ����������а汾�����ݵ���������׳��쳣
	 * @param in
	 * @return
	 * @throws Exception
	 */
	Element readRoot(InputStream in) throws Exception {
		Element root = PersistentUtil.readRoot(in);
		if(root != null){
			if(!root.getName().equals(PersistentUtil.HS_DOC) || root.attributeValue(PersistentUtil.HS_DOC_VERSION) == null){
				throw new NoDocVersionInfoException();
			}
			String[] docVersions = root.attributeValue(PersistentUtil.HS_DOC_VERSION).split("\\.");
			if(docVersions.length != 4){
				throw new AresDocVersionException(getRealVersion(),root.attributeValue(PersistentUtil.HS_DOC_VERSION));
			}
			docVersions[0] = docVersions[0].replaceAll("v", "").replaceAll("V", "");
			if(!docVersions[0].equals(String.valueOf(getVersionOne())) || 
				!docVersions[1].equals(String.valueOf(getVersionTwo())) || 
				!docVersions[2].equals(String.valueOf(getVersionThree()))){
				throw new AresDocVersionException(getRealVersion(),root.attributeValue(PersistentUtil.HS_DOC_VERSION));
			}
		}
		return root;
	}
	
	/**
	 * ����������ĵ��汾
	 * @return
	 */
	public String getRealVersion(){
		return "V"   + String.valueOf(getVersionOne()) + "."
					 + String.valueOf(getVersionTwo()) + "."
					 + String.valueOf(getVersionThree()) + "."
					 + String.valueOf(getVersionFour());
	}
	
	
	/**
	 * ��ð汾�ŵ�1λ
	 * @return
	 */
	public abstract int getVersionOne();
	/**
	 * ��ð汾�ŵ�2λ
	 * @return
	 */
	public abstract int getVersionTwo();
	/**
	 * ��ð汾�ŵ�3λ
	 * @return
	 */
	public abstract int getVersionThree();
	/**
	 * ��ð汾�ŵ�4λ
	 * @return
	 */
	public abstract int getVersionFour();
	/**
	 * �����root�ڵ���hsdoc�ڵ�
	 * @param root
	 * @param info
	 * @throws Exception
	 */
	public abstract void read(Element root, Object info) throws Exception;
	/**
	 * �����root�ڵ���hsdoc�ڵ�
	 * @param root
	 * @return
	 * @throws Exception
	 */
	public abstract Object read(Element root) throws Exception;
	/**
	 * �����root�ڵ���hsdoc�ڵ�
	 * @param root
	 * @param info
	 * @throws Exception
	 */
	public abstract void write(Element root, Object info) throws Exception;
}
