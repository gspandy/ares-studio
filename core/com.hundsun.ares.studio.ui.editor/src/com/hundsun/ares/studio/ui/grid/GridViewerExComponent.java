/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
//import org.eclipse.emf.ecore.EStructuralFeature;
//import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.model.extendable.ExtendFieldsEntity;
import com.hundsun.ares.studio.core.model.extendable.ExtendFieldsHeader;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;
import com.hundsun.ares.studio.ui.extendpoint.manager.ExtendFieldManager;
import com.hundsun.ares.studio.ui.grid.renderer.ColumnHeaderRenderer;
import com.hundsun.ares.studio.ui.page.IExtendFieldLoader;
import com.hundsun.ares.studio.ui.userdialog.XmlConfigInterface;
import com.hundsun.ares.studio.ui.userdialog.XmlConfigInterfaceConverter;
//import org.eclipse.core.databinding.observable.map.IObservableMap;
//import org.eclipse.emf.databinding.EMFObservables;

/**
 * grid�����ؼ�
 * @author maxh
 *
 */
public abstract class GridViewerExComponent<T> extends GridViewerStyleControl<T>{

//	protected ObservableListContentProvider tableViewerContentProvider = new ObservableListContentProvider();
	
	/**
	 * ��ID ��������չ��
	 */
	protected String[] viewerPropertys;
	
	/**
	 * ���� ��������չ��
	 */
	protected String[] viewerTitles;
	
	/** ���������ж���ӳ�� */
	protected HashMap<String, GridViewerColumn> columnMap = new HashMap<String, GridViewerColumn>();
	
	/** �ض��ĵ�Ԫ��༭�������ȼ�����Ĭ�� */
	protected HashMap<String, HashMap<Object, CellEditor> > specialEditorMap = new HashMap<String, HashMap<Object,CellEditor>>();
	
	/** ��������Ĭ�ϵ�Ԫ���޸���ӳ�� */
	protected HashMap<String, CellEditor> editorMap = new HashMap<String, CellEditor>();
	
	// ������п����顣 ��Ϊdispose()���õ�ʱ�����еĿؼ��Ѿ���dispose�ˣ����޷��ٻ�ȡ��ȡ�
	// ����ֻ�ܼӸ������п�ļ����������Ĵ˻��档
	protected int[] cachedWidth;
	
	protected static final String SAVED_WIDTHES = "saved_width";
	
	/**
	 * ���һ��������ض���Ԫ��༭��<BR>
	 * ������������Ĭ�ϵ��е�Ԫ��༭�����趨<BR>
	 * 
	 * ��Ԫ��༭���������disposeʱ��dispose
	 * 
	 * @param obj
	 * @param property
	 * @param editor
	 */
	final protected void addSpecialCellEditor(String property, T obj, CellEditor editor) {
		specialEditorMap.get(property).put(obj, editor);
	}
	
	abstract protected GridViewerColumn createColumn(String property,GridColumn column);
	
	/**
	 * �õ��������Ĳ�����Ϣ�����������ڸ߶�
	 * 
	 * @return
	 */
	protected GridData getCompositeLayoutData() {
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		gd.widthHint = 200;
		gd.verticalSpan = 1;
		return gd;
	}
	
	protected CellEditor getCellEditor(Object element,String property) {
		HashMap<Object, CellEditor> map = specialEditorMap.get(property);
		if (map != null && map.containsKey(element)) {
			return map.get(element);
		}
		return editorMap.get(property);
	}
	
	abstract protected String getId();
	
	/**
	 * ȡ���ϴα�����п�
	 * @return �ϴα�����п����顣
	 */
	protected int[] getSavedColumnWidthes() {
		String[] savedWidthes = null;
		IDialogSettings settings = ARESEditorPlugin.getDefault().getDialogSettings().getSection(getId());
		if (settings != null) {
			savedWidthes = settings.getArray(SAVED_WIDTHES);
			if (savedWidthes != null) {
				int[] saved = new int[savedWidthes.length];
				for (int i = 0; i < savedWidthes.length; i++) {
					saved[i] = Integer.parseInt(savedWidthes[i]);
				}
				return saved;
			}
		}
		return new int[0];
	}
	
	protected abstract Grid getGrid();
	
	/**
	 * ��ʼ������������ز���<BR>
	 * ʹ��setColumns����
	 */
	protected abstract void initViewColumn();
	
	/**
	 * ��ʼ���ؼ�
	 * ��Ҫ�����ñ����
	 * @param viewer
	 */
	protected void initViewer(ColumnViewer viewer) {
		// ����Tooltip��ʾ
		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.RECREATE);
		//��ʼ������������ز���
		initViewColumn();
		//�趨�޸��¼� �����޸Ļ���˫���޸�
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(viewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				if(doubleCheckChange){
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || 
					event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION || 
					event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}else{
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || 
					event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION || 
					event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			}
		};

		
		if(viewer instanceof GridTableViewer){
			GridViewerEditor.create(((GridTableViewer)viewer), actSupport, ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
		}else if(viewer instanceof GridTreeViewer){
			GridViewerEditor.create(((GridTreeViewer)viewer), actSupport, ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
		}
		
		//���������İ�������
		if(getHelpContextId() != null){
			PlatformUI.getWorkbench().getHelpSystem().setHelp(getGrid(), getHelpContextId());
		}
	}
	
	/**
	 * ������д�÷���
	 * �ṩ�����İ����˵�
	 * @return
	 */
	protected String getHelpContextId() {
		return null;
	}

	/**
	 * �����п�ĸı䲢ˢ�»��档
	 * 
	 * @param column
	 */
	private void listenToColumnWidth(final GridColumn column) {
		Grid table = column.getParent();
		final int index = table.indexOf(column);
		column.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				cachedWidth[index] = column.getWidth();
			}
		});
	}
	
	/**
	 * �Ƴ�֮ǰ���õ��ض���Ԫ��༭��<BR>
	 * ע����£��Ƴ���CellEditorӦ���ֹ���dispose
	 * 
	 * @param obj
	 */
	final protected void removeSpecialCellEditor(T obj) {
		specialEditorMap.remove(obj);
	}
	
	/**
	 * ���ó�ʼ��Ϣ<BR>
	 * 
	 * 
	 * @param captions
	 *            ����
	 * @param widths
	 *            ������
	 * @param styles
	 *            ��񣬿�Ϊnull����ȫ��ΪĬ�ϵ�SWT.NONE
	 * @param propertys
	 *            ����������Ϊnull�����Ա�������Ϊ������
	 * @param editors
	 *            ��Ԫ��༭��,��Ϊnull����ʹ��TextCellEditor
	 */
	protected void setColumns(String[] captions, int[] widths, int[] styles, String[] propertys, CellEditor[] editors) {
		if (captions == null) {
			return;
		}
		if (propertys == null) {
			propertys = captions;
		}
		if (styles == null) {
			styles = new int[captions.length];
			for (int i = 0; i < styles.length; i++) {
				styles[i] = SWT.NULL;
			}
		}
		//cellEditorĬ��Ϊȫ������ͨ�ı��ؼ�
		if (editors == null) {
			editors = new CellEditor[captions.length];
			for (int i = 0; i < editors.length; i++) {
				editors[i] = new TextCellEditor(getGrid());
			}
		}
		
		viewerPropertys = new String[propertys.length];
		viewerTitles = new String[captions.length];
		
		System.arraycopy(propertys, 0, viewerPropertys, 0, propertys.length);
		System.arraycopy(captions, 0, viewerTitles, 0, captions.length);
		
		//������չ�е���Ϣ
		ExtendFieldsEntity extendEntity = getExtendFields();
		if(extendEntity.getExtendFields().size() != 0){
			ColumnsFormater format = new ColumnsFormater(captions,widths,styles,propertys,editors,extendEntity);
			captions = format.getCaptions();
			widths = format.getWidths();
			styles = format.getStyles();
			propertys = format.getPropertys();
			editors = format.getEditors();
		}
		
		


		// �ָ��ϴα���Ŀ�� ������
		if (captions.length == widths.length 
				&& captions.length == styles.length 
				&& captions.length == propertys.length 
				&& captions.length == editors.length) {
			cachedWidth = new int[captions.length];
			int[] savedWidthes = getSavedColumnWidthes();
			ColumnHeaderRenderer columnHeaderRenderer = new ColumnHeaderRenderer();
			columnHeaderRenderer.setColor(getColumnTopColor());
			columnHeaderRenderer.setLineColor(getColumnTopLineColor());
			for (int i = 0; i < captions.length; i++) {
				final GridColumn column = new GridColumn(getGrid(), styles[i]);
				column.setText(captions[i]);
				column.setHeaderRenderer(columnHeaderRenderer);
				if (savedWidthes.length == captions.length) {
					if(savedWidthes[i] == 0){
						column.setWidth(widths[i]);
					}else {
						column.setWidth(savedWidthes[i]);
					}
				} else {
					column.setWidth(widths[i]);
				}
				// �����п�
				listenToColumnWidth(column);
				column.setMoveable(true);

				GridViewerColumn viewercolumn = createColumn(propertys[i],column);
				editorMap.put(propertys[i], editors[i]);
				columnMap.put(propertys[i], viewercolumn);
				specialEditorMap.put(propertys[i], new HashMap<Object, CellEditor>());
			}
		}
	}
	
	/**
	 * ���ӶԱ���EMF��֧��
	 * @param captions
	 * @param widths
	 * @param styles
	 * @param propertys
	 * @param editors
	 * @param attr
	 * @author wangdong
	 */
	protected void setColumns(String[] captions, int[] widths, int[] styles,
			String[] propertys, CellEditor[] editors, EAttribute[] attr) {
		if (captions == null) {
			return;
		}
		if (propertys == null) {
			propertys = captions;
		}
		if (styles == null) {
			styles = new int[captions.length];
			for (int i = 0; i < styles.length; i++) {
				styles[i] = SWT.NULL;
			}
		}
		// cellEditorĬ��Ϊȫ������ͨ�ı��ؼ�
		if (editors == null) {
			editors = new CellEditor[captions.length];
			for (int i = 0; i < editors.length; i++) {
				editors[i] = new TextCellEditor(getGrid());
			}
		}

		viewerPropertys = new String[propertys.length];
		viewerTitles = new String[captions.length];

		System.arraycopy(propertys, 0, viewerPropertys, 0, propertys.length);
		System.arraycopy(captions, 0, viewerTitles, 0, captions.length);

		// ������չ�е���Ϣ
		ExtendFieldsEntity extendEntity = getExtendFields();
		if (extendEntity.getExtendFields().size() != 0) {
			ColumnsFormater format = new ColumnsFormater(captions, widths,
					styles, propertys, editors, extendEntity);
			captions = format.getCaptions();
			widths = format.getWidths();
			styles = format.getStyles();
			propertys = format.getPropertys();
			editors = format.getEditors();
		}

		// �ָ��ϴα���Ŀ�� ������
		if (captions.length == widths.length
				&& captions.length == styles.length
				&& captions.length == propertys.length
				&& captions.length == editors.length) {
			cachedWidth = new int[captions.length];
			int[] savedWidthes = getSavedColumnWidthes();
			ColumnHeaderRenderer columnHeaderRenderer = new ColumnHeaderRenderer();
			columnHeaderRenderer.setColor(getColumnTopColor());
			columnHeaderRenderer.setLineColor(getColumnTopLineColor());
			for (int i = 0; i < captions.length; i++) {
				final GridColumn column = new GridColumn(getGrid(), styles[i]);
				column.setText(captions[i]);
				column.setHeaderRenderer(columnHeaderRenderer);
				if (savedWidthes.length == captions.length) {
					column.setWidth(savedWidthes[i]);
				} else {
					column.setWidth(widths[i]);
				}
				// �����п�
				listenToColumnWidth(column);
				column.setMoveable(true);

				GridViewerColumn viewercolumn = createColumn(propertys[i],
						column);
				editorMap.put(propertys[i], editors[i]);
				columnMap.put(propertys[i], viewercolumn);
				specialEditorMap.put(propertys[i],
						new HashMap<Object, CellEditor>());
				
//				if(attr!=null){
//					IObservableMap[] attributeMaps = EMFObservables
//							.observeMaps(
//									tableViewerContentProvider.getKnownElements(),
//									new EStructuralFeature[] {attr[i]});
//					viewercolumn.setLabelProvider(new GenericObservableMapCellLabelProvider(
//									attributeMaps, "{0}"));
//				}
			}
		}
	}

	
	/**
	 * ��ȡ��չ��
	 * @return
	 */
	protected ExtendFieldsEntity getExtendFields(){
		ExtendFieldsEntity entity = new ExtendFieldsEntity();
		entity.getExtendFields().addAll(readPluginExtendFields());
		entity.getExtendFields().addAll(readConfigExtendFields());
		return entity;
	}
	
	/**
	 * ��ȡ����չ�㷽ʽ��ӵ���չ��
	 * @return
	 */
	protected List<ExtendFieldsHeader> readPluginExtendFields(){
		List<ExtendFieldsHeader> result = new ArrayList<ExtendFieldsHeader>();
		IExtendFieldLoader loader = ExtendFieldManager.getDefault().getMap().get(getId());
		if(loader != null && loader.shouldLoad()){
			result.addAll(Arrays.asList(loader.getExtendFields()));
		}
		return result;
	}
	
	/**
	 * FIXME ��û������
	 * ��ȡ�������ļ���ӵ���չ��
	 * @return
	 */
	protected List<ExtendFieldsHeader> readConfigExtendFields(){
		if(getResource() != null){
			XmlConfigInterface config =  XmlConfigInterfaceConverter.getConverter().getConfig(getResource().getARESProject());
			if(config != null){
				List<ExtendFieldsHeader> list = config.getExtendColumns().get(getId());
				if(list != null){
					return list;
				}
			}
		}
		return new ArrayList<ExtendFieldsHeader>();
	}
	
	/* (non-Javadoc)
	 * �ڱ��رյ�ʱ���¼�п������´λָ�
	 */
	@Override
	public void dispose() {

		if (cachedWidth != null) {
			// ������
			String[] widthes = new String[cachedWidth.length];
			for (int i = 0; i < cachedWidth.length; i++) {
				widthes[i] = String.valueOf(cachedWidth[i]);
			}

			IDialogSettings settings = ARESEditorPlugin.getDefault().getDialogSettings();
			IDialogSettings mySettings = settings.addNewSection(getId());
			mySettings.put(SAVED_WIDTHES, widthes);
		}

		for (CellEditor cell : editorMap.values()) {
			cell.dispose();
		}

		for (HashMap<Object, CellEditor> map : specialEditorMap.values()) {
			for (CellEditor cell : map.values()) {
				cell.dispose();
			}
		}

	}
	
	/**
	 * �����չ�еĹ�����
	 * @author maxh
	 */
	class ColumnsFormater{
		List<String> captions = new ArrayList<String>();
		List<Integer> widths = new ArrayList<Integer>();
		List<Integer> styles = new ArrayList<Integer>();
		List<String> propertys = new ArrayList<String>();
		List<CellEditor> editors = new ArrayList<CellEditor>();
		public ColumnsFormater(String[] captions, int[] widths, int[] styles, String[] propertys, CellEditor[] editors,ExtendFieldsEntity extendEntity) {
			this.captions.addAll(Arrays.asList(captions));
			for(int width:widths){
				this.widths.add(width);
			}
			for(int style:styles){
				this.styles.add(style);
			}
			this.propertys.addAll(Arrays.asList(propertys));
			this.editors.addAll(Arrays.asList(editors));
			for(ExtendFieldsHeader header:extendEntity.getExtendFields()){
				this.captions.add(header.getText());
				this.widths.add(header.getWidth());
				this.styles.add(SWT.NULL);
				this.propertys.add(header.getId());
				if(header.getType() == ExtendFieldsHeader.TYPE_COMMON){
					this.editors.add(new TextCellEditor(getGrid()));
				}else{
					try {
						this.editors.add(new ComboBoxCellEditor(getGrid(),header.getValues().split(",")));
					} catch (Exception e) {
						e.printStackTrace();
						this.editors.add(new TextCellEditor(getGrid()));
					}
				}
			}
		}
		public String[] getCaptions() {
			return captions.toArray(new String[captions.size()]);
		}
		public int[] getWidths() {
			int[] result = new int[widths.size()];
			for(int i = 0;i < widths.size();i++){
				result[i] = widths.get(i);
			}
			return result;
		}
		public int[] getStyles() {
			int[] result = new int[styles.size()];
			for(int i = 0;i < styles.size();i++){
				result[i] = styles.get(i);
			}
			return result;
		}
		public String[] getPropertys() {
			return propertys.toArray(new String[propertys.size()]);
		}
		public CellEditor[] getEditors() {
			return editors.toArray(new CellEditor[editors.size()]);
		}
	}
	
}


