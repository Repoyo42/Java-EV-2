package com.DespachoService.Despacho;

import com.DespachoService.Despacho.Model.Despacho;
import com.DespachoService.Despacho.Model.EstadoDespacho;
import com.DespachoService.Despacho.Repository.DespachoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:despacho;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class DespachoApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DespachoRepository repository;

	@BeforeEach
	void limpiarDespachos() {
		repository.deleteAll();
	}

	@Test
	void creaDespachoYAvanzaElFlujoDePicking() throws Exception {
		Despacho despacho = new Despacho(null, 50L, "Av. Central 123", null);
		String respuestaCrear = mockMvc.perform(post("/despachos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(despacho)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();
		Despacho creado = objectMapper.readValue(respuestaCrear, Despacho.class);

		mockMvc.perform(get("/despachos/orden/{ordenId}", 50))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.estado").value("PENDIENTE"));

		mockMvc.perform(patch("/despachos/{id}/estado", creado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(EstadoDespacho.EN_PICKING)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.estado").value("EN_PICKING"));

		mockMvc.perform(patch("/despachos/{id}/estado", creado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(EstadoDespacho.EN_TRANSITO)))
			.andExpect(status().isConflict());

		mockMvc.perform(post("/despachos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(despacho)))
			.andExpect(status().isConflict());
	}

	@Test
	void rechazaDespachosSinOrdenNiDireccion() throws Exception {
		Despacho despacho = new Despacho(null, 0L, " ", null);

		mockMvc.perform(post("/despachos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(despacho)))
			.andExpect(status().isBadRequest());
	}

}
