package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class MultaServices {

	public static int save(Multa multa){
		return save(multa, null);
	}

	public static int save(Multa multa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 1;
			if(multa.getCdMulta()==0){
				retorno = MultaDAO.insert(multa, connect);
				if(retorno>0){
					multa.setCdMulta(retorno);
					multa.setIdInfracao(new DecimalFormat("00000").format(retorno));
				}
			}

			if(retorno>0)
				retorno = MultaDAO.update(multa, connect);

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return multa.getCdMulta();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MultaServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM fta_multa A " +
						   "LEFT OUTER JOIN fta_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " +
						   "LEFT OUTER JOIN fta_modelo_veiculo C ON (B.cd_modelo=C.cd_modelo) " +
						   "LEFT OUTER JOIN  bpm_marca D ON (C.cd_marca=D.cd_marca) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	public static ResultSetMap get(int cdMulta) {
		return get(cdMulta, null);
	}

	public static ResultSetMap get(int cdMulta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_multa A " +
						   "LEFT OUTER JOIN fta_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " +
						   "LEFT OUTER JOIN fta_modelo_veiculo C ON (B.cd_modelo=C.cd_modelo) " +
						   "LEFT OUTER JOIN  bpm_marca D ON (C.cd_marca=D.cd_marca) WHERE A.cd_multa=?");
			pstmt.setInt(1, cdMulta);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MultaServices.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MultaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}