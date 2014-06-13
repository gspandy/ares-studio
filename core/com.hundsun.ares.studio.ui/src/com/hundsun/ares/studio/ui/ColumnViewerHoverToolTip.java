package com.hundsun.ares.studio.ui;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ColumnViewerHoverToolTip{
	
	/**���*/
	ColumnViewer viewer;
	/**��ǰ������ڵĵ�Ԫ��*/
	ViewerCell cell;
	String text = "";
	
	/**tooltip��ʾ��*/
	Shell currentShell;
	/**tooltip��ʾ����Ϣ��*/
	StyledText txtInfo;
	
	Listener treeViewerListener = new TreeViewerListener();
	Listener toolTipListener = new ToolTipListener();
	Control control;

	protected ColumnViewerHoverToolTip(ColumnViewer viewer) {
		this.viewer = viewer;
		this.control = viewer.getControl();
		
		this.control.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				deactivate();
			}

		});
		
		activate();
	}	
	public static void enableFor(ColumnViewer viewer) {
		new ColumnViewerHoverToolTip(viewer);
	}

	protected Composite createToolTipContentArea(Event event, Composite parent) {
		control.forceFocus();
		int textStyle=SWT.V_SCROLL | SWT.WRAP;

		final Composite composite= new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		String text = getText();
		
		txtInfo = new StyledText(composite, textStyle);
		txtInfo.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		txtInfo.setFont(new Font(txtInfo.getDisplay(),"����",9,SWT.NORMAL));
		if (text != null) {
			txtInfo.setText(text);
			txtInfo.setCaret(null);
			txtInfo.setEditable(false);
		}
		
		//������ֻ���ڰ���F2�������ͣ����tooltip�ϵ�ʱ�����ʾ
		setScrollBarVisible(false);
		Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		
		CLabel label = new CLabel(composite, SWT.NONE);
		
		label.setText("Press F2 for focus");
		label.setFont(new Font(label.getDisplay(),"����",7,SWT.NORMAL));
		label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		composite.setLayout(new GridLayout(1, false));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(txtInfo);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(separator);
		GridDataFactory.fillDefaults().hint(-1,7).align(SWT.END, SWT.FILL).grab(true, false).applyTo(label);
		
		toolTipActivate(composite);
		composite.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				toolTipDeactivate(composite);
			}
		});
		
		return txtInfo;
		
	}
	
	
	private void toolTipShow(Shell tip, Event event) {
		if (!tip.isDisposed()) {
			tip.pack();
			Point size = tip.getSize();
			//tooltip�����������Ϊ200
			if(size.x > 200){
				size = new Point(200, size.y + 36);
				tip.setSize(size);
			}
			Rectangle cellBound = cell.getBounds();
			Point location = fixupDisplayBounds(size, getLocation(cellBound.x,cellBound.y + cellBound.height));

			tip.setLocation(location);
			tip.setVisible(true);
			control.forceFocus();
			txtInfo.setFocus();
			txtInfo.setText(getText());
			if(!txtInfo.getVerticalBar().isVisible()){
				//Ĭ��״̬����ʾ4�У��ܹ�Լ110���ֽڡ������Ļ������...
				if(getText().getBytes().length>110){
					int end = 54;
					String tmp = StringUtils.substring(getText(), 0, end);
					while(tmp.getBytes().length<=105){
						end += (110-tmp.getBytes().length)/2;
						String addStr = StringUtils.substring(getText(), tmp.length(),end);
						if(addStr.length() == 0){
							break;
						}
						tmp += addStr;
					}
					txtInfo.setText(tmp + "...");
				}
			}
		}
	}
	/**��ȡ�����table/tree���������*/
	public Point getLocation(int x, int y) {
		return control.toDisplay(x , y);
	}
	
	//tipSize��tooltip�Ŀ��       location:tooltip�ĳ�ʼ��ʾ���꣨��Ԫ�������λ�ã�
	private Point fixupDisplayBounds(Point tipSize, Point location) {
			
		//���ճ�ʼ��ʾλ�úͿ�߻�ȡ����tooltip����������
			Point rightBounds = new Point(tipSize.x + location.x, tipSize.y
					+ location.y);
			
			Rectangle clientArea = ((Composite)control).getClientArea();
			Point pt = control.toDisplay(clientArea.x,clientArea.y);
			Rectangle bounds = new Rectangle(pt.x, pt.y, control.getSize().x,  control.getSize().y);
			
			//���tooltip�ķ�Χ�����˱������Ҫ���е���
			if (!(bounds.contains(location) && bounds.contains(rightBounds))) {
				//���tootilp���ұ߳����˱���ұ�
				if (rightBounds.x > bounds.x + bounds.width) {
					//��ʼλ�����ƣ�ֱ��tootip���ұߺͱ����ұ�һ��
					location.x -= rightBounds.x - (bounds.x + bounds.width);
				}
				
				//���tootip���±߳����˱����±�
				if (rightBounds.y > bounds.y + bounds.height) {
					//��ʼλ�������ƣ�ֱ��tooltip���±ߺ͵�Ԫ����ϱ�һ��
					location.y = location.y - cell.getBounds().height - tipSize.y;//-= rightBounds.y - (bounds.y + bounds.height);
				}
				
				//��֤���ƻ������ƺ��ʼxy����û�г���������ϱ�
				if (location.x < bounds.x) {
					location.x = bounds.x;
				}

				if (location.y < bounds.y) {
					location.y = bounds.y;
				}
			}

		return location;
	}
	
	protected boolean shouldCreateToolTip(Event event) {
		boolean rv = false;
		//�ж�ǰ��tooltip�Ƿ���ڣ������ǰtootip�������ˣ���cell��Ϊnull
		if(currentShell == null || currentShell.isDisposed()){
			cell = null;
		}
		try{
			Point point = new Point(event.x, event.y);
			
			ViewerCell curCell = viewer.getCell(point);
			//����͵�ǰ��ʾtooltip�ĵ�Ԫ����ͬһ��
			if(cell != null && cell.equals(curCell)){
				return false;
			}
			//�����ǰ������ڵ�Ԫ�񲻴���
			cell = curCell;
			if(curCell == null){
				return false;
			}
			
			Object element = cell.getElement();
			
			CellLabelProvider labelProvider = viewer.getLabelProvider(cell.getColumnIndex());
			
			String text = labelProvider.getToolTipText(element);
			setText(text);
			rv = StringUtils.isNotBlank(getText());
			
		}catch(Exception e){
			return false;
		}

		return rv;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	private void toolTipHide(){
		if(currentShell != null && !currentShell.isDisposed()){
			currentShell.close();
			currentShell.dispose();
		}
	};
	
	private void setScrollBarVisible(boolean visible){
		if(txtInfo != null && !txtInfo.isDisposed() && txtInfo.getVerticalBar().getVisible() == !visible){
			txtInfo.getVerticalBar().setVisible(visible);
			if(visible){
				txtInfo.setFocus();
				txtInfo.setText(getText());
				currentShell.setSize(currentShell.getSize().x + txtInfo.getVerticalBar().getSize().x, currentShell.getSize().y);
			}
		}
	}
	public void toolTipActivate(Control control) {
		toolTipDeactivate(control);
		control.addListener(SWT.MouseHover,toolTipListener);
		control.addListener(SWT.MouseDown,toolTipListener);
		control.addListener(SWT.FocusOut,toolTipListener);
		control.addListener(SWT.KeyDown,toolTipListener);
		if (control instanceof Composite) {
			for(Control child : ((Composite)control).getChildren()){
				toolTipActivate(child);
			}
		}
	}

	/**
	 * Deactivate tooltip support for the underlying control
	 */
	public void toolTipDeactivate(Control control) {
		control.removeListener(SWT.MouseHover,toolTipListener);
		control.removeListener(SWT.MouseDown,toolTipListener);
		control.removeListener(SWT.FocusOut,toolTipListener);
		control.removeListener(SWT.KeyDown,toolTipListener);
		if (control instanceof Composite) {
			for(Control child : ((Composite)control).getChildren()){
				toolTipDeactivate(child);
			}
		}
	}
	
	public void activate() {
		deactivate();
		control.addListener(SWT.MouseHover, treeViewerListener);
		control.addListener(SWT.KeyDown, treeViewerListener);
		control.addListener(SWT.MouseMove, treeViewerListener);
	}

	/**
	 * Deactivate tooltip support for the underlying control
	 */
	public void deactivate() {
		control.removeListener(SWT.MouseHover, treeViewerListener);
		control.removeListener(SWT.KeyDown, treeViewerListener);
		control.removeListener(SWT.MouseMove, treeViewerListener);
	}
	class TreeViewerListener implements Listener{

		@Override
		public void handleEvent(Event event) {
			if (event.widget instanceof Control) {

				Control c = (Control) event.widget;
				switch (event.type) {
				//���ͣ��
				case SWT.MouseHover:
					if(shouldCreateToolTip(event)){
						//�Ȱѵ�ǰ�Ѿ��򿪵�tooltip��������������һ���µ�tooltip����ʾ����
						toolTipHide();
						currentShell = new Shell(c.getShell(), SWT.ON_TOP | SWT.TOOL
								| SWT.NO_FOCUS | SWT.RESIZE);
						currentShell.setLayout(new FillLayout());
						createToolTipContentArea(event, currentShell);
						toolTipShow(currentShell, event);
						currentShell.addControlListener(new ControlAdapter() {
							@Override
							public void controlResized(ControlEvent e) {
								setScrollBarVisible(true);
							}
						});
					}
					break;
				case SWT.KeyDown:
					//esc�����ٵ�ǰ�򿪵�tooltip
					if(event.keyCode == SWT.ESC){
						toolTipHide();
					}else if(event.keyCode == SWT.F2){
						//F2��ǰtooltip��ȡ���㣬��������ʾ
						setScrollBarVisible(true);
					}
					break;
				case SWT.MouseMove:
					Point point = new Point(event.x, event.y);
					if(currentShell != null && !currentShell.isDisposed() && cell != null){
						if(!currentShell.getBounds().contains(point) && !txtInfo.getVerticalBar().isVisible() && !cell.getBounds().contains(point)){
							//������λ�ò��ڵ�Ԫ���tooltip��Χ�ڣ���tooltipû�л�ȡ���㣬������tooltip
							toolTipHide();
						}
					}
					break;
				}
			}
		}
		
	}
	class ToolTipListener implements Listener{
		@Override
		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.MouseHover:
			case SWT.MouseDown:
				setScrollBarVisible(true);
				break;
			case SWT.KeyDown:
				if(event.keyCode == SWT.ESC){
					toolTipHide();
				}else if(event.keyCode == SWT.F2){
					setScrollBarVisible(true);
				}
				break;
			case SWT.FocusOut:
				//tooltipʧȥ����������
				toolTipHide();
				break;
			default:
				break;
			}
		}
		
	}
}
