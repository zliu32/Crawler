import java.util.ArrayList;
import java.util.List;

public class SpiderMain {


    public static void main(String[] args) {
      /*  Crawler crawler = new Crawler();
        crawler.startCrawler("http://www.baidu.com/", "China");*/
        List<String> seeds = new ArrayList<String>();
        seeds.add("http://www.baidu.com/");
        SpiderEngine engine = new SpiderEngine();
        engine.run(seeds);
    }
}
