public class main {
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        sistema.carregarEventos(); // Lê do arquivo
        sistema.menuPrincipal();   // Executa o menu
        sistema.salvarEventos();   // Salva no arquivo ao sair
    }
}

