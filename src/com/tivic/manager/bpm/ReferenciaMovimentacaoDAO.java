package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import sol.dao.Util;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class ReferenciaMovimentacaoDAO{

	public static int insert(ReferenciaMovimentacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ReferenciaMovimentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_referencia");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdReferencia()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_movimentacao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("bpm_referencia_movimentacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMovimentacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_referencia_movimentacao (cd_referencia,"+
			                                  "cd_movimentacao,"+
			                                  "cd_setor,"+
			                                  "dt_movimentacao) VALUES (?, ?, ?, ?)");
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setInt(2, code);
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getDtMovimentacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtMovimentacao().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ReferenciaMovimentacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ReferenciaMovimentacao objeto, int cdReferenciaOld, int cdMovimentacaoOld) {
		return update(objeto, cdReferenciaOld, cdMovimentacaoOld, null);
	}

	public static int update(ReferenciaMovimentacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ReferenciaMovimentacao objeto, int cdReferenciaOld, int cdMovimentacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_referencia_movimentacao SET cd_referencia=?,"+
												      		   "cd_movimentacao=?,"+
												      		   "cd_setor=?,"+
												      		   "dt_movimentacao=? WHERE cd_referencia=? AND cd_movimentacao=?");
			pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setInt(2,objeto.getCdMovimentacao());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getDtMovimentacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtMovimentacao().getTimeInMillis()));
			pstmt.setInt(5, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.setInt(6, cdMovimentacaoOld!=0 ? cdMovimentacaoOld : objeto.getCdMovimentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReferencia, int cdMovimentacao) {
		return delete(cdReferencia, cdMovimentacao, null);
	}

	public static int delete(int cdReferencia, int cdMovimentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_referencia_movimentacao WHERE cd_referencia=? AND cd_movimentacao=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.setInt(2, cdMovimentacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ReferenciaMovimentacao get(int cdReferencia, int cdMovimentacao) {
		return get(cdReferencia, cdMovimentacao, null);
	}

	public static ReferenciaMovimentacao get(int cdReferencia, int cdMovimentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_referencia_movimentacao WHERE cd_referencia=? AND cd_movimentacao=?");
			pstmt.setInt(1, cdReferencia);
			pstmt.setInt(2, cdMovimentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ReferenciaMovimentacao(rs.getInt("cd_referencia"),
						rs.getInt("cd_movimentacao"),
						rs.getInt("cd_setor"),
						(rs.getTimestamp("dt_movimentacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimentacao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_referencia_movimentacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaMovimentacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_referencia_movimentacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
