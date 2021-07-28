package com.devwoo.projectstudy.compress;

import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.dec.BrotliInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.jpountz.lz4.LZ4FrameInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * @author 우두홍(2021 - 06 - 28)
 */
public class FileUncompressIntegrationTest extends FileCompressor {

    @AfterAll
    static void afterAll() {
        System.out.println("=파일 압축 풀기 시간 순서=======================================================");
        compressorTestInfoList.stream().sorted(durationComparator).forEach(compressorTestInfo -> {
            System.out.println(compressorTestInfo.getCompressorName());
            System.out.println("  - 소요시간(s): " + compressorTestInfo.getDurationStr());
            System.out.println("  - 파일사이즈(kb): " + compressorTestInfo.getFileSizeStr(FileSizeUnit.KB));
        });
    }

    @Test
    void refitTest1() throws IOException {
        repetitionTest(() -> zStandardTest(), CompressType.Z_STANDARD);
        repetitionTest(() -> gZipTest(), CompressType.GZIP);
        repetitionTest(() -> lz4Test(), CompressType.LZ4);
        repetitionTest(() -> brotliTest(), CompressType.BROTLI);
        repetitionTest(() -> lzmaTest(), CompressType.LZMA);
    }

    private Path zStandardTest() {
        Path uncompressFilePath = CompressType.Z_STANDARD.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(
            () -> new ZstdCompressorInputStream(new BufferedInputStream(Files.newInputStream(CompressType.Z_STANDARD.getCompressFilePath(COMPRESS_SAMPLE_PATH_STR))))
            , uncompressFilePath
        );
        return uncompressFilePath;
    }

    private Path gZipTest() {
        Path uncompressFilePath = CompressType.GZIP.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new GzipCompressorInputStream(new BufferedInputStream(Files.newInputStream(CompressType.GZIP.getCompressFilePath(COMPRESS_SAMPLE_PATH_STR))))
            , uncompressFilePath);
        return uncompressFilePath;
    }

    private Path lzmaTest() {
        Path uncompressFilePath = CompressType.LZMA.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new LZMACompressorInputStream(Files.newInputStream(CompressType.LZMA.getCompressFilePath(COMPRESS_SAMPLE_PATH_STR)))
            , uncompressFilePath);
        return uncompressFilePath;
    }

    private Path lz4Test() {
        Path uncompressFilePath = CompressType.LZ4.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new LZ4FrameInputStream(new BufferedInputStream(Files.newInputStream(CompressType.LZ4.getCompressFilePath(COMPRESS_SAMPLE_PATH_STR))))
            , uncompressFilePath);
        return uncompressFilePath;
    }

    private Path brotliTest() {
        BrotliLoader.isBrotliAvailable();
        Path uncompressFilePath = CompressType.BROTLI.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new BrotliInputStream(Files.newInputStream(CompressType.BROTLI.getCompressFilePath(COMPRESS_SAMPLE_PATH_STR)))
            , uncompressFilePath);
        return uncompressFilePath;
    }
}
