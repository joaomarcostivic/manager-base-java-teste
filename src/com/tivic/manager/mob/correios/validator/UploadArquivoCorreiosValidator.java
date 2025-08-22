package com.tivic.manager.mob.correios.validator;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.correios.IFTPSService;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoCorreiosDTO;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.IArquivoRetorno;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class UploadArquivoCorreiosValidator implements Validator<ArquivoRetornoCorreiosDTO>{

	private IArquivoRepository arquivorepository;
	private IFTPSService iftpService;
	
	public UploadArquivoCorreiosValidator() throws Exception {
		this.arquivorepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		this.iftpService = (IFTPSService) BeansFactory.get(IFTPSService.class);
	}
	
	@Override
	public void validate(ArquivoRetornoCorreiosDTO arquivoRetornoCorreiosDTO, CustomConnection customConnection) throws Exception {
		String nmArquivo = removeFileExtension(arquivoRetornoCorreiosDTO.getNmArquivo());
		List<Arquivo> listArquivo = findArquivo(nmArquivo, customConnection);	
		if(!listArquivo.isEmpty()) {
			Arquivo arquivo = listArquivo.get(0);
			if(arquivo.getCdArquivo() > 0 && arquivo.getNmArquivo().equals(nmArquivo)) {
				if(iftpService.isConnected()) {
					iftpService.deleteFile(arquivoRetornoCorreiosDTO.getNmArquivo());
				}
				throw new BadRequestException("O arquivo " + arquivoRetornoCorreiosDTO.getNmArquivo() + " já foi importado.");
			}
		}
	}
	
	private List<Arquivo> findArquivo(String nmArquivo, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosLikeAnyString("nm_arquivo", nmArquivo);
		 return arquivorepository.find(searchCriterios);
	}
	
	private String removeFileExtension(String nmFile) throws Exception {
		return nmFile.substring(0, nmFile.lastIndexOf('.'));
	}
}
