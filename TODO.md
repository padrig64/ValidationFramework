# TO FIX

* Convenient constructor in the component decorations (abstract + cell + comp)
* Implement the same on tab and cell decoration (including disabled decoration on disabled tab)
* Painting of translucent decorations
* Remaining icontip's tooltips when quickly moving mouse (in/outside tables): only on Linux? still happening?
* Icon decoration steals mouse event even outside clipping bounds
* Consistent behavior of dispose() methods

# TO MAKE

* More JavaFX 2 support
*
* ResultCollector disposes other validator?
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

# TO THINK ABOUT

* General-purpose conditional logic
* Binding
* Predicates
* Component state conditional rules
* Different rules for different data providers in same validator (with result aggregation)
* Thread-safety (triggers from multiple-threads)
* Data providers with different getters
* Validation flow logging
* Use of IDs
* Re-use instances of T, DP, R, RH, V
* Heterogeneous Group
* All features described in the README file
* Dialog strategies
