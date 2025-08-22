package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tivic.sol.connection.Conexao;

public class ViagemPessoaServices {

	public static final int TP_OUTROS 	   = 0;
	public static final int TP_RESPONSAVEL = 1;
	public static final int TP_DIGITADOR   = 2;
	public static final int TP_MOTORISTA   = 3;
	
	public static int deleteAllByViagem(int cdViagem){
		return deleteAllByViagem(cdViagem, null);
	}
	
	public static int deleteAllByViagem(int cdViagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_viagem_pessoa WHERE cd_viagem = ?");
			pstmt.setInt(1, cdViagem);
			
			return pstmt.executeUpdate();
		}
		
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemPessoaServices.save: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
