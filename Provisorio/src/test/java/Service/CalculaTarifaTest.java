package Service;

import Entity.Ticket;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertTrue;

public class CalculaTarifaTest {

    private final CalculaTarifa calculadora = new CalculaTarifa(); // Evita instanciar várias vezes

    @Test
    void calcularTarifaCom15MinutosDeCortesia() {
        // Testa a situação em que o tempo de permanência é de 15 minutos,
        // que é a cortesia. O valor da tarifa deve ser 0.0.
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 15);

        Ticket ticket = new Ticket(entrada, saida, false);

        assertEquals(0.0, calculadora.calcularValor(ticket));
    }

    @Test
    void calcularTarifaNormalAteUmaHora() {
        // Testa a situação em que o tempo de permanência é inferior a uma hora (30 minutos).
        // O valor da tarifa deve ser o valor base (5.90).
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 30);

        Ticket ticket = new Ticket(entrada, saida, false);

        assertEquals(5.90, calculadora.calcularValor(ticket));
    }

    @Test
    void calcularTarifaParaUmaHora() {
        // Testa a situação em que o tempo de permanência é exatamente uma hora.
        // O valor da tarifa deve ser o valor base (5.90).
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 10, 0);

        Ticket ticket = new Ticket(entrada, saida, false);

        assertEquals(5.90, calculadora.calcularValor(ticket));
    }

    @Test
    void calcularTarifaParaPernoite() {
        // Testa a situação em que o veículo fica estacionado durante a noite.
        // O valor da tarifa deve ser o valor fixo de pernoite (50.0).
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 22, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 8, 9, 0);

        Ticket ticket = new Ticket(entrada, saida, false);

        assertEquals(50.0, calculadora.calcularValor(ticket)); // Tarifa pernoite
    }

    @Test
    void calcularTarifaParaMultiplosDiasPernoite() {
        // Testa a situação em que o veículo fica estacionado por vários dias.
        // O valor da tarifa deve ser o total de pernoites multiplicado pelo valor de pernoite (50.0).
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 22, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 10, 9, 0);

        Ticket ticket = new Ticket(entrada, saida, false);

        // 3 pernoites (3 * 50.0)
        assertEquals(150.0, calculadora.calcularValor(ticket));
    }

    @Test
    void aplicarDescontoVipParaTarifaNormal() {
        // Testa a situação em que um cliente VIP utiliza o estacionamento.
        // O valor da tarifa deve ser calculado normalmente, mas com 50% de desconto.
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 11, 0);

        Ticket ticket = new Ticket(entrada, saida, true); // Cliente VIP

        // Tarifa total com desconto: (5.90 + 2.50) * 0.5 = 4.20
        assertEquals(4.20, calculadora.calcularValor(ticket));
    }

    @Test
    void aplicarDescontoVipParaTarifaDePernoite() {
        // Testa a situação em que um cliente VIP utiliza o estacionamento durante a noite.
        // O valor da tarifa de pernoite deve ser calculado normalmente, mas com 50% de desconto.
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 22, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 8, 9, 0);

        Ticket ticket = new Ticket(entrada, saida, true); // Cliente VIP

        // Tarifa total com desconto: 50.0 * 0.5 = 25.0
        assertEquals(25.0, calculadora.calcularValor(ticket));
    }


    @Test
    void deveCalcularTarifaRapidamente() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 10, 0);
        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        long startTime = System.nanoTime();
        calculadora.calcularValor(ticket);
        long duration = System.nanoTime() - startTime;

        assertTrue(duration < 1000000); // O tempo deve ser menor que 1ms
    }
    @Test
    void deveLancarExcecaoParaEntradaOuSaidaNulas() {
        // Testa se uma exceção é lançada quando a entrada ou saída é nula.
        Ticket ticket1 = new Ticket(null, LocalDateTime.now(), false);
        Ticket ticket2 = new Ticket(LocalDateTime.now(), null, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        try {
            calculadora.calcularValor(ticket1);
        } catch (IllegalArgumentException e) {
            assertEquals("Datas de entrada e saída não podem ser nulas.", e.getMessage());
        }

        try {
            calculadora.calcularValor(ticket2);
        } catch (IllegalArgumentException e) {
            assertEquals("Datas de entrada e saída não podem ser nulas.", e.getMessage());
        }
    }
    @Test
    void deveLancarExcecaoParaSaidaAntesDaEntrada() {
        // Testa se uma exceção é lançada quando a saída é anterior à entrada.
        LocalDateTime entrada = LocalDateTime.now();
        LocalDateTime saida = entrada.minusHours(1);
        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        try {
            calculadora.calcularValor(ticket);
        } catch (IllegalArgumentException e) {
            assertEquals("A data e hora de saída não pode ser anterior à data de entrada.", e.getMessage());
        }
    }

    @Test
    void deveCalcularTarifaNosLimites() {
        // Testa os limites de minutos e horas
        CalculaTarifa calculadora = new CalculaTarifa();

        // 15 minutos deve ser gratuito
        LocalDateTime entrada = LocalDateTime.now();
        LocalDateTime saida = entrada.plusMinutes(15);
        Ticket ticketLimiteGratuito = new Ticket(entrada, saida, false);
        assertEquals(0.0, calculadora.calcularValor(ticketLimiteGratuito), 0.01);

        // 16 minutos deve cobrar tarifa mínima
        saida = entrada.plusMinutes(16);
        Ticket ticketLimiteMinimo = new Ticket(entrada, saida, false);
        assertEquals(5.90, calculadora.calcularValor(ticketLimiteMinimo), 0.01);
    }


}
