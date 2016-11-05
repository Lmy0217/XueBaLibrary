package org.ncu.xuebalibrary.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.dao.CategoryDAO;
import org.ncu.xuebalibrary.dao.CommentDAO;
import org.ncu.xuebalibrary.dao.ContentDAO;
import org.ncu.xuebalibrary.dao.UserDAO;
import org.ncu.xuebalibrary.entity.Content;
import org.ncu.xuebalibrary.entity.User;
import org.ncu.xuebalibrary.util.GenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

	@Autowired
	private ContentDAO contentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private CommentDAO commentDAO;
	
	public boolean create(String title, String text, long userid, long categoryid, List<String> info) {
		
		if(title == null || text == null || userid <= 0 || categoryid < 0 || !checkTitle(title) || !checkText(text) || !userDAO.checkId(userid) || (categoryid != 0 && !categoryDAO.checkId(categoryid))) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + GenerationUtil.getContentId());
		map.put("title", title);
		map.put("text", text);
		map.put("user_id", "" + userid);
		if(categoryid != 0) map.put("category_id", "" + categoryid);
		map.put("created", time);
		map.put("modified", time);
		
		boolean flag = contentDAO.insert(map) == 1;
		
		if(flag) {
			userDAO.addContent(userid, 1);
			if(info != null) info.add(Strings.SUCCESS_0012);
		} else {
			if(info != null) info.add(Strings.FAIL_0039);
		}
		
		return flag;
	}
	
	public boolean update(long id, String title, String text, long categoryid, long userid, List<String> info) {
		
		if(id <= 0 || title == null || text == null || categoryid < 0 || userid <= 0 || !checkTitle(title) || !checkText(text) || (categoryid != 0 && !categoryDAO.checkId(categoryid)) || !userDAO.checkId(userid)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		List<Content> list = contentDAO.select(map, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0040);
			return false;
		}
		
		Content content = list.get(0);
		if(content.getUser_id() != userid) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("title", title);
		newMap.put("text", text);
		newMap.put("category_id", "" + categoryid);
		newMap.put("modified", time);
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + id);
		
		boolean result = contentDAO.update(newMap, null, findMap) == 1;
		if(result) {
			if(info != null) info.add(Strings.SUCCESS_0013);
		} else {
			if(info != null) info.add(Strings.FAIL_0042);
		}
		return result;
	}
	
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
		List<Content> list2 = contentDAO.select(map2, null, null);
		if(list2 == null || list2.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0040);
			return false;
		}
		Content content = list2.get(0);
		
		if(content.getUser_id() != userid && !user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		boolean flag = contentDAO.delete(map2) == 1;
		if(flag) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("grandparent_id", "" + content.getId());
			commentDAO.delete(map);
			
			userDAO.addContent(userid, -1);
			
			if(info != null) info.add(Strings.SUCCESS_0016);
		} else {
			if(info != null) info.add(Strings.FAIL_0046);
		}
		return flag;
	}
	
	public List<Content> get(long id, String title, String text, long categoryid, long page, List<String> info) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(id > 0) map.put("id", "" + id);
		if(categoryid >= 0) map.put("category_id", "" + categoryid);
		HashMap<String, String> likeMap = new HashMap<String, String>();
		if(title != null && checkTitle(title)) likeMap.put("title", "%" + title + "%");
		if(text != null && checkText(text)) likeMap.put("text", "%" + text + "%");
		
		String other = null;
		if(page > 0) other = "order by created desc limit " + ((page - 1) * Strings.PAGE_CONTENT) + "," + Strings.PAGE_CONTENT;
		
		List<Object> count = null;
		List<Content> list = null;
		if(map.size() != 0 || likeMap.size() != 0) {
			count = contentDAO.count(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, null);
			list = contentDAO.select(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, other != null ? other : null);
		}
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_CONTENT + 1));//Strings.SUCCESS_0019);
		} else {
			if(info != null) info.add(Strings.FAIL_0054);
		}
		return list;
	}
	
	public List<Content> select(long id, String title, String text, long contentuserid, long categoryid, String status, long userid, long page, List<String> info) {
		
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
			if(contentuserid >= 0) map.put("user_id", "" + contentuserid);
		} else {
			map.put("user_id", "" + userid);
		}
		HashMap<String, String> likeMap = new HashMap<String, String>();
		if(title != null && checkTitle(title)) likeMap.put("title", "%" + title + "%");
		if(text != null && checkText(text)) likeMap.put("text", "%" + text + "%");
		
		String other = null;
		if(page > 0) other = "order by created desc limit " + ((page - 1) * Strings.PAGE_CONTENT) + "," + Strings.PAGE_CONTENT;
		
		List<Object> count = contentDAO.count(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, null);
		List<Content> list = contentDAO.select(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, other != null ? other : null);
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_CONTENT + 1));//Strings.SUCCESS_0019);
		} else {
			if(info != null) info.add(Strings.FAIL_0054);
		}
		return list;
	}
	
	public List<Map<String, String>> allToMap(List<Content> contentList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(contentList == null || contentList.size() == 0) return list;
		for(Content content : contentList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + content.getId());
			map.put("title", content.getTitle());
			map.put("text", content.getText());
			map.put("user_id", "" + content.getUser_id());
			map.put("category_id", "" + content.getCategory_id());
			map.put("status", content.getStatus());
			map.put("order_number", "" + content.getOrder_number());
			map.put("view_count", "" + content.getView_count());
			map.put("comment_count", "" + content.getComment_count());
			map.put("comment_time", content.getComment_time() != null ? content.getComment_time().toString() : "");
			map.put("comment_user_id", "" + content.getComment_user_id());
			map.put("created", content.getCreated().toString());
			map.put("modified", content.getModified().toString());
			list.add(map);
		}
		return list;
	}
	
	public List<Map<String, String>> someToMap(List<Content> contentList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(contentList == null || contentList.size() == 0) return list;
		for(Content content : contentList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + content.getId());
			map.put("title", content.getTitle());
			map.put("text", content.getText());
			map.put("user_id", "" + content.getUser_id());
			map.put("category_id", "" + content.getCategory_id());
			map.put("view_count", "" + content.getView_count());
			map.put("comment_count", "" + content.getComment_count());
			map.put("comment_time", content.getComment_time() != null ? content.getComment_time().toString() : "");
			map.put("comment_user_id", "" + content.getComment_user_id());
			map.put("modified", content.getModified().toString());
			list.add(map);
		}
		return list;
	}
	
	public boolean addView(long id, List<String> info) {
		
		if(id <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		boolean flag = contentDAO.addView(id, 1L);
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0030);
		} else {
			if(info != null) info.add(Strings.FAIL_0061);
		}
		return flag;
	}
	
	public boolean checkTitle(String title) {
		return true;
	}
	
	public boolean checkText(String text) {
		return true;
	}
}
