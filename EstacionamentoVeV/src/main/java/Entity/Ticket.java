package Entity;

import java.time.LocalDateTime;

public class Ticket {
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private boolean vip;

    public Ticket(LocalDateTime dataHoraEntrada, LocalDateTime dataHoraSaida, boolean clienteVip) {
        this.entrada = dataHoraEntrada;
        this.saida = dataHoraSaida;
        this.vip = clienteVip;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public boolean isVip() {
        return vip;
    }
}
