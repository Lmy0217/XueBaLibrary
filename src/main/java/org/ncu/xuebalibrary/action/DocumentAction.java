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
import org.ncu.xuebalibrary.entity.Document;
import org.ncu.xuebalibrary.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "document", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class DocumentAction extends ActionSupport {

	private static final long serialVersionUID = -2469145745050781590L;

	@Autowired
	private DocumentService documentService;
	
	private String type;
	private long id;
	private String title;
	private String summary;
	private long userid = -1;
	private long categoryid = -1;
	private int price;
	private String status;
	private int rate;
	private long page = 1;
	
	private List<Document> list;
	
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

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
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
			list = documentService.get(id, title, summary, categoryid, page, info);
			if(list != null) {
				setMap(map(Strings.SUCCESS, info.get(0), documentService.someToMap(list), null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_VIEW)) {
			flag = documentService.addView(id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		}
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("documentsumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			setMap(map(Strings.FAIL, Strings.FAIL_0064, null, null));
			return "result";
		}
		session.setAttribute("documentsumbittime", time);
		
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
		
		if(type.equals(Strings.TYPE_UPDATE)) {
			flag = documentService.update(id, categoryid, price, status, (Long)obj_id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = documentService.delete((Long)obj_id, id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			list = documentService.select(id, title, summary, categoryid, userid, status, (Long)obj_id, page, info);
			if(list != null) {
				setMap(map(Strings.SUCCESS, info.get(0), documentService.allToMap(list), null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_VOTEUP)) {
			flag = documentService.voteUp(id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_VOTEDOWN)) {
			flag = documentService.voteDown(id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_RATE)) {
			flag = documentService.rate(id, rate, info);
			if(flag) {
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
