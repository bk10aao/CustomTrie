package CustomTrie;

import java.util.List;

public interface Trie {

    void insert(String word);

    boolean search(String word);

    List<String> startsWith(String prefix);

    boolean delete(String word);

    boolean isEmpty();

    int size();

    void clear();
}
