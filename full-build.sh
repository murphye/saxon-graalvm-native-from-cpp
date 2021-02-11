#!/bin/sh

cd saxon-lib

# 1) Must have Docker running
# 2) Ignore the java.nio.file.NoSuchFileException, the build succeeded, Quarkus just doesn't understand that an .so was created instead of a runner
./mvnw clean package -Dquarkus.native.container-build=true

cd ..

sleep 2

# Copy Native Image Outputs

cp ./saxon-lib/target/libsaxon-native-image-source-jar/graal_isolate* transform-app

sleep 1

cp ./saxon-lib/target/libsaxon-native-image-source-jar/{libsaxon-native.h,libsaxon-native.so,libsaxon-native_dynamic.h} transform-app

cd transform-app

# Compile and Link
g++ transform.cc -L . -I . -lsaxon-native -o transform

# Run Example
./transform

cd ..
