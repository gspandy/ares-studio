package com.hundsun.ares.studio.ui.aresaction;

import org.eclipse.core.resources.IFile;

import com.hundsun.ares.studio.core.IARESResource;


// ��������ͨ��Adapter��ʽ�������ּ̳�û�����塣
public interface IARESCompileAction extends IARESAction {
	/**
	 * ���ݵ�ǰ·�� ��ȡ����ɹ���·��
	 * @param path
	 * @return
	 */
	IFile[] getCompileResultPath(IARESResource res);
}
