package com.devwoo.projectstudy.compress;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;

/**
 * @author 우두홍(2021 - 06 - 28)
 */
@Getter
public enum CompressType {
    Z_STANDARD("zstd_", ".zstd") //https://github.com/luben/zstd-jni
    , BROTLI("brotli_", ".br") //https://jvmbrotli.com/
    , LZMA("lzma_", ".lzma") //https://www.7-zip.org/sdk.html
    , LZ4("lz4_", ".lz4") //https://lz4.github.io/lz4/
    , GZIP("gzip_", ".gz") //https://www.gzip.org/
    ;

    private String prefix;
    private String suffix;

    CompressType(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public Path getCompressFilePath(String prefixPathStr) {
        return Paths.get(prefixPathStr + "\\compress" + this.suffix);
    }

    public Path getUncompressFilePath(String prefixPathStr) {
        return Paths.get(prefixPathStr + "\\" + this.prefix + "uncompress.txt");
    }

}
