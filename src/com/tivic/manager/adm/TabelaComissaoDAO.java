package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TabelaComissaoDAO{

	public static int insert(TabelaComissao objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaComissao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_tabela_comissao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaComissao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tabela_comissao (cd_tabela_comissao,"+
			                                  "nm_tabela_comissao,"+
			                                  "dt_inicio_validade,"+
			                                  "dt_final_validade,"+
			                                  "id_tabela_comissao,"+
			                                  "lg_progressiva,"+
			                                  "vl_aplicacao,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTabelaComissao());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdTabelaComissao());
			pstmt.setInt(6,objeto.getLgProgressiva());
			pstmt.setFloat(7,objeto.getVlAplicacao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaComissao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TabelaComissao objeto, int cdTabelaComissaoOld) {
		return update(objeto, cdTabelaComissaoOld, null);
	}

	public static int update(TabelaComissao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TabelaComissao objeto, int cdTabelaComissaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tabela_comissao SET cd_tabela_comissao=?,"+
												      		   "nm_tabela_comissao=?,"+
												      		   "dt_inicio_validade=?,"+
												      		   "dt_final_validade=?,"+
												      		   "id_tabela_comissao=?,"+
												      		   "lg_progressiva=?,"+
												      		   "vl_aplicacao=?,"+
												      		   "cd_empresa=? WHERE cd_tabela_comissao=?");
			pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setString(2,objeto.getNmTabelaComissao());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdTabelaComissao());
			pstmt.setInt(6,objeto.getLgProgressiva());
			pstmt.setFloat(7,objeto.getVlAplicacao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEmpresa());
			pstmt.setInt(9, cdTabelaComissaoOld!=0 ? cdTabelaComissaoOld : objeto.getCdTabelaComissao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaComissao) {
		return delete(cdTabelaComissao, null);
	}

	public static int delete(int cdTabelaComissao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tabela_comissao WHERE cd_tabela_comissao=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaComissao get(int cdTabelaComissao) {
		return get(cdTabelaComissao, null);
	}

	public static TabelaComissao get(int cdTabelaComissao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao WHERE cd_tabela_comissao=?");
			pstmt.setInt(1, cdTabelaComissao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaComissao(rs.getInt("cd_tabela_comissao"),
						rs.getString("nm_tabela_comissao"),
						(rs.getTimestamp("dt_inicio_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_validade").getTime()),
						(rs.getTimestamp("dt_final_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_validade").getTime()),
						rs.getString("id_tabela_comissao"),
						rs.getInt("lg_progressiva"),
						rs.getFloat("vl_aplicacao"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tabela_comissao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
