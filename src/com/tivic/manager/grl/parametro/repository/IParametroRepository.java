package com.tivic.manager.grl.parametro.repository;

import java.awt.image.BufferedImage;

import com.tivic.manager.grl.Parametro;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IParametroRepository {
	void insert(Parametro objeto, CustomConnection customConnection) throws ValidacaoException, Exception;
	void update(Parametro objeto, CustomConnection customConnection) throws ValidacaoException, Exception;
	String getValorOfParametroAsString(String nmParametro) throws Exception;
	String getValorOfParametroAsString(String nmParametro, CustomConnection customConnection) throws Exception;
	int getValorOfParametroAsInt(String nmParametro) throws Exception;
	int getValorOfParametroAsInt(String nmParametro, CustomConnection customConnection) throws Exception;
	byte[] getValorOfParametroAsByte(String nmParametro) throws Exception;
	byte[] getValorOfParametroAsByte(String nmParametro, CustomConnection customConnection) throws Exception;
	BufferedImage byteasImgBuffer(String string) throws Exception;
	BufferedImage byteasImgBuffer(String string, CustomConnection customConnection) throws Exception;
	String recImageToString(String nmParametro) throws Exception;
	String recImageToString(String nmParametro, CustomConnection customConnection) throws Exception;
	Parametro getParametroByName(String nmParametro) throws Exception;
	Parametro getParametroByName(String nmParametro, CustomConnection customConnection) throws Exception;
	byte[] getValorOfParametroImageAsBytes(String nmParametro) throws Exception;
	boolean getValorOfParametroAsBoolean(String nmParametro) throws Exception;
	boolean getValorOfParametroAsBoolean(String nmParametro, CustomConnection customConnection) throws Exception;
	int getValorAsIntWithCustomDb(String nmParametro) throws Exception;
	String getValorAsStringWithCustomDb(String nmParametro) throws Exception;
	Parametro getByName(String name) throws Exception;
	Parametro getByName(String name, CustomConnection customConnection) throws Exception;
	int getOrgaoAutuanteParamValue() throws Exception;
	int getOrgaoAutuanteParamValue(CustomConnection customConnection) throws Exception;
}
