package fileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {
    public enum Extension {
        FILE("F"),
        DIRECTORY("D");

        private String name;

        public String getName() {
            return name;
        }

        Extension(String name) {
            this.name = name;
        }
    }

    private String fileName;
    private Extension extension;
    private long size;
    private LocalDateTime lastModified;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public FileInfo(Path path) {
        try {
            this.fileName = path.getFileName().toString();
            this.size = Files.size(path);
            this.extension = Files.isDirectory(path) ? Extension.DIRECTORY : Extension.FILE;
            if(this.extension == Extension.DIRECTORY) {
                size = -1L;
            }
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));
        } catch (IOException e) {
            throw new RuntimeException("Unable to collect file info from this path");
        }
    }
}
