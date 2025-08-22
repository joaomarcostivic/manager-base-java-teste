package com.tivic.manager.psq;

import java.sql.Connection;
import java.sql.SQLException;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

@DestinationConfig(enabled = false)
public class TemaServices {

	public static int save(Tema tema){
		return save(tema, null);
	}
	public static int save(Tema tema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno;
			if(tema.getCdTema()==0){
				retorno = TemaDAO.insert(tema, connect);
			}
			else{
				retorno = TemaDAO.update(tema, connect);
				retorno = retorno>0?tema.getCdTema():retorno;
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TemaServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TemaServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}