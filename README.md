# ReaKtive
An FRP library for Kotlin, well integrating with JavaFX

## Getting Started

### Prerequisites
- Java 1.8 (not higher not lower)
- Kotlin 1.3.11
- [KRef](https://github.com/NKB03/KRef) (to install just follow the build instructions in the [README](https://github.com/NKb03/KRef/blob/master/README.md))

### Installing
To install ReaKtive you need to follow these steps:
- Clone the repository `git clone https://github.com/NKB03/ReaKtive <target_dir>`
- Build with gradle: `gradle build`
- Publish to maven local to make the library available for other projects: `gradle publishToMavenLocal`
- Open in Intellij or any other IDE
If any errors occur while installing please feel free to create an issue or write me an e-mail.

### Running tests
To run the tests you IntelliJ and the Kotlin Spek Plugin.  
In Intellij:
- Edit Run configurations
- Add new configuration
- Select "Spek - JVM"
- For type select "Package"
- For package select "reaktive"
- For module select "reaktive-test"  
Testing via gradle is not supported.  

## Contributing
Contributing is very much appreciated. Please feel free to suggest any improvements by creating an issue.  
If you have questions about the API or the internals just contact me via e-mail.  

## Authors
- Nikolaus Knop (niko.knop003@gmail.com