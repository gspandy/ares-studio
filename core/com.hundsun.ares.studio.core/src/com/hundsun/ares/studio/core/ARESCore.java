/*
 * ϵͳ����: ARES Ӧ�ÿ��ٿ�����ҵ�׼�
 * ģ������:
 * �� �� ��: ARESCore.java
 * �����Ȩ: ���ݺ������ӹɷ����޹�˾
 * ����ĵ�:
 * �޸ļ�¼:
 * �޸�����      �޸���Ա                     �޸�˵��<BR>
 * ========     ======  ============================================
 * 20110224     mawb	��Ӧ�޸ĵ��ţ�20110128022
 * ========     ======  ============================================
 * �����¼��
 * 
 * ������Ա��
 * �������ڣ�
 * �������⣺
 */
package com.hundsun.ares.studio.core;

import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import com.hundsun.ares.studio.core.logging.ARESLevelMatchFilter;
import com.hundsun.ares.studio.internal.core.ARESModel;
import com.hundsun.ares.studio.internal.core.ARESModelManager;
import com.hundsun.ares.studio.internal.core.ExternalResPathEntry;
import com.hundsun.ares.studio.internal.core.MementoTokenizer;
import com.hundsun.ares.studio.internal.core.ResPathEntry;

/**
 * The activator class controls the plug-in life cycle
 */
public class ARESCore extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hundsun.ares.studio.core";
	
	// The nature ID
	public static final String NATURE_ID = PLUGIN_ID + ".aresnature";
	public static final String BUILDER_ID = PLUGIN_ID + ".aresbuilder";

	/** ��Ŀ���Ե���չҳ��NatureID */
	public static final String EXTEND_NATURE = PLUGIN_ID + ".extendnature";
	public static final String REF_NATURE = PLUGIN_ID + ".refnature";
	
	// The shared instance
	private static ARESCore plugin;
	
//	private static MessageConsoleStream stream;
//	private static PrintWriter writer;
	
	
	public static Logger logger = Logger.getLogger(ARESCore.class.getName());

	/**
	 * ��ǰʹ�õĽŲ�����������ǰ�� JavaScript
	 */
	public static final String SCRIPT_ENGINE_NAME = "JavaScript";
		
	/**
	 * The constructor
	 */
	public ARESCore() {
	}
	
	public static IARESModel getModel() {
		return ARESModelManager.getManager().getModel();
	}
	
	public static IARESElement create(IResource resource) {
		return ARESModelManager.getManager().create(resource);
	}
	
	public static IARESProject create(IProject project) {
		return ARESModelManager.getManager().create(project);
	}
	
	public static IARESElement create(String handleIdentifier) {
		if (handleIdentifier == null)
			return null;
		MementoTokenizer memento = new MementoTokenizer(handleIdentifier);
		ARESModel model = (ARESModel) ARESModelManager.getManager().getModel();
		return model.getHandleFromMemento(memento);
	}

	public static void addElementListener(IARESElementChangeListener listener) {
		ARESModelManager.getManager().addElementChangeListener(listener);
	}
	
	public static void removeElementListener(IARESElementChangeListener listener) {
		ARESModelManager.getManager().addElementChangeListener(listener);
	}
	
	/**
	 * ���һ������̨�����������֮�����رա�
	 * @return
	 */
//	public MessageConsoleStream newConsoleStream() {
//		MessageConsole console = ConsoleHelper.getConsole();
//		return console.newMessageStream();
//	}
	
	/**
	 * ��ȡ��ܹ����һ������̨���������ܸ�����������������ڵĹ���ʹ����<b>����</b>�ر��������
	 * @return
	 */
//	public MessageConsoleStream getSystemConsoleStream() {
//		if (stream == null)
//			stream = newConsoleStream();
//		return stream;
//	}
//	
//	public PrintWriter getSystemConsoleWriter() {
//		if (writer == null) {
//			MessageConsoleStream stream = getSystemConsoleStream();
//			writer = new PrintWriter(stream, true);
//		}
//		return writer;
//	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		

		// IMPORTANT: ������ʼ������Ӧ�ö������֮��
		setUpLogger();
		ARESModelManager.getManager().startUp();
	}
	
	// ��ʼ���Զ������־���á�
	private void setUpLogger() {
//		try {			
//			// ����class-loader�����⣬�������ļ������ö�û�����ã���ʱֻ���ó���ʽ�������á�
//			Logger aresRootLogger = Logger.getLogger("com.hundsun.ares.studio");
//			ConsoleHandler handler = new ConsoleHandler();
//			handler.setFormatter(new ARESLoggingFormatter());
//			aresRootLogger.addHandler(handler);
//			aresRootLogger.setUseParentHandlers(false);
//			logger.fine("ARES Logger set up succesfully.");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			logError("Setup ARES logger failed!", ex);
//			logger.log(Level.SEVERE, "Setup ARES logger failed!", ex);
//		}
		
		Logger consoleLogger = Logger.getLogger("com.hundsun.ares.studio.core.Console");
		consoleLogger.setAdditivity(false);
		WriterAppender appender = (WriterAppender) consoleLogger.getAppender("ARESConsole");
		if (appender != null) {
			// ����info��Ӧ��appender
			OutputStream infoStream = System.out;
			OutputStream warnStream = System.err;
			OutputStream errorStream = System.err;
			
			appender.setWriter(new PrintWriter(infoStream) {
				@Override
				public void close() {
					// bug: ��Ϊ���ares ui������ڵ�ʱ�򣬻����裬setWriter��ʱ��log4j���Զ��ر�ԭ�е�writter������system.out���ر�...
					// ���Դ˴�close��ʱ���Ȱ�out���null������sys.out���ر�...
					out = null;
					super.close();
				}
			});
			
			appender.addFilter(new ARESLevelMatchFilter(Level.INFO));
			
			PatternLayout layout = (PatternLayout) appender.getLayout();
			
			// ���error��Ӧ��appender
			WriterAppender errorAppender = new WriterAppender(
					new PatternLayout(layout.getConversionPattern()), 
					new PrintWriter(errorStream));
			errorAppender.addFilter(new ARESLevelMatchFilter(Level.ERROR));
			consoleLogger.addAppender(errorAppender);
			
			// ���warn��Ӧ��appender
			WriterAppender warnAppender = new WriterAppender(
					new PatternLayout(layout.getConversionPattern()), 
					new PrintWriter(warnStream));
			warnAppender.addFilter(new ARESLevelMatchFilter(Level.WARN));
			consoleLogger.addAppender(warnAppender);
		}
	}

	public void logError(String message, Throwable e) {
		plugin.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, -1, message, e));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		ARESModelManager.getManager().shutDown();
//		if (writer != null)
//			writer.close();
//		if (stream != null && !stream.isClosed())
//			stream.close();
		
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ARESCore getDefault() {
		return plugin;
	}
	
	/**
	 * ����һ��ָ��������Ŀ��Entry.
	 * @param path ������Ŀ��·��������/testproject
	 * @return ��Ӧ��ָ��·����Ŀ��Entry
	 */
	public static IResPathEntry newProjectEntry(IPath path, String type) {
		return new ResPathEntry(IResPathEntry.RPE_PROJECT, IARESModuleRoot.KIND_SOURCE, type, path);
	}
	
	public static IResPathEntry newProjectEntry(IPath path) {
		return new ResPathEntry(IResPathEntry.RPE_PROJECT, IARESModuleRoot.KIND_SOURCE, null, path);
	}
	
	/**
	 * ����һ��ָ��Source�ļ��е�Entry.
	 * @param rootId ��Ӧ��ModuleRoot����ID
	 * @param path ��ӦModuleRoot��·��
	 * @return ��Ӧpath�����͵�source Entry
	 */
	public static IResPathEntry newSourceEntry(String rootId, IPath path) {
		return new ResPathEntry(IResPathEntry.RPE_SOURCE, IARESModuleRoot.KIND_SOURCE, rootId, path);
	}
	
	/**
	 * ����һ����ӦZip����Entry.
	 * @param path �����ļ�path
	 * @return ��Ӧָ���İ���Entry
	 */
	public static IResPathEntry newLibEntry(IPath path) {
		return new ResPathEntry(IResPathEntry.RPE_LIBRAY, IARESModuleRoot.KIND_BINARY, null, path);
	}
	
	/**
	 * ����һ��ָ��������Ŀ��Entry.
	 * @param path ������Ŀ��·��������/testproject
	 * @return ��Ӧ��ָ��·����Ŀ��Entry
	 */
	public static IExternalResPathEntry newExternalProjectEntry(IPath path, String type) {
		return new ExternalResPathEntry(IResPathEntry.RPE_PROJECT, IARESModuleRoot.KIND_SOURCE, type, path);
	}
	
	public static IExternalResPathEntry newExternalProjectEntry(IPath path) {
		return new ExternalResPathEntry(IResPathEntry.RPE_PROJECT, IARESModuleRoot.KIND_SOURCE, null, path);
	}
	
	/**
	 * ����һ��ָ��Source�ļ��е�Entry.
	 * @param rootId ��Ӧ��ModuleRoot����ID
	 * @param path ��ӦModuleRoot��·��
	 * @return ��Ӧpath�����͵�source Entry
	 */
	public static IExternalResPathEntry newExternalSourceEntry(String rootId, IPath path) {
		return new ExternalResPathEntry(IResPathEntry.RPE_SOURCE, IARESModuleRoot.KIND_SOURCE, rootId, path);
	}
	
	/**
	 * ����һ����ӦZip����Entry.
	 * @param path �����ļ�path
	 * @return ��Ӧָ���İ���Entry
	 */
	public static IExternalResPathEntry newExternalLibEntry(IPath path) {
		return new ExternalResPathEntry(IResPathEntry.RPE_LIBRAY, IARESModuleRoot.KIND_BINARY, null, path);
	}
	
}
