/**
 * Դ�������ƣ�ExcelBuilder.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.jres.service.stock;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.biz.ARESObject;
import com.hundsun.ares.studio.biz.BizPackage;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizResType;
import com.hundsun.ares.studio.biz.excel.export.AbstractBuilder;
import com.hundsun.ares.studio.biz.excel.export.Area;
import com.hundsun.ares.studio.biz.excel.export.Group;
import com.hundsun.ares.studio.biz.excel.export.KeyValueBlock;
import com.hundsun.ares.studio.biz.excel.export.TableBlock;
import com.hundsun.ares.studio.biz.excel.export.TextBlock;
import com.hundsun.ares.studio.biz.excel.export.writer.ExcelWriter;
import com.hundsun.ares.studio.biz.excel.export.writer.GroupWriter;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.excel.AbstractSheetHandler;
import com.hundsun.ares.studio.core.excel.BlockTypes;
import com.hundsun.ares.studio.core.excel.ISheetHandler;
import com.hundsun.ares.studio.core.excel.POIUtils;
import com.hundsun.ares.studio.core.excel.SheetParser;
import com.hundsun.ares.studio.core.excel.handler.IPropertyHandlerFactory;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.core.util.ResourcesUtil;
import com.hundsun.ares.studio.core.util.log.Log;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryList;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.StandardFieldList;
import com.hundsun.ares.studio.jres.service.Service;
import com.hundsun.ares.studio.jres.service.ServicePackage;

/**
 * @author sundl
 *
 */
public class ExcelBuilder extends AbstractBuilder {
	
	public ExcelBuilder(IARESProject project) {
		super(project);
	}

	
	/** ������ģ��Ľӿ����� */
	static String[] BASIC_PRO_INTERNET = new String[] {
		"���ܺ�", "�Ϲ��ܺ�", "��������", 
		"��������", "�汾��", "���������", 
		"Ӣ����", "Ͷ���߷�Χ", 
		"��ʽ", "HTTP����ʽ", "������Ȩ����", 
		"ҵ��Χ", "��Ʒ��Χ", "����״̬", 
		"��������"
	};
	
	/** ��ͨģ���µĽӿڻ�����Ϣ���� */
	static String[] BASIC_PRO_NORMAL = new String[] {
		"���ܺ�", "�Ϲ��ܺ�", "��������", 
		"��������", "�汾��", "���������", 
		"Ӣ����", "Ͷ���߷�Χ", 
		/*"��ʽ", "HTTP����ʽ", "������Ȩ����", */ // ֻ�л�����ģ��������Щ����
		"ҵ��Χ", "��Ʒ��Χ", "����״̬", 
		"��������"
	};
	
	static String[] OBJ_BASIC_INFO_PROPERTIES = new String[] {
		"������", "��������",
		"����������", "����״̬"
	};
	
	// ����ӿڻ�����Ϣ������ռλ��Ϣ�� ���ִ���ռ�õ�Ԫ��ĸ���
	static int[] BASIC_INFO_SPANS_INTERNET = new int[] {
		1, 1, 1,
		1, 1, 1,
		3, 1,
		1, 1, 1,
		1, 1, 1,
		5
	};
	static int[] BASIC_INFO_SPANS_NORMAL = new int[] {
		1, 1, 1,
		1, 1, 1,
		3, 1,
		/* 1, 1, 1, */
		1, 1, 1,
		5
	};
	
	static int[] OBJ_BASIC_INFO_SPANS = new int[] {
		2, 1,
		2, 1
	};
	
	// �������������ͷ
	static String[] INPUT_PARAM_PROPERTIES = new String[] {"�������", "������", "����", "˵��", "����", "ȱʡֵ"};
	static String[] OUTPUT_PARAM_PROPERTIES = new String[] {"�������", "������", "����", "˵��", "����", "ȱʡֵ"};
	static String[] ERROR_INFO_PROPERTIES = new String[] {"����˵��", "�����", "������Ϣ", "����˵��", "���󼶱�", ""};
	// �������Ա�ͷ
	static String[] OBJ_ATTR_PROPERTIES = new String[] {"��������", "������", "����", "˵��", };

	
	static String[] SHEET_NAMES = new String[] {
		"����", "�汾ҳ", "Լ������", "��׼�ֶ�", "�����ֵ�", "�����б�", "����-ȫ��",
		"�����б�", "����ӿ�-ȫ��", "��Ϣ�������", "���ƽӿ��б�", "���ƽӿ�"
	};
	
	Multimap<IARESModule, IARESResource> services;
	
	String fileName;
	// ��ϵͳ����Ӧ����ڶ���
	String subsysName;
	IARESModule module;
	// ģ��������Ӧ��������У����磨�ܱ߽ӿڹ淶���ڻ�����Ȩ��V1.4��
	String moduleName;
	// ģ���ļ���·��
	String templatePath;
	
	// �ݴ��׼�ֶκ������ֵ���Ϣ�����ⷴ������
	private Map<String, StandardField> stdFields = new HashMap<String, StandardField>();
	private Map<String, DictionaryType> dictTypes = new HashMap<String, DictionaryType>();
	
	// ���õ��������ֵ���
	private Set<String> uesedDicts = new HashSet<String>();
	private Set<String> usedStdFields = new HashSet<String>();
	
	List<String> bizScopes;
	
	private Workbook workbook;
	
	public void build() {
		// �ȴ���ֻ��һ�ֵ������������Ҫ��������֧�ֶ�ģ�鷽ʽ
		if (services.keySet().size() == 1) {
			module = (IARESModule) services.keySet().toArray()[0];
			// ��ʼ����Դ���ݣ�Ԫ����(��׼�ֶΡ������ֵ�)�� ������Դ�б������б�
			initMetadata();
			
			List<Service> servicesList = new ArrayList<Service>();
			for (IARESResource res : services.get(module)) {
				Service service = null;
				try {
					service = res.getInfo(Service.class);
				} catch (ARESModelException e) {
					logger.error(e);
				}
				if (service == null)
					continue;
				servicesList.add(service);
				// �ռ���������õ��ı�׼�ֶκ������ֵ���Ϣ
				collectStdFiledAndDicts(service);
			}
			
			Collections.sort(servicesList, new Comparator<Service>() {
				@Override
				public int compare(Service o1, Service o2) {
					return StringUtils.defaultString(o1.getObjectId()).compareTo(StringUtils.defaultString(o2.getObjectId()));
				}
			});
			
			IARESResource[] objResources = module.getARESResources(IBizResType.Object);
			List<ARESObject> objects = new ArrayList<ARESObject>();
			for (IARESResource res : objResources) {
				try {
					ARESObject object = res.getInfo(ARESObject.class);
					objects.add(object);
				} catch (ARESModelException e) {
					logger.error(e);
				}
			}
			
			// ����ģ���������ļ���
			subsysName = ARESElementUtil.getModuleCName(ARESElementUtil.getTopModule(module));
			String moduleFullName = ResourcesUtil.getChineseFileName("_", module);
			moduleName = ARESElementUtil.getModuleCName(module) + "�ӿڹ淶(" + StringUtils.join(bizScopes, ',') + ")";
			fileName = "d:\\" + moduleFullName + "(" +StringUtils.join(bizScopes, ',') + ").xls";
			
			// ����ҳ���汾ҳ���в���ģ�����ʣ��������滻���е�����
			// ģ���е�����ҳ����Ϊ�̶�����ҳ��
			try {
				parseTemplate();
			} catch (IOException e) {
				logger.error(e);
			}
			
			createStdFieldGroup();
			createDictGroup();
			createObjectGroup(objects);
			createServiceGroup(servicesList);
			// Ϊ�˴��������ӣ��б�����������Service����֮��������
			createServiceListGroup(servicesList);
			createObjListGroup(objects);
		}
	}
	
	private void initMetadata() {
		IARESResource[] resource;
		try {
			resource = project.findResource(IMetadataResType.StdField);
			if (resource.length > 0) {
				IARESResource stdResource = resource[0];
				StandardFieldList stdList = stdResource.getInfo(StandardFieldList.class);
				for (StandardField f : stdList.getItems()) {
					stdFields.put(f.getName(), EcoreUtil.copy(f));
				}
			}
			
			resource = project.findResource(IMetadataResType.Dict);
			if (resource.length > 0) {
				IARESResource dictResource = resource[0];
				DictionaryList dictionaryList = dictResource.getInfo(DictionaryList.class);
				for (DictionaryType dict : dictionaryList.getItems()) {
					dictTypes.put(dict.getName(), EcoreUtil.copy(dict));
				}
			}
		} catch (ARESModelException e) {
			logger.error(e);
		}

	}
	
	// ɨ���������ӿ��õ� �ı�׼�ֶκͶ�Ӧ�������ֵ�
	private void collectStdFiledAndDicts(Service service) {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(service.getInterface().getInputParameters());
		parameters.addAll(service.getInterface().getOutputParameters());
		for (Parameter p : parameters) {
			StandardField field = this.stdFields.get(p.getId());
			if (field != null) {
				usedStdFields.add(p.getId());
				String dict = field.getDictionaryType();
				if (StringUtils.isNotEmpty(dict))
					uesedDicts.add(dict);
			}
		}
	}
	
	// ���в���ģ�����ʵ�ҳ��
	private void parseTemplate() throws IOException {
		InputStream inputStream = null;
		if (StringUtils.isNotEmpty(templatePath)) {
			inputStream = new FileInputStream(templatePath);
		} else {
			URL url = ServiceStockExtPlugin.getDefault().getBundle().getEntry("tmpl.xls");
			inputStream = url.openStream();
		}
		try {
			workbook = new HSSFWorkbook(inputStream);
			setUpCover(workbook);
			setUpHisSheet(workbook);
			filterPushInterfaces(workbook);
			
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * ���÷���ҳ����Ϣ
	 * @param workbook
	 */
	private void setUpCover(Workbook workbook) {
		Sheet sheet = workbook.getSheet("����");
		if (sheet != null) {
			Row row = sheet.getRow(8);
			Cell cell = row.getCell(0);
			cell.setCellValue(subsysName);
			
			row = sheet.getRow(9);
			cell  = row.getCell(0);
			cell.setCellValue(moduleName);
			
			row = sheet.getRow(24);
			cell = row.getCell(0);
			cell.setCellValue(new SimpleDateFormat("yyyy��MM��dd��").format(new Date()));
		}
	}
	
	/**
	 * ���������ð汾ҳ��Ϣ��ģ��
	 * @param workbook
	 */
	private void setUpHisSheet(Workbook workbook) {
		Sheet sheet = workbook.getSheet("�汾ҳ");
		if (sheet != null) {
			SheetParser parser = new SheetParser();
			parser.areaTags.add("�޸İ汾");
			parser.blocks.put("�޸İ汾", BlockTypes.TABLE);
			Log log = new Log();
			
			final List<String> properties = new ArrayList<String>();
			final int[] startRow = new int[1];
			ISheetHandler handler = new AbstractSheetHandler() {
				@Override
				public void header(String[] header) {
					for (String h : header) {
						properties.add(h);
						startRow[0] = this.parser.getCurrentRow();
					}
				}
			};
			handler.init(parser, log);
			parser.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			parser.handlers.add(handler);
			parser.parse(sheet);
			
			List<RevisionHistory> histories = RevisionHistoryUtil.getHistories(module);
			TableBlock blcok = buildTableBlock(properties.toArray(new String[0]), null, histories, getPropertyHandlerFactory(CorePackage.Literals.REVISION_HISTORY));
			Group group = new Group();
			group.name = "�汾ҳ";
			
			Area area = new Area();
			area.blocks.add(blcok);
			group.areas.add(area);
			groups.add(group);
		}
	}
	
	// �������ƽӿڣ���ʱ����
	private void filterPushInterfaces(Workbook workbook) {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		List<Row> rowsToDelete = new ArrayList<Row>();

		Sheet sheet = workbook.getSheet("���ƽӿ��б�");
		if (sheet != null) {
			int rowCount = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rowCount; i++) {
				Row row = sheet.getRow(i);
				Cell cell = row.getCell(4);
				String bizScope = POIUtils.getCellStringValue(cell, evaluator);
				if (!bizScopes.contains(bizScope)) {
					rowsToDelete.add(row);
				}
			}
			int deletedRows = 0;
			for (Row row : rowsToDelete) {
				sheet.shiftRows(row.getRowNum() -deletedRows + 2, rowCount - 1, -1);
				deletedRows++;
			}
		}
		
	}
	
	
	private void createStdFieldGroup() {
		List<StandardField> standardFields = new ArrayList<StandardField>();
		for (String id : usedStdFields) {
			StandardField f = stdFields.get(id);
			standardFields.add(f);
		}
		
		Collections.sort(standardFields, new Comparator<StandardField>() {
			@Override
			public int compare(StandardField o1, StandardField o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		
		int i = 1;
		for (StandardField f : standardFields) {
			f.getData().put("id", String.valueOf(i++));
		}
		
		StdFieldBlockBuilder builder = new StdFieldBlockBuilder(project, standardFields);
		builder.build();
		groups.addAll(builder.getGroups());
	}
	
	private void createServiceListGroup(List<Service> serviceList) {
		ServiceListGroupBuilder builder = new ServiceListGroupBuilder(project, serviceList);
		builder.build();
		groups.addAll(builder.getGroups());
	}
	
	private void createDictGroup() {
		List<DictionaryType> dictList = new ArrayList<DictionaryType>();
		int i = 1;
		for (String id : uesedDicts) {
			DictionaryType type = dictTypes.get(id);
			if (type != null) {
				type.getData().put("id", String.valueOf(i++));
				dictList.add(type);
			}
		}
		DictGroupBuilder builder = new DictGroupBuilder(project, dictList);
		builder.build();
		groups.addAll(builder.getGroups());
	}
	
	private void createObjListGroup(List<ARESObject> objects) {
		if (objects == null || objects.isEmpty())
			return;
		ObjectListBuilder builder = new ObjectListBuilder(project, objects);
		builder.build();
		groups.addAll(builder.getGroups());
	}
	
	private void createObjectGroup(List<ARESObject> objects) {
		if (objects == null || objects.isEmpty())
			return;
		
		IPropertyHandlerFactory objectPropertyHandlerFactory = getPropertyHandlerFactory(BizPackage.Literals.ARES_OBJECT);
		IPropertyHandlerFactory paramPropertyHandlerFactory = getPropertyHandlerFactory(BizPackage.Literals.PARAMETER);
		Group group = new Group();
		groups.add(group);
		group.name = "����-ȫ��";
		for (ARESObject object : objects) {
			Area area = new Area();
			group.areas.add(area);
			KeyValueBlock basicInfoBlock = buildKeyValueBlock(object, OBJ_BASIC_INFO_PROPERTIES, BASIC_INFO_SPANS_INTERNET, objectPropertyHandlerFactory);
			basicInfoBlock.propertyPerLine = 2;
			area.blocks.add(basicInfoBlock);
			TableBlock inputParamBlock = buildTableBlock(OBJ_ATTR_PROPERTIES, TABLE_COL_STYLES, object.getProperties(), paramPropertyHandlerFactory);
			area.blocks.add(inputParamBlock);
			TextBlock hisTextBlock = buildTextBlock("�޸ļ�¼", 3, object, objectPropertyHandlerFactory, true);
			area.blocks.add(hisTextBlock);
		}
	}
	
	private void createServiceGroup(List<Service> services) {
		IPropertyHandlerFactory servicePropertyHandlerFactory = getPropertyHandlerFactory(ServicePackage.Literals.SERVICE);
		IPropertyHandlerFactory paramPropertyHandlerFactory = getPropertyHandlerFactory(BizPackage.Literals.PARAMETER);
		IPropertyHandlerFactory errorPropertyHandlerFactory = getPropertyHandlerFactory(BizPackage.Literals.ERROR_INFO);
		Group group = new Group();
		groups.add(group);
		group.name = "����ӿ�-ȫ��";
		String[] properties = null;
		int[] spans = null;
		if (StringUtils.startsWith(subsysName, "������")) {
			properties = BASIC_PRO_INTERNET;
			spans = BASIC_INFO_SPANS_INTERNET;
		} else {
			properties = BASIC_PRO_NORMAL;
			spans = BASIC_INFO_SPANS_NORMAL;
		}
		for (Service service : services) {
			Area area = new Area();
			group.areas.add(area);
			KeyValueBlock basicInfoBlock = buildKeyValueBlock(service, properties, spans, servicePropertyHandlerFactory);
			area.blocks.add(basicInfoBlock);
			TableBlock inputParamBlock = buildTableBlock(INPUT_PARAM_PROPERTIES, TABLE_COL_STYLES, service.getInterface().getInputParameters(), paramPropertyHandlerFactory);
			area.blocks.add(inputParamBlock);
			TableBlock outputParamBlock = buildTableBlock(OUTPUT_PARAM_PROPERTIES, TABLE_COL_STYLES, service.getInterface().getOutputParameters(), paramPropertyHandlerFactory);
			area.blocks.add(outputParamBlock);
			TextBlock bizDescription = buildTextBlock("ҵ��˵��", service,  servicePropertyHandlerFactory, false);
			area.blocks.add(bizDescription);
			TableBlock errorInfoBlock = buildTableBlock(ERROR_INFO_PROPERTIES, TABLE_COL_STYLES, service.getInterface().getErrorInfos(), errorPropertyHandlerFactory);
			area.blocks.add(errorInfoBlock);
			TextBlock hisTextBlock = buildTextBlock("�޸ļ�¼", service, servicePropertyHandlerFactory, true);
			area.blocks.add(hisTextBlock);
		}
	}
	
	public void writeFile() throws IOException {
		ExcelWriter writer = new ExcelWriter(workbook, groups) {
			@Override
			protected GroupWriter createGroupWriter(Group group) {
				GroupWriter writer = super.createGroupWriter(group);
				if (group.name.equals("�汾ҳ")) {
					writer.newSheet = false;
					writer.startRow = 11;
				}
				return writer;
			}
		};
		writer.write();
		
		setSheetOrders(SHEET_NAMES);
		int length = workbook.getNumberOfSheets();
		for (int i = 0; i < length; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			sheet.protectSheet("hundsun123");
		}

		FileOutputStream output = null;
		try {
			output = new FileOutputStream(fileName);
			workbook.write(output);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	private void setSheetOrders(String[] sheets) {
		int i = 0;
		for (String sheet : sheets) {
			if (workbook.getSheet(sheet) != null) {
				workbook.setSheetOrder(sheet, i++);
			}
		}
	}
	
}
