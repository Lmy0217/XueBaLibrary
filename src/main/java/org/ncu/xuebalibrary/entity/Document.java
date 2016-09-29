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
@Table(name = "document")
public class Document {

	/** ����ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	/** ���� */
	@Column(name = "title", nullable = false)
	private String title;
	
	/** ���� */
	@Column(name = "summary")
	private String summary;
	
	/** �洢·�� */
	@Column(name = "path", unique = true, nullable = false)
	private String path;
	
	/** �ĵ���׺ */
	@Column(name = "suffix", nullable = false)
	private String suffix;
	
	/** ���ID */
	@Column(name = "category_id", nullable = false)
	private long category_id;
	
	/** �û�ID */
	@Column(name = "user_id", nullable = false)
	private long user_id;
	
	/** �۸� */
	@Column(name = "price", columnDefinition = Strings.TYPE_INT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private int price;
	
	/** ״̬ */
	@Column(name = "status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_UNCHECK + Strings.QUOTE)
	private String status;
	
	/** ������ */
	@Column(name = "order_number")
	private long order_number;
	
	/** ֧������ */
	@Column(name = "vote_up", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long vote_up;
	
	/** �������� */
	@Column(name = "vote_down", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long vote_down;
	
	/** ������ */
	@Column(name = "view_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long view_count;
	
	/** ���� */
	@Column(name = "rate")
	private int rate;
	
	/** ���ִ��� */
	@Column(name = "rate_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long rate_count;
	
	/** mime */
	@Column(name = "mime_type")
	private String mime_type;
	
	/** SEO�ؼ��� */
	@Column(name = "meta_keywords")
	private String meta_keywords;
	
	/** SEO������Ϣ */
	@Column(name = "meta_description")
	private String meta_description;
	
	/** �������� */
	@Column(name = "created")
	private Date created;
	
	/** ���������� */
	@Column(name = "modified")
	private Date modified;
	
	/** ��ע��Ϣ */
	@Column(name = "remarks")
	private String remarks;

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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public long getCategory_id() {
		return category_id;
	}

	public void setCategory_id(long category_id) {
		this.category_id = category_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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

	public long getView_count() {
		return view_count;
	}

	public void setView_count(long view_count) {
		this.view_count = view_count;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public long getRate_count() {
		return rate_count;
	}

	public void setRate_count(long rate_count) {
		this.rate_count = rate_count;
	}

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}

	public String getMeta_keywords() {
		return meta_keywords;
	}

	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}

	public String getMeta_description() {
		return meta_description;
	}

	public void setMeta_description(String meta_description) {
		this.meta_description = meta_description;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
