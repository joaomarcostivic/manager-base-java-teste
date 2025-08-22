package com.tivic.manager.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import com.tivic.manager.adm.Cliente;
import com.tivic.manager.agd.Agendamento;
import com.tivic.manager.agd.AgendamentoDAO;
import com.tivic.manager.agd.AgendamentoServices;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.manager.adm.ClienteDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEmpresa;
import com.tivic.manager.grl.PessoaEmpresaDAO;
import com.tivic.manager.grl.PessoaEmpresaServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaFisicaServices;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaJuridicaServices;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.util.Util;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.mail.SMTPClient;

@DestinationConfig(enabled = false)
public class AtendimentoServices {

	public static final int ST_ABERTO    = 0;
	public static final int ST_ANALISE   = 1;
	public static final int ST_CONCLUIDO = 2;
	public static final int ST_REABERTO  = 3;

	public static String[] classificacao = {"Ativo", "Receptivo"};
	public static String[] situacao      = {"Aberto", "Em Análise", "Concluído", "Reaberto"};
	public static String[] relevancia    = {"Nenhuma", "Mínima", "Pequena", "Média", "Grande", "Máxima"};
	public static String[] avaliacao     = {"Totalmente insatisfeito", "Insatisfeito", "Nem satisfeito, nem insatisfeito", "Satisfeito", "Totalmente satisfeito"};
	public static String[] tipoUsuario   = {"Associado PF", "Associado PJ", "Funcionário", "Não Associado PF", "Não Associado PJ"};

	public static int atendimento(int cdCentral, int cdUsuario, int cdTipoAtendimento, int tpRelevancia, int tpClassificacao,
			int cdFormaContato, int cdFormaDivulgacao, String txtMensagem, int cdPessoa, int gnPessoa, String nmPessoa,
			String nrCpf, String nrCnpj, int tpSexo, int cdEndereco, String nmLogradouro, String nrEndereco, String nmComplemento,
			String nmBairro, String nmPontoReferencia, String nrCep, String nrTelefone1, String nrTelefone2,
			String nrFax, int cdEstado, int cdCidade, String nmEmail, GregorianCalendar dtPrevisaoResposta, int cdEmpresa, int cdFaixaRenda, ArrayList<HashMap<String,Object>> registros){
		return atendimento(cdCentral, cdUsuario, cdTipoAtendimento, tpRelevancia, tpClassificacao, cdFormaContato, cdFormaDivulgacao,
				txtMensagem, cdPessoa, gnPessoa, nmPessoa, nrCpf, nrCnpj, tpSexo, cdEndereco, nmLogradouro,
				nrEndereco, nmComplemento, nmBairro, nmPontoReferencia, nrCep, nrTelefone1, nrTelefone2,
				nrFax, cdEstado, cdCidade, nmEmail, dtPrevisaoResposta, cdEmpresa, cdFaixaRenda, registros, null);
	}

	public static int atendimento(int cdCentral, int cdUsuario, int cdTipoAtendimento, int tpRelevancia, int tpClassificacao,
			int cdFormaContato, int cdFormaDivulgacao, String txtMensagem, int cdPessoa, int gnPessoa, String nmPessoa, String nrCpf, String nrCnpj,
			int tpSexo, int cdEndereco, String nmLogradouro, String nrEndereco, String nmComplemento, String nmBairro,
			String nmPontoReferencia, String nrCep, String nrTelefone1, String nrTelefone2,
			String nrFax, int cdEstado, int cdCidade, String nmEmail, GregorianCalendar dtPrevisaoResposta, int cdEmpresa, int cdFaixaRenda, ArrayList<HashMap<String,Object>> registros, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			nrCpf = nrCpf==null ? "" : nrCpf.replaceAll("[\\.\\-]", "").trim();
			nrCnpj = nrCnpj==null ? "" : nrCnpj.replaceAll("[\\./\\-]", "").trim();
			nrCep = nrCep.replaceAll("[\\.\\-]", "");
			nrTelefone1 = nrTelefone1.replaceAll("[\\.\\-]", "");
			nrTelefone2 = nrTelefone2.replaceAll("[\\.\\-]", "");
			nrFax = nrFax.replaceAll("[\\.\\-]", "");

			//Pessoa
			Pessoa pessoa;			
			if(cdPessoa!=0)
				pessoa = (gnPessoa==1)?PessoaFisicaDAO.get(cdPessoa, connect):PessoaJuridicaDAO.get(cdPessoa, connect);				
			else
				pessoa = gnPessoa==PessoaServices.TP_FISICA && nrCpf!=null && !nrCpf.trim().equals("") ?
						PessoaFisicaServices.getByCpf(nrCpf, connect) :
						nrCnpj!=null && !nrCnpj.trim().equals("") ? PessoaJuridicaServices.getByCnpj(nrCnpj, connect) : null;			
			cdPessoa = pessoa==null ? cdPessoa : pessoa.getCdPessoa();						
			int retorno = 0;
			boolean isInsertPessoa = pessoa==null;			
			if(pessoa==null){				
				if(gnPessoa==1){					
					pessoa = new PessoaFisica(0,
							0 /*cdPessoaSuperior*/,
							0 /*cdPais*/,
							nmPessoa,
							nrTelefone1,
							nrTelefone2,
							"" /*nrCelular*/,
							nrFax,
							nmEmail,
							new GregorianCalendar() /*dtCadastro*/,
							PessoaServices.TP_FISICA /*gnPessoa*/,
							null /*imgFoto*/,
							1 /*stCadastro*/,
							"" /*nmUrl*/,
							"" /*nmApelido*/,
							"" /*txtObservacao*/,
							0 /*lgNotificacao*/,
							"" /*idPessoa*/,
							0 /*cdClassificacao*/,
							0 /*cdFormaDivulgacao*/,
							null /*dtChegadaPais*/,
							0 /*cdNaturalidade*/,
							0 /*cdEscolaridade*/,
							null /*dtNascimento*/,
							nrCpf,
							"" /*sgOrgaoRg*/,
							"" /*nmMae*/,
							"" /*nmPai*/,
							tpSexo,
							0 /*stEstadoCivil*/,
							"" /*nrRg*/,
							"" /*nrCnh*/,
							null /*dtValidadeCnh*/,
							null /*dtPrimeiraHabilitacao*/,
							0 /*tpCategoriaHabilitacao*/,
							0 /*tpRaca*/,
							0 /*lgDeficienteFisico*/,
							"" /*nmFormaTratamento*/,
							0 /*cdEstadoRg*/,
							null /*dtEmissaoRg*/,
							null /*blbFingerprint*/);

					retorno = PessoaFisicaDAO.insert((PessoaFisica)pessoa, connect);
					Cliente cliente = new Cliente();
					cliente.setCdEmpresa(cdEmpresa);
					cliente.setCdPessoa(retorno);
					cdPessoa = cliente.getCdPessoa();
					if(cdFaixaRenda != 0)
						cliente.setCdFaixaRenda(cdFaixaRenda);
					retorno = ClienteDAO.insert(cliente, connect);
				}
				else{
					pessoa = new PessoaJuridica(0,
							0 /*cdPessoaSuperior*/,
							0 /*cdPais*/,
							nmPessoa,
							nrTelefone1,
							nrTelefone2,
							"" /*nrCelular*/,
							nrFax,
							nmEmail,
							new GregorianCalendar() /*dtCadastro*/,
							PessoaServices.TP_JURIDICA /*gnPessoa*/,
							null /*imgFoto*/,
							1 /*stCadastro*/,
							"" /*nmUrl*/,
							"" /*nmApelido*/,
							"" /*txtObservacao*/,
							0 /*lgNotificacao*/,
							"" /*idPessoa*/,
							0 /*cdClassificacao*/,
							0 /*cdFormaDivulgacao*/,
							null /*dtChegadaPais*/,
							nrCnpj,
							"" /*nmRazaoSocial*/,
							"" /*nrInscricaoEstatual*/,
							"" /*nrInscricaoMunicipal*/,
							0 /*nrFuncionarios*/,
							null /*dtInicioAtividade*/,
							0 /*cdNaturezauridica*/,
							0 /*tpEmpresa*/,
							null /*dtTerminoValidade*/);
					retorno = PessoaJuridicaDAO.insert((PessoaJuridica)pessoa, connect);
					Cliente cliente = new Cliente();
					cliente.setCdEmpresa(cdEmpresa);
					cliente.setCdPessoa(retorno);
					cdPessoa = cliente.getCdPessoa();
					if(cdFaixaRenda != 0)
						cliente.setCdFaixaRenda(cdFaixaRenda);
					retorno = ClienteDAO.insert(cliente, connect);
				}
				if(retorno>0) {
					pessoa.setCdPessoa(cdPessoa);
					cdPessoa = pessoa.getCdPessoa();
				}
				else{
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -2;
				}
			}
			else{
				if(gnPessoa==1){
					pessoa = (PessoaFisica)pessoa;
					((PessoaFisica)pessoa).setNmPessoa(nmPessoa);
					((PessoaFisica)pessoa).setNrCpf(nrCpf);
					retorno = PessoaFisicaDAO.update((PessoaFisica)pessoa, connect);
					
					Cliente cliente = ClienteDAO.get(cdEmpresa, retorno, connect);
					if(cliente != null){
						cliente.setCdEmpresa(cdEmpresa);
						cliente.setCdPessoa(retorno);
						if(cdFaixaRenda != 0)
							cliente.setCdFaixaRenda(cdFaixaRenda);
						retorno = ClienteDAO.update(cliente, connect);
					}
					else{				
						cliente = new Cliente();
						cliente.setCdEmpresa(cdEmpresa);
						cliente.setCdPessoa(retorno);
						if(cdFaixaRenda != 0)
							cliente.setCdFaixaRenda(cdFaixaRenda);
						retorno = ClienteDAO.insert(cliente, connect);
					}
				}
				else{					
					((PessoaJuridica)pessoa).setNmPessoa(nmPessoa);
					((PessoaJuridica)pessoa).setNrCnpj(nrCnpj);
					retorno = PessoaJuridicaDAO.update((PessoaJuridica)pessoa, connect);
					Cliente cliente = ClienteDAO.get(cdEmpresa, retorno, connect);
					if(cliente != null){						
						cliente.setCdEmpresa(cdEmpresa);
						cliente.setCdPessoa(retorno);
						if(cdFaixaRenda != 0)
							cliente.setCdFaixaRenda(cdFaixaRenda);
						retorno = ClienteDAO.update(cliente, connect);
					}
					else{						
						cliente = new Cliente();
						cliente.setCdEmpresa(cdEmpresa);
						cliente.setCdPessoa(retorno);
						if(cdFaixaRenda != 0)
							cliente.setCdFaixaRenda(cdFaixaRenda);
						retorno = ClienteDAO.insert(cliente, connect);
					}
				}
				if(retorno<0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -3;
				}
			}

			//Endereco
			PessoaEndereco endereco = new PessoaEndereco(cdEndereco,
					pessoa.getCdPessoa(), null, 0, 0, 0, 0, cdCidade,
					nmLogradouro, nmBairro, nrCep, nrEndereco, nmComplemento, "",
					nmPontoReferencia, 0, 1);			
			if(cdEndereco>0)
				retorno = PessoaEnderecoDAO.update(endereco, connect);
			else
				retorno = PessoaEnderecoDAO.insert(endereco, connect);

			if(retorno>0)
				endereco.setCdEndereco((cdEndereco>0)?cdEndereco:retorno);
			else{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -4;
			}

			if(dtPrevisaoResposta==null){
				dtPrevisaoResposta = new GregorianCalendar();
				dtPrevisaoResposta.add(Calendar.DAY_OF_MONTH, 5);

				dtPrevisaoResposta.set(Calendar.HOUR_OF_DAY, 0);
				dtPrevisaoResposta.set(Calendar.MINUTE, 0);
				dtPrevisaoResposta.set(Calendar.SECOND, 0);
				dtPrevisaoResposta.set(Calendar.MILLISECOND, 0);
			}

			//Atendimento
			int cdAtendimento = 0;
			Atendimento atendimento = new Atendimento(0,
					cdCentral,
					pessoa.getCdPessoa(),
					(cdUsuario==0)?ST_ABERTO:ST_ANALISE,
					dtPrevisaoResposta,
					tpRelevancia /*tpRelevancia*/,
					null /*txtRelevancia*/,
					0 /*tpAvaliacao*/,
					null /*txtAvaliacao*/,
					String.valueOf(System.currentTimeMillis()) /*idAtendimento*/,
					String.valueOf(System.currentTimeMillis()) /*dsSenha*/,
					new GregorianCalendar() /*dtAdmissao*/,
					0,
					cdCentral /*cdCentralResponsavel*/,
					cdUsuario /*cdAtendenteResponsavel*/,
					tpClassificacao /*tpClassificacao*/,
					cdFormaDivulgacao /*cdFormaDivulgacao*/,
					cdTipoAtendimento /*cdTipoAtendimento*/,
					cdFormaContato /*cdFormaContato*/,
					0 /*cdAtendimentoSuperior*/,
					0 /*cdProdutoServico*/);
			retorno = AtendimentoDAO.insert(atendimento, connect);
			cdAtendimento = retorno;
			if(retorno>0){
				atendimento.setCdAtendimento(retorno);
				
				if(TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.ADMISSAO) == null){
					Conexao.rollback(connect);
					return -1;
				}
				
				Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,
						0 /*cdPessoa*/,
						txtMensagem /*txtOcorrencia*/,
						new GregorianCalendar() /*dtOcorrencia*/,
						TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.ADMISSAO).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
						1 /*stOcorrencia*/,
						0 /*cdSistema*/,
						cdUsuario,
						atendimento.getCdAtendimento(),
						0 /*cdAgendamento*/,
						cdCentral,
						cdUsuario);
				retorno = OcorrenciaDAO.insert(historico, connect);
			}
			else{
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -5;
			}

			int cdVinculo = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);			
//			CentralAtendimento central = cdCentral<=0 ? null : CentralAtendimentoDAO.get(cdCentral, connect);
//			int cdEmpresa = central==null ? 0 : central.getCdEmpresa();
			if (cdVinculo>0 && cdEmpresa>0 && (isInsertPessoa || PessoaEmpresaDAO.get(cdEmpresa, cdPessoa, cdVinculo, connect)==null)) {
				if (PessoaEmpresaDAO.insert(new PessoaEmpresa(cdEmpresa,
						cdPessoa,
						cdVinculo,
						null /*dtVinculo*/,
						PessoaEmpresaServices.ST_ATIVO), connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
			}

			for(int i = 0; i < registros.size(); i++){
				int cdTipoNecessidade = registros.get(i)==null || registros.get(i).get("cdTipoNecessidade")==null ? 0 : (Integer) registros.get(i).get("cdTipoNecessidade");
				String vlNecessidade  = registros.get(i)==null || registros.get(i).get("vlNecessidade")==null ? "" : (String) registros.get(i).get("vlNecessidade");
				AtendimentoNecessidade atendNecessidade = new AtendimentoNecessidade(cdAtendimento, cdTipoNecessidade, vlNecessidade);
				if(AtendimentoNecessidadeDAO.insert(atendNecessidade, connect) <= 0){
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
				
			}
			
			if(retorno<0){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -6;
			}
			else if (isConnectionNull)
				connect.commit();

			return atendimento.getCdAtendimento();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.atendimento: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int save(Atendimento atendimento){
		return save(atendimento, null);
	}
	public static int save(Atendimento atendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno;
			if(atendimento.getCdAtendimento()==0){
				retorno = AtendimentoDAO.insert(atendimento, connect);
			}
			else{
				retorno = AtendimentoDAO.update(atendimento, connect);
				retorno = retorno>0?atendimento.getCdAtendimento():retorno;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAtendimentosOfCliente(int cdPessoa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_pessoa", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int cdUltimoTipoOcorrencia = 0;
		int cdTipoOcorrenciaContains = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++)
			if (criterios.get(i).getColumn().equalsIgnoreCase("cdUltimoTipoOcorrencia")) {
				cdUltimoTipoOcorrencia = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTipoOcorrenciaContains")) {
				cdTipoOcorrenciaContains = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		return Search.find("SELECT A.*, B.nm_pessoa, B.nm_email, B.nr_telefone1, B.nr_telefone2, C.nm_central, k.nm_central as nm_central_responsavel, M.nm_pessoa as nm_atendente_responsavel, O.*, P.nm_cidade, Q.sg_estado, R.nm_tipo_atendimento,  " +
						   "(SELECT F.nm_tipo_ocorrencia FROM crm_ocorrencia D, grl_ocorrencia E, grl_tipo_ocorrencia F WHERE A.cd_atendimento=D.cd_atendimento AND D.cd_ocorrencia=E.cd_ocorrencia AND E.cd_tipo_ocorrencia = F.cd_tipo_ocorrencia ORDER BY E.dt_ocorrencia DESC LIMIT 1) as nm_tipo_ocorrencia, " +
						   "(SELECT F.dt_ocorrencia FROM crm_ocorrencia E, grl_ocorrencia F WHERE E.cd_ocorrencia=F.cd_ocorrencia AND A.cd_atendimento=E.cd_atendimento ORDER BY F.dt_ocorrencia DESC LIMIT 1) as dt_alteracao, " +
						   "(SELECT H.nm_pessoa " +
						     " FROM crm_ocorrencia F, grl_ocorrencia I, seg_usuario G, grl_pessoa H " +
						     " WHERE A.cd_atendimento=F.cd_atendimento " +
						     " AND F.cd_ocorrencia=I.cd_ocorrencia " +
						     " AND F.cd_atendente = G.cd_usuario " +
						     " AND G.cd_pessoa = H.cd_pessoa " +
						     " ORDER BY I.dt_ocorrencia DESC LIMIT 1) as nm_atendente_alteracao, " +
						   "(SELECT J.nm_central " +
						     " FROM crm_ocorrencia I, grl_ocorrencia K, crm_central_atendimento J " +
						     " WHERE A.cd_atendimento=I.cd_atendimento " +
						     " AND I.cd_ocorrencia=K.cd_ocorrencia " +
						     " AND I.cd_central=J.cd_central " +
						     " ORDER BY K.dt_ocorrencia DESC LIMIT 1) as nm_central_alteracao " +
						   "FROM crm_atendimento A " +
						   "JOIN grl_pessoa B ON (A.cd_pessoa=B.cd_pessoa) " +
						   "JOIN crm_central_atendimento C ON (A.cd_central=C.cd_central) " +
						   "JOIN crm_tipo_atendimento R ON (A.cd_tipo_atendimento=R.cd_tipo_atendimento) " +
						   "LEFT OUTER JOIN crm_central_atendimento K ON (A.cd_central_responsavel=K.cd_central) " +
						   "LEFT OUTER JOIN seg_usuario L ON (A.cd_atendente_responsavel = L.cd_usuario) " +
						   "LEFT OUTER JOIN grl_pessoa M ON (L.cd_pessoa = M.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_pessoa_endereco O ON (A.cd_pessoa = O.cd_pessoa AND O.lg_principal=1) " +
					       "LEFT OUTER JOIN grl_cidade P ON (O.cd_cidade = P.cd_cidade) " +
					       "LEFT OUTER JOIN grl_estado Q ON (P.cd_estado = Q.cd_estado) " +
						   "WHERE 1=1 " +
						   (cdTipoOcorrenciaContains>0 ? "  AND " + cdTipoOcorrenciaContains + " IN (SELECT B1.cd_tipo_ocorrencia " +
						   "			FROM crm_ocorrencia A1, grl_ocorrencia B1 " +
						   "			WHERE A1.cd_ocorrencia = B1.cd_ocorrencia " +
						   "			  AND A1.cd_atendimento = A.cd_atendimento) " : "") +
						   (cdUltimoTipoOcorrencia>0 ? "  AND (SELECT MAX(B1.cd_tipo_ocorrencia) " +
						   "	   FROM crm_ocorrencia A1, grl_ocorrencia B1 " +
						   "	   WHERE A1.cd_ocorrencia = B1.cd_ocorrencia " +
						   "		 AND A1.cd_atendimento = A.cd_atendimento " +
						   "		 AND B1.dt_ocorrencia = (SELECT MAX(D1.dt_ocorrencia) " +
						   "								 FROM crm_ocorrencia C1, grl_ocorrencia D1 " +
						   "								 WHERE C1.cd_ocorrencia = D1.cd_ocorrencia " +
						   "								   AND C1.cd_atendimento = A.cd_atendimento)) = " + cdUltimoTipoOcorrencia : ""), "ORDER BY A.tp_relevancia DESC, A.dt_admissao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int setTpRelevancia(int cdAtendimento, int cdCentral, int cdUsuario, int tpRelevancia, String txtRelevancia) {
		return setTpRelevancia(cdAtendimento, cdCentral, cdUsuario, tpRelevancia, txtRelevancia, null);
	}

	public static int setTpRelevancia(int cdAtendimento, int cdCentral, int cdUsuario, int tpRelevancia, String txtRelevancia, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Atendimento atendimento = AtendimentoDAO.get(cdAtendimento, connect);
			
			atendimento.setTpRelevancia(tpRelevancia);
			atendimento.setTxtRelevancia(txtRelevancia);

			AtendimentoDAO.update(atendimento, connect);

			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,0 /*cdPessoa*/,
					                              "Relevância alterada: "+AtendimentoServices.relevancia[tpRelevancia] /*txtOcorrencia*/,
					                              new GregorianCalendar() /*dtOcorrencia*/,
					                              TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.RELEVANCIA).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
					                              1 /*stOcorrencia*/, 0 /*cdSistema*/, cdUsuario, atendimento.getCdAtendimento(), 0 /*cdAgendamento*/,
					                              cdCentral, cdUsuario);

			int retorno = insertHistorico(cdAtendimento, historico, connect);
			if(retorno<0){
				Conexao.rollback(connect);
				return -2;
			}
			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setDtPrevisaoResposta(int cdAtendimento, int cdCentral, int cdUsuario, GregorianCalendar dtPrevisaoResposta) {
		return setDtPrevisaoResposta(cdAtendimento, cdCentral, cdUsuario, dtPrevisaoResposta, null);
	}

	public static int setDtPrevisaoResposta(int cdAtendimento, int cdCentral, int cdUsuario, GregorianCalendar dtPrevisaoResposta, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Atendimento atendimento = AtendimentoDAO.get(cdAtendimento, connect);
			atendimento.setDtPrevisaoResposta(dtPrevisaoResposta);

			AtendimentoDAO.update(atendimento, connect);

			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,
					0 /*cdPessoa*/,
					"Previsão de resposta: " + Util.formatDate(dtPrevisaoResposta, "dd/MM/yyyy") /*txtOcorrencia*/,
					new GregorianCalendar() /*dtOcorrencia*/,
					TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.PREVISAO).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
					1 /*stOcorrencia*/,
					0 /*cdSistema*/,
					cdUsuario,
					atendimento.getCdAtendimento(),
					0 /*cdAgendamento*/,
					cdCentral,
					cdUsuario);

			int retorno = insertHistorico(cdAtendimento, historico, connect);
			if(retorno<0){
				Conexao.rollback(connect);
				return -2;
			}
			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.setDtPrevisaoResposta: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setResponsavel(int cdAtendimento, int cdCentral, int cdUsuario, int cdCentralResponsavel, int cdUsuarioResponsavel) {
		return setResponsavel(cdAtendimento, cdCentral, cdUsuario, cdCentralResponsavel, cdUsuarioResponsavel, null);
	}
	public static int setResponsavel(int cdAtendimento, int cdCentral, int cdUsuario, int cdCentralResponsavel, int cdUsuarioResponsavel, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Atendimento atendimento = AtendimentoDAO.get(cdAtendimento, connect);
			atendimento.setCdCentralResponsavel(cdCentralResponsavel);
			atendimento.setCdAtendenteResponsavel(cdUsuarioResponsavel);
			AtendimentoDAO.update(atendimento, connect);

			CentralAtendimento central = CentralAtendimentoDAO.get(cdCentralResponsavel, connect);

			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,
					0 /*cdPessoa*/,
					((atendimento.getStAtendimento()==ST_ABERTO)?"Atendimento atribuído a ":"Atendimento transferido para ")+"["+central.getNmCentral()+"] "+AtendenteServices.getNmAtendente(cdUsuarioResponsavel) /*txtOcorrencia*/,
					new GregorianCalendar() /*dtOcorrencia*/,
					TipoOcorrenciaServices.getByTpAcao((atendimento.getStAtendimento()==ST_ABERTO)?TipoOcorrenciaServices.ATRIBUICAO:TipoOcorrenciaServices.TRANSFERENCIA).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
					1 /*stOcorrencia*/,
					0 /*cdSistema*/,
					cdUsuario,
					atendimento.getCdAtendimento(),
					0 /*cdAgendamento*/,
					cdCentral,
					cdUsuario);

			int retorno = insertHistorico(cdAtendimento, historico, connect);
			if(retorno<0){
				Conexao.rollback(connect);
				return -2;
			}
			else if (isConnectionNull)
				connect.commit();

			return atendimento.getCdAtendimento();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.setResponsavel: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setCdAgendamento(int cdAtendimento, int cdCentral, int cdUsuario, String nmAgendamento,
			GregorianCalendar dtInicial, int cdTipoLembrete, int tpAgendamento){
		return setCdAgendamento(cdAtendimento, cdCentral, cdUsuario, nmAgendamento, dtInicial, cdTipoLembrete, tpAgendamento, null);
	}

	public static int setCdAgendamento(int cdAtendimento, int cdCentral, int cdUsuario, String nmAgendamento,
			GregorianCalendar dtInicial, int cdTipoLembrete, int tpAgendamento, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			HashMap<String, Object> hash = AgendamentoServices.insert(nmAgendamento, dtInicial, cdTipoLembrete,
					tpAgendamento, cdUsuario, connection);
			if (hash==null) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			Agendamento agendamento = (Agendamento)hash.get("agendamentoDefault");
			int cdAgendamento = agendamento.getCdAgendamento();

			if (setCdAgendamento(cdAtendimento, cdCentral, cdUsuario, cdAgendamento, connection)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setCdAgendamento(int cdAtendimento, int cdCentral, int cdUsuario, int cdAgendamento) {
		return setCdAgendamento(cdAtendimento, cdCentral, cdUsuario, cdAgendamento, null);
	}

	public static int setCdAgendamento(int cdAtendimento, int cdCentral, int cdUsuario, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Agendamento agendamento = AgendamentoDAO.get(cdAgendamento, connect);

			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,
					0 /*cdPessoa*/,
					"("+Util.formatDate(agendamento.getDtInicial(), "dd/MM/yyyy HH:mm")+") "+agendamento.getNmAgendamento() /*txtOcorrencia*/,
					new GregorianCalendar() /*dtOcorrencia*/,
					TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.AGENDAMENTO).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
					1 /*stOcorrencia*/,
					0 /*cdSistema*/,
					cdUsuario,
					cdAtendimento,
					0 /*cdAgendamento*/,
					cdCentral,
					cdUsuario);

			int retorno = insertHistorico(cdAtendimento, historico, connect);
			if(retorno<0){
				Conexao.rollback(connect);
				return -2;
			}
			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.setCdAgendamento: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int setConclusao(int cdAtendimento, int cdCentral, int cdTipoResultado, int cdUsuario, String txtMensagem) {
		return setConclusao(cdAtendimento, cdCentral, cdTipoResultado, cdUsuario, txtMensagem, false, null);
	}

	public static int setConclusao(int cdAtendimento, int cdCentral, int cdTipoResultado, int cdUsuario, String txtMensagem, boolean sendEmail) {
		return setConclusao(cdAtendimento, cdCentral, cdTipoResultado, cdUsuario, txtMensagem, sendEmail, null);
	}

	public static int setConclusao(int cdAtendimento, int cdCentral, int cdTipoResultado, int cdUsuario, String txtMensagem, boolean sendEmail, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/, 0 /*cdPessoa*/, txtMensagem /*txtOcorrencia*/, new GregorianCalendar() /*dtOcorrencia*/,
												  TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.CONCLUSAO).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
												  1 /*stOcorrencia*/, 0 /*cdSistema*/, cdUsuario, cdAtendimento, 0 /*cdAgendamento*/, cdCentral, cdUsuario);

			if(insertHistorico(cdAtendimento, historico, connect)<0){
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -2;
			}
			if(cdTipoResultado > 0)
				connect.prepareStatement("UPDATE crm_atendimento SET cd_tipo_resultado = "+cdTipoResultado+" WHERE cd_atendimento = "+cdAtendimento).executeUpdate();

			if (sendEmail)
				verifySendEmail(historico, connect);

			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.setTxtResposta: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int verifySendEmail(Ocorrencia ocorrencia, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			String nmServidor 		= ParametroServices.getValorOfParametro("NM_SERVIDOR_SMTP_CRM", null, connection);
			String nmLogin 			= ParametroServices.getValorOfParametro("NM_LOGIN_SERVIDOR_SMTP_CRM", null, connection);
			String nmSenha 			= ParametroServices.getValorOfParametro("NM_SENHA_SERVIDOR_SMTP_CRM", null, connection);
			String nmEmailRemetente = ParametroServices.getValorOfParametro("NM_EMAIL_REMETENTE_SMTP_CRM", null, connection);
			int lgAutenticacao 		= ParametroServices.getValorOfParametroAsInteger("LG_AUTENTICACAO_SMTP_CRM", 1, 0, connection);
			int nrPorta 			= nmServidor!=null && nmServidor.indexOf(":")!=-1 ? Integer.parseInt(nmServidor.substring(nmServidor.indexOf(':')+1).trim()) : 25;
			int lgSsl 				= ParametroServices.getValorOfParametroAsInteger("LG_SSL_SMTP_CRM", 1, 0, connection);
			nmServidor = nmServidor!=null && nmServidor.indexOf(":")!=-1 ? nmServidor.substring(0, nmServidor.indexOf(':')).trim() : nmServidor;
			if (nmServidor!=null && !nmServidor.trim().equals("") && nmLogin!=null && !nmLogin.trim().equals("") &&
				nmSenha!=null && !nmSenha.trim().equals("")) {
					Atendimento atendimento = AtendimentoDAO.get(ocorrencia.getCdAtendimento(), connection);
					Pessoa pessoa = atendimento==null ? null : PessoaDAO.get(atendimento.getCdPessoa(), connection);
					String nmEmail = pessoa==null ? null : pessoa.getNmEmail();
					if (nmEmail!=null && !nmEmail.trim().equals("")) {
						StringBuffer buffer = new StringBuffer();
						buffer.append("Prezado Cliente:");
						buffer.append("\n\n");
						buffer.append("Segue-se abaixo resposta referente à sua solicitação em nosso Sistema Online de Atendimento:");
						buffer.append("\n\n");
						buffer.append(ocorrencia.getTxtOcorrencia());
						SMTPClient smtp = new SMTPClient(nmServidor, nrPorta, nmLogin, nmSenha, lgAutenticacao==1, lgSsl==1);
						smtp.connect();
						smtp.send(nmEmailRemetente, nmEmailRemetente, nmEmail, "Sisteme Online de Atendimento - Resposta de Requisição",
								  buffer.toString(), "text/plain", "ISO8859-1");
					}
			}

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setOutroTipoOcorrencia(int cdAtendimento, int cdCentral, int cdUsuario, String txtMensagem, int cdTipoOcorrencia) {
		return setOutroTipoOcorrencia(cdAtendimento, cdCentral, cdUsuario, txtMensagem, cdTipoOcorrencia, null);
	}

	public static int setOutroTipoOcorrencia(int cdAtendimento, int cdCentral, int cdUsuario, String txtMensagem, int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,
					0 /*cdPessoa*/,
					txtMensagem /*txtOcorrencia*/,
					new GregorianCalendar() /*dtOcorrencia*/,
					cdTipoOcorrencia /*cdTipoOcorrencia*/,
					1 /*stOcorrencia*/,
					0 /*cdSistema*/,
					cdUsuario,
					cdAtendimento,
					0 /*cdAgendamento*/,
					cdCentral,
					cdUsuario);

			int retorno = insertHistorico(cdAtendimento, historico, connect);
			if(retorno<0){
				Conexao.rollback(connect);
				return -2;
			}
			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.setOutroTipoOcorrencia: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int reabrirAtendimento(int cdAtendimento, int cdCentral, int cdUsuario, String txtMensagem) {
		return reabrirAtendimento(cdAtendimento, cdCentral, cdUsuario, txtMensagem, null);
	}

	public static int reabrirAtendimento(int cdAtendimento, int cdCentral, int cdUsuario, String txtMensagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,
					0 /*cdPessoa*/,
					txtMensagem /*txtOcorrencia*/,
					new GregorianCalendar() /*dtOcorrencia*/,
					TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.REABERTURA).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
					1 /*stOcorrencia*/,
					0 /*cdSistema*/,
					cdUsuario,
					cdAtendimento,
					0 /*cdAgendamento*/,
					cdCentral,
					cdUsuario);

			int retorno = insertHistorico(cdAtendimento, historico, connect);
			if(retorno<0){
				Conexao.rollback(connect);
				return -2;
			}
			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.reabrirAtendimento: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static int anexarArquivo(byte[] blbArquivo, String nmArquivo, String txtObservacao, int cdAtendimento, int cdCentral, int cdUsuario) {
		return anexarArquivo(blbArquivo, nmArquivo, txtObservacao, cdAtendimento, cdCentral, cdUsuario, null);
	}

	public static int anexarArquivo(byte[] blbArquivo, String nmArquivo, String txtObservacao, int cdAtendimento, int cdCentral, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Ocorrencia historico = new Ocorrencia(0 /*cdOcorrencia*/,
					0 /*cdPessoa*/,
					"("+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+") Arquivo anexado. \nDescrição: "+txtObservacao /*txtOcorrencia*/,
					new GregorianCalendar() /*dtOcorrencia*/,
					TipoOcorrenciaServices.getByTpAcao(TipoOcorrenciaServices.ARQUIVO).getCdTipoOcorrencia() /*cdTipoOcorrencia*/,
					1 /*stOcorrencia*/,
					0 /*cdSistema*/,
					cdUsuario,
					cdAtendimento,
					0 /*cdAgendamento*/,
					cdCentral,
					cdUsuario);

			int retorno = insertHistorico(cdAtendimento, historico, connect);
			if(retorno>0){
				retorno = OcorrenciaServices.insertArquivo(blbArquivo, nmArquivo, retorno, txtObservacao, connect);
			}

			if(retorno<0){
				Conexao.rollback(connect);
				return -2;
			}
			else if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.setTxtResposta: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static int insertHistorico(int cdAtendimento, Ocorrencia historico) {
		return insertHistorico(cdAtendimento, historico, null);
	}

	public static int insertHistorico(int cdAtendimento, Ocorrencia historico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			TipoOcorrencia tipo = TipoOcorrenciaDAO.get(historico.getCdTipoOcorrencia(), connect);
			int stAtendimento = ST_ABERTO;
			switch(tipo.getTpAcao()){
				case TipoOcorrenciaServices.ADMISSAO:
					stAtendimento = ST_ABERTO;
					break;
				case TipoOcorrenciaServices.PREVISAO:
				case TipoOcorrenciaServices.RELEVANCIA:
				case TipoOcorrenciaServices.AGENDAMENTO:
				case TipoOcorrenciaServices.ATRIBUICAO:
				case TipoOcorrenciaServices.TRANSFERENCIA:
				case TipoOcorrenciaServices.ARQUIVO:
				case TipoOcorrenciaServices.OUTROS:
					stAtendimento = ST_ANALISE;
					break;
				case TipoOcorrenciaServices.CONCLUSAO:
					stAtendimento = ST_CONCLUIDO;
					break;
				case TipoOcorrenciaServices.REABERTURA:
					stAtendimento = ST_REABERTO;
					break;
			}

			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_atendimento SET st_atendimento=? WHERE cd_atendimento=?");
			pstmt.setInt(1, stAtendimento);
			pstmt.setInt(2, cdAtendimento);
			pstmt.executeUpdate();

			int retorno = OcorrenciaDAO.insert(historico, connect);
			if(retorno<0){
				Conexao.rollback(connect);
				return -1;
			}
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.insertHistorico: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.insertHistorico: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getHistorico(int cdAtendimento) {
		return getHistorico(cdAtendimento, null);
	}

	public static ResultSetMap getHistorico(int cdAtendimento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, G.*, D.*, E.*, F.*, H.*, I.cd_arquivo, I.nm_arquivo, I.dt_arquivamento " +
					"FROM crm_ocorrencia A " +
					"JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) " +
					"JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) " +
					"JOIN crm_tipo_ocorrencia G ON (B.cd_tipo_ocorrencia = G.cd_tipo_ocorrencia) " +
					"LEFT OUTER JOIN seg_usuario D ON (A.cd_atendente = D.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa E ON (D.cd_pessoa = E.cd_pessoa) " +
					"LEFT OUTER JOIN crm_central_atendimento F ON (A.cd_central=F.cd_central) " +
					"LEFT OUTER JOIN grl_ocorrencia_arquivo H ON (A.cd_ocorrencia=H.cd_ocorrencia) " +
					"LEFT OUTER JOIN grl_arquivo I ON (H.cd_arquivo=I.cd_arquivo) " +
					"WHERE A.cd_atendimento = ? " +
					"ORDER BY dt_ocorrencia");
			pstmt.setInt(1, cdAtendimento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoServices.getHistorico: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
