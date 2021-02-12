#!/bin/sh

cd saxon-lib

# 1) Must have Docker running
# 2) Ignore the java.nio.file.NoSuchFileException, the build succeeded, Quarkus just doesn't understand that an .so was created instead of a runner
./mvnw clean package -Dquarkus.native.container-build=true

cd ..

# Remove Existing Outputs (if they exist)

rm transform-app/graal_isolate*
rm transform-app/libsaxon-native*
rm transform-app/transform

# Copy Native Image Outputs

rm ./saxon-lib/target/libsaxon-native-image-source-jar/libsaxon-native.jar
cp ./saxon-lib/target/libsaxon-native-image-source-jar/graal_isolate* transform-app
cp ./saxon-lib/target/libsaxon-native-image-source-jar/libsaxon-native* transform-app

cd transform-app

# Compile and Link
protoc -I=proto --java_out=saxon-lib/src/main/java proto/addressbook.proto
protoc -I=proto --cpp_out=transform-app proto/addressbook.proto
g++ transform.cc addressbook.pb.cc -L . -I . -lsaxon-native -lprotobuf -o transform

# Run Example

echo "\n\nRunning the `transform` application"
export LD_LIBRARY_PATH="$(pwd)"
./transform

cd ..
