package org.ncu.xuebalibrary.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ncu.xuebalibrary.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDAO<T, PK extends Serializable> {

	@Autowired
	private SessionFactory sessionFactory;

	private Class<?> entityClass;
	private String entityTableName;

	public BaseDAO() {
		entityClass = ReflectUtil.getGenericParameterType(this.getClass());
		String temp = entityClass.getName();
		entityTableName = temp.substring(1 + temp.lastIndexOf("."),
				temp.length()).toLowerCase();
	}

	public long excuteBySQL(String sql) {

		long result = -1;
		Session s = null;

		try {
			s = sessionFactory.getCurrentSession();
			s.beginTransaction();
			result = s.createSQLQuery(sql).executeUpdate();
			s.getTransaction().commit();
		} catch (Exception e) {
			result = -1;
			if (s != null)
				s.getTransaction().rollback();
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> queryBySQL(String sql) {

		List<T> list = null;
		Session s = null;

		try {
			s = sessionFactory.getCurrentSession();
			s.beginTransaction();
			list = s.createSQLQuery(sql).addEntity(entityClass).list();
			s.getTransaction().commit();
		} catch (Exception e) {
			list = null;
			if (s != null)
				s.getTransaction().rollback();
			e.printStackTrace();
		}

		return list;
	}

	public long insert(HashMap<String, String> map) {

		if (map == null || map.isEmpty())
			return -1;

		StringBuilder k = new StringBuilder();
		StringBuilder v = new StringBuilder();

		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iter.next();
			if (!checkInput(entry.getKey()) || !checkInput(entry.getValue()))
				return -2;
			k.append(entry.getKey() + ",");
			v.append("'" + entry.getValue() + "',");
		}

		k.deleteCharAt(k.length() - 1);
		v.deleteCharAt(v.length() - 1);

		return excuteBySQL("insert into " + entityTableName + " ("
				+ k.toString() + ") values (" + v.toString() + ")");
	}

	public long delete(HashMap<String, String> map) {

		if (map == null || map.isEmpty())
			return excuteBySQL("delete from " + entityTableName);

		StringBuilder kv = new StringBuilder();

		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iter.next();
			if (!checkInput(entry.getKey()) || !checkInput(entry.getValue()))
				return -2;
			kv.append(entry.getKey() + "='" + entry.getValue() + "' and ");
		}

		kv.delete(kv.length() - 5, kv.length());

		return excuteBySQL("delete from " + entityTableName + " where "
				+ kv.toString());
	}

	public long update(HashMap<String, String> newMap,
			HashMap<String, Long> addMap, HashMap<String, String> map) {

		if ((newMap == null || newMap.isEmpty())
				&& (addMap == null || addMap.isEmpty()))
			return -1;

		StringBuilder newsb = new StringBuilder();

		if (newMap != null && !newMap.isEmpty()) {
			Iterator<Entry<String, String>> newiter = newMap.entrySet()
					.iterator();
			while (newiter.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) newiter
						.next();
				if (!checkInput(entry.getKey())
						|| !checkInput(entry.getValue()))
					return -2;
				newsb.append(entry.getKey() + "='" + entry.getValue() + "',");
			}
		}
		if (addMap != null && !addMap.isEmpty()) {
			Iterator<Entry<String, Long>> additer = addMap.entrySet()
					.iterator();
			while (additer.hasNext()) {
				Entry<String, Long> entry = (Entry<String, Long>) additer
						.next();
				if (!checkInput(entry.getKey()))
					return -2;
				newsb.append(entry.getKey() + "=" + entry.getKey() + "+'"
						+ entry.getValue() + "',");
			}
		}

		newsb.deleteCharAt(newsb.length() - 1);

		StringBuilder sb = null;
		if (map != null && !map.isEmpty()) {
			sb = new StringBuilder();
			Iterator<Entry<String, String>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) iter
						.next();
				if (!checkInput(entry.getKey())
						|| !checkInput(entry.getValue()))
					return -2;
				sb.append(entry.getKey() + "='" + entry.getValue() + "' and ");
			}
			sb.delete(sb.length() - 5, sb.length());
		}

		return excuteBySQL("update " + entityTableName + " set "
				+ newsb.toString()
				+ (sb != null ? (" where " + sb.toString()) : ""));
	}

	public List<T> select(HashMap<String, String> map,
			HashMap<String, String> likeMap, String other) {

		if ((map == null || map.isEmpty())
				&& (likeMap == null || likeMap.isEmpty()))
			return queryBySQL("select * from "
					+ entityTableName
					+ ((other != null && !other.equals("")) ? (" " + other)
							: ""));

		StringBuilder sb = null;

		if (map != null && !map.isEmpty()) {
			sb = new StringBuilder();
			Iterator<Entry<String, String>> newiter = map.entrySet().iterator();
			while (newiter.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) newiter
						.next();
				if (!checkInput(entry.getKey())
						|| !checkInput(entry.getValue()))
					return null;
				sb.append(entry.getKey() + "='" + entry.getValue() + "' and ");
			}
		}

		if (sb != null)
			sb.delete(sb.length() - 5, sb.length());

		StringBuilder likesb = null;

		if (likeMap != null && !likeMap.isEmpty()) {
			likesb = new StringBuilder();
			Iterator<Entry<String, String>> likeiter = likeMap.entrySet()
					.iterator();
			while (likeiter.hasNext()) {
				Entry<String, String> entry = (Entry<String, String>) likeiter
						.next();
				if (!checkInput(entry.getKey())
						|| !checkInput(entry.getValue()))
					return null;
				likesb.append(entry.getKey() + " like '" + entry.getValue()
						+ "' or ");
			}
		}

		if (likesb != null)
			likesb.delete(likesb.length() - 4, likesb.length());

		if (!checkOther(other))
			return null;

		return queryBySQL("select * from " + entityTableName + " where "
				+ (sb != null ? sb.toString() : "")
				+ ((sb != null && likesb != null) ? " and " : "")
				+ (likesb != null ? ("(" + likesb.toString() + ")") : "")
				+ ((other != null && !other.equals("")) ? (" " + other) : ""));
	}

	public boolean checkInput(String input) {
		//TODO sql×¢Èë
		return true;
	}
	
	public boolean checkOther(String other) {
		
		return true;
	}
}
