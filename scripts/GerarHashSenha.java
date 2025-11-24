import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilitário para gerar hash BCrypt de senhas
 * 
 * Este utilitário demonstra que o sistema usa hash criptográfico (BCrypt)
 * conforme requisito RNF01 do PDF do projeto.
 * 
 * Uso:
 * 1. Compile: javac -cp "target/classes;~/.m2/repository/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar" GerarHashSenha.java
 * 2. Execute: java -cp ".;target/classes;~/.m2/repository/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar" GerarHashSenha
 * 
 * Ou via Maven:
 * mvn compile exec:java -Dexec.mainClass="GerarHashSenha"
 */
public class GerarHashSenha {
    public static void main(String[] args) {
        // Senha padrão para demonstração
        String senha = "admin123";
        
        // Gerar hash BCrypt
        String hash = BCrypt.hashpw(senha, BCrypt.gensalt());
        
        System.out.println("==========================================");
        System.out.println("Gerador de Hash BCrypt - SOS-Rota");
        System.out.println("==========================================");
        System.out.println("Senha: " + senha);
        System.out.println("Hash BCrypt: " + hash);
        System.out.println("\nUse este hash no SQL:");
        System.out.println("INSERT INTO usuarios (login, senha_hash, perfil) VALUES ('seu_login', '" + hash + "', 'REGULADOR');");
        System.out.println("\n==========================================");
        System.out.println("NOTA: O sistema já gera hash automaticamente");
        System.out.println("ao cadastrar usuários pela tela de cadastro.");
        System.out.println("Este utilitário é apenas para referência.");
        System.out.println("==========================================");
    }
}

