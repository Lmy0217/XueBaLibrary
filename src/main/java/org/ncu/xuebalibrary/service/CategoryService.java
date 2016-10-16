package org.ncu.xuebalibrary.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.dao.CategoryDAO;
import org.ncu.xuebalibrary.dao.ContentDAO;
import org.ncu.xuebalibrary.dao.DocumentDAO;
import org.ncu.xuebalibrary.dao.UserDAO;
import org.ncu.xuebalibrary.entity.Category;
import org.ncu.xuebalibrary.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ContentDAO contentDAO;
	@Autowired
	private DocumentDAO documentDAO;
	
	public boolean create(String name, String text, long parentid, long userid, List<String> info) {
		
		if(name == null || parentid < 0 || userid <= 0 || !checkName(name) || (text != null && !checkText(text)) || (parentid != 0 && !categoryDAO.checkId(parentid)) || !userDAO.checkId(userid)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "" + userid);
		List<User> list = userDAO.select(map1, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		if(!user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		if(text != null) map.put("text", text);
		if(parentid != 0) map.put("parent_id", "" + parentid);
		map.put("created", time);
		
		boolean flag = categoryDAO.insert(map) == 1;
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0017);
		} else {
			if(info != null) info.add(Strings.FAIL_0047);
		}
		return flag;
	}
	
	public boolean update(long id, String name, String text, long parentid, long userid, List<String> info) {
		
		if(id <= 0 || name == null || parentid < 0 || userid <= 0 || !checkName(name) || (text != null && !checkText(text)) || (parentid != 0 && !categoryDAO.checkId(parentid)) || !userDAO.checkId(userid)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "" + userid);
		List<User> list = userDAO.select(map1, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		if(!user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("name", name);
		newMap.put("text", text);
		newMap.put("parent_id", "" + parentid);
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + id);
		
		boolean flag = categoryDAO.update(newMap, null, findMap) == 1;
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0018);
		} else {
			if(info != null) info.add(Strings.FAIL_0048);
		}
		return flag;
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
		if(!user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		if(!categoryDAO.checkId(id)) {
			if(info != null) info.add(Strings.FAIL_0049);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		boolean flag = categoryDAO.delete(map) == 1;
		if(flag) {
			HashMap<String, String> newMap1 = new HashMap<String, String>();
			newMap1.put("parent_id", "0");
			HashMap<String, String> findMap1 = new HashMap<String, String>();
			findMap1.put("parent_id", "" + id);
			categoryDAO.update(newMap1, null, findMap1);
			
			HashMap<String, String> newMap2 = new HashMap<String, String>();
			newMap2.put("category_id", "0");
			HashMap<String, String> findMap2 = new HashMap<String, String>();
			findMap2.put("category_id", "" + id);
			contentDAO.update(newMap2, null, findMap2);
			documentDAO.update(newMap2, null, findMap2);
			
			if(info != null) info.add(Strings.SUCCESS_0020);
		} else {
			if(info != null) info.add(Strings.FAIL_0050);
		}
		return flag;
	}
	
	public List<Category> get(long id, long parentid, List<String> info) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(id > 0) map.put("id", "" + id);
		if(parentid >= 0) map.put("parent_id", "" + parentid);
		
		String other = "order by order_number desc";
		
		List<Category> list = null;
		if(map.size() != 0) list = categoryDAO.select(map, null, other);
		
		if(list != null) {
			if(info != null) info.add(Strings.SUCCESS_0023);
		} else {
			if(info != null) info.add(Strings.FAIL_0053);
		}
		return list;
	}
	
	public List<Category> select(long id, String name, String text, long parentid, String status, long userid, List<String> info) {
		
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
		if(!user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return null;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(id > 0) map.put("id", "" + id);
		if(parentid >= 0) map.put("parent_id", "" + parentid);
		if(status != null) map.put("status", status);
		HashMap<String, String> likeMap = new HashMap<String, String>();
		if(name != null && checkName(name)) likeMap.put("name", "%" + name + "%");
		if(text != null && checkText(text)) likeMap.put("text", "%" + text + "%");
		
		String other = "order by order_number desc";
		
		List<Category> list = categoryDAO.select(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, other);
		
		if(list != null) {
			if(info != null) info.add(Strings.SUCCESS_0023);
		} else {
			if(info != null) info.add(Strings.FAIL_0053);
		}
		return list;
	}
	
	public boolean checkName(String name) {
		return true;
	}
	
	public boolean checkText(String text) {
		return true;
	}
}
