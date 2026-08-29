package CustomTrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

/**
 * A Trie (prefix tree) implementation for storing and retrieving strings efficiently.
 * <p>
 * This implementation supports insertion, deletion, exact-match search, and
 * prefix-based retrieval of words. All public methods that accept a word or
 * prefix sanitize it identically (via trimming) before use — leading and
 * trailing whitespace is insignificant everywhere, so {@code insert("cat")},
 * {@code search(" cat ")}, and {@code delete("cat  ")} are all equivalent.
 * Internal whitespace (e.g. {@code "New York"}) is preserved and treated as
 * ordinary characters. A word that trims to empty (or is {@code null}) is
 * treated as invalid input by {@link #insert(String)}, {@link #delete(String)},
 * and {@link #search(String)}; an empty (or all-whitespace) prefix passed to
 * {@link #startsWith(String)}, however, matches every word in the trie.
 * <p>
 * Children of each node are stored in a {@link TreeMap}, so traversal order
 * (e.g. during {@link #startsWith(String)}) is lexicographic by character.
 * <p>
 * This class is not thread-safe. External synchronization is required if an
 * instance is accessed by multiple threads concurrently.
 *
 * @see Trie
 */
public class CustomTrie implements Trie {

    /**
     * The root node of the trie. Does not itself represent any character.
     */
    private final Node root = new Node();

    /**
     * The number of complete words currently stored in the trie.
     */
    private int size;

    /**
     * Constructs an empty trie containing no words.
     */
    public CustomTrie() {
    }

    /**
     * Constructs a trie pre-populated with the given words.
     * <p>
     * Each word is inserted using the same rules as {@link #insert(String)} —
     * {@code null}, empty, or blank words are silently ignored, and whitespace
     * is trimmed.
     *
     * @param words the words to insert into the new trie
     * @throws NullPointerException if {@code words} is {@code null}
     */
    public CustomTrie(List<String> words) {
        requireNonNull(words);
        for(String s : words)
            insert(s);
    }

    /**
     * Constructs a trie pre-populated with the given words.
     * <p>
     * Each word is inserted using the same rules as {@link #insert(String)} —
     * {@code null}, empty, or blank words are silently ignored, and whitespace
     * is trimmed.
     *
     * @param words the words to insert into the new trie
     * @throws NullPointerException if {@code words} is {@code null}
     */
    public CustomTrie(String[] words) {
        requireNonNull(words);
        for (String word : words)
            insert(word);
    }

    /**
     * Constructs a new trie containing a copy of all words in {@code other}.
     * <p>
     * The two tries are independent after construction — subsequent
     * modifications to either do not affect the other.
     *
     * @param trie the trie to copy words from
     * @throws NullPointerException if {@code trie} is {@code null}
     */
    public CustomTrie(CustomTrie trie) {
        requireNonNull(trie);
        for (String word : trie.startsWith(""))
            insert(word);
    }

    /**
     * Removes all words from the trie, resetting it to an empty state.
     */
    public void clear() {
        root.children.clear();
        size = 0;
    }

    /**
     * Deletes the given word from the trie, if present.
     * <p>
     * The input is sanitized before processing; if sanitization yields
     * {@code null} (e.g. the input was {@code null}, empty, or blank),
     * this method returns {@code false} without modifying the trie.
     * <p>
     * Any nodes that become unnecessary (i.e. no longer mark the end of a word
     * and have no remaining children) as a result of the deletion are pruned
     * from the trie.
     *
     * @param word the word to delete
     * @return {@code true} if the word was found and deleted; {@code false}
     *         if the word was not present or the input was invalid
     */
    public boolean delete(String word) {
        String sanitized = sanitizeInput(word);
        if (sanitized == null)
            return false;
        return deleteHelper(root, sanitized, 0);
    }

    /**
     * Indicates whether this trie is equal to another object.
     * <p>
     * Two {@code CustomTrie} instances are considered equal if they have the
     * same {@link #size()} and contain exactly the same set of words, as
     * determined by comparing the results of {@code startsWith("")} on each
     * instance. The comparing object must be a {@code CustomTrie} of the
     * exact same class (not merely an instance of it) for this method to
     * return {@code true}.
     *
     * @param o the object to compare against
     * @return {@code true} if {@code o} is a {@code CustomTrie} containing
     *         the same words as this trie; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CustomTrie))
            return false;
        CustomTrie other = (CustomTrie) o;
        return this.size == other.size
                && this.startsWith("").equals(other.startsWith(""));
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     * <p>
     * The hash code is derived from both the {@link #size()} of the trie and
     * the full list of words it contains, so two tries considered equal by
     * {@link #equals(Object)} are guaranteed to produce the same hash code.
     *
     * @return a hash code for this trie
     */
    @Override
    public int hashCode() {
        return hash(size, startsWith(""));
    }

    /**
     * Inserts the given word into the trie.
     * <p>
     * The input is sanitized before processing; if sanitization yields
     * {@code null} (e.g. the input was {@code null}, empty, or blank),
     * this method does nothing. If the word is already present, the trie
     * is left unchanged and the size is not incremented.
     *
     * @param word the word to insert
     */
    public void insert(String word) {
        String sanitized = sanitizeInput(word);
        if (sanitized == null)
            return;
        Node cursor = root;
        for (int i = 0; i < sanitized.length(); i++)
            cursor = cursor.children.computeIfAbsent(sanitized.charAt(i), k -> new Node());
        if (!cursor.isEnd) {
            cursor.isEnd = true;
            size++;
        }
    }

    /**
     * Indicates whether the trie currently contains any words.
     *
     * @return {@code true} if no words are stored in the trie; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Searches the trie for an exact match of the given value.
     * <p>
     * The input is sanitized before processing, so leading/trailing
     * whitespace does not affect the result.
     *
     * @param value the word to search for
     * @return {@code true} if the value exists in the trie as a complete word;
     *         {@code false} otherwise
     */
    public boolean search(final String value) {
        Node node = findNode(sanitizeInput(value));
        return node != null && node.isEnd;
    }

    /**
     * Returns the number of complete words currently stored in the trie.
     *
     * @return the number of words in the trie
     */
    public int size() {
        return size;
    }

    /**
     * Returns all complete words in the trie that begin with the given prefix.
     * <p>
     * The prefix is trimmed before processing. If {@code prefix} is
     * {@code null}, an empty list is returned. If the trimmed prefix is
     * empty (i.e. {@code prefix} was empty or all whitespace), every word in
     * the trie is returned. Otherwise, only words beginning with the trimmed
     * prefix are returned. Words are collected in lexicographic order.
     *
     * @param prefix the prefix to search for; an empty or blank prefix matches all words
     * @return a list of words starting with the given prefix; empty if none are found
     *         or if {@code prefix} is {@code null}
     */
    public List<String> startsWith(final String prefix) {
        List<String> result = new ArrayList<>();
        if (prefix == null)
            return result;
        String trimmed = prefix.trim();
        Node node = trimmed.isEmpty() ? root : findNode(trimmed);
        if (node != null)
            collectWords(node, new StringBuilder(trimmed), result);
        return result;
    }

    /**
     * Returns a string representation of this trie, including its size and
     * the full list of words it currently contains.
     * <p>
     * The word list is computed live via {@code startsWith("")} on each
     * call, so the returned string always reflects the trie's current state.
     *
     * @return a string of the form {@code "CustomTrie{size=N, words=[...]}"}
     */
    @Override
    public String toString() {
        return "CustomTrie{size=" + size + ", words=" + startsWith("") + "}";
    }

    /**
     * Recursively collects all complete words reachable from the given node,
     * appending each discovered word to the result list.
     *
     * @param node          the node to start collecting from
     * @param stringBuilder a builder holding the characters accumulated on the
     *                      path from the root to {@code node}; mutated during
     *                      recursion and restored before returning
     * @param result        the list to which discovered words are added
     */
    private void collectWords(final Node node, final StringBuilder stringBuilder, final List<String> result) {
        if (node.isEnd)
            result.add(stringBuilder.toString());
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            stringBuilder.append(entry.getKey());
            collectWords(entry.getValue(), stringBuilder, result);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
    }

    /**
     * Recursively traverses the trie to delete the character at the given index
     * of {@code word}, pruning nodes that become unnecessary as a result.
     *
     * @param current the current node being examined
     * @param word    the sanitized word being deleted
     * @param index   the index of the character in {@code word} currently being processed
     * @return {@code true} if a word-ending flag was cleared as a result of this
     *         call (i.e. the word existed and was deleted); {@code false} otherwise
     */
    private boolean deleteHelper(Node current, String word, int index) {
        char ch = word.charAt(index);
        Node node = current.children.get(ch);
        if (node == null)
            return false;
        boolean deleted;
        if (index == word.length() - 1) {
            if (!node.isEnd)
                return false;
            node.isEnd = false;
            size--;
            deleted = true;
        } else
            deleted = deleteHelper(node, word, index + 1);
        if (deleted && !node.isEnd && node.children.isEmpty())
            current.children.remove(ch);
        return deleted;
    }

    /**
     * Traverses the trie following the characters of {@code input} and returns
     * the node reached, if any.
     *
     * @param input the string to trace through the trie; may be {@code null}
     * @return the node corresponding to the last character of {@code input},
     *         or {@code null} if {@code input} is {@code null}, empty, or no
     *         such path exists in the trie
     */
    private Node findNode(String input) {
        if (input == null || input.isEmpty())
            return null;
        Node cursor = root;
        for (int i = 0; i < input.length(); i++) {
            cursor = cursor.children.get(input.charAt(i));
            if (cursor == null)
                return null;
        }
        return cursor;
    }

    /**
     * Trims the given string and normalizes blank or empty results to {@code null}.
     *
     * @param s the string to sanitize
     * @return the trimmed string, or {@code null} if {@code s} is {@code null}
     *         or trims to an empty string
     */
    private String sanitizeInput(String s) {
        if (s == null)
            return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * A single node in the trie, representing one character position along
     * some path from the root.
     */
    private static class Node {
        /**
         * Whether this node marks the end of a complete word.
         */
        private boolean isEnd = false;

        /**
         * Child nodes keyed by the character they represent, ordered by character.
         */
        private final Map<Character, Node> children = new TreeMap<>();
    }
}
