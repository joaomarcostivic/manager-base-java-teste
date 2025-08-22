package com.tivic.manager.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tivic.sol.connection.Conexao;


public class ComponenteReferenciaServices {
	
	public static final String[] situacaoComponente = {"Em uso", "Em manutenção interna", "Em manutenção externa", "Não localizado", "Baixado"};
	
	public static final int ST_EM_USO = 0;
	public static final int ST_EM_MANUTENCAO_INTERNA = 1;
	public static final int ST_EM_MANUTENCAO_EXTERNA = 2;
	public static final int ST_NAO_LOCALIZADO = 3;
	public static final int ST_BAIXADO = 4;
	
	public static int deleteByReferencia(int cdReferencia) {
		return deleteByReferencia(cdReferencia, null);
	}

	public static int deleteByReferencia(int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_componente_referencia WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.executeUpdate();
			
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaServices.deleteByReferencia: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaServices.deleteByReferencia: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
