./gradlew clean build
curl -F "file=@./library/build/libs/library-*-sources.jar" http://repo.keep.moe/api/travis/push/qkv.php
curl -F "jar=@./library/build/libs/library-*-javadoc.jar" http://repo.keep.moe/api/travis/push/qkv.php