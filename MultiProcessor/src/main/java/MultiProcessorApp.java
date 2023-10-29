import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class MultiProcessorApp {
    public static void main(String[] args) {
        // Создаем пул потоков с использованием ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Создаем общий ресурс, к которому будут обращаться потоки
        SharedResource sharedResource = new SharedResource();

        // Запускаем задачи в пуле потоков
        executor.execute(new MyRunnable(sharedResource, "Hello"));
        executor.execute(new MyRunnable(sharedResource, "World"));

        // Завершаем работу пула потоков после выполнения всех задач
        executor.shutdown();
        try {
            // Ожидаем завершения выполнения всех задач в течение 5 секунд
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                // Если задачи не завершились вовремя, прерываем их выполнение
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            // В случае прерывания, прерываем все задачи
            executor.shutdownNow();
        }
    }

    static class MyRunnable implements Runnable {
        private SharedResource sharedResource;
        private String data;

        public MyRunnable(SharedResource sharedResource, String data) {
            this.sharedResource = sharedResource;
            this.data = data;
        }

        public void run() {
            // Код, который будет выполняться в каждом потоке
            sharedResource.processData(data);
        }
    }

    static class SharedResource {
        public synchronized void processData(String data) {
            // Код обработки данных
            System.out.println("Поток " + Thread.currentThread().getId() + " обрабатывает данные: " + data);
        }
    }
}
