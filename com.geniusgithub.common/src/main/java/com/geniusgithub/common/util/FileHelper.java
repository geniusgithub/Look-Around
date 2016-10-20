package com.geniusgithub.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public class FileHelper {

	private final static String TAG = FileHelper.class.getSimpleName();
	private static final int FILE_BUFFER_SIZE = 51200;
	
	
	public static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			AlwaysLog.e(TAG, "param invalid, filePath: " + filePath);
			return false;
		}

		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
	}
	

	
	public static boolean createDirectory(String filePath){
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		if (file.exists()){
			return true;
		}
		
		return file.mkdirs();

	}
	
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			AlwaysLog.e(TAG, "Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				AlwaysLog.d(TAG, "delete filePath: " + list[i].getAbsolutePath());
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}

		AlwaysLog.d(TAG, "delete filePath: " + file.getAbsolutePath());
		file.delete();
		return true;
	}

	
	public static boolean writeFile(String filePath, String fileContent) {
		return writeFile(filePath, fileContent, false);
	}
	
	public static boolean writeFile(String filePath, String fileContent, boolean append) {
		if (null == filePath || fileContent == null || filePath.length() < 1 || fileContent.length() < 1) {
			AlwaysLog.e(TAG, "Invalid param. filePath: " + filePath + ", fileContent: " + fileContent);
			return false;
		}
		
		try {
	       File file = new File(filePath);
	       if (!file.exists()) {
	    	   if (!file.createNewFile()) {
	    		   return false;
	    	   }
	       }
	       
	       BufferedWriter output = new BufferedWriter(new FileWriter(file, append));
	       output.write(fileContent);
	       output.flush();
	       output.close();
		} catch (IOException ioe) {
			AlwaysLog.e(TAG, "writeFile ioe: " + ioe.toString());
			return false;
		}
		
		return true;
	}
	
	public static long getFileSize(String filePath) {
		if (null == filePath) {
			AlwaysLog.e(TAG, "Invalid param. filePath: " + filePath);
			return 0;
		}
		
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}

		return file.length();
	}
	
	public static long getFileModifyTime(String filePath) {
		if (null == filePath) {
			AlwaysLog.e(TAG, "Invalid param. filePath: " + filePath);
			return 0;
		}
		
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}

		return file.lastModified();
	}
	
	public static boolean setFileModifyTime(String filePath, long modifyTime) {
		if (null == filePath) {
			AlwaysLog.e(TAG, "Invalid param. filePath: " + filePath);
			return false;
		}
		
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}

		return file.setLastModified(modifyTime);
	}
	
	public static boolean copyFile(ContentResolver cr, String fromPath, String destUri) {
		if (null == cr || null == fromPath || fromPath.length() < 1 || null == destUri || destUri.length() < 1) {
			AlwaysLog.e(TAG, "copyFile Invalid param. cr="+cr+", fromPath="+fromPath+", destUri="+destUri);
			return false;
		}
		
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(fromPath);
			if (null == is) {
				AlwaysLog.e(TAG, "Failed to open inputStream: "+fromPath+"->"+destUri);
				return false;
			}
			
			// check output uri
			String path = null;
			Uri uri = null;
			
			String lwUri = destUri.toLowerCase();
			if (lwUri.startsWith("content://")) {
				uri = Uri.parse(destUri);
			} else if (lwUri.startsWith("file://")) {
				uri = Uri.parse(destUri);
				path = uri.getPath();
			} else {
				path = destUri;
			}
			
			// open output 
			if (null != path) {
				File fl = new File(path);
	            String pth = path.substring(0, path.lastIndexOf("/"));
	            File pf = new File(pth);
	            
	            if (pf.exists() && !pf.isDirectory()) {
	            	pf.delete();
	            }
	            
	            pf = new File(pth+File.separator);
	            
	            if (!pf.exists()) {
	                if (!pf.mkdirs()) {
	                    AlwaysLog.e(TAG, "Can't make dirs, path=" + pth);
	                }
	            }
	            
	            pf = new File(path);
	            if (pf.exists()) {
	            	if (pf.isDirectory()) deleteDirectory(path);
	            	else pf.delete();
	            }
	            
				os = new FileOutputStream(path);
				fl.setLastModified(System.currentTimeMillis());
			} else {
				os = new ParcelFileDescriptor.AutoCloseOutputStream(cr.openFileDescriptor(uri, "w"));
			}
			
			// copy file
			byte[] dat = new byte[1024];
			int i = is.read(dat);
			while(-1 != i) {
				os.write(dat, 0, i);
				i = is.read(dat);
			}
			
			is.close();
			is = null;
			
			os.flush();
			os.close();
			os = null;
			
			return true;
			
		} catch(Exception ex) {
			AlwaysLog.e(TAG, "Exception, ex: " + ex.toString());
		} finally {
			if(null != is) {
				try{is.close();} catch(Exception ex) {};
			}
			if(null != os) {
				try{os.close();} catch(Exception ex) {};
			}
		}
		return false;
	}

	
	/*************ZIP file operation***************/
	public static boolean readZipFile(String zipFileName, StringBuffer crc) {
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				long size = entry.getSize();
				crc.append(entry.getCrc() + ", size: " + size);
	        }
	        zis.close();
		} catch (Exception ex) {
			AlwaysLog.e(TAG, "Exception: " + ex.toString());
			return false;
		}
		return true;
	}
	
	public static byte[] readGZipFile (String zipFileName) {
		if (fileIsExist(zipFileName)) {
			AlwaysLog.i(TAG, "zipFileName: " + zipFileName);
			try {
				FileInputStream fin = new FileInputStream(zipFileName);
				int size;
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((size = fin.read(buffer, 0, buffer.length)) != -1) {
					baos.write(buffer, 0, size);
				}
				return baos.toByteArray();
			} catch (Exception ex) {
				AlwaysLog.i(TAG, "read zipRecorder file error");
			}
		}
		return null;
	}


}