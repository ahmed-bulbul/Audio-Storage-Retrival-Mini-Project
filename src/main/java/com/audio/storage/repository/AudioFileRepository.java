package com.audio.storage.repository;

import com.audio.storage.entity.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {

    @Query("SELECT a FROM AudioFile a WHERE a.user.id = :userId AND a.phrase.id = :phraseId")
    Optional<AudioFile> findByUserIdAndPhraseId(@Param("userId") Long userId, @Param("phraseId") Long phraseId);

}
