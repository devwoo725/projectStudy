package com.devwoo.projectstudy.compress;

import com.github.luben.zstd.Zstd;
import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.enc.BrotliOutputStream;
import com.nixxcode.jvmbrotli.enc.Encoder;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Deflater;
import net.jpountz.lz4.LZ4FrameOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * @author 우두홍(2021 - 06 - 28)
 */
public class FileCompressIntegrationTest extends FileCompressor {

    @AfterAll
    static void afterAll() {
        System.out.println("=파일 압축 시간 순서=======================================================");
        compressorTestInfoList.stream().sorted(durationComparator).forEach(compressorTestInfo -> {
            System.out.println(compressorTestInfo.getCompressorName());
            System.out.println("  - 소요시간(s): " + compressorTestInfo.getDurationStr());
            System.out.println("  - 파일사이즈(kb): " + compressorTestInfo.getFileSizeStr(FileSizeUnit.KB));
        });

        System.out.println("=파일 압축 용량 순서=======================================================");
        compressorTestInfoList.stream().sorted(fileSizeComparator).forEach(compressorTestInfo -> {
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
        Path compressFilePath = CompressType.Z_STANDARD.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(
            () -> new ZstdCompressorOutputStream(new BufferedOutputStream(Files.newOutputStream(compressFilePath)), Zstd.maxCompressionLevel())
        );
        return compressFilePath;
    }

    private Path gZipTest() {
        Path compressFilePath = CompressType.GZIP.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> {
            GzipParameters parameters = new GzipParameters();
            parameters.setCompressionLevel(Deflater.BEST_COMPRESSION);
            return new GzipCompressorOutputStream(new BufferedOutputStream(Files.newOutputStream(compressFilePath)), parameters);
        });
        return compressFilePath;
    }

    private Path lzmaTest() {
        Path compressFilePath = CompressType.LZMA.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(
            () -> new LZMACompressorOutputStream(new BufferedOutputStream(Files.newOutputStream(compressFilePath)))
        );
        return compressFilePath;
    }

    private Path lz4Test() {
        Path compressFilePath = CompressType.LZ4.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> new LZ4FrameOutputStream(new BufferedOutputStream(Files.newOutputStream(compressFilePath))));
        return compressFilePath;
    }

    private Path brotliTest() {
        BrotliLoader.isBrotliAvailable();
        Path compressFilePath = CompressType.BROTLI.getCompressFilePath(COMPRESS_PATH_STR);
        compressFile(() -> {
            Encoder.Parameters params = new Encoder.Parameters().setQuality(11);
            return new BrotliOutputStream(Files.newOutputStream(compressFilePath), params);
        });
        return compressFilePath;
    }
}
