# Custom Trie

An Implementation of a non-compressed Prefix-Trie.

# Time Complexity

| Method                         | Time Complexity          |
|:-------------------------------|:-------------------------|
| **`insert(word)`**             | $O(L \log \Sigma)$       |
| **`search(value)`**            | $O(L \log \Sigma)$       |
| **`delete(word)`**             | $O(L \log \Sigma)$       |
| **`startsWith(prefix)`**       | $O((P + M) \log \Sigma)$ |
| **`size()`**                   | $O(1)$                   |
| **`isEmpty()`**                | $O(1)$                   |
| **`clear()`**                  | $O(1)$                   |
| **`CustomTrie()`**             | $O(1)$                   |
| **`CustomTrie(List / Array)`** | $O(W \log \Sigma)$       |
| **`CustomTrie(CustomTrie)`**   | $O(W \log \Sigma)$       |
| **`equals(Object)`**           | $O(W \log \Sigma)$       |
| **`hashCode()`**               | $O(W \log \Sigma)$       |
| **`toString()`**               | $O(W \log \Sigma)$       |

# Space Complexity

| Method                         | Space Complexity |
|:-------------------------------|:-----------------|
| **`insert(word)`**             | $O(L)$           |
| **`search(value)`**            | $O(L)$           |
| **`delete(word)`**             | $O(L)$           |
| **`startsWith(prefix)`**       | $O(M + L_{max})$ |
| **`size()`**                   | $O(1)$           |
| **`isEmpty()`**                | $O(1)$           |
| **`clear()`**                  | $O(1)$           |
| **`CustomTrie()`**             | $O(1)$           |
| **`CustomTrie(List / Array)`** | $O(W)$           |
| **`CustomTrie(CustomTrie)`**   | $O(W)$           |
| **`equals(Object)`**           | $O(W)$           |
| **`hashCode()`**               | $O(W)$           |
| **`toString()`**               | $O(W)$           |

**Key**

* **$L$**: Length of the sanitized input word or search value.
* **$L_{max}$**: Maximum length of any single word in the trie (bounds recursion stack depth and `StringBuilder` buffer size).
* **$P$**: Length of the sanitized search prefix.
* **$M$**: Total character count across all matching words returned by a prefix query.
* **$W$**: Total sum of character lengths across all stored words in the trie ($\sum |w_i|$).
* **$\Sigma$**: Branching factor (number of unique child character edges at a node; introduces a $\log \Sigma$ factor for `TreeMap` lookups).