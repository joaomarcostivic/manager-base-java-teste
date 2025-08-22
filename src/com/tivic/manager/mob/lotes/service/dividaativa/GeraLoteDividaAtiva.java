package com.tivic.manager.mob.lotes.service.dividaativa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.mob.lotes.builders.LoteBuilder;
import com.tivic.manager.mob.lotes.builders.dividaativa.LoteDividaAtivaAitBuilder;
import com.tivic.manager.mob.lotes.builders.dividaativa.LoteDividaAtivaBuilder;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.enums.dividaativa.LoteStatusDividaAtivaEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtiva;
import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtivaAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaAitRepository;
import com.tivic.manager.mob.lotes.repository.dividaativa.LoteDividaAtivaRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraLoteDividaAtiva implements IGeraLoteDividaAtiva {
	
	private LoteRepository loteRepository;
	private LoteDividaAtivaRepository loteDividaAtivaRepository;
	private LoteDividaAtivaAitRepository loteDividaAtivaAitRepository;
	private IArquivoRepository arquivoRepository;
	private IGeraArquivoDividaAtiva geraArquivoDividaAtiva;
	private LocalDateTime now;
	
	public GeraLoteDividaAtiva() throws Exception {
		loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		loteDividaAtivaRepository = (LoteDividaAtivaRepository) BeansFactory.get(LoteDividaAtivaRepository.class);
		loteDividaAtivaAitRepository = (LoteDividaAtivaAitRepository) BeansFactory.get(LoteDividaAtivaAitRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		geraArquivoDividaAtiva = (IGeraArquivoDividaAtiva) BeansFactory.get(IGeraArquivoDividaAtiva.class);
	}
	
	@Override
	public Lote gerarLote(int cdUsuario, CustomConnection customConnection) throws Exception {
		Lote lote = setLote(cdUsuario);
		loteRepository.insert(lote, customConnection);
		return lote;
	}	
	
	private Lote setLote(int cdUsuario) {
		now = LocalDateTime.now();
		return new LoteBuilder()
				.setDtCriacao(new GregorianCalendar())
				.setIdLote(String.format("DIV/%s", now.format(DateTimeFormatter.ofPattern("ddMMHHmmss"))))
				.setCdCriador(cdUsuario)
				.build();
	}
	
	@Override
	public int gerarLoteDividaAtiva(int cdLote, CustomConnection customConnection) throws Exception {
		LoteDividaAtiva loteDividaAtiva = setLoteDividaAtiva(cdLote);
		loteDividaAtivaRepository.insert(loteDividaAtiva, customConnection);
		return loteDividaAtiva.getCdLoteDividaAtiva();
	}
	
	private LoteDividaAtiva setLoteDividaAtiva(int cdLote) {
		return new LoteDividaAtivaBuilder()
				.setCdLote(cdLote)
				.setStLote(LoteStatusDividaAtivaEnum.GERADO.getKey())
				.build();
	}

	@Override
	public void gerarLoteDividaAtivaAit(List<DividaAtivaDTO> aitList, int cdLoteDividaAtiva, CustomConnection customConnection) throws Exception {
		for(DividaAtivaDTO dividaAtiva: aitList) {
			loteDividaAtivaAitRepository.insert(setLoteDividaAtivaAit(dividaAtiva.getCdAit(), cdLoteDividaAtiva), customConnection);
		}
	}
	
	private LoteDividaAtivaAit setLoteDividaAtivaAit (int cdAit, int cdLoteDividaAtiva) {
		return new LoteDividaAtivaAitBuilder()
				.setCdAit(cdAit)
				.setCdDividaAtiva(cdLoteDividaAtiva)
				.build();
	}

	@Override
	public int inserirArquivo(int cdLote, CustomConnection customConnection) throws Exception {
		byte[] arquivoCsv = geraArquivoDividaAtiva.gerarArquivoEnvio(cdLote, customConnection);
		Arquivo csv = setArquivo(arquivoCsv);
		arquivoRepository.insert(csv, customConnection);
		return csv.getCdArquivo();
	}
	
	private Arquivo setArquivo(byte[] csv) {
		now = LocalDateTime.now();
		return new ArquivoBuilder()
				.setNmArquivo(String.format("DIV/%s", now.format(DateTimeFormatter.ofPattern("ddMMHHmm"))))
				.setDtCriacao(new GregorianCalendar())
				.setNmDocumento("ARQUIVO CSV DIVIDA ATIVA")
				.setBlbArquivo(csv)
				.build();
	}
	
	@Override
	public void atualizaLote(int cdLote, int cdArquivo, CustomConnection customConnection) throws Exception {
		Lote lote = loteRepository.get(cdLote, customConnection);
		lote.setCdArquivo(cdArquivo);
		loteRepository.update(lote, customConnection);
	}
}
