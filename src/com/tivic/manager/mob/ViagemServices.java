package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.manager.acd.OcorrenciaTurma;
import com.tivic.manager.acd.OcorrenciaTurmaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class ViagemServices {

	public static final String[] situacaoViagem = { "Previsão", "Em andamento", "Encerrada", "Interrompida", "Cancelada", "Inativada", "Não concluído" };
	public static final int ST_PREVISAO = 0;
	public static final int ST_EM_ANDAMENTO = 1;
	public static final int ST_ENCERRADA = 2;
	public static final int ST_INTERROMPIDA = 3;
	public static final int ST_CANCELADA = 4;
	public static final int ST_INATIVADA = 5;
	public static final int ST_NAO_CONCLUIDO = 6;
	
	
	public static Result save(Viagem viagem, Viagem viagemOld, int cdUsuario){
		return save(viagem, viagemOld, cdUsuario, null);
	}
	
	public static Result save(Viagem viagem){
		return save(viagem, null, 0, null);
	}
	
	public static Result save(Viagem viagem, ArrayList<HashMap<String, Object>> conjuntoConcessaoVeiculo){
		
		for(HashMap<String, Object> concessaoVeiculo : conjuntoConcessaoVeiculo){
			
			viagem.setCdConcessaoVeiculo(Integer.parseInt(String.valueOf(concessaoVeiculo.get("CD_CONCESSAO_VEICULO"))));
			viagem.setCdMotorista(Integer.parseInt(String.valueOf(concessaoVeiculo.get("CD_MOTORISTA"))));
			
			Result result = save((Viagem)viagem.clone(), null, 0, null);
			if(result.getCode() < 0){
				return result;
			}
		}
		
		return new Result(1, "Viagens salvas com sucesso", "VIAGEM", viagem);
	}

	public static Result save(Viagem viagem, Connection connect){
		return save(viagem, null, 0, null);
	}

	public static Result save(Viagem viagem, Viagem viagemOld, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(viagem==null)
				return new Result(-1, "Erro ao salvar. Viagem é nulo");

			
			if(viagem.getHrPartida().compareTo(viagem.getHrChegada()) >= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "A hora da partida deve ser obrigatoriamente menor do que a hora de chegada");
			}
			
			GregorianCalendar hrHoje = Util.getHoraAtual();
			GregorianCalendar dtHojeF = Util.getDataAtual();
			dtHojeF.set(Calendar.HOUR_OF_DAY, 23);
			dtHojeF.set(Calendar.MINUTE, 59);
			dtHojeF.set(Calendar.SECOND, 59);
			
			if(!viagem.getDtViagem().after(dtHojeF) && viagem.getHrChegada().compareTo(hrHoje) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "A hora da chegada deve ser obrigatoriamente maior do que a hora atual");
			}
			
			if(viagem.getStViagem() == ST_EM_ANDAMENTO && viagem.getDtViagem().after(dtHojeF)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Viagens Em Andamento só podem ser cadastradas com a data de hoje");
			}
			
			GregorianCalendar dtHojeI = Util.getDataAtual();
			dtHojeI.set(Calendar.HOUR_OF_DAY, 0);
			dtHojeI.set(Calendar.MINUTE, 0);
			dtHojeI.set(Calendar.SECOND, 0);
			
			if(viagem.getDtViagem().before(dtHojeI)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não é possível criar uma viagem de um dia anterior à hoje");
			}
			
			ResultSetMap rsmBuscarOutrasViagensMotorista = new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_viagem V "
																								+ "		WHERE V.dt_viagem >= '"+Util.convCalendarStringSql(viagem.getDtViagem())+" 00:00:00'"
																								+ "   	  AND V.dt_viagem <= '"+Util.convCalendarStringSql(viagem.getDtViagem())+" 23:59:59'"
																								+ "		  AND V.cd_motorista = " + viagem.getCdMotorista() 
																								+ " 	  AND V.cd_viagem <> " + viagem.getCdViagem() 
																								+ " 	  AND V.st_viagem IN ("+ST_EM_ANDAMENTO+", "+ST_PREVISAO+")").executeQuery());
			while(rsmBuscarOutrasViagensMotorista.next()){
				if(com.tivic.manager.util.UtilServices.choqueHorarios(viagem.getHrPartida(), viagem.getHrChegada(), rsmBuscarOutrasViagensMotorista.getGregorianCalendar("hr_partida"), rsmBuscarOutrasViagensMotorista.getGregorianCalendar("hr_chegada"))){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Esse motorista está/estará em outra viagem no horário informado");
				}
			}
			
			ResultSetMap rsmBuscarOutrasViagensVeiculo = new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_viagem V "
																								+ "		WHERE V.dt_viagem = '"+Util.convCalendarStringSql(viagem.getDtViagem())+"' "
																								+ "		  AND V.cd_concessao_veiculo = " + viagem.getCdConcessaoVeiculo() 
																								+ " 	  AND V.cd_viagem <> " + viagem.getCdViagem() 
																								+ " 	  AND V.st_viagem IN ("+ST_EM_ANDAMENTO+", "+ST_PREVISAO+")").executeQuery());
			while(rsmBuscarOutrasViagensVeiculo.next()){
				if(com.tivic.manager.util.UtilServices.choqueHorarios(viagem.getHrPartida(), viagem.getHrChegada(), rsmBuscarOutrasViagensMotorista.getGregorianCalendar("hr_partida"), rsmBuscarOutrasViagensMotorista.getGregorianCalendar("hr_chegada"))){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Esse veículo está/estará em outra viagem no horário informado");
				}
			}
			
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagem.getCdConcessaoVeiculo(), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Pessoa motorista = PessoaDAO.get(viagem.getCdMotorista(), connect);
				
			GregorianCalendar dtViagemI = (GregorianCalendar)viagem.getDtViagem().clone();
			dtViagemI.set(Calendar.HOUR_OF_DAY, 0);
			dtViagemI.set(Calendar.MINUTE, 0);
			dtViagemI.set(Calendar.SECOND, 0);
			GregorianCalendar dtViagemF = (GregorianCalendar)viagem.getDtViagem().clone();			
			dtViagemF.set(Calendar.HOUR_OF_DAY, 23);
			dtViagemF.set(Calendar.MINUTE, 59);
			dtViagemF.set(Calendar.SECOND, 59);
			
			
			MotoristaFolgaAgendamento motoristaFolgaAgendamento = MotoristaFolgaAgendamentoServices.getAtualByMotorista(viagem.getCdMotorista(), connect);
			if(motoristaFolgaAgendamento.getDtAgendamento().after(dtViagemI) && motoristaFolgaAgendamento.getDtAgendamento().before(dtViagemF)){
				
				GregorianCalendar dtHoje = Util.getDataAtual();
				
				boolean hoje = dtHoje.after(dtViagemI) && dtHoje.before(dtViagemF);
				
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "O motorista " + motorista.getNmPessoa() + (hoje ? " está de folga hoje" : " estará de folga nessa data"));
			}
			
			ResultSetMap rsmViagemAgendamento = new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_viagem_agendamento WHERE st_viagem_agendamento = "+ViagemAgendamentoServices.ST_ATIVO+" AND (cd_motorista = " + viagem.getCdMotorista() + " OR cd_concessao_veiculo = " + viagem.getCdConcessaoVeiculo() + ")").executeQuery());
			while(rsmViagemAgendamento.next()){
				
				boolean horaCoincidente = false;
			
				
				if(rsmViagemAgendamento.getInt("lg_livre_intervalo") == 1){
					horaCoincidente = com.tivic.manager.util.UtilServices.choqueHorarios(viagem.getHrPartida(), viagem.getHrChegada(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}
				
				else if(rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") != null){
					horaCoincidente = com.tivic.manager.util.UtilServices.choqueHorarios(viagem.getHrPartida(), viagem.getHrChegada(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"))
								   || com.tivic.manager.util.UtilServices.choqueHorarios(viagem.getHrPartida(), viagem.getHrChegada(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_final"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}
				
				else if(rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") == null){
					horaCoincidente = com.tivic.manager.util.UtilServices.choqueHorarios(viagem.getHrPartida(), viagem.getHrChegada(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"));
				}
				
				
				if(((viagem.getDtViagem().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && rsmViagemAgendamento.getInt("lg_segunda") == 1) || 
						(viagem.getDtViagem().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && rsmViagemAgendamento.getInt("lg_terca") == 1) || 
						(viagem.getDtViagem().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY && rsmViagemAgendamento.getInt("lg_quarta") == 1) || 
						(viagem.getDtViagem().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY && rsmViagemAgendamento.getInt("lg_quinta") == 1) || 
						(viagem.getDtViagem().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && rsmViagemAgendamento.getInt("lg_sexta") == 1) || 
						(viagem.getDtViagem().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && rsmViagemAgendamento.getInt("lg_sabado") == 1) || 
						(viagem.getDtViagem().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && rsmViagemAgendamento.getInt("lg_domingo") == 1))&& horaCoincidente){
					
					boolean motoristaOcupado = false;
					if(rsmViagemAgendamento.getInt("cd_motorista") == viagem.getCdMotorista()){
						motoristaOcupado = true;
					}
					
					boolean veiculoOcupado = false;
					if(rsmViagemAgendamento.getInt("cd_concessao_veiculo") == viagem.getCdConcessaoVeiculo()){
						veiculoOcupado = true;
					}
					
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "O " + (motoristaOcupado ? "motorista" : "") + (veiculoOcupado ? (motoristaOcupado ? " e o veículo" : "veículo") : "") + " estará ocupado com uma viagem fixa");
				}
				
			}
			
			int retorno;
			if(viagem.getCdViagem()==0){
				retorno = ViagemDAO.insert(viagem, connect);
				viagem.setCdViagem(retorno);
				
				if(viagem.getStViagem() == ST_PREVISAO){
					int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_VIAGEM_PREVIA, connect).getCdTipoOcorrencia();
					OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Adicionada viagem prévia com veículo/motorista " + 
																			concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																				viagem.getCdViagem(), viagem.getCdViagem(),  
																				viagem.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																				viagem.getCdMotorista(), viagem.getCdMotorista(),
																				viagem.getStViagem(), viagem.getStViagem(),
																				viagem.getHrPartida(), viagem.getHrPartida(),
																				viagem.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																			
					OcorrenciaViagemServices.save(ocorrencia, null, connect);
				}
				
				else if(viagem.getStViagem() == ST_EM_ANDAMENTO){
					int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ADICIONAR_VIAGEM, connect).getCdTipoOcorrencia();
					OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Adicionada viagem com veículo/motorista " + 
																			concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																				viagem.getCdViagem(), viagem.getCdViagem(),  
																				viagem.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																				viagem.getCdMotorista(), viagem.getCdMotorista(),
																				viagem.getStViagem(), viagem.getStViagem(),
																				viagem.getHrPartida(), viagem.getHrPartida(),
																				viagem.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																			
					OcorrenciaViagemServices.save(ocorrencia, null, connect);
				}
				
				
			}
			else {
				retorno = ViagemDAO.update(viagem, connect);
				
				int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ATUALIZAR_VIAGEM, connect).getCdTipoOcorrencia();
				OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Atualizada viagem com veículo/motorista " + 
																		concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																			viagemOld.getCdViagem(), viagem.getCdViagem(),  
																			viagemOld.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																			viagemOld.getCdMotorista(), viagem.getCdMotorista(),
																			viagemOld.getStViagem(), viagem.getStViagem(),
																			viagemOld.getHrPartida(), viagem.getHrPartida(),
																			viagemOld.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																		
				OcorrenciaViagemServices.save(ocorrencia, null, connect);
			}
			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VIAGEM", viagem);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(Viagem viagem) {
		return remove(viagem.getCdViagem(), 0);
	}
	public static Result remove(int cdViagem, int cdUsuario){
		return remove(cdViagem, cdUsuario, false, null, null);
	}
	public static Result remove(int cdViagem, boolean cascade){
		return remove(cdViagem, 0, cascade, null, null);
	}
	public static Result remove(int cdViagem, boolean cascade, AuthData authData){
		return remove(cdViagem, 0, cascade, authData, null);
	}
	public static Result remove(int cdViagem, int cdUsuario, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = ViagemDAO.delete(cdViagem, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			
			Viagem viagem = ViagemDAO.get(cdViagem, connect);
			
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagem.getCdConcessaoVeiculo(), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Pessoa motorista = PessoaDAO.get(viagem.getCdMotorista(), connect);
			
			if(viagem.getStViagem() == ST_PREVISAO){
				int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMOVER_VIAGEM_PREVIA, connect).getCdTipoOcorrencia();
				OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Remover viagem prévia com veículo/motorista " + 
																		concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																			viagem.getCdViagem(), viagem.getCdViagem(),  
																			viagem.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																			viagem.getCdMotorista(), viagem.getCdMotorista(),
																			viagem.getStViagem(), viagem.getStViagem(),
																			viagem.getHrPartida(), viagem.getHrPartida(),
																			viagem.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																		
				OcorrenciaViagemServices.save(ocorrencia, null, connect);
			}
			
			if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_viagem");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllAtivas() {
		return getAllAtivas(null);
	}

	public static ResultSetMap getAllAtivas(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_viagem WHERE st_viagem IN ("+ViagemServices.ST_PREVISAO+", "+ViagemServices.ST_EM_ANDAMENTO+", "+ViagemServices.ST_ENCERRADA+", "+ViagemServices.ST_NAO_CONCLUIDO+")");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				GregorianCalendar dtHoje = Util.getDataAtual();
				dtHoje.set(Calendar.HOUR_OF_DAY, 0);
				dtHoje.set(Calendar.MINUTE, 0);
				dtHoje.set(Calendar.SECOND, 0);
				
				if(rsm.getGregorianCalendar("dt_viagem").before(dtHoje) && (rsm.getInt("st_viagem") == ViagemServices.ST_PREVISAO || rsm.getInt("st_viagem") == ViagemServices.ST_EM_ANDAMENTO)){
					Viagem viagem = ViagemDAO.get(rsm.getInt("cd_viagem"), connect);
					viagem.setStViagem(ViagemServices.ST_NAO_CONCLUIDO);
					if(ViagemDAO.update(viagem, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
					
					rsm.setValueToField("ST_VIAGEM", ViagemServices.ST_NAO_CONCLUIDO);
				}
				
				Pessoa pessoa = PessoaDAO.get(rsm.getInt("cd_motorista"), connect);
				rsm.setValueToField("NM_MOTORISTA", pessoa.getNmPessoa());
				
				ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(rsm.getInt("cd_concessao_veiculo"), connect);
				Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
				MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
				
				rsm.setValueToField("CL_VEICULO_COMPLETO", "E" + concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo());
				
				rsm.setValueToField("CL_HORARIO", Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("HR_PARTIDA")) + " - " + Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("HR_CHEGADA")));
				
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_viagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result efetivar(int cdViagem, int cdUsuario) {
		return efetivar(cdViagem, cdUsuario, null);
	}

	public static Result efetivar(int cdViagem, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			Viagem viagem = ViagemDAO.get(cdViagem, connect);
			
			viagem.setStViagem(ST_EM_ANDAMENTO);
			if(ViagemDAO.update(viagem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar efetivar a viagem");
			}
			

			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagem.getCdConcessaoVeiculo(), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Pessoa motorista = PessoaDAO.get(viagem.getCdMotorista(), connect);
			
			int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ENCERRAR_VIAGEM, connect).getCdTipoOcorrencia();
			OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Efetivar viagem com veículo/motorista " + 
																	concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																		viagem.getCdViagem(), viagem.getCdViagem(),  
																		viagem.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																		viagem.getCdMotorista(), viagem.getCdMotorista(),
																		ST_PREVISAO, viagem.getStViagem(),
																		viagem.getHrPartida(), viagem.getHrPartida(),
																		viagem.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																	
			OcorrenciaViagemServices.save(ocorrencia, null, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Viagem efetivar com sucesso");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.efetivar: " + e);
			return new Result(-1, "Erro ao efetivar viagem");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result inativar(int cdViagem, int cdUsuario) {
		return inativar(cdViagem, cdUsuario, null);
	}

	public static Result inativar(int cdViagem, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			Viagem viagem = ViagemDAO.get(cdViagem, connect);
			
			viagem.setStViagem(ST_INATIVADA);
			if(ViagemDAO.update(viagem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar inativar a viagem");
			}
			

			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagem.getCdConcessaoVeiculo(), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Pessoa motorista = PessoaDAO.get(viagem.getCdMotorista(), connect);
			
			int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_INATIVAR_VIAGEM_PREVIA, connect).getCdTipoOcorrencia();
			OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Inativada viagem prevista com veículo/motorista " + 
																	concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																		viagem.getCdViagem(), viagem.getCdViagem(),  
																		viagem.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																		viagem.getCdMotorista(), viagem.getCdMotorista(),
																		ST_PREVISAO, viagem.getStViagem(),
																		viagem.getHrPartida(), viagem.getHrPartida(),
																		viagem.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																	
			OcorrenciaViagemServices.save(ocorrencia, null, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Viagem inativada com sucesso");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.inativar: " + e);
			return new Result(-1, "Erro ao efetivar viagem");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveSubstituicao(Viagem viagem) {
		return saveSubstituicao(viagem, null, 0, null);
	}
	
	public static Result saveSubstituicao(Viagem viagem, Viagem viagemOld) {
		return saveSubstituicao(viagem, viagemOld, 0, null);
	}
	
	public static Result saveSubstituicao(Viagem viagem, Viagem viagemOld, int cdUsuario) {
		return saveSubstituicao(viagem, viagemOld, cdUsuario, null);
	}

	public static Result saveSubstituicao(Viagem viagem, Viagem viagemOld, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(viagem.getCdConcessaoVeiculo() == viagemOld.getCdConcessaoVeiculo() && viagem.getCdMotorista() == viagemOld.getCdMotorista()){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Ao realizar uma substituição, deve-se modificar ou o veículo ou o motorista da viagem anterior.");
			}
			
			viagemOld.setStViagem(ST_INTERROMPIDA);
			if(ViagemDAO.update(viagemOld, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar viagem antiga");
			}
			
			if(viagemOld.getLgManutencao() == 1){
				ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagemOld.getCdConcessaoVeiculo(), connect);
				concessaoVeiculo.setLgManutencao(1);
				if(ConcessaoVeiculoDAO.update(concessaoVeiculo, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar concessão veículo");
				}
			}
			
			viagem.setCdViagem(0);
			viagem.setStViagem(ST_EM_ANDAMENTO);
			viagem.setLgManutencao(0);
			
			Result result = save(viagem, viagemOld, cdUsuario, connect);
			if(result.getCode() < 0){
				return result;
			}
			
			viagemOld.setCdViagemAnterior(viagem.getCdViagem());
			if(ViagemDAO.update(viagemOld, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar viagem antiga");
			}
			
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagem.getCdConcessaoVeiculo(), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Pessoa motorista = PessoaDAO.get(viagem.getCdMotorista(), connect);
			
			ConcessaoVeiculo concessaoVeiculoOld = ConcessaoVeiculoDAO.get(viagemOld.getCdConcessaoVeiculo(), connect);
			Veiculo veiculoOld = VeiculoDAO.get(concessaoVeiculoOld.getCdVeiculo(), connect);
			MarcaModelo marcaModeloOld = MarcaModeloDAO.get(veiculoOld.getCdMarca(), connect);
			Pessoa motoristaOld = PessoaDAO.get(viagemOld.getCdMotorista(), connect);
			
			int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_SUBSTITUICAO_VEICULO_VIAGEM, connect).getCdTipoOcorrencia();
			OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Substituido veículo/motorista " + 
																	concessaoVeiculoOld.getNrPrefixo() + " " + marcaModeloOld.getNmMarca() + "/" + marcaModeloOld.getNmModelo() + " - " + motoristaOld.getNmPessoa() + 
																	" por " + concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																		viagemOld.getCdViagem(), viagem.getCdViagem(),  
																		viagemOld.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																		viagemOld.getCdMotorista(), viagem.getCdMotorista(),
																		viagemOld.getStViagem(), viagem.getStViagem(),
																		viagemOld.getHrPartida(), viagem.getHrPartida(),
																		viagemOld.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																	
			OcorrenciaViagemServices.save(ocorrencia, null, connect);
			
			
			if(isConnectionNull)
				connect.commit();
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.saveSubstituicao: " + e);
			return new Result(-1, "Erro ao salvar substituição da viagem");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result encerrar(int cdViagem, int cdUsuario) {
		return encerrar(cdViagem, cdUsuario, null);
	}

	public static Result encerrar(int cdViagem, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			Viagem viagem = ViagemDAO.get(cdViagem, connect);
			
			viagem.setStViagem(ST_ENCERRADA);
			if(ViagemDAO.update(viagem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar encerrar a viagem");
			}
			
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagem.getCdConcessaoVeiculo(), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Pessoa motorista = PessoaDAO.get(viagem.getCdMotorista(), connect);
			
			int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ENCERRAR_VIAGEM, connect).getCdTipoOcorrencia();
			OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Encerrada viagem com veículo/motorista " + 
																	concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																		viagem.getCdViagem(), viagem.getCdViagem(),  
																		viagem.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																		viagem.getCdMotorista(), viagem.getCdMotorista(),
																		ST_EM_ANDAMENTO, viagem.getStViagem(),
																		viagem.getHrPartida(), viagem.getHrPartida(),
																		viagem.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																	
			OcorrenciaViagemServices.save(ocorrencia, null, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Viagem encerrada com sucesso");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.encerrar: " + e);
			return new Result(-1, "Erro ao encerrar viagem");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result cancelar(int cdViagem, int cdUsuario) {
		return cancelar(cdViagem, cdUsuario, null);
	}

	public static Result cancelar(int cdViagem, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			Viagem viagem = ViagemDAO.get(cdViagem, connect);
			
			viagem.setStViagem(ST_CANCELADA);
			if(ViagemDAO.update(viagem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar cancelar a viagem");
			}
			
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(viagem.getCdConcessaoVeiculo(), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Pessoa motorista = PessoaDAO.get(viagem.getCdMotorista(), connect);
			
			int cdTipoOcorrenciaSubstituicaoVeiculoViagem = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CANCELAR_VIAGEM, connect).getCdTipoOcorrencia();
			OcorrenciaViagem ocorrencia = new OcorrenciaViagem(0, 0, "Cancelada viagem com veículo/motorista " + 
																	concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmMarca() + "/" + marcaModelo.getNmModelo() + " - " + motorista.getNmPessoa(), Util.getDataAtual(), cdTipoOcorrenciaSubstituicaoVeiculoViagem, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario,
																		viagem.getCdViagem(), viagem.getCdViagem(),  
																		viagem.getCdConcessaoVeiculo(), viagem.getCdConcessaoVeiculo(),
																		viagem.getCdMotorista(), viagem.getCdMotorista(),
																		ST_EM_ANDAMENTO, viagem.getStViagem(),
																		viagem.getHrPartida(), viagem.getHrPartida(),
																		viagem.getHrChegada(), viagem.getHrChegada(), viagem.getCdViagemAgendamento());
																	
			OcorrenciaViagemServices.save(ocorrencia, null, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Viagem encerrada com sucesso");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemServices.cancelar: " + e);
			return new Result(-1, "Erro ao cancelar viagem");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}