./gradlew clean build
curl -F "file=@./library/build/libs/library-0.6-source.jar" http://repo.keep.moe/api/travis/push/qkv.php
curl -F "file=@./library/build/libs/library-0.6-javadoc.jar" http://repo.keep.moe/api/travis/push/qkv.php