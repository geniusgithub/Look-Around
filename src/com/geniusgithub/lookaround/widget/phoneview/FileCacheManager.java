package com.geniusgithub.lookaround.widget.phoneview;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FileCacheManager {
	private static final long MAX_SIZE = 30 * 1024 * 1024; // 30MB

	public static void cleanDir(File dir) {
		if (dir == null || dir.isFile()) {
			return;
		}
		
		long size = 0;

		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				size += file.length();
			}
		}

		if (size <= MAX_SIZE) {
			return;
		}

		if (files.length > 1) {
			Arrays.sort(files, new ComparatorByLastModifiedTime());
		}

		for (File file : files) {
			file.delete();

			if ((size -= file.length()) <= MAX_SIZE) {
				break;
			}
		}
	}

	/**
	 * 按照文件时间排序，旧文件排在最前面
	 */
	private static class ComparatorByLastModifiedTime implements
			Comparator<File> {

		@Override
		public int compare(File lhs, File rhs) {
			long diff = lhs.lastModified() - rhs.lastModified();

			return diff > 0 ? 1 : diff < 0 ? -1 : 0;
		}

	}
}
