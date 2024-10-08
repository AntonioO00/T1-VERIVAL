package Service;

import Entity.Ticket;
import Utils.Util;

import java.time.Duration;

public class CalculoValor {
    private static final double TARIFA = 5.90;
    private static final double ADICIONAL = 2.50;
    private static final double PERNOITE = 50.00;
    private static final int CORTESIA = 15;

    public double valorTicket(Ticket ticket) {
        validarTicket(ticket);

        Duration duracao = Duration.between(ticket.getEntrada(), ticket.getSaida());
        long minutos = duracao.toMinutes();
        long horas = (minutos + 59) / 60;

        if (minutos <= CORTESIA) {
            return 0.0;
        }

        if (Util.isPernoite(ticket.getEntrada(), ticket.getSaida())) {
            long diasP = Util.calcularDiasPernoite(ticket.getEntrada(), ticket.getSaida());
            double valorP = diasP * PERNOITE;
            return descontoVip(valorP, ticket.isVip());
        }

        double valor = TARIFA;

        if (horas > 1) {
            valor += (horas - 1) * ADICIONAL;
        }

        return descontoVip(valor, ticket.isVip());
    }

    private double descontoVip(double valor, boolean isVip) {
        return isVip ? valor * 0.5 : valor;
    }


    private void validarTicket(Ticket ticket) {

        if (ticket == null) {
            throw new IllegalArgumentException("O ticket não pode ser nulo");
        }
        if (ticket.getEntrada() == null || ticket.getSaida() == null) {
            throw new IllegalArgumentException("A data e hora de entrada e saída não podem ser nulas");
        }
        if (ticket.getSaida().isBefore(ticket.getEntrada())) {
            throw new IllegalArgumentException("A data e hora de saída não pode ser anterior à data de entrada");
        }
    }


}
