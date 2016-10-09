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
@Table(name = "category")
public class Category {

	/** ����ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	/** ���� */
	@Column(name = "name", nullable = false)
	private String name;
	
	/** ���� */
	@Column(name = "text")
	private String text;
	
	/** ͼ�� */
	@Column(name = "icon")
	private String icon;
	
	/** ��������ID */
	@Column(name = "parent_id", columnDefinition = Strings.TYPE_BIGINT + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long parent_id;
	
	/** �÷�����ĵ����� */
	@Column(name = "document_count", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long document_count;
	
	/** ״̬ */
	@Column(name = "status", columnDefinition = Strings.TYPE_VARCHAR + Strings.DEFAULT + Strings.QUOTE + Strings.STATUS_NORMAL + Strings.QUOTE)
	private String status;
	
	/** ������ */
	@Column(name = "order_number", columnDefinition = Strings.TYPE_BIGINT + Strings.UNSIGNED + Strings.DEFAULT + Strings.QUOTE + Strings.NUMERIAL_ZERO + Strings.QUOTE)
	private long order_number;
	
	/** SEO�ؼ��� */
	@Column(name = "meta_keywords")
	private String meta_keywords;
	
	/** SEO������Ϣ */
	@Column(name = "meta_description")
	private String meta_description;
	
	/** �������� */
	@Column(name = "created")
	private Date created;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}

	public long getDocument_count() {
		return document_count;
	}

	public void setDocument_count(long document_count) {
		this.document_count = document_count;
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
}
