# Saxon GraalVM Native Image called from C++

The purpose of this repository is to show a proof of concept on how to use the Saxon XSLT engine from C++, without using the Saxon C distribution. This is possible through GraalVM Native Image which allows the creation of a shared library (.so) containing a nativly compiled version of your Java function.


## Credits

1. [Code in Java, Execute as C++](https://towardsdatascience.com/code-in-java-execute-as-c-921f5db45f20)
2. [klakegg/docker-saxon](https://github.com/klakegg/docker-saxon)
3. [GraalVM Build a Shared Library](https://www.graalvm.org/reference-manual/native-image/#build-a-shared-library)
