package com.tivic.manager.mob;

import java.sql.Connection;
import java.text.ParseException;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import sol.dao.ResultSetMap;

public class AitReportServicesNic {
	protected String gerarAitNic(int cdAit, int cdUsuario) throws ValidacaoException, ParseException
	{
		return gerarAitNic(cdAit, cdUsuario, null);
	}
	
	@SuppressWarnings("static-access")
	protected String gerarAitNic(int cdAit, int cdUsuario,  Connection connect) throws ValidacaoException, ParseException
	{
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		Ait aitOriginario = new Ait();
		AitDAO aitDAO = new AitDAO();
		AitMovimento aitMovimento = new AitMovimento();
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		AitReportValidatorsNic validatorsNic = new AitReportValidatorsNic();
		
		aitOriginario = aitDAO.get(cdAit, connect);
		aitMovimento = movimentoServices.getMovimentoTpStatus(cdAit, movimentoServices.NIP_ENVIADA);
		validatorsNic.verificarValidacoes(cdAit, aitOriginario.getDtInfracao(),aitMovimento.getDtMovimento(), connect);
		validatorsNic.verificarNipEmitida(aitMovimento);
		validatorsNic.verificarCriterios(cdAit, connect);
		validatorsNic.verificarResponsabilidadeInfracao(cdAit, connect);
		validatorsNic.verificarAitOriginario(cdAit, connect);
		validatorsNic.verificarTipoInfracao(cdAit, connect);
		
		String idAit = buscarProximoidNic(cdAit, connect);
		Ait aitNic = criarAitNic(aitOriginario, idAit, cdUsuario, connect);
		AitDAO.insert(aitNic, connect);
		registrarAit(aitNic.getCdAit(), cdUsuario, connect);
		
		if (isConnectionNull)
		{
			Conexao.desconectar (connect);
		}
		
		return aitNic.getIdAit();
	}
	
	@SuppressWarnings("static-access")
	private static String buscarProximoidNic(int cdAit, Connection connect)
	{
		String idNic = null;
		
		AitReportValidatorsNic validatorsNic = new AitReportValidatorsNic();
		TalonarioServices talonarioServices = new TalonarioServices();
		TalonarioAITDAO talonarioSearch = new TalonarioAITDAO(); 
		ResultSetMap talao = new ResultSetMap();
	
		talao = talonarioSearch.find(validatorsNic.criteriosTalonario(talonarioServices.TP_TALONARIO_NIC), connect);
	
		if (talao.next())
		{
			String sgTalao = talao.getString("sg_talao") != null ? talao.getString("sg_talao") : null;		
			String idInicial = sgTalao != null ? sgTalao + validatorsNic.tratarNrTalao(talao.getString("nr_inicial")) : talao.getString("nr_inicial");
			String idFinal = sgTalao != null ? sgTalao + validatorsNic.tratarNrTalao(talao.getString("nr_final")) : talao.getString("nr_final");
			int idAit =  gerarIdAit(idInicial, idFinal, connect);
			idNic = talao.getString("sg_talao") != null ? talao.getString("sg_talao") + validatorsNic.tratarNrTalao(String.valueOf(idAit)) : validatorsNic.tratarNrTalao(String.valueOf(idAit));
		}
		
		return idNic;
	}
	
	@SuppressWarnings("static-access")
	private static Ait criarAitNic(Ait aitOriginario, String idAit, int cdUsuario, Connection connect)
	{
		InfracaoServices infracaoServices = new InfracaoServices();
		Ait aitNic = new Ait();
		
		int cdInfracaoNic = infracaoServices.getCodInfracao(infracaoServices.MULTA_NAO_INDENTIFICACAO_CONDUTOR_2, connect);
		
		aitNic.setIdAit(idAit);
		aitNic.setDtInfracao(aitOriginario.getDtInfracao());
		aitNic.setCdAgente(aitOriginario.getCdAgente());
		aitNic.setNrPlaca(aitOriginario.getNrPlaca());
		aitNic.setCdInfracao(cdInfracaoNic);
		aitNic.setCdAitOrigem(aitOriginario.getCdAit());
		aitNic.setNrFatorNic(calcularFatorMultiplicador(aitOriginario, connect));
		aitNic.setVlMulta((aitOriginario.getVlMulta() * aitNic.getNrFatorNic()));
		aitNic.setDsLocalInfracao(aitOriginario.getDsLocalInfracao());
		aitNic.setDsObservacao("AIT Originária: " + aitOriginario.getCdAit());
		aitNic.setVlVelocidadePermitida(aitOriginario.getVlVelocidadePermitida());
		aitNic.setVlVelocidadeAferida(aitOriginario.getVlVelocidadeAferida());
		aitNic.setVlVelocidadePenalidade(aitOriginario.getVlVelocidadePenalidade());
		aitNic.setTpPessoaProprietario(aitOriginario.getTpPessoaProprietario());
		aitNic.setNmProprietario(aitOriginario.getNmProprietario());
		aitNic.setNrCpfCnpjProprietario(aitOriginario.getNrCpfCnpjProprietario());
		aitNic.setSgUfVeiculo(aitOriginario.getSgUfVeiculo());
		aitNic.setNrRenavan(aitOriginario.getNrRenavan());
		aitNic.setCdBairro(aitOriginario.getCdBairro());
		aitNic.setCdMarcaVeiculo(aitOriginario.getCdMarcaVeiculo());
		aitNic.setCdEspecieVeiculo(aitOriginario.getCdEspecieVeiculo());
		aitNic.setCdCidade(aitOriginario.getCdCidade());
		aitNic.setNrCep(aitOriginario.getNrCep());
		aitNic.setDsLogradouro(aitOriginario.getDsLogradouro());
		aitNic.setDsLocalInfracao(aitOriginario.getDsLocalInfracao());
		aitNic.setDsPontoReferencia(aitOriginario.getDsPontoReferencia());
		aitNic.setCdMovimentoAtual(1);
		aitNic.setCdUsuario(cdUsuario);
		
		return aitNic;
	}
	
	private static int gerarIdAit(String idInicial, String idFinal, Connection connect)
	{
		AitReportNicDAO nicDao = new AitReportNicDAO(connect);
		
		String idAitMax = nicDao.getMaiorIdAitNic(idInicial, idFinal);
		
		if (idAitMax == null)
		{
			return Integer.parseInt(idInicial.replaceAll("[\\D]", ""));
		}
		else
		{
			return Integer.parseInt(idAitMax.replaceAll("[\\D]", "")) + 1;
		}
			
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	protected static AitMovimento imporPenalidade(int cdAit, boolean lote, int cdUsuario, Connection connect) throws ValidacaoException, Exception 
	{
		
		AitMovimentoServices aitMovimento = new AitMovimentoServices();
		AitReportServicesNip reportServicesNip = new AitReportServicesNip();
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		Ait ait = AitDAO.get(cdAit, connect);
		validarImposicaoPenalidade(ait, lote, connect);
		AitMovimento aitPenalidade = new AitMovimento();
		com.tivic.manager.str.AitMovimento aitPenalidadeOld = new com.tivic.manager.str.AitMovimento();
		
		if (lgBaseAntiga)
		{
			 aitPenalidadeOld = com.tivic.manager.str.AitMovimentoServices.createPenalidade(ait, cdUsuario, connect);
		}
		else
		{
			aitPenalidade = aitMovimento.createPenalidade(ait, cdUsuario);
		}

		try 
		{
			connect.setAutoCommit(false);
			
			if (lgBaseAntiga)
			{
				aitPenalidadeOld = aitMovimento.savePenalidadeOld(aitPenalidadeOld, connect);
				com.tivic.manager.str.AitMovimentoServices.updateTpStatusAit(cdAit, com.tivic.manager.str.AitMovimentoServices.NIP_ENVIADA, connect);
				reportServicesNip.insertNrNotificacaoNipOld(cdAit, connect);
			}
			else
			{
				aitPenalidade = aitMovimento.savePenalidade(aitPenalidade, connect);
				aitMovimento.salvarMovimento(ait, aitPenalidade, connect);
			}
			
			connect.commit();
			connect.setAutoCommit(true);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			connect.rollback();
		}
		finally 
		{
			if (isConnectionNull)
			{
				Conexao.desconectar(connect);
			}
		}
		
		return aitPenalidade;
	}
	
	@SuppressWarnings("static-access")
	public static void validarImposicaoPenalidade(Ait ait, boolean lote, Connection connect) throws ValidacaoException 
	{
		AitMovimentoServices aitMovimento = new AitMovimentoServices();
		aitMovimento.validarExistenciaNip(ait, connect);
	}
	
	@SuppressWarnings("static-access")
	private void registrarAit(int cdAit, int cdUsuario, Connection connect)
	{
		AitMovimentoDAO movimentoDAO = new AitMovimentoDAO();
		AitMovimento movimento =  criarMovimentoRegistro(cdAit, cdUsuario);
		movimentoDAO.insert(movimento);
	}
	
	@SuppressWarnings("static-access")
	private static AitMovimento criarMovimentoRegistro(int cdAit, int cdUsuario)
	{
		
		AitMovimento movimento = new AitMovimento();
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		
		movimento.setCdAit(cdAit);
		movimento.setDtMovimento(new GregorianCalendar());
		movimento.setTpStatus(movimentoServices.REGISTRO_INFRACAO);
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(cdUsuario);
		
		return movimento;
		
	}
	
	private static int calcularFatorMultiplicador(Ait aitOriginario, Connection connect)
	{
		
		AitReportNicDAO nicDAO = new AitReportNicDAO(connect);
		
		String placaVeiculo = aitOriginario.getNrPlaca();
		int codInfracao = aitOriginario.getCdInfracao();
		ResultSetMap rsmMultiplicador = nicDAO.buscarCometimentoInfracao(codInfracao, placaVeiculo, connect);
		
		if (rsmMultiplicador.getLines().size() > 0)
		{
			return rsmMultiplicador.getLines().size() + 1;
		}
		else
		{
			return 1;
		}
		
	}
	
	@SuppressWarnings("static-access")
	protected static boolean verificarVencimentoNic(int cdAit) throws ValidacaoException
	{
		AitServices aitServices = new AitServices();
		AitReportGetCriteriosNIP criteriosNip = new AitReportGetCriteriosNIP();
		AitReportValidatorsNIP validatorsNip = new AitReportValidatorsNIP();
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		AitMovimento movimento = new AitMovimento();
		
		movimento = movimentoServices.getMovimentoTpStatus(cdAit, movimentoServices.NIP_ENVIADA);
		
		if(movimento != null)
		{
			ResultSetMap rsm = aitServices.find(criteriosNip.criteriosVencimento(cdAit));
			boolean nipVencida = validatorsNip.verificarVencimento(rsm);
			return nipVencida;
		}
		else
		{
			throw new ValidacaoException ("Para emissão da 2º via deste documento é preciso antes emitir a 1º");
		}
	}
	
}
