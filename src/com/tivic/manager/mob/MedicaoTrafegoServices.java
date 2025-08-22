package com.tivic.manager.mob;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.postgresql.util.PGInterval;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dataprom.radar.ws.infracao.TransmitirInfracaoESTV1Request.ItemTransmitirInfracaoESTV1;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class MedicaoTrafegoServices {
	
	public static final String[] tiposVeiculo = {"Leve 1", "Leve 2", "Pesado 1", "Pesado 2", "Especial"};

	public static Result save(MedicaoTrafego medicaoTrafego){
		return save(medicaoTrafego, null, null);
	}

	public static Result save(MedicaoTrafego medicaoTrafego, AuthData authData){
		return save(medicaoTrafego, authData, null);
	}

	public static Result save(MedicaoTrafego medicaoTrafego, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(medicaoTrafego==null)
				return new Result(-1, "Erro ao salvar. MedicaoTrafego é nulo");

			int retorno;
			if(medicaoTrafego.getCdMedicao()==0){
				retorno = MedicaoTrafegoDAO.insert(medicaoTrafego, connect);
				medicaoTrafego.setCdMedicao(retorno);
			}
			else {
				retorno = MedicaoTrafegoDAO.update(medicaoTrafego, connect);
			}
			
			if(medicaoTrafego.getOcorrencias() != null) {
				for (OcorrenciaTrafego ocorrencia : medicaoTrafego.getOcorrencias()) {
					ocorrencia.setCdMedicao(medicaoTrafego.getCdMedicao());
					Result r = OcorrenciaTrafegoServices.save(ocorrencia, null, connect);
					if(r.getCode() < 0) {
						if(isConnectionNull) {
							Conexao.rollback(connect);
						}
						return r;
					}
				}
			}
			
			if(retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MEDICAOTRAFEGO", medicaoTrafego);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(ArrayList<MedicaoTrafego> medicoes) {
		return save(medicoes, null);
	}
	
	public static Result save(ArrayList<MedicaoTrafego> medicoes, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			Result result = null;
			
			if(medicoes==null || medicoes.size()==0) {
				return new Result(-1, "Não há medições.");
			}
			
			for (MedicaoTrafego medicaoTrafego : medicoes) {
				result = save(medicaoTrafego, null, connection);
				
				if(result.getCode() < 0) {
					if(isConnectionNull)
						Conexao.rollback(connection);
					return result;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1, "Salvo com sucesso...");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result remove(MedicaoTrafego medicaoTrafego) {
		return remove(medicaoTrafego.getCdMedicao(), medicaoTrafego.getCdEquipamento(), medicaoTrafego.getCdVia(), medicaoTrafego.getCdFaixa());
	}
	public static Result remove(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa){
		return remove(cdMedicao, cdEquipamento, cdVia, cdFaixa, false, null, null);
	}
	public static Result remove(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, boolean cascade){
		return remove(cdMedicao, cdEquipamento, cdVia, cdFaixa, cascade, null, null);
	}
	public static Result remove(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, boolean cascade, AuthData authData){
		return remove(cdMedicao, cdEquipamento, cdVia, cdFaixa, cascade, authData, null);
	}
	public static Result remove(int cdMedicao, int cdEquipamento, int cdVia, int cdFaixa, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = MedicaoTrafegoDAO.delete(cdMedicao, cdEquipamento, cdVia, cdFaixa, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_medicao_trafego");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MedicaoTrafegoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mob_medicao_trafego", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	

	/**
	 * Importa informações de tráfego
	 * @param connection
	 * @return
	 * 
	 * @category AZTECH
	 * 
	 * @author mauricio cordeiro
	 * @since 20191106
	 * @updated 20200124
	 */
	public static Result processarArquivos(File[] files, int cdEquipamento, Connection connection) {
		boolean isConnectionNull = connection == null;
		
		try {		
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			Result r = new Result(-1);
			
			SimpleDateFormat sdfPeriodo = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
			SimpleDateFormat sdfMedicao = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			PreparedStatement ps = null;
			ResultSet rs = null;			
			
			Equipamento equipamento = EquipamentoDAO.get(cdEquipamento, connection);
			
			for (int i = 0; i < files.length; i++) { // para cada arquivo de tráfego
				
				File file = files[i];
				ArrayList<MedicaoTrafego> trafego = new ArrayList<MedicaoTrafego>();

				// PERÍODO
				String[] dsPeriodo = file.getName().replaceAll("_trafego.xml", "").split("_to_");				
				
				GregorianCalendar dtInicial = new GregorianCalendar();
				dtInicial.setTimeInMillis(sdfPeriodo.parse(dsPeriodo[0]).getTime());
				GregorianCalendar dtFinal = new GregorianCalendar();
				dtFinal.setTimeInMillis(sdfPeriodo.parse(dsPeriodo[1]).getTime());
				
				int cdVia = 0;
				
				System.out.println("\t\tTráfego entre "+Util.formatDate(dtInicial, "dd/MM/yyyy HH:mm:ss")+" e "+Util.formatDate(dtFinal, "dd/MM/yyyy HH:mm:ss"));
								
    			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.parse(file);
				
				NodeList trafegoNodeList = document.getElementsByTagName("Trafego");	
				
				if(trafegoNodeList==null || trafegoNodeList.getLength()<=0 || trafegoNodeList.item(0)==null)
					continue;
								
				for (int j = 0; j < trafegoNodeList.getLength(); j++) {
	    			Node currentNode = trafegoNodeList.item(j);
	    			
	    			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	    				
	    				NodeList nodeList = currentNode.getChildNodes();
		    			
		    			MedicaoTrafego medicao = new MedicaoTrafego();
	        			medicao.setOcorrencias(new ArrayList<OcorrenciaTrafego>());
	        			medicao.setCdEquipamento(cdEquipamento);
	        			medicao.setDtInicial(dtInicial);
	        			medicao.setDtFinal(dtFinal);
	        			medicao.setQtVeiculos(1);
	    				
	    	        	for (int k = 0; k < nodeList.getLength(); k++) {
	    	        		Node node = nodeList.item(k);
	    	        		if (node.getNodeType() == Node.ELEMENT_NODE) {
	    	        			
	    	        			GregorianCalendar dtMedicao = dtInicial;
	    	        			int cdFaixa = 0;
	    	        			Double vlVelocidadeMedida = 0.0d;
	    	        			Double vlVelocidadeLimite = 0.0d;
	    	        			Double vlVelocidadeConsiderada = 0.0d;
	    	        			Double vlComprimentoVeiculo = 0.0d;    	        			
	    	        			
	    	        			switch(node.getNodeName()) {
    	    	        		case "datahora":    	    	        			
    	    	        			dtMedicao.setTimeInMillis(sdfMedicao.parse(node.getTextContent()).getTime());
    	    	        			
    	    	        			break;
    	    	        		case "local_inst":
    	    	        			// VIA
    	    	        			String nmVia = node.getTextContent(); //.split(",")[0].replaceAll("[0-9]", "").replaceAll(",", "").trim();
    	    	        			//nmVia = nmVia.replace("Z", "_");
    	    	        			    	    	        			
    	    	        			ps = connection.prepareStatement("SELECT * FROM mob_via WHERE nm_via iLIKE '%"+nmVia+"%' AND cd_orgao = " + equipamento.getCdOrgao());
    	    	        			rs = ps.executeQuery();
    	    	        			if(rs.next()) {
    	    	        				cdVia = rs.getInt("cd_via");
    	    	        				medicao.setCdVia(cdVia);
    	    	        			}
    	    	        			
    	    	        			break;
    	    	        		case "faixa":
    	    	        			int nrFaixa = Integer.parseInt(node.getTextContent());
    	    	        			
    	    	        			if(medicao.getCdVia()>0) {
    	    	        				ps = connection.prepareStatement("SELECT * FROM mob_faixa WHERE cd_via = "+medicao.getCdVia()+" AND nr_faixa = "+nrFaixa);
    	    	        				rs = ps.executeQuery();
    	    	        				if(rs.next()) {
    	    	        					cdFaixa = rs.getInt("cd_faixa");
	    	        						medicao.setCdFaixa(cdFaixa);
    	    	        				}
    	    	        			}
    	    	        			
    	    	        			break;
    	    	        		case "velocidade_regulamentada": //limite
    	    	        			vlVelocidadeLimite = Double.parseDouble(node.getTextContent());
    	    	        			medicao.setVlVelocidadeLimite(vlVelocidadeLimite);
    	    	        			//tolerada
    	    	        			medicao.setVlVelocidadeTolerada((vlVelocidadeLimite + 7));
    	    	        			break;
    	    	        		case "velocidade_detectada": // medida
    	    	        			vlVelocidadeMedida = Double.parseDouble(node.getTextContent());
    	    	        			medicao.setVlVelocidadeMedida(vlVelocidadeMedida);
    	    	        			break;
    	    	        		case "velocidade_considerada": //considerada
    	    	        			vlVelocidadeConsiderada = Double.parseDouble(node.getTextContent());
    	    	        			medicao.setVlVelocidadeConsiderada(vlVelocidadeConsiderada);
    	    	        			break;
    	    	        		case "comprimento":
    	    	        			vlComprimentoVeiculo = Double.parseDouble(node.getTextContent());
    	    	        			medicao.setVlComprimentoVeiculo(vlComprimentoVeiculo);
    	    	        			
    	    	        			/*
    	    	        			 * 0 	- 2.99 	- Leve 1 (Ex: Moto)
    	    	        			 * 3 	- 5.99 	- Leve 2
    	    	        			 * 6 	- 14.99 - Pesado 1
    	    	        			 * 15 	- 18.99 - Pesado 2
    	    	        			 * > 19 		- Especial
    	    	        			 */
    	    	        			if(vlComprimentoVeiculo < 3)
    	    	        				medicao.setTpVeiculo(0);
    	    	        			else if(vlComprimentoVeiculo >= 3 && vlComprimentoVeiculo < 6)
    	    	        				medicao.setTpVeiculo(1);
    	    	        			else if(vlComprimentoVeiculo >= 6 && vlComprimentoVeiculo < 15)
    	    	        				medicao.setTpVeiculo(2);
    	    	        			else if(vlComprimentoVeiculo >= 15 && vlComprimentoVeiculo < 19)
    	    	        				medicao.setTpVeiculo(3);
    	    	        			else if(vlComprimentoVeiculo >= 19)
    	    	        				medicao.setTpVeiculo(4);
    	    	        			
    	    	        			break;
    	    	        		case "ocr_foto":
    	    	        			String[] placas = node.getTextContent().split("\\|");
    	    	        			OcorrenciaTrafego ocorrencia = null;
    	    	        			
    	    	        			if(placas!=null && placas.length > 1) { 
    	    	        				for (int l = 0; l < placas.length; l++) {
    	    	        					String placa = placas[l].trim().replaceAll(" ", "");
        	    	        				
    	    	        					if(placa!=null && !placa.equals("")) {

            	    	        				ocorrencia = new OcorrenciaTrafego();
    	    	        						
    	    	        						ocorrencia.setCdEquipamento(medicao.getCdEquipamento());
    	    	        						ocorrencia.setNrPlaca(placa);
    	        	    	        			ocorrencia.setCdVia(medicao.getCdVia());
    	        	    	        			ocorrencia.setCdFaixa(medicao.getCdFaixa());
    	        	    	        			ocorrencia.setVlComprimentoVeiculo(medicao.getVlComprimentoVeiculo());
    	        	    	        			ocorrencia.setVlVelocidadeMedida(medicao.getVlVelocidadeMedida());
    	        	    	        			ocorrencia.setTpOcorrencia(OcorrenciaTrafegoServices.TP_DETECCAO_PLACA); 
	        	    	        				ocorrencia.setDtOcorrencia(dtMedicao); 
    	        	    	        			
    	        	    	        			medicao.getOcorrencias().add(ocorrencia);
    	        	    	        			
    	        	    	        			break;
    	    	        					}
    	    	        				}
    	    	        			}
    	    	        			//
    	    	        			break;
	    	        			}
	    	        		}
	    	        	}

		    			trafego.add(medicao);
	    			}	    				
				}
				
				// trafego = agruparMedicoes(trafego, cdVia, connection);
				
				r = save(trafego, connection);
				
				if(r.getCode() < 0) {
					if(isConnectionNull)
						Conexao.rollback(connection);
					return r;
				}
			}		
			
			return new Result(1, "Tráfego registrado com sucesso.");
		} catch(Exception e) {
			e.printStackTrace(System.out);
			if(isConnectionNull) {
				Conexao.rollback(connection);
			}
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result processarLista(List<ItemTransmitirInfracaoESTV1> list, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			
			int cdVia = 0;
			
//			System.out.println("================================================================================");
//			System.out.println("PERSISTINDO TrafegoMonitor\t"+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy hh:mm:ss"));
//			System.out.println(list.size() + " itens...");
//			System.out.println("================================================================================");
									
			ArrayList<MedicaoTrafego> parsedList = new ArrayList<MedicaoTrafego>();
			
			for (ItemTransmitirInfracaoESTV1 item : list) {
				Equipamento equipamento = EquipamentoServices.getByIdEquipamento(item.getEquipamento());
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.nm_via", equipamento.getDsLocal(), ItemComparator.LIKE, Types.VARCHAR));
				ResultSetMap rsm = ViaServices.find(criterios);
								
				if(rsm.next()) {
					cdVia = rsm.getInt("CD_VIA");
				}
				
				parsedList.add(new com.tivic.manager.mob.MedicaoTrafego(0, 
						equipamento.getCdEquipamento(), //cdEquipamento, 
						cdVia, //cdVia, 
						item.getPista(), //cdFaixa, 
						Util.convDateToCalendar(item.getDataConclusao()), //dtInicial, 
						Util.convDateToCalendar(item.getDataConclusao()), //dtFinal, 
						item.getTipoVeiculo(), //tpVeiculo, 
						1, //qtVeiculos, 
						new Double(item.getVelocidadeConsiderada()), //vlVelocidadeConsiderada, 
						new Double(item.getVelocidadeLimite()), //vlVelocidadeLimite, 
						new Double(item.getVelocidadeMedida()), //vlVelocidadeMedida, 
						new Double(item.getVelocidadeTolerada()), //vlVelocidadeTolerada, 
						new Double(item.getComprimentoVeiculo()) //vlComprimentoVeiculo
					));
			}
			
			Result r = MedicaoTrafegoServices.save(parsedList);
						
			return r;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			if(isConnectionNull) {
				Conexao.rollback(connection);
			}
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	
	/**
	 * Agrupa medições de tráfego por faixa da via e por tipo de veículo
	 * @param src lista de medições unitárias (1 veículo)
	 * @param connection
	 * @return
	 */
	public static ArrayList<MedicaoTrafego> agruparMedicoes(ArrayList<MedicaoTrafego> src, int cdVia, Connection connection) {
		try {
			
			//cdVia = cdVia==0 ? src.get(0).getCdVia() : cdVia;
			int qtFaixa = 0;
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM mob_faixa WHERE cd_via="+cdVia);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				qtFaixa++;
			}
			
			/*
			 * Grupo de medições por faixa e por tipo de veículo
			 * 
			 * faixa 1 [tp 0][tp 1][tp 2][tp 3][tp 4]
			 * faixa 2 [tp 0][tp 1][tp 2][tp 3][tp 4]
			 * faixa n [tp 0][tp 1][tp 2][tp 3][tp 4]
			 */
			MedicaoTrafego [][] grupo = new MedicaoTrafego[qtFaixa][5];
			
			for (MedicaoTrafego m : src) {
				Faixa faixa = FaixaDAO.get(m.getCdVia(), m.getCdFaixa(), connection);
				if(grupo[faixa.getNrFaixa()-1][m.getTpVeiculo()] == null) {
					grupo[faixa.getNrFaixa()-1][m.getTpVeiculo()] = m;
				} else {
					grupo[faixa.getNrFaixa()-1][m.getTpVeiculo()]
							.setQtVeiculos(grupo[faixa.getNrFaixa()-1][m.getTpVeiculo()]
								.getQtVeiculos() + 1);
					
					if(m.getOcorrencias().size() > 0 && m.getOcorrencias().get(0) != null)
						grupo[faixa.getNrFaixa()-1][m.getTpVeiculo()].getOcorrencias().add(m.getOcorrencias().get(0));
				}
			}
				
			/*
			 * Lista de grupos não nulos
			 */
			ArrayList<MedicaoTrafego> trafego = new ArrayList<MedicaoTrafego>();
			for(int i = 0; i < qtFaixa ; i++) {
				for(int j = 0; j < 5 ; j++) {					
					if(grupo[i][j] != null) {
						trafego.add(grupo[i][j]);
					}
				}
			}
			
			return trafego;			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static void main(String[] args) {
		try {
						
//			File[] files = new File[1];
//			files[0] = new File("D:\\20191106_16_45_00_to_20191106_16_50_00_trafego.xml");
//			processarArquivos(files, 422, null);
			
			System.out.println(getTrafegoHorario(235930, new GregorianCalendar(2020, Calendar.JANUARY, 1, 0, 0, 0), new GregorianCalendar(2020, Calendar.JANUARY, 31, 23, 59, 59)));
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	// ========================================================================
	// ========================================================================
	// ==                                                                    ==
	// ==                             STATS                                  ==
	// ==                                                                    ==
	// ========================================================================
	// ========================================================================
	
	/**
	 * Tráfego horário no período
	 * @param dtInicial início do período de análise
	 * @param dtFinal fim do período de análise
	 * @return
	 */
	public static ResultSetMap getTrafegoHorario(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getTrafegoHorario(cdOrgao, dtInicial, dtFinal, null);
	}
	public static ResultSetMap getTrafegoHorario(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement ps = connection
					.prepareStatement(
							" SELECT SUM(qt_veiculos) AS qtd, date_trunc('hour', dt_inicial::time) AS hora"
						  + " FROM mob_medicao_trafego"
						  + " WHERE cd_medicao IN ("
						  + " 	SELECT A.cd_medicao"
						  + "	FROM mob_medicao_trafego A"
						  + "   JOIN mob_via B ON (A.cd_via = B.cd_via)"
						  + "	WHERE A.dt_inicial BETWEEN ? AND ?"
						  + "   AND B.cd_orgao = ?"
						  + " )"
						  + " GROUP BY date_trunc('hour', dt_inicial::time)"
						  + " ORDER BY hora");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, cdOrgao);

			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			while(rsm.next()) {
				PGInterval interval = (PGInterval) rsm.getObject("hora");
				rsm.setValueToField("label", interval.getHours()+"h");
			}
			rsm.beforeFirst();
			
			return rsm;			
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoServices.getTrafegoPeriodo\n");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}

	}
	
	
	/**
	 * Tráfego horário no período
	 * @param dtInicial início do período de análise
	 * @param dtFinal fim do período de análise
	 * @return
	 */
	public static ResultSetMap getTrafegoHorarioTipoVeiculo(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getTrafegoHorarioTipoVeiculo(cdOrgao, dtInicial, dtFinal, null);
	}
	public static ResultSetMap getTrafegoHorarioTipoVeiculo(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			
			for(int i = 0; i < tiposVeiculo.length; i++) {
				
				PreparedStatement ps = connection
						.prepareStatement(
								" SELECT SUM(qt_veiculos) AS qtd, date_trunc('hour', dt_inicial::time) AS hora"
							  + " FROM mob_medicao_trafego"
							  + " WHERE cd_medicao IN ("
							  + " 	SELECT A.cd_medicao"
							  + "	FROM mob_medicao_trafego A"
							  + "   JOIN mob_via B ON (A.cd_via = B.cd_via)"
							  + "	WHERE A.dt_inicial BETWEEN ? AND ?"
							  + "	AND A.tp_veiculo = ?"
							  + "   AND B.cd_orgao = ?"
							  + " )"
							  + " GROUP BY date_trunc('hour', dt_inicial::time)"
							  + " ORDER BY hora");
				
				ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
				ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
				ps.setInt(3, i);
				ps.setInt(4, cdOrgao);
				
				ResultSetMap rsmAux = new ResultSetMap(ps.executeQuery());
				while(rsmAux.next()) {
					PGInterval interval = (PGInterval) rsmAux.getObject("hora");
					rsmAux.setValueToField("label", interval.getHours()+"h");
				}
				rsmAux.beforeFirst();
								
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("TP_VEICULO", i);
				register.put("NM_TP_VEICULO", tiposVeiculo[i]);
				register.put("ARRAY_DATA", rsmAux.getLines());
				
				rsm.addRegister(register);
				
			}
						
			return rsm;			
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoServices.getTrafegoPeriodo\n");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static ResultSetMap getTrafegoVeiculo(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal) {
		return getTrafegoVeiculo(cdOrgao, dtInicial, dtFinal, null);
	}
	public static ResultSetMap getTrafegoVeiculo(int cdOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {		
			
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap();
			
			PreparedStatement ps = connection
					.prepareStatement(
							" SELECT SUM(A.qt_veiculos) AS qtd, A.tp_veiculo"
						  + " FROM mob_medicao_trafego A"
						  + " JOIN mob_via B ON (A.cd_via = B.cd_via)"
						  + " WHERE A.dt_inicial BETWEEN ? AND ?"
						  + " AND B.cd_orgao = ?"
						  + " GROUP BY A.tp_veiculo"
						  + " ORDER BY qtd DESC");
			
			ps.setTimestamp(1, Util.convCalendarToTimestamp(dtInicial));
			ps.setTimestamp(2, Util.convCalendarToTimestamp(dtFinal));
			ps.setInt(3, cdOrgao);
			
			rsm = new ResultSetMap(ps.executeQuery());
			while(rsm.next()) {
				rsm.setValueToField("nm_tp_veiculo", tiposVeiculo[rsm.getInt("tp_veiculo")]);
			}
			rsm.beforeFirst();
						
			return rsm;			
		} catch(Exception e) {
			System.out.println("Erro! MedicaoTrafegoServices.getTrafegoVeiculo\n");
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	

}
