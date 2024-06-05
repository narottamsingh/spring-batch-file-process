package com.bce.batch.config;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZipFileLister {

    public Map<String, List<String>> listSourceZip(String folder) throws IOException {
    	
    	 
           
           
        Map<String, List<String>> m = new HashMap<>();
        File folderDir = new File(folder);

        // Find all zip files in the specified folder
        Map<String, List<String>> oneFolderMap = Files.find(Paths.get(folderDir.getPath()), 1,
                (path, attr) -> path.toFile().getName().endsWith(".zip"))
                .map(e -> {
                    return e;
                })
                .collect(Collectors.groupingBy(p -> "test:" + p.getParent().toString().trim(),
                        Collectors.mapping(p -> p.getParent().toString().trim()+p.getFileName().toString().trim(), Collectors.toList())));

        return oneFolderMap;
    }
    
   
    
    
    
    public Map<String, List<Path>> listSourceZipT(String folder) throws IOException {
        Map<String, List<Path>> m = new HashMap<>();
        File folderDir = new File(folder);

        // Find all zip files in the specified folder
        Map<String, List<Path>> oneFolderMap = Files.find(Paths.get(folderDir.getPath()), 1,
                (path, attr) -> path.toFile().getName().endsWith(".zip"))
                .map(e -> {
                    System.out.println(e.toFile().toString());
                    return e;
                })
                .collect(Collectors.groupingBy(
                        p -> p.getFileName().toString().trim() + p.getParent().toString(),
                        Collectors.mapping(p -> p, Collectors.toList())
                ));

        return oneFolderMap;
    }

    
    

    public static void main(String[] args) {
        ZipFileLister lister = new ZipFileLister();
        try {
        	
        	String str="  sjhs djhdjh d  dd";
        	str=str.replaceAll("\\s", "");
        	System.out.println(str);
        	
            Map<String, List<String>> result = lister.listSourceZip("/home/narottam/test/");
			/*
			 * result.forEach((folder, files) -> { System.out.println("Folder: " + folder);
			 * System.out.println("Files: " + files); });
			 */
            System.out.println(result);
            System.out.println("\n\n");
           // Map<String, List<Path>> result1 = lister.listSourceZipT("/home/narottam/test/");
            //System.out.println(result1);
			/*
			 * result1.forEach((folder, files) -> { System.out.println("Folder: " + folder);
			 * System.out.println("Files: " + files); });
			 */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
