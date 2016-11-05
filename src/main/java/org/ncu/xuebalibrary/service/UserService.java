package org.ncu.xuebalibrary.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.dao.CommentDAO;
import org.ncu.xuebalibrary.dao.ContentDAO;
import org.ncu.xuebalibrary.dao.DocumentDAO;
import org.ncu.xuebalibrary.dao.UserDAO;
import org.ncu.xuebalibrary.entity.User;
import org.ncu.xuebalibrary.util.EmailUtil;
import org.ncu.xuebalibrary.util.SecurityUtil;
import org.ncu.xuebalibrary.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private DocumentDAO documentDAO;
	@Autowired
	private ContentDAO contentDAO;
	@Autowired
	private CommentDAO commentDAO;
	
	public boolean registerByEmail(String username, String password, String email, List<String> info) {
		
		if(username == null || password == null || !checkUsername(username) || !checkPassword(password)) {
			if(info != null) info.add(Strings.FAIL_0001);
			return false;
		}
		
		if(email == null || !checkEmail(email)) {
			if(info != null) info.add(Strings.FAIL_0002);
			return false;
		}
		
		if(!userDAO.emailCheck(email)) {
			if(info != null) info.add(Strings.FAIL_0003);
			return false;
		}
		
		String salt = SecurityUtil.saltGenerate();
		password = SecurityUtil.encrypt(password, salt);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", StringUtil.string2Hex(password));
		map.put("salt", StringUtil.string2Hex(salt));
		map.put("email_status", email);
		map.put("point", "" + Strings.REGISTER_POINT);
		map.put("created", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
		
		boolean flag = userDAO.insert(map) == 1;
		
		if(flag) {
			//TODO
			HashMap<String, String> findmap = new HashMap<String, String>();
			findmap.put("username", username);
			sendActivateEmail(findmap, null);
			if(info != null) info.add(Strings.SUCCESS_0001);
		} else {
			if(info != null) info.add(Strings.FAIL_0004);
		}
		
		return flag;
	}
	
	public User login(String username, String password, List<String> info) {
		
		if(username == null || password == null || (!checkUsername(username) && !checkEmail(username) && !checkMobile(username)) || !checkPassword(password)) {
			if(info != null) info.add(Strings.FAIL_0001);
			return null;
		}
		
		User user = userDAO.loginSelect(username);
		if(user == null) {
			if(info != null) info.add(Strings.FAIL_0008);
			return null;
		}
		
		if(!SecurityUtil.slowEquals(StringUtil.string2Byte(SecurityUtil.encrypt(password, StringUtil.hex2String(user.getSalt()))), StringUtil.hex2Byte(user.getPassword()))) {
			if(info != null) info.add(Strings.FAIL_0006);
			return null;
		}
		
		String salt = SecurityUtil.saltGenerate();
		password = SecurityUtil.encrypt(password, salt);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + user.getId());
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("password", StringUtil.string2Hex(password));
		newMap.put("salt", StringUtil.string2Hex(salt));
		newMap.put("logged", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
		
		userDAO.update(newMap, null, map);
		
		if(info != null) info.add(Strings.SUCCESS_0002);
		return user;
	}
	
	public boolean updatePassword(long id, String password, String newPassword, List<String> info) {
		
		if(id <= 0 || password == null || newPassword == null || !checkPassword(password) || !checkPassword(newPassword)) {
			if(info != null) info.add(Strings.FAIL_0007);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		List<User> list = userDAO.select(map, null, null);
		
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		if(!SecurityUtil.slowEquals(StringUtil.string2Byte(SecurityUtil.encrypt(password, StringUtil.hex2String(user.getSalt()))), StringUtil.hex2Byte(user.getPassword()))) {
			if(info != null) info.add(Strings.FAIL_0006);
			return false;
		}
		
		String salt = SecurityUtil.saltGenerate();
		newPassword = SecurityUtil.encrypt(newPassword, salt);
		
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + id);
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("password", StringUtil.string2Hex(newPassword));
		newMap.put("salt", StringUtil.string2Hex(salt));
		
		boolean result = userDAO.update(newMap, null, map) == 1;
		if(result) {
			if(info != null) info.add(Strings.SUCCESS_0003);
		} else {
			if(info != null) info.add(Strings.FAIL_0009);
		}
		return result;
	}
	
	public boolean sendResetPasswordEmail(String username, List<String> info) {
		
		if(username == null || (!checkUsername(username) && !checkEmail(username) && !checkMobile(username))) {
			if(info != null) info.add(Strings.FAIL_0005);
			return false;
		}
		
		User user = userDAO.loginSelect(username);
		if(user == null) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		if(!user.getEmail_status().equals(Strings.STATUS_ACTIVITED)) {
			if(info != null) info.add(Strings.FAIL_0020);
			return false;
		}
		
		String email = user.getEmail();
		if(email == null || !checkEmail(email)) {
			if(info != null) info.add(Strings.FAIL_0002);
			return false;
		}
		
		String random = StringUtil.string2Hex(SecurityUtil.saltGenerate());
		String time = "" + (System.currentTimeMillis() + Strings.OVERTIME_PASSWORD);
		String key = random + "," + time;
		String param = StringUtil.string2Hex(SecurityUtil.sha(key));
		if(random == null || param == null) {
			if(info != null) info.add(Strings.FAIL_0011);
			return false;
		}
		
		param = "password.action?type=" + Strings.TYPE_FORGET + "&id=" + user.getId() + "&key=" + param;
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("password_status", key);
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + user.getId());
		if(userDAO.update(newMap, null, findMap) != 1) {
			if(info != null) info.add(Strings.FAIL_0012);
			return false;
		}
		
		if(!EmailUtil.send(email, Strings.EMAIL_SUBJECT_PASSWORD, param)) {
			if(info != null) info.add(Strings.FAIL_0013);
			return false;
		}
		
		if(info != null) info.add(Strings.SUCCESS_0006);
		return true;
	}
	
	public boolean resetPasswordByEmail(long id, String key, List<String> info) {
		
		if(id <= 0 || key == null || key.length() == 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		List<User> list = userDAO.select(map, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		if(user.getPassword_status().equals(Strings.STATUS_NORMAL) || user.getPassword_status().equals(Strings.STATUS_RESET)) {
			if(info != null) info.add(Strings.FAIL_0021);
			return false;
		}
		
		if(Long.parseLong(user.getPassword_status().split(",")[1]) < System.currentTimeMillis()) {
			if(info != null) info.add(Strings.FAIL_0022);
			return false;
		}
		
		if(!SecurityUtil.slowEquals(StringUtil.hex2Byte(key), StringUtil.string2Byte(SecurityUtil.sha(user.getPassword_status())))) {
			if(info != null) info.add(Strings.FAIL_0016);
			return false;
		}
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		String time = "" + (System.currentTimeMillis() + Strings.OVERTIME_RESET);
		newMap.put("password_status", Strings.STATUS_RESET + "," + time);
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + id);
		
		boolean result = userDAO.update(newMap, null, findMap) == 1;
		if(result) {
			if(info != null) info.add(Strings.SUCCESS_0007);
		} else {
			if(info != null) info.add(Strings.FAIL_0023);
		}
		return result;
	}
	
	public boolean resetPassword(long id, String newPassword, List<String> info) {
		
		if(id <= 0 || newPassword == null || !checkPassword(newPassword)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		List<User> list = userDAO.select(map, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		String password_status = user.getPassword_status();
		if(!password_status.split(",")[0].equals(Strings.STATUS_RESET)) {
			if(info != null) info.add(Strings.FAIL_0025);
			return false;
		}
		
		//TODO password_status
		if(Long.parseLong(password_status.split(",")[1]) < System.currentTimeMillis()) {
			if(info != null) info.add(Strings.FAIL_0022);
			return false;
		}
		
		String salt = SecurityUtil.saltGenerate();
		newPassword = SecurityUtil.encrypt(newPassword, salt);
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("password", StringUtil.string2Hex(newPassword));
		newMap.put("salt", StringUtil.string2Hex(salt));
		newMap.put("password_status", Strings.STATUS_NORMAL);
		
		boolean result = userDAO.update(newMap, null, map) == 1;
		if(result) {
			if(info != null) info.add(Strings.SUCCESS_0008);
		} else {
			if(info != null) info.add(Strings.FAIL_0026);
		}
		return result;
	}
	
	public boolean sendActivateEmail(HashMap<String, String> map, List<String> info) {
		
		if(map == null || map.size() == 0) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		List<User> list = userDAO.select(map, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		if(!user.getStatus().equals(Strings.STATUS_UNCHECK)) {
			if(info != null) info.add(Strings.FAIL_0010);
			return false;
		}
		
		String email = user.getEmail_status().split(",")[0];
		if(!checkEmail(email)) {
			if(info != null) info.add(Strings.FAIL_0002);
			return false;
		}
		
		String random = StringUtil.string2Hex(SecurityUtil.saltGenerate());
		String time = "" + (System.currentTimeMillis() + Strings.EMAIL_OVERTIME);
		String key = email + "," + random + "," + time;
		String param = StringUtil.string2Hex(SecurityUtil.sha(key));
		if(random == null || param == null) {
			if(info != null) info.add(Strings.FAIL_0011);
			return false;
		}
		
		param = "email.action?type=" + Strings.TYPE_ACTIVITE + "&id=" + user.getId() + "&key=" + param;
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("email_status", key);
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + user.getId());
		if(userDAO.update(newMap, null, findMap) != 1) {
			if(info != null) info.add(Strings.FAIL_0012);
			return false;
		}
		
		if(!EmailUtil.send(email, Strings.EMAIL_SUBJECT_ACTIVITE, param)) {
			if(info != null) info.add(Strings.FAIL_0013);
			return false;
		}
		
		if(info != null) info.add(Strings.SUCCESS_0004);
		return true;
	}
	
	public boolean activateEmail(long id, String key, List<String> info) {
		
		if(id <= 0 || key == null || key.length() == 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		List<User> list = userDAO.select(map, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		if(!user.getStatus().equals(Strings.STATUS_UNCHECK)) {
			if(info != null) info.add(Strings.FAIL_0010);
			return false;
		}
		
		if(Long.parseLong(user.getEmail_status().split(",")[2]) < System.currentTimeMillis()) {
			if(info != null) info.add(Strings.FAIL_0015);
			return false;
		}
		
		if(!SecurityUtil.slowEquals(StringUtil.hex2Byte(key), StringUtil.string2Byte(SecurityUtil.sha(user.getEmail_status())))) {
			if(info != null) info.add(Strings.FAIL_0016);
			return false;
		}
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("email", user.getEmail_status().split(",")[0]);
		newMap.put("email_status", Strings.STATUS_ACTIVITED);
		newMap.put("status", Strings.STATUS_NORMAL);
		newMap.put("activated", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
		HashMap<String, String> findMap = new HashMap<String, String>();
		findMap.put("id", "" + id);
		
		boolean result = userDAO.update(newMap, null, findMap) == 1;
		if(result) {
			if(info != null) info.add(Strings.SUCCESS_0005);
		} else {
			if(info != null) info.add(Strings.FAIL_0017);
		}
		return result;
	}
	
	//TODO test
	public boolean delete(long userid, long id, List<String> info) {
		
		if(id <= 0 || userid <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "" + id);
		List<User> list1 = userDAO.select(map1, null, null);
		if(list1 == null || list1.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		User user1 = list1.get(0);
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "" + userid);
		List<User> list2 = userDAO.select(map2, null, null);
		if(list2 == null || list2.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		User user2 = list2.get(0);
		
		if(!(user1.getRole().equals(Strings.ROLE_OPERATOR) && user2.getRole().equals(Strings.ROLE_ADMINISTRATOR)) 
				&& !(user1.getRole().equals(Strings.ROLE_VISITOR) && user2.getRole().equals(Strings.ROLE_OPERATOR))
				&& !(user1.getRole().equals(Strings.ROLE_VISITOR) && user2.getRole().equals(Strings.ROLE_ADMINISTRATOR))) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		boolean flag = userDAO.delete(map1) == 1;
		if(flag) {
			HashMap<String, String> newMap1 = new HashMap<String, String>();
			newMap1.put("user_id", "0");
			HashMap<String, String> findMap1 = new HashMap<String, String>();
			findMap1.put("user_id", "" + id);
			documentDAO.update(newMap1, null, findMap1);
			contentDAO.update(newMap1, null, findMap1);
			commentDAO.update(newMap1, null, findMap1);
			
			HashMap<String, String> newMap2 = new HashMap<String, String>();
			newMap2.put("comment_user_id", "0");
			HashMap<String, String> findMap2 = new HashMap<String, String>();
			findMap2.put("comment_user_id", "" + id);
			contentDAO.update(newMap2, null, findMap2);
			
			HashMap<String, String> newMap3 = new HashMap<String, String>();
			newMap3.put("parent_user_id", "0");
			HashMap<String, String> findMap3 = new HashMap<String, String>();
			findMap3.put("parent_user_id", "" + id);
			commentDAO.update(newMap3, null, findMap3);
			
			if(info != null) info.add(Strings.SUCCESS_0022);
		} else {
			if(info != null) info.add(Strings.FAIL_0052);
		}
		return flag;
	}
	
	public boolean update(long id, long point, String role, long userid, List<String> info) {
		//TODO role
		if(id <= 0 || point < 0 || role == null || userid <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "" + id);
		List<User> list1 = userDAO.select(map1, null, null);
		if(list1 == null || list1.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		User user1 = list1.get(0);
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "" + userid);
		List<User> list2 = userDAO.select(map2, null, null);
		if(list2 == null || list2.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		User user2 = list2.get(0);
		
		if(!(user1.getRole().equals(Strings.ROLE_OPERATOR) && user2.getRole().equals(Strings.ROLE_ADMINISTRATOR) && (role.equals(Strings.ROLE_VISITOR) || role.equals(Strings.ROLE_OPERATOR) || role.equals(Strings.ROLE_ADMINISTRATOR))) 
				&& !(user1.getRole().equals(Strings.ROLE_VISITOR) && user2.getRole().equals(Strings.ROLE_OPERATOR) && (role.equals(Strings.ROLE_VISITOR) || role.equals(Strings.ROLE_OPERATOR)))
				&& !(user1.getRole().equals(Strings.ROLE_VISITOR) && user2.getRole().equals(Strings.ROLE_ADMINISTRATOR) && (role.equals(Strings.ROLE_VISITOR) || role.equals(Strings.ROLE_OPERATOR) || role.equals(Strings.ROLE_ADMINISTRATOR)))) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("point", "" + point);
		newMap.put("role", role);
		
		boolean flag = userDAO.update(newMap, null, map1) == 1;
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0027);
		} else {
			if(info != null) info.add(Strings.FAIL_0058);
		}
		return flag;
	}
	
	public List<User> get(long id, long page, List<String> info) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(id > 0) map.put("id", "" + id);
		map.put("status", Strings.STATUS_NORMAL);
		
		String other = null;
		if(page > 0) other = "limit " + ((page - 1) * Strings.PAGE_USER) + "," + Strings.PAGE_USER;
		
		List<Object> count = null;
		List<User> list = null;
		if(map.size() != 0) {
			count = userDAO.count(map, null, null);
			list = userDAO.select(map, null, other != null ? other : null);
		}
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_USER + 1));//Strings.SUCCESS_0024);
		} else {
			if(info != null) info.add(Strings.FAIL_0057);
		}
		return list;
	}
	
	public List<User> select(long id, String username, String email, String emailstatus, String role, String status, long userid, long page, List<String> info) {
		
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
		if(user.getRole().equals(Strings.ROLE_OPERATOR) || user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(id > 0) map.put("id", "" + id);
		} else {
			map.put("id", "" + userid);
		}
		if(email != null && checkEmail(email)) map.put("email", email);
		if(emailstatus != null) map.put("email_status", emailstatus);
		if(role != null) map.put("role", role);
		if(status != null) map.put("status", status);
		HashMap<String, String> likeMap = new HashMap<String, String>();
		if(username != null && checkUsername(username)) likeMap.put("username", "%" + username + "%");
		
		String other = null;
		if(page > 0) other = "limit " + ((page - 1) * Strings.PAGE_USER) + "," + Strings.PAGE_USER;
		
		List<Object> count = userDAO.count(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, null);
		List<User> list = userDAO.select(map.size() != 0 ? map : null, likeMap.size() != 0 ? likeMap : null, other != null ? other : null);
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_USER + 1));//Strings.SUCCESS_0024);
		} else {
			if(info != null) info.add(Strings.FAIL_0057);
		}
		return list;
	}
	
	public List<Map<String, String>> allToMap(List<User> userList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(userList == null || userList.size() == 0) return list;
		for(User user : userList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + user.getId());
			map.put("username", user.getUsername());
			map.put("email", user.getEmail());
			map.put("mobile", user.getMobile());
			map.put("point", "" + user.getPoint());
			map.put("role", user.getRole());
			map.put("avatar", user.getAvatar());
			map.put("document_count", "" + user.getDocument_count());
			map.put("content_count", "" + user.getContent_count());
			map.put("comment_count", "" + user.getComment_count());
			map.put("status", user.getStatus());
			map.put("signed", user.getSigned() != null ? user.getSigned().toString() : "");
			map.put("created", user.getCreated().toString());
			map.put("logged", user.getLogged() != null ? user.getLogged().toString() : "");
			map.put("activated", user.getActivated() != null ? user.getActivated().toString() : "");
			list.add(map);
		}
		return list;
	}
	
	public List<Map<String, String>> someToMap(List<User> userList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(userList == null || userList.size() == 0) return list;
		for(User user : userList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + user.getId());
			map.put("username", user.getUsername());
			map.put("document_count", "" + user.getDocument_count());
			map.put("content_count", "" + user.getContent_count());
			map.put("comment_count", "" + user.getComment_count());
			list.add(map);
		}
		return list;
	}
	
	public boolean sign(long id, List<String> info) {
		
		if(id <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "" + id);
		List<User> list = userDAO.select(map, null, null);
		if(list == null || list.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0008);
			return false;
		}
		
		User user = list.get(0);
		
		String time = (new SimpleDateFormat("yyyy-MM-dd 00:00:00.0")).format(new Date());
		long random = SecurityUtil.random();
		if(user.getSigned() != null && user.getSigned().toString().equals(time)) {
			if(info != null) info.add(Strings.FAIL_0063);
			return false;
		}
		
		HashMap<String, String> newMap = new HashMap<String, String>();
		newMap.put("signed", time);
		HashMap<String, Long> addMap = new HashMap<String, Long>();
		addMap.put("point", random);
		boolean flag = userDAO.update(newMap, addMap, map) == 1;
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0032 + "Get " + random + " point!");
		} else {
			if(info != null) info.add(Strings.FAIL_0063);
		}
		return flag;
	}
	
	public boolean checkUsername(String username) {
		return true;
	}
	
	public boolean checkPassword(String password) {
		return true;
	}
	
	public boolean checkEmail(String email) {
		return true;
	}
	
	public boolean checkMobile(String mobile) {
		return true;
	}
}
