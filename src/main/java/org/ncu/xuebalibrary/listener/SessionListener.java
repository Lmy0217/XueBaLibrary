package org.ncu.xuebalibrary.listener;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	
	private static HashMap<Long, HttpSession> userSession = new HashMap<Long, HttpSession>();

	public void sessionCreated(HttpSessionEvent arg0) {

	}

	public void sessionDestroyed(HttpSessionEvent arg0) {

	}

	public static boolean add(HttpSession session) {
		
		if(session == null) return false;
		
		Object obj_id = session.getAttribute("id");
		if(obj_id == null) return false;
		
		HttpSession oldsession = userSession.get((Long)obj_id);
		if(oldsession != null) oldsession.invalidate();
		userSession.put((Long)obj_id, session);
		
		return true;
	}
	
	public static boolean remove(HttpSession session) {
		
		if(session == null) return false;
		
		Object obj_id = session.getAttribute("id");
		if(obj_id != null) userSession.remove((Long)obj_id);
		session.invalidate();
		
		return true;
	}
}
