package org.ncu.xuebalibrary.action;

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

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "loginout", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class LoginOutAction extends ActionSupport {

	private static final long serialVersionUID = 4260387038302563868L;
	
	private String type;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	private Map<String, Object> map;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("loginoutsumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
			return "result";
		}
		session.setAttribute("loginoutsumbittime", time);
		
		if(type == null || (!type.equals(Strings.TYPE_USER) && !type.equals(Strings.TYPE_ADMIN))) {
			setMap(map(Strings.FAIL, Strings.FAIL_0014, null, null));
			return "result";
		}
		
		if(type.equals(Strings.TYPE_USER)) {
			setMap(map(Strings.SUCCESS, null, null, "login.html"));
		} else if(session != null && session.getAttribute("role") != null && (session.getAttribute("role").equals(Strings.ROLE_OPERATOR) || session.getAttribute("role").equals(Strings.ROLE_ADMINISTRATOR))) {
			setMap(map(Strings.SUCCESS, null, null, "admin-login.html"));
		} else {
			setMap(map(Strings.FAIL, Strings.FAIL_0041, null, null));
			return "result";
		}
		
		session.invalidate();
		return "result";
	}
}
