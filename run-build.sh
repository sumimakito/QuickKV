gradlew clean build
curl --form "jar=./library/build/libs/library-*-sources.jar" http://code.keep.moe/api/ci/push/QuickKV
curl --form "jar=./library/build/libs/library-*-javadoc.jar" http://code.keep.moe/api/ci/push/QuickKV