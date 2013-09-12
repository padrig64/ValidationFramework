# TO FIX

* Convenient constructor in the component decorations (abstract + cell + comp)
* Allow decoration on disabled components
* Implement de same on tab decoration
* Painting of translucent decorations
* Remaining icontip's tooltips when quickly moving mouse (in/outside tables): only on Linux? still happening?
* Icon decoration steals mouse event even outside clipping bounds

# TO MAKE

* ResultCollector disposes other validator
* JFormattedTextFieldOptionalFormatterRule
* JFormattedTextFieldOptionalValueProvider
* JTableCellEditorCancelledTrigger
* JTableComboBoxEditorValueProvider
* JTableModelChangeTrigger
* JTableModelDataProvider
* JTableTextEditorFormatterRule
* NotNullAndNotEmptyStringBooleanRule
* NumericRangeCheckResultHandler
* NumericRangesValidRule

* Make consistent use of generics for Swing data providers
* Specify result for null rule input
* Make validation of min + max + min/max group + global easier
* Implement Disposable where appropriate
* User-modification trigger for (formatted) textfields
* IconResultHandler: dynamic message (maybe override the getInvalidText() method)
* Consistent comments (use of dispose(), cast transformers, default behaviors, what classes can be used for, etc.)
* Allow disabled decoration on tab?
* Allow disabled decoration on cell?
* Table model change trigger
* Table editing started/stopped/canceled triggers
* Table checkbox editor trigger
* Table editing: with/without valid commit to model, column-wise validator, per-cell/per-row group validation
* More consistency for handling "no result" results
* Initial state/trigger when building the validators
* Fix ToolTipDialog transparency support (Java 6, Java 7 and JNA)
* Label boolean feedback
* Mappable validator builder
* Manual trigger or trigger() method on the validator? How about the mappable validator?
* Common settings (default icons, etc.)
* Rule-based formatter
* Reformat/Revert JFormattedTextField when focus lost? Save values allowed by the rules (wider than the default number formatter)
* Icon decoration above combobox popups
* Tab decoration depending on tab selection
* Avoid instantiation of result collectors (collect directly from validators, etc.)
* Better icon tip anchoring for IconTipDecorator
* Alternative location of icon tip w.r.t. screen edge
* Common implementation for the icon tip
* Auto-scroll/jump to errors in case of scrollpanes, tabbedpane, etc.
* Initial focus for JFormattedTextField helper
* Make it possible to have the component decoration still visible even if the decorated component is disabled (config)

# TO THINK ABOUT

* JavaFX 2 support
* Make anchor link in the AbstractIconFeedback dependent of the look-and-feel
* Initial state for result handler before any validation has been performed yet
* Differentiate between model changes due to user action and system action
* General-purpose conditional logic
* Thread-safety (triggers from multiple-threads)
* Make triggers not trigger until the whole validator is ready
* Remove data provider marker interface (check validation of heterogeneous groups first)
* Property data provider
* Check licenses again
* Validation flow logging
* Use of IDs
* Use of predicates or compose heterogeneous rules or component state conditional rule or RuleTransformerWrapper/DataProviderTransformerWrapper
* Aggregate boolean result collectors
* Aggregate data from multiple providers, aggregate rule results
* Validator marker interface?
* Heterogeneous Group
* Put decoration per plane and not per parent
* Exception-based results
* Conditional logic framework
* All features described in the README file
* Dialog strategies
