package org.ncu.xuebalibrary.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ncu.xuebalibrary.config.Strings;
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
		
		if(username == null || password == null || !checkUsername(username) || !checkPassword(password)) {
			if(info != null) info.add(Strings.FAIL_0001);
			return null;
		}
		
		User user = userDAO.loginSelect(username);
		if(user == null) {
			if(info != null) info.add(Strings.FAIL_0005);
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
	
	public boolean forgetPassword(String username) {
		
		return false;
	}
	
	public boolean updateForgetPassword() {
		
		return false;
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
		
		if(!EmailUtil.send(email, param)) {
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
