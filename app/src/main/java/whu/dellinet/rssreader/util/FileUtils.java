package whu.dellinet.rssreader.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.os.Environment;

public class FileUtils {
	public static String getSDRootPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory()+"";
		}else{
			return null;
		}
	}
	public static boolean saveToFile(InputStream in,String path,String fileName) {
		FileOutputStream outputStream=null;
		byte[] buffers=new byte[1024*4];
		File file=new File(path, fileName);
		try {
			file.createNewFile();
			outputStream=new FileOutputStream(file);
			while (in.read(buffers)!=-1) {
				outputStream.write(buffers);
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			if (outputStream!=null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static long getDirSize(File dir) {
		if (dir==null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize=0;
		File[] files=dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize+=file.length();
			}else if (file.isDirectory()) {
				dirSize+=file.length();
				dirSize+=getDirSize(file);
			}
		}
		return dirSize;
	}
	public static String formatFileSize(long fileSize) {
		DecimalFormat format=new DecimalFormat("#.00");
		String fileSizeString="";
		if (fileSize<1024) {
			fileSizeString=format.format((double)fileSize)+"B";
		}else if (fileSize<1024*1024) {
			fileSizeString=format.format((double)fileSize/1024)+"KB";
		}else if (fileSize<1024*1024*1024) {
			fileSizeString=format.format((double)fileSize/(1024*1024))+"MB";
		}else {
			fileSizeString=format.format((double)fileSize/(1024*1024*1024))+"GB";
		}
		return fileSizeString;
	}
	//删除指定目录下的文件，此目录下不能有子目录存在
	public static boolean deleteDirectory(String dirPath) {
		SecurityManager securityManager=new SecurityManager();
		if ((!dirPath.equals(""))&&(dirPath!=null)) {
			File file=new File(dirPath);
			securityManager.checkDelete(file.toString());
			if (file.isDirectory()) {
				File[] files=file.listFiles();
				for (File file2 : files) {
					file2.delete();
				}
				file.delete();
				return true;
			}
			
		}
		return false;
	}
	public static boolean deleteAllFile(String dirPath) {
		boolean state=false;
		File file=new File(dirPath);
		if (file.isDirectory()) {
			String[] fileNames=file.list();
			for (String string : fileNames) {
				deleteAllFile(dirPath+File.separator+string);
			}
		}
		file.delete();
		return true;
		
	}
	//根据文件路径创建文件，不覆盖源文件
	public static File creatNewFile(String filePath) {
		File file=new File(filePath);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
}
