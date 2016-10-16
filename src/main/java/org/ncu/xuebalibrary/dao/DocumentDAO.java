package org.ncu.xuebalibrary.dao;

import org.ncu.xuebalibrary.entity.Document;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentDAO extends BaseDAO<Document, Long> {

	public boolean addVoteUp(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update document set vote_up = vote_up + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
	
	public boolean addVoteDown(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update document set vote_down = vote_down + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
	
	public boolean addView(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update document set view_count = view_count + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
	
	public boolean addRate(long id, int rate) {
		
		if(id <= 0 || rate < 0) return false;
		
		String sql = "update document set rate = (rate * rate_count + '" + rate + "') / (rate_count + '1'), rate_count = rate_count + '1' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
}
