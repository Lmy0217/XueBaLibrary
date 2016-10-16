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

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "loginout", results = {
		@Result(name = "login", type = "redirect", location = "/login.html"),
		@Result(name = "result", type = "json", params = { "root", "result" })
})
public class LoginOutAction extends ActionSupport {

	private static final long serialVersionUID = 4260387038302563868L;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	private String result;
	
	private List<String> info;
	
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
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("sumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			info.add(Strings.FAIL_0064);
			setResult(info.get(0));
			return "result";
		}
		session.setAttribute("sumbittime", time);
		
		session.invalidate();
		
		return "login";
	}
}
