package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.util.Result;
import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class AtendenteServices {

	public static Result insert(Atendente objeto) {
		return insert(objeto, null);
	}

	public static Result insert(Atendente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			Atendente atendente = AtendenteDAO.get(objeto.getCdCentral(), objeto.getCdUsuario(), connect);
			if(atendente!=null)
				return new Result(connect.prepareStatement("UPDATE crm_atendente SET st_atendente = 1 " +
                        								   "WHERE cd_central = "+objeto.getCdCentral()+
                        								   "  AND cd_usuario = "+objeto.getCdUsuario()).executeUpdate());
			else
				return new Result(AtendenteDAO.insert(objeto, connect));

		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir atendente na central de atendimento!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result delete(int cdCentral, int cdUsuario) {
		return delete(cdCentral, cdUsuario, null);
	}

	public static Result delete(int cdCentral, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int ret = AtendenteDAO.delete(cdCentral, cdUsuario, connect);
			if(ret <= 0)
				return new Result(connect.prepareStatement("UPDATE crm_atendente SET st_atendente = 0 " +
						                                   "WHERE cd_central="+cdCentral+" AND cd_usuario="+cdUsuario).executeUpdate());
			else
				return new Result(ret);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir/inativar atendente!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int existeAtendente(String nmLogin) {
		return existeAtendente(nmLogin, null);
	}

	public static int existeAtendente(String nmLogin, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM CRM_ATENDENTE WHERE nm_login_im = ?");
			pstmt.setString(1, nmLogin);

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next())
				return 1;
			else
				return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getNmAtendente(int cdAtendente) {
		return getNmAtendente(cdAtendente, null);
	}

	public static String getNmAtendente(int cdAtendente, Connection connection) {
		boolean isConnectioNull = connection==null;
		try {
			if (isConnectioNull)
				connection = Conexao.conectar();

			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement("SELECT B.NM_PESSOA FROM seg_usuario A "+
																		    "LEFT OUTER JOIN grl_pessoa B ON (A.CD_PESSOA=B.CD_PESSOA) "+
																		    "WHERE cd_usuario = "+cdAtendente).executeQuery());
			if(rsm.next())
				return rsm.getString("NM_PESSOA");
			else
				return "";
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectioNull)
				Conexao.desconectar(connection);
		}
	}

}
