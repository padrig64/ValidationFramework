/*
 * Copyright THALES NEDERLAND B.V. and/or its suppliers
 *
 * THIS SOFTWARE SOURCE CODE AND ANY EXECUTABLE DERIVED THEREOF ARE PROPRIETARY
 * TO THALES NEDERLAND B.V. AND/OR ITS SUPPLIERS, AS APPLICABLE, AND SHALL NOT
 * BE USED IN ANY WAY OTHER THAN BEFOREHAND AGREED ON BY THALES NEDERLAND B.V.,
 * NOR BE REPRODUCED OR DISCLOSED TO THIRD PARTIES WITHOUT PRIOR WRITTEN
 * AUTHORIZATION BY THALES NEDERLAND B.V. AND/OR ITS SUPPLIERS, AS APPLICABLE.
 */

package com.github.validationframework.swing.resulthandler;

import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.swing.decoration.utils.IconUtils;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

/**
 * Result handler showing an icon in the tab of a specific index inside a tabbed pane.
 */
public class BooleanTabIconFeedback implements ResultHandler<Boolean> {

	public static final Icon DEFAULT_VALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/valid.png", BooleanTabIconFeedback.class);

	public static final Icon DEFAULT_INVALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/invalid.png", BooleanTabIconFeedback.class);

	private final Icon validIcon;

	private final Icon invalidIcon;

	private final JTabbedPane tabbedPane;

	private final int tabIndex;

	public BooleanTabIconFeedback(final JTabbedPane tabbedPane, final int tabIndex) {
		this(tabbedPane, tabIndex, DEFAULT_VALID_ICON, DEFAULT_INVALID_ICON);
	}

	public BooleanTabIconFeedback(final JTabbedPane tabbedPane, final int tabIndex, final Icon validIcon,
								  final Icon invalidIcon) {
		this.tabbedPane = tabbedPane;
		this.tabIndex = tabIndex;
		this.validIcon = validIcon;
		this.invalidIcon = invalidIcon;
	}

	/**
	 * @see ResultHandler#handleResult(Object)
	 */
	@Override
	public void handleResult(final Boolean result) {
		if ((result == null) || !result) {
			tabbedPane.setIconAt(tabIndex, invalidIcon);
		} else {
			tabbedPane.setIconAt(tabIndex, validIcon);
		}
	}
}
