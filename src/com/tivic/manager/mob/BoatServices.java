package com.tivic.manager.mob;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.CategoriaVeiculoDAO;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoDAO;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoDAO;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.boat.enums.TipoAcidenteBoatEnum;
import com.tivic.manager.mob.boat.validations.SaveDeclaracaoValidations;
import com.tivic.manager.mob.edat.notificacao.NotificacaoDeclarante;
import com.tivic.manager.mob.orgao.OrgaoRepository;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.str.endereco.Endereco;
import com.tivic.manager.str.endereco.EnderecoBuilder;
import com.tivic.manager.str.endereco.EnderecoDAO;
import com.tivic.manager.str.endereco.enderecoboat.EnderecoBoat;
import com.tivic.manager.str.endereco.enderecoboat.EnderecoBoatDAO;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.SecretServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.mail.Mailer;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.ReportServices;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BoatServices {
	
	public static final int ST_INDEFERIDA  = 0;
	public static final int ST_EM_ANALISE  = 1;
	public static final int ST_DEFERIDA    = 2;
	public static final int ST_PENDENTE    = 3;
	public static final int DEFAULT_VALUE_CD_ORGAO = 0;
	
	public static final int TP_ACIDENTE_OUTRO = 4;
	
	public static final String[] stBoat = {"Indeferida", "Em análise", "Deferida", "Pendente"};
	
	public static final String[] tpAcidente = {"Colisão Lateral", "Colisão Frontal", "Colisão Transversal", "Choque", 
			"Outro", "Atropelamento de Animal", "Saída de Pista", "Colisão com objeto móvel", "Colisão com objeto estático", 
			"Engavetamento", "Tombamento", "Derramamento de carga"};
	
	public static final String[] tpCondicaoVia = {"Seca", "Molhada", "Oleosa", "Danificada", "Em Obra", "Outra"};

	public static final String[] stSinalizacaoHorizontal = {"Boa", "Prejudicada", "Incorreta", "Inexistente"};
	
	public static final String[] stSinalizacaoVertical = {"Boa", "Prejudicada", "Incorreta", "Inexistente"};

	public static final String[] stSemaforo = {"Funcionando", "Desligado", "Defeituoso", "Inexistente", "Intermitente"};

	public static final String[] tpCondicaoClima = {"Bom", "Neblina", "Chuva", "Outra"};

	public static final String[] tpTracadoVia = {"Reta", "Curva", "Cruzamento"};
	
	public static final String[] tpCaracteristica = {"Com Vítima", "Sem Vítima"};
	
	public static final String[] tpPavimento = {"Asfalto", "Concreto", "Terra", "Paralelepípedo", "Cascalho", "Outro"};
	
	public static final String[] tpClassificacao = {"Condutor", "Passageiro", "Pedestre Atingido"};

	
	public static Result sync(ArrayList<Boat> boats) {
		return sync(boats, null);
	}
	
	public static Result sync(ArrayList<Boat> boats, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
//			if(boats.size()>1) {
//				for (Boat boat : boats) {
//					for (Boat boatCheck : boats) {
//						if(boat!=boatCheck && boat.getNrBoat()==boatCheck.getNrBoat()) {
//							boats.remove(boat);
//							break;
//						}
//					}
//				}
//			}
			
			ArrayList<Boat> boatsRetorno = new ArrayList<Boat>();
			ArrayList<Boat> boatsDuplicadas = new ArrayList<Boat>();
			ArrayList<Boat> boatsErro = new ArrayList<Boat>();
			
			int retorno = 0;
			for (Boat boat: boats) {
				
				/* ******************************
				 * Força a inclusão do veículo
				 * TODO: tem que mudar isso daí
				 */
				for (BoatVeiculo veiculo : boat.getVeiculos()) {
					veiculo.setCdVeiculo(0);
				}
				
				Result r = sync(boat, connect);
				retorno = r.getCode();
				
				if(r.getCode()<=0) {
					boatsErro.add(boat);
					continue;
				}
				else if(r.getCode()==2) {
					boatsDuplicadas.add(boat);
					continue;
				}
				else {
					boat.setCdBoat(r.getCode());
					boatsRetorno.add(boat);
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (boats.size() == boatsRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar BOATs.");
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar BOATs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(Boat boat, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo BOAT...");
			System.out.println("\tNr. BOAT: "+boat.getNrBoat());
			System.out.println("\tAgente: "+AgenteDAO.get(boat.getCdAgente(), connect).getNmAgente());
			System.out.println("\tLocalizacao: ["+boat.getVlLatitude()+", "+boat.getVlLongitude()+"]");
			
			int retorno = 0;
			
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM MOB_BOAT WHERE NR_BOAT = " + boat.getNrBoat()
				).executeQuery();
				
			if(rs.next()) {
				retorno = 2;
				System.out.println("Diagnostico: BOAT Duplicada...");
			}
			else {
				int situacaoBoat = ParametroServices.getValorOfParametroAsInteger("BOAT_SITUACAO_SYNC", ST_EM_ANALISE);
				int cdCidadeOrgao = ParametroServices.getValorOfParametroAsInteger("mob_cd_cidade_dat", DEFAULT_VALUE_CD_ORGAO);
				boat.setCdBoat(0);//garantir insercao
				boat.setStBoat(situacaoBoat);
				if(cdCidadeOrgao == 0) {
					retorno = -1;	
					return new Result(retorno, "Erro ao inserir o cdCidade.");
				}
				boat.setCdCidade(cdCidadeOrgao);
				retorno = saveSync(boat, connect).getCode();
				if(retorno > 0) {
					System.out.println("Diagnostico: BOAT Recebida...");
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
			}
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar BOATs.");
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar BOATs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(Boat boat) throws Exception{
		return save(boat, null);
	}

	@SuppressWarnings("deprecation")
	public static Result save(Boat boat, Connection connect) throws Exception{
		boolean isConnectionNull = connect==null;
		CustomConnection customConnection = new CustomConnection();
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			customConnection.initConnection(true, connect);
			if(boat==null)
				return new Result(-1, "Erro ao salvar. Boat é nulo");

			if(boat.getCdCidade()<=0)
				boat.setCdCidade(0);
			
			if(boat.getNrProtocolo() == null || boat.getNrProtocolo().isEmpty())
				boat.setNrProtocolo(gerarNrProtocolo(boat, connect));	
			
			int retorno;
			if(boat.getCdBoat()==0){
				retorno = BoatDAO.insert(boat, connect);
				boat.setCdBoat(retorno);
				if(boat.getEndereco() != null && boat.getEndereco().getCdEndereco() > 0) {
					inserirEnderecoBoat(boat, connect);
				}
			}
			else {
				retorno = BoatDAO.update(boat, connect);
				if(boat.getEndereco() != null &&boat.getEndereco().getCdEndereco() > 0) {
					atualizarEnderecoBoat(boat, connect);	
				} else if(boat.getEndereco() != null && boat.getEndereco().getCdEndereco() > 0) {
					inserirEnderecoBoat(boat, connect);
				}
			}
			
			//INSERIR IMAGENS
			if(retorno>0 && boat.getImagens()!=null && boat.getImagens().size()>0) {
				retorno = BoatImagemServices.save(boat.getCdBoat(), boat.getImagens(), connect).getCode();
			}
			
			//INSERIR VEICULOS
			if(retorno>0 && boat.getVeiculos()!=null && boat.getVeiculos().size()>0) {
				retorno = BoatVeiculoServices.save(boat.getCdBoat(), boat.getVeiculos(), connect).getCode();
			}
			
			//INSERIR VITIMAS
			if(retorno>0 && boat.getVitimas()!=null && boat.getVitimas().size()>0) {
				retorno = BoatVitimaServices.save(boat.getCdBoat(), boat.getVitimas(), connect).getCode();
			}
			
			//INSERIR TESTEMUNHAS
			if(retorno>0 && boat.getTestemunhas()!=null && boat.getTestemunhas().size()>0) {
				retorno = BoatTestemunhaServices.save(boat.getCdBoat(), boat.getTestemunhas(), connect).getCode();
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOAT", boat);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull) 
				Conexao.desconectar(connect);				
		}
	}
	
	private static void inserirEnderecoBoat(Boat boat,  Connection connect) {
		Endereco endereco = setEndereco(boat);
		EnderecoDAO.insert(endereco, connect);
		EnderecoBoatDAO.insert(new EnderecoBoat(endereco.getCdEndereco(), boat.getCdBoat()), connect);
	}
	
	private static void atualizarEnderecoBoat(Boat boat,  Connection connect) {
		EnderecoDAO.update(setEndereco(boat), connect);
	}
	
	
	private static Endereco setEndereco(Boat boat) {
		return new EnderecoBuilder()
				.setCdEndereco(boat.getEndereco().getCdEndereco())
				.setCdCidade(boat.getEndereco().getCdCidade())
				.setDsLogradouro(boat.getEndereco().getDsLogradouro())
				.setNrCep(boat.getEndereco().getNrCep())
				.setNrEndereco(boat.getEndereco().getNrEndereco())
				.setDsComplemento(boat.getEndereco().getDsComplemento())
				.setNmBairro(boat.getEndereco().getNmBairro())
				.builder();
	}
	
	@Deprecated
	public static Result saveSync(Boat boat, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(boat==null)
				return new Result(-1, "Erro ao salvar. Boat é nulo");

			if(boat.getCdCidade()<=0)
				boat.setCdCidade(0);
			
			int retorno;
			if(boat.getCdBoat()==0){
				retorno = BoatDAO.insert(boat, connect);
				boat.setCdBoat(retorno);
			}
			else {
				retorno = BoatDAO.update(boat, connect);
			}
			
			//INSERIR IMAGENS
			if(retorno>0 && boat.getImagens()!=null && boat.getImagens().size()>0) {
				retorno = BoatImagemServices.save(boat.getCdBoat(), boat.getImagens(), connect).getCode();
			}
			
			//INSERIR VEICULOS
			if(retorno>0 && boat.getVeiculos()!=null && boat.getVeiculos().size()>0) {
				retorno = BoatVeiculoServices.saveSync(boat.getCdBoat(), boat.getVeiculos(), connect).getCode();
			}
			
			//INSERIR VITIMAS
			if(retorno>0 && boat.getVitimas()!=null && boat.getVitimas().size()>0) {
				retorno = BoatVitimaServices.save(boat.getCdBoat(), boat.getVitimas(), connect).getCode();
			}
			
			//INSERIR TESTEMUNHAS
			if(retorno>0 && boat.getTestemunhas()!=null && boat.getTestemunhas().size()>0) {
				retorno = BoatTestemunhaServices.save(boat.getCdBoat(), boat.getTestemunhas(), connect).getCode();
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOAT", boat);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveDAT(Boat boat, Declarante declarante, BoatDeclarante boatDeclarante, DeclaranteEndereco declaranteEndereco) {
		return saveDAT(boat, declarante, boatDeclarante, declaranteEndereco, null);
	}

	public static Result saveDAT(Boat boat, Declarante declarante, BoatDeclarante boatDeclarante, DeclaranteEndereco declaranteEndereco, Connection connection) {
		boolean isConnectionNull = connection == null;
		
		try {
			Mailer mailer = null;
			BoatOcorrencia boatOcorrencia = null;

			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			
			if (verificarStBoatJulgado(boat.getCdBoat(), connection)) {
				throw new Exception("Esse BOAT foi julgado e, portanto, não pode ser editado.");
			}
			 		
			new SaveDeclaracaoValidations().validate(boat, null);
			
			NotificacaoDeclarante notificacao = new NotificacaoDeclarante(boat);

			if(boat.getCdBoat() == 0) {				
				mailer = notificacao.enviarNotificacaoRegistroDeclaracao();				
				int nrBoat = getNrBoat(connection);		
				boat.setDtComunicacao(new GregorianCalendar());
				
				boatOcorrencia = new BoatOcorrencia(0, 0, 0, new GregorianCalendar(), "Registro da declaração.", 0);
				
				if(nrBoat>=0) {
					boat.setNrBoat(nrBoat);
				}				
			} else {
				boatOcorrencia = new BoatOcorrencia(0, 0, 0, new GregorianCalendar(), "Alterações foram realizadas na declaração.", 0);
			}
			
			boat.setStBoat(ST_EM_ANALISE);
			
			
			
			//BOAT
			Result result = save(boat, connection);
			
			if(boat.getNrProtocolo() == null || boat.getNrProtocolo().trim().equals("")) {
				boat.setNrProtocolo(gerarNrProtocolo(boat, connection));	
				BoatDAO.update(boat, connection);
			}
			
			if(result.getCode() <= 0) {
				if(isConnectionNull)
					connection.rollback();
				return result;
			}
			
			boat = (Boat)result.getObjects().get("BOAT");
			boatOcorrencia.setCdBoat(boat.getCdBoat());
			
			if(declarante.getNrCpf()!=null)
				declarante.setNrCpf(Util.limparFormatos(declarante.getNrCpf()));
			if(declarante.getNrCnh()!=null)
				declarante.setNrCnh(Util.limparFormatos(declarante.getNrCnh()));
			if(declarante.getNrTelefone()!=null)
				declarante.setNrTelefone(Util.limparFormatos(declarante.getNrTelefone()));
			if(declarante.getNrCelular()!=null)
				declarante.setNrCelular(Util.limparFormatos(declarante.getNrCelular()));
			
			result = DeclaranteServices.save(declarante, null, connection);
			if(result.getCode() <= 0) {
				if(isConnectionNull)
					connection.rollback();
				return result;
			}
			
			declarante = (Declarante)result.getObjects().get("DECLARANTE");
		
			//BOAT_DECLARANTE
			boatDeclarante.setCdBoat(boat.getCdBoat());
			boatDeclarante.setCdDeclarante(declarante.getCdDeclarante());
			result = BoatDeclaranteServices.save(boatDeclarante, null, connection);
			
			//DECLARANTE_ENDERECO
			declaranteEndereco.setCdDeclarante(declarante.getCdDeclarante());
			result = DeclaranteEnderecoServices.save(declaranteEndereco, null, connection);

			BoatOcorrenciaServices.save(boatOcorrencia, null, connection);

			if(result.getCode() <= 0) {
				if(isConnectionNull)
					connection.rollback();
				return result;
			}	
			
			if(isConnectionNull)
				connection.commit();
			
			if(mailer != null) {
				mailer = notificacao.enviarNotificacaoRegistroDeclaracao();	
				mailer.send();
			}
			
			result = new Result(1, "DAT gravado com sucesso.", "BOAT", boat);
			result.addObject("BOAT_DECLARANTE", boatDeclarante);
			result.addObject("DECLARANTE", declarante);
			result.addObject("DECLARANTE_ENDERECO", declaranteEndereco);
			result.addObject("AUTH_HASH", gerarHmac(boat));
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	private static boolean verificarStBoatJulgado(int cdBoat, Connection connection) throws SQLException {
	    boolean editavel = false;
	    String sql = "SELECT st_boat FROM mob_boat WHERE cd_boat = ?";
	    PreparedStatement preparedStatement = connection.prepareStatement(sql);
	    preparedStatement.setInt(1, cdBoat);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
	        editavel = resultSet.getInt("st_boat") == ST_DEFERIDA || 
	                resultSet.getInt("st_boat") == ST_INDEFERIDA;
	    }
	    return editavel;
	}

	
	public static Result remove(Boat boat) {
		return remove(boat.getCdBoat(), true);
	}
	public static Result remove(int cdBoat){
		return remove(cdBoat, false, null, null);
	}
	public static Result remove(int cdBoat, boolean cascade){
		return remove(cdBoat, cascade, null, null);
	}
	public static Result remove(int cdBoat, boolean cascade, AuthData authData){
		return remove(cdBoat, cascade, authData, null);
	}
	public static Result remove(int cdBoat, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				retorno = BoatVitimaServices.removeAll(cdBoat, connect).getCode();
				retorno = BoatTestemunhaServices.removeAll(cdBoat, connect).getCode();
				retorno = BoatImagemServices.removeAll(cdBoat, connect).getCode();
				retorno = BoatVeiculoServices.removeAll(cdBoat, connect).getCode();
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = BoatDAO.delete(cdBoat, connect);
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
	
	public static Result deleteCascade(int cdBoat) throws Exception {
		Connection connect = null;
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			List<BoatVitima> vitimas = searchBoatVitima(cdBoat).getList(BoatVitima.class);
			List<BoatTestemunha> testemunhas = searchBoatTestemunha(cdBoat).getList(BoatTestemunha.class);
			List<BoatImagem> imagens = searchBoatImagem(cdBoat).getList(BoatImagem.class);
			List<BoatVeiculo> veiculos = searchBoatVeiculo(cdBoat).getList(BoatVeiculo.class);
			
			if(!vitimas.isEmpty()) {
				for(BoatVitima vitima : vitimas) {
					retorno = BoatVitimaDAO.delete(vitima.getCdBoat(), vitima.getCdPessoa());
				}
			}
			if(!testemunhas.isEmpty()) {
				for(BoatTestemunha testemunha : testemunhas) {
					retorno = BoatTestemunhaDAO.delete(testemunha.getCdBoat(), testemunha.getCdPessoa());
				}
			}
			if(!imagens.isEmpty()) {
				for(BoatImagem imagem : imagens) {
					retorno = BoatImagemDAO.delete(imagem.getCdImagem(), imagem.getCdBoat());
				}
			}
			if(!veiculos.isEmpty()) {
				for(BoatVeiculo veiculo : veiculos) {
					retorno = BoatVeiculoDAO.delete(veiculo.getCdBoatVeiculo(), veiculo.getCdBoat());
				}
			}
			if(retorno >= 0)
				retorno = BoatDAO.delete(cdBoat, connect);
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
	
	private static com.tivic.sol.search.Search<BoatVitima> searchBoatVitima(int cdBoat) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_boat", cdBoat);
		com.tivic.sol.search.Search<BoatVitima> search = new SearchBuilder<BoatVitima>("mob_boat_vitima")
				.fields("cd_boat, cd_pessoa")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private static com.tivic.sol.search.Search<BoatTestemunha> searchBoatTestemunha(int cdBoat) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_boat", cdBoat);
		com.tivic.sol.search.Search<BoatTestemunha> search = new SearchBuilder<BoatTestemunha>("mob_boat_testemunha")
				.fields("cd_boat, cd_pessoa")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private static com.tivic.sol.search.Search<BoatImagem> searchBoatImagem(int cdBoat) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_boat", cdBoat);
		com.tivic.sol.search.Search<BoatImagem> search = new SearchBuilder<BoatImagem>("mob_boat_imagem")
				.fields("cd_imagem, cd_boat")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private static com.tivic.sol.search.Search<BoatVeiculo> searchBoatVeiculo(int cdBoat) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_boat", cdBoat);
		com.tivic.sol.search.Search<BoatVeiculo> search = new SearchBuilder<BoatVeiculo>("mob_boat_veiculo")
				.fields("cd_boat_veiculo, cd_boat")
				.searchCriterios(searchCriterios)
				.build();
		return search;
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat ORDER BY nr_boat DESC, dt_ocorrencia DESC");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getProximoNrDocumento(AuthData authData) {
		return getProximoNrDocumento(authData, null);
	}

	public static String getProximoNrDocumento(AuthData authData, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			//int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2("BOAT", -1, authData.getEmpresa().getCdEmpresa(), connection)) <= 0)
				return null;

			return new DecimalFormat("000000").format(nrDocumento);
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			convertPlaca(criterios);

			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			int qtLimite = 0;
			int qtDeslocamento = -1;
			String orderBy = "";
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
					qtDeslocamento = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY"))
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				else
					crt.add(criterios.get(i));
			}

			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);

			ResultSetMap rsm = Search.find(
					" SELECT " + sqlLimit[0] + " DISTINCT(A.cd_boat), A.*, D.*, C.ds_declaracao, C.tp_relacao, F.* "
							+ " FROM mob_boat A" + " LEFT OUTER JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)"
							+ " LEFT OUTER JOIN mob_boat_declarante C ON (A.cd_boat = C.cd_boat)"
							+ " LEFT OUTER JOIN mob_declarante D ON (C.cd_declarante = D.cd_declarante)"
							+ " LEFT OUTER JOIN grl_endereco_boat E ON (A.cd_boat = E.cd_boat)"
							+ " LEFT OUTER JOIN grl_endereco F ON (F.cd_endereco = E.cd_endereco)"
							+ " WHERE 1=1 ",
					orderBy + " " + sqlLimit[1], crt, connect != null ? connect : Conexao.conectar(), connect == null);

			if (qtDeslocamento != -1) {
				ResultSetMap rsmTotal = Search.find(
						"" + " SELECT COUNT(DISTINCT A.cd_boat) FROM mob_boat A"
								+ " LEFT OUTER JOIN mob_boat_veiculo B ON (A.cd_boat = B.cd_boat)"
								+ " LEFT OUTER JOIN mob_boat_declarante C ON (A.cd_boat = C.cd_boat)"
								+ " LEFT OUTER JOIN mob_declarante D ON (C.cd_declarante = D.cd_declarante)",
						" ", crt, connect != null ? connect : Conexao.conectar(), connect == null);

				if (rsmTotal.next())
					rsm.setTotal(rsmTotal.getInt("COUNT"));
			}
			return rsm;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatServices.find: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getNrBoat(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
			}
			
			int NrBoat = -1;
			
			ResultSet rs = connection.prepareStatement("SELECT MAX(nr_boat) AS nr_boat FROM mob_boat").executeQuery();
			if(rs.next()) {
				NrBoat = (rs.getInt("nr_boat") + 1);
			}
			
			return NrBoat;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result get(String nrProtocolo, String nrCpf) {
		return get(nrProtocolo, nrCpf, null);
	}
	public static Result get(String nrProtocolo, String nrCpf, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();	
			}
			
			int cdBoat = 0;
			
			PreparedStatement ps = connection.prepareStatement("SELECT cd_boat FROM mob_boat WHERE nr_protocolo = ?");
			ps.setString(1, nrProtocolo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				cdBoat = rs.getInt("cd_boat");
			}
			
			if(cdBoat <= 0) {
				return new Result(-1, "DAT não encontrado.");
			}
			
			// BOAT ================================================================================
			Boat boat = BoatDAO.get(cdBoat, connection);
			
//			if(boat.getStBoat() == ST_PENDENTE || boat.getStBoat() == ST_ANALISE) {
//				return new Result(-5, "DAT não disponível para impressão.");
//			}
//			if(boat.getStBoat() == ST_INATIVO || boat.getStBoat() == ST_DEFERIDA) {
//				return new Result(-5, "O DAT foi indeferido. Por favor, entre em contato com o órgão de trânsito.");
//			}
			TipoAcidente tipoAcidente = TipoAcidenteDAO.get(boat.getCdTipoAcidente(), connection);
			JSONObject jsonBoat = new JSONObject();
			jsonBoat.put("boat", boat);
			jsonBoat.put("dsDtOcorrencia", Util.formatDate(boat.getDtOcorrencia(), "dd/MM/yyyy"));
			jsonBoat.put("dsHrOcorrencia", Util.formatDate(boat.getDtOcorrencia(), "HH:mm"));
			jsonBoat.put("nmTpCondicaoVia", boat.getTpCondicaoVia() == 5 && boat.getNmOutraCondicaoVia() != null ? boat.getNmOutraCondicaoVia() : tpCondicaoVia[boat.getTpCondicaoVia()]);
			jsonBoat.put("nmTpCondicaoClima", boat.getTpCondicaoClima() == 3 && boat.getNmOutraCondicaoClima() != null ? boat.getNmOutraCondicaoClima() : tpCondicaoClima[boat.getTpCondicaoClima()]);
			jsonBoat.put("nmTipoAcidente", tipoAcidente != null ? tipoAcidente.getNmTipoAcidente() : tpAcidente[boat.getTpAcidente()]);
			jsonBoat.put("nmTpTracadoVia", tpTracadoVia[boat.getTpTracadoVia()]);
			jsonBoat.put("nmStSemaforo", Util.indexInBound(stSemaforo, boat.getStSemaforo()) ? stSemaforo[boat.getStSemaforo()] : null);
			jsonBoat.put("nmStSinalzacaoHorizontal", stSinalizacaoHorizontal[boat.getStSinalizacaoHorizontal()]);
			jsonBoat.put("nmStSinalzacaoVertical", stSinalizacaoVertical[boat.getStSinalizacaoVertical()]);
			ResultSetMap rsmRelacoes = BoatRelacaoServices.getBoatsRelacionados(cdBoat, connection);
			ArrayList<String> dsRelacionados = new ArrayList<String>();
			while(rsmRelacoes.next()) {
				Boat boatRelacionado = BoatDAO.get(rsmRelacoes.getInt("CD_BOAT_RELACAO"), connection);
				dsRelacionados.add(boatRelacionado.getNrProtocolo());
			}
			jsonBoat.put("dsRelacionados", String.join(", ", dsRelacionados));
			
			if(boat.getCdCidade() > 0) {
				Cidade cidade = CidadeDAO.get(boat.getCdCidade(), connection);
				
				jsonBoat.put("nmCidade", cidade.getNmCidade());
				jsonBoat.put("sgEstado", EstadoDAO.get(cidade.getCdEstado(), connection).getSgEstado());
			}
			
			String dsLocal = boat.getDsLocalOcorrencia()!=null ? boat.getDsLocalOcorrencia() : " , , , ";
			String[] logradouro = dsLocal.split(",|- "); // [nm_logradouro, nr_endereco, nm_bairro, nr_cep]
			if(dsLocal.length() > 0) {
				jsonBoat.put("nmLogradouro", Util.indexInBound(logradouro, 0) ? logradouro[0].trim() : "");
				jsonBoat.put("nrEndereco", Util.indexInBound(logradouro, 1) ? logradouro[1].trim() : "");
				jsonBoat.put("nmBairro", Util.indexInBound(logradouro, 2) ? logradouro[2].trim() : "");
				jsonBoat.put("nrCep", Util.indexInBound(logradouro, 3) ? logradouro[3].trim() : "");
			}
			// =====================================================================================
			
			// DECLARANTE ==========================================================================
			Declarante declarante = DeclaranteServices.get(boat.getCdBoat());
			boat.setDeclarante(declarante);
			
			if(declarante == null) {
				return new Result(-2, "Esse DAT não possui um declarante válido.");
			}
			
			if(nrCpf == null || nrCpf.length() < 11 ) {
				return new Result(-4, "O declarante não possui um CPF cadastrado.");
			}
			
			nrCpf = nrCpf.replaceAll("-", "").replaceAll("_", "").replaceAll("\\.", "").substring(0, 11);
			if(!nrCpf.equals(declarante.getNrCpf())) {
				System.out.println(nrCpf + "=/=" + declarante.getNrCpf());
				return new Result(-3, "Declarante informado não condiz com o registrado.");
			}
			
			JSONObject jsonDeclarante = new JSONObject();
			jsonDeclarante.put("declarante", declarante);
			jsonDeclarante.put("dsDtNascimento", Util.formatDate(declarante.getDtNascimento(), "dd/MM/yyyy"));
			jsonDeclarante.put("nmTpSexo", DeclaranteServices.tpSexo[declarante.getTpSexo()]);
			// =====================================================================================	

			// DECLARANTE_ENDERECO =================================================================
			DeclaranteEndereco declaranteEndereco = DeclaranteEnderecoServices.get(declarante.getCdDeclarante(), connection);
			JSONObject jsonDeclaranteEndereco = new JSONObject();
			jsonDeclaranteEndereco.put("declaranteEndereco", declaranteEndereco);
			if(declaranteEndereco.getCdCidade() > 0) {
				Cidade cidade = CidadeDAO.get(declaranteEndereco.getCdCidade(), connection);
				jsonDeclaranteEndereco.put("nmCidade", cidade.getNmCidade());
				jsonDeclaranteEndereco.put("sgEstado", EstadoDAO.get(cidade.getCdEstado(), connection).getSgEstado());
			}
			// =====================================================================================
			
			// BOAT_DECLARANTE =====================================================================
			BoatDeclarante boatDeclarante = BoatDeclaranteDAO.get(cdBoat, declarante.getCdDeclarante(), connection);
			JSONObject jsonBoatDeclarante = new JSONObject();
			jsonBoatDeclarante.put("boatDeclarante", boatDeclarante);		
			jsonDeclarante.put("nmTpRelacao", DeclaranteTipoRelacaoEnum.valueOf(boatDeclarante.getTpRelacao()));	
			// =====================================================================================
			
			// VEICULO =============================================================================
			ResultSetMap rsmVeiculo = BoatVeiculoServices.getAllVeiculosByBoat(cdBoat);
			while(rsmVeiculo.next()) {
				if(rsmVeiculo.getInt("cd_cidade", 0)>0) {
					Cidade cidade = CidadeDAO.get(rsmVeiculo.getInt("cd_cidade"), connection);
					rsmVeiculo.setValueToField("DS_CIDADE", cidade.getNmCidade()+"-"+EstadoDAO.get(cidade.getCdEstado(), connection).getSgEstado());	
				}
				rsmVeiculo.setValueToField("DS_DT_PRIMEIRA_HABILITACAO", Util.formatDate(rsmVeiculo.getGregorianCalendar("dt_primeira_habilitacao"), "dd/MM/yyyy"));
				rsmVeiculo.setValueToField("DS_DT_VENCIMENTO_CNH", Util.formatDate(rsmVeiculo.getGregorianCalendar("dt_vencimento_cnh"), "dd/MM/yyyy"));
				rsmVeiculo.setValueToField("NM_TP_CATEGORIA_CNH", Util.indexInBound(BoatVeiculoServices.tpCategoriaCnh, rsmVeiculo.getInt("tp_categoria_cnh")) ? BoatVeiculoServices.tpCategoriaCnh[rsmVeiculo.getInt("tp_categoria_cnh")] : null);
				if(rsmVeiculo.getInt("cd_tipo", 0) > 0) {
					TipoVeiculo tipoVeiculo = TipoVeiculoDAO.get(rsmVeiculo.getInt("cd_tipo"), connection);
					rsmVeiculo.setValueToField("NM_TIPO_VEICULO", tipoVeiculo != null ? tipoVeiculo.getNmTipoVeiculo() : null);
				}
				if(rsmVeiculo.getInt("cd_categoria", 0) > 0) {
					CategoriaVeiculo categoria = CategoriaVeiculoDAO.get(rsmVeiculo.getInt("cd_categoria"), connection);
					rsmVeiculo.setValueToField("NM_CATEGORIA", categoria != null ? categoria.getNmCategoria() : null);
				}
				if(rsmVeiculo.getInt("cd_especie", 0) > 0) {
					EspecieVeiculo especie = EspecieVeiculoDAO.get(rsmVeiculo.getInt("cd_especie"), connection);
					rsmVeiculo.setValueToField("DS_ESPECIE", especie != null ? especie.getDsEspecie() : null);
				}
				if(rsmVeiculo.getInt("cd_marca", 0) > 0) {
					MarcaModelo marcaModelo = MarcaModeloDAO.get(rsmVeiculo.getInt("cd_marca"), connection);
					rsmVeiculo.setValueToField("NM_MODELO", marcaModelo != null ? marcaModelo.getNmModelo() : null);
				}
			} 
			rsmVeiculo.beforeFirst();
			// =====================================================================================			

			// IMAGENS =============================================================================
			ResultSetMap rsmImagens = BoatImagemServices.getAllWithBytes(cdBoat, connection);
			JSONArray arrayImagens = new JSONArray();
			while(rsmImagens.next()) {
				JSONObject imagem = new JSONObject();
				imagem.put("cdBoatVeiculo", rsmImagens.getInt("cd_boat_veiculo"));		
				byte[] img = (byte[])rsmImagens.getObject("blb_imagem");
				
				switch(rsmImagens.getInt("tp_posicao")) {
					case BoatImagemServices.TP_FRENTE: {
						imagem.put("imgFrontURL", new String(Base64.getEncoder().encode(img)));
						break;
					}
					case BoatImagemServices.TP_LATERAL_DIREITA: {
						imagem.put("imgRightURL", new String(Base64.getEncoder().encode(img)));
						break;
					}
					case BoatImagemServices.TP_LATERAL_ESQUERDA: {
						imagem.put("imgLeftURL", new String(Base64.getEncoder().encode(img)));
						break;
					}
					case BoatImagemServices.TP_TRASEIRA: {
						imagem.put("imgBackURL", new String(Base64.getEncoder().encode(img)));
						break;
					}
					case BoatImagemServices.TP_CENARIO: {
						imagem.put("imgCenaVia", new String(Base64.getEncoder().encode(img)));
						break;
					}
				}
				arrayImagens.put(imagem);
			}
			// =====================================================================================

			// HASH ================================================================================
			String nrHash = gerarHmac(boat);
			// =====================================================================================
						
			Result result = new Result(1, "");
			
			result.addObject("boat", boat);
			result.addObject("declarante", declarante);
			result.addObject("boatDeclarante", boatDeclarante);
			result.addObject("declaranteEndereco", declaranteEndereco);
			result.addObject("rsmVeiculo", rsmVeiculo);
			result.addObject("arrayImagens", arrayImagens);
			result.addObject("nrHash", nrHash);

			result.addObject("jsonBoat", jsonBoat);
			result.addObject("jsonDeclarante", jsonDeclarante);
			result.addObject("jsonBoatDeclarante", jsonBoatDeclarante);
			result.addObject("jsonDeclaranteEndereco", jsonDeclaranteEndereco);
			
			return result;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result validarHash(String nrProtocolo, String nrHash) {
		return validarHash(nrProtocolo, nrHash, null);
	}
	public static Result validarHash(String nrProtocolo, String nrHash, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();	
			}
			
			int cdBoat = 0;
			
			PreparedStatement ps = connection.prepareStatement("SELECT cd_boat FROM mob_boat WHERE nr_protocolo = ?");
			ps.setString(1, nrProtocolo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				cdBoat = rs.getInt("cd_boat");
			}
			
			if(cdBoat <= 0) {
				return new Result(-1, "DAT não encontrado.", "LG_VALIDO", false);
			}
			
			Boat boat = BoatDAO.get(cdBoat, connection);
			
			if(boat==null || boat.getDtComunicacao()==null) {
				return new Result(-2, "Não é possível validar este DAT.", "LG_VALIDO", false);
			}
			
			String hashFromBoat = gerarHmac(boat);
			
			boolean lgValido = nrHash.equals(hashFromBoat);			
			
			return new Result(1, "Verificação realizada.", "LG_VALIDO", lgValido);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static String generateDatProtocol(Boat boat) {
		return gerarNrProtocolo(boat, null);
	}

	public static String gerarNrProtocolo(Boat boat, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			// -> [YY][MM][dd][000][000]
			// -> [ano][mes][dia][cdBoat][nrBoat]
			
			StringBuilder nrProtocolo = new StringBuilder();	
			
			DecimalFormat twoChar = new DecimalFormat("00");
			DecimalFormat threeChar = new DecimalFormat("000");
									
			nrProtocolo.append(twoChar.format(boat.getDtOcorrencia().get(Calendar.YEAR) % 100)); //ano
			nrProtocolo.append(twoChar.format(boat.getDtOcorrencia().get(Calendar.MONTH)+1)); //mes
			nrProtocolo.append(twoChar.format(boat.getDtOcorrencia().get(Calendar.DAY_OF_MONTH))); //dia
			nrProtocolo.append(threeChar.format(boat.getCdBoat() % 1000)); //cdBoat
			nrProtocolo.append(threeChar.format(boat.getNrBoat() % 1000)); //nrBoat		

			return nrProtocolo.toString();
		} catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * STATS
	 */
	@Deprecated
	public static Result statsQtBoat() {
		GregorianCalendar hoje = new GregorianCalendar();
		GregorianCalendar dtInicial = new GregorianCalendar(hoje.get(Calendar.YEAR)-1, Calendar.JANUARY, 1, 0, 0, 0);
		GregorianCalendar dtFinal = new GregorianCalendar(hoje.get(Calendar.YEAR), Calendar.JANUARY, 31, 0, 0, 0);
		
		return statsQtBoat(dtInicial, dtFinal, null);
	}
	
	public static Result statsQtBoat(GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return statsQtBoat(dtInicial, dtFinal, null);
	}
	
	public static Result statsQtBoat(GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
	
		boolean isConnectionNull = connection == null;
		
		try {			
			if (isConnectionNull) 
				connection = Conexao.conectar();
			
			Result result = new Result(1, "");
			
			PreparedStatement ps = connection.prepareStatement(
					" SELECT COUNT(*) AS qt_boat"
					+ " FROM mob_boat"
					+ " WHERE dt_ocorrencia BETWEEN ? AND ?");
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));	
	
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				result.addObject("QT_BOAT", rs.getInt("qt_boat"));
			}
			
			return result;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	
	
	/** **************************************************
	 *  Gerar Hmac para autenticação do boat impresso
	 */	
	private static String gerarHmac(Boat boat) throws Exception {
		return SecretServices
				.onlyPrimes(SecretServices
						.generateHmac(
								boat.toString(), 
								SecretServices.KEY, 
								SecretServices.HMAC_SHA256));
	}
	
	public static Result getHmac(int cdBoat) {
		return getHmac(cdBoat, null);
	}

	public static Result getHmac(int cdBoat, Connection connection) {
		boolean isConnectionNull = connection == null;

		try {			
			if (isConnectionNull) 
				connection = Conexao.conectar();
			Boat boat = BoatDAO.get(cdBoat, connection);
			Result result = new Result(1, gerarHmac(boat));

			return result;
			
		} catch(Exception e) {
			return null;
		}
	}
	
	public static Result relacionarBoat(int cdBoat, int nrBoat, int cdAgente, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {		
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Agente agente = com.tivic.manager.mob.AgenteDAO.get(cdAgente, connect);
			
			if(agente == null) {
				return new Result(-1, "O agente informado não existe.");
			}
			
			Boat boat = BoatDAO.get(cdBoat, connect);

			ResultSetMap rsmBoatRelacao = find(new Criterios("A.nr_boat", Integer.toString(nrBoat), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(rsmBoatRelacao.size() == 0) {
				return new Result(-2, "O DAT com a numeração informada não foi encontrado.");				
			}
						
			if(boat == null) {
				return new Result(-3, "É necessário informar o DAT e o DAT relacionado.");
			}
			
			if(boat.getStBoat() == ST_INDEFERIDA) {
				return new Result(-4, "Este BOAT já foi indeferido, não é possível o relacionar com outras ocorrências.");
			}
			
			rsmBoatRelacao.goTo(0);
			
			int cdBoatRelacionado = rsmBoatRelacao.getInt("CD_BOAT");
			String nrBoatRelacionado = rsmBoatRelacao.getString("NR_BOAT");
			
			BoatRelacao boatRelacao1 = BoatRelacaoDAO.get(cdBoat, cdBoatRelacionado, connect);
			BoatRelacao boatRelacao2 = BoatRelacaoDAO.get(cdBoatRelacionado, cdBoat, connect);
			
			if(boatRelacao1 != null || boatRelacao2 != null) {
				return new Result(-5, "Estes DATs informados já foram associados.");
			}
			
			BoatRelacao boatRelacao = new BoatRelacao();
			boatRelacao.setCdBoat(boat.getCdBoat());
			boatRelacao.setCdBoatRelacao(cdBoatRelacionado);
			
			BoatOcorrencia boatOcorrencia = new BoatOcorrencia();
			boatOcorrencia.setCdAgente(agente.getCdAgente());
			boatOcorrencia.setCdBoat(boat.getCdBoat());
			boatOcorrencia.setDtOcorrencia(new GregorianCalendar());
			boatOcorrencia.setDsOcorrencia("O agente vinculou o DAT N° " + boat.getNrBoat() + " com o DAT N°" + nrBoatRelacionado);
									
			Result save = BoatRelacaoServices.save(boatRelacao, null, connect);
			
			if(save.getCode()<=0) {
				Conexao.rollback(connect);
				return new Result(-5, "Não foi possível vincular os DAT.");
			}
				
			Result saveOcorrencia = BoatOcorrenciaServices.save(boatOcorrencia, null, connect);
			
			if(saveOcorrencia.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(saveOcorrencia.getCode(), (saveOcorrencia.getCode()<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BoatRelacao", boatRelacao);			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao relacionar BOATs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result deferir(Boat boat, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {		
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(boat.getTxtDescricaoSumaria() == null || boat.getTxtDescricaoSumaria().trim().equals("")) {
				return new Result(-1, "É necessário justificar o deferimento do DAT.");				
			}

			NotificacaoDeclarante notificacao = new NotificacaoDeclarante(boat);
			Agente agente = com.tivic.manager.mob.AgenteDAO.get(boat.getCdAgente(), connect);
			
			if(agente == null) {
				return new Result(-2, "O agente informado não existe.");
			}
			
			boat.setStBoat(ST_DEFERIDA);
			Result save = BoatServices.save(boat, connect);
			
			BoatOcorrencia boatOcorrencia = new BoatOcorrencia();
			boatOcorrencia.setCdAgente(agente.getCdAgente());
			boatOcorrencia.setCdBoat(boat.getCdBoat());
			boatOcorrencia.setDtOcorrencia(new GregorianCalendar());
			boatOcorrencia.setDsOcorrencia("O agente deferiu o DAT N° " + boat.getNrBoat());
			BoatOcorrenciaServices.save(boatOcorrencia, null, connect);
			
			if(save.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			notificacao.enviarNotificacaoDeferimentoDeclaracao().send();

			return new Result(save.getCode(), (save.getCode()<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOAT", boat);			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao deferir o BOAT");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	public static Result indeferir(Boat boat, ArrayList<Inconsistencia> inconsistencias, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {		
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(boat.getDsObservacao() != null && boat.getDsObservacao().trim().equals("")) {
				return new Result(-1, "É necessário justificar o deferimento do DAT.");				
			}

			NotificacaoDeclarante notificacao = new NotificacaoDeclarante(boat);
			Agente agente = com.tivic.manager.mob.AgenteDAO.get(boat.getCdAgente(), connect);
			
			if(agente == null) {
				return new Result(-2, "O agente informado não existe.");
			}
			
			boat.setStBoat(ST_INDEFERIDA);
			
			Result save = BoatServices.save(boat, connect);
			
			// Removendo todas as inconsistências do boat.
			BoatInconsistenciaServices.remove(boat.getCdBoat(), null, connect);

			// Adicionando-as novamente
			for(Inconsistencia inc : inconsistencias) {				
				BoatInconsistencia boatInconsistencia = new BoatInconsistencia(boat.getCdBoat(), inc.getCdInconsistencia());
				BoatInconsistenciaServices.save(boatInconsistencia, null, connect);
			}			

			BoatOcorrencia boatOcorrencia = new BoatOcorrencia();
			boatOcorrencia.setCdAgente(agente.getCdAgente());
			boatOcorrencia.setCdBoat(boat.getCdBoat());
			boatOcorrencia.setDtOcorrencia(new GregorianCalendar());
			boatOcorrencia.setDsOcorrencia("O agente indeferiu o DAT N° " + boat.getNrBoat());
			BoatOcorrenciaServices.save(boatOcorrencia, null, connect);

			if(save.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			notificacao.enviarNotificacaoIndeferimentoDeclaracao(inconsistencias).send();

			return new Result(save.getCode(), (save.getCode()<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOAT", boat);			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao deferir o DAT");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static Result solicitarCorrecao(Boat boat, ArrayList<Inconsistencia> inconsistencias, Connection connect) {
		boolean isConnectionNull = connect == null;

		try {		
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			NotificacaoDeclarante notificacao = new NotificacaoDeclarante(boat);
			Agente agente = com.tivic.manager.mob.AgenteDAO.get(boat.getCdAgente(), connect);
			
			if(agente == null) {
				return new Result(-2, "O agente informado não existe.");
			}
			
			boat.setStBoat(ST_PENDENTE);
			
			Result save = BoatServices.save(boat, connect);
			
			// Removendo todas as inconsistências do boat.
			Result result = BoatInconsistenciaServices.remove(boat.getCdBoat(), null, connect);
			
			if(result.getCode() <= 0) {
				return new Result(-3, "Não foi possível atualizar as inconsistências da declaração informada.");				
			}

			// Adicionando-as novamente
			for(Inconsistencia inc : inconsistencias) {				
				BoatInconsistencia boatInconsistencia = new BoatInconsistencia(boat.getCdBoat(), inc.getCdInconsistencia());
				BoatInconsistenciaServices.save(boatInconsistencia, null, connect);
			}			

			BoatOcorrencia boatOcorrencia = new BoatOcorrencia();
			boatOcorrencia.setCdAgente(agente.getCdAgente());
			boatOcorrencia.setCdBoat(boat.getCdBoat());
			boatOcorrencia.setDtOcorrencia(new GregorianCalendar());
			boatOcorrencia.setDsOcorrencia("O agente solicitou a correção do DAT N° " + boat.getNrBoat());
			BoatOcorrenciaServices.save(boatOcorrencia, null, connect);
			
			if(save.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			notificacao.enviarNotificacaoCorrecaoDeclaracao(inconsistencias).send();

			return new Result(save.getCode(), (save.getCode()<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOAT", boat);			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao solicitar correção do DAT");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	public static Result imprimirReport(String nrProtocolo) {
		return imprimirReport(nrProtocolo, null);
	}
	
	public static Result imprimirReport(String nrProtocolo, Connection connection) {
		boolean isConnectionNull = connection == null;
		if (isConnectionNull)
			connection = Conexao.conectar();
		try {
			ResultSet rs;
			PreparedStatement pstmt;
			pstmt = connection.prepareStatement("SELECT cd_boat FROM mob_boat WHERE nr_protocolo=?");
			pstmt.setString(1, nrProtocolo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				
				int cdBoat = rs.getInt("cd_boat");
				
				JSONObject jsonBoat = new JSONObject();	
				Boat boat = BoatDAO.get(cdBoat, connection);
								
				if(Util.isStrBaseAntiga()) {
					PreparedStatement pstmt3 = connection.prepareStatement("select * from PARAMETRO", ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					rs = pstmt3.executeQuery();
					ResultSetMap rsmParametro = new ResultSetMap(rs);

					if(rsmParametro.next()) {
						jsonBoat.put("DS_TITULO_1",rsmParametro.getString("DS_TITULO_1"));
						jsonBoat.put("DS_TITULO_2",rsmParametro.getString("DS_TITULO_2"));
						jsonBoat.put("DS_TITULO_3",rsmParametro.getString("DS_TITULO_3"));
						jsonBoat.put("NR_TELEFONE",rsmParametro.getString("NR_TELEFONE"));
						jsonBoat.put("NM_EMAIL",rsmParametro.getString("NM_EMAIL"));
					}
					rs.beforeFirst();
					if(rs.next()) {
						jsonBoat.put("LOGO_1", rs.getBytes("IMG_LOGO_ORGAO"));	
						jsonBoat.put("LOGO_2", rs.getBytes("IMG_LOGO_DEPARTAMENTO"));
					}
				}
				
				jsonBoat.put("NR_HASH", getHmac(cdBoat).getMessage());
				jsonBoat.put("NR_BOAT", boat.getNrBoat());
				jsonBoat.put("DT_OCORRENCIA", Util.formatDate(boat.getDtOcorrencia(), "dd/MM/yyyy HH:mm"));
				jsonBoat.put("NM_TP_ACIDENTE", getLabelTipoAcidente(boat, connection));
				jsonBoat.put("DS_LOCAL_OCORRENCIA", boat.getDsLocalOcorrencia());
				jsonBoat.put("DS_PONTO_REFERENCIA", boat.getDsPontoReferencia());
				if(boat.getTpCondicaoVia() == 5) {//outra
					jsonBoat.put("NM_TP_CONDICAO_VIA", boat.getNmOutraCondicaoVia());				
				} else {
					jsonBoat.put("NM_TP_CONDICAO_VIA", tpCondicaoVia[boat.getTpCondicaoVia()]);
				}
				if(boat.getTpCondicaoVia() == 5) { //outro
					jsonBoat.put("NM_TP_PAVIMENTO", boat.getNmOutroPavimento());
				}
				else {
					jsonBoat.put("NM_TP_PAVIMENTO", tpPavimento[boat.getTpPavimento()]);
				}
				if(boat.getTpCondicaoClima() == 3) { //OUTRO
					jsonBoat.put("NM_TP_CONDICAO_CLIMA", boat.getNmOutraCondicaoClima());
				} else {
					jsonBoat.put("NM_TP_CONDICAO_CLIMA", tpCondicaoClima[boat.getTpCondicaoClima()]);
				}
				jsonBoat.put("TXT_DESCRICAO_SUMARIA", boat.getTxtDescricaoSumaria());
				jsonBoat.put("NR_OCORRENCIA_POLICIAL", boat.getNrOcorrenciaPolicial());
				jsonBoat.put("NM_DELEGACIA_COMUNICADA", boat.getNmDelegaciaComunicada());
				jsonBoat.put("DT_COMUNICACAO", Util.formatDate(boat.getDtComunicacao(), "dd/MM/yyyy"));
				jsonBoat.put("HR_COMUNICACAO", Util.formatDate(boat.getDtComunicacao(), "HH:mm"));
				jsonBoat.put("NM_AGENTE_POLICIAL", boat.getNmAgentePolicial());
				jsonBoat.put("TXT_DOCUMENTOS_ENTREGUES", boat.getTxtDocumentosEntregues() != null ? boat.getTxtDocumentosEntregues() : "");
				jsonBoat.put("TXT_OCORRENCIA_POLICIAL", boat.getTxtOcorrenciaPolicial());
				jsonBoat.put("NM_ST_BOAT", stBoat[boat.getStBoat()]);
				jsonBoat.put("NM_TP_CARACTERISTICA", boat.getTpCaracteristicaAcidente() == -1 ? "NÃO INFORMADO" : tpCaracteristica[boat.getTpCaracteristicaAcidente()]);
				jsonBoat.put("NM_TP_SEMAFORO", Util.indexInBound(stSemaforo, boat.getStSemaforo()) ? stSemaforo[boat.getStSemaforo()] : null);
				jsonBoat.put("NM_ST_SINALIZACAO_HORIZONTAL", stSinalizacaoHorizontal[boat.getStSinalizacaoHorizontal()]);
				jsonBoat.put("NM_ST_SINALIZACAO_VERTICAL", stSinalizacaoVertical[boat.getStSinalizacaoVertical()]);
				jsonBoat.put("NM_AGENTE", boat.getCdAgente() != 0 ? AgenteDAO.get(boat.getCdAgente(), connection).getNmAgente() : "");
				jsonBoat.put("NR_PROTOCOLO", nrProtocolo);
				jsonBoat.put("DS_OBSERVACAO", boat.getDsObservacao());

				ResultSetMap rsmRelacoes = BoatRelacaoServices.getBoatsRelacionados(cdBoat, connection);
				ArrayList<String> dsRelacionados = new ArrayList<String>();
				while(rsmRelacoes.next()) {
					Boat boatRelacionado = BoatDAO.get(rsmRelacoes.getInt("CD_BOAT_RELACAO"), connection);
					dsRelacionados.add(boatRelacionado.getNrProtocolo());
				}
				
				jsonBoat.put("DS_RELACIONADOS", String.join(", ", dsRelacionados));
				
				String urlValidarBoat = ManagerConf.getInstance().get("MOB_URL_VALIDAR_BOAT", ""); //Util.getConfManager().getProps().getProperty("MOB_URL_VALIDAR_BOAT") != null ? Util.getConfManager().getProps().getProperty("MOB_URL_VALIDAR_BOAT") : "";
				jsonBoat.put("DS_URL_AUTENTICADOR", urlValidarBoat);				
				if(boat.getCdCidade() > 0) {
					Cidade cidade = CidadeDAO.get(boat.getCdCidade(), connection);
					if(cidade != null) {
						jsonBoat.put("NM_CIDADE", cidade.getNmCidade());
						jsonBoat.put("SG_ESTADO", EstadoDAO.get(cidade.getCdEstado(), connection).getSgEstado());
					}
				}
				ResultSetMap rsm = new ResultSetMap();
				ResultSetMap rsmVitimas = BoatVitimaServices.getAllVitimasByBoat(cdBoat, connection);
				ResultSetMap rsmTestemunhas = BoatTestemunhaServices.getAllTestemunhaByBoat(cdBoat, connection);
				ResultSetMap rsmVeiculo = BoatVeiculoServices.getAllVeiculosByBoat(cdBoat, connection);
				ResultSetMap rsmImagens = BoatImagemServices.getAllWithBytes(cdBoat, connection);
				
				for(int i = 0; rsmVeiculo != null && i<rsmVeiculo.size(); i++) {
					rsmVeiculo.goTo(i);
					if(rsmVeiculo.getInt("tp_categoria_cnh") >= 0) {
						rsmVeiculo.setValueToField("NM_TP_CATEGORIA_CNH", BoatVeiculoServices.tpCategoriaCnh[rsmVeiculo.getInt("tp_categoria_cnh")]);
					}
					rsmVeiculo.setValueToField("PRINT_VEICULO", true);
					rsmVeiculo.setValueToField("NR_VEICULO", i+1);
					rsm.addRegister(rsmVeiculo.getRegister());
				}
				while(rsmVitimas.next()) {
					rsmVitimas.setValueToField("TP_NATUREZA_FERIMENTO", 
					rsmVitimas.getInt("TP_NATUREZA_FERIMENTO"));
					rsmVitimas.setValueToField("PRINT_VITIMA", true);
					rsmVitimas.setValueToField("NM_TP_CLASSIFICACAO", tpClassificacao[rsmVitimas.getInt("TP_CLASSIFICACAO")]);
					rsm.addRegister(rsmVitimas.getRegister());
				}
				while(rsmTestemunhas.next()) {
					rsmTestemunhas.setValueToField("PRINT_TESTEMUNHA", true);
					rsm.addRegister(rsmTestemunhas.getRegister());
				}
				
				while(rsmImagens.next()) {
					if(rsmImagens.getInt("tp_posicao") == 0) {
						BoatImagem boatImagem = BoatImagemDAO.get(rsmImagens.getInt("cd_imagem"), rsmImagens.getInt("cd_boat"), connection);
						if(boatImagem == null) {
							continue;
						}
						byte[] im = ImagemServices.getBytesImageScaled(boatImagem.getBlbImagem(), 500, -1, null);
						rsmImagens.setValueToField("IMAGE_URL", im);
						rsmImagens.setValueToField("TP_POSICAO", rsmImagens.getInt("tp_posicao"));
						rsmImagens.setValueToField("PRINT_IMAGE", true);
						rsm.addRegister(rsmImagens.getRegister());
					} else if(rsmImagens.getInt("tp_posicao") == 5) {
						BoatImagem boatImagem = BoatImagemDAO.get(rsmImagens.getInt("cd_imagem"), rsmImagens.getInt("cd_boat"), connection);
						if(boatImagem == null) {
							continue;
						}
						byte[] im = ImagemServices.getBytesImageScaled(boatImagem.getBlbImagem(), 500, -1, null);
						rsmImagens.setValueToField("IMAGE_URL", im);
						rsmImagens.setValueToField("IMAGE_URL", im);
						rsmImagens.setValueToField("TP_POSICAO", rsmImagens.getInt("tp_posicao"));
						rsmImagens.setValueToField("PRINT_DECLARACAO", true);
						rsm.addRegister(rsmImagens.getRegister());
					} else{
						for(int i = 0; rsmVeiculo != null && i<rsmVeiculo.size(); i++) {
							rsmVeiculo.goTo(i);
							if(rsmVeiculo.getInt("cd_boat_veiculo")==rsmImagens.getInt("cd_boat_veiculo")) {
								BoatImagem imagem = BoatImagemDAO.get(rsmImagens.getInt("cd_imagem"), cdBoat, connection);
								if(imagem == null) {
									continue;
								}
								byte[] im = ImagemServices.getBytesImageScaled(imagem.getBlbImagem(), 500, -1, null);
								rsmVeiculo.setValueToField("POSICAO_"+rsmImagens.getInt("tp_posicao"), im);
							}
						}
					}
				}
				Result r = new Result(1, "");
				r.addObject("jsonBoat", jsonBoat);
				r.addObject("rsm", rsm);
				return r;
			} else {
				return null;
			}

		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static byte[] imprimirDAT(HashMap<String, Object> dat, Connection connection) {
		boolean isConnectionNull = connection == null;
		if (isConnectionNull)
			connection = Conexao.conectar();
		try {
			ResultSet rs;
			
			String urlValidarBoat = ManagerConf.getInstance().get("MOB_URL_VALIDAR_BOAT", "");
			HashMap<String, Object> params = new HashMap<String, Object>();

			if(Util.isStrBaseAntiga()) {
				PreparedStatement pstmt3 = connection.prepareStatement("select * from PARAMETRO", ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = pstmt3.executeQuery();
				ResultSetMap rsmParametro = new ResultSetMap(rs);
				
				if(rsmParametro.next()) {
					params.put("DS_TITULO_1",rsmParametro.getString("DS_TITULO_1"));
					params.put("DS_TITULO_2",rsmParametro.getString("DS_TITULO_2"));
					params.put("DS_TITULO_3",rsmParametro.getString("DS_TITULO_3"));
					params.put("NR_TELEFONE",rsmParametro.getString("NR_TELEFONE"));
					params.put("NM_EMAIL",rsmParametro.getString("NM_EMAIL"));
				}
				rs.beforeFirst();
				if(rs.next()) {
					params.put("LOGO_1", Base64.getEncoder().encodeToString(rs.getBytes("IMG_LOGO_ORGAO")));	
					params.put("LOGO_2", Base64.getEncoder().encodeToString( rs.getBytes("IMG_LOGO_DEPARTAMENTO")));
				}
				
			} else {
				params.put("DS_TITULO_1", ParametroServices.getValoresOfParametro("DS_TITULO_1"));
				params.put("DS_TITULO_2", ParametroServices.getValoresOfParametro("DS_TITULO_2"));
				params.put("DS_TITULO_3", ParametroServices.getValoresOfParametro("DS_TITULO_3"));
				params.put("NR_TELEFONE", ParametroServices.getValoresOfParametro("DAT_NR_TELEFONE"));
				params.put("NM_EMAIL", ParametroServices.getValoresOfParametro("DAT_NM_EMAIL"));				
			}

			Boat boat = ((Boat) dat.get("boat"));
			JSONObject jsonBoat = ((JSONObject) dat.get("jsonBoat"));
			JSONObject jsonDeclarante = ((JSONObject) dat.get("jsonDeclarante"));
			JSONObject jsonDeclaranteEndereco = ((JSONObject) dat.get("jsonDeclaranteEndereco"));
			JSONObject jsonBoatDeclarante = ((JSONObject) dat.get("jsonBoatDeclarante"));
			JSONArray arrayImagens = ((JSONArray) dat.get("arrayImagens"));
			String nrHash = ((String) dat.get("nrHash"));
			
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
			GregorianCalendar dtOcorrencia = boat.getDtOcorrencia();
			dtOcorrencia.setTimeZone(TimeZone.getTimeZone("UTC"));

			params.put("DS_URL_AUTENTICADOR", urlValidarBoat);
			
			params.put("NM_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getNmDeclarante());
			params.put("NR_CPF_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getNrCpf());
			params.put("DT_NASCIMENTO_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getDtNascimento().getTimeInMillis() + "");
			params.put("NM_SEXO_DECLARANTE", jsonDeclarante.get("nmTpSexo"));
			params.put("NM_EMAIL_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getNmEmail());
			params.put("NR_TELEFONE_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getNrTelefone());
			params.put("NR_CELULAR_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getNrCelular());			
			params.put("NM_CIDADE_DECLARANTE", jsonDeclaranteEndereco.get("nmCidade"));
			params.put("SG_UF_DECLARANTE", jsonDeclaranteEndereco.has("sgEstado") ? jsonDeclaranteEndereco.get("sgEstado") : "");
			params.put("NR_CEP_DECLARANTE", ((DeclaranteEndereco) jsonDeclaranteEndereco.get("declaranteEndereco")).getNrCep());
			params.put("NM_LOGRADOURO_DECLARANTE", ((DeclaranteEndereco) jsonDeclaranteEndereco.get("declaranteEndereco")).getNmLogradouro());
			params.put("NR_ENDERECO_DECLARANTE", ((DeclaranteEndereco) jsonDeclaranteEndereco.get("declaranteEndereco")).getNrEndereco());
			params.put("NM_BAIRRO_DECLARANTE", ((DeclaranteEndereco) jsonDeclaranteEndereco.get("declaranteEndereco")).getNmBairro());
			params.put("NM_COMPLEMENTO_DECLARANTE", ((DeclaranteEndereco) jsonDeclaranteEndereco.get("declaranteEndereco")).getDsComplemento());
			
			params.put("DS_DECLARACAO", ((BoatDeclarante)jsonBoatDeclarante.get("boatDeclarante")).getDsDeclaracao());
			params.put("NM_PROFISSAO", "");
			params.put("DT_DECLARACAO", "");
			
			params.put("NR_PROTOCOLO", boat.getNrProtocolo());
			params.put("NR_HASH", nrHash);
			params.put("NM_CIDADE_ACIDENTE", jsonBoat.get("nmCidade"));
			params.put("SG_ESTADO_ACIDENTE", jsonBoat.has("sgEstado") ? jsonBoat.get("sgEstado") : "");
			params.put("DT_ACIDENTE", sdfDate.format(dtOcorrencia.getTime()));
			params.put("HR_ACIDENTE", sdfTime.format(dtOcorrencia.getTime()));
			params.put("LG_DANO_PATRIMONIO_PUBLICO", ((Boat) jsonBoat.get("boat")).getLgDanoPatrimonioPublico());
			params.put("LG_DANO_MEIO_AMBIENTE", ((Boat) jsonBoat.get("boat")).getLgDanoMeioAmbiente());
			params.put("DS_LOCAL_ACIDENTE", jsonBoat.get("nmLogradouro"));
			params.put("NR_CEP_ACIDENTE", jsonBoat.get("nrCep"));
			params.put("NM_BAIRRO_ACIDENTE", jsonBoat.get("nmBairro"));
			params.put("DS_PONTO_REFERENCIA", ((Boat) jsonBoat.get("boat")).getDsPontoReferencia());
			params.put("NR_LOCAL_ACIDENTE", jsonBoat.get("nrEndereco"));
			params.put("NM_TIPO_ACIDENTE", jsonBoat.get("nmTipoAcidente"));
			params.put("NM_CONDICAO_PISTA", jsonBoat.get("nmTpCondicaoVia"));
			params.put("NM_TRACADO_VIA", jsonBoat.get("nmTpTracadoVia"));
			params.put("NM_CONDICAO_METEOROLOGICA", jsonBoat.get("nmTpCondicaoClima"));
			params.put("NM_SEMAFORO", jsonBoat.get("nmStSemaforo"));
			params.put("NM_SINALIZACAO_HORIZONTAL", jsonBoat.get("nmStSinalzacaoHorizontal"));
			params.put("NM_SINALIZACAO_VERTICAL", jsonBoat.get("nmStSinalzacaoVertical"));
			
			ResultSetMap rsmVeiculos = ((ResultSetMap) dat.get("rsmVeiculo"));
			String noImage = Base64.getEncoder().encodeToString(Files.readAllBytes(new File(ContextManager.getRealPath()+"/imagens/placeholder-image.png").toPath()));
				
			while(rsmVeiculos.next()) {
				String imgFrontURL = null;
				String imgRightURL = null;
				String imgLeftURL = null;
				String imgBackURL = null;
				
				for(int i = 0; i < arrayImagens.length(); i++) {				
					JSONObject current = arrayImagens.getJSONObject(i);
					
					if(current.getInt("cdBoatVeiculo") == rsmVeiculos.getInt("CD_BOAT_VEICULO")) {
						if(current.has("imgFrontURL")) {
							imgFrontURL = current.getString("imgFrontURL");
						}
						if(current.has("imgRightURL")) {
							imgRightURL = current.getString("imgRightURL");
						}
						if(current.has("imgLeftURL")) {
							imgLeftURL = current.getString("imgLeftURL");
						}
						if(current.has("imgBackURL")) {
							imgBackURL = current.getString("imgBackURL");
						}							
					}			
					
					if(current.has("imgCenaVia") && current.getString("imgCenaVia") != null) {
						params.put("imgCenaVia", current.getString("imgCenaVia"));						
					}
				}
				
				rsmVeiculos.getRegister().put("imgFrontURL", imgFrontURL != null ? imgFrontURL : noImage);
				rsmVeiculos.getRegister().put("imgRightURL", imgRightURL != null ? imgRightURL : noImage);
				rsmVeiculos.getRegister().put("imgLeftURL",  imgLeftURL  != null ? imgLeftURL  : noImage);
				rsmVeiculos.getRegister().put("imgBackURL", imgBackURL  != null ? imgBackURL  : noImage);
			}

			byte[] print = ReportServices.getPdfReport("mob/autoboat", params, rsmVeiculos);
			
			return print;			
		}  catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	

	public static byte[] imprimirProtocolo(HashMap<String, Object> dat, Connection connection) {
		boolean isConnectionNull = connection == null;
		if (isConnectionNull)
			connection = Conexao.conectar();
		try {
			ResultSet rs;
			
			String urlValidarBoat = ManagerConf.getInstance().get("MOB_URL_VALIDAR_BOAT", "");
			HashMap<String, Object> params = new HashMap<String, Object>();

			if(Util.isStrBaseAntiga()) {
				PreparedStatement pstmt3 = connection.prepareStatement("select * from PARAMETRO", ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rs = pstmt3.executeQuery();
				ResultSetMap rsmParametro = new ResultSetMap(rs);
				
				if(rsmParametro.next()) {
					params.put("DS_TITULO_1",rsmParametro.getString("DS_TITULO_1"));
					params.put("DS_TITULO_2",rsmParametro.getString("DS_TITULO_2"));
					params.put("DS_TITULO_3",rsmParametro.getString("DS_TITULO_3"));
					params.put("NR_TELEFONE",rsmParametro.getString("NR_TELEFONE"));
					params.put("NM_EMAIL",rsmParametro.getString("NM_EMAIL"));
				}
				rs.beforeFirst();
				if(rs.next()) {
					params.put("LOGO_1", Base64.getEncoder().encodeToString(rs.getBytes("IMG_LOGO_ORGAO")));	
					params.put("LOGO_2", Base64.getEncoder().encodeToString( rs.getBytes("IMG_LOGO_DEPARTAMENTO")));
				}
				
			} else {
				params.put("DS_TITULO_1", ParametroServices.getValoresOfParametro("DS_TITULO_1"));
				params.put("DS_TITULO_2", ParametroServices.getValoresOfParametro("DS_TITULO_2"));
				params.put("DS_TITULO_3", ParametroServices.getValoresOfParametro("DS_TITULO_3"));
				params.put("NR_TELEFONE", ParametroServices.getValoresOfParametro("DAT_NR_TELEFONE"));
				params.put("NM_EMAIL", ParametroServices.getValoresOfParametro("DAT_NM_EMAIL"));				
			}

			Boat boat = ((Boat) dat.get("boat"));
			JSONObject jsonDeclarante = ((JSONObject) dat.get("jsonDeclarante"));

			params.put("DS_URL_AUTENTICADOR", urlValidarBoat);
			
			params.put("NM_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getNmDeclarante());
			params.put("NR_CPF_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getNrCpf());
			params.put("DT_NASCIMENTO_DECLARANTE", ((Declarante) jsonDeclarante.get("declarante")).getDtNascimento().getTimeInMillis() + "");
			params.put("NR_PROTOCOLO", boat.getNrProtocolo());
			params.put("DS_DT_REGISTRO", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
			params.put("TXT_URL_VALIDAR_BOAT", urlValidarBoat);
			
			ResultSetMap rsm = new ResultSetMap();
			rsm.addRegister(new HashMap<String, Object>());

			byte[] print = ReportServices.getPdfReport("mob/autoboat_protocolo", params, rsm);
			
			return print;			
		}  catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Boat retornarExistente(Boat boat, Connection connect) {
		ArrayList<ItemComparator> crt = new Criterios();
		crt.add(new ItemComparator("A.dt_ocorrencia", Util.formatDate(boat.getDtOcorrencia(), "DD-MM-YYYY"), ItemComparator.EQUAL, Types.TIMESTAMP));
        crt.add(new ItemComparator("D.dt_acidente", boat.getDeclarante().getNrCpf(), ItemComparator.EQUAL, Types.TIMESTAMP));
								        
		ResultSetMap rsm = find(crt, connect);
		
		if(!rsm.next()) {
			return null;
		}
		
		return boat;
	}
	
	private static String getLabelTipoAcidente(Boat boat, Connection connect) {
		try {
			
			if(boat.getCdTipoAcidente() > 0) {
				TipoAcidente tipoAcidente = TipoAcidenteDAO.get(boat.getCdTipoAcidente(), connect);
				return tipoAcidente != null ? tipoAcidente.getNmTipoAcidente() : "Não identificado";
			}
			
			if(tpAcidente[boat.getTpAcidente()] == null) {
				return "Não identificado";
			}
			
			return boat.getTpAcidente() == BoatServices.TP_ACIDENTE_OUTRO ? 
				   boat.getNmTipoAcidenteOutro() : TipoAcidenteBoatEnum.valueOf(boat.getTpAcidente());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return "Não identificado";
		}
	}
	
	private static void convertPlaca(ArrayList<ItemComparator> criterios) {
		for (ItemComparator criterio : criterios) {
			if (criterio.getColumn().equalsIgnoreCase("B.nr_placa")) {
				String placa = criterio.getValue().toString();
				char quintoCaractere = placa.charAt(4);
				
				if (Character.isDigit(quintoCaractere)) {
					char novoQuintoCaractere = converterCaractereInverso(quintoCaractere);
					placa = placa.substring(0, 4) + novoQuintoCaractere + placa.substring(5);
				} else {
					char novoQuintoCaractere = converterCaractere(quintoCaractere);
					placa = placa.substring(0, 4) + novoQuintoCaractere + placa.substring(5);
				}
				
				String placas = "'" + criterio.getValue() + "' , '" + placa + "'";
				criterio.setTypeComparation(ItemComparator.IN);
				criterio.setValue(placas);
				break;
			}
		}
	}
	
	private static char converterCaractere(char caractere) {
	    switch (caractere) {
	        case 'A': return '0';
	        case 'B': return '1';
	        case 'C': return '2';
	        case 'D': return '3';
	        case 'E': return '4';
	        case 'F': return '5';
	        case 'G': return '6';
	        case 'H': return '7';
	        case 'I': return '8';
	        case 'J': return '9';
	        default: return caractere;
	    }
	}
	
	private static char converterCaractereInverso(char caractere) {
	    switch (caractere) {
	        case '0': return 'A';
	        case '1': return 'B';
	        case '2': return 'C';
	        case '3': return 'D';
	        case '4': return 'E';
	        case '5': return 'F';
	        case '6': return 'G';
	        case '7': return 'H';
	        case '8': return 'I';
	        case '9': return 'J';
	        default: return caractere;
	    }
	}
	
}