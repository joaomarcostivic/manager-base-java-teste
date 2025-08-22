package com.tivic.manager.wsdl.detran.sp.incluirautoinfracao;

import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.Logradouro;
import com.tivic.manager.grl.LogradouroDAO;
import com.tivic.manager.grl.Pais;
import com.tivic.manager.grl.PaisDAO;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitImagemServices;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.sp.incluirautoinfracao.IncluirAutoInfracaoDadosEntrada;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ResultSetMap;

public class IncluirAutoInfracaoDadosEntradaFactory {
	
	private static final int MARCA = 0;
	private static final int MODELO = 1;

	public static IncluirAutoInfracaoDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject) throws ValidacaoException{
		Ait ait = aitDetranObject.getAit();
		
		String idCidade = "";
		String nmPais = "";
		Cidade cidadeVeiculo = CidadeDAO.get(ait.getCdCidade());
		if(cidadeVeiculo != null) {
			idCidade = cidadeVeiculo.getIdCidade();
			Estado estadoVeiculo = EstadoDAO.get(cidadeVeiculo.getCdEstado());
			Pais paisVeiculo = PaisDAO.get(estadoVeiculo.getCdPais());
			nmPais = paisVeiculo.getNmPais();
		}
		Logradouro logradouro = LogradouroDAO.get(ait.getCdLogradouroInfracao());
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Agente agente = AgenteDAO.get(ait.getCdAgente());
		Equipamento equipamento = EquipamentoDAO.get(ait.getCdEquipamento());
		ResultSetMap imagens = AitImagemServices.getFromAit(ait.getCdAit());
		
		if(infracao.getNrCodDetran() < 9999) {
			throw new ValidacaoException("Código de Infração do AIT " + ait.getIdAit() + " não existente ou inválido");
		}
		
		if(equipamento == null) {
			throw new ValidacaoException("Equipamento do AIT " + ait.getIdAit() + " não encontrado.");
		}
		
		if (logradouro == null) {
			throw new ValidacaoException("Logradouro do AIT " + ait.getIdAit() + " não encontrado.");
		}
		
		if (agente == null) {
			throw new ValidacaoException("Agente do AIT " + ait.getIdAit() + " não encontrado.");
		}
		
		IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada = new IncluirAutoInfracaoDadosEntrada();
		incluirAutoInfracaoDadosEntrada.setTipoAuto("P");
		incluirAutoInfracaoDadosEntrada.setStatusAuto(0);
		incluirAutoInfracaoDadosEntrada.setNumeroAuto(ait.getIdAit());
		incluirAutoInfracaoDadosEntrada.setPlaca(ait.getNrPlaca());
		incluirAutoInfracaoDadosEntrada.setCpfCnpjProprietario(ait.getNrCpfCnpjProprietario());
		incluirMarcaModeloAutuacao(ait,incluirAutoInfracaoDadosEntrada);
		incluirAutoInfracaoDadosEntrada.setEspecie(ait.getCdEspecieVeiculo());
		incluirAutoInfracaoDadosEntrada.setMunicipioVeiculo(tratarIdCidade(idCidade, ait.getIdAit()));
		incluirAutoInfracaoDadosEntrada.setCorVeiculo(ait.getCdCorVeiculo());
		incluirAutoInfracaoDadosEntrada.setPaisVeiculo(nmPais);
		incluirAutoInfracaoDadosEntrada.setDataInfracao(ait.getDtInfracao());
		incluirAutoInfracaoDadosEntrada.setHoraInfracao(ait.getDtInfracao());
		//ENDERECO
		incluirAutoInfracaoDadosEntrada.setCodigoLogradouro(logradouro.getCdLogradouro());
		incluirAutoInfracaoDadosEntrada.setDescricaoLogradouro(tratarQuebraDeLinha(logradouro.getNmLogradouro()));
		incluirAutoInfracaoDadosEntrada.setReferencia("");
		incluirAutoInfracaoDadosEntrada.setComplemento(tratarQuebraDeLinha(ait.getDsPontoReferencia()));
		incluirAutoInfracaoDadosEntrada.setCodigoLogradouroCruzamento(0);
		//---
		incluirAutoInfracaoDadosEntrada.setInfracao(String.valueOf(infracao.getNrCodDetran()));
		incluirAutoInfracaoDadosEntrada.setDesdobramento(String.valueOf(infracao.getNrCodDetran()));
		incluirAutoInfracaoDadosEntrada.setOrgaoAutuador(Integer.parseInt(orgao.getIdOrgao()));
		//Equipamento fiscalizador
		incluirAutoInfracaoDadosEntrada.setTipoEquipamentoFiscalizador("");
		incluirAutoInfracaoDadosEntrada.setNumeroEquipamentoFiscalizador("");
		incluirAutoInfracaoDadosEntrada.setNumeroCertificadoAfericao("");
		incluirAutoInfracaoDadosEntrada.setMarcaModeloEquipamento("");
		incluirAutoInfracaoDadosEntrada.setTipoEquipamento("");
		//Medicação de equipamento fiscalizador
		incluirAutoInfracaoDadosEntrada.setDataAfericaoEquipamento(ait.getDtAfericao());
		incluirAutoInfracaoDadosEntrada.setMedidaPermitida(ait.getVlVelocidadePermitida());
		incluirAutoInfracaoDadosEntrada.setMedidaConsiderada(ait.getVlVelocidadeAferida());
		incluirAutoInfracaoDadosEntrada.setMedidaAferida(ait.getVlVelocidadeAferida());
		//---
		incluirAutoInfracaoDadosEntrada.setCnhCondutor(ait.getNrCnhCondutor());
		incluirAutoInfracaoDadosEntrada.setUfCnhCondutor(ait.getUfCnhCondutor());
		if(ait.getTpDocumento() == AitServices.TP_DOCUMENTO_CPF)
			incluirAutoInfracaoDadosEntrada.setCpfCondutor(ait.getNrDocumento());
		else
			incluirAutoInfracaoDadosEntrada.setCpfCondutor("");
		incluirAutoInfracaoDadosEntrada.setNomeCondutor(ait.getNmCondutor());
		if(ait.getTpDocumento() == AitServices.TP_DOCUMENTO_RG)
			incluirAutoInfracaoDadosEntrada.setRgCondutor(ait.getNrDocumento());
		else
			incluirAutoInfracaoDadosEntrada.setRgCondutor("");
		incluirAutoInfracaoDadosEntrada.setUfRgCondutor("");
		incluirAutoInfracaoDadosEntrada.setObservacaoCondutor("");
		incluirAutoInfracaoDadosEntrada.setObservacaoAutoInfracao(tratarQuebraDeLinha(ait.getDsObservacao().replaceAll("\"", "'")));
		incluirAutoInfracaoDadosEntrada.setAgenteAutuador(agente.getNrMatricula());
		incluirAutoInfracaoDadosEntrada.setIdEquipamento(equipamento.getIdEquipamento());
		incluirAutoInfracaoDadosEntrada.setIdImpressora("");
		incluirAutoInfracaoDadosEntrada.setAutoEntregue(ait.getLgAutoAssinado()==1 ? 11 : 10);
		incluirAutoInfracaoDadosEntrada.setLatitude(String.valueOf(ait.getVlLatitude()));
		incluirAutoInfracaoDadosEntrada.setLongitude(String.valueOf(ait.getVlLongitude()));
		incluirAutoInfracaoDadosEntrada.setDataCancelamento(ait.getDtCancelamentoMovimento());
		incluirAutoInfracaoDadosEntrada.setObservacaoCancelamento("");
		incluirAutoInfracaoDadosEntrada.setNumeroNovoAuto("");
		//Embarcador e Transportador
		incluirAutoInfracaoDadosEntrada.setNomeEmbarcador("");
		incluirAutoInfracaoDadosEntrada.setCpfCnpjEmbarcador("");
		incluirAutoInfracaoDadosEntrada.setNomeTransportador("");
		incluirAutoInfracaoDadosEntrada.setCnpjTransportador("");
		incluirAutoInfracaoDadosEntrada.setObservacaoEmbarcador("");
		//---
		incluirAutoInfracaoDadosEntrada.setDataValidacao(DateUtil.getDataAtual());
		incluirAutoInfracaoDadosEntrada.setQuantidadeImagens(imagens.size());
		incluirAutoInfracaoDadosEntrada.setNumeroAutoInfracaoDetran(ait.getIdAit());
		return incluirAutoInfracaoDadosEntrada;
	}
	
	private static void incluirMarcaModeloAutuacao(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		MarcaModelo marcaModelo = MarcaModeloDAO.get(ait.getCdMarcaAutuacao());
		if(marcaModelo.getNmMarca() != null && !marcaModelo.getNmMarca().equals("")) {
			incluirAutoInfracaoDadosEntrada.setMarca(marcaModelo.getNmMarca());
		}
		else {
			incluirAutoInfracaoDadosEntrada.setMarca(marcaModelo.getNmModelo().split("[/ ]")[MARCA]);
			if(incluirAutoInfracaoDadosEntrada.getMarca() == null) {
				incluirAutoInfracaoDadosEntrada.setMarca(marcaModelo.getNmModelo());
			}
		}
		if(incluirAutoInfracaoDadosEntrada.getMarca().equals("I")) {
			incluirAutoInfracaoDadosEntrada.setMarca(marcaModelo.getNmModelo().split("/")[MODELO].split(" ")[MARCA]);
		}
		
		incluirAutoInfracaoDadosEntrada.setModelo(marcaModelo.getNmModelo());
	}
	
	private static String tratarIdCidade(String idCidade, String idAit) throws ValidacaoException{
		if (idCidade.trim().equals("")) {
			throw new ValidacaoException("ID da Cidade do AIT: " + idAit + " não encontrado.");
		}
		Integer identificador = Integer.parseInt(idCidade);
		return Util.fillNum(identificador, 4);
	}
	
	private static String tratarQuebraDeLinha(String texto) {
		return texto.replaceAll("\\r\\n|\\n", "").trim();
	}
}
