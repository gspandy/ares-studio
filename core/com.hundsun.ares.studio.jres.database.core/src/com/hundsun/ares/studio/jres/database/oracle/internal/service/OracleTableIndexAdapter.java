/**
 * Դ�������ƣ�OracleTableIndexAdapter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import java.util.Collections;
import java.util.List;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.service.FastFindArrayList;
import com.hundsun.ares.studio.core.service.IKeyProvider;
import com.hundsun.ares.studio.jres.database.internal.service.TableIndexAdapter;
import com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableIndex;
import com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.oracle.OracleIndexProperty;

/**
 * @author wangxh
 *
 */
public class OracleTableIndexAdapter extends TableIndexAdapter implements IOracleTableIndex {

	private FastFindArrayList<String, IOracleTableIndexColumn> columnList;
	private IARESProject project;
	public OracleTableIndexAdapter(TableIndex index,IARESProject project) {
		super(index,project);
		this.project = project;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.internal.service.TableIndexAdapter#getColumnList()
	 */
	@Override
	public List<? extends IOracleTableIndexColumn> getColumnList() {
		return Collections.unmodifiableList(getcolumnList());
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.internal.service.TableIndexAdapter#getColum(java.lang.String)
	 */
	@Override
	public IOracleTableIndexColumn getColum(String name) {
		return getcolumnList().find(name);
	}
	private FastFindArrayList<String, IOracleTableIndexColumn> getcolumnList(){
		if(columnList == null){
			columnList = new FastFindArrayList<String, IOracleTableIndexColumn>(new IKeyProvider<String, IOracleTableIndexColumn>() {

				@Override
				public String getKey(IOracleTableIndexColumn obj) {
					return obj.getColumnName();
				}
			});
			for(TableIndexColumn col : index.getColumns()){
				columnList.add(new OrableTableIndexColumnAdapter(col));
			}
		}
		return columnList;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableIndex#isReverse()
	 */
	@Override
	public boolean isReverse() {
		return ((OracleIndexProperty) index).isReverse();
	}
	
}
