package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemDAO;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.DateServices;
import sol.util.Result;

public class RegraFaturamentoServices {
	public static Result save(RegraFaturamento regraFaturamento){
		return save(regraFaturamento, null, null, null, null, null, null, null, null);
	}
	
	public static Result save(RegraFaturamento regraFaturamento, ArrayList<Integer> tiposPrazo, ArrayList<Integer> gruposProcesso, 
			ArrayList<Integer> tiposProcesso, ArrayList<Integer> instancias,
			ArrayList<Integer> estados, ArrayList<Integer> comarcas, ArrayList<Integer> tiposAndamento){
		return save(regraFaturamento, tiposPrazo, gruposProcesso, tiposProcesso, instancias, estados, comarcas, tiposAndamento, null);
	}
	
	public static Result save(RegraFaturamento regraFaturamento, ArrayList<Integer> tiposPrazo, ArrayList<Integer> gruposProcesso, 
			ArrayList<Integer> tiposProcesso, ArrayList<Integer> instancias, 
			ArrayList<Integer> estados, ArrayList<Integer> comarcas, ArrayList<Integer> tiposAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(regraFaturamento==null)
				return new Result(-1, "Erro ao salvar. Regra de faturamento é nulo");
			
			int retorno;
			if(regraFaturamento.getCdRegraFaturamento()==0){
				retorno = RegraFaturamentoDAO.insert(regraFaturamento, connect);
				regraFaturamento.setCdRegraFaturamento(retorno);
			}
			else {
				retorno = RegraFaturamentoDAO.update(regraFaturamento, connect);
			}

			PreparedStatement pstmt = null;
			int cdRegra = regraFaturamento.getCdRegraFaturamento();
			
			if(retorno>0) {
				//TIPOS DE PRAZO
				if(tiposPrazo!=null) {
					pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_tipo_prazo"
													+ " WHERE cd_regra_faturamento = "+cdRegra);
					retorno = pstmt.executeUpdate();
					if(retorno<0) {
						Conexao.rollback(connect);
						return new Result(-2, "Erro ao salvar tipo de prazo");
					}
						

					for (Integer cdTipoPrazo : tiposPrazo) {
						RegraFatTipoPrazo rftp = new RegraFatTipoPrazo(cdRegra, cdTipoPrazo);
						LogUtils.debug("cdRegra: "+cdRegra);
						LogUtils.debug("cdTipoPrazo: "+cdTipoPrazo);
						retorno = RegraFatTipoPrazoDAO.insert(rftp, connect);
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(-3, "Erro ao salvar tipo de prazo");
						}
					}
					
					LogUtils.debug("tipoPrazo.retorno: "+retorno);
				}
				
				// GRUPO DE PROCESSO
				if(gruposProcesso!=null) {
					pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_grupo_processo"
													+ " WHERE cd_regra_faturamento = "+cdRegra);
					retorno = pstmt.executeUpdate();
					if(retorno<0) {
						Conexao.rollback(connect);
						return new Result(-4, "Erro ao salvar grupo de processo");
					}

					for (Integer cdGrupoProcesso : gruposProcesso) {
						RegraFatGrupoProcesso rfgp = new RegraFatGrupoProcesso(cdRegra, cdGrupoProcesso);
						retorno = RegraFatGrupoProcessoDAO.insert(rfgp, connect);
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(-5, "Erro ao salvar grupo de processo");
						}
					}
					
					LogUtils.debug("grupoProcesso.retorno: "+retorno);
				}
				
				// TIPO DE PROCESSO
				if(tiposProcesso!=null) {
					pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_tipo_processo"
													+ " WHERE cd_regra_faturamento = "+cdRegra);
					retorno = pstmt.executeUpdate();
					if(retorno<0) {
						Conexao.rollback(connect);
						return new Result(-6, "Erro ao salvar tipo de processo");
					}

					for (Integer cdTipoProcesso : tiposProcesso) {
						RegraFatTipoProcesso rftp = new RegraFatTipoProcesso(cdRegra, cdTipoProcesso);
						retorno = RegraFatTipoProcessoDAO.insert(rftp, connect);
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(-7, "Erro ao salvar tipo de processo");
						}
					}
					
					LogUtils.debug("tipoProcesso.retorno: "+retorno);
				}
						
				// INSTANCIA
				if(instancias!=null) {
					pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_instancia"
													+ " WHERE cd_regra_faturamento = "+cdRegra);
					retorno = pstmt.executeUpdate();
					if(retorno<0) {
						Conexao.rollback(connect);
						return new Result(-8, "Erro ao salvar instancia");
					}

					for (Integer tpInstancia : instancias) {
						RegraFatInstancia rfi = new RegraFatInstancia(cdRegra, tpInstancia);
						retorno = RegraFatInstanciaDAO.insert(rfi, connect);
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(-9, "Erro ao salvar instancia");
						}
					}
					
					LogUtils.debug("instancia.retorno: "+retorno);
				}
				
				// ESTADO
				if(estados!=null) {
					pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_estado"
													+ " WHERE cd_regra_faturamento = "+cdRegra);
					retorno = pstmt.executeUpdate();
					if(retorno<0) {
						Conexao.rollback(connect);
						return new Result(-10, "Erro ao salvar estado");
					}

					for (Integer cdEstado : estados) {
						RegraFatEstado rfe = new RegraFatEstado(cdRegra, cdEstado);
						retorno = RegraFatEstadoDAO.insert(rfe, connect);
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(-11, "Erro ao salvar estado");
						}
					}
					
					LogUtils.debug("estados.retorno: "+retorno);
				}
				
				// COMARCA
				if(comarcas!=null) {
					pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_cidade"
													+ " WHERE cd_regra_faturamento = "+cdRegra);
					retorno = pstmt.executeUpdate();
					if(retorno<0) {
						Conexao.rollback(connect);
						return new Result(-12, "Erro ao salvar comarca");
					}

					for (Integer cdCidade : comarcas) {
						RegraFatCidade rfc = new RegraFatCidade(cdRegra, cdCidade);
						retorno = RegraFatCidadeDAO.insert(rfc, connect);
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(-13, "Erro ao salvar comarca");
						}
					}
					
					LogUtils.debug("comarcas.retorno: "+retorno);
				}
				
				//TIPOS DE ANDAMENTO
				if(tiposAndamento!=null) {
					pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_tipo_andamento"
													+ " WHERE cd_regra_faturamento = "+cdRegra);
					retorno = pstmt.executeUpdate();
					if(retorno<0) {
						Conexao.rollback(connect);
						return new Result(-14, "Erro ao salvar tipo de andamento");
					}

					for (Integer cdTipoAndamento : tiposAndamento) {
						RegraFatTipoAndamento rfta = new RegraFatTipoAndamento(cdRegra, cdTipoAndamento);
						LogUtils.debug("cdRegra: "+cdRegra);
						LogUtils.debug("cdTipoAndamento: "+cdTipoAndamento);
						retorno = RegraFatTipoAndamentoDAO.insert(rfta, connect);
						if(retorno<=0) {
							Conexao.rollback(connect);
							return new Result(-15, "Erro ao salvar tipo de andamento");
						}
					}
					
					LogUtils.debug("tipoAndamento.retorno: "+retorno);
				}
			}
			
			LogUtils.debug("isConnectionNull: "+isConnectionNull);
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(retorno>=0?cdRegra:retorno, (retorno<0)?"Erro ao salvar...":"Salvo com sucesso...", 
					"REGRAFATURAMENTO", regraFaturamento);
			result.addObject("GRUPOSPROCESSO", gruposProcesso);
			result.addObject("TIPOSPROCESSO", tiposProcesso);
			result.addObject("TIPOSINSTANCIA", instancias);
			result.addObject("ESTADOS", estados);
			result.addObject("COMARCAS", comarcas);
			
			return result;
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
	
	public static Result remove(int cdRegraFaturamento){
		return remove(cdRegraFaturamento, false, null);
	}
	
	public static Result remove(int cdRegraFaturamento, boolean cascade){
		return remove(cdRegraFaturamento, cascade, null);
	}
	
	public static Result remove(int cdRegraFaturamento, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				PreparedStatement pstmt = null;
				
				pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_grupo_processo"
						+ " WHERE cd_regra_faturamento = "+cdRegraFaturamento);
				retorno = pstmt.executeUpdate();
				if(retorno<0)
					Conexao.rollback(connect);
				
				pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_tipo_processo"
						+ " WHERE cd_regra_faturamento = "+cdRegraFaturamento);
				retorno = pstmt.executeUpdate();
				if(retorno<0)
					Conexao.rollback(connect);
				
				pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_instancia"
										+ " WHERE cd_regra_faturamento = "+cdRegraFaturamento);
				retorno = pstmt.executeUpdate();
				if(retorno<0)
					Conexao.rollback(connect);
				
				
				pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_estado"
										+ " WHERE cd_regra_faturamento = "+cdRegraFaturamento);
				retorno = pstmt.executeUpdate();
				if(retorno<0)
					Conexao.rollback(connect);
				
				
				pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_cidade"
										+ " WHERE cd_regra_faturamento = "+cdRegraFaturamento);
				retorno = pstmt.executeUpdate();
				if(retorno<0)
					Conexao.rollback(connect);
				
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = RegraFaturamentoDAO.delete(cdRegraFaturamento, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta regra de faturamento está vinculada a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Regra de faturamento excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir regra de faturamento!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_faturamento ORDER BY tp_natureza_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTipoPrazo(int cdRegra) {
		return getTipoPrazo(cdRegra, null);
	}

	public static ResultSetMap getTipoPrazo(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_tipo_prazo WHERE cd_regra_faturamento = "+cdRegra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getGrupoProcesso(int cdRegra) {
		return getGrupoProcesso(cdRegra, null);
	}

	public static ResultSetMap getGrupoProcesso(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_grupo_processo WHERE cd_regra_faturamento = "+cdRegra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTipoProcesso(int cdRegra) {
		return getTipoProcesso(cdRegra, null);
	}

	public static ResultSetMap getTipoProcesso(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_tipo_processo WHERE cd_regra_faturamento = "+cdRegra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTpInstancia(int cdRegra) {
		return getTpInstancia(cdRegra, null);
	}

	public static ResultSetMap getTpInstancia(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_instancia WHERE cd_regra_faturamento = "+cdRegra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getEstado(int cdRegra) {
		return getEstado(cdRegra, null);
	}

	public static ResultSetMap getEstado(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_estado WHERE cd_regra_faturamento = "+cdRegra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getComarca(int cdRegra) {
		return getComarca(cdRegra, null);
	}

	public static ResultSetMap getComarca(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_cidade WHERE cd_regra_faturamento = "+cdRegra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTipoAndamento(int cdRegra) {
		return getTipoAndamento(cdRegra, null);
	}

	public static ResultSetMap getTipoAndamento(int cdRegra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_tipo_andamento WHERE cd_regra_faturamento = "+cdRegra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = Search.find("SELECT A.*, B.nm_produto_servico, C.nm_orgao, C1.nm_pessoa as nm_correspondente, D.nm_pessoa AS nm_cliente, "+ 
				    " E.nm_grupo_processo, F.nm_tipo_processo, G.nm_tipo_prazo, H.nm_tipo_andamento, I.nr_processo " +
					" FROM prc_regra_faturamento 		   A " +
					" LEFT OUTER JOIN grl_produto_servico  B ON (A.cd_produto_servico = B.cd_produto_servico) "+
		            " LEFT OUTER JOIN prc_orgao            C ON (A.cd_orgao = C.cd_orgao) "+
		            " LEFT OUTER JOIN grl_pessoa          C1 ON (C.cd_pessoa = C1.cd_pessoa) "+
		            " LEFT OUTER JOIN grl_pessoa           D ON (A.cd_cliente = D.cd_pessoa) "+
		            " LEFT OUTER JOIN prc_grupo_processo   E ON (A.cd_grupo_processo = E.cd_grupo_processo) "+
		            " LEFT OUTER JOIN prc_tipo_processo    F ON (A.cd_tipo_processo = F.cd_tipo_processo) "+
		            " LEFT OUTER JOIN prc_tipo_prazo   	   G ON (A.cd_tipo_prazo = G.cd_tipo_prazo) "+
		            " LEFT OUTER JOIN prc_tipo_andamento   H ON (A.cd_tipo_andamento = H.cd_tipo_andamento) "+
		            " LEFT OUTER JOIN prc_processo 		   I ON (A.cd_processo = I.cd_processo)",
		            " ORDER BY A.nm_regra_faturamento, A.tp_natureza_evento, A.vl_servico", 
		            criterios, connect, false);
			
			while(rsm.next()) {
				
				ResultSetMap rsmAux = new ResultSetMap(connect
						.prepareStatement("SELECT B.nm_grupo_processo "
								+ " FROM prc_regra_fat_grupo_processo A "
								+ " JOIN prc_grupo_processo B ON (A.cd_grupo_processo=B.cd_grupo_processo)"
								+ " WHERE A.cd_regra_faturamento="+rsm.getInt("cd_regra_faturamento"))
							.executeQuery());
				
				rsm.setValueToField("DS_GRUPO_PROCESSO", Util.join(rsmAux, "nm_grupo_processo"));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Cria Processos Financeiros para os processos encontrados com base nas
	 * regras de faturamento.
	 * 
	 * @param regras ArrayList<Integer> códigos das regras a serem aplicadas
	 * @param dtInicial início do período
	 * @param dtFinal fim do período
	 * @param cdUsuario código do usuário que está realizando a ação
	 * 
	 * @return Result resultado da operação, NR_NOVOS: número de registros criados
	 * 
	 * @see
	 * {@link #aplicarRegraMensalidade(ArrayList, GregorianCalendar, GregorianCalendar, int, Connection)}
	 */
	public static Result aplicar(ArrayList<Integer> regras, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdUsuario, AuthData authData) {
		return aplicar(regras, dtInicial, dtFinal, cdUsuario, authData, null);
	}

	public static Result aplicar(ArrayList<Integer> regras, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdUsuario, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
						
			int nrNovos = 0; //conta novos registros criados
			RegraFaturamento rf = null; 
			PreparedStatement pstmt = null;
			ResultSetMap rsmProcessos = null;

			if(dtInicial==null) {
				return new Result(-1, "Não foi indicada uma data inicial do período.");
			}
			
			if(dtFinal==null) {
				return new Result(-1, "Não foi indicada uma data final do período.");
			}
			
			LogUtils.debug("RegraFaturamentoServices.aplicar");
			LogUtils.debug("dtInicial: "+Util.convCalendarStringCompleto(dtInicial));
			LogUtils.debug("dtFinal: "+Util.convCalendarStringCompleto(dtFinal));
			
			LogUtils.createTimer("REGRA_FATURAMENTO_TIMER");
			
			for (Integer cdRegraFaturamento : regras) {
				rf = RegraFaturamentoDAO.get(cdRegraFaturamento, connect); //regra que está sendo aplicada
				
				LogUtils.debug("RegraFaturamento (rf):\n"+rf);
				
				int tpAgenda = (rf.getTpFatoGerador() == 1 || rf.getTpFatoGerador() == 2) ? 
									TipoPrazoServices.TP_AUDIENCIA : 
							   (rf.getTpFatoGerador() == 6 || rf.getTpFatoGerador() == 7) ? 
									TipoPrazoServices.TP_DILIGENCIA : 
								    TipoPrazoServices.TP_PRAZO;
				
				int stAgenda = (rf.getTpFatoGerador() == 1 || rf.getTpFatoGerador() == 3) ? 
									AgendaItemServices.ST_AGENDA_A_CUMPRIR : 
									AgendaItemServices.ST_AGENDA_CUMPRIDO;
				
				ArrayList<RegraFaturamento> regrasFat = new ArrayList<RegraFaturamento>();
				
				ResultSetMap rsm = null;
				//TIPO PRAZO
				ArrayList<Integer> tiposPrazo = new ArrayList<>();
				rsm = getTipoPrazo(cdRegraFaturamento, connect);
				while (rsm.next()) {
					tiposPrazo.add(rsm.getInt("CD_TIPO_PRAZO"));
					
				}
				
				//GRUPO PROCESSO
				ArrayList<Integer> gruposProcesso = new ArrayList<>();
				rsm = getGrupoProcesso(cdRegraFaturamento, connect);
				while (rsm.next()) {
					gruposProcesso.add(rsm.getInt("CD_GRUPO_PROCESSO"));
				}
				
				//TIPO PROCESSO
				ArrayList<Integer> tiposProcesso = new ArrayList<>();
				rsm = getTipoProcesso(cdRegraFaturamento, connect);
				while (rsm.next()) {
					tiposProcesso.add(rsm.getInt("CD_TIPO_PROCESSO"));
					
				}
				
				//INSTANCIA
				ArrayList<Integer> instancias = new ArrayList<>();
				rsm = getTpInstancia(cdRegraFaturamento, connect);
				while (rsm.next()) {
					if(rsm.getInt("TP_INSTANCIA")>0)
						instancias.add(rsm.getInt("TP_INSTANCIA"));
				}
				
				//ESTADO
				ArrayList<Integer> estados = new ArrayList<>();
				rsm = getEstado(cdRegraFaturamento, connect);
				while (rsm.next()) {
					if(rsm.getInt("CD_ESTADO")>0)
						estados.add(rsm.getInt("CD_ESTADO"));
				}
				
				//COMARCA
				ArrayList<Integer> comarcas = new ArrayList<>();
				rsm = getComarca(cdRegraFaturamento, connect);
				while (rsm.next()) {
					if(rsm.getInt("CD_CIDADE")>0)
						comarcas.add(rsm.getInt("CD_CIDADE"));
				}
				
				//TIPO ANDAMENTO
				ArrayList<Integer> tiposAndamento = new ArrayList<>();
				rsm = getTipoAndamento(cdRegraFaturamento, connect);
				while (rsm.next()) {
					tiposAndamento.add(rsm.getInt("CD_TIPO_ANDAMENTO"));
					
				}
				
				switch (rf.getTpFatoGerador()) { //verificar qual o fato gerador
					case 0: //ANDAMENTO
						
						String sql = "SELECT A.cd_processo, A.cd_andamento, A.dt_andamento as dt_evento"
								+ " FROM prc_processo_andamento A"
								+ " JOIN prc_processo B ON (A.cd_processo = B.cd_processo)"
								+ " LEFT OUTER JOIN prc_tipo_processo B1 ON (B.cd_tipo_processo = B1.cd_tipo_processo)"
								+ " LEFT OUTER JOIN grl_cidade D ON (B.cd_cidade = D.cd_cidade)"
								+ (rf.getCdCliente()>0 ? " LEFT OUTER JOIN prc_parte_cliente E ON (B.cd_processo = E.cd_processo)" : "")
								+ " LEFT OUTER JOIN prc_tribunal F ON (B.cd_tribunal = F.cd_tribunal)"
								+ " WHERE 1=1"//A.cd_tipo_andamento = ?"
								+ (tiposAndamento.size()>0 ? " AND A.cd_tipo_andamento IN ("+Util.join(tiposAndamento)+")" : "")
								+ " AND A.dt_andamento BETWEEN ? AND ?"
								+ " AND NOT EXISTS (SELECT cd_processo, cd_andamento "
								+ "				 	FROM prc_processo_financeiro"
								+ "                	WHERE cd_processo = A.cd_processo"
								+ "                	AND A.cd_andamento = cd_andamento"
								+ "					AND cd_regra_faturamento = ?)"
								// critérios adicionais:
								+ (rf.getCdCliente()>0 ? " AND E.cd_pessoa = "+rf.getCdCliente() : "")
								+ (rf.getTpParteCliente()>0 ? " AND B.lg_cliente_autor = "+(rf.getTpParteCliente()==0 ? "1" : "0") : "")
								+ (rf.getCdOrgao()>0 ? " AND B.cd_orgao = "+rf.getCdOrgao() : "")
								+ (gruposProcesso.size()>0 ? " AND B.cd_grupo_processo IN ("+Util.join(gruposProcesso)+")" : "")
								+ (rf.getCdAreaDireito()>0 ? " AND B1.cd_area_direito = "+rf.getCdAreaDireito() : "")
								+ (tiposProcesso.size()>0 ? " AND B.cd_tipo_processo IN ("+Util.join(tiposProcesso)+")" : "")
								+ (estados.size()>0 ? " AND D.cd_estado IN ("+Util.join(estados)+")" : "")
								+ (instancias.size()>0 ? " AND B.tp_instancia IN ("+Util.join(instancias)+")" : "")
								+ (rf.getTpSegmento()>=0 ? " AND F.tp_segmento = "+rf.getTpSegmento() : "")
								+ (comarcas.size()>0 ? " AND B.cd_cidade IN ("+Util.join(comarcas)+")" : "")
								+ (rf.getCdGrupoTrabalho()>0 ? " AND B.cd_grupo_trabalho = "+rf.getCdGrupoTrabalho() : "")
								+ (rf.getCdProcesso()>0 ? " AND B.cd_processo="+rf.getCdProcesso() : "")
								+ (rf.getCdJuizo()>0 ? " AND B.cd_juizo="+rf.getCdJuizo() : "")
								+ (rf.getNrJuizo()!=null && !rf.getNrJuizo().equals("") ? " AND B.nr_juizo='"+rf.getNrJuizo()+"'" : "")
								+ " AND B.st_processo = 1";
						
						pstmt = connect.prepareStatement(sql);
						//pstmt.setInt(1, rf.getCdTipoAndamento());
						pstmt.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
						pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
						pstmt.setInt(3, cdRegraFaturamento);
												
						break;
						
					case 1: //AUDIÊNCIA AGENDADA
					case 2: //AUDIÊNCIA REALIZADA
					case 3: //COMPROMISSO/PRAZO AGENDADO
					case 4: //COMPROMISSO/PRAZO REALIZADO

						String sql2 = "SELECT A.cd_processo, A.cd_agenda_item, A.cd_pessoa, A.dt_alteracao as dt_evento"
								+ " FROM agd_agenda_item A"
								+ " JOIN prc_processo B ON (A.cd_processo = B.cd_processo)"
								+ " LEFT OUTER JOIN prc_tipo_processo B1 ON (B.cd_tipo_processo = B1.cd_tipo_processo)"
								+ " JOIN prc_tipo_prazo C ON (A.cd_tipo_prazo = C.cd_tipo_prazo)"
								+ " LEFT OUTER JOIN grl_cidade D ON (B.cd_cidade = D.cd_cidade)"
								+ (rf.getCdCliente()>0 ? " LEFT OUTER JOIN prc_parte_cliente E ON(B.cd_processo = E.cd_processo)" : "")
								+ " LEFT OUTER JOIN prc_tribunal F ON (B.cd_tribunal = F.cd_tribunal)"
								+ " WHERE C.tp_agenda_item = ?"
								+ " AND A.dt_inicial BETWEEN ? AND ?"
								+ " AND A.st_agenda_item = ?"
								+ " AND NOT EXISTS (SELECT cd_processo, cd_agenda_item "
								+ "				 	FROM prc_processo_financeiro"
								+ "                	WHERE cd_processo = A.cd_processo"
								+ "                	AND A.cd_agenda_item = cd_agenda_item"
								+ "					AND cd_regra_faturamento = ?)"
								+ (tiposPrazo.size()>0 ? " AND A.cd_tipo_prazo IN ("+Util.join(tiposPrazo)+")" : "")
								// critérios adicionais:
								+ (rf.getCdCliente()>0 ? " AND E.cd_pessoa = "+rf.getCdCliente() : "")
								+ (rf.getTpParteCliente()>0 ? " AND B.lg_cliente_autor = "+(rf.getTpParteCliente()==1 ? "1" : "0") : "")
								+ (rf.getCdOrgao()>0 ? " AND B.cd_orgao = "+rf.getCdOrgao() : "")
								+ (gruposProcesso.size()>0 ? " AND B.cd_grupo_processo IN ("+Util.join(gruposProcesso)+")" : "")
								+ (rf.getCdAreaDireito()>0 ? " AND B1.cd_area_direito = "+rf.getCdAreaDireito() : "")
								+ (tiposProcesso.size()>0 ? " AND B.cd_tipo_processo IN ("+Util.join(tiposProcesso)+")" : "")
								+ (estados.size()>0 ? " AND D.cd_estado IN ("+Util.join(estados)+")" : "")
								+ (instancias.size()>0 ? " AND B.tp_instancia IN ("+Util.join(instancias)+")" : "")
								+ (rf.getTpSegmento()>=0 ? " AND F.tp_segmento = "+rf.getTpSegmento() : "")
								+ (comarcas.size()>0 ? " AND B.cd_cidade IN ("+Util.join(comarcas)+")" : "")
								+ (rf.getCdGrupoTrabalho()>0 ? " AND B.cd_grupo_trabalho = "+rf.getCdGrupoTrabalho() : "")
								+ (rf.getCdProcesso()>0 ? " AND B.cd_processo="+rf.getCdProcesso() : "")
								+ (rf.getCdJuizo()>0 ? " AND B.cd_juizo="+rf.getCdJuizo() : "")
								+ (rf.getNrJuizo()!=null && !rf.getNrJuizo().equals("") ? " AND B.nr_juizo='"+rf.getNrJuizo()+"'" : "")
								+ " AND B.st_processo = 1";
						
						pstmt = connect.prepareStatement(sql2);
						pstmt.setInt(1, tpAgenda);
						pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
						pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
						pstmt.setInt(4, stAgenda);
						pstmt.setInt(5, cdRegraFaturamento);
												
						break;
					case 5: //MENSALIDADE
						/*
						 * As regras cujo o fator gerador é mensalidade serão
						 * tratadas em outro método (aplicarRegraMensalidade()).
						 */
						regrasFat.add(rf);
						
						break;
					case 6: //DILIGENCIA CUMPRIDA
					case 7: //DILIGENCIA CUMPRIDA (CONTRATACAO AVULSA)
						
						Orgao correspondente = OrgaoDAO.get(rf.getCdOrgao(), connect);
						int cdResponsavel = correspondente!=null ? correspondente.getCdPessoa() : 0;
						
						
						String sql3 = "SELECT A.cd_processo, A.cd_agenda_item, A.cd_pessoa, A.dt_alteracao as dt_evento, A.vl_servico, A.qt_preposto, "
								+ "           G.tp_contratacao "
								+ " FROM agd_agenda_item A"
								+ " JOIN prc_processo B ON (A.cd_processo = B.cd_processo)"
								+ " LEFT OUTER JOIN prc_tipo_processo B1 ON (B.cd_tipo_processo = B1.cd_tipo_processo)"
								+ " JOIN prc_tipo_prazo C ON (A.cd_tipo_prazo = C.cd_tipo_prazo)"
								+ " LEFT OUTER JOIN grl_cidade D ON (B.cd_cidade = D.cd_cidade)"
								+ (rf.getCdCliente()>0 ? " LEFT OUTER JOIN prc_parte_cliente E ON(B.cd_processo = E.cd_processo)" : "")
								+ " LEFT OUTER JOIN prc_tribunal F ON (B.cd_tribunal = F.cd_tribunal)"
								+ " LEFT OUTER JOIN prc_orgao G ON (G.cd_pessoa = A.cd_pessoa)"
								+ " WHERE C.tp_agenda_item = ?"
								+ " AND A.dt_realizacao BETWEEN ? AND ?"
								+ " AND A.st_agenda_item = ?"
								+ " AND NOT EXISTS (SELECT cd_processo, cd_agenda_item "
								+ "				 	FROM prc_processo_financeiro"
								+ "                	WHERE cd_processo = A.cd_processo"
								+ "                	AND A.cd_agenda_item = cd_agenda_item)"
								//+ "					AND cd_regra_faturamento=?)"
								+ (tiposPrazo.size()>0 ? " AND A.cd_tipo_prazo IN ("+Util.join(tiposPrazo)+")" : "")
								// critérios adicionais:
								+ (rf.getCdCliente()>0 ? " AND E.cd_pessoa = "+rf.getCdCliente() : "")
								+ (rf.getTpParteCliente()>0 ? " AND B.lg_cliente_autor = "+(rf.getTpParteCliente()==1 ? "1" : "0") : "")
								+ (cdResponsavel>0 ? " AND A.cd_pessoa = "+cdResponsavel : "")
								+ (gruposProcesso.size()>0 ? " AND B.cd_grupo_processo IN ("+Util.join(gruposProcesso)+")" : "")
								+ (rf.getCdAreaDireito()>0 ? " AND B1.cd_area_direito = "+rf.getCdAreaDireito() : "")
								+ (tiposProcesso.size()>0 ? " AND B.cd_tipo_processo IN ("+Util.join(tiposProcesso)+")" : "")
								+ (estados.size()>0 ? " AND D.cd_estado IN ("+Util.join(estados)+")" : "")
								+ (instancias.size()>0 ? " AND B.tp_instancia IN ("+Util.join(instancias)+")" : "")
								+ (rf.getTpSegmento()>=0 ? " AND F.tp_segmento = "+rf.getTpSegmento() : "")
								+ (rf.getTpFatoGerador() == 7 ? " AND G.tp_contratacao = 1 " : "")
								+ (comarcas.size()>0 ? " AND B.cd_cidade IN ("+Util.join(comarcas)+")" : "")
								+ (rf.getCdGrupoTrabalho()>0 ? " AND B.cd_grupo_trabalho = "+rf.getCdGrupoTrabalho() : "")
								+ (rf.getCdProcesso()>0 ? " AND B.cd_processo="+rf.getCdProcesso() : "")
								+ (rf.getCdJuizo()>0 ? " AND B.cd_juizo="+rf.getCdJuizo() : "")
								+ (rf.getNrJuizo()!=null && !rf.getNrJuizo().equals("") ? " AND B.nr_juizo='"+rf.getNrJuizo()+"'" : "");
//								+ " AND A.qt_preposto "+ (rf.getLgPreposto()==0 ? "= 0" : "> 0");

						LogUtils.debug("SQL Diligencia Cumprida:\n"+Search.formatSQL(sql3));
						
						pstmt = connect.prepareStatement(sql3);
						pstmt.setInt(1, tpAgenda);
						pstmt.setTimestamp(2, Util.convCalendarToTimestamp(dtInicial));
						pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtFinal));
						pstmt.setInt(4, AgendaItemServices.ST_AGENDA_CUMPRIDO);
						//pstmt.setInt(5, cdRegraFaturamento);
										
						break;
					default:
						break;
				}
				
				Result result = null;
				if(pstmt!=null) {
					
					rsmProcessos = new ResultSetMap(pstmt.executeQuery());
					
					
					while(rsmProcessos.next()) { //para cada processo encontrado
						int cdProcesso = rsmProcessos.getInt("CD_PROCESSO");
						int cdAndamento = rsmProcessos.getInt("CD_ANDAMENTO", 0);
						int cdAgendaItem = rsmProcessos.getInt("CD_AGENDA_ITEM", 0);
						
						GregorianCalendar dtEvento = Util.getUltimoDiaMes(new GregorianCalendar().get(Calendar.MONTH), new GregorianCalendar().get(Calendar.YEAR));
						if(cdAndamento>0) {
							ProcessoAndamento andamento = ProcessoAndamentoDAO.get(cdAndamento, cdProcesso, connect);
							dtEvento = andamento.getDtAndamento();
						}
						else if(cdAgendaItem>0) {
							AgendaItem agenda = AgendaItemDAO.get(cdAgendaItem, connect);
							if(agenda.getStAgendaItem()==AgendaItemServices.ST_AGENDA_CUMPRIDO)
								dtEvento = agenda.getDtRealizacao();
							else
								dtEvento = agenda.getDtInicial();
						}
											
						// prestador/tomador
						int cdPessoa = ProdutoServicoDAO.get(rf.getCdProdutoServico(), connect).getCdFabricante();
						Double vlServico = rf.getVlServico();
						
						
						/**
						 * PARA DILIGENCIAS CUMPRIDAS O TOMADOR/PRESTADOR É O RESPONSAVEL PELA AGENDA 
						 * E O VALOR É ZERO ATÉ QUE SE DECIDA A ESTRUTURA DE CONTRATO
						 **/
						if(rf.getTpFatoGerador()==6 || rf.getTpFatoGerador()==7) {
							cdPessoa = rsmProcessos.getInt("CD_PESSOA", 0);
							if(rsmProcessos.getInt("TP_CONTRATACAO")==1) //AVULSO
								vlServico = rsmProcessos.getDouble("vl_servico")>0 ? rsmProcessos.getDouble("vl_servico") : 0;
						}
						
						if(rf.getVlPreposto()>0 && rsmProcessos.getInt("QT_PREPOSTO", 0)>0)
							vlServico += (rf.getVlPreposto() * rsmProcessos.getInt("QT_PREPOSTO",0));
						
						ProcessoFinanceiro processoFinanceiro = new ProcessoFinanceiro(cdProcesso, 
								0/*cdEventoFinanceiro*/, rf.getCdProdutoServico(), cdAndamento, 
								cdPessoa, rf.getTpNaturezaEvento(), dtEvento, 
								vlServico, new GregorianCalendar()/*dtLancamento*/, 0/*cdContaPagar*/, 
								0/*cdContaReceber*/, cdUsuario, 0/*cdArquivo*/, null/*dtRevisao*/, 
								0/*cdUsuarioRevisao*/, rf.getTpSegmento(), rf.getTpInstancia(), rf.getCdEstado(), 
								cdAgendaItem, cdRegraFaturamento, 0/*cdEventoFinanceiroOrigem*/,
                                ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
						result = ProcessoFinanceiroServices.save(processoFinanceiro, authData, connect);
					
						
						if(result.getCode()<0) {
							return new Result(-1, "RegraFaturamentoService.aplicar: Erro ao gerar ProcessoFinanceiro (DESPESA). "+result.getMessage());
						}
						
						//se for uma DESPESA cujo serviço esteja cadastrado como REEMBOLSAVEL, gerar a receita correspondente
						if(rf.getTpNaturezaEvento()==ProcessoFinanceiroServices.TP_NATUREZA_DESPESA) {
							ProdutoServico servico = ProdutoServicoDAO.get(rf.getCdProdutoServico(), connect);
							if(servico.getLgReembolsavel()>0) {
								ProcessoFinanceiro processoFinanceiroReembolso = new ProcessoFinanceiro(cdProcesso, 
										0/*cdEventoFinanceiro*/, rf.getCdProdutoServico(), cdAndamento, 
										cdPessoa, ProcessoFinanceiroServices.TP_NATUREZA_RECEITA, dtEvento, 
										vlServico, new GregorianCalendar()/*dtLancamento*/, 0/*cdContaPagar*/, 
										0/*cdContaReceber*/, cdUsuario, 0/*cdArquivo*/, null/*dtRevisao*/, 
										0/*cdUsuarioRevisao*/, rf.getTpSegmento(), rf.getTpInstancia(), rf.getCdEstado(), 
										cdAgendaItem, cdRegraFaturamento, ((ProcessoFinanceiro)result.getObjects().get("PROCESSOFINANCEIRO")).getCdEventoFinanceiro(),
		                                ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
								result = ProcessoFinanceiroServices.save(processoFinanceiroReembolso, authData, connect);
								
								if(result.getCode()<0) {
									return new Result(-1, "RegraFaturamentoService.aplicar: Erro ao gerar ProcessoFinanceiro (RECEITA). "+result.getMessage());
								}
							}
						}
						
						nrNovos++;
					}
				}
				
				// Aplicar regras de mensalidade
				if(regrasFat.size()>0) {
					result = aplicarRegraMensalidade(regrasFat, dtInicial, dtFinal, cdUsuario, authData, connect);
					if(result.getCode()<0)
						return result;
					
					nrNovos += (int)result.getObjects().get("NR_NOVOS");
				}
			}
			
			if(isConnectionNull)
				connect.commit();			
			
			Result result = new Result(1, "Regra(s) aplicada(s) com sucesso!");
			result.addObject("NR_NOVOS", nrNovos);
			
			LogUtils.logTimer("REGRA_FATURAMENTO_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			LogUtils.destroyTimer("REGRA_FATURAMENTO_TIMER");
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro: RegraFaturamentoService.aplicar");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Aplicar regras cujo fato gerador é MENSALIDADE.
	 * 
	 * AO GERAR MENSALIDADES DEVE-SE VERIFICAR 2 PARAMETROS: 
	 * QT_MAXIMA - Quantidade máxima de mensalidades que podem ser geradas
	 * QT_APOS_ENCERRAMENTO - Quantidade de mensalidades que podem ser geradas após o encerramento 
	 * do processo, nunca superando a quantidade máxima de mensalidades
	 * Verificar DT_SENTENCA para verificar a data do encerramento. Verificar quantas mensalidades podem
	 * ser geradas após esta data
	 * 
	 * @param regras ArrayList<RegraFaturamento> regras de faturamento de mensalidade
	 * @param dtInicial Data de início do período
	 * @param dtFinal Data final do período
	 * @param cdUsuario código do usuário
	 * @param connect 
	 * @return Result + object = NR_NOVOS: número de processos alterados
	 * 
	 * @see RegraFaturamentoServices.aplicar
	 * 
	 * @author Maurício
	 * @since 15/04/2015
	 */
	private static Result aplicarRegraMensalidade(ArrayList<RegraFaturamento> regras, GregorianCalendar dtInicial, 
			GregorianCalendar dtFinal, int cdUsuario, AuthData authData) {
		return aplicarRegraMensalidade(regras, dtInicial, dtFinal, cdUsuario, authData, null);
	}
	
	private static Result aplicarRegraMensalidade(ArrayList<RegraFaturamento> regras, GregorianCalendar dtInicial, 
			GregorianCalendar dtFinal, int cdUsuario, AuthData authData, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int nrNovos = 0;
			
			// Para cada uma das regras:
			for (RegraFaturamento rf : regras) {
				
				ResultSetMap rsm = null;
				//GRUPO PROCESSO
				ArrayList<Integer> gruposProcesso = new ArrayList<>();
				rsm = getGrupoProcesso(rf.getCdRegraFaturamento(), connect);
				while (rsm.next()) {
					gruposProcesso.add(rsm.getInt("CD_GRUPO_PROCESSO"));
				}
				
				//TIPO PROCESSO
				ArrayList<Integer> tiposProcesso = new ArrayList<>();
				rsm = getTipoProcesso(rf.getCdRegraFaturamento(), connect);
				while (rsm.next()) {
					tiposProcesso.add(rsm.getInt("CD_TIPO_PROCESSO"));
					
				}
				
				//INSTANCIA
				ArrayList<Integer> instancias = new ArrayList<>();
				rsm = getTpInstancia(rf.getCdRegraFaturamento(), connect);
				while (rsm.next()) {
					if(rsm.getInt("TP_INSTANCIA")>0)
						instancias.add(rsm.getInt("TP_INSTANCIA"));
				}
				
				//ESTADO
				ArrayList<Integer> estados = new ArrayList<>();
				rsm = getEstado(rf.getCdRegraFaturamento(), connect);
				while (rsm.next()) {
					if(rsm.getInt("CD_ESTADO")>0)
						estados.add(rsm.getInt("CD_ESTADO"));
				}
				
				//COMARCA
				ArrayList<Integer> comarcas = new ArrayList<>();
				rsm = getComarca(rf.getCdRegraFaturamento(), connect);
				while (rsm.next()) {
					if(rsm.getInt("CD_CIDADE")>0)
						comarcas.add(rsm.getInt("CD_CIDADE"));
				}
				
				String sql = "SELECT (SELECT COUNT(*)"
						+ "			FROM prc_processo_financeiro B"
						+ " 		WHERE B.cd_processo = A.cd_processo"
						+ "	        AND B.cd_regra_faturamento = "+rf.getCdRegraFaturamento()+") AS qt_processo_financeiro, A.cd_processo, A.dt_sentenca"
						+ " FROM prc_processo A"
						+ " LEFT OUTER JOIN prc_tipo_processo A1 ON (A.cd_tipo_processo = A1.cd_tipo_processo)"
						+ " LEFT OUTER JOIN grl_cidade D ON (A.cd_cidade = D.cd_cidade)"
						+ (rf.getCdCliente()>0 ? " JOIN prc_parte_cliente E ON(A.cd_processo = E.cd_processo AND E.cd_pessoa = "+rf.getCdCliente()+")" : "")
						+ " LEFT OUTER JOIN prc_tribunal F ON (A.cd_tribunal = F.cd_tribunal)"
						// critérios adicionais:
						+ " WHERE 1=1 "
						+ (rf.getTpParteCliente()>0 ? " AND A.lg_cliente_autor = "+(rf.getTpParteCliente()==1 ? "1" : "0") : "")
						+ (rf.getCdOrgao()>0 ? " AND A.cd_orgao = "+rf.getCdOrgao() : "")
						+ (gruposProcesso.size()>0 ? " AND B.cd_grupo_processo IN ("+Util.join(gruposProcesso)+")" : "")
						+ (rf.getCdAreaDireito()>0 ? " AND A1.cd_area_direito = "+rf.getCdAreaDireito() : "")
						+ (tiposProcesso.size()>0 ? " AND B.cd_tipo_processo IN ("+Util.join(tiposProcesso)+")" : "")
						+ (estados.size()>0 ? " AND D.cd_estado IN ("+Util.join(estados)+")" : "")
						+ (instancias.size()>0 ? " AND B.tp_instancia IN ("+Util.join(instancias)+")" : "")
						+ (rf.getTpSegmento()>=0 ? " AND F.tp_segmento = "+rf.getTpSegmento() : "")
						+ (rf.getCdGrupoTrabalho()>0 ? " AND A.cd_grupo_trabalho = "+rf.getCdGrupoTrabalho() : "")
						+ (comarcas.size()>0 ? " AND B.cd_cidade IN ("+Util.join(comarcas)+")" : "")
						+ (rf.getCdProcesso()>0 ? " AND A.cd_processo="+rf.getCdProcesso() : "")
						+ (rf.getCdJuizo()>0 ? " AND A.cd_juizo="+rf.getCdJuizo() : "")
						+ (rf.getNrJuizo()!=null && !rf.getNrJuizo().equals("") ? " AND A.nr_juizo='"+rf.getNrJuizo()+"'" : "")
						+ " AND A.st_processo = "+ProcessoServices.ST_PROCESSO_ATIVO
						+ " GROUP BY A.cd_processo, A.dt_sentenca";
				
				PreparedStatement pstmt = connect.prepareStatement(sql);
				
				ResultSetMap rsmProcessos = new ResultSetMap(pstmt.executeQuery());
				
				Result r = null;
				
				while(rsmProcessos.next()) {
					GregorianCalendar dtSentenca = Util.convTimestampToCalendar(rsmProcessos.getTimestamp("dt_sentenca"));
					int qtProcessoFinanceiro = rsmProcessos.getInt("qt_processo_financeiro");
					int cdProcesso = rsmProcessos.getInt("cd_processo");
					int qtMaxima = rf.getQtMaxima();
					int qtEncerramento = rf.getQtAposEncerramento();
					
					// Se já houver a quantidade máxima de processos financeiros, passa para
					// o próximo processo
					if(qtProcessoFinanceiro>=qtMaxima)
						continue;
					
					int nrMeses = DateServices.countMonthsBetween(dtInicial, dtFinal);
					
					// prestador/tomador
					int cdPessoa = ProdutoServicoDAO.get(rf.getCdProdutoServico(), connect).getCdFabricante();
					
					if(dtSentenca!=null) {
						/*
						 * Gerar processos financeiros até dt_sentenca
						 */
						if(dtInicial.getTimeInMillis()<=dtSentenca.getTimeInMillis() && dtSentenca.getTimeInMillis()<dtFinal.getTimeInMillis()) {
							nrMeses = DateServices.countMonthsBetween(dtInicial, dtSentenca);
							
							GregorianCalendar dtTemp = (GregorianCalendar)dtInicial.clone();
							for (int i = 0; i < nrMeses; i++) {
								GregorianCalendar dtEvento = Util.getUltimoDiaMes(dtTemp.get(Calendar.MONTH), dtTemp.get(Calendar.YEAR));
								
								/*
								 * Verificar se já existe uma mensalidade para a regra no mês 
								 */
								if(!validarDtEvento(cdProcesso, rf.getCdRegraFaturamento(), dtEvento, connect))
									continue;
								
								ProcessoFinanceiro processoFinanceiro = new ProcessoFinanceiro(cdProcesso, 
										0/*cdEventoFinanceiro*/, rf.getCdProdutoServico(), 0/*cdAndamento*/, 
										cdPessoa, rf.getTpNaturezaEvento(), dtEvento, 
										rf.getVlServico(), new GregorianCalendar()/*dtLancamento*/, 0/*cdContaPagar*/, 
										0/*cdContaReceber*/, cdUsuario, 0/*cdArquivo*/, null/*dtRevisao*/, 
										0/*cdUsuarioRevisao*/, rf.getTpSegmento(), rf.getTpInstancia(), rf.getCdEstado(), 
										0/*cdAgendaItem*/, rf.getCdRegraFaturamento(), 0/*cdEventoFinanceiroOrigem*/,
                                        ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
								r = ProcessoFinanceiroServices.save(processoFinanceiro, connect);
								
								if(r.getCode()<0) {
									return new Result(-2, "RegraFaturamentoService.aplicarRegraMensalidade: "
											+ "Erro ao gerar ProcessoFinanceiro. "+r.getMessage());
								}
								
								qtProcessoFinanceiro++;
								nrNovos++;
								
								if(qtProcessoFinanceiro>=qtMaxima)
									break;
								
								dtTemp.add(Calendar.MONTH, 1);
							
							}
							
							/*
							 * Gerar processos financeiros após dt_sentenca
							 */
							dtTemp = (GregorianCalendar)dtSentenca.clone();
							for (int i = 0; i < qtEncerramento; i++) {
								GregorianCalendar dtEvento = Util.getUltimoDiaMes(dtTemp.get(Calendar.MONTH), dtTemp.get(Calendar.YEAR));
								
								/*
								 * Verificar se já existe uma mensalidade para a regra no mês 
								 */
								if(!validarDtEvento(cdProcesso, rf.getCdRegraFaturamento(), dtEvento, connect))
									continue;
								
								ProcessoFinanceiro processoFinanceiro = new ProcessoFinanceiro(cdProcesso, 
										0/*cdEventoFinanceiro*/, rf.getCdProdutoServico(), 0/*cdAndamento*/, 
										cdPessoa, rf.getTpNaturezaEvento(), dtEvento, 
										rf.getVlServico(), new GregorianCalendar()/*dtLancamento*/, 0/*cdContaPagar*/, 
										0/*cdContaReceber*/, cdUsuario, 0/*cdArquivo*/, null/*dtRevisao*/, 
										0/*cdUsuarioRevisao*/, rf.getTpSegmento(), rf.getTpInstancia(), rf.getCdEstado(), 
										0/*cdAgendaItem*/, rf.getCdRegraFaturamento(), 0/*cdEventoFinanceiroOrigem*/,
                                        ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
								r = ProcessoFinanceiroServices.save(processoFinanceiro, connect);
								
								if(r.getCode()<0) {
									return new Result(-3, "RegraFaturamentoService.aplicarRegraMensalidade: "
											+ "Erro ao gerar ProcessoFinanceiro. "+r.getMessage());
								}
								
								qtProcessoFinanceiro++;
								nrNovos++;
								
								if(qtProcessoFinanceiro>=qtMaxima)
									break;
								
								dtTemp.add(Calendar.MONTH, 1);
							}
						}
						else if(dtSentenca.getTimeInMillis()>=dtFinal.getTimeInMillis()) {
							
							/*
							 * Período anterior a data de sentença
							 * Gera processos financeiros sem considerar dt_sentenca
							 */
							GregorianCalendar dtTemp = (GregorianCalendar)dtInicial.clone();
							for (int i = 0; i < nrMeses; i++) {
								GregorianCalendar dtEvento = Util.getUltimoDiaMes(dtTemp.get(Calendar.MONTH), dtTemp.get(Calendar.YEAR));
								
								/*
								 * Verificar se já existe uma mensalidade para a regra no mês 
								 */
								if(!validarDtEvento(cdProcesso, rf.getCdRegraFaturamento(), dtEvento, connect))
									continue;
								
								ProcessoFinanceiro processoFinanceiro = new ProcessoFinanceiro(cdProcesso, 
										0/*cdEventoFinanceiro*/, rf.getCdProdutoServico(), 0/*cdAndamento*/, 
										cdPessoa, rf.getTpNaturezaEvento(), dtEvento, 
										rf.getVlServico(), new GregorianCalendar()/*dtLancamento*/, 0/*cdContaPagar*/, 
										0/*cdContaReceber*/, cdUsuario, 0/*cdArquivo*/, null/*dtRevisao*/, 
										0/*cdUsuarioRevisao*/, rf.getTpSegmento(), rf.getTpInstancia(), rf.getCdEstado(), 
										0/*cdAgendaItem*/, rf.getCdRegraFaturamento(), 0/*cdEventoFinanceiroOrigem*/,
                                        ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
								r = ProcessoFinanceiroServices.save(processoFinanceiro, connect);
								
								if(r.getCode()<0) {
									return new Result(-4, "RegraFaturamentoService.aplicarRegraMensalidade: "
											+ "Erro ao gerar ProcessoFinanceiro. "+r.getMessage());
								}
								
								qtProcessoFinanceiro++;
								nrNovos++;
								
								if(qtProcessoFinanceiro>=qtMaxima)
									break;
								
								dtTemp.add(Calendar.MONTH, 1);
							
							}
						}
						else if(dtSentenca.getTimeInMillis()<=dtInicial.getTimeInMillis()) {
							
							/*
							 * Gera apenas o número permitido de processos financeiros
							 * após dt_sentenca
							 */
							GregorianCalendar dtTemp = (GregorianCalendar)dtInicial.clone();
							for (int i = 0; i < qtEncerramento; i++) {
								GregorianCalendar dtEvento = Util.getUltimoDiaMes(dtTemp.get(Calendar.MONTH), dtTemp.get(Calendar.YEAR));
								
								/*
								 * Verificar se já existe uma mensalidade para a regra no mês 
								 */
								if(!validarDtEvento(cdProcesso, rf.getCdRegraFaturamento(), dtEvento, connect))
									continue;
								
								ProcessoFinanceiro processoFinanceiro = new ProcessoFinanceiro(cdProcesso, 
										0/*cdEventoFinanceiro*/, rf.getCdProdutoServico(), 0/*cdAndamento*/, 
										cdPessoa, rf.getTpNaturezaEvento(), dtEvento, 
										rf.getVlServico(), new GregorianCalendar()/*dtLancamento*/, 0/*cdContaPagar*/, 
										0/*cdContaReceber*/, cdUsuario, 0/*cdArquivo*/, null/*dtRevisao*/, 
										0/*cdUsuarioRevisao*/, rf.getTpSegmento(), rf.getTpInstancia(), rf.getCdEstado(), 
										0/*cdAgendaItem*/, rf.getCdRegraFaturamento(), 0/*cdEventoFinanceiroOrigem*/,
                                        ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
								r = ProcessoFinanceiroServices.save(processoFinanceiro, connect);
								
								if(r.getCode()<0) {
									return new Result(-5, "RegraFaturamentoService.aplicarRegraMensalidade: "
											+ "Erro ao gerar ProcessoFinanceiro. "+r.getMessage());
								}
								
								qtProcessoFinanceiro++;
								nrNovos++;
								
								if(qtProcessoFinanceiro>=qtMaxima)
									break;
								
								dtTemp.add(Calendar.MONTH, 1);
							
							}
							
						}
					}
					else { //SE DT_SENTENCA É NULO
						/*
						 * Gera processos financeiros sem considerar dt_sentenca, que não existe
						 */
						GregorianCalendar dtTemp = (GregorianCalendar)dtInicial.clone();
						for (int i = 0; i < nrMeses; i++) {
							GregorianCalendar dtEvento = Util.getUltimoDiaMes(dtTemp.get(Calendar.MONTH), dtTemp.get(Calendar.YEAR));
							
							/*
							 * Verificar se já existe uma mensalidade para a regra no mês 
							 */
							if(!validarDtEvento(cdProcesso, rf.getCdRegraFaturamento(), dtEvento, connect))
								continue;
							
							ProcessoFinanceiro processoFinanceiro = new ProcessoFinanceiro(cdProcesso, 
									0/*cdEventoFinanceiro*/, rf.getCdProdutoServico(), 0/*cdAndamento*/, 
									cdPessoa, rf.getTpNaturezaEvento(), dtEvento, 
									rf.getVlServico(), new GregorianCalendar()/*dtLancamento*/, 0/*cdContaPagar*/, 
									0/*cdContaReceber*/, cdUsuario, 0/*cdArquivo*/, null/*dtRevisao*/, 
									0/*cdUsuarioRevisao*/, rf.getTpSegmento(), rf.getTpInstancia(), rf.getCdEstado(), 
									0/*cdAgendaItem*/, rf.getCdRegraFaturamento(), 0/*cdEventoFinanceiroOrigem*/,
                                    ProcessoFinanceiroServices.NR_REFERENCIA_PARCELA_UNICA);
							r = ProcessoFinanceiroServices.save(processoFinanceiro, connect);
							
							if(r.getCode()<0) {
								return new Result(-6, "RegraFaturamentoService.aplicarRegraMensalidade: "
										+ "Erro ao gerar ProcessoFinanceiro. "+r.getMessage());
							}
							
							qtProcessoFinanceiro++;
							nrNovos++;
							
							if(qtProcessoFinanceiro>=qtMaxima)
								break;
							
							dtTemp.add(Calendar.MONTH, 1);
						}
					}
				}
			}
						
			Result result = new Result(1);
			result.addObject("NR_NOVOS", nrNovos);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao aplicar regra de mensalidade.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Verifica se já não existe um evento de determinada regra em um determinado dia
	 * 
	 * @param cdProcesso código do processo
	 * @param cdRegraFaturamento código da regra de faturamento
	 * @param dtEvento data do evento
	 * @param connect conexão com o BD
	 * @return true: data válida, false: data não válida
	 */
	private static boolean validarDtEvento(int cdProcesso, int cdRegraFaturamento, GregorianCalendar dtEvento, Connection connect) {
		try {
			PreparedStatement pstmtAux = connect.prepareStatement(
					"SELECT * FROM prc_processo_financeiro "
					+ " WHERE cd_processo = "+cdProcesso
					+ " AND cd_regra_faturamento = "+cdRegraFaturamento
					+ " AND dt_evento_financeiro = '"+Util.convCalendarStringSql(dtEvento)+"'"
			);
			ResultSetMap rsmAux = new ResultSetMap(pstmtAux.executeQuery());
			
			return !rsmAux.next();
		}
		catch(Exception e){
			return true;
		}
	}
}
