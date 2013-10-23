// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.responders.search;

import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureReadOperation;
import fitnesse.authentication.SecureResponder;
import fitnesse.components.TraversalListener;
import fitnesse.components.Traverser;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.responders.ChunkingResponder;
import fitnesse.responders.templateUtilities.HtmlPage;
import fitnesse.responders.templateUtilities.PageTitle;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;

public abstract class ResultResponder extends ChunkingResponder implements
        SecureResponder, Traverser<Object>, ResultResponderStrategy {

    private final boolean shouldRespondWith404;

    protected ResultResponder() {
        this(true);
    }

    protected ResultResponder(boolean shouldRespondWith404) {
        this.shouldRespondWith404 = shouldRespondWith404;
    }

    protected PageCrawler getPageCrawler() {
        return root.getPageCrawler();
    }

    protected void doSending() {
        HtmlPage htmlPage = context.pageFactory.newPage();
        htmlPage.setTitle(getTitle());
        htmlPage.setPageTitle(new PageTitle(getTitle()) {
            public String getTitle() {
                return "search";
            }

            public String getLink() {
                return null;
            }
        });
        htmlPage.setMainTemplate("searchResults");

        if (page == null)
            page = context.root.getPageCrawler().getPage(context.root, PathParser.parse("FrontPage"));
        if (request.getQueryString() == null || request.getQueryString().equals(""))
            htmlPage.put("request", request.getBody());
        else
            htmlPage.put("request", request.getQueryString());
        htmlPage.put("page", page);
        htmlPage.put("viewLocation", request.getResource());
        htmlPage.setNavTemplate("viewNav");
        htmlPage.put("resultResponder", this);

        htmlPage.render(response.getWriter());

        response.closeAll();
    }

    protected String getTitle() {
        return getTitleForStrategy(request);
    }

    public void traverse(TraversalListener<Object> observer) {
        traverseForStrategy(observer, page, request, response, root);
    }

    public SecureOperation getSecureOperation() {
        return new SecureReadOperation();
    }

    protected boolean shouldRespondWith404() {
        return shouldRespondWith404;
    }
}


