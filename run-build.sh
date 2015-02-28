./gradlew clean build
curl -F "file=@./library/build/libs/library-0.6-sources.jar" http://repo.keep.moe/api/travis/push/qkv.php
curl -F "jar=@./library/build/libs/library-0.6-javadoc.jar" http://repo.keep.moe/api/travis/push/qkv.php