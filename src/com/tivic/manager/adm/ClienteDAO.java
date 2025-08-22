package com.tivic.manager.adm;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sun.security.jca.GetInstance;

import java.util.ArrayList;

public class ClienteDAO{

	public static int insert(Cliente objeto) {
		return insert(objeto, null);
	}

	public static int insert(Cliente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cliente (cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "lg_convenio,"+
			                                  "lg_ecommerce,"+
			                                  "lg_limite_credito,"+
			                                  "lg_agenda,"+
			                                  "nr_dias_carencia_fatura,"+
			                                  "vl_limite_credito,"+
			                                  "vl_limite_mensal,"+
			                                  "vl_limite_factoring,"+
			                                  "vl_limite_factoring_emissor,"+
			                                  "vl_limite_factoring_unitario,"+
			                                  "lg_pista,"+
			                                  "lg_loja,"+
			                                  "lg_veiculos_cadastrados,"+
			                                  "nr_limite_abastecimentos,"+
			                                  "vl_limite_vale,"+
			                                  "cd_convenio,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_cidade,"+
			                                  "cd_tipo_logradouro,"+
			                                  "nm_cargo,"+
			                                  "nm_logradouro,"+
			                                  "nr_endereco,"+
			                                  "nm_complemento,"+
			                                  "nm_bairro,"+
			                                  "nm_ponto_referencia,"+
			                                  "nr_cep,"+
			                                  "nm_contato,"+
			                                  "nm_email,"+
			                                  "nr_dependentes,"+
			                                  "vl_salario,"+
			                                  "st_aluguel,"+
			                                  "vl_aluguel,"+
			                                  "dt_admissao,"+
			                                  "nm_outra_renda,"+
			                                  "vl_outra_renda,"+
			                                  "cd_faixa_renda,"+
			                                  "lg_controle_veiculo,"+
			                                  "nm_empresa_trabalho,"+
			                                  "nr_telefone_trabalho,"+
			                                  "qt_prazo_minimo_factoring,"+
			                                  "qt_prazo_maximo_factoring,"+
			                                  "qt_idade_minima_factoring,"+
			                                  "vl_ganho_minimo_factoring,"+
			                                  "pr_taxa_minima_factoring,"+
			                                  "vl_taxa_devolucao_factoring,"+
			                                  "pr_taxa_padrao_factoring,"+
			                                  "pr_taxa_juros_factoring,"+
			                                  "pr_taxa_prorrogacao_factoring,"+
			                                  "qt_maximo_documento,"+
			                                  "cd_classificacao_cliente," +
			                                  "nr_codigo_barras," +
			                                  "cd_rota," +
			                                  "lg_analise," +
			                                  "cd_profissao," +
			                                  "cd_pessoa_cobranca," +
			                                  "cd_cobranca," +
			                                  "cd_programa_fatura," +
			                                  "txt_observacao," +
			                                  "tp_credito," + 
			                                  "lg_SPC," +
			                                  "lg_SERASA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getLgConvenio());
			pstmt.setInt(4,objeto.getLgEcommerce());
			pstmt.setInt(5,objeto.getLgLimiteCredito());
			pstmt.setInt(6,objeto.getLgAgenda());
			pstmt.setInt(7,objeto.getNrDiasCarenciaFatura());
			pstmt.setFloat(8,objeto.getVlLimiteCredito());
			pstmt.setFloat(9,objeto.getVlLimiteMensal());
			pstmt.setFloat(10,objeto.getVlLimiteFactoring());
			pstmt.setFloat(11,objeto.getVlLimiteFactoringEmissor());
			pstmt.setFloat(12,objeto.getVlLimiteFactoringUnitario());
			pstmt.setInt(13,objeto.getLgPista());
			pstmt.setInt(14,objeto.getLgLoja());
			pstmt.setInt(15,objeto.getLgVeiculosCadastrados());
			pstmt.setInt(16,objeto.getNrLimiteAbastecimentos());
			pstmt.setFloat(17,objeto.getVlLimiteVale());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdConvenio());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTabelaPreco());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdCidade());
			if(objeto.getCdTipoLogradouro()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdTipoLogradouro());
			pstmt.setString(22,objeto.getNmCargo());
			pstmt.setString(23,objeto.getNmLogradouro());
			pstmt.setString(24,objeto.getNrEndereco());
			pstmt.setString(25,objeto.getNmComplemento());
			pstmt.setString(26,objeto.getNmBairro());
			pstmt.setString(27,objeto.getNmPontoReferencia());
			pstmt.setString(28,objeto.getNrCep());
			pstmt.setString(29,objeto.getNmContato());
			pstmt.setString(30,objeto.getNmEmail());
			pstmt.setInt(31,objeto.getNrDependentes());
			pstmt.setFloat(32,objeto.getVlSalario());
			pstmt.setInt(33,objeto.getStAluguel());
			pstmt.setFloat(34,objeto.getVlAluguel());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(35, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(35,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			pstmt.setString(36,objeto.getNmOutraRenda());
			pstmt.setFloat(37,objeto.getVlOutraRenda());
			if(objeto.getCdFaixaRenda()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdFaixaRenda());
			pstmt.setInt(39,objeto.getLgControleVeiculo());
			pstmt.setString(40,objeto.getNmEmpresaTrabalho());
			pstmt.setString(41,objeto.getNrTelefoneTrabalho());
			pstmt.setInt(42,objeto.getQtPrazoMinimoFactoring());
			pstmt.setInt(43,objeto.getQtPrazoMaximoFactoring());
			pstmt.setInt(44,objeto.getQtIdadeMinimaFactoring());
			pstmt.setFloat(45,objeto.getVlGanhoMinimoFactoring());
			pstmt.setFloat(46,objeto.getPrTaxaMinimaFactoring());
			pstmt.setFloat(47,objeto.getVlTaxaDevolucaoFactoring());
			pstmt.setFloat(48,objeto.getPrTaxaPadraoFactoring());
			pstmt.setFloat(49,objeto.getPrTaxaJurosFactoring());
			pstmt.setFloat(50,objeto.getPrTaxaProrrogacaoFactoring());
			pstmt.setInt(51,objeto.getQtMaximoDocumento());
			if(objeto.getCdClassificacaoCliente()==0)
				pstmt.setNull(52, Types.INTEGER);
			else
				pstmt.setInt(52,objeto.getCdClassificacaoCliente());
			pstmt.setString(53,objeto.getNrCodigoBarras());
			if(objeto.getCdRota()==0)
				pstmt.setNull(54, Types.INTEGER);
			else
				pstmt.setInt(54,objeto.getCdRota());
			pstmt.setInt(55,objeto.getLgAnalise());
			if(objeto.getCdProfissao()==0)
				pstmt.setNull(56, Types.INTEGER);
			else
				pstmt.setInt(56,objeto.getCdProfissao());
			if(objeto.getCdPessoaCobranca()==0)
				pstmt.setNull(57, Types.INTEGER);
			else
				pstmt.setInt(57,objeto.getCdPessoaCobranca());
			if(objeto.getCdCobranca()==0)
				pstmt.setNull(58, Types.INTEGER);
			else
				pstmt.setInt(58,objeto.getCdCobranca());
			if(objeto.getCdProgramaFatura()==0)
				pstmt.setNull(59, Types.INTEGER);
			else
				pstmt.setInt(59,objeto.getCdProgramaFatura());
			pstmt.setString(60,objeto.getTxtObservacao());
			pstmt.setInt(61, objeto.getTpCredito());
			pstmt.setInt(62, objeto.getLgSPC());
			pstmt.setInt(63, objeto.getLgSERASA());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Cliente objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Cliente objeto, int cdEmpresaOld, int cdPessoaOld) {
		return update(objeto, cdEmpresaOld, cdPessoaOld, null);
	}

	public static int update(Cliente objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Cliente objeto, int cdEmpresaOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_cliente SET cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "lg_convenio=?,"+
												      		   "lg_ecommerce=?,"+
												      		   "lg_limite_credito=?,"+
												      		   "lg_agenda=?,"+
												      		   "nr_dias_carencia_fatura=?,"+
												      		   "vl_limite_credito=?,"+
												      		   "vl_limite_mensal=?,"+
												      		   "vl_limite_factoring=?,"+
												      		   "vl_limite_factoring_emissor=?,"+
												      		   "vl_limite_factoring_unitario=?,"+
												      		   "lg_pista=?,"+
												      		   "lg_loja=?,"+
												      		   "lg_veiculos_cadastrados=?,"+
												      		   "nr_limite_abastecimentos=?,"+
												      		   "vl_limite_vale=?,"+
												      		   "cd_convenio=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_tipo_logradouro=?,"+
												      		   "nm_cargo=?,"+
												      		   "nm_logradouro=?,"+
												      		   "nr_endereco=?,"+
												      		   "nm_complemento=?,"+
												      		   "nm_bairro=?,"+
												      		   "nm_ponto_referencia=?,"+
												      		   "nr_cep=?,"+
												      		   "nm_contato=?,"+
												      		   "nm_email=?,"+
												      		   "nr_dependentes=?,"+
												      		   "vl_salario=?,"+
												      		   "st_aluguel=?,"+
												      		   "vl_aluguel=?,"+
												      		   "dt_admissao=?,"+
												      		   "nm_outra_renda=?,"+
												      		   "vl_outra_renda=?,"+
												      		   "cd_faixa_renda=?,"+
												      		   "lg_controle_veiculo=?,"+
												      		   "nm_empresa_trabalho=?,"+
												      		   "nr_telefone_trabalho=?,"+
												      		   "qt_prazo_minimo_factoring=?,"+
												      		   "qt_prazo_maximo_factoring=?,"+
												      		   "qt_idade_minima_factoring=?,"+
												      		   "vl_ganho_minimo_factoring=?,"+
												      		   "pr_taxa_minima_factoring=?,"+
												      		   "vl_taxa_devolucao_factoring=?,"+
												      		   "pr_taxa_padrao_factoring=?,"+
												      		   "pr_taxa_juros_factoring=?,"+
												      		   "pr_taxa_prorrogacao_factoring=?,"+
												      		   "qt_maximo_documento=?,"+
												      		   "cd_classificacao_cliente=?," +
												      		   "nr_codigo_barras=?," +
												      		   "cd_rota=?, " +
												      		   "lg_analise=?," +
												      		   "cd_profissao=?," +
												      		   "cd_pessoa_cobranca=?," +
												      		   "cd_cobranca=?," +
												      		   "cd_programa_fatura=?," +
												      		   "txt_observacao=?," +
												      		   "tp_credito=?,"+
												      		   "lg_SPC=?," +
												      		   "lg_SERASA=? WHERE cd_empresa=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getLgConvenio());
			pstmt.setInt(4,objeto.getLgEcommerce());
			pstmt.setInt(5,objeto.getLgLimiteCredito());
			pstmt.setInt(6,objeto.getLgAgenda());
			pstmt.setInt(7,objeto.getNrDiasCarenciaFatura());
			pstmt.setFloat(8,objeto.getVlLimiteCredito());
			pstmt.setFloat(9,objeto.getVlLimiteMensal());
			pstmt.setFloat(10,objeto.getVlLimiteFactoring());
			pstmt.setFloat(11,objeto.getVlLimiteFactoringEmissor());
			pstmt.setFloat(12,objeto.getVlLimiteFactoringUnitario());
			pstmt.setInt(13,objeto.getLgPista());
			pstmt.setInt(14,objeto.getLgLoja());
			pstmt.setInt(15,objeto.getLgVeiculosCadastrados());
			pstmt.setInt(16,objeto.getNrLimiteAbastecimentos());
			pstmt.setFloat(17,objeto.getVlLimiteVale());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdConvenio());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdTabelaPreco());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdCidade());
			if(objeto.getCdTipoLogradouro()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdTipoLogradouro());
			pstmt.setString(22,objeto.getNmCargo());
			pstmt.setString(23,objeto.getNmLogradouro());
			pstmt.setString(24,objeto.getNrEndereco());
			pstmt.setString(25,objeto.getNmComplemento());
			pstmt.setString(26,objeto.getNmBairro());
			pstmt.setString(27,objeto.getNmPontoReferencia());
			pstmt.setString(28,objeto.getNrCep());
			pstmt.setString(29,objeto.getNmContato());
			pstmt.setString(30,objeto.getNmEmail());
			pstmt.setInt(31,objeto.getNrDependentes());
			pstmt.setFloat(32,objeto.getVlSalario());
			pstmt.setInt(33,objeto.getStAluguel());
			pstmt.setFloat(34,objeto.getVlAluguel());
			if(objeto.getDtAdmissao()==null)
				pstmt.setNull(35, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(35,new Timestamp(objeto.getDtAdmissao().getTimeInMillis()));
			pstmt.setString(36,objeto.getNmOutraRenda());
			pstmt.setFloat(37,objeto.getVlOutraRenda());
			if(objeto.getCdFaixaRenda()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdFaixaRenda());
			pstmt.setInt(39,objeto.getLgControleVeiculo());
			pstmt.setString(40,objeto.getNmEmpresaTrabalho());
			pstmt.setString(41,objeto.getNrTelefoneTrabalho());
			pstmt.setInt(42,objeto.getQtPrazoMinimoFactoring());
			pstmt.setInt(43,objeto.getQtPrazoMaximoFactoring());
			pstmt.setInt(44,objeto.getQtIdadeMinimaFactoring());
			pstmt.setFloat(45,objeto.getVlGanhoMinimoFactoring());
			pstmt.setFloat(46,objeto.getPrTaxaMinimaFactoring());
			pstmt.setFloat(47,objeto.getVlTaxaDevolucaoFactoring());
			pstmt.setFloat(48,objeto.getPrTaxaPadraoFactoring());
			pstmt.setFloat(49,objeto.getPrTaxaJurosFactoring());
			pstmt.setFloat(50,objeto.getPrTaxaProrrogacaoFactoring());
			pstmt.setInt(51,objeto.getQtMaximoDocumento());
			if(objeto.getCdClassificacaoCliente()==0)
				pstmt.setNull(52, Types.INTEGER);
			else
				pstmt.setInt(52,objeto.getCdClassificacaoCliente());
			pstmt.setString(53,objeto.getNrCodigoBarras());
			if(objeto.getCdRota()==0)
				pstmt.setNull(54, Types.INTEGER);
			else
				pstmt.setInt(54,objeto.getCdRota());
			pstmt.setInt(55,objeto.getLgAnalise());
			if(objeto.getCdProfissao()==0)
				pstmt.setNull(56, Types.INTEGER);
			else
				pstmt.setInt(56,objeto.getCdProfissao());
			if(objeto.getCdPessoaCobranca()==0)
				pstmt.setNull(57, Types.INTEGER);
			else
				pstmt.setInt(57,objeto.getCdPessoaCobranca());
			if(objeto.getCdCobranca()==0)
				pstmt.setNull(58, Types.INTEGER);
			else
				pstmt.setInt(58,objeto.getCdCobranca());
			if(objeto.getCdProgramaFatura()==0)
				pstmt.setNull(59, Types.INTEGER);
			else
				pstmt.setInt(59,objeto.getCdProgramaFatura());
			pstmt.setString(60,objeto.getTxtObservacao());
			pstmt.setInt(61, objeto.getTpCredito());
			pstmt.setInt(62, objeto.getLgSPC());
			pstmt.setInt(63, objeto.getLgSERASA());
			pstmt.setInt(64, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(65, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			return pstmt.executeUpdate();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdPessoa) {
		return delete(cdEmpresa, cdPessoa, null);
	}

	public static int delete(int cdEmpresa, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_cliente WHERE cd_empresa=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Cliente get(int cdEmpresa, int cdPessoa) {
		return get(cdEmpresa, cdPessoa, null);
	}

	public static Cliente get(int cdEmpresa, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente WHERE cd_empresa=? AND cd_pessoa=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Cliente(rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("lg_convenio"),
						rs.getInt("lg_ecommerce"),
						rs.getInt("lg_limite_credito"),
						rs.getInt("lg_agenda"),
						rs.getInt("nr_dias_carencia_fatura"),
						rs.getFloat("vl_limite_credito"),
						rs.getFloat("vl_limite_mensal"),
						rs.getFloat("vl_limite_factoring"),
						rs.getFloat("vl_limite_factoring_emissor"),
						rs.getFloat("vl_limite_factoring_unitario"),
						rs.getInt("lg_pista"),
						rs.getInt("lg_loja"),
						rs.getInt("lg_veiculos_cadastrados"),
						rs.getInt("nr_limite_abastecimentos"),
						rs.getFloat("vl_limite_vale"),
						rs.getInt("cd_convenio"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_tipo_logradouro"),
						rs.getString("nm_cargo"),
						rs.getString("nm_logradouro"),
						rs.getString("nr_endereco"),
						rs.getString("nm_complemento"),
						rs.getString("nm_bairro"),
						rs.getString("nm_ponto_referencia"),
						rs.getString("nr_cep"),
						rs.getString("nm_contato"),
						rs.getString("nm_email"),
						rs.getInt("nr_dependentes"),
						rs.getFloat("vl_salario"),
						rs.getInt("st_aluguel"),
						rs.getFloat("vl_aluguel"),
						(rs.getTimestamp("dt_admissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_admissao").getTime()),
						rs.getString("nm_outra_renda"),
						rs.getFloat("vl_outra_renda"),
						rs.getInt("cd_faixa_renda"),
						rs.getInt("lg_controle_veiculo"),
						rs.getString("nm_empresa_trabalho"),
						rs.getString("nr_telefone_trabalho"),
						rs.getInt("qt_prazo_minimo_factoring"),
						rs.getInt("qt_prazo_maximo_factoring"),
						rs.getInt("qt_idade_minima_factoring"),
						rs.getFloat("vl_ganho_minimo_factoring"),
						rs.getFloat("pr_taxa_minima_factoring"),
						rs.getFloat("vl_taxa_devolucao_factoring"),
						rs.getFloat("pr_taxa_padrao_factoring"),
						rs.getFloat("pr_taxa_juros_factoring"),
						rs.getFloat("pr_taxa_prorrogacao_factoring"),
						rs.getInt("qt_maximo_documento"),
						rs.getInt("cd_classificacao_cliente"),
						rs.getString("nr_codigo_barras"),
						rs.getInt("cd_rota"),
						rs.getInt("lg_analise"),
						rs.getInt("cd_profissao"),
						rs.getInt("cd_pessoa_cobranca"),
						rs.getInt("cd_cobranca"),
						rs.getInt("cd_programa_fatura"),
						rs.getString("txt_observacao"),
						rs.getInt("tp_credito"),
						rs.getInt("lg_SPC"),
						rs.getInt("lg_SERASA"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cliente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClienteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_cliente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
