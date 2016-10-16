package org.ncu.xuebalibrary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.ncu.xuebalibrary.config.Strings;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class FileUtil {
	
	public static final HashMap<String, String> AllowedTypes = new HashMap<String, String>(){
		private static final long serialVersionUID = 5142441353734092047L;
		{
			put("application/msword", ".doc");
			put("application/vnd.ms-excel", ".xls");
			put("application/vnd.ms-powerpoint", ".ppt");
			
			put("application/x-xls", ".xls");
			put("application/x-ppt", ".ppt");
			
			put("application/pdf", ".pdf");
			
			put("text/plain", ".txt");
		}};

	public static File getDocDirectory(Object obj) {
		
		if(obj == null) return null;
		
		File dirFile = null;
		try {
			String classPath = obj.getClass().getClassLoader().getResource("").getPath();
			String path = classPath.substring(0, classPath.indexOf(Strings.PROJECT_NAME)) + Strings.DIR_DOC;
			dirFile = new File(path);
			if(!dirFile.exists() || !dirFile.isDirectory()) dirFile.mkdir();
			dirFile.setReadable(true);
			dirFile.setWritable(true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return dirFile;
	}
	
	public static File getSwfDirectory(Object obj) {
		
		if(obj == null) return null;
		
		File dirFile = null;
		try {
			String classPath = obj.getClass().getClassLoader().getResource("").getPath();
			String path = classPath.substring(0, classPath.indexOf(Strings.PROJECT_NAME)) + Strings.DIR_SWF;
			dirFile = new File(path);
			if(!dirFile.exists() || !dirFile.isDirectory()) dirFile.mkdir();
			dirFile.setReadable(true);
			dirFile.setWritable(true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return dirFile;
	}
	
	public static boolean deleteDocFile(Object obj, String filename) {
		
		if(obj == null || filename == null || filename.length() == 0) return false;
		
		try {
			File parent = getDocDirectory(obj);
			if(parent == null) throw new Exception(Strings.FAIL_0027);
			File file = new File(parent, filename);
			if(!file.delete()) file.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean deleteSwfFile(Object obj, String filename) {
		
		if(obj == null || filename == null || filename.length() == 0) return false;
		
		try {
			File parent = getSwfDirectory(obj);
			if(parent == null) throw new Exception(Strings.FAIL_0027);//TODO
			File file = new File(parent, filename);
			if(!file.delete()) file.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static String getDocname(long userId) {
		
		if(userId <= 0) return null;
		
		return (new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date()) + "-" + userId;
	}
	
	public static String getFilenameWithoutSuffix(String filename) {
		
		if(filename == null || filename.length() == 0) return null;
		
		int index = filename.lastIndexOf(".");
		if(index == -1 || index == 0) return null;
		
		String filenameWithoutSuffix = filename.substring(0, index);
		
		return filenameWithoutSuffix;
	}
	
	public static String getSuffix(String filename) {
		
		if(filename == null || filename.length() == 0) return null;
		
		int index = filename.lastIndexOf(".");
		if(index == -1) return null;
		
		String suffix = filename.substring(index).toLowerCase();
		if(!AllowedTypes.containsValue(suffix)) return null;
		
		return suffix;
	}
	
	//TODO 不支持docx,xlsx,pptx, 支持doc,xls,ppt,txt,pdf
	public static boolean toSwf(Object obj, String filename) {
		
		if(obj == null || filename == null || filename.length() == 0) return false;
		
		String filenameWithoutSuffix = getFilenameWithoutSuffix(filename);
		String suffix = getSuffix(filename);
		if(filenameWithoutSuffix == null || suffix == null) return false;
		
		File swfDir = getSwfDirectory(obj);
		File docFile = null;
		File pdfFile = null;
		
		try {
			docFile = new File(getDocDirectory(obj), filename);
			pdfFile = new File(swfDir, filenameWithoutSuffix + ".pdf");
		} catch (Exception e) {
			e.printStackTrace();
			if(pdfFile != null && pdfFile != docFile && !pdfFile.delete()) pdfFile.deleteOnExit();
			return false;
		}
		
		if(!suffix.equals(AllowedTypes.get("application/pdf"))) {
			OpenOfficeConnection connection = null;
			try {
				connection = new SocketOpenOfficeConnection(8100);
				connection.connect();
				
				DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
				converter.convert(docFile, pdfFile);
				
			} catch (Exception e) {
				e.printStackTrace();
				if(pdfFile != null && pdfFile != docFile && !pdfFile.delete()) pdfFile.deleteOnExit();
				return false;
				
			} finally {
				if(connection != null) connection.disconnect();
			}
		} else {
			pdfFile = docFile;
		}
		
		String command = "\"" + Strings.LIB_SWFTOOLS + "\" -o \"" + swfDir.getAbsolutePath() + "\\" + filenameWithoutSuffix + ".swf\" -s flashversion=9 \"" + pdfFile.getAbsolutePath() + "\"";
		Process pro = null;
		try {
			pro = Runtime.getRuntime().exec(command);
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while (bufferedReader.readLine() != null);
			bufferedReader.close();
			
			pro.waitFor();
			
		} catch (Exception e) {
			e.printStackTrace();
			if(pro != null) pro.destroy();
			if(pdfFile != null && pdfFile != docFile && !pdfFile.delete()) pdfFile.deleteOnExit();
			return false;
		}
		
		if(pdfFile != null && pdfFile != docFile && !pdfFile.delete()) pdfFile.deleteOnExit();
		if(pro.exitValue() != 0) {
			pro.destroy();
			return false;
		}
		pro.destroy();
		return true;
	}
}
