package com.tivic.manager.fsc;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.HashMap;
import java.util.ArrayList;

public class SituacaoTributariaDAO{

	public static int insert(SituacaoTributaria objeto) {
		return insert(objeto, null);
	}

	public static int insert(SituacaoTributaria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tributo");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTributo()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_situacao_tributaria");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("fsc_situacao_tributaria", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSituacaoTributaria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_situacao_tributaria (cd_tributo,"+
			                                  "cd_situacao_tributaria,"+
			                                  "nr_situacao_tributaria,"+
			                                  "nm_situacao_tributaria,"+
			                                  "sg_situacao_tributaria,"+
			                                  "tp_situacao_tributaria_ecf,"+
			                                  "lg_simples,"+
			                                  "lg_substituicao,"+
			                                  "lg_gera_credito,"+
			                                  "lg_partilha,"+
			                                  "lg_motivo_isencao,"+
			                                  "lg_reducao_base," +
			                                  "lg_retido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTributo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTributo());
			pstmt.setInt(2, code);
			pstmt.setString(3,objeto.getNrSituacaoTributaria());
			pstmt.setString(4,objeto.getNmSituacaoTributaria());
			pstmt.setString(5,objeto.getSgSituacaoTributaria());
			pstmt.setInt(6,objeto.getTpSituacaoTributariaEcf());
			pstmt.setInt(7,objeto.getLgSimples());
			pstmt.setInt(8,objeto.getLgSubstituicao());
			pstmt.setInt(9,objeto.getLgGeraCredito());
			pstmt.setInt(10,objeto.getLgPartilha());
			pstmt.setInt(11,objeto.getLgMotivoIsencao());
			pstmt.setInt(12,objeto.getLgReducaoBase());
			pstmt.setInt(13,objeto.getLgRetido());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SituacaoTributaria objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(SituacaoTributaria objeto, int cdTributoOld, int cdSituacaoTributariaOld) {
		return update(objeto, cdTributoOld, cdSituacaoTributariaOld, null);
	}

	public static int update(SituacaoTributaria objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(SituacaoTributaria objeto, int cdTributoOld, int cdSituacaoTributariaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_situacao_tributaria SET cd_tributo=?,"+
												      		   "cd_situacao_tributaria=?,"+
												      		   "nr_situacao_tributaria=?,"+
												      		   "nm_situacao_tributaria=?,"+
												      		   "sg_situacao_tributaria=?,"+
												      		   "tp_situacao_tributaria_ecf=?,"+
												      		   "lg_simples=?,"+
												      		   "lg_substituicao=?,"+
												      		   "lg_gera_credito=?,"+
												      		   "lg_partilha=?,"+
												      		   "lg_motivo_isencao=?,"+
												      		   "lg_reducao_base=?," +
												      		   "lg_retido=? WHERE cd_tributo=? AND cd_situacao_tributaria=?");
			pstmt.setInt(1,objeto.getCdTributo());
			pstmt.setInt(2,objeto.getCdSituacaoTributaria());
			pstmt.setString(3,objeto.getNrSituacaoTributaria());
			pstmt.setString(4,objeto.getNmSituacaoTributaria());
			pstmt.setString(5,objeto.getSgSituacaoTributaria());
			pstmt.setInt(6,objeto.getTpSituacaoTributariaEcf());
			pstmt.setInt(7,objeto.getLgSimples());
			pstmt.setInt(8,objeto.getLgSubstituicao());
			pstmt.setInt(9,objeto.getLgGeraCredito());
			pstmt.setInt(10,objeto.getLgPartilha());
			pstmt.setInt(11,objeto.getLgMotivoIsencao());
			pstmt.setInt(12,objeto.getLgReducaoBase());
			pstmt.setInt(13,objeto.getLgRetido());
			pstmt.setInt(14, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			pstmt.setInt(15, cdSituacaoTributariaOld!=0 ? cdSituacaoTributariaOld : objeto.getCdSituacaoTributaria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTributo, int cdSituacaoTributaria) {
		return delete(cdTributo, cdSituacaoTributaria, null);
	}

	public static int delete(int cdTributo, int cdSituacaoTributaria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_situacao_tributaria WHERE cd_tributo=? AND cd_situacao_tributaria=?");
			pstmt.setInt(1, cdTributo);
			pstmt.setInt(2, cdSituacaoTributaria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SituacaoTributaria get(int cdTributo, int cdSituacaoTributaria) {
		return get(cdTributo, cdSituacaoTributaria, null);
	}

	public static SituacaoTributaria get(int cdTributo, int cdSituacaoTributaria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_situacao_tributaria WHERE cd_tributo=? AND cd_situacao_tributaria=?");
			pstmt.setInt(1, cdTributo);
			pstmt.setInt(2, cdSituacaoTributaria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SituacaoTributaria(rs.getInt("cd_tributo"),
						rs.getInt("cd_situacao_tributaria"),
						rs.getString("nr_situacao_tributaria"),
						rs.getString("nm_situacao_tributaria"),
						rs.getString("sg_situacao_tributaria"),
						rs.getInt("tp_situacao_tributaria_ecf"),
						rs.getInt("lg_simples"),
						rs.getInt("lg_substituicao"),
						rs.getInt("lg_gera_credito"),
						rs.getInt("lg_partilha"),
						rs.getInt("lg_motivo_isencao"),
						rs.getInt("lg_reducao_base"),
						rs.getInt("lg_retido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_situacao_tributaria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoTributariaDAO.getAll: " + e);
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
		ResultSetMap rsm = Search.find("SELECT * FROM fsc_situacao_tributaria ", " ORDER BY CAST (nr_situacao_tributaria AS INTEGER) ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}

}
