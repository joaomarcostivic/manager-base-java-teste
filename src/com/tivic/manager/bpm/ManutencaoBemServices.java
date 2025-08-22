package com.tivic.manager.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;


public class ManutencaoBemServices {
	
	public static int deleteByReferencia(int cdReferencia) {
		return deleteByReferencia(cdReferencia, null);
	}

	public static int deleteByReferencia(int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//deletando manutencoes componente
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_manutencao_componente WHERE cd_manutencao IN (SELECT cd_manutencao FROM bpm_manutencao_bem WHERE cd_referencia = ?)");
			pstmt.setInt(1, cdReferencia);
			pstmt.executeUpdate();
			
			pstmt = connect.prepareStatement("DELETE FROM bpm_manutencao_bem WHERE cd_referencia = ?");
			pstmt.setInt(1, cdReferencia);
			pstmt.executeUpdate();
			
			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoBemServices.deleteByReferencia: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ManutencaoBemServices.deleteByReferencia: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}