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
@Action(value = "email", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class EmailAction extends ActionSupport {

	private static final long serialVersionUID = -8373931086159849174L;

	@Autowired
	private UserService userService;
	
	private String type;
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
		Object obj_sumbittime = session.getAttribute("emailsumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
			return "result";
		}
		session.setAttribute("emailsumbittime", time);
		
		if(type == null) {
			setMap(map(Strings.FAIL, Strings.FAIL_0014, null, null));
			return "result";
		} else if(type.equals(Strings.TYPE_SEND_ACTIVITE)) {
			
			Object obj_id = session.getAttribute("id");
			if(obj_id == null) {
				setMap(map(Strings.FAIL, Strings.FAIL_0019, null, "login.html"));
				return "result";
			}
			
			long activiteemailtime = System.currentTimeMillis();
			Object obj_activiteemailtime = session.getAttribute("activiteemailtime");
			if(obj_activiteemailtime != null && activiteemailtime - (Long)obj_activiteemailtime <= Strings.EMAIL_SPACE){
				setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
				return "result";
			}
			session.setAttribute("activiteemailtime", activiteemailtime);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", "" + (Long)obj_id);
			flag = userService.sendActivateEmail(map, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
			
		} else if(type.equals(Strings.TYPE_ACTIVITE)) {
			
			flag = userService.activateEmail(id, key, info);
			if(flag) {
				Object obj_id = session.getAttribute("id");
				if(obj_id != null && id == (Long)obj_id) {
					session.setAttribute("status", Strings.STATUS_NORMAL);
				}
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
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
