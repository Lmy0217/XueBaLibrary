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
import org.ncu.xuebalibrary.listener.SessionListener;
import org.ncu.xuebalibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "password", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class PasswordAction extends ActionSupport {

	private static final long serialVersionUID = 1957271307096784480L;
	
	@Autowired
	private UserService userService;
	
	private String type;
	private String password;
	private String newpassword;
	private String username;
	private long id;
	private String key;
	
	private List<String> info;
	
	private Map<String, Object> map;
	
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
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
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
		
		if(type == null) {
			setMap(map(Strings.FAIL, Strings.FAIL_0014, null, null));
			return "result";
		} else if(type.equals(Strings.TYPE_UPDATE)) {
			
			Object obj_id = session.getAttribute("id");
			if(obj_id == null) {
				setMap(map(Strings.FAIL, Strings.FAIL_0019, null, "login.html"));
				return "result";
			}
			
			Object obj_status = session.getAttribute("status");
			if(obj_status == null || ((String)obj_status).equals(Strings.STATUS_UNCHECK)) {
				setMap(map(Strings.FAIL, Strings.FAIL_0020, null, null));
				return "result";
			}
			
			flag = userService.updatePassword((Long)obj_id, password, newpassword, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
			
		} else if(type.equals(Strings.TYPE_SEND_PASSWORD)) {
			
			if(session.getAttribute("id") != null) {
				setMap(map(Strings.FAIL, null, null, "index.html"));
				return "result";
			}
			
			long passwordemailtime = System.currentTimeMillis();
			Object obj_passwordemailtime = session.getAttribute("passwordemailtime");
			if(obj_passwordemailtime != null && passwordemailtime - (Long)obj_passwordemailtime <= Strings.EMAIL_SPACE){
				setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
				return "result";
			}
			session.setAttribute("passwordemailtime", passwordemailtime);
			
			flag = userService.sendResetPasswordEmail(username, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
			
		} else if(type.equals(Strings.TYPE_FORGET)) {
			
			if(session.getAttribute("id") != null) {
				setMap(map(Strings.FAIL, null, null, "index.html"));
				return "result";
			}
			
			flag = userService.resetPasswordByEmail(id, key, info);
			if(flag) {
				session.setAttribute("id", id);
				SessionListener.add(session);
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
			
		} else if(type.equals(Strings.TYPE_RESET)){
			
			Object obj_id = session.getAttribute("id");
			if(obj_id == null) {
				setMap(map(Strings.FAIL, Strings.FAIL_0026, null, null));
				return "result";
			}
			
			flag = userService.resetPassword((Long)obj_id, newpassword, info);
			if(flag) {
				session.invalidate();
				setMap(map(Strings.SUCCESS, info.get(0), null, "login.html"));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
			
		} else {
			setMap(map(Strings.FAIL, Strings.FAIL_0014, null, null));
			return "result";
		}
	}
}
