package com.tivic.manager.util.manipulatefiles;

import java.io.IOException;
import com.itextpdf.text.DocumentException;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IConcatFileByFolder {
	byte[] concat(String nameFileWithExtension, byte[] byteFile) throws IOException, ValidacaoException, DocumentException;
}
