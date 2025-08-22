package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BemPenhoraDAO{

	public static int insert(BemPenhora objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(BemPenhora objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_BEM");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "CD_PROCESSO");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("PRC_BEM_PENHORA", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdBem()<=0)
				objeto.setCdBem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_BEM_PENHORA (CD_BEM,"+
			                                  "CD_PROCESSO,"+
			                                  "CD_CIDADE,"+
			                                  "NM_BEM,"+
			                                  "ST_BEM,"+
			                                  "VL_BEM,"+
			                                  "NM_LOGRADOURO,"+
			                                  "NM_BAIRRO,"+
			                                  "NM_DISTRITO,"+
			                                  "NR_ENDERECO,"+
			                                  "NM_COMPLEMENTO,"+
			                                  "NM_ENDERECO,"+
			                                  "DT_ATUALIZACAO_EDI,"+
			                                  "ST_ATUALIZACAO_EDI,"+
			                                  "TP_BEM,"+
			                                  "TP_GARANTIA,"+
			                                  "TP_GRAU,"+
			                                  "VL_JUDICIAL,"+
			                                  "VL_CREDOR,"+
			                                  "DT_ESTIMATIVA,"+
			                                  "DT_AVALIACAO_JUDICIAL,"+
			                                  "DT_AVALIACAO_CREDOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdBem());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCidade());
			pstmt.setString(4,objeto.getNmBem());
			pstmt.setInt(5,objeto.getStBem());
			pstmt.setDouble(6,objeto.getVlBem());
			pstmt.setString(7,objeto.getNmLogradouro());
			pstmt.setString(8,objeto.getNmBairro());
			pstmt.setString(9,objeto.getNmDistrito());
			pstmt.setString(10,objeto.getNrEndereco());
			pstmt.setString(11,objeto.getNmComplemento());
			pstmt.setString(12,objeto.getNmEndereco());
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(14,objeto.getStAtualizacaoEdi());
			pstmt.setInt(15,objeto.getTpBem());
			pstmt.setInt(16,objeto.getTpGarantia());
			pstmt.setInt(17,objeto.getTpGrau());
			pstmt.setDouble(18,objeto.getVlJudicial());
			pstmt.setDouble(19,objeto.getVlCredor());
			if(objeto.getDtEstimativa()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtEstimativa().getTimeInMillis()));
			if(objeto.getDtAvaliacaoJudicial()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtAvaliacaoJudicial().getTimeInMillis()));
			if(objeto.getDtAvaliacaoCredor()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtAvaliacaoCredor().getTimeInMillis()));
			pstmt.executeUpdate();
			return objeto.getCdBem();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BemPenhora objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BemPenhora objeto, int cdBemOld, int cdProcessoOld) {
		return update(objeto, cdBemOld, cdProcessoOld, null);
	}

	public static int update(BemPenhora objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BemPenhora objeto, int cdBemOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_BEM_PENHORA SET CD_BEM=?,"+
												      		   "CD_PROCESSO=?,"+
												      		   "CD_CIDADE=?,"+
												      		   "NM_BEM=?,"+
												      		   "ST_BEM=?,"+
												      		   "VL_BEM=?,"+
												      		   "NM_LOGRADOURO=?,"+
												      		   "NM_BAIRRO=?,"+
												      		   "NM_DISTRITO=?,"+
												      		   "NR_ENDERECO=?,"+
												      		   "NM_COMPLEMENTO=?,"+
												      		   "NM_ENDERECO=?,"+
												      		   "DT_ATUALIZACAO_EDI=?,"+
												      		   "ST_ATUALIZACAO_EDI=?,"+
												      		   "TP_BEM=?,"+
												      		   "TP_GARANTIA=?,"+
												      		   "TP_GRAU=?,"+
												      		   "VL_JUDICIAL=?,"+
												      		   "VL_CREDOR=?,"+
												      		   "DT_ESTIMATIVA=?,"+
												      		   "DT_AVALIACAO_JUDICIAL=?,"+
												      		   "DT_AVALIACAO_CREDOR=? WHERE CD_BEM=? AND CD_PROCESSO=?");
			pstmt.setInt(1,objeto.getCdBem());
			pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCidade());
			pstmt.setString(4,objeto.getNmBem());
			pstmt.setInt(5,objeto.getStBem());
			pstmt.setDouble(6,objeto.getVlBem());
			pstmt.setString(7,objeto.getNmLogradouro());
			pstmt.setString(8,objeto.getNmBairro());
			pstmt.setString(9,objeto.getNmDistrito());
			pstmt.setString(10,objeto.getNrEndereco());
			pstmt.setString(11,objeto.getNmComplemento());
			pstmt.setString(12,objeto.getNmEndereco());
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(14,objeto.getStAtualizacaoEdi());
			pstmt.setInt(15,objeto.getTpBem());
			pstmt.setInt(16,objeto.getTpGarantia());
			pstmt.setInt(17,objeto.getTpGrau());
			pstmt.setDouble(18,objeto.getVlJudicial());
			pstmt.setDouble(19,objeto.getVlCredor());
			if(objeto.getDtEstimativa()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtEstimativa().getTimeInMillis()));
			if(objeto.getDtAvaliacaoJudicial()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtAvaliacaoJudicial().getTimeInMillis()));
			if(objeto.getDtAvaliacaoCredor()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtAvaliacaoCredor().getTimeInMillis()));
			pstmt.setInt(23, cdBemOld!=0 ? cdBemOld : objeto.getCdBem());
			pstmt.setInt(24, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdBem, int cdProcesso) {
		return delete(cdBem, cdProcesso, null);
	}

	public static int delete(int cdBem, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_BEM_PENHORA WHERE CD_BEM=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdBem);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BemPenhora get(int cdBem, int cdProcesso) {
		return get(cdBem, cdProcesso, null);
	}

	public static BemPenhora get(int cdBem, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_BEM_PENHORA WHERE CD_BEM=? AND CD_PROCESSO=?");
			pstmt.setInt(1, cdBem);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BemPenhora(rs.getInt("CD_BEM"),
						rs.getInt("CD_PROCESSO"),
						rs.getInt("CD_CIDADE"),
						rs.getString("NM_BEM"),
						rs.getInt("ST_BEM"),
						rs.getDouble("VL_BEM"),
						rs.getString("NM_LOGRADOURO"),
						rs.getString("NM_BAIRRO"),
						rs.getString("NM_DISTRITO"),
						rs.getString("NR_ENDERECO"),
						rs.getString("NM_COMPLEMENTO"),
						rs.getString("NM_ENDERECO"),
						(rs.getTimestamp("DT_ATUALIZACAO_EDI")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ATUALIZACAO_EDI").getTime()),
						rs.getInt("ST_ATUALIZACAO_EDI"),
						rs.getInt("TP_BEM"),
						rs.getInt("TP_GARANTIA"),
						rs.getInt("TP_GRAU"),
						rs.getDouble("VL_JUDICIAL"),
						rs.getDouble("VL_CREDOR"),
						(rs.getTimestamp("DT_ESTIMATIVA")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ESTIMATIVA").getTime()),
						(rs.getTimestamp("DT_AVALIACAO_JUDICIAL")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_AVALIACAO_JUDICIAL").getTime()),
						(rs.getTimestamp("DT_AVALIACAO_CREDOR")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_AVALIACAO_CREDOR").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_BEM_PENHORA");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemPenhoraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM PRC_BEM_PENHORA", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
