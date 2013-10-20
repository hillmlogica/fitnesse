package fitnesse.responders.refactoring;

import fitnesse.components.ContentReplacingSearchObserver;
import fitnesse.components.PageFinder;
import fitnesse.components.RegularExpressionWikiPageFinder;
import fitnesse.components.TraversalListener;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.responders.search.DelegatingResultResponder;
import fitnesse.responders.search.ResultResponder;
import fitnesse.responders.search.ResultResponderStrategy;
import fitnesse.wiki.WikiPage;

public class SearchReplaceResponder implements ResultResponderStrategy, TraversalListener<WikiPage> {

    private TraversalListener<? super WikiPage> contentReplaceObserver;
    private TraversalListener<? super WikiPage> webOutputObserver;

    private SearchReplaceResponder() {
    }

    public static ResultResponder createSearchReplaceResponder() {
        return new DelegatingResultResponder(new SearchReplaceResponder(), true);
    }

    public String getTitle(Request request) {
        return String.format("Replacing matching content \"%s\" with content \"%s\"",
                getSearchString(request), getReplacementString(request));
    }

    private String getReplacementString(Request request) {
        return (String) request.getInput("replacementString");
    }

    private String getSearchString(Request request) {
        return (String) request.getInput("searchString");
    }

    public void process(WikiPage page) {
        contentReplaceObserver.process(page);
        webOutputObserver.process(page);
    }

    @Override
    public void traverse(TraversalListener<Object> observer, WikiPage page, WikiPage root, Request request, ChunkedResponse response) {
        webOutputObserver = observer;
        String searchString = getSearchString(request);
        String replacementString = getReplacementString(request);

        contentReplaceObserver = new ContentReplacingSearchObserver(searchString, replacementString);
        PageFinder finder = new RegularExpressionWikiPageFinder(searchString, this);
        finder.search(page);
    }

}
