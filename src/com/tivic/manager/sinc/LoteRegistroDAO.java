package com.tivic.manager.sinc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LoteRegistroDAO{

	public static int insert(LoteRegistro objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteRegistro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("sinc_lote_registro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLoteRegistro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO sinc_lote_registro (cd_lote_registro,"+
			                                  "cd_lote,"+
			                                  "cd_tabela,"+
			                                  "nm_valor_chaves,"+
			                                  "id_origem,"+
			                                  "tp_atualizacao,"+
			                                  "dt_atualizacao,"+
			                                  "nm_chave_referencia,"+
			                                  "nm_campo_alterado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			if(objeto.getCdTabela()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTabela());
			pstmt.setString(4,objeto.getNmValorChaves());
			pstmt.setString(5,objeto.getIdOrigem());
			pstmt.setInt(6,objeto.getTpAtualizacao());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setString(8,objeto.getNmChaveReferencia());
			pstmt.setString(9,objeto.getNmCampoAlterado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteRegistro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LoteRegistro objeto, int cdLoteRegistroOld) {
		return update(objeto, cdLoteRegistroOld, null);
	}

	public static int update(LoteRegistro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LoteRegistro objeto, int cdLoteRegistroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE sinc_lote_registro SET cd_lote_registro=?,"+
												      		   "cd_lote=?,"+
												      		   "cd_tabela=?,"+
												      		   "nm_valor_chaves=?,"+
												      		   "id_origem=?,"+
												      		   "tp_atualizacao=?,"+
												      		   "dt_atualizacao=?,"+
												      		   "nm_chave_referencia=?,"+
												      		   "nm_campo_alterado=? WHERE cd_lote_registro=?");
			pstmt.setInt(1,objeto.getCdLoteRegistro());
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			if(objeto.getCdTabela()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTabela());
			pstmt.setString(4,objeto.getNmValorChaves());
			pstmt.setString(5,objeto.getIdOrigem());
			pstmt.setInt(6,objeto.getTpAtualizacao());
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setString(8,objeto.getNmChaveReferencia());
			pstmt.setString(9,objeto.getNmCampoAlterado());
			pstmt.setInt(10, cdLoteRegistroOld!=0 ? cdLoteRegistroOld : objeto.getCdLoteRegistro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteRegistro) {
		return delete(cdLoteRegistro, null);
	}

	public static int delete(int cdLoteRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM sinc_lote_registro WHERE cd_lote_registro=?");
			pstmt.setInt(1, cdLoteRegistro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteRegistro get(int cdLoteRegistro) {
		return get(cdLoteRegistro, null);
	}

	public static LoteRegistro get(int cdLoteRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote_registro WHERE cd_lote_registro=?");
			pstmt.setInt(1, cdLoteRegistro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteRegistro(rs.getInt("cd_lote_registro"),
						rs.getInt("cd_lote"),
						rs.getInt("cd_tabela"),
						rs.getString("nm_valor_chaves"),
						rs.getString("id_origem"),
						rs.getInt("tp_atualizacao"),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						rs.getString("nm_chave_referencia"),
						rs.getString("nm_campo_alterado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote_registro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteRegistro> getList() {
		return getList(null);
	}

	public static ArrayList<LoteRegistro> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteRegistro> list = new ArrayList<LoteRegistro>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LoteRegistro obj = LoteRegistroDAO.get(rsm.getInt("cd_lote_registro"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroDAO.getList: " + e);
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
		return Search.find("SELECT * FROM sinc_lote_registro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
