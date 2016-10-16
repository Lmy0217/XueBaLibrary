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
	
	public static final String SUCCESS_0001 = "Register success!";//ע��ɹ���";
	public static final String SUCCESS_0002 = "Login success!";//��½�ɹ���";
	public static final String SUCCESS_0003 = "Update password success!";//�޸�����ɹ���";
	public static final String SUCCESS_0004 = "Send activite email success!";//�����ʼ����ͳɹ���";
	public static final String SUCCESS_0005 = "Activate email success!";//���伤��ɹ���";
	public static final String SUCCESS_0006 = "Send reset password email success!";//���������ʼ����ͳɹ���";
	public static final String SUCCESS_0007 = "Get reset password role!";//�����������Ȩ�޳ɹ���";
	public static final String SUCCESS_0008 = "Reset password success!";//�������óɹ���";
	public static final String SUCCESS_0009 = "Upload file success!";//�ĵ��ϴ��ɹ���";
	public static final String SUCCESS_0010 = "Add file success!";//�ĵ���ӳɹ���";
	public static final String SUCCESS_0011 = "Get document success!";//����ĵ��ɹ���";
	public static final String SUCCESS_0012 = "Create content success!";//���Ӵ����ɹ���";
	public static final String SUCCESS_0013 = "Update content success!";//���Ӹ��³ɹ���";
	public static final String SUCCESS_0014 = "Create comment success!";//�ظ������ɹ���";
	public static final String SUCCESS_0015 = "Delete comment success!";//�ظ�ɾ���ɹ���";
	public static final String SUCCESS_0016 = "Delete content success!";//����ɾ���ɹ���";
	public static final String SUCCESS_0017 = "Create category success!";//��𴴽��ɹ���";
	public static final String SUCCESS_0018 = "Update category success!";//�����³ɹ���";
	public static final String SUCCESS_0019 = "Get content success!";//���ӻ�óɹ���";
	public static final String SUCCESS_0020 = "Delete category success!";//���ɾ���ɹ���";
	public static final String SUCCESS_0021 = "Delete document success!";//�ĵ�ɾ���ɹ���";
	public static final String SUCCESS_0022 = "Delete user success!";//�û�ɾ���ɹ���";
	public static final String SUCCESS_0023 = "Get category success!";//������ͳɹ���";
	public static final String SUCCESS_0024 = "Get user success!";//����û��ɹ���";
	public static final String SUCCESS_0025 = "Get comment success!";//������۳ɹ���";
	public static final String SUCCESS_0026 = "Update document success!";//�ĵ����³ɹ���";
	public static final String SUCCESS_0027 = "Update user success!";//�����û��ɹ���";
	public static final String SUCCESS_0028 = "Vote up success!";
	public static final String SUCCESS_0029 = "Vote down success!";
	public static final String SUCCESS_0030 = "Add view success!";
	public static final String SUCCESS_0031 = "Add rate success!";
	public static final String SUCCESS_0032 = "Sign in success!";
	
	public static final String FAIL_0001 = "Username or password format error!";//�û����������ʽ����";
	public static final String FAIL_0002 = "Email format error!";//�����ʽ����";
	public static final String FAIL_0003 = "Email is registered!";//�����ѱ�ע�ᣡ";
	public static final String FAIL_0004 = "Username is registered!";//�û����ѱ�ע�ᣡ";
	public static final String FAIL_0005 = "Username format error!";//�û�����ʽ����";
	public static final String FAIL_0006 = "Username or password error!";//�û������������";
	public static final String FAIL_0007 = "Password format error!";//�����ʽ����";
	public static final String FAIL_0008 = "User is not exist!";//�û������ڣ�";
	public static final String FAIL_0009 = "Update password failed!";//�޸�����ʧ�ܣ�";
	public static final String FAIL_0010 = "Email is activited!";//�ʼ��Ѽ��";
	public static final String FAIL_0011 = "Create email failed!";//�ʼ�����ʧ�ܣ�";
	public static final String FAIL_0012 = "Save email failed!";//�ʼ�����ʧ�ܣ�";
	public static final String FAIL_0013 = "Send email failed!";//�ʼ�����ʧ�ܣ�";
	public static final String FAIL_0014 = "Param format error!";//������ʽ����";
	public static final String FAIL_0015 = "Over activite time!";//��������ʱ�䣡";
	public static final String FAIL_0016 = "ID or key error!";//id��key����";
	public static final String FAIL_0017 = "Activate email failed!";//���伤��ʧ�ܣ�";
	public static final String FAIL_0018 = "Numbered Mode!";//�ݲ�֧�֣�";
	public static final String FAIL_0019 = "Please login!";//���¼��";
	public static final String FAIL_0020 = "Please activite email!";//���ȼ����ʼ���";
	public static final String FAIL_0021 = "User is not in reset password status!";//�û���������������״̬��";
	public static final String FAIL_0022 = "Over reset time!";//��������ʱ�䣡";
	public static final String FAIL_0023 = "Failed to get reset password role!";//�����������Ȩ��ʧ�ܣ�";
	public static final String FAIL_0024 = "Please login out and try again!";//���˳���½���ԣ�";
	public static final String FAIL_0025 = "User is not in reset status!";//�û���������������״̬��";
	public static final String FAIL_0026 = "Failed to reset password!";//��������ʧ�ܣ�";
	public static final String FAIL_0027 = "Get upload directory failed!";//��ȡ�ϴ��ļ���ʧ�ܣ�";
	public static final String FAIL_0028 = "Document type is not allowed!";//��֧�ֵ��ĵ���ʽ��";
	public static final String FAIL_0029 = "Upload document failed!";//�ϴ��ĵ�ʧ�ܣ�";
	public static final String FAIL_0030 = "Same document is exist!";//������ͬ�ĵ���";
	public static final String FAIL_0031 = "Document is not upload!";//�ĵ�δ�ϴ���";
	public static final String FAIL_0032 = "Get document infomation failed!";//��ȡ�ĵ���Ϣʧ�ܣ�";
	public static final String FAIL_0033 = "Add document failed!";//�ĵ����ʧ�ܣ�";
	public static final String FAIL_0034 = "Document is not exist!";//�ĵ������ڣ�";
	public static final String FAIL_0035 = "Point is not enough!";//���ֲ��㣡";
	public static final String FAIL_0036 = "Get document failed!";//����ĵ�ʧ�ܣ�";
	public static final String FAIL_0037 = "Deduct point failed!";//�۳�����ʧ�ܣ�";
	public static final String FAIL_0038 = "Convert document failed!";//ת���ĵ�ʧ�ܣ�";
	public static final String FAIL_0039 = "Create content failed!";//���Ӵ���ʧ�ܣ�";
	public static final String FAIL_0040 = "Content is not exist!";//���Ӳ����ڣ�";
	public static final String FAIL_0041 = "Permission denied!";//û��Ȩ�ޣ�";
	public static final String FAIL_0042 = "Update content failed!";//���Ӹ���ʧ�ܣ�";
	public static final String FAIL_0043 = "Comment is not exist!";//�ظ������ڣ�";
	public static final String FAIL_0044 = "Create comment failed!";//�ظ�����ʧ�ܣ�";
	public static final String FAIL_0045 = "Delete comment failed!";//�ظ�ɾ��ʧ�ܣ�";
	public static final String FAIL_0046 = "Delete content failed!";//����ɾ��ʧ�ܣ�";
	public static final String FAIL_0047 = "Create category failed!";//��𴴽�ʧ�ܣ�";
	public static final String FAIL_0048 = "Update category failed!";//������ʧ�ܣ�";
	public static final String FAIL_0049 = "Category is not exist!";//��𲻴��ڣ�";
	public static final String FAIL_0050 = "Delete category failed!";//���ɾ��ʧ�ܣ�";
	public static final String FAIL_0051 = "Delete document failed!";//�ĵ�ɾ��ʧ�ܣ�";
	public static final String FAIL_0052 = "Delete user failed!";//�û�ɾ��ʧ�ܣ�";
	public static final String FAIL_0053 = "Get category failed!";//�������ʧ�ܣ�";
	public static final String FAIL_0054 = "Get content failed!";//�������ʧ�ܣ�";
	public static final String FAIL_0055 = "Get comment failed!";//�������ʧ�ܣ�";
	public static final String FAIL_0056 = "Update document failed!";//�ĵ�����ʧ�ܣ�";
	public static final String FAIL_0057 = "Get user failed!";//����û�ʧ�ܣ�";
	public static final String FAIL_0058 = "Update user failed!";//�û�����ʧ�ܣ�";
	public static final String FAIL_0059 = "Vote up failed!";
	public static final String FAIL_0060 = "Vote down failed!";
	public static final String FAIL_0061 = "Add view failed!";
	public static final String FAIL_0062 = "Add rate failed!";
	public static final String FAIL_0063 = "Sign in failed!";//ǩ��ʧ�ܣ�";
	public static final String FAIL_0064 = "Operation too fast!";//�������죡";
}
