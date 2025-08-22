package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.manager.grl.Equipamento;
import com.tivic.manager.grl.EquipamentoDAO;
import com.tivic.manager.grl.EquipamentoHistorico;
import com.tivic.manager.grl.EquipamentoHistoricoDAO;
import com.tivic.manager.grl.EquipamentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.log.Sistema;
import com.tivic.manager.log.SistemaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.util.Result;


public class AgenteServices {
	
	public static Result save(Agente agente){
		return save(agente, null, null);
	}

	public static Result save(Agente agente, AuthData authData){
		return save(agente, authData, null);
	}

	public static Result save(Agente agente, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agente==null)
				return new Result(-1, "Erro ao salvar. Agente é nulo");

			int retorno;
			
			if(agente.getCdAgente()==0) {
				if(Util.isStrBaseAntiga()) {
					int rAntigo = insert(agente, connect);
					if(rAntigo<0) {
						Conexao.rollback(connect);
						return new Result(rAntigo, "Erro ao salvar (base antiga)");
					}
					connect.commit();
					agente.setCdAgente(rAntigo);
					return new Result(rAntigo, "Salvo com sucesso", "AGENTE", agente);
				} else {				
					retorno = AgenteDAO.insert(agente, connect);
					agente.setCdAgente(retorno);
				}
			} else {
				if(Util.isStrBaseAntiga()) {
					int rAntigo = update(agente, connect);					
					if(rAntigo<0) {
						Conexao.rollback(connect);
						return new Result(rAntigo, "Erro ao salvar (base antiga)");
					}
					
					connect.commit();
					agente.setCdAgente(rAntigo);
					return new Result(1, "Salvo com sucesso", "AGENTE", agente);
				} else {
					retorno = AgenteDAO.update(agente, connect);
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENTE", agente);
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
	
	public static Result autenticar(String nmLogin, String nmSenha, String idEquipamento) {
		return autenticar(nmLogin, nmSenha, idEquipamento, false, null);
	}
	
	public static Result autenticar(String nmLogin, String nmSenha, String idEquipamento, boolean lgTransporte) {
		return autenticar(nmLogin, nmSenha, idEquipamento, lgTransporte, null);
	}

	public static Result autenticar(String nmLogin, String nmSenha, String idEquipamento, boolean lgTransporte, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			boolean lgGctPadrao = Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
						
			String sql = "SELECT * FROM seg_usuario " +
						 " WHERE nm_login	 = ? " +
						 "   AND st_usuario = 1";
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM usuario " +
					  " WHERE nm_nick	 = ? " +
					  "   AND st_usuario = 1";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nmLogin);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				Usuario usuario = new Usuario();
				Pessoa pessoa =  new Pessoa();
				Agente agente = new Agente();
								
				Equipamento equipamento = new Equipamento();
				ArrayList<Talonario> talonarios = new ArrayList<Talonario>();
				
				String nmUsuario = lgBaseAntiga ? rsm.getString("nm_usuario") : "";
				if(lgBaseAntiga) {
					usuario = new Usuario(rsm.getInt("cod_usuario"),
						0,
						0,
						rsm.getString("nm_nick"),
						rsm.getString("nm_senha"),
						rsm.getInt("nr_nivel"),
						null,
						rsm.getInt("st_usuario"));
					
					agente = AgenteServices.getAgenteByUsuario(rsm.getInt("cod_usuario"), connect);
				}
				else {
					usuario = new Usuario(rsm.getInt("cd_usuario"),
							rsm.getInt("cd_pessoa"),
							rsm.getInt("cd_pergunta_secreta"),
							rsm.getString("nm_login"),
							rsm.getString("nm_senha"),
							rsm.getInt("tp_usuario"),
							rsm.getString("nm_resposta_secreta"),
							rsm.getInt("st_usuario"));
					
					pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					agente = AgenteServices.getAgenteByUsuario(rsm.getInt("cd_usuario"), connect);
					
					nmUsuario = agente.getNmAgente();
				}
				
				
				if(nmSenha.equals(usuario.getNmSenha())){					
					//verificar travamento de login
					Equipamento e = getEquipamentoLogin(usuario.getCdUsuario(), connect);
					if(isLoginAgenteTravado(usuario.getCdUsuario(), connect) && !idEquipamento.equals(e.getIdEquipamento())) {
						return new Result(-6, "O agente '"+agente.getNmAgente()+"' está autenticado "+ 
											  (e!=null ? "no dispositivo "+e.getNmEquipamento() : "em outro dispositivo") + 
											  ". Entregue o outro dispositivo antes de continuar.");
					}
										
					//verificar equipamento
					pstmt = connect.prepareStatement("SELECT cd_equipamento FROM grl_equipamento WHERE id_equipamento = ?");
					pstmt.setString(1, idEquipamento);
					ResultSet rsEquipamento = pstmt.executeQuery();
					
					if(rsEquipamento.next()) {
						equipamento = EquipamentoDAO.get(rsEquipamento.getInt("cd_equipamento"), connect);
						
						if(equipamento.getStEquipamento()==EquipamentoServices.INATIVO) {
							rsEquipamento.close();
							return new Result(-6, "Este equipamento está aguardando a ativação. Em breve um de nossos operadores irá desbloquea-lo.");
						}
						
						if(agente==null || agente.getCdAgente()==0) {
							return new Result(-7, "O usuário indicado não está vinculado à um agente ou não é um agente válido.");
						}
						
						//verificar talonarios
						if(lgBaseAntiga){
							sql = "SELECT * FROM talonario " +
								  "WHERE cod_agente = ? " +
								  "  AND ( tp_talao = ";  
								  
								  if(lgTransporte){
									  sql += TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE + ")";									  
								  }
								  else{									  							  									  
									  sql += TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO + 
											  " OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT + 
											  " OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV +
											  " OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_RRD + ")";								  									  
								  }
								  								  
								  
								 sql +=  "  AND st_talao = " + TalonarioServices.ST_TALAO_ATIVO +
      								   //"  AND ? <= nr_final " +
		     						     " ORDER BY dt_entrega";
						}						
						else{
							sql = "SELECT * FROM mob_talonario " +
								  "WHERE cd_agente = ? " +
								  "  AND (tp_talao = " + (lgTransporte ? TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE : TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO) +
								  " 	    OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT +
								  " 	    OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV + 
								  " 	    OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_RRD + ")"+
								  "  AND st_talao = " + TalonarioServices.ST_TALAO_ATIVO +
								  //"  AND ? <= A.nr_final" +
								  " ORDER BY dt_entrega";
						}
												
						pstmt = connect.prepareStatement(sql);
						pstmt.setInt(1, agente.getCdAgente());
						//pstmt.setInt(2, nrUltimoAit);
						ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());
												
						//boolean lgTalaoAtivo = false;
						while(rsmTalonario.next()) {
							
							int nrUltimoAitTalao = 0;
							
							//Ultimo AIT do talonario
							if(lgBaseAntiga) {
								if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE)
									sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM AIT_TRANSPORTE WHERE cod_agente = ? AND nr_ait >=  ? AND nr_ait <= ?";
								else{
									
									// Pesquisa o último rdd
									if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD){
										sql = "SELECT MAX(nr_rrd) as nr_ultimo_rrd FROM mob_rrd WHERE nr_rrd >=  ? AND nr_rrd <= ?";
									}// Pesquisa o último trrav
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV){
										sql = "SELECT MAX(nr_trrav) as nr_ultimo_trrav FROM mob_trrav  WHERE nr_trrav >=  ? AND nr_trrav <= ?";
									}//BOAT
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT){
										sql = "SELECT MAX(nr_boat) as nr_ultimo_boat FROM mob_boat WHERE nr_boat >=  ? AND nr_boat <= ?";
									}//AIT
									else {
										if(lgGctPadrao){									
											sql = "SELECT * FROM AIT WHERE cod_agente = ?";
										}
										else{
											// Pesquisa o último ait
											if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO){
												sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM AIT WHERE nr_ait >=  ? AND nr_ait <= ?";
											}
										}
									}
									
								}
							}
							else {
								if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE){
									sql = "SELECT * FROM mob_ait_transporte WHERE nr_ait >= ? AND nr_ait <= ? AND cd_agente = ? ";			
								}
								else {
									if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO){
										sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE nr_ait >=  ? AND nr_ait <= ?";
									}
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD) {
										sql = "SELECT MAX(nr_rrd) as nr_ultimo_rrd FROM mob_rrd WHERE nr_rrd >=  ? AND nr_rrd <= ?";
									}
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV) {
										sql = "SELECT MAX(nr_trrav) as nr_ultimo_trrav FROM mob_trrav WHERE nr_trrav >=  ? AND nr_trrav <= ?";
									}
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT) {
										sql = "SELECT MAX(nr_boat) as nr_ultimo_boat FROM mob_boat WHERE nr_boat >=  ? AND nr_boat <= ?";
									}
								}
							}
							
							
							//System.out.println(sql);
							PreparedStatement pstmt2 = connect.prepareStatement(sql);
							
							if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) {
								pstmt2.setString(1, rsmTalonario.getString("NR_INICIAL"));
								pstmt2.setString(2, rsmTalonario.getString("NR_FINAL"));
								pstmt2.setInt(3, agente.getCdAgente());
							}
							else{
								if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO && lgGctPadrao){
									pstmt2.setInt(1, agente.getCdAgente());
								}
								else{ 
									//pstmt2.setInt(1, agente.getCdAgente());
									pstmt2.setInt(1, rsmTalonario.getInt("NR_INICIAL"));
									pstmt2.setInt(2, rsmTalonario.getInt("NR_FINAL"));																		
								}

							}
							
							ResultSet rsNr = pstmt2.executeQuery();
							
							// Ordena e pega o maior nr da AIT Transporte ou No caso do ait no Padrão GCT
							List<Integer> aitsAgente = new ArrayList<Integer>();
							if( (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) || 
								(lgBaseAntiga && lgGctPadrao ) ) {	
								
								if(rsNr.next()) {
								
									if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD){									
										nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_RRD") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_RRD");
									}
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT){									
										nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_BOAT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_BOAT");
									}
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV){									
										nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_TRRAV") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_TRRAV");
									}
									else { 	
									
										String nrAitTexto;
										Integer nrAit;
										do {
											nrAitTexto = rsNr.getString("NR_AIT");
											nrAitTexto = nrAitTexto.replaceAll("[a-zA-Z]", "");
											//System.out.println(nrAitTexto);
											nrAit = Integer.parseInt(nrAitTexto);
											
											if( nrAit >= rsmTalonario.getInt("NR_INICIAL") && nrAit <= rsmTalonario.getInt("NR_FINAL"))									
											    aitsAgente.add(nrAit);
										}
										while(rsNr.next());
										
										Collections.sort(aitsAgente);
										
										//Caso em que não há nehum ait registrada dentro da faixa desse talão
										if(aitsAgente.size() == 0){
											nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL")-1; 									
										}
										//Caso que pega a última ait registrada desse talão
										else {
											nrUltimoAitTalao = aitsAgente.get(aitsAgente.size() - 1);
										}
										
										if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE && nrUltimoAitTalao == 0)
											nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL")-1;
										
										System.out.println("nrUltimoAitTalao: "+ nrUltimoAitTalao);
									}
								}
								
							}
							else{							
								if(rsNr.next()){
									if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD){									
										nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_RRD") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_RRD");
									}
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT){									
										nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_BOAT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_BOAT");
									}
									else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV){									
										nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_TRRAV") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_TRRAV");
									}
									else { 									
										nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_AIT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_AIT");
									}									
								}
								else{
								    nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL")-1;//rsmTalonario.getInt("NR_FINAL");
								}
							}
							

							System.out.println("\n--\nTalão Tipo: "+ rsmTalonario.getInt("TP_TALAO"));
							System.out.println("   Inicial: "+ rsmTalonario.getInt("NR_INICIAL"));
							System.out.println("     Final: "+ rsmTalonario.getInt("NR_FINAL"));
							System.out.println(" Ultimo Nº: "+ nrUltimoAitTalao);
							
							rsNr.close();
							
							
							//SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
							if(nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL")) {
								
								if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) {
									com.tivic.manager.mob.Talonario talao = com.tivic.manager.mob.TalonarioDAO.get(rsmTalonario.getInt("cd_talao"), connect);
									talao.setStTalao(TalonarioServices.ST_TALAO_PENDENTE);
									com.tivic.manager.mob.TalonarioDAO.update(talao, connect);
								}
								else {
									int cdTalonario = rsmTalonario.getInt((lgBaseAntiga ? "cod_talao" : "cd_talao"));
									Talonario talao = TalonarioDAO.get(cdTalonario, connect);
									talao.setStTalao(TalonarioServices.ST_TALAO_PENDENTE);
									TalonarioDAO.update(talao, connect);
								}
								continue;
							}
							
							Talonario talao;
							if(lgBaseAntiga)
								talao = new Talonario(rsmTalonario.getInt("COD_TALAO"),
										rsmTalonario.getInt("COD_AGENTE"),
										rsmTalonario.getInt("NR_INICIAL"),
										rsmTalonario.getInt("NR_FINAL"),
										(rsmTalonario.getTimestamp("DT_ENTREGA")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()),
										(rsmTalonario.getTimestamp("DT_DEVOLUCAO")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()),
										rsmTalonario.getInt("ST_TALAO"),
										rsmTalonario.getInt("NR_TALAO"),
										rsmTalonario.getInt("TP_TALAO"),
										rsmTalonario.getString("SG_TALAO"), nrUltimoAitTalao);
							else
								talao = new Talonario(rsmTalonario.getInt("CD_TALAO"),
										rsmTalonario.getInt("CD_AGENTE"),
										rsmTalonario.getInt("NR_INICIAL"),
										rsmTalonario.getInt("NR_FINAL"),
										(rsmTalonario.getTimestamp("DT_ENTREGA")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()),
										(rsmTalonario.getTimestamp("DT_DEVOLUCAO")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()),
										rsmTalonario.getInt("ST_TALAO"),
										rsmTalonario.getInt("NR_TALAO"),
										rsmTalonario.getInt("TP_TALAO"),
										rsmTalonario.getString("SG_TALAO"), nrUltimoAitTalao);
							talonarios.add(talao);
						}	
						
						if(talonarios.size()==0) {
							return new Result(-5, "Nenhum talão distribuído para o agente. Entre em contato com a administração.");
						}
						
						HashMap<String, Object> parametros = new HashMap<String, Object>();
						if(lgBaseAntiga) {						
						
							//BUSCA DE PARAMETROS DO SERVER PARA SINCRONIZACAO NOS EQUIPAMENTOS						
							PreparedStatement pstmt3 = connect.prepareStatement("select * from PARAMETRO");
							ResultSetMap rsmParametro = new ResultSetMap(pstmt3.executeQuery());
							
							if(rsmParametro.next()) {
								parametros.put("DS_TITULO_1",rsmParametro.getString("DS_TITULO_1"));
								parametros.put("DS_TITULO_2",rsmParametro.getString("DS_TITULO_2"));
								parametros.put("DS_TITULO_3",rsmParametro.getString("DS_TITULO_3"));
								parametros.put("NM_MUNICIPIO",rsmParametro.getString("NM_MUNICIPIO_MOBILE"));
								parametros.put("NM_PRINTER",rsmParametro.getString("NM_PRINTER"));
								parametros.put("NM_ORGAO",rsmParametro.getString("NM_ORGAO"));
								parametros.put("ID_ORGAO",rsmParametro.getString("CD_ORGAO_AUTUANTE"));
								parametros.put("HOST_GEO",rsmParametro.getString("HOST_GEO"));
								parametros.put("URL_DETRAN",rsmParametro.getString("URL_DETRAN"));
								parametros.put("LG_PREENCHIMENTO_AUTOMATICO",rsmParametro.getInt("LG_PREENCHIMENTO_AUTOMATICO"));
								parametros.put("NR_VERSAO_MOBILE",rsmParametro.getString("NR_VERSAO_MOBILE"));
								parametros.put("LG_ATUALIZACAO_MOBILE",rsmParametro.getInt("LG_ATUALIZACAO_MOBILE"));
								parametros.put("URL_ATUALIZACAO_MOBILE",rsmParametro.getString("URL_ATUALIZACAO_MOBILE"));
								

								parametros.put("CD_ORGAO_AUTUADOR",rsmParametro.getInt("CD_ORGAO_AUTUADOR"));								
								parametros.put("LG_BOAT",rsmParametro.getInt("LG_BOAT"));
								parametros.put("LG_TRRAV",rsmParametro.getInt("LG_TRRAV"));		
								parametros.put("LG_RRD",rsmParametro.getInt("LG_RRD"));						
								
								parametros.put("NR_TELEFONE",rsmParametro.getString("NR_TELEFONE"));
								parametros.put("NM_EMAIL",rsmParametro.getString("NM_EMAIL"));
								parametros.put("NM_LOGRADOURO", rsmParametro.getString("NM_LOGRADOURO"));
								parametros.put("NR_ENDERECO", rsmParametro.getString("NR_ENDERECO"));
								parametros.put("NM_BAIRRO", rsmParametro.getString("NM_BAIRRO"));
								parametros.put("SG_DEPARTAMENTO", rsmParametro.getString("SG_DEPARTAMENTO"));
								
								parametros.put("NR_VERSAO_MOBILE", rsmParametro.getString("NR_VERSAO_MOBILE"));
								
								parametros.put("CD_MUNICIPIO_AUTUADOR",rsmParametro.getInt("CD_MUNICIPIO_AUTUADOR"));
								parametros.put("LG_BOAT",rsmParametro.getInt("LG_BOAT"));
								parametros.put("LG_TRRAV",rsmParametro.getInt("LG_TRRAV"));
								parametros.put("LG_RRD",rsmParametro.getInt("LG_RRD"));
								parametros.put("TP_CONVENIO_PADRAO",rsmParametro.getInt("TP_CONVENIO_PADRAO"));
								
								parametros.put("NR_VERSAO_MOBILE", rsmParametro.getString("NR_VERSAO_MOBILE"));
								

								parametros.put("NM_SENHA_ADMINISTRATIVA", rsmParametro.getString("NM_SENHA_ADMINISTRATIVA"));
															
							}						
						}
						else {
							String DS_TITULO_1 = ParametroServices.getValorOfParametroAsString("DS_TITULO_1", "-1");
							String DS_TITULO_2 = ParametroServices.getValorOfParametroAsString("DS_TITULO_2", "-1");
							String DS_TITULO_3 = ParametroServices.getValorOfParametroAsString("DS_TITULO_3", "-1");
							String NM_MUNICIPIO = ParametroServices.getValorOfParametroAsString("NM_MUNICIPIO", "-1");
							String NM_PRINTER = ParametroServices.getValorOfParametroAsString("NM_PRINTER", "-1");
							String NM_ORGAO = ParametroServices.getValorOfParametroAsString("NM_ORGAO", "-1");
							String ID_ORGAO = ParametroServices.getValorOfParametroAsString("ID_ORGAO", "-1");
							String HOST_GEO = ParametroServices.getValorOfParametroAsString("HOST_GEO", "-1");
							String URL_DETRAN = ParametroServices.getValorOfParametroAsString("URL_DETRAN", "-1");
							int LG_PREENCHIMENTO_AUTOMATICO = ParametroServices.getValorOfParametroAsInteger("LG_PREENCHIMENTO_AUTOMATICO", -1);
							String NR_VERSAO_MOBILE = ParametroServices.getValorOfParametroAsString("NR_VERSAO_MOBILE", "-1");
							int LG_ATUALIZACAO_MOBILE = ParametroServices.getValorOfParametroAsInteger("LG_ATUALIZACAO_MOBILE", -1);
							String URL_ATUALIZACAO_MOBILE = ParametroServices.getValorOfParametroAsString("URL_ATUALIZACAO_MOBILE", "-1");		
													
							String NR_TELEFONE = ParametroServices.getValorOfParametroAsString("NR_TELEFONE", "-1");
							String NM_EMAIL = ParametroServices.getValorOfParametroAsString("NM_EMAIL", "-1");
							String NM_LOGRADOURO = ParametroServices.getValorOfParametroAsString("NM_LOGRADOURO", "-1");
							String NR_ENDERECO = ParametroServices.getValorOfParametroAsString("NR_ENDERECO", "-1");
							String NM_BAIRRO = ParametroServices.getValorOfParametroAsString("NM_BAIRRO", "-1");
							String SG_DEPARTAMENTO = ParametroServices.getValorOfParametroAsString("SG_DEPARTAMENTO", "-1");

							int LG_BOAT = ParametroServices.getValorOfParametroAsInteger("LG_BOAT", -1);
							int LG_TRRAV = ParametroServices.getValorOfParametroAsInteger("LG_TRRAV", -1);
							int LG_RRD = ParametroServices.getValorOfParametroAsInteger("LG_RRD", -1);
							
							int CD_ORGAO_AUTUADOR = ParametroServices.getValorOfParametroAsInteger("CD_ORGAO_AUTUADOR", -1);
							
							String NM_SENHA_ADMINISTRATIVA = ParametroServices.getValorOfParametroAsString("NM_SENHA_ADMINISTRATIVA", "tivicait123");
							
							parametros.put("DS_TITULO_1",DS_TITULO_1);
							parametros.put("DS_TITULO_2",DS_TITULO_2);
							parametros.put("DS_TITULO_3",DS_TITULO_3);
							parametros.put("NM_MUNICIPIO",NM_MUNICIPIO);
							parametros.put("NM_PRINTER",NM_PRINTER);
							parametros.put("NM_ORGAO",NM_ORGAO);
							parametros.put("ID_ORGAO",ID_ORGAO);
							parametros.put("HOST_GEO",HOST_GEO);
							parametros.put("URL_DETRAN",URL_DETRAN);
							parametros.put("LG_PREENCHIMENTO_AUTOMATICO",LG_PREENCHIMENTO_AUTOMATICO);
							parametros.put("NR_VERSAO_MOBILE",NR_VERSAO_MOBILE);
							parametros.put("LG_ATUALIZACAO_MOBILE",LG_ATUALIZACAO_MOBILE);
							parametros.put("URL_ATUALIZACAO_MOBILE",URL_ATUALIZACAO_MOBILE);
							
							parametros.put("NR_TELEFONE",NR_TELEFONE);
							parametros.put("NM_EMAIL",NM_EMAIL);
							parametros.put("NM_LOGRADOURO",NM_LOGRADOURO);
							parametros.put("NR_ENDERECO",NR_ENDERECO);
							parametros.put("NM_BAIRRO",NM_BAIRRO);
							parametros.put("SG_DEPARTAMENTO",SG_DEPARTAMENTO);							

							parametros.put("LG_BOAT",LG_BOAT);
							parametros.put("LG_TRRAV",LG_TRRAV);
							parametros.put("LG_RRD",LG_RRD);
							
							parametros.put("NM_SENHA_ADMINISTRATIVA", NM_SENHA_ADMINISTRATIVA);

							parametros.put("CD_ORGAO_AUTUADOR",CD_ORGAO_AUTUADOR);								
							parametros.put("LG_BOAT",LG_BOAT);
							parametros.put("LG_TRRAV",LG_TRRAV);	
							parametros.put("LG_RRD",LG_RRD);
						}					
						
						//Result r = new Result(usuario.getCdUsuario(), "Autenticado com sucesso...");
						Result r = new Result(1, "Autenticado com sucesso...");
						
						r.addObject("USUARIO", usuario);
						r.addObject("PESSOA", pessoa);
						r.addObject("AGENTE", agente);
						r.addObject("EQUIPAMENTO", equipamento);
						r.addObject("TALONARIOS", talonarios);
						r.addObject("PARAMETROS", parametros);
						//r.addObject("NR_ULTIMO_AIT", nrUltimoAit);
						
						/* REGISTRAR LOG */
						if(!lgBaseAntiga && usuario!=null && pessoa!=null){
							GregorianCalendar dtLog = new GregorianCalendar();
							String remoteIP = null;
							String txtExecucao = "Autenticação de acesso de agente às " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +
									nmUsuario + ", através da conta " + usuario.getNmLogin() +
								((remoteIP!=null)?", IP "+remoteIP:"")+
								((equipamento!=null)?", Dispositivo "+equipamento.getNmEquipamento():"");
							
							Sistema log = new Sistema(0, //cdLog
									dtLog, //dtLog
									txtExecucao, //txtLog
									com.tivic.manager.log.SistemaServices.TP_LOGIN, //tpLog
									usuario.getCdUsuario());//cdUsuario
							SistemaDAO.insert(log, connect);
						}
						
						/* REGISTRAR HISTORICO EQUIPAMENTO */
						if(equipamento!=null && usuario!=null) {
							GregorianCalendar dtHistorico = new GregorianCalendar();
							String txtHistorico = "Equipamento retirado às " + Util.formatDateTime(dtHistorico, "dd/MM/yyyy HH:mm:ss", "") + " por " +
									nmUsuario + ", através da conta " + usuario.getNmLogin() +
									((equipamento!=null)?", Dispositivo "+equipamento.getNmEquipamento() + " [" + equipamento.getIdEquipamento() +"]":"");
							
							EquipamentoHistorico historico = new EquipamentoHistorico(equipamento.getCdEquipamento(), 0, 
									usuario.getCdUsuario(), dtHistorico, txtHistorico, 
									EquipamentoServices.TALONARIO_ELETRONICO, 0, 0);
							
							EquipamentoHistoricoDAO.insert(historico, connect);
						}
						
						/* TRAVAR LOGIN DE AGENTE PARA EVITAR O USO EM 2 TABLETS */
						if(usuario!=null) {
							if(travarLoginAgente(usuario.getCdUsuario(), equipamento.getCdEquipamento()).getCode()<=0)
								return new Result(-8, "Erro ao travar login do agente.");
						}
						
						return r;
					}
					else {
						return new Result(-3, "Equipamento não registrado.");
					}
				}
				else
					return new Result(-2, "Senha inválida...");
			}
			else
				return new Result(-1, "Login inválido...");
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-4, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result travarLoginAgente(int cdUsuario, int cdEquipamento) {
		return alterarTravamentoAgente(cdUsuario, 1, cdEquipamento, null);
	}
	
	public static Result destravarLoginAgente(int cdUsuario) {
		return alterarTravamentoAgente(cdUsuario, 0, 0, null);
	}
	
	public static Result alterarTravamentoAgente(int cdUsuario, int stLogin, int cdEquipamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = lgBaseAntiga ? "update usuario set st_login=?, cd_equipamento=? where cod_usuario = ?" : 
										"update seg_usuario set st_login=?, cd_equipamento=? where cd_usuario = ?";
	
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, stLogin);
			if(cdEquipamento<=0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2, cdEquipamento);
			pstmt.setInt(3, cdUsuario);
			pstmt.executeUpdate();
						
			Agente agente = AgenteServices.getAgenteByUsuario(cdUsuario, connect);
			
			if(agente == null) {
				LogUtils.info("Agente indicado é nulo ou inexistente.");
				return new Result(-3, "Agente indicado é nulo ou inexistente.");
			}
				
			System.out.println("["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Agente '"+agente.getNmAgente()+"', login "+(stLogin==0?" destravado" : " travado")+".");
			
			return new Result(1, "Login de agente travado com sucesso.");
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace();
			return new Result(-1, "Erro ao alterar travamento de login do agente.");
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-2, "Erro desconhecido ao alterar travamento de login do agente.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean isLoginAgenteTravado(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = lgBaseAntiga ? "select st_login from usuario where cod_usuario = ?" : 
										"select st_login from seg_usuario where cd_usuario = ?";
	
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdUsuario);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return rsm.getInt("ST_LOGIN") == 1 ? true : false;
			
			return false;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.isLoginAgenteTravado: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.isLoginAgenteTravado: " + e);
			return  false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Equipamento getEquipamentoLogin(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = lgBaseAntiga ? "select cd_equipamento from usuario where cod_usuario = ?" : 
										"select cd_equipamento from seg_usuario where cd_usuario = ?";
	
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdUsuario);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return EquipamentoDAO.get(rsm.getInt("cd_equipamento"), connect);
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getEquipamentoLogin: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getEquipamentoLogin: " + e);
			return  null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Agente getAgenteByUsuario(int cdUsuario) {
		return getAgenteByUsuario(cdUsuario, null);
	}

	public static Agente getAgenteByUsuario(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			boolean lgBaseAntiga = Util.isStrBaseAntiga();
			
			String sql = "SELECT * FROM mob_agente WHERE cd_usuario=?";
					
			if(lgBaseAntiga)
				sql = "SELECT * FROM agente WHERE cod_usuario=?";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga) {
					return new Agente(rs.getInt("cod_agente"),
							rs.getString("nm_agente"),
							rs.getString("ds_endereco"),
							rs.getString("bairro"),
							rs.getString("nr_cep"),
							rs.getInt("cod_municipio"),
							rs.getString("nr_matricula"),
							rs.getInt("cod_usuario"),
							rs.getInt("tp_agente"));
				}
				else {
					return new Agente(rs.getInt("cd_agente"),
							rs.getString("nm_agente"),
							rs.getString("ds_endereco"),
							rs.getString("nm_bairro"),
							rs.getString("nr_cep"),
							rs.getInt("cd_cidade"),
							rs.getString("nr_matricula"),
							rs.getInt("cd_usuario"),
							rs.getInt("tp_agente"));
				}
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getAgenteByUsuario: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getAgenteByUsuario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Agente getByNrMatricula(String nrMatricula) {
		return getByNrMatricula(nrMatricula, null);
	}

	public static Agente getByNrMatricula(String nrMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = "SELECT * FROM mob_agente WHERE nr_matricula=?";
					
			if(lgBaseAntiga)
				sql = "SELECT * FROM agente WHERE nr_matricula=?";
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nrMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga) {
					return new Agente(rs.getInt("cod_agente"),
							rs.getString("nm_agente"),
							rs.getString("ds_endereco"),
							rs.getString("bairro"),
							rs.getString("nr_cep"),
							rs.getInt("cod_municipio"),
							rs.getString("nr_matricula"),
							rs.getInt("cod_usuario"),
							rs.getInt("tp_agente"));
				}
				else {
					return new Agente(rs.getInt("cd_agente"),
							rs.getString("nm_agente"),
							rs.getString("ds_endereco"),
							rs.getString("nm_bairro"),
							rs.getString("nr_cep"),
							rs.getInt("cd_cidade"),
							rs.getString("nr_matricula"),
							rs.getInt("cd_usuario"),
							rs.getInt("tp_agente"));
				}
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getByNrMatricula: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getByNrMatricula: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getAllByIdOrgao(String idOrgao) {
		return getAllByIdOrgao(idOrgao, null);
	}

	public static ResultSetMap getAllByIdOrgao(String idOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_agente A " +
					"JOIN str_orgao B ON (A.cd_orgao = B.cd_orgao AND B.id_orgao=?) " +
					"ORDER BY A.nm_agente");
			pstmt.setString(1, idOrgao);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getAllByIdOrgao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getAllByIdOrgao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllAgentes() {
		return getAllAgentes(null);
	}
	
	public static ResultSetMap getAllAgentes(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			pstmt = connect.prepareStatement("SELECT * FROM str_agente");
			
			if(lgBaseAntiga) {
				pstmt = connect.prepareStatement(
						"SELECT A.cod_agente AS cd_agente, A.*, A.cod_usuario AS cd_usuario, B.* "
						+ " FROM agente A "
						+ " LEFT OUTER JOIN usuario B ON (A.cod_usuario = B.cod_usuario)"
						+ " ORDER BY A.nm_agente"
						);
			}
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getAllAgentes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getAllAgentes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getUltimoNumeroBoat(Agente agente, boolean lgTransporte) {
		return getUltimoNumeroBoat(agente, lgTransporte, null);
	}
	
	public static int getUltimoNumeroBoat(Agente agente, boolean lgTransporte, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			int nrUltimoAitTalao = 0;
			//ArrayList<Talonario> talonarios = new ArrayList<Talonario>();
			
			String sql = "SELECT * FROM talonario " +
						  "WHERE cod_agente = ? " +
						  "  AND ( tp_talao = " + TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT + ")";
		    sql 	  +=  "  AND st_talao = 1 " +
		    			  " ORDER BY dt_entrega";
		    
		    PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, agente.getCdAgente());
			//pstmt.setInt(2, nrUltimoAit);
			ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());
									
			//boolean lgTalaoAtivo = false;
			while(rsmTalonario.next()) {				
				sql = "SELECT MAX(nr_boat) as nr_ultimo_boat FROM mob_boat  WHERE cd_agente = ? AND nr_boat >=  ? AND nr_boat <= ?";
				
				PreparedStatement pstmt2 = connect.prepareStatement(sql);
				pstmt2.setInt(1, agente.getCdAgente());
				pstmt2.setInt(2, rsmTalonario.getInt("NR_INICIAL"));
				pstmt2.setInt(3, rsmTalonario.getInt("NR_FINAL"));	
				
				ResultSet rsNr = pstmt2.executeQuery();
				
				//List<Integer> aitsAgente = new ArrayList<Integer>();
				
				if(rsNr.next()){
					nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_BOAT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_BOAT");									
				}
				else{
				    nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL")-1;
				}
				
				rsNr.close();
				
				//SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
				if(nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL"))
					continue;
				
			}
					 
			 
			 return nrUltimoAitTalao + 1;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getUltimoNumeroBoat: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getUltimoNumeroAit(Agente agente, boolean lgTransporte) {
		return getUltimoNumeroAit(agente, lgTransporte, null);
	}
	
	public static int getUltimoNumeroAit(Agente agente, boolean lgTransporte, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			int nrUltimoAitTalao = 0;
			//ArrayList<Talonario> talonarios = new ArrayList<Talonario>();
			
			String sql = "SELECT * FROM talonario " +
						  "WHERE cod_agente = ? " +
						  "  AND ( tp_talao = " + TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO + ")";
		    sql 	  +=  "  AND st_talao = 1 " +
		    			  " ORDER BY dt_entrega";
		    
		    PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, agente.getCdAgente());
			//pstmt.setInt(2, nrUltimoAit);
			ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());
									
			//boolean lgTalaoAtivo = false;
			while(rsmTalonario.next()) {				
				sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM AIT WHERE cod_agente = ? AND nr_ait >=  ? AND nr_ait <= ?";
				
				PreparedStatement pstmt2 = connect.prepareStatement(sql);
				pstmt2.setInt(1, agente.getCdAgente());
				pstmt2.setInt(2, rsmTalonario.getInt("NR_INICIAL"));
				pstmt2.setInt(3, rsmTalonario.getInt("NR_FINAL"));	
				System.out.println(pstmt2.toString());
				ResultSet rsNr = pstmt2.executeQuery();
				
				//List<Integer> aitsAgente = new ArrayList<Integer>();
				
				if(rsNr.next()){
					nrUltimoAitTalao = rsNr.getInt("NR_ULTIMA_AIT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_BOAT");									
				}
				else{
				    nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL")-1;
				}
				
				rsNr.close();
				
				//SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
				if(nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL"))
					continue;
				
			}
					 
			 
			 return nrUltimoAitTalao + 1;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getUltimoNumeroAit: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ArrayList<Talonario> getAgenteTalonarioVM(Agente agente, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {

			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			//boolean lgGctPadrao = Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
			
			String sql = "";
			PreparedStatement pstmt;
			//Result result;
			ArrayList<Talonario> talonarios = new ArrayList<Talonario>();
			
			//verificar talonarios
			if(lgBaseAntiga){
				sql = "SELECT * FROM talonario " +
					  "WHERE cod_agente = ? " +
					  "  AND ( tp_talao = " + TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO + ")"; 
			 sql +=   "  AND st_talao = 1 " +
					  " ORDER BY dt_entrega";
			}						
			else{
				sql = "SELECT * FROM mob_talonario " +
					  "WHERE cd_agente = ? " +
					  "  AND (tp_talao = " + TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO + ")" +
					  "  AND st_talao = 1" +
					  //"  AND ? <= A.nr_final" +
					  " ORDER BY dt_entrega";
			}
									
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, agente.getCdAgente());
			//pstmt.setInt(2, nrUltimoAit);
			ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());
									
			//boolean lgTalaoAtivo = false;
			while(rsmTalonario.next()) {

				int nrUltimoAitTalao = 0;
				
				//Ultimo AIT do talonario
				if(lgBaseAntiga) {
					sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM AIT WHERE cod_agente = ? AND nr_ait >=  ? AND nr_ait <= ?";			
				} else {
					if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO){
						sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE cd_agente = ? AND nr_ait >=  ? AND nr_ait <= ?";
					}
				}
				
				PreparedStatement pstmt2 = connect.prepareStatement(sql);
				
				if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO) {
					pstmt2.setInt(1, agente.getCdAgente());
					pstmt2.setString(2, rsmTalonario.getString("NR_INICIAL"));
					pstmt2.setString(3, rsmTalonario.getString("NR_FINAL"));
				}
				
				System.out.println(pstmt2.toString());
				
				ResultSet rsNr = pstmt2.executeQuery();
				
				if( (rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_VIDEO_MONITORAMENTO)) {						
					if(rsNr.next()) {				
						nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_AIT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_AIT");
					}					
				}				
	
				System.out.println("\n--\nTalão Tipo: "+ rsmTalonario.getInt("TP_TALAO"));
				System.out.println("   Inicial: "+ rsmTalonario.getInt("NR_INICIAL"));
				System.out.println("     Final: "+ rsmTalonario.getInt("NR_FINAL"));
				System.out.println(" Ultimo Nº: "+ nrUltimoAitTalao);
				
				rsNr.close(); 		
				//SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
				if(nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL"))
					continue;
				
				Talonario talao;
				if(lgBaseAntiga)
					talao = new Talonario(rsmTalonario.getInt("COD_TALAO"),
							rsmTalonario.getInt("COD_AGENTE"),
							rsmTalonario.getInt("NR_INICIAL"),
							rsmTalonario.getInt("NR_FINAL"),
							(rsmTalonario.getTimestamp("DT_ENTREGA")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()),
							(rsmTalonario.getTimestamp("DT_DEVOLUCAO")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()),
							rsmTalonario.getInt("ST_TALAO"),
							rsmTalonario.getInt("NR_TALAO"),
							rsmTalonario.getInt("TP_TALAO"),
							rsmTalonario.getString("SG_TALAO"), nrUltimoAitTalao);
				else
					talao = new Talonario(rsmTalonario.getInt("CD_TALAO"),
							rsmTalonario.getInt("CD_AGENTE"),
							rsmTalonario.getInt("NR_INICIAL"),
							rsmTalonario.getInt("NR_FINAL"),
							(rsmTalonario.getTimestamp("DT_ENTREGA")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()),
							(rsmTalonario.getTimestamp("DT_DEVOLUCAO")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()),
							rsmTalonario.getInt("ST_TALAO"),
							rsmTalonario.getInt("NR_TALAO"),
							rsmTalonario.getInt("TP_TALAO"),
							rsmTalonario.getString("SG_TALAO"), nrUltimoAitTalao);
				talonarios.add(talao);				
			}	
			
			
			if(talonarios.size()==0) {
				return null;
			}
			
			Collections.sort(talonarios, new Comparator<Talonario>() {
		        @Override public int compare(Talonario p1, Talonario p2) {
		            return p1.getNrInicial() - p2.getNrInicial();
		        }
		    });
			return talonarios;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getUltimoNumeroAit: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<String, Object> getParametros(Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		//boolean lgGctPadrao = Util.getConfManager().getProps().getProperty("GCT_PADRAO").equals("1");
		
		try {
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			if(lgBaseAntiga) {						
			
				//BUSCA DE PARAMETROS DO SERVER PARA SINCRONIZACAO NOS EQUIPAMENTOS
				PreparedStatement pstmt3 = connect.prepareStatement(
						"select NR_VERSAO_MOBILE, NM_MUNICIPIO_MOBILE, NM_ORGAO, URL_DETRAN, IMG_LOGO_ORGAO, NR_ENDERECO, NR_TELEFONE, SG_DEPARTAMENTO, NM_LOGRADOURO, NM_PRINTER, " + 
						"LG_PREENCHIMENTO_AUTOMATICO, NM_EMAIL, IMG_LOGO_DEPARTAMENTO, CD_MUNICIPIO_AUTUADOR, HOST_GEO, CD_ORGAO_AUTUANTE, URL_ATUALIZACAO_MOBILE, LG_ATUALIZACAO_MOBILE," + 
						" DS_TITULO_2, DS_TITULO_3, DS_TITULO_1, NM_SENHA_ADMINISTRATIVA, NM_BAIRRO from PARAMETRO", ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = pstmt3.executeQuery();
				ResultSetMap rsmParametro = new ResultSetMap(rs);
				
				
				if(rsmParametro.next()) {
					parametros.put("DS_TITULO_1",rsmParametro.getString("DS_TITULO_1"));
					parametros.put("DS_TITULO_2",rsmParametro.getString("DS_TITULO_2"));
					parametros.put("DS_TITULO_3",rsmParametro.getString("DS_TITULO_3"));
					parametros.put("NM_MUNICIPIO",rsmParametro.getString("NM_MUNICIPIO_MOBILE"));
					parametros.put("NM_PRINTER",rsmParametro.getString("NM_PRINTER"));
					parametros.put("CD_MUNICIPIO_AUTUADOR",rsmParametro.getString("CD_MUNICIPIO_AUTUADOR"));
					parametros.put("NM_ORGAO",rsmParametro.getString("NM_ORGAO"));
					parametros.put("ID_ORGAO",rsmParametro.getString("CD_ORGAO_AUTUANTE"));
					parametros.put("HOST_GEO",rsmParametro.getString("HOST_GEO"));
					parametros.put("URL_DETRAN",rsmParametro.getString("URL_DETRAN"));
					parametros.put("LG_PREENCHIMENTO_AUTOMATICO",rsmParametro.getInt("LG_PREENCHIMENTO_AUTOMATICO"));
					parametros.put("NR_VERSAO_MOBILE",rsmParametro.getString("NR_VERSAO_MOBILE"));
					parametros.put("LG_ATUALIZACAO_MOBILE",rsmParametro.getInt("LG_ATUALIZACAO_MOBILE"));
					parametros.put("URL_ATUALIZACAO_MOBILE",rsmParametro.getString("URL_ATUALIZACAO_MOBILE"));
					
					parametros.put("NR_TELEFONE",rsmParametro.getString("NR_TELEFONE"));
					parametros.put("NM_EMAIL",rsmParametro.getString("NM_EMAIL"));
					parametros.put("NM_LOGRADOURO", rsmParametro.getString("NM_LOGRADOURO"));
					parametros.put("NR_ENDERECO", rsmParametro.getString("NR_ENDERECO"));
					parametros.put("NM_BAIRRO", rsmParametro.getString("NM_BAIRRO"));
					parametros.put("SG_DEPARTAMENTO", rsmParametro.getString("SG_DEPARTAMENTO"));
					
					parametros.put("NR_VERSAO_MOBILE", rsmParametro.getString("NR_VERSAO_MOBILE"));

					parametros.put("NM_SENHA_ADMINISTRATIVA", rsmParametro.getString("NM_SENHA_ADMINISTRATIVA"));	
				}
								
				rs.beforeFirst();
				
//				if(rs.next()) {
//					parametros.put("IMG_LOGO_ORGAO", rs.getBytes("IMG_LOGO_ORGAO"));	
//					parametros.put("IMG_LOGO_DEPARTAMENTO", rs.getBytes("IMG_LOGO_DEPARTAMENTO"));		
//				}
			}
			else {
				String DS_TITULO_1 = ParametroServices.getValorOfParametroAsString("DS_TITULO_1", "-1");
				String DS_TITULO_2 = ParametroServices.getValorOfParametroAsString("DS_TITULO_2", "-1");
				String DS_TITULO_3 = ParametroServices.getValorOfParametroAsString("DS_TITULO_3", "-1");
				String NM_MUNICIPIO = ParametroServices.getValorOfParametroAsString("NM_MUNICIPIO", "-1");
				String NM_PRINTER = ParametroServices.getValorOfParametroAsString("NM_PRINTER", "-1");
				String NM_ORGAO = ParametroServices.getValorOfParametroAsString("NM_ORGAO", "-1");
				String ID_ORGAO = ParametroServices.getValorOfParametroAsString("ID_ORGAO", "-1");
				String HOST_GEO = ParametroServices.getValorOfParametroAsString("HOST_GEO", "-1");
				String URL_DETRAN = ParametroServices.getValorOfParametroAsString("URL_DETRAN", "-1");
				int LG_PREENCHIMENTO_AUTOMATICO = ParametroServices.getValorOfParametroAsInteger("LG_PREENCHIMENTO_AUTOMATICO", -1);
				String NR_VERSAO_MOBILE = ParametroServices.getValorOfParametroAsString("NR_VERSAO_MOBILE", "-1");
				int LG_ATUALIZACAO_MOBILE = ParametroServices.getValorOfParametroAsInteger("LG_ATUALIZACAO_MOBILE", -1);
				String URL_ATUALIZACAO_MOBILE = ParametroServices.getValorOfParametroAsString("URL_ATUALIZACAO_MOBILE", "-1");		
										
				String NR_TELEFONE = ParametroServices.getValorOfParametroAsString("NR_TELEFONE", "-1");
				String NM_EMAIL = ParametroServices.getValorOfParametroAsString("NM_EMAIL", "-1");
				String NM_LOGRADOURO = ParametroServices.getValorOfParametroAsString("NM_LOGRADOURO", "-1");
				String NR_ENDERECO = ParametroServices.getValorOfParametroAsString("NR_ENDERECO", "-1");
				String NM_BAIRRO = ParametroServices.getValorOfParametroAsString("NM_BAIRRO", "-1");
				String SG_DEPARTAMENTO = ParametroServices.getValorOfParametroAsString("SG_DEPARTAMENTO", "-1");
				
				String NM_SENHA_ADMINISTRATIVA = ParametroServices.getValorOfParametroAsString("NM_SENHA_ADMINISTRATIVA", "tivicait123");
				
				parametros.put("DS_TITULO_1",DS_TITULO_1);
				parametros.put("DS_TITULO_2",DS_TITULO_2);
				parametros.put("DS_TITULO_3",DS_TITULO_3);
				parametros.put("NM_MUNICIPIO",NM_MUNICIPIO);
				parametros.put("NM_PRINTER",NM_PRINTER);
				parametros.put("NM_ORGAO",NM_ORGAO);
				parametros.put("ID_ORGAO",ID_ORGAO);
				parametros.put("HOST_GEO",HOST_GEO);
				parametros.put("URL_DETRAN",URL_DETRAN);
				parametros.put("LG_PREENCHIMENTO_AUTOMATICO",LG_PREENCHIMENTO_AUTOMATICO);
				parametros.put("NR_VERSAO_MOBILE",NR_VERSAO_MOBILE);
				parametros.put("LG_ATUALIZACAO_MOBILE",LG_ATUALIZACAO_MOBILE);
				parametros.put("URL_ATUALIZACAO_MOBILE",URL_ATUALIZACAO_MOBILE);
				
				parametros.put("NR_TELEFONE",NR_TELEFONE);
				parametros.put("NM_EMAIL",NM_EMAIL);
				parametros.put("NM_LOGRADOURO",NM_LOGRADOURO);
				parametros.put("NR_ENDERECO",NR_ENDERECO);
				parametros.put("NM_BAIRRO",NM_BAIRRO);
				parametros.put("SG_DEPARTAMENTO",SG_DEPARTAMENTO);
				
				parametros.put("NM_SENHA_ADMINISTRATIVA", NM_SENHA_ADMINISTRATIVA);
				
			}	
			
			return parametros;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getParametros: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insert(Agente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agente (cod_agente,"+
			                                  "nm_agente,"+
			                                  "ds_endereco,"+
			                                  "bairro,"+
			                                  "nr_cep,"+
			                                  "cod_municipio,"+
			                                  "nr_matricula,"+
			                                  "cod_usuario,"+
			                                  "tp_agente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAgente());
			pstmt.setString(3,objeto.getDsEndereco());
			pstmt.setString(4,objeto.getNmBairro());
			pstmt.setString(5,objeto.getNrCep());
			pstmt.setInt(6,objeto.getCdMunicipio());
			pstmt.setString(7,objeto.getNrMatricula());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.setInt(9, objeto.getTpAgente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int update(Agente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agente SET cod_agente=?,"+
												      		   "nm_agente=?,"+
												      		   "ds_endereco=?,"+
												      		   "bairro=?,"+
												      		   "nr_cep=?,"+
												      		   "cod_municipio=?,"+
												      		   "nr_matricula=?,"+
												      		   "cod_usuario=? WHERE cod_agente=?");
			pstmt.setInt(1,objeto.getCdAgente());
			pstmt.setString(2,objeto.getNmAgente());
			pstmt.setString(3,objeto.getDsEndereco());
			pstmt.setString(4,objeto.getNmBairro());
			pstmt.setString(5,objeto.getNrCep());
			pstmt.setInt(6,objeto.getCdMunicipio());
			pstmt.setString(7,objeto.getNrMatricula());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.setInt(9,objeto.getCdAgente());
			pstmt.executeUpdate();
			
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! AgenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.out.println("Erro! AgenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static com.tivic.manager.mob.Agente toMobAgente(com.tivic.manager.str.Agente agente) {
				
		return new com.tivic.manager.mob.Agente(agente.getCdAgente(),
				agente.getCdUsuario(),
				agente.getNmAgente(),
				agente.getDsEndereco(),
				agente.getNmBairro(),
				agente.getNrCep(),
				agente.getCdMunicipio(),
				agente.getNrMatricula(),
				agente.getTpAgente());
	}
}