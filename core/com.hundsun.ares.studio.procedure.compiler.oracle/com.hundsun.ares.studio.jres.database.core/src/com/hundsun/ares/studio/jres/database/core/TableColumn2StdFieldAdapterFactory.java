package com.hundsun.ares.studio.jres.database.core;

import org.eclipse.core.runtime.IAdapterFactory;

import com.hundsun.ares.studio.jres.model.database.ColumnType;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.metadata.MetadataFactory;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;

public class TableColumn2StdFieldAdapterFactory implements IAdapterFactory {

	private static Class<?>[] ADAPTERS = new Class<?> [] {
		StandardField.class
	};
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof TableColumn) {
			TableColumn column = (TableColumn) adaptableObject;
			// ��׼�ֶβ��������䣬 �Ǳ�׼�ֶβ�����
			if (adapterType == StandardField.class && column.getColumnType() == ColumnType.STD_FIELD) {
				String name = column.getFieldName();
				StandardField std = MetadataFactory.eINSTANCE.createStandardField();
				std.setName(name);
				return std;
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return ADAPTERS;
	}

}
