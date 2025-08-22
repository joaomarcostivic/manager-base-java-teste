package com.tivic.manager.str.ait;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.TalonarioServices;
import com.tivic.manager.str.Ait;
import com.tivic.manager.str.AitServices;
import com.tivic.manager.str.InfracaoServices;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.Conexao;

import junit.framework.TestCase;

public class AitVerificadorDuplicidadeBaseAntigaTest extends TestCase {
	
	private final int NR_INICIAL = 1;
	private final int NR_FINAL = 100;

	Connection connect;
	Talonario talao;
	List<Ait> aits;
	
	protected void setUp() throws Exception {		
		InicializationBeans.init(new InjectApplicationBuilder());
		connect = Conexao.conectar();		
	}
	
	public void testTalaoDuplicado() throws Exception {
		prepararTalao("T1");
		gerarAits();
		Ait ait = aits.get(5);		

		System.out.println(ait.getCdAit());
		
		IAitVerificadorDuplicidade verificador = new AitDuplicidadeDatabaseFactory().verificador();
		Ait aitDuplicada = verificador.findByNrAit(ait, connect).get();
		
		assertNotNull("O objeto não deve ser nulo", aitDuplicada);
	}
	
	public void testTalaoNaoDuplicado() throws Exception {
		prepararTalao("T2");	
		gerarAits();

		Ait ait = aits.get(5);	
		
		IAitVerificadorDuplicidade verificador = new AitDuplicidadeDatabaseFactory().verificador();
		Ait aitDuplicada = verificador.findByNrAit(ait, connect).get();
		
		assertNull("O objeto deve ser nulo", aitDuplicada);
	}
	
	public void prepararTalao(String sgTalao) {
		talao = new Talonario();	
		talao.setNrInicial(NR_INICIAL);
		talao.setNrFinal(NR_FINAL);
		talao.setDtEntrega(new GregorianCalendar());
		talao.setSgTalao(sgTalao);
		talao.setStTalao(TalonarioServices.ST_TALAO_ATIVO);
		talao.setCdAgente(1);
		
		com.tivic.manager.str.Talonario talaostr = (com.tivic.manager.str.Talonario) TalonarioServices.save(talao, null, connect).getObjects().get("TALONARIO");
		talao.setCdTalao(talaostr.getCdTalao());
	}
	
	public void gerarAits() {
		aits = new ArrayList<Ait>();
		
		for(int i = 0; i <= NR_FINAL; i++) { 	
			String sgNrAit = talao.getSgTalao() + Util.fillNum(i, 8);
			
			Ait ait = new Ait();
			ait.setNrAit(i);
			ait.setIdAit(sgNrAit);
			ait.setDsLocalInfracao("");
			ait.setCdTalao(talao.getCdTalao());
			ait.setCdAgente(1);
			ait.setCdInfracao(InfracaoServices.getVigenteByNrCodDetran(74550, null).getCdInfracao());

			if(!talao.getSgTalao().equals("T2")) {
				ait.setCdAit(AitServices.insert(ait, connect));
			}
			
			aits.add(ait);
		}
	}
	
	protected void tearDown() throws SQLException {		
		try {
			TalonarioServices.remove(talao.getCdTalao(), false, null,  connect);
		} finally {
			connect.close();
		}
	}
}