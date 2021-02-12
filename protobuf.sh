protoc -I=proto --java_out=saxon-lib/src/main/java proto/addressbook.proto

protoc -I=proto --cpp_out=transform-app proto/addressbook.proto
