import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Sistema {
    private List<usuario> usuarios = new ArrayList<>();
    private List<Evento> eventos = new ArrayList<>();
    private Map<usuario, List<Evento>> participacoes = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    // === MENU PRINCIPAL ===
    public void menuPrincipal() {
        int opcao;
        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Cadastrar evento");
            System.out.println("3 - Listar eventos");
            System.out.println("4 - Confirmar participação");
            System.out.println("5 - Cancelar participação");
            System.out.println("6 - Meus eventos");
            System.out.println("7 - Sair");
            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer

            switch(opcao) {
                case 1 -> cadastrarUsuario();
                case 2 -> cadastrarEvento();
                case 3 -> listarEventos();
                case 4 -> confirmarParticipacao();
                case 5 -> cancelarParticipacao();
                case 6 -> verMeusEventos();
                case 7 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while(opcao != 7);
    }

    // === CADASTRAR USUÁRIO ===
    private void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Cidade: ");
        String cidade = sc.nextLine();

        usuario u = new usuario(nome, email, cidade);
        usuarios.add(u);
        participacoes.put(u, new ArrayList<>()); // inicia lista vazia de eventos
        System.out.println("Usuário cadastrado com sucesso!");
    }

    // === CADASTRAR EVENTO ===
    private void cadastrarEvento() {
        System.out.print("Nome do evento: ");
        String nome = sc.nextLine();
        System.out.print("Endereço: ");
        String endereco = sc.nextLine();
        System.out.print("Categoria (Show/Festa/Esporte): ");
        String categoria = sc.nextLine();
        System.out.print("Data e hora (dd/MM/yyyy HH:mm): ");
        String dataHora = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime horario = LocalDateTime.parse(dataHora, fmt);

        Evento e = new Evento(nome, endereco, categoria, horario, descricao);
        eventos.add(e);
        System.out.println("Evento cadastrado!");
    }

    // === LISTAR EVENTOS ORDENADOS POR DATA ===
    private void listarEventos() {
        if(eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado!");
            return;
        }

        eventos.sort(Comparator.comparing(Evento::getHorario));
        System.out.println("\n===== LISTA DE EVENTOS =====");
        for(int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            System.out.println((i+1) + " - " + e + " | Status: " + statusEvento(e));
        }
    }

    // === CONFIRMAR PARTICIPAÇÃO ===
    private void confirmarParticipacao() {
        if(usuarios.isEmpty() || eventos.isEmpty()) {
            System.out.println("Cadastre usuários e eventos antes!");
            return;
        }

        usuario usuario = escolherusuario();
        listarEventos();

        System.out.print("Digite o número do evento que deseja participar: ");
        int idx = sc.nextInt();
        sc.nextLine();

        if(idx < 1 || idx > eventos.size()) {
            System.out.println("Evento inválido!");
            return;
        }

        Evento e = eventos.get(idx-1);
        participacoes.get(usuario).add(e);
        System.out.println("Participação confirmada no evento: " + e.getNome());
    }

    // === CANCELAR PARTICIPAÇÃO ===
    private void cancelarParticipacao() {
        if(usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado!");
            return;
        }

        usuario usuario = escolherusuario();
        List<Evento> meusEventos = participacoes.get(usuario);

        if(meusEventos.isEmpty()) {
            System.out.println("Você não está participando de nenhum evento!");
            return;
        }

        System.out.println("\n===== MEUS EVENTOS =====");
        for(int i = 0; i < meusEventos.size(); i++) {
            System.out.println((i+1) + " - " + meusEventos.get(i));
        }

        System.out.print("Digite o número do evento para cancelar: ");
        int idx = sc.nextInt();
        sc.nextLine();

        if(idx < 1 || idx > meusEventos.size()) {
            System.out.println("Evento inválido!");
            return;
        }

        Evento cancelado = meusEventos.remove(idx-1);
        System.out.println("Você cancelou a participação em: " + cancelado.getNome());
    }

    private usuario escolherusuario() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'escolherusuario'");
    }

    // === VER EVENTOS DO USUÁRIO ===
    private void verMeusEventos() {
        if(usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado!");
            return;
        }

        usuario usuario = escolherusuario();
        List<Evento> meusEventos = participacoes.get(usuario);

        if(meusEventos.isEmpty()) {
            System.out.println("Você não confirmou presença em nenhum evento!");
            return;
        }

        System.out.println("\n===== MEUS EVENTOS =====");
        for(Evento e : meusEventos) {
            System.out.println(e + " | Status: " + statusEvento(e));
        }
    }

    // === ESCOLHER USUÁRIO ===
    private usuario escolherUsuario() {
        System.out.println("\n===== USUÁRIOS =====");
        for(int i = 0; i < usuarios.size(); i++) {
            System.out.println((i+1) + " - " + usuarios.get(i));
        }
        System.out.print("Escolha o usuário: ");
        int idx = sc.nextInt();
        sc.nextLine();
        return usuarios.get(idx-1);
    }

    // === STATUS DO EVENTO ===
    private String statusEvento(Evento e) {
        LocalDateTime agora = LocalDateTime.now();
        if(e.getHorario().isBefore(agora.minusMinutes(1))) {
            return "Já ocorreu";
        } else if(e.getHorario().isAfter(agora.plusMinutes(1))) {
            return "Ainda vai acontecer";
        } else {
            return "Acontecendo agora";
        }
    }

    // === SALVAR EVENTOS NO ARQUIVO ===
    public void salvarEventos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("events.data"))) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for(Evento e : eventos) {
                bw.write(e.getNome() + ";" + e.getEndereco() + ";" + e.getCategoria() + ";" +
                         e.getHorario().format(fmt) + ";" + e.getDescricao());
                bw.newLine();
            }
        } catch(IOException ex) {
            System.out.println("Erro ao salvar eventos: " + ex.getMessage());
        }
    }

    // === CARREGAR EVENTOS DO ARQUIVO ===
    public void carregarEventos() {
        try (BufferedReader br = new BufferedReader(new FileReader("events.data"))) {
            String linha;
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            while((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                LocalDateTime horario = LocalDateTime.parse(partes[3], fmt);
                Evento e = new Evento(partes[0], partes[1], partes[2], horario, partes[4]);
                eventos.add(e);
            }
        } catch(FileNotFoundException e) {
            System.out.println("Arquivo de eventos não encontrado, iniciando vazio...");
        } catch(IOException ex) {
            System.out.println("Erro ao carregar eventos: " + ex.getMessage());
        }
    }
}
