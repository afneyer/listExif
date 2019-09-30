package com.afn.pictures.listExif;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Start Photo Taken Analysis");

		final File dir = new File("C:\\Users\\Kathleen\\Pictures");

		for (File photoDir : dir.listFiles()) {
			String photoDirName = photoDir.getName();
			// System.err.format("INFO: PhotoDir = |%s|\n",photoDirName);
			if (photoDir.isDirectory()) {
				
				/* Find earliest and latest date each photo directory */
				Date earliestDate = null;
				Date latestDate = null;
				if (!photoDirName.startsWith("+") && ! startsWithDate(photoDirName) ) {
				for (final File photo : photoDir.listFiles()) {
					String fileName = photo.getName();
					if (photo.isFile() && ! ignoreFile (fileName)) {
						Date photoDate = getOriginalDate(photo);
						if (earliestDate == null) {
							earliestDate = photoDate;
						}
						if (latestDate == null) {
							latestDate = photoDate;
						}
						// System.err.format("---INFO: Processing Photo File = |%s|%s|\n",photo,photoDate);
						if (photoDate == null) {
							// System.err.format("INFO: No date for %s name %s is not a file\n", photoDirName, fileName);
						} else {
						if (! photoDate.after(earliestDate)) {
							earliestDate = photoDate;
						}
						if (! photoDate.before(latestDate)) {
							latestDate = photoDate;
						}
						}
					} else {
						System.err.format("WARN: Entry in photo directory %s name %s is not a file\n", photoDirName, fileName);
					}
				}
				}
				if ( earliestDate != null && latestDate != null ) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String dateString = df.format(earliestDate);
					String newFileName = dateString + " " + photoDirName;
					System.out.format("New Photo Dir Name = |%s|\n",newFileName);
				}
				
			} else {
				//
			}
		}
	}

	static boolean ignoreFile(String fileName) {
		String ext = getExtension(fileName);
		if ( ext != null && ext.equals("ini")) {
			return true;
		}
		return false;
	}

	static String getExtension(String fileName) {
		if (fileName != null) {
			 String [] parts = fileName.split("\\.");
			 if ( parts.length >= 1 ) {
				 return parts[parts.length-1];
			 }
		}
		return null;
	}

	public static Date getOriginalDate( File file ) {
		Date origDate = null;
		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (Exception e) {
			System.err.format("ERROR: Image processing error for dir=%s , file=%s\n", file.getPath(), file.getName());
			return null;
		};
		
		if (metadata == null) {
			return null;
		}
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		if (directory != null) {
		if (directory.hasErrors()) {
			for (String error : directory.getErrors()) {
				System.err.format("ERROR: ExifDirectory error %s for dir=%s , file=%s \n", error, file.getPath(), file.getName());
			}
		}
		origDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		} else {
			System.err.format("ERROR: Image has no ExifSubDirectory dir=%s , file=%s \n", file.getPath(), file.getName());
		}
		
		return origDate;
	}
	
	public static boolean startsWithDate (String str) {
		String start = str.substring(0,3);
		if ( isNumeric(start) ) {
			return true;
		}
		return false;
	}
	private static boolean isNumeric(String str) {
		try {
	        int i = Integer.parseInt(str);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
		return true;
	}

	/*
	 * Maybe obsolete
	 *
	 */
	// TODO remove
	public static void printMetaData(File file) {

		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (ImageProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		if (directory.hasErrors()) {
			for (String error : directory.getErrors()) {
				System.err.format("ERROR: %s", error);
			}
		}
		Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		System.out.format("[%s] - %s\n", file.getName(), date);

	}

}
