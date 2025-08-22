package com.tivic.manager.seg;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tivic.manager.acd.InstituicaoDAO;
import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.manager.acd.InstituicaoServices;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.EmpresaDAO;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.PessoaEmpresaDAO;
import com.tivic.manager.grl.PessoaEmpresaServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.log.ExecucaoAcao;
import com.tivic.manager.log.ExecucaoAcaoDAO;
import com.tivic.manager.log.LogServices;
import com.tivic.manager.log.Sistema;
import com.tivic.manager.log.SistemaDAO;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.srh.DadosFuncionaisServices;
import com.tivic.manager.str.Agente;
import com.tivic.manager.str.AgenteServices;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;
import com.tivic.sol.auth.jwt.JWT;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import flex.messaging.endpoints.AMFEndpoint;
import flex.messaging.endpoints.Endpoint;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.security.Action;
import sol.security.Group;
import sol.security.Module;
import sol.security.StatusPermissionActionUser;
import sol.security.User;
import sol.security.UserServices;
import sol.util.HashServices;
import sol.util.Result;
import sol.util.SessionCounter;

public class UsuarioServices implements UserServices {

	public static final int GERENTE          = 1;
	public static final int ATENDENTE        = 2;
	public static final int USUARIO_CONSULTA = 3;
	public static final int OPERADOR         = 4;

	public static final int ADMINISTRADOR = 0;
	public static final int USUARIO_COMUM = 1;
	public static final int CORRESPONDENTE = 2;
	public static final int CONSULTA = 3;
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static final int ERROR_MAX_USUARIOS_ATIVOS = -2;

	public static final String MASTER_LOGIN = "tivic";
	public static final String MASTER_PASSW = "CDF3DP!";
	
	public static final String[] tiposUsuario = {"Administrador", "Operador"};
	
	public static final int MODULO_SECRETARIA = 1;
	public static final int MODULO_PROFESSOR = 2;
	public static final int MODULO_ESCOLA = 3;
	public static final int MODULO_BIBLIOTECA = 4;
	public static final int MODULO_RESERVA = 5;
	public static final int MODULO_CAE = 6;
	public static final int MODULO_PED = 9;
	public static final int MODULO_FINANCEIRO_SECRETARIA= 11;
	public static final int MODULO_TRANSPORTE = 12;
	
	/*Erros no retorno da autenticacao*/
	public static final int ERR_DENIED_LOGIN_INVALIDO = -1;
	public static final int ERR_DENIED_SENHA_INVALIDA = -2;
	public static final int ERR_DENIED_USUARIO_MODULO_PRINCIPAL = -5;
	public static final int ERR_MODULO_INATIVO = -6;
	public static final int ERR_DENIED_USUARIO_MODULO = -7;
	public static final int ERR_EQUIPAMENTO_INATIVO = -9;
	public static final int ERR_EQUIPAMENTO_NAO_REGISTRADO = -10;
	public static final int ERR_USUARIO_OUTRO_EQUIPAMENTO = -11;
	public static final int ERR_USUARIO_INATIVO = -12;		
	public static final int ERR_USUARIO_AINDA_LOGADO = -13;		
	public static final int ERR_NAO_IDENTIFICADO = -99;
	public static final int ERR_USUARIO_NAO_AUTORIZADO = -100;
	
	public static final int SUCESSO = 1;
	
	/**
	 * WARNING não alterar, utilizado para criar o hash de senha
	 */
	private static final String SALT = "9afs1F4F494S9as9V9d8A4slçjfWASh2XAsFGJ9dqmcvxznjvoa9S1Z";

	/**
	 * @category SICOE
	 */
	public static final String[] tipoUsuario = {"Administrador", "Gerente", "Atendente", "Usuário Consulta"};
	
	{		
		getAll(1);
	}
	
	/**
	 * INICIALIZANDO OS USUARIOS
	 */
	public static Result init() {
		System.out.println("Inicializando usuários...");
		Connection connect = Conexao.conectar();
		try {
			ResultSet rs = connect.prepareStatement("SELECT * FROM seg_usuario "+
		                                            "WHERE nm_login = \'tivic\'").executeQuery();
			Usuario usuario = null;
			if(rs.next())	{
				usuario = UsuarioDAO.get(rs.getInt("cd_usuario"), connect);
				if(usuario.getStUsuario()==0){
					usuario.setStUsuario(1);
					UsuarioDAO.update(usuario, connect);
				}
			}
			else	{ // Criando
				usuario = new Usuario(0,0,0,"tivic","tivic",ADMINISTRADOR,"",1/*Ativo*/);
				UsuarioDAO.insert(usuario, connect);
			}
			// Verifica acesso ao módulo painel de controle
			Modulo modulo = ModuloServices.getModuloById("seg");
			if(modulo != null)	{
				rs = connect.prepareStatement("SELECT * FROM seg_usuario_modulo WHERE cd_modulo = "+modulo.getCdModulo()+" AND cd_usuario = "+usuario.getCdUsuario()).executeQuery();
				if(!rs.next())
					addPermissaoModulo(usuario.getCdUsuario(), modulo.getCdModulo(), modulo.getCdSistema(), 1, connect);
					
			}
			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar verificar usuário padrão", e);
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Result save(Usuario usuario){
		return save(usuario, null);
	}
	
	public static Result save(Usuario usuario, Connection connect){
		return save(usuario, true, connect);
	}
	
	public static Result save(Usuario usuario, boolean checkOldBase, Connection connect){
		boolean isConnectionNull = connect==null;
		//TODO Remover teste de loginHash após atualização do banco
		int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(usuario==null)
				return new Result(-1, "Erro ao salvar. usuário é nulo");
			
			
			String strBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA");

	  boolean lgBaseAntiga = checkOldBase && strBaseAntiga != null ? strBaseAntiga.equals("1") : false;
			
			int retorno;
			if(usuario.getCdUsuario()==0){
				
				String sql = "SELECT * FROM seg_usuario " +
						   "WHERE nm_login=? and st_usuario=?";
			
				if(lgBaseAntiga) {
					sql = "SELECT * FROM usuario " +
							   "WHERE nm_nick=? and st_usuario=?";
				}
				
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setString(1, usuario.getNmLogin());
				pstmt.setInt(2, ST_ATIVO);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())
					return new Result(-2, "Já existe um usuário com o login escolhido.");
				
				if( lgLoginHash > 0 )
					usuario.setNmSenha( getPasswordHash( usuario.getNmSenha() ) );
				
				if(lgBaseAntiga)
					retorno = insertBaseAntiga(usuario, connect);
				else
					retorno = UsuarioDAO.insert(usuario, connect);
				
				usuario.setCdUsuario(retorno);
			}
			else {
				
				String sql = "SELECT * FROM seg_usuario " +
						   "WHERE nm_login=? and st_usuario=? and cd_usuario<>?";
			
				if(lgBaseAntiga) {
					sql = "SELECT * FROM usuario " +
							   "WHERE nm_nick=? and st_usuario=? and cod_usuario<>?";
				}
				
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setString(1, usuario.getNmLogin());
				pstmt.setInt(2, ST_ATIVO);
				pstmt.setInt(3, usuario.getCdUsuario());
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())
					return new Result(-2, "Já existe um usuário ATIVO este login.");
				
				
				if(lgLoginHash > 0 && isChangePassword(usuario, connect) )
					usuario.setNmSenha( getPasswordHash( usuario.getNmSenha() ) );

				if(lgBaseAntiga)
					retorno = updateBaseAntiga(usuario, connect);
				else
					retorno = UsuarioDAO.update(usuario, connect);
				
				
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "USUARIO", usuario);
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
	
	
	public static int insertBaseAntiga(Usuario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("seg_usuario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdUsuario()<=0)
				objeto.setCdUsuario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO usuario (cod_usuario,"+
			                                  "nm_nick,"+
			                                  "nm_usuario,"+
			                                  "nr_nivel,"+
			                                  "nm_senha,"+
			                                  "st_usuario) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdUsuario());
			pstmt.setString(2, objeto.getNmLogin());
			pstmt.setString(3, objeto.getNmRespostaSecreta()); //ponga neste campo para nome do usuario
			pstmt.setInt(4, objeto.getTpUsuario());
			pstmt.setString(5, objeto.getNmSenha());
			pstmt.setInt(6, objeto.getStUsuario());
			pstmt.executeUpdate();
			return objeto.getCdUsuario();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.insertBaseAntiga: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int updateBaseAntiga(Usuario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE usuario SET cod_usuario=?,"+
												      		   "nm_nick=?,"+
												      		   "nm_usuario=?,"+
												      		   "nr_nivel=?,"+
												      		   "nm_senha=?,"+
												      		   "st_usuario=? WHERE cod_usuario=?");
			pstmt.setInt(1, objeto.getCdUsuario());
			pstmt.setString(2, objeto.getNmLogin());
			pstmt.setString(3, objeto.getNmRespostaSecreta()); //ponga neste campo para nome do usuario
			pstmt.setInt(4, objeto.getTpUsuario());
			pstmt.setString(5, objeto.getNmSenha());
			pstmt.setInt(6, objeto.getStUsuario());
			pstmt.setInt(7, objeto.getCdUsuario());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.updateBaseAntiga: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static Boolean isChangePassword(Usuario usuario, Connection connect ){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Boolean change = false;
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_usuario " +
					"WHERE cd_usuario = ? ");
			pstmt.setInt(1, usuario.getCdUsuario());
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				if( !usuario.getNmSenha().equals( rsm.getString("NM_SENHA") ) ){
					change = true;
				}
				
			}
			
			return change;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdUsuario){
		int retorno = delete(cdUsuario);
		return new Result(retorno, retorno > 0 ? "excluído com sucesso" : "Erro ao excluir");
	}
	
	
	
	/**
	 * @category SICOE
	 */
	public static ResultSetMap getAtividadeOfUsuario(int cdUsuario) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_atividade, B.id_atividade, C.nm_cadastro, C.id_cadastro " +
					                         "FROM seg_usuario_atividade A, seg_atividade B, seg_cadastro C "+
		                                     "WHERE A.cd_atividade = B.cd_atividade "+
		                                     "  AND A.cd_cadastro  = B.cd_cadastro "+
		                                     "  AND A.cd_sistema   = B.cd_sistema " +
		                                     "  AND A.cd_sistema   = C.cd_sistema " +
		                                     "  AND A.cd_cadastro  = C.cd_cadastro " +
		                                     "  AND A.cd_usuario   = "+cdUsuario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	/**
	 * @category SICOE
	 */
	public static ResultSetMap getUsuariosOnLine() {
		try {
			ResultSetMap rsm = new ResultSetMap();
			ArrayList<?> sessoes = SessionCounter.getSessions();
			for(int i=0; i<sessoes.size(); i++)	{
				Usuario usuario = (Usuario)((HttpSession)sessoes.get(i)).getAttribute("usuario");
				if(usuario!=null)	{
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.cd_usuario", String.valueOf(usuario.getCdUsuario()), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmUsuario = find(criterios);
					if(rsmUsuario.next())	{
						HashMap<String,Object> register = rsmUsuario.getRegister();
						register.put("ID_SESSAO", ((HttpSession)sessoes.get(i)).getId());
						register.put("CL_TIPO", tipoUsuario[usuario.getTpUsuario()]);
						rsm.addRegister(register);
					}
				}
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getUsuariosOnLine: " + e);
			return null;
		}
	}
	/**
	 * @category SICOE
	 */
	public static int shutdownUser(String id) {
		try {
			HttpSession sessao = SessionCounter.getSessionById(id);
			if(sessao!=null)	{
				sessao.invalidate();
				return 1;
			}
			else
				return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.shutdownUser: " + e);
			return -1;
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		if(connect == null){
			connect = Conexao.conectar();
		}
		
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			criterios.add(new ItemComparator("A.st_usuario", Integer.toString(UsuarioServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			String nmPessoa = "";
			int limit = -1;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("B.NM_PESSOA") && !lgBaseAntiga) {
					nmPessoa =	Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("limit") && !lgBaseAntiga) {
					limit = Integer.parseInt(criterios.get(i).getValue().toString());
					criterios.remove(i);
					i--;
				}
			}
			String sql = "SELECT A.cd_usuario, A.cd_pessoa, A.cd_pergunta_secreta, A.nm_login, "+
					" A.tp_usuario, A.nm_resposta_secreta, A.st_usuario, A.nm_login_im, A.nm_senha_im, A.nm_apelido_im, A.st_login, A.cd_equipamento, "+
					"B.nm_pessoa, B.nm_pessoa AS nm_usuario, C.* " +
					"FROM seg_usuario A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
					"LEFT OUTER JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento) " +
					"WHERE 1=1 "+ 
					(!nmPessoa.equals("") ?	
					"AND TRANSLATE (B.nm_pessoa, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmPessoa)+"%' "
					: "");
		
			if(lgBaseAntiga) {
			
				for (ItemComparator itemComparator : criterios) {
					if(itemComparator.getColumn().equalsIgnoreCase("A.NM_LOGIN"))
						itemComparator.setColumn("A.NM_NICK");
					
					if(itemComparator.getColumn().equalsIgnoreCase("B.NM_PESSOA"))
						itemComparator.setColumn("A.NM_USUARIO");
					
					if(itemComparator.getColumn().equalsIgnoreCase("A.TP_USUARIO"))
						itemComparator.setColumn("A.NR_NIVEL");
					
				}
				
				sql = "SELECT A.*, A.cod_usuario as cd_usuario, A.nm_usuario as nm_pessoa, A.nr_nivel as tp_usuario, A.nm_nick as nm_login, B.*  " +
						"FROM usuario A "+
						"LEFT OUTER JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) ";
			}
			
			String strLimit = (limit > 0 ? " LIMIT "+limit : "");
			
			return Search.find(sql, "ORDER BY nm_usuario "+strLimit, criterios, Conexao.conectar(), true);
		}
		catch(Exception e) {
			Util.registerLog(e);
			return null;
		}
	}

	public static ResultSetMap findWithEmpresa(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.NM_PESSOA AS NM_USUARIO, D.NM_EMPRESA "+
		                   "FROM SEG_USUARIO A, GRL_PESSOA B, SEG_USUARIO_EMPRESA C, GRL_EMPRESA D "+
		                   "WHERE A.CD_USUARIO = B.CD_PESSOA "+
		                   "  AND A.CD_USUARIO = C.CD_USUARIO "+
		                   "  AND C.CD_EMPRESA = D.CD_EMPRESA",
		                   "ORDER BY nm_empresa, nm_pessoa", criterios, Conexao.conectar(), true);
	}
	/**
	 * @category Manager
	 */
	public static int insert(Usuario objeto, String nmUsuario) {
		return insert(objeto, new PessoaFisica(objeto.getCdPessoa()>0 ? objeto.getCdPessoa() : 0,
				0, //cdPessoaSuperior
				0, //cdPais
				nmUsuario,
				"", //nrTelefone1
				"", //nrTelefone2
				"", //nrCelular
				"", //nrFax
				"", //nmEmail
				new GregorianCalendar(), //dtCadastro
				PessoaServices.TP_FISICA, //gnPessoa
				null, //imgFoto
				1, //stCadastro
				"", //nmUrl
				"", //nmApelido
				"", //txtObservacao
				0, //lgNotificacao
				"", //idPessoa
				0, //cdClassificacao
				0, //cdFormaDivulgacao
				null, //dtChegadaPais
				0, //cdCidade
				0, //cdEscolaridade
				null, //dtNascimento
				"", //nrCpf
				"", //sgOrgaoRg
				"", //nmMae
				"", //nmPai
				0, //tpSexo
				0, //stEstadoCivil
				"", //nrRg
				"", //nrCnh
				null, //dtValidadeCnh
				null, //dtPrimeiraHabilitacao
				0, //tpCategoriaHabilitacao
				0, //tpRaca
				0, //lgDeficienteFisico
				"", //nmFormaTratamento
				0, //cdEstadoRg
				null,//dtEmissaoRg
				null,//blbFingerPrint
				0 /*cdConjuge*/, 0 /*qtMembrosFamilia*/, 0 /*vlRendaFamiliarPerCapta*/, 0 /*tpNacionalidade*/,0 /*tpFiliacaoPai*/), 
					null);
	}

	public static int insert(Usuario objeto, PessoaFisica pessoa) {
		return insert(objeto, pessoa, null);
	}

	public static int insert(Usuario objeto, PessoaFisica pessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			int cdPessoa = 0;
			// Salva usuário mesmo que não consiga salvar pessoa
			try	{
				cdPessoa = objeto.getCdPessoa();
				if (cdPessoa<=0 && (cdPessoa = PessoaFisicaDAO.insert(pessoa, connect)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					// return -1;
				}
				else if (PessoaFisicaDAO.update(pessoa, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					// return -1;
				}
				objeto.setCdPessoa(cdPessoa);
			}
			catch(Exception e){

			}
			// Ajuda a ignorar informações de vínculo
			try	{
				int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0, 0, connect);
				if (cdVinculo>0 && cdPessoa>0) {
					if (objeto.getStUsuario()==ST_ATIVO) {
						PreparedStatement pstmt = connect.prepareStatement("SELECT cd_empresa " +
								"FROM grl_empresa A " +
								"WHERE NOT A.cd_empresa IN (SELECT cd_empresa " +
								"							FROM grl_pessoa_empresa " +
								"							WHERE cd_pessoa = ? " +
								"							  AND cd_vinculo = ?)");
						pstmt.setInt(1, cdPessoa);
						pstmt.setInt(2, cdVinculo);
						ResultSet rs = pstmt.executeQuery();
						while (rs.next())
							if (PessoaEmpresaDAO.insert(new PessoaEmpresa(rs.getInt("cd_empresa"),
									cdPessoa,
									cdVinculo,
									null /*dtVinculo*/,
									PessoaEmpresaServices.ST_ATIVO), connect) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connect);
								return -1;
							}
					}
					else {
						PreparedStatement pstmt = connect.prepareStatement("DELETE " +
								"FROM grl_pessoa_empresa " +
								"WHERE cd_pessoa = ? " +
								"  AND cd_vinculo = ?");
						pstmt.setInt(1, cdPessoa);
						pstmt.setInt(2, cdVinculo);
						pstmt.execute();
					}
				}
			}
			catch(Exception e){};
			//TODO Remover teste de loginHash após atualização do banco
			int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
			if( lgLoginHash > 0 )
				objeto.setNmSenha( getPasswordHash( objeto.getNmSenha() ) );
			int code = UsuarioDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.desconectar(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return code;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Usuario objeto, String nmUsuario) {
		return update(objeto, nmUsuario, null);
	}

	public static int update(Usuario objeto, String nmUsuario, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PessoaFisica pessoa = objeto.getCdPessoa()<=0 || PessoaFisicaDAO.get(objeto.getCdPessoa(), connection) == null ? new PessoaFisica(0,
					0, //cdPessoaSuperior
					0, //cdPais
					nmUsuario,
					"", //nrTelefone1
					"", //nrTelefone2
					"", //nrCelular
					"", //nrFax
					"", //nmEmail
					new GregorianCalendar(), //dtCadastro
					PessoaServices.TP_FISICA, //gnPessoa
					null, //imgFoto
					1, //stCadastro
					"", //nmUrl
					"", //nmApelido
					"", //txtObservacao
					0, //lgNotificacao
					"", //idPessoa
					0, //cdClassificacao
					0, //cdFormaDivulgacao
					null, //dtChegadaPais
					0, //cdCidade
					0, //cdEscolaridade
					null, //dtNascimento
					"", //nrCpf
					"", //sgOrgaoRg
					"", //nmMae
					"", //nmPai
					0, //tpSexo
					0, //stEstadoCivil
					"", //nrRg
					"", //nrCnh
					null, //dtValidadeCnh
					null, //dtPrimeiraHabilitacao
					0, //tpCategoriaHabilitacao
					0, //tpRaca
					0, //lgDeficienteFisico
					"", //nmFormaTratamento
					0, //cdEstadoRg
					null,//dtEmissaoRg
					null,//blbFingerPrint
					0 /*cdConjuge*/, 0 /*qtMembrosFamilia*/, 0 /*vlRendaFamiliarPerCapta*/, 0 /*tpNacionalidade*/,0 /*tpFiliacaoPai*/) : 
					PessoaFisicaDAO.get(objeto.getCdPessoa(), connection);
			pessoa.setNmPessoa(nmUsuario);
			if (update(objeto, pessoa, connection) <= 0) {
				if (isConnectionNull) {
					Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(Usuario objeto, PessoaFisica pessoa) {
		return update(objeto, pessoa, null);
	}

	public static int update(Usuario objeto, PessoaFisica pessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (pessoa.getCdPessoa()==0) {
				int cdPessoa = 0;
				if ((cdPessoa = PessoaFisicaDAO.insert(pessoa, connect)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
				else {
					pessoa.setCdPessoa(cdPessoa);
					objeto.setCdPessoa(cdPessoa);
				}
			}
			else if (PessoaFisicaDAO.update(pessoa, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_COLABORADOR", 0, 0, connect);
			if (cdVinculo>0 && objeto.getCdPessoa()>0) {
				if (objeto.getStUsuario()==ST_ATIVO) {
					PreparedStatement pstmt = connect.prepareStatement("SELECT cd_empresa " +
							"FROM grl_empresa A " +
							"WHERE NOT A.cd_empresa IN (SELECT cd_empresa " +
							"							FROM grl_pessoa_empresa " +
							"							WHERE cd_pessoa = ? " +
							"							  AND cd_vinculo = ?)");
					pstmt.setInt(1, objeto.getCdPessoa());
					pstmt.setInt(2, cdVinculo);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
						if (PessoaEmpresaDAO.insert(new PessoaEmpresa(rs.getInt("cd_empresa"),
								objeto.getCdPessoa(),
								cdVinculo,
								null /*dtVinculo*/,
								PessoaEmpresaServices.ST_ATIVO), connect) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connect);
							return -1;
						}
				}
				else {
					PreparedStatement pstmt = connect.prepareStatement("DELETE " +
							"FROM grl_pessoa_empresa " +
							"WHERE cd_pessoa = ? " +
							"  AND cd_vinculo = ?");
					pstmt.setInt(1, objeto.getCdPessoa());
					pstmt.setInt(2, cdVinculo);
					pstmt.execute();
				}
			}
			
			//TODO Remover teste de loginHash após atualização do banco
			int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
			if( lgLoginHash > 0 && isChangePassword(objeto, connect) )
				objeto.setNmSenha( getPasswordHash( objeto.getNmSenha() ) );
			if (UsuarioDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public boolean isLoginUsed(String nmLogin, int cdUsuario) {
		return isLoginUsed(nmLogin, cdUsuario, null);
	}

	public static boolean isLoginUsed(String nmLogin, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_USUARIO " +
															   "WHERE UPPER(NM_LOGIN) = ? " +
															   (cdUsuario!=0 ? " AND CD_USUARIO <> ? " : ""));
			pstmt.setString(1, nmLogin==null ? "" : nmLogin.toUpperCase());
			if (cdUsuario!=0)
				pstmt.setInt(2, cdUsuario);
			return pstmt.executeQuery().next();
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.isLoginUsed: " + e);
			return true;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static boolean isExistsUsuario() {
		return isExistsUsuario(null);
	}

	public static boolean isExistsUsuario(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_USUARIO");
			return pstmt.executeQuery().next();
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.isExistsUsuario: " + e);
			return true;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int insertUsuarioGrupo(int cdUsuario, int cdGrupo) {
		return insertUsuarioGrupo(cdUsuario, cdGrupo, null);
	}

	public static int insertUsuarioGrupo(int cdUsuario, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_GRUPO (CD_USUARIO,"+
			                                  "CD_GRUPO) VALUES (?, ?)");
			pstmt.setInt(1,cdUsuario);
			pstmt.setInt(2,cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.insertUsuarioGrupo: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllUsuarioGrupoByUsuario(int cdUsuario) {
		return getAllUsuarioGrupoByUsuario(cdUsuario, null);
	}

	public static ResultSetMap getAllUsuarioGrupoByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM SEG_USUARIO_GRUPO A, SEG_GRUPO B  "+
											 " WHERE A.CD_GRUPO = B.CD_GRUPO "+
											 "   AND CD_USUARIO = ?");
			pstmt.setInt(1,cdUsuario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.getAllUsuarioGrupoByUsuario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteUsuarioGrupo(int cdUsuario, int cdGrupo) {
		return deleteUsuarioGrupo(cdUsuario, cdGrupo, null);
	}

	public static int deleteUsuarioGrupo(int cdUsuario, int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SEG_USUARIO_GRUPO WHERE CD_USUARIO=? AND CD_GRUPO=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.deleteUsuarioGrupo: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha, boolean returnInfo, boolean returnInfoAgenda, int cdModulo) {
		return loginAndUpdateSession(session, nmLogin, nmSenha, returnInfo, returnInfoAgenda, null, cdModulo, false, 0, null);
	}

	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha) {
		return loginAndUpdateSession(session, nmLogin, nmSenha, false, false, null, 0, false, 0, null);
	}

	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha, boolean returnInfo) {
		return loginAndUpdateSession(session, nmLogin, nmSenha, returnInfo, false, null, 0, false, 0, null);
	}

	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha, boolean returnInfo, boolean returnInfoAgenda) {
		return loginAndUpdateSession(session, nmLogin, nmSenha, returnInfo, returnInfoAgenda, null, 0, false, 0, null);
	}

	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha, boolean returnInfo, boolean returnInfoAgenda, String remoteIP) {
		return loginAndUpdateSession(session, nmLogin, nmSenha, returnInfo, returnInfoAgenda, remoteIP, 0, false, 0, null);
	}
	
	public static Result loginAndUpdateSession(String nmLogin, String nmSenha, int cdModulo, boolean lgSupervisao, int cdEmpresa) {
		return loginAndUpdateSession(null, nmLogin, nmSenha, true, false, null, cdModulo, lgSupervisao, cdEmpresa, null);
	}
	
	public static Result loginAndUpdateSession(HttpSession session, String nmLogin, String nmSenha, boolean returnInfo, boolean returnInfoAgenda, String remoteIP, int cdModulo, boolean lgSupervisao, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			// remove atributos da sessao
			if(session != null){
				session.removeAttribute("usuario");
				session.removeAttribute("user");
				session.removeAttribute("objectsSystem");
				session.removeAttribute("permissionsActionsUser");
			}
			
			int cdUsuario = login(nmLogin, nmSenha, connection);
			
			
			/*
			 * linha previamente comentada, porem foi reativada pois
			 * quando um usuário e senha eram digitados errados, não
			 * retornava mensagem.
			 */
			if (cdUsuario<=0)
				return new Result(-1, "Login ou senha incorreta!");
			
			Result result = new Result(cdUsuario);
			if(cdUsuario > 0){
				// Verifica acesso do usuário ao módulo
				if(cdModulo > 0)	{
					ResultSet rs = connection.prepareStatement("SELECT * FROM seg_usuario_modulo WHERE cd_modulo = "+cdModulo+" AND cd_usuario = "+cdUsuario).executeQuery();
					if(!rs.next()){
						return new Result(-1, "O usuário não tem acesso a esse módulo!");
					}
				}
				if(lgSupervisao){
					int cdGrupoSupervisor = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_USUARIO_SUPERVISOR", 0, cdEmpresa);
					ResultSetMap rsmUsuarios = GrupoServices.getUsuariosOfGrupo(cdGrupoSupervisor);
					while(rsmUsuarios.next()){
						if(cdUsuario == rsmUsuarios.getInt("cd_usuario"))
							return new Result(1, "Permissão concedida");
					}
					return new Result(-1, "Permissão negada");
				}
				// Instanciando usuário
				Usuario usuario = UsuarioDAO.get(cdUsuario, connection);
				if(usuario!=null)
					usuario.setNmSenha("");
				// usuário agenda
				com.tivic.manager.agd.Usuario usuarioAgd = returnInfoAgenda ? com.tivic.manager.agd.UsuarioDAO.get(cdUsuario, connection) : null;
				usuario.setNmSenha("");
				if (usuarioAgd!=null)
					usuarioAgd.setNmSenha("");
				// Instanciando outras variáveis
				PessoaFisica pessoaFisica  = usuario==null || usuario.getCdPessoa()==0 ? null : PessoaFisicaDAO.get(usuario.getCdPessoa(), connection);
				Pessoa pessoa              = pessoaFisica!=null ? pessoaFisica : PessoaDAO.get(usuario.getCdPessoa(), connection);
				String nmPessoa            = pessoa==null ? null : pessoa.getNmPessoa();
				sol.security.System system = SistemaServices.findSystemBySg("dotMng", connection);
				User user                  = new User(usuario.getCdUsuario(), nmPessoa, nmLogin, null);
				Object[] objects           = ObjetoServices.getObjectBySigleSystem("dotMng", connection);
				// Permissões
				StatusPermissionActionUser[] permissions = AcaoServices.getStatusPermissionActionsOfUser(cdUsuario, connection);
				GregorianCalendar dtLog = new GregorianCalendar();
				for (int i=0; permissions!=null && i<permissions.length; i++) {
					StatusPermissionActionUser permission = permissions[i];
					Action action = permission.getAction();
					String nmAction = action==null ? null : action.getName();
					if (nmAction!=null && nmAction.equals("com.tivic.manager.seg.UsuarioServices.loginAndUpdateSession")) {
						Module module = action==null ? null : action.getModule();
						int cdAcao    = action.getId();
						int cdModuloTmp  = module==null ? 0 : module.getId();
						int cdSistema = system==null ? 0 : system.getId();
						String txtExecucao = "Login às " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +nmPessoa + ", através da conta " + usuario.getNmLogin() +
						((remoteIP!=null)?", IP "+remoteIP:"");
						ExecucaoAcao log = new ExecucaoAcao(0/*cdLog*/, cdAcao, cdModuloTmp, cdSistema, cdUsuario, dtLog, txtExecucao, "1"/*txtResultadoExecucao*/, 1/*tpResultadoExecucao*/);
						ExecucaoAcaoDAO.insert(log, connection);
						break;
					}
				}
				// Atribui os valores para a sessão
				if(session != null){
					session.setAttribute("usuario", usuario);
					session.setAttribute("user", user);
					session.setAttribute("objectsSystem", objects);
					session.setAttribute("system", system);
					session.setAttribute("pessoa", pessoaFisica!=null ? pessoaFisica : pessoa);
					session.setAttribute("permissionsActionsUser", permissions);
				}
				// Parametros definidos para a sessao
				int vlTempoMaxSessao = ParametroServices.getValorOfParametroAsInteger("TEMPO_MAXIMO_SESSAO", 30, 0, connection);
				session.setMaxInactiveInterval(vlTempoMaxSessao*60);//em segundos
	
				
				if (returnInfo) {
					result.addObject("usuario", usuario);
					if (returnInfoAgenda)
						result.addObject("usuario_agd", usuarioAgd);
					result.addObject("nmUsuario", pessoa==null ? "" : pessoa.getNmPessoa());
					result.addObject("hrAccess", dtLog);
				}
				//
				if(cdModulo > 0)	{
					ResultSetMap rsmEmpresas = getEmpresaOfUsuarioModulo(cdUsuario, 1/*cdSistema*/, cdModulo, false, connection);
					session.setAttribute("rsmEmpresas", rsmEmpresas);
					result.addObject("rsmEmpresas", rsmEmpresas);
				}
			}
			
			if(isConnectionNull){
				connection.commit();
			}
			
			return result;
		}
		catch(Exception e) {
			if(isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar efetuar login!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int login(String nmLogin, String nmSenha) {
		return autenticar(nmLogin, nmSenha, null, null, null).getCode();
	}
	public static int login(String nmLogin, String nmSenha, Connection connect)	{
		return autenticar(nmLogin, nmSenha, null, null, connect).getCode();
	}
	
	public static Result autenticar(String nmLogin, String nmSenha) {
		return autenticar(nmLogin, nmSenha, null, null, null);
	}
	
	public static Result autenticar(String nmLogin, String nmSenha, String idModulo) {
		return autenticar(nmLogin, nmSenha, idModulo, null, null);
	}
	
	public static Result autenticar(String nmLogin, String nmSenha, String idModulo, String idEquipamento) {
		return autenticar(nmLogin, nmSenha, idModulo, idEquipamento, null);
	}

	public static Result autenticar(String nmLogin, String nmSenha, String idModulo, String idEquipamento, Connection connect)	{
		return autenticar(nmLogin, nmSenha, idModulo, idEquipamento, 0, connect);
	}
	
	public static Result autenticar(String nmLogin, String nmSenha, String idModulo, String idEquipamento, int tpModulo) {
		return autenticar(nmLogin, nmSenha, idModulo, idEquipamento, tpModulo, null);
	}

	public static Result autenticar(String nmLogin, String nmSenha, String idModulo, String idEquipamento, int tpModulo, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
									
			Boolean lgBaseAntiga = false;
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA") != null)
				lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");

			// TODO Alterar após completar migração do banco antigo para o novo
			if(lgBaseAntiga)
				return autenticarBaseAntiga(nmLogin, nmSenha, connect);
				
			
			//TODO Remover teste de loginHash após atualização do banco
			int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
			sol.util.ConfManager conf = Util.getConfManager();
	    	String sqlUsuario = "SELECT * FROM seg_usuario " +
							    "WHERE nm_login	 = ? " +
							    "  AND st_usuario = 1";
	    	String passUppercase = conf.getProps().getProperty("PASS_UPPERCASE");
	    	if(passUppercase!=null && passUppercase.equals("1")) {
	    		nmLogin = nmLogin.toUpperCase();
	    		nmSenha = nmSenha.toUpperCase();
	    		//
	    		sqlUsuario = "SELECT * FROM seg_usuario " +
					         "WHERE UPPER(nm_login) = ? " +
					         "  AND st_usuario      = 1";	    	
	    	}
	    	//TODO Remover teste de loginHash após atualização do banco
	    	if( lgLoginHash > 0 )
	    		nmSenha = getPasswordHash( nmSenha);
			PreparedStatement pstmt = connect.prepareStatement(sqlUsuario);
			pstmt.setString(1, nmLogin);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				if(rs.getInt("tp_usuario") == CORRESPONDENTE) {
					return new Result(ERR_DENIED_USUARIO_MODULO_PRINCIPAL, "Usuario Correspondente nao tem acesso ao modulo principal.", "httpStatusCode", HttpURLConnection.HTTP_FORBIDDEN);
				}
				
				Usuario usuario = new Usuario(rs.getInt("cd_usuario"), rs.getInt("cd_pessoa"), rs.getInt("cd_pergunta_secreta"),rs.getString("nm_login"),
	                      rs.getString("nm_senha"), rs.getInt("tp_usuario"), rs.getString("nm_resposta_secreta"), rs.getInt("st_usuario"));
				
				Modulo modulo = ModuloServices.getModuloById(idModulo, connect);
								
				// Verifica acesso do usuário ao módulo
				if(modulo!=null) {
					if(modulo.getLgAtivo()!=1)
						return new Result(ERR_MODULO_INATIVO, "O modulo "+ modulo.getNmModulo() +" esta inativo!", "httpStatusCode", HttpURLConnection.HTTP_FORBIDDEN);
					
					if(!connect.prepareStatement("SELECT * FROM seg_usuario_modulo WHERE cd_modulo = "+modulo.getCdModulo()+" AND cd_sistema = "+modulo.getCdSistema()+" AND cd_usuario = "+usuario.getCdUsuario()).executeQuery().next()){
						return new Result(ERR_DENIED_USUARIO_MODULO, "O usuario nao tem acesso a esse modulo!", "httpStatusCode", HttpURLConnection.HTTP_FORBIDDEN);
					}
				}
				//else 
				//	return new Result(-8, "O módulo não existe no sistema!");

				
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
				
				if(nmSenha.equals(usuario.getNmSenha()) || (passUppercase!=null && passUppercase.equals("1") && nmSenha.equalsIgnoreCase(usuario.getNmSenha()))){
					Result r = new Result(usuario.getCdUsuario(), "Autenticado com sucesso...", "httpStatusCode", HttpURLConnection.HTTP_OK);
					
					Empresa empresa = EmpresaServices.getDefaultEmpresa(connect);
					
					if(modulo!=null)	{
						
						ResultSetMap rsmEmpresas = getEmpresaOfUsuarioModulo(usuario.getCdUsuario(), modulo.getCdSistema(), modulo.getCdModulo(), false, empresa.getCdEmpresa(), idModulo, tpModulo, connect);
						r.addObject("EMPRESAS", rsmEmpresas);
						
						if(rsmEmpresas.next())
							empresa = EmpresaDAO.get(rsmEmpresas.getInt("CD_EMPRESA"), connect);
					}
					else 
						r.addObject("EMPRESAS", new ResultSetMap());
					
					
					r.addObject("USUARIO", usuario);
					r.addObject("PESSOA", pessoa);
					r.addObject("EMPRESA", empresa);
					r.addObject("SUPORTE", PessoaDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_PESSOA_SUPORTE", 0), connect));
					r.addObject("DESENVOLVEDOR", PessoaDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_DESENVOLVEDOR", 0), connect));
					ResultSetMap rsmSetores = new ResultSetMap();
										
					if(empresa!=null)
						rsmSetores = DadosFuncionaisServices.getSetorOf(empresa!=null?empresa.getCdEmpresa():0, pessoa!=null?pessoa.getCdPessoa():0, connect);
					r.addObject("SETORES", rsmSetores);
					
					//EDF
					int cdPessoaPrefeituraMunicipal = ParametroServices.getValorOfParametroAsInteger("CD_PESSOA_PREFEITURA_MUNICIPAL", 0, 0, connect);
					if(cdPessoaPrefeituraMunicipal>0) {
						Pessoa prefeitura = PessoaDAO.get(cdPessoaPrefeituraMunicipal, connect);
						r.addObject("PREFEITURA", prefeitura);
					}
					
					int cdInstituicaoSecretariaMunicipal = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0, 0, connect);
					if(cdInstituicaoSecretariaMunicipal>0) {
						r.addObject("SECRETARIA", InstituicaoDAO.get(cdInstituicaoSecretariaMunicipal, connect));
					}
					
					/* REGISTRAR LOG */
					if(usuario!=null && pessoa!=null){
						GregorianCalendar dtLog = new GregorianCalendar();
						String remoteIP = null;
						String txtExecucao = "Autenticação de acesso às " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +
							pessoa.getNmPessoa() + ", através da conta " + usuario.getNmLogin() +
							((remoteIP!=null)?", IP "+remoteIP:"");
						
						Sistema log = new Sistema(0, //cdLog
								dtLog, //dtLog
								txtExecucao, //txtLog
								com.tivic.manager.log.SistemaServices.TP_LOGIN, //tpLog
								usuario.getCdUsuario());//cdUsuario
						SistemaDAO.insert(log, connect);
					}
					
					/*
					 * EQUIPAMENTO 
					 */
					// 007					
					Equipamento equipamento = null;
					if(idEquipamento!=null && nmSenha.equals(usuario.getNmSenha())) {
						//verificar equipamento
						pstmt = connect.prepareStatement("SELECT cd_equipamento FROM grl_equipamento WHERE id_equipamento = ?");
						pstmt.setString(1, idEquipamento);
						ResultSet rsEquipamento = pstmt.executeQuery();
				
						
						if(rsEquipamento.next()) {
							equipamento = EquipamentoDAO.get(rsEquipamento.getInt("cd_equipamento"), connect);
							
							if(equipamento.getStEquipamento()==EquipamentoServices.INATIVO) {
								rsEquipamento.close();
								return new Result(ERR_EQUIPAMENTO_INATIVO, "Este equipamento esta aguardando a ativacao. Em breve um de nossos operadores ira desbloquea-lo.");
							}
						}
						else {
							rs.close();
							return new Result(ERR_EQUIPAMENTO_NAO_REGISTRADO, "Equipamento nao registrado.");
						}
						
						r.addObject("EQUIPAMENTO", equipamento);
					}
					
					rs.close();
					
					/*
					 * NETWORK
					 */
					Endpoint clientEndpoint = FlexContext.getEndpoint();
					if(clientEndpoint instanceof AMFEndpoint){
						HashMap<String, Object> regNetwork = new HashMap<>();
						
						HttpServletRequest request = FlexContext.getHttpRequest();
						
						regNetwork.put("IP", (request != null) ? request.getRemoteAddr() : "0.0.0.0");
						
						r.addObject("NETWORK", regNetwork);
					}
					
					/*
					 * SERVER TIME
					 */
					r.addObject("SERVERTIME", new GregorianCalendar());
					
					/*
					 * Authorization Token
					 */
					HashMap<String, Object> headers = new HashMap<String, Object>();
					HashMap<String, Object> payload = new HashMap<String, Object>();
					GregorianCalendar issue = new GregorianCalendar();
					issue.add(Calendar.DAY_OF_MONTH, 7);
					
					headers.put("exp", issue);
					headers.put("sub", usuario.getNmLogin());

					payload.put("id", usuario.getCdUsuario());
					payload.put("login", usuario.getNmLogin());
					JWT jwt = new JWT();
					String authorization = jwt.generate(headers, payload);
					usuario.setToken(authorization);
					r.addObject("AUTHORIZATION", authorization);
					
					return r;
				}
				else {
					
					rs.close();
					return new Result(ERR_DENIED_SENHA_INVALIDA, "Senha invalida...", "httpStatusCode", HttpURLConnection.HTTP_UNAUTHORIZED);
				}
			
			}
			else {
				if(UsuarioDAO.getAll().size() == 0){
					//TODO Remover teste de loginHash após atualização do banco
					if(nmLogin.equals(MASTER_LOGIN) && nmSenha.equals( (lgLoginHash>0?getPasswordHash( MASTER_PASSW ):MASTER_PASSW) )){
						Pessoa pessoa = new Pessoa();
						pessoa.setNmPessoa("SUPORTE TIVIC");
						int retorno = PessoaDAO.insert(pessoa, connect);
						if(retorno > 0){
							Result result = save(new Usuario(0, retorno, 0, MASTER_LOGIN, MASTER_PASSW, UsuarioServices.ADMINISTRADOR, null, UsuarioServices.ST_ATIVO), connect);
							retorno = result.getCode();
						}
						
						if(retorno <= 0) {
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(ERR_NAO_IDENTIFICADO, "Erro ao inserir o usuario padrao", "httpStatusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
						}
						
						connect.commit();						
					}
				}

				rs.close();
				return new Result(ERR_DENIED_LOGIN_INVALIDO, "Login inválido.", "httpStatusCode", HttpURLConnection.HTTP_UNAUTHORIZED);
			}
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-3, "Erro ao autenticar...", "httpStatusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result autenticarBaseAntiga(String nmLogin, String nmSenha, Connection connect) throws Exception {
	    boolean isConnectionNull = connect == null;
	    try {
	        if (isConnectionNull) {
	            connect = Conexao.conectar();
	        }
	        Usuario usuario = buscarUsuario(nmLogin, connect);
	        if (usuario != null && validarSenha(usuario, nmSenha)) {
	            return processarAutenticacao(usuario, connect);
	        } else {
	            return new Result(ERR_DENIED_LOGIN_INVALIDO, "Login inválido.", "httpStatusCode", HttpURLConnection.HTTP_UNAUTHORIZED);
	        }
	    } catch (Exception e) {
	        e.printStackTrace(System.out);
	        Util.registerLog(e);
	        return new Result(ERR_NAO_IDENTIFICADO);
	    } finally {
	        if (isConnectionNull) {
	            Conexao.desconectar(connect);
	        }
	    }
	}

	private static Usuario buscarUsuario(String nmLogin, Connection connect) throws Exception {
	    String sqlUsuarioEquip = "SELECT * FROM usuario WHERE nm_nick = ? AND st_usuario = ?";
	    try (PreparedStatement pstmt = connect.prepareStatement(sqlUsuarioEquip)) {
	        pstmt.setString(1, nmLogin);
	        pstmt.setInt(2, ST_ATIVO);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            return rs.next() ? usuarioResultSet(rs) : null;
	        }
	    }
	}
	
	private static Result processarAutenticacao(Usuario usuario, Connection connect) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    Agente agente = AgenteServices.getAgenteByUsuario(usuario.getCdUsuario(), connect);
	    int validationCode = validarLogin(usuario, agente, customConnection);
	    Result result = buildResult(validationCode, usuario, agente, customConnection);
	    if (validationCode == SUCESSO) {
	    	atualizarStLogin(usuario, connect);
	        atualizarCdEquipamento(usuario, connect);
	    }
	    return result;
	}

	private static int validarLogin(Usuario usuario, Agente agente, CustomConnection customConnection) throws Exception {
	    if (usuario.getStLogin() == ST_ATIVO && usuario.getCdEquipamento() != 0) {
	        if (usuario.getCdEquipamento() == ParametroServices.getValorOfParametroAsInteger("CD_EQUIPAMENTO_VIDEOMONITORAMENTO", 0)) {
	            return ERR_USUARIO_AINDA_LOGADO;
	        } else if (usuario.getCdEquipamento() > 0) {
	            return ERR_USUARIO_OUTRO_EQUIPAMENTO;
	        }
	    }
	    return SUCESSO;
	}
	
	private static Result buildResult(int validadionCode, Usuario usuario, Agente agente, CustomConnection customConnection) throws Exception {
	    String nmEquipamento = null;
	    switch (validadionCode) {
	        case SUCESSO:
	            return buildSuccessResult(usuario, agente);
	        case ERR_USUARIO_AINDA_LOGADO:
	            nmEquipamento = getEquipamentoByCodigo(usuario.getCdEquipamento(), customConnection);
	            return buildErrorResult(ERR_USUARIO_AINDA_LOGADO, "Este usuário já está autenticado no " + nmEquipamento, usuario);
	        case ERR_USUARIO_OUTRO_EQUIPAMENTO:
	            nmEquipamento = getEquipamentoByCodigo(usuario.getCdEquipamento(), customConnection);
	            return buildErrorResult(ERR_USUARIO_OUTRO_EQUIPAMENTO, "Este usuário já está vinculado a uma sessão no dispositivo: " + nmEquipamento, usuario);
	        default:
	            throw new Exception("Ocorreu um erro ao fazer login");
	    }
	}

	private static Result buildSuccessResult(Usuario usuario, Agente agente) throws Exception {
	    Result result = new Result(1);
	    result.addObject("USUARIO", usuario);
	    result.addObject("AGENTE", agente);
	    result.addObject("AUTHORIZATION", gerarToken(usuario));
	    return result;
	}

	private static Result buildErrorResult(int errorCode, String errorMessage, Usuario usuario) {
	    Result resultError = new Result(errorCode, errorMessage);
	    resultError.addObject("USUARIO", usuario);
	    return resultError;
	}

	private static Usuario usuarioResultSet(ResultSet rs) throws SQLException {
	    return new Usuario(
	            rs.getInt("cod_usuario"),
	            0,
	            rs.getString("nm_nick"),
	            0,
	            rs.getInt("nr_nivel"),
	            rs.getString("nm_senha"),
	            rs.getInt("st_usuario"),
	            null,
	            rs.getInt("ST_LOGIN"),
	            rs.getInt("cd_equipamento")
	    );
	}

	private static boolean validarSenha(Usuario usuario, String nmSenha) {
	    return nmSenha.equals(usuario.getNmSenha());
	}

	private static void atualizarStLogin(Usuario usuario, Connection connect) throws SQLException {
	    String sql = "UPDATE USUARIO SET st_login = ? WHERE nm_nick = ?";
	    try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
	    	pstmt.setInt(1, ST_ATIVO);
	        pstmt.setString(2, usuario.getNmLogin());
	        pstmt.executeUpdate();
	    }
	}
	
	private static void atualizarCdEquipamento(Usuario usuario, Connection connect) throws SQLException {
	    String sql = "UPDATE USUARIO SET cd_equipamento = ? WHERE nm_nick = ?";
	    try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
	        pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_EQUIPAMENTO_VIDEOMONITORAMENTO", 0));
	        pstmt.setString(2, usuario.getNmLogin());
	        pstmt.executeUpdate();
	    }
	}

	private static String gerarToken(Usuario usuario) throws Exception {
	    HashMap<String, Object> headers = new HashMap<>();
	    HashMap<String, Object> payload = new HashMap<>();
	    GregorianCalendar issue = new GregorianCalendar();
	    issue.add(Calendar.DAY_OF_MONTH, 7);
	    headers.put("exp", issue);
	    headers.put("sub", usuario.getNmLogin());
	    payload.put("id", usuario.getCdUsuario());
	    payload.put("login", usuario.getNmLogin());
	    JWT jwt = new JWT();
	    return jwt.generate(headers, payload);
	}

	
	/**
	 * @category mob
	 */
	public static Result mobAutenticar(String nmUsuario, String nmSenha, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns módulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return mobAutenticarOld(nmUsuario, nmSenha, connect);
		
		boolean isConnectionNull = connect == null;
			
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			boolean isAutorizado = verificarPermissao(nmUsuario, connect);
			
			PreparedStatement ps = connect.prepareStatement("SELECT * FROM seg_usuario WHERE nm_login=?");
			ps.setString(1, nmUsuario);
			
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) 
				return new Result(ERR_DENIED_LOGIN_INVALIDO, "Login invalido.");
			else if(rs.getInt("ST_USUARIO") == ST_INATIVO)
				return new Result(ERR_USUARIO_INATIVO, "Usuário inativo.");
			else if(!rs.getString("NM_SENHA").equals(nmSenha))
				return new Result(ERR_DENIED_SENHA_INVALIDA, "Senha inválida.");
			else if(!isAutorizado && rs.getInt("TP_USUARIO") != ADMINISTRADOR)
				return new Result(ERR_USUARIO_NAO_AUTORIZADO, "Usuário não autorizado.");
			
			int cdUsuario = rs.getInt("cd_usuario");
			int stLogin = rs.getInt("ST_LOGIN");
			int tpUsuario = rs.getInt("TP_USUARIO");
			int cdPessoa = rs.getInt("cd_pessoa");
			
			/*
			 * Authorization Token
			 */
			JWT jwt = new JWT();
			HashMap<String, Object> headers = new HashMap<String, Object>();
			HashMap<String, Object> payload = new HashMap<String, Object>();
			GregorianCalendar issue = new GregorianCalendar();
			issue.add(Calendar.HOUR, 12);
			
			headers.put("exp", issue);
			headers.put("sub", nmUsuario);

			payload.put("id", cdUsuario);
			payload.put("login", nmUsuario);
			
			String token = jwt.generate(headers, payload);
			
			Usuario usuario = new Usuario();
			usuario.setCdUsuario(cdUsuario);
			usuario.setTpUsuario(tpUsuario);
			usuario.setStLogin(stLogin);
			usuario.setNmLogin(nmUsuario);
			usuario.setToken(token);
			usuario.setCdPessoa(cdPessoa);
			
			Result result;
			
			if(stLogin > 0) {
				usuario.setStLogin(1);
				result = new Result(1, "Autenticado (Usuário ativo em outro equipamento)", "USUARIO", usuario);
			} else {
				result = new Result(1, "Autenticado", "USUARIO", usuario);
				
				PreparedStatement ls = connect.prepareStatement("UPDATE seg_usuario SET st_login = 1 WHERE nm_login = ?");
				ls.setString(1, nmUsuario);				
				ls.executeUpdate();
			}
			
			
			result.addObject("AGENTE", com.tivic.manager.mob.AgenteServices.get(cdUsuario, connect));
			result.addObject("PESSOA", PessoaDAO.get(usuario.getCdPessoa(), connect));
			
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(ERR_NAO_IDENTIFICADO, "Autenticado", "USUARIO", null);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static boolean verificarPermissao(String nmUsuario, Connection connect) {
		try {
			PreparedStatement ps = connect.prepareStatement("SELECT * FROM seg_usuario_permissao_acao A "
					+ " LEFT JOIN seg_usuario B ON (B.cd_usuario = A.cd_usuario)"
					+ " LEFT JOIN seg_acao C ON(A.cd_acao = C.cd_acao) "
					+ " WHERE C.id_acao = 'SEG_ACESSAR_SISTEMA' AND lg_natureza = 1 AND nm_login=?");
			ps.setString(1, nmUsuario);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * @category mob
	 * metodo de autenticação usado no eTransito Angular, fase de migração de modelo de dados
	 */
	private static Result mobAutenticarOld(String nmUsuario, String nmSenha, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement ps = connect.prepareStatement("SELECT * FROM usuario WHERE nm_nick=?");
			ps.setString(1, nmUsuario);
			
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next())
				return new Result(ERR_DENIED_LOGIN_INVALIDO, "Login inválido.");
			else if(rs.getInt("ST_USUARIO") == ST_INATIVO)
				return new Result(ERR_USUARIO_INATIVO, "Usuário inativo.");
			else if(!rs.getString("NM_SENHA").equals(nmSenha))
				return new Result(ERR_DENIED_SENHA_INVALIDA, "Senha inválida.");
			
			int cdUsuario = rs.getInt("cod_usuario");
			
			/*
			 * Authorization Token
			 */
			HashMap<String, Object> headers = new HashMap<String, Object>();
			HashMap<String, Object> payload = new HashMap<String, Object>();
			GregorianCalendar issue = new GregorianCalendar();
			issue.add(Calendar.HOUR, 12);
			
			headers.put("exp", issue);
			headers.put("sub", nmUsuario);

			payload.put("id", cdUsuario);
			payload.put("login", nmUsuario);
			JWT jwt = new JWT();
			String token = jwt.generate(headers, payload);
			
			Usuario usuario = new Usuario();
			usuario.setCdUsuario(cdUsuario);
			usuario.setNmLogin(nmUsuario);
			usuario.setToken(token);
			
			Result result;
			
				result = new Result(1, "Autenticado (Usuário ativo em outro equipamento)", "USUARIO", usuario);
				result = new Result(1, "Autenticado", "USUARIO", usuario);
				
				
			com.tivic.manager.str.Agente agente = com.tivic.manager.str.AgenteServices.getAgenteByUsuario(cdUsuario, connect);
			result.addObject("AGENTE", agente != null ? com.tivic.manager.str.AgenteServices.toMobAgente(agente) : null);
			result.addObject("PESSOA", PessoaDAO.get(usuario.getCdPessoa(), connect));
			
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result(ERR_NAO_IDENTIFICADO, "Autenticado", "USUARIO", null);
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result cadastrar(PessoaFisica pessoaFisica, Usuario usuario) {
		return cadastrar(pessoaFisica, usuario, null);
	}
	
	public static Result cadastrar(PessoaFisica pessoaFisica, Usuario usuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			pessoaFisica.setNrCpf(Util.apenasNumeros(pessoaFisica.getNrCpf()));
			
			if(pessoaFisica.getNrCpf() == null || !Util.isCpfValido(pessoaFisica.getNrCpf())) {
				 return new Result(-1, "O CPF informado não é válido.");
			 }
			 
			 if(PessoaFisicaServices.getByCpf(pessoaFisica.getNrCpf(), connect) != null) {
				 return new Result(-2, "O CPF informado já está cadastrado.");
			 }
			 
			 Result savePessoa = PessoaServices.save(pessoaFisica, null, 0, 0, connect);
			 usuario.setCdPessoa(pessoaFisica.getCdPessoa());
			 usuario.setStUsuario(ST_ATIVO);
			 
			 if (savePessoa.getCode()>0) {
				 usuario.setNmLogin(pessoaFisica.getNrCpf());
				 Result saveUsuario = save(usuario, connect);
				 usuario = (Usuario)saveUsuario.getObjects().get("USUARIO");
				 
				 if(saveUsuario.getCode() < 0) {
					 connect.rollback();
					 return new Result(-3, "Erro ao salvar dados da pessoa");
				 }
				 
			 } else {
				 connect.rollback();
				 return new Result(-4, "Erro ao salvar dados de usuário");
			 }
			 
			 connect.commit();
			 
			/**
			 * Authorization Token
			 */
			HashMap<String, Object> headers = new HashMap<String, Object>();
			HashMap<String, Object> payload = new HashMap<String, Object>();
			GregorianCalendar issue = new GregorianCalendar();
			issue.add(Calendar.DAY_OF_MONTH, 7);
			
			headers.put("exp", issue);
			headers.put("sub", pessoaFisica.getNmPessoa());

			payload.put("id", usuario.getCdUsuario());
			JWT jwt = new JWT();
			return new Result(1, "usuário cadastro com sucesso!", "AUTHORIZATION", jwt.generate(headers, payload));			
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-3, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result autenticarCpf(String nrCpf, String nmSenha, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(!Util.isCpfValido(nrCpf)) {
				return new Result(-1, "O CPF informado não é válido.");
			}

			String sqlUsuario = "SELECT * FROM seg_usuario A, grl_pessoa B, grl_pessoa_fisica C " +
					            " WHERE C.nr_cpf = ? " +
					            "   AND A.st_usuario = " + ST_ATIVO;

			PreparedStatement pstmt = connect.prepareStatement(sqlUsuario);
			pstmt.setString(1, nrCpf);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.size() == 0) {
				return new Result(-2, "usuário não cadastrado.");					
			}
			
			if(nmSenha == null || nmSenha.equals("")) {
				return new Result(-3, "A senha não foi informada.");				
			}

			/**
			 * Authorization Token
			 */
			HashMap<String, Object> headers = new HashMap<String, Object>();
			HashMap<String, Object> payload = new HashMap<String, Object>();
			GregorianCalendar issue = new GregorianCalendar();
			issue.add(Calendar.DAY_OF_MONTH, 7);
			
			headers.put("exp", issue);
			headers.put("sub", rsm.getString("NM_PESSOA"));

			payload.put("id", rsm.getString("CD_USUARIO"));
			JWT jwt = new JWT();
			return new Result(1, "usuário autenticado com sucesso!", "AUTHORIZATION", jwt.generate(headers, payload));			
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-3, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	/**
	 * Elimina a necessidade de solicitar dados de autenticação, uma vez que se conheça
	 * o usuário.
	 * 
	 * XGH at its finest.
	 * 
	 * @param cdUsuario
	 * @return Result (com as mesmas infos de {@link #autenticar(String, String, String, String, int, Connection)})
	 * 
	 * @since 04/01/2018
	 * @author mauricio
	 * 
	 * @category JurisWordAddIn
	 */
	public static Result cheatAuth(int cdUsuario) {
		
		Usuario usuario = UsuarioDAO.get(cdUsuario);
		if(usuario==null) {
			return new Result(-1, "usuário não encontrado");
		}
		
		return autenticar(usuario.getNmLogin(), usuario.getNmSenha(), null, null, null);
	}
	
	public static String getPasswordHash(String senha){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(SALT);
		strBuilder.append(senha);
		String hash = HashServices.md5( strBuilder.toString());
		
		return hash;
	}
	public static Result sair(String nmLogin) {
		return logout(nmLogin, null);
	}
	
	public static Result mobLogout (String nmUsuario) {
		return mobLogout(nmUsuario, null);
	}
	
	public static Result mobLogout (String nmUsuario, Connection connect) {
		
		/*
		 * TL;DR
		 * Alguns m�dulos novos do eTransito Angular precisaram ser utilizados na base antiga (ex: eTransito BI). 
		 * Manter este desvio de fluxo no momento
		 */
		if(Util.isStrBaseAntiga())
			return mobLogoutOld(nmUsuario, connect);
		
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_usuario WHERE nm_login = ?");
			pstmt.setString(1, nmUsuario);
			
			ResultSetMap result = new ResultSetMap(pstmt.executeQuery());
			
			if (result.getLines().size() <= 0) {
				return new Result(-1, "Usuário não encontrado !");
			} else {
				PreparedStatement ps = connect.prepareStatement("UPDATE seg_usuario SET st_login = 0, cd_equipamento = 0 WHERE nm_login = ?");
				ps.setString(1, nmUsuario);
				int res = ps.executeUpdate();
				
				if(res == 1)
					return new Result(1, "Logout efetuado com sucesso.");
				else
					return new Result(0, "Não foi possível efetuar o logout");
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result (-3, "Erro na requisição");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static Result mobLogoutOld(String nmUsuario, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM USUARIO WHERE nm_nick = ?");
			pstmt.setString(1, nmUsuario);
			
			ResultSetMap result = new ResultSetMap(pstmt.executeQuery());
			
			if (result.getLines().size() <= 0) {
				return new Result(-1, "Usu�rio n�o encontrado !");
			} else {
				PreparedStatement ps = connect.prepareStatement("UPDATE USUARIO SET st_login = 0, cd_equipamento = null WHERE nm_nick = ?");
				ps.setString(1, nmUsuario);
				int res = ps.executeUpdate();
				
				if(res == 1)
					return new Result(1, "Logout efetuado com sucesso.");
				else
					return new Result(0, "N�o foi poss�vel efetuar o logout");
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return new Result (-3, "Erro na requisi��o");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result logout(String nmLogin, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_usuario " +
															   "WHERE nm_login	 = ? " +
															   "  AND st_usuario = 1");
			pstmt.setString(1, nmLogin);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				Usuario usuario = new Usuario(rs.getInt("cd_usuario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_pergunta_secreta"),
						rs.getString("nm_login"),
						rs.getString("nm_senha"),
						rs.getInt("tp_usuario"),
						rs.getString("nm_resposta_secreta"),
						rs.getInt("st_usuario"));
				
				FlexSession session = FlexContext.getFlexSession();
			    session.setAttribute("usuario", usuario);
				
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);

				if(usuario!=null && pessoa!=null){
					GregorianCalendar dtLog = new GregorianCalendar();
					String remoteIP = null;
					String txtExecucao = "Logout às " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +
						pessoa.getNmPessoa() + ", através da conta " + usuario.getNmLogin() +
						((remoteIP!=null)?", IP "+remoteIP:"");
					Sistema log = new Sistema(0, //cdLog
							dtLog, //dtLog
							txtExecucao, //txtLog
							com.tivic.manager.log.SistemaServices.TP_LOGOUT, //tpLog
							usuario.getCdUsuario());//cdUsuario
					SistemaDAO.insert(log, connect);
				}
				return new Result(usuario.getCdUsuario(), "Logout com sucesso...");
			}
			else
				return new Result(-2, "usuário não existe...");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao sair do sistema...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int logout(HttpSession session, String remoteIP) {
		return logout(session, remoteIP, null);
	}

	public static int logout(HttpSession session, String remoteIP, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;



			Usuario usuario = (Usuario)session.getAttribute("usuario");
			Pessoa pessoa = (Pessoa)session.getAttribute("pessoa");

			if(usuario!=null && pessoa!=null){
				GregorianCalendar dtLog = new GregorianCalendar();
				String txtExecucao = "Logout às " + Util.formatDateTime(dtLog, "dd/MM/yyyy HH:mm:ss", "") + " por " +
					pessoa.getNmPessoa() + ", através da conta " + usuario.getNmLogin() +
					((remoteIP!=null)?", IP "+remoteIP:"");
				Sistema log = new Sistema(0, //cdLog
						dtLog, //dtLog
						txtExecucao, //txtLog
						com.tivic.manager.log.SistemaServices.TP_LOG_GERAL, //tpLog
						usuario.getCdUsuario());//cdUsuario
				SistemaDAO.insert(log, connection);
			}

			session.removeAttribute("usuario");
			session.removeAttribute("user");
			session.removeAttribute("objectsSystem");
			session.removeAttribute("system");
			session.removeAttribute("pessoa");
			session.removeAttribute("permissionsActionsUser");

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findUsuarioPessoa(ArrayList<ItemComparator> criterios) {
		
		return Search.find("SELECT A.cd_usuario, A.cd_pergunta_secreta, A.nm_login, " +
				"A.tp_usuario, A.nm_resposta_secreta, A.st_usuario, " +
				"A.cd_pessoa, B.nm_pessoa, B.nm_pessoa AS nm_usuario, B.id_pessoa, B2.*, B3.* " +
				"FROM SEG_USUARIO A "  +
			    "LEFT OUTER JOIN GRL_PESSOA B ON (A.CD_PESSOA=B.CD_PESSOA) "+
			    "LEFT OUTER JOIN GRL_PESSOA_FISICA B2 ON (A.CD_PESSOA=B2.CD_PESSOA) "+
			    "LEFT OUTER JOIN GRL_PESSOA_JURIDICA B3 ON (A.CD_PESSOA=B3.CD_PESSOA) ", criterios, Conexao.conectar());
	}

	public int insertUser(User user) {
		if (user==null)
			return -1;
		Usuario usuario = new Usuario(0, 0, 0, user.getLogin(), user.getPassword(), USUARIO_COMUM, "", ST_ATIVO);
		PessoaFisica pessoa = new PessoaFisica(0,
				0, //cdPessoaSuperior
				0, //cdPais
				user.getName(),
				"", //nrTelefone1
				"", //nrTelefone2
				"", //nrCelular
				"", //nrFax
				"", //nmEmail
				new GregorianCalendar(), //dtCadastro
				PessoaServices.TP_FISICA, //gnPessoa
				null, //imgFoto
				1, //stCadastro
				"", //nmUrl
				"", //nmApelido
				"", //txtObservacao
				0, //lgNotificacao
				"", //idPessoa
				0, //cdClassificacao
				0, //cdFormaDivulgacao
				null, //dtChegadaPais
				0, //cdCidade
				0, //cdEscolaridade
				null, //dtNascimento
				"", //nrCpf
				"", //sgOrgaoRg
				"", //nmMae
				"", //nmPai
				0, //tpSexo
				0, //stEstadoCivil
				"", //nrRg
				"", //nrCnh
				null, //dtValidadeCnh
				null, //dtPrimeiraHabilitacao
				0, //tpCategoriaHabilitacao
				0, //tpRaca
				0, //lgDeficienteFisico
				"", //nmFormaTratamento
				0, //cdEstadoRg
				null,//dtEmissaoRg
				null,//blbFingerPrint
				0 /*cdConjuge*/, 0 /*qtMembrosFamilia*/, 0 /*vlRendaFamiliarPerCapta*/, 0 /*tpNacionalidade*/,0 /*tpFiliacaoPai*/); 
		return insert(usuario, pessoa);
	}

	public int updateUser(User user) {
		if (user==null || user.getId()<=0)
			return -1;
		Usuario usuario = UsuarioDAO.get(user.getId());
		if (usuario==null)
			return -1;
		PessoaFisica pessoa = usuario.getCdPessoa()<=0 ? null : PessoaFisicaDAO.get(usuario.getCdPessoa());
		if (pessoa!=null)
			pessoa.setNmPessoa(user.getName());
		else
			pessoa = new PessoaFisica(0,
					0, //cdPessoaSuperior
					0, //cdPais
					user.getName(),
					"", //nrTelefone1
					"", //nrTelefone2
					"", //nrCelular
					"", //nrFax
					"", //nmEmail
					new GregorianCalendar(), //dtCadastro
					PessoaServices.TP_FISICA, //gnPessoa
					null, //imgFoto
					1, //stCadastro
					"", //nmUrl
					"", //nmApelido
					"", //txtObservacao
					0, //lgNotificacao
					"", //idPessoa
					0, //cdClassificacao
					0, //cdFormaDivulgacao
					null, //dtChegadaPais
					0, //cdCidade
					0, //cdEscolaridade
					null, //dtNascimento
					"", //nrCpf
					"", //sgOrgaoRg
					"", //nmMae
					"", //nmPai
					0, //tpSexo
					0, //stEstadoCivil
					"", //nrRg
					"", //nrCnh
					null, //dtValidadeCnh
					null, //dtPrimeiraHabilitacao
					0, //tpCategoriaHabilitacao
					0, //tpRaca
					0, //lgDeficienteFisico
					"", //nmFormaTratamento
					0, //cdEstadoRg
					null,//dtEmissaoRg
					null,//blbFingerprint
					0 /*cdConjuge*/, 0 /*qtMembrosFamilia*/, 0 /*vlRendaFamiliarPerCapta*/, 0 /*tpNacionalidade*/,0 /*tpFiliacaoPai*/); 
		usuario.setNmLogin(user.getLogin());
		usuario.setNmSenha(user.getPassword());
		return UsuarioDAO.update(usuario);
	}

	public int deleteUser(int idUser) {
		return UsuarioDAO.delete(idUser);
	}

	public User[] getUsers() {
		ResultSetMap rsm = findUsuarioPessoa(new ArrayList<ItemComparator>());
		User[] users = new User[rsm==null ? 0 : rsm.size()];
		for (int i = 0; rsm!=null && rsm.next(); i++)
			users[i] = new User(rsm.getInt("CD_USUARIO"), rsm.getString("NM_PESSOA"), rsm.getString("NM_LOGIN"), rsm.getString("NM_SENHA"));
		return users;
	}

	public static User findUser(int idUser) {
		return findUser(idUser, null);
	}

	public static User findUser(int idUser, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			Usuario usuario = UsuarioDAO.get(idUser);
			return usuario==null ? null : new User(usuario.getCdUsuario(), null, usuario.getNmLogin(), null);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String getUsuarioNome(int cdUsuario) {
		return getUsuarioNome(cdUsuario, null);
	}

	public static String getUsuarioNome(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT nm_pessoa FROM seg_usuario A, grl_pessoa B " +
					                                "WHERE A.cd_pessoa  = B.cd_pessoa " +
					                                "  AND A.cd_usuario = "+cdUsuario).executeQuery();
			return rs.next() ? rs.getString("nm_pessoa") : "Nenhum";
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

	public Result addUsuarioGrupo(int cdUsuario, int cdGrupo) {
		int retorno = addUserToGroup(cdUsuario, cdGrupo);
		
		return new Result(retorno, retorno > 0 ? "usuário adicionado ao grupo." : "Erro ao adicionar usuário ao grupo");
	}
	
	public Result removeUsuarioGrupo(int cdUsuario, int cdGrupo) {
		int retorno = deleteUserOfGroup(cdUsuario, cdGrupo);
		
		return new Result(retorno, retorno > 0 ? "usuário removido do grupo." : "Erro ao remover usuário do grupo");
	}
	
	public int addUserToGroup(int idUser, int idGroup) {
		return addUserToGroup(idUser, idGroup, null);
	}

	public static int addUserToGroup(int idUser, int idGroup, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_GRUPO (CD_USUARIO, CD_GRUPO) VALUES (?, ?)");
			pstmt.setInt(1, idUser);
			pstmt.setInt(2, idGroup);
			pstmt.execute();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public int deleteUserOfGroup(int idUser, int idGroup) {
		return deleteUserOfGroup(idUser, idGroup, null);
	}

	public static int deleteUserOfGroup(int idUser, int idGroup, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM SEG_USUARIO_GRUPO " +
															   "WHERE CD_USUARIO = ? " +
															   " AND CD_GRUPO = ?");
			pstmt.setInt(1, idUser);
			pstmt.setInt(2, idGroup);
			pstmt.execute();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public Group[] getGroupsOfUser(int idUser) {
		return getGroupsOfUser(idUser, null);
	}

	public static Group[] getGroupsOfUser(int idUser, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_GRUPO A, SEG_USUARIO_GRUPO B " +
															   "WHERE A.CD_GRUPO = B.CD_GRUPO " +
															   "  AND B.CD_USUARIO = ?");
			pstmt.setInt(1, idUser);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			Group[] groups = rsm==null || rsm.size()==0 ? null : new Group[rsm.size()];
			for (int i=0; rsm!=null && rsm.next(); i++)
				groups[i] = new Group(rsm.getInt("CD_GRUPO"), rsm.getString("NM_GRUPO"));
			return groups;
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

	public User[] findUsers(int idUser, String nameUser, String loginUser) {
		Connection connect = null;
		try {
			connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_USUARIO A " +
									"LEFT OUTER JOIN GRL_PESSOA B ON (A.CD_PESSOA = B.CD_PESSOA) " +
									"WHERE 1 = 1" +
									(idUser>0 ? " AND CD_USUARIO = ?" : "") +
									(nameUser!=null && !nameUser.trim().equals("") ? " AND NM_PESSOA LIKE ?" : "") +
									(loginUser!=null && !loginUser.trim().equals("") ? " AND NM_LOGIN = ?" : ""));
			int i = 1;
			if (idUser>0)
				pstmt.setInt(i++, idUser);
			if (nameUser!=null && !nameUser.trim().equals(""))
				pstmt.setString(i++, nameUser.trim().toUpperCase() + "%");
			if (loginUser!=null && !loginUser.trim().equals(""))
				pstmt.setString(i++, loginUser.trim());
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			User[] users = rsm==null || rsm.size()==0 ? null : new User[rsm.size()];
			for (i=0; rsm!=null && rsm.next(); i++)
				users[i] = new User(rsm.getInt("CD_USUARIO"), rsm.getString("NM_PESSOA"), rsm.getString("NM_LOGIN"), rsm.getString("NM_SENHA"));
			return users;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static boolean getPermissao(int cdUsuario, String idAcao) {
		return getPermissao(cdUsuario, idAcao, null);
	}
	
	public static boolean getPermissao(int cdUsuario, String idAcao, Connection connect) {
		boolean isConnectionNull = (connect == null);
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			Usuario usuario = new ConversorBaseAntigaNovaFactory().getUsuarioRepository().get(cdUsuario);	
						
			if( usuario != null && usuario.getTpUsuario() == 0)
				return true;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM SEG_USUARIO_PERMISSAO_ACAO " + 
					"WHERE CD_USUARIO = ? AND " + 
					"CD_ACAO = (SELECT CD_ACAO FROM SEG_ACAO WHERE ID_ACAO = ?)"
					);
			
			pstmt.setInt(1, cdUsuario);
			pstmt.setString(2, idAcao);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			UsuarioPermissaoAcao upa;
			
			if(rsm.next()) {
				upa = new UsuarioPermissaoAcao(
					rsm.getInt("cd_usuario"),
					rsm.getInt("cd_acao"),
					rsm.getInt("cd_modulo"),
					rsm.getInt("cd_sistema"),
					rsm.getInt("lg_natureza")
				);
				
				return (upa.getLgNatureza() == 1);
			} else {
				return false;
			}
			
		} catch (Exception e){
			e.printStackTrace(System.out);
			return false;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getPermissoesAcao(int cdUsuario, int cdSistema, int cdModulo, int cdAgrupamento) {
		return getPermissoesAcao(cdUsuario, cdSistema, cdModulo, cdAgrupamento, false, null);
	}
	
	public static ResultSetMap getPermissoesAcao(int cdUsuario, int cdSistema, int cdModulo, int cdAgrupamento, boolean lgOrganizacaoDestaque) {
		return getPermissoesAcao(cdUsuario, cdSistema, cdModulo, cdAgrupamento, lgOrganizacaoDestaque, null);
	}
	
	public static ResultSetMap getPermissoesAcao(int cdUsuario, int cdSistema, int cdModulo, int cdAgrupamento, boolean lgOrganizacaoDestaque, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String sql = "SELECT A.*, B.*, C.*, D.nm_agrupamento, D.ds_agrupamento, D.id_agrupamento, E.cd_usuario, E.nm_login, " +
					   "			(SELECT COUNT(*) FROM SEG_USUARIO_PERMISSAO_ACAO F " +
					   "			 WHERE F.CD_USUARIO = " +cdUsuario+
					   "			   AND F.CD_SISTEMA = A.CD_SISTEMA " +
					   "			   AND F.CD_MODULO  = A.CD_MODULO " +
					   "			   AND F.CD_ACAO    = A.CD_ACAO) AS LG_PERMISSAO, " +
					   "			(SELECT lg_natureza FROM SEG_USUARIO_PERMISSAO_ACAO F " +
					   "			 WHERE F.CD_USUARIO = " +cdUsuario+
					   "			   AND F.CD_SISTEMA = A.CD_SISTEMA " +
					   "			   AND F.CD_MODULO  = A.CD_MODULO " +
					   "			   AND F.CD_ACAO    = A.CD_ACAO) AS LG_NATUREZA, " +
					   "			(SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_ACAO D, SEG_USUARIO_GRUPO E " +
					   "			 WHERE E.CD_USUARIO = " +cdUsuario+
					   "			   AND D.CD_GRUPO   = E.CD_GRUPO " +
					   "			   AND D.CD_SISTEMA = A.CD_SISTEMA " +
					   "			   AND D.CD_MODULO  = A.CD_MODULO " +
					   "			   AND D.CD_ACAO    = A.CD_ACAO) AS LG_PERMISSAO_GRUPO " +
					   "FROM seg_acao A " +
					   "JOIN seg_modulo  B ON (A.cd_modulo = B.cd_modulo AND A.cd_sistema = B.cd_sistema) " +
					   "JOIN seg_sistema C ON (B.cd_sistema = C.cd_sistema) " +
					   "LEFT OUTER JOIN seg_agrupamento_acao D ON (A.cd_agrupamento = D.cd_agrupamento " +
					   "                                       AND A.cd_modulo = D.cd_modulo " +
					   "                                       AND A.cd_sistema = D.cd_sistema)" +
					   "JOIN seg_usuario E ON (E.cd_usuario = " +cdUsuario+ ") " +
					   "WHERE B.lg_ativo = 1 " +
					   (cdModulo==0 ? "" : " AND A.cd_modulo = "+cdModulo) +
					   (cdSistema==0 ? "" : " AND A.cd_sistema = "+cdSistema) +
					   (cdAgrupamento==0 ? "" : " AND A.cd_agrupamento = "+cdAgrupamento)+
					   (lgOrganizacaoDestaque ? " AND (A.tp_organizacao = 0 OR A.tp_organizacao is null)" : "") + //Permissoes comuns
				   	   " ORDER BY D.nm_agrupamento, A.tp_organizacao DESC, A.cd_acao_superior, A.nr_ordem ";
			

			LogUtils.debug("SQL:\n"+Search.formatSQL(sql));
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ResultSetMap rsmFinal = new ResultSetMap();
			
			String nmAgrupamento = "";
			while (rsm.next()) {
				HashMap<String, Object> reg = rsm.getRegister();
				reg.put("LG_NATUREZA", rsm.getObject("lg_natureza")==null ? 0 : rsm.getInt("LG_NATUREZA"));
				reg.put("NM_DESTAQUE", "Outras");
				rsmFinal.addRegister(reg);
			}
			
			
			//injetar agrupamentos que contenham destaques
			if(lgOrganizacaoDestaque) {
				ResultSetMap rsm1 = getAgrupamentosDestacados(cdSistema, cdModulo, connect);
				nmAgrupamento = "";
				while (rsm1.next()) {
					if( rsm1.getString("nm_agrupamento") != null && !nmAgrupamento.equals(rsm1.getString("nm_agrupamento"))){
						nmAgrupamento = rsm1.getString("nm_agrupamento");
						
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("NM_AGRUPAMENTO", nmAgrupamento);
						register.put("NM_DESTAQUE", "Básicas");
						register.put("RSM_DESTACADO", getPermissoesDestacadas(cdUsuario, cdSistema, cdModulo, rsm1.getInt("cd_agrupamento"), connect));
						rsmFinal.addRegister(register);
					}
				}
			}
			
			return rsmFinal;
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
	
	public static ResultSetMap getPermissoesDestacadas(int cdUsuario, int cdSistema, int cdModulo, int cdAgrupamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					   "SELECT A.*, B.*, C.*, D.nm_agrupamento, D.ds_agrupamento, D.id_agrupamento, " +
					   "			(SELECT COUNT(*) FROM SEG_USUARIO_PERMISSAO_ACAO F " +
					   "			 WHERE F.CD_USUARIO = " +cdUsuario+
					   "			   AND F.CD_SISTEMA = A.CD_SISTEMA " +
					   "			   AND F.CD_MODULO  = A.CD_MODULO " +
					   "			   AND F.CD_ACAO    = A.CD_ACAO) AS LG_PERMISSAO, " +
					   "			(SELECT lg_natureza FROM SEG_USUARIO_PERMISSAO_ACAO F " +
					   "			 WHERE F.CD_USUARIO = " +cdUsuario+
					   "			   AND F.CD_SISTEMA = A.CD_SISTEMA " +
					   "			   AND F.CD_MODULO  = A.CD_MODULO " +
					   "			   AND F.CD_ACAO    = A.CD_ACAO) AS LG_NATUREZA, " +
					   "			(SELECT COUNT(*) FROM SEG_GRUPO_PERMISSAO_ACAO D, SEG_USUARIO_GRUPO E " +
					   "			 WHERE E.CD_USUARIO = " +cdUsuario+
					   "			   AND D.CD_GRUPO   = E.CD_GRUPO " +
					   "			   AND D.CD_SISTEMA = A.CD_SISTEMA " +
					   "			   AND D.CD_MODULO  = A.CD_MODULO " +
					   "			   AND D.CD_ACAO    = A.CD_ACAO) AS LG_PERMISSAO_GRUPO " +
					   "FROM seg_acao A " +
					   "JOIN seg_modulo  B ON (A.cd_modulo = B.cd_modulo AND A.cd_sistema = B.cd_sistema) " +
					   "JOIN seg_sistema C ON (B.cd_sistema = C.cd_sistema) " +
					   "LEFT OUTER JOIN seg_agrupamento_acao D ON (A.cd_agrupamento = D.cd_agrupamento " +
					   "                                       AND A.cd_modulo = D.cd_modulo " +
					   "                                       AND A.cd_sistema = D.cd_sistema)" +
					   "WHERE B.lg_ativo = 1 " +
					   (cdModulo==0 ? "" : " AND A.cd_modulo = "+cdModulo) +
					   (cdSistema==0 ? "" : " AND A.cd_sistema = "+cdSistema) +
					   (cdAgrupamento==0 ? "" : " AND A.cd_agrupamento = "+cdAgrupamento)+
					   " AND tp_organizacao > 0 " + //Permissoes destacadas
				   	   " ORDER BY D.nm_agrupamento, A.tp_organizacao DESC, A.nr_ordem, A.ds_acao");
			ResultSetMap rsm = new ResultSetMap();
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				HashMap<String, Object> reg = ResultSetMap.getLineHashMap(rs);
				reg.put("LG_NATUREZA", rs.getObject("lg_natureza")==null ? -1 : rs.getInt("LG_NATUREZA"));
				rsm.addRegister(reg);
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

	public static ResultSetMap getAgrupamentosDestacados(int cdSistema, int cdModulo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					   "SELECT A.*, B.*, C.*, D.nm_agrupamento, D.ds_agrupamento, D.id_agrupamento "+
					   "FROM seg_acao A " +
					   "JOIN seg_modulo  B ON (A.cd_modulo = B.cd_modulo AND A.cd_sistema = B.cd_sistema) " +
					   "JOIN seg_sistema C ON (B.cd_sistema = C.cd_sistema) " +
					   "LEFT OUTER JOIN seg_agrupamento_acao D ON (A.cd_agrupamento = D.cd_agrupamento " +
					   "                                       AND A.cd_modulo = D.cd_modulo " +
					   "                                       AND A.cd_sistema = D.cd_sistema)" +
					   "WHERE  B.lg_ativo = 1 " +
					   (cdModulo==0 ? "" : " AND A.cd_modulo = "+cdModulo) +
					   (cdSistema==0 ? "" : " AND A.cd_sistema = "+cdSistema) +
					   " AND A.tp_organizacao > 0 " + //Permissoes destacadas
				   	   " ORDER BY D.nm_agrupamento, A.tp_organizacao DESC, A.nr_ordem, A.ds_acao");

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
	
	public static Result adicionarPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, int lgNatureza) {
		int retorno = addPermissaoAcao(cdUsuario, cdAcao, cdModulo, cdSistema, lgNatureza, null, null);
		return new Result(retorno, retorno>0 ? "Permissão incluída com sucesso" : "Erro ao incluir permissão");
	}
	
	public static int addPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, int lgNatureza) {
		return addPermissaoAcao(cdUsuario, cdAcao, cdModulo, cdSistema, lgNatureza, null, null);
	}
	
	public static int addPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, int lgNatureza, Connection connect) {
		return addPermissaoAcao(cdUsuario, cdAcao, cdModulo, cdSistema, lgNatureza, null, connect);
	}
	
	public static int addPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, int lgNatureza, AuthData auth) {
		return addPermissaoAcao(cdUsuario, cdAcao, cdModulo, cdSistema, lgNatureza, auth, null);
	}

	public static int addPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, int lgNatureza, AuthData auth, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			
			UsuarioPermissaoAcao permissaoOld = UsuarioPermissaoAcaoDAO.get(cdUsuario, cdAcao, cdModulo, cdSistema, connect);
			
			PreparedStatement pstmt = connect.prepareStatement(
					     "UPDATE seg_usuario_permissao_acao SET lg_natureza = " +lgNatureza+
						 " WHERE CD_USUARIO = " +cdUsuario+
						 "   AND CD_SISTEMA = " +cdSistema+
						 "   AND CD_MODULO  = " +cdModulo+
						 "   AND CD_ACAO    = "+cdAcao);
			if(pstmt.executeUpdate()<=0) {
				pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_PERMISSAO_ACAO (CD_USUARIO, CD_SISTEMA, CD_MODULO, CD_ACAO, LG_NATUREZA) VALUES(?, ?, ?, ?, ?)");
				pstmt.setInt(1, cdUsuario);
				pstmt.setInt(2, cdSistema);
				pstmt.setInt(3, cdModulo);
				pstmt.setInt(4, cdAcao);
				pstmt.setInt(5, lgNatureza);
				pstmt.executeUpdate();
			}
			
			UsuarioPermissaoAcao permissaoNew = UsuarioPermissaoAcaoDAO.get(cdUsuario, cdAcao, cdModulo, cdSistema, connect);

			// TODO: log de alterações de permissão
			@SuppressWarnings("unused")
			int cdAcaoPermissao = 0;
			ResultSet rs = connect.prepareStatement("SELECT * FROM seg_acao WHERE id_acao='SEG.UPD_PERMISSAO'").executeQuery();
			if(rs.next()) {
				cdAcaoPermissao = rs.getInt("cd_acao");
			} rs.close();
			
			LogServices.log(ComplianceManager.TP_ACAO_UPDATE, "SEG.UPD_PERMISSAO", auth, permissaoNew, permissaoOld);
			
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public Result adicionarPermissoesAcoes(int cdUsuario, HashMap<String, Object>[] acoes, int lgNatureza) {
		return adicionarPermissoesAcoes(cdUsuario, acoes, lgNatureza, null, null);
	}
	
	public Result adicionarPermissoesAcoes(int cdUsuario, HashMap<String, Object>[] acoes, int lgNatureza, AuthData auth) {
		return adicionarPermissoesAcoes(cdUsuario, acoes, lgNatureza, auth, null);
	}
	
	public Result adicionarPermissoesAcoes(int cdUsuario, HashMap<String, Object>[] acoes, int lgNatureza, AuthData auth, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			for (int i = 0; i < acoes.length; i++) {
				retorno = addPermissaoAcao(cdUsuario, 
										   (Integer)acoes[i].get("CD_ACAO"), 
										   (Integer)acoes[i].get("CD_MODULO"), 
										   (Integer)acoes[i].get("CD_SISTEMA"), lgNatureza, auth, connect);
				if(retorno<=0)
					break;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao incluir permissões");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Permissões incluídas com sucesso" : "Erro ao incluir permissões");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao incluir permissões");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public Result removerPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema) {
		int retorno = dropPermissaoAcao(cdUsuario, cdAcao, cdModulo, cdSistema, null);
		return new Result(retorno, retorno>0 ? "Permissão removida com sucesso" : "Erro ao remover permissão");
	}
	
	
	public int dropPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema) {
		return dropPermissaoAcao(cdUsuario, cdAcao, cdModulo, cdSistema, null);
	}
	
	public int dropPermissaoAcao(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					                 "DELETE FROM SEG_USUARIO_PERMISSAO_ACAO " +
									 "WHERE CD_USUARIO = " + cdUsuario +
									 "  AND CD_SISTEMA = " + cdSistema +
									 "  AND CD_MODULO  = " + cdModulo +
									 "  AND CD_ACAO    = " + cdAcao);
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public Result removerPermissoesAcoes(int cdUsuario, HashMap<String, Object>[] acoes) {
		return removerPermissoesAcoes(cdUsuario, acoes, null);
	}
	
	public Result removerPermissoesAcoes(int cdUsuario, HashMap<String, Object>[] acoes, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			for (int i = 0; i < acoes.length; i++) {
				retorno = dropPermissaoAcao(cdUsuario, 
										   (Integer)acoes[i].get("CD_ACAO"), 
										   (Integer)acoes[i].get("CD_MODULO"), 
										   (Integer)acoes[i].get("CD_SISTEMA"), connect);
				if(retorno<=0)
					break;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao remover permissões");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Permissões removidas com sucesso" : "Erro ao remover permissões");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao remover permissões");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int addPermissaoModulo(int cdUsuario, int cdModulo, int cdSistema, int lgNatureza) {
		return addPermissaoModulo(cdUsuario, cdModulo, cdSistema, lgNatureza, null);
	}

	public static int addPermissaoModulo(int cdUsuario, int cdModulo, int cdSistema, int lgNatureza, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					     "UPDATE seg_usuario_modulo SET lg_natureza = " +lgNatureza+
						 " WHERE CD_USUARIO = " +cdUsuario+
						 "   AND CD_SISTEMA = " +cdSistema+
						 "   AND CD_MODULO  = " +cdModulo);
			if(pstmt.executeUpdate()<=0) {
				pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_MODULO (CD_USUARIO, CD_SISTEMA, CD_MODULO, LG_NATUREZA) VALUES(?, ?, ?, ?)");
				pstmt.setInt(1, cdUsuario);
				pstmt.setInt(2, cdSistema);
				pstmt.setInt(3, cdModulo);
				pstmt.setInt(4, lgNatureza);
				pstmt.executeUpdate();
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int dropPermissaoModulo(int cdUsuario, int cdModulo, int cdSistema) {
		return dropPermissaoModulo(cdUsuario, cdModulo, cdSistema, null);
	}
	public static int dropPermissaoModulo(int cdUsuario, int cdModulo, int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
				                 "DELETE FROM SEG_USUARIO_MODULO_EMPRESA " +
								 "WHERE CD_USUARIO = " + cdUsuario +
								 "  AND CD_SISTEMA = " + cdSistema +
								 "  AND CD_MODULO  = " + cdModulo);
			if(pstmt.executeUpdate() < 0){
				Conexao.rollback(connect);
				return -1;
			}
			
			pstmt = connect.prepareStatement(
					                 "DELETE FROM SEG_USUARIO_MODULO " +
									 "WHERE CD_USUARIO = " + cdUsuario +
									 "  AND CD_SISTEMA = " + cdSistema +
									 "  AND CD_MODULO  = " + cdModulo);
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setNovaSenha(String nmLogin, String nmSenha, String nmNovaSenha) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
		try {
			pstmt = connect.prepareStatement("SELECT cd_usuario FROM seg_usuario A " +
											 "WHERE A.NM_LOGIN = ? " +
											 "  AND A.NM_SENHA = ?");
			pstmt.setString(1, nmLogin);
			//TODO Remover teste de loginHash após atualização do banco
			if( lgLoginHash > 0  )
				pstmt.setString(2, getPasswordHash(nmSenha));
			else
				pstmt.setString(2, nmSenha);
			rs = pstmt.executeQuery();
			if(rs.next())	{
				int cdUsuario = rs.getInt("cd_usuario");
				pstmt = connect.prepareStatement("UPDATE seg_usuario SET nm_senha = ? " +
						                         "WHERE cd_usuario = "+cdUsuario);
				
				//TODO Remover teste de loginHash após atualização do banco
				if( lgLoginHash > 0  )
					pstmt.setString(1, getPasswordHash(nmNovaSenha));
				else
					pstmt.setString(1, nmNovaSenha);
				return pstmt.executeUpdate();
			}
			else
				return -10;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.setNovaSenha: " + e);
			return -2;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {		
		return getAll(-1, null);
	}

	public static ResultSetMap getAll(int stUsuario) {
		return getAll(stUsuario, null);
	}

	public static ResultSetMap getAll(boolean lgAtivos) {
		return getAll(ST_ATIVO, null);
	}

	public static ResultSetMap getAll(int stUsuario, Connection connect) {		
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

      String strBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA");
			
			boolean lgBaseAntiga = strBaseAntiga != null ? strBaseAntiga.equals("1") : false;
			
			String sql = "SELECT A.*, B.nm_pessoa, B.nm_pessoa AS nm_usuario, C.* " +
					"FROM seg_usuario A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
					"LEFT OUTER JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento) " +
					(stUsuario==-1 ? "" : "WHERE A.st_usuario = " + stUsuario) +
					" ORDER BY nm_usuario";
		
			if(lgBaseAntiga)
				sql = "SELECT A.*, A.cod_usuario as cd_usuario, A.nm_usuario as nm_pessoa, A.nr_nivel as tp_usuario, A.nm_nick as nm_login, B.*  " +
						"FROM usuario A "+
						"LEFT OUTER JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) " +
						(stUsuario==-1 ? "" : "WHERE A.st_usuario = " + stUsuario) +
						" ORDER BY nm_usuario";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! UserServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario) {
		return delete(cdUsuario, null);
	}

	public static int delete(int cdUsuario, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			if(lgBaseAntiga) {
				
				PreparedStatement pstmt = connection.prepareStatement("DELETE FROM usuario WHERE cod_usuario=?");
				pstmt.setInt(1, cdUsuario);
				
				int returnCode = pstmt.executeUpdate();
				
				if (returnCode <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				if (isConnectionNull)
					connection.commit();
			}
			else {
				
				connection.prepareStatement("DELETE FROM agd_usuario WHERE cd_usuario = "+cdUsuario).executeUpdate();
	
				connection.prepareStatement("DELETE FROM seg_usuario_grupo WHERE cd_usuario = "+cdUsuario).executeUpdate();
	
				connection.prepareStatement("DELETE FROM seg_usuario_permissao_acao WHERE cd_usuario = "+cdUsuario).executeUpdate();
	
				connection.prepareStatement("DELETE FROM seg_usuario_permissao_objeto WHERE cd_usuario = "+cdUsuario).executeUpdate();
	
				connection.prepareStatement("DELETE FROM seg_usuario_modulo_empresa WHERE cd_usuario = "+cdUsuario).executeUpdate();
				
				connection.prepareStatement("DELETE FROM seg_usuario_modulo WHERE cd_usuario = "+cdUsuario).executeUpdate();
	
				connection.prepareStatement("DELETE FROM seg_usuario_empresa WHERE cd_usuario = "+cdUsuario).executeUpdate();
	
				connection.prepareStatement("DELETE FROM log_execucao_acao " +
						                    "WHERE cd_execucao IN (SELECT cd_log FROM log_sistema WHERE cd_usuario = "+cdUsuario+")").execute();
	
				connection.prepareStatement("DELETE FROM log_acesso_objeto " +
						                    "WHERE cd_acesso IN (SELECT cd_log FROM log_sistema WHERE cd_usuario = "+cdUsuario+")").execute();
	
				connection.prepareStatement("DELETE FROM log_sistema WHERE cd_usuario = "+cdUsuario).execute();
				
				
				if (UsuarioDAO.delete(cdUsuario, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

				if (isConnectionNull)
					connection.commit();
			}

			
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * @category SICOE
	 * @param cdUsuario
	 * @param nmCadastro
	 * @param nmAtividade
	 * @return
	 */
	public static boolean hasPermissao(int cdUsuario, String nmCadastro, String nmAtividade) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_atividade A, seg_cadastro B, seg_atividade C "+
		                                     "WHERE A.cd_atividade = C.cd_atividade "+
		                                     "  AND A.cd_cadastro  = C.cd_cadastro "+
		                                     "  AND A.cd_sistema   = C.cd_sistema " +
		                                     "  AND A.cd_sistema   = B.cd_sistema " +
		                                     "  AND A.cd_cadastro  = B.cd_cadastro " +
		                                     "  AND id_cadastro  = \'"+nmCadastro+"\'"+
		                                     "  AND id_atividade = \'"+nmAtividade+"\'"+
		                                     "  AND A.CD_USUARIO   = "+cdUsuario);
			return pstmt.executeQuery().next();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.hasPermissao: " + e);
			return false;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int insertEmpresaToUsuario(int cdUsuario, int cdEmpresa, GregorianCalendar hrInicial, GregorianCalendar hrFinal) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if(hrInicial!=null && hrInicial.get(Calendar.YEAR)<1950)
				hrInicial.set(Calendar.YEAR, 1950);
			if(hrFinal!=null && hrFinal.get(Calendar.YEAR)<1950)
				hrFinal.set(Calendar.YEAR, 1950);
			int code = Conexao.getSequenceCode("SEG_USUARIO_EMPRESA");
			pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_EMPRESA "+
		                                     "(CD_USUARIO, CD_EMPRESA, NR_HORARIO, HR_INICIAL, HR_FINAL) "+
		                                     "VALUES (?,?,?,?,?)");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, code);
			if(hrInicial==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4, new Timestamp(hrInicial.getTimeInMillis()));
			if(hrFinal==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
			pstmt.setTimestamp(5, new Timestamp(hrFinal.getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.insertEmpresaToUsuario: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int insertAtividadeToUsuario(int cdUsuario, int cdAtividade, int cdCadastro, int cdSistema) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("INSERT INTO SEG_USUARIO_ATIVIDADE "+
		                                     "(CD_USUARIO, CD_ATIVIDADE,CD_CADASTRO,CD_SISTEMA) VALUES (?,?,?,?)");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdAtividade);
			pstmt.setInt(3, cdCadastro);
			pstmt.setInt(4, cdSistema);
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.insertAtividadeToUsuario: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteAtividadeOfUsuario(int cdUsuario, int cdAtividade, int cdCadastro, int cdSistema) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SEG_USUARIO_ATIVIDADE "+
		                                     "WHERE CD_ATIVIDADE = "+cdAtividade+
		                                     "  AND CD_CADASTRO  = "+cdCadastro+
		                                     "  AND CD_SISTEMA   = "+cdSistema+
		                                     "  AND CD_USUARIO   = "+cdUsuario);
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.deleteAtividadeOfUsuario: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteEmpresaOfUsuario(int cdUsuario, int cdEmpresa, int nrHorario) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SEG_USUARIO_EMPRESA "+
		                                     "WHERE CD_EMPRESA = ? "+
		                                     "  AND CD_USUARIO = ? "+
		                                     "  AND NR_HORARIO = ?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, nrHorario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.deleteEmpresaOfUsuario: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Usuario getByPessoa(int cdPessoa) {
		return getByPessoa(cdPessoa, null);
	}

	public static Usuario getByPessoa(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Usuario(rs.getInt("cd_usuario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_pergunta_secreta"),
						rs.getString("nm_login"),
						rs.getString("nm_senha"),
						rs.getInt("tp_usuario"),
						rs.getString("nm_resposta_secreta"),
						rs.getInt("st_usuario"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getByPessoa: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAtendente(int cdUsuario) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.NM_PESSOA AS NM_USUARIO "+
  					 "FROM seg_usuario A, grl_pessoa B, seg_usuario_empresa C "+
  					 "WHERE A.cd_usuario = B.cd_pessoa "+
  					 "  AND A.cd_usuario = C.cd_usuario "+
  					 "  AND C.cd_empresa = ? "+
  					 "  AND A.TP_USUARIO <> 3");
			pstmt.setInt(1, cdUsuario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.getAtendente: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getUsuarioOfEmpresa(int cdEmpresa) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.NM_PESSOA AS NM_USUARIO "+
		                   					 "FROM seg_usuario A, grl_pessoa B, seg_usuario_empresa C "+
		                   					 "WHERE A.cd_usuario = B.cd_pessoa "+
		                   					 "  AND A.cd_usuario = C.cd_usuario "+
		                   					 "  AND C.cd_empresa = ?");
			pstmt.setInt(1, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getUsuarioOfEmpresa: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getEmpresaOfUsuario(int cdUsuario) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.nm_pessoa AS nm_fantasia, C.nm_pessoa AS nm_empresa "+
		                   					 "FROM seg_usuario_empresa A " +
		                   					 "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) "+
		                   					 "JOIN grl_pessoa  C ON (A.cd_empresa = C.cd_pessoa) "+
		                   					 "WHERE A.cd_empresa = "+cdUsuario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getUsuarioOfEmpresa: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getEmpresaOfUsuarioModulo(int cdUsuario, int cdSistema, int cdModulo) {
		return getEmpresaOfUsuarioModulo(cdUsuario, cdSistema, cdModulo, true);
	}
	public static ResultSetMap getEmpresaOfUsuarioModulo(int cdUsuario, int cdSistema, int cdModulo, boolean showAllEmpresa) {
		return getEmpresaOfUsuarioModulo(cdUsuario, cdSistema, cdModulo, showAllEmpresa, null);
	}
	public static ResultSetMap getEmpresaOfUsuarioModulo(int cdUsuario, int cdSistema, int cdModulo, boolean showAllEmpresa, Connection connect) {
		return getEmpresaOfUsuarioModulo(cdUsuario, cdSistema, cdModulo, showAllEmpresa, 0, null, 0, null);
	}
	public static ResultSetMap getEmpresaOfUsuarioModulo(int cdUsuario, int cdSistema, int cdModulo, boolean showAllEmpresa, int cdEmpresa, String idModulo, int tpModulo) {
		return getEmpresaOfUsuarioModulo(cdUsuario, cdSistema, cdModulo, showAllEmpresa, cdEmpresa, idModulo, tpModulo, null);
	}
	public static ResultSetMap getEmpresaOfUsuarioModulo(int cdUsuario, int cdSistema, int cdModulo, boolean showAllEmpresa, int cdEmpresa, String idModulo, int tpModulo, Connection connect) {
		boolean isConnNull = connect==null;
		try {
			if(isConnNull)
				connect = Conexao.conectar();
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoSecretaria = 0;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoServices.getPeriodoLetivoRecente(cdSecretaria);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			
			
			PreparedStatement pstmt = connect.prepareStatement(
					      "SELECT A.*, B.*, B.nm_pessoa AS nm_fantasia, C.cd_usuario, C.cd_sistema, C.cd_modulo, C.cd_usuario AS lg_empresa "+
							(idModulo != null && (idModulo.equals("acd") || idModulo.equals("ped")) ? " , INS.* " : "") +
		                  "FROM grl_empresa A " +
		                  "JOIN grl_pessoa  B ON (A.cd_empresa = B.cd_pessoa AND B.st_cadastro = "+PessoaServices.ST_ATIVO + ")" +
		                  (showAllEmpresa ? "LEFT OUTER " : "")+
		                  " JOIN seg_usuario_modulo_empresa C ON (C.cd_empresa = A.cd_empresa " +
		                  "                                   AND C.cd_usuario = " +cdUsuario+
		                  "                                   AND C.cd_modulo  = " +cdModulo+
		                  "                                   AND C.cd_sistema = " +cdSistema+") " +
		                  (idModulo != null && (idModulo.equals("acd") || idModulo.equals("ped")) ? " JOIN acd_instituicao INS ON (A.cd_empresa = INS.cd_instituicao) " +
		                  		(cdEmpresa != cdSecretaria ? "										  JOIN acd_instituicao_periodo INS_P ON (A.cd_empresa = INS_P.cd_instituicao AND INS_P.cd_periodo_letivo_superior = "+cdPeriodoSecretaria+") "
		                  		+ "																	  JOIN acd_instituicao_educacenso INS_E ON (A.cd_empresa = INS_E.cd_instituicao AND INS_E.cd_periodo_letivo = INS_P.cd_periodo_letivo) " : "") : "") +
		                  (idModulo != null && (idModulo.equals("acd") || idModulo.equals("ped")) && tpModulo == MODULO_ESCOLA ? 
		                		  " WHERE A.cd_empresa <> " + cdEmpresa + (cdEmpresa != cdSecretaria ? " AND st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE : "") : 
		                  (idModulo != null && (idModulo.equals("acd") || idModulo.equals("ped")) && (tpModulo == MODULO_SECRETARIA || tpModulo == MODULO_CAE 
		                  													|| tpModulo == MODULO_PED || tpModulo == MODULO_TRANSPORTE
		                  													|| tpModulo == MODULO_FINANCEIRO_SECRETARIA) ? 
		                		  " WHERE A.cd_empresa = " + cdEmpresa + (cdEmpresa != cdSecretaria ? " AND st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE : "") : "")) +
		                  " ORDER BY B.nm_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getUsuarioOfEmpresa: " + e);
			return null;
		}
		finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	public static int insertUsuarioModuloEmpresa(int cdUsuario, int cdModulo, int cdSistema, int cdEmpresa){
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					     "SELECT * FROM seg_usuario_modulo_empresa "+
						 "WHERE cd_usuario = " +cdUsuario+
						 "  AND cd_sistema = " +cdSistema+
						 "  AND cd_modulo  = " +cdModulo+
						 "  AND cd_empresa = "+cdEmpresa);
			if(!pstmt.executeQuery().next()) {
				pstmt = connect.prepareStatement("INSERT INTO seg_usuario_modulo_empresa (CD_USUARIO, CD_SISTEMA, CD_MODULO, CD_EMPRESA) VALUES(?, ?, ?, ?)");
				pstmt.setInt(1, cdUsuario);
				pstmt.setInt(2, cdSistema);
				pstmt.setInt(3, cdModulo);
				pstmt.setInt(4, cdEmpresa);
				pstmt.executeUpdate();
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteUsuarioModuloEmpresa(int cdUsuario, int cdModulo, int cdSistema, int cdEmpresa){
		Connection connect = Conexao.conectar();
		try {
			return connect.prepareStatement("DELETE FROM seg_usuario_modulo_empresa "+
											"WHERE cd_usuario = " +cdUsuario+
											"  AND cd_sistema = " +cdSistema+
											"  AND cd_modulo  = " +cdModulo+
											"  AND cd_empresa = "+cdEmpresa).executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getModulosByUsuario(int cdUsuario) {
		return getModulosByUsuario(cdUsuario, false);
	}
	public static ResultSetMap getModulosByUsuario(int cdUsuario, boolean carregaTodos) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_usuario, B.lg_natureza, 0 AS lg_natureza_grupo "+
		                   					 "FROM seg_modulo A " +
		                   					 "LEFT OUTER JOIN seg_usuario_modulo B ON (B.cd_modulo  = A.cd_modulo" +
		                   					 "                                     AND B.cd_usuario = "+cdUsuario+") " +
		                   					 "WHERE A.lg_ativo = 1");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getUsuarioOfEmpresa: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Usuario getBaseAntiga(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM usuario WHERE cod_usuario=?");
			pstmt.setInt(1, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Usuario(rs.getInt("cod_usuario"),
						0,
						0,
						rs.getString("nm_nick"),
						rs.getString("nm_senha"),
						rs.getInt("nr_nivel"),
						null,
						rs.getInt("st_usuario"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Usuario getByNmLogin(String nmLogin, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA")!=null && 
					   Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");


			String sql = "SELECT * FROM seg_usuario WHERE nm_login=?";
			
			if(lgBaseAntiga)
				sql = "SELECT * FROM usuario WHERE nm_nick=?";

			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nmLogin);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga)
					return new Usuario(rs.getInt("cod_usuario"),
							0,
							0,
							rs.getString("nm_nick"),
							rs.getString("nm_senha"),
							rs.getInt("nr_nivel"),
							null,
							rs.getInt("st_usuario"));
				else
					return new Usuario(rs.getInt("cd_usuario"),
							rs.getInt("cd_pessoa"),
							rs.getInt("cd_pergunta_secreta"),
							rs.getString("nm_login"),
							rs.getString("nm_senha"),
							rs.getInt("tp_usuario"),
							rs.getString("nm_resposta_secreta"),
							rs.getInt("st_usuario"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result syncBaseAntiga() {
		return syncBaseAntiga(null);
	}
	
	public static Result syncBaseAntiga(Connection connect) {
		boolean isConnectionNull = connect==null;
		
		boolean syncBaseAntiga = Util.getConfManager().getProps().getProperty("SYNC_BASE_ANTIGA")!= null && 
								 Util.getConfManager().getProps().getProperty("SYNC_BASE_ANTIGA").equals("1");
		boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA")!=null && 
							   Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		
		if(!syncBaseAntiga)
			return new Result(2, "Serviço de sincronia desligado.");
		
		if(!lgBaseAntiga)
			return new Result(3, "Utilizando a base nova. Não necessita sincronia.");
		
		try {
			System.out.println("Sincronizando usuários da base antiga");
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmUsuarios = getAll(-1, connect);
			
			
			int retorno = 0;
				
			int qtTotalUsuarios = rsmUsuarios.size();
			int qtTotalInseridos = 0;
			int qtTotalAtualizados = 0;
			
			while(rsmUsuarios.next()) {
				
				Usuario uNovo = getByNmLogin(rsmUsuarios.getString("NM_NICK"), connect);
				Usuario uAntigo = getBaseAntiga(rsmUsuarios.getInt("COD_USUARIO"), connect);
				
				Result r;
				if(uNovo==null) { //inserir?
					//uAntigo.setCdUsuario(0);
					r = new Result(UsuarioDAO.insert(uAntigo, connect));
					qtTotalInseridos++;
				}
				else {
					uAntigo.setCdUsuario(uNovo.getCdUsuario());
					r = save(uAntigo, false, connect);
					qtTotalAtualizados++;
				}
				
				if(r.getCode() <= 0){
					Conexao.rollback(connect);
					System.out.println("Erro ao sincronizar usuário '"+rsmUsuarios.getString("NM_NICK")+"'!");
					return new Result(-2, "Erro ao sincronizar usuário '"+rsmUsuarios.getString("NM_NICK")+"'!");
				}
				
				retorno = r.getCode();
			}
			
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-3, "Erro ao sincronizar usuários!");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			System.out.println("usuários da base antiga sincronizados com sucesso!");
			System.out.println("  TOTAL: "+qtTotalUsuarios);
			System.out.println("  Inseridos: "+qtTotalInseridos);
			System.out.println("  Atualizados: "+qtTotalAtualizados);
			
			return new Result(1, "usuários da base antiga sincronizados com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar usuários da base antiga!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * @see #removeByConta(int, Connection)
	 */
	public static Result removeByConta(int cdConta) {
		return removeByConta(cdConta, null);
	}

	/**
	 * Método que remove um usuário de uma conta pela chave primária da conta
	 * 
	 * @author Luiz Romario Filho
	 * @param cdConta id da conta que precisa remover o usuário
	 * @since 28/08/2014
	 * @return result com código e mensagem da transação, code >= 0 ? sucesso : erro 
	 * 
	 */
	public static Result removeByConta(int cdConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(connect.prepareStatement("DELETE FROM seg_usuario_conta_financeira WHERE cd_conta="+cdConta).execute()) {
				retorno = 1;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este usuário está vinculado a outros registros e não pode ser excluído!");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "usuário excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir usuário!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para alterar a senha de um usuário
	 * @author Edgard Hufelande
	 * @param cdUsuario - código de indentificação da conta na tabela.
	 * @param nmSenhaAtual - A senha atualmente usada pelo usuário.
	 * @param nmNovaSenha - A nova senha na qual será aplicada na tabela.
	 * @param nmConfirmarSenha - Confirmação da nova senha, validar para não haver problemas para o usuário futuramente.
	 * @return Result - Resultado final do procedimento do método.
	 */
	public static Result validateSenha(int cdUsuario, String nmSenhaAtual, String nmNovaSenha, String nmConfirmarSenha){
		return validateSenha(cdUsuario, nmSenhaAtual, nmNovaSenha, nmConfirmarSenha, null);
	}
	
	public static Result validateSenha(int cdUsuario, String nmSenhaAtual, String nmNovaSenha, String nmConfirmarSenha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}		
			/* Consulta para o Firebird */ 
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM seg_usuario A WHERE CD_USUARIO = ?");
			pstmt.setInt(1, cdUsuario);			
			ResultSetMap rs = new ResultSetMap(pstmt.executeQuery());
			//TODO Remover teste de loginHash após atualização do banco
			int lgLoginHash = ParametroServices.getValorOfParametroAsInteger("LG_LOGIN_HASH", 0, 0, connect);
			while(rs.next()){
				if (rs.getString("NM_SENHA") != null && !rs.getString("NM_SENHA").equals(getPasswordHash(nmSenhaAtual)) && !rs.getString("NM_SENHA").equals(nmSenhaAtual)){
					return new Result(-1, "Informe a senha atual corretamente.");
				} else {
					PreparedStatement pstmt1 = connect.prepareStatement("UPDATE seg_usuario SET NM_SENHA = ? WHERE CD_USUARIO = ?");
					pstmt1.setString(1, (lgLoginHash>0?getPasswordHash(nmNovaSenha):nmNovaSenha));
					pstmt1.setInt(2, cdUsuario);
					pstmt1.execute();
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "A senha do usuário foi alterada com sucesso!", "NM_NOVA_SENHA", (lgLoginHash>0?getPasswordHash(nmNovaSenha):nmNovaSenha));
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
	
	/**
	 * Busca o usuário do sistema da pessoa
	 * 
	 * @param cdPessoa código da pessoa
	 * @return Result com objeto USUARIO
	 */
	public static Result getUsuarioByPessoa(int cdPessoa){
		return getUsuarioByPessoa(cdPessoa, null);
	}
	
	public static Result getUsuarioByPessoa(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}		
			
			Usuario usuario = null;
			Result result = null;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM seg_usuario WHERE cd_pessoa = " + cdPessoa
			);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				usuario = new Usuario(rs.getInt("cd_usuario"),
									rs.getInt("cd_pessoa"),
									rs.getInt("cd_pergunta_secreta"),
									rs.getString("nm_login"),
									rs.getString("nm_senha"),
									rs.getInt("tp_usuario"),
									rs.getString("nm_resposta_secreta"),
									rs.getInt("st_usuario"));
			
				result = new Result(1, "", "USUARIO", usuario);
			
			}
			else {
				result = new Result(-2, "Nenhum usuário encontrado para esta pessoa.");
			}
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}	
	}
	
	
	public static Result getUsuarioByLogin(String nmLogin){
		return getUsuarioByLogin(nmLogin, null);
	}
	
	public static Result getUsuarioByLogin(String nmLogin, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}		
			
			Usuario usuario = null;
			Result result = null;
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM seg_usuario WHERE nm_login = '" + nmLogin + "'"
			);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				usuario = new Usuario(rs.getInt("cd_usuario"),
									rs.getInt("cd_pessoa"),
									rs.getInt("cd_pergunta_secreta"),
									rs.getString("nm_login"),
									rs.getString("nm_senha"),
									rs.getInt("tp_usuario"),
									rs.getString("nm_resposta_secreta"),
									rs.getInt("st_usuario"));
			
				result = new Result(1, "", "USUARIO", usuario);
			
			}
			else {
				result = new Result(-2, "Nenhum usuário encontrado para esta pessoa.");
			}
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}	
	}
	
	
	public static boolean isAdmSecretariaEducacao(int cdUsuario) {
		return isAdmSecretariaEducacao(cdUsuario, null);
	}
	public static boolean isAdmSecretariaEducacao(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM seg_usuario_modulo_empresa WHERE cd_empresa = 2 AND cd_modulo = 9 AND cd_usuario = " + cdUsuario).executeQuery());
			return rsm.next();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean isSuporte(int cdUsuario) {
		return isSuporte(cdUsuario, null);
	}
	public static boolean isSuporte(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			return ParametroServices.getValorOfParametroAsInteger("CD_PESSOA_SUPORTE", 0) == UsuarioDAO.get(cdUsuario, connect).getCdPessoa();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}		
	
	public static int retirarEquipamento(int cdUsuario){
		return retirarEquipamento(cdUsuario,null);	
	}
	
	public static int retirarEquipamento(int cdUsuario, Connection connect ){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			String sql = " UPDATE seg_usuario SET st_login=null, cd_equipamento=null " +
                    " WHERE cd_usuario = ? ";
		
			if(lgBaseAntiga) {
				sql = " UPDATE usuario SET st_login=null, cd_equipamento=null " +
                        " WHERE cod_usuario = ? ";
			}
			
			PreparedStatement pstmt = connect.prepareStatement(sql);			
			pstmt.setInt(1, cdUsuario);
						
			int retornoQuery = pstmt.executeUpdate();
			connect.commit();
			
			return retornoQuery;
			
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Usuario getByCdUsuario(int cdUsuario) {
		return getByCdUsuario(cdUsuario, null);
	}
	
	public static Usuario getByCdUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_USUARIO WHERE CD_USUARIO = ?");
			pstmt.setInt(1, cdUsuario);
			
			ResultSetMap res = new ResultSetMap(pstmt.executeQuery());
			
			Usuario usuario;
			
			if(res.next()) {
				usuario = new Usuario (
						res.getInt("CD_USUARIO"),
						res.getInt("CD_PESSOA"),
						res.getInt("CD_PERGUNTA_SECRETA"),
						res.getString("NM_LOGIN"),
						res.getString("NM_SENHA"),
						res.getInt("TP_USUARIO"),
						res.getString("NM_RESPOSTA_SECRETA"),
						res.getInt("ST_USUARIO")
						);
			}else {
				usuario = new Usuario();
			}
			
			return usuario;
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertDTO(UsuarioPessoaDTO _dto) {
		try {
			PessoaFisica pessoaFisica = new PessoaFisica();
			
			pessoaFisica.setNrCpf(_dto.getPessoaFisica().getNrCpf());
			pessoaFisica.setNmPessoa(_dto.getPessoa().getNmPessoa());
			pessoaFisica.setStCadastro(PessoaServices.ST_ATIVO);
			pessoaFisica.setDtCadastro(Util.getDataAtual());
			pessoaFisica.setGnPessoa(PessoaServices.TP_FISICA);
			pessoaFisica.setNmPessoa(_dto.getPessoa().getNmPessoa());
			pessoaFisica.setNmEmail(_dto.getPessoa().getNmEmail());	
			pessoaFisica.setNmApelido(_dto.getPessoa().getNmApelido());
			pessoaFisica.setNrCelular(_dto.getPessoa().getNrCelular());
			
			//PESSOA
			Result nResult = PessoaServices.save(_dto.getPessoa(), null, 0, _dto.getVinculo().getCdVinculo());
			
			if(nResult.getCode()  < 0)
				return nResult;
			
			_dto.setPessoa((Pessoa)nResult.getObjects().get("PESSOA"));
			
			//PESSOA FISICA    
			pessoaFisica.setCdPessoa(_dto.getPessoa().getCdPessoa());
			PessoaFisicaServices.save(pessoaFisica);
			
			//USUARIO
			_dto.getUsuario().setCdPessoa(_dto.getPessoa().getCdPessoa());

			//PESSOA EMPRESA
			Empresa _empresa = EmpresaServices.getEmpresaById("JARI");
			
			PessoaEmpresa _pessoaEmpresa = new PessoaEmpresa();
			_pessoaEmpresa.setCdVinculo(_dto.getVinculo().getCdVinculo());
			_pessoaEmpresa.setCdPessoa(_dto.getPessoa().getCdPessoa());
			_pessoaEmpresa.setCdEmpresa(_empresa.getCdEmpresa());
			_pessoaEmpresa.setDtVinculo(Util.getDataAtual());
			
			Result _result = PessoaEmpresaServices.save(_pessoaEmpresa);
			if(_result.getCode() < 0)
				return _result;
			
			//USUARIO
			Usuario res = UsuarioServices.getByNmLogin(_dto.getUsuario().getNmLogin(), null);
			
			Result result = UsuarioServices.save(_dto.getUsuario());
			if(result.getCode() < 0)
				return result;
			
			_dto.setUsuario((Usuario)result.getObjects().get("USUARIO"));
			
			Result successResult = new Result(0);
			successResult.setMessage("Usuário salvo com sucesso.");
			successResult.getObjects().put("DTO", _dto);

			return successResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static ResultSetMap findDto(ArrayList<ItemComparator> criterios, Connection connect) {
		if(connect == null)
			connect = Conexao.conectar();
		
		try {
			
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			criterios.add(new ItemComparator("A.st_usuario", Integer.toString(UsuarioServices.ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			String nmPessoa = "";
			int limit = -1;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("B.NM_PESSOA") && !lgBaseAntiga) {
					nmPessoa =	Util.limparTexto(criterios.get(i).getValue());
					nmPessoa = criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("limit") && !lgBaseAntiga) {
					limit = Integer.parseInt(criterios.get(i).getValue().toString());
					criterios.remove(i);
					i--;
				}
			}
			String sql = "SELECT B.*, A.*, B.nm_pessoa AS nm_usuario, C.*, D.*, E.cd_vinculo " +
					"FROM seg_usuario A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa) " +
					"LEFT OUTER JOIN grl_equipamento C ON (A.cd_equipamento = C.cd_equipamento) " +
					"LEFT OUTER JOIN grl_pessoa_fisica D ON (B.cd_pessoa = D.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_empresa E ON (A.cd_pessoa = E.cd_pessoa) " +
					"WHERE 1=1";
		
			if(lgBaseAntiga) {
			
				for (ItemComparator itemComparator : criterios) {
					if(itemComparator.getColumn().equalsIgnoreCase("A.NM_LOGIN"))
						itemComparator.setColumn("A.NM_NICK");
					
					if(itemComparator.getColumn().equalsIgnoreCase("B.NM_PESSOA"))
						itemComparator.setColumn("A.NM_USUARIO");
					
					if(itemComparator.getColumn().equalsIgnoreCase("A.TP_USUARIO"))
						itemComparator.setColumn("A.NR_NIVEL");
					
				}
				
				sql = "SELECT A.*, A.cod_usuario as cd_usuario, A.nm_usuario as nm_pessoa, A.nr_nivel as tp_usuario, A.nm_nick as nm_login, B.*  " +
						"FROM usuario A "+
						"LEFT OUTER JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) ";
			}
			
			String strLimit = (limit > 0 ? " LIMIT "+limit : "");

			return Search.find(sql, " ORDER BY B.nm_pessoa "+strLimit, criterios, Conexao.conectar(), true);
		}
		catch(Exception e) {
			Util.registerLog(e);
			return null;
		}
	}
	
	public static ResultSetMap getAllByVinculo(String nmVinculo) {
		return getAllByVinculo(nmVinculo, null);
	}
	
	public static ResultSetMap getAllByVinculo(String nmVinculo, Connection conn) {
		 boolean isConnNull = (conn == null);
		 int _paramRelator = ParametroServices.getValorOfParametroAsInteger(nmVinculo, 0);
		 
		 if(_paramRelator < 0)
			 return null;
		 
		 try {
			 if(isConnNull) 
				 conn = Conexao.conectar();
			 
			 PreparedStatement pstmt = conn.prepareStatement(
					"SELECT A.*, C.* FROM GRL_PESSOA A " + 
			 		"JOIN GRL_PESSOA_EMPRESA B ON (A.CD_PESSOA = B.CD_PESSOA) " + 
			 		"JOIN SEG_USUARIO C ON (A.CD_PESSOA = C.CD_PESSOA) " + 
			 		"WHERE B.CD_VINCULO = ?");
			 
			 pstmt.setInt(1, _paramRelator);
			 
			 return new ResultSetMap(pstmt.executeQuery());
		 } catch(Exception e) {
			 e.printStackTrace(System.out);
			 return null;
		 } finally {
			 if(isConnNull)
				 Conexao.desconectar(conn);
		 }
	}
	private static String getEquipamentoByCodigo(int cdEquipamento, CustomConnection customConnection) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("cd_equipamento", cdEquipamento);
	    com.tivic.sol.search.Search<Equipamento> search = new SearchBuilder<Equipamento>("grl_equipamento")
	            .fields("nm_equipamento")
	            .searchCriterios(searchCriterios)
	            .customConnection(customConnection)
	            .build();
	    List<Equipamento> equipamentos = search.getList(Equipamento.class);
	    if (!equipamentos.isEmpty()) {
	        return equipamentos.get(0).getNmEquipamento();
	    } else {
	        throw new NoSuchElementException("Nenhum equipamento encontrado para o código: " + cdEquipamento);
	    }
	}

}	

