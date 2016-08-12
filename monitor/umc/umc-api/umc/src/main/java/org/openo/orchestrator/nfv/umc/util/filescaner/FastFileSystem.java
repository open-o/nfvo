package org.openo.orchestrator.nfv.umc.util.filescaner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openo.orchestrator.nfv.umc.pm.common.GeneralFileLocaterImpl;
import org.openo.orchestrator.nfv.umc.util.ExtensionAccess;

public class FastFileSystem {

	private static int WORK_THEAD_NUM = Runtime.getRuntime()
			.availableProcessors();

	private static Map<String, FastDir> dirToFastDir = new ConcurrentHashMap<String, FastDir>();

	private static Logger logger = Logger.getLogger(FastFileSystem.class);

	private ExecutorService pool = null;

	private static FastFileSystem fastFileSystem = null;
	
	private static String initDir = null;

	private FastFileSystem() {
	}

	public static void init()
	{
		setInitDir(GeneralFileLocaterImpl.getGeneralFileLocater().getConfigPath());
		FastFileSystem.getInstance();
		File descFiles[] = getFiles("*-extendsdesc.xml");
		File implFiles[] = getFiles("*-extendsimpl.xml");
		ExtensionAccess.tryToInjectExtensionBindings(descFiles, implFiles);
	}
	public static FastFileSystem getInstance() {
		if (fastFileSystem == null) {
			fastFileSystem = new FastFileSystem();
			fastFileSystem.initFFS();
		}
		return fastFileSystem;
	}

	private void initFFS() {

		String processPath = null;
		try {
			processPath = getInitDir();
		} catch (Exception ignore) {
			System.out.println(ignore.getMessage());
			return;
		}

		File procDir = new File(processPath);

		try {
			long start = System.currentTimeMillis();
			pool = new ThreadPoolExecutor(WORK_THEAD_NUM, WORK_THEAD_NUM, 0L,
					TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
					Executors.defaultThreadFactory(), new CallerRunsPolicy());

			// mk porcess
			FastDir procFastDir = new FastDir(procDir);
			dirToFastDir.put(procDir.getAbsolutePath(), procFastDir);
			addSubDir(procFastDir);

			List<FastDir> subDirs = procFastDir.getSubDirs();
			CountDownLatch rootlatch = new CountDownLatch(subDirs.size());
			for (FastDir subDir : subDirs) {

				SubDirSearchRunner runner = new SubDirSearchRunner(subDir,
						rootlatch);
				pool.execute(runner);

			}
			rootlatch.await();
			pool.shutdownNow();
			long end = System.currentTimeMillis() - start;

		} catch (Exception e) {
		    logger.error("Unable to initialize FFS, ignore: "
					+ e.getMessage());
		}
	}

	private FastFileSystem(String path) {
		init(path);
	}

	protected static FastFileSystem getInstance_test(String path) {
		FastFileSystem retFastFileSystem = new FastFileSystem(path);
		return retFastFileSystem;
	}

	private static void addSubDir(FastDir parDir) {
		String[] file_list = parDir.getDir().list();// �г�Ŀ¼�µ������ļ���Ŀ¼
		for (int i = 0; i < file_list.length; i++) {// ѭ������
			String filename = parDir.getDir() + File.separator + file_list[i];// 
			File current_file = new File(filename);
			if (current_file.isFile()) {// ������ļ�
				parDir.addSubFile(current_file.getName(), current_file);
			} else {// �����Ŀ¼
				FastDir newsubDir = new FastDir(current_file);
				parDir.addSubDir(newsubDir);
				dirToFastDir.put(current_file.getAbsolutePath(), newsubDir);
			}

		}
	}

	private void init(FastDir rootFastDir) {

		SearchRunner runner = new SearchRunner(rootFastDir, dirToFastDir);
		runner.run();

	}

	private void init(String processPath) {
		File rootDirFile = new File(processPath);
		FastDir newFastDir = new FastDir(rootDirFile);
		dirToFastDir.put(rootDirFile.getAbsolutePath(), newFastDir);
		SearchRunner runner = new SearchRunner(newFastDir, dirToFastDir);
		runner.run();

	}

	public FastDir getFastDir(File dir) {
		FastDir ffdir = dirToFastDir.get(dir.getAbsolutePath());
		return ffdir;
	}

	protected static String getInitDir() {
		return initDir;
	}

	public static void setInitDir(String initDir) {
		FastFileSystem.initDir = initDir;
	}


	class SubDirSearchRunner implements Runnable {

		private FastDir rootFastDir;
		private CountDownLatch latch;

		SubDirSearchRunner(FastDir rootFastDir, CountDownLatch latch) {
			this.rootFastDir = rootFastDir;
			this.latch = latch;
		}

		@Override
        public void run() {

			try {
				init(rootFastDir);
			} finally {
				latch.countDown();
			}

		}

	}

	private static void getFiles(File searchDIR, ExtendedFileFilter filter,
			LinkedList<File> foundedFiles) {
		FastFileSystem ffs = FastFileSystem.getInstance();
		FastDir fastDir = ffs.getFastDir(searchDIR);

		if (fastDir != null) {

			List<File> searchFiles = fastDir.filterFiles(filter);
			if (searchFiles != null) {
				foundedFiles.addAll(searchFiles);
			}

			List<FastDir> subFastDirs = fastDir.getSubDirs();
			for (FastDir subDir : subFastDirs) {
				getFiles(subDir.getDir(), filter, foundedFiles);
			}
		}
	}

	public static File[] getFiles( String filePattern)
	{
		return getFiles(null, filePattern);
	}
	
	public static File[] getFiles(String dir, String filePattern)
	{
		String filePath = FastFileSystem.initDir;
		if (dir != null)
		{
			filePath = filePath + File.separator + dir;
		}
		ExtendedFileFilter filter = new ExtendedFileFilter(
				filePattern, true);
		LinkedList<File> list = new LinkedList<File>();
		getFiles(new File(filePath), filter, list);
		File[] files = new File[list.size()];
		list.toArray(files);
		return files;
	}
	
	public static File getFile(String dir, String fileName){
	    String filePath = FastFileSystem.initDir;
        if (dir != null)
        {
            filePath = filePath + File.separator + dir;
        }
        
        File file = new File(filePath + File.separator + fileName);
        if(!file.exists() || !file.canRead()){
            return null;
        }
        
        return file;
	}
	
	public static void main(String[] args) {
		//setInitDir("/E:\\GIT\\OpenO\\umc_remote\\umc-api\\microservice-standalone\\target\\assembly\\conf");
	    setInitDir("D:\\GitRoot\\umc\\umc-api\\microservice-standalone\\src\\main\\assembly\\conf");
		getInstance();
		ExtendedFileFilter filter = new ExtendedFileFilter(
				"*-i18n.yml", true);
		LinkedList<File> list = new LinkedList<File>();
		long zero = System.currentTimeMillis();
		//getFiles(new File("/E:\\GIT\\OpenO\\umc_remote\\umc-api\\microservice-standalone\\target\\assembly\\conf"), filter, list);
		getFiles(new File("D:\\GitRoot\\umc\\umc-api\\microservice-standalone\\src\\main\\assembly\\conf"), filter, list);
//		getFiles("D:\\ems_test\\netnumen40\\ums-server", filter, list);
		long div = System.currentTimeMillis() - zero;
//		if (div >= 50) {
			System.out.println("found files from fast file system: "
					+ filter  + div);
//		}
		File[] files = new File[list.size()];
		list.toArray(files);
		System.out.println("done file count:" + files.length);
	}
}

class SearchRunner implements Runnable {

	private FastDir parDir;
	private Map<String, FastDir> dirToFastDir = null;

	// private CountDownLatch latch;

	SearchRunner(FastDir parDir, Map<String, FastDir> dirToFastDir) {
		this.parDir = parDir;
		this.dirToFastDir = dirToFastDir;
	}

	@Override
    public void run() {
		walkDir(parDir, dirToFastDir);
	}

	private static void walkDir(FastDir parDir,
			Map<String, FastDir> dirToFastDir) {
		File directory = parDir.getDir();
		String file_list[] = directory.list();
		if (file_list != null) {
			for (int i = 0; i < file_list.length; i++) {
				String filename = directory + File.separator + file_list[i];
				File current_file = new File(filename);
				if (current_file.isFile()) {
					parDir.addSubFile(current_file.getName(), current_file);
				} else {// �����Ŀ¼
					FastDir newFastDir = new FastDir(current_file);
					parDir.addSubDir(newFastDir);
					dirToFastDir
							.put(current_file.getAbsolutePath(), newFastDir);
					walkDir(newFastDir, dirToFastDir);
				}

			}
		}
	}

	
}
