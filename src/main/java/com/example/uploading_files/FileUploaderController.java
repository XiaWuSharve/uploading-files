package com.example.uploading_files;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uploading_files.dto.FilenameDTO;
import com.example.uploading_files.exception.StorageFileNotFoundException;
import com.example.uploading_files.rest.FileModelAssembler;
import com.example.uploading_files.storage.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class FileUploaderController {
    private final StorageService storageService;
    private final FileModelAssembler fileModelAssembler;

    public FileUploaderController(StorageService storageService, FileModelAssembler fileModelAssembler) {
        this.storageService = storageService;
        this.fileModelAssembler = fileModelAssembler;
    }

    @GetMapping("/files")
    public CollectionModel<EntityModel<FilenameDTO>> listUploadedFiles() {
        List<EntityModel<FilenameDTO>> files = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            return fileModelAssembler.toModel(new FilenameDTO(filename));
        }).collect(Collectors.toList());
        return CollectionModel.of(files, linkTo(methodOn(FileUploaderController.class).listUploadedFiles()).withSelfRel()); 
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null) {
            throw new StorageFileNotFoundException("File not found: " + filename);
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files")
    public ResponseEntity<?> handleFileUpload(@RequestParam MultipartFile file) {
        Path storedPath = storageService.store(file);
        EntityModel<FilenameDTO> model = fileModelAssembler.toModel(new FilenameDTO(storedPath.getFileName().toString()));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
    
}
