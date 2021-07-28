package com.devwoo.projectstudy.compress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author 우두홍(2021 - 06 - 28)
 */
@Slf4j
public class FileCompressor {

    public static final String COMPRESS_PATH_STR = "src\\main\\resources\\file\\compress";
    public static final String UNCOMPRESS_PATH_STR = "src\\main\\resources\\file\\uncompress";
    public static final String COMPRESS_SAMPLE_PATH_STR = "src\\main\\resources\\file\\sample\\compress";
    public static final Path ORIGIN_DATA_PATH = Paths.get("src\\main\\resources\\file\\origin\\selfcheck_signal_testdata.txt");
    public static final int BUFFER_SIZE = 1024;

    public CompressorTestInfo compressorTestInfo;
    public static List<CompressorTestInfo> compressorTestInfoList;

    protected static Comparator<CompressorTestInfo> durationComparator = (info1, info2) -> {
        if (info1.getDuration() < info2.getDuration()) {
            return -1;
        } else if (info1.getDuration() > info2.getDuration()) {
            return 1;
        } else {
            return 0;
        }
    };

    protected static Comparator<CompressorTestInfo> fileSizeComparator = (info1, info2) -> {
        if (info1.getFileSize() < info2.getFileSize()) {
            return -1;
        } else if (info1.getFileSize() > info2.getFileSize()) {
            return 1;
        } else {
            return 0;
        }
    };

    @FunctionalInterface
    public interface CompressorStream<T> {

        T get() throws IOException;
    }

    @Getter
    @Setter
    @ToString
    public class CompressorTestInfo {

        private String compressorName;
        private long startMillisecond;
        private long endMillisecond;
        private long fileSize;

        public String getDurationStr() {
            return getDuration() + "s";
        }

        public float getDuration() {
            return (endMillisecond - startMillisecond) / 1000f;
        }

        public String getFileSizeStr(FileSizeUnit unit) {
            switch (unit) {
                case KB:
                    return (fileSize / 1024f) + unit.name().toLowerCase();
                default:
                    return String.valueOf(fileSize);
            }
        }
    }

    enum FileSizeUnit {
        KB
    }

    @BeforeAll
    static void setUp() {
        Arrays.stream(Paths.get(COMPRESS_PATH_STR).toFile().listFiles()).forEach(file -> file.delete());
        Arrays.stream(Paths.get(UNCOMPRESS_PATH_STR).toFile().listFiles()).forEach(file -> file.delete());
        compressorTestInfoList = new ArrayList<>();
    }

    protected void repetitionTest(Supplier<Path> supplier, CompressType compressType) throws IOException {
        repetitionTest(supplier, compressType, 100);
    }

    protected void repetitionTest(Supplier<Path> supplier, CompressType compressType, int repetitionCount) throws IOException {
        int count = 1;
        compressorTestInfo = new CompressorTestInfo();
        compressorTestInfo.setCompressorName(compressType.name());
        compressorTestInfo.setStartMillisecond(System.currentTimeMillis());
        List<Long> fileSizeList = new ArrayList<>();
        while (count < repetitionCount) {
            fileSizeList.add(Files.size(supplier.get()));
            count++;
        }

        compressorTestInfo.setFileSize(fileSizeList.stream().reduce(Long::sum).get() / fileSizeList.size());
        compressorTestInfo.setEndMillisecond(System.currentTimeMillis());
        compressorTestInfoList.add(compressorTestInfo);
    }

    /**
     * 파일을 압축한다.
     *
     * @param compressorOutputStream
     */
    protected void compressFile(CompressorStream<OutputStream> compressorOutputStream) {
        try (
            InputStream inputStream = Files.newInputStream(ORIGIN_DATA_PATH);
            OutputStream outputStream = compressorOutputStream.get();
        ) {
            writeFile(inputStream, outputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 파일 압축을 푼다.
     *
     * @param compressorInputStream
     * @param uncompressResultPath
     */
    protected void uncompressFile(CompressorStream<InputStream> compressorInputStream, Path uncompressResultPath) {
        try (
            InputStream inputStream = compressorInputStream.get();
            OutputStream outputStream = Files.newOutputStream(uncompressResultPath);
        ) {
            writeFile(inputStream, outputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 파일에 내용을 쓴다.
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    private void writeFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        final byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while (-1 != (length = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, length);
        }
    }
}
