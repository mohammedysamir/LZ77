import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class LZ77 {
    private ArrayList<String> Search=new ArrayList<>();
    private ArrayList<String> look_Ahead=new ArrayList<>();
    private ArrayList<Tags> tags=new ArrayList<Tags>();
    private String OriginalPath = "C:/Users/mohammed/Desktop/Multimedia/LZ77/src/OriginalText.txt";
    private String CompressPath = "C:/Users/mohammed/Desktop/Multimedia/LZ77/src/CompressedText.txt";
    private String DecompressPath = "C:/Users/mohammed/Desktop/Multimedia/LZ77/src/DecompressedText.txt";
    String content="";  
    int SearchSize,look_AheadSize=3;
    public void Compress() throws IOException {
        Path path = Paths.get(OriginalPath);
        content = Files.readString(path, StandardCharsets.US_ASCII);
        //String pass;
        /**
         * content:ABCDR
         * look_Ahead :
         * Search : C |D| R| 
         */
        int indexofText=0;
        for (; indexofText < content.length()&&indexofText<look_AheadSize; indexofText++)
            look_Ahead.add(String.valueOf(content.charAt(indexofText)));// Fill look ahead buffer
            ///try new condition
           while(!look_Ahead.isEmpty()) {
                if (!Search.contains(look_Ahead.get(0))) {// Substring NotFound in searchbuffer
                    Tags t = new Tags();
                    t.set_Tags(0, 0, look_Ahead.get(0).toString());
                    tags.add(t);
                    // remove from Look ahead and add to Search
                    Search.add(look_Ahead.get(0).toString());
                    look_Ahead.remove(look_Ahead.get(0));
                    if(indexofText<content.length()){
                        look_Ahead.add(String.valueOf(content.charAt(indexofText++)));//Adding new character to the buffer
                    }
                } else {
                    // found matching
                    //pos=end-index of matching;
                    //len= try get matching from search and count length
                    //nc=nc+content.charAt(indexofText++);
                }
                //Check search buffer size
            }
            ///
            FileWriter out = new FileWriter(CompressPath);
            for (int i = 0; i < tags.size(); i++)
                out.write(tags.get(i).toString());
            out.close();

    }

    public void Decompress() throws IOException {
        File inputFile=new File(CompressPath);
        FileWriter outputFile=new FileWriter(DecompressPath);
        Scanner scan=new Scanner(inputFile);
        String content="";
        while(scan.hasNextLine()){ //still there is line to read
            String Tag=scan.nextLine();
            String arrofTags[]=Tag.split(",");
            int pos=Integer.parseInt(arrofTags[0]);
            int len=Integer.parseInt(arrofTags[1]);
            String nextChar=arrofTags[2];
            if(pos==0&&len==0)//first time appear in the original 
               { 
                   outputFile.write(nextChar);
                   content+=nextChar; //adding char to content
               }
            else{ //appeard once before at leasts
                String o="";
                int beginofSubString=content.length()-pos;
                //there is position value , so go back p values to get char
                o+=content.charAt(beginofSubString);
                //take length substring and add it
                o+=content.substring(beginofSubString+1, beginofSubString+len);
                //add nextchar
                o+=nextChar;
                //print in file
                outputFile.write(o);
                content+=o;
            }
        }
        scan.close();
        outputFile.close();
    }
}