package servicos;

import entidades.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;

public class Database {

    private final String DATA_EXT = ".data";
    private final String DB_LOC = "C:\\TrabalhoCartao\\Users\\";

    public Database() {
        checkFolder();
    }

    public ArrayList<Usuario> get() throws IOException {

        List<File> filesInFolder = Files.walk(Paths.get(DB_LOC))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        
        ArrayList<Usuario> usuarios = new ArrayList<>();

        for (File file : filesInFolder) {
            if(getFileExtension(file).equals(DATA_EXT)) {
                
                try {
                    FileInputStream fi = new FileInputStream(file);
                    ObjectInputStream oi = new ObjectInputStream(fi);
                    Usuario usuario = (Usuario) oi.readObject();
                    
                    usuarios.add(usuario);
                } catch (Exception e) {
                    Dialog.Dialog(Alert.AlertType.ERROR, "Erro", "Não foi possivel pegar usuarios!!");
                }  
            }
        }
        
        if(!usuarios.isEmpty()) {
            return usuarios;
        }

        return null;
    }

    public Usuario getByCpf(String cpf) throws FileNotFoundException, IOException, ClassNotFoundException {
        if (checkUserSave(cpf)) {
            File file = new File(DB_LOC + cpf + DATA_EXT);
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            Usuario usuario = (Usuario) oi.readObject();
            return usuario;
        }
        return null;
    }

    private void push(Usuario user) {

    }

    public void post(Usuario user) throws FileNotFoundException, IOException {
        try (FileOutputStream fout = new FileOutputStream(DB_LOC + user.getCPF() + DATA_EXT); ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(user);
        } catch (Exception e) {
            Dialog.Dialog(Alert.AlertType.ERROR, "Erro", "Não foi possivel salvar o usaurio!");
        }
    }

    // EXTRAS -----------------------------------------
    public boolean checkUserSave(String cpf) {
        File dir = new File(DB_LOC + cpf + DATA_EXT);
        return dir.exists();
    }

    private void checkFolder() {
        File dir = new File(DB_LOC);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

}
