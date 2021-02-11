# Saxon GraalVM Native Image called from C++

The purpose of this repository is to show a proof of concept on how to use the Saxon XSLT engine from C++, without using the Saxon C distribution. This is possible through GraalVM Native Image which allows the creation of a shared library (.so) containing a nativly compiled version of your Java function.

## Getting Started

Examine and run the`full_build.sh` that will build the Saxon native library, build the C++ executable application, and run it.


### Typical Result (XML to JSON)
```
./transform
Result> [{"Item_Number":"20001","Item_Description":"Item Description 1"},{"Item_Number":"20002","Item_Description":"Item Description 2"},{"Item_Number":"20003","Item_Description":"Item Description 3"}]
2892
```


## Credits

1. [Code in Java, Execute as C++](https://towardsdatascience.com/code-in-java-execute-as-c-921f5db45f20)
2. [klakegg/docker-saxon](https://github.com/klakegg/docker-saxon)
3. [GraalVM Build a Shared Library](https://www.graalvm.org/reference-manual/native-image/#build-a-shared-library)
4. [How to call a Java entrypoint method with non-primitive types as parameters from C++ using GraalVM](https://stackoverflow.com/questions/64060787/how-to-call-a-java-entrypoint-method-with-non-primitive-types-as-parameters-from)
