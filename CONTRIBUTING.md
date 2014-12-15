Please note that these guidelines are very likely to evolve depending your questions, remarks and contributions.

# How to start

To start contributing, just fork and clone the ValidationFramework repository. You may refer to this article for further
information: [For A Repo](https://help.github.com/articles/fork-a-repo/).

Before you begin making changes, it may be a good idea to make sure that all the already existing unit tests actually
pass on your machine.

When you are done with your changes, you can push them to your fork and submit a pull request. Please refer to this
article for further information: [Using pull requests](https://help.github.com/articles/using-pull-requests/).


# Guidelines

* **Consistency, consistency, consistency:** The most important point to consider is to maintain the consistency in
terms of **naming** and **behavior**. Consistent naming particularly applies to protected and public classes, methods, constants, etc. This will help the users find their way in the framework and help them know what to expect.

* **Few dependencies:** Keeping the number of dependencies to third-party libraries as low as possible will ease the
integration of the framework in real world projects and reduces the risk of licensing problems.

* **Code style and formatting:** There are no strict rules for now. If you are using IntelliJ IDEA, you may use the
defaults settings, with the exception on the imports (see right below).

* **Public vs Protected vs Private:** Please use public and protected only when necessary. When in doubt, just use
**private**. It is always easy to make something protected or private later when needed, but much harder to make
something private again in case the implementation of a class changes.

* **Imports:** Please use **individual import** statements instead of wildcard imports.

* **Documentation:** All **public** and **protected** classes, methods, constants, etc., should be commented with
JavaDoc.

* **Tests:** Wherever possible, please add **JUnit** tests related to your changes. For GUI tests in the
validationframework-swing module, **Fest-Swing** will be used in the near future.

* **Sonar checks:** For now, static code analysis will be performed using **SonarQube** before releasing the
ValidationFramework. The set of rules will be made available in the near future, so that contributors can perform the
checks on their own code.

* **Copyright header:** All Java files and text-based resource files should have the copyright header.
