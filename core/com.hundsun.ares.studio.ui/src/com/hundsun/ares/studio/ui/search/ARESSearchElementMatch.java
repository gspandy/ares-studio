/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.search.ui.text.Match;

import com.hundsun.ares.studio.core.IARESElement;

/**
 * ����ƥ����
 * @author liaogc
 */
public class ARESSearchElementMatch extends Match {
		
		private List<IARESElement> children = new ArrayList<IARESElement>();
		
		public ARESSearchElementMatch(IARESElement element) {
			super(element, 0, 0);
		}

		/**
		 * @return the children
		 */
		public List<IARESElement> getChildren() {
			return children;
		}

		/**
		 * @param children the children to set
		 */
		public void setChildren(List<IARESElement> children) {
			this.children = children;
		}
}
