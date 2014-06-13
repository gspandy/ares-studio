/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import java.util.Map;

import org.eclipse.core.resources.IMarker;

/**
 * ����������Ϣ�Ľӿڡ�
 * ������ֱ�Ӷ�Ӧ��IMarker�����Ժͷ�����
 * @author sundl
 */
public interface IARESProblem {
	
	String UNPERSISTENT_PROPERTY_PREFIX = "hundsun.unpersistent";
	
	/**
	 * ����Marker��ʱ��ʹ�õ�Type����������ע��Marker���͵�ʱ��ʹ�õ�����.
	 * @return
	 */
	public String getType();
	
	boolean isError();
	void setError(boolean isError);
	
	boolean isWarning();
	void setWaring(boolean isWarning);

	/**
	 * 
	 * @return
	 * @see IMarker#LOCATION
	 */
	String getLocation();
	void setLocation(String location);
		
	/**
	 * @return
	 * @see IMarker#MESSAGE
	 */
	String getMessage();
	void setMessage(String message);
	
	public Object getAttribute(String attributeName);
	
	public int getAttribute(String attributeName, int defaultValue);
	
	public String getAttribute(String attributeName, String defaultValue);
	
	public boolean getAttribute(String attributeName, boolean defaultValue);
	
	public Map getAttributes();
	
	public Object[] getAttributes(String[] attributeNames);
	
	public void setAttribute(String attributeName, int value);
	
	public void setAttribute(String attributeName, Object value);

	public void setAttribute(String attributeName, boolean value);
	
	public void setAttributes(String[] attributeNames, Object[] values);
	
	public void setAttributes(Map attributes);
}
