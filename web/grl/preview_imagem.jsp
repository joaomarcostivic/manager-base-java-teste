<%@page import="java.util.*" %>
<%@page import="java.io.*" %>
<%@page import="com.tivic.manager.grl.*" %>
<%@page import="sol.util.MethodTrigger" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="net.sf.jmimemagic.*"%>
<%
	try {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache"); 
		int tpLocalizacao = RequestUtilities.getParameterAsInteger(request, "tpLocalizacao", 0);
		int lgDefaultInBlank = RequestUtilities.getParameterAsInteger(request, "lgDefaultInBlank", 0);
		String className = RequestUtilities.getParameterAsString(request, "className", "");		
		String method = RequestUtilities.getParameterAsString(request, "method", "");
		String idSession = RequestUtilities.getParameterAsString(request, "idSession", "");
		if (lgDefaultInBlank==0) {
			byte[] bytesImagem = null;
			if (tpLocalizacao==0) {
				bytesImagem = (byte[])session.getAttribute(idSession);
			}
			else if (tpLocalizacao==1) {
				Object obj = new MethodTrigger(request, response, null, true).getObjectResultCall();
				if (obj!=null && obj instanceof byte[]) {
					bytesImagem = (byte[])obj;
					session.setAttribute(idSession, bytesImagem);
				}
			}
			if (bytesImagem != null) {
				out.clear();
				try {
					Magic parser = new Magic();
					MagicMatch match = parser.getMagicMatch(bytesImagem);
					response.setContentType(match.getMimeType());
					OutputStream outResponse = response.getOutputStream();
					outResponse.write(bytesImagem);
					outResponse.flush();
				}
				catch(Exception e) {
					e.printStackTrace(System.out);
				}
			}
		}
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>