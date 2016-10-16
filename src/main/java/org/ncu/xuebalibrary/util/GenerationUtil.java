package org.ncu.xuebalibrary.util;

public class GenerationUtil {

	private static long contentId = 1;
	private static long contentAllocationSize = 2;
	
	private static long commentId = 2;
	private static long commentAllocationSize = 2;
	
	public static synchronized long getContentId() {
		long result = contentId;
		contentId += contentAllocationSize;
		return result;
	}
	
	public static synchronized long getCommentId() {
		long result = commentId;
		commentId += commentAllocationSize;
		return result;
	}
}
