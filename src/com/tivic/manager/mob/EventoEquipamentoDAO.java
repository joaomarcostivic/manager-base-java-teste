package com.tivic.manager.mob;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class EventoEquipamentoDAO{

	public static int insert(EventoEquipamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoEquipamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_evento_equipamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEvento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_evento_equipamento (cd_evento,"+
			                                  "cd_equipamento,"+
			                                  "cd_tipo_evento,"+
			                                  "dt_evento,"+
			                                  "nm_orgao_autuador,"+
			                                  "nm_equipamento,"+
			                                  "ds_local,"+
			                                  "id_identificacao_inmetro,"+
			                                  "dt_afericao,"+
			                                  "nr_pista,"+
			                                  "dt_conclusao,"+
			                                  "vl_limite,"+
			                                  "vl_velocidade_tolerada,"+
			                                  "vl_medida,"+
			                                  "vl_considerada,"+
			                                  "nr_placa,"+
			                                  "lg_tempo_real,"+
			                                  "tp_veiculo,"+
			                                  "vl_comprimento_veiculo,"+
			                                  "id_medida,"+
			                                  "nr_serial,"+
			                                  "nm_modelo_equipamento,"+
			                                  "nm_rodovia,"+
			                                  "nm_uf_rodovia,"+
			                                  "nm_km_rodovia,"+
			                                  "nm_metros_rodovia,"+
			                                  "nm_sentido_rodovia,"+
			                                  "id_cidade,"+
			                                  "nm_marca_equipamento,"+
			                                  "tp_equipamento,"+
			                                  "lg_valida_funcionamento,"+
			                                  "dt_execucao_job,"+
			                                  "id_uuid,"+
			                                  "tp_restricao,"+
			                                  "tp_classificacao,"+
			                                  "vl_permanencia,"+
			                                  "vl_semaforo_vermelho,"+
			                                  "st_evento,"+
			                                  "cd_motivo_cancelamento,"+
			                                  "txt_evento,"+
			                                  "lg_olpr,"+
			                                  "dt_cancelamento,"+
			                                  "cd_usuario_cancelamento,"+
			                                  "dt_processamento,"+
			                                  "cd_veiculo,"+
			                                  "lg_enviado,"+
			                                  "cd_usuario_confirmacao,"+
			                                  "vl_tolerancia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEquipamento());
			if(objeto.getCdTipoEvento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoEvento());
			if(objeto.getDtEvento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEvento().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmOrgaoAutuador());
			pstmt.setString(6,objeto.getNmEquipamento());
			pstmt.setString(7,objeto.getDsLocal());
			pstmt.setString(8,objeto.getIdIdentificacaoInmetro());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setInt(10,objeto.getNrPista());
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(12,objeto.getVlLimite());
			pstmt.setInt(13,objeto.getVlVelocidadeTolerada());
			pstmt.setInt(14,objeto.getVlMedida());
			pstmt.setInt(15,objeto.getVlConsiderada());
			pstmt.setString(16,objeto.getNrPlaca());
			pstmt.setInt(17,objeto.getLgTempoReal());
			pstmt.setInt(18,objeto.getTpVeiculo());
			pstmt.setInt(19,objeto.getVlComprimentoVeiculo());
			pstmt.setInt(20,objeto.getIdMedida());
			pstmt.setString(21,objeto.getNrSerial());
			pstmt.setString(22,objeto.getNmModeloEquipamento());
			pstmt.setString(23,objeto.getNmRodovia());
			pstmt.setString(24,objeto.getNmUfRodovia());
			pstmt.setString(25,objeto.getNmKmRodovia());
			pstmt.setString(26,objeto.getNmMetrosRodovia());
			pstmt.setString(27,objeto.getNmSentidoRodovia());
			pstmt.setInt(28,objeto.getIdCidade());
			pstmt.setString(29,objeto.getNmMarcaEquipamento());
			pstmt.setInt(30,objeto.getTpEquipamento());
			pstmt.setInt(31,objeto.getLgValidaFuncionamento());
			if(objeto.getDtExecucaoJob()==null)
				pstmt.setNull(32, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(32,new Timestamp(objeto.getDtExecucaoJob().getTimeInMillis()));
			pstmt.setString(33,objeto.getIdUuid());
			pstmt.setInt(34,objeto.getTpRestricao());
			pstmt.setInt(35,objeto.getTpClassificacao());
			pstmt.setInt(36,objeto.getVlPermanencia());
			pstmt.setInt(37,objeto.getVlSemaforoVermelho());
			pstmt.setInt(38,objeto.getStEvento());
			pstmt.setInt(39,objeto.getCdMotivoCancelamento());
			pstmt.setString(40,objeto.getTxtEvento());
			pstmt.setInt(41,objeto.getLgOlpr());
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(42, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(42,new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			if(objeto.getCdUsuarioCancelamento()==0)
				pstmt.setNull(43, Types.INTEGER);
			else
				pstmt.setInt(43,objeto.getCdUsuarioCancelamento());
			if(objeto.getDtProcessamento()==null)
				pstmt.setNull(44, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(44,new Timestamp(objeto.getDtProcessamento().getTimeInMillis()));
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(45, Types.INTEGER);
			else
				pstmt.setInt(45,objeto.getCdVeiculo());
			pstmt.setInt(46,objeto.getLgEnviado());
			if(objeto.getCdUsuarioConfirmacao()==0)
				pstmt.setNull(47, Types.INTEGER);
			else
				pstmt.setInt(47,objeto.getCdUsuarioConfirmacao());
			pstmt.setInt(48,objeto.getVlTolerancia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoEquipamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(EventoEquipamento objeto, int cdEventoOld) {
		return update(objeto, cdEventoOld, null);
	}

	public static int update(EventoEquipamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(EventoEquipamento objeto, int cdEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_evento_equipamento SET cd_evento=?,"+
												      		   "cd_equipamento=?,"+
												      		   "cd_tipo_evento=?,"+
												      		   "dt_evento=?,"+
												      		   "nm_orgao_autuador=?,"+
												      		   "nm_equipamento=?,"+
												      		   "ds_local=?,"+
												      		   "id_identificacao_inmetro=?,"+
												      		   "dt_afericao=?,"+
												      		   "nr_pista=?,"+
												      		   "dt_conclusao=?,"+
												      		   "vl_limite=?,"+
												      		   "vl_velocidade_tolerada=?,"+
												      		   "vl_medida=?,"+
												      		   "vl_considerada=?,"+
												      		   "nr_placa=?,"+
												      		   "lg_tempo_real=?,"+
												      		   "tp_veiculo=?,"+
												      		   "vl_comprimento_veiculo=?,"+
												      		   "id_medida=?,"+
												      		   "nr_serial=?,"+
												      		   "nm_modelo_equipamento=?,"+
												      		   "nm_rodovia=?,"+
												      		   "nm_uf_rodovia=?,"+
												      		   "nm_km_rodovia=?,"+
												      		   "nm_metros_rodovia=?,"+
												      		   "nm_sentido_rodovia=?,"+
												      		   "id_cidade=?,"+
												      		   "nm_marca_equipamento=?,"+
												      		   "tp_equipamento=?,"+
												      		   "lg_valida_funcionamento=?,"+
												      		   "dt_execucao_job=?,"+
												      		   "id_uuid=?,"+
												      		   "tp_restricao=?,"+
												      		   "tp_classificacao=?,"+
												      		   "vl_permanencia=?,"+
												      		   "vl_semaforo_vermelho=?,"+
												      		   "st_evento=?,"+
												      		   "cd_motivo_cancelamento=?,"+
												      		   "txt_evento=?,"+
												      		   "lg_olpr=?,"+
												      		   "dt_cancelamento=?,"+
												      		   "cd_usuario_cancelamento=?,"+
												      		   "dt_processamento=?,"+
												      		   "cd_veiculo=?,"+
												      		   "lg_enviado=?,"+
												      		   "cd_usuario_confirmacao=?,"+
												      		   "vl_tolerancia=? WHERE cd_evento=?");

			pstmt.setInt(1,objeto.getCdEvento());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEquipamento());
			if(objeto.getCdTipoEvento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoEvento());
			if(objeto.getDtEvento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEvento().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmOrgaoAutuador());
			pstmt.setString(6,objeto.getNmEquipamento());
			pstmt.setString(7,objeto.getDsLocal());
			pstmt.setString(8,objeto.getIdIdentificacaoInmetro());
			if(objeto.getDtAfericao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtAfericao().getTimeInMillis()));
			pstmt.setInt(10,objeto.getNrPista());
			if(objeto.getDtConclusao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtConclusao().getTimeInMillis()));
			pstmt.setInt(12,objeto.getVlLimite());
			pstmt.setInt(13,objeto.getVlVelocidadeTolerada());
			pstmt.setInt(14,objeto.getVlMedida());
			pstmt.setInt(15,objeto.getVlConsiderada());
			pstmt.setString(16,objeto.getNrPlaca());
			pstmt.setInt(17,objeto.getLgTempoReal());
			pstmt.setInt(18,objeto.getTpVeiculo());
			pstmt.setInt(19,objeto.getVlComprimentoVeiculo());
			pstmt.setInt(20,objeto.getIdMedida());
			pstmt.setString(21,objeto.getNrSerial());
			pstmt.setString(22,objeto.getNmModeloEquipamento());
			pstmt.setString(23,objeto.getNmRodovia());
			pstmt.setString(24,objeto.getNmUfRodovia());
			pstmt.setString(25,objeto.getNmKmRodovia());
			pstmt.setString(26,objeto.getNmMetrosRodovia());
			pstmt.setString(27,objeto.getNmSentidoRodovia());
			pstmt.setInt(28,objeto.getIdCidade());
			pstmt.setString(29,objeto.getNmMarcaEquipamento());
			pstmt.setInt(30,objeto.getTpEquipamento());
			pstmt.setInt(31,objeto.getLgValidaFuncionamento());
			if(objeto.getDtExecucaoJob()==null)
				pstmt.setNull(32, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(32,new Timestamp(objeto.getDtExecucaoJob().getTimeInMillis()));
			pstmt.setString(33,objeto.getIdUuid());
			pstmt.setInt(34,objeto.getTpRestricao());
			pstmt.setInt(35,objeto.getTpClassificacao());
			pstmt.setInt(36,objeto.getVlPermanencia());
			pstmt.setInt(37,objeto.getVlSemaforoVermelho());
			pstmt.setInt(38,objeto.getStEvento());
			pstmt.setInt(39,objeto.getCdMotivoCancelamento());
			pstmt.setString(40,objeto.getTxtEvento());
			pstmt.setInt(41,objeto.getLgOlpr());
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(42, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(42,new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			if(objeto.getCdUsuarioCancelamento()==0)
				pstmt.setNull(43, Types.INTEGER);
			else
				pstmt.setInt(43,objeto.getCdUsuarioCancelamento());
			if(objeto.getDtProcessamento()==null)
				pstmt.setNull(44, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(44,new Timestamp(objeto.getDtProcessamento().getTimeInMillis()));
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(45, Types.INTEGER);
			else
				pstmt.setInt(45,objeto.getCdVeiculo());
			pstmt.setInt(46,objeto.getLgEnviado());
			if(objeto.getCdUsuarioConfirmacao()==0)
				pstmt.setNull(47, Types.INTEGER);
			else
				pstmt.setInt(47,objeto.getCdUsuarioConfirmacao());
			pstmt.setInt(48,objeto.getVlTolerancia());
			pstmt.setInt(49, cdEventoOld!=0 ? cdEventoOld : objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEvento) {
		return delete(cdEvento, null);
	}

	public static int delete(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_evento_equipamento WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoEquipamento get(int cdEvento) {
		return get(cdEvento, null);
	}

	public static EventoEquipamento get(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_equipamento WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoEquipamento(rs.getInt("cd_evento"),
						rs.getInt("cd_equipamento"),
						rs.getInt("cd_tipo_evento"),
						(rs.getTimestamp("dt_evento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_evento").getTime()),
						rs.getString("nm_orgao_autuador"),
						rs.getString("nm_equipamento"),
						rs.getString("ds_local"),
						rs.getString("id_identificacao_inmetro"),
						(rs.getTimestamp("dt_afericao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_afericao").getTime()),
						rs.getInt("nr_pista"),
						(rs.getTimestamp("dt_conclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_conclusao").getTime()),
						rs.getInt("vl_limite"),
						rs.getInt("vl_velocidade_tolerada"),
						rs.getInt("vl_medida"),
						rs.getInt("vl_considerada"),
						rs.getString("nr_placa"),
						rs.getInt("lg_tempo_real"),
						rs.getInt("tp_veiculo"),
						rs.getInt("vl_comprimento_veiculo"),
						rs.getInt("id_medida"),
						rs.getString("nr_serial"),
						rs.getString("nm_modelo_equipamento"),
						rs.getString("nm_rodovia"),
						rs.getString("nm_uf_rodovia"),
						rs.getString("nm_km_rodovia"),
						rs.getString("nm_metros_rodovia"),
						rs.getString("nm_sentido_rodovia"),
						rs.getInt("id_cidade"),
						rs.getString("nm_marca_equipamento"),
						rs.getInt("tp_equipamento"),
						rs.getInt("lg_valida_funcionamento"),
						(rs.getTimestamp("dt_execucao_job")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_execucao_job").getTime()),
						rs.getString("id_uuid"),
						rs.getInt("tp_restricao"),
						rs.getInt("tp_classificacao"),
						rs.getInt("vl_permanencia"),
						rs.getInt("vl_semaforo_vermelho"),
						rs.getInt("st_evento"),
						rs.getInt("cd_motivo_cancelamento"),
						rs.getString("txt_evento"),
						rs.getInt("lg_olpr"),
						(rs.getTimestamp("dt_cancelamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						rs.getInt("cd_usuario_cancelamento"),
						(rs.getTimestamp("dt_processamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_processamento").getTime()),
						rs.getInt("cd_veiculo"),
						rs.getInt("lg_enviado"),
						rs.getInt("cd_usuario_confirmacao"),
						rs.getInt("vl_tolerancia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_equipamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EventoEquipamento> getList() {
		return getList(null);
	}

	public static ArrayList<EventoEquipamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoEquipamento> list = new ArrayList<EventoEquipamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoEquipamento obj = EventoEquipamentoDAO.get(rsm.getInt("cd_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoEquipamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_evento_equipamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
