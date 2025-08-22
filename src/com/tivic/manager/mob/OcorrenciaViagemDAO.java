package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OcorrenciaViagemDAO{

	public static int insert(OcorrenciaViagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(OcorrenciaViagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.OcorrenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ocorrencia_viagem (cd_ocorrencia,"+
			                                  "cd_viagem_anterior,"+
			                                  "cd_viagem_posterior,"+
			                                  "cd_concessao_veiculo_anterior,"+
			                                  "cd_concessao_veiculo_posterior,"+
			                                  "cd_motorista_anterior,"+
			                                  "cd_motorista_posterior,"+
			                                  "st_viagem_anterior,"+
			                                  "st_viagem_posterior,"+
			                                  "hr_partida_anterior,"+
			                                  "hr_chegada_anterior,"+
			                                  "hr_partida_posterior,"+
			                                  "hr_chegada_posterior,"+
			                                  "cd_viagem_agendamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdViagemAnterior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdViagemAnterior());
			if(objeto.getCdViagemPosterior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdViagemPosterior());
			if(objeto.getCdConcessaoVeiculoAnterior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConcessaoVeiculoAnterior());
			if(objeto.getCdConcessaoVeiculoPosterior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdConcessaoVeiculoPosterior());
			if(objeto.getCdMotoristaAnterior()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMotoristaAnterior());
			if(objeto.getCdMotoristaPosterior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdMotoristaPosterior());
			pstmt.setInt(8,objeto.getStViagemAnterior());
			pstmt.setInt(9,objeto.getStViagemPosterior());
			if(objeto.getHrPartidaAnterior()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getHrPartidaAnterior().getTimeInMillis()));
			if(objeto.getHrChegadaAnterior()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getHrChegadaAnterior().getTimeInMillis()));
			if(objeto.getHrPartidaPosterior()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getHrPartidaPosterior().getTimeInMillis()));
			if(objeto.getHrChegadaPosterior()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getHrChegadaPosterior().getTimeInMillis()));
			if(objeto.getCdViagemAgendamento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdViagemAgendamento());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OcorrenciaViagem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OcorrenciaViagem objeto, int cdOcorrenciaOld) {
		return update(objeto, cdOcorrenciaOld, null);
	}

	public static int update(OcorrenciaViagem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OcorrenciaViagem objeto, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			OcorrenciaViagem objetoTemp = get(objeto.getCdOcorrencia(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO mob_ocorrencia_viagem (cd_ocorrencia,"+
			                                  "cd_viagem_anterior,"+
			                                  "cd_viagem_posterior,"+
			                                  "cd_concessao_veiculo_anterior,"+
			                                  "cd_concessao_veiculo_posterior,"+
			                                  "cd_motorista_anterior,"+
			                                  "cd_motorista_posterior,"+
			                                  "st_viagem_anterior,"+
			                                  "st_viagem_posterior,"+
			                                  "hr_partida_anterior,"+
			                                  "hr_chegada_anterior,"+
			                                  "hr_partida_posterior,"+
			                                  "hr_chegada_posterior,"+
			                                  "cd_viagem_agendamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE mob_ocorrencia_viagem SET cd_ocorrencia=?,"+
												      		   "cd_viagem_anterior=?,"+
												      		   "cd_viagem_posterior=?,"+
												      		   "cd_concessao_veiculo_anterior=?,"+
												      		   "cd_concessao_veiculo_posterior=?,"+
												      		   "cd_motorista_anterior=?,"+
												      		   "cd_motorista_posterior=?,"+
												      		   "st_viagem_anterior=?,"+
												      		   "st_viagem_posterior=?,"+
												      		   "hr_partida_anterior=?,"+
												      		   "hr_chegada_anterior=?,"+
												      		   "hr_partida_posterior=?,"+
												      		   "hr_chegada_posterior=?,"+
												      		   "cd_viagem_agendamento=? WHERE cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdViagemAnterior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdViagemAnterior());
			if(objeto.getCdViagemPosterior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdViagemPosterior());
			if(objeto.getCdConcessaoVeiculoAnterior()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdConcessaoVeiculoAnterior());
			if(objeto.getCdConcessaoVeiculoPosterior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdConcessaoVeiculoPosterior());
			if(objeto.getCdMotoristaAnterior()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdMotoristaAnterior());
			if(objeto.getCdMotoristaPosterior()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdMotoristaPosterior());
			pstmt.setInt(8,objeto.getStViagemAnterior());
			pstmt.setInt(9,objeto.getStViagemPosterior());
			if(objeto.getHrPartidaAnterior()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getHrPartidaAnterior().getTimeInMillis()));
			if(objeto.getHrChegadaAnterior()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getHrChegadaAnterior().getTimeInMillis()));
			if(objeto.getHrPartidaPosterior()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getHrPartidaPosterior().getTimeInMillis()));
			if(objeto.getHrChegadaPosterior()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getHrChegadaPosterior().getTimeInMillis()));
			if(objeto.getCdViagemAgendamento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdViagemAgendamento());
			if (objetoTemp != null) {
				pstmt.setInt(15, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! OcorrenciaViagemDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia) {
		return delete(cdOcorrencia, null);
	}

	public static int delete(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ocorrencia_viagem WHERE cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.delete(cdOcorrencia, connect)<=0) {
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
			System.err.println("Erro! OcorrenciaViagemDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OcorrenciaViagem get(int cdOcorrencia) {
		return get(cdOcorrencia, null);
	}

	public static OcorrenciaViagem get(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_viagem A, grl_ocorrencia B WHERE A.cd_ocorrencia=B.cd_ocorrencia AND A.cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OcorrenciaViagem(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("st_ocorrencia"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_viagem_anterior"),
						rs.getInt("cd_viagem_posterior"),
						rs.getInt("cd_concessao_veiculo_anterior"),
						rs.getInt("cd_concessao_veiculo_posterior"),
						rs.getInt("cd_motorista_anterior"),
						rs.getInt("cd_motorista_posterior"),
						rs.getInt("st_viagem_anterior"),
						rs.getInt("st_viagem_posterior"),
						(rs.getTimestamp("hr_partida_anterior")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida_anterior").getTime()),
						(rs.getTimestamp("hr_chegada_anterior")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada_anterior").getTime()),
						(rs.getTimestamp("hr_partida_posterior")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida_posterior").getTime()),
						(rs.getTimestamp("hr_chegada_posterior")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada_posterior").getTime()),
						rs.getInt("cd_viagem_agendamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_viagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OcorrenciaViagem> getList() {
		return getList(null);
	}

	public static ArrayList<OcorrenciaViagem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OcorrenciaViagem> list = new ArrayList<OcorrenciaViagem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OcorrenciaViagem obj = OcorrenciaViagemDAO.get(rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ocorrencia_viagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}