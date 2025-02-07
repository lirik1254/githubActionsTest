package org.example.documentservice.services;

import org.example.documentservice.models.Documents;
import org.example.documentservice.repos.DocumentRepository;
import org.example.documentservice.utils.DocumentCreationException;
import org.example.documentservice.utils.DocumentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Documents> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Documents createDocument(Documents document) {
        try {
            return documentRepository.save(document);
        } catch (Exception e) {
            throw new DocumentCreationException("Не удалось создать документ");
        }
    }

    public Optional<Documents> getDocumentById(Integer id) {
        return documentRepository.findById(id);
    }

    public Documents updateDocument(Integer id, Documents document) {
        Documents existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found with id: " + id));

        if (document.getTitle() != null) {
            existingDocument.setTitle(document.getTitle());
        }
        if (document.getFileUrl() != null) {
            existingDocument.setFileUrl(document.getFileUrl());
        }
        if (document.getCategory() != null) {
            existingDocument.setCategory(document.getCategory());
        }

        try {
            return documentRepository.save(existingDocument);
        } catch (Exception e) {
            throw new RuntimeException("Error updating document", e);  // Обработка ошибки сохранения
        }
    }

    public void deleteDocument(Integer id) {
        if (!documentRepository.existsById(id)) {
            throw new DocumentNotFoundException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }
}
