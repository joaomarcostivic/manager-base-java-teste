package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProgramaFaturaPlanoDAO{

	public static int insert(ProgramaFaturaPlano objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProgramaFaturaPlano objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_programa_fatura_plano (cd_programa_fatura,"+
			                                  "cd_plano_pagamento) VALUES (?, ?)");
			if(objeto.getCdProgramaFatura()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProgramaFatura());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPlanoPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProgramaFaturaPlano objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProgramaFaturaPlano objeto, int cdProgramaFaturaOld, int cdPlanoPagamentoOld) {
		return update(objeto, cdProgramaFaturaOld, cdPlanoPagamentoOld, null);
	}

	public static int update(ProgramaFaturaPlano objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProgramaFaturaPlano objeto, int cdProgramaFaturaOld, int cdPlanoPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_programa_fatura_plano SET cd_programa_fatura=?,"+
												      		   "cd_plano_pagamento=? WHERE cd_programa_fatura=? AND cd_plano_pagamento=?");
			pstmt.setInt(1,objeto.getCdProgramaFatura());
			pstmt.setInt(2,objeto.getCdPlanoPagamento());
			pstmt.setInt(3, cdProgramaFaturaOld!=0 ? cdProgramaFaturaOld : objeto.getCdProgramaFatura());
			pstmt.setInt(4, cdPlanoPagamentoOld!=0 ? cdPlanoPagamentoOld : objeto.getCdPlanoPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProgramaFatura, int cdPlanoPagamento) {
		return delete(cdProgramaFatura, cdPlanoPagamento, null);
	}

	public static int delete(int cdProgramaFatura, int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_programa_fatura_plano WHERE cd_programa_fatura=? AND cd_plano_pagamento=?");
			pstmt.setInt(1, cdProgramaFatura);
			pstmt.setInt(2, cdPlanoPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProgramaFaturaPlano get(int cdProgramaFatura, int cdPlanoPagamento) {
		return get(cdProgramaFatura, cdPlanoPagamento, null);
	}

	public static ProgramaFaturaPlano get(int cdProgramaFatura, int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_programa_fatura_plano WHERE cd_programa_fatura=? AND cd_plano_pagamento=?");
			pstmt.setInt(1, cdProgramaFatura);
			pstmt.setInt(2, cdPlanoPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProgramaFaturaPlano(rs.getInt("cd_programa_fatura"),
						rs.getInt("cd_plano_pagamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_programa_fatura_plano");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProgramaFaturaPlano> getList() {
		return getList(null);
	}

	public static ArrayList<ProgramaFaturaPlano> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProgramaFaturaPlano> list = new ArrayList<ProgramaFaturaPlano>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProgramaFaturaPlano obj = ProgramaFaturaPlanoDAO.get(rsm.getInt("cd_programa_fatura"), rsm.getInt("cd_plano_pagamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaPlanoDAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_programa_fatura_plano", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
