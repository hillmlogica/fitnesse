// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.responders.search;

import fitnesse.components.TraversalListener;
import fitnesse.components.WhereUsedPageFinder;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.wiki.WikiPage;

public class WhereUsedResponder implements ResultResponderStrategy {
    public static ResultResponder createWhereUsedResponder() {
        return new ResultResponder(new WhereUsedResponder());
    }

    @Override
    public void traverseForStrategy(TraversalListener<Object> observer, WikiPage page, Request request, ChunkedResponse response, WikiPage root) {
        new WhereUsedPageFinder(page, observer).search(root);
    }

    public String getTitleForStrategy(Request request) {
        return "Where Used Results";
    }
}
