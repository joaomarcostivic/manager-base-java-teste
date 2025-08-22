package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ViagemAgendamentoDAO{

	public static int insert(ViagemAgendamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(ViagemAgendamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_viagem_agendamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdViagemAgendamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_viagem_agendamento (cd_viagem_agendamento,"+
			                                  "cd_motorista,"+
			                                  "cd_concessao_veiculo,"+
			                                  "txt_descricao,"+
			                                  "dt_registro,"+
			                                  "hr_partida_inicio,"+
			                                  "hr_chegada_inicio,"+
			                                  "hr_partida_final,"+
			                                  "hr_chegada_final,"+
			                                  "lg_livre_intervalo,"+
			                                  "st_viagem_agendamento,"+
			                                  "cd_instituicao,"+
			                                  "cd_setor,"+
			                                  "nr_capacidade,"+
			                                  "lg_segunda,"+
			                                  "lg_terca,"+
			                                  "lg_quarta,"+
			                                  "lg_quinta,"+
			                                  "lg_sexta,"+
			                                  "lg_sabado,"+
			                                  "lg_domingo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMotorista());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConcessaoVeiculo());
			pstmt.setString(4,objeto.getTxtDescricao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			if(objeto.getHrPartidaInicio()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getHrPartidaInicio().getTimeInMillis()));
			if(objeto.getHrChegadaInicio()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getHrChegadaInicio().getTimeInMillis()));
			if(objeto.getHrPartidaFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getHrPartidaFinal().getTimeInMillis()));
			if(objeto.getHrChegadaFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getHrChegadaFinal().getTimeInMillis()));
			pstmt.setInt(10,objeto.getLgLivreIntervalo());
			pstmt.setInt(11,objeto.getStViagemAgendamento());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdInstituicao());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdSetor());
			pstmt.setInt(14,objeto.getNrCapacidade());
			pstmt.setInt(15,objeto.getLgSegunda());
			pstmt.setInt(16,objeto.getLgTerca());
			pstmt.setInt(17,objeto.getLgQuarta());
			pstmt.setInt(18,objeto.getLgQuinta());
			pstmt.setInt(19,objeto.getLgSexta());
			pstmt.setInt(20,objeto.getLgSabado());
			pstmt.setInt(21,objeto.getLgDomingo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ViagemAgendamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ViagemAgendamento objeto, int cdViagemAgendamentoOld) {
		return update(objeto, cdViagemAgendamentoOld, null);
	}

	public static int update(ViagemAgendamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ViagemAgendamento objeto, int cdViagemAgendamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_viagem_agendamento SET cd_viagem_agendamento=?,"+
												      		   "cd_motorista=?,"+
												      		   "cd_concessao_veiculo=?,"+
												      		   "txt_descricao=?,"+
												      		   "dt_registro=?,"+
												      		   "hr_partida_inicio=?,"+
												      		   "hr_chegada_inicio=?,"+
												      		   "hr_partida_final=?,"+
												      		   "hr_chegada_final=?,"+
												      		   "lg_livre_intervalo=?,"+
												      		   "st_viagem_agendamento=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_setor=?,"+
												      		   "nr_capacidade=?,"+
												      		   "lg_segunda=?,"+
												      		   "lg_terca=?,"+
												      		   "lg_quarta=?,"+
												      		   "lg_quinta=?,"+
												      		   "lg_sexta=?,"+
												      		   "lg_sabado=?,"+
												      		   "lg_domingo=? WHERE cd_viagem_agendamento=?");
			pstmt.setInt(1,objeto.getCdViagemAgendamento());
			if(objeto.getCdMotorista()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMotorista());
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConcessaoVeiculo());
			pstmt.setString(4,objeto.getTxtDescricao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			if(objeto.getHrPartidaInicio()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getHrPartidaInicio().getTimeInMillis()));
			if(objeto.getHrChegadaInicio()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getHrChegadaInicio().getTimeInMillis()));
			if(objeto.getHrPartidaFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getHrPartidaFinal().getTimeInMillis()));
			if(objeto.getHrChegadaFinal()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getHrChegadaFinal().getTimeInMillis()));
			pstmt.setInt(10,objeto.getLgLivreIntervalo());
			pstmt.setInt(11,objeto.getStViagemAgendamento());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdInstituicao());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdSetor());
			pstmt.setInt(14,objeto.getNrCapacidade());
			pstmt.setInt(15,objeto.getLgSegunda());
			pstmt.setInt(16,objeto.getLgTerca());
			pstmt.setInt(17,objeto.getLgQuarta());
			pstmt.setInt(18,objeto.getLgQuinta());
			pstmt.setInt(19,objeto.getLgSexta());
			pstmt.setInt(20,objeto.getLgSabado());
			pstmt.setInt(21,objeto.getLgDomingo());
			pstmt.setInt(22, cdViagemAgendamentoOld!=0 ? cdViagemAgendamentoOld : objeto.getCdViagemAgendamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdViagemAgendamento) {
		return delete(cdViagemAgendamento, null);
	}

	public static int delete(int cdViagemAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_viagem_agendamento WHERE cd_viagem_agendamento=?");
			pstmt.setInt(1, cdViagemAgendamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ViagemAgendamento get(int cdViagemAgendamento) {
		return get(cdViagemAgendamento, null);
	}

	public static ViagemAgendamento get(int cdViagemAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_viagem_agendamento WHERE cd_viagem_agendamento=?");
			pstmt.setInt(1, cdViagemAgendamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ViagemAgendamento(rs.getInt("cd_viagem_agendamento"),
						rs.getInt("cd_motorista"),
						rs.getInt("cd_concessao_veiculo"),
						rs.getString("txt_descricao"),
						(rs.getTimestamp("dt_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro").getTime()),
						(rs.getTimestamp("hr_partida_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida_inicio").getTime()),
						(rs.getTimestamp("hr_chegada_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada_inicio").getTime()),
						(rs.getTimestamp("hr_partida_final")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_partida_final").getTime()),
						(rs.getTimestamp("hr_chegada_final")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_chegada_final").getTime()),
						rs.getInt("lg_livre_intervalo"),
						rs.getInt("st_viagem_agendamento"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_setor"),
						rs.getInt("nr_capacidade"),
						rs.getInt("lg_segunda"),
						rs.getInt("lg_terca"),
						rs.getInt("lg_quarta"),
						rs.getInt("lg_quinta"),
						rs.getInt("lg_sexta"),
						rs.getInt("lg_sabado"),
						rs.getInt("lg_domingo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_viagem_agendamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ViagemAgendamento> getList() {
		return getList(null);
	}

	public static ArrayList<ViagemAgendamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ViagemAgendamento> list = new ArrayList<ViagemAgendamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ViagemAgendamento obj = ViagemAgendamentoDAO.get(rsm.getInt("cd_viagem_agendamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_viagem_agendamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}