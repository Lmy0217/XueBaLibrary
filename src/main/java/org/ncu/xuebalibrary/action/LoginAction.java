package org.ncu.xuebalibrary.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.entity.User;
import org.ncu.xuebalibrary.listener.SessionListener;
import org.ncu.xuebalibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "login", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class LoginAction extends ActionSupport {

	private static final long serialVersionUID = -8494866431642674255L;
	
	@Autowired
	private UserService userService;

	private String username;
	private String password;
	
	private List<String> info;
	
	private Map<String, Object> map;
	
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

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	public Map<String, Object> map(String success, String info, List<Map<String, String>> data, String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("info", info);
		map.put("data", data);
		map.put("url", url);
		return map;
	}

	public String execute() {
		
		request = ServletActionContext.getRequest();
		session = request.getSession();
		
		info = new ArrayList<String>();
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("sumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
			return "result";
		}
		session.setAttribute("sumbittime", time);
		
		if(session.getAttribute("id") != null) {
			setMap(map(Strings.FAIL, null, null, "index.html"));
			return "result";
		}
		
		User user = userService.login(username, password, info);
		
		if(user != null) {
			session.setAttribute("id", user.getId());
			session.setAttribute("username", user.getUsername());
			session.setAttribute("role", user.getRole());
			session.setAttribute("status", user.getStatus());
			SessionListener.add(session);
			setMap(map(Strings.SUCCESS, info.get(0), null, "index.html"));
			return "result";
		} else {
			setMap(map(Strings.FAIL, info.get(0), null, null));
			return "result";
		}
	}
}
