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
}
