package com.tivic.manager.importacao;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.tivic.manager.bdv.Veiculo;
import com.tivic.manager.bdv.VeiculoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class WebImport {
	
	public static Result bdvImport() {
		Connection connection = null;
		try {
//			connection = Conexao.conectar("org.postgresql.Driver", "jdbc\\:postgresql\\://192.168.1.20/etransitovca", "postgres", "433uqm!");
			connection = Conexao.conectar();
			
			if(connection == null || !connection.isValid(1))
				return new Result(-1, "Não foi possível estabelecer conexão com a base de origem.");
			
			ResultSetMap rs = new ResultSetMap(connection
					.prepareStatement("SELECT distinct(A.nr_placa), A.cod_especie, A.cod_marca, A.cod_cor, A.cod_tipo," + 
							"       A.cod_categoria, A.cod_municipio, A.dt_infracao, " + 
							"       A.uf_veiculo, A.cd_renavan, A.ds_ano_fabricacao, A.ds_ano_modelo, A.nm_proprietario," + 
							"       A.tp_pessoa_proprietario, A.nr_cpf_cnpj_proprietario," + 
							"       A.cd_veiculo, A.cod_marca_autuacao, A.nm_proprietario_autuacao," + 
							"       B.*, C.*, D.*, E.*, F.*, G.* " + 
							" FROM ait A" + 
							" join marca_modelo B on (a.cod_marca = b.cod_marca)" + 
							" join cor c on (a.cod_cor = c.cod_cor)" + 
							" join tipo_veiculo d on (a.cod_tipo = d.cod_tipo)" + 
							" join categoria_veiculo e on (a.cod_categoria = e.cod_categoria)" + 
							" join municipio f on (a.cod_municipio = f.cod_municipio)" + 
							" join especie_veiculo g on (a.cod_especie = g.cod_especie) "+
							" WHERE nr_controle IS NOT NULL " +
							" ORDER BY dt_infracao")
					.executeQuery());
			
			int total = rs.getLines().size();
			
			while(rs.next()) {
				if(rs.getPointer() < 19423) {
					System.out.print(".");
					continue;
				}
				
				System.out.println("\n"+Util.fillNum(rs.getPointer(), 6)+"/"+Util.fillNum(total, 6)+" ========================");
				System.out.println("\tplaca: "+rs.getString("nr_placa"));
				
				// TODO: POST veiculo
				URL url = new URL("https://radar.tivic.com.br/etransito/rws/bdv/veiculo");
				URLConnection con = url.openConnection();
				HttpURLConnection http = (HttpURLConnection)con;
				http.setRequestMethod("POST"); 
				http.setDoInput(true);
				http.setDoOutput(true);
				
				// TODO: JWT auth
				
				http.addRequestProperty("Content-Type", "application/json");
				
				JSONObject json = new JSONObject();
				json.put("cdVeiculo", 0);
				json.put("nrPlaca", rs.getString("nr_placa"));
				json.put("nrRenavan", Integer.toString(rs.getInt("cd_renavan")));
				json.put("nrAnoModelo", Integer.parseInt(rs.getString("ds_ano_modelo").trim()));
				json.put("nrAnoFabricacao", Integer.parseInt(rs.getString("ds_ano_fabricacao").trim()));
				json.put("nrCodigoMunicipio", Integer.parseInt(rs.getString("cod_municipio")));
				json.put("nmMunicipio", rs.getString("nm_municipio"));
				json.put("sgEstado", rs.getString("nm_uf"));
				json.put("nrCodigoMarca", rs.getInt("cod_marca"));
				json.put("nmMarcaModelo", rs.getString("nm_modelo")!=null ? rs.getString("nm_modelo") : rs.getString("nm_marca"));
				json.put("nrCodigoCor", rs.getInt("cod_cor"));
				json.put("nmCor", rs.getString("nm_cor"));
				json.put("nrCodigoEspecie", rs.getInt("cod_especie"));
				json.put("nmEspecie", rs.getString("ds_especie"));
				json.put("nmTipo", rs.getString("nm_tipo"));
				json.put("nmCategoria", rs.getString("nm_categoria"));
				json.put("nrChassi", Util.formatDate(rs.getGregorianCalendar("dt_infracao"), "dd/MM/yyyy HH:mm:ss"));
				
				OutputStream out = http.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				
				writer.write(json.toString());
				writer.flush();
				writer.close();
				
				out.close();
				
		        if (http.getResponseCode() == HttpURLConnection.HTTP_OK) { //success
		        	System.out.println("\tVeículo "+http.getResponseMessage());

		        } else {
		        	System.out.println("\tERRO: "+http.getResponseMessage());
		        }
			}
			
			return new Result(1);			
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		} finally {
			Conexao.desconectar(connection);
		}
	}
	

	public static void main(String[] args) {
		bdvImport();
	}
	
	public static Result bdvMigrar() {
		Connection sourceConn = null;
		Connection targetConn = null;
		try {
			sourceConn = Conexao.conectar("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/valenca", "postgres", "433UQM!");
			targetConn = Conexao.conectar();
			targetConn.setAutoCommit(false);
			
			Result result = null;
			
			ResultSetMap rs = new ResultSetMap(sourceConn
					.prepareStatement("SELECT distinct(A.nr_placa), A.cod_especie, A.cod_marca, A.cod_cor, A.cod_tipo," + 
							"       A.cod_categoria, A.cod_municipio, A.dt_infracao, " + 
							"       A.uf_veiculo, A.cd_renavan, A.ds_ano_fabricacao, A.ds_ano_modelo, A.nm_proprietario," + 
							"       A.tp_pessoa_proprietario, A.nr_cpf_cnpj_proprietario," + 
							"       A.cd_veiculo, A.cod_marca_autuacao, A.nm_proprietario_autuacao," + 
							"       B.*, C.*, D.*, E.*, F.*, G.* " + 
							" FROM ait A" + 
							" join marca_modelo B on (a.cod_marca = b.cod_marca)" + 
							" join cor c on (a.cod_cor = c.cod_cor)" + 
							" join tipo_veiculo d on (a.cod_tipo = d.cod_tipo)" + 
							" join categoria_veiculo e on (a.cod_categoria = e.cod_categoria)" + 
							" join municipio f on (a.cod_municipio = f.cod_municipio)" + 
							" join especie_veiculo g on (a.cod_especie = g.cod_especie) "+
							" WHERE nr_controle IS NOT NULL " +
							" ORDER BY dt_infracao")
					.executeQuery());
			

			long startTime = System.currentTimeMillis();
			long total = rs.getLines().size();
			long current = 0;
			
			while(rs.next()) {
				
				printProgress(startTime, total, current++);
				
				Veiculo veiculo = new Veiculo(0, 
						rs.getString("nr_placa"), //nrPlaca, 
						rs.getString("nr_renavan", null), //nrRenavan, 
						0, //tpVeiculo, 
						0, //tpCarroceria, 
						Integer.parseInt(rs.getString("ds_ano_modelo", "0").replaceAll("[^\\d.]", "")), //nrAnoModelo, 
						Integer.parseInt(rs.getString("ds_ano_fabricacao", "0").replaceAll("[^\\d.]", "")), //nrAnoFabricacao, 
						rs.getInt("cod_municipio"), //nrCodigoMunicipio, 
						rs.getString("nm_municipio"), //nmMunicipio, 
						rs.getString("nm_uf"), //sgEstado, 
						rs.getInt("cod_marca"), //nrCodigoMarca, 
						rs.getString("nm_modelo")!=null ? rs.getString("nm_modelo") : rs.getString("nm_marca"), //nmMarcaModelo, 
						rs.getInt("cod_cor"), //nrCodigoCor, 
						rs.getString("nm_cor"), //nmCor, 
						rs.getInt("cod_especie"), //nrCodigoEspecie, 
						rs.getString("nm_especie"), //nmEspecie, 
						rs.getString("nm_tipo"), //nmTipo, 
						rs.getString("nm_categoria"), //nmCategoria, 
						rs.getGregorianCalendar("dt_infracao"), //dtInformacao, 
						null //nrChassi
					);
				
				result = VeiculoServices.save(veiculo, null, targetConn);
				if(result.getCode() < 0) {
					Conexao.rollback(targetConn);
					return result;
				}
			}
			
			targetConn.commit();
			
			return new Result(1, "Sucesso!");			
		} catch (Exception e) {
			e.printStackTrace(System.err);
			Conexao.rollback(targetConn);
			return null;
		} finally {
			Conexao.desconectar(sourceConn);
			Conexao.desconectar(targetConn);
		}
	}
	
	
	

	private static void printProgress(long startTime, long total, long current) {
	    long eta = current == 0 ? 0 : 
	        (total - current) * (System.currentTimeMillis() - startTime) / current;

	    String etaHms = current == 0 ? "N/A" : 
	            String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
	                    TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
	                    TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

	    StringBuilder string = new StringBuilder(140);   
	    int percent = (int) (current * 100 / total);
	    string
	        .append('\r')
	        .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
	        .append(String.format(" %d%% [", percent))
	        .append(String.join("", Collections.nCopies(percent, "=")))
	        .append('>')
	        .append(String.join("", Collections.nCopies(100 - percent, " ")))
	        .append(']')
	        //.append(String.join("", Collections.nCopies((int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
	        .append(String.format(" %d/%d, ETA: %s", current, total, etaHms));

	  
	    System.out.print(string);
	}

}
