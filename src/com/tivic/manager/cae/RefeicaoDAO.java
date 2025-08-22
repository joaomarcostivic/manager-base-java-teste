package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class RefeicaoDAO{

	public static int insert(Refeicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Refeicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_cardapio");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdCardapio()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_preparacao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPreparacao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_refeicao");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("cae_refeicao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRefeicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_refeicao (cd_cardapio,"+
			                                  "cd_preparacao,"+
			                                  "cd_refeicao,"+
			                                  "nr_dia,"+
			                                  "tp_horario) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdCardapio()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCardapio());
			if(objeto.getCdPreparacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPreparacao());
			pstmt.setInt(3, code);
			pstmt.setInt(4,objeto.getNrDia());
			pstmt.setInt(5,objeto.getTpHorario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Refeicao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(Refeicao objeto, int cdCardapioOld, int cdPreparacaoOld, int cdRefeicaoOld) {
		return update(objeto, cdCardapioOld, cdPreparacaoOld, cdRefeicaoOld, null);
	}

	public static int update(Refeicao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(Refeicao objeto, int cdCardapioOld, int cdPreparacaoOld, int cdRefeicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_refeicao SET cd_cardapio=?,"+
												      		   "cd_preparacao=?,"+
												      		   "cd_refeicao=?,"+
												      		   "nr_dia=?,"+
												      		   "tp_horario=? WHERE cd_cardapio=? AND cd_preparacao=? AND cd_refeicao=?");
			pstmt.setInt(1,objeto.getCdCardapio());
			pstmt.setInt(2,objeto.getCdPreparacao());
			pstmt.setInt(3,objeto.getCdRefeicao());
			pstmt.setInt(4,objeto.getNrDia());
			pstmt.setInt(5,objeto.getTpHorario());
			pstmt.setInt(6, cdCardapioOld!=0 ? cdCardapioOld : objeto.getCdCardapio());
			pstmt.setInt(7, cdPreparacaoOld!=0 ? cdPreparacaoOld : objeto.getCdPreparacao());
			pstmt.setInt(8, cdRefeicaoOld!=0 ? cdRefeicaoOld : objeto.getCdRefeicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCardapio, int cdPreparacao, int cdRefeicao) {
		return delete(cdCardapio, cdPreparacao, cdRefeicao, null);
	}

	public static int delete(int cdCardapio, int cdPreparacao, int cdRefeicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_refeicao WHERE cd_cardapio=? AND cd_preparacao=? AND cd_refeicao=?");
			pstmt.setInt(1, cdCardapio);
			pstmt.setInt(2, cdPreparacao);
			pstmt.setInt(3, cdRefeicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Refeicao get(int cdCardapio, int cdPreparacao, int cdRefeicao) {
		return get(cdCardapio, cdPreparacao, cdRefeicao, null);
	}

	public static Refeicao get(int cdCardapio, int cdPreparacao, int cdRefeicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_refeicao WHERE cd_cardapio=? AND cd_preparacao=? AND cd_refeicao=?");
			pstmt.setInt(1, cdCardapio);
			pstmt.setInt(2, cdPreparacao);
			pstmt.setInt(3, cdRefeicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Refeicao(rs.getInt("cd_cardapio"),
						rs.getInt("cd_preparacao"),
						rs.getInt("cd_refeicao"),
						rs.getInt("nr_dia"),
						rs.getInt("tp_horario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_refeicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Refeicao> getList() {
		return getList(null);
	}

	public static ArrayList<Refeicao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Refeicao> list = new ArrayList<Refeicao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Refeicao obj = RefeicaoDAO.get(rsm.getInt("cd_cardapio"), rsm.getInt("cd_preparacao"), rsm.getInt("cd_refeicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RefeicaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_refeicao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
