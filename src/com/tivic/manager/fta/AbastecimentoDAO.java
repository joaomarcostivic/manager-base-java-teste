package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AbastecimentoDAO{

	public static int insert(Abastecimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Abastecimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_abastecimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAbastecimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_abastecimento (cd_abastecimento,"+
			                                  "cd_conta_pagar,"+
			                                  "cd_veiculo,"+
			                                  "qt_hodometro,"+
			                                  "lg_completo,"+
			                                  "st_abastecimento,"+
			                                  "qt_litros_autorizada,"+
			                                  "qt_litros_abastecida,"+
			                                  "vl_autorizado,"+
			                                  "vl_abastecido,"+
			                                  "dt_autorizacao,"+
			                                  "dt_abastecimento,"+
			                                  "vl_litro_combustivel,"+
			                                  "tp_combustivel,"+
			                                  "cd_responsavel,"+
			                                  "nr_autorizacao,"+
			                                  "qt_vias_impressas,"+
			                                  "cd_fornecedor,"+
			                                  "cd_autorizacao,"+
			                                  "tp_abastecimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			pstmt.setFloat(4,objeto.getQtHodometro());
			pstmt.setInt(5,objeto.getLgCompleto());
			pstmt.setInt(6,objeto.getStAbastecimento());
			pstmt.setFloat(7,objeto.getQtLitrosAutorizada());
			pstmt.setFloat(8,objeto.getQtLitrosAbastecida());
			pstmt.setFloat(9,objeto.getVlAutorizado());
			pstmt.setFloat(10,objeto.getVlAbastecido());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			if(objeto.getDtAbastecimento()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtAbastecimento().getTimeInMillis()));
			pstmt.setFloat(13,objeto.getVlLitroCombustivel());
			pstmt.setInt(14,objeto.getTpCombustivel());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdResponsavel());
			pstmt.setString(16,objeto.getNrAutorizacao());
			pstmt.setInt(17,objeto.getQtViasImpressas());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdFornecedor());
			if(objeto.getCdAutorizacao()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdAutorizacao());
			pstmt.setInt(20,objeto.getTpAbastecimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Abastecimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Abastecimento objeto, int cdAbastecimentoOld) {
		return update(objeto, cdAbastecimentoOld, null);
	}

	public static int update(Abastecimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Abastecimento objeto, int cdAbastecimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_abastecimento SET cd_abastecimento=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "cd_veiculo=?,"+
												      		   "qt_hodometro=?,"+
												      		   "lg_completo=?,"+
												      		   "st_abastecimento=?,"+
												      		   "qt_litros_autorizada=?,"+
												      		   "qt_litros_abastecida=?,"+
												      		   "vl_autorizado=?,"+
												      		   "vl_abastecido=?,"+
												      		   "dt_autorizacao=?,"+
												      		   "dt_abastecimento=?,"+
												      		   "vl_litro_combustivel=?,"+
												      		   "tp_combustivel=?,"+
												      		   "cd_responsavel=?,"+
												      		   "nr_autorizacao=?,"+
												      		   "qt_vias_impressas=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "cd_autorizacao=?,"+
												      		   "tp_abastecimento=? WHERE cd_abastecimento=?");
			pstmt.setInt(1,objeto.getCdAbastecimento());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			pstmt.setFloat(4,objeto.getQtHodometro());
			pstmt.setInt(5,objeto.getLgCompleto());
			pstmt.setInt(6,objeto.getStAbastecimento());
			pstmt.setFloat(7,objeto.getQtLitrosAutorizada());
			pstmt.setFloat(8,objeto.getQtLitrosAbastecida());
			pstmt.setFloat(9,objeto.getVlAutorizado());
			pstmt.setFloat(10,objeto.getVlAbastecido());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			if(objeto.getDtAbastecimento()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtAbastecimento().getTimeInMillis()));
			pstmt.setFloat(13,objeto.getVlLitroCombustivel());
			pstmt.setInt(14,objeto.getTpCombustivel());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdResponsavel());
			pstmt.setString(16,objeto.getNrAutorizacao());
			pstmt.setInt(17,objeto.getQtViasImpressas());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdFornecedor());
			if(objeto.getCdAutorizacao()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdAutorizacao());
			pstmt.setInt(20,objeto.getTpAbastecimento());
			pstmt.setInt(21, cdAbastecimentoOld!=0 ? cdAbastecimentoOld : objeto.getCdAbastecimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAbastecimento) {
		return delete(cdAbastecimento, null);
	}

	public static int delete(int cdAbastecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_abastecimento WHERE cd_abastecimento=?");
			pstmt.setInt(1, cdAbastecimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Abastecimento get(int cdAbastecimento) {
		return get(cdAbastecimento, null);
	}

	public static Abastecimento get(int cdAbastecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_abastecimento WHERE cd_abastecimento=?");
			pstmt.setInt(1, cdAbastecimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Abastecimento(rs.getInt("cd_abastecimento"),
						rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_veiculo"),
						rs.getFloat("qt_hodometro"),
						rs.getInt("lg_completo"),
						rs.getInt("st_abastecimento"),
						rs.getFloat("qt_litros_autorizada"),
						rs.getFloat("qt_litros_abastecida"),
						rs.getFloat("vl_autorizado"),
						rs.getFloat("vl_abastecido"),
						(rs.getTimestamp("dt_autorizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_autorizacao").getTime()),
						(rs.getTimestamp("dt_abastecimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_abastecimento").getTime()),
						rs.getFloat("vl_litro_combustivel"),
						rs.getInt("tp_combustivel"),
						rs.getInt("cd_responsavel"),
						rs.getString("nr_autorizacao"),
						rs.getInt("qt_vias_impressas"),
						rs.getInt("cd_fornecedor"),
						rs.getInt("cd_autorizacao"),
						rs.getInt("tp_abastecimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_abastecimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_abastecimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
