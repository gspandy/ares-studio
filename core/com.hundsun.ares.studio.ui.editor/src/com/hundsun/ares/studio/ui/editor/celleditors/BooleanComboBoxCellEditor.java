/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.ui.editor.celleditors;

import java.text.MessageFormat;

import org.apache.commons.lang.BooleanUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * @author gongyf
 *
 */
public class BooleanComboBoxCellEditor extends CellEditor {

	private String trueText;
	private String falseText;
	/**
	 * The custom combo box control.
	 * ȷ�������˵�����һ����true���ڶ�����false
	 */
	private CCombo comboBox;
	
	private boolean bValue;
	
	/**
	 * Default ComboBoxCellEditor style
	 */
	private static final int defaultStyle = SWT.NONE;
	
	
	
	/**
	 * �Ľ��Ĳ�������celleditor������������ʱ���һ���checkboxcelleditorһ�£�������ɳ��򴥷�������tab������
	 * �����ʾһ��combobox���༭
	 * 
	 * @param parent
	 * @param trueText
	 * @param falseText
	 */
	public BooleanComboBoxCellEditor(Composite parent, String trueText,
			String falseText) {
		this(parent, trueText, falseText, defaultStyle);
	}

	
	
	/**
	 * @param parent
	 * @param trueText
	 * @param falseText
	 * @param style
	 */
	public BooleanComboBoxCellEditor(Composite parent,
			String trueText, String falseText, int style) {
		super(parent, style);
		setTrueText(trueText);
		setFalseText(falseText);
	}



	/**
	 * @param trueText the trueText to set
	 */
	public void setTrueText(String trueText) {
		this.trueText = trueText;
		populateComboBoxItems();
	}
	
	/**
	 * @return the trueText
	 */
	public String getTrueText() {
		return trueText;
	}
	
	/**
	 * @param falseText the falseText to set
	 */
	public void setFalseText(String falseText) {
		this.falseText = falseText;
		populateComboBoxItems();
	}
	
	/**
	 * @return the falseText
	 */
	public String getFalseText() {
		return falseText;
	}
	
	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems() {
		if (comboBox != null && getTrueText() != null && getFalseText() != null) {
			comboBox.removeAll();
			comboBox.setItems(new String[]{getTrueText(), getFalseText()});

			setValueValid(true);
			bValue = true;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		comboBox = new CCombo(parent, getStyle() | SWT.READ_ONLY);
		comboBox.setFont(parent.getFont());

		populateComboBoxItems();

		comboBox.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});

		comboBox.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

			public void widgetSelected(SelectionEvent event) {
				bValue = comboBox.getSelectionIndex() == 0;
			}
		});

		comboBox.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		comboBox.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				BooleanComboBoxCellEditor.this.focusLost();
			}
		});
		return comboBox;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return bValue;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		comboBox.setFocus();
	}

	public LayoutData getLayoutData() {
		LayoutData layoutData = super.getLayoutData();
		if ((comboBox == null) || comboBox.isDisposed()) {
			layoutData.minimumWidth = 60;
		} else {
			// make the comboBox 10 characters wide
			GC gc = new GC(comboBox);
			layoutData.minimumWidth = (gc.getFontMetrics()
					.getAverageCharWidth() * 10) + 10;
			gc.dispose();
		}
		return layoutData;
	}
	
	/**
	 * Applies the currently selected value and deactivates the cell editor
	 */
	void applyEditorValueAndDeactivate() {
		// must set the selection before getting value
		bValue = comboBox.getSelectionIndex() == 0;
		Object newValue = doGetValue();
		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);

		if (!isValid) {
			// combo so format using its text value
			setErrorMessage(MessageFormat.format(getErrorMessage(),
					new Object[] { comboBox.getText() }));
		}

		fireApplyEditorValue();
		deactivate();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		Assert.isTrue(comboBox != null);
		bValue = BooleanUtils.toBoolean(String.valueOf(value));
		comboBox.select(bValue ? 0 : 1);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.CellEditor#activate(org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent)
	 */
	@Override
	public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		if (activationEvent.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL 
				|| activationEvent.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC) {
			// ͨ��tab��������߳�����ü�����Ҫ��ʾcontrol
			super.activate(activationEvent);
			
		} else {
			
			// �������⴦��
			// ���û��������ʱϣ��ֱ�Ӹı�ֵ������ʾ�ؼ�
			// 1��������ﲻ��ui���첽ִ�У���ΪfireApplyEditorValue()�����лὫcellEditor����ڲ���������Ϊnull������
			// org.eclipse.jface.viewers.ColumnViewerEditor.activateCellEditor(ColumnViewerEditorActivationEvent)��207�г��ֿ�ָ�����
			// �����첽ִ�к󣬻���activateCellEditor�������ִ�У��Ͳ������
			// 2�������Ϊ�и��첽���⣬�����combobox����һ˲�����ʧ�������û��о��ϲ��ã�������Ҫ������������
			// ���õķ������Ƚ��ø��������ػ棬���Ǹ���������ʾ�����仯����ȫ�������Ժ�Żָ����������ػ棬���е��������⽫��ʧ
			getControl().getParent().setRedraw(false);
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					
					bValue = !bValue;
					fireApplyEditorValue();
					getControl().getParent().setRedraw(true);
					
				}
			});

		}
		
	}
	
}
