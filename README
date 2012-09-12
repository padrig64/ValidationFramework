The Validation Framework is a set of base classes, interfaces and utilities to
help in implementing full-featured input validation: from the validation triggers
to the resulting feedback and assistance to the user.

This library is still in development and will continue growing. The following
features are expected ('.' are to be implemented, '*' are already provided):

INPUT VALIDATION
 * Instant validation (for example, on each key stroke);
 * Delayed validation (for example, when focus is lost or when the ENTER key is
   pressed, or when Apply button is pressed);
 * Validation on individual components;
 * Validation on groups of components without inter-dependencies;
 * Validation on groups of components with inter-dependencies;
 * Validation of groups within groups;
 . Validation of heterogeneous fields and sub-groups
 * Syntax validation and semantic validation;
 * Support for tables: per-cell validation;
 * Support for tables: per-column validation.

FEEDBACK
 * Instant feedback;
 . Delayed feedback;
 . Value proposal in case of error (either automatic proposal for related
   components within a group), that is to say automatic correction of related
   components;
 * Conditional logic for components (for example, enabling/disabling of an Apply
   button in case of valid/invalid input);
 * Simple set of decorations for user assistance (icons, convenient tooltips,
   hints, etc.), staying close to the current look-and-feel.
 . Extended set of decorations for user assistance.

EASY AND EXTENDABLE FRAMEWORK
 * Very simple to use;
 * Most generic concepts as possible (validation trigger -> data reader ->
   validation algorithm -> validation rules -> validation results -> feedback to
   user -> component decorations);
 * Easily pluggable to custom/special components;
 * Bundled support for Swing components;
 . Bundled support for JavaFX 2;
 . Bundled support for other libraries;
 * Allow validation not only for GUI components through pluggable architecture
   (however, GUI validation is the focus of the framework);
 * Flexibility of use (one validator for the whole application, or one validator
   per input component, or whatever);
 * Ease of use (for the programmer), possibly through a simple DSL;
 . Extended DSL for even more convenient use;
 * Allow to plug custom rules (of course);
 * Allow to plug custom feedbacks;
 * Allow to plug custom decorations;
 * Allow to plug custom validation/feedback algorithms;
 . Set the validation to a certain state (for example, forcing invalidation
   and/or its feedback);
 . Anything useful you can imagine.

MISC
 . Support for runtime look-and-feel switches for bundled feedback
   implementations;
 . Easily pluggable to some other validation frameworks like Commons Validator
   framework from Jakarta, etc.
