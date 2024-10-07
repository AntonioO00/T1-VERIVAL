package Aplication;


import Entity.Ticket;
import Service.CalculaTarifa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Estacionamento {
    private static final DateTimeFormatter FORMATADOR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CalculaTarifa calculadora = new CalculaTarifa();

        while (true) {
            System.out.println("\n=== Sistema de Controle de Estacionamento ===");
            System.out.println("1. Inserir Ticket de Estacionamento");
            System.out.println("2. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            if (opcao == 2) {
                System.out.println("Encerrando o sistema...");
                break;
            }

            if (opcao == 1) {
                Ticket ticket = coletarDadosTicket(scanner);
                double valor = calculadora.calcularValor(ticket);
                System.out.printf("Valor a ser pago: R$%.2f%n", valor);
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }

    private static Ticket coletarDadosTicket(Scanner scanner) {
        System.out.print("Digite a data e hora de entrada (dd/MM/yyyy HH:mm): ");
        String entradaStr = scanner.nextLine();
        LocalDateTime entrada = LocalDateTime.parse(entradaStr, FORMATADOR_DATA_HORA);

        System.out.print("Digite a data e hora de saída (dd/MM/yyyy HH:mm): ");
        String saidaStr = scanner.nextLine();
        LocalDateTime saida = LocalDateTime.parse(saidaStr, FORMATADOR_DATA_HORA);

        System.out.print("O cliente é VIP? (s/n): ");
        boolean clienteVip = scanner.nextLine().equalsIgnoreCase("s");

        return new Ticket(entrada, saida, clienteVip);
    }
}
