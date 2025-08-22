package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AndamentoPrazoDAO{

	public static int insert(AndamentoPrazo objeto) {
		return insert(objeto, null);
	}

	public static int insert(AndamentoPrazo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("PRC_ANDAMENTO_PRAZO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAndamentoPrazo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_ANDAMENTO_PRAZO (CD_ANDAMENTO_PRAZO,"+
			                                  "CD_TIPO_ANDAMENTO,"+
			                                  "CD_TIPO_PROCESSO,"+
			                                  "CD_TIPO_PRAZO,"+
			                                  "QT_DIAS,"+
			                                  "ST_LIMINAR,"+
			                                  "CD_AREA_DIREITO,"+
			                                  "TP_POSICAO_CLIENTE,"+
			                                  "TP_INSTANCIA,"+
			                                  "TP_CONTAGEM_PRAZO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoAndamento());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoProcesso());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoPrazo());
			pstmt.setInt(5,objeto.getQtDias());
			pstmt.setInt(6,objeto.getStLiminar());
			if(objeto.getCdAreaDireito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAreaDireito());
			pstmt.setInt(8,objeto.getTpPosicaoCliente());
			pstmt.setInt(9,objeto.getTpInstancia());
			pstmt.setInt(10,objeto.getTpContagemPrazo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AndamentoPrazo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AndamentoPrazo objeto, int cdAndamentoPrazoOld) {
		return update(objeto, cdAndamentoPrazoOld, null);
	}

	public static int update(AndamentoPrazo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AndamentoPrazo objeto, int cdAndamentoPrazoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_ANDAMENTO_PRAZO SET CD_ANDAMENTO_PRAZO=?,"+
												      		   "CD_TIPO_ANDAMENTO=?,"+
												      		   "CD_TIPO_PROCESSO=?,"+
												      		   "CD_TIPO_PRAZO=?,"+
												      		   "QT_DIAS=?,"+
												      		   "ST_LIMINAR=?,"+
												      		   "CD_AREA_DIREITO=?,"+
												      		   "TP_POSICAO_CLIENTE=?,"+
												      		   "TP_INSTANCIA=?,"+
												      		   "TP_CONTAGEM_PRAZO=? WHERE CD_ANDAMENTO_PRAZO=?");
			pstmt.setInt(1,objeto.getCdAndamentoPrazo());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoAndamento());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoProcesso());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTipoPrazo());
			pstmt.setInt(5,objeto.getQtDias());
			pstmt.setInt(6,objeto.getStLiminar());
			if(objeto.getCdAreaDireito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdAreaDireito());
			pstmt.setInt(8,objeto.getTpPosicaoCliente());
			pstmt.setInt(9,objeto.getTpInstancia());
			pstmt.setInt(10,objeto.getTpContagemPrazo());
			pstmt.setInt(11, cdAndamentoPrazoOld!=0 ? cdAndamentoPrazoOld : objeto.getCdAndamentoPrazo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAndamentoPrazo) {
		return delete(cdAndamentoPrazo, null);
	}

	public static int delete(int cdAndamentoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_ANDAMENTO_PRAZO WHERE CD_ANDAMENTO_PRAZO=?");
			pstmt.setInt(1, cdAndamentoPrazo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AndamentoPrazo get(int cdAndamentoPrazo) {
		return get(cdAndamentoPrazo, null);
	}

	public static AndamentoPrazo get(int cdAndamentoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_ANDAMENTO_PRAZO WHERE CD_ANDAMENTO_PRAZO=?");
			pstmt.setInt(1, cdAndamentoPrazo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AndamentoPrazo(rs.getInt("CD_ANDAMENTO_PRAZO"),
						rs.getInt("CD_TIPO_ANDAMENTO"),
						rs.getInt("CD_TIPO_PROCESSO"),
						rs.getInt("CD_TIPO_PRAZO"),
						rs.getInt("QT_DIAS"),
						rs.getInt("ST_LIMINAR"),
						rs.getInt("CD_AREA_DIREITO"),
						rs.getInt("TP_POSICAO_CLIENTE"),
						rs.getInt("TP_INSTANCIA"),
						rs.getInt("TP_CONTAGEM_PRAZO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_ANDAMENTO_PRAZO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AndamentoPrazo> getList() {
		return getList(null);
	}

	public static ArrayList<AndamentoPrazo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AndamentoPrazo> list = new ArrayList<AndamentoPrazo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AndamentoPrazo obj = AndamentoPrazoDAO.get(rsm.getInt("CD_ANDAMENTO_PRAZO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AndamentoPrazoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_ANDAMENTO_PRAZO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
