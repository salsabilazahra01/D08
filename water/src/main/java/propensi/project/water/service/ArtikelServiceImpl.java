package propensi.project.water.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import propensi.project.water.model.artikel.ArtikelModel;
import propensi.project.water.repository.Artikel.ArtikelDb;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class ArtikelServiceImpl implements ArtikelService{

    @Autowired
    ArtikelDb artikelDb;

    @Override
    public ArtikelModel addArtikel(ArtikelModel artikel){
        return artikelDb.save(artikel);
    }

    @Override
    public List<ArtikelModel> getListArtikel(){
        return artikelDb.findAll();
    }

    @Override
    public ArtikelModel findByIdArtikel(String idArtikel) { return artikelDb.findById(idArtikel).orElse(null);}

    @Override
    public void deleteArtikel(ArtikelModel artikel){
        artikelDb.delete(artikel);
    }

    @Override
    public void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        folder.delete();
    }
    @Override
    public boolean deleteFile(String directory, String filename) {
        try {
            Path root = Paths.get(directory);
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public ArtikelModel updateArtikel(ArtikelModel artikel){
        return artikelDb.save(artikel);
    }


}
