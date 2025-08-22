package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.adm.ContaPagar;
import com.tivic.manager.adm.ContaPagarDAO;
import com.tivic.manager.adm.ContaPagarServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class AbastecimentoServices {
	public static final String[] tipoAbastecimento = {"Por Valor", "Por Litros", "Completar Tanque"};
	public static final int TP_ABASTECIMENTO_VALOR = 0;
	public static final int TP_ABASTECIMENTO_LITROS = 1;
	public static final int TP_ABASTECIMENTO_COMPLETAR_TANQUE = 2;

	public static final String[] situacaoAbastecimento = {"Autorizado", "Realizado"};
	public static final int ST_ABASTECIMENTO_AUTORIZADO = 0;
	public static final int ST_ABASTECIMENTO_REALIZADO = 1;
	public static final int ST_ABASTECIMENTO_CANCELADO = 2;

	public static int save(Abastecimento abastecimento, int cdEmpresa){
		return save(abastecimento, cdEmpresa, null, null);
	}

	public static int save(Abastecimento abastecimento, int cdEmpresa, AutorizacaoSaida autorizacao){
		return save(abastecimento, cdEmpresa, autorizacao, null);
	}

	public static int save(Abastecimento abastecimento, int cdEmpresa, AutorizacaoSaida autorizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(abastecimento==null)
				return -10;

			Veiculo veiculo = VeiculoDAO.get(abastecimento.getCdVeiculo(), connect);

			if(veiculo==null)
				return -20;

			int retorno;
			//autorizacao de saida
			if(autorizacao!=null){
				if(autorizacao.getCdAutorizacao()==0){
					autorizacao.setDtAutorizacao(new GregorianCalendar());
					autorizacao.setTpMotivo(AutorizacaoSaidaServices.TP_MOTIVO_SAIDA_ABASTECIMENTO);
					retorno = AutorizacaoSaidaDAO.insert(autorizacao, connect);
					autorizacao.setCdAutorizacao(retorno);
				}
				else
					retorno = AutorizacaoSaidaDAO.update(autorizacao, connect);

				if(retorno<0){
					Conexao.rollback(connect);
					return -30;
				}
			}

			//abastecimento
			if(abastecimento.getCdAbastecimento()==0){
				abastecimento.setDtAutorizacao(new GregorianCalendar());
				abastecimento.setCdAutorizacao(autorizacao.getCdAutorizacao());

				abastecimento.setNrAutorizacao(getProximoNrAutorizacao(cdEmpresa, connect));

				retorno = AbastecimentoDAO.insert(abastecimento, connect);
				abastecimento.setCdAbastecimento(retorno);
			}
			else
				retorno = AbastecimentoDAO.update(abastecimento, connect);


			if(retorno>0 && abastecimento.getStAbastecimento()==ST_ABASTECIMENTO_REALIZADO){
				//atualizar hodometro
				veiculo.setQtHodometroAtual(autorizacao.getQtHodometroChegada());
				retorno = VeiculoDAO.update(veiculo, connect);

				if(retorno>0){
					//conta a pagar
					ContaPagar conta = new ContaPagar(0, //int cdContaPagar,
													  0, //int cdContrato,
													  abastecimento.getCdFornecedor(), //int cdPessoa,
													  cdEmpresa, //int cdEmpresa,
													  0, //int cdContaOrigem,
													  0, //int cdDocumentoEntrada,
													  0, //int cdConta,
													  0, //int cdContaBancaria,
													  abastecimento.getDtAbastecimento(), //GregorianCalendar dtVencimento,
													  new GregorianCalendar(), //GregorianCalendar dtEmissao,
													  null, //GregorianCalendar dtPagamento,
													  null, //GregorianCalendar dtAutorizacao,
													  null, //String nrDocumento,
													  null, //String nrReferencia,
													  0, //int nrParcela,
													  ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DOCUMENTO_ABASTECIMENTO", 0, 0, connect), //int cdTipoDocumento,
													  abastecimento.getVlAbastecido(), //float vlConta,
													  0, //float vlAbatimento,
													  0, //float vlAcrescimo,
													  0, //float vlPago,
													  "ABASTECIMENTO Nº "+abastecimento.getNrAutorizacao()+" do veículo placa "+veiculo.getNrPlaca(), //String dsHistorico,
													  ContaPagarServices.ST_EM_ABERTO, //int stConta,
													  0, //int lgAutorizado,
													  ContaPagarServices.UNICA_VEZ, //int tpFrequencia,
													  0, //int qtParcelas,
													  0, //float vlBaseAutorizacao,
													  0, //int cdViagem,
													  0, //int cdManutencao,
													  null /*String txtObservacao*/,
													  new GregorianCalendar(),
													  abastecimento.getDtAbastecimento(), 0/*cdTurno*/);
					retorno = ContaPagarServices.insert(conta, true, true, connect).getCode();
				}
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return (retorno<0)?retorno:abastecimento.getCdAbastecimento();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -50;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String getProximoNrAutorizacao(int cdEmpresa) {
		return getProximoNrAutorizacao(cdEmpresa, null);
	}

	public static String getProximoNrAutorizacao(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("AUTORIZACAO_ABASTECIMENTO", nrAno, cdEmpresa, connection)) <= 0)
				return null;

			return new DecimalFormat("000000").format(nrDocumento) + "/" + nrAno;
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

	public static int delete(int cdAbastecimento) {
		return delete(cdAbastecimento, null);
	}

	public static int delete(int cdAbastecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdAbastecimento<=0)
				return -1;

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Abastecimento abastecimento = AbastecimentoDAO.get(cdAbastecimento, connect);

			if(abastecimento==null)
				return -2;

			//se já autorizado nao pode ser apagado
			if(abastecimento.getStAbastecimento()==ST_ABASTECIMENTO_REALIZADO)
				return -3;
			else{
				if(AbastecimentoDAO.delete(abastecimento.getCdAbastecimento(), connect)<=0){
					Conexao.rollback(connect);
					return -4;
				}
				//ocorrencia
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_autorizacao_saida WHERE cd_autorizacao=?");
				pstmt.setInt(1, abastecimento.getCdAutorizacao());
				pstmt.executeUpdate();
			}

			if(isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoServices.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -3;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int confirmarAbastecimento(int cdAbastecimento, int cdContaPagar){
		try {
			if(cdAbastecimento<=0)
				return -1;

			Abastecimento abastecimento = AbastecimentoDAO.get(cdAbastecimento);
			if(abastecimento==null)
				return -2;

			ContaPagar conta = ContaPagarDAO.get(cdContaPagar);
			if(conta==null)
				return -3;

			abastecimento.setStAbastecimento(1);
			abastecimento.setCdContaPagar(cdContaPagar);
			abastecimento.setDtAbastecimento(new GregorianCalendar());
			abastecimento.setVlAbastecido(conta.getVlConta().floatValue());
			return AbastecimentoDAO.update(abastecimento);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoServices.confirmarAbastecimento: " + e);
			return -1;
		}
	}

	public static int deleteByVeiculo(int cdVeiculo) {
		return deleteByVeiculo(cdVeiculo, null);
	}

	public static int deleteByVeiculo(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}

			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_abastecimento WHERE cd_veiculo=?");
			pstmt.setInt(1, cdVeiculo);
			pstmt.executeUpdate();

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AbastecimentoServices.deleteByVeiculo: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PneuServices.deleteByReferencia: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nr_placa , C.nm_modelo, D.nm_marca, E.cd_responsavel, E.dt_saida, E.dt_chegada, E.dt_autorizacao, E.cd_responsavel, E.tp_motivo, " +
							" F.nm_pessoa as nm_responsavel, G.nm_pessoa as nm_fornecedor " +
			                " FROM fta_abastecimento A " +
			                " JOIN fta_veiculo B ON (A.cd_veiculo=B.cd_veiculo) " +
			                " JOIN fta_modelo_veiculo C ON (B.cd_modelo=C.cd_modelo) " +
			                " JOIN bpm_marca D ON (C.cd_marca=D.cd_marca) " +
			                " LEFT OUTER JOIN fta_autorizacao_saida E ON (A.cd_autorizacao=E.cd_autorizacao) " +
			                " LEFT OUTER JOIN grl_pessoa F ON (F.cd_pessoa=E.cd_responsavel) " +
			                " LEFT OUTER JOIN grl_pessoa G ON (G.cd_pessoa=A.cd_fornecedor) ",
			                "ORDER BY A.dt_autorizacao DESC", criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false, false);
	}
}