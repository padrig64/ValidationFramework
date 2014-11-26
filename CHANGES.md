# Version 3.3.0 (to be released)

* Added contains(Object) method to the ReadableListProperty interface
* Added interfaces and abstract implementations of a set property
* Fixed type erasure problem of the remove() method in the SimpleListProperty
* Added JButtonMnemonicProperty and JLabelDisplayedMnemonicProperty
* Added ActionProperty
* Added convenient getter and clear methods in the CompositeReadableProperty and CompositeWritable properties

# Version 3.2.7

* Added interfaces for readable/writable list properties and map properties
* Added SimpleListProperty and SimpleMapProperty
* Added EqualsTransformer and NotEqualsTransformer
* Added NumberEqualToTransformer
* Added NumberGreaterThanOrEqualToTransformer
* Added NumberGreaterThanTransformer
* Added NumberLessThanOrEqualToTransformer
* Added NumberLessThanTransformer
* Added JListSelectedItemCountProperty
* Added JTableRowCountProperty
* Added JTableSelectedRowCountProperty
* Added JTableSelectedRowIndexProperty
* Added JTableRowIndexToModelTransformer
* Added JTableRowIndexToViewTransformer
* Added JTableColumnIndexToModelTransformer
* Added JTableColumnIndexToViewTransformer
* Moved CollectionElementTransformer to 'collection' sub-package for consistency
* Added GetCollectionSizeTransformer
* Added GetMapSizeTransformer
* Added JDialogTitleProperty
* Added JFrameTitleProperty
* Fixed issue where the value of the JTextComponentTextProperty was not immediately updated upon replacement of the document

# Version 3.2.6

* Fixed bug #7: tooltip on IconComponentDecoration now shows again

# Version 3.2.4

* Fixed JToggleButtonSelectedProperty value
* Fixed initial value for ButtonPressedProperty and JTextComponentTextProperty
* Made the JTextComponentTextProperty fire less events when setting its value
* Added PropertyValueChangeTrigger to trigger validation when the value of a ReadableProperty changes
* Added PropertyValueProvider to retrieve the value from a ReadableProperty
* Added PropertyResultHandler to set the validation result into a WritableProperty
* Added ResultHandlerProperty to set the property value in to a ResultHandler
* Added InvokeLaterPropertyWrapper to notify the value change listeners later on the EDT
* Added possibility to use readable/writable properties in the GeneralValidator builder (deprecates use of ResultCollector)
* Made IconComponentDecoration create a ToolTipDialog window only if there is a text to show
* Updated to SLF4J 1.7.7

# Version 3.2.0

* Prevented recursion when setting the value of readable writable properties
* Added tooltip text property for JComponent
* Added foreground color and background color properties for Component
* Added value property for JFormattedTextField
* Added convenient comparison method in ValueUtils
* Added ConstantTransformer to always return a specific value

# Version 3.1.1

* Made the FormatTransformer return the toString() of the input when no format is specified
* Added convenience method in the FormatTransformer to get the format property
* Added method to the FormatWrapper to get the delegate format
* Improved implementation of the SimpleProperty
* Fixed useless firing of value change events when binding properties
* Added PrintStreamValueChangeAdapter to help debugging
* Added method in AbstractReadableProperty to get the value change listeners
* Added editable property for JTextComponent
* Added text and icon related properties for JLabel and JButton
* Added component size and location related properties
* Added component minimum/preferred/maximum size related properties
* Added window/dialog/frame related properties
* Added pressed property for buttons, toggle buttons and menu items

# Version 3.1.0

* Added JComboBoxSelectedIndexProperty and JComboBoxSelectedValueProperty in the Swing package
* Moved the SimpleFormatProperty to core
* Made the FormatTransformer and ParseTransformer use a format property to allow binding on the used Format object
* Added simple NumberTo*Transformers in core
* Made it possible to inhibit firing of value change events of readable properties

# Version 3.0.3

* Moved the Transformer and Aggregator interfaces from the base package to the API package (reason for the major release)
* Added simple property binding in core and for Swing to allow binding and transformation of properties (see wiki)
* Added convenient FormatWrapper in core to allow stricter parsing, parsing of null/empty text, and formatting null values
* Added ChainedTransformer in core

# Version 2.1.6

* Fixed possible memory leak by removing decoration from layered pane when decorated component no longer in the component hierarchy tree
* Fixed bug where IconComponentDecoration tooltip was constantly remaining on screen
* Added ParseTransformer in core
* Added FormatTransformer in core
* Added CompositeTrigger in core
* Added convenience getters to the AbstractSimpleValidator
* Minor fixes to avoid a NullPointerException in the JTableTextEditorModelChangedTrigger and JTableComboBoxEditorModelChangedTrigger

# Version 2.1.5

* Added initial support for JavaFX 2
* Added EqualsBooleanRule in core
* Added convenient BooleanResultCollector in core
* Added IsTrueRule and IsFalseRule in core
* Added InvokeLaterResultHandler wrapper
* Fixed logic issue in InvokeLaterTrigger wrapper
* Added convenient constructor to IconComponentDecoration
* Added convenient constructor to ComponentKeyStrokeTrigger
* Fixed NullPointerException when calling dispose() multiple times on some triggers
* Added experimental property binding mainly meant for Swing (not JavaFX)

# Version 2.1.2

* Added NegateBooleanTransformer in core
* Removed ambiguous constructor in AndBooleanAggregator and OrBooleanAggregator
* Added the _nouses instruction for the maven bundle plugin
* Fixed NullPointerException in AbstractComponentDecoration sometimes occurring depending on the window hierarchy

# Version 2.1.1

* Added SimpleResultCollector for convenience over the ResultCollector
* Made the GeneralValidatorBuilder collect from other simple validators
* Added StringNotEmptyRule as a simpler alternative to StringLengthGreaterThanRule and StringLengthGreaterThanOrEqualToRule
* Added convenience trigger() method in the GeneralValidatorBuilder DSL after adding the first result handler to the validator under construction
* Fixed visibility of AbstractComponentDecoration when set manually and the component goes to showing state
* Fixed tracking of decorated table cells while dragging a column

# Version 2.1.0

* Added ToStringTransformer in core
* Added PropertyChangeTrigger in core that can be used as PropertyChangeListener to trigger the validation
* Added ActionTrigger in Swing support that can be used as an Action or ActionListener to trigger the validation
* Added CollectionElementTransformer in core
* Added ComponentKeyStrokeTrigger and convenience derivates
* Fixed compilation warning when adding rule input transformer using the GeneralValidatorBuilder
* Reversed flag in the InvokeLateTrigger wrapper
* Undeprecated old validators to allow smoother transition from version 1.x.x to 2.x.x
* Simplified mapping strategy names between data providers and rules, and between rules and result handlers in the GeneralValidator
* Added comments

# Version 2.0.0

* Changed groupid from 'validationframework' to 'com.google.code.validationframework' for future availability in Maven Central
* Added all-purpose GeneralValidator and GeneralValidatorBuilder that can replace all the previously existing validators
* Improved javadoc
* Migrated to JNA 3.5.2, MigLayout 4.2 and SLF4J 1.7.5
* Added transformers data providers based on the Swing components that use Object in their interface
* Updated names of generic types for better consistency and understanding
* Added convenience constructor to IconBooleanFeedback and ListCompositeDataProvider
* Added convenience in AndBooleanAggregator and OrBooleanAggregator to better support null values
* Added convenience getComponent() method to Swing triggers, data providers and rules
* Added InvokeLaterTrigger wrapper to re-schedule a trigger later on the Event Dispatch Thread
* Added IllegalCharacterBooleanRule as a simple alternative to the StringRegexRule
* Added TransformedDataProvider to adapt the type handled by data providers when added to validators handling another type
* Added TransformedResultHandler to adapt the type handled by result handler when added to validators handling another type
* Fixed size of tab title renderer in tabbed panes to avoid the contents to move up and down when the icon is shown and hidden
* Renamed ManualTrigger's triggers method for simplicity
* Renamed ButtonGroup-related data providers for consistency
* Made triggers, data providers, rules, result handlers and transformers from core Disposable where applicable
* Other minor bugs and code quality fixes

# Version 1.1.1

* Made validationframework-core, validationframework-swing and validationframework-experimental OSGI-compliant
* Extracted demo files validationframework-experimental to reduce dependencies
* Introduced the ResultAggregationValidator that will be soon used in builders

# Version 1.1.0

* Migrated to TimingFramework 5.5.0, SLF4J 1.7.2, JUnit 4.11, Mockito 1.9.5
* Updated class diagrams
* Made base classes consistent in terms of naming and behavior

# Version 1.0.3

* Fixed position of icon tip decoration on edited table cell after column re-sorting
* Migrated to JNA 3.5.1

# Version 1.0.2

* Tooltip of icon decoration now in a JWindow instead of a JDialog to avoid positioning issues in some window managers

# Version 1.0.1

* Improved javadoc
* Fixed exception when validation elements are disposed multiple times
* Added convenience constructors in some result handlers
* Added ManualTrigger
* Fixed minor issues (mainly clipping issues) in the icon decorator

# Version 1.0.0

* Fixed Sonar issues
* Improved javadoc
* Improved data provider interface
* Made number-related rules more consistent and logical
* Fixed NullPointerException in StringRegexRule when data is null
* Changed log level in the AbstractCellIconFeedback

# Version 0.0.2

* Minor fixes for future deployment in central repository

# Version 0.0.1

* Initial release
