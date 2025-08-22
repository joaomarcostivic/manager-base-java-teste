package com.tivic.manager.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

public class Extenso {

    public Extenso() {
        nro = new ArrayList<Integer>();
    }

    public Extenso(BigDecimal dec) {
        this();
        setNumber(dec);
    }

    public Extenso(double dec) {
        this();
        setNumber(dec);
    }

    public void setNumber(BigDecimal dec) {
        num = dec.setScale(2, 4).multiply(BigDecimal.valueOf(100L)).toBigInteger();
        nro.clear();
        if(num.equals(BigInteger.ZERO)) {
            nro.add(new Integer(0));
            nro.add(new Integer(0));
        }
        else {
            addRemainder(100);
            for(; !num.equals(BigInteger.ZERO); addRemainder(1000));
        }
    }

    public void setNumber(double dec) {
        setNumber(new BigDecimal(dec));
    }

    public void show() {
        for(Iterator<?> valores = nro.iterator(); valores.hasNext(); 
        System.out.println(((Integer)valores.next()).intValue()));
        System.out.println(toString());
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for(int ct = nro.size() - 1; ct > 0; ct--) {
            if(buf.length() > 0 && !ehGrupoZero(ct))
                buf.append(" e ");
            buf.append(numToString(((Integer)nro.get(ct)).intValue(), ct));
        }
        if(buf.length() > 0) {
            if(ehUnicoGrupo())
                buf.append(" de ");
            for(; buf.toString().endsWith(" "); buf.setLength(buf.length() - 1));
            if(ehPrimeiroGrupoUm())
                buf.insert(0, "h");
            if(nro.size() == 2 && ((Integer)nro.get(1)).intValue() == 1)
                buf.append(" real");
            else
                buf.append(" reais");
            if(((Integer)nro.get(0)).intValue() != 0)
                buf.append(" e ");
        }
        if(((Integer)nro.get(0)).intValue() != 0)
            buf.append(numToString(((Integer)nro.get(0)).intValue(), 0));
        return buf.toString();
    }

    public String toString(boolean isMoney) {
        StringBuffer buf = new StringBuffer();
        for(int ct = nro.size() - 1; ct > 0; ct--) {
            if(buf.length() > 0 && !ehGrupoZero(ct))
                buf.append(" e ");
            buf.append(numToString(((Integer)nro.get(ct)).intValue(), ct));
        }
        if(buf.length() > 0) {
            if(ehUnicoGrupo())
                buf.append(" de ");
            for(; buf.toString().endsWith(" "); buf.setLength(buf.length() - 1));
            if(ehPrimeiroGrupoUm() & isMoney)
                buf.insert(0, "h");
            if (isMoney) {
	            if(nro.size() == 2 && ((Integer)nro.get(1)).intValue() == 1)
	                buf.append(" real");
	            else
	                buf.append(" reais");
	            if(((Integer)nro.get(0)).intValue() != 0)
	                buf.append(" e ");
            }
        }
        if(isMoney && ((Integer)nro.get(0)).intValue() != 0)
            buf.append(numToString(((Integer)nro.get(0)).intValue(), 0));
        return buf.toString();
    }
    
    private boolean ehPrimeiroGrupoUm() {
        return ((Integer)nro.get(nro.size() - 1)).intValue() == 1;
    }

    private void addRemainder(int divisor) {
        BigInteger newNum[] = num.divideAndRemainder(BigInteger.valueOf(divisor));
        nro.add(new Integer(newNum[1].intValue()));
        num = newNum[0];
    }

    private boolean ehUnicoGrupo() {
        if(nro.size() <= 3)
            return false;
        if(!ehGrupoZero(1) && !ehGrupoZero(2))
            return false;
        boolean hasOne = false;
        for(int i = 3; i < nro.size(); i++) {
            if(((Integer)nro.get(i)).intValue() == 0)
                continue;
            if(hasOne)
                return false;
            hasOne = true;
        }
        return true;
    }

    boolean ehGrupoZero(int ps) {
        if(ps <= 0 || ps >= nro.size())
            return true;
        else
            return ((Integer)nro.get(ps)).intValue() == 0;
    }

    private String numToString(int numero, int escala) {
        int unidade = numero % 10;
        int dezena = numero % 100;
        int centena = numero / 100;
        StringBuffer buf = new StringBuffer();
        if(numero != 0) {
            if(centena != 0)
                if(dezena == 0 && centena == 1)
                    buf.append(Numeros[2][0]);
                else
                    buf.append(Numeros[2][centena]);
            if(buf.length() > 0 && dezena != 0)
                buf.append(" e ");
            if(dezena > 19) {
                dezena /= 10;
                buf.append(Numeros[1][dezena - 2]);
                if(unidade != 0) {
                    buf.append(" e ");
                    buf.append(Numeros[0][unidade]);
                }
            }
            else if(centena == 0 || dezena != 0)
                buf.append(Numeros[0][dezena]);
            buf.append(" ");
            if(numero == 1)
                buf.append(Qualificadores[escala][0]);
            else
                buf.append(Qualificadores[escala][1]);
        }
        return buf.toString();
    }
    
    public String numExtToString(int numero) {
        int unidade = numero % 10;
        int dezena = (numero % 100) ;
        int centena = numero / 100;
        
        StringBuffer buf = new StringBuffer();
        if(numero != 0) {
            if(centena != 0)
                if(dezena == 1 && centena == 1)
                    buf.append(Ordinais[2][1]);
                else
                    buf.append(Ordinais[2][centena]);
            if(buf.length() > 0 && dezena != 0)
                buf.append(" ");
            if(dezena > 9) {
                dezena /= 10;

                buf.append(Ordinais[1][dezena - 1]);
                if(unidade != 0) {
                    buf.append(" ");
                    buf.append(Ordinais[0][unidade]);
                }
            }
            else if(centena == 0 || dezena != 0)
                buf.append(Ordinais[0][dezena%10]);
            buf.append(" ");
//            if(numero == 1)
//                buf.append(Qualificadores[escala][0]);
//            else
//                buf.append(Qualificadores[escala][1]);
        }
        return buf.toString();
    }

    public static void main(String args[]) {
//            Extenso teste = new Extenso(2323243.23);
//            System.out.println("Extenso : " + teste.toString(false));
    	Extenso teste = new Extenso();
    	for (int i = 1; i <=31; i++) {
    		System.out.println(teste.numExtToString(i));
		}
    	
    }

    private ArrayList<Integer> nro;
    private BigInteger num;
    private String Qualificadores[][] = {
        {"centavo", "centavos"},
        {"", ""}, 
        {"mil", "mil"},
        {"milhão", "milhões"},
        {"bilhão", "bilhões"}, 
        {"trilhão", "trilhaes"}, 
        {"quatrilhão", "quatrilhões"},
        {"quintilhão", "quintilhões"}, 
        {"sextilhão", "sextilhões"}, 
        {"septilhão", "septilhões"}
    };
    
    private String Numeros[][] = {
        {"zero", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", 
         "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"},
        {"vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"}, 
        {"cem", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"}
    };
    
    private String Ordinais[][] = {
            {"", "primeiro", "segundo", "terceiro", "quarto", "quinto", "sexto", "sétimo", "oitavo", "nono"},
            {"décimo", "vigésimo", "trigésimo", "quadragésimo", "quinquagésimo", "sexagésimo", " septuagésimo", " octogésimo", "nonagésimo"}, 
            {"centésimo", "ducentésimo", " trecentésimo", " quadrigentésimo", " quingentésimo", " sexcentésimo", " septigentésimo", " octigentésimo", " nongentésimo"}
        };
}