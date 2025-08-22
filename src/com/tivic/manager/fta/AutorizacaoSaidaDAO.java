package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AutorizacaoSaidaDAO{

	public static int insert(AutorizacaoSaida objeto) {
		return insert(objeto, null);
	}

	public static int insert(AutorizacaoSaida objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_autorizacao_saida", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAutorizacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_autorizacao_saida (cd_autorizacao,"+
			                                  "cd_veiculo,"+
			                                  "cd_viagem,"+
			                                  "cd_manutencao,"+
			                                  "cd_responsavel,"+
			                                  "qt_hodometro_saida,"+
			                                  "qt_hodometro_chegada,"+
			                                  "dt_autorizacao,"+
			                                  "dt_saida,"+
			                                  "tp_motivo,"+
			                                  "txt_autorizacao,"+
			                                  "dt_chegada) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdViagem());
			if(objeto.getCdManutencao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdManutencao());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			pstmt.setFloat(6,objeto.getQtHodometroSaida());
			pstmt.setFloat(7,objeto.getQtHodometroChegada());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			if(objeto.getDtSaida()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setInt(10,objeto.getTpMotivo());
			pstmt.setString(11,objeto.getTxtAutorizacao());
			if(objeto.getDtChegada()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtChegada().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AutorizacaoSaida objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AutorizacaoSaida objeto, int cdAutorizacaoOld) {
		return update(objeto, cdAutorizacaoOld, null);
	}

	public static int update(AutorizacaoSaida objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AutorizacaoSaida objeto, int cdAutorizacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_autorizacao_saida SET cd_autorizacao=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_viagem=?,"+
												      		   "cd_manutencao=?,"+
												      		   "cd_responsavel=?,"+
												      		   "qt_hodometro_saida=?,"+
												      		   "qt_hodometro_chegada=?,"+
												      		   "dt_autorizacao=?,"+
												      		   "dt_saida=?,"+
												      		   "tp_motivo=?,"+
												      		   "txt_autorizacao=?,"+
												      		   "dt_chegada=? WHERE cd_autorizacao=?");
			pstmt.setInt(1,objeto.getCdAutorizacao());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdViagem());
			if(objeto.getCdManutencao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdManutencao());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			pstmt.setFloat(6,objeto.getQtHodometroSaida());
			pstmt.setFloat(7,objeto.getQtHodometroChegada());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			if(objeto.getDtSaida()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setInt(10,objeto.getTpMotivo());
			pstmt.setString(11,objeto.getTxtAutorizacao());
			if(objeto.getDtChegada()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtChegada().getTimeInMillis()));
			pstmt.setInt(13, cdAutorizacaoOld!=0 ? cdAutorizacaoOld : objeto.getCdAutorizacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAutorizacao) {
		return delete(cdAutorizacao, null);
	}

	public static int delete(int cdAutorizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_autorizacao_saida WHERE cd_autorizacao=?");
			pstmt.setInt(1, cdAutorizacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AutorizacaoSaida get(int cdAutorizacao) {
		return get(cdAutorizacao, null);
	}

	public static AutorizacaoSaida get(int cdAutorizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_autorizacao_saida WHERE cd_autorizacao=?");
			pstmt.setInt(1, cdAutorizacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AutorizacaoSaida(rs.getInt("cd_autorizacao"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_viagem"),
						rs.getInt("cd_manutencao"),
						rs.getInt("cd_responsavel"),
						rs.getFloat("qt_hodometro_saida"),
						rs.getFloat("qt_hodometro_chegada"),
						(rs.getTimestamp("dt_autorizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_autorizacao").getTime()),
						(rs.getTimestamp("dt_saida")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saida").getTime()),
						rs.getInt("tp_motivo"),
						rs.getString("txt_autorizacao"),
						(rs.getTimestamp("dt_chegada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_chegada").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_autorizacao_saida");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AutorizacaoSaidaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_autorizacao_saida", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
