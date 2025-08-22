package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.EquipamentoHistorico;
import com.tivic.manager.grl.EquipamentoHistoricoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.Equipamento;
import com.tivic.manager.grl.EquipamentoServices;
import com.tivic.manager.grl.EquipamentoDAO;
import com.tivic.manager.log.Sistema;
import com.tivic.manager.log.SistemaDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AgenteServices {
		
	public static final int TP_TRANSITO = 0;
	public static final int TP_TRANSPORTE = 1;
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;
	
	
	public static Result autenticar(String nmLogin, String nmSenha, int lgTransporte){
		return autenticar(nmLogin, nmSenha, lgTransporte, null);
	}
	
	public static Result autenticar(String nmLogin, String nmSenha, int lgTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			nmLogin = nmLogin.trim();
			nmSenha = nmSenha.trim();
	    	if(ManagerConf.getInstance().getAsBoolean("PASS_UPPERCASE")) {
	    		nmLogin = nmLogin.toUpperCase();
	    		nmSenha = nmSenha.toUpperCase();
	    	}
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement("SELECT * FROM mob_agente A "+
																		  " JOIN seg_usuario B ON ( A.cd_usuario = B.cd_usuario ) "+
																		  " JOIN grl_pessoa C ON ( B.cd_pessoa = C.cd_pessoa ) "+
																		  " WHERE B.nm_login = '"+nmLogin+"' and B.nm_senha = '"+nmSenha+"'").executeQuery());
			if( nmLogin.length() == 0 || nmSenha.length() == 0 || !rsm.next() ){
				return new Result(-1, "Login ou Senha invalido");
			}
			
			HashMap<String, Object> agente = rsm.getRegister();
			agente.put("NM_SENHA", "");
			return new Result(1, "Login efetuado com sucesso!", "AGENTE", agente);
			
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
	
	public static Result save(Agente agente){
		return save(agente, null);
	}

	public static Result save(com.tivic.manager.mob.Agente agente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(agente==null)
				return new Result(-1, "Erro ao salvar. Agente e nulo");
			
			Result result = null;
			int retorno;
			
			if(Util.isStrBaseAntiga()) {
				result = com.tivic.manager.str.AgenteServices.save(toStrAgente(agente), null, connect);
								
				retorno = result.getCode();
			} else {
				if(agente.getCdAgente()==0) {
					retorno = AgenteDAO.insert(agente, connect);
					agente.setCdAgente(retorno);
				}
				else {
					retorno = AgenteDAO.update(agente, connect);
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return (result!=null ? result : new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AGENTE", agente));
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
	public static Result remove(int cdAgente){
		return remove(cdAgente, false, null);
	}
	public static Result remove(int cdAgente, boolean cascade){
		return remove(cdAgente, cascade, null);
	}
	public static Result remove(int cdAgente, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(Util.isStrBaseAntiga()) {
				//return com.tivic.manager.str.AgenteServices.remove
			}
			
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = AgenteDAO.delete(cdAgente, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro esta vinculado a outros e não pode ser excluído!");
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
		return getAll(null, null);
	}

	public static ResultSetMap getAll(Connection connect) {
		
		return getAll(null, null);
	}
	
	public static Agente getBaseAntiga(int cdAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agente WHERE cod_agente=?");
			pstmt.setInt(1, cdAgente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Agente(rs.getInt("cod_agente"),
						rs.getInt("cod_usuario"),
						rs.getString("nm_agente"),
						rs.getString("ds_endereco"),
						rs.getString("bairro"),
						rs.getString("nr_cep"),
						rs.getInt("cod_municipio"),
						rs.getString("nr_matricula"),
						rs.getInt("tp_agente"),
						0,
						0);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByIdOrgao(String idOrgao) {
		ArrayList<ItemComparator> criterios = new ArrayList<>();
		criterios.add(new ItemComparator("E.id_orgao", idOrgao, ItemComparator.LIKE, Types.VARCHAR));
		criterios.add(new ItemComparator("A.st_Agente", String.valueOf(ST_ATIVO), ItemComparator.EQUAL, Types.SMALLINT));
		criterios.add(new ItemComparator("ORDERBY", "A.nm_agente", ItemComparator.LIKE, Types.VARCHAR));
		return getAll(criterios, null);
	}
	
	public static ResultSetMap getAllByTpAgente(int tpAgente) {
		ArrayList<ItemComparator> criterios = new ArrayList<>();
		criterios.add(new ItemComparator("A.tp_agente", String.valueOf(tpAgente), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("ORDERBY", "A.nm_agente", ItemComparator.LIKE, Types.VARCHAR));
		return getAll(criterios, null);
	}
	
	public static ResultSetMap getAll(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
//			if(Util.isStrBaseAntiga()) {
//				return com.tivic.manager.str.AgenteServices.
//			}
			
			String orderBy = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
		
			ResultSetMap rsm = Search.find(" SELECT A.*, B.*, C.*, F.*, D.nm_cidade, E.nm_orgao, E.id_orgao " +
											 "FROM mob_agente  A "+
											 "LEFT JOIN SEG_USUARIO 		     B ON ( A.CD_USUARIO = B.CD_USUARIO ) "+
											 "LEFT JOIN GRL_PESSOA 			 C ON ( B.CD_PESSOA = C.CD_PESSOA ) " + 
											 "LEFT OUTER JOIN grl_cidade D ON (A.cd_cidade = D.cd_cidade) " +
											 "LEFT OUTER JOIN mob_orgao  E ON (A.cd_orgao = E.cd_orgao) " +
											 "LEFT OUTER JOIN grl_equipamento  F ON (B.cd_equipamento = F.cd_equipamento) " , 
											 (orderBy != "" ? orderBy : ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findByNrMatricula(String nrMatricula) {
		ArrayList<ItemComparator> criterios = new ArrayList<>();
		criterios.add(new ItemComparator("nr_matricula", nrMatricula, ItemComparator.LIKE, Types.VARCHAR));
		return find(criterios, null);
	}
	
	

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String orderBy = "";
			String limit = "";
			String keyword  = "";
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("CD_AGENTE")) {
					if(lgBaseAntiga) {
						criterios.add(new ItemComparator("COD_AGENTE", criterios.get(i).getValue().toString().trim(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.remove(i);
						i--;
					}
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("keyword")) {
					if(Util.isNumber(criterios.get(i).getValue()))
						keyword = criterios.get(i).getValue();
					else
						criterios.add(new ItemComparator("A.nm_agente", criterios.get(i).getValue(), ItemComparator.LIKE_ANY, Types.VARCHAR));

					criterios.remove(i);
				}
				else if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
					limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}

			ResultSetMap rsm = new ResultSetMap();
			
			if(lgBaseAntiga) {
				rsm = Search.find(" SELECT A.*, A.COD_AGENTE as CD_AGENTE, "
						+ " B.nm_municipio "
						+ " FROM agente A"
						+ " LEFT OUTER JOIN municipio B ON (A.cod_municipio = B.cod_municipio)"
						+ " LEFT OUTER JOIN usuario   C ON (A.cod_usuario = C.cod_usuario)", 
						(orderBy != "" ? orderBy : ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			} else {
				
				String sql = "SELECT A.*, F.*, B.nm_cidade, C.nm_orgao, C.id_orgao "+
				 		   "FROM mob_agente A "+
						   "LEFT OUTER JOIN grl_cidade  B ON (A.cd_cidade = B.cd_cidade) "+
						   "LEFT OUTER JOIN mob_orgao   C ON (A.cd_orgao = C.cd_orgao) " +
						   "LEFT OUTER JOIN seg_usuario D ON (A.cd_usuario = D.cd_usuario) " +
						   "LEFT OUTER JOIN grl_pessoa  E ON (D.cd_pessoa = E.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_equipamento  F ON (D.cd_equipamento = F.cd_equipamento) ";
				
				if(!keyword.equals("")) {
					if(Util.isNumber(keyword))
						sql += " WHERE ((UPPER(A.nm_agente) LIKE '%"+keyword+"%' OR CAST(A.nr_matricula as VARCHAR) LIKE '%"+keyword+"%')) ";
				}
				
				rsm = Search.find(sql, 
						   (orderBy != "" ? orderBy : "") + limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			}
			
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteServices.find: " + e);
			return null;
		}
	}	

	
	public static Agente get(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement ps = connect.prepareStatement("SELECT cd_agente FROM mob_agente WHERE cd_usuario=?");
			ps.setInt(1, cdUsuario);
			
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
				return null;
						
			return AgenteDAO.get(rs.getInt("cd_agente"), connect);
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.out.println("Erro! AgenteServices.get: " +  e);
			return null;
		} finally{
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
		if(Util.isStrBaseAntiga())
			return com.tivic.manager.str.AgenteServices.autenticar(nmLogin, nmSenha, idEquipamento, lgTransporte, connect);
		
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String sql = "SELECT * FROM seg_usuario " +
						 " WHERE nm_login	 = ? " +
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
				
				String nmUsuario = agente==null ? "" : agente.getNmAgente();
												
				if(nmSenha.equalsIgnoreCase(usuario.getNmSenha())) {					
					//verificar travamento de login
					Equipamento e = getEquipamentoLogin(usuario.getCdUsuario(), connect);
					if(e!=null && isLoginAgenteTravado(usuario.getCdUsuario(), connect) && !idEquipamento.equals(e.getIdEquipamento())) {
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
						sql = "SELECT * FROM mob_talonario " +
								  "WHERE cd_agente = ? " +
								  "  AND (tp_talao = " + (lgTransporte ? TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE : TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO) +
								  " 	    OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT +
								  " 	    OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_TRRAV + 
								  " 	    OR tp_talao = " + 	TalonarioServices.TP_TALONARIO_ELETRONICO_RRD + ")"+
								  "  AND st_talao = " + TalonarioServices.ST_TALAO_ATIVO +
								  //"  AND ? <= A.nr_final" +
								  " ORDER BY dt_entrega";
												
						pstmt = connect.prepareStatement(sql);
						pstmt.setInt(1, agente.getCdAgente());
						//pstmt.setInt(2, nrUltimoAit);
						ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());
						
												
						//boolean lgTalaoAtivo = false;
						while(rsmTalonario.next()) {
							
							int nrUltimoAitTalao = 0;
							
							//Ultimo AIT do talonario
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
							
							
							//System.out.println(sql);
							PreparedStatement pstmt2 = connect.prepareStatement(sql);
							
							if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) {
								pstmt2.setString(1, rsmTalonario.getString("NR_INICIAL"));
								pstmt2.setString(2, rsmTalonario.getString("NR_FINAL"));
								pstmt2.setInt(3, agente.getCdAgente());
							}
							else {
								pstmt2.setInt(1, rsmTalonario.getInt("NR_INICIAL"));
								pstmt2.setInt(2, rsmTalonario.getInt("NR_FINAL"));	
							}
							
							ResultSet rsNr = pstmt2.executeQuery();
							
							// Ordena e pega o maior nr da AIT Transporte ou No caso do ait no Padrão GCT
							List<Integer> aitsAgente = new ArrayList<Integer>();
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
							

							System.out.println("\n--\nTalao Tipo: "+ rsmTalonario.getInt("TP_TALAO"));
							System.out.println("   Inicial: "+ rsmTalonario.getInt("NR_INICIAL"));
							System.out.println("     Final: "+ rsmTalonario.getInt("NR_FINAL"));
							System.out.println(" Ultimo No.: "+ nrUltimoAitTalao);
							
							rsNr.close();
							
							
							//SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
							if(nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL")) {
								
								if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) {
									com.tivic.manager.mob.Talonario talao = com.tivic.manager.mob.TalonarioDAO.get(rsmTalonario.getInt("cd_talao"), connect);
									talao.setStTalao(TalonarioServices.ST_TALAO_PENDENTE);
									com.tivic.manager.mob.TalonarioDAO.update(talao, connect);
								}
								else {
									int cdTalonario = rsmTalonario.getInt("cd_talao");
									Talonario talao = TalonarioDAO.get(cdTalonario, connect);
									talao.setStTalao(TalonarioServices.ST_TALAO_PENDENTE);
									TalonarioDAO.update(talao, connect);
								}
								continue;
							}
							
							Talonario talao = new Talonario(rsmTalonario.getInt("CD_TALAO"), 
									rsmTalonario.getInt("NR_TALAO"), 
									rsmTalonario.getInt("NR_INICIAL"), 
									rsmTalonario.getInt("NR_FINAL"), 
									rsmTalonario.getInt("CD_AGENTE"), 
									(rsmTalonario.getTimestamp("DT_ENTREGA")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()),
									(rsmTalonario.getTimestamp("DT_DEVOLUCAO")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()),
									rsmTalonario.getInt("ST_TALAO"), 
									rsmTalonario.getInt("TP_TALAO"), 
									rsmTalonario.getString("SG_TALAO"), 
									rsmTalonario.getInt("ST_LOGIN"), 
									nrUltimoAitTalao);
							
							
							talonarios.add(talao);
						}	
						
						if(talonarios.size()==0) {
							return new Result(-5, "Nenhum talão distribuído para o agente. Entre em contato com a administção.");
						}
						
						HashMap<String, Object> parametros = new HashMap<String, Object>();
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

						int CD_MUNICIPIO_AUTUADOR = ParametroServices.getValorOfParametroAsInteger("CD_MUNICIPIO_AUTUADOR", -1);
						
						int LG_BOAT = ParametroServices.getValorOfParametroAsInteger("LG_BOAT", -1);
						int LG_TRRAV = ParametroServices.getValorOfParametroAsInteger("LG_TRRAV", -1);
						int LG_RRD = ParametroServices.getValorOfParametroAsInteger("LG_RRD", -1);
						
						String NM_SENHA_ADMINISTRATIVA = ParametroServices.getValorOfParametroAsString("NM_SENHA_ADMINISTRATIVA", "tivicait123");

						int CD_ORGAO_AUTUADOR = ParametroServices.getValorOfParametroAsInteger("CD_ORGAO_AUTUADOR", -1);
						int TP_CONVENIO_PADRAO = ParametroServices.getValorOfParametroAsInteger("TP_CONVENIO_PADRAO", -1);
						
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

						parametros.put("CD_MUNICIPIO_AUTUADOR",CD_MUNICIPIO_AUTUADOR);
						parametros.put("LG_BOAT",LG_BOAT);
						parametros.put("LG_TRRAV",LG_TRRAV);
						parametros.put("LG_RRD",LG_RRD);
						
						
						parametros.put("NR_TELEFONE",NR_TELEFONE);
						parametros.put("NM_EMAIL",NM_EMAIL);
						parametros.put("NM_LOGRADOURO",NM_LOGRADOURO);
						parametros.put("NR_ENDERECO",NR_ENDERECO);
						parametros.put("NM_BAIRRO",NM_BAIRRO);
						parametros.put("SG_DEPARTAMENTO",SG_DEPARTAMENTO);
						
						parametros.put("NM_SENHA_ADMINISTRATIVA", NM_SENHA_ADMINISTRATIVA);				

						parametros.put("CD_ORGAO_AUTUADOR",CD_ORGAO_AUTUADOR);								
						parametros.put("LG_BOAT",LG_BOAT);
						parametros.put("LG_TRRAV",LG_TRRAV);	
						parametros.put("LG_RRD",LG_RRD);
						parametros.put("TP_CONVENIO_PADRAO",TP_CONVENIO_PADRAO);
						
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
						if(usuario!=null && pessoa!=null){
							GregorianCalendar dtLog = new GregorianCalendar();
							String remoteIP = null;
							String txtExecucao = "Autenticação de acesso de agente as " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +
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
							String txtHistorico = "Equipamento retirado as " + Util.formatDateTime(dtHistorico, "dd/MM/yyyy HH:mm:ss", "") + " por " +
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
						
						
						return ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA") ? r : toStr(r);
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
			
			String sql = "update seg_usuario set st_login=?, cd_equipamento=? where cd_usuario = ?";
	
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
						
			String sql = "SELECT * FROM mob_agente WHERE cd_usuario=?";
								
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Agente(rs.getInt("cd_agente"),
						rs.getInt("cd_usuario"),
						rs.getString("nm_agente"),
						rs.getString("ds_endereco"),
						rs.getString("nm_bairro"),
						rs.getString("nr_cep"),
						rs.getInt("cd_cidade"),
						rs.getString("nr_matricula"),
						rs.getInt("tp_agente"));
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
	
	public static boolean isLoginAgenteTravado(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "select st_login from seg_usuario where cd_usuario = ?";
	
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdUsuario);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return (rsm.getInt("ST_LOGIN") == 1);
			
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
			
			String sql = "select cd_equipamento from seg_usuario where cd_usuario = ?";
	
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
	
	private static com.tivic.manager.str.Agente toStrAgente(com.tivic.manager.mob.Agente agente) {
		return new com.tivic.manager.str.Agente(
				agente.getCdAgente(), 
				agente.getNmAgente(), 
				agente.getDsEndereco(), 
				agente.getNmBairro(), 
				agente.getNrCep(), 
				agente.getCdCidade(), 
				agente.getNrMatricula(), 
				agente.getCdUsuario(), 
				agente.getTpAgente());
	}
	
	@SuppressWarnings("unchecked")
	private static Result toStr(Result in) {
		Result out = in;
		
		//talonario
		ArrayList<com.tivic.manager.str.Talonario> strTalonarios = new ArrayList<com.tivic.manager.str.Talonario>();
		for (Talonario talonario : ((ArrayList<Talonario>) in.getObjects().get("TALONARIOS"))) {
			strTalonarios.add(TalonarioServices.toStrTalonario(talonario));
		}		
		out.addObject("TALONARIOS", strTalonarios);
		
		//agente
		out.addObject("AGENTE", toStrAgente((Agente) in.getObjects().get("AGENTE")));
		
		//
		
		return out;
	}
	
	public static ResultSetMap getByMatricula(String nrMatricula) {
		return getByMatricula(nrMatricula, null);
	}
	
	public static ResultSetMap getByMatricula(String nrMatricula, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AGENTE WHERE NR_MATRICULA = ?");
			pstmt.setString(1, nrMatricula);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		} catch( Exception e ) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
