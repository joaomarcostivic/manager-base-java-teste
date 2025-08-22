package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpSession;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.util.Util;

import sol.util.Result;

public class UsuarioServices {

	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha) {
		return com.tivic.manager.seg.UsuarioServices.loginAndUpdateSession(session, nmLogin, nmSenha, true);
	}
	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			// remove atributos da sessao
			session.removeAttribute("usuario");
			session.removeAttribute("user");
			session.removeAttribute("objectsSystem");
			session.removeAttribute("permissionsActionsUser");
			
			int cdUsuario = login(nmLogin.toUpperCase(), nmSenha.toUpperCase(), connection);
			if (cdUsuario<=0)
				return new Result(-1, "Login ou senha inválidos!");
			else {
				ResultSet rs = connection.prepareStatement("SELECT * FROM seg_usuario WHERE cd_usuario = "+cdUsuario).executeQuery();
				int tpUsuario = 0;
				if(rs.next())
					tpUsuario = rs.getInt("tp_usuario");
				Usuario usuario = new com.tivic.manager.seg.Usuario(cdUsuario, 0 /*cdPessoa*/, 0 /*cdPerguntaSecreta*/, nmLogin, null /*nmSenha*/,
															 tpUsuario, null /*nmRespostaSecreta*/, 1 /*stUsuario*/);
				session.setAttribute("usuario", usuario);
				return new Result(cdUsuario, "Login efetuado com sucesso!", "usuario", usuario);
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(0, "Erro ao tentar efetuar login!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int login(String nmLogin, String nmSenha) {
		return login(nmLogin, nmSenha, null);
	}

	public static int login(String nmLogin, String nmSenha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM SEG_USUARIO " +
					"WHERE NM_LOGIN=? ");
			pstmt.setString(1, nmLogin.toUpperCase());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				if(nmSenha.equals(rs.getString("NM_SENHA")))
					return rs.getInt("CD_USUARIO");
				else
					return -2;
			}
			else{
				return -1;
			}
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			java.lang.System.err.println("Erro! UsuarioDAO.get: " + e);
			return -3;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
