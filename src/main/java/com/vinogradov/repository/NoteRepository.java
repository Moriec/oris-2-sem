package com.vinogradov.repository;

import com.vinogradov.model.Note;
import com.vinogradov.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("select n from Note n where n.author = :author")
    List<Note> findByAuthor(@Param("author") User author);

    @Query("select n from Note n where n.isPublic = true")
    List<Note> findByIsPublicTrue();
}
