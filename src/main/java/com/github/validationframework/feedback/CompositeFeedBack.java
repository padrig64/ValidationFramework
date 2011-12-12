package com.github.validationframework.feedback;

import java.util.ArrayList;
import java.util.List;

public class CompositeFeedBack<R> implements FeedBack<R> {

	private List<FeedBack<R>> feedBacks = new ArrayList<FeedBack<R>>();

	public void addFeedBack(FeedBack<R> feedBack) {
		feedBacks.add(feedBack);
	}

	public void removeFeedBack(FeedBack<R> feedBack) {
		feedBacks.remove(feedBack);
	}

	public void feedback(R result) {
		for (FeedBack<R> feedBack : feedBacks) {
			feedBack.feedback(result);
		}
	}
}
