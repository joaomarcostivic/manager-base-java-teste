<%@page import="java.net.URLEncoder, java.io.OutputStream, java.io.ByteArrayInputStream, sol.util.MethodTrigger, sol.util.RequestUtilities" %><% 
	try {
		String nmArquivo = RequestUtilities.getParameterAsString(request, "nmArquivo", "");
		
		nmArquivo = URLEncoder.encode(nmArquivo, "ISO-8859-1");
		
		byte[] bytes = null;
		Object obj = new MethodTrigger(request, response, null, true).getObjectResultCall();
		if (obj!=null && obj instanceof byte[])
			bytes = (byte[])obj;
		if (bytes != null) {
			out.clear();
			response.setContentType("application/octet-stream"); 
			response.setHeader("Content-Disposition","attachment; filename="+nmArquivo); 
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);  
			OutputStream rout = response.getOutputStream();  
			byte[] buffer = new byte[2048];//Buffer para leitura  
			int lidos;  
			while ((lidos = in.read(buffer))!= -1){  
				rout.write(buffer,0,lidos);//Transfere
				rout.flush();  
			}  
			in.close();  
			out.close();//Fecha streams  
		}
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>