package Service;

import Entity.Ticket;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertTrue;

public class CalculaTarifaTest {

    private final CalculoValor calculadora = new CalculoValor();

    @Test
    void calcularTarifaCom15MinutosDeCortesia() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 15);
        Ticket ticket = new Ticket(entrada, saida, false);
        assertEquals(0.0, calculadora.valorTicket(ticket));
    }

    @Test
    void calcularTarifaNormalAteUmaHora() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 30);
        Ticket ticket = new Ticket(entrada, saida, false);
        assertEquals(5.90, calculadora.valorTicket(ticket));
    }

    @Test
    void calcularTarifaParaUmaHora() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 10, 0);
        Ticket ticket = new Ticket(entrada, saida, false);
        assertEquals(5.90, calculadora.valorTicket(ticket));
    }

    @Test
    void calcularTarifaParaPernoite() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 22, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 8, 9, 0);
        Ticket ticket = new Ticket(entrada, saida, false);
        assertEquals(50.0, calculadora.valorTicket(ticket));
    }

    @Test
    void calcularTarifaParaMultiplosDiasPernoite() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 22, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 10, 9, 0);
        Ticket ticket = new Ticket(entrada, saida, false);
        assertEquals(150.0, calculadora.valorTicket(ticket));
    }

    @Test
    void aplicarDescontoVipParaTarifaNormal() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 11, 0);
        Ticket ticket = new Ticket(entrada, saida, true);
        assertEquals(4.20, calculadora.valorTicket(ticket));
    }

    @Test
    void aplicarDescontoVipParaTarifaDePernoite() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 22, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 8, 9, 0);
        Ticket ticket = new Ticket(entrada, saida, true);
        assertEquals(25.0, calculadora.valorTicket(ticket));
    }

    @Test
    void calcularTarifaRapidamente() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 10, 0);
        Ticket ticket = new Ticket(entrada, saida, false);

        long startTime = System.nanoTime();
        calculadora.valorTicket(ticket);
        long duration = System.nanoTime() - startTime;

        assertTrue(duration < 1000000);
    }

    @Test
    void calcularTarifaNosLimites() {
        LocalDateTime entrada = LocalDateTime.now();
        LocalDateTime saida = entrada.plusMinutes(15);
        Ticket ticketLimiteGratuito = new Ticket(entrada, saida, false);
        assertEquals(0.0, calculadora.valorTicket(ticketLimiteGratuito), 0.01);

        saida = entrada.plusMinutes(16);
        Ticket ticketLimiteMinimo = new Ticket(entrada, saida, false);
        assertEquals(5.90, calculadora.valorTicket(ticketLimiteMinimo), 0.01);
    }

    @Test
    void lancarExcecaoParaDataEntradaPosteriorSaida() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 8, 12, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 8, 10, 0);
        Ticket ticket = new Ticket(entrada, saida, false);
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.valorTicket(ticket);
        });
    }

    @Test
    void lancarExcecaoParaTicketNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.valorTicket(null);
        });
    }

    @Test
    void lancarExcecaoParaDataEntradaNula() {
        LocalDateTime saida = LocalDateTime.of(2024, 10, 8, 12, 0);
        Ticket ticket = new Ticket(null, saida, false);
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.valorTicket(ticket);
        });
    }

    @Test
    void lancarExcecaoParaDataSaidaNula() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 8, 12, 0);
        Ticket ticket = new Ticket(entrada, null, false);
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.valorTicket(ticket);
        });
    }

    @Test
    void lancarExcecaoParaDataSaidaAnteriorEntrada() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 8, 12, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 8, 10, 0);
        Ticket ticket = new Ticket(entrada, saida, false);
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.valorTicket(ticket);
        });
    }

    @Test
    void calcularTarifaCorretamenteParaDiversasParticoes() {
        // Período gratuito
        LocalDateTime entrada1 = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida1 = entrada1.plusMinutes(10);
        Ticket ticket1 = new Ticket(entrada1, saida1, false);
        assertEquals(0.0, calculadora.valorTicket(ticket1));

        // Tarifa normal
        LocalDateTime entrada2 = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida2 = entrada2.plusMinutes(30);
        Ticket ticket2 = new Ticket(entrada2, saida2, false);
        assertEquals(5.90, calculadora.valorTicket(ticket2));

        // Tarifa de pernoite
        LocalDateTime entrada3 = LocalDateTime.of(2024, 10, 7, 22, 0);
        LocalDateTime saida3 = entrada3.plusHours(11);
        Ticket ticket3 = new Ticket(entrada3, saida3, false);
        assertEquals(50.0, calculadora.valorTicket(ticket3));

        //  Ticket nulo
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.valorTicket(null);
        });

        //  Data de entrada posterior à data de saída
        LocalDateTime entrada4 = LocalDateTime.of(2024, 10, 8, 12, 0);
        LocalDateTime saida4 = LocalDateTime.of(2024, 10, 8, 10, 0);
        Ticket ticket4 = new Ticket(entrada4, saida4, false);
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.valorTicket(ticket4);
        });
    }

    @Test
    void calcularTarifaComValoresAleatorios() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            LocalDateTime entrada = LocalDateTime.now().minusHours(random.nextInt(24)).minusMinutes(random.nextInt(60));
            LocalDateTime saida = entrada.plusHours(random.nextInt(24)).plusMinutes(random.nextInt(60));
            Ticket ticket = new Ticket(entrada, saida, random.nextBoolean());

            double expectedValue = calculadora.valorTicket(ticket);
            assertEquals(expectedValue, calculadora.valorTicket(ticket), 0.01);
        }
    }


}
