package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class CardapioDAO{

	public static int insert(Cardapio objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cardapio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("cae_cardapio", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCardapio(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_cardapio (cd_cardapio,"+
			                                  "nm_cardapio,"+
			                                  "dt_inicial_validade,"+
			                                  "dt_final_validade,"+
			                                  "cd_curso,"+
			                                  "cd_recomendacao_nutricional,"+
			                                  "cd_modalidade,"+
			                                  "cd_cardapio_grupo,"+
			                                  "id_cardapio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCardapio());
			if(objeto.getDtInicialValidade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicialValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			if(objeto.getCdCurso()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCurso());
			if(objeto.getCdRecomendacaoNutricional()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdRecomendacaoNutricional());
			if(objeto.getCdModalidade()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdModalidade());
			pstmt.setInt(8,objeto.getCdCardapioGrupo());
			pstmt.setString(9,objeto.getIdCardapio());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cardapio objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Cardapio objeto, int cdCardapioOld) {
		return update(objeto, cdCardapioOld, null);
	}

	public static int update(Cardapio objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Cardapio objeto, int cdCardapioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_cardapio SET cd_cardapio=?,"+
												      		   "nm_cardapio=?,"+
												      		   "dt_inicial_validade=?,"+
												      		   "dt_final_validade=?,"+
												      		   "cd_curso=?,"+
												      		   "cd_recomendacao_nutricional=?,"+
												      		   "cd_modalidade=?,"+
												      		   "cd_cardapio_grupo=?,"+
												      		   "id_cardapio=? WHERE cd_cardapio=?");
			pstmt.setInt(1,objeto.getCdCardapio());
			pstmt.setString(2,objeto.getNmCardapio());
			if(objeto.getDtInicialValidade()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicialValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			if(objeto.getCdCurso()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCurso());
			if(objeto.getCdRecomendacaoNutricional()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdRecomendacaoNutricional());
			if(objeto.getCdModalidade()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdModalidade());
			pstmt.setInt(8,objeto.getCdCardapioGrupo());
			pstmt.setString(9,objeto.getIdCardapio());
			pstmt.setInt(10, cdCardapioOld!=0 ? cdCardapioOld : objeto.getCdCardapio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCardapio) {
		return delete(cdCardapio, null);
	}

	public static int delete(int cdCardapio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_cardapio WHERE cd_cardapio=?");
			pstmt.setInt(1, cdCardapio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cardapio get(int cdCardapio) {
		return get(cdCardapio, null);
	}

	public static Cardapio get(int cdCardapio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio WHERE cd_cardapio=?");
			pstmt.setInt(1, cdCardapio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cardapio(rs.getInt("cd_cardapio"),
						rs.getString("nm_cardapio"),
						(rs.getTimestamp("dt_inicial_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial_validade").getTime()),
						(rs.getTimestamp("dt_final_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_validade").getTime()),
						rs.getInt("cd_curso"),
						rs.getInt("cd_recomendacao_nutricional"),
						rs.getInt("cd_modalidade"),
						rs.getInt("cd_cardapio_grupo"),
						rs.getString("id_cardapio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_cardapio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Cardapio> getList() {
		return getList(null);
	}

	public static ArrayList<Cardapio> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Cardapio> list = new ArrayList<Cardapio>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Cardapio obj = CardapioDAO.get(rsm.getInt("cd_cardapio"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CardapioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_cardapio", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
