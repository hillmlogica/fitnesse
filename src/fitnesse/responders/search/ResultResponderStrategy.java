package fitnesse.responders.search;

import fitnesse.components.TraversalListener;

public interface ResultResponderStrategy {
    String getTitle();

    void traverse(TraversalListener<Object> observer);
}
