package com.tivic.manager.mob;

import java.sql.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AitTransporteDAO{

	public static int insert(AitTransporte objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_ait_transporte", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAit(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_transporte (cd_ait,"+
			                                  "cd_infracao,"+
			                                  "ds_providencia,"+
			                                  "dt_prazo,"+
			                                  "cd_agente,"+
			                                  "nm_preposto,"+
			                                  "nr_ait,"+
			                                  "ds_observacao,"+
			                                  "dt_infracao,"+
			                                  "dt_emissao_nip,"+
			                                  "nr_via_nip,"+
			                                  "st_ait,"+
			                                  "cd_motivo,"+
			                                  "cd_usuario_cancelamento,"+
			                                  "cd_ocorrencia,"+
			                                  "vl_longitude,"+
			                                  "vl_latitude,"+
			                                  "cd_cidade,"+
			                                  "cd_equipamento,"+
			                                  "nr_ponto,"+
			                                  "ds_local_infracao,"+
			                                  "lg_reincidencia,"+
			                                  "nm_testemunha1,"+
			                                  "nr_rg_testemunha1,"+
			                                  "nm_endereco_testemunha1,"+
			                                  "nm_testemunha2,"+
			                                  "nr_rg_testemunha2,"+
			                                  "nm_endereco_testemunha2,"+
			                                  "tp_ait,"+
			                                  "cd_talao,"+
			                                  "dt_notificacao_inicial,"+
			                                  "cd_concessao_veiculo,"+
			                                  "cd_linha,"+
			                                  "cd_recurso1,"+
			                                  "cd_recurso2,"+
			                                  "cd_conta_receber,"+
			                                  "cd_concessao,"+
			                                  "dt_limite_analisar_recurso1,"+
			                                  "dt_limite_notificar_recurso1,"+
			                                  "dt_limite_recurso2,"+
			                                  "dt_limite_analisar_recurso2,"+
			                                  "dt_limite_notificar_recurso2,"+
			                                  "dt_julgamento1,"+
			                                  "dt_julgamento2,"+
			                                  "dt_cancelamento,"+
			                                  "dt_notificacao1,"+
			                                  "dt_notificacao2,"+
			                                  "st_ait_cancelada,"+
			                                  "dt_recebimento,"+
			                                  "dt_limite_emissao,"+
			                                  "dt_limite_recurso1,"+
			                                  "cd_motivo_encerramento_ait,"+
			                                  "vl_multa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			pstmt.setString(3,objeto.getDsProvidencia());
			if(objeto.getDtPrazo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtPrazo().getTimeInMillis()));
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			pstmt.setString(6,objeto.getNmPreposto());
			pstmt.setString(7,objeto.getNrAit());
			pstmt.setString(8,objeto.getDsObservacao());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			pstmt.setInt(11,objeto.getNrViaNip());
			pstmt.setInt(12,objeto.getStAit());
			if(objeto.getCdMotivo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdMotivo());
			if(objeto.getCdUsuarioCancelamento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdUsuarioCancelamento());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdOcorrencia());
			pstmt.setDouble(16,objeto.getVlLongitude());
			pstmt.setDouble(17,objeto.getVlLatitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdCidade());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEquipamento());
			pstmt.setString(20,objeto.getNrPonto());
			pstmt.setString(21,objeto.getDsLocalInfracao());
			pstmt.setInt(22,objeto.getLgReincidencia());
			pstmt.setString(23,objeto.getNmTestemunha1());
			pstmt.setString(24,objeto.getNrRgTestemunha1());
			pstmt.setString(25,objeto.getNmEnderecoTestemunha1());
			pstmt.setString(26,objeto.getNmTestemunha2());
			pstmt.setString(27,objeto.getNrRgTestemunha2());
			pstmt.setString(28,objeto.getNmEnderecoTestemunha2());
			pstmt.setInt(29,objeto.getTpAit());
			if(objeto.getCdTalao()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdTalao());
			if(objeto.getDtNotificacaoInicial()==null)
				pstmt.setNull(31, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(31,new Timestamp(objeto.getDtNotificacaoInicial().getTimeInMillis()));
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdLinha());
			if(objeto.getCdRecurso1()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdRecurso1());
			if(objeto.getCdRecurso2()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdRecurso2());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdContaReceber());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(37, Types.INTEGER);
			else
				pstmt.setInt(37,objeto.getCdConcessao());
			if(objeto.getDtLimiteAnalisarRecurso1()==null)
				pstmt.setNull(38, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(38,new Timestamp(objeto.getDtLimiteAnalisarRecurso1().getTimeInMillis()));
			if(objeto.getDtLimiteNotificarRecurso1()==null)
				pstmt.setNull(39, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(39,new Timestamp(objeto.getDtLimiteNotificarRecurso1().getTimeInMillis()));
			if(objeto.getDtLimiteRecurso2()==null)
				pstmt.setNull(40, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(40,new Timestamp(objeto.getDtLimiteRecurso2().getTimeInMillis()));
			if(objeto.getDtLimiteAnalisarRecurso2()==null)
				pstmt.setNull(41, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(41,new Timestamp(objeto.getDtLimiteAnalisarRecurso2().getTimeInMillis()));
			if(objeto.getDtLimiteNotificarRecurso2()==null)
				pstmt.setNull(42, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(42,new Timestamp(objeto.getDtLimiteNotificarRecurso2().getTimeInMillis()));
			if(objeto.getDtJulgamento1()==null)
				pstmt.setNull(43, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(43,new Timestamp(objeto.getDtJulgamento1().getTimeInMillis()));
			if(objeto.getDtJulgamento2()==null)
				pstmt.setNull(44, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(44,new Timestamp(objeto.getDtJulgamento2().getTimeInMillis()));
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(45, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(45,new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			if(objeto.getDtNotificacao1()==null)
				pstmt.setNull(46, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(46,new Timestamp(objeto.getDtNotificacao1().getTimeInMillis()));
			if(objeto.getDtNotificacao2()==null)
				pstmt.setNull(47, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(47,new Timestamp(objeto.getDtNotificacao2().getTimeInMillis()));
			pstmt.setInt(48,objeto.getStAitCancelada());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(49, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(49,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			if(objeto.getDtLimiteEmissao()==null)
				pstmt.setNull(50, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(50,new Timestamp(objeto.getDtLimiteEmissao().getTimeInMillis()));
			if(objeto.getDtLimiteRecurso1()==null)
				pstmt.setNull(51, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(51,new Timestamp(objeto.getDtLimiteRecurso1().getTimeInMillis()));
			if(objeto.getCdMotivoEncerramentoAit()==0)
				pstmt.setNull(52, Types.INTEGER);
			else
				pstmt.setInt(52,objeto.getCdMotivoEncerramentoAit());
			if(objeto.getVlMulta()==null)
				pstmt.setNull(53, Types.DOUBLE);
			else
				pstmt.setDouble(53,objeto.getVlMulta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitTransporte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitTransporte objeto, int cdAitOld) {
		return update(objeto, cdAitOld, null);
	}

	public static int update(AitTransporte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitTransporte objeto, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_transporte SET cd_ait=?,"+
												      		   "cd_infracao=?,"+
												      		   "ds_providencia=?,"+
												      		   "dt_prazo=?,"+
												      		   "cd_agente=?,"+
												      		   "nm_preposto=?,"+
												      		   "nr_ait=?,"+
												      		   "ds_observacao=?,"+
												      		   "dt_infracao=?,"+
												      		   "dt_emissao_nip=?,"+
												      		   "nr_via_nip=?,"+
												      		   "st_ait=?,"+
												      		   "cd_motivo=?,"+
												      		   "cd_usuario_cancelamento=?,"+
												      		   "cd_ocorrencia=?,"+
												      		   "vl_longitude=?,"+
												      		   "vl_latitude=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_equipamento=?,"+
												      		   "nr_ponto=?,"+
												      		   "ds_local_infracao=?,"+
												      		   "lg_reincidencia=?,"+
												      		   "nm_testemunha1=?,"+
												      		   "nr_rg_testemunha1=?,"+
												      		   "nm_endereco_testemunha1=?,"+
												      		   "nm_testemunha2=?,"+
												      		   "nr_rg_testemunha2=?,"+
												      		   "nm_endereco_testemunha2=?,"+
												      		   "tp_ait=?,"+
												      		   "cd_talao=?,"+
												      		   "dt_notificacao_inicial=?,"+
												      		   "cd_concessao_veiculo=?,"+
												      		   "cd_linha=?,"+
												      		   "cd_recurso1=?,"+
												      		   "cd_recurso2=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "cd_concessao=?,"+
												      		   "dt_limite_analisar_recurso1=?,"+
												      		   "dt_limite_notificar_recurso1=?,"+
												      		   "dt_limite_recurso2=?,"+
												      		   "dt_limite_analisar_recurso2=?,"+
												      		   "dt_limite_notificar_recurso2=?,"+
												      		   "dt_julgamento1=?,"+
												      		   "dt_julgamento2=?,"+
												      		   "dt_cancelamento=?,"+
												      		   "dt_notificacao1=?,"+
												      		   "dt_notificacao2=?,"+
												      		   "st_ait_cancelada=?,"+
												      		   "dt_recebimento=?,"+
												      		   "dt_limite_emissao=?,"+
												      		   "dt_limite_recurso1=?,"+
												      		   "cd_motivo_encerramento_ait=?,"+
												      		   "vl_multa=? WHERE cd_ait=?");
			pstmt.setInt(1,objeto.getCdAit());
			if(objeto.getCdInfracao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInfracao());
			pstmt.setString(3,objeto.getDsProvidencia());
			if(objeto.getDtPrazo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtPrazo().getTimeInMillis()));
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			pstmt.setString(6,objeto.getNmPreposto());
			pstmt.setString(7,objeto.getNrAit());
			pstmt.setString(8,objeto.getDsObservacao());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			pstmt.setInt(11,objeto.getNrViaNip());
			pstmt.setInt(12,objeto.getStAit());
			if(objeto.getCdMotivo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdMotivo());
			if(objeto.getCdUsuarioCancelamento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdUsuarioCancelamento());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdOcorrencia());
			pstmt.setDouble(16,objeto.getVlLongitude());
			pstmt.setDouble(17,objeto.getVlLatitude());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdCidade());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEquipamento());
			pstmt.setString(20,objeto.getNrPonto());
			pstmt.setString(21,objeto.getDsLocalInfracao());
			pstmt.setInt(22,objeto.getLgReincidencia());
			pstmt.setString(23,objeto.getNmTestemunha1());
			pstmt.setString(24,objeto.getNrRgTestemunha1());
			pstmt.setString(25,objeto.getNmEnderecoTestemunha1());
			pstmt.setString(26,objeto.getNmTestemunha2());
			pstmt.setString(27,objeto.getNrRgTestemunha2());
			pstmt.setString(28,objeto.getNmEnderecoTestemunha2());
			pstmt.setInt(29,objeto.getTpAit());
			if(objeto.getCdTalao()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdTalao());
			if(objeto.getDtNotificacaoInicial()==null)
				pstmt.setNull(31, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(31,new Timestamp(objeto.getDtNotificacaoInicial().getTimeInMillis()));
			if(objeto.getCdConcessaoVeiculo()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdConcessaoVeiculo());
			if(objeto.getCdLinha()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdLinha());
			if(objeto.getCdRecurso1()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdRecurso1());
			if(objeto.getCdRecurso2()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdRecurso2());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdContaReceber());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(37, Types.INTEGER);
			else
				pstmt.setInt(37,objeto.getCdConcessao());
			if(objeto.getDtLimiteAnalisarRecurso1()==null)
				pstmt.setNull(38, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(38,new Timestamp(objeto.getDtLimiteAnalisarRecurso1().getTimeInMillis()));
			if(objeto.getDtLimiteNotificarRecurso1()==null)
				pstmt.setNull(39, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(39,new Timestamp(objeto.getDtLimiteNotificarRecurso1().getTimeInMillis()));
			if(objeto.getDtLimiteRecurso2()==null)
				pstmt.setNull(40, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(40,new Timestamp(objeto.getDtLimiteRecurso2().getTimeInMillis()));
			if(objeto.getDtLimiteAnalisarRecurso2()==null)
				pstmt.setNull(41, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(41,new Timestamp(objeto.getDtLimiteAnalisarRecurso2().getTimeInMillis()));
			if(objeto.getDtLimiteNotificarRecurso2()==null)
				pstmt.setNull(42, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(42,new Timestamp(objeto.getDtLimiteNotificarRecurso2().getTimeInMillis()));
			if(objeto.getDtJulgamento1()==null)
				pstmt.setNull(43, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(43,new Timestamp(objeto.getDtJulgamento1().getTimeInMillis()));
			if(objeto.getDtJulgamento2()==null)
				pstmt.setNull(44, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(44,new Timestamp(objeto.getDtJulgamento2().getTimeInMillis()));
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(45, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(45,new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			if(objeto.getDtNotificacao1()==null)
				pstmt.setNull(46, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(46,new Timestamp(objeto.getDtNotificacao1().getTimeInMillis()));
			if(objeto.getDtNotificacao2()==null)
				pstmt.setNull(47, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(47,new Timestamp(objeto.getDtNotificacao2().getTimeInMillis()));
			pstmt.setInt(48,objeto.getStAitCancelada());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(49, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(49,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			if(objeto.getDtLimiteEmissao()==null)
				pstmt.setNull(50, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(50,new Timestamp(objeto.getDtLimiteEmissao().getTimeInMillis()));
			if(objeto.getDtLimiteRecurso1()==null)
				pstmt.setNull(51, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(51,new Timestamp(objeto.getDtLimiteRecurso1().getTimeInMillis()));
			if(objeto.getCdMotivoEncerramentoAit()==0)
				pstmt.setNull(52, Types.INTEGER);
			else
				pstmt.setInt(52,objeto.getCdMotivoEncerramentoAit());
			if(objeto.getVlMulta()==null)
				pstmt.setNull(53, Types.DOUBLE);
			else
				pstmt.setDouble(53,objeto.getVlMulta());
			pstmt.setInt(54, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit) {
		return delete(cdAit, null);
	}

	public static int delete(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_transporte WHERE cd_ait=?");
			pstmt.setInt(1, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitTransporte get(int cdAit) {
		return get(cdAit, null);
	}

	public static AitTransporte get(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_transporte WHERE cd_ait=?");
			pstmt.setInt(1, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitTransporte(rs.getInt("cd_ait"),
						rs.getInt("cd_infracao"),
						rs.getString("ds_providencia"),
						(rs.getTimestamp("dt_prazo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prazo").getTime()),
						rs.getInt("cd_agente"),
						rs.getString("nm_preposto"),
						rs.getString("nr_ait"),
						rs.getString("ds_observacao"),
						(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
						(rs.getTimestamp("dt_emissao_nip")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao_nip").getTime()),
						rs.getInt("nr_via_nip"),
						rs.getInt("st_ait"),
						rs.getInt("cd_motivo"),
						rs.getInt("cd_usuario_cancelamento"),
						rs.getInt("cd_ocorrencia"),
						rs.getDouble("vl_longitude"),
						rs.getDouble("vl_latitude"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_equipamento"),
						rs.getString("nr_ponto"),
						rs.getString("ds_local_infracao"),
						rs.getInt("lg_reincidencia"),
						rs.getString("nm_testemunha1"),
						rs.getString("nr_rg_testemunha1"),
						rs.getString("nm_endereco_testemunha1"),
						rs.getString("nm_testemunha2"),
						rs.getString("nr_rg_testemunha2"),
						rs.getString("nm_endereco_testemunha2"),
						rs.getInt("tp_ait"),
						rs.getInt("cd_talao"),
						(rs.getTimestamp("dt_notificacao_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao_inicial").getTime()),
						rs.getInt("cd_concessao_veiculo"),
						rs.getInt("cd_linha"),
						rs.getInt("cd_recurso1"),
						rs.getInt("cd_recurso2"),
						rs.getInt("cd_conta_receber"),
						rs.getInt("cd_concessao"),
						(rs.getTimestamp("dt_limite_analisar_recurso1")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_analisar_recurso1").getTime()),
						(rs.getTimestamp("dt_limite_notificar_recurso1")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_notificar_recurso1").getTime()),
						(rs.getTimestamp("dt_limite_recurso2")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_recurso2").getTime()),
						(rs.getTimestamp("dt_limite_analisar_recurso2")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_analisar_recurso2").getTime()),
						(rs.getTimestamp("dt_limite_notificar_recurso2")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_notificar_recurso2").getTime()),
						(rs.getTimestamp("dt_julgamento1")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_julgamento1").getTime()),
						(rs.getTimestamp("dt_julgamento2")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_julgamento2").getTime()),
						(rs.getTimestamp("dt_cancelamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						(rs.getTimestamp("dt_notificacao1")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao1").getTime()),
						(rs.getTimestamp("dt_notificacao2")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao2").getTime()),
						rs.getInt("st_ait_cancelada"),
						(rs.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recebimento").getTime()),
						(rs.getTimestamp("dt_limite_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_emissao").getTime()),
						(rs.getTimestamp("dt_limite_recurso1")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_recurso1").getTime()),
						rs.getInt("cd_motivo_encerramento_ait"),
						rs.getDouble("vl_multa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_transporte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitTransporte> getList() {
		return getList(null);
	}

	public static ArrayList<AitTransporte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitTransporte> list = new ArrayList<AitTransporte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitTransporte obj = AitTransporteDAO.get(rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_transporte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}