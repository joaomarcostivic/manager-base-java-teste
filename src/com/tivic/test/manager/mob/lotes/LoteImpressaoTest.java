package com.tivic.test.manager.mob.lotes;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tivic.manager.mob.lotes.dto.CreateAitLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.dto.impressao.CreateLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.test.manager.mob.lotes.config.InjectLoteDependenciesTest;

public class LoteImpressaoTest {
	private ILoteImpressaoService loteImpressaoService;

	@Before
	public void before() throws Exception{
		InicializationBeans.init(new InjectLoteDependenciesTest());
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
	}
	
    private CreateLoteImpressaoDTO createDefaultLoteImpressao() {
        CreateLoteImpressaoDTO input = new CreateLoteImpressaoDTO();
        input.setCdCriador(1);
        input.setTpImpressao(TipoLoteImpressaoEnum.LOTE_NAI.getKey());
        input.setAits(Arrays.asList(new CreateAitLoteImpressaoDTO()));
        return input;
    }
	
	@Test
    public void shouldCreateLoteImpressao() throws Exception {
        CreateLoteImpressaoDTO input = createDefaultLoteImpressao();
        LoteImpressao output = loteImpressaoService.insert(input);
		assertTrue("CD_LOTE_IMPRESSAO deveria existir", output.getCdLoteImpressao() > 0);
		
		LoteImpressao loteImpressao = loteImpressaoService.get(output.getCdLoteImpressao());
		assertNotNull(loteImpressao, "O lote de impressão deveria ser salvo");
    }

    @Test
    public void shouldGetLoteImpressao() throws Exception {
        CreateLoteImpressaoDTO input = createDefaultLoteImpressao();
        LoteImpressao output = loteImpressaoService.insert(input);
        LoteImpressao loteImpressao = loteImpressaoService.get(output.getCdLoteImpressao());
        assertNotNull(loteImpressao, "O lote de impressão deveria ser salvo");
    }

    @Test
    public void shouldFindLoteImpressao() throws Exception {
        CreateLoteImpressaoDTO input = createDefaultLoteImpressao();
        LoteImpressao output = loteImpressaoService.insert(input);

        assertTrue("CD_LOTE_IMPRESSAO deveria existir", output.getCdLoteImpressao() > 0);

        SearchCriterios criterios = new SearchCriterios();
        criterios.addCriteriosEqualInteger("cd_lote_impressao", output.getCdLoteImpressao());
        List<LoteImpressao> loteImpressao = loteImpressaoService.find(criterios);

        assertTrue("Lista não deveria estar vazia", !loteImpressao.isEmpty());
    }
}
