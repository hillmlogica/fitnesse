package fitnesse.responders.search;

import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureReadOperation;
import fitnesse.authentication.SecureResponder;
import fitnesse.components.TraversalListener;
import fitnesse.components.Traverser;
import fitnesse.responders.ChunkingResponder;
import fitnesse.responders.templateUtilities.HtmlPage;
import fitnesse.responders.templateUtilities.PageTitle;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PathParser;

public class DelegatingResultResponder extends ChunkingResponder implements SecureResponder, Traverser<Object> {
    private final ResultResponderStrategy strategy;
    private final boolean shouldRespondWith404;

    public DelegatingResultResponder(ResultResponderStrategy strategy, boolean shouldRespondWith404) {
        this.strategy = strategy;
        this.shouldRespondWith404 = shouldRespondWith404;
    }

    public String getTitle() {
        return strategy.getTitle(request);
    }

    @Override
    public void traverse(TraversalListener<Object> observer) {
        strategy.traverse(observer, page, root, request, response);
    }

    @Override
    protected boolean shouldRespondWith404() {
        return shouldRespondWith404;
    }

    public Class<? extends ResultResponderStrategy> strategyClass() {
        return strategy.getClass();
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

    public SecureOperation getSecureOperation() {
        return new SecureReadOperation();
    }
}
