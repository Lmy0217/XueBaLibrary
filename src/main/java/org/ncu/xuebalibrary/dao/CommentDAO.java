package org.ncu.xuebalibrary.dao;

import java.util.List;

import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDAO extends BaseDAO<Comment, Long> {
	
	private static long nextId = 0;
	
	private long getMaxId() {
		
		String sql = "select max(id) from comment";
		List<Object> max = queryBySQLFunction(sql);
		if(max != null && max.get(0) != null) {
			return Long.parseLong("" + max.get(0));
		} else {
			return 2 - Strings.COMMENTALLOCATIONSIZE;
		}
	}
	
	public long getNextId() {
		
		if(nextId == 0) nextId = getMaxId();
		
		nextId += Strings.COMMENTALLOCATIONSIZE;
		return nextId;
	}

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
