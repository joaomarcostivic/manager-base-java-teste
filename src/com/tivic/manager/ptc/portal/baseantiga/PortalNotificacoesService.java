package com.tivic.manager.ptc.portal.baseantiga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.pessoa.dto.AitPortalDTO;
import com.tivic.manager.mob.pessoa.dto.ListAitPortalDTOBuilder;
import com.tivic.manager.ptc.portal.jari.cartajulgamento.IGeraCartaJulgamento;
import com.tivic.manager.str.ait.veiculo.ConvertPlaca;
import com.tivic.manager.util.ValidatorUtils;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class PortalNotificacoesService implements IPortalNotificacoesService {
	
	private IGeraCartaJulgamento geraCartaJulgamento;
	
	public PortalNotificacoesService() throws Exception {
		this.geraCartaJulgamento = (IGeraCartaJulgamento) BeansFactory.get(IGeraCartaJulgamento.class);
	}

	@Override
	public List<AitPortalDTO> listarNotificacoes(String nrPlaca, String nrRenavam, String nrAit)
			throws ValidacaoException, Exception {
		CustomConnection customConnection = new CustomConnection();
		String nrPlacaConvertida = ConvertPlaca.convertPlaca(nrPlaca);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("cd_renavan", nrRenavam, true);
		searchCriterios.addCriteriosEqualString("nr_ait", nrAit, nrAit != null);
		Search<AitPortalDTO> search = new SearchBuilder<AitPortalDTO>("ait A")
		.fields("nr_placa, dt_infracao, dt_movimento, nr_ait, tp_status, codigo_ait as cd_ait, dt_prazo_defesa, dt_vencimento, lg_penalidade_advertencia_nip")
		.searchCriterios(searchCriterios)
		.additionalCriterias("nr_placa IN (" + "'" + nrPlaca + "'" + ", " + "'" + nrPlacaConvertida + "'" + ")")
		.orderBy("dt_infracao desc")
		.customConnection(customConnection)
		.build();
		
		List<AitPortalDTO> lista = search.getList(AitPortalDTO.class);
		if(lista.size() == 0)
			throw new NoContentException("Nenhum resultado encontrado.");

		return new ListAitPortalDTOBuilder(lista).build();
	}

	@Override
	public byte[] gerarSegundaViaNotificacao(int cdAit, int tpStatus) throws Exception {
		switch(tpStatus) {
			case AitMovimentoServices.NAI_ENVIADO:
				return gerarSegundaViaNAI(cdAit);
			case AitMovimentoServices.NIP_ENVIADA:
				return gerarSegundaViaNIP(cdAit);
			default:
				throw new ValidacaoException("Tipo de notificação não encontrada.");
		}
	}
	
	private byte[] gerarSegundaViaNAI(int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			validarParametros();
			byte[] printNAI = AitReportServices.getNAISegundaVia(cdAit, customConnection.getConnection());
			customConnection.finishConnection();
			return printNAI;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private byte[] gerarSegundaViaNIP(int cdAit) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        validarParametros();
	        byte[] printNAI = AitReportServices.getNIPSegundaVia(cdAit, customConnection.getConnection());
	        customConnection.finishConnection();
	        return printNAI;
	    } finally {
	        customConnection.closeConnection();
	    }
	}

	@Override
	public List<CidadeDTO> findCidade() throws SQLException {
		Connection connect = Conexao.conectar();
		String sql = "SELECT * FROM municipio";
		PreparedStatement pstmt = connect.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		List<CidadeDTO> cidadeList = new ArrayList<CidadeDTO>();
		while(rs.next()){
			CidadeDTO cidade = new CidadeDTO();
			cidade.setCdCidade(rs.getInt("cod_municipio"));
			cidade.setNmCidade(rs.getString("nm_municipio") + "-" + rs.getString("nm_uf"));
			cidade.setSgCidade(rs.getString("nm_uf"));
			cidadeList.add(cidade);
		}
		return cidadeList;
		
	}
	
	private void validarParametros() throws Exception {
		ValidatorUtils.validateString(ParametroServices.getValorOfParametro("nr_cep"), "Parâmetro de Número de CEP não configurado para o remetente.");
		ValidatorUtils.validateString(ParametroServices.getValorOfParametro("nm_logradouro"), "Parâmetro de Nome de Logradouro não configurado para o remetente.");
		ValidatorUtils.validateString(ParametroServices.getValorOfParametro("nr_endereco"), "Parâmetro de Número de Endereço não configurado para o remetente.");
		ValidatorUtils.validateString(ParametroServices.getValorOfParametro("nm_bairro"), "Parâmetro de Nome de Bairro não configurado para o remetente.");
		ValidatorUtils.validateString(ParametroServices.getValorOfParametro("nm_municipio"), "Parâmetro de Nome de Cidade não configurado para o remetente.");
		ValidatorUtils.validateInteger(ParametroServices.getValorOfParametroAsInteger("TP_SISTEMA_PORTAL_ANTIGO", 0), false, "Parâmetro de Nome de Sistema não configurado.");
	}
	
	@Override
	public byte[] getCartaJulgamento(int cdAit, int tpStatus) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] cartaJulgamento = getCartaJulgamento(cdAit, tpStatus, customConnection);
			customConnection.finishConnection();
			return cartaJulgamento;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] getCartaJulgamento(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		byte[] cartaJulgamento = this.geraCartaJulgamento.gerar(cdAit, tpStatus, customConnection);
		return cartaJulgamento;
	}
	
}
