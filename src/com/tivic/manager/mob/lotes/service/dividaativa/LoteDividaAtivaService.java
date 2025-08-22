package com.tivic.manager.mob.lotes.service.dividaativa;

import java.util.List;

import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaImportacaoDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaRetornoDTO;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.service.dividaativa.exceptions.NenhumLoteDividaAtivaEncontradoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class LoteDividaAtivaService implements ILoteDividaAtivaService {
	
	private IGeraLoteDividaAtiva geraLoteDividaAtiva;
	private IConfirmaLoteDividaAtiva confirmaLoteDividaAtiva;
	private IArquivoRepository arquivoRepository;
	
	public LoteDividaAtivaService() throws Exception {
		geraLoteDividaAtiva = (IGeraLoteDividaAtiva) BeansFactory.get(IGeraLoteDividaAtiva.class);
		confirmaLoteDividaAtiva = (IConfirmaLoteDividaAtiva) BeansFactory.get(IConfirmaLoteDividaAtiva.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
	}

	@Override
	public Lote gerarLoteDividaAtiva(List<DividaAtivaDTO> aitList, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Lote loteDividaAtiva = gerarLoteDividaAtiva(aitList, cdUsuario, customConnection);
			customConnection.finishConnection();
			return loteDividaAtiva;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public Lote gerarLoteDividaAtiva(List<DividaAtivaDTO> aitList, int cdUsuario, CustomConnection customConnection) throws Exception {
		Lote lote = geraLoteDividaAtiva.gerarLote(cdUsuario, customConnection);
		int cdLoteDividaAtiva = geraLoteDividaAtiva.gerarLoteDividaAtiva(lote.getCdLote(), customConnection);
		geraLoteDividaAtiva.gerarLoteDividaAtivaAit(aitList, cdLoteDividaAtiva, customConnection); 
		int cdArquivo = geraLoteDividaAtiva.inserirArquivo(lote.getCdLote(), customConnection);
		geraLoteDividaAtiva.atualizaLote(lote.getCdLote(), cdArquivo, customConnection);
		lote.setCdArquivo(cdArquivo);
		return lote;
	}

	@Override
	public byte[] getArquivo(int cdArquivo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			byte[] arquivo = getArquivo(cdArquivo, customConnection);
			customConnection.finishConnection();
			return arquivo;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public byte[] getArquivo(int cdArquivo, CustomConnection customConnection) throws Exception {
		return arquivoRepository.get(cdArquivo, customConnection).getBlbArquivo();
	}
	
	
	@Override
	public DividaAtivaImportacaoDTO confirmarDividaAtiva(DividaAtivaImportacaoDTO dividaAtivaImportacaoDTO, int cdUsuario) throws Exception {
		return confirmaLoteDividaAtiva.confirmar(dividaAtivaImportacaoDTO, cdUsuario);
	}

	@Override
	public List<DividaAtivaDTO> searchInLoteDividaAtiva(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<DividaAtivaDTO> aits = searchInLoteDividaAtiva(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aits;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<DividaAtivaDTO> searchInLoteDividaAtiva(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<DividaAtivaDTO> search = new SearchBuilder<DividaAtivaDTO>("grl_lote A")
				.fields("A.id_lote, D.id_ait, D.nm_proprietario as nm_pessoa, D.nr_placa, "
						+ "D.vl_multa, B.st_lote, C.lg_erro as st_ait, C.dt_envio, B.cd_arquivo_retorno ")
				.addJoinTable("JOIN mob_lote_divida_ativa B ON (A.cd_lote = B.cd_lote)")
				.addJoinTable("JOIN mob_lote_divida_ativa_ait C ON (B.cd_lote_divida_ativa = C.cd_lote_divida_ativa)")
				.addJoinTable("JOIN mob_ait D ON (C.cd_ait = D.cd_ait)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.count()
				.build();
		
		if(search.getList(DividaAtivaDTO.class).isEmpty()) {
			throw new NenhumLoteDividaAtivaEncontradoException();
		}
		
		return search.getList(DividaAtivaDTO.class);
	}
}
