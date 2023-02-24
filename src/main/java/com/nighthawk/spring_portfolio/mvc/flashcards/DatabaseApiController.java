package com.nighthawk.spring_portfolio.mvc.flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/db")
public class DatabaseApiController {

  @GetMapping("/sqlite.db")
  public ResponseEntity<Object> getDB () throws IOException {
    Resource coolDbResource = new FileSystemResource("volumes/sqlite.db");

    // Check if the file exists
    if (!coolDbResource.exists()) {
        throw new FileNotFoundException("sqlite.db file not found");
    }

    // Set the content type of the file
    String contentType = "application/octet-stream";

    return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + coolDbResource.getFilename() + "\"")
            .body(coolDbResource);
  }
}
