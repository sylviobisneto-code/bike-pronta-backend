
        package org.example.bikeprontabackend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/uploads")
public class CloudinaryUploadController {

    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024;

    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp"
    );

    private final Cloudinary cloudinary;

    public CloudinaryUploadController() {
        String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
        String apiKey = System.getenv("CLOUDINARY_API_KEY");
        String apiSecret = System.getenv("CLOUDINARY_API_SECRET");

        if (cloudName == null || cloudName.isBlank()
                || apiKey == null || apiKey.isBlank()
                || apiSecret == null || apiSecret.isBlank()) {
            throw new IllegalStateException(
                    "Configure as variáveis de ambiente do Cloudinary."
            );
        }

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName.trim(),
                "api_key", apiKey.trim(),
                "api_secret", apiSecret.trim(),
                "secure", true
        ));
    }

    @PostMapping(
            value = "/imagem",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> enviarImagem(
            @RequestParam("arquivo") MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione uma imagem."
            );
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "A imagem deve ter no máximo 5 MB."
            );
        }

        String tipo = arquivo.getContentType();

        if (tipo == null || !TIPOS_PERMITIDOS.contains(tipo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Formato não permitido. Use JPG, PNG ou WebP."
            );
        }

        try {
            Map<?, ?> resultado = cloudinary.uploader().upload(
                    arquivo.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "bike-pronta/bicicletas",
                            "resource_type", "image"
                    )
            );

            String url = (String) resultado.get("secure_url");

            if (url == null || url.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "O Cloudinary não retornou a URL da imagem."
                );
            }

            System.out.println("Upload realizado com sucesso.");

            return ResponseEntity.ok(Map.of("url", url));

        } catch (ResponseStatusException e) {
            throw e;

        } catch (Exception e) {
            // Exibe a causa real no console do IntelliJ.
            System.err.println("ERRO AO ENVIAR IMAGEM AO CLOUDINARY:");
            e.printStackTrace();

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Falha no Cloudinary: "
                            + (e.getMessage() == null
                            ? e.getClass().getSimpleName()
                            : e.getMessage())
            );
        }
    }
}
