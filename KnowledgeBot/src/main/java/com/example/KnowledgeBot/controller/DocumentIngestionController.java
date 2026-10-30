package com.example.KnowledgeBot.controller;

import com.example.KnowledgeBot.service.DocumentIngestionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class DocumentIngestionController {

    private DocumentIngestionServiceImpl documentIngestionService;

    public DocumentIngestionController(DocumentIngestionServiceImpl documentIngestionService) {
        this.documentIngestionService = documentIngestionService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest(@RequestParam("file") MultipartFile file){
        documentIngestionService.ingest(file);
        return ResponseEntity.ok("Document Uploaded Successfully");
    }
}
