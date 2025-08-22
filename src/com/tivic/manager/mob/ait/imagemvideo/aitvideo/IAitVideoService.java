package com.tivic.manager.mob.ait.imagemvideo.aitvideo;

import java.util.List;

import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IAitVideoService {
	List<AitImagem> buscarListaVideos(AitVideoSearch aitVideoSearch) throws ValidacaoException, Exception;
	AitImagem buscarVideo(AitVideoSearch aitVideoSearch) throws ValidacaoException, Exception;
}