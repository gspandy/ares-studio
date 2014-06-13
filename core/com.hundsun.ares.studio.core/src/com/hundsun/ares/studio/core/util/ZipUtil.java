/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Zip, Unzip��ع��߷���
 * @author sundl
 */
public class ZipUtil {
	
	private static final String SEP = "/";

	/**
	 * ��ѹ������Ӧ��ѹ���ļ�����ָ����λ�á�
	 * @param is
	 * @param target
	 * @param filePattern �ļ�����·����ƥ��������ʽ������null����ȫ����ѹ
	 * @param overwrite �����Ѿ����ڵ��ļ��Ƿ񸲸�
	 * @param pm
	 */
	public static void Unzip(InputStream is, IContainer target, String filePattern, boolean overwrite, IProgressMonitor monitor) throws CoreException, IOException{
		ZipArchiveInputStream zip = new ZipArchiveInputStream(is, null, true);
		ArchiveEntry entry;
		monitor = Util.monitorFor(monitor);
		monitor.beginTask("", IProgressMonitor.UNKNOWN);
		entry = zip.getNextEntry();
		while (entry != null) {
			boolean canProcess = true;

			if (filePattern != null) {
				if (!entry.getName().matches(filePattern)) {
					canProcess = false;
				}
			}
			
			if (canProcess) {
				if (entry.isDirectory()) {
					IFolder folder = target.getFolder(new Path(entry.getName()));
					ResourcesUtil.safelyCreateFolder(folder);
				} else {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = zip.read(buffer)) > 0) {
						bos.write(buffer, 0, len);
					}
					IFile file = target.getFile(new Path(entry.getName()));
					ResourcesUtil.safelyCreateFile(file, new ByteArrayInputStream(bos.toByteArray()), overwrite, null);
				}
			}
			
//			if (!entry.isDirectory() && canProcess) {
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				byte[] buffer = new byte[1024];
//				int len;
//				while ((len = zip.read(buffer)) > 0) {
//					bos.write(buffer, 0, len);
//				}
//				IFile file = target.getFile(new Path(entry.getName()));
//				ResourcesUtil.safelyCreateFile(file, new ByteArrayInputStream(bos.toByteArray()), overwrite, null);
//			}

			monitor.worked(1);
			entry = zip.getNextEntry();
		}
		monitor.done();
	}

	/**
	 * ��ѹ������Ӧ��ѹ���ļ�����ָ����λ�á�
	 * @param is
	 * @param target
	 * @param overwrite �����Ѿ����ڵ��ļ��Ƿ񸲸�
	 * @param pm
	 */
	public static void Unzip(InputStream is, IContainer target, boolean overwrite, IProgressMonitor monitor) throws CoreException, IOException{
		Unzip(is, target, null, overwrite, monitor);
	}
	
	/**
	 * ��ѹzip�е�ĳ���ļ��е����ݵ�ָ����λ�á�
	 * ��ѹ������������ļ���·������:
	 * zip�ṹ����:
	 * 		xxx/aaa/bbb
	 * �����ѹxxx��һ��Ŀ��Ŀ¼target�Ļ��������Ϊ��
	 * 		target/aaa/bbb
	 * @param is
	 * @param parentName
	 * @param target
	 * @param filePattern
	 * @param overwrite
	 * @param monitor
	 * @throws IOException 
	 * @throws CoreException 
	 */
	public static void unzipSubEntries(InputStream is, String parentName, IContainer target, String filePattern, boolean overwrite, IProgressMonitor monitor) throws IOException, CoreException {
		ZipArchiveInputStream zip = new ZipArchiveInputStream(is, null, true);
		ArchiveEntry entry;
		monitor = Util.monitorFor(monitor);
		monitor.beginTask("", IProgressMonitor.UNKNOWN);
		entry = zip.getNextEntry();
		while (entry != null) {
			boolean canProcess = true;
			// pattern
			if (filePattern != null) {
				if (!entry.getName().matches(filePattern)) {
					canProcess = false;
				}
			}
			
			// prefix
			if (!entry.getName().startsWith(parentName + SEP)) {
				canProcess = false;
			}
			
			if (canProcess) {
				if (entry.isDirectory()) {
					IFolder folder = target.getFolder(new Path(StringUtils.substringAfter(entry.getName(), SEP)));
					ResourcesUtil.safelyCreateFolder(folder);
				} else {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = zip.read(buffer)) > 0) {
						bos.write(buffer, 0, len);
					}
					IFile file = target.getFile(new Path(StringUtils.substringAfter(entry.getName(), SEP)));
					ResourcesUtil.safelyCreateFile(file, new ByteArrayInputStream(bos.toByteArray()), overwrite, null);
				}
			}

			monitor.worked(1);
			entry = zip.getNextEntry();
		}
		monitor.done();
	}
	
	/**
	 * ��ĳ��Bundle�н�ѹ��ĳ��entry�����zip�ļ���ָ����λ�á�
	 * @param bundleName
	 * @param entry
	 * @param target
	 * @param filePattern zip�ļ�����Ҫ��ѹ���ļ���������ʽ
	 * @param overwrite
	 * @param monitor
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void UnzipFromBundle(String bundleName, String entry, IContainer target, String filePattern, boolean overwrite, IProgressMonitor monitor) throws CoreException, IOException{
		Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null)
			return;
		URL url = bundle.getEntry(entry);
		InputStream is = url.openStream();
		Unzip(is, target, filePattern, overwrite, monitor);
	}
	
	/**
	 * ��ĳ��Bundle�н�ѹ��ĳ��entry�����zip�ļ���ָ����λ�á�
	 * @param bundleName
	 * @param entry
	 * @param target
	 * @param overwrite
	 * @param monitor
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void UnzipFromBundle(String bundleName, String entry, IContainer target, boolean overwrite, IProgressMonitor monitor) throws CoreException, IOException{
		UnzipFromBundle(bundleName, entry, target, null, overwrite, monitor);
	}
	
	public static void UnzipSubEntriesFromBundle(String bundleName, String entry, String parent, IContainer target, boolean overwrite, IProgressMonitor monitor) throws IOException, CoreException {
		Bundle bundle = Platform.getBundle(bundleName);
		if (bundle == null)
			return;
		URL url = bundle.getEntry(entry);
		InputStream is = url.openStream();
		unzipSubEntries(is, parent, target, null, overwrite, monitor);
	}

	/**
	 * ��ָ����zip entry�Լ���Ӧ������д���������ȥ������������ر�������������Ҫ�Լ��������Ĺر�
	 * @param entry
	 * @param content
	 * @param output
	 * @throws IOException
	 */
	public static void writeZipEntry(ArchiveEntry entry, InputStream content, ZipArchiveOutputStream output) throws IOException {
		output.putArchiveEntry(entry);
		byte[] buffer = new byte[2048];
		int length;
		while ((length = content.read(buffer)) != -1) {
			output.write(buffer, 0, length);
		}
		output.closeArchiveEntry();
	}
	
	/**
	 * @param entry
	 * @param content
	 * @param output
	 * @throws IOException
	 * @see {@link ZipUtil#writeZipEntry(ZipEntry, InputStream, ZipOutputStream)}
	 */
	public static void writeZipEntry(ArchiveEntry entry, byte[] content, ZipArchiveOutputStream output) throws IOException {
		output.putArchiveEntry(entry);
		output.write(content);
		output.closeArchiveEntry();
	}
	
	public static void addFolder(final IContainer container, final ZipArchiveOutputStream output) {
		try {
			final IPath basePath = new Path(container.getName());
			container.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					String name = resource.getName();
					switch (resource.getType()) {
					case IResource.FOLDER:
						if (name.equals(".svn"))
							return false;
						return true;
					case IResource.FILE:
						IFile file = (IFile) resource;
						IPath rPath = resource.getFullPath().removeFirstSegments(container.getFullPath().segmentCount());
						IPath path = basePath.append(rPath);
						ZipArchiveEntry entry = new ZipArchiveEntry(path.toString());
						try {
							InputStream is = file.getContents();
							writeZipEntry(entry, is, output);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return false;
					}
					return false;
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * ��������Ϊ������Դ��ʱ����<code>iproject<code>��������Ŀ¼�ṹҲѹ����ares.<br>
	 * ����ѹ����<code>moduleName<code>Ŀ¼�£�ѹ��Ŀ¼�����У����Ե�<code>ingoreDirs<code>��Ŀ¼
	 * @param iproject
	 * @param zipStream
	 * @param moduleName
	 * @param ingoreDirs
	 * 
	 * @author xuzhen
	 * 2011-09-16
	 */
	public static void addProject(final IProject iproject, final ZipArchiveOutputStream zipStream,final String moduleName, final List<String> ingoreDirs) {
		final IPath basePath = new Path(moduleName);
		try {
			iproject.getProject().accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					String name = resource.getName();
					switch (resource.getType()) {
					case IResource.FOLDER:
						if ( ingoreDirs.contains(name) ) {
							return false;
						}
						{
							IPath rPath = resource.getFullPath().removeFirstSegments(iproject.getFullPath().segmentCount());
							IPath path = basePath.append(rPath);
							ZipArchiveEntry entry = new ZipArchiveEntry(path+"/");
							try {
								zipStream.putArchiveEntry(entry);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						return true;
					case IResource.FILE:
						{
							IFile file = (IFile) resource;
							IPath rPath = resource.getFullPath().removeFirstSegments(iproject.getFullPath().segmentCount());
							IPath path = basePath.append(rPath);
							ZipArchiveEntry entry = new ZipArchiveEntry(path.toString());
							try {
								InputStream is = file.getContents();
								ZipUtil.writeZipEntry(entry, is, zipStream);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						return false;
					case IResource.PROJECT:
						return true;
					}
					return false;
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}
	
}
