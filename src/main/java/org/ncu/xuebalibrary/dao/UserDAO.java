package org.ncu.xuebalibrary.dao;

import java.util.List;

import org.ncu.xuebalibrary.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends BaseDAO<User, Long> {

	public User loginSelect(String username) {
		
		if(username == null || username.length() == 0) return null;
		
		String sql = "select * from user where username = '" + username + "' or email = '" + username + "' or mobile = '" + username + "'";
		List<User> list = queryBySQL(sql);
		
		return (list != null && list.size() > 0) ? list.get(0) : null;
	}
	
	public boolean emailCheck(String email) {
		
		if(email == null || email.length() == 0) return false;
		//TODO count(*)
		String sql = "select * from user where email = '" + email + "'";
		List<User> list = queryBySQL(sql);
		
		return list != null && list.size() == 0;
	}
	
	public boolean checkId(long id) {
		
		if(id <= 0) return false;
		
		String sql = "select * from user where id = '" + id + "'";
		List<User> list = queryBySQL(sql);
		
		return list != null && list.size() == 1;
	}
	
	public boolean addDocument(long number) {
		
		if(number <= 0) return false;
		
		String sql = "update user set document_count = document_count + '" + number + "'";
		return excuteBySQL(sql) == 1;
	}
}
