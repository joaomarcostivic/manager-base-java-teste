package com.tivic.manager.ptc.protocolosv3.externo;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IProtocoloExternoService {
	public ProtocoloExternoDTO insert(ProtocoloExternoDTO protocoloExternoDTO) throws Exception;
	public ProtocoloExternoDTO insert(ProtocoloExternoDTO protocoloExternoDTO, CustomConnection customConnection) throws Exception;
	public ProtocoloExternoDTO update(ProtocoloExternoDTO protocoloExternoDTO, int cdDocumentoExterno, int cdDocumento) throws Exception;
	public ProtocoloExternoDTO update(ProtocoloExternoDTO protocoloExternoDTO, int cdDocumentoExterno, int cdDocumento, CustomConnection customConnection) throws Exception;
	public ProtocoloExternoDTO get(int cdDocumentoExterno, int cdDocumento) throws Exception;
	public ProtocoloExternoDTO get(int cdDocumentoExterno, int cdDocumento, CustomConnection customConnection) throws Exception;
	public PagedResponse<ProtocoloExternoDTO> find(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<ProtocoloExternoDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
