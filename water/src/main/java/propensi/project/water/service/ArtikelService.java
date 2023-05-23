package propensi.project.water.service;

import propensi.project.water.model.artikel.ArtikelModel;

import java.io.File;
import java.util.List;

public interface ArtikelService {
    public ArtikelModel addArtikel(ArtikelModel artikel);
    public List<ArtikelModel> getListArtikel();
    public ArtikelModel findByIdArtikel(String idArtikel);
    public void deleteArtikel(ArtikelModel artikel);
    public void deleteFolder(File folder);
    public ArtikelModel updateArtikel(ArtikelModel artikel);
    public boolean deleteFile(String directory, String filename);
}
