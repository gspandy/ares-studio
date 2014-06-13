/**
 * Դ�������ƣ�AbstractSheetHandler.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Sheet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.excel.ISheetHandler;
import com.hundsun.ares.studio.core.excel.Module;
import com.hundsun.ares.studio.core.excel.Resource;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.util.log.Log;
import com.hundsun.ares.studio.core.util.log.Log.Location;

/**
 * ISheetHanlder�ĳ�����ʵ��
 * @author sundl
 *
 */
public abstract class AbstractSheetHandler implements ISheetHandler{

	protected SheetParser parser;
	protected Log log;
	
	protected Sheet sheet;
	protected Module module;
	protected List<Resource> resourceList = new ArrayList<Resource>();
	
	protected Location resStartLoc;
	
	@Override
	public void init(SheetParser parser, Log log) {
		this.parser = parser;
		this.log = log;
	}
	
	protected Location generateLocation() {
		if (sheet != null && parser.exlParser != null) {
			Location loc =  Log.createLocation(sheet.getSheetName(), parser.getCurrentRow(), -1);
			loc.file = parser.exlParser.file.getName();
			return loc;
		}
		else
			return Log.createLocation(null, parser.getCurrentRow(), -1);
	}
	
	protected IARESProject getProject() {
		return (IARESProject) parser.exlParser.context.get("project");
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#startSheet(org.apache.poi.hssf.usermodel.HSSFSheet)
	 */
	@Override
	public void startSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#startArea(java.lang.String)
	 */
	@Override
	public void startArea(String startTag) {
		resStartLoc = generateLocation();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#startBlock(java.lang.String, int)
	 */
	@Override
	public void startBlock(String startTag, int type) {
		if (StringUtils.equals(startTag, "ģ��������") ||
				StringUtils.equals(startTag, "ģ��Ӣ����")
				|| StringUtils.equals(startTag, "ģ����")) {
			module = new Module();
		}
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#keyValue(java.lang.String, java.lang.String)
	 */
	@Override
	public void keyValue(String key, String value) {
		// ����ģ����Ӣ������Ϣ
		String blockTag = parser.getCurrentBlockTag();
		if (StringUtils.equals(blockTag, "ģ��������") || StringUtils.equals(blockTag, "ģ����")
					|| StringUtils.equals(blockTag, "ģ��Ӣ����")) {
			if (StringUtils.equals(key, "ģ��������")) {
				module.cName = value;
			} else if (StringUtils.equals(key, "ģ����") 
							|| StringUtils.equals(key, "ģ��Ӣ����")) {
				module.name = value;
				String correctName = correctResName(module.name);
				if (correctName != null) {
					module.name = correctName;
					log.warn(String.format("ģ����[%s]�����������淶�����滻Ϊ[%s]", value, correctName), generateLocation());
				}
			} else {
				log.error("�޷��������ԣ�" + key , generateLocation());
			}
		}
	}
	
	public static String correctResName(String name) {
		String result = null;
		if (StringUtils.contains(name, '(') || StringUtils.contains(name, ')')) {
			result = StringUtils.replace(name, "(", "_");
			result = StringUtils.replace(result, ")", "");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#header(java.lang.String[])
	 */
	@Override
	public void header(String[] header) {
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#row(java.lang.String[])
	 */
	@Override
	public void row(String[] row) {
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endBlock()
	 */
	@Override
	public void endBlock() {
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endArea()
	 */
	@Override
	public void endArea() {
		
	}
	
	protected void resourceFound(Resource res) {
		this.resourceList.add(res);
		res.startLoc = resStartLoc;
		res.endLoc = generateLocation();
		
		// ������Դ��Ϊ�յ����
		if (StringUtils.isEmpty(res.name)) {
			if (res.info instanceof BasicResourceInfo) {
				BasicResourceInfo basicInfo = (BasicResourceInfo) res.info;
				String id = ((BasicResourceInfo) res.info).getObjectId();
				if (!StringUtils.isEmpty(id)) {
					// Ӣ����Ϊ�յ����˶���ţ������ö����������Ӣ����
					String generatedName = "r_" + id;
					basicInfo.setName(generatedName);
					res.name = generatedName;
					log.warn(String.format("��ԴӢ����Ϊ��, ����ʱ��ʹ�ö�����Զ������ļ�����%s", res.getDescription()), generateLocation());
				} else {
					log.error(String.format("��ԴӢ����Ϊ�գ������ҲΪ�գ��޷����룺%s", res.getDescription()), res.startLoc);
				}
			} else {
				log.error(String.format("��ԴӢ����Ϊ�գ��޷����룺%s", res.getDescription()), generateLocation());
			}
		} else {
			// ������Դ���еķǷ��ַ�������()
			String correctName = correctResName(res.name);
			if (correctName != null) {
				log.warn(String.format("��Դ��[%s]�����������淶�����滻Ϊ[%s]", res.name, correctName), generateLocation());
				res.name = correctName;
			}
		}
		
		if (res.info instanceof JRESResourceInfo) {
			JRESResourceInfo info = (JRESResourceInfo) res.info;
			String id = info.getObjectId();
			Collection<RevisionHistory> histories = parser.exlParser.histories.get(id);
			for (RevisionHistory his : histories) {
				info.getHistories().add(EcoreUtil.copy(his));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.biz.excel.ISheetHandler#endSheet()
	 */
	@Override
	public void endSheet() {
		if (parser.exlParser != null)
			parser.exlParser.resources.putAll(module, resourceList);
		
		// �˴�����һ����Դ�Ƿ����ظ�����Դ��
		Multimap<String, Resource> resMap = ArrayListMultimap.create();
		for (Resource res : resourceList) {
			// ��Դ��Ϊ��Ҳ����һ�ִ����Ѿ��������ط������˼�飬�˴��Ͳ����ظ�
			if (!StringUtils.isEmpty(res.name)) {
				resMap.put(res.name, res);
			}
		}
		
		for (String name : resMap.keySet()) {
			if (resMap.get(name).size() > 1) {
				Collection<Resource> resources = resMap.get(name);
				
				// �ظ�����Դ�Ƿ񶼾��ж����
				boolean noObjIdIsNull = true;
				for (Resource r : resources) {
					BasicResourceInfo brInfo = (BasicResourceInfo) r.info;
					if (StringUtils.isEmpty(brInfo.getObjectId())) {
						noObjIdIsNull = false;
						break;
					} else {
						continue;
					}
				}
				
				List<String> resNames = new ArrayList<String>();
				List<String> resLocations = new ArrayList<String>();
				
				for (Resource r : resources) {
					resLocations.add(r.startLoc == null ? "" : String.valueOf(r.startLoc.row));
					if (r.info instanceof BasicResourceInfo) {
						BasicResourceInfo brInfo = (BasicResourceInfo) r.info;
						String id = brInfo.getObjectId();
						// ��Դ���ظ�������£��������Ų�Ϊ�գ������ö���ż�ǰ׺��Ϊ��Դ�������⸲��
						// �����Դ���ظ��������ҲΪ�գ��Ǿ�û�취�ˣ��Ⱥ󴴽�������Դ������ʱ����ʾ����
						if (noObjIdIsNull) {
							String generatedName = "r_" + id;
							brInfo.setName(generatedName);
							r.name = generatedName;
							resNames.add(String.format("(%s)%s", brInfo.getObjectId(), brInfo.getChineseName()));
						} 
					}
				}
				if (noObjIdIsNull) {
					String resNamesStr = StringUtils.join(resNames, ',');
					log.warn(String.format("����������Դ��Ӣ����(%s)�ظ��������ļ�ʱ��ʹ�ö�����Զ������ļ�������Դ�� %s", name, resNamesStr), generateLocation());
				} else {
					String resLocStr = StringUtils.join(resLocations, ',');
					log.error(String.format("���ֶ����Դ��Ӣ����(%s)�ظ����Ҳ���ȫ�����ж���ţ��޷��Զ���������Դ����ֻ�ܵ�������֮һ����Դ�����кŷֱ�Ϊ��%s", name, resLocStr), generateLocation());
				}
			}
		}
		resMap.clear();
		resMap = null;
		
		sheet = null;
	}
	
}
