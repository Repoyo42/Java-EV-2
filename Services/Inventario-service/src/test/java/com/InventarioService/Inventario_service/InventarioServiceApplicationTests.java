package com.InventarioService.Inventario_service;

import com.InventarioService.Inventario_service.Model.Producto;
import com.InventarioService.Inventario_service.Repository.ProductoRepository;
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
		"spring.datasource.url=jdbc:h2:mem:inventario;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class InventarioServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductoRepository repository;

	@BeforeEach
	void limpiarProductos() {
		repository.deleteAll();
	}

	@Test
	void crudDeProductos() throws Exception {
		Producto producto = new Producto(null, "SKU-100", "Teclado", "Teclado mecanico", 8);
		String respuestaCrear = mockMvc.perform(post("/inventario")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(producto)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();
		Producto creado = objectMapper.readValue(respuestaCrear, Producto.class);

		mockMvc.perform(get("/inventario/{id}", creado.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.sku").value("SKU-100"));

		Producto actualizado = new Producto(null, "SKU-100", "Teclado compacto", "Bluetooth", 5);
		mockMvc.perform(put("/inventario/{id}", creado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actualizado)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombre").value("Teclado compacto"))
			.andExpect(jsonPath("$.cantidad").value(5));

		mockMvc.perform(get("/inventario"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].sku").value("SKU-100"));

		mockMvc.perform(delete("/inventario/{id}", creado.getId()))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/inventario/{id}", creado.getId()))
				.andExpect(status().isNotFound());
	}

	@Test
	void rechazaProductosConDatosInvalidos() throws Exception {
		Producto producto = new Producto(null, " ", "", "Sin datos validos", -1);

		mockMvc.perform(post("/inventario")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(producto)))
			.andExpect(status().isBadRequest());
	}

}
