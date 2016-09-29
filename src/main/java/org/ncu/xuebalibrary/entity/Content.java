package org.ncu.xuebalibrary.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ncu.xuebalibrary.config.Strings;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "content")
public class Content {

	/** ����ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	/** ���� */
	@Column(name = "title", nullable = false)
	private String title;
	
	/** ���� */
	@Column(name = "text", nullable = false)
	private String text;
	
	/** �û�ID */
	@Column(name = "user_id", nullable = false)
	private long user_id;
	
	/** ���ID */
	@Column(name = "category_id", nullable = false)
	private long category_id;
	
	/** ״̬ */
	@Column(name = "status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_NORMAL + Strings.QUOTE)
	private String status;
	
	/** ������ */
	@Column(name = "order_number")
	private long order_number;
	
	/** ������ */
	@Column(name = "view_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long view_count;
	
	/** �������� */
	@Column(name = "comment_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long comment_count;
	
	/** �������ʱ�� */
	@Column(name = "comment_time")
	private Date comment_time;
	
	/** ��������û�ID */
	@Column(name = "comment_user_id")
	private long comment_user_id;
	
	/** �������� */
	@Column(name = "created")
	private Date created;
	
	/** ���������� */
	@Column(name = "modified")
	private Date modified;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getOrder_number() {
		return order_number;
	}

	public void setOrder_number(long order_number) {
		this.order_number = order_number;
	}

	public long getView_count() {
		return view_count;
	}

	public void setView_count(long view_count) {
		this.view_count = view_count;
	}

	public long getComment_count() {
		return comment_count;
	}

	public void setComment_count(long comment_count) {
		this.comment_count = comment_count;
	}

	public Date getComment_time() {
		return comment_time;
	}

	public void setComment_time(Date comment_time) {
		this.comment_time = comment_time;
	}

	public long getComment_user_id() {
		return comment_user_id;
	}

	public void setComment_user_id(long comment_user_id) {
		this.comment_user_id = comment_user_id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
}
