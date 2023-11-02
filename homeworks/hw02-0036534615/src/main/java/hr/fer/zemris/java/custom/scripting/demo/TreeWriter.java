package hr.fer.zemris.java.custom.scripting.demo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.INodeVisitor;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.util.FilesUtil;

public class TreeWriter {
	
	
	public static class WriterVisitor implements INodeVisitor{

		@Override
		public void visitTextNode(TextNode node) {
			System.out.println(node.toString());
		    for(int i=0;i<node.numberOfChilden();i++) {
		    	   node.getChild(i).accept(this);
		    }
			
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			//System.out.println(node.toString());
		     for(int i=0;i<node.numberOfChilden();i++) {
		    	   node.getChild(i).accept(this);
		     }
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.println(node.toString());
		       for(int i=0;i<node.numberOfChilden();i++) {
		    	   node.getChild(i).accept(this);
		    }
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
		   System.out.println(node.toString());
	       for(int i=0;i<node.numberOfChilden();i++) {
	    	   node.getChild(i).accept(this);
	       }
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
		/*if(args.length != 1) {
			throw new IllegalArgumentException();
		}*/
		
		String docBody = FilesUtil.readFromFile("src/main/resources/osnovni.smscr");
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode node = parser.getDocumentNode();
		
	}
	
	
}
