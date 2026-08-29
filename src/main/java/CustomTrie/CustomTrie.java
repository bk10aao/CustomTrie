package CustomTrie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomTrie implements Trie {

    private final Map<Character, Node> map = new HashMap<>();

    private int size;

    public void clear() {
        map.clear();
        size = 0;
    }

    public boolean delete(String word) {
        return false;
    }

    public void insert(String word) {
        if (word == null)
            return;
        word = word.trim();
        if (word.isEmpty())
            return;
        char[] chars = word.toCharArray();
        Node cursor = map.computeIfAbsent(chars[0], k -> new Node());
        for(int i = 1; i < chars.length; i++)
            cursor = cursor.children.computeIfAbsent(chars[i], k -> new Node());
        if(!cursor.isEnd) {
            cursor.isEnd = true;
            size++;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean search(final String value) {
        if (value == null || value.isEmpty())
            return false;
        char[] chars = value.toCharArray();
        Node node = map.get(chars[0]);
        if(node == null)
            return false;
        for(int i = 1; i < chars.length; i++) {
            node = node.children.get(chars[i]);
            if(node == null)
                return false;
        }
        return node.isEnd;
    }

    public int size() {
        return size;
    }

    public List<String> startsWith(final String prefix) {
        List<String> result = new ArrayList<>();
        if (prefix == null || prefix.isEmpty())
            return result;
        char[] chars = prefix.toCharArray();
        Node node = map.get(chars[0]);
        if(node == null)
            return result;
        for(int i = 1; i < chars.length; i++) {
            node = node.children.get(chars[i]);
            if(node == null)
                return result;
        }
        collectWords(node, new StringBuilder(prefix), result);
        Collections.sort(result);
        return result;
    }

    private void collectWords(final Node node, final StringBuilder stringBuilder, final List<String> result) {
        if(node.isEnd)
            result.add(stringBuilder.toString());
        for(Map.Entry<Character, Node> entry : node.children.entrySet()) {
            stringBuilder.append(entry.getKey());
            collectWords(entry.getValue(), stringBuilder, result);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
    }

    private static class Node {
        private boolean isEnd = false;
        private final Map<Character, Node> children = new HashMap<>();
    }
}
