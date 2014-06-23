/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package utils.logic;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import utils.conf.SharedPref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YouTubeSearch {

	/**
	 * Global application name
	 */
	private static String Application_Name = "HebrewSongDownloader";

	/**
	 * Global instance of the HTTP transport.
	 */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/**
	 * Global instance of the JSON factory.
	 */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/**
	 * Global instance of the max number of videos we want returned (50 = upper limit per page).
	 */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 10;

	/**
	 * Global instance of Youtube object to make all API requests.
	 */
	private static YouTube youtube;

	/**
	 * Initializes YouTube object to search for videos on YouTube (Youtube.Search.List). The program
	 * then prints the names and thumbnails of each of the videos (only first 50 videos).
	 * <p/>
	 * TODO : SearchResult is probably too low-level for a library withing the application,
	 * should process the search result and transform it to a relevant class
	 *
	 * @param query
	 * @return
	 */
    public static List<SearchResult> getSearchResults(String query) {
        List<SearchResult> searchResultList = new ArrayList<SearchResult>();

        try {
		    /* The YouTube object is used to make all API requests. The last argument is required, but
            * because we don't need anything initialized when the HttpRequest is initialized, we override
            * the interface and provide a no-op function.
            */
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName(Application_Name).build();

            YouTube.Search.List search = youtube.search().list("id,snippet");

      /*
       * It is important to set your developer key from the Google Developer Console for
       * non-authenticated requests (found under the API Access tab at this link:
       * code.google.com/apis/). This is good practice and increased your quota.
       */
            String apiKey = SharedPref.YouTubeApiKey;
            search.setKey(apiKey);
            search.setQ(query);
      /*

       * We are only searching for videos (not playlists or channels). If we were searching for
       * more, we would add them as a string like this: "video,playlist,channel".
       */
            search.setType("video");
      /*
	   * This method reduces the info returned to only the fields we need and makes calls more
       * efficient.
       */
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
                //TODO Delete its annoying!!!
//                prettyPrint(searchResultList.iterator(), query);
            }

        } catch (GoogleJsonResponseException e) {
            LogUtils.logData("flow_debug", "There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            LogUtils.logData("flow_debug", "There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return searchResultList;
    }

	/*
	   * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
	   * thumbnail.
	   *
	   * @param iteratorSearchResults Iterator of SearchResults to print
	   *
	   * @param query Search query (String)
	   */
	private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        LogUtils.logData("flow_debug", "\n=============================================================");
        LogUtils.logData("flow_debug", (
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\"."));
        LogUtils.logData("flow_debug", "=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
            LogUtils.logData("flow_debug", " There aren't any results for your query.");
        }

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Double checks the kind is video.
			if (rId.getKind().equals("youtube#video")) {
				Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

                LogUtils.logData("flow_debug", " Video Id" + rId.getVideoId());
                LogUtils.logData("flow_debug", " Title: " + singleVideo.getSnippet().getTitle());
                LogUtils.logData("flow_debug", " Thumbnail: " + thumbnail.getUrl());
                LogUtils.logData("flow_debug", "\n-------------------------------------------------------------\n");
            }
		}
	}
}
