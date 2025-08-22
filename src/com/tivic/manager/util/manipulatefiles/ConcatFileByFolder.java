package com.tivic.manager.util.manipulatefiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.DocumentException;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.files.pdf.IPdfGenerator;

public class ConcatFileByFolder implements IConcatFileByFolder {
	private ArrayList<String> pathFileConcat;
	private IManipulateFolder manipulateFolder;
	private byte[] byteFileConcat;
	private IGenerateFile generateFile;
	private Runtime rt = Runtime.getRuntime();
	private File fileConcat;
	private List<byte[]> listByteFile;
	private IPdfGenerator pdfGenerator;
	private String path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR", "/tivic/work/") + "ConcatFile";
	
	public ConcatFileByFolder() throws Exception {
		manipulateFolder = (IManipulateFolder) BeansFactory.get(IManipulateFolder.class);
		generateFile = (IGenerateFile) BeansFactory.get(IGenerateFile.class);
		pdfGenerator = (IPdfGenerator) BeansFactory.get(IPdfGenerator.class);
		manipulateFolder.createFolder(path);
	}

	@Override
	public byte[] concat(String nameFileWithExtension, byte[] byteFile) throws IOException, ValidacaoException, DocumentException {
		this.pathFileConcat = manipulateFolder.listFiles(new File(path));
		this.byteFileConcat = new byte[] {};
		if (pathFileConcat.isEmpty()) {
			generateFile.generateFile(byteFile, path, nameFileWithExtension);
			rt.gc();
		} else {
			pathFileConcat = manipulateFolder.listFiles(new File(path));
			this.fileConcat = new File(pathFileConcat.get(0));
			this.byteFileConcat = generateFile.convertFileToByte(fileConcat);
			this.listByteFile = new ArrayList<byte[]>();
			this.listByteFile.add(byteFileConcat);
			this.listByteFile.add(byteFile);
			this.byteFileConcat = pdfGenerator.generator(this.listByteFile);
			generateFile.generateFile(this.byteFileConcat, path, nameFileWithExtension);
			rt.gc();
		}
		return this.byteFileConcat;
	}

}
