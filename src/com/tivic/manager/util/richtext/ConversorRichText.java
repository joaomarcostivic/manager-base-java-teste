package com.tivic.manager.util.richtext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

public class ConversorRichText implements IConversorRichText {

	@Override
	public String convert(String richText) throws IOException, BadLocationException {
		writeRichText(richText);
		return readRichText();
	}
	
	private void writeRichText(String richText) throws IOException {
        OutputStream os = new FileOutputStream("indeferimento.txt");
        Writer wr = new OutputStreamWriter(os); 
        BufferedWriter br = new BufferedWriter(wr);
        br.write(richText);
        br.close();
	}
		
	public String readRichText() throws IOException, BadLocationException {
		InputStream is = new FileInputStream("indeferimento.txt");
		DefaultStyledDocument styledDoc = new DefaultStyledDocument();
		new RTFEditorKit().read(is, styledDoc, 0);
		String bodyText = styledDoc.getText(0, styledDoc.getLength());
		File fileText = new File("indeferimento.txt");
		fileText.delete();
		return bodyText;
	}

}
