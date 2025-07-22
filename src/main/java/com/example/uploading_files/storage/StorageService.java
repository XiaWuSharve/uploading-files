package com.example.uploading_files.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();

    Path store(MultipartFile file);

    Path load(String filename);

    Stream<Path> loadAll();

    Resource loadAsResource(String filename);

    void deleteAll();
}

