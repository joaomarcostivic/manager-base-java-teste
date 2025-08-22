package com.tivic.manager.quark;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.util.MethodTrigger;
import sol.util.RequestUtilities;


@WebServlet("/QuarkServlet")
public class QuarkServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
	public QuarkServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
		try {

			if(QuarkManager.getClassCount()==0) {
				result(-1, "Nenhum serviço definido.", response);
				return;
			}
			
			String   id   		= RequestUtilities.getParameterAsString(request, "id", null);
			String   args   	= RequestUtilities.getParameterAsString(request, "args", "()");
			String   objects   	= RequestUtilities.getParameterAsString(request, "objects", null);
			String   execute   	= RequestUtilities.getParameterAsString(request, "execute", null);
			
			if(id==null) {
				result(-2, "Definição de chamada não especificada.", response);
				return;
			}
			
			String[] callIds = id.split("-");
			
			if(callIds.length<2) {
				result(-3, "Definição de chamada incompleta.", response);
				return;
			}
			
			String classId = callIds[0];
			
			/**
			 * PROCESSANDO CHAMADA
			 */
			String className = QuarkManager.getClassNameById(classId);
			
			if(className == null || className.equals("")) {
				result(-4, "Classe de id '" + className + "' não encontrada no mapeamento.", response);
				return;
			}
			
			String methodName = QuarkManager.getMethodNameById(id); 
			
			if(methodName == null || methodName.equals("")) {
				result(-5, "Metodo de id '" + methodName + "' não encontrado no mapeamento.", response);
				return;
			}
			
			request.setAttribute("className", className);
			request.setAttribute("method", methodName + args);
			request.setAttribute("objects", objects);
			request.setAttribute("execute", execute);
			
			String chamada = "\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] QuarkServlet";
			chamada += "\n     ID Chamada: " + id;
			chamada += "\nClasse e método: " + className + "." + methodName;
			chamada += "\n     Argumentos: " + args;
			
			LogUtils.debug(chamada);
			
   		 	new MethodTrigger(request, response);
		} catch (Exception e) {  
            e.printStackTrace();  
        }
		
	}

	private void result(int code, String message, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.print("{\"code\": \""+code+"\", \"message\": \""+message+"\"}");
	}
}
