package com.vanguard;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CsvFileImportEvent extends ApplicationEvent {
    private final String filePath;
    private final Long fileId;

    public CsvFileImportEvent(Object source, Long fileId, String filePath) {
        super(source);
        this.fileId = fileId;
        this.filePath = filePath;
    }
}
