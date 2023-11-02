package hr.fer.oprpp2.hw03.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import hr.fer.oprpp2.hw03.models.Band;
import hr.fer.oprpp2.hw03.models.PoolResult;

public class FileUtil {

	
	
	public static List<Band> parseFileToBands(String fileName) throws FileNotFoundException{
		String fileContent = readFromFile(fileName);
		
		String[] rows = fileContent.split("\n");
		
		List<Band> bands = new ArrayList<>();
		for(String row : rows) {
			String[] elements = row.split(" ");
			
			bands.add(new Band(elements[0],elements[1]+" "+elements[2],elements[3]));
		}
		
		return bands;
	}
	
	
	public static void createResultTemplate(String fileName,List<Band> bands) {
		
		bands.forEach((band) -> {
			String result = band.getId() +" "+0;
			try {
				writeToFile(result,fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	public static List<PoolResult> parseResults(String fileName){
		String content = "";
		try {
			content = readFromFile(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] contentSplited = content.split("\n");
		
		List<PoolResult> results = new ArrayList<>();
		
		for(String row : contentSplited) {
			String[] elements = row.split(" ");
			
			results.add(new PoolResult(elements[0],elements[1]));
		}
		
		return results;
	}
	
	
	public static void writeToFile(String text,String filePath) throws IOException {
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true)) ){
			writer.write(text+"\n");
			writer.close();
		}
	}
	
	public static String readFromFile(String filePath) throws FileNotFoundException {
		String result = "";
		try(Scanner scanner = new Scanner(new File(filePath))){
			 String tempResult = "";
			 while(scanner.hasNext()) {
				 tempResult = scanner.nextLine();
				 if (tempResult.isEmpty()) continue;
				 result+= tempResult+"\n";
			 }
		}
		return result;
	}
	
	public static void deleteContentFromFile(String filePath) {
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));
			writer.write("");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
