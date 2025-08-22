package com.tivic.manager.fsc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.manager.alm.GrupoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;

public class RegistroEcfServices {

	public static Result loadFromFile(String fileName) {
		Connection connect = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(fileName, "rw");
			String line = "";
			while((line = raf.readLine()) != null)	{
				if(line.substring(0,2).equals("10") || line.substring(0,2).equals("11") || line.substring(0,2).equals("90"))
					continue;
				Result result = save(line, connect);
				if(result.getCode() <= 0) {
					System.out.println("Registro não salvo: "+line);
					Util.registerLog(new Exception("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line));
				}	
			}
			raf.close();
			return new Result(1);
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static Result save(String txtRegistroEcf) {
		return save(txtRegistroEcf, null);
	}

	public static Result save(String txtRegistroEcf, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			int cdReferenciaEcf             = 0;
			String tpRegistroEcf            = txtRegistroEcf.substring(0, 2).equals("60") ? txtRegistroEcf.substring(0, 3) : txtRegistroEcf.substring(0, 2);
			GregorianCalendar dtRegistroEcf = null;
			// Buscando o código do ECF - para registros do tipo 60
			if(tpRegistroEcf.substring(0,2).equals("60") && !tpRegistroEcf.equals("60R")) {
				dtRegistroEcf = Util.convStringToCalendar(txtRegistroEcf.substring(9, 11)+"/"+txtRegistroEcf.substring(7, 9)+"/"+txtRegistroEcf.substring(3, 7));
				//
				String nrSerieEcf   = txtRegistroEcf.substring(11, 31);
				ResultSet rs = connect.prepareStatement("SELECT cd_referencia FROM bpm_referencia " +
													    "WHERE nr_serie = \'"+nrSerieEcf.trim()+"\'").executeQuery();
				if(rs.next()) 
					cdReferenciaEcf = rs.getInt("cd_referencia");
				else
					return new Result(-1, "O cadastro do ECF com serial \'"+nrSerieEcf.trim()+"\' não foi localizado!");
			}
			else if(tpRegistroEcf.equals("60R"))
				dtRegistroEcf = Util.convStringToCalendar("01/"+txtRegistroEcf.substring(3, 5)+"/"+txtRegistroEcf.substring(5, 9));
			else if (tpRegistroEcf.equals("75"))
				dtRegistroEcf = Util.convStringToCalendar(txtRegistroEcf.substring(8, 10)+"/"+txtRegistroEcf.substring(6, 8)+"/"+txtRegistroEcf.substring(2, 6));
			
			ResultSet rs = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
					                                "WHERE txt_registro_ecf   = \'"+txtRegistroEcf+"\'").executeQuery();
			// Montando o registro
			RegistroEcf registroEcf = new RegistroEcf(0,tpRegistroEcf, dtRegistroEcf, 0 /*vlRegistroEcf*/, txtRegistroEcf, cdReferenciaEcf, 0 /*lgSped*/, "");
			if(rs.next())
				registroEcf.setCdRegistroEcf(rs.getInt("cd_registro_ecf"));
			// Salvando
			if(registroEcf.getCdRegistroEcf() <= 0)
				return new Result(RegistroEcfDAO.insert(registroEcf, connect), "Falha ao tentar inserir!");
			else
				return new Result(RegistroEcfDAO.update(registroEcf, connect), "Falha ao tentar atualizar!");
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result arrumarResultado(String fileName, int cdEmpresa, byte[] arquivo){
		try {
			Result resultado = new Result(1);
			resultado = configurarCodigoItem(fileName, cdEmpresa, arquivo);
			if(resultado.getCode() < 0){
				return resultado;
			}
			resultado = configurarCfopsCorretos(fileName, cdEmpresa, arquivo);
			if(resultado.getCode() < 0){
				return resultado;
			}
			resultado = configurarCfop(fileName, cdEmpresa, arquivo);
			
			return resultado;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace();
			return new Result(-1, "Erro ao arrumar resultado.");
		}
	}
	
	public static Result configurarCodigoItem(String fileName, int cdEmpresa, byte[] arquivo){
		Connection connect = Conexao.conectar();
		RandomAccessFile raf = null;
		try{
			connect.setAutoCommit(false);
			
			if(arquivo != null){
				File arqTemp = new File(fileName);
				FileOutputStream fileW = new FileOutputStream(arqTemp);
				fileW.write(arquivo);
				fileW.flush();
				fileW.close();
			}
			
			raf = new RandomAccessFile(fileName, "rw");
			ArrayList<String> vetorTexto = new ArrayList<String>();
			String line = "";
			
			
			PreparedStatement pstmtProdutoServico = connect.prepareStatement("SELECT A.cd_produto_servico, A.id_produto_servico, B.id_reduzido FROM grl_produto_servico A " +
																	"			JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = "+cdEmpresa+")" +
																	"		  WHERE A.cd_produto_servico = ? OR A.id_produto_servico = ? OR B.id_reduzido = ?");
			
//			PreparedStatement pstmtDocumentoSaidaItem = connect.prepareStatement("SELECT * FROM alm_documento_saida_item A " +
//																	"	JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
//																	" WHERE B.nr_documento_saida like ?");
			
			HashMap<Integer, String> linhasTexto = new HashMap<Integer, String>();
//			String nrDocumento = "";
			while(fileName != null && (line = raf.readLine()) != null)	{
				String lineQuebrada[] = line.split("\\|");
//				if(lineQuebrada[1].equals("C405")){
//					nrDocumento = lineQuebrada[5];
//				}
//				else 
				if(lineQuebrada[1].equals("C425") || lineQuebrada[1].equals("C470") || lineQuebrada[1].equals("C495")){
					String codItem = "";
					if(lineQuebrada[1].equals("C425") || lineQuebrada[1].equals("C470")){
						codItem = lineQuebrada[2];
					}
					else{
						codItem = lineQuebrada[3];
					}
					
					
					if(Util.isNumber(codItem))
						pstmtProdutoServico.setInt(1, Integer.parseInt(codItem));
					else
						pstmtProdutoServico.setInt(1, 0);
					
					pstmtProdutoServico.setString(2, codItem);
					pstmtProdutoServico.setString(3, codItem);
					
					ResultSetMap rsmProdutoServico = new ResultSetMap(pstmtProdutoServico.executeQuery());
					
					if(rsmProdutoServico.next()){
						if(rsmProdutoServico.getString("cd_produto_servico") != null && !rsmProdutoServico.getString("cd_produto_servico").equals(""))
							codItem = rsmProdutoServico.getString("cd_produto_servico");
					}
					else{
						Connection connect2 = Conexao.conectar("jdbc:postgresql://127.0.0.1/postoCanes23092014", "postgres", "t1v1k!");
						try{
							
							PreparedStatement pstmtProdutoServico2 = connect2.prepareStatement("SELECT A.cd_produto_servico, A.id_produto_servico, B.id_reduzido FROM grl_produto_servico A " +
																	"			JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = "+cdEmpresa+")" +
																	"		  WHERE A.cd_produto_servico = ? OR A.id_produto_servico = ? OR B.id_reduzido = ?");
							
							
							if(Util.isNumber(codItem))
								pstmtProdutoServico2.setInt(1, Integer.parseInt(codItem));
							else
								pstmtProdutoServico2.setInt(1, 0);
							
							pstmtProdutoServico2.setString(2, codItem);
							pstmtProdutoServico2.setString(3, codItem);
							
							rsmProdutoServico = new ResultSetMap(pstmtProdutoServico2.executeQuery());
							
							if(rsmProdutoServico.next()){
								if(rsmProdutoServico.getString("cd_produto_servico") != null && !rsmProdutoServico.getString("cd_produto_servico").equals(""))
									codItem = rsmProdutoServico.getString("cd_produto_servico");
							}
							else{
								return new Result(-1, "Produto não encontrado");
							}
						}
						catch(Exception e){
							Conexao.rollback(connect2);
						}
						finally{
							Conexao.desconectar(connect2);
						}
						
						
						
						
						
						
						
						
						
						
//						//Busca o valor unitario para buscar o codigo de chave do produto
//						float vlUnitario = 0;
//						//Busca a quantidade para buscar o codigo de chave do produto
//						float qtSaida = 0;
//						
//						if(lineQuebrada[1].equals("C425") || lineQuebrada[1].equals("C470")){
//							vlUnitario = Float.parseFloat(lineQuebrada[5].replaceAll(",", "."));
//							qtSaida = Float.parseFloat(lineQuebrada[3].replaceAll(",", "."));
//						}
//						else{
//							vlUnitario = Float.parseFloat(lineQuebrada[7].replaceAll(",", "."));
//							qtSaida = Float.parseFloat(lineQuebrada[4].replaceAll(",", "."));
//						}
//						
//						//Verifica o preco do produto no documento para saber o codigo real dele
//						pstmtDocumentoSaidaItem.setString(1, nrDocumento);
//						ResultSetMap rsmDocumentoSaidaItem = new ResultSetMap(pstmtDocumentoSaidaItem.executeQuery());
//						int produtoEncontrado = 0;
//						while(rsmDocumentoSaidaItem.next()){
//							if(Util.arredondar(rsmDocumentoSaidaItem.getFloat("vl_unitario"), 2) == vlUnitario 
//							&& Util.arredondar(rsmDocumentoSaidaItem.getFloat("qt_saida"), 3) == qtSaida){
//								codItem = String.valueOf(rsmDocumentoSaidaItem.getInt("cd_produto_servico"));
//								produtoEncontrado++;
//							}
//						}
//						
//						if(produtoEncontrado == 0){
//							return new Result(-1, "Produto não encontrado");
//						}
//						else if(produtoEncontrado > 1){
//							return new Result(-1, "Mais de um produto com mesmo preço e quantidade. Impossível identificar o código do item");
//						}
						
					}
					
					
					if(lineQuebrada[1].equals("C425") || lineQuebrada[1].equals("C470")){
						lineQuebrada[2] = codItem;
					}
					else{
						lineQuebrada[3] = codItem;
					}
					
					int tamanho = 0;
					if(lineQuebrada[1].equals("C425")){
						tamanho = 7;
					}
					else if(lineQuebrada[1].equals("C470")){
						tamanho = 11;
					}
					else{
						tamanho = 15;
					}
					
					line = "";
					for(int i = 1; i <= tamanho; i++){
						String parte = (lineQuebrada.length > i ? lineQuebrada[i] : "");
						line += "|" + parte; 
					}
					line += "|"; 
					
				}
				
				if(lineQuebrada[1].equals("C405"))
					linhasTexto = new HashMap<Integer, String>();
				
				int nrLinha = (lineQuebrada[1].equals("C425") ? buscarItensIguais(linhasTexto, line) : 0);
				if(lineQuebrada[1].equals("C425") && nrLinha > 0){
					vetorTexto.set(nrLinha, linhasTexto.get(nrLinha));
				}
				else{
					if(lineQuebrada[1].equals("C425")){
						linhasTexto.put(vetorTexto.size(), line + "\r\n");
					}
					vetorTexto.add(line + "\r\n");
				}
				
				
			}
			
			try{
				//
				String texto = "";
				for(int i = 0; i < vetorTexto.size(); i++)
					texto += vetorTexto.get(i);
				
				FileOutputStream gravar;
				File arq = new File(fileName); 
				gravar = new FileOutputStream(arq);
				gravar.write(texto.getBytes());
				gravar.flush();
				gravar.close();
				
				
			}
			catch(Exception e){
				Util.registerLog(e);
				e.printStackTrace();
			}
			
			return new Result(1);
		}
		
		catch(NumberFormatException e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			System.out.println(e.getMessage());
			return new Result(-1, "Erro configurarCodigoItem: " + e);
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			return new Result(-1, "Erro configurarCodigoItem: " + e);
		}
	}
	
	
	public static int buscarItensIguais(HashMap<Integer, String> linhasTexto, String line){
		try {
			for(int key : linhasTexto.keySet()){
				String linhaTextoQuebrada[] = linhasTexto.get(key).split("\\|");
				String lineQuebrada[] 		= line.split("\\|");
				
				if(linhaTextoQuebrada[2].equals(lineQuebrada[2])){
					float qtItemA = Float.parseFloat(linhaTextoQuebrada[3].replaceAll(",", "."));
					float qtItemB = Float.parseFloat(lineQuebrada[3].replaceAll(",", "."));
					float vlItemA = Float.parseFloat(linhaTextoQuebrada[5].replaceAll(",", "."));
					float vlItemB = Float.parseFloat(lineQuebrada[5].replaceAll(",", "."));
					float vlItemPISA = Float.parseFloat(linhaTextoQuebrada[6].replaceAll(",", "."));
					float vlItemPISB = Float.parseFloat(lineQuebrada[6].replaceAll(",", "."));
					float vlItemCOFINSA = Float.parseFloat(linhaTextoQuebrada[7].replaceAll(",", "."));
					float vlItemCOFINSB = Float.parseFloat(lineQuebrada[7].replaceAll(",", "."));
					linhaTextoQuebrada[3] = Util.formatNumber((qtItemA + qtItemB), 2);
					linhaTextoQuebrada[5] = Util.formatNumber((vlItemA + vlItemB), 2);
					linhaTextoQuebrada[6] = Util.formatNumber((vlItemPISA + vlItemPISB), 2);
					linhaTextoQuebrada[7] = Util.formatNumber((vlItemCOFINSA + vlItemCOFINSB), 2);
					
					line = "";
					for(int j = 1; j <= 7; j++){
						String parte = (linhaTextoQuebrada.length > j ? linhaTextoQuebrada[j] : "");
						line += "|" + parte; 
					}
					line += "|\r\n"; 
					linhasTexto.put(key, line);
					return key;
				}
			}
			
			return 0;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace();
			return -1;
		}
	}
	
	public static Result configurarCfopsCorretos(String fileName, int cdEmpresa, byte[] arquivo){
		Connection connect = Conexao.conectar();
		
		try{
			
			PreparedStatement pstmtCombustivel = connect.prepareStatement("SELECT C.id_reduzido, B.id_produto_servico, B.cd_produto_servico " +
																		  "FROM alm_produto_grupo A " +
																		  "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
																		  "JOIN grl_produto_servico_empresa C ON (A.cd_produto_servico = C.cd_produto_servico" +
																		  "											AND C.cd_empresa = "+cdEmpresa+") " +
																		  "WHERE A.cd_grupo IN " + GrupoServices.getAllCombustivel(cdEmpresa, connect));
			
			ResultSetMap rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
			
			connect.setAutoCommit(false);
			if(arquivo != null){
				File arqTemp = new File(fileName);
				FileOutputStream fileW = new FileOutputStream(arqTemp);
				fileW.write(arquivo);
				fileW.flush();
				fileW.close();
			}
			
			File arq = new File(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arq.getPath()), "ISO-8859-1"));   
            
            String texto = "";
            while(br.ready()){
		    	String linha = br.readLine();
                
                linha = linha.replace("|5202|", "|5102|");
                
                while(rsmCombustivel.next()){
                	String codItem = (rsmCombustivel.getString("id_reduzido") != null && !rsmCombustivel.getString("id_reduzido").equals("") ? rsmCombustivel.getString("id_reduzido") : 
                					  rsmCombustivel.getString("id_produto_servico") != null && !rsmCombustivel.getString("id_produto_servico").equals("") ? rsmCombustivel.getString("id_produto_servico") :
                				      rsmCombustivel.getString("cd_produto_servico"));
	                if(Util.contemCadeia(linha, codItem))
	                    linha = linha.replace("|5102|", "|5656|");
	                    
                }
                rsmCombustivel.beforeFirst();
                texto += linha + "\r\n";
                    
            }
            
            br.close();
            
            
            File arquivo2 = new File(fileName);
            arquivo2.createNewFile();
            FileWriter fw = new FileWriter(arquivo2);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(texto);
            bw.close();
            fw.close();
            
            return new Result(1);
	   }
		
			
       catch(Exception e){
    	   Util.registerLog(e);
    	   e.printStackTrace(); 
    	   return new Result(-1);
       }
		
		finally{
			Conexao.rollback(connect);
		}
	}
	
	public static Result configurarCfop(String fileName, int cdEmpresa, byte[] arquivo){
		Connection connect = Conexao.conectar();
		RandomAccessFile raf = null;
		try{
			connect.setAutoCommit(false);
			if(arquivo != null){
				File arqTemp = new File(fileName);
				FileOutputStream fileW = new FileOutputStream(arqTemp);
				fileW.write(arquivo);
				fileW.flush();
				fileW.close();
			}
			raf = new RandomAccessFile(fileName, "rw");
			String texto = "";
			String line = "";
			
			PreparedStatement pstmtCombustivel = connect.prepareStatement("SELECT B.cd_produto_servico AS cd_combustivel, B.nm_produto_servico AS nm_combustivel " +
																		  "FROM alm_produto_grupo A " +
																		  "JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
																		  "JOIN grl_produto_servico_empresa C ON (A.cd_produto_servico = C.cd_produto_servico" +
																		  "											AND C.cd_empresa = "+cdEmpresa+") " +
																		  "WHERE A.cd_grupo IN " + GrupoServices.getAllCombustivel(cdEmpresa, connect) + 
																		  " AND (B.cd_produto_servico = ? OR B.id_produto_servico = ? OR C.id_reduzido = ?)");

			ResultSetMap rsmCombustivel = null;
			float vlSomaCombustivel = 0;
			while(fileName != null && (line = raf.readLine()) != null)	{
				String lineQuebrada[] = line.split("\\|");
				while(lineQuebrada[1].equals("C425")){
					
					String codItem = lineQuebrada[2];
					
					if(Util.isNumber(codItem))
						pstmtCombustivel.setInt(1, Integer.parseInt(codItem));
					else
						pstmtCombustivel.setInt(1, 0);
					
					pstmtCombustivel.setString(2, codItem);
					pstmtCombustivel.setString(3, codItem);
					
					rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
					if(rsmCombustivel.next()){
						vlSomaCombustivel += Util.arredondar(Float.parseFloat(lineQuebrada[5].replace(",", ".")), 2);
						vlSomaCombustivel = (float)Util.arredondar(vlSomaCombustivel, 2);
					}
					
					texto += line + "\r\n";
					line = raf.readLine();
					lineQuebrada = line.split("\\|");
				}
				
				while(lineQuebrada[1].equals("C490")){
					boolean acrescenta = false;
					if(lineQuebrada[2].equals("030") && lineQuebrada[3].equals("5102")){
						float vlRegistro = (float)Util.arredondar(Float.parseFloat(lineQuebrada[5].replace(",", ".")), 2);
						vlRegistro -= vlSomaCombustivel;
						vlRegistro = (float)Util.arredondar(vlRegistro, 2);
						lineQuebrada[5] = String.valueOf(vlRegistro).replace(".", ",");
						acrescenta = true;
					}
					
					int tamanho = 8;
					
					if(!lineQuebrada[5].equals("0") && !lineQuebrada[5].equals("0,00")){
						line = "";
						for(int i = 1; i <= tamanho; i++){
							String parte = (lineQuebrada.length > i ? lineQuebrada[i] : "");
							line += "|" + parte; 
						}
						line += "|"; 
						texto += line + "\r\n";
					}
					if(acrescenta){
						texto += "|" + lineQuebrada[1] + "|" + lineQuebrada[2] + "|" + "5656" + "|" + lineQuebrada[4] + "|" + String.valueOf(vlSomaCombustivel).replace(".", ",") + "|" + (lineQuebrada.length >= 7 ? lineQuebrada[6] : "") + "|" + (lineQuebrada.length >= 8 ? lineQuebrada[7] : "") + "|" + (lineQuebrada.length >= 9 ? lineQuebrada[8] : "") + "|\r\n";
					}
					line = raf.readLine();
					lineQuebrada = line.split("\\|");
					
					if(acrescenta)
						vlSomaCombustivel = 0;
				}
				
				
				texto += line + "\r\n";
			}
			
			try{
				//
				FileOutputStream gravar;
				File arq = new File(fileName); 
				gravar = new FileOutputStream(arq);
				gravar.write(texto.getBytes());
				gravar.close();
				
			}
			catch(Exception e){
				Util.registerLog(e);
				e.printStackTrace();}
			
			return new Result(1);
		}
		
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace();
			return new Result(-1, "Erro configurarCfop");}
	}
	
	public static Result salvarRegistroEcf(int cdEmpresa){
		return loadFromFileSPED("E://DNA//Arquivos Impressora//Arquivo Impressora Posto Canes Mes 10 SPED ICMS-IPI.txt", cdEmpresa, null);
	}
	
	public static Result loadFromFileSPED(String fileName, int cdEmpresa){
		return loadFromFileSPED(fileName, cdEmpresa, null);
	}
	
	public static Result loadFromFileSPED(String fileName, int cdEmpresa, byte[] arquivo) {
		Connection connect = Conexao.conectar();
		RandomAccessFile raf = null;
		try {
			connect.setAutoCommit(false);
			Result resultadoArrumar = arrumarResultado(fileName, cdEmpresa, arquivo);
			if (resultadoArrumar.getCode() < 0){
				return resultadoArrumar;
			}
			raf = new RandomAccessFile(fileName, "rw");
			String arquivoStr = "";
			if(arquivo != null)
				arquivoStr = new String(arquivo);
			
			String line = "";
			ResultSetMap rsm = new ResultSetMap();
			int codReturn = 1;
			Boolean verificar = new Boolean(false);
			int cont = 0;
			String primeiros = "";
			for(int i = 0; i < arquivoStr.length() - 1; i++){
				char letra = (char)arquivoStr.charAt(i);
				if(letra == '\n'){
					if(cont < 2){
						primeiros += line;
						if(cont==1){
							Result result = saveSPED(primeiros, verificar, connect);
							verificar = (Boolean)result.getObjects().get("verificar");
							if(result.getCode() <= 0) {
								System.out.println("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line);
								Util.registerLog(new Exception("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line));
								if(result.getObjects().get("ERRO") != null){
									ResultSetMap rsmAux = (ResultSetMap) result.getObjects().get("ERRO");
									ArrayList<HashMap<String, Object>> registros = rsmAux.getLines();
									for(int j = 0; j < registros.size();j++){
										rsm.addRegister(registros.get(j));
									}
								}
								codReturn = -1;
							}
						}
					}
					else{
						Result result = saveSPED(line, verificar, connect);
						verificar = (Boolean)result.getObjects().get("verificar");
						if(result.getCode() <= 0) {
							System.out.println("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line);
							Util.registerLog(new Exception("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line));
							if(result.getObjects().get("ERRO") != null){
								ResultSetMap rsmAux = (ResultSetMap) result.getObjects().get("ERRO");
								ArrayList<HashMap<String, Object>> registros = rsmAux.getLines();
								for(int j = 0; i < registros.size();j++){
									rsm.addRegister(registros.get(j));
								}
							}
							codReturn = -1;
						}	
					}
					cont++;
					line = "";
				}
				else{
					line += arquivoStr.charAt(i);
				}
				
				
			}

			while(fileName != null && (line = raf.readLine()) != null)	{
				if(cont < 2){
					primeiros += line;
					if(cont==1){
						Result result = saveSPED(primeiros, verificar, connect);
						verificar = (Boolean)result.getObjects().get("verificar");
						if(result.getCode() <= 0) {
							System.out.println("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line);
							Util.registerLog(new Exception("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line));
							if(result.getObjects().get("ERRO") != null){
								ResultSetMap rsmAux = (ResultSetMap) result.getObjects().get("ERRO");
								ArrayList<HashMap<String, Object>> registros = rsmAux.getLines();
								for(int i = 0; i < registros.size();i++){
									rsm.addRegister(registros.get(i));
								}
							}
							codReturn = -1;
						}
					}
				}
				else{
					Result result = saveSPED(line, verificar, connect);
					verificar = (Boolean)result.getObjects().get("verificar");
					if(result.getCode() <= 0) {
						System.out.println("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line);
						Util.registerLog(new Exception("Registro não salvo! ERRO: "+result.getMessage()+", Código: "+result.getCode()+": "+line));
						if(result.getObjects().get("ERRO") != null){
							ResultSetMap rsmAux = (ResultSetMap) result.getObjects().get("ERRO");
							ArrayList<HashMap<String, Object>> registros = rsmAux.getLines();
							for(int i = 0; i < registros.size();i++){
								rsm.addRegister(registros.get(i));
							}
						}
						codReturn = -1;
					}	
				}
				cont++;
			}
			raf.close();
			Result resultado = new Result(codReturn);
			if(rsm.getLines().size() > 0){
				resultado.addObject("ERRO", rsm);
			}
			if(codReturn < 0){
				Conexao.rollback(connect);
			}
			else{
				connect.commit();
			}
			return resultado;
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar registro!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static Result saveSPED(String txtRegistroEcf, Boolean verificar) {
		return saveSPED(txtRegistroEcf, verificar, null);
	}

	public static Result saveSPED(String txtRegistroEcf, Boolean verificar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull){
				connect = Conexao.conectar();
			}
			int cdReferenciaEcf             = 0;
			String tokens[] = (txtRegistroEcf + "?").split("\\|");
			
			String tpRegistroEcf            = tokens[1];
			GregorianCalendar dtRegistroEcf = null;
			
			String dsValidacao = "";
			ResultSetMap rsm = new ResultSetMap();
			// Buscando o código do ECF - para registros do tipo 60
			if(tpRegistroEcf.equals("C400")) {
				String codMod  		= tokens[2];
				String modEqui 		= tokens[3];
				String nrSerieEcf   = tokens[4];
				String nrCaixaEcf   = tokens[5];
				String dtDoc  		= tokens[8];
				String Cro		    = tokens[9];
				String Crz			= tokens[10];
				String numCooFin    = tokens[11];
				String gtFin        = tokens[12];
				String vlBrt        = tokens[13];
				
				
				if(codMod == null || codMod.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código do modelo do documento fiscal";
				if(modEqui == null || modEqui.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Modelo do equipamento";
				if(nrSerieEcf == null || nrSerieEcf.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número de série de fabricação do ECF";
				if(nrCaixaEcf == null || nrCaixaEcf.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número do caixa atribuído ao ECF";
				if(dtDoc == null || dtDoc.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data do movimento a que se refere a Redução Z";
				if(Cro == null || Cro.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Posição do Contador de Reinício de Operação";
				if(Crz == null || Crz.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Posição do Contador de Redução Z";
				if(numCooFin == null || numCooFin.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número do Contador de Ordem de Operação do último documento emitido no dia";
				if(gtFin == null || gtFin.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor do Grande Total final";
				if(vlBrt == null || vlBrt.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor da venda bruta";
				
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do equipamento ECF e Redução Z faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
				    
			    	//entrouDs = true;
			    }
			    else{
			    	dtRegistroEcf = Util.convStringToCalendar(dtDoc.substring(0, 2)+"/"+dtDoc.substring(2, 4)+"/"+dtDoc.substring(4, 8));
			    }
				
				ResultSet rs = connect.prepareStatement("SELECT cd_referencia FROM bpm_referencia " +
													    "WHERE nr_serie = \'"+nrSerieEcf.trim()+"\'").executeQuery();
				if(rs.next()) 
					cdReferenciaEcf = rs.getInt("cd_referencia");
				else{
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "O cadastro do ECF com serial \'"+nrSerieEcf.trim()+"\' não foi localizado!");
			    	rsm.addRegister(register);
			    }
			}
			if(tpRegistroEcf.equals("C405")){
				String dtDoc  		= tokens[2];
				dtRegistroEcf = Util.convStringToCalendar(dtDoc.substring(0, 2)+"/"+dtDoc.substring(2, 4)+"/"+dtDoc.substring(4, 8));
			}
			if(tpRegistroEcf.equals("C420")){
				String codTotPart = tokens[2];
				String vlAcumTot  = tokens[3];
				
				if(codTotPart == null || codTotPart.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código do totalizador";
				if(vlAcumTot == null || vlAcumTot.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor acumulado no totalizador";
				
			    if(!dsValidacao.equals("")){
//			    	HashMap<String, Object> register = new HashMap<String, Object>();
//			    	register.put("ERRO", "Dados do Registro dos totalizadores parciais da Redução Z faltando: "+dsValidacao);
//			    	rsm.addRegister(register);
//			    
//			    	dsValidacao = "";
				    
			    	//entrouDs = true;
			    }
			    
				
			}
			if(tpRegistroEcf.equals("C425")){
				String codItem   = tokens[2];
				String qtItem    = tokens[3];
				String nrUnidade = tokens[4];
				String vlItem    = tokens[5];
				
				if(codItem == null || codItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código do item";
				if(qtItem == null || qtItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade acumulada do item";
				if(nrUnidade == null || nrUnidade.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade do item";
				if(vlItem == null || vlItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor acumulado do item";
				
			    if(!dsValidacao.equals("")){
//			    	HashMap<String, Object> register = new HashMap<String, Object>();
//			    	register.put("ERRO", "Dados do Resumo de itens do movimento diário faltando: "+dsValidacao);
//			    	rsm.addRegister(register);
//			    
//			    	dsValidacao = "";
				    
			    	//entrouDs = true;
			    }
			    
				
			}
			if(tpRegistroEcf.equals("C460")){
				String codMod   = tokens[2];
				String codSit   = tokens[3];
				String numDoc   = tokens[4];
				String dtDoc    = tokens[5];
				String vlDoc    = tokens[6];
				
				if(codMod == null || codMod.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código do modelo do documento fiscal";
				if(codSit == null || codSit.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código da situação do documento fiscal";
				if(numDoc == null || numDoc.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número do documento fiscal";
				if(dtDoc == null || dtDoc.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data da emissão do documento fiscal";
				if(vlDoc == null || vlDoc.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total do documento fiscal";
				
			    if(!dsValidacao.equals("")){
//			    	HashMap<String, Object> register = new HashMap<String, Object>();
//			    	register.put("ERRO", "Dados do Resumo de itens do movimento diário faltando: "+dsValidacao);
//			    	rsm.addRegister(register);
//			    
//			    	dsValidacao = "";
				    
			    	//entrouDs = true;
			    }
			    else
			    	dtRegistroEcf = Util.convStringToCalendar(dtDoc.substring(0, 2)+"/"+dtDoc.substring(2, 4)+"/"+dtDoc.substring(4, 8));
			}
			if(tpRegistroEcf.equals("C470")){
				String codItem   = tokens[2];
				String qtItem    = tokens[3];
				String nrUnidade = tokens[5];
				String vlItem    = tokens[6];
				String cstIcms   = tokens[7];
				String nrCfop    = tokens[8];
				
				if(codItem == null || codItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código do item";
				if(qtItem == null || qtItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade do item";
				if(nrUnidade == null || nrUnidade.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade do item";
				if(vlItem == null || vlItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total do item";
				if(cstIcms == null || cstIcms.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código da Situação Tributária";
				if(nrCfop == null || nrCfop.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código Fiscal de Operação e Prestação";
				
			    if(!dsValidacao.equals("")){
//			    	HashMap<String, Object> register = new HashMap<String, Object>();
//			    	register.put("ERRO", "Dados do Resumo de itens do movimento diário faltando: "+dsValidacao);
//			    	rsm.addRegister(register);
//			    
//			    	dsValidacao = "";
				    
			    	//entrouDs = true;
			    }
			    
			}
			if(tpRegistroEcf.equals("C490")){
				String cstIcms   = tokens[2];
				String nrCfop    = tokens[3];
				String vlOper    = tokens[4];
				String vlBcIcms  = tokens[5];
				String vlIcms    = tokens[6];
				
				if(cstIcms == null || cstIcms.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código da Situação Tributária";
				if(nrCfop == null || nrCfop.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código Fiscal de Operação e Prestação";
				if(vlOper == null || vlOper.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor da operação correspondente à combinação de CST_ICMS, CFOP, e alíquota do ICMS";
				if(vlBcIcms == null || vlBcIcms.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor acumulado da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS";
				if(vlIcms == null || vlIcms.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor acumulado do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS";
				
			    if(!dsValidacao.equals("")){
//			    	HashMap<String, Object> register = new HashMap<String, Object>();
//			    	register.put("ERRO", "Dados do Registro analítico do movimento diário faltando: "+dsValidacao);
//			    	rsm.addRegister(register);
//			    
//			    	dsValidacao = "";
				    
			    	//entrouDs = true;
			    }
			}
			if(tpRegistroEcf.equals("C495")){
				String codItem   = tokens[3];
				String qtItem    = tokens[4];
				String nrUnidade = tokens[6];
				String vlItem    = tokens[7];
				
				if(codItem == null || codItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Código do item";
				if(qtItem == null || qtItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade do item";
				if(nrUnidade == null || nrUnidade.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade do item";
				if(vlItem == null || vlItem.equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total do item";
				
			    if(!dsValidacao.equals("")){
//			    	HashMap<String, Object> register = new HashMap<String, Object>();
//			    	register.put("ERRO", "Dados do Resumo mensal de itens do ECF por estabelecimento faltando: "+dsValidacao);
//			    	rsm.addRegister(register);
//			    
//			    	dsValidacao = "";
				    
			    	//entrouDs = true;
			    }
			    
			}
			// Montando o registro
			RegistroEcf registroEcf = new RegistroEcf(0,tpRegistroEcf, dtRegistroEcf, 0 /*vlRegistroEcf*/, txtRegistroEcf, cdReferenciaEcf, 1 /*lgSped*/, "");
			System.out.println("Texto a ser comparado = "  + txtRegistroEcf);
			System.out.println("verificar = " + verificar);
			ResultSetMap rs = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
													"WHERE txt_registro_ecf   = \'"+txtRegistroEcf+"\'").executeQuery());
			System.out.println("rs = " + rs);
			if(tpRegistroEcf.equals("C400")){
				if(rs.next()){
					registroEcf.setCdRegistroEcf(rs.getInt("cd_registro_ecf"));
					verificar = true;
				}
				else{
					verificar = false;
				}
			}
			else{
				if(rs.next() && verificar){
					registroEcf.setCdRegistroEcf(rs.getInt("cd_registro_ecf"));
				}
			}
			// Salvando
			if(rsm.getLines().size()==0){
				System.out.println("registroEcf = " + registroEcf);
				if(registroEcf.getCdRegistroEcf() <= 0){
					Result resultado = new Result(RegistroEcfDAO.insert(registroEcf, connect), "Falha ao tentar inserir!");
					resultado.addObject("verificar", verificar);
					System.out.println("Retorno = " + resultado);
					return resultado;
				}
				else{
					Result resultado = new Result(RegistroEcfDAO.update(registroEcf, connect), "Falha ao tentar atualizar!");
					resultado.addObject("verificar", verificar);
					System.out.println("Retorno2 = " + resultado);
					return resultado;
				}
			}
			else{
				Result resultado = new Result(-1);
				resultado.setMessage("SPED contém Erros!");
				resultado.addObject("ERRO", rsm);
				resultado.addObject("verificar", verificar);
				return resultado;
			}
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			Result resultado = new Result(-1, "Falha ao tentar salvar registro!", e);
			resultado.addObject("verificar", verificar);
			return resultado;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void conferirValorDeItens(){
		Connection connect = Conexao.conectar();
		try {
			
			PreparedStatement pstmtEcf = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																 "WHERE CAST(dt_registro_ecf AS DATE) >= '2014-01-01' AND CAST(dt_registro_ecf AS DATE) <= '2014-01-31'" +
																 "  AND tp_registro_ecf LIKE \'C460\'");
			PreparedStatement pstmtDocItens = connect.prepareStatement("SELECT A.*, C.qt_precisao_custo FROM alm_documento_saida_item A, alm_documento_saida B, grl_produto_servico_empresa C WHERE A.cd_documento_saida = B.cd_documento_saida AND B.nr_documento_saida like ? AND C.cd_produto_servico = A.cd_produto_servico AND C.cd_empresa = 3 ORDER BY cd_item");

			PreparedStatement pstmtEcfC470  = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																		"WHERE cd_registro_ecf = ?");
			
			ResultSetMap rs = new ResultSetMap(pstmtEcf.executeQuery());
			ResultSetMap rsDocItens = new ResultSetMap();
			int quantErros = 0;
			while(rs.next()){
				String registro = rs.getString("txt_registro_ecf");
				String[] set = registro.split("\\|");
				float vlDoc = Float.parseFloat(set[6].replaceAll(",", "."));
				String nrDoc = set[4];
				pstmtDocItens.setString(1, nrDoc);
				rsDocItens = new ResultSetMap(pstmtDocItens.executeQuery());
				int cdRegistroEcf = rs.getInt("cd_registro_ecf");
				float vlDocSomado = 0;
				float vlDescSomado = 0;
				while(rsDocItens.next() && true){
					System.out.println("rsDocItens = " + rsDocItens);
					pstmtEcfC470.setInt(1, ++cdRegistroEcf);
					ResultSetMap rsC470 = new ResultSetMap(pstmtEcfC470.executeQuery());
					System.out.println("TESTE: " + rsC470);
					if(rsC470.next() && (rsC470.getString("tp_registro_ecf").equals("C470"))){
						String reg = rsC470.getString("txt_registro_ecf");
						String[] set2 = reg.split("\\|");
						System.out.println("Valor do item "+rsDocItens.getString("cd_item")+" no ECF = " + set2[6]);
						System.out.println("Quantidade do item "+rsDocItens.getString("cd_item")+" no ECF = " + set2[3]);
						System.out.println("Valor do item "+rsDocItens.getString("cd_item")+" no DOC= " + rsDocItens.getFloat("vl_unitario") + ", Quantidade do Item = " + rsDocItens.getFloat("qt_saida") + ", Descontos = " + rsDocItens.getFloat("vl_desconto") + ", Acrescimos = " + rsDocItens.getFloat("vl_acrescimo"));
						System.out.println("Valor do item "+rsDocItens.getString("cd_item")+" no DOC1= " + (float)Util.arredondar(rsDocItens.getFloat("vl_unitario") * (float)Util.arredondar(rsDocItens.getFloat("qt_saida"), 3) - rsDocItens.getFloat("vl_desconto") + rsDocItens.getFloat("vl_acrescimo"), 2));
						System.out.println("Valor do item "+rsDocItens.getString("cd_item")+" no DOC2= " + (float)Util.arredondar((float)Util.arredondar(rsDocItens.getFloat("vl_unitario"), rsDocItens.getInt("qt_precisao_custo")) * rsDocItens.getFloat("qt_saida") - rsDocItens.getFloat("vl_desconto") + rsDocItens.getFloat("vl_acrescimo"), 2));
						System.out.println("Valor do item "+rsDocItens.getString("cd_item")+" no DOC3= " + (float)Util.arredondar((float)Util.arredondar(rsDocItens.getFloat("vl_unitario"), rsDocItens.getInt("qt_precisao_custo")) * (float)Util.arredondar(rsDocItens.getFloat("qt_saida"), 3) - rsDocItens.getFloat("vl_desconto") + rsDocItens.getFloat("vl_acrescimo"), 2));
						vlDocSomado += (float)Util.arredondar(rsDocItens.getFloat("vl_unitario") * (float)Util.arredondar(rsDocItens.getFloat("qt_saida"), 3) + rsDocItens.getFloat("vl_acrescimo"), 2);
						if(Float.parseFloat(set2[6].replaceAll(",", ".")) != (float)Util.arredondar(rsDocItens.getFloat("vl_unitario") * (float)Util.arredondar(rsDocItens.getFloat("qt_saida"), 3) - rsDocItens.getFloat("vl_desconto") + rsDocItens.getFloat("vl_acrescimo"), 2)){
							System.out.println("ERRO: " + Float.parseFloat(set2[6].replaceAll(",", ".")) + " != " + (float)Util.arredondar(rsDocItens.getFloat("vl_unitario") * (float)Util.arredondar(rsDocItens.getFloat("qt_saida"), 3) - rsDocItens.getFloat("vl_desconto") + rsDocItens.getFloat("vl_acrescimo"), 2));
							quantErros++;
						}
						vlDescSomado += rsDocItens.getFloat("vl_desconto");
					}
					else
						break;
					System.out.println();
				}
				
				System.out.println("Valor do documento no ECF 	   = " + vlDoc);
				System.out.println("Valor do documento no DOC      = " + vlDocSomado);
				System.out.println("Valor do documento no DOC DESC = " + vlDescSomado);
				System.out.println();
			}
			
			System.out.println("Quantidade de Erros = " + quantErros);
			
			
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			Conexao.desconectar(connect);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDadosHomologacao() {
		Connection connect = Conexao.conectar();		
		try {			
			PreparedStatement pstmt;
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_app, B.nm_path FROM fsc_dados_homologacao A "+
											 "LEFT OUTER JOIN fsc_app_vinculado B ON (A.cd_homologacao = B.cd_homologacao) ");			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			System.err.println("Erro! PessoaServices.findDesenvolvedor: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
//	public static ResultSetMap corrigirIdProduto(){
//		
//	}
	
}
