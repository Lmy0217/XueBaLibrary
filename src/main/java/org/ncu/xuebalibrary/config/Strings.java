package org.ncu.xuebalibrary.config;

public class Strings {
	
	public static final String URL = "http://127.0.0.1:8080/XueBaLibrary/";

	public static final String QUOTE = "'";
	
	public static final String STATUS_UNDEFINED = "undefined";
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_ACTIVITED = "activited";
	public static final String STATUS_UNCHECK = "uncheck";
	
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
	
	public static final String SUCCESS_0001 = "Register success!";//注册成功！";
	public static final String SUCCESS_0002 = "Login success!";//登陆成功！";
	public static final String SUCCESS_0003 = "Update password success!";//修改密码成功！";
	public static final String SUCCESS_0004 = "Send activite email success!";//激活邮件发送成功！";
	public static final String SUCCESS_0005 = "Activate email success!";//邮箱激活成功！";
	
	public static final String FAIL_0001 = "Username or password format error!";//用户名或密码格式错误！";
	public static final String FAIL_0002 = "Email format error!";//邮箱格式错误！";
	public static final String FAIL_0003 = "Email is registered!";//邮箱已被注册！";
	public static final String FAIL_0004 = "Username is registered!";//用户名已被注册！";
	public static final String FAIL_0005 = "Username error!";//不存在这个用户！";
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
}
