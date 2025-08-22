package com.tivic.manager.mob.ait.imagemvideo.aitimagem;

import java.util.List;

import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IAitImagemService {
	List<AitImagem> buscarListaImagens(AitImagemSearch aitImagemSearch) throws ValidacaoException, Exception;
	AitImagem buscarImagem(AitImagemSearch aitImagemSearch) throws ValidacaoException, Exception;
}