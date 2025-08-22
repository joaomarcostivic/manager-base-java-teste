package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class AitReportValidatorsNic {
	
	@SuppressWarnings("static-access")
	public void verificarCriterios (int cdAit, Connection connect) throws ValidacaoException, ParseException
	{	
		boolean movimentoRegistrado = false;
		String nmModelo = null;
		String dsEspecie = null;
		
		AitMovimentoServices aitMovimentoServices = new AitMovimentoServices();
		AitServices aitServices = new AitServices();
		
		Ait ait = AitDAO.get(cdAit, connect);
		ResultSetMap aitMovimento = AitMovimentoDAO.find(criteriosMovimento(cdAit), connect);
		
		if (ait.getTpPessoaProprietario() != aitServices.TP_PESSOA_PROPRIETARIO_JURIDICO)
		{
			throw new  ValidacaoException("Só é possivel emitir NIC para proprietário que seja pessoa Jurídica.");
		}
		
		if (ait.getNrControle() == null || ait.getNrControle().equals("0"))
		{
			throw new  ValidacaoException("Não foi possivel gerar a NIC, verifique se o AIT esta enviado é com o número de controle");
		}
		
		if (ait.getNmProprietario() == null || ait.getNmProprietario().trim().equals(""))
		{
			throw new  ValidacaoException("Para a emissão da NIC o AIT deve possuir o nome do proprietário");
		}
		
		if (ait.getNrCpfCnpjProprietario() == null ||  ait.getNrCpfCnpjProprietario().trim().equals(""))
		{
			throw new  ValidacaoException("Para a emissão da NIC o ait deve possuir o CPF/CNPJ do proprietário");
		}
		
		nmModelo = getNmModelo(cdAit, connect);
		if (nmModelo == null || nmModelo.trim().equals(""))
		{
			throw new  ValidacaoException("Para a emissão da NIC o ait deve possuir o modelo do veiculo");
		}
		
		dsEspecie = getDsEspecie(cdAit, connect);
		if (dsEspecie == null || dsEspecie.trim().equals(""))
		{
			throw new  ValidacaoException("Para a emissão da NIC o ait deve possuir a especie do veiculo");	
		}
		
		if (ait.getNrRenavan() == null || ait.getNrRenavan().trim().equals(""))
		{
			throw new  ValidacaoException("Para a emissão da NIC o ait deve possuir o numero de renavan");	
		}
		
		while (aitMovimento.next())
		{
			if (aitMovimento.getInt("tp_status") == aitMovimentoServices.REGISTRO_INFRACAO)
			{
				movimentoRegistrado = true;
			}
		}
		aitMovimento.beforeFirst();
		
		if (!movimentoRegistrado)
		{
			throw new  ValidacaoException("Para a emissão da NIC o AIT deve estar registrado.");
		}
		
	}
	
	private static ArrayList<ItemComparator> criteriosMovimento(int cdAit)
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		criterios.add(new ItemComparator("cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios;
	}
	
	private static String getNmModelo (int cdAit, Connection connect)
	{
		String nmModelo = null;
		boolean isConnectionNull = connect==null;
		
		try 
		{
			if (isConnectionNull) 
				connect = Conexao.conectar();
			
			String sql =  "SELECT A.cd_ait, A.cd_marca_veiculo, B.cd_marca, B.nm_modelo " 
						+ "FROM mob_ait A "
						+ "INNER JOIN fta_marca_modelo B on (A.cd_marca_veiculo = B.cd_marca) "
						+ "WHERE cd_ait = ? ";
			
			PreparedStatement ps;
			ps = connect.prepareStatement(sql);
			ps.setInt(1, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			if (rsm.next())
				nmModelo = rsm.getString("nm_modelo");
			
			return nmModelo;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static String getDsEspecie (int cdAit, Connection connect)
	{
		String nmDsEspecie = null;
		boolean isConnectionNull = connect==null;
		
		try 
		{
			if (isConnectionNull) 
			{
				connect = Conexao.conectar();
			}
			
			String sql =  "SELECT A.cd_ait, A.cd_especie_veiculo, B.cd_especie, B.DS_ESPECIE " 
						+ "FROM mob_ait A "
						+ "INNER JOIN fta_especie_veiculo B on (A.cd_especie_veiculo = B.cd_especie) "
						+ "WHERE cd_ait = ? ";
			
			PreparedStatement ps;
			ps = connect.prepareStatement(sql);
			ps.setInt(1, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			if (rsm.next())
				nmDsEspecie = rsm.getString("DS_ESPECIE");
			
			return nmDsEspecie;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private void verificarTempoEmissao(GregorianCalendar dtInfracao, GregorianCalendar dtMovimentoNip) throws ParseException, ValidacaoException
	{
		
		int doisAnos = 730;
		GregorianCalendar dtInicial = new GregorianCalendar();
		dtInicial.set(Calendar.HOUR, 0);
		dtInicial.set(Calendar.MINUTE, 0);
		dtInicial.set(Calendar.SECOND, 0);
		dtInicial.add(Calendar.DATE, -doisAnos);
		
		int cincoAnos = 1825;
		GregorianCalendar dtFinal = new GregorianCalendar();
		dtFinal.set(Calendar.HOUR, 0);
		dtFinal.set(Calendar.MINUTE, 0);
		dtFinal.set(Calendar.SECOND, 0);
		dtFinal.add(Calendar.DATE, -cincoAnos);
		
		
		if (dtInfracao.before(dtFinal))	
		{
			throw new  ValidacaoException("Não e possivel gerar multa para infração com mais de 5 anos");
		}
		
		if (dtMovimentoNip.after(dtInicial))
		{
			throw new  ValidacaoException("Para emissão da NIC a NIP deve ter sido emitida a pelo menos 2 anos.");
		}
		
	}
	
	public void verificarParamns(HashMap<String, Object> paramns) throws ValidacaoException 
	{
		String NR_BANCO_CREDITO = null;
		String NR_AGENCIA_CREDITO = null;
		String NR_CONTA_CREDITO = null;
		
		NR_BANCO_CREDITO = (String) paramns.get("NR_BANCO_CREDITO");
		NR_AGENCIA_CREDITO = (String) paramns.get("NR_AGENCIA_CREDITO");
		NR_CONTA_CREDITO = (String) paramns.get("NR_CONTA_CREDITO");
		
		if (NR_BANCO_CREDITO == null)
			throw new ValidacaoException("Verifique os parametros, não foi encontrado o número do banco");
		
		if (NR_AGENCIA_CREDITO == null)
			throw new ValidacaoException("Verifique os parametros, não foi encontrado o número da agência");
		
		if (NR_CONTA_CREDITO == null)
			throw new ValidacaoException("Verifique os parametros, não foi encontrado o número da conta");

	}
	
	protected void verificarValidacoes(int cdAit, GregorianCalendar dtInfracao, GregorianCalendar dtMovimentoNip, Connection connect) throws ValidacaoException, ParseException
	{
		
		verificarExistenciaNic(cdAit, connect);
		verificarCancelamento(cdAit, connect);
		verificarApresentacaoCondutor(cdAit, connect);
		verificarTempoEmissao(dtInfracao, dtMovimentoNip);
		verificarRecursosDeferidos(cdAit, connect);
		verificarRecursosAbertos(cdAit, connect);
		verificarDisponibilidadeTalao(connect);

	}
	
	@SuppressWarnings("static-access")
	private void verificarExistenciaNic(int cdAit, Connection connect) throws ValidacaoException
	{
		AitServices aitServices = new AitServices();
		ResultSetMap isNic = new ResultSetMap();
		
		isNic = aitServices.find(criteriosNotIsNic(cdAit), connect);
		
		if (isNic.next())
		{
			throw new ValidacaoException("Já existe NIC emitida para este AIT.");
		}
		
	}
	
	@SuppressWarnings("static-access")
	protected void isNic(int cdAit, Connection connect) throws ValidacaoException
	{
		AitDAO aitSearch = new AitDAO();
		AitServices aitServices = new AitServices();
		ResultSetMap isNic = new ResultSetMap();
		Ait ait = aitSearch.get(cdAit, connect);
		
		
		isNic = aitServices.find(criteriosIsNic(ait.getCdAitOrigem()), connect);
		
		if (isNic.next())
		{
			return;
			
		}
		else
		{
			throw new ValidacaoException("Para emissão da via de NIC e preciso que o AIT tenha sido gerado apartir de uma NIP");
		}
		
	}
	
	private ArrayList<ItemComparator> criteriosNotIsNic(int cdAit)
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		criterios.add(new ItemComparator("A.cd_ait_origem", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios;
	}
	
	private static ArrayList<ItemComparator> criteriosIsNic(int cdAitOrigem)
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAitOrigem), ItemComparator.EQUAL, Types.INTEGER));
		
		return criterios;
	}
	
	private void verificarCancelamento(int cdAit, Connection connect) throws ValidacaoException
	{
		AitMovimentoServices aitMovimento = new AitMovimentoServices();
		
		@SuppressWarnings("static-access")
		List<AitMovimento> listaCancelamento = aitMovimento.getAllCancelamentos(cdAit, connect);
				
		if (listaCancelamento.isEmpty())
		{
			return;
		}
		
		throw new ValidacaoException("Não é possivel gerar NIC para infrações canceladas.");
	}
	
	@SuppressWarnings("static-access")
	private void verificarApresentacaoCondutor(int cdAit, Connection connect) throws ValidacaoException
	{
		AitMovimentoServices movimento = new AitMovimentoServices();
		AitMovimentoDAO aitMovimentoDao = new AitMovimentoDAO();
		
		ResultSetMap aitMovimento = aitMovimentoDao.find(criteriosMovimento(cdAit), connect);
		
		while (aitMovimento.next())
		{
			if (aitMovimento.getInt("TP_STATUS") == movimento.APRESENTACAO_CONDUTOR)
			{
				throw new ValidacaoException("Não é possivel gerar NIC para infrações com apresentação de condutor.");
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void verificarRecursosDeferidos(int cdAit, Connection connect) throws ValidacaoException
	{
		AitMovimentoServices movimento = new AitMovimentoServices();
		AitMovimento defesaDeferida = new AitMovimento();
		AitMovimento jariDeferida = new AitMovimento();
		AitMovimento cetranDeferida = new AitMovimento();
		
		defesaDeferida = movimento.getMovimentoTpStatus(cdAit, movimento.DEFESA_DEFERIDA, connect);
		jariDeferida = movimento.getMovimentoTpStatus(cdAit, movimento.JARI_COM_PROVIMENTO, connect);
		cetranDeferida = movimento.getMovimentoTpStatus(cdAit, movimento.CETRAN_DEFERIDO, connect);
		
		if (defesaDeferida != null)
		{
			throw new ValidacaoException("Não é possivel gerar NIC para infrações com DEFESA deferida.");
		}
		
		if (jariDeferida != null)
		{
			throw new ValidacaoException("Não é possivel gerar NIC para infrações com JARI deferida.");
		}
		
		if (cetranDeferida != null)
		{
			throw new ValidacaoException("Não é possivel gerar NIC para infrações com CETRAN deferida.");
		}
	}
	
	private void verificarRecursosAbertos(int cdAit, Connection connect) throws ValidacaoException
	{
		verificarDefesaAberta(cdAit, connect);
		verificarJariAberta(cdAit, connect);
		verificarCetranAberta(cdAit, connect);
	}	
	
	@SuppressWarnings("static-access")
	private static void verificarDefesaAberta(int cdAit, Connection connect) throws ValidacaoException
	{
		AitMovimentoServices movimento = new AitMovimentoServices();
		
		boolean defesaPrevia = false;
		boolean defesaDeferida = false;
		boolean defesaIndeferida = false;
		
		defesaPrevia = movimento.getMovimentoTpStatus(cdAit, movimento.DEFESA_PREVIA, connect) != null ? true : false;
		defesaDeferida = movimento.getMovimentoTpStatus(cdAit, movimento.DEFESA_DEFERIDA, connect) != null ? true : false;
		defesaIndeferida = movimento.getMovimentoTpStatus(cdAit, movimento.DEFESA_INDEFERIDA, connect) != null ? true : false;
		
		if (defesaPrevia && !(defesaDeferida || defesaIndeferida))
		{
			throw new ValidacaoException("Não é possivel gerar NIC com Defesa Previa em aberto.");
		}
	}
	
	@SuppressWarnings("static-access")
	private static void verificarJariAberta(int cdAit, Connection connect) throws ValidacaoException
	{
		AitMovimentoServices movimento = new AitMovimentoServices();
		
		boolean jari = false;
		boolean jariComProvimento = false;
		boolean jariSemProvimento = false;
		
		jari = movimento.getMovimentoTpStatus(cdAit, movimento.RECURSO_JARI, connect) != null ? true : false;
		jariComProvimento = movimento.getMovimentoTpStatus(cdAit, movimento.JARI_COM_PROVIMENTO, connect) != null ? true : false;
		jariSemProvimento = movimento.getMovimentoTpStatus(cdAit, movimento.JARI_SEM_PROVIMENTO, connect) != null ? true : false;
		
		if (jari && !(jariComProvimento || jariSemProvimento))
		{
			throw new ValidacaoException("Não é possivel gerar NIC com JARI em aberto.");
		}
	}
	
	@SuppressWarnings("static-access")
	private static void verificarCetranAberta(int cdAit, Connection connect) throws ValidacaoException
	{
		AitMovimentoServices movimento = new AitMovimentoServices();
		
		boolean cetran = false;
		boolean cetranDeferido = false;
		boolean cetranIndeferido = false;
		
		cetran = movimento.getMovimentoTpStatus(cdAit, movimento.RECURSO_CETRAN, connect) != null ? true : false;
		cetranDeferido = movimento.getMovimentoTpStatus(cdAit, movimento.CETRAN_DEFERIDO, connect) != null ? true : false;
		cetranIndeferido = movimento.getMovimentoTpStatus(cdAit, movimento.CETRAN_INDEFERIDO, connect) != null ? true : false;
		
		if (cetran && !(cetranDeferido || cetranIndeferido))
		{
			throw new ValidacaoException("Não é possivel gerar NIC com CETRAN em aberto.");
		}
	}
	
	protected void verificarDisponibilidadeTalao() throws ValidacaoException
	{
		verificarDisponibilidadeTalao(1, null);
	}
	
	protected void verificarDisponibilidadeTalao(int qtdNic) throws ValidacaoException
	{
		verificarDisponibilidadeTalao(qtdNic, null);
	}
	
	protected void verificarDisponibilidadeTalao(Connection connect) throws ValidacaoException
	{
		verificarDisponibilidadeTalao(1, connect);
	}
	
	@SuppressWarnings("static-access")
	protected void verificarDisponibilidadeTalao(int qtdNic, Connection connect) throws ValidacaoException
	{
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		try
		{
			TalonarioServices talonarioServices = new TalonarioServices();
			TalonarioAITDAO talonarioSearch = new TalonarioAITDAO(); 
			ResultSetMap talao = new ResultSetMap();
			AitReportNicDAO nicDao = new AitReportNicDAO(connect);
		
			talao = talonarioSearch.find(criteriosTalonario(talonarioServices.TP_TALONARIO_NIC), connect);
		
			if (talao.next())
			{
	
				String sgTalao = talao.getString("sg_talao") != null ? talao.getString("sg_talao") : null;		
				String idInicial = sgTalao != null ? sgTalao + tratarNrTalao(talao.getString("nr_inicial")) : talao.getString("nr_inicial");
				String idFinal = sgTalao != null ? sgTalao + tratarNrTalao(talao.getString("nr_final")) : talao.getString("nr_final");
				String idAitUsados = nicDao.getMaiorIdAitNic(idInicial, idFinal);
				int cdTalao = talao.getInt("cd_talao");
				
				if (idAitUsados == null)
				{
					int qtdTalaoDisponivel = talao.getInt("nr_final");
					compararSolicitacaoXDisponibilidade(cdTalao, qtdNic, qtdTalaoDisponivel, connect);
					return;
				}
				else
				{
					int qtdTalaoDisponivel = verificarQuantidadeTaloesDisponiveis(idFinal, idAitUsados);
					compararSolicitacaoXDisponibilidade(cdTalao, qtdNic, qtdTalaoDisponivel, connect);	
					return;
				}
				
			}
			else
			{
				throw new ValidacaoException("Não existe talão para emissão de NIC, por favor lance um.");
			}
		}
		finally
		{
			if (isConnectionNull)
			{
				Conexao.desconectar (connect);
			}
		}
		
	}
	
	protected static ArrayList<ItemComparator> criteriosTalonario(int tpTalonario)
	{
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		criterios.add(new ItemComparator("tp_talao", String.valueOf(tpTalonario), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("st_talao", String.valueOf(2), ItemComparator.MINOR, Types.INTEGER));
		
		return criterios;
	}
	
	private static int verificarQuantidadeTaloesDisponiveis(String idFinal, String idMax)
	{
		int qtdTaloesDisponiveis = 0;
		int numFinal =  Integer.parseInt(idFinal.replaceAll("[\\D]", ""));
		int numMax = Integer.parseInt(idMax.replaceAll("[\\D]", ""));
		
		qtdTaloesDisponiveis = numFinal - numMax;
		
		return qtdTaloesDisponiveis;
	}
	
	private static boolean compararSolicitacaoXDisponibilidade(int cdTalao, int qtdNic, int qtdTaloesDisponiveis, Connection connect) throws ValidacaoException
	{
		
		if (qtdTaloesDisponiveis >= qtdNic)
		{
			return true;
		}
		else
		{
			verificarPreenchimentoTalao(cdTalao, qtdTaloesDisponiveis, connect);
			throw new ValidacaoException ("Não ha quantidade de talão disponivel, por favor lance mais.");
		}
			
	}
	
	private static void verificarPreenchimentoTalao(int cdTalao, int qtdTaloesDisponiveis, Connection connect)
	{
		final int talaoPreenchido = 0;
		
		switch (qtdTaloesDisponiveis)
		{
			case talaoPreenchido:
				atualizarStTalonario(cdTalao, connect);
				break;
			default:
				return;
		}
	}
	
	@SuppressWarnings("static-access")
	private static void atualizarStTalonario(int cdTalao, Connection connect)
	{
		TalonarioServices talonarioServices = new TalonarioServices();
		TalonarioAITDAO talonarioSearch = new TalonarioAITDAO(); 
		TalonarioAIT talonarioAit = new TalonarioAIT();
		
		talonarioAit = talonarioSearch.get(cdTalao);
		talonarioAit.setStTalao(talonarioServices.ST_TALAO_CONCLUIDO);
		talonarioSearch.update(talonarioAit);
	}
	
	protected static String tratarNrTalao(String nrTalao)
	{
		int size = 8;
		String zerosEsquerda = "";
		size -= nrTalao.length();
		
		for (int i = 0; i < size; i++)
		{
			zerosEsquerda += "0";
		}
		
		return zerosEsquerda + nrTalao;
	}
	
	@SuppressWarnings("static-access")
	protected void verificarRegistroAIT(int cdAit, Connection connect) throws ValidacaoException
	{
		AitMovimento aitMovimento = new AitMovimento();
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		
		aitMovimento = movimentoServices.getMovimentoTpStatus(cdAit, movimentoServices.REGISTRO_INFRACAO, connect);

		if (aitMovimento.getLgEnviadoDetran() < 1)
		{
			throw new ValidacaoException ("Para a emissão da NIC e preciso que o AIT tenha sido enviado ao orgão de transito.");
		}
	}
	
	protected void verificarNipEmitida(AitMovimento aitMovimento) throws ValidacaoException
	{	
		if (aitMovimento == null)
		{
			throw new ValidacaoException ("Para a emissão da NIC e preciso que o AIT tenha NIP emitida");
		}
	}
	
	@SuppressWarnings("static-access")
	protected void verificarResponsabilidadeInfracao(int cdAit, Connection connect) throws ValidacaoException
	{
		InfracaoServices infracaoServices = new InfracaoServices();
		Ait ait = AitDAO.get(cdAit, connect);
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao(), connect);
		
		if (infracao.getTpResponsabilidade() == infracaoServices.MULTA_RESPONSABILIDADE_PROPRIETARIO)
		{
			throw new ValidacaoException("Não é possivel gerar NIC para infrações de responsabilidade do proprietário.");
		}
	}
	
	protected void verificarAitOriginario(int cdAit, Connection connect) throws ValidacaoException
	{
		Ait ait = AitDAO.get(cdAit, connect);
		
		if (ait.getCdAitOrigem() > 0)
		{
			throw new ValidacaoException("Não é possivel gerar NIC para AIT que possua código de ait originario, verifique se este AIT já não e NIC.");
		}
	}
	
	protected void verificarTipoInfracao(int cdAit, Connection connect) throws ValidacaoException
	{
		Ait ait = AitDAO.get(cdAit, connect);
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao(), connect);
		
		if (infracao.getNrCodDetran() == InfracaoServices.MULTA_NAO_INDENTIFICACAO_CONDUTOR 
				|| infracao.getNrCodDetran() == InfracaoServices.MULTA_NAO_INDENTIFICACAO_CONDUTOR_2)
		{
			throw new ValidacaoException("Não é possivel gerar NIC para AIT que possua código de infração igual: 50002 ou 50020");
		}
	}
}
