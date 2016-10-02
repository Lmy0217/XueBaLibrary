package org.ncu.xuebalibrary.config;

public class Strings {
	
	public static final String URL = "http://127.0.0.1:8080/XueBaLibrary/";

	public static final String QUOTE = "'";
	
	public static final String STATUS_UNDEFINED = "undefined";
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_ACTIVITED = "activited";
	public static final String STATUS_UNCHECK = "uncheck";
	public static final String STATUS_RESET = "reset";
	
	public static final String NUMERIAL_DEFAULT_POINT = "0";
	public static final String NUMERIAL_ZERO = "0";
	
	public static final String ROLE_VISITOR = "visitor";
	
	public static final String TYPE_VARCHAR = "varchar(255)";
	public static final String TYPE_BIGINT = "bigint(20)";
	public static final String TYPE_INT = "int(11)";
	
	public static final String TYPE_SEND_ACTIVITE = "sendactivite";
	public static final String TYPE_ACTIVITE = "activite";
	public static final String TYPE_SEND_PASSWORD = "sendpassword";
	public static final String TYPE_PASSWORD = "update";
	public static final String TYPE_FORGET = "forget";
	public static final String TYPE_RESET = "reset";
	
	public static final String DEFAULT = " DEFAULT ";
	public static final String UNSIGNED = " unsigned";
	
	public static final String SECURITY_TYPE = "sha-256";
	public static final int SECURITY_SALT_LENGTH = 16;
	
	public static final String EMAIL_HOST = "smtp.163.com";
	public static final String EMAIL_PROTOCOL = "smtp";
	public static final int EMAIL_PORT = 25;
	public static final String EMAIL_FROM = "xuebalibrary@163.com";
	public static final String EMAIL_PWD = "xuebalibrary1234";
	public static final long EMAIL_OVERTIME = 24 * 60 * 60 * 1000;
	public static final String EMAIL_SUBJECT_ACTIVITE = "5a2m6Zy45paH5bqT6LSm5Y+35r+A5rS76YKu5Lu277yM6K+35ZyoMjTlsI/ml7blhoXngrnlh7vpk77mjqXmv4DmtLvmgqjnmoTotKblj7c=";
	public static final String EMAIL_SUBJECT_PASSWORD = "5a2m6Zy45paH5bqT6YeN572u5a+G56CB6YKu5Lu277yM6K+35ZyoMjTlsI/ml7blhoXngrnlh7vpk77mjqXph43nva7mgqjnmoTotKblj7flr4bnoIE=";
	
	public static final long PASSWORD_OVERTIME = 24 * 60 * 60 * 1000;
	public static final long RESET_OVERTIME = 5 * 60 * 1000;
	
	public static final String SUCCESS_0001 = "Register success!";//注册成功！";
	public static final String SUCCESS_0002 = "Login success!";//登陆成功！";
	public static final String SUCCESS_0003 = "Update password success!";//修改密码成功！";
	public static final String SUCCESS_0004 = "Send activite email success!";//激活邮件发送成功！";
	public static final String SUCCESS_0005 = "Activate email success!";//邮箱激活成功！";
	public static final String SUCCESS_0006 = "Send reset password email success!";//重置密码邮件发送成功！";
	public static final String SUCCESS_0007 = "Get reset password role!";//获得重置密码权限成功！";
	public static final String SUCCESS_0008 = "Reset password success!";//重置密码成功！";
	
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
}
