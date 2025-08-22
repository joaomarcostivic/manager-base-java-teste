package com.tivic.manager.str.ait.veiculo;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.NoContentException;

public class ConvertPlaca {
	private static final String NR_PLACA = "^[A-Z]{3}\\d{4}$";
	private static final Map<Character, Character> numeroParaLetra = new HashMap<>();
	private static final Map<Character, Character> letraParaNumero = new HashMap<>();
	static {
        letraParaNumero.put('A', '0');
        letraParaNumero.put('B', '1');
        letraParaNumero.put('C', '2');
        letraParaNumero.put('D', '3');
        letraParaNumero.put('E', '4');
        letraParaNumero.put('F', '5');
        letraParaNumero.put('G', '6');
        letraParaNumero.put('H', '7');
        letraParaNumero.put('I', '8');
        letraParaNumero.put('J', '9');
        for (Map.Entry<Character, Character> entry : letraParaNumero.entrySet()) {
            numeroParaLetra.put(entry.getValue(), entry.getKey());
        }
    }
	
	public static String convertPlaca(String nrPlaca) throws Exception {
        if (nrPlaca == null || nrPlaca.length() != 7) {
            throw new NoContentException("Placa inválida.");
        }
        char[] caracteres = nrPlaca.toCharArray();
        boolean isPlacaNormal = nrPlaca.matches(NR_PLACA); 
        Character caracterConvertido = isPlacaNormal ? numeroParaLetra.get(caracteres[4]) : letraParaNumero.get(caracteres[4]);
        if (caracterConvertido != null) {
            caracteres[4] = caracterConvertido;
        } else {
            throw new NoContentException("Placa inválida.");
        }
        return new String(caracteres);
    }
}
