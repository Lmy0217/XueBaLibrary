package org.ncu.xuebalibrary.dao;

import java.util.List;

import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.entity.Content;
import org.springframework.stereotype.Repository;

@Repository
public class ContentDAO extends BaseDAO<Content, Long> {
	
	private static long nextId = -1;
	
	private long getMaxId() {
		
		String sql = "select max(id) from content";
		List<Object> max = queryBySQLFunction(sql);
		if(max != null && max.get(0) != null) {
			return Long.parseLong("" + max.get(0));
		} else {
			return 1 - Strings.CONTENTALLOCATIONSIZE;
		}
	}
	
	public long getNextId() {
		
		if(nextId == -1) nextId = getMaxId();
		
		nextId += Strings.CONTENTALLOCATIONSIZE;
		return nextId;
	}

	public boolean checkId(long id) {

		if(id <= 0) return false;
		
		String sql = "select * from content where id = '" + id + "'";
		List<Content> list = queryBySQL(sql);
		
		return list != null && list.size() == 1;
	}
	
	public boolean addView(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update content set view_count = view_count + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
	
	public boolean addComment(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update content set comment_count = comment_count + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
}
