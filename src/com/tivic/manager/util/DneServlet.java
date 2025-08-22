package com.tivic.manager.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import sol.util.RequestUtilities;


@WebServlet("/DneServlet")
public class DneServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    public DneServlet() {
        super();

    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			response.setContentType("application/json");
			
			String cep = RequestUtilities.getParameterAsString(request, "cep", "");
			String format = RequestUtilities.getParameterAsString(request, "format", "");
			
			ArrayList<HashMap<String, String>> webServices = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> wsList = new HashMap<String, String>();
			wsList.put("ViaCep", "https://www.viacep.com.br/ws/%1$s/%2$s/");
//			wsList.put("Postmon", "http://api.postmon.com.br/cep/%1$s?format=%2$s");
			webServices.add(wsList);
				
			PrintWriter out = response.getWriter();
			
			JSONObject json = new JSONObject();
			for (HashMap<String, String> wss : webServices){
				for (String ws : wss.values()){
					String r = DneUtilities.findEnderecoByCep(cep, format, ws);
					if(r != null){
						json = new JSONObject(r);
						break;
					}
				}
			}
		
			out.print(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
