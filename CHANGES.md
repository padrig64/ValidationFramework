# Version 2.1.3 (to be released)

* Added EqualsBooleanRule in core
* Added convenient constructor to IconComponentDecoration
* Fixed NullPointerException when calling dispose() multiple times on some triggers

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
