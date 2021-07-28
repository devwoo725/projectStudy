package com.devwoo.projectstudy.compress;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.github.luben.zstd.Zstd;
import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.dec.BrotliInputStream;
import com.nixxcode.jvmbrotli.enc.BrotliOutputStream;
import com.nixxcode.jvmbrotli.enc.Encoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Deflater;
import lombok.extern.slf4j.Slf4j;
import net.jpountz.lz4.LZ4FrameInputStream;
import net.jpountz.lz4.LZ4FrameOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author 우두홍(2021 - 06 - 25)
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileCompressServiceTest extends FileCompressor {
    @Test
    @DisplayName("zstd(Zstandard) 압축 테스트")
    @Order(1)
    void test1_1() throws IOException {
        Path compressResultPath = CompressType.Z_STANDARD.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> new ZstdCompressorOutputStream(new BufferedOutputStream(Files.newOutputStream(compressResultPath)), Zstd.maxCompressionLevel()));

        assertCompressResult(compressResultPath);
    }

    @Test
    @DisplayName("zstd(Zstandard) 압축 풀기 테스트")
    @Order(6)
    void test1_2() throws IOException {
        Path uncompressResultPath = CompressType.Z_STANDARD.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new ZstdCompressorInputStream(new BufferedInputStream(Files.newInputStream(CompressType.Z_STANDARD.getCompressFilePath(COMPRESS_PATH_STR))))
            , uncompressResultPath);

        assertUncompressResult(uncompressResultPath);
    }

    @Test
    @DisplayName("Brotli 압축 테스트")
    @Order(2)
    void test5_1() throws IOException {
        BrotliLoader.isBrotliAvailable();
        Path compressResultPath = CompressType.BROTLI.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> {
            Encoder.Parameters params = new Encoder.Parameters().setQuality(11);
            return new BrotliOutputStream(Files.newOutputStream(compressResultPath), params);
        });

        assertCompressResult(compressResultPath);
    }

    @Test
    @DisplayName("Brotli 압축 풀기 테스트")
    @Order(7)
    void test5_2() throws IOException {
        BrotliLoader.isBrotliAvailable();
        Path uncompressResultPath = CompressType.BROTLI.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new BrotliInputStream(Files.newInputStream(CompressType.BROTLI.getCompressFilePath(COMPRESS_PATH_STR)))
            , uncompressResultPath);

        assertUncompressResult(uncompressResultPath);
    }

    @Test
    @DisplayName("LZMA 압축 테스트")
    @Order(3)
    void test6_1() throws IOException {
        Path compressResultPath = CompressType.LZMA.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> new LZMACompressorOutputStream(new BufferedOutputStream(Files.newOutputStream(compressResultPath))));

        assertCompressResult(compressResultPath);
    }

    @Test
    @DisplayName("LZMA 압축 풀기 테스트")
    @Order(8)
    void test6_2() throws IOException {
        Path uncompressResultPath = CompressType.LZMA.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new LZMACompressorInputStream(Files.newInputStream(CompressType.LZMA.getCompressFilePath(COMPRESS_PATH_STR)))
            , uncompressResultPath);

        assertUncompressResult(uncompressResultPath);
    }

    @Test
    @DisplayName("lz4 압축 테스트")
    @Order(4)
    void test3_1() throws IOException {
        Path compressResultPath = CompressType.LZ4.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> new LZ4FrameOutputStream(new BufferedOutputStream(Files.newOutputStream(compressResultPath))));

        assertCompressResult(compressResultPath);
    }

    @Test
    @DisplayName("lz4 압축 풀기 테스트")
    @Order(9)
    void test3_2() throws IOException {
        Path uncompressResultPath = CompressType.LZ4.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new LZ4FrameInputStream(new BufferedInputStream(Files.newInputStream(CompressType.LZ4.getCompressFilePath(COMPRESS_PATH_STR))))
            , uncompressResultPath);

        assertUncompressResult(uncompressResultPath);
    }

    @Test
    @DisplayName("gzip 압축 테스트")
    @Order(5)
    void test2_1() throws IOException {
        Path compressResultPath = CompressType.GZIP.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> {
            GzipParameters parameters = new GzipParameters();
            parameters.setCompressionLevel(Deflater.BEST_COMPRESSION);
            return new GzipCompressorOutputStream(new BufferedOutputStream(Files.newOutputStream(compressResultPath)), parameters);
        });

        assertCompressResult(compressResultPath);
    }

    @Test
    @DisplayName("gzip 압축 풀기 테스트")
    @Order(10)
    void test2_2() throws IOException {
        Path uncompressResultPath = CompressType.GZIP.getUncompressFilePath(UNCOMPRESS_PATH_STR);
        uncompressFile(() -> new GzipCompressorInputStream(new BufferedInputStream(Files.newInputStream(CompressType.GZIP.getCompressFilePath(COMPRESS_PATH_STR))))
            , uncompressResultPath);

        assertUncompressResult(uncompressResultPath);
    }

    private void assertCompressResult(Path compressResultPath) throws IOException {
        assertThat(Files.exists(compressResultPath)).isTrue();
    }

    private void assertUncompressResult(Path uncompressResultPath) throws IOException {
        assertThat(Files.exists(uncompressResultPath)).isTrue();
        assertThat(Files.size(uncompressResultPath)).isEqualTo(Files.size(ORIGIN_DATA_PATH));
    }
}
