public class AuthenticationManager {
    private FileManager fileManager;
    
    public AuthenticationManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    public Client validateClient(String clientId) {
        return fileManager.findClientById(clientId);
    }
}