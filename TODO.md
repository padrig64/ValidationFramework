# Known issues

* Painting of decoration when decorated component is still (0,0,0,0) and invalid
* Painting of translucent decorations on OS X
* Icon decoration steals mouse event even outside clipping bounds
* Inconsistent behavior of dispose() methods: test setting value before/after dispose() (from property, from component), test listener before/after dispose(), test multiple dispose() (Release 4.0.0)

# Ideas for the short term

* Better use of "? super" and "? extends" wildcards (Release 4.0.0)
* Convenient value change listeners to check the correct behavior of the properties (incl. set, list and map properties): always on the same thread, always on EDT, never on EDT, only called when values are different, etc.
* Set-, List- and Map-related transformers
* More JavaFX 2 support

* Separate the property binding mechanism from the validation framework (Release 5.0.0?)

* Clean up demo module

* JFormattedTextFieldOptionalFormatterRule
* JFormattedTextFieldOptionalValueProvider
* JTableCellEditorCanceledTrigger
* JTableComboBoxEditorValueProvider
* JTableModelChangeTrigger
* JTableModelDataProvider
* JTableTextEditorFormatterRule
* NumericRangeCheckResultHandler
* NumericRangesValidRule

* Make consistent use of generics for Swing data providers
* Allow disabled icon decoration on disabled tab
* Consistent constructor for decorations (abstract + cell + comp)
* Specify result for null rule input
* Make validation of min + max + min/max group + global easier
* Implement Disposable where appropriate
* User-modification trigger for (formatted) textfields
* IconResultHandler: dynamic message (maybe override the getInvalidText() method)
* Consistent comments (use of dispose(), cast transformers, default behaviors, what classes can be used for, etc.)
* Table model change trigger
* Table editing started/stopped/canceled triggers
* Table checkbox editor trigger
* Table editing: with/without valid commit to model, column-wise validator, per-cell/per-row group validation
* More consistency for handling "no result" results
* Initial state/trigger when building the validators/result handlers/triggers (no trigger until ready?)
* Fix ToolTipDialog transparency support (Java 6, Java 7 and JNA)
* Label boolean feedback
* Mappable validator builder
* Common settings (default icons, etc.)
* Rule-based formatter
* Icon decoration above combobox popups
* Tab decoration depending on tab selection
* Better icon tip anchoring for IconTipDecorator
* Alternative location of icon tip w.r.t. screen edge
* Common implementation for the icon tip
* Auto-scroll/jump to errors in case of scrollpanes, tabbedpane, etc.
* Add more examples, javadoc and UML diagrams to wiki (incl. example of Formatter/Format configuration)

# Ideas for the long term

* Better support for Java 8 regarding the use of lambdas and method references (e.g. for set/list/map properties)
* Common builder API to build validators and property bindings for general-purpose conditional logic
* Predicates/conditions
* Validation rules depending on component state (visible, enabled, etc.)
* Different rules for different data providers in same validator (with result aggregation)
* Thread-safety (triggers from multiple-threads)
* Data providers with different getters
* Validation flow logging
* Use of IDs
* Re-use instances of W, DP, R, RH, V
* Heterogeneous Group
* All features described in the wiki
* Dialog strategies
