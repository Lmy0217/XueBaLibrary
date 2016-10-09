package org.ncu.xuebalibrary.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "loginout", results = {
		@Result(name = "login", type = "redirect", location = "/login.html")
})
public class LoginOutAction extends ActionSupport {

	private static final long serialVersionUID = 4260387038302563868L;
	
	private HttpServletRequest request;
	private HttpSession session;

	public String execute() {
		
		request = ServletActionContext.getRequest();
		session = request.getSession();
		
		session.invalidate();
		
		return "login";
	}
}
