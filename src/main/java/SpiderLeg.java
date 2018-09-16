import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg implements Runnable {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private String target;


    public SpiderLeg(String target) {
        this.target = target;
    }

    public void run() {
        while (true) {
            String nextUrl = SpiderEngine.toVisit.poll();
            if (nextUrl != null) {
                if (!SpiderEngine.visited.contains(nextUrl)) {
                    SpiderEngine.visited.add(nextUrl);
                    search(nextUrl);
                }
            }
        }
    }

    private void search(String url) {
        System.out.println(SpiderEngine.visited.size() + " " + SpiderEngine.toVisit.size());
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            if (connection.response().statusCode() == 200) {
                //System.out.println("\n**Visiting** Received web page at " + url + " From " + Thread.currentThread().getName());
                if (searchForWord(htmlDocument)) {
                    System.out.println(String.format("Find %s at %s From thread %s", this.target, url, Thread.currentThread().getName()));
                }
            }
            if (!connection.response().contentType().contains("text/html")) {
               // System.out.println("**Failure** Retrieved something other than HTML");
                return;
            }
            Elements linkedPage = htmlDocument.select("a[href]");
            // System.out.println("Found (" + linkedPage.size() + ") links");
            for (Element link : linkedPage) {
                String nextUrl = link.absUrl("href");
                SpiderEngine.toVisit.add(nextUrl);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            System.gc();
        }
    }

    private boolean searchForWord(Document document) {
        String bodyText = document.body().text();
        return bodyText.toLowerCase().contains(this.target.toLowerCase());
    }
}
