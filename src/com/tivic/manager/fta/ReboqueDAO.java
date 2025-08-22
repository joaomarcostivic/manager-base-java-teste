package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ReboqueDAO{

	public static int insert(Reboque objeto) {
		return insert(objeto, null);
	}

	public static int insert(Reboque objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.bpm.ReferenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdReferencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_reboque (cd_reboque,"+
			                                  "tp_reboque,"+
			                                  "tp_carga,"+
			                                  "nr_capacidade,"+
			                                  "tp_eixo_dianteiro,"+
			                                  "tp_eixo_traseiro,"+
			                                  "qt_eixos_dianteiros,"+
			                                  "qt_eixos_traseiros) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setInt(2,objeto.getTpReboque());
			pstmt.setInt(3,objeto.getTpCarga());
			pstmt.setString(4,objeto.getNrCapacidade());
			pstmt.setInt(5,objeto.getTpEixoDianteiro());
			pstmt.setInt(6,objeto.getTpEixoTraseiro());
			pstmt.setInt(7,objeto.getQtEixosDianteiros());
			pstmt.setInt(8,objeto.getQtEixosTraseiros());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Reboque objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Reboque objeto, int cdReboqueOld) {
		return update(objeto, cdReboqueOld, null);
	}

	public static int update(Reboque objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Reboque objeto, int cdReboqueOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Reboque objetoTemp = get(objeto.getCdReferencia(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO fta_reboque (cd_reboque,"+
			                                  "tp_reboque,"+
			                                  "tp_carga,"+
			                                  "nr_capacidade,"+
			                                  "tp_eixo_dianteiro,"+
			                                  "tp_eixo_traseiro,"+
			                                  "qt_eixos_dianteiros,"+
			                                  "qt_eixos_traseiros) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE fta_reboque SET cd_reboque=?,"+
												      		   "tp_reboque=?,"+
												      		   "tp_carga=?,"+
												      		   "nr_capacidade=?,"+
												      		   "tp_eixo_dianteiro=?,"+
												      		   "tp_eixo_traseiro=?,"+
												      		   "qt_eixos_dianteiros=?,"+
												      		   "qt_eixos_traseiros=? WHERE cd_reboque=?");
			pstmt.setInt(1,objeto.getCdReferencia());
			pstmt.setInt(2,objeto.getTpReboque());
			pstmt.setInt(3,objeto.getTpCarga());
			pstmt.setString(4,objeto.getNrCapacidade());
			pstmt.setInt(5,objeto.getTpEixoDianteiro());
			pstmt.setInt(6,objeto.getTpEixoTraseiro());
			pstmt.setInt(7,objeto.getQtEixosDianteiros());
			pstmt.setInt(8,objeto.getQtEixosTraseiros());
			if (objetoTemp != null) {
				pstmt.setInt(9, cdReboqueOld!=0 ? cdReboqueOld : objeto.getCdReferencia());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.ReferenciaDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdReboque) {
		return delete(cdReboque, null);
	}

	public static int delete(int cdReboque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_reboque WHERE cd_reboque=?");
			pstmt.setInt(1, cdReboque);
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.ReferenciaDAO.delete(cdReboque, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Reboque get(int cdReboque) {
		return get(cdReboque, null);
	}

	public static Reboque get(int cdReboque, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_reboque A, bpm_referencia B WHERE A.cd_reboque=B.cd_referencia AND A.cd_reboque=?");
			pstmt.setInt(1, cdReboque);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Reboque(rs.getInt("cd_referencia"),
						rs.getInt("cd_bem"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_marca"),
						(rs.getTimestamp("dt_aquisicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aquisicao").getTime()),
						(rs.getTimestamp("dt_garantia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_garantia").getTime()),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						(rs.getTimestamp("dt_baixa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_baixa").getTime()),
						rs.getString("nr_serie"),
						rs.getString("nr_tombo"),
						rs.getInt("st_referencia"),
						rs.getString("nm_modelo"),
						(rs.getTimestamp("dt_incorporacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_incorporacao").getTime()),
						rs.getFloat("qt_capacidade"),
						rs.getInt("lg_producao"),
						rs.getString("id_referencia"),
						rs.getInt("cd_local_armazenamento"),
						rs.getInt("tp_reboque"),
						rs.getInt("tp_carga"),
						rs.getString("nr_capacidade"),
						rs.getInt("tp_eixo_dianteiro"),
						rs.getInt("tp_eixo_traseiro"),
						rs.getInt("qt_eixos_dianteiros"),
						rs.getInt("qt_eixos_traseiros"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_reboque");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReboqueDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_reboque", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
