package fileSystem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class FileNameExistsException extends Exception{
    public FileNameExistsException(String fileName,String folderName) {
        super(String.format("There is already a file named %s in the folder %s",fileName,folderName));
    }
}
interface IFile extends Comparable<IFile> {
    String getFileName();

    long getFileSize();

    String getFileInfo(int tabs);

    void sortBySize();
    long findLargestFile ();
}

class IndentPrinter{

    public static String printIndentation(int level){
        return "\t".repeat(level);
    }
}
class File implements IFile {

    private String fileName;
    private long size;

    public File(String name, long size) {
        this.fileName = name;
        this.size = size;
    }

    public File(String name) {
        this.fileName = name;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public long getFileSize() {
        return this.size;
    }

    /*
    String репрезентацијата на една обична датотека е
     File name [името на фајлот со 10 места порамнето на десно]
    File size: [големината на фајлот со 10 места пораменета на десно ]
     */

    @Override
    public String getFileInfo(int tabs) {
        return String.format("%sFile name: %10s File size: %10d\n",IndentPrinter.printIndentation(tabs),getFileName(),getFileSize());
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public long findLargestFile() {
        return this.size;
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(this.size,o.getFileSize());
    }
}

class Folder extends File implements IFile{

    private List<IFile> files;

    public Folder(String name){
        super(name);
        files=new ArrayList<>();
    }

    private boolean ifNameExists(String name){

        return files.stream()
                .map(IFile::getFileName)
                .anyMatch(i->i.equals(name));

    }
    public void addFile (IFile file) throws FileNameExistsException {
        if(ifNameExists(file.getFileName()))
            throw new FileNameExistsException(file.getFileName(),this.getFileName());

        files.add(file);
    }


    @Override
    public String getFileName() {
        return super.getFileName();
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int tabs) {
        StringBuilder stringBuilder=new StringBuilder();

        stringBuilder.append(String.format("%sFolder name: %10s Folder size: %10d\n",
                IndentPrinter.printIndentation(tabs),getFileName(),getFileSize()));

        files.stream().forEach(i->stringBuilder.append(i.getFileInfo(tabs+1)));

        return stringBuilder.toString();
    }

    @Override
    public void sortBySize() {

        files.stream().sorted().forEach(IFile::sortBySize);

    }

    @Override
    public long findLargestFile() {
        OptionalLong largest=files.stream().mapToLong(i->i.findLargestFile()).max();

        if(largest.isPresent())
            return largest.getAsLong();
        else
            return 0L;
    }
}

 class FileSystem{

    private Folder rootDirectory;

     public FileSystem(Folder rootDirectory) {
         this.rootDirectory = rootDirectory;
     }

     public FileSystem() {
         rootDirectory=new Folder("root");
     }

     void addFile (IFile file) throws FileNameExistsException {
         rootDirectory.addFile(file);

     }

     long findLargestFile (){

         return rootDirectory.findLargestFile();
     }

     void sortBySize(){

         rootDirectory.sortBySize();
     }

     @Override
     public String toString() {
         return rootDirectory.getFileInfo(0);
     }
 }




public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}
