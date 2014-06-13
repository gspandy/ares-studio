/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * @author liaogc
 */
public class SearcherUitl {
	
	/**
	 * ������е�������
	 * @param elements
	 * @return
	 */
	public static List<String>getSearchItems(){
		List<String> searchItems = new ArrayList<String>();
		List<ARESSearcherElement> elements = new ArrayList<ARESSearcherElement>();
		 elements.addAll(ARESSearcherManager.getInstance().getSearcherElements().values());
		 Collections.sort(elements, new ARESSearcherElementComparator());
		for(ARESSearcherElement element:elements){
			List<String> items = element.getSearchItems();
			for(String searchItem:items){
				if(!searchItems.contains(searchItem)){
					searchItems.add(searchItem);
				}
			}
		}
		return searchItems;
	}
	
	/**
	 * ���������Դ��������˵��
	 * @return
	 */
 public static List<String> getSearchCResTypes(){
	 List<String> cResTypes = new ArrayList<String>();
	 List<ARESSearcherElement> elements = new ArrayList<ARESSearcherElement>();
	 elements.addAll(ARESSearcherManager.getInstance().getSearcherElements().values());
	 Collections.sort(elements, new ARESSearcherElementComparator());
	 for(ARESSearcherElement element:elements){
		 for(String cResType:element.getSearcherResTypes()){
			 if(!cResTypes.contains(cResType)){
				 cResTypes.add(cResType);
			 }
		 }
		
	 }
	 return cResTypes;
	 
 }
 /**
  * ������Դ���Ͷ�Ӧ��������
  * @param resTypes
  * @return
  */
 public static List<String> getSearchItemsByResTypes(List<String> resTypes){
	 List<String> searchItems = new ArrayList<String>();
	 Collection<ARESSearcherElement> elements = ARESSearcherManager.getInstance().getSearcherElements().values();
	 for(ARESSearcherElement element:elements){
		 for(String resType:resTypes){
			 if(element.getSearcherResTypes().contains(resType)){
				 List<String> items =  element.getSearchItems();
				 for(String item:items){
					 if(!searchItems.contains(item)){
						 searchItems.add(item);
					 }
				 }
			 }
		 }
	 }
		return searchItems;

	}
 
 /**
  * ������Դ��Ӧ������˳��
  * 
  * @author liaogc
  */
 private static class ARESSearcherElementComparator implements  Comparator{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object arg0, Object arg1) {
		ARESSearcherElement element0 = (ARESSearcherElement) arg0;
		ARESSearcherElement element1 = (ARESSearcherElement) arg1;
		int order0 = NumberUtils.toInt(element0.getOrder(),-1);
		int order1 = NumberUtils.toInt(element1.getOrder(),-1);
		return order0-order1;
	}
	 
 }

}
