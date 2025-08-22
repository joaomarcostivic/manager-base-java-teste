<%@page import="java.io.*"%>
<%@page import="sol.util.MethodTrigger"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="net.sf.jmimemagic.*"%>
<%
	try {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache"); 
		int tpLocalizacao    = RequestUtilities.getParameterAsInteger(request, "tpLocalizacao", 0);
		String time 		 = RequestUtilities.getParameterAsString(request, "time", "0");
		int lgDefaultInBlank = RequestUtilities.getParameterAsInteger(request, "lgDefaultInBlank", 0);
		String idSession     = RequestUtilities.getParameterAsString(request, "idSession", "");
		if (lgDefaultInBlank == 0) {
			byte[] bytesImagem = null;
			// Apenas pega informações da sessão e joga no request
			if (tpLocalizacao==2) {
				tpLocalizacao = 1;
				request.setAttribute("className", session.getAttribute("className_"+time));
				request.setAttribute("objects", session.getAttribute("objects_"+time));
				request.setAttribute("execute", session.getAttribute("execute_"+time));
				request.setAttribute("method", session.getAttribute("method_"+time));
				session.removeAttribute("className_"+time);
				session.removeAttribute("objects_"+time);
				session.removeAttribute("execute_"+time);
				session.removeAttribute("method_"+time);
			}
			// Imagem guardada na sessão
			if (tpLocalizacao==0)
				bytesImagem = (byte[])session.getAttribute(idSession);
			
			else if (tpLocalizacao==1) {
				Object obj = new MethodTrigger(request, response, null, true).getObjectResultCall();
				if (obj != null && obj instanceof byte[]) {
					bytesImagem = (byte[])obj;
					if(!idSession.equals(""))
						session.setAttribute(idSession, bytesImagem);
				}
			}
			// Após recuperar a imagem tenta escrever no out
			if (bytesImagem != null) {
				out.clear();
				try {
					MagicMatch match;
					try	{
						match = Magic.getMagicMatch(bytesImagem);
						response.setContentType(match.getMimeType());
					}
					catch(Exception e){
						e.printStackTrace(System.out);
						response.setContentType("image/jpeg");
					}
					OutputStream outResponse = response.getOutputStream();
					outResponse.write(bytesImagem);
					outResponse.flush();
					outResponse.close();
				}
				catch(Exception e) {
					System.out.println("erro no preview_imagem.jsp");
					e.printStackTrace(System.out);
				}
			}
		}
	}
	catch(Exception e) {
		System.out.println("erro no preview_imagem.jsp: Exception");
		e.printStackTrace(System.out);
	}
%>