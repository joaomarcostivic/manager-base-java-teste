package com.tivic.manager.util.manipulatefiles;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GenerateFile implements IGenerateFile{

	@Override
	public void generateFile(byte[] file, String path, String nameFile) throws IOException {
		File someFile = new File(path + "/" + nameFile);
		FileOutputStream fos = new FileOutputStream(someFile);
		fos.write(file);
		fos.flush();
		fos.close();
	}

	@Override
	public byte[] convertFileToByte(File file) throws ValidacaoException {
		ByteArrayOutputStream arquivoEmBytes = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			FileInputStream fis = new FileInputStream(file);
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				arquivoEmBytes.write(buf, 0, readNum);
			}
			byte[] bytes = arquivoEmBytes.toByteArray();
			return bytes;
		} 
		catch (IOException e) {
			e.printStackTrace();
			throw new ValidacaoException("Erro ao converter arquivo em byte");
		}
	}
}
