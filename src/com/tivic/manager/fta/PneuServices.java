package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;


public class PneuServices {
	public static int deleteByVeiculo(int cdVeiculo) {
		return deleteByVeiculo(cdVeiculo, null);
	}

	public static int deleteByVeiculo(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//deletando movimentacoes pneu
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_movimentacao_pneu WHERE cd_referencia=?");
			pstmt.setInt(1, cdVeiculo);
			pstmt.executeUpdate();
			
			//deletando pneus
			pstmt = connect.prepareStatement("DELETE FROM fta_pneu WHERE cd_componente_pneu IN (SELECT cd_componente FROM bpm_componente_referencia WHERE cd_referencia = ?)");
			pstmt.setInt(1, cdVeiculo);
			pstmt.executeUpdate();
			
			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PneuServices.deleteByVeiculo: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PneuServices.deleteByVeiculo: " +  e);
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