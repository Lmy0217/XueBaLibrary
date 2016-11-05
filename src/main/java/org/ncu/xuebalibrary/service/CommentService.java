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
import org.ncu.xuebalibrary.dao.UserDAO;
import org.ncu.xuebalibrary.entity.Comment;
import org.ncu.xuebalibrary.entity.Content;
import org.ncu.xuebalibrary.entity.User;
import org.ncu.xuebalibrary.util.GenerationUtil;
import org.ncu.xuebalibrary.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

	@Autowired
	private CommentDAO commentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ContentDAO contentDAO;
	
	public boolean create(String text, long userid, long parentid, List<String> info) {
		
		if(text == null || userid <= 0 || parentid <= 0 || !checkText(text) || !userDAO.checkId(userid)) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		boolean flag = false;
		if(isContentId(parentid)) {
			
			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("id", "" + parentid);
			List<Content> list1 = contentDAO.select(map1, null, null);
			if(list1 == null || list1.size() != 1) {
				if(info != null) info.add(Strings.FAIL_0040);
				return false;
			}
			Content content = list1.get(0);
			
			String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "" + GenerationUtil.getCommentId());
			map.put("text", text);
			map.put("user_id", "" + userid);
			map.put("parent_id", "" + parentid);
			map.put("grandparent_id", "" + parentid);
			map.put("order_number", StringUtil.long2FormatString(content.getComment_count() + 1));
			map.put("created", time);
			
			flag = commentDAO.insert(map) == 1;
			if(flag) {
				userDAO.addComment(userid, 1L);
				
				HashMap<String, String> newMap = new HashMap<String, String>();
				newMap.put("comment_time", time);
				newMap.put("comment_user_id", "" + userid);
				HashMap<String, Long> addMap = new HashMap<String, Long>();
				addMap.put("comment_count", 1L);
				contentDAO.update(newMap, addMap, map1);
				
				if(info != null) info.add(Strings.SUCCESS_0014);
			} else {
				if(info != null) info.add(Strings.FAIL_0044);
			}
			
		} else if(isCommentId(parentid)) {
			
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("id", "" + parentid);
			List<Comment> list2 = commentDAO.select(map2, null, null);
			if(list2 == null || list2.size() != 1) {
				if(info != null) info.add(Strings.FAIL_0043);
				return false;
			}
			Comment comment = list2.get(0);
			
			String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "" + GenerationUtil.getCommentId());
			map.put("text", text);
			map.put("user_id", "" + userid);
			map.put("parent_user_id", "" + comment.getUser_id());
			map.put("created", time);
			
			if(isContentId(comment.getParent_id())) {
				map.put("parent_id", "" + comment.getId());
				map.put("grandparent_id", "" + comment.getParent_id());
				map.put("order_number", "" + StringUtil.long2FormatString(comment.getComment_count() + 1));
				
			} else if(isCommentId(comment.getParent_id())) {
				
				map.put("parent_id", "" + comment.getParent_id());
				map.put("grandparent_id", "" + comment.getGrandparent_id());
				map.put("order_number", comment.getOrder_number() + "," + StringUtil.long2FormatString(comment.getComment_count() + 1));
			}
			
			flag = commentDAO.insert(map) == 1;
			if(flag) {
				userDAO.addComment(userid, 1L);
				
				if(isContentId(comment.getParent_id())) {
					commentDAO.addComment(parentid, 1L);
				} else if(isCommentId(comment.getParent_id())) {
					commentDAO.addComment(comment.getParent_id(), 1L);
				}
				
				if(info != null) info.add(Strings.SUCCESS_0014);
			} else {
				if(info != null) info.add(Strings.FAIL_0044);
			}
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
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "" + id);
		List<Comment> list2 = commentDAO.select(map2, null, null);
		if(list2 == null || list2.size() != 1) {
			if(info != null) info.add(Strings.FAIL_0043);
			return false;
		}
		Comment comment = list2.get(0);
		
		if(comment.getUser_id() != userid && !user.getRole().equals(Strings.ROLE_OPERATOR) && !user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(info != null) info.add(Strings.FAIL_0041);
			return false;
		}
		
		boolean flag = commentDAO.delete(map2) == 1;
		if(flag) {
			if(isContentId(comment.getParent_id())) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("parent_id", "" + comment.getId());
				commentDAO.delete(map);
				contentDAO.addComment(comment.getParent_id(), -1L);
				
			} else if(isCommentId(comment.getParent_id())) {
				commentDAO.addComment(comment.getParent_id(), -1L);
			}
			userDAO.addComment(comment.getUser_id(), -1L);
			
			if(info != null) info.add(Strings.SUCCESS_0015);
		} else {
			if(info != null) info.add(Strings.FAIL_0045);
		}
		return flag;
	}
	
	public List<Comment> get(long id, long parentid, long page, List<String> info) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(id > 0) map.put("id", "" + id);
		if(parentid > 0) map.put("parent_id", "" + parentid);
		
		String other = null;
		if(page > 0) other = "order by order_number limit " + ((page - 1) * Strings.PAGE_COMMENT) + "," + Strings.PAGE_COMMENT;
		
		List<Object> count = null;
		List<Comment> list = null;
		if(map.size() != 0 ) {
			count = commentDAO.count(map, null, other != null ? other : null);
			list = commentDAO.select(map, null, other != null ? other : null);
		}
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_COMMENT + 1));//Strings.SUCCESS_0025);
		} else {
			if(info != null) info.add(Strings.FAIL_0055);
		}
		return list;
	}
	
	public List<Comment> select(long id, long commentuserid, long parentid, long parentuserid, long grandparentid, String status, long userid, long page, List<String> info) {
		
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
		if(parentid > 0) map.put("parent_id", "" + parentid);
		if(grandparentid > 0) map.put("grandparent_id", "" + grandparentid);
		if(status != null) map.put("status", status);
		if(user.getRole().equals(Strings.ROLE_OPERATOR) || user.getRole().equals(Strings.ROLE_ADMINISTRATOR)) {
			if(commentuserid >= 0) map.put("user_id", "" + commentuserid);
			if(parentuserid >= 0) map.put("parent_user_id", "" + parentuserid);
		} else {
			map.put("user_id", "" + userid);
		}
		
		String other = null;
		if(page > 0) other = "order by order_number limit " + ((page - 1) * Strings.PAGE_COMMENT) + "," + Strings.PAGE_COMMENT;
		
		List<Object> count = commentDAO.count(map, null, other != null ? other : null);
		List<Comment> list = commentDAO.select(map, null, other != null ? other : null);
		
		if(count != null && list != null) {
			if(info != null) info.add("" + ((Long.parseLong("" + count.get(0)) - 1) / Strings.PAGE_COMMENT + 1));//Strings.SUCCESS_0025);
		} else {
			if(info != null) info.add(Strings.FAIL_0055);
		}
		return list;
	}
	
	public List<Map<String, String>> allToMap(List<Comment> commentList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(commentList == null || commentList.size() == 0) return list;
		for(Comment comment : commentList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + comment.getId());
			map.put("text", comment.getText());
			map.put("user_id", "" + comment.getUser_id());
			map.put("parent_id", "" + comment.getParent_id());
			map.put("parent_user_id", "" + comment.getParent_user_id());
			map.put("grandparent_id", "" + comment.getGrandparent_id());
			map.put("order_number", comment.getOrder_number());
			map.put("comment_count", "" + comment.getComment_count());
			map.put("vote_up", "" + comment.getVote_up());
			map.put("vote_down", "" + comment.getVote_down());
			map.put("status", comment.getStatus());
			map.put("created", comment.getCreated().toString());
			list.add(map);
		}
		return list;
	}
	
	public List<Map<String, String>> someToMap(List<Comment> commentList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if(commentList == null || commentList.size() == 0) return list;
		for(Comment comment : commentList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "" + comment.getId());
			map.put("text", comment.getText());
			map.put("user_id", "" + comment.getUser_id());
			map.put("parent_id", "" + comment.getParent_id());
			map.put("comment_count", "" + comment.getComment_count());
			map.put("vote_up", "" + comment.getVote_up());
			map.put("vote_down", "" + comment.getVote_down());
			map.put("created", comment.getCreated().toString());
			list.add(map);
		}
		return list;
	}
	
	public boolean voteUp(long id, List<String> info) {
		
		if(id <= 0) {
			if(info != null) info.add(Strings.FAIL_0014);
			return false;
		}
		
		boolean flag = commentDAO.addVoteUp(id, 1L);
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
		
		boolean flag = commentDAO.addVoteDown(id, 1L);
		if(flag) {
			if(info != null) info.add(Strings.SUCCESS_0029);
		} else {
			if(info != null) info.add(Strings.FAIL_0060);
		}
		return flag;
	}
	
	public boolean checkText(String text) {
		return true;
	}
	
	public boolean isContentId(long parentid) {
		return parentid > 0 && parentid % 2 == 1;
	}
	
	public boolean isCommentId(long parentid) {
		return parentid > 0 && parentid % 2 == 0;
	}
}
