package ua.edu.viti.military.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Фільтр для додавання Correlation ID до кожного запиту для розподіленого трейсування
 * Це допомагає відстежувати запити крізь різні сервіси та логи
 */
@Slf4j
public class CorrelationIdFilter extends OncePerRequestFilter {
    
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Отримати або створити Correlation ID
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        
        // Додати в MDC (Mapped Diagnostic Context) для логування
        org.slf4j.MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
        
        try {
            // Додати в Response Header
            response.setHeader(CORRELATION_ID_HEADER, correlationId);
            
            log.debug("Processing request with correlationId: {} | Method: {} | URI: {}", 
                    correlationId, request.getMethod(), request.getRequestURI());
            
            filterChain.doFilter(request, response);
            
        } finally {
            // Очистити MDC після обробки запиту
            org.slf4j.MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }
    
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // Не фільтрувати health check endpoint
        String path = request.getRequestURI();
        return path.equals("/actuator/health") || path.equals("/health");
    }
}
