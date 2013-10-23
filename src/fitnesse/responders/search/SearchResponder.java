// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.responders.search;

import fitnesse.components.RegularExpressionWikiPageFinder;
import fitnesse.components.TitleWikiPageFinder;
import fitnesse.components.TraversalListener;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.wiki.WikiPage;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.LITERAL;

public class SearchResponder extends ResultResponder {
    private SearchResponder() {
        super(false);
    }

    public static ResultResponder createSearchResponder() {
        return new SearchResponder();
    }

    private String getSearchString(Request request) {
        return (String) request.getInput("searchString");
    }

    private String getSearchType(Request request) {
        String searchType = (String) request.getInput("searchType");

        if (searchType == null || searchType.toLowerCase().contains("title"))
            return "Title";
        else
            return "Content";
    }

    protected String getTitleForStrategy(Request request) {
        return getSearchType(request) + " Search Results for '" + getSearchString(request) + "'";
    }

    @Override
    public void traverseForStrategy(TraversalListener<Object> observer, WikiPage page, Request request, ChunkedResponse response, WikiPage root) {
        String searchString = getSearchString(request);
        if (!"".equals(searchString)) {
            String searchType = getSearchType(request);
            if ("Title".equals(searchType))
                new TitleWikiPageFinder(searchString, observer).search(root);
            else {
                Pattern regularExpression = Pattern.compile(searchString, CASE_INSENSITIVE + LITERAL);
                new RegularExpressionWikiPageFinder(regularExpression, observer).search(root);
            }
        }
    }
}
