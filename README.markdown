## _spray_ Template Project (modified for the RPI/BBB)

This version of the spray-template project is modified to run smoothly on the
Raspberry Pi/BeagleBone Black.

There is nothing beagle-bone-specific. There are - however - numerous dependencies upgrade
compared to the upstream `on_spray-can_1.2` branch from matsluni

* _spray-can_, Scala 2.10 + Akka 2.2 + spray 1.2 (the `on_spray-can_1.2` branch)

The essential modifications are the sizing of the akka dispatcher thread-pool
(see application.conf) and the use of the sbt assembly plugin (see build.sbt)
to make it easy to package the spray-app outside of the RPI.

Detailed information about the modifications and the use of spray.io and
spray-can on the RPI can be found on the spray blog:
<http://spray.io/blog/2013-07-23-spray-on-the-raspberry-pi/>

Follow these steps to get started:

1. Git-clone this repository.

        $ git clone git://github.com/....git my-project

2. Change directory into your clone:

        $ cd my-project

3. Launch SBT:

        $ sbt

4. Compile everything and run all tests:

        > test

5. Start the application:

        > re-start

6. Browse to http://localhost:8080/

7. Start the application:

        > re-stop

8. Instructions to run it on the RPI/BBB:
From within sbt just type `assembly` to package the app. The packaged jar file
can than be transferred to the RPI/BBB.
