package exception;

import com.classmate.reservaservice.exception.GlobalExceptionHandler;
import com.classmate.reservaservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldHandleNotFoundException() {

        ResourceNotFoundException ex =
                new ResourceNotFoundException("No encontrado");

        ResponseEntity<Map<String, String>> response =
                handler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND,
                response.getStatusCode());

        assertEquals("No encontrado",
                response.getBody().get("mensaje"));
    }

    @Test
    void shouldHandleRuntimeException() {

        RuntimeException ex =
                new RuntimeException("Error runtime");

        ResponseEntity<Map<String, String>> response =
                handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        assertEquals("Error runtime",
                response.getBody().get("mensaje"));
    }

    @Test
    void shouldHandleGenericException() {

        Exception ex =
                new Exception("Error");

        ResponseEntity<Map<String, String>> response =
                handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode());

        assertEquals("Error interno del servidor",
                response.getBody().get("mensaje"));
    }
}