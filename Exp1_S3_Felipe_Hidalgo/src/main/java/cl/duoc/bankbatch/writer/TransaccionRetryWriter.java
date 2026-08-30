package cl.duoc.bankbatch.writer;

import cl.duoc.bankbatch.model.Transaccion;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class TransaccionRetryWriter implements ItemWriter<Transaccion> {

    private final JdbcBatchItemWriter<Transaccion> transaccionWriter;
    private final boolean activarPruebaRetry;

    // Garantiza que la falla controlada ocurra solamente una vez
    private final AtomicBoolean fallaSimulada = new AtomicBoolean(false);

    public TransaccionRetryWriter(
            @Qualifier("transaccionWriter")
            JdbcBatchItemWriter<Transaccion> transaccionWriter,
            @Value("${batch.prueba.retry:false}")
            boolean activarPruebaRetry) {

        this.transaccionWriter = transaccionWriter;
        this.activarPruebaRetry = activarPruebaRetry;
    }

    @Override
    public void write(Chunk<? extends Transaccion> chunk) throws Exception {

        // Solo se utiliza para demostrar el mecanismo Retry
        if (activarPruebaRetry
                && fallaSimulada.compareAndSet(false, true)) {

            System.out.println(
                    "[RETRY] Falla transitoria simulada. "
                    + "Spring Batch intentara nuevamente la escritura."
            );

            throw new TransientDataAccessResourceException(
                    "Falla transitoria controlada para probar Retry"
            );
        }

        // Si no hay falla, utiliza el writer real de MySQL
        transaccionWriter.write(chunk);
    }
}