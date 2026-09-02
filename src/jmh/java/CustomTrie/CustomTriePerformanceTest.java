package CustomTrie;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class CustomTriePerformanceTest {

    @Param({"10000", "20000", "30000", "40000", "50000", "70000", "80000", "90000", "100000"})
    public int size;

    private List<String> wordsList;
    private String[] wordsArray;
    private CustomTrie readOnlyTrie;
    private CustomTrie equalMatchTrie;

    private String hitWord;
    private String missWord;
    private String searchPrefix;

    @Setup(Level.Trial)
    public void setupTrial() {
        Random random = new Random(42);
        wordsList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            wordsList.add(generateRandomString(random));
        }
        wordsArray = wordsList.toArray(new String[0]);
        readOnlyTrie = new CustomTrie(wordsList);
        equalMatchTrie = new CustomTrie(wordsList);

        hitWord = wordsList.get(random.nextInt(wordsList.size()));
        missWord = "NON_EXISTENT_KEY_" + random.nextInt();
        searchPrefix = hitWord.substring(0, Math.min(3, hitWord.length()));
    }

    // --- Constructors & Bulk Operations ---

    @Benchmark
    public CustomTrie benchmarkDefaultConstructor() {
        return new CustomTrie();
    }

    @Benchmark
    public CustomTrie benchmarkListConstructor() {
        return new CustomTrie(wordsList);
    }

    @Benchmark
    public CustomTrie benchmarkArrayConstructor() {
        return new CustomTrie(wordsArray);
    }

    @Benchmark
    public CustomTrie benchmarkCopyConstructor() {
        return new CustomTrie(readOnlyTrie);
    }

    @Benchmark
    public CustomTrie benchmarkInsert() {
        CustomTrie target = new CustomTrie();
        for (String word : wordsList) {
            target.insert(word);
        }
        return target;
    }

    // --- Read-Only Benchmarks (Uses Trial-level State) ---

    @Benchmark
    public boolean benchmarkSearchHit() {
        return readOnlyTrie.search(hitWord);
    }

    @Benchmark
    public boolean benchmarkSearchMiss() {
        return readOnlyTrie.search(missWord);
    }

    @Benchmark
    public List<String> benchmarkStartsWith() {
        return readOnlyTrie.startsWith(searchPrefix);
    }

    @Benchmark
    public boolean benchmarkIsEmpty() {
        return readOnlyTrie.isEmpty();
    }

    @Benchmark
    public int benchmarkSize() {
        return readOnlyTrie.size();
    }

    @Benchmark
    public String benchmarkToString() {
        return readOnlyTrie.toString();
    }

    @Benchmark
    public boolean benchmarkEquals() {
        return readOnlyTrie.equals(equalMatchTrie);
    }

    @Benchmark
    public int benchmarkHashCode() {
        return readOnlyTrie.hashCode();
    }

    // --- Mutating Benchmarks (Uses Separate State) ---

    @Benchmark
    public boolean benchmarkDelete(MutatingState state) {
        return state.mutableTrie.delete(hitWord);
    }

    @Benchmark
    public void benchmarkClear(MutatingState state) {
        state.mutableTrie.clear();
    }

    @State(Scope.Thread)
    public static class MutatingState {
        public CustomTrie mutableTrie;

        @Setup(Level.Invocation)
        public void setup(CustomTriePerformanceTest parent) {
            mutableTrie = new CustomTrie(parent.wordsList);
        }
    }

    private static String generateRandomString(Random rand) {
        int length = 5 + rand.nextInt(15 - 5 + 1);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + rand.nextInt(26)));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CustomTriePerformanceTest.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(2)
                .forks(1)
                .result("CustomTrie_performance_results.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();

        Collection<RunResult> results = new Runner(opt).run();
        writeCustomCsv(results);
    }

    private static void writeCustomCsv(Collection<RunResult> results) {
        try (FileWriter writer = new FileWriter("CustomTrie_jmh_performance.csv")) {
            writer.write("Benchmark;Size;Score (ns/op)\n");
            for (RunResult result : results) {
                String benchmarkName = result.getParams().getBenchmark();
                String shortName = benchmarkName.substring(benchmarkName.lastIndexOf('.') + 1);

                double score = result.getPrimaryResult().getScore();
                String sizeVal = result.getParams().getParam("size");

                writer.write("\"" + shortName + "\";" + (sizeVal != null ? sizeVal : "N/A") + ";" + score + "\n");
            }
            System.out.println("JMH Performance report saved: CustomTrie_jmh_performance.csv");
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }
}