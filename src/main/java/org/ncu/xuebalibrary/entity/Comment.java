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
@Table(name = "comment")
public class Comment {

	/** 主键ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	/** 内容 */
	@Column(name = "text", nullable = false)
	private String text;
	
	/** 用户ID */
	@Column(name = "user_id", nullable = false)
	private long user_id;
	
	/** 回复的评论或帖子ID */
	@Column(name = "parent_id", nullable = false)
	private long parent_id;
	
	/** 回复的用户ID */
	@Column(name = "parent_user_id")
	private long parent_user_id;
	
	/** 排序编号 */
	@Column(name = "order_number", nullable = false)
	private long order_number;
	
	/** 回复数量 */
	@Column(name = "comment_count", columnDefinition = Strings.TYPE_BIGINT + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long comment_count;
	
	/** 支持数量 */
	@Column(name = "vote_up", columnDefinition = Strings.TYPE_BIGINT + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long vote_up;
	
	/** 反对数量 */
	@Column(name = "vote_down", columnDefinition = Strings.TYPE_BIGINT + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long vote_down;
	
	/** 状态 */
	@Column(name = "status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_NORMAL + Strings.QUOTE)
	private String status;
	
	/** 评论时间 */
	@Column(name = "created")
	private Date created;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}

	public long getParent_user_id() {
		return parent_user_id;
	}

	public void setParent_user_id(long parent_user_id) {
		this.parent_user_id = parent_user_id;
	}

	public long getOrder_number() {
		return order_number;
	}

	public void setOrder_number(long order_number) {
		this.order_number = order_number;
	}

	public long getComment_count() {
		return comment_count;
	}

	public void setComment_count(long comment_count) {
		this.comment_count = comment_count;
	}

	public long getVote_up() {
		return vote_up;
	}

	public void setVote_up(long vote_up) {
		this.vote_up = vote_up;
	}

	public long getVote_down() {
		return vote_down;
	}

	public void setVote_down(long vote_down) {
		this.vote_down = vote_down;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
