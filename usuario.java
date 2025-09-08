public class usuario {
    public usuario (String nome, String cidade, String email) {
        //TODO Auto-generated constructor stub
    }

    public class Usuario {
    private String nome;
    private String email;
    private String cidade;

    public Usuario(String nome, String email, String cidade) {
        this.nome = nome;
        this.email = email;
        this.cidade = cidade;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCidade() { return cidade; }

    @Override
    public String toString() {
        return "Usuário: " + nome + " | Email: " + email + " | Cidade: " + cidade;
    }
}




}