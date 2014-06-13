/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.search;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.core.IARESProject;

/**
 * ��Ҫ�����Դ������
 * @author liaogc
 */
public  class ARESSearchQuery implements ISearchQuery {
	protected final IARESElement[] scope;/*��������*/
	protected final String searchText;/*�����ı�*/
	protected final boolean isCaseSensitive;/*�Ƿ��Сд*/
	protected  List<String>  searchResTypes;/*��������(��������)*/
	protected  List<String>  searchItems;/*������*/
	protected ARESSearchQuery query;/*����*/

	protected ARESElementSearchResult searchResult;


	public ARESSearchQuery(String searchText, boolean isCaseSensitive, List<String> searchForResTypes, List<String> searchItems, IARESElement[] scope) {
		this.searchText = searchText;
		this.isCaseSensitive = isCaseSensitive;
		this.searchResTypes = searchForResTypes;
		this.searchItems = searchItems;
		this.scope = scope;
		query = this;
	}

	public IARESElement[] getSearchScope() {
		return scope;
	}

	public boolean canRunInBackground() {
		return true;
	}

	/**
	 * ����Ԫ�ز�ͬ��Դ��:���ò�ͬ����չ���search����
	 * @param searchPattern
	 * @param e
	 */
	private void search(IARESElement e) {
		Collection<ARESSearcherElement> searchElements = ARESSearcherManager.getInstance().getSearcherElements().values();
		for(ARESSearcherElement element :searchElements){
			element.getSearcher().search(this.searchResTypes, this.searchItems, this.scope, this);
		}
	}

	/**
	 * 
	 * @param element ��������Դ
	 * @return ƥ����
	 */
	public ARESSearchElementMatch addMatch(IARESElement element) {
		// �Ѿ����ڲ���Ҫ���
		if (searchResult.getMatchCount(element) > 0) {
			return (ARESSearchElementMatch) searchResult.getMatches(element)[0];
		}

		// Ҫ�����и��ڵ����
		IARESElement parent = element.getParent();
		ARESSearchElementMatch thisMatch = new ARESSearchElementMatch(element);
		if (!(element instanceof IARESProject)) {

			ARESSearchElementMatch parentMatch = addMatch(parent);
			parentMatch.getChildren().add(element);
		} 
		searchResult.addMatch(thisMatch);
		return thisMatch;
	}










	public IStatus run(final IProgressMonitor monitor) {
		ARESElementSearchResult result = (ARESElementSearchResult) getSearchResult();
		result.removeAll();
		monitor.beginTask("������Ŀ", scope.length * 10);
		if(StringUtils.isNotBlank(searchText)){
			for (IARESElement e : scope) {
				search(e);
				monitor.worked(10);
			}
		}
		
		monitor.done();
		return Status.OK_STATUS;

	}


	public String getLabel() {
		return "������Ŀ";
	}

	public String getSearchString() {
		return searchText;
	}

	public Pattern getSearchPattern() {
		// ת��Ϊ����ʽ

		StringBuffer sbPattern = new StringBuffer();
		asRegEx(getSearchString(), sbPattern);

		if (isCaseSensitive) {
			// Ĭ�������д�Сд��
			return Pattern.compile(sbPattern.toString());
		} else {
			return Pattern.compile(sbPattern.toString(), Pattern.CASE_INSENSITIVE);
		}

	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public boolean canRerun() {
		return true;
	}

	public ISearchResult getSearchResult() {
		if (searchResult == null) {
			searchResult = new ARESElementSearchResult(this);
		}
		return searchResult;
	}

	/**
	 * ���س��ֹؼ��ֵ�λ��
	 */

	public int getPosition(String pseudoCode, String keyWord, int fromIndex) {
		return pseudoCode.indexOf(keyWord, fromIndex);
	}

	/**
	 * Translates a StringMatcher pattern (using '*' and '?') to a regex pattern
	 * string
	 * 
	 * @param stringMatcherPattern
	 *         a pattern using '*' and '?'
	 */
	private static StringBuffer asRegEx(String stringMatcherPattern, StringBuffer out) {
		boolean escaped = false;
		boolean quoting = false;

		int i = 0;
		while (i < stringMatcherPattern.length()) {
			char ch = stringMatcherPattern.charAt(i++);

			if (ch == '*' && !escaped) {
				if (quoting) {
					out.append("\\E"); //$NON-NLS-1$
					quoting = false;
				}
				out.append(".*"); //$NON-NLS-1$
				escaped = false;
				continue;
			} else if (ch == '?' && !escaped) {
				if (quoting) {
					out.append("\\E"); //$NON-NLS-1$
					quoting = false;
				}
				out.append("."); //$NON-NLS-1$
				escaped = false;
				continue;
			} else if (ch == '\\' && !escaped) {
				escaped = true;
				continue;

			} else if (ch == '\\' && escaped) {
				escaped = false;
				if (quoting) {
					out.append("\\E"); //$NON-NLS-1$
					quoting = false;
				}
				out.append("\\\\"); //$NON-NLS-1$
				continue;
			}

			if (!quoting) {
				out.append("\\Q"); //$NON-NLS-1$
				quoting = true;
			}
			if (escaped && ch != '*' && ch != '?' && ch != '\\')
				out.append('\\');
			out.append(ch);
			escaped = ch == '\\';

		}
		if (quoting)
			out.append("\\E"); //$NON-NLS-1$

		return out;
	}

}
