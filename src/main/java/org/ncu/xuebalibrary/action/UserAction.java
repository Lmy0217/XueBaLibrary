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
import org.ncu.xuebalibrary.entity.User;
import org.ncu.xuebalibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "user", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "get", type = "json", params = { "includeProperties", "list\\[\\d+\\]\\.id,list\\[\\d+\\]\\.username,list\\[\\d+\\]\\.document_count,list\\[\\d+\\]\\.content_count,list\\[\\d+\\]\\.comment_count" }),
		@Result(name = "select", type = "json", params = { "root", "list" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
})
public class UserAction extends ActionSupport {

	private static final long serialVersionUID = -6211343708636136782L;

	@Autowired
	private UserService userService;
	
	private String type;
	private long id;
	private String username;
	private String email;
	private String emailstatus;
	private long point;
	private String role;
	private String status;
	private long page = 1;
	
	private String result;
	
	private List<User> list;
	
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailstatus() {
		return emailstatus;
	}

	public void setEmailstatus(String emailstatus) {
		this.emailstatus = emailstatus;
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

	public List<User> getList() {
		return list;
	}

	public void setList(List<User> list) {
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
			setList(userService.get(id, page, info));
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
		
		if(type.equals(Strings.TYPE_UPDATE)) {
			flag = userService.update(id, point, role, (Long)obj_id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = userService.delete((Long)obj_id, id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			setList(userService.select(id, username, email, emailstatus, role, status, (Long)obj_id, page, info));
			if(getList() != null) {
				return "select";
			}
		} else if(type.equals(Strings.TYPE_SIGN)) {
			userService.sign((Long)obj_id, info);
		} else {
			info.add(Strings.FAIL_0014);
		}
		
		setResult(info.get(0));
		return "result";
	}
}
