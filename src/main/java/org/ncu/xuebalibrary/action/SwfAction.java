package org.ncu.xuebalibrary.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.ncu.xuebalibrary.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "swf", results = {
		@Result(name = "swf", type = "stream", params = { "contentType", "application/x-shockwave-flash", "inputName", "swf", "bufferSize", "4096" }),
		@Result(name = "result", type = "json", params = { "root", "result" })
})
public class SwfAction extends ActionSupport {

	private static final long serialVersionUID = 1113769079910550509L;

	@Autowired
	private DocumentService documentService;
	
	private InputStream swf;
	
	private long documentid;
	
	private String result;
	
	private List<String> info;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	public InputStream getSwf() {
		return swf;
	}
	
	public void setSwf(InputStream swf) {
		this.swf = swf;
	}

	public long getDocumentid() {
		return documentid;
	}

	public void setDocumentid(long documentid) {
		this.documentid = documentid;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String execute() {
		
		request = ServletActionContext.getRequest();
		session = request.getSession();
		
		info = new ArrayList<String>();
		
		Object obj_id = session.getAttribute("id");
		
		FileInputStream fileInputStream = documentService.show(obj_id == null ? 0 : (Long)obj_id, documentid, info);
		if(fileInputStream == null) return "result";
		
		setSwf(fileInputStream);
		return "swf";
	}
}
