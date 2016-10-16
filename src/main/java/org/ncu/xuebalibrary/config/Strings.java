package org.ncu.xuebalibrary.config;

public class Strings {
	
	public static final String PROJECT_NAME = "XueBaLibrary";
	public static final String PROJECT_URL = "http://127.0.0.1:8080/XueBaLibrary/";
	
	public static final String LIB_SWFTOOLS = "F:\\Program Files (x86)\\SWFTools\\pdf2swf.exe";
	
	public static final String DIR_DOC = "XueBaLibraryDocs";
	public static final String DIR_SWF = "XueBaLibraryDocs/swf";

	public static final String QUOTE = "'";
	
	public static final String STATUS_UNDEFINED = "undefined";
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_ACTIVITED = "activited";
	public static final String STATUS_UNCHECK = "uncheck";
	public static final String STATUS_RESET = "reset";
	
	public static final String NUMERIAL_DEFAULT_POINT = "0";
	public static final String NUMERIAL_ZERO = "0";
	
	public static final String ROLE_VISITOR = "visitor";
	public static final String ROLE_OPERATOR = "operator";
	public static final String ROLE_ADMINISTRATOR = "administrator";
	
	public static final String TYPE_VARCHAR = "varchar(255)";
	public static final String TYPE_BIGINT = "bigint(20)";
	public static final String TYPE_INT = "int(11)";
	public static final String TYPE_TEXT = "text";
	
	public static final String TYPE_SEND_ACTIVITE = "sendactivite";
	public static final String TYPE_ACTIVITE = "activite";
	public static final String TYPE_SEND_PASSWORD = "sendpassword";
	public static final String TYPE_UPDATE = "update";
	public static final String TYPE_FORGET = "forget";
	public static final String TYPE_RESET = "reset";
	public static final String TYPE_CREATE = "create";
	public static final String TYPE_GET = "get";
	public static final String TYPE_DELETE = "delete";
	public static final String TYPE_SELECT = "select";
	public static final String TYPE_VIEW = "view";
	public static final String TYPE_VOTEUP = "voteup";
	public static final String TYPE_VOTEDOWN = "votedown";
	public static final String TYPE_RATE = "rate";
	public static final String TYPE_SIGN = "sign";
	
	public static final String DEFAULT = " DEFAULT ";
	public static final String UNSIGNED = " unsigned";
	
	public static final String SECURITY_TYPE = "sha-256";
	public static final int SECURITY_SALT_LENGTH = 16;
	public static final String SECURITY_HASH = "md5";
	
	public static final int RANDOM_SIGN_POINT_BEGIN = 5;
	public static final int RANDOM_SIGN_POINT_LENGTH = 6;
	
	public static final int REGISTER_POINT = 20;
	
	public static final long TIME_SUMBIT_SPACE = 1000;
	
	public static final String FORMAT_COMMENTORDER = "000000000";
	
	public static final long PAGE_USER = 20;
	public static final long PAGE_DOCUMENT = 20;
	public static final long PAGE_CONTENT = 20;
	public static final long PAGE_COMMENT = 20;
	
	public static final String EMAIL_HOST = "smtp.163.com";
	public static final String EMAIL_PROTOCOL = "smtp";
	public static final int EMAIL_PORT = 25;
	public static final String EMAIL_FROM = "xuebalibrary@163.com";
	public static final String EMAIL_PWD = "xuebalibrary1234";
	public static final long EMAIL_SPACE = 60 * 1000;
	public static final long EMAIL_OVERTIME = 24 * 60 * 60 * 1000;
	public static final String EMAIL_SUBJECT_ACTIVITE = "5a2m6Zy45paH5bqT6LSm5Y+35r+A5rS76YKu5Lu277yM6K+35ZyoMjTlsI/ml7blhoXngrnlh7vpk77mjqXmv4DmtLvmgqjnmoTotKblj7c=";
	public static final String EMAIL_SUBJECT_PASSWORD = "5a2m6Zy45paH5bqT6YeN572u5a+G56CB6YKu5Lu277yM6K+35ZyoMjTlsI/ml7blhoXngrnlh7vpk77mjqXph43nva7mgqjnmoTotKblj7flr4bnoIE=";
	
	public static final long OVERTIME_PASSWORD = 24 * 60 * 60 * 1000;
	public static final long OVERTIME_RESET = 5 * 60 * 1000;
	
	public static final String SUCCESS_0001 = "Register success!";//注册成功！";
	public static final String SUCCESS_0002 = "Login success!";//登陆成功！";
	public static final String SUCCESS_0003 = "Update password success!";//修改密码成功！";
	public static final String SUCCESS_0004 = "Send activite email success!";//激活邮件发送成功！";
	public static final String SUCCESS_0005 = "Activate email success!";//邮箱激活成功！";
	public static final String SUCCESS_0006 = "Send reset password email success!";//重置密码邮件发送成功！";
	public static final String SUCCESS_0007 = "Get reset password role!";//获得重置密码权限成功！";
	public static final String SUCCESS_0008 = "Reset password success!";//密码重置成功！";
	public static final String SUCCESS_0009 = "Upload file success!";//文档上传成功！";
	public static final String SUCCESS_0010 = "Add file success!";//文档添加成功！";
	public static final String SUCCESS_0011 = "Get document success!";//获得文档成功！";
	public static final String SUCCESS_0012 = "Create content success!";//帖子创建成功！";
	public static final String SUCCESS_0013 = "Update content success!";//帖子更新成功！";
	public static final String SUCCESS_0014 = "Create comment success!";//回复创建成功！";
	public static final String SUCCESS_0015 = "Delete comment success!";//回复删除成功！";
	public static final String SUCCESS_0016 = "Delete content success!";//帖子删除成功！";
	public static final String SUCCESS_0017 = "Create category success!";//类别创建成功！";
	public static final String SUCCESS_0018 = "Update category success!";//类别更新成功！";
	public static final String SUCCESS_0019 = "Get content success!";//帖子获得成功！";
	public static final String SUCCESS_0020 = "Delete category success!";//类别删除成功！";
	public static final String SUCCESS_0021 = "Delete document success!";//文档删除成功！";
	public static final String SUCCESS_0022 = "Delete user success!";//用户删除成功！";
	public static final String SUCCESS_0023 = "Get category success!";//获得类型成功！";
	public static final String SUCCESS_0024 = "Get user success!";//获得用户成功！";
	public static final String SUCCESS_0025 = "Get comment success!";//获得评论成功！";
	public static final String SUCCESS_0026 = "Update document success!";//文档更新成功！";
	public static final String SUCCESS_0027 = "Update user success!";//更新用户成功！";
	public static final String SUCCESS_0028 = "Vote up success!";
	public static final String SUCCESS_0029 = "Vote down success!";
	public static final String SUCCESS_0030 = "Add view success!";
	public static final String SUCCESS_0031 = "Add rate success!";
	public static final String SUCCESS_0032 = "Sign in success!";
	
	public static final String FAIL_0001 = "Username or password format error!";//用户名或密码格式错误！";
	public static final String FAIL_0002 = "Email format error!";//邮箱格式错误！";
	public static final String FAIL_0003 = "Email is registered!";//邮箱已被注册！";
	public static final String FAIL_0004 = "Username is registered!";//用户名已被注册！";
	public static final String FAIL_0005 = "Username format error!";//用户名格式错误！";
	public static final String FAIL_0006 = "Username or password error!";//用户名或密码错误！";
	public static final String FAIL_0007 = "Password format error!";//密码格式错误！";
	public static final String FAIL_0008 = "User is not exist!";//用户不存在！";
	public static final String FAIL_0009 = "Update password failed!";//修改密码失败！";
	public static final String FAIL_0010 = "Email is activited!";//邮件已激活！";
	public static final String FAIL_0011 = "Create email failed!";//邮件生成失败！";
	public static final String FAIL_0012 = "Save email failed!";//邮件保存失败！";
	public static final String FAIL_0013 = "Send email failed!";//邮件发送失败！";
	public static final String FAIL_0014 = "Param format error!";//参数格式错误！";
	public static final String FAIL_0015 = "Over activite time!";//超过激活时间！";
	public static final String FAIL_0016 = "ID or key error!";//id或key错误！";
	public static final String FAIL_0017 = "Activate email failed!";//邮箱激活失败！";
	public static final String FAIL_0018 = "Numbered Mode!";//暂不支持！";
	public static final String FAIL_0019 = "Please login!";//请登录！";
	public static final String FAIL_0020 = "Please activite email!";//请先激活邮件！";
	public static final String FAIL_0021 = "User is not in reset password status!";//用户不处于重置密码状态！";
	public static final String FAIL_0022 = "Over reset time!";//超过重置时间！";
	public static final String FAIL_0023 = "Failed to get reset password role!";//获得重置密码权限失败！";
	public static final String FAIL_0024 = "Please login out and try again!";//请退出登陆重试！";
	public static final String FAIL_0025 = "User is not in reset status!";//用户不处于重置密码状态！";
	public static final String FAIL_0026 = "Failed to reset password!";//重置密码失败！";
	public static final String FAIL_0027 = "Get upload directory failed!";//获取上传文件夹失败！";
	public static final String FAIL_0028 = "Document type is not allowed!";//不支持的文档格式！";
	public static final String FAIL_0029 = "Upload document failed!";//上传文档失败！";
	public static final String FAIL_0030 = "Same document is exist!";//存在相同文档！";
	public static final String FAIL_0031 = "Document is not upload!";//文档未上传！";
	public static final String FAIL_0032 = "Get document infomation failed!";//获取文档信息失败！";
	public static final String FAIL_0033 = "Add document failed!";//文档添加失败！";
	public static final String FAIL_0034 = "Document is not exist!";//文档不存在！";
	public static final String FAIL_0035 = "Point is not enough!";//积分不足！";
	public static final String FAIL_0036 = "Get document failed!";//获得文档失败！";
	public static final String FAIL_0037 = "Deduct point failed!";//扣除积分失败！";
	public static final String FAIL_0038 = "Convert document failed!";//转换文档失败！";
	public static final String FAIL_0039 = "Create content failed!";//帖子创建失败！";
	public static final String FAIL_0040 = "Content is not exist!";//帖子不存在！";
	public static final String FAIL_0041 = "Permission denied!";//没有权限！";
	public static final String FAIL_0042 = "Update content failed!";//帖子更新失败！";
	public static final String FAIL_0043 = "Comment is not exist!";//回复不存在！";
	public static final String FAIL_0044 = "Create comment failed!";//回复创建失败！";
	public static final String FAIL_0045 = "Delete comment failed!";//回复删除失败！";
	public static final String FAIL_0046 = "Delete content failed!";//帖子删除失败！";
	public static final String FAIL_0047 = "Create category failed!";//类别创建失败！";
	public static final String FAIL_0048 = "Update category failed!";//类别更新失败！";
	public static final String FAIL_0049 = "Category is not exist!";//类别不存在！";
	public static final String FAIL_0050 = "Delete category failed!";//类别删除失败！";
	public static final String FAIL_0051 = "Delete document failed!";//文档删除失败！";
	public static final String FAIL_0052 = "Delete user failed!";//用户删除失败！";
	public static final String FAIL_0053 = "Get category failed!";//获得类型失败！";
	public static final String FAIL_0054 = "Get content failed!";//获得帖子失败！";
	public static final String FAIL_0055 = "Get comment failed!";//获得帖子失败！";
	public static final String FAIL_0056 = "Update document failed!";//文档更新失败！";
	public static final String FAIL_0057 = "Get user failed!";//获得用户失败！";
	public static final String FAIL_0058 = "Update user failed!";//用户更新失败！";
	public static final String FAIL_0059 = "Vote up failed!";
	public static final String FAIL_0060 = "Vote down failed!";
	public static final String FAIL_0061 = "Add view failed!";
	public static final String FAIL_0062 = "Add rate failed!";
	public static final String FAIL_0063 = "Sign in failed!";//签到失败！";
	public static final String FAIL_0064 = "Operation too fast!";//操作过快！";
}
