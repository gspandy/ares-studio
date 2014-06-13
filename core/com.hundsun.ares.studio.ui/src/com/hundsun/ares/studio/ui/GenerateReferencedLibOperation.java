/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IReferencedLibrary;
import com.hundsun.ares.studio.core.util.ZipUtil;
import com.hundsun.ares.studio.internal.core.ReferencedLibraryInfo;
import com.hundsun.ares.studio.internal.core.ReferencedLibrayUtil;

/**
 * �������ð��Ĳ���
 * @author sundl
 */
public class GenerateReferencedLibOperation implements IRunnableWithProgress {

	protected IARESProject project;
	private ReferencedLibraryInfo info;
	private String outputPath;
	
	/**
	 * @param project ��Ŀ
	 * @param outputPath ���·���������Ӧ���ļ��Ѿ����ڣ���ֱ�Ӹ���
	 */
	public GenerateReferencedLibOperation(IARESProject project, ReferencedLibraryInfo info,String outputPath) {
		this.project = project;
		this.info = info;
		this.outputPath = outputPath;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		Assert.isNotNull(project, "û����Ŀ?");
		Assert.isNotNull(outputPath, "·������Ϊ��");
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		
		ZipArchiveOutputStream zos = null;
		try {
			// collect resources
			List<IARESResource> resourcesTobeExport = new ArrayList<IARESResource>();
			List<IARESModule> modulesToBeExport = new ArrayList<IARESModule>();
			List<IARESModuleRoot> rootsToBeExport = new ArrayList<IARESModuleRoot>();
			
			for (IARESModuleRoot root : project.getModuleRoots()) {
				if (!root.isArchive()) {
					rootsToBeExport.add(root);
					for (IARESModule module : root.getModules()) {
						modulesToBeExport.add(module);
						resourcesTobeExport.addAll(Arrays.asList(module.getARESResources())); 
					}
				}
			}
			
			monitor.beginTask("�������ð�", resourcesTobeExport.size() + 1);
			File file = new File(outputPath);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			zos = new ZipArchiveOutputStream(fos);
			
			// resources
			for (IARESResource aresRes : resourcesTobeExport) {
				monitor.subTask("������Դ��" + aresRes.getFullyQualifiedName());
				IFile resFile = (IFile)aresRes.getResource();
				IPath path = resFile.getProjectRelativePath();
				InputStream content = resFile.getContents();
				ZipArchiveEntry entry = new ZipArchiveEntry(path.toString());
				ZipUtil.writeZipEntry(entry, content, zos);
				monitor.worked(1);
			}
			
			addExternalFiles(zos);
			
			// .aar
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ReferencedLibrayUtil.writeRefLibInfo(info, bos);
			ZipArchiveEntry entry = new ZipArchiveEntry(IReferencedLibrary.PROPERTIE_FILE);
			ZipUtil.writeZipEntry(entry, bos.toByteArray(), zos);
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	protected void addExternalFiles(ZipArchiveOutputStream zipStream) {
		// Ĭ�ϲ���������ļ��к��ļ�
	}

}
