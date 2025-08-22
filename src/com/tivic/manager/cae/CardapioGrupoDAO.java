package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class CardapioGrupoDAO{

	public static int insert(CardapioGrupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(CardapioGrupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("cae_cardapio_grupo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCardapioGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_cardapio_grupo (cd_cardapio_grupo,"+
			                                  "nm_cardapio_grupo,"+
			                                  "dt_inicial_grupo,"+
			                                  "dt_final_grupo,"+
			                                  "cd_modalidade,"+
			                                  "tp_cardapio_grupo) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCardapioGrupo());
			if(objeto.getDtInicialGrupo()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicialGrupo().getTimeInMillis()));
			if(objeto.getDtFinalGrupo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalGrupo().getTimeInMillis()));
			if(objeto.getCdModalidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdModalidade());
			pstmt.setInt(6,objeto.getTpCardapioGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CardapioGrupo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CardapioGrupo objeto, int cdCardapioGrupoOld) {
		return update(objeto, cdCardapioGrupoOld, null);
	}

	public static int update(CardapioGrupo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CardapioGrupo objeto, int cdCardapioGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_cardapio_grupo SET cd_cardapio_grupo=?,"+
												      		   "nm_cardapio_grupo=?,"+
												      		   "dt_inicial_grupo=?,"+
												      		   "dt_final_grupo=?,"+
												      		   "cd_modalidade=?,"+
												      		   "tp_cardapio_grupo=? WHERE cd_cardapio_grupo=?");
			pstmt.setInt(1,objeto.getCdCardapioGrupo());
			pstmt.setString(2,objeto.getNmCardapioGrupo());
			if(objeto.getDtInicialGrupo()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicialGrupo().getTimeInMillis()));
			if(objeto.getDtFinalGrupo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalGrupo().getTimeInMillis()));
			if(objeto.getCdModalidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdModalidade());
			pstmt.setInt(6,objeto.getTpCardapioGrupo());
			pstmt.setInt(7, cdCardapioGrupoOld!=0 ? cdCardapioGrupoOld : objeto.getCdCardapioGrupo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCardapioGrupo) {
		return delete(cdCardapioGrupo, null);
	}

	public static int delete(int cdCardapioGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_cardapio_grupo WHERE cd_cardapio_grupo=?");
			pstmt.setInt(1, cdCardapioGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CardapioGrupo get(int cdCardapioGrupo) {
		return get(cdCardapioGrupo, null);
	}

	public static CardapioGrupo get(int cdCardapioGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio_grupo WHERE cd_cardapio_grupo=?");
			pstmt.setInt(1, cdCardapioGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CardapioGrupo(rs.getInt("cd_cardapio_grupo"),
						rs.getString("nm_cardapio_grupo"),
						(rs.getTimestamp("dt_inicial_grupo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial_grupo").getTime()),
						(rs.getTimestamp("dt_final_grupo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_grupo").getTime()),
						rs.getInt("cd_modalidade"),
						rs.getInt("tp_cardapio_grupo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CardapioGrupo> getList() {
		return getList(null);
	}

	public static ArrayList<CardapioGrupo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CardapioGrupo> list = new ArrayList<CardapioGrupo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CardapioGrupo obj = CardapioGrupoDAO.get(rsm.getInt("cd_cardapio_grupo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioGrupoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_cardapio_grupo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
