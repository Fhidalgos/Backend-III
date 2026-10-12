package cl.duoc.batch_service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.SkipListener;
import org.springframework.stereotype.Component;

@Component
public class BatchErrorListener implements SkipListener<Object, Object> {

    private static final Logger logger =
            LoggerFactory.getLogger(BatchErrorListener.class);

    @Override
    public void onSkipInRead(Throwable error) {
        logger.warn(
                "Registro omitido durante la lectura. Motivo: {}",
                error.getMessage()
        );
    }

    @Override
    public void onSkipInProcess(Object item, Throwable error) {
        logger.warn(
                "Registro omitido durante el procesamiento. Registro: {}. Motivo: {}",
                item,
                error.getMessage()
        );
    }

    @Override
    public void onSkipInWrite(Object item, Throwable error) {
        logger.error(
                "Registro omitido durante la escritura. Registro: {}. Motivo: {}",
                item,
                error.getMessage()
        );
    }
}