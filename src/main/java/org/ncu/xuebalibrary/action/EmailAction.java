package org.ncu.xuebalibrary.action;

import java.util.ArrayList;
import java.util.HashMap;
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
@Action(value = "email", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
})
public class EmailAction extends ActionSupport {

	private static final long serialVersionUID = -8373931086159849174L;

	@Autowired
	private UserService userService;
	
	private String type;
	private long id;
	private String key;
	
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
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
		
		if(type == null) {
			info.add(Strings.FAIL_0014);
		} else if(type.equals(Strings.TYPE_SEND_ACTIVITE)) {
			
			Object obj_id = session.getAttribute("id");
			if(obj_id == null) {
				info.add(Strings.FAIL_0019);
				setResult(info.get(0));
				return "login";
			}
			
			long activiteemailtime = System.currentTimeMillis();
			Object obj_activiteemailtime = session.getAttribute("activiteemailtime");
			if(obj_activiteemailtime != null && activiteemailtime - (Long)obj_activiteemailtime <= Strings.EMAIL_SPACE){
				info.add(Strings.FAIL_0064);
				setResult(info.get(0));
				return "result";
			}
			session.setAttribute("activiteemailtime", activiteemailtime);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "" + (Long)obj_id);
			flag = userService.sendActivateEmail(map, info);
			
		} else if(type.equals(Strings.TYPE_ACTIVITE)) {
			
			flag = userService.activateEmail(id, key, info);
			
			if(flag) {
				Object obj_id = session.getAttribute("id");
				if(obj_id != null && id == (Long)obj_id) {
					session.setAttribute("status", Strings.STATUS_NORMAL);
				}
			}
			
		} else {
			info.add(Strings.FAIL_0014);
		}
		
		setResult(info.get(0));
		return "result";
	}
}
