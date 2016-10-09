package org.ncu.xuebalibrary.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.dao.CategoryDAO;
import org.ncu.xuebalibrary.dao.DocumentDAO;
import org.ncu.xuebalibrary.dao.UserDAO;
import org.ncu.xuebalibrary.entity.Document;
import org.ncu.xuebalibrary.entity.User;
import org.ncu.xuebalibrary.util.FileUtil;
import org.ncu.xuebalibrary.util.SecurityUtil;
import org.ncu.xuebalibrary.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

	@Autowired
	private DocumentDAO documentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	
	public boolean upload(long id, File uploadFile, String uploadFileName, String uploadContentType, List<Object> list, List<String> info) {
		
		if(id <= 0 || uploadFile == null || uploadFileName == null || uploadContentType == null || list == null || !checkFileName(uploadFileName) || !checkFileType(uploadContentType)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		if(!checkHash(uploadFile, list)) {
			if(info != null) info.add(Strings.FAIL_0030);
			return false;
		}
		
		File parent = null;
		String filename = null;
		File file = null;
		try {
			byte[] buffer = new byte[1024];
			
			FileInputStream inStream = new FileInputStream(uploadFile);
			
			parent = FileUtil.getDocDirectory(this);
			if(parent == null) {
				inStream.close();
				if(info != null) info.add(Strings.FAIL_0027);
				return false;
			}
			filename = FileUtil.getDocname(id);
			if(filename == null) {
				inStream.close();
				if(info != null) info.add(Strings.FAIL_0028);
				return false;
			}
			filename = filename + FileUtil.AllowedTypes.get(uploadContentType);
			list.add(filename);
			file = new File(parent, filename);
			FileOutputStream outStream = new FileOutputStream(file);
			
			int length = 0;
			while((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}
			
			inStream.close();
			outStream.flush();
			outStream.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			if(info != null) info.add(Strings.FAIL_0029);
			return false;
		}
		
		if(!FileUtil.toSwf(this, filename)) {
			if(!file.delete()) file.deleteOnExit();
			if(info != null) info.add(Strings.FAIL_0038);
			return false;
		}
		
		if(info != null) info.add(Strings.SUCCESS_0009);
		return true;
	}
	
	public boolean create(String title, String summary, String filename, long categoryid, long userid, int price, String hash, List<String> info) {
		
		if(title == null || filename == null || categoryid <= 0 || userid  <= 0 || price < 0 || hash == null || !checkTitle(title) || (summary != null && !checkSummary(summary)) || filename.length() == 0 || !categoryDAO.checkId(categoryid) || !userDAO.checkId(userid) || !checkPrice(price) || hash.length() == 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		String filenameWithoutSuffix = FileUtil.getFilenameWithoutSuffix(filename);
		String suffix = FileUtil.getSuffix(filename);
		String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		if(filenameWithoutSuffix == null || suffix == null) {
			if(info != null) info.add(Strings.FAIL_0032);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("title", title);
		if(summary != null) map.put("summary", summary);
		map.put("path", filenameWithoutSuffix);
		map.put("category_id", "" + categoryid);
		map.put("user_id", "" + userid);
		map.put("price", "" + price);
		map.put("hash", hash);
		map.put("suffix", suffix);
		map.put("created", time);
		map.put("modified", time);
		
		boolean flag = documentDAO.insert(map) == 1;
		
		if(flag) {
			//TODO trigger
			userDAO.addDocument(1);
			if(info != null) info.add(Strings.SUCCESS_0010);
		} else {
			if(info != null) info.add(Strings.FAIL_0033);
		}
		
		return flag;
	}
	
	public boolean download(long userid, long id, List<Object> list, List<String> info) {
		
		if(userid <= 0 || id <= 0 || list == null/* || !userDAO.checkId(userid)*/) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "" + userid);
		List<User> list1 = userDAO.select(map1, null, null);
		if(list1 == null || list1.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		User user = list1.get(0);
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "" + id);
		List<Document> list2 = documentDAO.select(map2, null, null);
		if(list2 == null || list2.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0034);
			return false;
		}
		Document document = list2.get(0);
		
		//TODO download
		if(user.getId() != document.getUser_id() && user.getPoint() < document.getPrice()) {
			if(info != null) info.add(Strings.FAIL_0035);
			return false;
		}
		
		if(document.getTitle() != null && checkTitle(document.getTitle())) {
			list.add(document.getTitle() + document.getSuffix());
		} else {
			list.add(document.getPath() + document.getSuffix());
		}
		
		try {
			list.add(new FileInputStream(new File(FileUtil.getDocDirectory(this), document.getPath() + document.getSuffix())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if(info != null) info.add(Strings.FAIL_0036);
			return false;
		}
		
		if(document.getPrice() != 0 && user.getId() != document.getUser_id()) {
			HashMap<String, Long> addMap = new HashMap<String, Long>();
			addMap.put("point", (long)(-document.getPrice()));
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "" + user.getId());
			if(userDAO.update(null, addMap, map) != 1) {
				if(info != null) info.add(Strings.FAIL_0037);
				return false;
			}
		}
		
		if(info != null) info.add(Strings.SUCCESS_0011);
		return true;
	}
	
	public FileInputStream show(long userid, long id, List<String> info) {
		
		if(id <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return null;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		List<Document> list = documentDAO.select(map, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0034);
			return null;
		}
		
		Document document = list.get(0);
		if(document.getStatus().equals(Strings.STATUS_UNCHECK)) {
			if(info != null) info.add(Strings.FAIL_0034);
			return null;
		}
		
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(FileUtil.getSwfDirectory(this), document.getPath() + ".swf"));
		} catch (Exception e) {
			e.printStackTrace();
			if(info != null) info.add(Strings.FAIL_0036);
			return null;
		}
		
		if(info != null) info.add(Strings.SUCCESS_0011);
		return fileInputStream;
	}
	
	public boolean checkHash(File file, List<Object> result) {
		
		if(file == null) return false;
		
		String hash = StringUtil.string2Hex(SecurityUtil.hash(file));
		if(hash == null) return false;
		if(result != null) result.add(hash);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("hash", hash);
		List<Document> list = documentDAO.select(map, null, null);
		
		return list != null && list.size() == 0;
	}
	
	public boolean checkFileName(String fileName) {
		return true;
	}
	
	public boolean checkFileType(String fileType) {
		return true;
	}
	
	public boolean checkTitle(String title) {
		return true;
	}
	
	public boolean checkSummary(String summary) {
		return true;
	}
	
	public boolean checkPrice(int price) {
		return true;
	}
}
