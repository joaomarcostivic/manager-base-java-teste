package com.tivic.manager.mob.lotes.service.dividaativa;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaImportacaoDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaRetornoDTO;
import com.tivic.manager.mob.lotes.enums.dividaativa.LoteStatusDividaAtivaEnum;
import com.tivic.manager.mob.lotes.enums.dividaativa.SituacaoAitDividaAtivaEnum;
import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtiva;
import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtivaAit;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaAitRepository;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaRepository;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ConfirmaLoteDividaAtiva implements IConfirmaLoteDividaAtiva {
	
	private IAitMovimentoService aitMovimentoService;
	private LoteDividaAtivaRepository loteDividaAtivaRepository;
	private LoteDividaAtivaAitRepository loteDividaAtivaAitRepository;
	private IArquivoRepository arquivoRepository;
	
	public ConfirmaLoteDividaAtiva() throws Exception {
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		loteDividaAtivaRepository = (LoteDividaAtivaRepository) BeansFactory.get(LoteDividaAtivaRepository.class);
		loteDividaAtivaAitRepository = (LoteDividaAtivaAitRepository) BeansFactory.get(LoteDividaAtivaAitRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		
	}

	@Override
	public DividaAtivaImportacaoDTO confirmar(DividaAtivaImportacaoDTO dividaAtivaImportacaoDTO, int cdUsuario) throws Exception {
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		CustomConnection customConnection = new CustomConnection();
		ServicoDetranServices servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		
		try {
			customConnection.initConnection(true);
			
			int cdArquivoRetorno = inserirCsvRetorno(dividaAtivaImportacaoDTO.getCsv(), cdUsuario, customConnection);
			int cdLoteDividaAtiva = atualizarLote(dividaAtivaImportacaoDTO.getDividasRetorno().get(0).getIdAit(), cdArquivoRetorno, customConnection);
			
			
			for(DividaAtivaRetornoDTO dividaRetorno: dividaAtivaImportacaoDTO.getDividasRetorno()) {
				int cdAit = getAitById(dividaRetorno.getIdAit(), customConnection);
				atualizarLoteAit(cdLoteDividaAtiva, cdAit, dividaRetorno, customConnection);
				AitMovimento movimento = aitMovimentoService.getStatusMovimento(cdAit, TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey(), customConnection);
				if(movimento.getCdAit() == 0 && dividaRetorno.getIdInsercao() == SituacaoAitDividaAtivaEnum.INSERIDO.getKey()) {
					AitMovimento aitMovimento = setMovimento(cdAit, cdUsuario);
					aitMovimentoList.add(aitMovimento);
					aitMovimentoService.insert(aitMovimento, customConnection);		
				}
			}
			customConnection.finishConnection();
			servicoDetranServices.remessa(aitMovimentoList);
			return dividaAtivaImportacaoDTO;	
		} finally {
			customConnection.closeConnection();
		}
	}

	private int getAitById(String idAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ait", idAit);
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait")
				.fields("cd_ait")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		return search.getList(Ait.class).get(0).getCdAit();
	}
	
	private AitMovimento setMovimento(int cdAit, int cdUsuario) {
		return new AitMovimentoBuilder()
				.setDtMovimento()
				.setCdAit(cdAit)
				.setTpStatus(TipoStatusEnum.SUSPENSAO_DIVIDA_ATIVA.getKey())
				.setCdUsuario(cdUsuario)
				.build();
	}
	
	private int atualizarLote(String idAit, int cdArquivoRetorno, CustomConnection customConnection) throws Exception {
		LoteDividaAtiva lote = loteDividaAtivaRepository.getLoteGerado(idAit, customConnection);
		lote.setStLote(LoteStatusDividaAtivaEnum.FINALIZADO.getKey());
		lote.setCdArquivoRetorno(cdArquivoRetorno);
		loteDividaAtivaRepository.update(lote, customConnection);
		return lote.getCdLoteDividaAtiva();
	}
	
	private void atualizarLoteAit(int cdLoteDividaAtiva, int cdAit, DividaAtivaRetornoDTO dividaRetorno, CustomConnection customConnection) throws Exception {
		LoteDividaAtivaAit dividaAtivaAit = loteDividaAtivaAitRepository.get(cdLoteDividaAtiva, cdAit, customConnection);
		dividaAtivaAit.setDtEnvio(dividaRetorno.getIdInsercao() == SituacaoAitDividaAtivaEnum.INSERIDO.getKey() ? dividaRetorno.getDtEnvio() : null);
		dividaAtivaAit.setLgErro(dividaRetorno.getIdInsercao() == SituacaoAitDividaAtivaEnum.INSERIDO.getKey() ? 0 : 1);
		loteDividaAtivaAitRepository.update(dividaAtivaAit, customConnection);
	}
	
	private int inserirCsvRetorno(byte[] csv, int cdUsuario, CustomConnection customConnection) throws Exception {
		Arquivo arquivoRetorno = new ArquivoBuilder()
				.setNmArquivo("Arquivo de Retorno DividaAtiva")
				.setNmDocumento("Arquivo da Divida Ativa")
				.setDtCriacao(new GregorianCalendar())
				.setCdUsuario(cdUsuario)
				.setBlbArquivo(csv)
				.build();
		
		arquivoRepository.insert(arquivoRetorno, customConnection);
		return arquivoRetorno.getCdArquivo();
	}
}