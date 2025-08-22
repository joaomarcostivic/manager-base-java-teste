package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface IAtaService {
	AtaDTO getAtaDTOJari(SearchCriterios searchCriterios) throws Exception;
	public AtaDTO insert(AtaDTO ataDto) throws Exception;
	List<AtaDTO> insertList(List<AtaDTO> listAtaDTO) throws Exception;
	public AtaDTO updateListAta(AtaDTO ataDto) throws Exception;
	public void salvarRelatores(int cdAta, CustomConnection customConnection) throws IllegalArgumentException, Exception;
	public Search<Ata> find(SearchCriterios searchCriterios) throws Exception;
	public Search<AtaDTO> findAtaDTO(SearchCriterios searchCriterios) throws Exception;
	public Search<AtaDTO> findOcorrenciaDTO(SearchCriterios searchCriterios) throws Exception;
	public byte[] getBoletimReport(SearchCriterios searchCriterios) throws Exception ;
	public byte[] printAta(Integer cdAta) throws Exception;
	public Search<AtaDTO> findDocumentosAta(SearchCriterios searchCriterios) throws Exception;
	public List<AtaDTO> updateAtaJari(List<AtaDTO> listAta) throws Exception;
	public Ata getAtaById(String idAta) throws Exception;
}
