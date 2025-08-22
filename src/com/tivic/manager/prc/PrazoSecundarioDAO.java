package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class PrazoSecundarioDAO{

	public static int insert(PrazoSecundario objeto) {
		return insert(objeto, null);
	}

	public static int insert(PrazoSecundario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_prazo_secundario (cd_prazo_secundario,"+
			                                  "cd_tipo_prazo,"+
			                                  "cd_tipo_processo,"+
			                                  "qt_dias,"+
			                                  "st_liminar,"+
			                                  "tp_contagem_prazo,"+
			                                  "tp_acao) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdPrazoSecundario());
			pstmt.setInt(2,objeto.getCdTipoPrazo());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoProcesso());
			pstmt.setInt(4,objeto.getQtDias());
			pstmt.setInt(5,objeto.getStLiminar());
			pstmt.setInt(6,objeto.getTpContagemPrazo());
			pstmt.setInt(7,objeto.getTpAcao());
			pstmt.executeUpdate();
			return objeto.getCdPrazoSecundario();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PrazoSecundario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PrazoSecundario objeto, int cdPrazoSecundarioOld, int cdTipoPrazoOld) {
		return update(objeto, cdPrazoSecundarioOld, cdTipoPrazoOld, null);
	}

	public static int update(PrazoSecundario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PrazoSecundario objeto, int cdPrazoSecundarioOld, int cdTipoPrazoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_prazo_secundario SET cd_prazo_secundario=?,"+
												      		   "cd_tipo_prazo=?,"+
												      		   "cd_tipo_processo=?,"+
												      		   "qt_dias=?,"+
												      		   "st_liminar=?,"+
												      		   "tp_contagem_prazo=?,"+
												      		   "tp_acao=? WHERE cd_prazo_secundario=? AND cd_tipo_prazo=?");
			pstmt.setInt(1,objeto.getCdPrazoSecundario());
			pstmt.setInt(2,objeto.getCdTipoPrazo());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoProcesso());
			pstmt.setInt(4,objeto.getQtDias());
			pstmt.setInt(5,objeto.getStLiminar());
			pstmt.setInt(6,objeto.getTpContagemPrazo());
			pstmt.setInt(7,objeto.getTpAcao());
			pstmt.setInt(8, cdPrazoSecundarioOld!=0 ? cdPrazoSecundarioOld : objeto.getCdPrazoSecundario());
			pstmt.setInt(9, cdTipoPrazoOld!=0 ? cdTipoPrazoOld : objeto.getCdTipoPrazo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPrazoSecundario, int cdTipoPrazo) {
		return delete(cdPrazoSecundario, cdTipoPrazo, null);
	}

	public static int delete(int cdPrazoSecundario, int cdTipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_prazo_secundario WHERE cd_prazo_secundario=? AND cd_tipo_prazo=?");
			pstmt.setInt(1, cdPrazoSecundario);
			pstmt.setInt(2, cdTipoPrazo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PrazoSecundario get(int cdPrazoSecundario, int cdTipoPrazo) {
		return get(cdPrazoSecundario, cdTipoPrazo, null);
	}

	public static PrazoSecundario get(int cdPrazoSecundario, int cdTipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_prazo_secundario WHERE cd_prazo_secundario=? AND cd_tipo_prazo=?");
			pstmt.setInt(1, cdPrazoSecundario);
			pstmt.setInt(2, cdTipoPrazo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PrazoSecundario(rs.getInt("cd_prazo_secundario"),
						rs.getInt("cd_tipo_prazo"),
						rs.getInt("cd_tipo_processo"),
						rs.getInt("qt_dias"),
						rs.getInt("st_liminar"),
						rs.getInt("tp_contagem_prazo"),
						rs.getInt("tp_acao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_prazo_secundario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PrazoSecundario> getList() {
		return getList(null);
	}

	public static ArrayList<PrazoSecundario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PrazoSecundario> list = new ArrayList<PrazoSecundario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PrazoSecundario obj = PrazoSecundarioDAO.get(rsm.getInt("cd_prazo_secundario"), rsm.getInt("cd_tipo_prazo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PrazoSecundarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_prazo_secundario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}