package com.tivic.manager.ptc.protocolosv3;

import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.portal.vagaespecial.ArquivoDTO;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IProtocoloService {
	public ProtocoloDTO insert(ProtocoloInsertDTO protocolo) throws Exception, ValidacaoException;
	public ProtocoloDTO insert(ProtocoloInsertDTO protocolo, CustomConnection customConnection) throws Exception, ValidacaoException;
	public ProtocoloDTO update(ProtocoloDTO protocolo) throws Exception, ValidacaoException;
	public ProtocoloDTO update(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception;
	public ProtocoloDTO publicar(ProtocoloDTO protocolo) throws Exception, ValidacaoException;
	public ProtocoloDTO publicar(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception;
	public PagedResponse<ProtocoloSearchDTO> find(SearchCriterios searchCriterios) throws Exception, ValidacaoException;
	public ProtocoloDTO cancelar(ProtocoloDTO protocolo) throws Exception, ValidacaoException;
	public ProtocoloDTO cancelar(ProtocoloDTO protocolo, CustomConnection customConnection)	throws Exception, ValidacaoException;
	public Documento getDocumento(int cdDocumento) throws Exception;
	public Documento getDocumento(int cdDocumento, CustomConnection customConnection) throws Exception;
	public void enviarDetran(AitMovimento aitMovimento) throws Exception;
	public PagedResponse<ProtocoloSearchDTO> findProtocoloCredencialEstacionamento(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<ProtocoloSearchDTO> findProtocoloCredencialEstacionamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public PagedResponse<ProtocoloSearchDTO> findJari(SearchCriterios searchCriterios) throws Exception;
	public ProtocoloSearchDTO getProtocoloCredencialEstacionamento(SearchCriterios searchCriterios) throws Exception;
	public ProtocoloSearchDTO getProtocoloCredencialEstacionamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<ArquivoDTO> getArquivoProtocolo(int cdDcumento) throws Exception;
	public List<ArquivoDTO> getArquivoProtocolo(int cdDcumento, CustomConnection customConnection) throws Exception;
}
