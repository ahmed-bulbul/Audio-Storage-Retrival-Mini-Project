package com.audio.storage.repository;

import com.audio.storage.entity.AudioFile;
import com.audio.storage.entity.Phrase;
import com.audio.storage.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class AudioFileRepositoryTest {

    @Autowired
    private AudioFileRepository audioFileRepository;

    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Phrase testPhrase;

    @BeforeEach
    public void setup() {
        // Ensure there is at least one User and one Phrase in the database
        testUser = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No users found in the database."));
        testPhrase = phraseRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No phrases found in the database."));

        // Save a test AudioFile
        AudioFile audioFile = new AudioFile();
        audioFile.setFilePath("test.wav");
        audioFile.setUser(testUser); // Set managed user
        audioFile.setPhrase(testPhrase); // Set managed phrase
        audioFileRepository.save(audioFile);
    }

    @Test
    void testFindByUserIdAndPhraseId() {
        // Test method
        Optional<AudioFile> result = audioFileRepository.findByUserIdAndPhraseId(testUser.getId(), testPhrase.getId());

        // Verify
        assertThat(result)
                .as("AudioFile should be found for the given userId and phraseId")
                .isPresent()
                .get()
                .satisfies(audioFile -> {
                    assertThat(audioFile.getUser().getId()).isEqualTo(testUser.getId());
                    assertThat(audioFile.getPhrase().getId()).isEqualTo(testPhrase.getId());
                });
    }

    @Test
    void testFindByUserIdAndPhraseId_NotFound() {
        // Test with non-existing userId and phraseId
        Optional<AudioFile> result = audioFileRepository.findByUserIdAndPhraseId(99L, 99L);

        // Verify
        assertThat(result)
                .as("No AudioFile should be found for non-existing userId and phraseId")
                .isNotPresent();
    }

    @AfterEach
    public void cleanUp() {
        audioFileRepository.deleteAll();
    }
}
