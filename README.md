# Custom Trie

An Implementation of a non-compressed Prefix-Trie.

*For the standard, non-compressed implementation, see [Custom Compressed Trie](https://github.com/bk10aao/CustomCompressedTrie).*

# Build and Test

To build and test the project run command `./gradlew clean build`

# Time Complexity

| Method                      |                        V1                        |
|:----------------------------|:------------------------------------------------:|
| `Constructor()`             |                      $O(1)$                      |
| `Constructor(List<String>)` |                $O(W \log \Sigma)$                |
| `Constructor(String[])`     |                $O(W \log \Sigma)$                |
| `Constructor(Trie)`         |                $O(S \log \Sigma)$                |
| `clear()`                   |                      $O(1)$                      |
| `delete(String)`            |                $O(L \log \Sigma)$                |
| `equals(Object)`            |                $O(S \log \Sigma)$                |
| `hashCode()`                |                $O(S \log \Sigma)$                |
| `insert(String)`            |                $O(L \log \Sigma)$                |
| `isEmpty()`                 |                      $O(1)$                      |
| `search(String)`            |                $O(L \log \Sigma)$                |
| `size()`                    |                      $O(1)$                      |
| `startsWith(String)`        | $O(P \log \Sigma + L_{\text{out}} \log \Sigma)$  |
| `toString()`                |                $O(S \log \Sigma)$                |

# Space Complexity

| Method                               |               V1               |
|:-------------------------------------|:------------------------------:|
| `Constructor()`                      |             $O(1)$             |
| `Constructor(List<String>)`          |             $O(1)$             |
| `Constructor(String[])`              |             $O(1)$             |
| `Constructor(Trie)`                  |             $O(S)$             |
| `clear()`                            |             $O(1)$             |
| `delete(String)`                     |             $O(L)$             |
| `equals(Object)`                     |             $O(S)$             |
| `hashCode()`                         |             $O(S)$             |
| `insert(String)`                     |             $O(1)$             |
| `isEmpty()`                          |             $O(1)$             |
| `search(String)`                     |             $O(1)$             |
| `size()`                             |             $O(1)$             |
| `startsWith(String)`                 | $O(L_{\text{out}} + L_{\max})$ |
| `toString()`                         |             $O(S)$             |

**Key**
* **$L$** - Length of the sanitized input word or search value.
* **$L_{\max}$** - Maximum length of any single word in the trie (bounds recursion stack depth and `StringBuilder` buffer size).
* **$L_{\text{out}}$** - Total character count across all matching words returned by a prefix query.
* **$P$** - Length of the sanitized search prefix.
* **$S$** - Total number of nodes in the trie structure.
* **$W$** - Total sum of character lengths across all stored words in the trie ($\sum |w_i|$).
* **$\Sigma$** - Branching factor (number of unique child character edges at a node; introduces a $\log \Sigma$ factor for `TreeMap` lookups).
# Performance Charts

#### Note: The following performance charts are designed to be viewed in dark mode.
![constructor.png](PerformanceCharts/constructor.png)
![constructor_collection.png](PerformanceCharts/constructor_array.png)
![constructor_int.png](PerformanceCharts/constructor_list.png)
![constructor_int_float.png](PerformanceCharts/copy_constructor.png)
![add.png](PerformanceCharts/clear.png)
![addAll.png](PerformanceCharts/delete.png)
![clear.png](PerformanceCharts/equals.png)
![clone.png](PerformanceCharts/hashCode.png)
![contains.png](PerformanceCharts/insert.png)
![containsAll.png](PerformanceCharts/isEmpty.png)
![equals.png](PerformanceCharts/search_hit.png)
![hashCode.png](PerformanceCharts/search_miss.png)
![isEmpty.png](PerformanceCharts/size.png)
![iterator.png](PerformanceCharts/startsWith.png)
![iterator.png](PerformanceCharts/toString.png)