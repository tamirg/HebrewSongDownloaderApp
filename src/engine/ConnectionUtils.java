package engine;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Tamir on 20/06/2014.
 */
public class ConnectionUtils {

    public static Document connectToURL(String urlToConnect) throws IOException {
        Connection.Response response = Jsoup.connect(urlToConnect)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip,deflate,sdch")
                .header("Accept-Language", "en-US,en;q=0.8")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Cookie", "PHPSESSID=4f6mrg4vvij23h14hk63970cb4; visid_incap_46879=gHs/SCCZRk60JPQZvA4FkWpzpFMAAAAAQUIPAAAAAAC/8HlZ3JrG++8veAocjprq; incap_ses_86_46879=Jm03bZx2wFvgLbRDUYkxAWpzpFMAAAAAVOG8cokYZS9ErQcWPM2xSQ==; _ga=GA1.2.598400109.1403286378")
                .header("Host", "www.unidown.com")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36")
                .followRedirects(true)
                .method(Connection.Method.GET)
                .execute();
        Document pageDoc = response.parse();

        return pageDoc;
    }

    public static Document songDownloadParsedHTML(String urlToConnect) throws IOException {
        Connection.Response response = Jsoup.connect(urlToConnect)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip,deflate,sdch")
                .header("Accept-Language", "en-US,en;q=0.8")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Cookie", "PHPSESSID=5d7kef4tb0nllvtvviajaie472; dwn=TEtnRUVCb1VyeTE1; visid_incap_46879=gHs/SCCZRk60JPQZvA4FkWpzpFMAAAAAQUIPAAAAAAC/8HlZ3JrG++8veAocjprq; incap_ses_86_46879=K0mQCsMlVwZ+iMBDUYkxAU+KpFMAAAAAIhADh/BTBATvW0f0TNjxsg==; _ga=GA1.2.598400109.1403286378")
                .header("Host", "www.unidown.com")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36")
                .followRedirects(true)
                .method(Connection.Method.GET)
                .execute();
        Document pageDoc = response.parse();

        return pageDoc;
    }

}
