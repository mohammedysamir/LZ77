import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class LZ77 {
    private ArrayList<String> Search = new ArrayList<>();
    private ArrayList<String> look_Ahead = new ArrayList<>();
    private ArrayList<Tags> tags = new ArrayList<Tags>();
    private String OriginalPath = "C:/Users/mohammed/Desktop/Multimedia/LZ77/src/alphabet.txt";
    private String CompressPath = "C:/Users/mohammed/Desktop/Multimedia/LZ77/src/CompressedText.txt";
    private String DecompressPath = "C:/Users/mohammed/Desktop/Multimedia/LZ77/src/DecompressedText.txt";
    String content = "";
    int SearchSize = 1024, look_AheadSize =50;

    public void Compress() throws IOException {
        tags.clear();
        Path path = Paths.get(OriginalPath);
        content = ReadFromFile(path);
        int indexofText = 0;
        for (; indexofText < content.length() && indexofText < look_AheadSize; indexofText++)
            look_Ahead.add(String.valueOf(content.charAt(indexofText)));// Fill look ahead buffer
        while (!look_Ahead.isEmpty()) {
            if (!Search.contains(look_Ahead.get(0))) {// Substring NotFound in searchbuffer
                Tags t = new Tags();
                t.set_Tags(0, 0, look_Ahead.get(0).toString());
                tags.add(t);
                // remove from Look ahead and add to Search
                Search.add(look_Ahead.get(0).toString());
                look_Ahead.remove(look_Ahead.get(0));

                if (indexofText < content.length()) {
                    look_Ahead.add(String.valueOf(content.charAt(indexofText++)));// Adding new character to the buffer
                }
            } else {// matching found
                // pos=end-index of matching;
                int pos = Search.size() - Search.lastIndexOf(look_Ahead.get(0));
                // len= try get matching from search and count length
                int len = 1; // default value
                int iter = 1;
                String LMatching = look_Ahead.get(0);
                String SMatching = Search.get(Search.size() - pos);
                // need to recheck condition
                while (LMatching.equals(SMatching) && iter < pos && iter < look_Ahead.size()) {
                    len++;
                    LMatching += look_Ahead.get(iter);
                    SMatching += Search.get(Search.size() - pos + iter);
                    iter++;
                }
                // nc=nc+content.charAt(indexofText++);
                String nc = Character.toString(LMatching.charAt(LMatching.length() - 1));
                Tags t = new Tags();
                t.set_Tags(pos, len-1, nc); // define tag and set properties
                tags.add(t); // add it to array to print
                // shift by len+1
                for (; len > 0; len--) {
                    if (Search.size() == SearchSize)
                        Search.remove(0);// if buffer is full
                    Search.add(look_Ahead.get(0));
                    look_Ahead.remove(0);
                    if (indexofText < content.length()) {
                        // Adding new character to the buffer
                        look_Ahead.add(String.valueOf(content.charAt(indexofText++)));
                    }
                }
            }
        }
        FileWriter out = new FileWriter(CompressPath);
        ;
        WriteToFile(out, tags);
    }

    public void Decompress() throws IOException {
        tags.clear();
        File inputFile = new File(CompressPath);
        FileWriter outputFile = new FileWriter(DecompressPath);
        Scanner scan = new Scanner(inputFile);
        String content = "";
        while (scan.hasNextLine()) { // still there is line to read
            String Tag = scan.nextLine();
            String arrofTags[] = Tag.split(",");
            int pos = Integer.parseInt(arrofTags[0]);
            int len = Integer.parseInt(arrofTags[1]);
            String nextChar = arrofTags[2];
            if (pos == 0 && len == 0)// first time appear in the original
                content += nextChar; // adding char to content
            else { // appeard once before at leasts
                String o = "";
                int beginofSubString = content.length() - pos;
                // there is position value , so go back p values to get char
                //o += content.charAt(beginofSubString);
                // take length substring and add it
                o += content.substring(beginofSubString, beginofSubString + len);
                // add nextchar
                o += nextChar;
                content += o;
            }
        }
        outputFile.write(content);//print into file 
        scan.close();
        outputFile.close();
    }

    private void WriteToFile(FileWriter out, ArrayList<Tags> tags) throws IOException {
        for (int i = 0; i < tags.size(); i++)
            out.write(tags.get(i).toString());
        out.close();
    }

    private String ReadFromFile(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8).replace("\n", "").replace("\r", "");
        // remove carrige return and line feed
    }

    public boolean isEqual() throws IOException {
        String Original=ReadFromFile(Paths.get(OriginalPath));
        String ResultDecompress=ReadFromFile(Paths.get(DecompressPath));
        return Original.equals(ResultDecompress)?true:false;
    }
}