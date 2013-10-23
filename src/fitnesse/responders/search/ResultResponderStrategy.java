package fitnesse.responders.search;

import fitnesse.components.TraversalListener;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.wiki.WikiPage;

public interface ResultResponderStrategy {
    String getTitleForStrategy(Request request);

    void traverseForStrategy(TraversalListener<Object> observer, WikiPage page, Request request, ChunkedResponse response, WikiPage root);
}
