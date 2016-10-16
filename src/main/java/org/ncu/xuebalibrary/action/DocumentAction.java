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
import org.ncu.xuebalibrary.entity.Document;
import org.ncu.xuebalibrary.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "document", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "get", type = "json", params = { "includeProperties", "list\\[\\d+\\]\\.id,list\\[\\d+\\]\\.title,list\\[\\d+\\]\\.summary,list\\[\\d+\\]\\.category_id,list\\[\\d+\\]\\.user_id,list\\[\\d+\\]\\.price,list\\[\\d+\\]\\.vote_up,list\\[\\d+\\]\\.vote_down,list\\[\\d+\\]\\.view_count,list\\[\\d+\\]\\.rate,list\\[\\d+\\]\\.rate_count,list\\[\\d+\\]\\.modified" }),
		@Result(name = "select", type = "json", params = { "root", "list" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
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
	
	private String result;
	
	private List<Document> list;
	
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<Document> getList() {
		return list;
	}

	public void setList(List<Document> list) {
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
			setList(documentService.get(id, title, summary, categoryid, page, info));
			if(getList() != null) {
				return "get";
			} else {
				setResult(info.get(0));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_VIEW)) {
			documentService.addView(id, info);
			setResult(info.get(0));
			return "result";
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
		
		if(type.equals(Strings.TYPE_UPDATE)) {
			flag = documentService.update(id, categoryid, price, status, (Long)obj_id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = documentService.delete((Long)obj_id, id, info);
			if(flag) {
				
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			setList(documentService.select(id, title, summary, categoryid, userid, status, (Long)obj_id, page, info));
			if(getList() != null) {
				return "select";
			}
		} else if(type.equals(Strings.TYPE_VOTEUP)) {
			documentService.voteUp(id, info);
		} else if(type.equals(Strings.TYPE_VOTEDOWN)) {
			documentService.voteDown(id, info);
		} else if(type.equals(Strings.TYPE_RATE)) {
			documentService.rate(id, rate, info);
		}
		
		setResult(info.get(0));
		return "result";
	}
}
