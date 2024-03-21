package be.technobel.ylorth.reservastock_rest.pl.rest;

import be.technobel.ylorth.reservastock_rest.bll.exception.NotFoundException;
import be.technobel.ylorth.reservastock_rest.dal.models.FileEntity;
import be.technobel.ylorth.reservastock_rest.dal.repository.FileRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final FileRepository fileRepository;

    public FileUploadController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile fichier) {
        try {

            FileEntity fileEntity = new FileEntity();
            fileEntity.setNom(fichier.getOriginalFilename());
            fileEntity.setContenu(fichier.getBytes());

            fileRepository.save(fileEntity);

            System.out.println("Téléchargement et enregistrement réussis");

            return ResponseEntity.ok("Téléchargement et enregistrement réussis");
        } catch (IOException e) {
            return new ResponseEntity<>("Échec du téléchargement ou de l'enregistrement", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        Optional<FileEntity> fichierOptional = fileRepository.findById(fileId);

        if (fichierOptional.isPresent()) {
            FileEntity fichier = fichierOptional.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", fichier.getNom());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fichier.getContenu());
        } else {
            throw new NotFoundException("File not found");
        }
    }
}
