/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.extendpoint.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * ��չ��ģ������
 * @author maxh
 *
 */
public class AresExtendPointModelDetail {
	String mainGetMethod = "";
	String showName = "";
	String showPic = "";
	List<AresExtendPointFieldDetail> fields = new ArrayList<AresExtendPointFieldDetail>();
	public String getMainGetMethod() {
		return mainGetMethod;
	}
	public void setMainGetMethod(String mainGetMethod) {
		this.mainGetMethod = mainGetMethod;
	}
	public List<AresExtendPointFieldDetail> getFields() {
		return fields;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getShowPic() {
		return showPic;
	}
	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}
	
}
