/**
 * Դ�������ƣ�SheetParser.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.hundsun.ares.studio.core.util.log.Log;

/**
 * ExcelSheet������<p>
 * �����������һ��Sheet�ָ��1������area(�����Ӧ��һ���������ȶ����һ���������)��
 * ÿ��area�ַָ�ɶ��Block��Block�����ͷ�ΪKEY-Value��Text��Table�ȣ��ο�{@link BlockTypes}<p>
 * 
 * ÿ��area��block���ɿ����Զ���Ŀ�ʼtagȷ�����ο�{@link SheetParser#areaTags}��{@link SheetParser#blocks}
 * 
 * Parser����ֻ�����ĵ��ṹ��������ҵ���߼��� ҵ���߼�����Handler����ġ�
 * 
 * @author sundl
 * @see BlockTypes
 */
public class SheetParser {
	
	private static final Logger logger = Logger.getLogger(SheetParser.class);

	private List<Integer> aresStart = new ArrayList<Integer>();
	
	private Map<Integer, Integer> blockStart = new HashMap<Integer, Integer>();
	
	/** ����ʼ��ǩ */
	public List<String> areaTags = new ArrayList<String>();
	/** block��ʼ��ǩ */
	public Map<String, Integer> blocks = new HashMap<String, Integer>();
	// ��־���
	public Log log; 
	public List<ISheetHandler> handlers = new ArrayList<ISheetHandler>();
	public ExcelParser exlParser;
	public int startRow = -1;
	
	/** ������ʽ�ã����븳ֵ */
	public FormulaEvaluator evaluator;
	
	private String currentBlockTag;
	private int currentBlockType;
	private boolean headerFound = false;
	private StringBuffer text = new StringBuffer();
	
	private int rowIndex = -1;
	private int culumnIndex = -1;
	
	public SheetParser() {
		// Ĭ�ϵļ���TAG�� ������������sheetҳ�е�ģ����Ϣ����
		areaTags.add("ģ��������");
		areaTags.add("ģ��Ӣ����");
		areaTags.add("ģ����");		// == Ӣ����
		
		blocks.put("ģ��������", BlockTypes.KEY_VALUE);
		blocks.put("ģ����", BlockTypes.KEY_VALUE);
		blocks.put("ģ��Ӣ����", BlockTypes.KEY_VALUE);
	}
	
	public List<Integer> getAresStart() {
		return aresStart;
	}

	public Map<Integer, Integer> getBlockStart() {
		return blockStart;
	}


	public int getCurrentRow() {
		return rowIndex;
	}
	
	public int getCurrentBlockType() {
		return currentBlockType;
	}
	
	public String getCurrentBlockTag() {
		return currentBlockTag;
	}
	
	public String getText() {
		return text.toString();
	}
	
	private void reset() {
		currentBlockType = -1;
		headerFound = false;
	}
	
	public void parse(Sheet sheet) { 
		logger.debug("��ʼ����Sheetҳ:" + sheet.getSheetName());
		startSheet(sheet);
		
		int rowCount = sheet.getPhysicalNumberOfRows();
		logger.debug("������" + rowCount);
		
		// ������ͷ�����ʱ���¼��ʼ��
		int headerStartCol = 0;
		
		for (Row r : sheet) {
			HSSFRow row = (HSSFRow) r;
			if (row == null)
				continue;
			
			this.rowIndex = row.getRowNum();
			if (rowIndex < startRow) {
				continue;	
			}
			// sundl: row.getFirstCellNum()���Ǻ��ȶ�����һ�еĿո���ʱ�����null����ʱ�����ǿ��ַ����������������ֵ������
			// Ŀǰǿ��д��1�� �������Կ��Ǹĳ��Լ�д�ҵ�һ���ǿ��С�
			int firstCellNum = row.getFirstCellNum();
			if (firstCellNum == -1)
				continue;
			else
				firstCellNum = 1; 		
			
			HSSFCell firstCell = row.getCell(firstCellNum);
			String firstcellString = getCellStringValue(firstCell, evaluator);
			
			if (!firstcellString.isEmpty()) {
				if (aresStart.contains(rowIndex)) {
					endArea();
					logger.debug("Area��ʼ,�к�: " + rowIndex);
					startArea(firstcellString);
				}else if (areaTags.indexOf(firstcellString) != -1) {
					endArea();
					logger.debug("Area��ʼ,�к�: " + rowIndex);
					startArea(firstcellString);
				}
				
				if (blockStart.keySet().contains(rowIndex)) {
					endBlock();
					startBlock(firstcellString, blockStart.get(rowIndex));
				}else if (blocks.containsKey(firstcellString)) {
					endBlock();
					startBlock(firstcellString, blocks.get(firstcellString));
					// Text�����һ����ʱ������
					if (currentBlockType == BlockTypes.TEXT) {
						continue;
					}
				}
			}
			
			String[] strings = readRowStrings(row, 1, evaluator);
			switch (currentBlockType) {
				case BlockTypes.KEY_VALUE:
					parseKeyValue(row, 1);
					break;
				case BlockTypes.TABLE:
					if (!headerFound) {
						// header����˵��Ӧ�ô��ڿ��ַ�������ͷӦ�ò��ᣬ����β���ܴ���
						while (StringUtils.isEmpty(strings[strings.length - 1])) {
							strings = (String[]) ArrayUtils.remove(strings, strings.length - 1);
						}
						headerStartCol = firstCellNum;
						header(strings);
						headerFound = true;
					} else {
						strings = POIUtils.readRowStrings(row, headerStartCol, evaluator);
						row(strings);
					}
					break;
				case BlockTypes.TEXT:
					text.append(StringUtils.join(strings));
					text.append('\n');
					break;
			}
		}
		
		endBlock();
		endArea();
		endSheet();
	}
	
	public static String[] readRowStrings(HSSFRow row, int startCol, FormulaEvaluator evaluator) {
		List<String> strings = new ArrayList<String>();
		int max = row.getLastCellNum();
		for (int i = startCol; i < max; i++) {
			HSSFCell cell = row.getCell(i);
			if (cell == null) {
				strings.add(StringUtils.EMPTY);
			} else {
				String str = getCellStringValue((HSSFCell)cell, evaluator);
				strings.add(str);
			}
		}
		return strings.toArray(new String[0]);
	}
	
	public static String getCellStringValue(HSSFCell cell, FormulaEvaluator evaluator) {
		if (cell != null) {
		    switch (evaluator.evaluateInCell(cell).getCellType()) {
		        case Cell.CELL_TYPE_BOOLEAN:
		        	return BooleanUtils.toStringTrueFalse(cell.getBooleanCellValue());
		        case Cell.CELL_TYPE_NUMERIC:
		        	// FIXME ������ʵ�ų��� �������͵�����
		        	return String.valueOf((int)cell.getNumericCellValue());
		        case Cell.CELL_TYPE_STRING:
		        	return cell.getStringCellValue();
		        case Cell.CELL_TYPE_BLANK:
		            break;
		        case Cell.CELL_TYPE_ERROR:

		        // CELL_TYPE_FORMULA will never occur
		        case Cell.CELL_TYPE_FORMULA:
		            break;
		    }
		}
		return StringUtils.EMPTY;
	}
	
	private void parseKeyValue(HSSFRow row, int startCol) {
		String key = null;
		String value = null;
		for (int i = startCol; i < row.getLastCellNum(); i++) {
			HSSFCell cell = row.getCell(i);
			String str = getCellStringValue(cell, evaluator);
			if (key == null) {
				if (StringUtils.isEmpty(str)) {
					//���ܵ�ǰ��Ԫ�ϲ��˼�����Ԫ��֮������Ϣ����return��Ϊcontinue  by wangxh 2014��2��20��14:44:02
					continue;
				}
				key = str;
			} else {
				value = str;
				keyValue(key, value);
				key = null;
				value = null;
			}
		}
	}
	
	public void startSheet(Sheet sheet) {
		for (ISheetHandler handler : handlers) {
			try {
				handler.startSheet(sheet);
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY,e);
			}
		}
	}
	
	public void startArea(String startTag) {
		for (ISheetHandler handler : handlers) {
			try {
				handler.startArea(startTag);
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY,e);
			}
		}
	}
	
	public void startBlock(String startTag, int type) {
		currentBlockTag = startTag;
		currentBlockType = type;
		for (ISheetHandler handler : handlers) {
			try {
				handler.startBlock(startTag, type);
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY, e);
			}
		}
	}
	
	public void keyValue(String key, String value) {
		for (ISheetHandler handler : handlers) {
			try {
				handler.keyValue(key, value);
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY,e);
			}
		}
	}
	
	public void header(String[] header) {
		for (ISheetHandler handler : handlers) {
			try {
				handler.header(header);
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY,e);
			}
		}
	}
	
	public void row(String[] row) {
		for (ISheetHandler handler : handlers) {
			try {
				handler.row(row);
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY,e);
			}
		}
	}
	
	public void endBlock() {
		for (ISheetHandler handler : handlers) {
			try {
				handler.endBlock();
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY,e);
			}
		}
		
		switch (currentBlockType) {
			case BlockTypes.TABLE:
				headerFound = false;
				break;
			case BlockTypes.TEXT:
				text = new StringBuffer();
				break;
		}
		
	}
	
	public void endArea() {
		for (ISheetHandler handler : handlers) {
			try {
				handler.endArea();
			} catch (Exception e) {
				logger.error(StringUtils.EMPTY,e);
			}
		}
	}
	
	public void endSheet() {
		for (ISheetHandler handler : handlers) {
			try {
				handler.endSheet();
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	
}
