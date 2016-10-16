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
@Action(value = "register", results = {
		@Result(name = "login", type = "redirect", location = "/login.html"),
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "index", type = "redirect", location = "/index.html")
})
public class RegisterAction extends ActionSupport {

	private static final long serialVersionUID = -8494866431642674255L;
	
	@Autowired
	private UserService userService;

	private String username;
	private String password;
	private String email;
	private String mobile;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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
		
		boolean flag = false;
		info = new ArrayList<String>();
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("sumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			info.add(Strings.FAIL_0064);
			setResult(info.get(0));
			return "result";
		}
		session.setAttribute("sumbittime", time);
		
		if(session.getAttribute("id") != null) return "index";
		
		if(email != null && userService.checkEmail(email)) {
			
			long registeremailtime = System.currentTimeMillis();
			Object obj_registeremailtime = session.getAttribute("registeremailtime");
			if(obj_registeremailtime != null && registeremailtime - (Long)obj_registeremailtime <= Strings.EMAIL_SPACE){
				info.add(Strings.FAIL_0064);
				setResult(info.get(0));
				return "result";
			}
			session.setAttribute("registeremailtime", registeremailtime);
			
			flag = userService.registerByEmail(username, password, email, info);
		} else if (mobile != null && userService.checkMobile(mobile)) {
			info.add(Strings.FAIL_0018);
		} else {
			info.add(Strings.FAIL_0014);
		}
		
		if(flag) {
			return "login";
		}
		
		setResult(info.get(0));
		return "result";
	}
}
