package com.tivic.manager.seg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sol.util.Jso;
import sol.util.RequestUtilities;

public class LoadPermissions extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/html");

			Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
			String idForm = RequestUtilities.getParameterAsString(request, "idForm", "");


			if(usuario==null){
				//usuario nao logado
				response.getWriter().println("{error: -1, message: 'Usuario nao logou no sistema'}");
			}
			else if(idForm==null || idForm.trim().equals("")){
				response.getWriter().println("{error: -2, message: 'Formulario nao encontrado'}");
			}
			else{
				response.getWriter().println(Jso.getStream(FormularioServices.getPermissoesObjetos(usuario.getCdUsuario(), idForm, null)));
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
	}

}
