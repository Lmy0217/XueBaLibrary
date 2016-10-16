package org.ncu.xuebalibrary.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.entity.Content;
import org.ncu.xuebalibrary.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "content", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "get", type = "json", params = { "includeProperties", "list\\[\\d+\\]\\.id,list\\[\\d+\\]\\.title,list\\[\\d+\\]\\.text,list\\[\\d+\\]\\.user_id,list\\[\\d+\\]\\.category_id,list\\[\\d+\\]\\.view_count,list\\[\\d+\\]\\.comment_count,list\\[\\d+\\]\\.comment_time,list\\[\\d+\\]\\.comment_user_id,list\\[\\d+\\]\\.modified" }),
		@Result(name = "select", type = "json", params = { "root", "list" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
})
public class ContentAction extends ActionSupport {

	private static final long serialVersionUID = -9212673060032748324L;

	@Autowired
	private ContentService contentService;
	
	private String type;
	private long id;
	private String title;
	private String text;
	private long userid = -1;
	private long categoryid = -1;
	private String status;
	private long page = 1;
	
	private String result;
	
	private List<Content> list;
	
	private List<String> info;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
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
	
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public List<Content> getList() {
		return list;
	}

	public void setList(List<Content> list) {
		this.list = list;
	}
	
	public String execute() {
		
		request = ServletActionContext.getRequest();
		session = request.getSession();
		
		boolean flag = false;
		info = new ArrayList<String>();
		
		if(type == null) {
			info.add(Strings.FAIL_0014);
			setResult(info.get(0));
			return "result";
		} else if(type.equals(Strings.TYPE_GET)) {
			setList(contentService.get(id, title, text, categoryid, page, info));
			if(getList() != null) {
				return "get";
			} else {
				setResult(info.get(0));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_VIEW)) {
			contentService.addView(id, info);
			setResult(info.get(0));
			return "result";
		}
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("sumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			info.add(Strings.FAIL_0064);
			setResult(info.get(0));
			return "result";
		}
		session.setAttribute("sumbittime", time);
		
		Object obj_id = session.getAttribute("id");
		if(obj_id == null) {
			info.add(Strings.FAIL_0019);
			setResult(info.get(0));
			return "login";
		}
		
		Object obj_status = session.getAttribute("status");
		if(obj_status == null || ((String)obj_status).equals(Strings.STATUS_UNCHECK)) {
			info.add(Strings.FAIL_0020);
			setResult(info.get(0));
			return "result";
		}
		
		if(type.equals(Strings.TYPE_CREATE)) {
			flag = contentService.create(title, text, (Long)obj_id, categoryid, info);
			//TODO Ìø×ªÎÊÌâ
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_UPDATE)) {
			flag = contentService.update(id, title, text, categoryid, (Long)obj_id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = contentService.delete((Long)obj_id, id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			setList(contentService.select(id, title, text, userid, categoryid, status, (Long)obj_id, page, info));
			if(getList() != null) {
				return "select";
			}
		}
		
		setResult(info.get(0));
		return "result";
	}
}
