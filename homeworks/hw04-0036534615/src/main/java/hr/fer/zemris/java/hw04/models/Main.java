package hr.fer.zemris.java.hw04.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("C:\\Projects\\fakultet\\OPRPP2\\hw04-0036534615\\src\\main\\webapp\\WEB-INF\\polls.txt"));
		
		for(String line : lines) {
			System.out.println(line);
		}
	}
}
