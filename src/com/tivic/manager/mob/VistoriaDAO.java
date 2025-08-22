package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class VistoriaDAO{

	public static int insert(Vistoria objeto) {
		return insert(objeto, null);
	}

	public static int insert(Vistoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_vistoria", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVistoria(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_vistoria (cd_vistoria,"+
			                                  "dt_vistoria,"+
			                                  "cd_agente,"+
			                                  "cd_pessoa,"+
			                                  "cd_equipamento,"+
			                                  "cd_veiculo,"+
			                                  "cd_plano_vistoria,"+
			                                  "cd_vistoria_anterior,"+
			                                  "st_vistoria,"+
			                                  "dt_aplicacao,"+
			                                  "tp_vistoria,"+
			                                  "ds_observacao,"+
			                                  "id_selo,"+
			                                  "cd_vistoriador,"+
			                                  "cd_condutor,"+
			                                  "cd_concessao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtVistoria()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtVistoria().getTimeInMillis()));
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPessoa());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEquipamento());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdVeiculo());
			pstmt.setInt(7,objeto.getCdPlanoVistoria());
			pstmt.setInt(8,objeto.getCdVistoriaAnterior());
			pstmt.setInt(9,objeto.getStVistoria());
			if(objeto.getDtAplicacao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtAplicacao().getTimeInMillis()));
			pstmt.setInt(11,objeto.getTpVistoria());
			pstmt.setString(12,objeto.getDsObservacao());
			pstmt.setString(13,objeto.getIdSelo());
			if(objeto.getCdVistoriador()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdVistoriador());
			if(objeto.getCdCondutor()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdCondutor());
			pstmt.setInt(16,objeto.getCdConcessao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Vistoria objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Vistoria objeto, int cdVistoriaOld) {
		return update(objeto, cdVistoriaOld, null);
	}

	public static int update(Vistoria objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Vistoria objeto, int cdVistoriaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_vistoria SET cd_vistoria=?,"+
												      		   "dt_vistoria=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_equipamento=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_plano_vistoria=?,"+
												      		   "cd_vistoria_anterior=?,"+
												      		   "st_vistoria=?,"+
												      		   "dt_aplicacao=?,"+
												      		   "tp_vistoria=?,"+
												      		   "ds_observacao=?,"+
												      		   "id_selo=?,"+
												      		   "cd_vistoriador=?,"+
												      		   "cd_condutor=?,"+
												      		   "cd_concessao=? WHERE cd_vistoria=?");
			pstmt.setInt(1,objeto.getCdVistoria());
			if(objeto.getDtVistoria()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtVistoria().getTimeInMillis()));
			if(objeto.getCdAgente()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAgente());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPessoa());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEquipamento());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdVeiculo());
			pstmt.setInt(7,objeto.getCdPlanoVistoria());
			pstmt.setInt(8,objeto.getCdVistoriaAnterior());
			pstmt.setInt(9,objeto.getStVistoria());
			if(objeto.getDtAplicacao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtAplicacao().getTimeInMillis()));
			pstmt.setInt(11,objeto.getTpVistoria());
			pstmt.setString(12,objeto.getDsObservacao());
			pstmt.setString(13,objeto.getIdSelo());
			if(objeto.getCdVistoriador()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdVistoriador());
			if(objeto.getCdCondutor()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdCondutor());
			pstmt.setInt(16,objeto.getCdConcessao());
			pstmt.setInt(17, cdVistoriaOld!=0 ? cdVistoriaOld : objeto.getCdVistoria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVistoria) {
		return delete(cdVistoria, null);
	}

	public static int delete(int cdVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_vistoria WHERE cd_vistoria=?");
			pstmt.setInt(1, cdVistoria);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Vistoria get(int cdVistoria) {
		return get(cdVistoria, null);
	}

	public static Vistoria get(int cdVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria WHERE cd_vistoria=?");
			pstmt.setInt(1, cdVistoria);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Vistoria(rs.getInt("cd_vistoria"),
						(rs.getTimestamp("dt_vistoria")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vistoria").getTime()),
						rs.getInt("cd_agente"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_equipamento"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_plano_vistoria"),
						rs.getInt("cd_vistoria_anterior"),
						rs.getInt("st_vistoria"),
						(rs.getTimestamp("dt_aplicacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aplicacao").getTime()),
						rs.getInt("tp_vistoria"),
						rs.getString("ds_observacao"),
						rs.getString("id_selo"),
						rs.getInt("cd_vistoriador"),
						rs.getInt("cd_condutor"),
						rs.getInt("cd_concessao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Vistoria> getList() {
		return getList(null);
	}

	public static ArrayList<Vistoria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Vistoria> list = new ArrayList<Vistoria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Vistoria obj = VistoriaDAO.get(rsm.getInt("cd_vistoria"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
