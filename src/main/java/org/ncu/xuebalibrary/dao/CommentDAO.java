package org.ncu.xuebalibrary.dao;

import java.util.List;

import org.ncu.xuebalibrary.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDAO extends BaseDAO<Comment, Long> {

	public boolean checkId(long id) {

		if(id <= 0) return false;
		
		String sql = "select * from comment where id = '" + id + "'";
		List<Comment> list = queryBySQL(sql);
		
		return list != null && list.size() == 1;
	}
	
	public boolean addComment(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update comment set comment_count = comment_count + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
	
	public boolean addVoteUp(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update comment set vote_up = vote_up + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
	
	public boolean addVoteDown(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update comment set vote_down = vote_down + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
}
