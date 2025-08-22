package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FidelidadeMovimentoDAO{

	public static int insert(FidelidadeMovimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FidelidadeMovimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_fidelidade_movimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMovimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_fidelidade_movimento (cd_movimento,"+
			                                  "cd_pessoa,"+
			                                  "cd_fidelidade,"+
			                                  "dt_movimento,"+
			                                  "txt_historico,"+
			                                  "cd_tipo_movimento,"+
			                                  "cd_ocorrencia,"+
			                                  "dt_validade,"+
			                                  "vl_movimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdFidelidade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFidelidade());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtHistorico());
			if(objeto.getCdTipoMovimento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoMovimento());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdOcorrencia());
			if(objeto.getDtValidade()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setFloat(9,objeto.getVlMovimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FidelidadeMovimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FidelidadeMovimento objeto, int cdMovimentoOld) {
		return update(objeto, cdMovimentoOld, null);
	}

	public static int update(FidelidadeMovimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FidelidadeMovimento objeto, int cdMovimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_fidelidade_movimento SET cd_movimento=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_fidelidade=?,"+
												      		   "dt_movimento=?,"+
												      		   "txt_historico=?,"+
												      		   "cd_tipo_movimento=?,"+
												      		   "cd_ocorrencia=?,"+
												      		   "dt_validade=?,"+
												      		   "vl_movimento=? WHERE cd_movimento=?");
			pstmt.setInt(1,objeto.getCdMovimento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdFidelidade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFidelidade());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtHistorico());
			if(objeto.getCdTipoMovimento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoMovimento());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdOcorrencia());
			if(objeto.getDtValidade()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setFloat(9,objeto.getVlMovimento());
			pstmt.setInt(10, cdMovimentoOld!=0 ? cdMovimentoOld : objeto.getCdMovimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMovimento) {
		return delete(cdMovimento, null);
	}

	public static int delete(int cdMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_fidelidade_movimento WHERE cd_movimento=?");
			pstmt.setInt(1, cdMovimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FidelidadeMovimento get(int cdMovimento) {
		return get(cdMovimento, null);
	}

	public static FidelidadeMovimento get(int cdMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade_movimento WHERE cd_movimento=?");
			pstmt.setInt(1, cdMovimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FidelidadeMovimento(rs.getInt("cd_movimento"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_fidelidade"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getString("txt_historico"),
						rs.getInt("cd_tipo_movimento"),
						rs.getInt("cd_ocorrencia"),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						rs.getFloat("vl_movimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade_movimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeMovimentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_fidelidade_movimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
