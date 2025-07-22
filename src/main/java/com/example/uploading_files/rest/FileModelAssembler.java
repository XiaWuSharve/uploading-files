package com.example.uploading_files.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.uploading_files.FileUploaderController;
import com.example.uploading_files.dto.FilenameDTO;

@Component
public class FileModelAssembler implements RepresentationModelAssembler<FilenameDTO, EntityModel<FilenameDTO>> {

    @Override
    public EntityModel<FilenameDTO> toModel(FilenameDTO filename) {
        return EntityModel.of(filename,
                linkTo(methodOn(FileUploaderController.class).serveFile(filename.getFilename())).withSelfRel(),
                linkTo(methodOn(FileUploaderController.class).listUploadedFiles()).withRel("files"));
    }
}
