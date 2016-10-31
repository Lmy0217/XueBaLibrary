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
import org.ncu.xuebalibrary.entity.Comment;
import org.ncu.xuebalibrary.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "comment", results = {
		@Result(name = "result", type = "json", params = { "root", "map" })
})
public class CommentAction extends ActionSupport {

	private static final long serialVersionUID = -3490893861041393846L;

	@Autowired
	private CommentService commentService;
	
	private String type;
	private String text;
	private long parentid;
	private long id;
	private long userid = -1;
	private long parentuserid = -1;
	private long grandparentid;
	private String status;
	private long page = 1;
	
	private List<Comment> list;
	
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
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getParentuserid() {
		return parentuserid;
	}

	public void setParentuserid(long parentuserid) {
		this.parentuserid = parentuserid;
	}

	public long getGrandparentid() {
		return grandparentid;
	}

	public void setGrandparentid(long grandparentid) {
		this.grandparentid = grandparentid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
			list = commentService.get(id, parentid, page, info);
			if(list != null) {
				setMap(map(Strings.SUCCESS, info.get(0), commentService.someToMap(list), null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		}
		
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
		
		if(type.equals(Strings.TYPE_CREATE)) {
			flag = commentService.create(text, (Long)obj_id, parentid, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_DELETE)) {
			flag = commentService.delete((Long)obj_id, id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_SELECT)) {
			list = commentService.select(id, userid, parentid, parentuserid, grandparentid, status, (Long)obj_id, page, info);
			if(list != null) {
				setMap(map(Strings.SUCCESS, info.get(0), commentService.allToMap(list), null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_VOTEUP)) {
			flag = commentService.voteUp(id, info);
			if(flag) {
				setMap(map(Strings.SUCCESS, info.get(0), null, null));
				return "result";
			} else {
				setMap(map(Strings.FAIL, info.get(0), null, null));
				return "result";
			}
		} else if(type.equals(Strings.TYPE_VOTEDOWN)) {
			flag = commentService.voteDown(id, info);
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
