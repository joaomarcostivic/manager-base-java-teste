<%@page import="java.util.*" %>
<%@page import="java.io.*" %>
<%@page import="sol.util.MethodTrigger" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="net.sf.jmimemagic.*"%>
<%
	try {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache"); 

		byte[] bytesArquivo = null;
		Object obj = new MethodTrigger(request, response, null, true).getObjectResultCall();
		if (obj!=null && obj instanceof byte[])
			bytesArquivo = (byte[])obj;
		out.clear();
		try {
			Magic parser = new Magic();
			MagicMatch match = null;
			String dsExtension = "";
			String mimeType = null;
			try {
				match = parser.getMagicMatch(bytesArquivo);
				dsExtension = "." + match.getExtension();
			}
			catch(Exception e1) {
				dsExtension = "";
			}
			try {
				match = parser.getMagicMatch(bytesArquivo);
				mimeType = match.getMimeType();
			}
			catch(Exception e1) {
				dsExtension = "";
			}
			response.setContentType(mimeType==null ? "application/octet-stream" : mimeType); 
			response.setHeader("Content-Disposition","attachment; filename=arquivo" + dsExtension + ";");
			OutputStream outResponse = response.getOutputStream();
			outResponse.write(bytesArquivo);
			outResponse.flush();
		}
		catch(Exception e) {
			//e.printStackTrace(System.out);
		}
	}
	catch(Exception e) {
		//e.printStackTrace(System.out);
	}
%>