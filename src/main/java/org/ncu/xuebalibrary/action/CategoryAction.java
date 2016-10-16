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
import org.ncu.xuebalibrary.entity.Category;
import org.ncu.xuebalibrary.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "category", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "get", type = "json", params = { "includeProperties", "list\\[\\d+\\]\\.id,list\\[\\d+\\]\\.name,list\\[\\d+\\]\\.text,list\\[\\d+\\]\\.parent_id,list\\[\\d+\\]\\.document_count" }),
		@Result(name = "select", type = "json", params = { "root", "list" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
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
	
	private String result;
	
	private List<Category> list;
	
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
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public List<Category> getList() {
		return list;
	}

	public void setList(List<Category> list) {
		this.list = list;
	}
	
	public String execute() {
		
		request = ServletActionContext.getRequest();
		session = request.getSession();
		
		boolean flag = false;
		info = new ArrayList<String>();
		
		if(type == null) {
			info.add(Strings.FAIL_0014);
			setResult(info.get(0));
			return "result";
		} else if(type.equals(Strings.TYPE_GET)) {
			setList(categoryService.get(id, parentid, info));
			if(getList() != null) {
				return "get";
			} else {
				setResult(info.get(0));
				return "result";
			}
		}
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("sumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			info.add(Strings.FAIL_0064);
			setResult(info.get(0));
			return "result";
		}
		session.setAttribute("sumbittime", time);
		
		Object obj_id = session.getAttribute("id");
		if(obj_id == null) {
			info.add(Strings.FAIL_0019);
			setResult(info.get(0));
			return "login";
		}
		
		Object obj_status = session.getAttribute("status");
		if(obj_status == null || ((String)obj_status).equals(Strings.STATUS_UNCHECK)) {
			info.add(Strings.FAIL_0020);
			setResult(info.get(0));
			return "result";
		}
		
		if(type.equals(Strings.TYPE_CREATE)) {
			flag = categoryService.create(name, text, parentid, (Long)obj_id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_UPDATE)) {
			flag = categoryService.update(id, name, text, parentid, (Long)obj_id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = categoryService.delete((Long)obj_id, id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			setList(categoryService.select(id, name, text, parentid, status, (Long)obj_id, info));
			if(getList() != null) {
				return "select";
			}
		}
		
		setResult(info.get(0));
		return "result";
	}
}
