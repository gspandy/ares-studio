/**
 * Դ�������ƣ�DefaultProblemPool.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.ui.validate.IKeyConstructor;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.IProblemView;
import com.hundsun.ares.studio.ui.validate.KeyParameter;
import com.hundsun.ares.studio.ui.validate.ProblemPoolChangeEvent;

/**
 * ����أ����Ա�����
 * @author gongyf
 *
 */
public class DefaultProblemPool implements IProblemPool {
	
	protected boolean needNotify = true;
	protected Multimap<KeyParameter, Object> problems = HashMultimap.create();
	
	protected List<IProblemView> views = new ArrayList<IProblemView>();
	protected IKeyConstructor handler; 
	protected Map<Object, Object> context = new HashMap<Object, Object>();
	
	protected Set<Object> removeedProblemSet = Collections.synchronizedSet( new HashSet<Object>() );
	protected Set<Object> addProblemSet = Collections.synchronizedSet( new HashSet<Object>() );
	
	
	@Override
	public void setKeyConstructor(IKeyConstructor handler) {
		this.handler = handler;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#addView(com.hundsun.ares.studio.jres.ui.validate.IProblemView)
	 */
	@Override
	public void addView(IProblemView view) {
		synchronized (views) {
			views.add(view);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#removeView(com.hundsun.ares.studio.jres.ui.validate.IProblemView)
	 */
	@Override
	public void removeView(IProblemView view) {
		synchronized (views) {
			views.remove(view);
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#addProblem(com.hundsun.ares.studio.jres.ui.validate.KeyParameter, java.lang.Object)
	 */
	@Override
	public void addProblem(KeyParameter key, Object problem) {
		synchronized (problems) {
			doAddProblem(key, problem);
		}
		if (needNotify) {
			notifyViews();
		}
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#addProblem(java.lang.Object)
	 */
	@Override
	public void addProblem(Object problem) {
		Assert.isNotNull(handler, "key����������Ϊ��");
		addProblem(handler.constructKey(problem), problem);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#clear(com.hundsun.ares.studio.jres.ui.validate.KeyParameter)
	 */
	@Override
	public void clear(KeyParameter key) {
		synchronized (problems) {
			doRemoveProblem(key);
		}
		if (needNotify) {
			notifyViews();
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#getProblem(com.hundsun.ares.studio.jres.ui.validate.KeyParameter)
	 */
	@Override
	public Object[] getProblem(KeyParameter key) {
		synchronized (problems) {
			return problems.get(key).toArray();
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#hasProblem(com.hundsun.ares.studio.jres.ui.validate.KeyParameter)
	 */
	@Override
	public boolean hasProblem(KeyParameter key) {
		synchronized (problems) {
			for (KeyParameter mapKey : problems.keySet()) {
				if (mapKey.contains(key)) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#clear()
	 */
	@Override
	public void clear() {
		synchronized (problems) {
			removeedProblemSet.addAll(problems.values());
			problems.clear();
		}
		if (needNotify) {
			notifyViews();
		}
	}

	protected void notifyViews() {
		synchronized (views) {
			// ׼��֪ͨ�¼�
			ProblemPoolChangeEvent event = createChangeEvent(
					removeedProblemSet.toArray(), addProblemSet.toArray(), context);
			
			// ׼��������һ�α仯
			removeedProblemSet.clear();
			addProblemSet.clear();
			
			for (IProblemView view : views) {
				view.refresh(event);
			}
		}
	}
	
	/**
	 * ��key�ֽ����뵽map��ȥ
	 * @param key
	 * @param problem
	 */
	protected void doAddProblem(KeyParameter key, Object problem) {
		for (KeyParameter subKey : key.resolve()) {
			this.problems.put(subKey, problem);
		}
		this.addProblemSet.add(problem);
	}
	
	protected void doRemoveProblem(KeyParameter key) {
		for (Iterator<Entry<KeyParameter, Collection<Object>> > iterator = problems.asMap().entrySet().iterator(); iterator.hasNext(); ) {
			Entry<KeyParameter, Collection<Object>> entry = iterator.next();
			
			if (entry.getKey().contains(key)) {
				this.removeedProblemSet.addAll(entry.getValue());
				iterator.remove();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#addProblems(java.lang.Object[])
	 */
	@Override
	public void addProblems(Object[] problems) {
		synchronized (problems) {
			for (Object problem : problems) {
				KeyParameter key = handler.constructKey(problem);
				doAddProblem(key ,problem);
			}
			
		}
		if (needNotify) {
			notifyViews();
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#beginChange()
	 */
	@Override
	public void beginChange() {
		needNotify = false;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#endChange()
	 */
	@Override
	public void endChange() {
		needNotify = true;
		notifyViews();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IProblemPool#getKeys()
	 */
	@Override
	public Set<KeyParameter> getKeys() {
		return problems.keySet();
	}

	protected ProblemPoolChangeEvent createChangeEvent(Object[] removeedProblems, Object[] addProblems,
			Map<Object, Object> context) {
		return new ProblemPoolChangeEvent(this, removeedProblems, addProblems, context);
	}
}
