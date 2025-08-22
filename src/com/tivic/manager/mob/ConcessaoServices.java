package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.fta.VeiculoDAO;
import com.tivic.manager.fta.VeiculoServices;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Logradouro;
import com.tivic.manager.grl.LogradouroDAO;
import com.tivic.manager.grl.ModeloDocumento;
import com.tivic.manager.grl.ModeloDocumentoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.PessoaEmpresaDAO;
import com.tivic.manager.grl.PessoaEmpresaServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFichaMedica;
import com.tivic.manager.grl.PessoaFichaMedicaServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.grl.TipoLogradouroDAO;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ConcessaoServices {
	
	
	public static final int TP_COLETIVO_URBANO = 0;
	public static final int TP_COLETIVO_RURAL  = 1;
	public static final int TP_ESCOLAR_URBANO  = 2;
	public static final int TP_ESCOLAR_RURAL   = 3;
	public static final int TP_COMPLEMENTAR    = 4;
	public static final int TP_TAXI 		   = 5;
	public static final int TP_MOTO_TAXI 	   = 6;
	public static final int TP_FRETE_LOCACAO   = 7;
	public static final int TP_CONTRATO 	   = 8;
	public static final int TP_SECRETARIA  	   = 9;
	public static final int TP_ESCOLAR	 	   = 10;
	
	public static final String[] tiposConcessao = {"Coletivo Urbano", "Coletivo Rural", "Escolar Urbano", "Escolar Rural",
													"Complementar", "Táxi", "Moto Táxi", "Frete/Localização", "Contrato", "Secretaria", "Escolar"};
	
	//Situações		
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	public static final String[] situacaoConcessao = {"Inativo","Ativo"};

	public static Result save(Concessao concessao){
		return save(concessao, null);
	}

	public static Result save(Concessao concessao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(concessao==null)
				return new Result(-1, "Erro ao salvar. Concessao é nulo");

			int retorno;			
			
			concessao.setNrConcessao(Util.limparFormatos(concessao.getNrConcessao()));
			if(concessao.getCdConcessao()==0){
				
				ArrayList<ItemComparator> criterios = new ArrayList<>();
				criterios.add(new  ItemComparator("A.tp_concessao", String.valueOf(concessao.getTpConcessao()), ItemComparator.EQUAL, Types.INTEGER));
				
				int vlInicial = Integer.valueOf(ParametroServices.getValorOfParametro("LG_VARIOS_CONTRATOS_CONCESSAO", 0));
			
				if(vlInicial != 1 ){		
					//verifica se já existe concessão cadastrada para este concessionário
					criterios.add(new  ItemComparator("A.cd_concessionario", String.valueOf(concessao.getCdConcessionario()), ItemComparator.EQUAL, Types.INTEGER));

					ResultSetMap rsm = find(criterios);
					if(rsm.next())
						return new Result(-1, "Erro ao salvar. Essa pessoa já está vinculado à concessão/permissão número " + rsm.getString("nr_concessao") + " do tipo " + tiposConcessao[rsm.getInt("tp_concessao")]);
				}else{
					//Verifica se já existe o número cadatrado para o tipo de concessão informado
					criterios.add(new  ItemComparator("A.nr_concessao", concessao.getNrConcessao(), ItemComparator.LIKE, Types.VARCHAR));
					
					ResultSetMap rsm = find(criterios);
					if(rsm.next())
						return new Result(-1, "Cadastro Duplicado. Já existe um cadastro de Concessão/Permissão " + rsm.getString("nr_concessao") + 
											  " do tipo " + tiposConcessao[rsm.getInt("tp_concessao")]  + " para " + rsm.getString("nm_concessionario"));
				}
					
				retorno = ConcessaoDAO.insert(concessao, connect);
				concessao.setCdConcessao(retorno);
			}
			else {
				retorno = ConcessaoDAO.update(concessao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSAO", concessao);
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
	
	public static Result save(Concessao concessao, PessoaFichaMedica pessoaFichaMedica, Veiculo veiculo, int cdEmpresa, int cdVinculo){
		return save(concessao, pessoaFichaMedica, veiculo, cdEmpresa, cdVinculo, null);
	}
	
	public static Result save(Concessao concessao, PessoaFichaMedica pessoaFichaMedica, Veiculo veiculo,
			int cdEmpresa, int cdVinculo, Connection connect){
		return save(concessao, null, pessoaFichaMedica, veiculo,
				cdEmpresa, cdVinculo, connect);
	}
	
	public static Result save(Concessao concessao, PessoaFisica pessoaFisica, PessoaFichaMedica pessoaFichaMedica, Veiculo veiculo,
			int cdEmpresa, int cdVinculo){
		return save(concessao, pessoaFisica, pessoaFichaMedica, veiculo, cdEmpresa, cdVinculo, null);
	}
	public static Result save(Concessao concessao, PessoaFisica pessoaFisica, PessoaFichaMedica pessoaFichaMedica, Veiculo veiculo,
				int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(concessao==null)
				return new Result(-1, "Erro ao salvar. Concessao é nulo");
			if(pessoaFichaMedica==null)
				return new Result(-1, "Erro ao salvar. Contratado é nulo");
			if(veiculo==null)
				return new Result(-1, "Erro ao salvar. Veículo é nulo");
			
			Result r = new Result(-1, "Erro ao cadastrar Contratado.");
			
			//Verifica CPF
			//Salva pessoa
			if(pessoaFisica.getCdPessoa() == 0){
				r = PessoaServices.save(pessoaFisica, null/*endereco*/, cdEmpresa/*cdEmpresa*/, cdVinculo/*cdVinculo*/, connect);
			}
			//r = PessoaServices.save(pessoaFichaMedica, null/*endereco*/, cdEmpresa/*cdEmpresa*/, cdVinculo/*cdVinculo*/, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
				
			pessoaFichaMedica.setStCadastro( PessoaServices.ST_ATIVO );
			pessoaFichaMedica.setCdPessoa(pessoaFisica.getCdPessoa());
			//PessoaFichaMedicaServices.save(pessoaFichaMedica);
			//PessoaDAO.update( (Pessoa) pessoaFichaMedica, connect );
			//Salva pessoaFichaMedica
			r = PessoaFichaMedicaServices.save(pessoaFichaMedica, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			//Salva concessao
			concessao.setCdConcessionario( pessoaFichaMedica.getCdPessoa());
			r = ConcessaoServices.save(concessao, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			concessao.setCdConcessao( r.getCode() );
			
			//SALVA VEÍCULO
//			TipoVeiculo tipoVeiculo = TipoVeiculoDAO.get( veiculo.getCdTipoVeiculo() );
//			veiculo.setNmModelo( tipoVeiculo.getNmTipoVeiculo() );
			r = VeiculoServices.save(veiculo, null /*reboque*/, false, connect);
			
			veiculo.setCdVeiculo( r.getCode() );
			
			ConcessaoVeiculo concessaoVeiculo = new ConcessaoVeiculo( 0 /*cdConcessaoVeiculo*/,
								concessao.getCdConcessao(), veiculo.getCdVeiculo(), 0/*nrPrefixo*/,
								null/*dtAssinatura*/, null/*dtInicioOperacao*/,null/*dtFinalOperacao*/, 0/*tpFrota*/,
								ST_ATIVO, ConcessaoVeiculoServices.LG_RODANDO);
			Vistoria vistoria = new Vistoria( 0/*cdVistoria*/, 
							new GregorianCalendar()/*dtVistoria*/, 0/*cdAgente*/, pessoaFichaMedica.getCdPessoa()/*cdPessoa*/, 0/*cdEquipamento*/, 
							veiculo.getCdVeiculo()/*cdVeiculo*/, veiculo.getCdPlanoVistoria()/*cdPlanoVistoria*/, 0/*cdVistoriaAnterior*/, 
							0/*stVistoria*/, null/*dtAplicacao*/,0/*tpVistoria*/, ""/*dsObservacao*/, 
							""/*idSelo*/, 0/*cdVistoriador*/,
							pessoaFichaMedica.getCdPessoa()/*cdCondutor*/, concessao.getCdConcessao()/*cdConcessao*/);
			
			r = ConcessaoVeiculoServices.save(concessaoVeiculo, veiculo, vistoria, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			if(r.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			r.setMessage("Contratado cadastrado com sucesso.");
			return r;
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

	public static Result insert(Concessao concessao, Pessoa pessoa, PessoaJuridica pessoaJuridica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo){
		return insert(concessao, pessoa, pessoaJuridica, pessoaEndereco, cdEmpresa, cdVinculo, null);
	}
	public static Result insert(Concessao concessao, Pessoa pessoa, PessoaJuridica pessoaJuridica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(concessao==null)
				return new Result(-1, "Erro ao salvar. Concessão é nula.");
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula.");
			if(pessoaJuridica==null)
				return new Result(-1, "Erro ao salvar. Pessoa Jurídica é nula.");
			if(pessoaEndereco==null)
				return new Result(-1, "Erro ao salvar. Endereço é nulo.");
			if(cdEmpresa<=0)
				return new Result(-1, "Erro ao salvar. Empresa é nula.");
			if(cdVinculo<=0)
				return new Result(-1, "Erro ao vinculo. Vinculo é nulo.");
			
			Result r = new Result(-1, "Erro ao cadastrar contratado.");
			
			if(pessoa.getCdPessoa() == 0){
				pessoaJuridica.setCdPessoa(0);
				pessoaJuridica.setNmPessoa(pessoa.getNmPessoa());
				pessoaJuridica.setNrTelefone1(pessoa.getNrTelefone1());
				pessoaJuridica.setNrTelefone2(pessoa.getNrTelefone2());
				pessoaJuridica.setNmEmail(pessoa.getNmEmail());
				r.setCode(PessoaJuridicaDAO.insert(pessoaJuridica, connect));
			}
			
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			pessoaJuridica.setCdPessoa(r.getCode());
			pessoaEndereco.setCdPessoa(r.getCode());
			concessao.setCdConcessionario(r.getCode());
			
			r.setCode(PessoaEnderecoDAO.insert(pessoaEndereco, connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			//TODO: Parametrizar empresa e vinculo
			r.setCode(PessoaEmpresaDAO.insert(new PessoaEmpresa(cdEmpresa, pessoaJuridica.getCdPessoa(), cdVinculo, new GregorianCalendar(), PessoaEmpresaServices.ST_ATIVO), connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			r = ConcessaoServices.save(concessao, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			if(r.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			r.setMessage("Contratado cadastrado com sucesso.");
			return r;
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
	
	public static Result insert(Concessao concessao, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo){
		return insert(concessao, pessoa, pessoaFisica, pessoaEndereco, cdEmpresa, cdVinculo, null);
	}
	public static Result insert(Concessao concessao, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco,
			int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(concessao==null)
				return new Result(-1, "Erro ao salvar. Concessão é nula.");
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula.");
			if(pessoaFisica==null)
				return new Result(-1, "Erro ao salvar. Pessoa Física é nula.");
			if(pessoaEndereco==null)
				return new Result(-1, "Erro ao salvar. Endereço é nulo.");
			if(cdEmpresa<=0)
				return new Result(-1, "Erro ao salvar. Empresa é nula.");
			if(cdVinculo<=0)
				return new Result(-1, "Erro ao vinculo. Vinculo é nulo.");
			
			Result r = new Result(-1, "Erro ao cadastrar contratado.");
			
			if(pessoa.getCdPessoa() == 0){
				pessoaFisica.setCdPessoa(0);
				pessoaFisica.setNmPessoa(pessoa.getNmPessoa());
				pessoaFisica.setNrTelefone1(pessoa.getNrTelefone1());
				pessoaFisica.setNrTelefone2(pessoa.getNrTelefone2());
				pessoaFisica.setNmEmail(pessoa.getNmEmail());
				r.setCode(PessoaFisicaServices.save(pessoaFisica, pessoaEndereco, cdVinculo, cdEmpresa).getCode());
			}
			
			if(r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			concessao.setCdConcessionario(r.getCode());
			r = ConcessaoServices.save(concessao, connect);
			
			if(r.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			r.setMessage("Concessão cadastrada com sucesso.");
			return r;
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

	public static Result update(Concessao concessao, Pessoa pessoa, PessoaJuridica pessoaJuridica, PessoaEndereco pessoaEndereco){
		return update(concessao, pessoa, pessoaJuridica, pessoaEndereco, null);
	}
	public static Result update(Concessao concessao, Pessoa pessoa, PessoaJuridica pessoaJuridica, PessoaEndereco pessoaEndereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(concessao==null)
				return new Result(-1, "Erro ao salvar. Concessão é nula.");
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula.");
			if(pessoaJuridica==null)
				return new Result(-1, "Erro ao salvar. Pessoa Jurídica é nula.");
			if(pessoaEndereco==null)
				return new Result(-1, "Erro ao salvar. Endereço é nulo");
			
			Result r = new Result(-1, "Erro ao atualizar contratado.");
			
			pessoaJuridica.setNmPessoa(pessoa.getNmPessoa());
			pessoaJuridica.setNrTelefone1(pessoa.getNrTelefone1());
			pessoaJuridica.setNrTelefone2(pessoa.getNrTelefone2());
			pessoaJuridica.setNmEmail(pessoa.getNmEmail());
			
			r.setCode(PessoaJuridicaDAO.update(pessoaJuridica, connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			r.setCode(PessoaEnderecoDAO.update(pessoaEndereco, connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			r = ConcessaoServices.save(concessao, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			r.setMessage("Contratado atualizado com sucesso.");
			return r;
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
	
	public static Result update(Concessao concessao, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco){
		return update(concessao, pessoa, pessoaFisica, pessoaEndereco, null);
	}
	public static Result update(Concessao concessao, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(concessao==null)
				return new Result(-1, "Erro ao salvar. Concessão é nula.");
			if(pessoa==null)
				return new Result(-1, "Erro ao salvar. Pessoa é nula.");
			if(pessoaFisica==null)
				return new Result(-1, "Erro ao salvar. Pessoa Física é nula.");
			if(pessoaEndereco==null)
				return new Result(-1, "Erro ao salvar. Endereço é nulo");
			
			Result r = new Result(-1, "Erro ao atualizar contratado.");
			
			pessoaFisica.setNmPessoa(pessoa.getNmPessoa());
			pessoaFisica.setNrTelefone1(pessoa.getNrTelefone1());
			pessoaFisica.setNrTelefone2(pessoa.getNrTelefone2());
			pessoaFisica.setNmEmail(pessoa.getNmEmail());
			
			r.setCode(PessoaFisicaDAO.update(pessoaFisica, connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			r.setCode(PessoaEnderecoDAO.update(pessoaEndereco, connect));
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			r = ConcessaoServices.save(concessao, connect);
			if( r.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return r;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			r.setMessage("Contratado atualizado com sucesso.");
			return r;
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
	
	public static HashMap<String, String> getFieldsMapByMobilidade(int cdConcessao, int cdEmpresa, int cdUsuario, DocumentoOcorrencia ocorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			
			HashMap<String, String> fieldMap = new HashMap<String, String>();
			
			/* EMPRESA */
			fieldMap.put("<#Url_portal>", ParametroServices.getValorOfParametroAsString("NM_URL_PORTAL", ""));
			if(cdUsuario>0) {
				Pessoa p = PessoaDAO.get(UsuarioDAO.get(cdUsuario, connect).getCdPessoa(), connect);
				
				fieldMap.put("<#Nome_usuario>", p.getNmPessoa());
			}
			
			
			/* GERAL */			
			fieldMap.put("<#Data_impressao>", Util.formatDate(new GregorianCalendar(), "dd 'de' MMMM 'de' yyyy"));
			
			PessoaEndereco endereco = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresa, connect);
			if(endereco!=null) {
				Cidade cidadeEndereco = CidadeDAO.get(endereco.getCdCidade(), connect);
				if(cidadeEndereco!=null)
					fieldMap.put("<#Cidade>", cidadeEndereco.getNmCidade());
			}
			
			/* PERMISSAO */
			Concessao c = ConcessaoDAO.get(cdConcessao, connect);
			Pessoa p = PessoaDAO.get(c.getCdConcessionario());
			PessoaFisica pf = PessoaFisicaDAO.get(c.getCdConcessionario());
			PessoaEndereco enderecoPermissionario = PessoaEnderecoServices.getEnderecoPrincipal(p.getCdPessoa());
			Cidade cidadeEndereco = CidadeDAO.get(enderecoPermissionario.getCdCidade(), connect);
			Parada nOrdem = ParadaDAO.get(c.getCdConcessao(), connect);
			GrupoParada nPonto = GrupoParadaDAO.get(nOrdem.getCdGrupoParada(), connect);
			
			ConcessaoVeiculo concessaoVeiculo = null;
			Veiculo veiculo = null;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(c.getCdConcessao()), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmConcessaoVeiculo = ConcessaoVeiculoServices.find(criterios);
			
			while(rsmConcessaoVeiculo.next()) {
				
				if(rsmConcessaoVeiculo.getInt("ST_CONCESSAO_VEICULO") == ConcessaoVeiculoServices.ST_VINCULADO) {
					veiculo = VeiculoDAO.get(rsmConcessaoVeiculo.getInt("CD_VEICULO"), connect);
					concessaoVeiculo = ConcessaoVeiculoDAO.get(rsmConcessaoVeiculo.getInt("CD_CONCESSAO_VEICULO"), connect);
					break;
				}
			}
			
			
			MarcaModelo marca = MarcaModeloDAO.get(veiculo.getCdMarca(), connect);
			Logradouro enderecoPonto = LogradouroDAO.get(nOrdem.getCdLogradouro(), connect);
			TipoLogradouro logradouro = TipoLogradouroDAO.get(enderecoPonto.getCdLogradouro(), connect);
			ConcessionarioPessoa concessionarioPessoa = ConcessionarioPessoaDAO.get(c.getCdConcessionario(), connect);
		
			
			com.tivic.manager.grl.equipamento.Equipamento equipamento = null;
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_VEICULO", String.valueOf(veiculo.getCdVeiculo()), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmVeiculoEquipamento = VeiculoEquipamentoServices.find(criterios);
			
			while(rsmVeiculoEquipamento.next()) {
				equipamento = EquipamentoDAO.get(rsmVeiculoEquipamento.getInt("CD_VEICULO"), connect);
				
				if(equipamento != null && equipamento.getStEquipamento() == EquipamentoServices.ATIVO && equipamento.getTpEquipamento() == EquipamentoServices.TAXIMETRO) {
					break;
				}
			}

			
			/* PERMISSÃO */
			if(c!=null) {
				fieldMap.put("<#Nr_alvara>", c!=null ? c.getNrConcessao() : "NÚMERO DO ALVARÁ");
				fieldMap.put("<#Nr_ponto>", nOrdem!=null ? nOrdem.getDsReferencia() : "NÚMERO DO PONTO");
				fieldMap.put("<#Nr_ordem>", nPonto!=null ? nPonto.getNmGrupoParada() : "NÚMERO DE ORDEM");
				fieldMap.put("<#Dt_inicio_permissão>", (c.getDtInicioConcessao()!=null ? Util.formatDate(c.getDtInicioConcessao(), "dd/MM/yyyy") : "DATA DE INICIO DA PERMISSÃO"));
				fieldMap.put("<#Dt_final_permissão>", (c.getDtFinalConcessao()!=null ? Util.formatDate(c.getDtFinalConcessao(), "dd/MM/yyyy") : "DATA DO FIM DA PERMISSÃO"));
				fieldMap.put("<#Nm_logradouro_ponto>", enderecoPonto!=null ? enderecoPonto.getNmLogradouro() : "LOGRADOURO");
				fieldMap.put("<#Nm_tipo_logradouro_ponto>", logradouro!=null ? logradouro.getNmTipoLogradouro() : "TIPO LOGRADOURO");
				fieldMap.put("<#Nm_ponto_referencia_ponto>", nOrdem!=null ? nOrdem.getNmPontoReferencia() : "PONTO DE REFERÊNCIA");
			}
			
			/* PERMISSIONARIO */
			if(c!=null) {
				fieldMap.put("<#Nm_permissionario>", (p!=null ? p.getNmPessoa() : "NOME DO PERMISSIONÁRIO"));
				fieldMap.put("<#Nr_rg_permissionario>", (pf!=null ? pf.getNrRg() : "RG DO PERMISSIONÁRIO"));
				fieldMap.put("<#Nr_cpf_permissionario>", (pf!=null ? pf.getNrCpf() : "CPF DO PERMISSIONÁRIO"));
				fieldMap.put("<#Nr_cnh>", (pf!=null ? pf.getNrCnh() : "CNH DO PERMISSIONÁRIO"));
				fieldMap.put("<#Nm_categoria_cnh_permissionario>", (pf!=null ? PessoaFisicaServices.tipoCategoriaHabilitacao[pf.getTpCategoriaHabilitacao()] : "CATEGORIA DA CNH DO PERMISSIONÁRIO"));
				fieldMap.put("<#Dt_validade_cnh_permissionario>", (pf.getDtValidadeCnh()!=null ? Util.formatDate(pf.getDtValidadeCnh(), "dd/MM/yyyy") : "DATA DE VALIDADE DA CNH DO PERMISSIONÁRIO"));
				fieldMap.put("<#Nm_logradouro_permissionario>", enderecoPermissionario!=null ? enderecoPermissionario.getNmLogradouro() : "LOGRADOURO DO PERMISSIONÁRIO");
				fieldMap.put("<#Nr_endereco_permissionario>", enderecoPermissionario!=null ? enderecoPermissionario.getNrEndereco() : "Nº (ENDEREÇO) DO PERMISSIONÁRIO");
				fieldMap.put("<#Nm_bairro_permissionario>", enderecoPermissionario!=null ? enderecoPermissionario.getNmBairro() : "\"#BAIRRO DO PERMISSIONÁRIO");
				fieldMap.put("<#Nm_complemento_endereco_permissionario>", enderecoPermissionario!=null ? enderecoPermissionario.getDsEndereco() : "COMPLEMENTO DO ENDEREÇO DO PERMISSIONÁRIO");
				fieldMap.put("<#Nm_cidade_endereco_permissionario>", cidadeEndereco!=null ? cidadeEndereco.getNmCidade() : "CIDADE DO PERMISSIONÁRIO");
				fieldMap.put("<#Nr_cep_permissionario>", enderecoPermissionario!=null ? enderecoPermissionario.getNrCep() : "CEP DO PERMISSIONÁRIO");

			}
			
			/* AUXILIAR */
			if(concessionarioPessoa!=null && concessionarioPessoa.getTpVinculo() == ConcessionarioPessoaServices.TP_AUXILIAR) {
				if(c!=null) {
					fieldMap.put("<#Nm_auxiliar>", p!=null ? p.getNmPessoa() : "NOME DO AUXILIAR");
					fieldMap.put("<#Nr_cpf_auxiliar>", pf!=null ? pf.getNrCpf() : "CPF DO AUXILIAR");
					fieldMap.put("<#Nr_rg_auxiliar>", pf!=null ? pf.getNrRg() : "RG DO AUXILIAR");
					fieldMap.put("<#Nr_cnh_auxiliar>", (pf!=null ? pf.getNrCnh() : "CNH DO AUXILIAR"));
					fieldMap.put("<#Nm_categoria_cnh_auxiliar>", (pf!=null ? PessoaFisicaServices.tipoCategoriaHabilitacao[pf.getTpCategoriaHabilitacao()] : "CATEGORIA DA CNH DO AUXILIAR"));
					fieldMap.put("<#Dt_validade_cnh_auxiliar>", (pf.getDtValidadeCnh()!=null ? Util.formatDate(pf.getDtValidadeCnh(), "dd/MM/yyyy") : "VALIDADE DA CNH DO AUXILIAR"));
					fieldMap.put("<#Nm_logradouro_auxiliar>", enderecoPermissionario!=null ? enderecoPermissionario.getNmLogradouro() : "LOGRADOURO DO AUXILIAR");
					fieldMap.put("<#Nr_endereco_auxiliar>", enderecoPermissionario!=null ? enderecoPermissionario.getNrEndereco() : "Nº (ENDEREÇO) DO AUXILIAR");
					fieldMap.put("<#Nm_bairro_auxiliar>", enderecoPermissionario!=null ? enderecoPermissionario.getNmBairro() : "BAIRRO DO AUXILIAR");
					fieldMap.put("<#Nm_complemento_endereco_auxiliar>", enderecoPermissionario!=null ? enderecoPermissionario.getDsEndereco() : "COMPLEMENTO DO ENDEREÇO DO AUXILIAR");
					fieldMap.put("<#Nm_cidade_endereco_auxiliar>", cidadeEndereco!=null ? cidadeEndereco.getNmCidade() : "CIDADE DO AUXILIAR");
					fieldMap.put("<#Nr_cep_auxiliar>", enderecoPermissionario!=null ? enderecoPermissionario.getNrCep() : "CEP DO AUXILIAR");
				}
			}
			
			/* VEÍCULO */		
			if(c!=null) {
				fieldMap.put("<#Nm_proprietário>", (veiculo!=null ? p.getNmPessoa() : "NOME DO PROPRIETÁRIO DO VEÍCULO"));
				fieldMap.put("<#Nm_marca>", (veiculo!=null ? marca.getNmMarca() : "MARCA DO VEÍCULO"));
				fieldMap.put("<#Nm_modelo>", (veiculo!=null ? marca.getNmModelo() : "MODELO DO VEÍCULO"));
				fieldMap.put("<#Nr_ano_fabricação>", (veiculo!=null ? veiculo.getNrAnoFabricacao() : "ANO DA FABRICAÇÃO"));
				fieldMap.put("<#Nr_ano_modelo>", (veiculo!=null ? veiculo.getNrAnoModelo() : "ANO DO MODELO"));
				fieldMap.put("<#Nr_placa>", (veiculo!=null ? veiculo.getNrPlaca() : "PLACA DO VEÍCULO"));
				fieldMap.put("<#Nr_chassi>", (veiculo!=null ? veiculo.getNrChassi() : "NÚMERO DO CHASSI"));
				fieldMap.put("<#Nr_renavam>", (veiculo!=null ? veiculo.getNrRenavam() : "NÚMERO DO RENAVAN"));
				fieldMap.put("<#Nm_cor>", (veiculo!=null && !veiculo.getNmCor().trim().equals("") ? veiculo.getNmCor() : "COR DO VEÍCULO"));
				fieldMap.put("<#Nr_taximetro>", (equipamento!=null ? equipamento.getIdEquipamento() : "NÚMERO DO TAXÍMETRO"));
				fieldMap.put("<#Dt_inicio_operação>", (concessaoVeiculo.getDtInicioOperacao()!=null ? Util.formatDate(concessaoVeiculo.getDtInicioOperacao(), "dd/MM/yyyy") : "DATA INICIAL DA OPERAÇÃO"));
				fieldMap.put("<#Dt_final_operação>", (concessaoVeiculo.getDtFinalOperacao()!=null ? Util.formatDate(concessaoVeiculo.getDtFinalOperacao(), "dd/MM/yyyy") : "DATA FINAL DA OPERAÇÃO"));
			}
			
			
			return fieldMap;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
			
	}
	
	public static Result executeModeloWeb(int cdModelo, int cdConcessao, int cdEmpresa, int cdUsuario) {
		return executeModeloWeb(cdModelo, cdConcessao, cdEmpresa, cdUsuario, null, null);
	}
	
	public static Result executeModeloWeb(int cdModelo, int cdConcessao, int cdEmpresa, int cdUsuario, DocumentoOcorrencia ocorrencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ModeloDocumento modelo = ModeloDocumentoDAO.get(cdModelo, connection);
			if(modelo==null)
				return new Result(-1, "Modelo indicado não existe.");
			
			if(DocumentoDAO.get(cdConcessao, connection)==null)
				return new Result(-1, "Documento indicado não existe.");
			
			//if(EmpresaDAO.get(cdEmpresa, connection)==null)
			//	return new Result(-1, "Empresa indicada não existe.");
			
			String source = new String(modelo.getBlbConteudo(), "UTF-8");
			
			HashMap<String, String> fieldMap = getFieldsMapByMobilidade(cdConcessao, cdEmpresa, cdUsuario, ocorrencia, connection);
			for (Object key : fieldMap.keySet().toArray()) {
				
				String v = fieldMap.get(key) == null ? "" : fieldMap.get(key);
				String k = ((String)key).replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				
				v = v.replaceAll("\\n|\n|\\r|\r", "&#13;");
				
//				System.out.println("key: "+k+", value: "+v);
				source = source.replace(k, v);
			}
			
			return new Result(1, "Documento executado com sucesso.", "BLB_DOCUMENTO", source.getBytes());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoServices.executeModeloWeb: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result remove(int cdConcessao){
		return remove(cdConcessao, false, null);
	}
	public static Result remove(int cdConcessao, boolean cascade){
		return remove(cdConcessao, cascade, null);
	}
	public static Result remove(int cdConcessao, boolean cascade, Connection connect){
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
			retorno = ConcessaoDAO.delete(cdConcessao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estão vinculado a outros e não pode ser excluído!");
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
			pstmt = connect.prepareStatement( "SELECT A.*, C.* FROM mob_concessao A\r\n" + 
											  "LEFT OUTER JOIN grl_pessoa C ON (A.cd_concessionario = C.cd_pessoa)" 
											  );
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllAtivosByPrestador(int cdConcessionario) {
		return getAllAtivosByPrestador(cdConcessionario, null);
	}

	public static ResultSetMap getAllAtivosByPrestador(int cdConcessionario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao WHERE cd_concessionario = " + cdConcessionario + " AND st_concessao = " + ST_ATIVO + " ORDER BY nr_concessao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	    
	public static ResultSetMap find(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		return find(crt);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			boolean pesquisaPonto = false;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("NM_GRUPO_PARADA")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_PONTO")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("DS_REFERENCIA")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("NR_ORDEM")) {
					pesquisaPonto = true;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("pesquisaPonto")) {
					pesquisaPonto = true;
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsm = Search.find(
					" SELECT A.*, B.*, B.nm_pessoa AS NM_CONCESSIONARIO, " +
					"       D.NM_FROTA,  " + //E.*, E.NM_PESSOA AS NM_PROPRIETARIO, " +
					"       F.*, G.*, H.*, I.nm_cidade AS NM_NATURALIDADE, "+
					"		I.NM_CIDADE, J.SG_ESTADO, "+
					"       J.sg_estado AS SG_ESTADO_NATURALIDADE, " +
					"       M.tp_sangue, M.tp_fator_rh, N.nm_pais " + //, O.nm_categoria "+
					(pesquisaPonto ? ", L.nm_grupo_parada AS NR_PONTO, K.ds_referencia AS NR_ORDEM " : "") +
					"  FROM mob_concessao A " +
					"  LEFT OUTER JOIN grl_pessoa              B ON (A.cd_concessionario = B.cd_pessoa) " +
					//"  LEFT OUTER JOIN fta_veiculo             C ON (A.cd_veiculo = C.cd_veiculo) " +
					"  LEFT OUTER JOIN fta_frota               D ON (A.cd_frota = D.cd_frota) " +
					//"  LEFT OUTER JOIN grl_pessoa              E ON (C.cd_proprietario = E.cd_pessoa) " + 
					"  LEFT OUTER JOIN grl_pessoa_fisica       F ON (B.cd_pessoa = F.cd_pessoa) " + 
					"  LEFT OUTER JOIN grl_pessoa_juridica     G ON (B.cd_pessoa = G.cd_pessoa) " + 
					"  LEFT OUTER JOIN grl_pessoa_endereco     H ON (B.cd_pessoa = H.cd_pessoa AND H.lg_principal = 1) " + 
					"  LEFT OUTER JOIN grl_cidade          	   I ON (H.cd_cidade = I.cd_cidade) " +
					"  LEFT OUTER JOIN grl_estado          	   J ON (J.cd_estado = I.cd_estado) " +
					"  LEFT OUTER JOIN grl_pais                N ON (J.cd_pais = N.cd_pais) " +
					(pesquisaPonto ? 
					"  LEFT OUTER JOIN mob_parada          	   K ON (K.cd_concessao = A.cd_concessao) " +
					"  LEFT OUTER JOIN mob_grupo_parada        L ON (L.cd_grupo_parada = K.cd_grupo_parada) " : "" )+
					//
					"  LEFT OUTER JOIN grl_pessoa_ficha_medica M ON (B.cd_pessoa = M.cd_pessoa) "+
					//"  LEFT OUTER JOIN fta_categoria_veiculo   O ON (C.cd_categoria = O.cd_categoria) " +
					"  WHERE 1=1 ",
					" ORDER BY NM_CONCESSIONARIO ",
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.find: " + e);
			return null;
		}
	}	
	
	public static ResultSetMap findVeiculos(int cdConcessao){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new ItemComparator("A.CD_CONCESSAO", String.valueOf(cdConcessao), ItemComparator.EQUAL, Types.INTEGER));
		return findVeiculos(crt);
	}

	public static ResultSetMap findVeiculos(ArrayList<ItemComparator> criterios) {
		return findVeiculos(criterios, null);
	}

	public static ResultSetMap findVeiculos(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find(
				" SELECT B.NR_PREFIXO, C.*, D.NM_PESSOA AS NM_PROPRIETARIO FROM mob_concessao A "+
				" JOIN mob_concessao_veiculo B ON ( A.cd_concessao = B.cd_concessao ) "+
				" JOIN fta_veiculo           C ON ( B.cd_veiculo = C.cd_veiculo ) "+
				" LEFT JOIN grl_pessoa       D ON ( C.cd_proprietario = D.cd_pessoa ) "+
				" WHERE 1=1 ","",
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getConcessionarioContrato(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_CONTRATO), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(crt);
	}

	public static ResultSetMap getPermissionarioTaxi(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_TAXI), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(crt);
	}
	
	public static ResultSetMap getPermissionarioTaxi(ArrayList<ItemComparator> criterios){
		criterios.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_TAXI), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(criterios);
	}
	
	public static ResultSetMap getConcessionariosColetivoUrbano(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_COLETIVO_URBANO), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(crt);
	}
	public static ResultSetMap getConcessionarioProprio(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_SECRETARIA), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(crt);
	}
	public static ResultSetMap getConcessionariosColetivoRural(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_COLETIVO_RURAL), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(crt);
	}
	
	public static ResultSetMap getConcessionariosEscolar(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_ESCOLAR), ItemComparator.IN, Types.INTEGER));
		return findConcessionario(crt);
	}
	
	public static ResultSetMap getConcessionariosEscolarUrbano(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_ESCOLAR_URBANO), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(crt);
	}
	
	public static ResultSetMap getConcessionariosEscolarRural(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		crt.add(new  ItemComparator("B.TP_CONCESSAO", String.valueOf(TP_ESCOLAR_RURAL), ItemComparator.EQUAL, Types.INTEGER));
		return findConcessionario(crt);
	}
	
	public static ResultSetMap findConcessionario(){
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		return findConcessionario(crt);
	}

	public static ResultSetMap findConcessionario(ArrayList<ItemComparator> criterios) {
		return findConcessionario(criterios, null);
	}

	public static ResultSetMap findConcessionario(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try{
			ResultSetMap rsm = Search.find(
					" SELECT A.*, A.NM_PESSOA AS NM_CONCESSIONARIO, B.* " +
					" FROM grl_pessoa A " +
					" JOIN mob_concessao B ON (A.cd_pessoa = B.cd_concessionario) " +
					" WHERE ST_CONCESSAO = " + ST_ATIVO,
					" GROUP BY A.CD_PESSOA, B.CD_CONCESSAO "+
					" ORDER BY A.nm_pessoa ",
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			while(rsm.next()){
				
				String nrConcessao = rsm.getString("NR_CONCESSAO");
				
				if(nrConcessao.length() > 0){
					nrConcessao = rsm.getString("NR_CONCESSAO").substring(0, rsm.getString("NR_CONCESSAO").length()-4) + "/" +rsm.getString("NR_CONCESSAO").substring(rsm.getString("NR_CONCESSAO").length()-4, rsm.getString("NR_CONCESSAO").length()) + " - ";
				}
				
				rsm.setValueToField("CL_CONCESSIONARIO", nrConcessao + rsm.getString("NM_CONCESSIONARIO"));
			}
			rsm.beforeFirst();
			
			return rsm;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.findConcessionario: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * 
	 * @param criterios
	 * @param connect
	 * @return rsm contendo os permissionários de táxi e auxiliares, não necessariamente vinculados entre si
	 */
	public static ResultSetMap findCondutoresTaxi(ArrayList<ItemComparator> criterios){
		return findCondutoresTaxi(criterios, null);
	}
	public static ResultSetMap findCondutoresTaxi(ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try{
			ResultSetMap rsm = new ResultSetMap();
			
			if( criterios == null )
				criterios = new ArrayList<ItemComparator>();
			
			int cdVinculoPermissionario = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_PERMISSIONARIO", 0);
			int cdVinculoMotoristaAuxiliar = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_MOTORISTA_AUXILIAR", 0);

//			criterios.add(new  ItemComparator("D.TP_CONCESSAO", String.valueOf(TP_TAXI), ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new  ItemComparator("D.ST_CONCESSAO", String.valueOf(ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new  ItemComparator("C.CD_VINCULO", String.valueOf(cdVinculoMotoristaAuxiliar+","+cdVinculoPermissionario), ItemComparator.IN, Types.INTEGER));
			rsm = Search.find(
								" SELECT A.*, C.*, D.*, E.cd_pessoa as cd_ficha_medica, E.TP_SANGUE, E.TP_FATOR_RH " +
								" FROM GRL_PESSOA          A " +
								" JOIN GRL_PESSOA_EMPRESA                   B ON (A.cd_pessoa = B.cd_pessoa )   " +
								" JOIN GRL_VINCULO                          C ON (B.cd_vinculo = C.cd_vinculo ) " +
								" JOIN GRL_PESSOA_FISICA                    D ON (A.cd_pessoa = D.cd_pessoa ) " +
								" LEFT OUTER JOIN GRL_PESSOA_FICHA_MEDICA   E ON (A.cd_pessoa = E.cd_pessoa ) " +
								" WHERE 1 = 1 ",
								" ORDER BY NM_PESSOA",
								criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			rsm.beforeFirst();
			while( rsm.next() ){
				if( rsm.getObject("IMG_FOTO") != null )
					rsm.setValueToField("IMG_FOTO_STRING", Base64.encodeBase64String((byte[])rsm.getObject("IMG_FOTO")));
			
				if( rsm.getInt("CD_FICHA_MEDICA") > 0 ){
					String nmTipoSanguineo = "Não Informado";
					if( rsm.getInt("TP_SANGUE") >= 0  ){
						nmTipoSanguineo =  PessoaFichaMedicaServices.tipoSangue[ rsm.getInt("TP_SANGUE") ]+ 
								PessoaFichaMedicaServices.tipoFatorRh[ rsm.getInt("TP_FATOR_RH") ];
					}
					rsm.setValueToField("NM_TIPO_SANGUINEO", nmTipoSanguineo);
				}
				
			}
			return rsm;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.findCondutoresTaxi: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * 
	 * @param criterios
	 * @param connect
	 * @return rsm contendo os condutores contratados
	 */
	public static ResultSetMap findCondutoresContratados(ArrayList<ItemComparator> criterios){
		return findCondutoresContratados(criterios, null);
	}
	public static ResultSetMap findCondutoresContratados(ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try{
			ResultSetMap rsm = new ResultSetMap();
			
			if( criterios == null )
				criterios = new ArrayList<ItemComparator>();
			
			int cdVinculoContratado = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CONCESSIONARIO_CONTRATO", 0);
			if( cdVinculoContratado == 0 )
				return new ResultSetMap();
//			criterios.add(new  ItemComparator("D.TP_CONCESSAO", String.valueOf(TP_TAXI), ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new  ItemComparator("D.ST_CONCESSAO", String.valueOf(ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new  ItemComparator("C.CD_VINCULO", String.valueOf(cdVinculoContratado), ItemComparator.EQUAL, Types.INTEGER));
			rsm = Search.find(
					" SELECT A.*, C.*, D.*, A.nm_pessoa as nm_concessionario, E.cd_pessoa as cd_ficha_medica, E.TP_SANGUE, E.TP_FATOR_RH, F.* " +
							" FROM GRL_PESSOA          A " +
							" JOIN GRL_PESSOA_EMPRESA                   B ON (A.cd_pessoa = B.cd_pessoa )   " +
							" JOIN GRL_VINCULO                          C ON (B.cd_vinculo = C.cd_vinculo ) " +
							" JOIN GRL_PESSOA_FISICA                    D ON (A.cd_pessoa = D.cd_pessoa ) " +
							" LEFT OUTER JOIN GRL_PESSOA_FICHA_MEDICA   E ON (A.cd_pessoa = E.cd_pessoa ) " +
							" LEFT OUTER JOIN MOB_CONCESSAO             F ON (A.cd_pessoa = F.cd_concessionario ) " +
							" WHERE 1 = 1 ",
							" ORDER BY NM_PESSOA",
							criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			rsm.beforeFirst();
			while( rsm.next() ){
				if( rsm.getObject("IMG_FOTO") != null )
					rsm.setValueToField("IMG_FOTO_STRING", Base64.encodeBase64String((byte[])rsm.getObject("IMG_FOTO")));
				
				if( rsm.getInt("CD_FICHA_MEDICA") > 0 ){
					String nmTipoSanguineo = "Não Informado";
					if( rsm.getInt("TP_SANGUE") >= 0  ){
						nmTipoSanguineo =  PessoaFichaMedicaServices.tipoSangue[ rsm.getInt("TP_SANGUE") ]+ 
								PessoaFichaMedicaServices.tipoFatorRh[ rsm.getInt("TP_FATOR_RH") ];
					}
					rsm.setValueToField("NM_TIPO_SANGUINEO", nmTipoSanguineo);
				}
				
			}
			return rsm;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.findCondutorContratado: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result saveCondutorTaxiDocumentacao(Pessoa pessoa ){
		return saveCondutorTaxiDocumentacao(pessoa, null, null );
	}
	public static Result saveCondutorTaxiDocumentacao(Pessoa pessoa, ArrayList<Arquivo> arquivos ){
		return saveCondutorTaxiDocumentacao(pessoa, arquivos, null );
	}
	public static Result saveCondutorTaxiDocumentacao(Pessoa pessoa, Arquivo ... arquivos ){
		ArrayList<Arquivo> listArq = new ArrayList<Arquivo>();
		for( Arquivo arquivo : arquivos ){
			listArq.add(arquivo);
		}
		return saveCondutorTaxiDocumentacao(pessoa, listArq, null );
	}
	public static Result saveCondutorTaxiDocumentacao(Pessoa pessoa, ArrayList<Arquivo> arquivos, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Result result = new Result(1, "Condutor atualizado com sucesso!");
			if( pessoa.getImgFoto() == null ){
				Pessoa pessoaTmp = PessoaDAO.get(pessoa.getCdPessoa());
				pessoa.setImgFoto( pessoaTmp.getImgFoto() );
			}
			result.setCode( PessoaDAO.update(pessoa, connect) );
			if( result.getCode() <= 0 ){
				if( isConnectionNull )
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar informações!");
			}
			connect.commit();
			return result;
			
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.findConcessionarioAndAuxiliar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAllArquivoDocumetacaoCondutor(int cdPessoa) {
		return getAllArquivoDocumetacaoCondutor(cdPessoa, null);
	}

	public static ResultSetMap getAllArquivoDocumetacaoCondutor(int cdPessoa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(" SELECT A.*, B.*, C.nm_tipo_arquivo " +
																  " FROM grl_pessoa_arquivo A " +
																  " JOIN grl_arquivo B ON (A.cd_arquivo = B.cd_arquivo) " +
																  " JOIN grl_tipo_arquivo C ON (B.cd_tipo_arquivo = C.cd_tipo_arquivo) " +
																  " WHERE A.cd_pessoa = ?");
			pstmt.setInt(1, cdPessoa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if( rsm != null ){
				rsm.beforeFirst();
				while( rsm.next() ){
					if( rsm.getObject("BLB_ARQUIVO") != null ){
						rsm.setValueToField("BLB_ARQUIVO_STRING", Base64.encodeBase64String((byte[])rsm.getObject("BLB_ARQUIVO")));
					}
				}
			}
			return  rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	public static ResultSetMap getAllPermissionarioAuxiliarTaxi() {
		return getAllPermissionarioAuxiliar(TP_TAXI, null);
	}
	public static ResultSetMap getAllPermissionarioAuxiliar(int tpConcessao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT  E.ds_referencia AS nr_ordem, F.nm_grupo_parada AS nr_ponto, " +
																  "	       A.cd_concessao, A.tp_concessao, A.nr_concessao, A.cd_concessionario, " +
																  "	       C.nm_pessoa AS nm_permissionario, D.nm_pessoa AS nm_auxiliar, A.cd_concessionario, " +
																  "		   H.nr_placa"+
																  "	FROM mob_concessao A " +
																  "	LEFT OUTER JOIN mob_concessionario_pessoa B ON (A.cd_concessionario = B.cd_concessionario) " +
																  "	LEFT OUTER JOIN grl_pessoa                C ON (B.cd_concessionario = C.cd_pessoa )" +
																  "	LEFT OUTER JOIN grl_pessoa                D ON (B.cd_pessoa = D.cd_pessoa ) " +
																  "	LEFT OUTER JOIN mob_parada                E ON (A.cd_concessao = E.cd_concessao) " +
																  "	LEFT OUTER JOIN mob_grupo_parada          F ON (E.cd_grupo_parada = F.cd_grupo_parada) " +
																  "	LEFT OUTER JOIN mob_concessao_veiculo     G ON (A.cd_concessao = G.cd_concessao) " +
																  "	LEFT OUTER JOIN fta_veiculo		          H ON (G.cd_veiculo = H.cd_veiculo) " +
																  "	WHERE A.tp_concessao = ?" +
																  "	  AND B.st_concessionario_pessoa = " + ConcessionarioPessoaServices.ST_ATIVO +
																  "   AND A.st_concessao = " + ST_ATIVO + 
																  "	  AND G.st_concessao_veiculo = " + ConcessaoVeiculoServices.ST_VINCULADO +
																  " ORDER BY F.cd_grupo_parada, E.cd_parada, D.nm_pessoa ");
			pstmt.setInt(1, tpConcessao);
			ResultSetMap rsmPermissionario = new ResultSetMap(pstmt.executeQuery());
			
			return rsmPermissionario;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public List<ConcessaoColetivoRuralDTO> findColetivoRural(Criterios criterios) throws Exception {
		return findColetivoRural(criterios, null);
	}
	
	public List<ConcessaoColetivoRuralDTO> findColetivoRural(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {
			
			if(isConnNull)
				connection = Conexao.conectar();
			
			List<ConcessaoColetivoRuralDTO> list = new ArrayList<ConcessaoColetivoRuralDTO>();
			
			String sql = "SELECT A.cd_concessao FROM mob_concessao A" +
						" LEFT OUTER JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa)" +
						" LEFT OUTER JOIN grl_pessoa_juridica C ON (B.cd_pessoa = C.cd_pessoa)" +
						" LEFT OUTER JOIN mob_concessionario_pessoa D ON (D.cd_concessionario = A.cd_concessionario)" +
						" LEFT OUTER JOIN mob_concessao_veiculo E ON (E.cd_concessao = A.cd_concessao)" +
						" LEFT OUTER JOIN fta_veiculo F ON (F.cd_veiculo = E.cd_veiculo)" +
						" WHERE 1=1";
			
			ResultSetMap rsm = Search.find(sql, "GROUP BY A.cd_concessao", criterios, connection, isConnNull);
			
			while (rsm.next()) {

				ConcessaoColetivoRuralDTO dto = new ConcessaoColetivoRuralDTO();
				
				Concessao concessao = ConcessaoDAO.get(rsm.getInt("CD_CONCESSAO")); 
				dto.setConcessao(concessao);
				
				Pessoa pessoa = PessoaDAO.get(concessao.getCdConcessionario());
				dto.setPessoa(pessoa);
				
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(concessao.getCdConcessionario());
				dto.setPessoaJuridica(pessoaJuridica);
				
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(concessao.getCdConcessionario());
				dto.setPessoaEndereco(pessoaEndereco);
				
				dto.setLgHabilitacaoAuxiliar(ConcessionarioPessoaServices.isValidCnh(concessao.getCdConcessionario(), ConcessionarioPessoaServices.TP_AUXILIAR));
				dto.setLgTacografo(VeiculoEquipamentoServices.isValidTacografo(rsm.getInt("CD_CONCESSAO")));
				
				list.add(dto);
			}
			
			return list;
		} catch(Exception ex) {
			System.out.println("Erro! ConcessaoServices.findColetivoRural");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connection);
		}
	}
	
	public List<ConcessaoTaxiDTO> findTaxi(Criterios criterios) throws Exception {
		return findTaxi(criterios, null);
	}
	
	public List<ConcessaoTaxiDTO> findTaxi(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {
			if(isConnNull)
				connection = Conexao.conectar();
			
			Criterios    crt = new Criterios();
			
			int qtLimite = 0;
			int qtDeslocamento = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.valueOf(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
					qtDeslocamento = Integer.valueOf(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY"))
					continue;
				else
					crt.add(criterios.get(i));
					
			}
			
			List<ConcessaoTaxiDTO> list = new ArrayList<ConcessaoTaxiDTO>();
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);
			
			String sql = "SELECT " + sqlLimit[0] + " A.cd_concessao, E.cd_parada, G.nr_placa " + 
						" FROM mob_concessao A" +
						" LEFT OUTER JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa)" +
						" LEFT OUTER JOIN mob_concessionario_pessoa C ON (C.cd_concessionario = A.cd_concessionario)" +
						" LEFT OUTER JOIN mob_concessao_veiculo D ON (D.cd_concessao = A.cd_concessao)" +
						" LEFT OUTER JOIN mob_parada E ON (E.cd_concessao = A.cd_concessao)" +
						" LEFT OUTER JOIN mob_grupo_parada F ON (F.cd_grupo_parada = E.cd_grupo_parada)" +
						" LEFT OUTER JOIN fta_veiculo G ON (G.cd_veiculo = D.cd_veiculo)" +
						" WHERE 1=1";
			
			ResultSetMap rsm = Search.find(sql, " GROUP BY A.cd_concessao, E.cd_parada, G.nr_placa  " + sqlLimit[1], crt, connection, isConnNull);
			
			while (rsm.next()) {
				ConcessaoTaxiDTO dto = new ConcessaoTaxiDTO();
				
				Concessao concessao = ConcessaoDAO.get(rsm.getInt("CD_CONCESSAO")); 
				dto.setConcessao(concessao);
				
				Pessoa pessoa = PessoaDAO.get(concessao.getCdConcessionario());
				dto.setPessoa(pessoa);
				
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(concessao.getCdConcessionario());
				dto.setPessoaFisica(pessoaFisica);
				
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(concessao.getCdConcessionario());
				dto.setPessoaEndereco(pessoaEndereco);
				
				Parada parada = ParadaDAO.get(rsm.getInt("CD_PARADA"));
				dto.setParada(parada);
				
				GrupoParada grupoParada = ParadaServices.getGrupoParada(rsm.getInt("CD_CONCESSAO"));
				dto.setGrupoParada(grupoParada);
				
				dto.setLgHabilitacaoAuxiliar(ConcessionarioPessoaServices.isValidCnh(concessao.getCdConcessionario(), ConcessionarioPessoaServices.TP_AUXILIAR));
				
				dto.setNrPlaca(rsm.getString("NR_PLACA"));
				
				list.add(dto);
			}
			
			return list;
		} catch(Exception ex) {
			System.out.println("Erro! ConcessaoServices.findTaxi");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connection);
		}
	}
	
	public List<ConcessaoColetivoUrbanoDTO> findColetivoUrbano(Criterios criterios) throws Exception {
		return findColetivoUrbano(criterios, null);
	}
	
	public List<ConcessaoColetivoUrbanoDTO> findColetivoUrbano(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {
			
			if(isConnNull)
				connection = Conexao.conectar();
			
			List<ConcessaoColetivoUrbanoDTO> list = new ArrayList<ConcessaoColetivoUrbanoDTO>();
			
			String sql = "SELECT A.cd_concessao FROM mob_concessao A" +
						" JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa)" +
						" LEFT OUTER JOIN grl_pessoa_juridica C ON (B.cd_pessoa = C.cd_pessoa)" +
						" LEFT OUTER JOIN mob_linha D ON (D.cd_concessao = A.cd_concessao)" +
						" LEFT OUTER JOIN mob_concessao_veiculo E ON (E.cd_concessao = A.cd_concessao)" +
						" LEFT OUTER JOIN fta_veiculo F ON (F.cd_veiculo = A.cd_veiculo)" +
						" WHERE 1=1";
			
			ResultSetMap rsm = Search.find(sql, " GROUP BY A.cd_concessao", criterios, connection, isConnNull);
			
			while (rsm.next()) {

				ConcessaoColetivoUrbanoDTO dto = new ConcessaoColetivoUrbanoDTO();
				
				Concessao concessao = ConcessaoDAO.get(rsm.getInt("CD_CONCESSAO")); 
				dto.setConcessao(concessao);
				
				Pessoa pessoa = PessoaDAO.get(concessao.getCdConcessionario());
				dto.setPessoa(pessoa);
				
				PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(concessao.getCdConcessionario());
				dto.setPessoaJuridica(pessoaJuridica);
				
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(concessao.getCdConcessionario());
				dto.setPessoaEndereco(pessoaEndereco);
				
				dto.setQtLinhas(LinhaServices.getQtdByConcessao(concessao.getCdConcessao()));
				dto.setQtVeiculosVinculados(ConcessaoVeiculoServices.getQtVinculados(concessao.getCdConcessao()));
				dto.setQtVeiculosManutencao(ConcessaoVeiculoServices.getQtManutencao(concessao.getCdConcessao()));
				
				list.add(dto);
			}
			
			return list;
		} catch(Exception ex) {
			System.out.println("Erro! ConcessaoServices.findColetivoUrbano");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connection);
		}
	}
	
	/*FIM COLETIVO RURAL*/

	public static HashMap<String, Object> getSyncData() {
		return getSyncData(null);
	}

	public static HashMap<String, Object> getSyncData(Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		try {

			String sqlConcessao = " SELECT * FROM mob_concessao A WHERE st_concessao = ? ORDER BY cd_concessao";
			
			String sqlConcessaoPessoa = " SELECT A.* FROM mob_concessionario_pessoa A " +
		             "  WHERE  A.st_concessionario_pessoa = ?";

			pstmt1 = connect.prepareStatement(sqlConcessao);
			pstmt1.setInt(1, ST_ATIVO);
		
			pstmt2 = connect.prepareStatement(sqlConcessaoPessoa);
			pstmt2.setInt(1, ConcessionarioPessoaServices.ST_ATIVO);
			//pstmt2.setInt(2, ST_ATIVO);

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("Concessao", Util.resultSetToArrayList(pstmt1.executeQuery()));
			register.put("ConcessionarioPessoa", Util.resultSetToArrayList(pstmt2.executeQuery()));
									
			return register;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public List<Concessao> findConcessao(Criterios criterios) throws Exception {
		return findConcessao(criterios, null);
	}

	public List<Concessao> findConcessao(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {
			if(isConnNull)
				connection = Conexao.conectar();

			List<Concessao> list = new ArrayList<Concessao>();
			
			String sql = "SELECT A.cd_concessao FROM mob_concessao A" +
						" LEFT OUTER JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa)" +
						" WHERE 1=1";

			ResultSetMap rsm = Search.find(sql, "GROUP BY A.cd_concessao", criterios, connection, isConnNull);

			while (rsm.next()) {

				Concessao concessao = ConcessaoDAO.get(rsm.getInt("CD_CONCESSAO")); 

				list.add(concessao);
			}

			return list;
		} catch(Exception ex) {
			System.out.println("Erro! ConcessaoServices.findConcessao");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if (isConnNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap findNomeConcessionarioColetivoUrbano(ArrayList<ItemComparator> criterios) {
		return findNomeConcessionarioColetivoUrbano(criterios, null);
	}

	public static ResultSetMap findNomeConcessionarioColetivoUrbano(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try{
			ResultSetMap rsm = Search.find(
					"SELECT B.cd_pessoa, B.nm_pessoa, A.cd_concessao FROM mob_concessao A " + 
					"JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa) " + 
					"WHERE tp_concessao = " + TP_COLETIVO_URBANO ,
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			return rsm;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoServices.findColetivoUrbano: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
