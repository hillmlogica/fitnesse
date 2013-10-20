package fitnesse.responders.search;

import fitnesse.components.TraversalListener;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.wiki.WikiPage;

public interface ResultResponderStrategy {
    String getTitle(Request request);

    void traverse(TraversalListener<Object> observer, WikiPage page, WikiPage root, Request request, ChunkedResponse response);
}
