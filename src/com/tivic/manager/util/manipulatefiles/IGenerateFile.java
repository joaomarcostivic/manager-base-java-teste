package com.tivic.manager.util.manipulatefiles;

import java.io.File;
import java.io.IOException;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IGenerateFile {
	void generateFile(byte[] file, String path, String nameFile) throws IOException;
	byte[] convertFileToByte(File file) throws ValidacaoException;
}
