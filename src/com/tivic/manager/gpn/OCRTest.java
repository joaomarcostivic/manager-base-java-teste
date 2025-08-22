package com.tivic.manager.gpn;

import java.io.File;

import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.LoadLibs;

public class OCRTest {

	public static void main(String[] args) {
		
		File imageFile = new File("C:\\Users\\Maurício\\Desktop\\img6.jpg");
		ITesseract instance = new Tesseract();//Tesseract.getInstance();
		File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build bundles English data
        instance.setDatapath(tessDataFolder.getParent());
        instance.setLanguage("por");
         
        try {
            String result = instance.doOCR(imageFile);
           
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

	}

}
