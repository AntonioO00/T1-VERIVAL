package Service;

import Entity.Ticket;
import Utils.Tempo;

import java.time.Duration;
import java.time.LocalTime;

public class CalculaTarifa {
    private static final double TARIFA = 5.90;
    private static final double ADICIONAL = 2.50;
    private static final double PERNOITE = 50.00;
    private static final int CORTESIA = 15;
    private static final LocalTime HORA_ABERTURA = LocalTime.of(8, 0);

    public double calcularValor(Ticket ticket) {
        Duration duracao = Duration.between(ticket.getEntrada(), ticket.getSaida());
        long minutos = duracao.toMinutes();

        if (minutos <= CORTESIA) {
            return 0.0;
        }

        long horas = (minutos + 59) / 60; // Arredonda para cima

        // Verificar se é um pernoite
        if (Tempo.isPernoite(ticket.getEntrada(), ticket.getSaida())) {
            long diasPernoite = Tempo.calcularDiasPernoite(ticket.getEntrada(), ticket.getSaida());
            double valorPernoite = diasPernoite * PERNOITE;
            return ticket.isVip() ? valorPernoite * 0.5 : valorPernoite;
        }

        // Calcular tarifa normal
        double valor = TARIFA;
        if (horas > 1) {
            valor += (horas - 1) * ADICIONAL;
        }

        return ticket.isVip() ? valor * 0.5 : valor;
    }
}
