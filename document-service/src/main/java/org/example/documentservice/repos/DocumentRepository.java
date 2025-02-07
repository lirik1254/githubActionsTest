package org.example.documentservice.repos;

import org.example.documentservice.models.Documents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Documents, Integer> {
}

