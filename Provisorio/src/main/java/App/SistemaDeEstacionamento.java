package App;

import Entity.Ticket;
import Service.CalculoValor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SistemaDeEstacionamento {
    private static final DateTimeFormatter FORMATADOR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        CalculoValor calculoValor = new CalculoValor();

        while (true) {
            System.out.println("\n=== Sistema de Controle de Estacionamento ===");
            System.out.println("1. Inserir Ticket de Estacionamento");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = entrada.nextInt();
            entrada.nextLine();

            if (opcao == 1) {
                Ticket ticket = coletaTicket(entrada);
                double valor = calculoValor.valorTicket(ticket);
                System.out.printf("\n Valor a ser pago: R$%.2f%n", valor);

            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }

            if (opcao == 2) {
                System.out.println("Encerrando o sistema...");
                break;
            }
        }
        entrada.close();

    }

    private static Ticket coletaTicket(Scanner scanner) {
        System.out.println("-------- DADOS DE ENTRADA ------");
        System.out.print("Digite a data de entrada (dd/MM/yyyy): ");
        String dataEntradaStr = scanner.nextLine();
        System.out.print("Digite a hora de entrada (HH:mm): ");
        String horaEntradaStr = scanner.nextLine();
        LocalDateTime entrada = LocalDateTime.parse(dataEntradaStr + " " + horaEntradaStr, FORMATADOR_DATA_HORA);


        System.out.println("-------- DADOS DE SAÍDA ------");
        System.out.print("Digite a data de saída (dd/MM/yyyy): ");
        String dataSaidaStr = scanner.nextLine();
        System.out.print("Digite a hora de saída (HH:mm): ");
        String horaSaidaStr = scanner.nextLine();
        LocalDateTime saida = LocalDateTime.parse(dataSaidaStr + " " + horaSaidaStr, FORMATADOR_DATA_HORA);


        System.out.print("O cliente é VIP? (s/n): ");
        boolean clienteVip = scanner.nextLine().equalsIgnoreCase("s");

        return new Ticket(entrada, saida, clienteVip);
    }
}
