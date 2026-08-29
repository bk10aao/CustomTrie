import CustomTrie.CustomTrie;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrieTest {

    @Test
    public void givenWord_hello_onSearchFor_hello_returnsTrue() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        assertTrue(customTrie.search("hello"));
    }

    @Test
    public void givenWord_hello_onSearchFor_world_returnsFalse() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        assertFalse(customTrie.search("world"));
    }

    @Test
    public void givenWord_hello_onSearchFor_hell_returnsFalse() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        assertFalse(customTrie.search("hell"));
    }

    @Test
    public void givenWord_hello_and_help_onSearchFor_help_returnsTrue() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        customTrie.insert("help");

        assertTrue(customTrie.search("help"));
    }

    @Test
    public void givenWord_hello__help_dog_onSearchForEachWord_returnsTrue() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        customTrie.insert("help");
        customTrie.insert("dog");

        assertTrue(customTrie.search("help"));
        assertTrue(customTrie.search("hello"));
        assertTrue(customTrie.search("dog"));
    }

    @Test
    public void givenWord_hello__help_dog_onSearchForStartingWith_hel() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        customTrie.insert("help");
        customTrie.insert("dog");
        List<String> results = customTrie.startsWith("hel");
        assertEquals(2, results.size());
        assertEquals("hello", results.get(0));
        assertEquals("help", results.get(1));

    }
}
