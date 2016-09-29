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
import org.ncu.xuebalibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "password", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "login", location = "/login.html")
})
public class PasswordAction extends ActionSupport {

	private static final long serialVersionUID = 1957271307096784480L;
	
	@Autowired
	private UserService userService;
	
	private String type;
	private String password;
	private String newpassword;
	
	private String result;
	
	private List<String> info;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
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
		
		info = new ArrayList<String>();
		
		if(type == null) {
			info.add(Strings.FAIL_0014);
		} else if(type.equals(Strings.TYPE_PASSWORD)) {
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
			
			userService.updatePassword((Long)obj_id, password, newpassword, info);
		} else if(type.equals(Strings.TYPE_FORGET)) {
			info.add(Strings.FAIL_0018);
		} else {
			info.add(Strings.FAIL_0014);
		}
		
		setResult(info.get(0));
		return "result";
	}
}
