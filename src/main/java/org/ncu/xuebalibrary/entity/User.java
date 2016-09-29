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
@Table(name = "user")
public class User {

	/** 主键ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	/** 登陆名 */
	@Column(name = "username", unique = true)
	private String username;
	
	/** 密码 */
	@Column(name = "password", nullable = false)
	private String password;
	
	/** 盐 */
	@Column(name = "salt")
	private String salt;
	
	/** 邮箱 */
	@Column(name = "email", unique = true)
	private String email;
	
	/** 邮箱状态（是否认证等） */
	@Column(name = "email_status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_UNDEFINED + Strings.QUOTE)
	private String email_status;
	
	/** 手机 */
	@Column(name = "mobile", unique = true)
	private String mobile;
	
	/** 手机状态（是否认证等） */
	@Column(name = "mobile_status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_UNDEFINED + Strings.QUOTE)
	private String mobile_status;
	
	/** 积分 */
	@Column(name = "point", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_DEFAULT_POINT + Strings.QUOTE)
	private long point;
	
	/** 权限 */
	@Column(name = "role", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.ROLE_VISITOR + Strings.QUOTE)
	private String role;
	
	/** 头像 */
	@Column(name = "avatar")
	private String avatar;
	
	/** 文档数量 */
	@Column(name = "document_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long document_count;
	
	/** 帖子数量 */
	@Column(name = "content_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long content_count;
	
	/** 回复数量 */
	@Column(name = "comment_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long comment_count;
	
	/** 状态 */
	@Column(name = "status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_UNCHECK + Strings.QUOTE)
	private String status;
	
	/** 创建日期 */
	@Column(name = "created")
	private Date created;
	
	/** 最后的登陆时间 */
	@Column(name = "logged")
	private Date logged;
	
	/** 激活时间 */
	@Column(name = "activated")
	private Date activated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail_status() {
		return email_status;
	}

	public void setEmail_status(String email_status) {
		this.email_status = email_status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile_status() {
		return mobile_status;
	}

	public void setMobile_status(String mobile_status) {
		this.mobile_status = mobile_status;
	}

	public long getPoint() {
		return point;
	}

	public void setPoint(long point) {
		this.point = point;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getDocument_count() {
		return document_count;
	}

	public void setDocument_count(long document_count) {
		this.document_count = document_count;
	}

	public long getContent_count() {
		return content_count;
	}

	public void setContent_count(long content_count) {
		this.content_count = content_count;
	}

	public long getComment_count() {
		return comment_count;
	}

	public void setComment_count(long comment_count) {
		this.comment_count = comment_count;
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

	public Date getLogged() {
		return logged;
	}

	public void setLogged(Date logged) {
		this.logged = logged;
	}

	public Date getActivated() {
		return activated;
	}

	public void setActivated(Date activated) {
		this.activated = activated;
	}
}
