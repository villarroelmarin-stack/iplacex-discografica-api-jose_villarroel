package org.iplacex.proyectos.discografia.discos;

import java.util.List;
import java.util.Optional;

import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DiscoController {

    @Autowired
    private IDiscoRepository discoRepository;

    @Autowired
    private IArtistaRepository artistaRepository;

    @PostMapping(
        value = "/disco",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandlePostDiscoRequest(
            @RequestBody Disco disco) {

        try {
            if (!artistaRepository.existsById(disco.idArtista)) {
                return new ResponseEntity<>(
                    "El artista indicado no existe",
                    HttpStatus.NOT_FOUND
                );
            }

            Disco creado = discoRepository.insert(disco);

            return new ResponseEntity<>(
                creado,
                HttpStatus.CREATED
            );

        } catch (Exception e) {
            return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(
        value = "/discos",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Disco>> HandleGetDiscosRequest() {

        List<Disco> discos = discoRepository.findAll();

        return new ResponseEntity<>(
            discos,
            HttpStatus.OK
        );
    }

    @GetMapping(
        value = "/disco/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetDiscoRequest(
            @PathVariable("id") String id) {

        Optional<Disco> disco = discoRepository.findById(id);

        if (!disco.isPresent()) {
            return new ResponseEntity<>(
                "Disco no encontrado",
                HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
            disco.get(),
            HttpStatus.OK
        );
    }

    @GetMapping(
        value = "/artista/{id}/discos",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Disco>> HandleGetDiscosByArtistaRequest(
            @PathVariable("id") String id) {

        List<Disco> discos =
            discoRepository.findDiscosByIdArtista(id);

        return new ResponseEntity<>(
            discos,
            HttpStatus.OK
        );
    }
}