/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.extendpoint.manager;

/**
 * ��ʾ��չ���е�һ����չҳ����Ϣ
 * @author maxh
 *
 */
public class ExtendPageInfo implements Comparable<ExtendPageInfo> {
	
	Class pageClass;
	String pageId = "";
	String pageName = "";
	int order = 0;
	boolean hidden;	// ��ʾ����Ǹ����ص���չҳ�棬���������棬������̨�߼�����
	
	
	/**
	 * @param pageClass
	 * @param pageId
	 * @param pageName
	 * @param order
	 */
	public ExtendPageInfo(Class pageClass, String pageId, String pageName, int order, boolean hidden) {
		super();
		this.pageClass = pageClass;
		this.pageId = pageId;
		this.pageName = pageName;
		this.order = order;
		this.hidden = hidden;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	
	public Class getPageClass() {
		return pageClass;
	}
	public void setPageClass(Class pageClass) {
		this.pageClass = pageClass;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public boolean isHidden() {
		return hidden;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ExtendPageInfo o) {
		return order - o.order;
	}
}
