package com.vanguard.service;

import com.vanguard.exception.FileIOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("csv_files");

    public String store(MultipartFile file) {
        try {
            var locaiton = rootLocation.resolve(UUID.randomUUID().toString());
            Files.createDirectories(locaiton);
            Path destinationFile = locaiton.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.toString();
        } catch (IOException e) {
            throw new FileIOException("Failed to store file on file system.");
        }
    }
}
