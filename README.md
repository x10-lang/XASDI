## IBM X10-based Agent Simulation on Distributed Infrastructure (XASDI)


### About XASDI
XASDI is a platform for a massive agent-based simulation and is written in [the X10 programming language](http://x10-lang.org/).
In XASDI, an agent manager manages agents that are executed asynchronously in each place.
An agent in a place can communicate with an agent in another place by sending a communication message.


### Directories and files
- api (XASDI API javadoc)
- doc (XASDI Application Development Guide)
- jar (XASDI runtime jar file)
- src (XASDI runtime source code)
- MySample (a sample XASDI application source code (Eclipse project))
- README.md (this file)


### Building XASDI runtime from Source
1. Set up build environment
  - Java Development Kit (JDK) 6 or later (JDK 8 is recommended)
  - Ant 1.9.6 or later
  - X10 2.5.4 or later
      - Download the latest prebuilt binary for your platform from http://x10-lang.org/software/download-x10/latest-release.html
      - After downloading, untar and see the INSTALL file for further instructions.
2. cd src
3. execute ./build.sh


### Building and running a sample XASDI application
1. Set up build environment (if not yet done)
2. cd MySample
3. ant
4. execute ./run-Sample.sh


### Information for developing your own application
Refer to the XASDI Application Development Guide and API javadoc.


### License
XASDI is licensed to You under the Eclipse Public License (EPL).
You may not use XASDI except in compliance with the License.
You may obtain a copy of the License at
http://www.opensource.org/licenses/eclipse-1.0.php


### Sponsor
This project is supported by [CREST](http://www.jst.go.jp/kisoken/crest/en/index.html), Japan Science and Technology Agency.


### Feedback
Please feel free to submit your feedback or requests as [GitHub issues](https://github.com/x10-lang/xasdi/issues).
