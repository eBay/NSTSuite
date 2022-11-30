package com.ebay.service.logger.writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.io.FileMatchers.anExistingFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class FileWriterWithEncodingTest {

	private FileWriterWithEncoding writer;
	private BufferedReader reader;
	private File file;
	private String filePath = System.getProperty("user.dir") + File.pathSeparator + "%s";

	@AfterMethod
	public void afterMethod() throws IOException {

		if (writer != null) {
			writer.close();
		}

		if (reader != null) {
			reader.close();
		}

		if (file != null) {
			file.delete();
		}
	}

	@Test(groups = "unitTest")
	public void initializeWithFilePath() throws Exception {

		String pathname = String.format(filePath, "utf8.txt");
		file = new File(pathname);
		String fileContents = "∫ßüøπ˙h∆kkñ";

		writer = new FileWriterWithEncoding(pathname, Encode.UTF_8);
		writer.write(fileContents);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("Writer instantiated with pathname was not written or read back correctly.", lineRead,
				is(equalTo(fileContents)));
	}

	@Test(groups = "unitTest")
	public void initializeWithNonExistingFilePath() throws Exception {

		String pathname = String.format(filePath, "foo" + File.pathSeparator + "testNew.txt");
		
		File expectedOutputFile = new File(pathname);
		assertThat(expectedOutputFile, is(not(anExistingFile())));
		
		file = new File(pathname);
		String fileContents = "∫ßüøπ˙h∆kkñ";

		writer = new FileWriterWithEncoding(pathname, Encode.UTF_8);
		writer.write(fileContents);
		writer.close();

		assertThat(expectedOutputFile, is(anExistingFile()));
	}

	@Test(groups = "unitTest")
	public void writeUtf8() throws Exception {

		file = new File(String.format(filePath, "utf8.txt"));
		String fileContents = "∫ßüøπ˙h∆kkñ";

		writer = new FileWriterWithEncoding(file, Encode.UTF_8);
		writer.write(fileContents);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("UTF8 encoded file was not written or read back correctly.", lineRead, is(equalTo(fileContents)));
	}

	@Test(groups = "unitTest")
	public void writeISO_8859_1() throws Exception {

		file = new File(String.format(filePath, "iso_8859_1.txt"));
		String fileContents = "Hello World";

		writer = new FileWriterWithEncoding(file, Encode.ISO_8859_1);
		writer.write(fileContents);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("ISO-8859-1 encoded file was not written or read back correctly.", lineRead,
				is(equalTo(fileContents)));
	}

	@Test(groups = "unitTest")
	public void writeInt() throws Exception {

		file = new File(String.format(filePath, "utf8.txt"));
		String fileContents = "\u0003";

		writer = new FileWriterWithEncoding(file, Encode.UTF_8);
		writer.write(3);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("writeInt() operation was not written or read back correctly.", lineRead, is(equalTo(fileContents)));
	}

	@Test(groups = "unitTest")
	public void writeCharBuffer() throws Exception {

		file = new File(String.format(filePath, "utf8.txt"));
		char[] fileContents = new char[] { 'a', 'b', 'c' };

		writer = new FileWriterWithEncoding(file, Encode.UTF_8);
		writer.write(fileContents);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("write() character array operation was not written or read back correctly.", lineRead,
				is(equalTo(String.valueOf(fileContents))));
	}

	@Test(groups = "unitTest")
	public void writeCharBufferWithOffset() throws Exception {

		file = new File(String.format(filePath, "utf8.txt"));
		char[] fileContents = new char[] { 'a', 'b', 'c', 'd', 'e' };

		writer = new FileWriterWithEncoding(file, Encode.UTF_8);
		writer.write(fileContents, 2, 2);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("write() character array from offset operation was not written or read back correctly.", lineRead,
				is(equalTo(String.valueOf(fileContents, 2, 2))));
	}

	@Test(groups = "unitTest")
	public void writeString() throws Exception {

		file = new File(String.format(filePath, "utf8.txt"));
		String fileContents = "∫ßüøπ˙h∆kkñ";

		writer = new FileWriterWithEncoding(file, Encode.UTF_8);
		writer.write(fileContents);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("write() string was not written or read back correctly.", lineRead, is(equalTo(fileContents)));
	}

	@Test(groups = "unitTest")
	public void writeStringWithOffset() throws Exception {

		file = new File(String.format(filePath, "utf8.txt"));
		String fileContents = "∫ßüøπ˙h∆kkñ";

		writer = new FileWriterWithEncoding(file, Encode.UTF_8);
		writer.write(fileContents, 2, 3);
		writer.close();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("write() string from offset was not written or read back correctly.", lineRead,
				is(equalTo(fileContents.substring(2, 5))));
	}

	@Test(expectedExceptions = IOException.class)
	public void confirmFileIsClosed() throws Exception {

		file = new File(String.format(filePath, "iso_8859_1.txt"));
		String fileContents = "Hello World";

		writer = new FileWriterWithEncoding(file, Encode.ISO_8859_1);
		writer.close();
		writer.write(fileContents);
	}

	@Test(groups = "unitTest")
	public void flush() throws Exception {

		file = new File(String.format(filePath, "iso_8859_1.txt"));
		String fileContents = "Hello World";

		writer = new FileWriterWithEncoding(file, Encode.ISO_8859_1);
		writer.write(fileContents);
		writer.flush();

		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
		reader = new BufferedReader(inputStreamReader);
		String lineRead = reader.readLine();

		assertThat("ISO-8859-1 encoded file was not written or read back correctly.", lineRead,
				is(equalTo(fileContents)));
	}

}
