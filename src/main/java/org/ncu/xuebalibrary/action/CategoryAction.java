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
import org.ncu.xuebalibrary.entity.Category;
import org.ncu.xuebalibrary.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "category", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class CategoryAction extends ActionSupport {

	private static final long serialVersionUID = -1735211240500974077L;

	@Autowired
	private CategoryService categoryService;
	
	private String type;
	private long id;
	private String name;
	private String text;
	private long parentid = -1;
	private String status;
	
	private List<Category> list;
	
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public long getParentid() {
		return parentid;
	}
	
	public void setParentid(long parentid) {
		this.parentid = parentid;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		
		if(type == null) {
			setMap(map(Strings.FAIL, Strings.FAIL_0014, null, null));
			return "result";
		} else if(type.equals(Strings.TYPE_GET)) {
			list = categoryService.get(id, parentid, info);
			if(list != null) {
				setMap(map(Strings.SUCCESS, info.get(0), categoryService.someToMap(list), null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		}
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("categorysumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
			return "result";
		}
		session.setAttribute("categorysumbittime", time);
		
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
		
		if(type.equals(Strings.TYPE_CREATE)) {
			flag = categoryService.create(name, text, parentid, (Long)obj_id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_UPDATE)) {
			flag = categoryService.update(id, name, text, parentid, (Long)obj_id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = categoryService.delete((Long)obj_id, id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			list = categoryService.select(id, name, text, parentid, status, (Long)obj_id, info);
			if(list != null) {
				setMap(map(Strings.SUCCESS, info.get(0), categoryService.allToMap(list), null));
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
