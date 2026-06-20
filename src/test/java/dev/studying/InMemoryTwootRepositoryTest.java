package dev.studying;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTwootRepositoryTest {
    private InMemoryTwootRepository twootRepository;

    @BeforeEach
    public void setUp(){
        twootRepository = new InMemoryTwootRepository();
    }

    @Test
    public void shouldRetrieveAddedTwoot(){
        Twoot addedTwoot = twootRepository.add("1", "elitest", "Hello world");
        Optional<Twoot> result = twootRepository.get("1");
        assertTrue(result.isPresent());
        assertEquals("Hello world", result.get().getContent());
    }

    @Test
    public void shouldQueryTwootsFromFollowedUsersAfterPosition() {
        twootRepository.add("1", "elitest", "Hello");

        List<Twoot> results = new ArrayList<>();
        twootRepository.query(
                new TwootQuery().inUsers(Set.of("elitest")).lastSeenPosition(Position.INITIAL),
                results::add
        );

        assertEquals(1, results.size());
        assertEquals("Hello", results.get(0).getContent());
    }

}
