package fitnesse.responders.search;

import fitnesse.components.TraversalListener;
import fitnesse.wiki.WikiPage;

public interface ResultResponderStrategy {
    String getTitle();

    void traverse(TraversalListener<Object> observer, WikiPage page, WikiPage root);
}
