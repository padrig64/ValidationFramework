package com.github.validationframework.feedback;

import java.util.ArrayList;
import java.util.List;

public class CompositeFeedBack<R> implements FeedBack<R> {

	private final List<FeedBack<R>> feedBacks = new ArrayList<FeedBack<R>>();

	public void addFeedBack(final FeedBack<R> feedBack) {
		feedBacks.add(feedBack);
	}

	public void removeFeedBack(final FeedBack<R> feedBack) {
		feedBacks.remove(feedBack);
	}

	public void feedback(final R result) {
		for (final FeedBack<R> feedBack : feedBacks) {
			feedBack.feedback(result);
		}
	}
}
