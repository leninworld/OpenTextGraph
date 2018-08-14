/*****************************************************************/
/* Copyright 2013 Code Strategies                                */
/* This code may be freely used and distributed in any project.  */
/* However, please do not remove this credit if you publish this */
/* code in paper or electronic form, such as on a web site.      */
/*****************************************************************/

package crawler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.SizeFileComparator;

public class  ReadFile_Sizes_From_GivenFolder {

	public static void displayFiles(String directory) {
		File directoryF = new File(directory);
		File[] files = directoryF.listFiles();
		for (File file : files) {
			System.out.printf("%-20s Size:" + file.length() + "\n", file.getName());
		}
	}

	public static void displayFilesWithDirectorySizes(String directory) {
		File directoryF = new File(directory);
		File[] files = directoryF.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				System.out.printf("%-20s Size:" + FileUtils.sizeOfDirectory(file) + "\n", file.getName());
			} else {
				System.out.printf("%-20s Size:" + file.length() + "\n", file.getName());
			}
		}
	}
	// main
	public static void main(String[] args) throws IOException {
		String inputFolder = null;
		inputFolder="/Users/lenin/Downloads/Feeds-5-Jun/";
		File directoryF = new File(inputFolder);
		
		File[] files = directoryF.listFiles();

		System.out.println("Default order");
		displayFiles(inputFolder);
		System.out.println("-------------------------------------");
		
		Arrays.sort(files, SizeFileComparator.SIZE_COMPARATOR);
		System.out.println("\nSizeFileComparator.SIZE_COMPARATOR (Ascending, directories treated as 0)");
		displayFiles(inputFolder);
		System.out.println("-------------------------------------");
		
		Arrays.sort(files, SizeFileComparator.SIZE_REVERSE);
		System.out.println("\nSizeFileComparator.SIZE_REVERSE (Descending, directories treated as 0)");
		displayFiles(inputFolder);
		System.out.println("-------------------------------------");

		Arrays.sort(files, SizeFileComparator.SIZE_SUMDIR_COMPARATOR);
		System.out.println("\nSizeFileComparator.SIZE_SUMDIR_COMPARATOR (Ascending, directory size used)");
		displayFilesWithDirectorySizes(inputFolder);
		System.out.println("-------------------------------------");

		Arrays.sort(files, SizeFileComparator.SIZE_SUMDIR_REVERSE);
		System.out.println("\nSizeFileComparator.SIZE_SUMDIR_REVERSE (Descending, directory size used)");
		displayFilesWithDirectorySizes(inputFolder);
		System.out.println("-------------------------------------");
	}
	
}
