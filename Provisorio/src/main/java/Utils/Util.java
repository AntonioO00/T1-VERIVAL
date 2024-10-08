package Utils;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Util {
    public static boolean isPernoite(LocalDateTime entrada, LocalDateTime saida) {
        return saida.toLocalTime().isAfter(LocalTime.of(8, 0)) && saida.toLocalDate().isAfter(entrada.toLocalDate());
    }

    public static long calcularDiasPernoite(LocalDateTime entrada, LocalDateTime saida) {
        return saida.toLocalDate().toEpochDay() - entrada.toLocalDate().toEpochDay();
    }

}
