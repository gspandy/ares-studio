/**
 * Դ�������ƣ�JRESReferencProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.model.reference.RelationInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

public class JRESReferencProvider implements /*IResourceChangeListener,*/IResReferenceProvider{

	IARESProject project;
	
	public JRESReferencProvider(IARESProject project){
		this.project = project;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResReferenceProvider#getReferList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Object[] getReferList(String masterUniqueName,
			String masterNamespace, String masterType) {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResReferenceProvider#getLinkList(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Object[] getLinkList(String uniqueName, String namespace, String type) {
		List<RelationInfo> infoList = null;
		// FIXME JRESĿǰ��ʹ�������ռ�
		infoList = ReferenceManager.getInstance().getRelationInfoByTarget(type, uniqueName, project);
		
//		if (StringUtils.equals(namespace, IResourceTable.Scope_IGNORE_NAMESPACE) || StringUtils.isBlank(namespace)) {
//			infoList = ReferenceManager.getInstance().getRelationInfoByTarget(type, uniqueName, project);
//		} else {
//			infoList = ReferenceManager.getInstance().getRelationInfoByTarget(type, uniqueName, namespace, project);
//		}
		
		return Collections2.transform(infoList, new Function<RelationInfo, Map<Object, Object>>() {

			@Override
			public Map<Object, Object> apply(RelationInfo from) {
				return transform(from);
			}
		}).toArray();
	}

	private static Map<Object, Object> transform(RelationInfo rel) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(IResourceTable.RES_NAME, rel.getUsedRefName());
		map.put(IResourceTable.RES_NAMESPACE, rel.getUsedRefNamespace());
		map.put(IResourceTable.RES_TYPE, rel.getUsedRefType());
//		map.put(IResourceTable.TARGET_OWNER, rel.get);
		map.put(IResourceTable.TARGET_RESOURCE, rel.getHostResource());
		return map;
	}
	
//	private static Logger log = Logger.getLogger(JRESReferencProvider.class);
//	
//	IARESProject project;
//	
//	public ReferenceTable table = new ReferenceTable();
//	
//	private List<ITableDataProvider> tabledataprovider = new ArrayList<ITableDataProvider>();
//	
//	public JRESReferencProvider(IARESProject project){
//		this.project = project;
//		//����
//		try {
//			Connection connect = null;
//			try {
//				connect = MemDBConnectionPool.getInstance()
//				.getConnection();
////			table.setTextTable(true);   //���ı���ʽ�洢
//				table.createTable(connect, MemTable.genID());
//				
//			} finally {
//				MemDBConnectionPool.getInstance().returnConnection(connect);
//			}
//			
//			//Ĭ�ϱ������ṩ��
//			DefaultReferenceProvider defaultReferenceProvider = new DefaultReferenceProvider();
//			defaultReferenceProvider.setArgs(project,table);
//			tabledataprovider.add(defaultReferenceProvider);
//			
//			ITableProviderFactory[] providers = TableDataProviderRegistry.getInstance().getAdapterByType(IValidateConstant.EXTENTION_TYPE_REFERENCE_TABLE_PROVIDER);
//			for(ITableProviderFactory factory:providers){
//				for(ITableDataProvider item:factory.createProviders()){
//					item.setArgs(project,table);
//					tabledataprovider.add(item);
//				}
//			}
//			//��ʼ��
//			ArrayList<IContextUpdateSource> tmplist = new ArrayList<IContextUpdateSource>();
//			tmplist.add(new DefaultContextUpdateSource(ITableDataProvider.UPDATETYPE_INIT,null));
//			//��ʼ��ʱ��ס��
//			synchronized (table) {
//				update(tmplist);
//			}
//			ARESCore.addElementListener(new IARESElementChangeListener() {
//				@Override
//				public void elementChanged(ARESElementChangedEvent event) {
//					if (event.getType() == ARESElementChangedEvent.RES_PATH) {
//						IARESProject changedProject = (IARESProject) event.getElement();
//						if (changedProject.equals(JRESReferencProvider.this.project)) {
//							ArrayList<IContextUpdateSource> tmplist = new ArrayList<IContextUpdateSource>();
//							tmplist.add(new DefaultContextUpdateSource(IJRESContext.UPDATETYPE_RESPATH,new Object[]{event.getElement().getResource()}));
//							update(tmplist);
//						}
//					}
//				}
//			});
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void update(final Object context){
//		synchronized (IJRESContext.REFERENCE_UPDATE_SYNC_SIGNAL) {
//			for(ITableDataProvider provider:tabledataprovider){
//				provider.update(context);
//			}
//		}
//	}
//	
//	private Object getResourceByID(String index){
//		for(ITableDataProvider provider:tabledataprovider){
//			if(provider.contains(index)){
//				return provider.getDataByIndex(index);
//			}
//		}
//		return null;
//	}
//
//	public void clear(){
//		Connection connect = null;
//		try {
//			connect = MemDBConnectionPool.getInstance().getConnection();
//			Statement statement = connect.createStatement();
//			//statement.executeUpdate(String.format("drop table if exists %s;", tablename));
//			statement.executeUpdate(String.format("drop table %s;", table.getTablename()));
//			statement.close();
//		} catch (Throwable e) {
////			System.out.println("������ñ�����ʧ�ܡ�"+e.getMessage());
//		}finally{
//			MemDBConnectionPool.getInstance().returnConnection(connect);
//		}
//	}
//	
//	public void clearData(){
//		Connection connect = null;
//		try {
//			connect = MemDBConnectionPool.getInstance().getConnection();
//			Statement statement = connect.createStatement();
//			//statement.executeUpdate(String.format("drop table if exists %s;", tablename));
//			int lines = statement.executeUpdate(String.format("delete from %s;", table.getTablename()));
////			System.out.println("hhhhhhhhhhhhhh��ձ�"+lines);
//			statement.close();
//		} catch (Throwable e) {
//			System.out.println("������ñ�����ʧ�ܡ�"+e.getMessage());
//		}finally{
//			MemDBConnectionPool.getInstance().returnConnection(connect);
//		}
//	}
//	
//	@Override
//	protected void finalize() throws Throwable {
//		try {
//			clear();
//		} catch (Exception e) {
//		}
//		super.finalize();
//	}
//
//	@Override
//	public void resourceChanged(IResourceChangeEvent event) {
//		IResourceDelta delta = event.getDelta();
//		// ��Ŀ�رյ�ʱ����ʱ������null��
//		if (delta == null)
//			return;
//		
//		IResource resource = event.getResource();
//
//		// 2012-05-25 sundl �����Ŀɾ��/�ر�ǰ����
//		switch (event.getType()) {
//		case IResourceChangeEvent.PRE_DELETE:
//		case IResourceChangeEvent.PRE_CLOSE:
//			// project-delete event should be processed before the underlying resource is really deleted.
//			if (resource.getType() == IResource.PROJECT ) {
//				IProject project = resource.getProject();
//				IARESProject aresProj = ARESCore.create(project);
//				if (aresProj.exists() && aresProj.equals(this.project)) {
//					// ��Ŀɾ��/�ر�
//					log.debug(String.format("��⵽��Ŀ %s �رջ�ɾ�������������...", project.getName()));
//					// ContextManager�б�������Ŀ��ӦProvider���б����б���ɾ�����´���ȡ��ʱ�򣬾ͻ����³�ʼ��һ��
//					log.debug(String.format("----�����Ŀ %s ��ContextProviderManager�еĻ���...", project.getName()));
//					ContextProviderManager.getInstance().removeContextProvider(aresProj, IValidateConstant.KEY_REFERENCE_PROVIDER);
//					// ����JRESContextManager�н�����ͼ�����Ҳ������ʵĻ���drop�����Ե��±��޷�drop,��ʱֻ�������
//					clearData();
//				}
//			}
//			return;
//		case IResourceChangeEvent.POST_CHANGE:
//			//checkProjectChanges(delta);
//			handleResourceChangeDelta(delta);
//			break;
//		}
//	}
//	
//	private void handleResourceChangeDelta(IResourceDelta delta) {
//		final Map<Object, Object> changemap = new HashMap<Object, Object>();
//		try {
//			delta.accept(new IResourceDeltaVisitor() {
//
//				public boolean visit(IResourceDelta delta) throws CoreException {
//					if (delta.getResource().getType() == IResource.FILE) {
//						IProject tmp = (IProject) delta.getResource().getProject();
//						if (!changemap.containsKey(tmp)) {
//							changemap.put(tmp, new ArrayList());
//						}
//						
//						if (delta.getKind() == IResourceDelta.REMOVED ) {
//							((List)changemap.get(tmp)).add(
//									new DefaultContextUpdateSource(IJRESContext.UPDATETYPE_DEL,new Object[]{delta.getResource()}
//							));
//						}else if(delta.getKind() == IResourceDelta.ADDED){
//							((List)changemap.get(tmp)).add(
//									new DefaultContextUpdateSource(IJRESContext.UPDATETYPE_ADD,new Object[]{delta.getResource()}
//							));
//						}else if (delta.getKind() == IResourceDelta.CHANGED) {
//							// ����������ݱ仯����Ҫ�ռ���Ϣ
//							if ( (delta.getFlags() & IResourceDelta.CONTENT) == 0) {
//								return false;
//							}
//							String filename = delta.getResource().getName();
////							if (IJRESContext.RESPATH.equals(filename)) {
////								//����������Դ����
////								((List)changemap.get(tmp)).add(
////										new DefaultContextUpdateSource(IJRESContext.UPDATETYPE_RESPATH,new Object[]{delta.getResource()}
////								));
////							}
//							if (IJRESContext.PROJECT_XML.equals(filename)) {
//								//����������Դ����
//								((List)changemap.get(tmp)).add(
//										new DefaultContextUpdateSource(IJRESContext.PROJECT_XML,new Object[]{delta.getResource()}
//								));
//							}
//							String fileExtension = delta.getResource().getFileExtension();
//							((List)changemap.get(tmp)).add(
//									new DefaultContextUpdateSource(IJRESContext.UPDATETYPE_FILE_CHANGE,new Object[]{delta.getResource()}
//									));
//						}
//						return false; // �Ѹ��£������ٱ���
//				}
//					return true;
//			}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		//�����Դ�仯�Ĺ��̰��������̣�����
//		if(changemap.containsKey(this.project.getProject())){
//			update(changemap.get(project.getProject()));
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see com.hundsun.ares.studio.jres.context.IResReferenceProvider#getReferList(java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public Object[] getReferList(String masterUniqueName,
//			String masterNamespace, String masterType) {
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.hundsun.ares.studio.jres.context.IResReferenceProvider#getLinkList(java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public Object[] getLinkList(String uniqueName, String namespace, String type) {
//		synchronized (IJRESContext.REFERENCE_UPDATE_SYNC_SIGNAL) {
//			Statement statement = null;
//			ResultSet rs = null;
//			Connection connect = null;
//			List<Object> tlist = new ArrayList<Object>();
//			try {
//				connect = MemDBConnectionPool.getInstance().getConnection();
//				statement = connect.createStatement();
//				
//				StringBuffer sql = new StringBuffer();
//				sql.append(String.format("select %s from %s where",IReferenceTable.MASTER_INDEX, table.getTablename()));
//				if (StringUtils.isNotBlank(uniqueName)) {
//					sql.append(String.format(" %s='%s' and", IReferenceTable.SLAVE_NAME,uniqueName));
//				}
//				sql.append(String.format(" %s='%s'",IReferenceTable.SLAVE_TYPE,type ));
//				sql.append(String.format(" and %s='%s'",IReferenceTable.SLAVE_NAMESPACE,namespace ));
//				
//				rs = statement.executeQuery(sql.toString());
//				
//				// �ж����޼�¼
//				while (rs.next()) {
//					tlist.add(getResouceByIndex(rs.getString(IReferenceTable.MASTER_INDEX)));
//				} 
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					// �黹����
//					try {rs.close();} catch (Exception e2) {}
//					try {statement.close();} catch (Exception e2) {}
//					MemDBConnectionPool.getInstance().returnConnection(connect);
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//			return tlist.toArray();
//		}
//	}
//	
//	public Object getResouceByIndex(String index) {
//		for(ITableDataProvider provider:tabledataprovider){
//			if(provider.contains(index)){
//				return provider.getDataByIndex(index);
//			}
//		}
//		return null;
//	}

}
