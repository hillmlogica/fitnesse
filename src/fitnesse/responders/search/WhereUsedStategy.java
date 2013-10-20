// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.responders.search;

import fitnesse.components.TraversalListener;
import fitnesse.components.WhereUsedPageFinder;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.wiki.WikiPage;

public class WhereUsedStategy implements ResultResponderStrategy {


    private WhereUsedStategy() {
    }

    public static DelegatingResultResponder createWhereUsedResponder() {
        return new DelegatingResultResponder(new WhereUsedStategy(), true);
    }

    @Override
    public void traverse(TraversalListener<Object> observer, WikiPage page, WikiPage root, Request request, ChunkedResponse response) {
        new WhereUsedPageFinder(page, observer).search(root);
    }

    public String getTitle(Request request) {
        return "Where Used Results";
    }

}
