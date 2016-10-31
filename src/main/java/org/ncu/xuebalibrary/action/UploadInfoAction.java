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
import org.ncu.xuebalibrary.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "uploadinfo", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class UploadInfoAction extends ActionSupport {

	private static final long serialVersionUID = -4369380958524893886L;

	@Autowired
	private DocumentService documentService;
	
	private String title;
	private String summary;
	private long categoryid;
	private int price;
	
	private List<String> info;
	
	private Map<String, Object> map;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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
		
		Object obj_upload = session.getAttribute("upload");
		Object obj_hash = session.getAttribute("hash");
		if(obj_upload == null || obj_hash == null || ((String)obj_upload).length() == 0 || ((String)obj_hash).length() == 0) {
			setMap(map(Strings.FAIL, Strings.FAIL_0031, null, null));
			return "result";
		}
		
		flag = documentService.create(title, summary, (String)obj_upload, categoryid, (Long)obj_id, price, (String)obj_hash, info);
		
		if(flag) {
			session.removeAttribute("upload");
			session.removeAttribute("hash");
			setMap(map(Strings.SUCCESS, info.get(0), null, null));
			return "result";
		} else {
			setMap(map(Strings.FAIL, info.get(0), null, null));
			return "result";
		}
	}
}
