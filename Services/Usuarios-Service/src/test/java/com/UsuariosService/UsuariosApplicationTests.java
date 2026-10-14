package com.UsuariosService;

import com.UsuariosService.Model.Usuario;
import com.UsuariosService.Repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:usuarios;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class UsuariosApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UsuarioRepository repository;

	@BeforeEach
	void limpiarUsuarios() {
		repository.deleteAll();
	}

	@Test
	void crudDeUsuariosYRoles() throws Exception {
		Usuario usuario = new Usuario(null, "Ana Perez", "ana@example.com", Set.of("CLIENTE"));
		String respuestaCrear = mockMvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(usuario)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();
		Usuario creado = objectMapper.readValue(respuestaCrear, Usuario.class);

		mockMvc.perform(get("/usuarios/{id}", creado.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("ana@example.com"))
				.andExpect(jsonPath("$.roles[0]").value("CLIENTE"));

		Usuario actualizado = new Usuario(null, "Ana Perez", "ana@example.com", Set.of("ADMIN"));
		mockMvc.perform(put("/usuarios/{id}", creado.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(actualizado)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.roles[0]").value("ADMIN"));

		mockMvc.perform(get("/usuarios"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nombre").value("Ana Perez"));

		mockMvc.perform(delete("/usuarios/{id}", creado.getId()))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/usuarios/{id}", creado.getId()))
				.andExpect(status().isNotFound());
	}

	@Test
	void rechazaDatosInvalidosYEmailsDuplicados() throws Exception {
		Usuario invalido = new Usuario(null, " ", "correo-invalido", Set.of());
		mockMvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalido)))
			.andExpect(status().isBadRequest());

		Usuario usuario = new Usuario(null, "Luis Soto", "luis@example.com", Set.of("CLIENTE"));
		String respuestaCrear = mockMvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(usuario)))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();
		Usuario creado = objectMapper.readValue(respuestaCrear, Usuario.class);

		mockMvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(usuario)))
			.andExpect(status().isConflict());
	}

}
