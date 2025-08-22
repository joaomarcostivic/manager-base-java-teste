package com.tivic.manager.mob.lotes.service.dividaativa;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.lotes.builders.dividaativa.DividaAtivaCSVBuilder;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraArquivoDividaAtiva implements IGeraArquivoDividaAtiva {
	
	private LoteDividaAtivaRepository loteDividaAtivaRepository;
	
	public GeraArquivoDividaAtiva() throws Exception {
		loteDividaAtivaRepository = (LoteDividaAtivaRepository) BeansFactory.get(LoteDividaAtivaRepository.class);
	}

	public byte[] gerarArquivoEnvio(int cdLote, CustomConnection customConnection) throws Exception {
		List<DividaAtivaDTO> dividaList = loteDividaAtivaRepository.getInfoCSVByCdLote(setCriterios(cdLote), customConnection);
		List<String> registros = registrosEnvio(dividaList, customConnection);
		return buildCSV(registros);
	}
	
	private SearchCriterios setCriterios(int cdLote) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("E.tp_status", TipoStatusEnum.NIP_ENVIADA.getKey());
		searchCriterios.addCriteriosEqualInteger("A.cd_lote", cdLote);
		return searchCriterios;
	}
	
	private List<String> registrosEnvio(List<DividaAtivaDTO> aits, CustomConnection customConnection) throws Exception {
		List<String> dividas = new ArrayList<String>();
		for(DividaAtivaDTO divida: aits) {
			
			dividas.add(new DividaAtivaCSVBuilder(divida, customConnection).setIdAit()
	                .setColumn().setInscricaoMunicipal()
	                .setColumn().setCpfCnpj()
	                .setColumn().setNrParcelas()
	                .setColumn().setConta()
	                .setColumn().setDtEmissao()
	                .setColumn().setDtVencimento()
	                .setColumn().setDtValidade()
	                .setColumn().setVlMulta()
	                .setColumn().setMesMovimento()
	                .setColumn().setAnoMovimento()
	                .setColumn().setNmPessoa()
	                .setColumn().setTpLogradouro()
	                .setColumn().setDsLogradouro()
	                .setColumn().setNrLogradouro()
	                .setColumn().setNmBairro()
	                .setColumn().setNrCep()
	                .setColumn().setNmCidade()
	                .setColumn().setNmEstado()
	                .build());
		}
		return dividas;
	}
	
	private byte[] buildCSV(List<String> registros) throws Exception {
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
	        for (String ait : registros) {
	            bufferedWriter.write(ait);
	            bufferedWriter.newLine();
	        }
	    }
	    return output.toByteArray();
	}
}
