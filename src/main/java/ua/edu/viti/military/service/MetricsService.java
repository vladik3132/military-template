package ua.edu.viti.military.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервіс для записування метрик та моніторингу за допомогою Micrometer
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {
    
    private final MeterRegistry meterRegistry;
    
    // Counters
    private static final String METRIC_VEHICLES_CREATED = "vehicles.created.total";
    private static final String METRIC_DRIVERS_CREATED = "drivers.created.total";
    private static final String METRIC_MOVEMENTS_RECORDED = "movements.recorded.total";
    private static final String METRIC_ACCIDENTS = "accidents.total";
    private static final String METRIC_MAINTENANCE = "maintenance.scheduled.total";
    private static final String METRIC_ASSIGNMENTS = "assignments.total";
    
    // Timers
    private static final String METRIC_API_RESPONSE_TIME = "api.response.time";
    private static final String METRIC_DB_QUERY_TIME = "database.query.time";
    
    // Gauge (буде встановлено в контролерах)
    private static final String METRIC_ACTIVE_VEHICLES = "vehicles.active";
    private static final String METRIC_AVAILABLE_DRIVERS = "drivers.available";
    
    /**
     * Записати кількість створених транспортних засобів
     */
    public void recordVehicleCreated() {
        Counter.builder(METRIC_VEHICLES_CREATED)
                .description("Кількість створених транспортних засобів")
                .tag("type", "vehicle")
                .register(meterRegistry)
                .increment();
        log.debug("Metric recorded: {}", METRIC_VEHICLES_CREATED);
    }
    
    /**
     * Записати кількість створених водіїв
     */
    public void recordDriverCreated() {
        Counter.builder(METRIC_DRIVERS_CREATED)
                .description("Кількість створених водіїв")
                .tag("type", "driver")
                .register(meterRegistry)
                .increment();
        log.debug("Metric recorded: {}", METRIC_DRIVERS_CREATED);
    }
    
    /**
     * Записати зареєстровану операцію руху
     */
    public void recordMovementLogged(String movementType) {
        Counter.builder(METRIC_MOVEMENTS_RECORDED)
                .description("Кількість зареєстрованих операцій руху")
                .tag("type", movementType)
                .register(meterRegistry)
                .increment();
        log.debug("Metric recorded: {} with type: {}", METRIC_MOVEMENTS_RECORDED, movementType);
    }
    
    /**
     * Записати кількість аварій
     */
    public void recordAccident() {
        Counter.builder(METRIC_ACCIDENTS)
                .description("Кількість зареєстрованих аварій")
                .tag("severity", "high")
                .register(meterRegistry)
                .increment();
        log.warn("Metric recorded: {}", METRIC_ACCIDENTS);
    }
    
    /**
     * Записати планове обслуговування
     */
    public void recordMaintenanceScheduled(String maintenanceType) {
        Counter.builder(METRIC_MAINTENANCE)
                .description("Кількість планових обслуговувань")
                .tag("type", maintenanceType)
                .register(meterRegistry)
                .increment();
        log.debug("Metric recorded: {} with type: {}", METRIC_MAINTENANCE, maintenanceType);
    }
    
    /**
     * Записати призначення водія
     */
    public void recordAssignment() {
        Counter.builder(METRIC_ASSIGNMENTS)
                .description("Кількість призначень водіїв")
                .tag("action", "assign")
                .register(meterRegistry)
                .increment();
        log.debug("Metric recorded: {}", METRIC_ASSIGNMENTS);
    }
    
    /**
     * Записати час відповіді API запиту
     */
    public void recordApiResponseTime(String endpoint, long durationMs) {
        Timer.builder(METRIC_API_RESPONSE_TIME)
                .description("Час відповіді API запиту")
                .tag("endpoint", endpoint)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry)
                .record(java.time.Duration.ofMillis(durationMs));
        log.debug("API Response Time recorded for {}: {} ms", endpoint, durationMs);
    }
    
    /**
     * Записати час виконання запиту до БД
     */
    public void recordDatabaseQueryTime(String query, long durationMs) {
        Timer.builder(METRIC_DB_QUERY_TIME)
                .description("Час виконання запиту до бази даних")
                .tag("query", query)
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry)
                .record(java.time.Duration.ofMillis(durationMs));
        log.debug("Database Query Time recorded for {}: {} ms", query, durationMs);
    }
    
    /**
     * Встановити калібр для активних транспортних засобів
     */
    public void setActiveVehiclesGauge(long count) {
        meterRegistry.gauge(METRIC_ACTIVE_VEHICLES, count);
        log.debug("Gauge updated: {} = {}", METRIC_ACTIVE_VEHICLES, count);
    }
    
    /**
     * Встановити калібр для доступних водіїв
     */
    public void setAvailableDriversGauge(long count) {
        meterRegistry.gauge(METRIC_AVAILABLE_DRIVERS, count);
        log.debug("Gauge updated: {} = {}", METRIC_AVAILABLE_DRIVERS, count);
    }
}
