/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.model.chouse.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.hundsun.ares.studio.jres.model.chouse.AddConstraintModification;
import com.hundsun.ares.studio.jres.model.chouse.AddIndexField;
import com.hundsun.ares.studio.jres.model.chouse.AddIndexFieldModification;
import com.hundsun.ares.studio.jres.model.chouse.AddIndexModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnPKModification;
import com.hundsun.ares.studio.jres.model.chouse.AddTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.AddTableModification;
import com.hundsun.ares.studio.jres.model.chouse.CTCPMDetail;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnNullableModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnPrimaryKeyModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnTypeModification;
import com.hundsun.ares.studio.jres.model.chouse.ChangeTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.ChousePackage;
import com.hundsun.ares.studio.jres.model.chouse.ConstraintModifyDetail;
import com.hundsun.ares.studio.jres.model.chouse.Modification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveConstraintModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveIndexField;
import com.hundsun.ares.studio.jres.model.chouse.RemoveIndexFieldModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveIndexModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnPKModification;
import com.hundsun.ares.studio.jres.model.chouse.RemoveTableColumnUniqueModifycation;
import com.hundsun.ares.studio.jres.model.chouse.RenameTableColumnModification;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.jres.model.database.DatabasePackage;
import com.hundsun.ares.studio.jres.model.database.ForeignKey;
import com.hundsun.ares.studio.jres.model.database.TableColumn;
import com.hundsun.ares.studio.jres.model.database.TableIndex;
import com.hundsun.ares.studio.jres.model.database.TableIndexColumn;
import com.hundsun.ares.studio.jres.model.database.TableKey;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;

/**
 * ������չ��صĹ����෽��
 * @author gongyf
 *
 */
public class StockUtils {
	
	private static Logger logger = Logger.getLogger(StockUtils.class);
	
	/**
	 * ˵�������еķָ���
	 */
	private static final String SEPARATOR = ",";
	
	/**
	 * ��ȡ���޸ļ�¼����ϸ˵����Ϣ
	 * @param tableData 2014��3��25�� sundl��ӣ� ��Щ����£�modify��û��ӣ����Բ���ͨ��modifyֱ�ӻ�ȡ��tabledata����Ҫ���롣 ����Ϊnull�����Ϊnull�Ļ����ͻ����Ŵ�modifyͨ��emf��ȡtabledata
	 * @param modify
	 * @return
	 */
	public static String getModificationDescription(TableResourceData tableData, Modification modify) {
		if (modify != null) {
			StringBuffer sb = new StringBuffer();
			if (modify instanceof AddIndexModification) {
				// ��ӱ�����
				AddIndexModification m = (AddIndexModification) modify;			

				sb.append("���������� ");
				for (TableIndex index : m.getIndexs() ) {
					sb.append(String.format("%s:[",index.getName()));
					join(sb, index.getColumns(), DatabasePackage.Literals.TABLE_INDEX__NAME, SEPARATOR);
					sb.append("],");
				}
				sb = new StringBuffer( sb.subSequence(0, sb.length() - 1 ) );
				sb.append("��");
				// ��������ֶ�
				
			} else if (modify instanceof AddTableColumnModification) {
				// ��ӱ��ֶ�
				AddTableColumnModification m = (AddTableColumnModification) modify;
				
				sb.append("����˱��ֶΣ�");
				join(sb, m.getColumns(), DatabasePackage.Literals.TABLE_COLUMN__NAME, SEPARATOR);
				sb.append("��");
			} else if (modify instanceof AddTableModification) {
				AddTableModification m = (AddTableModification) modify;
				
				if (m.isNewSelfTable()) {
					sb.append("��ӱ�");
				}
				if (m.isNewHistoryTable()) {
					sb.append(" �����ʷ��");
				}
			} else if (modify instanceof ChangeTableColumnTypeModification) {
				ChangeTableColumnTypeModification m = (ChangeTableColumnTypeModification) modify;
				
				sb.append("�޸��˱��ֶ����ͣ�");
				join(sb, m.getDetails(), ChousePackage.Literals.CTCTM_DETAIL__NAME, SEPARATOR);
				sb.append("��");
				
			} else if (modify instanceof RemoveIndexModification) {
				RemoveIndexModification m = (RemoveIndexModification) modify;
				
				sb.append("ɾ���˱�������");
				join(sb, m.getIndexs(), ChousePackage.Literals.REMOVED_INDEX__NAME, SEPARATOR);
				sb.append("��");
			
			}else if (modify instanceof RemoveIndexFieldModification) {
				RemoveIndexFieldModification m = (RemoveIndexFieldModification) modify;
				
				sb.append("ɾ�������ֶΣ�");
				for(RemoveIndexField removeIndexField:m.getIndexs()){
					sb.append("����"+ removeIndexField.getName()+":"+"ɾ���������ֶΣ�");
					for(int i=0;i< removeIndexField.getIndexFields().size();i++){
						TableIndexColumn tableIndexColumn =  removeIndexField.getIndexFields().get(i);
						if(i!=  removeIndexField.getIndexFields().size()-1){
							sb.append(tableIndexColumn.getColumnName()).append(",");
						}else{
							sb.append(tableIndexColumn.getColumnName());
						}
						
					}
				}
				sb.append("��");
			
			} else if (modify instanceof AddIndexFieldModification) {
				AddIndexFieldModification m = (AddIndexFieldModification) modify;
				
				sb.append("���������ֶΣ�");
				for(AddIndexField addIndexField:m.getIndexs()){
					sb.append("����"+ addIndexField.getName()+":"+"�����������ֶΣ�");
					for(int i = 0;i< addIndexField.getIndexFields().size();i++){
						TableIndexColumn tableIndexColumn = addIndexField.getIndexFields().get(i);
						if(i!=addIndexField.getIndexFields().size()-1){
							sb.append(tableIndexColumn.getColumnName()).append(",");
						}else{
							sb.append(tableIndexColumn.getColumnName());
						}
						
					}
					
				}
				sb.append("��");
			
			} else if (modify instanceof RemoveTableColumnModification) {
				RemoveTableColumnModification m = (RemoveTableColumnModification) modify;
				
				sb.append("ɾ���˱��ֶΣ�");
				join(sb, m.getColumns(), DatabasePackage.Literals.TABLE_COLUMN__NAME, SEPARATOR);
				sb.append("��");
				
			} else if (modify instanceof RenameTableColumnModification) {
				RenameTableColumnModification m = (RenameTableColumnModification) modify;
				
				sb.append("�������˱��ֶΣ�");
				for (int i = 0; i < m.getDetails().size(); i++) {
					if (i > 0) {
						sb.append(SEPARATOR);
					}
					sb.append(m.getDetails().get(i).getOldName());
					sb.append("->");
					sb.append(m.getDetails().get(i).getNewName());
				}
				sb.append("��");
			}else if (modify instanceof ChangeTableColumnNullableModifycation) {
				ChangeTableColumnNullableModifycation m = (ChangeTableColumnNullableModifycation) modify;
				
				sb.append("�޸��˱��ֶ�����գ�");
				join(sb, m.getDetails(), ChousePackage.Literals.MODIFY_DETAIL__NAME, SEPARATOR);
				sb.append("��");
				
			}else if (modify instanceof ChangeTableColumnPrimaryKeyModifycation) {
				ChangeTableColumnPrimaryKeyModifycation m = (ChangeTableColumnPrimaryKeyModifycation) modify;
				
				sb.append("�޸��˱��ֶ�������");
				EList<CTCPMDetail> results = new BasicEList<CTCPMDetail>();
				for (CTCPMDetail detail : m.getDetails()) {
					if (detail.isPrimarkKey()) {
						results.add(detail);
					}
				}
				join(sb, results, ChousePackage.Literals.MODIFY_DETAIL__NAME, SEPARATOR);
				sb.append("��");
				
			}else if (modify instanceof RemoveTableColumnPKModification) {
				sb.append("ɾ���˱�����");
			}else if (modify instanceof AddTableColumnPKModification) {
				AddTableColumnPKModification m = (AddTableColumnPKModification) modify;
				
				sb.append("�����˱��ֶ�������");
				join(sb, m.getDetails(), ChousePackage.Literals.MODIFY_DETAIL__NAME, SEPARATOR);
				sb.append("��");
				
			}else if (modify instanceof ChangeTableColumnUniqueModifycation) {
				ChangeTableColumnUniqueModifycation m = (ChangeTableColumnUniqueModifycation) modify;
				
				sb.append("�޸��˱��ֶ�ΨһԼ����");
				join(sb, m.getDetails(), ChousePackage.Literals.MODIFY_DETAIL__NAME, SEPARATOR);
				sb.append("��");
				
			}else if (modify instanceof AddTableColumnUniqueModifycation) {
				AddTableColumnUniqueModifycation m = (AddTableColumnUniqueModifycation) modify;
				
				sb.append("�����˱��ֶ�ΨһԼ����");
				join(sb, m.getDetails(), ChousePackage.Literals.MODIFY_DETAIL__NAME, SEPARATOR);
				sb.append("��");
				
			}else if (modify instanceof RemoveTableColumnUniqueModifycation) {
				sb.append("ɾ���˱�ΨһԼ��");
			} else if (modify instanceof AddConstraintModification) {
				sb.append("������Լ��");
				try {
					AddConstraintModification add = (AddConstraintModification) modify;
					List<ConstraintModifyDetail> details = add.getDetails();
					if (details.size() > 0) {
						for (int i = 0; i < details.size(); i++) {
							sb.append(details.get(i).getName());
							if (i < details.size() -1)
								sb.append(",");
						}
					}
				} catch (Exception e) {
					logger.error(e);
				}
			} else if (modify instanceof RemoveConstraintModification) {
				sb.append("ɾ����Լ��");
				try {
					RemoveConstraintModification add = (RemoveConstraintModification) modify;
					List<ConstraintModifyDetail> details = add.getDetails();
					if (details.size() > 0) {
						for (int i = 0; i < details.size(); i++) {
							sb.append(details.get(i).getName());
							if (i < details.size() -1)
								sb.append(",");
						}
					}
				} catch (Exception e) {
					logger.error(e);
				}
			}
			TableResourceData table = tableData == null ? getTable(modify) : tableData;
			if (table != null) {
				StringBuffer bf = new StringBuffer("���ݱ�");
				bf.append(table.getName());
				bf.append("��");
				sb = bf.append(sb);
			}
			return sb.toString();
		}
		
		return StringUtils.EMPTY;
	}
	
	private static TableResourceData getTable (EObject modify){
		if (modify != null) {
			if (modify.eContainer() instanceof TableResourceData) {
				return (TableResourceData) modify.eContainer();
			}else {
				return getTable(modify.eContainer());
			}
		}
		return null;
	}
	
	/**
	 * ���ַ�����һ�ַָ����������
	 * @param sb
	 * @param list
	 * @param attribute
	 * @param separator
	 */
	private static void join(StringBuffer sb, List<? extends EObject> list, EAttribute attribute, String separator) {
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(list.get(i).eGet(attribute));
		}
	}
	
	public static TableKey toTableKey(ConstraintModifyDetail constraint) {
		TableKey key = DatabaseFactory.eINSTANCE.createTableKey();
		key.setMark(constraint.getMark());
		key.setName(constraint.getName());
		key.setType(constraint.getType());
		for (TableColumn c : constraint.getColumns()) {
			key.getColumns().add(EcoreUtil.copy(c));
		}
		for (ForeignKey fk : constraint.getForeignKey()) {
			key.getForeignKey().add(EcoreUtil.copy(fk));
		}
		return key;
	}
}
