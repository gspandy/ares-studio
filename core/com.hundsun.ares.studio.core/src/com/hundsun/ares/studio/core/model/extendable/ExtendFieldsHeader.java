/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
/**
 * 
 */
package com.hundsun.ares.studio.core.model.extendable;

import com.hundsun.ares.studio.core.model.ICreateInstance;

/**
 * һ����չ�ֶε���Ϣ
 * �����ֶ�ID
 * �ֶ�����
 * �ֶ�����
 * �ֶγ��ȵȵ�
 * @author maxh
 */
public class ExtendFieldsHeader implements  ICreateInstance<ExtendFieldsHeader>, Cloneable{
	
	
	/**
	 * ��ͨ�ı���
	 */
	public static final int TYPE_COMMON = 0;
	/**
	 * �����б���
	 */
	public static final int TYPE_COMBO = 1;
	
	
	public ExtendFieldsHeader() {
		// TODO Auto-generated constructor stub
	}
	
	public ExtendFieldsHeader(String id,String text){
		this.id = id;
		this.text = text;
	}
	
	public ExtendFieldsHeader(String id,String text,String type,String width,String value){
		this.id = id;
		this.text = text;
		this.type = type.equals(TYPE_COMBO)?1:0;
		try {
			this.width = Integer.valueOf(width);
		} catch (Exception e) {
			this.width = 100;
		}
		this.values = value;
	}

	/**
	 * �ֶ�id��map�е�key
	 */
	private String id = "";
	/**
	 * �ֶ����� ��ͷ�ı�
	 */
	private String text = "";
	/**
	 * �ֶ����� �ı����������б�
	 */
	private int type = TYPE_COMMON;
	/**
	 * �ֶο�� �п�
	 */
	private int width = 100;
	/**
	 * ����������б��� �����б��ֵ
	 */
	private String values = "";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.ICreateInstance#getNewInstance()
	 */
	public ExtendFieldsHeader getNewInstance() {
		try {
			return (ExtendFieldsHeader)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new ExtendFieldsHeader();
	}
	
	
	

}
