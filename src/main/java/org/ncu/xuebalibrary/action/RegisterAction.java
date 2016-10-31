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
import org.ncu.xuebalibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "register", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class RegisterAction extends ActionSupport {

	private static final long serialVersionUID = -8494866431642674255L;
	
	@Autowired
	private UserService userService;

	private String username;
	private String password;
	private String email;
	private String mobile;
	
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
		
		boolean flag = false;
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
		
		if(email != null && userService.checkEmail(email)) {
			
			long registeremailtime = System.currentTimeMillis();
			Object obj_registeremailtime = session.getAttribute("registeremailtime");
			if(obj_registeremailtime != null && registeremailtime - (Long)obj_registeremailtime <= Strings.EMAIL_SPACE){
				setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
				return "result";
			}
			session.setAttribute("registeremailtime", registeremailtime);
			
			flag = userService.registerByEmail(username, password, email, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, "login.html"));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if (mobile != null && userService.checkMobile(mobile)) {
			setMap(map(Strings.FAIL, Strings.FAIL_0018, null, null));
			return "result";
		} else {
			setMap(map(Strings.FAIL, Strings.FAIL_0014, null, null));
			return "result";
		}
	}
}
