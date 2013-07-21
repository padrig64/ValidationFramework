# Version 2.0.0 (to be released)

* Changed groupid from validationframework to com.google.code.validation for future availability in Maven Central
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
