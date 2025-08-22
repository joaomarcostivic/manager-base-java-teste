package com.tivic.manager.util.richtext;

import java.io.IOException;
import javax.swing.text.BadLocationException;

public interface IConversorRichText {
	String convert(String richText) throws IOException, BadLocationException;
}
