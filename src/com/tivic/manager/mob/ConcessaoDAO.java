package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ConcessaoDAO{

	public static int insert(Concessao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Concessao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_concessao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConcessao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_concessao (cd_concessao,"+
			                                  "dt_inicio_concessao,"+
			                                  "dt_final_concessao,"+
			                                  "id_concessao,"+
			                                  "st_concessao,"+
			                                  "nr_concessao,"+
			                                  "txt_observacao,"+
			                                  "cd_concessionario,"+
			                                  "cd_frota,"+
			                                  "cd_veiculo,"+
			                                  "tp_concessao,"+
			                                  "lg_renovavel,"+
			                                  "nr_prazo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtInicioConcessao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicioConcessao().getTimeInMillis()));
			if(objeto.getDtFinalConcessao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFinalConcessao().getTimeInMillis()));
			pstmt.setString(4,objeto.getIdConcessao());
			pstmt.setInt(5,objeto.getStConcessao());
			pstmt.setString(6,objeto.getNrConcessao());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdConcessionario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConcessionario());
			if(objeto.getCdFrota()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFrota());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdVeiculo());
			pstmt.setInt(11,objeto.getTpConcessao());
			pstmt.setInt(12,objeto.getLgRenovavel());
			pstmt.setInt(13,objeto.getNrPrazo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Concessao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Concessao objeto, int cdConcessaoOld) {
		return update(objeto, cdConcessaoOld, null);
	}

	public static int update(Concessao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Concessao objeto, int cdConcessaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_concessao SET cd_concessao=?,"+
												      		   "dt_inicio_concessao=?,"+
												      		   "dt_final_concessao=?,"+
												      		   "id_concessao=?,"+
												      		   "st_concessao=?,"+
												      		   "nr_concessao=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_concessionario=?,"+
												      		   "cd_frota=?,"+
												      		   "cd_veiculo=?,"+
												      		   "tp_concessao=?,"+
												      		   "lg_renovavel=?,"+
												      		   "nr_prazo=? WHERE cd_concessao=?");
			pstmt.setInt(1,objeto.getCdConcessao());
			if(objeto.getDtInicioConcessao()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicioConcessao().getTimeInMillis()));
			if(objeto.getDtFinalConcessao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFinalConcessao().getTimeInMillis()));
			pstmt.setString(4,objeto.getIdConcessao());
			pstmt.setInt(5,objeto.getStConcessao());
			pstmt.setString(6,objeto.getNrConcessao());
			pstmt.setString(7,objeto.getTxtObservacao());
			if(objeto.getCdConcessionario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConcessionario());
			if(objeto.getCdFrota()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFrota());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdVeiculo());
			pstmt.setInt(11,objeto.getTpConcessao());
			pstmt.setInt(12,objeto.getLgRenovavel());
			pstmt.setInt(13,objeto.getNrPrazo());
			pstmt.setInt(14, cdConcessaoOld!=0 ? cdConcessaoOld : objeto.getCdConcessao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessao) {
		return delete(cdConcessao, null);
	}

	public static int delete(int cdConcessao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_concessao WHERE cd_concessao=?");
			pstmt.setInt(1, cdConcessao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Concessao get(int cdConcessao) {
		return get(cdConcessao, null);
	}

	public static Concessao get(int cdConcessao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao WHERE cd_concessao=?");
			pstmt.setInt(1, cdConcessao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Concessao(rs.getInt("cd_concessao"),
						(rs.getTimestamp("dt_inicio_concessao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_concessao").getTime()),
						(rs.getTimestamp("dt_final_concessao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_concessao").getTime()),
						rs.getString("id_concessao"),
						rs.getInt("st_concessao"),
						rs.getString("nr_concessao"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_concessionario"),
						rs.getInt("cd_frota"),
						rs.getInt("cd_veiculo"),
						rs.getInt("tp_concessao"),
						rs.getInt("lg_renovavel"),
						rs.getInt("nr_prazo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Concessao> getList() {
		return getList(null);
	}

	public static ArrayList<Concessao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Concessao> list = new ArrayList<Concessao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Concessao obj = ConcessaoDAO.get(rsm.getInt("cd_concessao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_concessao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
