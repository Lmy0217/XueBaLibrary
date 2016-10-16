package org.ncu.xuebalibrary.dao;

import java.util.List;

import org.ncu.xuebalibrary.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAO extends BaseDAO<Category, Long> {

	public boolean checkId(long id) {

		if(id <= 0) return false;
		
		String sql = "select * from category where id = '" + id + "'";
		List<Category> list = queryBySQL(sql);
		
		return list != null && list.size() == 1;
	}
	
	public boolean addDocument(long id, long number) {
		
		if(id <= 0 || number <= 0) return false;
		
		String sql = "update category set document_count = document_count + '" + number + "' where id = '" + id + "'";
		return excuteBySQL(sql) == 1;
	}
}
