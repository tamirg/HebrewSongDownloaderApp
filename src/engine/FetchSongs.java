package engine;

import Exceptions.NoSongFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Tamir on 24/01/14.
 */
public class FetchSongs {
    private static final String UNIDOWN_URL_QUERY = "http://www.unidown.com/search.php?q=";
    static final String QUERY_ENCODING = "UTF-8";
    public static final String SONG_FILE_MP3_SUFFIX = ".mp3";

    public static URI parseUrl(String s) throws Exception {
        URL u = new URL(s);
        return new URI(
                u.getProtocol(),
                u.getAuthority(),
                u.getPath(),
                u.getQuery(),
                u.getRef());
    }

    public static String[] downloadHebrewSong(String nameOfSong, final String directoryForSong) throws Exception {
        nameOfSong = URLEncoder.encode(nameOfSong, QUERY_ENCODING);
        Document pageDoc = Jsoup.connect(UNIDOWN_URL_QUERY + nameOfSong).get();
        Elements downloadButton = pageDoc.getElementsByClass("download_button");
        if (downloadButton.isEmpty()) {
            throw new NoSongFoundException();
        } else {
            String songDownloadURL = downloadButton.first().child(0).attr("abs:href");

            Document songDownloadParsedHTML = Jsoup.connect(songDownloadURL).get();
            Elements elementsWithClassIframe = songDownloadParsedHTML.getElementsByClass("iframecontent").select("iframe");
            String iframeURL = elementsWithClassIframe.attr("abs:src");

            Document iframeSongDownloadDocument = Jsoup.connect(iframeURL).get();
            String songDownloadIframe = iframeSongDownloadDocument.getElementsByTag("iframe").attr("abs:src");

            // Gets the iframe for the download, can be more than one website
            Document iframeParsedHTML = Jsoup.connect(songDownloadIframe).userAgent("Mozilla").get();

            String songFinalDownloadURL = "";
            String songFileName = "";

            // Works for freeTUBEconvertor site!
            if (songDownloadIframe.contains("freetubeconvertor")) {
                songFinalDownloadURL = iframeParsedHTML.getElementById("downloadbox").child(1).child(0).attr("abs:href");
                songFileName = iframeParsedHTML.getElementById("downloadbox").child(0).text();
            }
            // Works for videotomp3now site!
            else if (songDownloadIframe.contains("videotomp3now")) {
                songFinalDownloadURL = iframeParsedHTML.getElementById("downloadbutton").child(0).attr("abs:href");
                songFileName = iframeParsedHTML.getElementById("url").children().select("h1").text();
            } else if (songDownloadIframe.contains("mp3tuber")) {
                songFinalDownloadURL = iframeParsedHTML.select("p:has(a)").first().child(0).attr("abs:href");
                songFileName = iframeParsedHTML.select("p[dir]").first().text();
            }

            //createHebrewSong(directoryForSong, songFinalDownloadURL, songFileName);
            return new String[]{directoryForSong, songFinalDownloadURL, songFileName};
        }
    }

    private static void createHebrewSong(String directoryForSong, String songFinalDownloadURL, String songFileName) throws IOException, Exception {
        URI songFinalDownloadURI = parseUrl(songFinalDownloadURL);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(songFinalDownloadURI.toString());
        HttpResponse responseSongFile = httpClient.execute(httpRequest);
        int songFileLengthInBytes;

        try {
            HttpEntity entity = responseSongFile.getEntity();
            songFileLengthInBytes = (int) entity.getContentLength();
            BufferedInputStream bfInputStream = new BufferedInputStream(entity.getContent());
            String filePath = directoryForSong + File.separator + songFileName + SONG_FILE_MP3_SUFFIX;
            BufferedOutputStream bfOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            int inByte = bfInputStream.read();
            while (inByte != -1) {
                bfOutputStream.write(inByte);
                inByte = bfInputStream.read();
            }

            bfInputStream.close();
            bfOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}