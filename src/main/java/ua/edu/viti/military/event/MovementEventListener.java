package ua.edu.viti.military.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Асинхронний обробник подій для військового транспорту.
 * Методи виконуються в окремих потоках для неблокування основного запиту.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MovementEventListener {

    /**
     * Обробляє подію призначення водія
     */
    @Async
    @EventListener
    public void onDriverAssigned(DriverAssignedEvent event) {
        log.info("🚗 Водій {} призначен до транспорту {} (ID: {})",
                event.getDriverName(), event.getRegistrationNumber(), event.getVehicleId());
        
        // Виконувати додаткові асинхронні операції:
        // - Відправити повідомлення водієві
        // - Оновити статистику в кеші
        // - Записати аудит в базу даних
        // - Відправити сповіщення адміністратору
    }

    /**
     * Обробляє подію повернення транспортного засобу
     */
    @Async
    @EventListener
    public void onVehicleReturned(VehicleReturnedEvent event) {
        log.info("🔙 Транспортний засіб {} повернутий у місце {}", 
                event.getRegistrationNumber(), event.getDestination());
        
        // Виконувати додаткові асинхронні операції:
        // - Очистити кеш для цього транспорту
        // - Оновити статистику використання
        // - Запустити перевірку стану
        // - Заслати звіт про повернення
    }

    /**
     * Обробляє подію планування технічного обслуговування
     */
    @Async
    @EventListener
    public void onMaintenanceScheduled(MaintenanceScheduledEvent event) {
        log.info("🔧 Техническое обслуживание {} для {} запланировано",
                event.getMaintenanceType(), event.getRegistrationNumber());
        
        // Виконувати додаткові асинхронні операції:
        // - Змінити статус транспорту на "MAINTENANCE"
        // - Блокувати призначення нових водіїв
        // - Запланувати комунікацію з технічним персоналом
        // - Оновити дошку оголошень
    }

    /**
     * Обробляє подію про інцидент/аварію
     */
    @Async
    @EventListener
    public void onAccidentReported(AccidentReportedEvent event) {
        log.warn("⚠️ АВАРІЯ! Транспортний засіб {} потребує негайного розслідування. " +
                "Опис: {}",
                event.getRegistrationNumber(), event.getDescription());
        
        // Виконувати додаткові асинхронні операції:
        // - Змінити статус на "ACCIDENT"
        // - Відправити критичне сповіщення менеджерам
        // - Збільшити лічильник аварій
        // - Заблокувати використання засобу до перевірки
        // - Запустити розслідування
    }
}
