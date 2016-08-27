/**
 * QucikKV
 * Copyright 2014-2016 Sumi Makito
 * Licensed under Apache License 2.0.
 *
 * @author sumimakito<sumimakito@hotmail.com>
 */

package com.github.sumimakito.quickkv.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressHelper {

    public static byte[] compress(byte[] bytes) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(baos);
        gzip.write(bytes);
        gzip.close();
        return baos.toByteArray();
    }

    public static String decompress(byte[] bytes) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis));
        StringBuilder sb = new StringBuilder();
        String l;
        while ((l=bf.readLine())!=null) {
            sb.append(l);
        }
        return sb.toString();
    }

    /*

    Somehow, Asm in zip4j may conflict with the one in json-smart.
    Use GZip to instead.

    public static boolean compress(String fSrcPath, String fDstPath, boolean override) throws Exception {
        File srcFile = new File(fSrcPath);
        File dstFile = new File(fDstPath);
        File dstParentDir = dstFile.getParentFile();
        if (dstParentDir.exists()) {
            if (dstParentDir.isFile() && override) {
                dstParentDir.delete();
            } else if (dstParentDir.isFile() && !override) {
                throw new Exception("Path in destination is blocked by a file.");
            }
            if (!dstParentDir.mkdirs()) throw new Exception("Cannot mkdirs for output file(s).");
            if (dstParentDir.isDirectory()) {
                if (dstFile.exists()) {
                    if (override) {
                        dstFile.delete();
                    } else {
                        throw new Exception("Output file already exists!");
                    }
                }
            }
        } else {
            if (!dstParentDir.mkdirs()) throw new Exception("Cannot mkdirs for output file(s).");
        }

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);    // 压缩级别

        try {
            ZipFile zipFile = new ZipFile(dstFile);
            zipFile.addFile(srcFile, parameters);
            return true;
        } catch (ZipException e) {
            throw e;
        }
    }

    public static File[] uncompress(String fZipPath, String fDstPath, boolean override) throws Exception {
        ZipFile zFile = new ZipFile(fZipPath);
        if (!zFile.isValidZipFile()) {
            throw new ZipException("Invalid zip file!");
        }
        File dstParentDir = new File(fDstPath);
        if (dstParentDir.exists()) {
            if (dstParentDir.isFile() && override) {
                dstParentDir.delete();
            } else if (dstParentDir.isFile() && !override) {
                throw new Exception("Path in destination is blocked by a file.");
            }
            if (!dstParentDir.mkdirs()) throw new Exception("Cannot mkdirs for output file(s).");
        } else {
            if (!dstParentDir.mkdirs()) throw new Exception("Cannot mkdirs for output file(s).");
        }
        zFile.extractAll(fDstPath);
        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<File>();
        for (FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(dstParentDir, fileHeader.getFileName()));
            }
        }
        File[] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }
    */
}

