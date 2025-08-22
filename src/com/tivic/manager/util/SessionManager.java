package com.tivic.manager.util;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;

import com.tivic.manager.seg.Usuario;

import java.util.*;

public class SessionManager implements HttpSessionListener {

	private static ArrayList<HttpSession> sessions = new ArrayList<HttpSession>();

	public void sessionCreated(HttpSessionEvent se) {
		
		HttpSession session = se.getSession();
		
		//TIMEOUT 15m
		session.setMaxInactiveInterval(15*60); 
		
		sessions.add(session);
		
		LogUtils.debug("Nova sessao criada\n\t\t"+
							"Criado em: "+Util.formatDateTime(session.getCreationTime(), "dd/MM/yyyy HH:mm:ss") + "\n\t\t" + 
							"       ID: "+session.getId()+ "\n\t\t" +
							" Acessado: "+Util.formatDateTime(session.getLastAccessedTime(), "dd/MM/yyyy HH:mm:ss") +"\n\t\t" +
							"  Timeout: "+(session.getMaxInactiveInterval()/60)+ "m\n\t\t" +
							"  Context: "+session.getServletContext().getContextPath());
		
		if(se.getSession()!=null && se.getSession().getAttribute("usuario") instanceof Usuario)
			LogUtils.debug("Usuário: "+((Usuario)se.getSession().getAttribute("usuario")).getNmLogin());
		
		LogUtils.debug(getSize()+" session(s)");	
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		
		LogUtils.debug("Sessao removida\n\t\t"+
						"           ID: " + session.getId()+"\n\t\t"+
						"          Uso: " + ((session.getLastAccessedTime()-session.getCreationTime())/1000/60)+"min\n\t\t"+
						"Tempo de vida: " + ((System.currentTimeMillis()-session.getCreationTime())/1000/60) +"min");
		LogUtils.debug(getSize()+" session(s)");	
		
		if(se.getSession()!=null && se.getSession().getAttribute("usuario") instanceof Usuario)
			LogUtils.debug("Usuário saiu da sessão: "+((Usuario)se.getSession().getAttribute("usuario")).getNmLogin());
		
		sessions.remove(se.getSession());
	}

	public static int getActiveSessions() {
		return sessions.size();
	}
	
	public static ArrayList<HttpSession> getSessions(){
		return sessions;
	}
	
	public static HttpSession getSessionById(String id){
		for(int i=0; i<sessions.size(); i++)	{
			if(id.equals(((HttpSession)sessions.get(i)).getId()))
				return (HttpSession)sessions.get(i);
		}
		return null;
	}
	
	public static long getMemoryUsage(){
		try{
			return ObjectUtils.sizeOf(sessions);
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getSize(){
		try{
			return sessions.size();
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}