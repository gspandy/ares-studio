/**
 * Դ�������ƣ�TriggerScriptWrapImpl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.clearinghouse.core.script.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.jres.model.chouse.util.RevHistoryCompator;
import com.hundsun.ares.studio.jres.model.database.oracle.TriggerResourceData;
import com.hundsun.ares.studio.jres.script.api.database.ITriggerScriptWrap;
import com.hundsun.ares.studio.jres.script.api.wrap.IRevHistoryScriptWrap;
import com.hundsun.ares.studio.jres.script.tool.HistoryCommentCompator;

/**
 * @author yanwj06282
 *
 */
public class TriggerScriptWrapImpl extends DatabaseResScriptWrapImpl implements
		ITriggerScriptWrap {

	private TriggerResourceData trigger;
	public TriggerScriptWrapImpl(IARESResource resource) {
		super(resource);
		try {
			trigger = resource.getInfo(TriggerResourceData.class);
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getObjectId() {
		return trigger.getObjectId();
	}

	@Override
	public TriggerResourceData getOriginalInfo() {
		return trigger;
	}
	
	public String getChineseName(){
		return trigger.getChineseName();
	}
	
	public String getSql(){
		return trigger.getSql();
	}
	
	public IRevHistoryScriptWrap[] getHistories(){
		List<IRevHistoryScriptWrap>	hisWraps = new ArrayList<IRevHistoryScriptWrap>();
		List<RevisionHistory> histories = new ArrayList<RevisionHistory>();
		
		for (RevisionHistory his : getOriginalInfo().getHistories()){
			try {
				if (HistoryCommentCompator.compareVersion(his.getVersion(), resource.getARESProject().getProjectProperty().getVersion())) {
					histories.add(his);
				}
			} catch (ARESModelException e) {
				e.printStackTrace();
			}
		}
		//����,��ǰ����
		Collections.sort(histories, new RevHistoryCompator());
		for (RevisionHistory his : histories){
			hisWraps.add(new RevHistoryScriptWrapImpl(his, resource));
		}
		return hisWraps.toArray(new IRevHistoryScriptWrap[0]);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.script.api.wrap.IDatabaseResScriptWrap#setObjectId(java.lang.String)
	 */
	@Override
	public void setObjectId(String objectId) {
		// TODO Auto-generated method stub
		getOriginalInfo().setObjectId(objectId);
	}
	
}
