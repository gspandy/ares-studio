package com.hundsun.ares.studio.jres.database.ui.editors.dialog.inner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

/**
 * ����ͨ����Ҳ��һ�У�һ�������Ű�ť�������Ҫ������ť��ʾ��ͬһ��(һ��ռ������)���������Ĳ�����������>1
 * 
 * @author yanyl
 * 
 */
public abstract class TableViewerWithButtons extends BasicTableViewer {

	private int btnPosition = SWT.RIGHT;

	/**
	 * �½�Ĭ����ʽ�Ĵ���ť�ı�񣬰�ť�ڱ���Ҳ�
	 */
	public TableViewerWithButtons(Composite parent) {
		this(parent, BasicTableViewer.DEFAULT_STYLE, SWT.RIGHT);
	}

	/**
	 * �½�Ĭ����ʽ�Ĵ���ť�ı��
	 * 
	 * @param btnPosition
	 *            ��ťλ�ã�����λ�ã�SWT.RIGHT������Ҳ� SWT.BOTTOM���ײ�
	 */
	public TableViewerWithButtons(Composite parent, int btnPosition) {
		this(parent, BasicTableViewer.DEFAULT_STYLE, btnPosition);
	}

	/**
	 * �½�����ť�ı��
	 * 
	 * @param btnPosition
	 *            ��ťλ�ã�����λ�ã�SWT.RIGHT������Ҳ� SWT.BOTTOM���ײ�
	 */
	public TableViewerWithButtons(Composite parent, int style, int btnPosition) {
		super(parent, style);

		this.btnPosition = btnPosition;

		boolean onBottom = btnPosition == SWT.BOTTOM;

		Layout parentlayout = (Layout) parent.getLayout();
		getTable().setLayoutData(
				getDefaultLayoutData(parentlayout instanceof GridLayout ? ((GridLayout) parentlayout).numColumns : 1));

		Composite buttonsComposite = new Composite(parent, 0);
		createButtons(buttonsComposite);

		GridLayoutFactory.fillDefaults().numColumns(onBottom ? buttonsComposite.getChildren().length : 1)
				.applyTo(buttonsComposite);
		GridDataFactory.fillDefaults().grab(onBottom, false)
				.align(onBottom ? SWT.BEGINNING : SWT.CENTER, onBottom ? SWT.CENTER : SWT.BEGINNING)
				.applyTo(buttonsComposite);
	}

	@Override
	protected GridData getDefaultLayoutData(int cols) {
		// ���ݸ������������Ͱ�ťλ�õ�����񲼾�
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		if (btnPosition == SWT.BOTTOM) {
			data.horizontalSpan = cols;
		} else {
			data.horizontalSpan = cols - 1;
		}
		data.heightHint = 220;
		return data;
	}
	
	/**
	 * �������õ����İ�ť
	 * 
	 * @param parent
	 *            ������
	 */
	protected abstract void createButtons(Composite parent);
}

/**
 * ��ť�����������ṩ��һЩ����õİ�ť
 * 
 * @author yanyl
 * 
 */
class TableButtonFactory {
	/**
	 * ��Ӱ�ť������󴥷�creator��create���������������������¶�����ӵ���������ĩβ
	 * 
	 * @param <T>
	 *            �����Ӱ�ť���½������Ķ��������
	 * @param parent
	 *            ��Ӱ�ť������
	 * @param tableViewer
	 *            ��Ӱ�ť��Ч�ı��
	 * @param creator
	 *            ʵ��InstanceCreator�ӿڵ��¶���Ĵ�����
	 * @return ���Ӳ������ڳɹ�������ִ��
	 * @see InstanceCreator
	 * @see ActionBuilder
	 */
	public static <T> ActionBuilder createAddBtn(Composite parent, final TableViewerWithButtons tableViewer,
			final InstanceCreator<T> creator) {
		Button add = new Button(parent, SWT.None);
		add.setText("���");
//		add.setImage(ImageProvider.getImage(ImageProvider.ADD));

		final ActionBuilder builder = new ActionBuilder();
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unchecked")
				Collection<T> inputs = (Collection<T>) tableViewer.getInput();
				T newInstance = creator.create(inputs);
				if (newInstance != null) {
					inputs.add(newInstance);
				}
				// ������ʵ��Ϊnullʱ����������߼��Ѿ���create()��������ִ����
				builder.excute();
				tableViewer.refresh();
			}
		});
		return builder;
	}

	/**
	 * ����ɾ����ť�������ɾ����ǰѡ�м�¼�����������ƶ�����һ����¼����û����һ����¼�򽫽����ƶ�����һ����¼
	 * 
	 * @param parent
	 *            ��ť������
	 * @param tableViewer
	 *            ��ť��Ч�ı��
	 * @return ���Ӷ������ڳɹ�ɾ���˽ڵ��ִ��
	 * @see ActionBuilder
	 */
	public static ActionBuilder createRemoveBtn(Composite parent, final TableViewerWithButtons tableViewer) {
		Button remove = new Button(parent, SWT.None);
		remove.setText("ɾ��");
//		remove.setImage(ImageProvider.getImage(ImageProvider.REMOVE));
		final ActionBuilder builder = new ActionBuilder();
		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<?> params = (List<?>) tableViewer.getInput();
				StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
				Object object = selection.getFirstElement();
				if (object != null) {
					int index = params.indexOf(object);
					params.remove(object);
					// ����list�л���������ѡ����һ��
					if (index >= 0 && params.size() > 0) {
						// ��ɾ�������һ����ѡ������ǰһ������
						while (index >= params.size()) {
							index--;
						}
						tableViewer.setSelection(new StructuredSelection(params.get(index)));
					}
					builder.excute();
					tableViewer.refresh();
				}
			}
		});
		return builder;
	}

	/**
	 * �������ư�ť����ѡ�м�¼�����ƶ�һ��
	 * 
	 * @param parent
	 *            ������
	 * @param tableViewer
	 *            ��Ч�ı��
	 * @return ���Ӳ������ڳɹ����ƺ�ִ��
	 * @see ActionBuilder
	 */
	public static ActionBuilder createMoveUpBtn(Composite parent, final TableViewerWithButtons tableViewer) {
		Button up = new Button(parent, SWT.None);
		up.setText("����");
//		up.setImage(ImageProvider.getImage(ImageProvider.UP));
		final ActionBuilder builder = new ActionBuilder();
		up.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<?> inputs = (List<?>) tableViewer.getInput();
				StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
				Object object = selection.getFirstElement();
				if (object != null) {
					int index = inputs.indexOf(object);
					if (index > 0) {
						swap(inputs, index, index - 1);
						builder.excute();
						tableViewer.refresh();
					}
				}
			}
		});
		return builder;
	}

	/**
	 * �������ư�ť����ѡ�м�¼�����ƶ�һ��
	 * 
	 * @param parent
	 *            ������
	 * @param tableViewer
	 *            ��Ч�ı��
	 * @return ���Ӳ������ڳɹ����ƺ�ִ��
	 * @see ActionBuilder
	 */
	public static ActionBuilder createMoveDownBtn(Composite parent, final TableViewerWithButtons tableViewer) {
		Button down = new Button(parent, SWT.None);
		down.setText("����");
//		up.setImage(ImageProvider.getImage(ImageProvider.DOWN));
		final ActionBuilder builder = new ActionBuilder();
		down.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<?> inputs = (List<?>) tableViewer.getInput();
				StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
				Object object = selection.getFirstElement();
				if (object != null) {
					int index = inputs.indexOf(object);
					if (index < inputs.size() - 1) {
						swap(inputs, index + 1, index);
						builder.excute();
						tableViewer.refresh();
					}
				}
			}
		});
		return builder;
	}

	/**
	 * ����List��Ԫ�ص�λ�ã�Collections�е�swap������unique�޶���List�е�Ԫ��ʱ���ܻ����쳣
	 * @param l �б�
	 * @param i Ԫ�ص�index
	 * @param j Ҫ������Ԫ�ص�index
	 */
	@SuppressWarnings("unchecked")
	private static void swap(@SuppressWarnings("rawtypes") List l, int i, int j) {
		int b = (i >= j) ? i : j;
		int s = (i < j) ? i : j;
		// remove�����Ӵ�Ŀ�ʼ��
		Object target = l.remove(b);
		Object src = l.remove(s);
		// add������С�Ŀ�ʼ
		l.add(s, target);
		l.add(b, src);
	}

}

/**
 * TableButtonFactory��������ť���������¶���ʹ�õĽӿ�
 * 
 * @author yanyl
 * 
 */
interface InstanceCreator<T> {
	/**
	 * ��Add��ť�������ã����ڴ���һ���µĶ���ʵ����Ҳ����ֱ���ڷ��������水�Լ�����Ҫ��inputs���������
	 * 
	 * @param inputs
	 *            tableViewer����������
	 * @return �µ�ʵ������ʵ�����ᱻ��ӵ����������б���ȥ����ʹ���Զ���������򷵻�null
	 */
	T create(Collection<T> inputs);
}

/**
 * ���Ӳ����������ͨ���ö�����������Ҫ�ڰ�ť����Ĳ���ִ�н��������еĶ���
 * 
 * @author yanyl
 * 
 */
class ActionBuilder {
	private List<ExtendAction> actions = new ArrayList<ExtendAction>();

	/**
	 * ��Ӹ��Ӷ���
	 * 
	 * @param action
	 * @return
	 * @see ExtendAction
	 */
	public ActionBuilder append(ExtendAction action) {
		actions.add(action);
		return this;
	}

	/**
	 * ִ�����еĸ��Ӷ���
	 */
	public void excute() {
		for (ExtendAction action : actions) {
			action.run();
		}
	}
}

/**
 * ���Ӳ����ӿ�
 * 
 * @author yanyl
 * 
 */
interface ExtendAction {
	/**
	 * ִ�����⸽�Ӳ���
	 */
	void run();
}
