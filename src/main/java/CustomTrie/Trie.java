package CustomTrie;

import java.util.List;

public interface Trie {

    void clear();

    boolean delete(String word);

    void insert(String word);

    boolean isEmpty();

    boolean search(String word);

    int size();

    List<String> startsWith(String prefix);
}
