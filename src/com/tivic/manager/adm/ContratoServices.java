package com.tivic.manager.adm;

import java.sql.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.text.*;

import com.tivic.manager.alm.GrupoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.ptc.*;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Recursos;
import com.tivic.manager.util.Util;


public class ContratoServices {

	public static final int tpCONTRATADA  = 0;
	public static final int tpCONTRATANTE = 1;
	public static final int tpCONVENENTE = 2;
	public static final int tpCONVENIADA = 3;

	public static final int ALL_CONTRATOS = 0;
	public static final int ALL_CONVENIOS = 1;
	public static final int ALL_CONTRATOS_CONVENIOS = 2;

	public static final int gnSERVICO      = 0;
	public static final int gnCONVENIO     = 1;
	public static final int gnMICROCREDITO = 2;
	public static final int gnCOMPRA_VENDA = 3;
	public static final int gnEDUCACAO     = 4;
	public static final int gnSEGURO       = 5;
	public static final int gnCONSORCIO    = 6;
	public static final int gnPLANO_SAUDE  = 7;

	public static final int AMORT_NENHUM = 0;
	public static final int PRICE = 1;
	public static final int SAC   = 2;
	public static final int MISTO = 3;

	public static final int ST_INATIVO   = 0;
	public static final int ST_CANCELADO = 0;
	public static final int ST_ATIVO     = 1;
	public static final int ST_ANALISE   = 2;
	public static final int ST_REPROVADO = 3;
	public static final int ST_ENCERRADO = 4;

	public static final int DESC_PERCENTUAL = 0;
	public static final int DESC_VALOR      = 1;

	public static final int SQL_PRODUTO_SERVICO    = 0;
	public static final int SQL_PRODUTO_REFERENCIA = 1;

	public static final int DEL_ALL_PARCELAS           = 0;
	public static final int DEL_ALL_PARCELAS_EM_ABERTO = 1;

	public static final String[] tipoItemContrato = {"Produtos/Serviços","Bens (Referência)"};

	public static final String[] situacaoContrato = {"Cancelado", "Ativo", "Em Andamento"};
//	public static final String[] situacaoContrato = {"Inativo","Ativo","Em Andamento","Reserva Administrativa","Encerrado","Cancelado"};

	public static final String[] tipoContrato = {"Contratada","Contratante"};

	public static final String[][] tipoConvenio = {new String[] {Integer.toString(tpCONVENENTE), "Convenente"},
												   new String[] {Integer.toString(tpCONVENIADA), "Conveniada"}};

	public static final String[] tipoAmortizacao = {"Nenhum", "PRICE", "SAC", "Misto"};

	public static float getValorParcela(int qtDiasCarencia, int qtParcelas, float prJuros, float vlTotal)	{
		prJuros = prJuros / 100;
		// (juros * power((1 + prJuros), qtParcelas) / (power((1 + prJuros), qtParcelas) - 1))
		double vlFator = prJuros * Math.pow((1+prJuros), qtParcelas) / (Math.pow((1+prJuros),qtParcelas)-1);
		return (float)(vlTotal * vlFator);
	}

	public static Result insert(Contrato objeto) {
		return insert(objeto, 0, true, null, null, null);
	}

	public static Result insert(Contrato objeto, int cdUsuario) {
		return insert(objeto, cdUsuario, true, null, null, null);
	}

	public static Result insert(Contrato objeto, int cdUsuario, boolean registerDoc, ArrayList<ContratoDesconto> descontos,
			ArrayList<ContratoProdutoServico> produtosServicos) {
		return insert(objeto, cdUsuario, registerDoc, descontos, produtosServicos, null);
	}

	public static Result insert(Contrato objeto, int cdUsuario, boolean registerDoc, ArrayList<ContratoDesconto> descontos,
			ArrayList<ContratoProdutoServico> produtosServicos, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Verifica campos
			if (produtosServicos!=null && produtosServicos.size()>0 && produtosServicos.get(0).getCdReferencia()>0)	{
				ResultSet rs = connection.prepareStatement("SELECT * FROM adm_contrato A, adm_contrato_produto_servico B " +
						                                   "WHERE A.cd_contrato = B.cd_contrato " +
						                                   "  AND A.st_contrato       <> "+ST_CANCELADO+
						                                   "  AND B.cd_empresa         = "+produtosServicos.get(0).getCdEmpresa()+
						                                   "  AND B.cd_produto_servico = "+produtosServicos.get(0).getCdProdutoServico()+
						                                   "  AND B.cd_referencia      = "+produtosServicos.get(0).getCdReferencia()).executeQuery();
				if(rs.next())
					return new Result(-1, "Lançamento do contrato em duplicidade!");
			}
			// Tabela de Amortização PRICE
			if(objeto.getTpAmortizacao()==PRICE && objeto.getDtAssinatura()!=null && objeto.getDtPrimeiraParcela()!=null)	{
				int nrDiasCarencia = Math.round((objeto.getDtAssinatura().getTimeInMillis() - objeto.getDtPrimeiraParcela().getTimeInMillis()) / (365*24*60*60*1000));
				float vlParcela = getValorParcela(nrDiasCarencia, objeto.getNrParcelas(), objeto.getPrJuros(), objeto.getVlContrato());
				objeto.setVlParcelas(vlParcela);
			}
			// Documento
			int cdDocumento = objeto.getCdDocumento();
			Documento documento = null;

			if (registerDoc && cdDocumento <= 0) {
				/* localiza o tipo de documento referente a Contrato */
				int cdTipoDocumento = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_PTC_CONTRATO", 0, 0, connection);
				if (cdTipoDocumento <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Tipo de documento padrão para contratos não localizado!");
				}
				// Fase Inicial
				int cdFaseInicial = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, connection);
				// Descobrindo o Setor
				Usuario usuario = UsuarioDAO.get(cdUsuario, connection);
				int cdSetor = 0;
				if(usuario!=null)	{
					ResultSetMap rsm = com.tivic.manager.srh.DadosFuncionaisServices.getSetorOf(objeto.getCdEmpresa(), usuario.getCdPessoa());
					if(rsm.next())
						cdSetor = rsm.getInt("cd_setor");
				}
				// Insere o Documento
				documento = new Documento(0/*cdDocumento*/, 0/*cdArquivo*/, cdSetor, cdUsuario, ""/*nmLocalOrigem*/,
										  objeto.getDtAssinatura()/*dtProtocolo*/, com.tivic.manager.ptc.DocumentoServices.TP_PUBLICO/*tpDocumento*/,
										  ""/*txtObservacao */, "", /*idDocumento*/ objeto.getNrContrato()/*nrDocumento*/,
										  cdTipoDocumento, 0 /* cdServico */, 0 /* cdAtendimento */, "" /*txtDocumento*/,
										  cdSetor/*cdSetorAtual*/, 0/*cdSituacaoDocumento*/, cdFaseInicial, objeto.getCdEmpresa(), 0, 
										  0 /*tpPrioridade*/, 0/*cdDocumentoSuperior*/, null /*dsAssunto*/, null /*nrAtendimento*/, 0/*lgNotificacao*/, 
										  0 /*cdTipoDocumentoAnterior*/, null/*nrDocumentoExterno*/, null/*nrAssunto*/,null,null, 0, 1);
				ArrayList<DocumentoPessoa> solicitantes = new ArrayList<DocumentoPessoa>();
				solicitantes.add(new DocumentoPessoa(0, objeto.getCdPessoa(), "Contratante"));
				sol.util.Result result = DocumentoServices.insert(documento, solicitantes, connection, new ArrayList<FormularioAtributoValor>());
				if(result.getCode() < 0)
					System.out.println("ERRO: "+result.getMessage()+"\n"+result.getDetail());
				cdDocumento = result.getCode();
				if (cdDocumento <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					result.setMessage("Erro ao tentar incluir documento no protocolo. "+result.getMessage());
					return result;
				}
				objeto.setCdDocumento(cdDocumento);
			}
			/* Rotina, em caráter temporário, para geração de número de contrato conforme moldes da Plandonto.
			 * Propõe-se substituição posterior por rotina genérica de geração de número de contrato
			 */
			if (objeto.getTpContrato()==tpCONVENENTE && objeto.getGnContrato()==gnCONVENIO) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(cd_contrato) " +
						"FROM adm_contrato ");
				ResultSet rs = pstmt.executeQuery();
				int cdContrato = !rs.next() ? 1 : rs.getInt(1)+1;
				String nrContrato = Util.formatDateTime(objeto.getDtInicioVigencia(), "MMyy") + "J" + new DecimalFormat("00000").format(cdContrato);
				objeto.setNrContrato(nrContrato);
			}
			else if (objeto.getTpContrato()==tpCONTRATADA && objeto.getGnContrato()==gnPLANO_SAUDE) {
				String nrContrato = "";
				if (objeto.getCdConvenio()>0) {
					Contrato convenio = ContratoDAO.get(objeto.getCdConvenio(), connection);
					nrContrato = convenio.getNrContrato()!=null && convenio.getNrContrato().length()==10 ? convenio.getNrContrato().substring(6) : "";
					ResultSet rs = connection.prepareStatement("SELECT COUNT(*) FROM adm_contrato " +
							                                   "WHERE tp_contrato = " +tpCONTRATADA+
							                                   "  AND cd_convenio = " + convenio.getCdConvenio()).executeQuery();
					int countConveniados = !rs.next() ? 1 : rs.getInt(1)+1;
					nrContrato += new DecimalFormat("000").format(countConveniados) + "0.00";
				}
				else {
					ResultSet rs = connection.prepareStatement("SELECT COUNT(*) FROM adm_contrato " +
							                                   "WHERE tp_contrato = " +tpCONTRATADA+
							                                   "  AND cd_convenio IS NULL").executeQuery();
					int countConveniados = !rs.next() ? 1 : rs.getInt(1)+1;
					nrContrato = "F" + new DecimalFormat("000000").format(countConveniados) + "0.00";
				}
				objeto.setNrContrato(nrContrato);
			}
			Result result = new Result(1);
			result.addObject("nrContrato", objeto.getNrContrato());
			objeto.setCdContrato(ContratoDAO.insert(objeto, connection));

			if (objeto.getCdContrato() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(objeto.getCdContrato(), "Erro ao tentar incluir Contrato!");
			}
			/*
			 * Incluido DESCONTOS
			 */
			for (int i=0; descontos!=null && i<descontos.size(); i++) {
				descontos.get(i).setCdContrato(objeto.getCdContrato());
				int code = ContratoDescontoDAO.insert(descontos.get(i), connection);
				if (code <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(code, "Erro ao incluir descontos!");
				}
			}
			/*
			 * Incluido PRODUTOS E SERVIÇOS
			 */
			for (int i=0; produtosServicos!=null && i<produtosServicos.size(); i++) {
				produtosServicos.get(i).setCdContrato(objeto.getCdContrato());
				int code = ContratoProdutoServicoDAO.insert(produtosServicos.get(i), connection);
				if (code<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(code, "Erro ao incluir produtos/serviços!");
				}
			}

			if (isConnectionNull)
				connection.commit();

			result.setCode(objeto.getCdContrato());
			if (documento!=null)
				result.addObject("nrProtocolo", documento.getNrDocumento());

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar incluir Contrato!");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result update(Contrato objeto) {
		return update(objeto, null, null, null);
	}

	public static Result update(Contrato objeto, ArrayList<ContratoDesconto> descontos) {
		return update(objeto, descontos, null, null);
	}

	public static Result update(Contrato objeto, ArrayList<ContratoDesconto> descontos, ArrayList<ContratoProdutoServico> produtosServicos) {
		return update(objeto, descontos, produtosServicos, null);
	}

	public static Result update(Contrato objeto, ArrayList<ContratoDesconto> descontos, ArrayList<ContratoProdutoServico> produtosServicos, Connection connection)
	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			Result result = new Result(ContratoDAO.update(objeto, connection));
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			if (produtosServicos!=null) {
				connection.prepareStatement("DELETE FROM adm_contrato_produto_servico A " +
											"WHERE A.cd_contrato = " +objeto.getCdContrato()+
											"  AND NOT EXISTS (SELECT * FROM adm_contrato_prd_srv_dep " +
											"				   WHERE cd_contrato        = A.cd_contrato " +
											"					 AND cd_produto_servico = A.cd_produto_servico)").executeUpdate();

				for (int i=0; produtosServicos!=null && i<produtosServicos.size(); i++) {
					produtosServicos.get(i).setCdContrato(objeto.getCdContrato());
					ContratoProdutoServico item = ContratoProdutoServicoDAO.get(objeto.getCdContrato(), produtosServicos.get(i).getCdProdutoServico(), connection);
					int code = 1;
					if (item==null)
						code = ContratoProdutoServicoDAO.insert(produtosServicos.get(i), connection);
					else
						code = ContratoProdutoServicoDAO.update(produtosServicos.get(i), connection);
					// Verifica retorno
					if (code<=0 && isConnectionNull)
						Conexao.rollback(connection);
					if(code <= 0)
						return new Result(code, "Erro ao tentar incluir produtos/serviço!");
				}
			}

			if (descontos!=null) {
				connection.prepareStatement("DELETE FROM adm_contrato_desconto WHERE cd_contrato = "+objeto.getCdContrato()).execute();

				for (int i=0; descontos!=null && i<descontos.size(); i++) {
					descontos.get(i).setCdContrato(objeto.getCdContrato());
					int code = ContratoDescontoDAO.insert(descontos.get(i), connection);
					if (code <=0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(code, "Erro ao tentar incluir desconto(s)!");
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			result.setCode(objeto.getCdContrato());
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar atualizar contrato!");
		}
		finally {
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
	}

	public static ResultSetMap getContasReceberConvenio(int cdConvenio, int nrMes, int nrAno) {
		GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1);
		GregorianCalendar dtFinal = Util.getUltimoDiaMes(nrMes, nrAno);
		return getContasReceberConvenio(cdConvenio, dtInicial, dtFinal, null);
	}

	public static ResultSetMap getContasReceberConvenio(int cdConvenio, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getContasReceberConvenio(cdConvenio, dtInicial, dtFinal, null);
	}

	public static ResultSetMap getContasReceberConvenio(int cdConvenio, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_conta_receber, A.ds_historico, " +
					"A.vl_conta, A.vl_abatimento, A.vl_acrescimo, B.nr_contrato, C.nm_pessoa, A.cd_pessoa, B.cd_contrato, " +
					"(SELECT SUM(D.vl_recebido - D.vl_juros - D.vl_multa + D.vl_desconto - D.vl_tarifa_cobranca) " +
					" FROM adm_movimento_conta_receber D " +
					" WHERE D.cd_conta_receber = A.cd_conta_receber) AS vl_pago " +
					"FROM adm_conta_receber A, adm_contrato B, grl_pessoa C " +
					"WHERE A.cd_contrato = B.cd_contrato " +
					"  AND B.cd_pessoa = C.cd_pessoa " +
					"  AND B.cd_convenio = ? " +
					"  AND A.st_conta = ? " +
					"  AND A.dt_vencimento >= ? " +
					"  AND A.dt_vencimento <= ? ");
			pstmt.setInt(1, cdConvenio);
			pstmt.setInt(2, ContaReceberServices.ST_EM_ABERTO);
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtFinal));

			return new ResultSetMap(pstmt.executeQuery());
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

	public static Result gerarFaturaConvenio(int cdConvenio, int nrMes, int nrAno, int cdUsuario, ArrayList<Integer> cdsContasReceber) {
		GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1);
		GregorianCalendar dtFinal = Util.getUltimoDiaMes(nrMes, nrAno);
		return gerarFaturaConvenio(cdConvenio, dtInicial, dtFinal, cdUsuario, cdsContasReceber, null);
	}

	public static Result gerarFaturaConvenio(int cdConvenio, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdUsuario,
			ArrayList<Integer> cdsContasReceber) {
		return gerarFaturaConvenio(cdConvenio, dtInicial, dtFinal, cdUsuario, cdsContasReceber, null);
	}

	public static Result gerarFaturaConvenio(int cdConvenio, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int cdUsuario, ArrayList<Integer> cdsContasReceber, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Contrato convenio = ContratoDAO.get(cdConvenio, connection);

			int cdTipoDocumentoDefault = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CONTRATO", 0, 0, connection);

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_conta_receber, A.ds_historico, " +
					"A.vl_conta, A.vl_abatimento, A.vl_acrescimo, B.nr_contrato, C.nm_pessoa, A.cd_pessoa, B.cd_contrato, " +
					"(SELECT SUM(D.vl_recebido - D.vl_juros - D.vl_multa + D.vl_desconto - D.vl_tarifa_cobranca) " +
					" FROM adm_movimento_conta_receber D " +
					" WHERE D.cd_conta_receber = A.cd_conta_receber) AS vl_pago " +
					"FROM adm_conta_receber A, adm_contrato B, grl_pessoa C " +
					"WHERE A.cd_contrato = B.cd_contrato " +
					"  AND B.cd_pessoa = C.cd_pessoa " +
					"  AND B.cd_convenio = ? " +
					"  AND A.st_conta = ? " +
					"  AND A.dt_vencimento >= ? " +
					"  AND A.dt_vencimento <= ? ");
			pstmt.setInt(1, cdConvenio);
			pstmt.setInt(2, ContaReceberServices.ST_EM_ABERTO);
			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtInicial));
			pstmt.setTimestamp(4, Util.convCalendarToTimestamp(dtFinal));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			if (rsm.size()==0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "O sistema acusa a inexistência de faturas individuais em aberto no período de referência informado.");
			}

			float vlTotal = 0;
			ArrayList<ContaReceberCategoria> categoriasFatura = new ArrayList<ContaReceberCategoria>();
			ArrayList<ContaReceberEvento> eventosFatura = new ArrayList<ContaReceberEvento>();
			while (rsm.next()) {
				if (cdsContasReceber!=null && cdsContasReceber.size()>0) {
					int i = 0;
					for (i=0; i<cdsContasReceber.size(); i++)
						if (cdsContasReceber.get(i) == rsm.getInt("cd_conta_receber"))
							break;
					if (i >= cdsContasReceber.size())
						continue;
				}
				ArrayList<ContaReceberCategoria> categoriasConta = new ArrayList<ContaReceberCategoria>();
				float vlConta = rsm.getFloat("vl_conta") + rsm.getFloat("vl_acrescimo") - rsm.getFloat("vl_abatimento");
				float vlPendente = vlConta - rsm.getFloat("vl_pago");
				vlTotal += vlPendente;

				pstmt = connection.prepareStatement("SELECT * " +
						"FROM adm_conta_receber_categoria " +
						"WHERE cd_conta_receber = ?");
				pstmt.setInt(1, rsm.getInt("cd_conta_receber"));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					categoriasConta.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
							rs.getInt("cd_categoria_economica"),
							rs.getFloat("vl_conta_categoria") * vlPendente/vlConta, rs.getInt("cd_centro_custo")));
				}
				for (int i=0; i<categoriasConta.size(); i++) {
					ContaReceberCategoria categoria = null;
					for (int j=0; j<categoriasFatura.size(); j++)
						if (categoriasFatura.get(j).getCdCategoriaEconomica() == categoriasConta.get(i).getCdCategoriaEconomica()) {
							categoria = categoriasFatura.get(j);
							break;
						}
					if (categoria!=null)
						categoria.setVlContaCategoria(categoria.getVlContaCategoria() + categoriasConta.get(i).getVlContaCategoria());
					else
						categoriasFatura.add(categoriasConta.get(i));
				}

				pstmt = connection.prepareStatement("SELECT * " +
						"FROM adm_conta_receber_evento " +
						"WHERE cd_conta_receber = ? " +
						"  AND st_evento = ?");
				pstmt.setInt(1, rsm.getInt("cd_conta_receber"));
				pstmt.setInt(2, ContaReceberEventoServices.ST_ATIVO);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					eventosFatura.add(new ContaReceberEvento(0 /*cdContaReceber*/,
							rs.getInt("cd_evento_financeiro") /*cdEventoFinanceiro*/,
							rs.getFloat("vl_evento_financeiro") * vlPendente/vlConta /*vlEventoFinanceiro*/,
							0 /*cdContaReceberEvento*/,
							rs.getInt("cd_pessoa") /*cdPessoa*/,
							rs.getInt("cd_contrato"),
							ContaReceberEventoServices.ST_ATIVO));
				}
			}
			if (vlTotal<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "O sistema acusa a inexistência de faturas individuais em aberto no período de referência informado.");
			}

			int nrDiaVenc = convenio.getNrDiaVencimento();
			GregorianCalendar dtVencimento = new GregorianCalendar();
			dtVencimento = new GregorianCalendar(dtInicial.get(Calendar.YEAR), dtInicial.get(Calendar.MONTH), 1);
			int diasMeses = Recursos.diasMeses[dtVencimento.get(Calendar.MONTH)];
			diasMeses = dtVencimento.get(Calendar.MONTH)==Calendar.FEBRUARY && dtVencimento.get(Calendar.YEAR)%4==0 ? diasMeses+1 : diasMeses;
			dtVencimento.set(Calendar.DAY_OF_MONTH, nrDiaVenc>diasMeses ? diasMeses : nrDiaVenc);

			Usuario usuario = cdUsuario<=0 ? null : UsuarioDAO.get(cdUsuario, connection);
			int cdResponsavel = usuario==null ? 0 : usuario.getCdPessoa();

			ContratoNegociacao negociacao = new ContratoNegociacao(convenio.getCdContrato(),
					0 /*cdNegociacao*/,
					new GregorianCalendar() /*dtNegociacao*/,
					cdUsuario /*cdUsuario*/,
					cdResponsavel /*cdResponsavel*/,
					0 /*cdDocumentoSaida*/,
					0 /*prMultaMora*/,
					0 /*prJurosMora*/,
					0 /*vlMultaMora*/,
					0 /*vlJurosMora*/,
					0 /*prMultaPenal*/,
					0 /*vlDesconto*/,
					0 /*vlAcrescimo*/,
					1 /*qtParcelas*/,
					dtVencimento /*dtPrimeiroVencimento*/,
					nrDiaVenc /*nrDiaVencimento*/,
					vlTotal /*vlParcela*/,
					vlTotal /*vlFinal*/,
					0 /*prCarenciaAnterior*/,
					0 /*vlCarencia*/,
					0 /*vlPago*/,
					"" /*txtObservacao*/,
					0 /*vlMultaPenal*/,
					0 /*vlCorridoContrato*/,
					ContratoNegociacaoServices.ST_ENCERRADO /*stNegociacao*/,
					ContratoNegociacaoServices.TP_FATURAMENTO_CONVENIO /*tpNegociacao*/,
					0 /*lgCarencia*/,
					0 /*prCorridoContrato*/);
			ArrayList<ContaReceberNegociacao> contas = new ArrayList<ContaReceberNegociacao>();
			rsm.beforeFirst();
			while (rsm.next()) {
				if (cdsContasReceber!=null && cdsContasReceber.size()>0) {
					int i = 0;
					for (i=0; i<cdsContasReceber.size(); i++)
						if (cdsContasReceber.get(i) == rsm.getInt("cd_conta_receber"))
							break;
					if (i >= cdsContasReceber.size())
						continue;
				}
				contas.add(new ContaReceberNegociacao(cdConvenio /*cdContrato*/,
						0 /*cdNegociacao*/,
						rsm.getInt("cd_conta_receber")));
			}
			HashMap<String, Object> resultsNeg = ContratoNegociacaoServices.insert(negociacao, contas, connection);
			if (resultsNeg==null || ((Integer)resultsNeg.get("cdNegociacao"))<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Problemas reportados ao registar fatura. Entre em contato com o suporte técnico.");
			}

			int cdNegociacao = (Integer)resultsNeg.get("cdNegociacao");
			pstmt = connection.prepareStatement("UPDATE adm_conta_receber " +
					"SET st_conta = ? " +
					"WHERE cd_conta_receber IN (SELECT cd_conta_receber " +
					"							FROM adm_conta_receber_negociacao " +
					"							WHERE cd_contrato = ? " +
					"							  AND cd_negociacao = ?)");
			pstmt.setInt(1, ContaReceberServices.ST_REFATURADA);
			pstmt.setInt(2, cdConvenio);
			pstmt.setInt(3, cdNegociacao);
			pstmt.execute();

			ContaReceber contaReceber = new ContaReceber(0 /*cdContaReceber*/,
					convenio.getCdPessoa(),
					convenio.getCdEmpresa(),
					convenio.getCdContrato(),
					0 /*cdContaOrigem*/,
					0 /*cdDocumentoSaida*/,
					convenio.getCdContaCarteira(),
					convenio.getCdConta(),
					0 /*cdFrete*/,
					"" /*nrDocumento*/,
					"" /*idContaReceber*/,
					1 /*nrParcela*/,
					"" /*nrReferencia*/,
					cdTipoDocumentoDefault /*cdTipoDocumento*/,
					"FATURA " + Recursos.meses[dtVencimento.get(Calendar.MONTH)].substring(0, 3).toUpperCase() + "/" + dtVencimento.get(Calendar.YEAR) /*dsHistorico*/,
					dtVencimento /*dtVencimento*/,
					new GregorianCalendar() /*dtEmissao*/,
					null /*dtRecebimento*/,
					null /*dtProrrogacao*/,
					Double.valueOf( Float.toString(vlTotal)) /*vlConta*/,
					0.0d /*vlAbatimento*/,
					0.0d /*vlAcrescimo*/,
					0.0d /*vlRecebido*/,
					ContaReceberServices.ST_EM_ABERTO /*stConta*/,
					ContaReceberServices.UNICA_VEZ /*tpFrequencia*/,
					1 /*qtParcelas*/,
					ContaReceberServices.TP_PARCELA /*tpContaReceber*/,
					cdNegociacao /*cdNegociacao*/,
					"" /*txtObservacao*/,
					0 /*cdPlanoPagamento*/,
					0 /*cdFormaPagamento*/,
					new GregorianCalendar(),
					dtVencimento,
					0/*cdTurno*/,
					0.0d/*prJuros*/,
					0.0d/*prMulta*/,
					0/*lgProtesto*/);

			pstmt = connection.prepareStatement("SELECT SUM(D.vl_conta_categoria) AS vl_categoria, D.cd_categoria_economica " +
					"FROM adm_conta_receber_categoria D, adm_conta_receber_negociacao B " +
					"WHERE D.cd_conta_receber = B.cd_conta_receber " +
					"  AND B.cd_contrato = ? " +
					"  AND B.cd_negociacao = ? " +
					"GROUP BY D.cd_categoria_economica");
			pstmt.setInt(1, cdConvenio);
			pstmt.setInt(2, cdNegociacao);
			rsm = new ResultSetMap(pstmt.executeQuery());

			Result result = null;
			if ((result = ContaReceberServices.insert(contaReceber, categoriasFatura, null /*tituloCredito*/, true /*ignorarDuplicidade*/,
					connection)) == null ||
					result.getCode()<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Problemas reportados ao registar fatura. Entre em contato com o suporte técnico.");
			}
			for (int i=0; i<eventosFatura.size(); i++) {
				eventosFatura.get(i).setCdContaReceber(result.getCode());
				if (ContaReceberEventoDAO.insert(eventosFatura.get(i), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas reportados ao registrar fatura. Entre em contato com o suporte técnico.");
				}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Fatura registrada com sucesso.");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erros reportados ao registrar fatura. Anote a mensagem de erro e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result reativarDependentes(int cdContrato, int cdProdutoServico, ArrayList<HashMap<String, Object>> beneficiarios) {
		return reativarDependentes(cdContrato, cdProdutoServico, beneficiarios, null);
	}

	public static Result reativarDependentes(int cdContrato, int cdProdutoServico, ArrayList<HashMap<String, Object>> beneficiarios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			for (int i=0; beneficiarios!=null && i<beneficiarios.size(); i++) {
				HashMap<String, Object> hashBeneficiario = beneficiarios.get(i);
				int cdDependente = (Integer)hashBeneficiario.get("cdBeneficiario");
				int cdDependencia = hashBeneficiario.get("cdDependencia")==null ? 0 : (Integer)hashBeneficiario.get("cdDependencia");

				ContratoDependente dependente = ContratoDependenteDAO.get(cdContrato, cdDependente, cdDependencia, connection);
				Pessoa beneficiario = PessoaDAO.get(cdDependente, connection);

				if (dependente.getStDependente() == ContratoDependenteServices.ST_ATIVO) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Certifique-se de que o " + beneficiario.getNmPessoa() + " já esteja ativo.");
				}

				dependente.setStDependente(ContratoDependenteServices.ST_ATIVO);
				dependente.setDtDesvinculacao(null);
				ContratoPrdSrvDep prdSrvDep = ContratoPrdSrvDepDAO.get(cdContrato, cdProdutoServico, cdDependente, cdDependencia, connection);
				Result result = null;
				if ((result = ContratoDependenteServices.update(dependente, prdSrvDep, connection))==null || result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erros reportados ao atualizar status do beneficiário " + beneficiario.getNmPessoa() + ".");
				}
			}

			Result result = gerarParcelasOfProdutoServico(cdContrato, cdProdutoServico, beneficiarios, connection);
			if (result==null || result.getCode()<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao reativar dependentes selecionados. Anote a mensagem de erro " +
					"e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result cancelDependentes(int cdContrato, int cdProdutoServico, ArrayList<HashMap<String, Object>> dependentes, GregorianCalendar dtCancelamento) {
		return cancelDependentes(cdContrato, cdProdutoServico, dependentes, null);
	}

	public static Result cancelDependentes(int cdContrato, int cdProdutoServico, ArrayList<HashMap<String, Object>> dependentes, GregorianCalendar dtCancelamento,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Contrato contrato = ContratoDAO.get(cdContrato, connection);
			int cdEventoAdesao = ProdutoServicoServices.getEventoFinanceiro(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.EVT_ADESAO, connection);
			int cdEventoParcela = ProdutoServicoServices.getEventoFinanceiro(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.EVT_CONTRATACAO, connection);
			EventoFinanceiro eventoAdesao = cdEventoAdesao<=0 ? null : EventoFinanceiroDAO.get(cdEventoAdesao, connection);
			EventoFinanceiro eventoParcela = cdEventoParcela<=0 ? null : EventoFinanceiroDAO.get(cdEventoParcela, connection);
			int cdCategoriaAdesao = eventoAdesao!=null && eventoAdesao.getCdCategoriaEconomica()!=0 ? eventoAdesao.getCdCategoriaEconomica() :
				ProdutoServicoServices.getCategoriaEconomica(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.CAT_RECEITA);
			int cdCategoriaParcela = eventoParcela!=null && eventoParcela.getCdCategoriaEconomica()!=0 ? eventoParcela.getCdCategoriaEconomica() :
				ProdutoServicoServices.getCategoriaEconomica(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.CAT_DESPESA);
			cdCategoriaAdesao = cdCategoriaAdesao<=0 ? ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, 0, connection) : cdCategoriaAdesao;
			cdCategoriaParcela = cdCategoriaParcela<=0 ? ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, 0, connection) : cdCategoriaParcela;

			for (int i=0; dependentes!=null && i<dependentes.size(); i++) {
				HashMap<String, Object> hashBeneficiario = dependentes.get(i);
				int cdDependente = (Integer)hashBeneficiario.get("cdDependente");
				int cdDependencia = hashBeneficiario.get("cdDependencia")==null ? 0 : (Integer)hashBeneficiario.get("cdDependencia");

				ContratoDependente dependente = ContratoDependenteDAO.get(cdContrato, cdDependente, cdDependencia, connection);
				Pessoa beneficiario = PessoaDAO.get(cdDependente, connection);

				if (dependente.getStDependente() == ContratoDependenteServices.ST_INATIVO) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Certifique-se de que o " + beneficiario.getNmPessoa() + " já esteja cancelado.");
				}

				PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
						"FROM adm_conta_receber_evento A, adm_conta_receber B " +
						"WHERE A.cd_conta_receber = B.cd_conta_receber " +
						"  AND B.cd_contrato = ? " +
						"  AND A.cd_pessoa = ? " +
						"  AND B.dt_vencimento > ? " +
						"  AND B.st_conta = ? " +
						"  AND NOT EXISTS (SELECT C.cd_conta_receber " +
						"				   FROM adm_movimento_conta_receber C " +
						"				   WHERE C.cd_conta_receber = B.cd_conta_receber)");
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, cdDependente);
				pstmt.setTimestamp(3, Util.convCalendarToTimestamp(dtCancelamento));
				pstmt.setInt(4, ContaReceberServices.ST_EM_ABERTO);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					pstmt = connection.prepareStatement("UPDATE adm_conta_receber " +
							"SET vl_conta = vl_conta - ? " +
							"WHERE cd_conta_receber = ?");
					pstmt.setFloat(1, rs.getFloat("vl_evento_financeiro"));
					pstmt.setInt(2, rs.getInt("cd_conta_receber"));
					pstmt.execute();

					int cdCategoriaEconomica = rs.getInt("tp_conta_receber")==ContaReceberServices.TP_TAXA_ADESAO ? cdCategoriaAdesao : cdCategoriaParcela;
					pstmt = connection.prepareStatement("UPDATE adm_conta_receber_categoria " +
							"SET vl_conta_categoria = vl_conta_categoria - ? " +
							"WHERE cd_conta_receber = ? " +
							"  AND cd_categoria_economica = ?");
					pstmt.setFloat(1, rs.getFloat("vl_evento_financeiro"));
					pstmt.setInt(2, rs.getInt("cd_conta_receber"));
					pstmt.setInt(3, cdCategoriaEconomica);
					pstmt.execute();

					if (ContaReceberEventoDAO.delete(rs.getInt("cd_conta_receber"), rs.getInt("cd_evento_financeiro"), rs.getInt("cd_conta_receber_evento"), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Problemas encontrados ao atualizar fatura em que há eventos financeiros relacionados " +
								"ao beneficiário " + beneficiario.getNmPessoa() + ".");
					}
				}

				pstmt = connection.prepareStatement("UPDATE adm_contrato_produto_servico " +
						"SET qt_produto_servico = qt_produto_servico - 1 " +
						"WHERE cd_contrato = ? " +
						"  AND cd_produto_servico = ? " +
						"  AND cd_produto_servico IN (SELECT cd_produto_servico " +
						"							  FROM adm_contrato_prd_srv_dep " +
						"							  WHERE cd_contrato = ? " +
						"								AND cd_produto_servico = ? " +
						"								AND cd_dependente = ? " +
						"								AND cd_dependencia = ?)");
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, cdProdutoServico);
				pstmt.setInt(3, cdContrato);
				pstmt.setInt(4, cdProdutoServico);
				pstmt.setInt(5, cdDependente);
				pstmt.setInt(6, cdDependencia);
				pstmt.execute();

				pstmt = connection.prepareStatement("DELETE " +
						"FROM adm_contrato_prd_srv_dep " +
						"WHERE cd_contrato = ? " +
						"  AND cd_produto_servico = ? " +
						"  AND cd_dependente = ? " +
						"  AND cd_dependencia = ?");
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, cdProdutoServico);
				pstmt.setInt(3, cdDependente);
				pstmt.setInt(4, cdDependencia);

				dependente.setStDependente(ContratoDependenteServices.ST_INATIVO);
				dependente.setDtDesvinculacao(dtCancelamento);
				if (ContratoDependenteDAO.update(dependente, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas encontrados ao atualizar dados do beneficiário " + beneficiario.getNmPessoa() + ".");
				}
			}

			PreparedStatement pstmt = connection.prepareCall("SELECT A.cd_conta_receber " +
					"FROM adm_conta_receber A " +
					"WHERE A.cd_contrato = ? " +
					"  AND A.st_conta = ? " +
					"  AND A.vl_conta = 0 " +
					"  AND NOT EXISTS (SELECT B.cd_conta_receber " +
					"				   FROM adm_movimento_conta_receber B " +
					"				   WHERE B.cd_conta_receber = A.cd_conta_receber)");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, ContaReceberServices.ST_EM_ABERTO);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), true, false, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Problemas encontrados ao atualizar fatura em que há eventos financeiros relacionados " +
							"ao cancelamento de um ou mais beneficiários selecionados.");
				}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao cancelar dependentes selecionados. Anote a mensagem de erro " +
					"e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result gerarParcelasOfProdutoServico(int cdContrato, int cdProdutoServico, ArrayList<HashMap<String, Object>> beneficiarios) {
		return gerarParcelasOfProdutoServico(cdContrato, cdProdutoServico, beneficiarios, null);
	}

	public static Result gerarParcelasOfProdutoServico(int cdContrato, int cdProdutoServico, ArrayList<HashMap<String, Object>> beneficiarios,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Contrato contrato = ContratoDAO.get(cdContrato, connection);

			int cdTipoDocumentoDefault = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_CONTRATO", 0, 0, connection);

			int cdEventoAdesao = ProdutoServicoServices.getEventoFinanceiro(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.EVT_ADESAO, connection);
			int cdEventoParcela = ProdutoServicoServices.getEventoFinanceiro(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.EVT_CONTRATACAO, connection);
			EventoFinanceiro eventoAdesao = cdEventoAdesao<=0 ? null : EventoFinanceiroDAO.get(cdEventoAdesao, connection);
			EventoFinanceiro eventoParcela = cdEventoParcela<=0 ? null : EventoFinanceiroDAO.get(cdEventoParcela, connection);
			int cdCategoriaAdesao = eventoAdesao!=null && eventoAdesao.getCdCategoriaEconomica()!=0 ? eventoAdesao.getCdCategoriaEconomica() :
				ProdutoServicoServices.getCategoriaEconomica(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.CAT_RECEITA, connection);
			int cdCategoriaParcela = eventoParcela!=null && eventoParcela.getCdCategoriaEconomica()!=0 ? eventoParcela.getCdCategoriaEconomica() :
				ProdutoServicoServices.getCategoriaEconomica(cdProdutoServico, contrato.getCdEmpresa(), GrupoServices.CAT_DESPESA, connection);
			cdCategoriaAdesao = cdCategoriaAdesao<=0 ? ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, 0, connection) : cdCategoriaAdesao;
			cdCategoriaParcela = cdCategoriaParcela<=0 ? ParametroServices.getValorOfParametroAsInteger("CD_CATEGORIA_RECEITAS_DEFAULT", 0, 0, connection) : cdCategoriaParcela;

			if (cdEventoAdesao<=0 || cdEventoParcela<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Certifique-se de que os eventos financeiros referentes às adesões e faturamento do produto ou serviço " +
						"contratado estejam configurados corretamente.");
			}

			if (cdCategoriaAdesao<=0 || cdCategoriaParcela<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Certifique-se de que a classificação econômica de receitas provenientes de adesões e faturas referentes ao produto ou serviço " +
						"contratado esteja configurado corretamente.");
			}

			ArrayList<String> observacoes = new ArrayList<String>();

			for (int i=0; beneficiarios!=null && i<beneficiarios.size(); i++) {
				HashMap<String, Object> hashBeneficiario = beneficiarios.get(i);
				int cdBeneficiario = (Integer)hashBeneficiario.get("cdBeneficiario");
				int cdDependencia = hashBeneficiario.get("cdDependencia")==null ? 0 : (Integer)hashBeneficiario.get("cdDependencia");
				int qtParcelas = hashBeneficiario.get("qtParcelas")==null ? 0 : (Integer)hashBeneficiario.get("qtParcelas");
				float vlAdesao = hashBeneficiario.get("vlAdesao")==null ? 0 : (Float)hashBeneficiario.get("vlAdesao");
				float vlPrimeiraParcela = hashBeneficiario.get("vlPrimeiraParcela")==null ? 0 : (Float)hashBeneficiario.get("vlPrimeiraParcela");
				float vlParcela = hashBeneficiario.get("vlParcela")==null ? 0 : (Float)hashBeneficiario.get("vlParcela");
				boolean isTitular = cdBeneficiario == contrato.getCdPessoa();
				ContratoDependente dependente = isTitular ? null : ContratoDependenteDAO.get(cdContrato, cdBeneficiario, cdDependencia, connection);
				Pessoa beneficiario = PessoaDAO.get(cdBeneficiario, connection);
				ContratoPrdSrvDep itemContratado = ContratoPrdSrvDepDAO.get(cdContrato, cdProdutoServico, cdBeneficiario, cdDependencia, connection);

				PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(nr_parcela) " +
						"FROM adm_conta_receber " +
						"WHERE cd_contrato = ?");
				pstmt.setInt(1, cdContrato);
				ResultSet rs = pstmt.executeQuery();
				int lastNumeroContaReceber = rs.next() ? rs.getInt(1) : 0;

				if (vlAdesao>0) {
					GregorianCalendar dtVencAdesao = isTitular ? contrato.getDtInicioVigencia() : dependente.getDtVinculacao();
					if (dtVencAdesao==null) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Certifique-se de que a data de início de vigência do serviço contratado, para o beneficiário " +
								beneficiario.getNmPessoa() + ", esteja configurado corretamente.");
					}

					pstmt = connection.prepareStatement("SELECT A.st_conta, A.cd_conta_receber, B.vl_evento_financeiro " +
							"FROM adm_conta_receber A, adm_conta_receber_evento B " +
							"WHERE A.cd_conta_receber = B.cd_conta_receber " +
							"  AND B.cd_evento_financeiro = ? " +
							"  AND A.cd_contrato = ? " +
							"  AND B.cd_pessoa = ? " +
							"  AND A.dt_vencimento >= ?  " +
							"  AND A.dt_vencimento <= ? ");
					pstmt.setInt(1, cdEventoAdesao);
					pstmt.setInt(2, cdContrato);
					pstmt.setInt(3, cdBeneficiario);
					pstmt.setTimestamp(4, Util.convCalendarToTimestamp(new GregorianCalendar(dtVencAdesao.get(Calendar.YEAR), dtVencAdesao.get(Calendar.MONTH), 1)));
					pstmt.setTimestamp(5, Util.convCalendarToTimestamp(new GregorianCalendar(dtVencAdesao.get(Calendar.YEAR), dtVencAdesao.get(Calendar.MONTH),
							dtVencAdesao.get(Calendar.MONTH)==Calendar.FEBRUARY && dtVencAdesao.get(Calendar.YEAR)%4==0 ? 29 :
								Recursos.diasMeses[dtVencAdesao.get(Calendar.MONTH)], 23, 59, 999)));
					rs = pstmt.executeQuery();
					int cdContaReceber = !rs.next() ? 0 : rs.getInt("cd_conta_receber");

					if (cdContaReceber>0 && rs.getInt("st_conta")==ContaReceberServices.ST_RECEBIDA) {
						observacoes.add("Não houve possibilidade de atualizar fatura referente à adesão do beneficiário " +
								beneficiario.getNmPessoa() + ", pois a mesma já se encontra em status de recebida/paga.");
					}
					else {
						String dsHistorico = "ADESÃO - " + beneficiario.getNmPessoa();
						dsHistorico = dsHistorico.length()>100 ? dsHistorico.substring(0, 100) : dsHistorico;
						ContaReceber contaReceber = cdContaReceber>0 ? ContaReceberDAO.get(cdContaReceber, connection) :
							new ContaReceber(0 /*cdContaReceber*/,
									contrato.getCdPessoa(),
									contrato.getCdEmpresa(),
									contrato.getCdContrato(),
									0 /*cdContaOrigem*/,
									0 /*cdDocumentoSaida*/,
									contrato.getCdContaCarteira(),
									contrato.getCdConta(),
									0 /*cdFrete*/,
									"" /*nrDocumento*/,
									"" /*idContaReceber*/,
									++lastNumeroContaReceber /*nrParcela*/,
									"" /*nrReferencia*/,
									cdTipoDocumentoDefault /*cdTipoDocumento*/,
									dsHistorico /*dsHistorico*/,
									dtVencAdesao /*dtVencimento*/,
									new GregorianCalendar() /*dtEmissao*/,
									null /*dtRecebimento*/,
									null /*dtProrrogacao*/,
									Double.valueOf( Float.toString(vlAdesao)) /*vlConta*/,
									0.0d /*vlAbatimento*/,
									0.0d /*vlAcrescimo*/,
									0.0d /*vlRecebido*/,
									ContaReceberServices.ST_EM_ABERTO /*stConta*/,
									ContaReceberServices.UNICA_VEZ /*tpFrequencia*/,
									1 /*qtParcelas*/,
									ContaReceberServices.TP_TAXA_ADESAO /*tpContaReceber*/,
									0 /*cdNegociacao*/,
									"" /*txtObservacao*/,
									0 /*cdPlanoPagamento*/,
									0 /*cdFormaPagamento*/,
									new GregorianCalendar(),
									dtVencAdesao,
									0/*cdTurno*/,
									0.0d/*prJuros*/,
									0.0d/*prMulta*/, 
									0/*lgProtesto*/);
						contaReceber.setVlConta(Double.valueOf( Float.toString(vlAdesao)));
						contaReceber.setDtVencimento(dtVencAdesao);
						ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
						categorias.add(new ContaReceberCategoria(contaReceber.getCdContaReceber(),
								cdCategoriaAdesao,
								vlAdesao, 0));
						Result result = null;
						if ((result = (contaReceber.getCdContaReceber()<=0 ? ContaReceberServices.insert(contaReceber, categorias, null /*tituloCredito*/, true /*ignorarDuplicidade*/, connection) :
							ContaReceberServices.update(contaReceber, categorias, null /*tituloCredito*/, connection))) == null || result.getCode()<=0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
						}
						cdContaReceber = contaReceber.getCdContaReceber();

						pstmt = connection.prepareStatement("DELETE " +
								"FROM adm_conta_receber_evento " +
								"WHERE cd_conta_receber = ?");
						pstmt.setInt(1, contaReceber.getCdContaReceber());
						pstmt.execute();

						if (ContaReceberEventoDAO.insert(new ContaReceberEvento(cdContaReceber,
								cdEventoAdesao,
								vlAdesao /*vlEventoFinanceiro*/,
								0 /*cdContaReceberEvento*/,
								cdBeneficiario /*cdPessoa*/,
								cdContrato,
								ContaReceberEventoServices.ST_ATIVO/*stEvento*/), connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
						}
					}

					pstmt = connection.prepareStatement("SELECT A.cd_conta_receber " +
							"FROM adm_conta_receber A, adm_conta_receber_evento B " +
							"WHERE A.cd_conta_receber = B.cd_conta_receber " +
							"  AND B.cd_evento_financeiro = ? " +
							"  AND A.cd_contrato = ? " +
							"  AND B.cd_pessoa = ? " +
							"  AND A.cd_conta_receber <> ? " +
							"  AND A.st_conta = ?");
					pstmt.setInt(1, cdEventoAdesao);
					pstmt.setInt(2, cdContrato);
					pstmt.setInt(3, cdBeneficiario);
					pstmt.setInt(4, cdContaReceber);
					pstmt.setInt(5, ContaReceberServices.ST_EM_ABERTO);
					rs = pstmt.executeQuery();
					while (rs.next())
						if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), false, false, connection) <= 0) {
							if (isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Problemas reportados ao excluir fatura. Entre em contato com o suporte técnico.");
						}
				}

				if (vlParcela>0 && qtParcelas>0) {
					ArrayList<Integer> contas = new ArrayList<Integer>();

					GregorianCalendar dtVencimento = itemContratado.getDtPrimeiraParcela();
					int nrDiaVencimento = contrato.getNrDiaVencimento();
					for (int j=0; j<qtParcelas; j++) {
						float vlParcelaTemp = j>0 ? vlParcela : vlPrimeiraParcela>0 ? vlPrimeiraParcela : vlParcela;
						pstmt = connection.prepareStatement("SELECT A.st_conta, A.cd_conta_receber, B.cd_evento_financeiro, " +
								"B.vl_evento_financeiro, B.st_evento " +
								"FROM adm_conta_receber A " +
								"LEFT OUTER JOIN adm_conta_receber_evento B ON (A.cd_conta_receber = B.cd_conta_receber AND " +
								"												B.cd_evento_financeiro = ? AND " +
								"												B.cd_pessoa = ?) " +
								"WHERE A.cd_contrato = ? " +
								"  AND A.dt_vencimento >= ? " +
								"  AND A.dt_vencimento <= ? " +
								"  AND NOT EXISTS (SELECT * " +
								"				   FROM adm_conta_receber_evento " +
								"				   WHERE cd_conta_receber = A.cd_conta_receber " +
								"					 AND cd_evento_financeiro = ?) ");
						pstmt.setInt(1, cdEventoParcela);
						pstmt.setInt(2, cdBeneficiario);
						pstmt.setInt(3, cdContrato);
						pstmt.setTimestamp(4, Util.convCalendarToTimestamp(new GregorianCalendar(dtVencimento.get(Calendar.YEAR), dtVencimento.get(Calendar.MONTH), 1)));
						pstmt.setTimestamp(5, Util.convCalendarToTimestamp(new GregorianCalendar(dtVencimento.get(Calendar.YEAR), dtVencimento.get(Calendar.MONTH),
								dtVencimento.get(Calendar.MONTH)==Calendar.FEBRUARY && dtVencimento.get(Calendar.YEAR)%4==0 ? 29 :
									Recursos.diasMeses[dtVencimento.get(Calendar.MONTH)], 23, 59, 999)));
						pstmt.setInt(6, cdEventoAdesao);

						rs = pstmt.executeQuery();
						int cdContaReceber = !rs.next() ? 0 : rs.getInt("cd_conta_receber");
						if (cdContaReceber>0 && rs.getInt("st_conta")==ContaReceberServices.ST_RECEBIDA) {
							contas.add(cdContaReceber);
							observacoes.add("Não houve possibilidade de incluir ou atualizar a parcela " + (j+1) + "/" + qtParcelas + " do beneficiário " +
									beneficiario.getNmPessoa() + ", pois a fatura de referência já se encontra em status de recebida/paga.");
						}
						else if (cdContaReceber>0 && rs.getInt("cd_evento_financeiro")!=0) {
							contas.add(cdContaReceber);
							if (rs.getInt("st_evento") == ContaReceberEventoServices.ST_INATIVO) {
								pstmt = connection.prepareStatement("UPDATE adm_conta_receber_evento " +
										"SET vl_evento_financeiro = ?, " +
										"	 st_evento = ? " +
										"WHERE cd_conta_receber = ? " +
										"  AND cd_contrato = ? " +
										"  AND cd_pessoa = ? ");
								pstmt.setFloat(1, vlParcelaTemp);
								pstmt.setInt(2, ContaReceberEventoServices.ST_ATIVO);
								pstmt.setInt(3, rs.getInt("cd_conta_receber"));
								pstmt.setInt(4, cdContrato);
								pstmt.setInt(5, cdBeneficiario);
								pstmt.execute();

								ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connection);
								contaReceber.setVlConta((contaReceber.getStConta()==ContaReceberServices.ST_CANCELADA ? 0 : contaReceber.getVlConta()) +
										vlParcelaTemp);
								contaReceber.setStConta(ContaReceberServices.ST_EM_ABERTO);
								if (ContaReceberDAO.update(contaReceber, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
								}

								ContaReceberCategoria categoria = ContaReceberCategoriaDAO.get(cdContaReceber, cdCategoriaParcela, connection);
								if (categoria!=null) {
									categoria.setVlContaCategoria(categoria.getVlContaCategoria() + vlParcelaTemp);
									if (ContaReceberCategoriaDAO.update(categoria, connection) <= 0) {
										if (isConnectionNull)
											Conexao.rollback(connection);
										return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
									}
								}
								else {
									if (ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceber, cdCategoriaParcela, vlParcelaTemp, 0), connection) <= 0) {
										if (isConnectionNull)
											Conexao.rollback(connection);
										return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
									}
								}
							}
							else if (rs.getFloat("vl_evento_financeiro") != vlParcelaTemp) {
								if(pstmt!=null)
									pstmt.close();
								pstmt = connection.prepareStatement("UPDATE adm_conta_receber_evento " +
										"SET vl_evento_financeiro = ?, " +
										"	 st_evento = ? " +
										"WHERE cd_conta_receber = ? " +
										"  AND cd_contrato = ? " +
										"  AND cd_pessoa = ? ");
								pstmt.setFloat(1, vlParcelaTemp);
								pstmt.setInt(2, ContaReceberEventoServices.ST_ATIVO);
								pstmt.setInt(3, rs.getInt("cd_conta_receber"));
								pstmt.setInt(4, cdContrato);
								pstmt.setInt(5, cdBeneficiario);
								pstmt.execute();

								ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connection);
								contaReceber.setVlConta((contaReceber.getStConta()==ContaReceberServices.ST_CANCELADA ? 0 : contaReceber.getVlConta()) +
										(vlParcelaTemp - rs.getFloat("vl_evento_financeiro")));
								contaReceber.setStConta(ContaReceberServices.ST_EM_ABERTO);
								if (ContaReceberDAO.update(contaReceber, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
								}

								ContaReceberCategoria categoria = ContaReceberCategoriaDAO.get(cdContaReceber, cdCategoriaParcela, connection);
								if (categoria!=null) {
									categoria.setVlContaCategoria(categoria.getVlContaCategoria() + (vlParcelaTemp - rs.getFloat("vl_evento_financeiro")));
									if (ContaReceberCategoriaDAO.update(categoria, connection) <= 0) {
										if (isConnectionNull)
											Conexao.rollback(connection);
										return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
									}
								}
								else {
									if (ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceber, cdCategoriaParcela, vlParcelaTemp - rs.getFloat("vl_evento_financeiro"), 0), connection) <= 0) {
										if (isConnectionNull)
											Conexao.rollback(connection);
										return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
									}
								}
							}
						}
						else if (cdContaReceber>0 && rs.getInt("cd_evento_financeiro")==0) {
							contas.add(cdContaReceber);
							if (ContaReceberEventoDAO.insert(new ContaReceberEvento(cdContaReceber,
									cdEventoParcela /*cdEventoFinanceiro*/,
									vlParcelaTemp /*vlEventoFinanceiro*/,
									0 /*cdContaReceberEvento*/,
									cdBeneficiario /*cdPessoa*/,
									cdContrato,
									ContaReceberEventoServices.ST_ATIVO /*stEvento*/), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
							}

							ContaReceber contaReceber = ContaReceberDAO.get(cdContaReceber, connection);
							contaReceber.setVlConta((contaReceber.getStConta()==ContaReceberServices.ST_CANCELADA ? 0 : contaReceber.getVlConta()) +
									+ vlParcelaTemp);
							contaReceber.setStConta(ContaReceberServices.ST_EM_ABERTO);
							if (ContaReceberDAO.update(contaReceber, connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
							}

							ContaReceberCategoria categoria = ContaReceberCategoriaDAO.get(cdContaReceber, cdCategoriaParcela, connection);
							if (categoria!=null) {
								categoria.setVlContaCategoria(categoria.getVlContaCategoria() + vlParcelaTemp);
								if (ContaReceberCategoriaDAO.update(categoria, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
								}
							}
							else {
								if (ContaReceberCategoriaDAO.insert(new ContaReceberCategoria(cdContaReceber, cdCategoriaParcela, vlParcelaTemp, 0), connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
								}
							}
						}
						else {
							int nrDiaVencimentoTemp = j==0 && contrato.getDtPrimeiraParcela().get(Calendar.MONTH)==dtVencimento.get(Calendar.MONTH) &&
							contrato.getDtPrimeiraParcela().get(Calendar.YEAR)==dtVencimento.get(Calendar.YEAR) ? contrato.getDtPrimeiraParcela().get(Calendar.DAY_OF_MONTH) : nrDiaVencimento;
							nrDiaVencimentoTemp = nrDiaVencimentoTemp>Recursos.diasMeses[dtVencimento.get(Calendar.MONTH)] ? Recursos.diasMeses[dtVencimento.get(Calendar.MONTH)] : nrDiaVencimentoTemp;
							nrDiaVencimentoTemp = dtVencimento.get(Calendar.MONTH)==Calendar.FEBRUARY && dtVencimento.get(Calendar.YEAR)%4==0 && nrDiaVencimentoTemp==29 ? 28 : nrDiaVencimentoTemp;
							GregorianCalendar dtVencimentoTemp = new GregorianCalendar(dtVencimento.get(Calendar.YEAR),
									dtVencimento.get(Calendar.MONTH), nrDiaVencimentoTemp);
							ContaReceber contaReceber = new ContaReceber(0 /*cdContaReceber*/,
									contrato.getCdPessoa(),
									contrato.getCdEmpresa(),
									contrato.getCdContrato(),
									0 /*cdContaOrigem*/,
									0 /*cdDocumentoSaida*/,
									contrato.getCdContaCarteira(),
									contrato.getCdConta(),
									0 /*cdFrete*/,
									"" /*nrDocumento*/,
									"" /*idContaReceber*/,
									++lastNumeroContaReceber /*nrParcela*/,
									"" /*nrReferencia*/,
									cdTipoDocumentoDefault /*cdTipoDocumento*/,
									"FATURA " + Recursos.meses[dtVencimentoTemp.get(Calendar.MONTH)].substring(0, 3).toUpperCase() + "/" + dtVencimentoTemp.get(Calendar.YEAR) /*dsHistorico*/,
									dtVencimentoTemp /*dtVencimento*/,
									new GregorianCalendar() /*dtEmissao*/,
									null /*dtRecebimento*/,
									null /*dtProrrogacao*/,
									Double.valueOf(Float.toString(vlParcelaTemp ))/*vlConta*/,
									0.0d /*vlAbatimento*/,
									0.0d /*vlAcrescimo*/,
									0.0d /*vlRecebido*/,
									ContaReceberServices.ST_EM_ABERTO /*stConta*/,
									ContaReceberServices.UNICA_VEZ /*tpFrequencia*/,
									1 /*qtParcelas*/,
									ContaReceberServices.TP_PARCELA /*tpContaReceber*/,
									0 /*cdNegociacao*/,
									"" /*txtObservacao*/,
									0 /*cdPlanoPagamento*/,
									0 /*cdFormaPagamento*/,
									new GregorianCalendar(),
									dtVencimentoTemp,
									0/*cdTurno*/,
									0.0d/*prJuros*/,
									0.0d/*prMulta*/, 
									0/*lgProtesto*/);
							ArrayList<ContaReceberCategoria> categorias = new ArrayList<ContaReceberCategoria>();
							categorias.add(new ContaReceberCategoria(0 /*cdContaReceber*/,
									cdCategoriaParcela /*cdCategoriaEconomica*/,
									vlParcelaTemp /*vlContaCategoria*/, 0 /*cdCentroCusto*/));
							Result result = null;
							if ((result = ContaReceberServices.insert(contaReceber, categorias, null /*tituloCredito*/, true /*ignorarDuplicidade*/,
									connection)) == null ||
									result.getCode()<=0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
							}

							if (ContaReceberEventoDAO.insert(new ContaReceberEvento(result.getCode() /*cdContaReceber*/,
									cdEventoParcela /*cdEventoFinanceiro*/,
									vlParcelaTemp /*vlEventoFinanceiro*/,
									0 /*cdContaReceberEvento*/,
									cdBeneficiario /*cdPessoa*/,
									cdContrato,
									ContaReceberEventoServices.ST_ATIVO /*stEvento*/), connection) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connection);
								return new Result(-1, "Problemas reportados ao atualizar fatura. Entre em contato com o suporte técnico.");
							}
							contas.add(result.getCode());
						}
						dtVencimento = new GregorianCalendar(dtVencimento.get(Calendar.MONTH)==Calendar.DECEMBER ? dtVencimento.get(Calendar.YEAR)+1 : dtVencimento.get(Calendar.YEAR),
								dtVencimento.get(Calendar.MONTH)==Calendar.DECEMBER ? Calendar.JANUARY : dtVencimento.get(Calendar.MONTH)+1, 1);
					}

					if (contas.size()>0) {
						String statement = "SELECT A.* " +
							"FROM adm_conta_receber_evento A, adm_conta_receber B " +
							"WHERE A.cd_conta_receber = B.cd_conta_receber " +
							"  AND B.cd_contrato = ? " +
							"  AND B.st_conta = ? " +
							"  AND (A.cd_contrato IS NULL OR A.cd_contrato = B.cd_contrato) " +
							"  AND A.cd_pessoa = ? " +
							"  AND A.st_evento = ? " +
							"  AND B.tp_conta_receber <> ? " +
							"  AND NOT B.cd_conta_receber IN (";
						for (int k=0; k<contas.size(); k++)
							statement += contas.get(k) + (k<contas.size()-1 ? ", " : "");
						statement += ")";
						pstmt = connection.prepareStatement(statement);
						pstmt.setInt(1, cdContrato);
						pstmt.setInt(2, ContaReceberServices.ST_EM_ABERTO);
						pstmt.setInt(3, cdBeneficiario);
						pstmt.setInt(4, ContaReceberEventoServices.ST_ATIVO);
						pstmt.setInt(5, ContaReceberServices.TP_TAXA_ADESAO);
						rs = pstmt.executeQuery();
						while (rs.next()) {
							pstmt = connection.prepareStatement("SELECT A.* " +
									"FROM adm_conta_receber_evento A, adm_conta_receber B " +
									"WHERE A.cd_conta_receber = B.cd_conta_receber " +
									"  AND A.cd_conta_receber = ? " +
									"  AND A.st_evento = ? " +
									"  AND (A.cd_contrato IS NULL OR A.cd_contrato = B.cd_contrato) " +
									"  AND A.cd_pessoa <> ?");
							pstmt.setInt(1, rs.getInt("cd_conta_receber"));
							pstmt.setInt(2, ContaReceberEventoServices.ST_ATIVO);
							pstmt.setInt(3, cdBeneficiario);
							if (!pstmt.executeQuery().next()) {
								pstmt = connection.prepareStatement("SELECT * " +
										"FROM adm_movimento_conta_receber " +
										"WHERE cd_conta_receber = ?");
								pstmt.setInt(1, rs.getInt("cd_conta_receber"));
								if (!pstmt.executeQuery().next()) {
									if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), true, false, connection) <= 0) {
										if (isConnectionNull)
											Conexao.rollback(connection);
										return new Result(-1, "Problemas reportados ao excluir fatura fora da faixa de faturamento. " +
												"Entre em contato com o suporte técnico.");
									}
								}
							}
							else {
								ContaReceber conta = ContaReceberDAO.get(rs.getInt("cd_conta_receber"), connection);
								conta.setVlConta(conta.getVlConta() - rs.getFloat("vl_evento_financeiro"));
								if (ContaReceberDAO.update(conta, connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Problemas reportados ao excluir fatura. Entre em contato com o suporte técnico.");
								}
								if(pstmt!=null)
									pstmt.close();
								pstmt = connection.prepareStatement("UPDATE adm_conta_receber_categoria " +
										"SET vl_conta_categoria = vl_conta_categoria - ? " +
										"WHERE cd_conta_receber = ? " +
										"  AND cd_categoria_economica = ?");
								pstmt.setFloat(1, rs.getFloat("vl_evento_financeiro"));
								pstmt.setInt(2, rs.getInt("cd_conta_receber"));
								pstmt.setInt(3, cdCategoriaParcela);
								pstmt.execute();

								if (ContaReceberEventoDAO.delete(rs.getInt("cd_conta_receber"), rs.getInt("cd_evento_financeiro"),
										rs.getInt("cd_conta_receber_evento"), connection) <= 0) {
									if (isConnectionNull)
										Conexao.rollback(connection);
									return new Result(-1, "Problemas reportados ao excluir fatura. Entre em contato com o suporte técnico.");
								}
							}
						}
					}
				}
			}

			if (isConnectionNull)
				connection.commit();

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("observacoes", observacoes);
			return new Result(1, "Faturamento concluído com sucesso.", "results", hash);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erros reportados ao lançar faturamento de produto/serviço contratato. Anote a mensagem de erro " +
					"e entre em contato com o suporte técnico.\n" +
					e.getLocalizedMessage());
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int gerarParcelasOfContrato(int cdContrato, int cdTipoDocumento)	{
		return gerarParcelasOfContrato(cdContrato, cdTipoDocumento, 0, 0, false, null).getCode();
	}

	public static int gerarParcelasOfContrato(int cdContrato, int cdTipoDocumento, String dsHistorico)	{
		return gerarParcelasOfContrato(cdContrato, cdTipoDocumento, 0, 0, 0, 0, dsHistorico, null).getCode();
	}

	public static Result gerarParcelasOfContrato(int cdContrato, int cdTipoDocumento, boolean returnContas)	{
		return gerarParcelasOfContrato(cdContrato, cdTipoDocumento, 0, 0, 0, 0, null, null);
	}

	public static Result gerarParcelasOfContrato(int cdContrato, int cdTipoDocumento, int cdConta, int cdContaCarteira, boolean returnContas)	{
		return gerarParcelasOfContrato(cdContrato, cdTipoDocumento, cdConta, cdContaCarteira, 0, 0, null, null);
	}

	public static Result gerarParcelasOfContrato(int cdContrato, int cdTipoDocumento, int cdConta, int cdContaCarteira,
			boolean returnContas, Connection connection)	{
		return gerarParcelasOfContrato(cdContrato, cdTipoDocumento, cdConta, cdContaCarteira, 0, 0, null, connection);
	}

	public static Result gerarParcelasOfContrato(int cdContrato, int cdTipoDocumento, int cdConta, int cdContaCarteira,
			int cdCategoriaAdesao, int cdCategoriaParcela, Connection connection)	{
		return gerarParcelasOfContrato(cdContrato, cdTipoDocumento, cdConta, cdContaCarteira,
				cdCategoriaAdesao, cdCategoriaParcela, null, connection);
	}

	public static Result gerarParcelasOfContrato(int cdContrato, int cdTipoDocumento, int cdConta, int cdContaCarteira,
			int cdCategoriaAdesao, int cdCategoriaParcela, String dsHistorico, Connection connection)	{
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			Contrato contrato = ContratoDAO.get(cdContrato, connection);
			if (cdConta > 0 && cdContaCarteira > 0) {
				contrato.setCdConta(cdConta);
				contrato.setCdContaCarteira(cdContaCarteira);
				if (ContratoDAO.update(contrato, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar o contrato!");
				}
			}

			if(cdCategoriaParcela<=0)
				cdCategoriaParcela = contrato.getCdCategoriaParcelas();

			if(cdCategoriaAdesao<=0)
				cdCategoriaAdesao = contrato.getCdCategoriaAdesao()>0 ? contrato.getCdCategoriaAdesao() : cdCategoriaParcela;

			int[] tipos = new int[] {1/*ContaPagarServices.DS*/, 1/*ContaPagarServices.DM*/, 1/*ContaPagarServices.NP*/,
					1/*ContaPagarServices.DM*/, 1/*ContaPagarServices.ME*/, 1/*ContaPagarServices.NS*/,
					1/*ContaPagarServices.PC*/};

			if(cdTipoDocumento<0)
				cdTipoDocumento = tipos[contrato.getGnContrato()];
			Result result = null;
			if(contrato.getTpContrato()==tpCONTRATADA)	{
				double vlIof = 0.0;
				result = ContaReceberServices.gerarParcelas(cdContrato,ContaReceberServices.OF_CONTRATO,contrato.getCdEmpresa(),contrato.getCdPessoa(),
															 cdTipoDocumento,contrato.getCdConta(),contrato.getCdContaCarteira(),contrato.getNrParcelas(),
															 new Double(contrato.getVlParcelas()),contrato.getTpDesconto(),new Double(contrato.getPrDesconto()),
															 new Double(contrato.getVlDesconto()), new Double(contrato.getVlAdesao()),
															 contrato.getDtPrimeiraParcela(),contrato.getNrDiaVencimento(),
															 ContaReceberServices.MENSAL,contrato.getNrContrato(),dsHistorico,contrato.getTpAmortizacao(),
															 new Double(contrato.getPrJuros()),new Double(contrato.getVlContrato()),vlIof,cdCategoriaAdesao,
															 cdCategoriaParcela,-1/*tpContaReceber*/,0/*cdNegociacao*/,
															 null/*dados da conta*/,false/*simula*/,connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
			}
			else {
				result = ContaPagarServices.gerarParcelas(cdContrato,ContaPagarServices.OF_CONTRATO,contrato.getCdEmpresa(),contrato.getCdPessoa(),
														  cdTipoDocumento,contrato.getCdConta(),contrato.getNrParcelas(),new Double(contrato.getVlParcelas()),
														  contrato.getPrDesconto(),new Double(contrato.getVlAdesao()),contrato.getDtPrimeiraParcela(),
														  contrato.getNrDiaVencimento(),ContaPagarServices.MENSAL,contrato.getNrContrato(), dsHistorico,
														  0, true, connection);
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar gerar as parcelas dos contatros!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int gerarContaPagarOfContrato(int cdContrato,	int cdTipoDocumentoPagar, int nrDiaVencimentoPagar,
			int nrParcelasPagar, int cdCategoriaEconomicaPagar,	float vlParcelaPagar, String dsHistorico, GregorianCalendar dtPrimeiraParcelaPagar) {
		return gerarContaPagarOfContrato(cdContrato,	cdTipoDocumentoPagar, nrDiaVencimentoPagar, nrParcelasPagar,
				cdCategoriaEconomicaPagar, vlParcelaPagar, dsHistorico,	dtPrimeiraParcelaPagar, false, null).getCode();
	}

	public static Result gerarContaPagarOfContrato(int cdContrato, int cdTipoDocumentoPagar, int nrDiaVencimentoPagar,
			int nrParcelasPagar, int cdCategoriaEconomicaPagar,	float vlParcelaPagar, String dsHistorico, GregorianCalendar dtPrimeiraParcelaPagar,
			boolean returnContas, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			Contrato contrato = ContratoDAO.get(cdContrato, connection);
			int[] tipos = new int[] { 1/* ContaPagarServices.DS */,
					1/* ContaPagarServices.DM */, 1/* ContaPagarServices.NP */,
					1/* ContaPagarServices.DM */, 1/* ContaPagarServices.ME */,
					1/* ContaPagarServices.NS */, 1 /* ContaPagarServices.PC */};
			if (cdTipoDocumentoPagar < 0)
				cdTipoDocumentoPagar = tipos[contrato.getGnContrato()];
			Result result = ContaPagarServices.gerarParcelas(cdContrato, ContaPagarServices.OF_CONTRATO, contrato.getCdEmpresa(), contrato.getCdPessoa(),
					                                         cdTipoDocumentoPagar, contrato.getCdConta(), nrParcelasPagar, new Double(vlParcelaPagar), 0, /* prDesconto */
					                                         new Double(0), /* vlAdesao */ dtPrimeiraParcelaPagar, nrDiaVencimentoPagar, ContaPagarServices.MENSAL,
					                                         contrato.getNrContrato(), dsHistorico, cdCategoriaEconomicaPagar, returnContas, connection);
			if (result.getCode() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return result;
			}
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao tentar contas a pagar do contrato!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAsResultSet(int cdContrato) {
		return getAsResultSet(cdContrato, false);
	}

	public static ResultSetMap getAsResultSet(int cdContrato, boolean getPrdSrvPrincipal) {
		return getAsResultSet(cdContrato, getPrdSrvPrincipal, false);
	}

	public static ResultSetMap getAsResultSet(int cdContrato, boolean getPrdSrvPrincipal, boolean isConvenio) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_contrato", Integer.toString(cdContrato), ItemComparator.EQUAL, Types.INTEGER));
		if (getPrdSrvPrincipal)
			criterios.add(new ItemComparator("findProdutoServicoPrincipal", "1", ItemComparator.EQUAL, Types.INTEGER));
		if (isConvenio)
			criterios.add(new ItemComparator("tpRegisters", Integer.toString(ALL_CONVENIOS), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connection) {
		int tpRegisters = ALL_CONTRATOS;
		boolean findProdutoServicoPrincipal = false;
		int tpVinculoPessoa = -1;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("tpRegisters")) {
				tpRegisters = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("findProdutoServicoPrincipal")) {
				findProdutoServicoPrincipal = true;
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("tpVinculo")) {
				tpVinculoPessoa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}
		int iCriterioPessoa = -1;
		if (tpVinculoPessoa==0) {
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("c.nm_pessoa")) {
					iCriterioPessoa = i;
					criterios.add(i+1, new ItemComparator("A2.nm_pessoa", criterios.get(i).getValue(),
							criterios.get(i).getTypeComparation(), criterios.get(i).getTypeColumn()));
					break;
				}
			}
		}
		
		String dtProtocoloCast = "to_char(P.dt_protocolo, 'dd/mm/yyyy')";
		if( Util.getCurrentDbName() == "Firebird" )
			dtProtocoloCast = " extract(day from P.dt_protocolo)||'/'||extract(month from P.dt_protocolo)||'/'||extract(year from P.dt_protocolo) ";
		
		
		String sql = "SELECT A.*, K.nm_razao_social AS nm_empresa, L.nm_pessoa AS nm_fantasia, C.nm_pessoa, CF.nr_cpf, CJ.nr_cnpj," +
			   "       D.nm_pessoa AS nm_agente, E.nm_indicador, " +
	           " 	   F.nm_categoria_economica AS nm_categoria_parcelas, F.nr_categoria_economica AS nr_categoria_parcelas," +
	           " 	   S.nm_categoria_economica AS nm_categoria_adesao, S.nr_categoria_economica AS nr_categoria_adesao," +
	           "       G.sg_carteira, G.nm_carteira, H.nm_conta, H.nr_conta, H.nr_dv, I.nr_agencia, J.nm_banco, M.nr_contrato AS nr_convenio, " +
	           "       M.id_contrato AS id_convenio, R.nm_pessoa AS nm_conveniado, Q.nm_tipo_operacao, N.nm_modelo AS nm_modelo_contrato, " +
			   "       N.txt_conteudo AS txt_contrato_modelo, O.nr_parcelas AS nr_parcelas_modelo, O.vl_adesao AS vl_adesao_modelo, " +
			   "	   O.pr_juros_mora AS pr_juros_mora_modelo, O.pr_multa_mora AS pr_multa_mora_modelo, O.pr_desconto AS pr_desconto_modelo, " +
			   "	   O.cd_indicador AS cd_indicador_modelo, P.nr_documento || ' - ' ||  "+dtProtocoloCast+" AS nr_protocolo, " +
			   "       X.nm_login AS nm_usuario_cancelamento, Y.nm_motivo " +
			   (tpVinculoPessoa==0 || tpVinculoPessoa==2 ? "	   , A.cd_contrato AS cd_contrato, A.gn_contrato AS gn_contrato, " +
			   "	   A2.nm_pessoa AS nm_dependente, A1.nr_dependente " : "") +
			   (findProdutoServicoPrincipal ? "	   ,U.cd_produto_servico, U.nm_produto_servico, V.vl_preco AS vl_plano " : "") +
	           "FROM adm_contrato A " +
	           "JOIN grl_empresa         B ON (A.cd_empresa = B.cd_empresa) " +
	           "JOIN grl_pessoa_juridica K ON (B.cd_empresa  = K.cd_pessoa) " + // Complemento dos dados da empresa
	           "JOIN grl_pessoa          L ON (K.cd_pessoa  = L.cd_pessoa) " +  // Complemento dos dados da empresa
	           "JOIN grl_pessoa          C ON (A.cd_pessoa  = C.cd_pessoa) " +
	           "LEFT OUTER JOIN grl_pessoa_fisica   CF ON (A.cd_pessoa  = CF.cd_pessoa) " +
	           "LEFT OUTER JOIN grl_pessoa_juridica CJ ON (A.cd_pessoa  = CJ.cd_pessoa) " +
	           (tpVinculoPessoa==0 || tpVinculoPessoa==2 ? "JOIN adm_contrato_dependente A1 ON (A.cd_contrato = A1.cd_contrato AND A1.cd_dependente <> A.cd_pessoa) " +
	           "JOIN grl_pessoa                         A2 ON (A1.cd_dependente = A2.cd_pessoa) " : "") +
	           "LEFT OUTER JOIN grl_pessoa              D ON (A.cd_agente = D.cd_pessoa) " +
	           "LEFT OUTER JOIN grl_indicador           E ON (A.cd_indicador = E.cd_indicador) " +
	           "LEFT OUTER JOIN adm_categoria_economica F ON (A.cd_categoria_parcelas = F.cd_categoria_economica) " +
	           "LEFT OUTER JOIN adm_categoria_economica S ON (A.cd_categoria_adesao = S.cd_categoria_economica) " +
	           "LEFT OUTER JOIN adm_conta_carteira      G ON (A.cd_conta = G.cd_conta " +
	           "									      AND A.cd_conta_carteira = G.cd_conta_carteira) " +
	           "LEFT OUTER JOIN adm_conta_financeira    H ON (A.cd_conta = H.cd_conta) " +
	           "LEFT OUTER JOIN grl_agencia             I ON (I.cd_agencia = H.cd_agencia) " +
	           "LEFT OUTER JOIN grl_banco               J ON (J.cd_banco = I.cd_banco) " +
	           "LEFT OUTER JOIN adm_contrato            M ON (M.cd_contrato = A.cd_convenio) " +
	           "LEFT OUTER JOIN grl_pessoa              R ON (R.cd_pessoa = M.cd_pessoa) " +
	           "LEFT OUTER JOIN grl_modelo_documento    N ON (N.cd_modelo = A.cd_modelo_contrato) " +
	           "LEFT OUTER JOIN adm_modelo_contrato     O ON (N.cd_modelo = A.cd_modelo_contrato) " +
	           "LEFT OUTER JOIN ptc_documento           P ON (P.cd_documento = A.cd_documento) "+
	           "LEFT OUTER JOIN adm_tipo_operacao       Q ON (Q.cd_tipo_operacao = A.cd_tipo_operacao) " +
	           (findProdutoServicoPrincipal ?
	        	"LEFT OUTER JOIN adm_contrato_produto_servico T ON (A.cd_contrato = T.cd_contrato AND " +
	           "												   T.cd_produto_servico = (SELECT MAX(cd_produto_servico) " +
	           "																		   FROM adm_contrato_produto_servico " +
	           "																		   WHERE cd_contrato = A.cd_contrato)) " +
	           "LEFT OUTER JOIN grl_produto_servico     U ON (T.cd_produto_servico = U.cd_produto_servico) " +
	           "LEFT OUTER JOIN adm_produto_servico_preco V ON (U.cd_produto_servico = V.cd_produto_servico AND " +
	           "												V.dt_termino_validade IS NULL AND " +
	           "												V.cd_tabela_preco = Q.cd_tabela_preco) " : "") +
	           "LEFT OUTER JOIN seg_usuario X ON (A.cd_usuario_cancelamento = X.cd_usuario) " +
	           "LEFT OUTER JOIN crt_motivo  Y ON (A.cd_motivo = Y.cd_motivo) " +
	           "WHERE 1=1 " +
	           (tpRegisters==ALL_CONTRATOS ? "  AND (A.tp_contrato = " + tpCONTRATADA + " OR A.tp_contrato = " + tpCONTRATANTE + ") " : "") +
	           (tpRegisters==ALL_CONVENIOS ? "  AND (A.tp_contrato = " + tpCONVENENTE + " OR A.tp_contrato = " + tpCONVENIADA + ") " : "");
		//
 		String relations = null;
 		if (iCriterioPessoa != -1) {
 			relations = "";
 			for (int i=1; criterios!=null && i<=criterios.size(); i++) {
 				relations += (!relations.equals("") ? " AND " : "") + (i==iCriterioPessoa+1 ? " (" + i + " OR " + (i+1) + ") " :  Integer.toString(i));
 				if (i == iCriterioPessoa+1)
 					++i;
 			}
 		}
		return Search.find(sql, "", criterios, relations, connection!=null ? connection : Conexao.conectar(), connection==null, false, true);
	}

	public static HashMap<String,Object> getResumoContrato(int cdContrato) {
		GregorianCalendar dtBase = new GregorianCalendar();
		float totalReceber = 0, totalRecebido = 0, totalMultasJuros = 0, totalAcrescimos = 0, totalDescontos = 0, totalAtraso = 0;
		GregorianCalendar dtVencimento = null;
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		ItemComparator itemCriterio = new ItemComparator("A.CD_CONTRATO", Integer.toString(cdContrato), ItemComparator.EQUAL, Types.INTEGER);
		criterios.add(itemCriterio);
		ResultSetMap rsm = com.tivic.manager.adm.ContratoServices.findExtrato(criterios);
		while(rsm.next())	{
			dtVencimento = rsm.getGregorianCalendar("DT_VENCIMENTO");
			totalReceber += rsm.getFloat("VL_CONTA");
			totalRecebido += rsm.getFloat("VL_RECEBIDO");
			totalAcrescimos += rsm.getFloat("VL_ACRESCIMO");
			totalDescontos += rsm.getFloat("VL_ABATIMENTO");
			if(dtVencimento.before(dtBase) && rsm.getInt("ST_CONTA") != 1)
				totalAtraso += rsm.getFloat("VL_RECEBER");
			if(rsm.getInt("ST_CONTA") == 1)
				totalMultasJuros += (rsm.getFloat("VL_RECEBIDO") - rsm.getFloat("VL_CONTA"));
		}
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		retorno.put("totalReceber", new Float(totalReceber));
		retorno.put("totalRecebido", new Float(totalRecebido));
		retorno.put("totalAcrescimos", new Float(totalAcrescimos));
		retorno.put("totalDescontos", new Float(totalDescontos));
		retorno.put("totalAtraso", new Float(totalAtraso));
		retorno.put("totalMultasJuros", new Float(totalMultasJuros));
		return retorno;
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy) {
		return findCompleto(criterios, groupBy, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, ArrayList<String> groupBy, Connection connection) {
		int tpRegisters = ALL_CONTRATOS;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("tpRegisters")) {
				tpRegisters = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				break;
			}
		}
		String groups = "";
		String fields = "A.*, K.nm_razao_social AS nm_empresa, L.nm_pessoa AS nm_fantasia, C.nm_pessoa, D.nm_pessoa AS nm_agente, " +
				        "E.nm_indicador, F.nm_categoria_economica AS nm_categoria_parcelas, F.nr_categoria_economica AS nr_categoria_parcelas, " +
				        "G.sg_carteira, G.nm_carteira, H.nm_conta, H.nr_conta, H.nr_dv, I.nr_agencia, " +
				        "S.nm_categoria_economica AS nm_categoria_adesao, S.nr_categoria_economica AS nr_categoria_adesao, " +
				        "J.nm_banco, M.nr_contrato AS nr_convenio, M.id_contrato AS id_convenio, " +
					    "R.nm_pessoa AS nm_conveniado, C.gn_pessoa, " +
						"N.nr_parcelas AS nr_parcelas_modelo, N.vl_adesao AS vl_adesao_modelo, " +
						"N.pr_juros_mora AS pr_juros_mora_modelo, N.pr_multa_mora AS pr_multa_mora_modelo, " +
						"N.pr_desconto AS pr_desconto_modelo, N.cd_indicador AS cd_indicador_modelo, " +
						"N.nr_parcelas AS nr_parcelas_modelo, N.vl_adesao AS vl_adesao_modelo, " +
					    "N.pr_juros_mora AS pr_juros_mora_modelo, N.pr_multa_mora AS pr_multa_mora_modelo, " +
					    "N.pr_desconto AS pr_desconto_modelo, N.cd_indicador AS cd_indicador_modelo, " +
				        "P.nm_modelo AS nm_modelo_contrato, P.txt_conteudo AS txt_contrato_modelo, " +
					    "A.cd_contrato AS cd_contrato, A.gn_contrato AS gn_contrato ";
		// Processa agrupamentos enviados em groupBy
		String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups,
				                                                     "SUM(A.VL_CONTRATO) AS VL_CONTRATO, COUNT(*) AS QT_CONTRATO");
		fields = retorno[0];
		groups = retorno[1];
		String sql = 	"SELECT "+fields+
						"FROM adm_contrato A " +
				        "JOIN grl_empresa                        B ON (A.cd_empresa = B.cd_empresa) " +
				        "JOIN grl_pessoa                         C ON (A.cd_pessoa  = C.cd_pessoa) " +
				        "JOIN grl_pessoa_juridica 			     K ON (B.cd_empresa  = K.cd_pessoa) " +
				        "JOIN grl_pessoa 						 L ON (K.cd_pessoa  = L.cd_pessoa) " +
				        "LEFT OUTER JOIN grl_pessoa              D ON (A.cd_agente = D.cd_pessoa) " +
				        "LEFT OUTER JOIN grl_indicador           E ON (A.cd_indicador = E.cd_indicador) " +
				        "LEFT OUTER JOIN adm_categoria_economica F ON (A.cd_categoria_parcelas = F.cd_categoria_economica) " +
				        "LEFT OUTER JOIN adm_categoria_economica S ON (A.cd_categoria_adesao = S.cd_categoria_economica) " +
				        "LEFT OUTER JOIN adm_conta_carteira      G ON (A.cd_conta = G.cd_conta AND " +
				        "	                                       	   A.cd_conta_carteira = G.cd_conta_carteira) " +
				        "LEFT OUTER JOIN adm_conta_financeira    H ON (A.cd_conta = H.cd_conta) " +
				        "LEFT OUTER JOIN grl_agencia             I ON (I.cd_agencia = H.cd_agencia) " +
				        "LEFT OUTER JOIN grl_banco               J ON (J.cd_banco = I.cd_banco) " +
				        "LEFT OUTER JOIN adm_contrato            M ON (M.cd_contrato = A.cd_convenio) " +
			            "LEFT OUTER JOIN grl_pessoa 		     R ON (R.cd_pessoa = M.cd_pessoa) " +
				        "LEFT OUTER JOIN adm_modelo_contrato     N ON (N.cd_modelo_contrato = A.cd_modelo_contrato) "+
        				"LEFT OUTER JOIN grl_pessoa_juridica     O ON (A.cd_pessoa = O.cd_pessoa) "+
        				"LEFT OUTER JOIN grl_modelo_documento    P ON (A.cd_modelo_contrato = P.cd_modelo) " +
        				"WHERE 1 = 1 " +
				        (tpRegisters==ALL_CONTRATOS ? "  AND (A.tp_contrato = " + tpCONTRATADA + " OR A.tp_contrato = " + tpCONTRATANTE + ") " : "") +
				        (tpRegisters==ALL_CONVENIOS ? "  AND (A.tp_contrato = " + tpCONVENENTE + " OR A.tp_contrato = " + tpCONVENIADA + ") " : "");
		return Search.find(sql, groups, criterios, Conexao.conectar(), true, false);
	}

	public static ResultSetMap findExtrato(ArrayList<ItemComparator> criterios) {
		String sql = "SELECT A.*, " +
					 "	B.*, " +
					 "	C.*, " +
					 "	H.nm_razao_social AS nm_empresa, I.nm_pessoa AS nm_fantasia, " +
					 "	E.nm_pessoa, " +
					 "	F.nm_pessoa AS nm_agente, " +
					 "	G.nm_grupo_solidario, " +
					 "	J.*, " +
					 "  K.dt_movimento, " +
					 "	(A.vl_contrato/A.nr_parcelas) AS vl_historico, " +
					 "	C.cd_conta_receber AS cd_conta_receber " +
					 "FROM adm_contrato A " +
					 "	LEFT OUTER JOIN mcr_contrato B ON (A.cd_contrato = B.cd_contrato) " +
					 "	JOIN adm_conta_receber C ON (A.cd_contrato = C.cd_contrato) " +
					 "	JOIN grl_empresa D ON (A.cd_empresa = D.cd_empresa) " +
					 "	JOIN grl_pessoa  E ON (A.cd_pessoa  = E.cd_pessoa) " +
					 "  JOIN grl_pessoa_juridica H ON (D.cd_empresa  = H.cd_pessoa) " +
					 "  JOIN grl_pessoa I ON (H.cd_pessoa  = I.cd_pessoa) " +
					 "	LEFT OUTER JOIN grl_pessoa F ON (A.cd_agente = F.cd_pessoa) " +
					 "	LEFT OUTER JOIN mcr_grupo_solidario G ON (B.cd_grupo_solidario = G.cd_grupo_solidario) " +
					 "	LEFT OUTER JOIN adm_movimento_conta_receber J ON (C.cd_conta_receber = J.cd_conta_receber) " +
					 "	LEFT OUTER JOIN adm_movimento_conta K ON (J.cd_movimento_conta = K.cd_movimento_conta) ";
		ResultSetMap rsm = Search.find(sql, "ORDER BY A.cd_pessoa, A.cd_contrato, C.dt_vencimento, K.dt_movimento ", criterios, Conexao.conectar(), true);
		while (rsm.next()) {
			float vlHistorico = rsm.getInt("nr_parcelas") <= 0 ? 0 : rsm.getFloat("vl_contrato") / rsm.getInt("nr_parcelas");
 			rsm.setValueToField("VL_HISTORICO", vlHistorico);
 		}
		rsm.beforeFirst();
 		return rsm;
	}

	public static ResultSetMap getAllContasReceber(int cdContrato) {
		return getAllContasReceber(cdContrato, null);
	}

	public static ResultSetMap getAllContasReceber(int cdContrato, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_contrato", Integer.toString(cdContrato), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_documento_saida", "0", ItemComparator.ISNULL, Types.INTEGER));
			return ContaReceberServices.find(criterios, connection);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllContasReceber: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllPagamentosEspecieTitulos(int cdContrato) {
		return getAllPagamentosEspecieTitulos(cdContrato, null);
	}

	public static ResultSetMap getAllPagamentosEspecieTitulos(int cdContrato, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_forma_pagamento, D.nm_forma_pagamento, " +
					"B.vl_recebido, A.dt_movimento " +
					"FROM adm_movimento_conta A, adm_movimento_conta_receber B, adm_conta_receber C, adm_forma_pagamento D " +
					"WHERE A.cd_movimento_conta = B.cd_movimento_conta " +
					"  AND A.cd_conta = B.cd_conta " +
					"  AND B.cd_conta_receber = C.cd_conta_receber " +
					"  AND A.cd_forma_pagamento = D.cd_forma_pagamento " +
					"  AND C.cd_contrato = ? " +
					"  AND C.cd_documento_saida IS NULL " +
					"  AND D.tp_forma_pagamento = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, FormaPagamentoServices.MOEDA_CORRENTE);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllContasPagasEspecieTitulos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static float getTotalPagoEspecie(int cdContrato) {
		return getTotalPagoEspecie(cdContrato, null);
	}

	public static float getTotalPagoEspecie(int cdContrato, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(A.vl_recebido - A.vl_juros - A.vl_multa + A.vl_desconto - A.vl_tarifa_cobranca) AS vl_pagamentos " +
					"FROM adm_movimento_conta_receber A, adm_movimento_conta B, adm_forma_pagamento C " +
					"WHERE A.cd_movimento_conta = B.cd_movimento_conta " +
					"  AND A.cd_conta = B.cd_conta " +
					"  AND B.cd_forma_pagamento = C.cd_forma_pagamento " +
					"  AND C.tp_forma_pagamento = ? " +
					"  AND A.cd_conta_receber IN (SELECT D.cd_conta_receber " +
					"							  FROM adm_conta_receber D " +
					"							  WHERE D.cd_contrato = ? " +
					"								AND D.cd_documento_saida IS NULL) OR " +
					"	   A.cd_conta_receber IN (SELECT I.cd_conta_receber " +
					"							  FROM adm_conta_receber D, adm_movimento_conta_receber E, adm_movimento_conta F, " +
					"								   adm_forma_pagamento G, adm_movimento_titulo_credito H, adm_titulo_credito I " +
					"								   WHERE D.cd_conta_receber = E.cd_conta_receber " +
					"									 AND E.cd_movimento_conta = F.cd_movimento_conta " +
					"									 AND E.cd_conta = F.cd_conta " +
					"									 AND F.cd_forma_pagamento = G.cd_forma_pagamento " +
					"									 AND G.tp_forma_pagamento <> ? " +
					"									 AND D.cd_contrato = ? " +
					"									 AND D.cd_documento_saida IS NULL " +
					"									 AND F.cd_movimento_conta = H.cd_movimento_conta " +
					"									 AND F.cd_conta = H.cd_conta " +
					"									 AND H.cd_titulo_credito = I.cd_titulo_credito)");
			pstmt.setInt(1, FormaPagamentoServices.MOEDA_CORRENTE);
			pstmt.setInt(2, cdContrato);
			pstmt.setInt(3, FormaPagamentoServices.MOEDA_CORRENTE);
			pstmt.setInt(4, cdContrato);
			ResultSet rs = pstmt.executeQuery();
			return !rs.next() ? 0 : rs.getFloat("vl_pagamentos");
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

	public static ResultSetMap getAllTitulos(int cdContrato) {
		return getAllTitulos(cdContrato, null);
	}

	public static ResultSetMap getAllTitulos(int cdContrato, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT DISTINCT A.*, " +
					"C.nm_banco, D.nm_tipo_documento, D.sg_tipo_documento, B.st_conta, B.nr_referencia, " +
					"B.ds_historico " +
					"FROM adm_titulo_credito A " +
					"JOIN adm_conta_receber B ON (A.cd_conta_receber = B.cd_conta_receber) " +
					"LEFT OUTER JOIN grl_banco C ON (A.cd_instituicao_financeira = C.cd_banco) " +
					"LEFT OUTER JOIN adm_tipo_documento D ON (A.cd_tipo_documento = D.cd_tipo_documento) " +
					"WHERE A.cd_titulo_credito IN (SELECT B.cd_titulo_credito " +
					"							   FROM adm_movimento_titulo_credito B, adm_movimento_conta C, " +
					"									adm_movimento_conta_receber D, adm_conta_receber E " +
					"							   WHERE B.cd_movimento_conta = C.cd_movimento_conta " +
					"								 AND B.cd_conta = C.cd_conta " +
					"								 AND C.cd_movimento_conta = D.cd_movimento_conta " +
					"								 AND C.cd_conta = D.cd_conta " +
					"								 AND D.cd_conta_receber = E.cd_conta_receber " +
					"								 AND E.cd_contrato = ?)");
			pstmt.setInt(1, cdContrato);
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getAllMovimentos(int cdContrato) {
		return getAllMovimentos(cdContrato, false, null);
	}

	public static ResultSetMap getAllMovimentos(int cdContrato, boolean groupByData) {
		return getAllMovimentos(cdContrato, groupByData, null);
	}

	@SuppressWarnings("unchecked")
	public static ResultSetMap getAllMovimentos(int cdContrato, boolean groupByData, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ResultSetMap rsmTemp = !groupByData ? null : new ResultSetMap();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, E.tp_conta, E.cd_empresa, " +
					"B.nm_forma_pagamento, D.nm_pessoa AS nm_empresa " +
					"FROM adm_movimento_conta A " +
					"JOIN adm_conta_financeira E ON (A.cd_conta = E.cd_conta) " +
					"LEFT OUTER JOIN adm_forma_pagamento B ON (A.cd_forma_pagamento = B.cd_forma_pagamento) " +
					"LEFT OUTER JOIN adm_conta_financeira C ON (A.cd_conta = C.cd_conta) " +
					"LEFT OUTER JOIN grl_pessoa D ON (C.cd_empresa = D.cd_pessoa) " +
					"WHERE (A.cd_movimento_conta, A.cd_conta) IN (SELECT B.cd_movimento_conta, A.cd_conta " +
					"											  FROM adm_movimento_conta_receber B, adm_conta_receber C " +
					"											  WHERE B.cd_movimento_conta = A.cd_movimento_conta " +
					"												AND B.cd_conta = A.cd_conta " +
					"												AND B.cd_conta_receber = C.cd_conta_receber " +
					"												AND C.cd_contrato = ?) ");
			pstmt.setInt(1, cdContrato);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm!=null && rsm.next()){
				pstmt = connection.prepareStatement("SELECT A.*, B.ds_historico, C.cd_pessoa, B.ds_historico, C.nm_pessoa, " +
						"C.gn_pessoa, D.nr_cpf, E.nr_cnpj " +
						"FROM adm_movimento_conta_receber A " +
						"JOIN adm_conta_receber B ON (A.cd_conta_receber = B.cd_conta_receber) " +
						"LEFT OUTER JOIN grl_pessoa C ON (B.cd_pessoa = C.cd_pessoa) " +
						"LEFT OUTER JOIN grl_pessoa_fisica D ON (C.cd_pessoa = D.cd_pessoa) " +
						"LEFT OUTER JOIN grl_pessoa_juridica E ON (C.cd_pessoa = E.cd_pessoa) " +
						"WHERE A.cd_conta = ? " +
						"  AND A.cd_movimento_conta = ?");
				pstmt.setInt(1, rsm.getInt("cd_conta"));
				pstmt.setInt(2, rsm.getInt("cd_movimento_conta"));
				rsm.getRegister().put("rsmContas", new ResultSetMap(pstmt.executeQuery()));

				pstmt = connection.prepareStatement("SELECT distinct B.cd_plano_pagamento, B.nm_plano_pagamento " +
						"FROM adm_contrato_plano_pagto A " +
						"JOIN adm_plano_pagamento B ON (A.cd_plano_pagamento = B.cd_plano_pagamento) " +
						"WHERE A.cd_conta = ? " +
						"  AND A.cd_movimento_conta = ?");
				pstmt.setInt(1, rsm.getInt("cd_conta"));
				pstmt.setInt(2, rsm.getInt("cd_movimento_conta"));
				ResultSetMap rsmPlanosPag = new ResultSetMap(pstmt.executeQuery());
				String nmPlanoPagamento = "";
				for (int i=0; rsmPlanosPag.next(); i++)
					nmPlanoPagamento += (i<=0 ? "" : ", ") + rsmPlanosPag.getString("nm_plano_pagamento");
				rsm.getRegister().put("NM_PLANO_PAGAMENTO", nmPlanoPagamento);
				rsm.getRegister().put("rsmPlanosPag", rsmPlanosPag);
				rsmPlanosPag.beforeFirst();
				rsm.getRegister().put("rsmPlanosPag", rsmPlanosPag);
			}
			rsm.beforeFirst();

			while(groupByData && rsm.next()) {
				GregorianCalendar dtMovimento = rsm.getGregorianCalendar("dt_movimento");
				dtMovimento.set(Calendar.HOUR_OF_DAY, 0);
				dtMovimento.set(Calendar.MINUTE, 0);
				dtMovimento.set(Calendar.SECOND, 0);
				dtMovimento.set(Calendar.MILLISECOND, 0);
				boolean isFound = rsmTemp.locate("DT_MOVIMENTO", dtMovimento, false, false);
				HashMap<String, Object> register = isFound ? rsmTemp.getRegister() : null;
				if (register==null) {
					register = new HashMap<String, Object>();
					register.put("DT_MOVIMENTO", dtMovimento);
					register.put("VL_MOVIMENTO", rsm.getFloat("VL_MOVIMENTO"));
					register.put("NM_FORMA_PAGAMENTO", rsm.getString("NM_FORMA_PAGAMENTO"));
					register.put("NM_EMPRESA", rsm.getString("NM_EMPRESA"));
					register.put("NM_PESSOA", "");
					register.put("NR_CPF_CNPJ", "");
					register.put("NM_PLANO_PAGAMENTO", "");
					register.put("movimentos", new ResultSetMap());
					register.put("sacados", new ArrayList<Integer>());
					register.put("planosPag", new ArrayList<Integer>());
					rsmTemp.addRegister(register);
				}
				else {
					register.put("VL_MOVIMENTO", rsmTemp.getFloat("VL_MOVIMENTO") + rsm.getFloat("VL_MOVIMENTO"));
					register.put("NM_FORMA_PAGAMENTO", rsmTemp.getString("NM_FORMA_PAGAMENTO") + ", " + rsm.getString("NM_FORMA_PAGAMENTO"));
				}
				((ResultSetMap)register.get("movimentos")).addRegister(rsm.getRegister());
				ResultSetMap rsmContas = (ResultSetMap)rsm.getRegister().get("rsmContas");
				String nmPessoa = (String)register.get("NM_PESSOA");
				String nrCpfCnpj = (String)register.get("NR_CPF_CNPJ");
				ArrayList<Integer> sacados = (ArrayList<Integer>)register.get("sacados");
				while (rsmContas.next()) {
					if (rsmContas.getInt("cd_pessoa")>0 && sacados.indexOf(rsmContas.getInt("cd_pessoa")) == -1) {
						nmPessoa = sacados.size()<=0 ? "" : nmPessoa + "; ";
						nmPessoa += rsmContas.getString("nm_pessoa");
						nrCpfCnpj = sacados.size()<=0 ? "" : nrCpfCnpj + "; ";
						nrCpfCnpj += rsmContas.getInt("gn_pessoa")==PessoaServices.TP_FISICA ?
								Util.formatCpf(rsmContas.getString("nr_cpf")) : Util.formatCnpj(rsmContas.getString("nr_cnpj"));
						sacados.add(rsmContas.getInt("cd_pessoa"));
					}
				}
				register.put("NM_PESSOA", nmPessoa);
				register.put("NR_CPF_CNPJ", nrCpfCnpj);
				rsmContas.beforeFirst();

				ArrayList<Integer> planosPag = (ArrayList<Integer>)register.get("planosPag");
				ResultSetMap rsmPlanosPag = (ResultSetMap)rsm.getRegister().get("rsmPlanosPag");
				String nmPlanoPagamento = (String)register.get("NM_PLANO_PAGAMENTO");
				while (rsmPlanosPag.next()) {
					if (planosPag.indexOf(rsmPlanosPag.getInt("cd_plano_pagamento")) == -1) {
						nmPlanoPagamento = planosPag.size()<=0 ? "" : nmPlanoPagamento + ", ";
						nmPlanoPagamento += rsmPlanosPag.getString("nm_plano_pagamento");
						planosPag.add(rsmPlanosPag.getInt("cd_plano_pagamento"));
					}
				}
				register.put("NM_PLANO_PAGAMENTO", nmPlanoPagamento);
				rsmPlanosPag.beforeFirst();
			}

			return groupByData ? rsmTemp : rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllMovimentos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllDescontos(int cdContrato) {
		return getAllDescontos(cdContrato, null);
	}

	public static ResultSetMap getAllDescontos(int cdContrato, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.pr_desconto AS pr_desconto_faixa, " +
					"C.nm_tipo_desconto, D.vl_movimento, E.txt_fidelidade " +
					"FROM adm_contrato_desconto A " +
					"LEFT OUTER JOIN adm_faixa_desconto B ON (A.cd_tipo_desconto = B.cd_tipo_desconto AND " +
					"										  A.cd_empresa = B.cd_empresa AND " +
					"										  A.cd_faixa_desconto = B.cd_faixa_desconto) " +
					"LEFT OUTER JOIN adm_tipo_desconto C ON (B.cd_tipo_desconto = C.cd_tipo_desconto) " +
					"LEFT OUTER JOIN crm_fidelidade_movimento D ON (A.cd_movimento_fidelidade = D.cd_movimento) " +
					"LEFT OUTER JOIN crm_fidelidade E ON (D.cd_fidelidade = E.cd_fidelidade) " +
					"WHERE A.cd_contrato = ?");
			pstmt.setInt(1, cdContrato);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllDescontos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllProdutosServicos(int cdContrato, int cdEmpresa) {
		return getAllProdutosServicos(cdContrato, cdEmpresa, null);
	}

	public static ResultSetMap getAllProdutosServicos(int cdContrato, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT DISTINCT A.*, B.st_produto_empresa, C.nm_produto_servico, C.txt_produto_servico, C.tp_produto_servico, " +
					 "       C.id_produto_servico, C.sg_produto_servico, E.sg_unidade_medida, E.nm_unidade_medida," +
					 "       F.nm_referencia, F.cd_local_armazenamento, G.nm_local_armazenamento, G.cd_local_armazenamento_superior " +
					 "FROM adm_contrato_produto_servico  A " +
					 "JOIN grl_produto_servico_empresa   B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					 "JOIN grl_produto_servico           C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_produto        D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_unidade_medida E ON (B.cd_unidade_medida  = E.cd_unidade_medida) " +
					 "LEFT OUTER JOIN alm_produto_referencia F ON (A.cd_empresa         = F.cd_empresa" +
                     "                                         AND A.cd_produto_servico = F.cd_produto_servico" +
                     "                                         AND A.cd_referencia      = F.cd_referencia)" +
                     "LEFT OUTER JOIN alm_local_armazenamento G ON (G.cd_local_armazenamento = F.cd_local_armazenamento) " +
					 "WHERE A.cd_contrato = " + cdContrato+
					 "	AND B.cd_empresa  = " + cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				int cdLocalSuperior = rsm.getInt("cd_local_armazenamento_superior");
				String nmLocalSuperior = "";
				while(cdLocalSuperior>0)	{
					ResultSet rs = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE cd_local_armazenamento = "+cdLocalSuperior).executeQuery();
					if(rs.next())	{
						nmLocalSuperior = rs.getString("nm_local_armazenamento")+(nmLocalSuperior.equals("") ? "" : " -> ")+nmLocalSuperior;
						cdLocalSuperior = rs.getInt("cd_local_armazenamento_superior");
					}
				}
				rsm.setValueToField("NM_LOCAL_SUPERIOR", nmLocalSuperior);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllProdutoServico: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllNegociacoes(int cdContrato) {
		return getAllNegociacoes(cdContrato, null);
	}

	public static ResultSetMap getAllNegociacoes(int cdContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, (SELECT SUM(C.vl_conta - C.vl_abatimento + C.vl_acrescimo) " +
					"FROM adm_conta_receber_negociacao B, adm_conta_receber C " +
					"WHERE B.cd_conta_receber = C.cd_conta_receber " +
					"  AND B.cd_contrato = A.cd_contrato " +
					"  AND B.cd_negociacao = A.cd_negociacao) AS vl_negociacao, " +
					"B.nm_pessoa AS nm_responsavel " +
					"FROM adm_contrato_negociacao A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_responsavel = B.cd_pessoa) " +
					"WHERE A.cd_contrato = ?");
			pstmt.setInt(1, cdContrato);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next()) {
				pstmt = connect.prepareStatement("SELECT * " +
						"FROM adm_conta_receber_negociacao " +
						"WHERE cd_contrato = ? " +
						"  AND cd_negociacao = ?");
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, rsm.getInt("cd_negociacao"));
				rsm.getRegister().put("CONTAS", new ResultSetMap(pstmt.executeQuery()));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllNegociacoes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllDependentes(int cdContrato) {
		return getAllDependentes(cdContrato, null);
	}

	public static ResultSetMap getAllDependentes(int cdContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_dependente, B.dt_cadastro, B.id_pessoa, " +
	 				"C.dt_nascimento, C.nr_rg, C.sg_orgao_rg, C.dt_emissao_rg, C.tp_sexo, C.st_estado_civil, " +
	 				"D.sg_estado AS sg_estado_rg, E.nm_escolaridade, F.nm_pessoa AS nm_agente, G.dt_contratacao, G.dt_primeira_parcela " +
	 	            "FROM adm_contrato_dependente A " +
	 	            "JOIN grl_pessoa B ON (A.cd_dependente = B.cd_pessoa) " +
	 	            "JOIN grl_pessoa_fisica C ON (B.cd_pessoa = C.cd_pessoa) " +
	 	            "LEFT OUTER JOIN grl_estado D ON (D.cd_estado = C.cd_estado_rg) " +
	 	            "LEFT OUTER JOIN grl_escolaridade E ON (C.cd_escolaridade = E.cd_escolaridade) " +
	 	            "LEFT OUTER JOIN grl_pessoa F ON (A.cd_agente = F.cd_pessoa) " +
	 	            "LEFT OUTER JOIN adm_contrato_prd_srv_dep G ON (A.cd_contrato = G.cd_contrato AND " +
	 	            "												A.cd_dependente = G.cd_dependente AND " +
	 	            "												A.cd_dependencia = G.cd_dependencia AND " +
	 	            "												G.cd_produto_servico = (SELECT MAX(H.cd_produto_servico) " +
	 	            "																		FROM adm_contrato_produto_servico H " +
	 	            "																		WHERE H.cd_contrato = A.cd_contrato)) " +
	 	            "WHERE A.cd_contrato = ? ");
			pstmt.setInt(1, cdContrato);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllProdutoServico: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllOrdensServico(int cdContrato) {
		return getAllOrdensServico(cdContrato, null);
	}

	public static ResultSetMap getAllOrdensServico(int cdContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_documento_saida, A.nr_documento_saida, A.dt_documento_saida, " +
				"	A.dt_emissao, A.vl_total_documento, A.st_documento_saida, A.cd_vendedor, A.cd_contrato, " +
				"	B.nm_pessoa AS nm_vendedor " +
				"FROM alm_documento_saida A " +
 	            "LEFT OUTER JOIN grl_pessoa B " +
 	            "	ON (B.cd_pessoa = A.cd_vendedor) " +
 	            "WHERE A.cd_contrato = ? ");
			pstmt.setInt(1, cdContrato);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.getAllOrdensServico: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato) {
		return delete(cdContrato, false, null);
	}

	public static int delete(int cdContrato, boolean deleteContas, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_contrato_prd_srv_dep " +
					"WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_contrato_produto_servico " +
					"WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_contrato_plano_pagto " +
					"WHERE cd_contrato = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_contrato_desconto " +
					"WHERE cd_contrato = ?");
			pstmt.setInt(1, cdContrato);
			pstmt.execute();

			pstmt = connection.prepareStatement("DELETE " +
					"FROM adm_contrato_dependente " +
					"WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.execute();

			pstmt = connection.prepareStatement("SELECT * " +
					"FROM adm_ocorrencia " +
					"WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				if (OcorrenciaServices.delete(rs.getInt("cd_ocorrencia"), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

			if (deleteContas) {
				pstmt = connection.prepareStatement("SELECT cd_conta_receber " +
						"FROM adm_conta_receber " +
						"WHERE cd_contrato = ?");
				pstmt.setInt(1, cdContrato);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), false, false, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
			}

			if (ContratoDAO.delete(cdContrato, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int deleteAllParcelas(int cdContrato, int tpDelete) {
		return deleteAllParcelas(cdContrato, tpDelete, null);
	}

	public static int deleteAllParcelas(int cdContrato, int tpDelete, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_receber, st_conta " +
					"FROM adm_conta_receber A " +
					"WHERE cd_contrato = ? " +
					(tpDelete == DEL_ALL_PARCELAS_EM_ABERTO ? "  AND st_conta = ? " +
					"  AND NOT EXISTS (SELECT B.cd_movimento_conta " +
					"				   FROM adm_movimento_conta_receber B " +
					"				   WHERE B.cd_conta_receber = A.cd_conta_receber)" : ""));
			pstmt.setInt(1, cdContrato);
			if (tpDelete == DEL_ALL_PARCELAS_EM_ABERTO)
				pstmt.setInt(2, ContaReceberServices.ST_EM_ABERTO);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("st_conta")==ContaReceberServices.ST_CANCELADA || rs.getInt("st_conta")==ContaReceberServices.ST_RECEBIDA ||
						rs.getInt("st_conta")==ContaReceberServices.ST_REFATURADA) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), false, false, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.deleteAllParcelas: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findItemContrato(ArrayList<ItemComparator> criterios) {
		return findItemContrato(criterios, null);
	}

	public static ResultSetMap findItemContrato(ArrayList<ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = null;
		try	{
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			int cdEmpresa = 0;
			int cdCliente = 0;
			int tpItemContrato = -1;
			String nmItemContrato = "";
			String idItemContrato = "";
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().toLowerCase().indexOf("cd_empresa") >= 0)
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("cd_pessoa") >= 0)
					cdCliente = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("tpitemcontrato") >= 0)
					tpItemContrato = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("nm_item_contrato") >= 0)
					nmItemContrato = criterios.get(i).getValue();
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("id_item_contrato") >= 0)
					idItemContrato = criterios.get(i).getValue();
				else
					crt.add(criterios.get(i));
			}
			String sSql = "";
			String sSqlProdutoServico = "SELECT A.cd_contrato, A.cd_produto_servico, A.dt_contratacao, A.qt_produto_servico, " +
				"	A.vl_produto_servico, " +
				"   B.dt_assinatura, B.cd_pessoa AS cd_cliente, B.nr_contrato, B.st_contrato, " +
				"	B.dt_inicio_vigencia, B.dt_final_vigencia, " +
			    "	C.nm_produto_servico AS nm_item_contrato, C.tp_produto_servico, C.id_produto_servico AS id_item_contrato, " +
			    "	C.cd_marca, C.nm_modelo, " +
				"   D.cd_empresa, D.cd_unidade_medida, D.id_reduzido, D.st_produto_empresa, " +
				"	E.nm_unidade_medida, E.sg_unidade_medida, " +
				"	F.nm_pessoa AS nm_cliente, " +
				"	G.nm_marca, " +
				"	H.cd_garantia AS cd_garantia_produto_servico, H.cd_seguradora AS cd_seguradora_produto_servico, H.cd_administradora AS cd_administradora_produto_servico, " +
				"	H.dt_garantia AS dt_garantia_produto_servico, H.tp_garantia AS tp_garantia_produto_servico, " +
				"	H.nr_tempo_garantia AS nr_tempo_garantia_produto_servico, " +
				"	H2.nm_pessoa AS nm_seguradora_produto_servico, H3.nm_pessoa AS nm_administradora_produto_servico, " +
				" 	I.nm_tipo_produto_servico " +
				" FROM adm_contrato_produto_servico A " +
				"	JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				"	JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
				"	LEFT OUTER JOIN grl_produto_servico_empresa D ON (" + (cdEmpresa > 0 ? " D.cd_empresa = " + cdEmpresa + " AND " : "") + "C.cd_produto_servico = D.cd_produto_servico) " +
				"	LEFT OUTER JOIN grl_unidade_medida E ON (D.cd_unidade_medida = E.cd_unidade_medida) " +
				"	LEFT OUTER JOIN grl_pessoa F ON (B.cd_pessoa = F.cd_pessoa) " +
				"	LEFT OUTER JOIN grl_marca G ON (C.cd_marca = G.cd_marca) " +
				"	LEFT OUTER JOIN adm_garantia H ON (A.cd_garantia = H.cd_garantia) " +
				"	LEFT OUTER JOIN grl_pessoa H2 ON (H.cd_seguradora = H2.cd_pessoa) " +
				"	LEFT OUTER JOIN grl_pessoa H3 ON (H.cd_administradora = H3.cd_pessoa) " +
				"	LEFT OUTER JOIN ord_tipo_produto_servico I ON (A.cd_tipo_produto_servico = I.cd_tipo_produto_servico) " +
				"WHERE (D.st_produto_empresa = " + ST_ATIVO +
				"    OR D.st_produto_empresa IS NULL) " +
				(cdCliente > 0 ? " AND B.cd_pessoa = " + cdCliente : "") +
				(nmItemContrato != "" ? " AND C.nm_produto_servico LIKE \'%" + nmItemContrato.toUpperCase() + "%\' " : "") +
				(idItemContrato != "" ? " AND C.id_produto_servico = \'" + idItemContrato.toUpperCase() + "\' " : "");
			String sSqlReferencia = "SELECT A.cd_contrato, A.cd_referencia, A.dt_contratacao, A.qt_produto_servico, " +
				"	A.vl_produto_servico, " +
				"   B.dt_assinatura, B.cd_pessoa AS cd_cliente, B.nr_contrato, B.st_contrato, " +
				"	B.dt_inicio_vigencia, B.dt_final_vigencia, " +
			    "	C.nm_produto_servico AS nm_item_contrato, C.tp_produto_servico, C1.nr_serie AS id_item_contrato, " +
			    "	C1.cd_marca, C1.nm_modelo, " +
				"   D.cd_empresa, D.cd_unidade_medida, D.id_reduzido, D.st_produto_empresa, " +
				"	E.nm_unidade_medida, E.sg_unidade_medida, " +
				"	F.nm_pessoa AS nm_cliente, " +
				"	G.nm_marca, " +
				"	H.cd_garantia AS cd_garantia_referencia, H.cd_seguradora AS cd_seguradora_referencia, H.cd_administradora AS cd_administradora_referencia, " +
				"	H.dt_garantia AS dt_garantia_referencia, H.tp_garantia AS tp_garantia_referencia, " +
				"	H.nr_tempo_garantia AS nr_tempo_garantia_referencia, " +
				"	H2.nm_pessoa AS nm_seguradora_referencia, H3.nm_pessoa AS nm_administradora_referencia, " +
				" 	I.nm_tipo_produto_servico AS nm_tipo_referencia " +
				" FROM adm_contrato_referencia A " +
				"	JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				"	JOIN bpm_referencia C1 ON (A.cd_referencia = C1.cd_referencia) " +
				"	JOIN bpm_bem C2 ON (C1.cd_bem = C2.cd_bem) " +
				"	JOIN grl_produto_servico C ON (C.cd_produto_servico = C2.cd_bem) " +
				"	LEFT OUTER JOIN grl_produto_servico_empresa D ON (" + (cdEmpresa > 0 ? " D.cd_empresa = " + cdEmpresa + " AND " : "") + "C.cd_produto_servico = D.cd_produto_servico) " +
				"	LEFT OUTER JOIN grl_unidade_medida E ON (D.cd_unidade_medida = E.cd_unidade_medida) " +
				"	LEFT OUTER JOIN grl_pessoa F ON (B.cd_pessoa = F.cd_pessoa) " +
				"	LEFT OUTER JOIN grl_marca G ON (C1.cd_marca = G.cd_marca) " +
				"	LEFT OUTER JOIN adm_garantia H ON (A.cd_garantia = H.cd_garantia) " +
				"	LEFT OUTER JOIN grl_pessoa H2 ON (H.cd_seguradora = H2.cd_pessoa) " +
				"	LEFT OUTER JOIN grl_pessoa H3 ON (H.cd_administradora = H3.cd_pessoa) " +
				"	LEFT OUTER JOIN ord_tipo_produto_servico I ON (A.cd_tipo_produto_servico = I.cd_tipo_produto_servico) " +
				"WHERE (D.st_produto_empresa = " + ST_ATIVO +
				"    OR D.st_produto_empresa IS NULL) " +
				(cdCliente > 0 ? " AND B.cd_pessoa = " + cdCliente : "") +
				(nmItemContrato != "" ? " AND C.nm_produto_servico LIKE \'%" + nmItemContrato.toUpperCase() + "%\' " : "") +
				(idItemContrato != "" ? " AND C1.nr_serie = \'" + idItemContrato.toUpperCase() + "\' " : "");
			String orderBy = "";
			switch (tpItemContrato) {
				case -1:
					sSql = sSqlProdutoServico + " UNION " + sSqlReferencia;
					orderBy = "ORDER BY nm_item_contrato";
					break;
				case SQL_PRODUTO_SERVICO:
					sSql = sSqlProdutoServico;
					orderBy = "ORDER BY C.nm_produto_servico";
					break;
				case SQL_PRODUTO_REFERENCIA:
					sSql = sSqlReferencia;
					orderBy = "ORDER BY C.nm_produto_servico";
					break;
			}
			rsm = Search.find(sSql, orderBy, crt, connect != null ? connect : Conexao.conectar(), connect == null);
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
		}
		return rsm;
	}

	public static Result cancelarContrato(int cdContrato, int cdMotivo, GregorianCalendar dtCancelamento, int cdUsuario){
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			//
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato SET st_contrato = "+ST_CANCELADO+
							                                   ", dt_cancelamento=?, cd_motivo="+cdMotivo+
							                                   ", cd_usuario_cancelamento="+cdUsuario+
							                                   " WHERE cd_contrato = "+cdContrato);
			pstmt.setTimestamp(1, new Timestamp(dtCancelamento.getTimeInMillis()));
			if(pstmt.executeUpdate() <=0)	{
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao tentar cancelar contrato");
			}
			//
			Result result = new Result(1);
			Contrato contrato = ContratoDAO.get(cdContrato, connect);
			if(contrato.getCdDocumento() > 0)
				result = DocumentoServices.arquivarDocumento(contrato.getCdDocumento(), cdUsuario, "Arquivado automaticamente pelo cancelamento de contrato!", connect);
			if(result.getCode() <= 0)	{
				Conexao.rollback(connect);
				return result;
			}
			//
			connect.commit();
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar cancelar contrato!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
}