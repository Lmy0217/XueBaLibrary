package org.ncu.xuebalibrary.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.service.DocumentService;
import org.ncu.xuebalibrary.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "upload", results = {
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
})
public class UploadAction extends ActionSupport {

	private static final long serialVersionUID = -1520232506872590485L;

	@Autowired
	private DocumentService documentService;
	
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	
	private String result;
	
	private List<String> info;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
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
		
		boolean flag = false;
		info = new ArrayList<String>();
		
		Object obj_id = session.getAttribute("id");
		if(obj_id == null) return "login";
		
		Object obj_status = session.getAttribute("status");
		if(obj_status == null || ((String)obj_status).equals(Strings.STATUS_UNCHECK)) {
			info.add(Strings.FAIL_0020);
			setResult(info.get(0));
			return "result";
		}
		
		List<Object> list = new ArrayList<Object>();
		flag = documentService.upload((Long)obj_id, upload, uploadFileName, uploadContentType, list, info);
		
		if(flag && list.size() == 2) {
			Object obj_upload = session.getAttribute("upload");
			if(obj_upload != null && ((String)obj_upload).length() > 0) FileUtil.deleteDocFile(this, (String)obj_upload);
			session.setAttribute("upload", list.get(1));
			session.setAttribute("hash", list.get(0));
		}
		
		setResult(info.get(0));
		return "result";
	}
}
