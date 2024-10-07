package Service;
import Entity.Ticket;
import Service.CalculaTarifa;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculaTarifaTest {

    @Test
    void deveCalcularTarifaCom15MinutosDeCortesia() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 10);

        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        assertEquals(0.0, calculadora.calcularValor(ticket));
    }

    @Test
    void deveCalcularTarifaNormalAteUmaHora() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 30);

        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        assertEquals(5.90, calculadora.calcularValor(ticket));
    }

    @Test
    void deveCalcularTarifaParaUmPeriodoDeUmaHora() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 10, 0);

        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        assertEquals(5.90, calculadora.calcularValor(ticket));
    }

    @Test
    void deveCalcularTarifaParaMaisDeUmaHora() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 11, 0);

        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        assertEquals(11.80, calculadora.calcularValor(ticket)); // Exemplo de tarifa que você deve ajustar
    }

    @Test
    void deveCalcularTarifaComClienteVIP() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 9, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 10, 0);

        Ticket ticket = new Ticket(entrada, saida, true); // Cliente VIP
        CalculaTarifa calculadora = new CalculaTarifa();

        assertEquals(4.50, calculadora.calcularValor(ticket)); // Exemplo de tarifa reduzida para VIP
    }

    @Test
    void deveLancarErroParaDataDeSaidaAnteriorADataDeEntrada() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 7, 10, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 0);

        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadora.calcularValor(ticket);
        });

        assertEquals("A data de saída deve ser posterior à data de entrada.", exception.getMessage());
    }

    @Test
    void deveCalcularTarifaParaPernoite() {
        LocalDateTime entrada = LocalDateTime.of(2024, 10, 6, 22, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 10, 7, 9, 30);

        Ticket ticket = new Ticket(entrada, saida, false);
        CalculaTarifa calculadora = new CalculaTarifa();

        assertEquals(20.00, calculadora.calcularValor(ticket)); // Exemplo de tarifa para pernoite
    }

}
