package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AfericaoNotificacaoServices {

	public static Result save(AfericaoNotificacao afericaoNotificacao){
		return save(afericaoNotificacao, null, null);
	}

	public static Result save(AfericaoNotificacao afericaoNotificacao, AuthData authData){
		return save(afericaoNotificacao, authData, null);
	}

	public static Result save(AfericaoNotificacao afericaoNotificacao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(afericaoNotificacao==null)
				return new Result(-1, "Erro ao salvar. AfericaoNotificacao é nulo");

			int retorno;
			if(afericaoNotificacao.getCdAfericaoNotificacao()==0){
				retorno = AfericaoNotificacaoDAO.insert(afericaoNotificacao, connect);
				afericaoNotificacao.setCdAfericaoNotificacao(retorno);
			}
			else {
				retorno = AfericaoNotificacaoDAO.update(afericaoNotificacao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AFERICAONOTIFICACAO", afericaoNotificacao);
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
	public static Result remove(AfericaoNotificacao afericaoNotificacao) {
		return remove(afericaoNotificacao.getCdAfericaoNotificacao());
	}
	public static Result remove(int cdAfericaoNotificacao){
		return remove(cdAfericaoNotificacao, false, null, null);
	}
	public static Result remove(int cdAfericaoNotificacao, boolean cascade){
		return remove(cdAfericaoNotificacao, cascade, null, null);
	}
	public static Result remove(int cdAfericaoNotificacao, boolean cascade, AuthData authData){
		return remove(cdAfericaoNotificacao, cascade, authData, null);
	}
	public static Result remove(int cdAfericaoNotificacao, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AfericaoNotificacaoDAO.delete(cdAfericaoNotificacao, connect);
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
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_notificacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_afericao_notificacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/**
	 * Sincronização
	 */
	public static Result sync(ArrayList<AfericaoNotificacao> afericaoNotificacoes) {
		return sync(afericaoNotificacoes, null);
	}
	
	public static Result sync(ArrayList<AfericaoNotificacao> afericaoNotificacoes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
						
			ArrayList<AfericaoNotificacao> afericaoNotificacaoRetorno = new ArrayList<AfericaoNotificacao>();
			ArrayList<AfericaoNotificacao> afericaoNotificacaoDuplicadas = new ArrayList<AfericaoNotificacao>();
			ArrayList<AfericaoNotificacao> afericaoNotificacaoErro = new ArrayList<AfericaoNotificacao>();
			
			
			System.out.println("tamanho = " + afericaoNotificacoes.size());
			
			int retorno = 0;
			for (AfericaoNotificacao afericaoNotificacao: afericaoNotificacoes) {
				
				Result r = sync(afericaoNotificacao, connect);
				retorno = r.getCode();
				
				if(r.getCode()<=0) {
					afericaoNotificacaoErro.add(afericaoNotificacao);
					continue;
				}
				else if(r.getCode()==2) {
					afericaoNotificacaoDuplicadas.add(afericaoNotificacao);
					continue;
				}
				else {
					afericaoNotificacao.setCdAfericaoNotificacao(r.getCode());
					afericaoNotificacaoRetorno.add(afericaoNotificacao);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (afericaoNotificacoes.size() == afericaoNotificacaoRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar AfericaoNotificacao.");			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AfericaoNotificacao");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(AfericaoNotificacao afericaoNotificacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			HorarioAfericao horarioAfericao = HorarioAfericaoDAO.get(afericaoNotificacao.getCdControle());

			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo AfericaoNotificacao...");
			System.out.println("\t Agente: "+ AgenteDAO.get(horarioAfericao.getCdAgente(), connect).getNmAgente());
			System.out.println("\t Linha: ["+ LinhaDAO.get(horarioAfericao.getCdLinha()).getNrLinha()+"]");
			System.out.println("\t Infracao: ["+ InfracaoTransporteDAO.get(afericaoNotificacao.getCdInfracao()).getNrInfracao()+"]");
			if (afericaoNotificacao.getCdAit()>0)
				System.out.println("\t AIT: ["+ AitTransporteDAO.get(afericaoNotificacao.getCdAit()).getNrAit()+"]");

			int retorno = 0;
			
			String sqlVerificaDuplicaidade;
			
			sqlVerificaDuplicaidade = "SELECT * FROM mob_afericao_notificacao WHERE " +
										"dt_afericao_notificacao = '" + Util.convCalendarStringSqlCompleto(afericaoNotificacao.getDtAfericaoNotificacao()) + "'" +
										" AND st_afericao_notificacao =" + afericaoNotificacao.getStAfericaoNotificacao() +
										" AND cd_infracao =" + afericaoNotificacao.getCdInfracao() +
										" AND cd_motivo =" + afericaoNotificacao.getCdMotivo() +
										" AND cd_controle =" + afericaoNotificacao.getCdControle() +
										" AND cd_ait =" + afericaoNotificacao.getCdAit() +
										" AND ds_cancelamento = '" + afericaoNotificacao.getDsCancelamento() + "'";
			
			ResultSet rs = connect.prepareStatement(sqlVerificaDuplicaidade).executeQuery();
			
			if(rs.next()) {
				retorno = 2;
				System.out.println("Diagnostico: AfericaoNotificacao Duplicada...");
			}
			else {
				retorno = AfericaoNotificacaoDAO.insert(afericaoNotificacao, connect);
				
				if(retorno > 0) {
					System.out.println("Diagnostico: Afericao Notificacao Recebida...");
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar Aferição Notificação.");
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AfericaoNotificacao.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}