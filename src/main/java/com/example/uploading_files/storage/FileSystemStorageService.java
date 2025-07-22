package com.example.uploading_files.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.uploading_files.exception.StorageException;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(@Value("${upload.dir}") String uploadDir) {
        if (uploadDir.trim().length() == 0) {
            throw new StorageException("File upload directory cannot be empty");
        }

        this.rootLocation = Paths.get(uploadDir);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public Path store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Fail to store empty file");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return this.rootLocation.relativize(destinationFile);
        } catch (Exception e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Path load(String filename) {
        return this.rootLocation.resolve(filename);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
    }

}
