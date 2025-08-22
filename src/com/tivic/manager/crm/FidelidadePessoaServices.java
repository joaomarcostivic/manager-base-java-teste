package com.tivic.manager.crm;

import java.sql.*;

import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

@DestinationConfig(enabled = false)
public class FidelidadePessoaServices {
	public static boolean exists(int cdFidelidade, int cdPessoa)	{
		return exists(cdFidelidade, cdPessoa, null);
	}
	
	public static boolean exists(int cdFidelidade, int cdPessoa, Connection connect)	{ 
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Se não passar nenhum programa de fidelidade verifica se existe um programa único
			if(cdFidelidade <= 0){
				ResultSet rs = connect.prepareStatement("SELECT * FROM crm_fidelidade").executeQuery();
				if(rs.next())
					cdFidelidade = rs.getInt("cd_fidelidade");
			}
			//
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade_pessoa WHERE cd_pessoa=? AND cd_fidelidade=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdFidelidade);
			return pstmt.executeQuery().next();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadePessoaServices.exists: " +  e);
			return false;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
