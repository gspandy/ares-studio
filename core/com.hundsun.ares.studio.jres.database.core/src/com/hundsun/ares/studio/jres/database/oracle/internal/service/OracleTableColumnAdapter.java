/**
 * Դ�������ƣ�OracleTableColumn.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.oracle.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���С��
 */
package com.hundsun.ares.studio.jres.database.oracle.internal.service;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.database.internal.service.TableColumnAdapter;
import com.hundsun.ares.studio.jres.database.oracle.service.IOracleTableColumn;
import com.hundsun.ares.studio.jres.model.database.TableColumn;

/**
 * @author wangxh
 *
 */
public class OracleTableColumnAdapter extends TableColumnAdapter implements IOracleTableColumn {

	public OracleTableColumnAdapter(TableColumn column,IARESProject project) {
		super(column,project);
	}

}
