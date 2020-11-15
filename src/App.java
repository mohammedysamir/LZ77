public class App {
    public static void main(String[] args) throws Exception {
        LZ77 l=new LZ77();
        l.Compress();
        l.Decompress();
        System.out.println(l.isEqual());
    }
}
