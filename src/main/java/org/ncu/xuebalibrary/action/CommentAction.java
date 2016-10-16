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
import org.ncu.xuebalibrary.entity.Comment;
import org.ncu.xuebalibrary.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "comment", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "get", type = "json", params = { "includeProperties", "list\\[\\d+\\]\\.id,list\\[\\d+\\]\\.text,list\\[\\d+\\]\\.user_id,list\\[\\d+\\]\\.parent_id,list\\[\\d+\\]\\.order_number,list\\[\\d+\\]\\.comment_count,list\\[\\d+\\]\\.vote_up,list\\[\\d+\\]\\.vote_down,list\\[\\d+\\]\\.created" }),
		@Result(name = "select", type = "json", params = { "root", "list" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
})
public class CommentAction extends ActionSupport {

	private static final long serialVersionUID = -3490893861041393846L;

	@Autowired
	private CommentService commentService;
	
	private String type;
	private String text;
	private long parentid;
	private long id;
	private long userid = -1;
	private long parentuserid = -1;
	private long grandparentid;
	private String status;
	private long page = 1;
	
	private String result;
	
	private List<Comment> list;
	
	private List<String> info;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getParentid() {
		return parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getParentuserid() {
		return parentuserid;
	}

	public void setParentuserid(long parentuserid) {
		this.parentuserid = parentuserid;
	}

	public long getGrandparentid() {
		return grandparentid;
	}

	public void setGrandparentid(long grandparentid) {
		this.grandparentid = grandparentid;
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
	
	public List<Comment> getList() {
		return list;
	}

	public void setList(List<Comment> list) {
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
			setList(commentService.get(id, parentid, page, info));
			if(getList() != null) {
				return "get";
			} else {
				setResult(info.get(0));
				return "result";
			}
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
			flag = commentService.create(text, (Long)obj_id, parentid, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = commentService.delete((Long)obj_id, id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			setList(commentService.select(id, userid, parentid, parentuserid, grandparentid, status, (Long)obj_id, page, info));
			if(getList() != null) {
				return "select";
			}
		} else if(type.equals(Strings.TYPE_VOTEUP)) {
			commentService.voteUp(id, info);
		} else if(type.equals(Strings.TYPE_VOTEDOWN)) {
			commentService.voteDown(id, info);
		}
		
		setResult(info.get(0));
		return "result";
	}
}
