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

	/** 主键ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	/** 标题 */
	@Column(name = "title", nullable = false)
	private String title;
	
	/** 内容 */
	@Column(name = "text", nullable = false)
	private String text;
	
	/** 用户ID */
	@Column(name = "user_id", nullable = false)
	private long user_id;
	
	/** 类别ID */
	@Column(name = "category_id", nullable = false)
	private long category_id;
	
	/** 状态 */
	@Column(name = "status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_NORMAL + Strings.QUOTE)
	private String status;
	
	/** 排序编号 */
	@Column(name = "order_number")
	private long order_number;
	
	/** 访问量 */
	@Column(name = "view_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long view_count;
	
	/** 评论总数 */
	@Column(name = "comment_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long comment_count;
	
	/** 最后评论时间 */
	@Column(name = "comment_time")
	private Date comment_time;
	
	/** 最后评论用户ID */
	@Column(name = "comment_user_id")
	private long comment_user_id;
	
	/** 创建日期 */
	@Column(name = "created")
	private Date created;
	
	/** 最后更新日期 */
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
