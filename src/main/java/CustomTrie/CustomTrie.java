package CustomTrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CustomTrie implements Trie {

    private final Node root = new Node();

    private int size;

    public void clear() {
        root.children.clear();
        size = 0;
    }

    public boolean delete(String word) {
        word = sanitizeInput(word);
        if (word == null)
            return false;
        return deleteHelper(root, word, 0);
    }

    public void insert(String word) {
        word = sanitizeInput(word);
        if (word == null)
            return;
        Node cursor = root;
        for (char c : word.toCharArray())
            cursor = cursor.children.computeIfAbsent(c, k -> new Node());
        if (!cursor.isEnd) {
            cursor.isEnd = true;
            size++;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean search(final String value) {
        Node node = findNode(value);
        return node != null && node.isEnd;
    }

    public int size() {
        return size;
    }

    public List<String> startsWith(final String prefix) {
        List<String> result = new ArrayList<>();
        String sanitizedPrefix = sanitizeInput(prefix);
        Node node = findNode(sanitizedPrefix);
        if (node != null)
            collectWords(node, new StringBuilder(sanitizedPrefix), result);
        return result;
    }

    private void collectWords(final Node node, final StringBuilder stringBuilder, final List<String> result) {
        if (node.isEnd)
            result.add(stringBuilder.toString());
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            stringBuilder.append(entry.getKey());
            collectWords(entry.getValue(), stringBuilder, result);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
    }

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

    private Node findNode(String input) {
        if (input == null || input.isEmpty())
            return null;
        Node cursor = root;
        for (char c : input.toCharArray())
            if ((cursor = cursor.children.get(c)) == null)
                return null;
        return cursor;
    }

    private String sanitizeInput(String s) {
        if (s == null)
            return null;
        String trimmed = s.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static class Node {
        private boolean isEnd = false;
        private final Map<Character, Node> children = new TreeMap<>();
    }
}
