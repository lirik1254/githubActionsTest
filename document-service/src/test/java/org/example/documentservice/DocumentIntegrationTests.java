package org.example.documentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.documentservice.controllers.DocumentController;
import org.example.documentservice.models.Documents;
import org.example.documentservice.repos.DocumentRepository;
import org.example.documentservice.services.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    @MockBean
    private DocumentRepository documentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Documents document;

    @BeforeEach
    public void setup() {
        document = new Documents();
        document.setDocumentId(1);
        document.setTitle("Test Document");
        document.setFileUrl("http://example.com/file");
        document.setCategory("General");
        document.setUploadDate(LocalDateTime.now());
        document.setAuthorId(1);
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        when(documentRepository.findAll()).thenReturn(List.of(document));

        mockMvc.perform(get("/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Document"));
    }

    @Test
    public void testGetDocumentById() throws Exception {
        when(documentRepository.findById(1)).thenReturn(Optional.of(document));

        mockMvc.perform(get("/documents/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Document"))
                .andExpect(jsonPath("$.fileUrl").value("http://example.com/file"));
    }

    @Test
    public void testGetDocumentById_NotFound() throws Exception {
        when(documentRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/documents/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateDocument() throws Exception {
        Documents newDocument = new Documents();
        newDocument.setTitle("New Document");
        newDocument.setFileUrl("http://example.com/newfile");
        newDocument.setCategory("Category");
        newDocument.setUploadDate(LocalDateTime.now());
        newDocument.setAuthorId(2);

        when(documentRepository.save(any(Documents.class))).thenReturn(newDocument);

        mockMvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDocument)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Document"))
                .andExpect(jsonPath("$.fileUrl").value("http://example.com/newfile"));
    }

    @Test
    public void testUpdateDocument() throws Exception {
        Documents updatedDocument = new Documents();
        updatedDocument.setTitle("Updated Document");
        updatedDocument.setFileUrl("http://example.com/updatedfile");
        updatedDocument.setCategory("Updated Category");
        updatedDocument.setUploadDate(LocalDateTime.now());
        updatedDocument.setAuthorId(2);

        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(Documents.class))).thenReturn(updatedDocument);

        mockMvc.perform(put("/documents/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDocument)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Document"))
                .andExpect(jsonPath("$.fileUrl").value("http://example.com/updatedfile"));
    }

    @Test
    public void testUpdateDocument_NotFound() throws Exception {
        Documents updatedDocument = new Documents();
        updatedDocument.setTitle("Updated Document");
        updatedDocument.setFileUrl("http://example.com/updatedfile");
        updatedDocument.setCategory("Updated Category");
        updatedDocument.setUploadDate(LocalDateTime.now());
        updatedDocument.setAuthorId(2);

        when(documentRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/documents/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDocument)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteDocument() throws Exception {
        when(documentRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/documents/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Document deleted successfully."));
    }

    @Test
    public void testDeleteDocument_NotFound() throws Exception {
        when(documentRepository.existsById(1)).thenReturn(false);

        mockMvc.perform(delete("/documents/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Document not found with id: 1"));
    }

    @Test
    public void testGetAllDocuments_Empty() throws Exception {
        when(documentRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
