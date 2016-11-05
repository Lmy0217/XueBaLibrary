package org.ncu.xuebalibrary.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			userDAO.addDocument(userid, 1L);
			categoryDAO.addDocument(categoryid, 1L);
			
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
			if(!userDAO.addPoint(userid, (long)(-document.getPrice()))) {
				if(info != null) info.add(Strings.FAIL_0037);
				return false;
			}			
			userDAO.addPoint(document.getUser_id(), document.getUser_id());
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
	
	//TODO test
	public boolean delete(long userid, long id, List<String> info) {
		
		if(userid <= 0 || id <= 0) {
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
		
		if(document.getUser_id() != userid && !user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		boolean flag = documentDAO.delete(map2) == 1;
		if(flag) {
			FileUtil.deleteDocFile(this, document.getPath() + document.getSuffix());
			FileUtil.deleteSwfFile(this, document.getPath() + ".swf");
			
			userDAO.addComment(document.getUser_id(), -1L);
			categoryDAO.addDocument(document.getCategory_id(), -1L);
			
			if(info != null) info.add(Strings.SUCCESS_0021);
		} else {
			if(info != null) info.add(Strings.FAIL_0051);
		}
		return flag;
	}
	
	//TODO test
	public boolean update(long id, long categoryid, int price, String status, long userid, List<String> info) {
		//TODO status
		if(id <= 0 || categoryid < 0 || price < 0 || status == null || userid <= 0 || !categoryDAO.checkId(categoryid) || !checkPrice(price)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "" + userid);
		List<User> list1 = userDAO.select(map1, null, null);
		if(list1 == null || list1.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		User user = list1.get(0);
		if(!user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("category_id", "" + categoryid);
		newMap.put("price", "" + price);
		newMap.put("status", status);
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + id);
		
		boolean flag = documentDAO.update(newMap, null, findMap) == 1;
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0026);
		} else {
			if(info != null) info.add(Strings.FAIL_0056);
		}
		return flag;
	}
	
	public List<Document> get(long id, String title, String summary, long categoryid, long page, List<String> info) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(id > 0) map.put("id", "" + id);
		if(categoryid >= 0) map.put("category_id", "" + categoryid);
		map.put("status", Strings.STATUS_NORMAL);
		HashMap<String, String> likeMap = new HashMap<String, String>();
		if(title != null && checkTitle(title)) likeMap.put("title", "%" + title + "%");
		if(summary != null && checkSummary(summary)) likeMap.put("text", "%" + summary + "%");
		
		String other = null;
		if(page > 0) other = "order by created desc limit " + ((page - 1) * Strings.PAGE_DOCUMENT) + "," + Strings.PAGE_DOCUMENT;
		
		List<Object> count = null;
		List<Document> list = null;
		if(map.size() != 0 || likeMap.size() != 0) {
			count = documentDAO.count(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, null);
			list = documentDAO.select(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, other != null ? other : null);
		}
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_DOCUMENT + 1));//Strings.SUCCESS_0011);
		} else {
			if(info != null) info.add(Strings.FAIL_0036);
		}
		return list;
	}
	
	public List<Document> select(long id, String title, String summary, long categoryid, long documentuserid, String status, long userid, long page, List<String> info) {
		
		if(userid <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return null;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "" + userid);
		List<User> list1 = userDAO.select(map1, null, null);
		if(list1 == null || list1.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0041);
			return null;
		}
		
		User user = list1.get(0);
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(id > 0) map.put("id", "" + id);
		if(categoryid >= 0) map.put("category_id", "" + categoryid);
		if(status != null) map.put("status", status);
		if(user.getRole().equals(Strings.ROLE_OPERATOR) || user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(documentuserid >= 0) map.put("user_id", "" + documentuserid);
		} else {
			map.put("user_id", "" + userid);
		}
		HashMap<String, String> likeMap = new HashMap<String, String>();
		if(title != null && checkTitle(title)) likeMap.put("title", "%" + title + "%");
		if(summary != null && checkSummary(summary)) likeMap.put("text", "%" + summary + "%");
		
		String other = null;
		if(page > 0) other = "order by created desc limit " + ((page - 1) * Strings.PAGE_DOCUMENT) + "," + Strings.PAGE_DOCUMENT;
		
		List<Object> count = documentDAO.count(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, null);
		List<Document> list = documentDAO.select(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, other != null ? other : null);
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_DOCUMENT + 1));//Strings.SUCCESS_0011);
		} else {
			if(info != null) info.add(Strings.FAIL_0036);
		}
		return list;
	}
	
	public List<Map<String, String>> allToMap(List<Document> documentList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(documentList == null || documentList.size() == 0) return list;
		for(Document document : documentList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + document.getId());
			map.put("title", document.getTitle());
			map.put("summary", document.getSummary());
			map.put("path", document.getPath());
			map.put("hash", document.getHash());
			map.put("suffix", document.getSuffix());
			map.put("category_id", "" + document.getCategory_id());
			map.put("user_id", "" + document.getUser_id());
			map.put("price", "" + document.getPrice());
			map.put("status", document.getStatus());
			map.put("order_number", "" + document.getOrder_number());
			map.put("vote_up", "" + document.getVote_up());
			map.put("vote_down", "" + document.getVote_down());
			map.put("view_count", "" + document.getView_count());
			map.put("rate", "" + document.getRate());
			map.put("rate_count", "" + document.getRate_count());
			map.put("mime_type", document.getMime_type());
			map.put("meta_keywords", document.getMeta_keywords());
			map.put("meta_description", document.getMeta_description());
			map.put("created", document.getCreated().toString());
			map.put("modified", document.getModified().toString());
			map.put("remarks", document.getRemarks());
			list.add(map);
		}
		return list;
	}
	
	public List<Map<String, String>> someToMap(List<Document> documentList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(documentList == null || documentList.size() == 0) return list;
		for(Document document : documentList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + document.getId());
			map.put("title", document.getTitle());
			map.put("summary", document.getSummary());
			map.put("category_id", "" + document.getCategory_id());
			map.put("user_id", "" + document.getUser_id());
			map.put("price", "" + document.getPrice());
			map.put("vote_up", "" + document.getVote_up());
			map.put("vote_down", "" + document.getVote_down());
			map.put("view_count", "" + document.getView_count());
			map.put("rate", "" + document.getRate());
			map.put("rate_count", "" + document.getRate_count());
			map.put("modified", document.getModified().toString());
			list.add(map);
		}
		return list;
	}
	
	public boolean voteUp(long id, List<String> info) {
		
		if(id <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		boolean flag = documentDAO.addVoteUp(id, 1L);
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0028);
		} else {
			if(info != null) info.add(Strings.FAIL_0059);
		}
		return flag;
	}
	
	public boolean voteDown(long id, List<String> info) {
		
		if(id <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		boolean flag = documentDAO.addVoteDown(id, 1L);
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0029);
		} else {
			if(info != null) info.add(Strings.FAIL_0060);
		}
		return flag;
	}
	
	public boolean addView(long id, List<String> info) {
		
		if(id <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		boolean flag = documentDAO.addView(id, 1L);
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0030);
		} else {
			if(info != null) info.add(Strings.FAIL_0061);
		}
		return flag;
	}
	
	public boolean rate(long id, int rate, List<String> info) {
		
		if(id <= 0 || rate < 0 || !checkRate(rate)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		boolean flag = documentDAO.addRate(id, rate);
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0031);
		} else {
			if(info != null) info.add(Strings.FAIL_0062);
		}
		return flag;
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
	
	public boolean checkRate(int rate) {
		return true;
	}
}
