# 압축 기술 성능 테스트 결과

## 테스트 파일
- sample file
    - path: src\main\resources\file\origin\selfcheck_signal_testdata.txt
    - size: 192kb

## 성능 테스트
### 방법
- 테스트 파일을 100회 압축, 압축풀기를 진행 
- 100회 동안 소요된 총 시간과 평균 압축 파일 사이즈 측정

### 압축/압축풀기 테스트 결과
- LZMA
    - 압축 방식이 압축률이 가장 좋았지만 압축 풀기에 소요되는 시간이 다른 방식에 비해 많이든다.
- Brotli/ZSTANDARD
    - 압축률이 다른 방식(gzip, lz4)에 비해 좋은 편이며 압축 풀기에 소요되는 시간도 적다.
  
---
  
- 압축 결과(압축 파일 사이즈로 오름차순)
  
  |압축 기술|소요 시간|압축 파일 사이즈|비고|
  |--------|--------|---------------|----|
  |**LZMA**|10.3s| 35.7kb| |
  |ZSTANDARD|24.6s|38.1kb| |
  |Brotli|27.7s|36.6kb| |
  |~~LZ4~~|0.3s|90kb|압축 시간은 빠르지만 압축 비율이 높지 않아 제외|
  |~~GZIP~~|3.2s|54kb|압축 시간은 빠르지만 압축 비율이 높지 않아 제외|

- 압축 풀기 결과(소요 시간으로 오름차순)
  
  |압축 기술|소요 시간|압축 파일 사이즈|비고|
  |--------|--------|---------------|----|
  |**Brotli**|0.3s|192.5kb| |
  |ZSTANDARD|0.4s|192.5kb| |
  |LZMA|7.6s|192.5kb| |
  |~~LZ4~~|0.4s|192.5kb|압축 비율이 높지 않아 제외|
  |~~GZIP~~|0.3s|192.5kb|압축 비율이 높지 않아 제외|

## 결론
- 압축률이 중요하다면 LZMA
- 시간이 중요하다면 Brotli,Zstandard

## COMPRESSOR 정리
### LZMA
- 높은 압축률과 빠른 압축 해제를 제공하므로 임베디드 애플리케이션에 매우 적합
- 확장자: .lzma
- [LZMA(Lempel-Ziv-Markov chain-Algorithm)](https://www.7-zip.org/sdk.html)
- [Common compress lzma](https://commons.apache.org/proper/commons-compress/examples.html)

### Brotli
- Google에서 개발한 Brotli는 LZ77 알고리즘의 최신 변형, Huffman 코딩 및 2차 컨텍스트 모델링의 조합을 사용하여 데이터를 압축하는 범용 무손실 압축 알고리즘
- 확장자: .br
- [Brotli](https://github.com/google/brotli)
- [jvmBrotli(brotli java lib)](https://jvmbrotli.com/)

### Zstandard
- Facebook에서 개발한 Zstandard는 빠른 압축 알고리즘으로 높은 압축률을 제공
- 확장자: .zstd
- [Zstandard](https://facebook.github.io/zstd/)
- [zstd-jni(zstandard java lib)](https://github.com/luben/zstd-jni)
