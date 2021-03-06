/*
The MIT License

Copyright (C) 2008 Yu Kobayashi http://yukoba.accelart.jp/

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.github.dokandev.dokanjava.samples.memoryfs;

import static com.github.dokandev.dokanjava.util.FileAttribute.FILE_ATTRIBUTE_DIRECTORY;
import static com.github.dokandev.dokanjava.util.FileAttribute.FILE_ATTRIBUTE_NORMAL;
import gnu.trove.TByteArrayList;

import java.time.ZonedDateTime;

import org.apache.commons.io.FilenameUtils;

import com.github.dokandev.dokanjava.ByHandleFileInformation;
import com.github.dokandev.dokanjava.Win32FindData;
import com.github.dokandev.dokanjava.util.FileTime;

public class MemFileInfo {
	static long nextFileIndex = 2;

	String fileName;
	final boolean isDirectory;
	final TByteArrayList content = new TByteArrayList();
	int fileAttribute = FILE_ATTRIBUTE_NORMAL.mask();
	long creationTime = 0;
	long lastAccessTime = 0;
	long lastWriteTime = 0;
	final long fileIndex;

	MemFileInfo(String fileName, boolean isDirectory) {
		this.fileName = fileName;
		this.isDirectory = isDirectory;
		fileIndex = getNextFileIndex();
		if (isDirectory)
			fileAttribute |= FILE_ATTRIBUTE_DIRECTORY.mask();
		long fileTime = FileTime.toFileTime(ZonedDateTime.now());
		creationTime = fileTime;
		lastAccessTime = fileTime;
		lastWriteTime = fileTime;
	}

	Win32FindData toWin32FindData() {
		return new Win32FindData(fileAttribute, creationTime, lastAccessTime, lastWriteTime,
				getFileSize(), 0, 0, FilenameUtils.getName(fileName), Utils.toShortName(fileName));
	}

	ByHandleFileInformation toByHandleFileInformation() {
		return new ByHandleFileInformation(fileAttribute, creationTime, lastAccessTime, lastWriteTime,
				MemoryFS.volumeSerialNumber, getFileSize(), 1, fileIndex);
	}

	int getFileSize() {
		return content.size();
	}

	static long getNextFileIndex() {
		return nextFileIndex++;
	}
}
