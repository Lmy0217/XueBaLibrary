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
@Action(value = "login", results = {
		@Result(name = "index", location = "/index.html"),
		@Result(name = "result", type = "json", params = { "root", "result" }),
})
public class LoginAction extends ActionSupport {

	private static final long serialVersionUID = -8494866431642674255L;
	
	@Autowired
	private UserService userService;

	private String username;
	private String password;
	
	private String result;
	
	private List<String> info;
	
	private HttpServletRequest request;
	private HttpSession session;

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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String execute() {
		
		request = ServletActionContext.getRequest();
		session = request.getSession();
		
		if(session.getAttribute("id") != null) return "index";
		
		User user = null;
		info = new ArrayList<String>();
		
		if(username != null && password != null && (userService.checkUsername(username) || userService.checkEmail(username) || userService.checkMobile(username)) && userService.checkPassword(password)) {
			user = userService.login(username, password, info);
		} else {
			info.add(Strings.FAIL_0014);
		}
		
		if(user != null) {
			session.setAttribute("id", user.getId());
			session.setAttribute("username", user.getUsername());
			session.setAttribute("role", user.getRole());
			session.setAttribute("status", user.getStatus());
		} else {
			
		}
		
		setResult(info.get(0));
		return user != null ? "index" : "result";
	}
}
