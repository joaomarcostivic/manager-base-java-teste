package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.Instituicao;
import com.tivic.manager.acd.InstituicaoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.seg.AuthData;

public class ViagemAgendamentoServices {

	public static final String[] situacaoViagemAgendamento = { "Inativo", "Ativo"};
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	public static Result save(ViagemAgendamento viagemAgendamento){
		return save(viagemAgendamento, null, null);
	}

	public static Result save(ViagemAgendamento viagemAgendamento, AuthData authData){
		return save(viagemAgendamento, authData, null);
	}

	public static Result save(ViagemAgendamento viagemAgendamento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(viagemAgendamento==null)
				return new Result(-1, "Erro ao salvar. ViagemAgendamento é nulo");

			ResultSetMap rsmViagemAgendamento = new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_viagem_agendamento WHERE cd_viagem_agendamento <> "+viagemAgendamento.getCdViagemAgendamento()+" AND st_viagem_agendamento = "+ViagemAgendamentoServices.ST_ATIVO+" AND (cd_motorista = " + viagemAgendamento.getCdMotorista() + " OR cd_concessao_veiculo = " + viagemAgendamento.getCdConcessaoVeiculo() + ")").executeQuery());
			while(rsmViagemAgendamento.next()){
				
				boolean horaCoincidente = false;
				
				
				if(viagemAgendamento.getLgLivreIntervalo() == 1 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 1){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"))
										  || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_final"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"))
										  || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaFinal(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"))
										  || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaFinal(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_final"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}
				else if(viagemAgendamento.getLgLivreIntervalo() == 1 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") != null){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"))
										  || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaFinal(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}
				else if(viagemAgendamento.getLgLivreIntervalo() == 1 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") == null){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"))
										  || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaFinal(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"));
				}
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 1 && viagemAgendamento.getHrChegadaFinal() != null){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"))
										  || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_final"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 1 && viagemAgendamento.getHrChegadaFinal() == null){
					horaCoincidente		   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"))
										  || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_final"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && viagemAgendamento.getHrChegadaFinal() != null && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") != null){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}				
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && viagemAgendamento.getHrChegadaFinal() == null && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") != null){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final"));
				}
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && viagemAgendamento.getHrChegadaFinal() != null && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") == null){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaFinal(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"));
				}				
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && rsmViagemAgendamento.getInt("lg_livre_intervalo") == 0 && viagemAgendamento.getHrChegadaFinal() == null && rsmViagemAgendamento.getGregorianCalendar("hr_chegada_final") == null){
					horaCoincidente 	   = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemAgendamento.getGregorianCalendar("hr_partida_inicio"), rsmViagemAgendamento.getGregorianCalendar("hr_chegada_inicio"));
				}
				
				if(((viagemAgendamento.getLgSegunda() == 1 && rsmViagemAgendamento.getInt("lg_segunda") == 1) || 
					(viagemAgendamento.getLgTerca() == 1 && rsmViagemAgendamento.getInt("lg_terca") == 1) || 
					(viagemAgendamento.getLgQuarta() == 1 && rsmViagemAgendamento.getInt("lg_quarta") == 1) || 
					(viagemAgendamento.getLgQuinta() == 1 && rsmViagemAgendamento.getInt("lg_quinta") == 1) || 
					(viagemAgendamento.getLgSexta() == 1 && rsmViagemAgendamento.getInt("lg_sexta") == 1) || 
					(viagemAgendamento.getLgSabado() == 1 && rsmViagemAgendamento.getInt("lg_sabado") == 1) || 
					(viagemAgendamento.getLgDomingo() == 1 && rsmViagemAgendamento.getInt("lg_domingo") == 1))&& horaCoincidente){
					
					boolean motoristaOcupado = false;
					if(rsmViagemAgendamento.getInt("cd_motorista") == viagemAgendamento.getCdMotorista()){
						motoristaOcupado = true;
					}
					
					boolean veiculoOcupado = false;
					if(rsmViagemAgendamento.getInt("cd_concessao_veiculo") == viagemAgendamento.getCdConcessaoVeiculo()){
						veiculoOcupado = true;
					}
					
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "O " + (motoristaOcupado ? "motorista" : "") + (veiculoOcupado ? (motoristaOcupado ? " e o veículo" : "veículo") : "") + " estará ocupado com uma viagem fixa");
				}
				
			}
			
			ResultSetMap rsmViagemPreviaAndamento = new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_viagem WHERE st_viagem IN ("+ViagemServices.ST_PREVISAO+", "+ViagemServices.ST_EM_ANDAMENTO+") AND (cd_motorista = " + viagemAgendamento.getCdMotorista() + " OR cd_concessao_veiculo = " + viagemAgendamento.getCdConcessaoVeiculo() + ")").executeQuery());
			while(rsmViagemPreviaAndamento.next()){
				
				boolean horaCoincidente = false;

				if(viagemAgendamento.getLgLivreIntervalo() == 1){
					horaCoincidente = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaFinal(), rsmViagemPreviaAndamento.getGregorianCalendar("hr_partida"), rsmViagemPreviaAndamento.getGregorianCalendar("hr_chegada"));
				}
				
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && viagemAgendamento.getHrChegadaFinal() != null){
					horaCoincidente = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemPreviaAndamento.getGregorianCalendar("hr_partida"), rsmViagemPreviaAndamento.getGregorianCalendar("hr_chegada"))
								   || com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaFinal(), viagemAgendamento.getHrChegadaFinal(), rsmViagemPreviaAndamento.getGregorianCalendar("hr_partida"), rsmViagemPreviaAndamento.getGregorianCalendar("hr_chegada"));
				}
				
				else if(viagemAgendamento.getLgLivreIntervalo() == 0 && viagemAgendamento.getHrChegadaFinal() == null){
					horaCoincidente = com.tivic.manager.util.UtilServices.choqueHorarios(viagemAgendamento.getHrPartidaInicio(), viagemAgendamento.getHrChegadaInicio(), rsmViagemPreviaAndamento.getGregorianCalendar("hr_partida"), rsmViagemPreviaAndamento.getGregorianCalendar("hr_chegada"));
				}
				
				if(((viagemAgendamento.getLgSegunda() == 1 && rsmViagemPreviaAndamento.getGregorianCalendar("dt_viagem").get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) || 
						(viagemAgendamento.getLgTerca() == 1 && rsmViagemPreviaAndamento.getGregorianCalendar("dt_viagem").get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) || 
						(viagemAgendamento.getLgQuarta() == 1 && rsmViagemPreviaAndamento.getGregorianCalendar("dt_viagem").get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) || 
						(viagemAgendamento.getLgQuinta() == 1 && rsmViagemPreviaAndamento.getGregorianCalendar("dt_viagem").get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) || 
						(viagemAgendamento.getLgSexta() == 1 && rsmViagemPreviaAndamento.getGregorianCalendar("dt_viagem").get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) || 
						(viagemAgendamento.getLgSabado() == 1 && rsmViagemPreviaAndamento.getGregorianCalendar("dt_viagem").get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || 
						(viagemAgendamento.getLgDomingo() == 1 && rsmViagemPreviaAndamento.getGregorianCalendar("dt_viagem").get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))&& horaCoincidente){
					
					boolean motoristaOcupado = false;
					if(rsmViagemAgendamento.getInt("cd_motorista") == viagemAgendamento.getCdMotorista()){
						motoristaOcupado = true;
					}
					
					boolean veiculoOcupado = false;
					if(rsmViagemAgendamento.getInt("cd_concessao_veiculo") == viagemAgendamento.getCdConcessaoVeiculo()){
						veiculoOcupado = true;
					}
					
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "O " + (motoristaOcupado ? "motorista" : "") + (veiculoOcupado ? (motoristaOcupado ? " e o veículo" : "veículo") : "") + " estará ocupado com uma viagem fixa");
				}
				
			}
			
			int retorno;
			if(viagemAgendamento.getCdViagemAgendamento()==0){
				retorno = ViagemAgendamentoDAO.insert(viagemAgendamento, connect);
				viagemAgendamento.setCdViagemAgendamento(retorno);
			}
			else {
				retorno = ViagemAgendamentoDAO.update(viagemAgendamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VIAGEMAGENDAMENTO", viagemAgendamento);
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
	
	public static Result remove(ViagemAgendamento viagemAgendamento) {
		return remove(viagemAgendamento.getCdViagemAgendamento());
	}
	public static Result remove(int cdViagemAgendamento){
		return remove(cdViagemAgendamento, false, null, null);
	}
	public static Result remove(int cdViagemAgendamento, boolean cascade){
		return remove(cdViagemAgendamento, cascade, null, null);
	}
	public static Result remove(int cdViagemAgendamento, boolean cascade, AuthData authData){
		return remove(cdViagemAgendamento, cascade, authData, null);
	}
	public static Result remove(int cdViagemAgendamento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ViagemAgendamentoDAO.delete(cdViagemAgendamento, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
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
		return getAll(false, null);
	}

	public static ResultSetMap getAll(Connection connect) {
		return getAll(false, connect);
	}
	
	public static ResultSetMap getAll(boolean isAtiva) {
		return getAll(isAtiva, null);
	}

	public static ResultSetMap getAll(boolean isAtiva, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_viagem_agendamento WHERE 1=1 " + (isAtiva ? " AND st_viagem_agendamento = " + ST_ATIVO : ""));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				String clDiasSemana = "";
				if(rsm.getInt("lg_segunda") == 1){
					clDiasSemana += "Segunda, ";
				}
				if(rsm.getInt("lg_terca") == 1){
					clDiasSemana += "Terça, ";
				}
				if(rsm.getInt("lg_quarta") == 1){
					clDiasSemana += "Quarta, ";
				}
				if(rsm.getInt("lg_quinta") == 1){
					clDiasSemana += "Quinta, ";
				}
				if(rsm.getInt("lg_sexta") == 1){
					clDiasSemana += "Sexta, ";
				}
				if(rsm.getInt("lg_sabado") == 1){
					clDiasSemana += "Sábado, ";
				}
				if(rsm.getInt("lg_domingo") == 1){
					clDiasSemana += "Domingo, ";
				}
				clDiasSemana = clDiasSemana.substring(0, clDiasSemana.length()-2);
				
				rsm.setValueToField("CL_DIAS_SEMANA", clDiasSemana);
				
				
				String clFaixaHorario = "";
				GregorianCalendar hrPartidaInicio = rsm.getGregorianCalendar("hr_partida_inicio");
				GregorianCalendar hrChegadaInicio = rsm.getGregorianCalendar("hr_chegada_inicio");
				GregorianCalendar hrPartidaFinal = rsm.getGregorianCalendar("hr_partida_final");
				GregorianCalendar hrChegadaFinal = rsm.getGregorianCalendar("hr_chegada_final");
				if(hrPartidaInicio != null){
					clFaixaHorario += (hrPartidaInicio.get(Calendar.HOUR_OF_DAY) > 9 ? hrPartidaInicio.get(Calendar.HOUR_OF_DAY) : "0" + hrPartidaInicio.get(Calendar.HOUR_OF_DAY)) + ":" + (hrPartidaInicio.get(Calendar.MINUTE) > 9 ? hrPartidaInicio.get(Calendar.MINUTE) : "0" + hrPartidaInicio.get(Calendar.MINUTE));
				}
				
				if(hrChegadaInicio != null){
					clFaixaHorario += "-" + (hrChegadaInicio.get(Calendar.HOUR_OF_DAY) > 9 ? hrChegadaInicio.get(Calendar.HOUR_OF_DAY) : "0" + hrChegadaInicio.get(Calendar.HOUR_OF_DAY)) + ":" + (hrChegadaInicio.get(Calendar.MINUTE) > 9 ? hrChegadaInicio.get(Calendar.MINUTE) : "0" + hrChegadaInicio.get(Calendar.MINUTE));
				}
				
				if(hrPartidaFinal != null){
					clFaixaHorario += " / " + (hrPartidaFinal.get(Calendar.HOUR_OF_DAY) > 9 ? hrPartidaFinal.get(Calendar.HOUR_OF_DAY) : "0" + hrPartidaFinal.get(Calendar.HOUR_OF_DAY)) + ":" + (hrPartidaFinal.get(Calendar.MINUTE) > 9 ? hrPartidaFinal.get(Calendar.MINUTE) : "0" + hrPartidaFinal.get(Calendar.MINUTE));
				}
				
				if(hrChegadaFinal != null){
					clFaixaHorario += "-" + (hrChegadaFinal.get(Calendar.HOUR_OF_DAY) > 9 ? hrChegadaFinal.get(Calendar.HOUR_OF_DAY) : "0" + hrChegadaFinal.get(Calendar.HOUR_OF_DAY)) + ":" + (hrChegadaFinal.get(Calendar.MINUTE) > 9 ? hrChegadaFinal.get(Calendar.MINUTE) : "0" + hrChegadaFinal.get(Calendar.MINUTE));
				}
				
				rsm.setValueToField("CL_FAIXA_HORARIO", clFaixaHorario);
				
				Pessoa motorista = PessoaDAO.get(rsm.getInt("cd_motorista"), connect);
				rsm.setValueToField("NM_MOTORISTA", motorista.getNmPessoa());
				
				ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(rsm.getInt("cd_concessao_veiculo"), connect);
				Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
				MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
				rsm.setValueToField("NM_VEICULO_COMPLETO", "E" + concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmModelo());
				
				Instituicao instituicao = InstituicaoDAO.get(rsm.getInt("cd_instituicao"), connect);
				if(instituicao != null)
					rsm.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
				
				Setor setor = SetorDAO.get(rsm.getInt("cd_setor"), connect);
				if(setor != null)
					rsm.setValueToField("NM_SETOR", setor.getNmSetor());
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViagemAgendamentoServices.getAll: " + e);
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
		ResultSetMap rsm = Search.find("SELECT * FROM mob_viagem_agendamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			String clDiasSemana = "";
			if(rsm.getInt("lg_segunda") == 1){
				clDiasSemana += "Segunda, ";
			}
			if(rsm.getInt("lg_terca") == 1){
				clDiasSemana += "Terça, ";
			}
			if(rsm.getInt("lg_quarta") == 1){
				clDiasSemana += "Quarta, ";
			}
			if(rsm.getInt("lg_quinta") == 1){
				clDiasSemana += "Quinta, ";
			}
			if(rsm.getInt("lg_sexta") == 1){
				clDiasSemana += "Sexta, ";
			}
			if(rsm.getInt("lg_sabado") == 1){
				clDiasSemana += "Sábado, ";
			}
			if(rsm.getInt("lg_domingo") == 1){
				clDiasSemana += "Domingo, ";
			}
			clDiasSemana = clDiasSemana.substring(0, clDiasSemana.length()-2);
			
			rsm.setValueToField("CL_DIAS_SEMANA", clDiasSemana);
			
			
			String clFaixaHorario = "";
			GregorianCalendar hrPartidaInicio = rsm.getGregorianCalendar("hr_partida_inicio");
			GregorianCalendar hrChegadaInicio = rsm.getGregorianCalendar("hr_chegada_inicio");
			GregorianCalendar hrPartidaFinal = rsm.getGregorianCalendar("hr_partida_final");
			GregorianCalendar hrChegadaFinal = rsm.getGregorianCalendar("hr_chegada_final");
			if(hrPartidaInicio != null){
				clFaixaHorario += (hrPartidaInicio.get(Calendar.HOUR_OF_DAY) > 9 ? hrPartidaInicio.get(Calendar.HOUR_OF_DAY) : "0" + hrPartidaInicio.get(Calendar.HOUR_OF_DAY)) + ":" + (hrPartidaInicio.get(Calendar.MINUTE) > 9 ? hrPartidaInicio.get(Calendar.MINUTE) : "0" + hrPartidaInicio.get(Calendar.MINUTE));
			}
			
			if(hrChegadaInicio != null){
				clFaixaHorario += (hrChegadaInicio.get(Calendar.HOUR_OF_DAY) > 9 ? hrChegadaInicio.get(Calendar.HOUR_OF_DAY) : "0" + hrChegadaInicio.get(Calendar.HOUR_OF_DAY)) + ":" + (hrChegadaInicio.get(Calendar.MINUTE) > 9 ? hrChegadaInicio.get(Calendar.MINUTE) : "0" + hrChegadaInicio.get(Calendar.MINUTE));
			}
			
			if(hrPartidaFinal != null){
				clFaixaHorario += " / " + (hrPartidaFinal.get(Calendar.HOUR_OF_DAY) > 9 ? hrPartidaFinal.get(Calendar.HOUR_OF_DAY) : "0" + hrPartidaFinal.get(Calendar.HOUR_OF_DAY)) + ":" + (hrPartidaFinal.get(Calendar.MINUTE) > 9 ? hrPartidaFinal.get(Calendar.MINUTE) : "0" + hrPartidaFinal.get(Calendar.MINUTE));
			}
			
			if(hrChegadaFinal != null){
				clFaixaHorario += (hrChegadaFinal.get(Calendar.HOUR_OF_DAY) > 9 ? hrChegadaFinal.get(Calendar.HOUR_OF_DAY) : "0" + hrChegadaFinal.get(Calendar.HOUR_OF_DAY)) + ":" + (hrChegadaFinal.get(Calendar.MINUTE) > 9 ? hrChegadaFinal.get(Calendar.MINUTE) : "0" + hrChegadaFinal.get(Calendar.MINUTE));
			}
			
			rsm.setValueToField("CL_FAIXA_HORARIO", clFaixaHorario);
			
			Pessoa motorista = PessoaDAO.get(rsm.getInt("cd_motorista"), connect);
			rsm.setValueToField("NM_MOTORISTA", motorista.getNmPessoa());
			
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(rsm.getInt("cd_concessao_veiculo"), connect);
			Veiculo veiculo = VeiculoDAO.get(concessaoVeiculo.getCdVeiculo(), connect);
			MarcaModelo marcaModelo = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			rsm.setValueToField("NM_VEICULO_COMPLETO", "E" + concessaoVeiculo.getNrPrefixo() + " " + marcaModelo.getNmModelo());
			
			Instituicao instituicao = InstituicaoDAO.get(rsm.getInt("cd_instituicao"), connect);
			if(instituicao != null)
				rsm.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
			
			Setor setor = SetorDAO.get(rsm.getInt("cd_setor"), connect);
			if(setor != null)
				rsm.setValueToField("NM_SETOR", setor.getNmSetor());
		}
		rsm.beforeFirst();
		
		return rsm;
	}

}