package com.classmate.usuarioservice.controller;

import com.classmate.usuarioservice.dto.UsuarioRequest;
import com.classmate.usuarioservice.dto.UsuarioResponse;
import com.classmate.usuarioservice.entity.TipoUsuario;
import com.classmate.usuarioservice.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest → levanta solo la capa web, sin BD ni MySQL
// @AutoConfigureMockMvc(addFilters = false) → desactiva security filters en tests
@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        usuarioResponse = new UsuarioResponse(
                1L, 10L, "Juan", "Pérez",
                "juan@mail.com", "+56912345678",
                LocalDate.of(1995, 5, 20), TipoUsuario.ESTUDIANTE
        );
    }

    // ─── TEST 1: POST /usuarios — crear usuario exitoso ──────────────────────
    @Test
    public void testCrearUsuario() throws Exception {

        UsuarioRequest request = new UsuarioRequest();
        request.setAuthUserId(10L);
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@mail.com");
        request.setTelefono("+56912345678");
        request.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        request.setTipoUsuario(TipoUsuario.ESTUDIANTE);

        when(usuarioService.crear(any())).thenReturn(usuarioResponse);

        mockMvc.perform(
                        post("/usuarios")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }

    // ─── TEST 2: GET /usuarios — listar todos ────────────────────────────────
    @Test
    public void testListarUsuarios() throws Exception {

        when(usuarioService.listar()).thenReturn(List.of(usuarioResponse));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    // ─── TEST 3: GET /usuarios/{id} — buscar por ID ──────────────────────────
    @Test
    public void testBuscarPorId() throws Exception {

        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioResponse);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.apellido").value("Pérez"));
    }

    // ─── TEST 4: GET /usuarios/auth/{authUserId} ──────────────────────────────
    @Test
    public void testBuscarPorAuthUserId() throws Exception {

        when(usuarioService.buscarPorAuthUserId(10L)).thenReturn(usuarioResponse);

        mockMvc.perform(get("/usuarios/auth/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authUserId").value(10));
    }

    // ─── TEST 5: PUT /usuarios/{id} — actualizar ─────────────────────────────
    @Test
    public void testActualizarUsuario() throws Exception {

        UsuarioRequest request = new UsuarioRequest();
        request.setAuthUserId(10L);
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@mail.com");
        request.setTelefono("+56912345678");
        request.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        request.setTipoUsuario(TipoUsuario.ESTUDIANTE);

        when(usuarioService.actualizar(eq(1L), any())).thenReturn(usuarioResponse);

        mockMvc.perform(
                        put("/usuarios/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    // ─── TEST 6: DELETE /usuarios/{id} — eliminar ────────────────────────────
    @Test
    public void testEliminarUsuario() throws Exception {

        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).eliminar(1L);
    }
}
