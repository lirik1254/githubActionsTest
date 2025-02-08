package org.example.documentservice.controllers;


import org.example.documentservice.models.Documents;
import org.example.documentservice.services.DocumentService;
import org.example.documentservice.utils.DocumentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/documents")
    public List<Documents> test() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/test")
    public String getAllDocuments() {
        return "test";
    }

    @GetMapping("documents/{id}")
    public Documents getDocumentById(@PathVariable Integer id) {
        return documentService.getDocumentById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));
    }

    @PostMapping("/documents")
    public ResponseEntity<Documents> createDocument(@RequestBody Documents document) {
        Documents createdDocument = documentService.createDocument(document);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
    }

    @PutMapping("/documents/{id}")
    public ResponseEntity<Documents> updateDocument(@PathVariable Integer id, @RequestBody Documents document) {
        Documents updatedDocument = documentService.updateDocument(id, document);
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Integer id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.status(HttpStatus.OK).body("Document deleted successfully.");
        } catch (DocumentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found with id: " + id);
        }
    }
}