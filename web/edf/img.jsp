<%@page import="java.io.*" %>
<%@page import="sol.util.MethodTrigger" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="net.sf.jmimemagic.*"%>
<%
	try {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache"); 
		
		String className = RequestUtilities.getParameterAsString(request, "className", "");
		String method = RequestUtilities.getParameterAsString(request, "method", "");
		
		if(className!=null && !className.equals("") && method!=null && !method.equals("")) {
			byte[] bytesImagem = null;
			Object obj = new MethodTrigger(request, response, null, true).getObjectResultCall();
			if (obj!=null && obj instanceof byte[])
				bytesImagem = (byte[])obj;
			if (bytesImagem != null) {
				out.clear();
				try {
					MagicMatch match;
					try	{
						match = Magic.getMagicMatch(bytesImagem);
					}
					finally	{
					}
					response.setContentType(match.getMimeType());
					OutputStream outResponse = response.getOutputStream();
					outResponse.write(bytesImagem);
					outResponse.flush();
					outResponse.close();
				}
				catch(Exception e) {
					System.out.println("img.jsp");
					e.printStackTrace(System.out);
				}
			}
		}
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>