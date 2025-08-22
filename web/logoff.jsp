<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.security.StatusPermissionActionUser"%>
<%@page import="sol.security.User"%>
<%@page import="java.util.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%
	String nmUsuario  = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");
	Usuario usuario = (Usuario) session.getAttribute("usuario");
	int cdUsuario = usuario != null ? usuario.getCdUsuario() : 0;
	String nmOperador = "";
	String nmLogin = "";
	String result = "";
	
	if (session.getAttribute("usuario") != null) {
		try {
			session.removeAttribute("usuario");
			String message = "Você foi deslogado do sistema com sucesso!"; 
			result  = "{code: 1, message: \"" + message + "\"}";
		} catch (Exception e) {
			String message = "Houve um problema ao efetuar sua saída do sistema! " + e.getMessage(); 
			result  = "{code: -1, message: \"" + message + "\"}";
		}
	}	
	out.print(result); 
%>