package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sol.dao.ResultSetMap;


import com.tivic.sol.connection.Conexao;

public class FaixaRendaServices {

	public static final int FORMAL   = 0;
	public static final int INFORMAL = 1;
	public static final String[] tipoRenda = {"Formal", "Informal"};
	
	public static int insert(FaixaRenda objeto) {
		return insert(objeto, null);
	}

	public static int insert(FaixaRenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			objeto.setTpRenda(FORMAL);
			
			int code = FaixaRendaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			objeto.setCdFaixaRenda(0);
			objeto.setTpRenda(INFORMAL);
			
			code = FaixaRendaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			connect.commit();
			
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_faixa_renda");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("CL_FAIXA_RENDA", rsm.getString("NM_FAIXA_RENDA") + " - " + tipoRenda[rsm.getInt("TP_RENDA")]); 
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FaixaRendaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

}
