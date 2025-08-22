package com.tivic.manager.fta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conexao.detran.mg.ConexaoProdemge;

import sol.util.Result;

public class MarcaModeloSyncMg implements MarcaModeloSync {
	
	public static final int CD_MARCA_MODELO			= 0;
	public static final int DS_MARCA_MODELO			= 1;
	private boolean syncStr = false;
	
	public MarcaModeloSyncMg() {}
	public MarcaModeloSyncMg(boolean syncStr) {
		this.syncStr = syncStr;
	}

	public Result sync() {		
		Connection connect = Conexao.conectar();
		
		try {
			connect.setAutoCommit(false);
			ConexaoProdemge prodemge = new ConexaoProdemge();
			HttpURLConnection conexao = prodemge.conectar("baixar-marcas-modelos-veiculos");
			List<String> modelos = new ArrayList<>();
			List<MarcaModelo> objModelos = new ArrayList<>();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
			String lineMarcaModelo = in.readLine();
			
			int updates = 0;
			int inserts = 0;
			while(lineMarcaModelo != null) {
				lineMarcaModelo = in.readLine();
				
				if(lineMarcaModelo == null)
					break;
				
				modelos.add(lineMarcaModelo);				
			}
			
			for(String modelo: modelos) {
				String[] dadosMarcaModelo = modelo.split(";");
				
				MarcaModelo marcaModelo = mountMarcaModelo(dadosMarcaModelo, connect);
				objModelos.add(marcaModelo);
				
				boolean exists = MarcaModeloDAO.get(marcaModelo.getCdMarca()) != null;
				try {
					if(exists) {
						updates++;
						MarcaModeloDAO.update(marcaModelo, connect);
					} else {
						inserts++;
						MarcaModeloDAO.directInsert(marcaModelo, connect);
						System.out.println(marcaModelo);
					}
				} catch(Exception ex) {
					connect.rollback();
					ex.printStackTrace(System.out);
					return new Result(-1, "Erro no processo de atualização de registros");
				}
				
			}
			if(this.syncStr)
				syncStrFta(objModelos, connect);
			
			System.out.println("Marcas Inseridas: " + inserts + " / Marcas Atualizadas: " + updates);
			connect.commit();
			return new Result(1, (inserts + updates) + " registros atualizados");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1, "Erro no processo de atualização de registros");
		}
	}
	
	public void syncStrFta(List<MarcaModelo> modelos, Connection connect) {
		try {						
			int updates = 0;
			int inserts = 0;
			
			for(MarcaModelo modelo: modelos) {
				
				com.tivic.manager.str.MarcaModelo marcaModelo = MarcaModeloServices.toStrMarcaModelo(modelo);
				
				boolean exists = com.tivic.manager.str.MarcaModeloDAO.get(marcaModelo.getCdMarca()) != null;
				try {
					if(exists) {
						updates++;
						com.tivic.manager.str.MarcaModeloDAO.update(marcaModelo, connect);
					} else {
						inserts++;
						com.tivic.manager.str.MarcaModeloDAO.directInsert(marcaModelo, connect);
						System.out.println(marcaModelo);
					}
				} catch(Exception ex) {
					connect.rollback();
					ex.printStackTrace(System.out);
				}
			}
			
			System.out.println("(STR) Marcas Inseridas: " + inserts + " / Marcas Atualizadas: " + updates);
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	private MarcaModelo mountMarcaModelo(String[] dadosModelo, Connection connect) {
		MarcaModelo marcaModelo = new MarcaModelo();
		
		if(dadosModelo[CD_MARCA_MODELO].contains("X"))
			marcaModelo.setCdMarca(Integer.parseInt(dadosModelo[CD_MARCA_MODELO].substring(1)) * -1);
		else
			marcaModelo.setCdMarca(Integer.parseInt(dadosModelo[CD_MARCA_MODELO]));
		
		marcaModelo.setNmMarca(dadosModelo[DS_MARCA_MODELO].split("[/]")[0]);
		marcaModelo.setNmModelo(dadosModelo[DS_MARCA_MODELO]);
		marcaModelo.setDtAtualizacao(new GregorianCalendar());
		marcaModelo.setNrMarca(dadosModelo[CD_MARCA_MODELO]);
		
		return marcaModelo;
	}
}
