package com.OrdenesService.Ordenes_service;

import com.OrdenesService.Ordenes_service.Model.EstadoOrden;
import com.OrdenesService.Ordenes_service.Model.ItemOrden;
import com.OrdenesService.Ordenes_service.Model.Orden;
import com.OrdenesService.Ordenes_service.Repository.OrdenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:ordenes;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class OrdenesServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrdenRepository repository;

	@BeforeEach
	void limpiarOrdenes() {
		repository.deleteAll();
	}

	@Test
	void crudDeOrdenesYProductos() throws Exception {
		Orden orden = new Orden(null, 10L, List.of(new ItemOrden(20L, 2)), null);
		String respuestaCrear = mockMvc.perform(post("/ordenes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orden)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();
		Orden creada = objectMapper.readValue(respuestaCrear, Orden.class);

		mockMvc.perform(get("/ordenes/{id}", creada.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.estado").value("PENDIENTE"))
				.andExpect(jsonPath("$.items[0].productoId").value(20))
				.andExpect(jsonPath("$.items[0].cantidad").value(2));

		mockMvc.perform(get("/ordenes/cliente/{clienteId}", 10))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(creada.getId()));

		Orden actualizada = new Orden(null, 10L, List.of(new ItemOrden(20L, 3)), EstadoOrden.CONFIRMADA);
		mockMvc.perform(put("/ordenes/{id}", creada.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actualizada)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.estado").value("CONFIRMADA"))
			.andExpect(jsonPath("$.items[0].cantidad").value(3));

		mockMvc.perform(delete("/ordenes/{id}", creada.getId()))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/ordenes/{id}", creada.getId()))
				.andExpect(status().isNotFound());
	}

	@Test
	void rechazaOrdenesSinClienteNiItemsValidos() throws Exception {
		Orden orden = new Orden(null, 0L, List.of(new ItemOrden(20L, 0)), null);

		mockMvc.perform(post("/ordenes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orden)))
			.andExpect(status().isBadRequest());
	}

}
