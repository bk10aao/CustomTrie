package CustomTrie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrieTest {

    private CustomTrie trie;

    @BeforeEach
    void setUp() {
        trie = new CustomTrie();
    }

    @Test
    @DisplayName("Should return true when searching for inserted word 'hello'")
    public void givenWord_hello_onSearchFor_hello_returnsTrue() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        assertTrue(customTrie.search("hello"));
    }

    @Test
    @DisplayName("Should return false when searching for non-existent word 'world'")
    public void givenWord_hello_onSearchFor_world_returnsFalse() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        assertFalse(customTrie.search("world"));
    }

    @Test
    @DisplayName("Should return false when searching for prefix 'hell' that is not a complete word")
    public void givenWord_hello_onSearchFor_hell_returnsFalse() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        assertFalse(customTrie.search("hell"));
    }

    @Test
    @DisplayName("Should return true when searching for second inserted word 'help'")
    public void givenWord_hello_and_help_onSearchFor_help_returnsTrue() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        customTrie.insert("help");

        assertTrue(customTrie.search("help"));
    }

    @Test
    @DisplayName("Should return true when searching for each of multiple inserted words")
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
    @DisplayName("Should return false when searching for null")
    void testSearchNull() {
        assertFalse(trie.search(null));
    }

    @Test
    @DisplayName("Should return false when searching for empty string")
    void testSearchEmpty() {
        assertFalse(trie.search(""));
    }

    @Test
    @DisplayName("Should return false when first character is not in trie")
    void testSearchMissingFirstChar() {
        trie.insert("hello");
        assertFalse(trie.search("world"));
    }

    @Test
    @DisplayName("Should return false when middle character is not in trie path")
    void testSearchMissingMiddleChar() {
        trie.insert("hello");
        assertFalse(trie.search("hex"));
    }

    @Test
    @DisplayName("Should return false when prefix exists but is not marked as end of word")
    void testSearchPrefixOnlyNotWord() {
        trie.insert("hello");
        assertFalse(trie.search("hel"));
    }

    @Test
    @DisplayName("Should return matching words 'hello' and 'help' for prefix 'hel'")
    public void givenWord_hello_help_dog_onSearchForStartingWith_hel_returns_hello_help() {
        CustomTrie customTrie = new CustomTrie();
        customTrie.insert("hello");
        customTrie.insert("help");
        customTrie.insert("dog");
        List<String> results = customTrie.startsWith("hel");
        assertEquals(2, results.size());
        assertEquals("hello", results.get(0));
        assertEquals("help", results.get(1));
    }

    @Test
    @DisplayName("Should return empty list when prefix is null")
    void testStartsWithNull() {
        List<String> results = trie.startsWith(null);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when prefix is empty")
    void testStartsWithEmpty() {
        List<String> results = trie.startsWith("");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when root character does not match prefix")
    void testStartsWithMissingFirstChar() {
        trie.insert("hello");
        List<String> results = trie.startsWith("z");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when intermediate character in prefix is missing")
    void testStartsWithMissingMiddleChar() {
        trie.insert("hello");
        List<String> results = trie.startsWith("hex");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should return alphabetically sorted matching words and navigate recursive subtrees")
    void testStartsWithMultipleMatchesSorted() {
        trie.insert("cater");
        trie.insert("cat");
        trie.insert("cart");
        trie.insert("dog");

        List<String> results = trie.startsWith("ca");

        assertEquals(3, results.size());
        assertEquals("cart", results.get(0));
        assertEquals("cat", results.get(1));
        assertEquals("cater", results.get(2));
    }

    @Test
    @DisplayName("Should include prefix itself if prefix is also a valid word")
    void testStartsWithPrefixIsAWord() {
        trie.insert("car");
        trie.insert("card");
        trie.insert("care");

        List<String> results = trie.startsWith("car");

        assertEquals(3, results.size());
        assertEquals("car", results.get(0));
        assertEquals("card", results.get(1));
        assertEquals("care", results.get(2));
    }

    @Test
    @DisplayName("Should insert a multi-character word successfully")
    void testInsertMultiCharWord() {
        trie.insert("hello");
        assertTrue(trie.search("hello"));
        assertEquals(1, trie.size());
        assertFalse(trie.isEmpty());
    }

    @Test
    @DisplayName("Should insert a single-character word successfully")
    void testInsertSingleCharWord() {
        trie.insert("a");
        assertTrue(trie.search("a"));
        assertEquals(1, trie.size());
    }

    @Test
    @DisplayName("Should trim whitespace before inserting")
    void testInsertTrimsWhitespace() {
        trie.insert("  world  ");
        assertTrue(trie.search("world"));
        assertFalse(trie.search("  world  "));
        assertEquals(1, trie.size());
    }

    @Test
    @DisplayName("Should return immediately when inserting null")
    void testInsertNull() {
        trie.insert(null);
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    @DisplayName("Should return immediately for empty or blank strings")
    void testInsertEmptyOrBlank(String word) {
        trie.insert(word);
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    @DisplayName("Should not increment size when inserting duplicate words")
    void testInsertDuplicate() {
        trie.insert("java");
        trie.insert("java");
        assertEquals(1, trie.size());
        assertTrue(trie.search("java"));
    }

    @Test
    @DisplayName("Should correctly insert words that share prefixes")
    void testInsertSharedPrefix() {
        trie.insert("car");
        trie.insert("card");

        assertEquals(2, trie.size());
        assertTrue(trie.search("car"));
        assertTrue(trie.search("card"));
        assertFalse(trie.search("ca"));
    }

    @Test
    @DisplayName("Should reset size to zero and remove all words when clear is called")
    void testClear() {
        trie.insert("apple");
        trie.insert("banana");

        trie.clear();

        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
        assertFalse(trie.search("apple"));
        assertFalse(trie.search("banana"));
    }

    @Test
    @DisplayName("Should return true for isEmpty on a newly initialized trie")
    void testIsEmptyTrueWhenNew() {
        assertTrue(trie.isEmpty());
    }

    @Test
    @DisplayName("Should return false for isEmpty after inserting a word")
    void testIsEmptyFalseAfterInsert() {
        trie.insert("hello");
        assertFalse(trie.isEmpty());
    }

    @Test
    @DisplayName("Should return 0 for size on a newly initialized trie")
    void testSizeZeroWhenNew() {
        assertEquals(0, trie.size());
    }

    @Test
    @DisplayName("Should accurately report size incrementing as unique words are added")
    void testSizeIncrementsCorrectly() {
        assertEquals(0, trie.size());
        trie.insert("cat");
        assertEquals(1, trie.size());
        trie.insert("dog");
        assertEquals(2, trie.size());
    }

    @Test
    @DisplayName("Should return false when attempting to delete null")
    void testDeleteNull() {
        assertFalse(trie.delete(null));
    }

    @Test
    @DisplayName("Should return false when attempting to delete empty or blank strings")
    void testDeleteEmptyOrBlank() {
        assertFalse(trie.delete(""));
        assertFalse(trie.delete("   "));
    }

    @Test
    @DisplayName("Should return false when deleting a word not present in the trie")
    void testDeleteNonExistentWord() {
        trie.insert("hello");
        assertFalse(trie.delete("world"));
    }

    @Test
    @DisplayName("Should return false when deleting a prefix of an inserted word that is not marked as a word")
    void testDeletePrefixThatIsNotAWord() {
        trie.insert("hello");
        assertFalse(trie.delete("hel"));
        assertTrue(trie.search("hello"));
        assertEquals(1, trie.size());
    }

    @Test
    @DisplayName("Should successfully delete an existing word and update size")
    void testDeleteExistingWord() {
        trie.insert("hello");
        assertTrue(trie.delete("hello"));
        assertFalse(trie.search("hello"));
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
    }

    @Test
    @DisplayName("Should delete a word with trailing/leading spaces after trimming")
    void testDeleteTrimsWhitespace() {
        trie.insert("world");
        assertTrue(trie.delete("  world  "));
        assertFalse(trie.search("world"));
        assertEquals(0, trie.size());
    }

    @Test
    @DisplayName("Should delete a longer word without removing shared prefix word")
    void testDeleteLongerWordPreservesPrefix() {
        trie.insert("car");
        trie.insert("card");

        assertTrue(trie.delete("card"));
        assertFalse(trie.search("card"));
        assertTrue(trie.search("car"));
        assertEquals(1, trie.size());
    }

    @Test
    @DisplayName("Should delete a prefix word without removing longer extending words")
    void testDeletePrefixPreservesLongerWord() {
        trie.insert("car");
        trie.insert("card");

        assertTrue(trie.delete("car"));
        assertFalse(trie.search("car"));
        assertTrue(trie.search("card"));
        assertEquals(1, trie.size());
    }
}