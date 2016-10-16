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
import org.ncu.xuebalibrary.config.Strings;
import org.ncu.xuebalibrary.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Action(value = "download", results = {
		@Result(name = "download", type = "stream", params = { "inputName", "download", "contentDisposition", "attachment;filename=\"${downloadFileName}\"", "bufferSize", "4096" }),
		@Result(name = "result", type = "json", params = { "root", "result" }),
		@Result(name = "login", type = "redirect", location = "/login.html")
})
public class DownloadAction extends ActionSupport {

	private static final long serialVersionUID = -7683445642308807635L;
	
	@Autowired
	private DocumentService documentService;
	
	private InputStream download;
	private String downloadFileName;
	
	private long documentid;
	
	private String result;
	
	private List<String> info;
	
	private HttpServletRequest request;
	private HttpSession session;
	
	public InputStream getDownload() {
		return download;
	}

	public void setDownload(InputStream download) {
		this.download = download;
	}
	
	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
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
		
		boolean flag = false;
		info = new ArrayList<String>();
		
		long time = System.currentTimeMillis();
		Object obj_sumbittime = session.getAttribute("sumbittime");
		if(obj_sumbittime != null && time - (Long)obj_sumbittime <= Strings.TIME_SUMBIT_SPACE){
			info.add(Strings.FAIL_0064);
			setResult(info.get(0));
			return "result";
		}
		session.setAttribute("sumbittime", time);
		
		Object obj_id = session.getAttribute("id");
		if(obj_id == null) return "login";
		
		Object obj_status = session.getAttribute("status");
		if(obj_status == null || ((String)obj_status).equals(Strings.STATUS_UNCHECK)) {
			info.add(Strings.FAIL_0020);
			setResult(info.get(0));
			return "result";
		}
		
		List<Object> list = new ArrayList<Object>();
		flag = documentService.download((Long)obj_id, documentid, list, info);
		
		if(flag && list.size() == 2) {
			setDownloadFileName((String)list.get(0));
			setDownload((FileInputStream)list.get(1));
		}
		if(!flag) {
			setResult(info.get(0));
			return "result";
		}
		
		return "download";
	}
}
