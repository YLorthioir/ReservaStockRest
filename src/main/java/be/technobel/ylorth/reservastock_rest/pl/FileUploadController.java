package be.technobel.ylorth.reservastock_rest.pl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("name") String nom,
                                             @RequestPart("file") MultipartFile fichier) {
        System.out.println(nom);
        System.out.println("Entrée controller Ok");

        return new ResponseEntity<>("Téléchargement réussi", HttpStatus.OK);
    }
}
