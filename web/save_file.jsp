<%@page import="java.util.Enumeration"%>
<%@page import="java.io.*"%>
<%@page import="sol.util.MethodTrigger"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="net.sf.jmimemagic.*"%>
<%
	try	{
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		String content  = sol.util.RequestUtilities.getParameterAsString(request, "content", "");
		String filename = sol.util.RequestUtilities.getParameterAsString(request, "filename", "");
		String idText	= sol.util.RequestUtilities.getParameterAsString(request, "idText", "");
		//
		int tpLocalizacao    = RequestUtilities.getParameterAsInteger(request, "tpLocalizacao", 0);
		if(!idText.equals("")){
			Enumeration e = (Enumeration)session.getAttributeNames();
			while(e.hasMoreElements()){
				Object o = e.nextElement();
			}
			content = (String) session.getAttribute(idText);
			System.out.println("content = " + content);
		}
		else{
			// Executar comando
			if (tpLocalizacao==1) {
				Object obj = new MethodTrigger(request, response, null, true).getObjectResultCall();
				if (obj != null && obj instanceof String)
					content = (String)obj;
			}
		}
		//
		out.clear();
		response.setContentType("application/octet-stream"); 
		response.setHeader("Content-Disposition","attachment; filename="+filename); 
		out.write(content);
		
		if(!idText.equals("")){
			session.removeAttribute(idText);
		}
	}
	catch(Exception e)	{
			e.printStackTrace(System.out);
	}
%>