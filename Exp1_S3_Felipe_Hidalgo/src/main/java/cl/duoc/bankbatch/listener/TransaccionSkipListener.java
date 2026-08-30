package cl.duoc.bankbatch.listener;

import cl.duoc.bankbatch.dto.TransaccionCsv;
import cl.duoc.bankbatch.model.Transaccion;

import org.springframework.batch.core.listener.SkipListener;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
public class TransaccionSkipListener
        implements SkipListener<TransaccionCsv, Transaccion> {

    @Override
    public void onSkipInRead(Throwable throwable) {

        if (throwable instanceof FlatFileParseException error) {

            System.out.println(
                    "[SKIP] Linea " + error.getLineNumber()
                    + " omitida por error de lectura: "
                    + error.getInput()
            );

        } else {

            System.out.println(
                    "[SKIP] Registro omitido durante la lectura: "
                    + throwable.getMessage()
            );
        }
    }

    @Override
    public void onSkipInProcess(
            TransaccionCsv item,
            Throwable throwable) {

        System.out.println(
                "[SKIP] Registro omitido durante procesamiento: "
                + item
                + " - "
                + throwable.getMessage()
        );
    }

    @Override
    public void onSkipInWrite(
            Transaccion item,
            Throwable throwable) {

        System.out.println(
                "[SKIP] Registro omitido durante escritura: "
                + item
                + " - "
                + throwable.getMessage()
        );
    }
}