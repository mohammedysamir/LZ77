import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class LZ77 {
    private Queue<String> Search=new LinkedList<>();
    private Queue<String> look_Ahead=new LinkedList<>();
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
                if (!Search.contains(look_Ahead.peek())||Search.isEmpty()) {// Substring NotFound in searchbuffer
                    Tags t = new Tags();
                    t.set_Tags(0, 0, look_Ahead.peek().toString());
                    tags.add(t);
                    // remove from Look ahead and add to Search
                    Search.add(look_Ahead.peek().toString());
                    look_Ahead.remove();
                    if(indexofText<content.length()){
                        look_Ahead.add(String.valueOf(content.charAt(indexofText++)));//Adding new character to the buffer
                    }
                } else {
                    // found matching
                    //pos=end-index of matching;
                    //len
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

    public void Decompress() {
        
    }
}