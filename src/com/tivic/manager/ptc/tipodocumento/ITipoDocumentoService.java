package com.tivic.manager.ptc.tipodocumento;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.TipoDocumento;


public interface ITipoDocumentoService {
	public TipoDocumento get(int id) throws BadRequestException, Exception;
	public List<TipoProtocolo> getTiposDisponiveis(int cdAit) throws BadRequestException, Exception;
} 
