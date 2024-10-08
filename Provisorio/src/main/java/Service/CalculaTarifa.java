package Service;

import Entity.Ticket;
import Utils.Util;

import java.time.Duration;

public class CalculaTarifa {
    private static final double TARIFA = 5.90;
    private static final double ADICIONAL = 2.50;
    private static final double PERNOITE = 50.00;
    private static final int CORTESIA = 15;

    public double calcularValor(Ticket ticket) {
        validarTicket(ticket); // Valida o ticket antes de prosseguir

        Duration duracao = Duration.between(ticket.getEntrada(), ticket.getSaida());
        long minutos = duracao.toMinutes();

        if (minutos <= CORTESIA) {
            return 0.0; // Cortesia para 15 minutos ou menos
        }

        long horas = (minutos + 59) / 60; // Arredonda para cima o valor das horas

        // Verificar se é um caso de pernoite
        if (Util.isPernoite(ticket.getEntrada(), ticket.getSaida())) {
            long diasPernoite = Util.calcularDiasPernoite(ticket.getEntrada(), ticket.getSaida());
            double valorPernoite = diasPernoite * PERNOITE;
            return aplicarDescontoSeVip(valorPernoite, ticket.isVip());
        }

        // Calcular tarifa padrão
        double valor = TARIFA;
        if (horas > 1) {
            valor += (horas - 1) * ADICIONAL;
        }

        return aplicarDescontoSeVip(valor, ticket.isVip());
    }

    private double aplicarDescontoSeVip(double valor, boolean isVip) {
        return isVip ? valor * 0.5 : valor;
    }

    // Método para validar o ticket antes do cálculo

    private void validarTicket(Ticket ticket) {
        if (ticket.getEntrada() == null || ticket.getSaida() == null) {
            throw new IllegalArgumentException("Datas de entrada e saída não podem ser nulas.");
        }
        if (ticket.getSaida().isBefore(ticket.getEntrada())) {
            throw new IllegalArgumentException("A data e hora de saída não pode ser anterior à data de entrada.");
        }
    }







}
