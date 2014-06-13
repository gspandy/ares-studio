/**
 * Դ�������ƣ�JRESStatisticProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.core
 * ����˵����JRES Studio�Ļ����ܹ���ģ�͹淶
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.context.statistic.IResourceTable;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * 
 * @author sundl
 * @Deprecated ע�⣬�˽ӿ������ص��������⣬ʹ��ReferenceManager��ؽӿڷ������档
 */
@Deprecated
public class JRESStatisticProvider implements /*IResourceChangeListener,*/IResStatisticProvider{
	
	private static Logger log = Logger.getLogger(JRESStatisticProvider.class);
	
	/**
	 * ��ǰ����
	 */
	private IARESProject project;
	
	private boolean useRequiredProjects;
	
	/**
	 * @param project
	 * @param useRequiredProjects
	 */
	public JRESStatisticProvider(IARESProject project,
			boolean useRequiredProjects) {
		super();
		this.project = project;
		this.useRequiredProjects = useRequiredProjects;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#isResouceExist(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isResouceExist(String uniqueName, String namespace,
			String restype) {
		return getResourceCount(uniqueName, namespace, restype) > 0;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#isResouceUnique(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isResouceUnique(String uniqueName, String namespace, String restype) {
		return getResourceCount(uniqueName, namespace, restype) == 1;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResouce(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Object[] getResouce(String uniqueName, String namespace,
			String restype) {
		List<ReferenceInfo> infoList = null;
		// FIXME JRESĿǰ��ʹ�������ռ�
		infoList = ReferenceManager.getInstance().getReferenceInfos(project, restype, uniqueName, useRequiredProjects);
		
//		if (StringUtils.equals(namespace, IResourceTable.Scope_IGNORE_NAMESPACE)) {
//			infoList = ReferenceManager.getInstance().getReferenceInfos(project, restype, uniqueName, useRequiredProjects);
//		} else {
//			infoList = ReferenceManager.getInstance().getReferenceInfos(project, restype, uniqueName, namespace, useRequiredProjects);
//		}
		
		
		return Collections2.transform(infoList, new Function<ReferenceInfo, Map<Object, Object>>() {

			@Override
			public Map<Object, Object> apply(ReferenceInfo from) {
				return transform(from);
			}
		}).toArray();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResouceByType(java.lang.String)
	 */
	@Override
	public Object[] getResouceByType(String restype) {
		List<ReferenceInfo> infoList = ReferenceManager.getInstance().getReferenceInfos(project, restype, useRequiredProjects);
		
		
		return Collections2.transform(infoList, new Function<ReferenceInfo, Map<Object, Object>>() {

			@Override
			public Map<Object, Object> apply(ReferenceInfo from) {
				return transform(from);
			}
		}).toArray();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResouceByTypes(java.lang.String[])
	 */
	@Override
	public Object[] getResouceByTypes(String[] restypes) {
		// ������ܳ�������Ĳ�������
		List<ReferenceInfo> infoList = new ArrayList<ReferenceInfo>();
		for (String type : restypes) {
			infoList.addAll(ReferenceManager.getInstance().getReferenceInfos(project, type, useRequiredProjects));
		}
		
		return Collections2.transform(infoList, new Function<ReferenceInfo, Map<Object, Object>>() {

			@Override
			public Map<Object, Object> apply(ReferenceInfo from) {
				return transform(from);
			}
		}).toArray();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResourceCount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int getResourceCount(String uniqueName, String namespace,
			String restype) {
		List<ReferenceInfo> infoList = null;
		if (StringUtils.equals(namespace, IResourceTable.Scope_IGNORE_NAMESPACE)) {
			infoList = ReferenceManager.getInstance().getReferenceInfos(project, restype, uniqueName, useRequiredProjects);
		} else {
			infoList = ReferenceManager.getInstance().getReferenceInfos(project, restype, uniqueName, namespace, useRequiredProjects);
		}
		return infoList.size();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResouceByIndex(java.lang.String)
	 */
	@Override
	public Object getResouceByIndex(String index) {
		throw new UnsupportedOperationException();
	}
	
	private static Map<Object, Object> transform(ReferenceInfo ref) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(IResourceTable.RES_NAME, ref.getRefName());
		map.put(IResourceTable.RES_NAMESPACE, ref.getRefNamespace());
		map.put(IResourceTable.RES_TYPE, ref.getRefType());
		map.put(IResourceTable.TARGET_OWNER, ref.getObject());
		map.put(IResourceTable.TARGET_RESOURCE, ref.getResource());
		return map;
	}
	
	
//	/**
//	 * ��Դ��
//	 */
//	public ResouceTable table = new ResouceTable();
//	
//	/**
//	 * �����ṩ���б�
//	 */
//	private List<ITableDataProvider> tabledataprovider = new ArrayList<ITableDataProvider>();
	
//	public JRESStatisticProvider(IARESProject project){
//		this.project = project;
//		Connection connect = null;
//		//����
//		try {
//			connect = MemDBConnectionPool.getInstance().getConnection();
////			table.setTextTable(true);   //���ı���ʽ�洢
//			table.createTable(connect, MemTable.genID());
//			log.debug(String.format("ΪProject%s����: %s", project.getProject().getName(), table.getTablename()));
//			
//			//Ĭ��
//			DefaultResourceProvider defaultResourceProvider = new DefaultResourceProvider();
//			defaultResourceProvider.setArgs(project,table);
//			tabledataprovider.add(defaultResourceProvider);
//			
//			ITableProviderFactory[] providers = TableDataProviderRegistry.getInstance().getAdapterByType(IValidateConstant.EXTENTION_TYPE_STATIC_TABLE_PROVIDER);
//			for(ITableProviderFactory factory:providers){
//				for(ITableDataProvider item:factory.createProviders()){
//					item.setArgs(project,table);
//					tabledataprovider.add(item);
//				}
//			}
//			//��ʼ��
//			ArrayList<IContextUpdateSource> tmplist = new ArrayList<IContextUpdateSource>();
//			tmplist.add(new DefaultContextUpdateSource(ITableDataProvider.UPDATETYPE_INIT,null));
//			
//			update(tmplist);
//			
//			ARESCore.addElementListener(new IARESElementChangeListener() {
//				@Override
//				public void elementChanged(ARESElementChangedEvent event) {
//					if (event.getType() == ARESElementChangedEvent.RES_PATH) {
//						IARESProject changedProject = (IARESProject) event.getElement();
//						if (changedProject.equals(JRESStatisticProvider.this.project)) {
//							log.debug(String.format("��Ŀ: %s respath �����仯����������...", changedProject.getProject()));
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
//		}finally{
//			MemDBConnectionPool.getInstance().returnConnection(connect);
//		}
//	}
//	
//	public ResouceTable getTable() {
//		return table;
//	}
//	
//	
//	public void update(final Object context){
//		log.debug(String.format("������Ŀ%s����", this.project.getProject().getName()));
//		JRESTreecacheManager.getInstance().remove(new Object[]{IJRESContext.SELECT_CHACHE});
//		synchronized (IJRESContext.UPDATE_SYNC_SIGNAL) {  //����ʱ��ס��
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
//			log.debug("Drop table: " + table.getTablename());
//			statement.execute(String.format("drop table %s;", table.getTablename()));
//			log.debug("Drop �ɹ�");
//			statement.close();
//		} catch (Throwable e) {
////			System.out.println("������ñ�����ʧ�ܡ�"+e.getMessage());
//			//log.debug("Drop ʧ��: " , e);
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
//					JRESTreecacheManager.getInstance().remove(new Object[]{IJRESContext.SELECT_CHACHE});
//					// ContextManager�б�������Ŀ��ӦProvider���б����б���ɾ�����´���ȡ��ʱ�򣬾ͻ����³�ʼ��һ��
//					log.debug(String.format("----�����Ŀ %s ��ContextProviderManager�еĻ���...", project.getName()));
//					ContextProviderManager.getInstance().removeContextProvider(aresProj, IValidateConstant.KEY_STATIC_PROVIDER);
//					log.debug(String.format("----�����Ŀ %s ��JRESContextManager�еĻ���...", project.getName()));
//					JRESContextManager.removeStatisticProvider(aresProj);
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
//
//	}
//	
//	private void handleResourceChangeDelta(IResourceDelta delta) {
//		final Map<Object, List<IContextUpdateSource>> changemap = new HashMap<Object,  List<IContextUpdateSource>>();
//		try {
//			delta.accept(new IResourceDeltaVisitor() {
//
//				public boolean visit(IResourceDelta delta) throws CoreException {
//					// ÿ��providerֻ�����Լ�����Ŀ������
//					IResource res = delta.getResource();
//					if (res != null) {
//						IProject proj = res.getProject();
//						if (proj != null) {//��projectΪ��˵���ǹ������䣬��������
//							if (!proj.equals(JRESStatisticProvider.this.project.getProject())) {
//								return false;
//							}
//						}
//					}
//					
//					if (delta.getResource().getType() == IResource.PROJECT) {
//						// ֻ��ARES������Ҫ�ռ���Ϣ
//						IProject project = (IProject) delta.getResource();
//						return JRESProjectUtils.isARESProject(project);
//					}
//					
//					if (delta.getResource().getType() == IResource.FOLDER) {
//						IFolder folder = (IFolder) delta.getResource();
//						// ֻ�����ǵ���Դ��Ҫ�ռ���Ϣ
//						if (folder.getParent() != null && folder.getParent().getType() == IResource.PROJECT) {
//							IARESProject aresProj = ARESCore.create(delta.getResource().getProject());
//							if (aresProj != null && aresProj.exists()) {
//								if (aresProj.getModuleRoot(folder) != null) {
//									return true;
//								}
//							}
//							
//							return false;
//						}
//						return true;
//					}
//					if (delta.getResource().getType() == IResource.FILE) {
//						IProject tmp = (IProject) delta.getResource().getProject();
//						if (!changemap.containsKey(tmp)) {
//							changemap.put(tmp, new ArrayList<IContextUpdateSource>());
//						}
//						
//						if (delta.getKind() == IResourceDelta.REMOVED ) {
//							log.debug(String.format("�ļ��� %s ɾ��...", delta.getResource().getFullPath()));
//							changemap.get(tmp).add(new DefaultContextUpdateSource(IJRESContext.UPDATETYPE_DEL, new Object[]{delta.getResource()}));
//						}else if(delta.getKind() == IResourceDelta.ADDED){
//							changemap.get(tmp).add(
//									new DefaultContextUpdateSource(IJRESContext.UPDATETYPE_ADD,new Object[]{delta.getResource()}
//							));
//							
//						}else if (delta.getKind() == IResourceDelta.CHANGED) {
//							// ����������ݱ仯����Ҫ�ռ���Ϣ
//							if ( (delta.getFlags() & IResourceDelta.CONTENT) == 0) {
//								return false;
//							}
//							String filename = delta.getResource().getName();
//							if (IJRESContext.PROJECT_XML.equals(filename)) {
//								//����������Դ����
//								changemap.get(tmp).add(
//										new DefaultContextUpdateSource(IJRESContext.PROJECT_XML,new Object[]{delta.getResource()}
//								));
//							}
//							String fileExtension = delta.getResource().getFileExtension();
//							changemap.get(tmp).add(
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
//		if(changemap.containsKey(this.project.getProject()) && !changemap.get(this.project.getProject()).isEmpty()){
//			update(changemap.get(project.getProject()));
//		}
//	}
//	
//	//Ĭ�ϼ������
//	PreparedStatement countstatement = null;
//	/**
//	 * Ĭ�ϵļ������
//	 * @return
//	 */
//	private PreparedStatement getDefaultCountStatement(){
//		if(null == countstatement){
//			Connection connect = null;
//			try {
//				connect = MemDBConnectionPool.getInstance().getConnection();
//				
//				StringBuffer sql = new StringBuffer();
//				sql.append(String.format("select count(*) as num from %s", table.getTablename()));
//				sql.append(String.format(" where %s=?", IResourceTable.RES_NAME));
//				sql.append(String.format(" and %s=?",IResourceTable.RES_TYPE ));
//				sql.append(String.format(" and %s=?",IResourceTable.RES_NAMESPACE ));
//				countstatement = connect.prepareStatement(sql.toString());
//			} catch (Exception e) {
//			}finally{
//				// �黹����
//				MemDBConnectionPool.getInstance().returnConnection(connect);
//			}
//		}
//		return countstatement;
//	}
//	
//	
//	//Ĭ�ϼ������
//	PreparedStatement countstatementIgnoreNamespace = null;
//	/**
//	 * ����������namespace
//	 * @return
//	 */
//	private PreparedStatement getCountStatementIgnoreNamespace(){
//		if(null == countstatementIgnoreNamespace){
//			Connection connect = null;
//			try {
//				connect = MemDBConnectionPool.getInstance().getConnection();
//				
//				StringBuffer sql = new StringBuffer();
//				sql.append(String.format("select count(*) as num from %s", table.getTablename()));
//				sql.append(String.format(" where %s=?", IResourceTable.RES_NAME));
//				sql.append(String.format(" and %s=?",IResourceTable.RES_TYPE ));
//				countstatementIgnoreNamespace = connect.prepareStatement(sql.toString());
//			} catch (Exception e) {
//			}finally{
//				// �黹����
//				MemDBConnectionPool.getInstance().returnConnection(connect);
//			}
//		}
//		return countstatementIgnoreNamespace;
//	}
//	
//	
//	public int getResourceCount(String uniqueName, String namespace,
//			String restype){
//		synchronized (table) {
//			ResultSet rs = null;
//			PreparedStatement stt = null;
//			try {
//				//��ȡԤ�������
//				if(IResourceTable.Scope_IGNORE_NAMESPACE.equals(namespace)){
//					//���������ռ�Ԥ�������
//					stt = getCountStatementIgnoreNamespace();
//					
//					stt.setString(1, uniqueName);
//					stt.setString(2, restype);
//				}else{
//					//Ĭ��Ԥ�������
//					stt = getDefaultCountStatement();
//					
//					stt.setString(1, uniqueName);
//					stt.setString(2, restype);
//					stt.setString(3, namespace);
//				}
//				
//				
//				rs = stt.executeQuery();
//				
//				// �ж����޼�¼
//				if (rs.next()) {
//					return rs.getInt("num");
//				} 
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					rs.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//			return 0;
//		}
//		
//	}
//
//	/* (non-Javadoc)
//	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#isResouceExist(java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public boolean isResouceExist(String uniqueName, String namespace,
//			String restype) {
//		return getResourceCount(uniqueName, namespace, restype) > 0;
//	}
//	
//
//	/* (non-Javadoc)
//	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#isResouceUnique(java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public boolean isResouceUnique(String uniqueName, String namespace,
//			String restype) {
//		return getResourceCount(uniqueName, namespace, restype) < 2;
//	}
//
//	PreparedStatement resourceStatement = null;
//	
//	private PreparedStatement getResourceStatement(){
//		if(null == resourceStatement){
//			Connection connect = null;
//			try {
//				connect = MemDBConnectionPool.getInstance().getConnection();
//				StringBuffer sql = new StringBuffer();
//				sql.append(String.format("select %s from %s",IResourceTable.RES_INDEX, table.getTablename()));
//				sql.append(String.format(" where %s=?", IResourceTable.RES_NAME));
//				sql.append(String.format(" and %s=?",IResourceTable.RES_TYPE ));
//				sql.append(String.format(" and %s=?",IResourceTable.RES_NAMESPACE ));
//				resourceStatement = connect.prepareStatement(sql.toString());
//			} catch (Exception e) {
//			}finally{
//				// �黹����
//				MemDBConnectionPool.getInstance().returnConnection(connect);
//			}
//		}
//		return resourceStatement;
//	}
//	
//	@Override
//	public Object[] getResouce(String uniqueName, String namespace,
//			String restype) {
//		synchronized (table) {
//			ResultSet rs = null;
//			List<Object> tlist = new ArrayList<Object>();
//			try {
//				
//				PreparedStatement tt = getResourceStatement();
//				tt.setString(1, uniqueName);
//				tt.setString(2, restype);
//				tt.setString(3, namespace);
//				
//				rs = tt.executeQuery();
//				
//				// �ж����޼�¼
//				while (rs.next()) {
//					Object obj = getResouceByIndex(rs.getString(IResourceTable.RES_INDEX));
//					tlist.add(obj);
//				} 
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					rs.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//			return tlist.toArray();
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResouceByIndex(java.lang.String)
//	 */
//	@Override
//	public Object getResouceByIndex(String index) {
//		for(ITableDataProvider provider:tabledataprovider){
//			if(provider.contains(index)){
//				return provider.getDataByIndex(index);
//			}
//		}
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResouceByType(java.lang.String)
//	 */
//	@Override
//	public Object[] getResouceByType(String restype) {
//		synchronized (table) {
//			Statement statement = null;
//			ResultSet rs = null;
//			Connection connect = null;
//			List<Object> tlist = new ArrayList<Object>();
//			try {
//				connect = MemDBConnectionPool.getInstance().getConnection();
//				statement = connect.createStatement();
//				
//				StringBuffer sql = new StringBuffer();
//				sql.append(String.format("select %s from %s",IResourceTable.RES_INDEX, table.getTablename()));
//				sql.append(String.format(" where %s='%s'",IResourceTable.RES_TYPE,restype ));
//				
//				rs = statement.executeQuery(sql.toString());
//				
//				// �ж����޼�¼
//				while (rs.next()) {
//					tlist.add(getResouceByIndex(rs.getString(IResourceTable.RES_INDEX)));
//				} 
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					// �黹����
//					MemDBConnectionPool.getInstance().returnConnection(connect);
//					
//					statement.close();
//					rs.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//			return tlist.toArray();
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see com.hundsun.ares.studio.jres.context.IResStatisticProvider#getResouceByTypes(java.lang.String[])
//	 */
//	@Override
//	public Object[] getResouceByTypes(String[] restypes) {
//		synchronized (table) {
//			Statement statement = null;
//			ResultSet rs = null;
//			Connection connect = null;
//			List<Object> tlist = new ArrayList<Object>();
//			try {
//				connect = MemDBConnectionPool.getInstance().getConnection();
//				statement = connect.createStatement();
//				
//				StringBuffer types = new StringBuffer();
//				for(int i = 0;i < restypes.length;i++){
//					if(i == (restypes.length  -1)){
//						types.append(String.format("'%s'", restypes[i]));
//					}else{
//						types.append(String.format("'%s',", restypes[i]));
//					}
//				}
//				
//				StringBuffer sql = new StringBuffer();
//				sql.append(String.format("select %s from %s",IResourceTable.RES_INDEX, table.getTablename()));
//				sql.append(String.format(" where %s in (%s)",IResourceTable.RES_TYPE,types.toString() ));
//				
//				rs = statement.executeQuery(sql.toString());
//				
//				// �ж����޼�¼
//				while (rs.next()) {
//					tlist.add(getResouceByIndex(rs.getString(IResourceTable.RES_INDEX)));
//				} 
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					// �黹����
//					MemDBConnectionPool.getInstance().returnConnection(connect);
//					
//					statement.close();
//					rs.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//			return tlist.toArray();
//		}
//	}

}
