package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;

public class CarroTest {

	private CarroService carroservice = new CarroService();
	
	@Test
	void ListaCarros() {
		List<Carro> carros = carroservice.getCarros();
		assertNotNull(carros);
		//Valida se encontrou algo
		assertTrue(carros.size()>0);
		//Valida se encontrou o Tucker
		Carro tucker = carroservice.findByName("Tucker 1948").get(0);
		assertEquals("Tucker 1948", tucker.getNome());
		//Valida se encontrou a Ferrari
		Carro ferrari = carroservice.findByName("Ferrari FF").get(0);
		assertEquals("Ferrari FF", ferrari.getNome());
		//Valida se encontrou o Bugatti
		Carro bugatti = carroservice.findByName("Bugatti Veyron").get(0);
		assertEquals("Bugatti Veyron", bugatti.getNome());
	}
	
	@Test
	void SalvarDeletarCarro() {
		Carro c = new Carro();
		c.setNome("Teste");
		c.setDesc("Teste desc");
		c.setUrlFoto("url foto aqui");
		c.setUrlVideo("url video aqui");
		c.setLatitude("lat");
		c.setLongitude("long");
		c.setTipo("tipo");
		carroservice.save(c);
		//id do carro salvo
		Long id = c.getId();
		assertNotNull(id);
		//Busca no banco para confirmar que o carro foi salvo
		c = carroservice.getCarro(id);
		assertEquals("Teste", c.getNome());
		assertEquals("Teste desc", c.getDesc());
		assertEquals("url foto aqui", c.getUrlFoto());
		assertEquals("url video aqui", c.getUrlVideo());
		assertEquals("lat", c.getLatitude());
		assertEquals("long", c.getLongitude());
		assertEquals("tipo", c.getTipo());
		//Atualiza o carro
		c.setNome("Teste UPDATE");
		carroservice.save(c);
		//Busca o carro novamente
		c = carroservice.getCarro(id);
		assertEquals("Teste UPDATE", c.getNome());
		//Deleta o carro
		carroservice.delete(id);
		//Busca o carro novamente
		c = carroservice.getCarro(id);
		//Desta vez o carro não existe mais
		assertNull(c);
	}

}
